package com.hzgc.hbase.staticreposuite;

import com.hzgc.dubbo.staticrepo.ObjectSearchResult;
import com.hzgc.hbase.staticrepo.*;
import com.hzgc.hbase.util.HBaseHelper;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.elasticsearch.client.Client;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class StaticRepoUtilSuite {

    @Test
    public void testAddObjectInfo(){
        String platformId = "1234";
        Map<String, Object> person = new HashMap<String, Object>();
        person.put("id","1111111111jkh11111111");
        person.put("name", "花满天");
        person.put("idcard", "1111111111jkh11111111");
        person.put("sex", "1");
       try {
            person.put("photo", Image2Byte2Image.image2byte("E:\\1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        person.put("reason", "赌博");
        person.put("pkey", "123456");
        person.put("creator", "羊驼");
        person.put("cphone", "18069222222");
        person.put("tag", "person");
        person.put("feature", "123455555555");

        int flag = new ObjectInfoHandlerImpl().addObjectInfo(platformId, person);
        System.out.println(flag);
    }
    @Test
    public void testUpdateObjectInfo(){
        Map<String, Object> person = new HashMap<String, Object>();
        person.put("id", "nihaome1112344dadafdfeawdafa33");
        person.put("platformid","nihao112344");
//        person.put("name", "小王炸炸");
        person.put("idcard", "dadafdfeawdafa3");
//        person.put("sex", "0");
//        try {
//            person.put("photo", Image2Byte2Image.image2byte("E:\\1.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        person.put("pkey", "123456");
//        person.put("reason", "赌博+暴力倾向");
//        person.put("creator", "羊驼神兽");
//        person.put("feature", "123455555555");

        int flag = new ObjectInfoHandlerImpl().updateObjectInfo(person);
        System.out.println(flag);
    }

    @Test
    public void testDeleteObjectInfo() throws IOException {
        Table tableName = HBaseHelper.getTable("objectinfo");
        Put put = new Put(Bytes.toBytes("111111111111111111123456"));
        put.addColumn(Bytes.toBytes("person"),Bytes.toBytes("name"),Bytes.toBytes("Liu siyang"));
        tableName.put(put);
        List<String> rowkeys = new ArrayList<String>();
        rowkeys.add("111111111111111111123456");
        int flag = new ObjectInfoHandlerImpl().deleteObjectInfo(rowkeys);
        System.out.println(flag);
    }
    @Test
    public void testHbaseConn(){
        Connection conn = HBaseHelper.getHBaseConnection();
        System.out.println(conn);
    }

    @Test
    public void testEsHbaseConn(){
        Client client = ElasticSearchHelper.getEsClient();
        System.out.println(client);
    }

    @Test
    public void testByGetByPlatFormIdAndIdCard(){
        ObjectInfoHandlerImpl impl = new ObjectInfoHandlerImpl();
        ObjectSearchResult searchResult = impl.searchByPlatFormIdAndIdCard("1234", "17719203341633873475",
                false, 1, 3);
        System.out.println(searchResult);
    }

    @Test
    public void testSearchByRowkey(){
        ObjectSearchResult objectSearchResult = new ObjectInfoHandlerImpl().searchByRowkey("a4495983547d4bec828a6b31e12bea80");
        System.out.println(objectSearchResult);
    }

    @Test
    public void testSearchByPhone(){
        ObjectSearchResult objectSearchResult = new ObjectInfoHandlerImpl().searchByCphone("185546925522", 1, 5);
        System.out.println(objectSearchResult);
    }

    @Test
    public void testSearchByCreator(){
        ObjectSearchResult objectSearchResult = new ObjectInfoHandlerImpl().searchByCreator("卫青",
                true, 1, 5);
        System.out.println(objectSearchResult);
    }

    @Test
    public void testSearchByName(){
        ObjectSearchResult objectSearchResult = new ObjectInfoHandlerImpl().searchByName("花妃花",
                false, 1, 5);
        System.out.println(objectSearchResult);
    }

    @Test
    public void testGetPhotoByKey() throws IOException {
        byte[] photo = new ObjectInfoHandlerImpl().getPhotoByKey("e590e89a002e44b3b302257a85684d1a");
        System.out.println(photo);
        //Image2Byte2Image.byte2image(photo, "C:\\Users\\lenovo\\Desktop\\nima.png");
    }

    @Test
    public void testimpl(){
        ObjectInfoInnerHandlerImpl objectInfoInnerHandler = new ObjectInfoInnerHandlerImpl();
        List<String> a = new ArrayList<>();
        a.add("123456");
        List<String> b = objectInfoInnerHandler.searchByPkeys(a);
        System.out.println(b);
    }
    @Test
    public void testGetPhotoByRowkey() throws IOException {
       ObjectInfoHandlerImpl objectInfoHandler = new ObjectInfoHandlerImpl();
        String rk = "6c18bbb907664f499199f1ffb4338520";
        byte[] photo = objectInfoHandler.getPhotoByKey(rk);
        System.out.println(photo);
        Image2Byte2Image.byte2image(photo, "C:\\nika1.png");
    }
    @Test
    public void testGetRocordOfObjectInfo(){
        SearchRecordHandlerImpl searchRecordHandler = new SearchRecordHandlerImpl();
        String rk = "fd62eda051c54cf984368972908bc7df";
        ObjectSearchResult o = searchRecordHandler.getRocordOfObjectInfo(rk,1,3);
        System.out.println(o);
    }

    @Test
    public void testSpilitToFindCol(){
        String demo = "keyvalues={a4495983547d4bec828a6b31e12bea80/person:cphone/1501481043631/Put/vlen=12/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:createtime/1501481043631/Put/vlen=19/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:creator/1501481043631/Put/vlen=9/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:feature/1501481043631/Put/vlen=12/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:idcard/1501481043631/Put/vlen=20/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:name/1501481043631/Put/vlen=9/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:photo/1501481043631/Put/vlen=0/seqid=0," +
                " a4495983547d4bec828a6b31e12bea80/person:pkey/1501481043631/Put/vlen=6/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:platformId/1501481043631/Put/vlen=4/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:reason/1501481043631/Put/vlen=6/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:sex/1501481043631/Put/vlen=1/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:tag/1501481043631/Put/vlen=6/seqid=0, " +
                "a4495983547d4bec828a6b31e12bea80/person:updatetime/1501481043631/Put/vlen=19/seqid=0}";
        String[] demos = demo.split(":");
        List<String> clos = new ArrayList<>();
        for (int i = 1;i < demos.length; i ++){
            System.out.println(demos[i].substring(0, demos[i].indexOf("/")));
            clos.add(demos[i].substring(0, demos[i].indexOf("/")));
        }
    }
    @Test
    public void testgetSearchPhoto(){
        SearchRecordHandlerImpl searchRecordHandler = new SearchRecordHandlerImpl();
        String rk = "fd62eda051c54cfmmnc7df";
        byte[] a = searchRecordHandler.getSearchPhoto(rk);
        System.out.println(a);
    }
}
