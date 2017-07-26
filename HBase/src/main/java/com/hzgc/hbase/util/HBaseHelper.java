package com.hzgc.hbase.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HBaseHelper {
    private static Logger LOG = Logger.getLogger(HBaseHelper.class);
    private static Configuration innerHBaseConf = null;
    private static Connection innerHBaseConnection = null;

    public HBaseHelper() {
        initHBaseConfiguration();
        LOG.info("init configuration is successful");
        initHBaseConnection();
        LOG.info("init connection is successful");
    }

    /**
     * 内部方法，用来初始化HBaseConfiguration
     */
    private static void initHBaseConfiguration() {
        try {
            innerHBaseConf = HBaseConfiguration.create();
            File hbaseFile = HBaseUtil.loadResourceFile("hbase-site.xml");
            innerHBaseConf.addResource(hbaseFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取HBaseConfiguration
     *
     * @return 返回HBaseConfiguration对象
     */
    public static Configuration getHBaseConfiguration() {
        if (null == innerHBaseConf) {
            initHBaseConfiguration();
        }
        return innerHBaseConf;
    }

    /**
     * 内部方法，用来初始化HBaseConnection
     */
    private static void initHBaseConnection() {
        try {
            innerHBaseConnection = ConnectionFactory.createConnection(HBaseHelper.getHBaseConfiguration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取HBase连接
     *
     * @return 返回HBaseConnection对象
     */
    public static Connection getHBaseConnection() {
        if (null == innerHBaseConnection || innerHBaseConnection.isClosed()) {
            LOG.info("The HBaseConnection is null or closed, recreate connection");
            initHBaseConnection();
        }
        return innerHBaseConnection;
    }

    public Table crateTableWithCoprocessor(String tableName,
                                           String observerName,
                                           String path,
                                           Map<String, String> mapOfOberserverArgs,
                                           int maxVersion, String... colfams) {
        HTableDescriptor tableDescriptor = null;
        Admin admin = null;
        Table table = null;
        // 创建表格
        try {
            admin = HBaseHelper.getHBaseConnection().getAdmin();
            if (admin.tableExists(TableName.valueOf(tableName))) {
                LOG.info("Table: " + tableName + " have already exit, quit with status 0.");
                return getTable(tableName);
            }
            tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            for (String columnFamily : colfams) {
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(columnFamily);
                columnDescriptor.setMaxVersions(maxVersion);
                tableDescriptor.addFamily(columnDescriptor);
            }
            admin.createTable(tableDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 添加Coprocessor。
        try {
            table = getTable(tableName);
            if (admin != null) {
                admin.disableTable(TableName.valueOf(tableName));
                if (tableDescriptor != null) {
                    tableDescriptor.addCoprocessor(observerName, new Path(path), Coprocessor.PRIORITY_USER, mapOfOberserverArgs);
                }
                admin.modifyTable(TableName.valueOf(tableName), tableDescriptor);
                admin.enableTable(TableName.valueOf(tableName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭admin 对象。
        try {
            if (admin != null) {
                admin.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }

    /**
     * 删除表格
     *
     * @param name 表名称
     */
    public void dropTable(String name) throws IOException {
        dropTable(TableName.valueOf(name));
    }

    /**
     * 内部方法：删除表格
     *
     * @param name 表名称
     */
    private void dropTable(TableName name) {
        Admin admin = null;
        try {
            admin = HBaseHelper.getHBaseConnection().getAdmin();
            boolean flag = admin.tableExists(name);
            if (flag) {
                if (admin.isTableEnabled(name)) {
                    admin.disableTable(name);
                }
                admin.deleteTable(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (admin != null) {
                    admin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取表对象
     *
     * @param name 表名称
     * @return 表对象
     */
    public static Table getTable(String name) {
        if (null != name && name.length() > 0) {
            try {
                return HBaseHelper.getHBaseConnection().getTable(TableName.valueOf(name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 关闭connection连接
     */
    public static void closeConn() {
        if (null != innerHBaseConnection && !innerHBaseConnection.isClosed()) {
            try {
                innerHBaseConnection.close();
                LOG.info("HBaseConnection close successfull");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
