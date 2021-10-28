
![](../../../logo/JindoFS.png)

## 介绍

jindotable-accesslog 主要提供解析 OSS 访问日志的 Hive UDF，使用该UDF可以切割OSS访问日志. 供用户对访问日志进行SQL分析，如何开启访问日志请访问[链接](https://help.aliyun.com/document_detail/31900.html?spm=a2c4g.11186623.2.24.61301f34IeiFxW) ,字段含义请参考[链接](https://help.aliyun.com/document_detail/31868.html?spm=a2c4g.11186623.6.725.4a9d52ccrnE41s) 。


## 使用
1. 进入项目，使用mvn进行打包 `mvn package`。
2. jar包位于`jindotable-accesslog/target`  目录下,获取jar包后按照正常的使用UDF方式进行使用。


## 示例
1. 加载原始表 `create external table access_log_source (line string) location 'oss://${bucket-name}/${日志前缀}/';`

2. 使用udf清理日志
`create view if not exists access_log as
 select 
 t.split[0] as Remote_IP              ,
 t.split[1] as Reserved               ,
 t.split[2] as Reserved1              ,
 t.split[3] as Time                   ,
 t.split[4] as Request_URI            ,
 t.split[5] as HTTP_Status            ,
 t.split[6] as SentBytes              ,
 t.split[7] as RequestTime            ,
 t.split[8] as Referer                ,
 t.split[9] as User_Agent             ,
 t.split[10] as HostName               ,
 t.split[11] as Request_ID             ,
 t.split[12] as LoggingFlag            ,
 t.split[13] as Requester              ,
 t.split[14] as Operation              ,
 t.split[15] as Bucket                 ,
 t.split[16] as Key                    ,
 t.split[17] as ObjectSize             ,
 t.split[18] as Server_Cost_Time       ,
 t.split[19] as Error_Code             ,
 t.split[20] as Request_Length         ,
 t.split[21] as UserID                 ,
 t.split[22] as Delta_DataSize         ,
 t.split[23] as Sync_Request           ,
 t.split[24] as StorageClass           ,
 t.split[25] as TargetStorageClass    ,
 t.split[26] as oss_acc_src_oms_region 
 from (select oss_log_split(line) as split from source) t ;
`
