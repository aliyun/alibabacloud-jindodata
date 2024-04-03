# Use Alibaba Cloud OSS-HDFS (JindoFS) as the underlying storage of HBase

HBase is a real-time database that provides high write performance in the Hadoop ecosystem. OSS-HDFS is a storage service released by Alibaba Cloud and is compatible with the Hadoop Distributed File System (HDFS) API. JindoSDK allows you to use OSS-HDFS as the underlying storage of HBase and store write-ahead logging (WAL) files of HBase in OSS-HDFS. This decouples storage and computing. OSS-HDFS is more flexible than the on-premises HDFS storage and reduces O&M costs. 

1. Download a JindoSDK package.

[Download](https://github.com/aliyun/alibabacloud-jindodata/blob/latest/docs/user/en/jindosdk/jindosdk_download.md) the latest version of the jindosdk-x.x.x.tar.gz package. 

2. Install JAR files.

Decompress the downloaded JindoSDK package and install the following JAR files that are contained in the package to the path specified by classpath of Hadoop:

*   jindo-core-x.x.x.jar
    
*   jindo-sdk-x.x.x.jar
    

Sample code for JindoSDK 4.6.12:

cp jindosdk-4.6.12/lib/jindo-core-4.6.12.jar <HADOOP\_HOME>/share/hadoop/hdfs/lib/

cp jindosdk-4.6.12/lib/jindo-sdk-4.6.12.jar <HADOOP\_HOME>/share/hadoop/hdfs/lib/

3. Configure the implementation class of OSS-HDFS and specify an AccessKey pair that you want to use to access OSS-HDFS.

Configure the implementation class of OSS-HDFS in the core-site.xml configuration file of Hadoop. 

<configuration\>

<property\>

<name\>fs.AbstractFileSystem.oss.impl</name\>

<value\>com.aliyun.jindodata.oss.OSS</value\>

</property\>

<property\>

<name\>fs.oss.impl</name\>

<value\>com.aliyun.jindodata.oss.JindoOssFileSystem</value\>

</property\>

</configuration\>

Configure the AccessKey ID and AccessKey secret that are used to access the bucket for which OSS-HDFS is enabled in the core-site.xml configuration file of Hadoop in advance. 

<configuration\>

<property\>

<name\>fs.oss.accessKeyId</name\>

<value\>xxx</value\>

</property\>

<property\>

<name\>fs.oss.accessKeySecret</name\>

<value\>xxx</value\>

</property\>

</configuration\>

JindoSDK also allows you to use other methods to configure an AccessKey pair. For more information, see Configure credential providers in JindoSDK. 

4. Configure an endpoint for OSS-HDFS.

To access OSS-HDFS, you must configure an endpoint in the cn-xxx.oss-dls.aliyuncs.com format. The endpoint of OSS-HDFS is different from the endpoint that is used to access OSS. The endpoint that is used to access OSS is in the oss-cn-xxx.aliyuncs.com format. JindoSDK accesses OSS-HDFS or OSS based on the endpoint that you configure. 

If you want to access OSS-HDFS, we recommend that you specify an access path in the oss://<Bucket>.<Endpoint>/<Object> format.

Example: oss://dls-chenshi-test.cn-shanghai.oss-dls.aliyuncs.com/Test. 

The preceding access path contains the endpoint of OSS-HDFS. JindoSDK accesses OSS-HDFS based on the endpoint in the access path. JindoSDK also allows you to use other methods to configure the endpoint that is used to access OSS-HDFS. For more information, see [Configure an endpoint to access OSS-HDFS](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_endpoint_configuration.md). 

5. Specify storage paths for HBase.

To specify paths in OSS-HDFS as the storage paths for HBase data and for WAL files, you must change the values of the hbase.rootdir and hbase.wal.dir parameters in the hbase-site configuration file to the OSS-HDFS paths. To release a cluster, you must disable the tables in the cluster and make sure that the updates in WAL files are written to HFile. 

|  Parameter  |  Description  |
| --- | --- |
|  hbase.rootdir  |  The root directory of HBase in OSS-HDFS. Set the value to oss://{Bucket.Endpoint}/hbase-root-dir.  |
|  hbase.wal.dir  |  The path in which the WAL files of HBase are stored in OSS-HDFS. Set the value to oss://{Bucket.Endpoint}/hbase.  |