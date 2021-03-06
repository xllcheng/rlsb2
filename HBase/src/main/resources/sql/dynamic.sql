人脸动态库
--------------------------------------------------------------------------------------------------
|                                   person                                                       |
--------------------------------------------------------------------------------------------------
|            |                           CF：i                                                   |
--------------------------------------------------------------------------------------------------
|    图片ID  |    图片     |    设备ID   |    描述信息   |    附加信息  |   时间    |   特征值        |
--------------------------------------------------------------------------------------------------
|   rowkey   |      p     |     s       |      d         |       e       |    t     |     f      |
--------------------------------------------------------------------------------------------------
create 'person',
{NAME => 'i', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', COMPRESSION =>
'SNAPPY', VERSIONS => '1', MIN_VERSIONS => '0', KEEP_DELETED_CELLS => 'false', BLOCKSIZE => '524280',
 IN_MEMORY => 'true', BLOCKCACHE => 'true'}

-------------------------------------------------------------------------------------------------------------

 车辆动态库
-------------------------------------------------------------------------------------------------------------
|                                   car                                                                     |
-------------------------------------------------------------------------------------------------------------
|            |                           CF：i                                                              |
-------------------------------------------------------------------------------------------------------------
|   图片ID   |    图片     |    设备ID   |    描述信息    |    附加信息   |   车牌号   |   时间   |   特征值 |
-------------------------------------------------------------------------------------------------------------
|   rowkey   |      p     |       s      |       d       |        e     |     n     |     t    |     f      |
-------------------------------------------------------------------------------------------------------------

create 'car',
{NAME => 'i', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', COMPRESSION =>
'SNAPPY', VERSIONS => '1', MIN_VERSIONS => '0', KEEP_DELETED_CELLS => 'false', BLOCKSIZE => '524280',
 IN_MEMORY => 'true', BLOCKCACHE => 'true'}

--------------------------------------------------------------------------------------------------------------

 应用平台上传的图片：
-------------------------------------------------------------------------------
|                           upFea                                             |
-------------------------------------------------------------------------------
|             |          CF：p          |              CF：c                  |
-------------------------------------------------------------------------------
|   图片ID    |    图片     |    特征    |   图片    |    特征    |      车牌号  |
-------------------------------------------------------------------------------
|   rowkey    |     p      |     f      |      p    |    f      |      n      |
-------------------------------------------------------------------------------

create 'upFea',
{NAME => 'p',DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', COMPRESSION =>
'SNAPPY', VERSIONS => '1', MIN_VERSIONS => '0', KEEP_DELETED_CELLS => 'false', BLOCKSIZE => '65535',
 IN_MEMORY => 'true', BLOCKCACHE => 'true'},
 {NAME => 'c',DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', COMPRESSION =>
 'SNAPPY', VERSIONS => '1', MIN_VERSIONS => '0', KEEP_DELETED_CELLS => 'false', BLOCKSIZE => '65535',
  IN_MEMORY => 'true', BLOCKCACHE => 'true'}

------------------------------------------------------------------------------------------------------------

 查询结果：
-------------------------------------------------------------
|                             searchRes                     |
-------------------------------------------------------------
|            |                 CF：i                        |
-------------------------------------------------------------
|   查询ID   |  查询图片ID |   List<Map<返回图片Id ,相似度>>   |
-------------------------------------------------------------
|    rowkey  |      q     |             m                   |
-------------------------------------------------------------

create 'searchRes',
{NAME => 'i', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', COMPRESSION =>
'SNAPPY', VERSIONS => '1', MIN_VERSIONS => '0', KEEP_DELETED_CELLS => 'false', BLOCKSIZE => '65535',
 IN_MEMORY => 'true', BLOCKCACHE => 'true'}