# Use JindoSDK with Spark to process data stored in Alibaba Cloud OSS-HDFS (JindoFS)

JindoSDK is a simple and easy-to-use Object Storage Service (OSS) client that is adapted to Hadoop and Spark ecosystems. JindoSDK provides a highly optimized Hadoop FileSystem class for OSS. In addition, JindoSDK with Spark provides better query performance than OSS clients in the Hadoop community. You can also obtain technical support from the service team of Alibaba Cloud E-MapReduce (EMR). 

Procedure

1. Confirm that the configuration file directory of Spark contains Hadoop-related configuration files.

2. Install JindoSDK to the path specified by classpath.

[Download](https://github.com/aliyun/alibabacloud-jindodata/blob/latest/docs/user/en/jindosdk/jindosdk_download.md) the latest version of the jindosdk-x.x.x.tar.gz package, decompress the package, and then install the files that are contained in the package to the path specified by classpath of Spark. 

cp jindosdk-x.x.x/lib/\*.jar  $SPARK\_HOME/jars/

3. Configure JindoSDK.

#### Global configuration, implemented by modifying the core-site.xml configuration file of Spark

*   Configure the implementation class of OSS-HDFS.
    

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

*   Specify an AccessKey pair that you want to use to access OSS-HDFS.
    

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

JindoSDK also allows you to use other methods to configure an AccessKey pair. For more information, see [Configure credential providers in JindoSDK](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/security/jindosdk_credential_provider_dls.md). 

*   Configure an endpoint for OSS-HDFS.
    

To access OSS-HDFS, you must configure an endpoint in the cn-xxx.oss-dls.aliyuncs.com format. The endpoint of OSS-HDFS is different from the endpoint that is used to access OSS. The endpoint that is used to access OSS is in the oss-cn-xxx.aliyuncs.com format. JindoSDK accesses OSS-HDFS or OSS based on the endpoint that you configure. 

If you want to access OSS-HDFS, we recommend that you specify an access path in the oss://<Bucket>.<Endpoint>/<Object> format.

Example: oss://dls-chenshi-test.cn-shanghai.oss-dls.aliyuncs.com/Test. 

The preceding access path contains the endpoint of OSS-HDFS. JindoSDK accesses OSS-HDFS based on the endpoint in the access path. JindoSDK also allows you to use other methods to configure the endpoint that is used to access OSS-HDFS. For more information, see [Configure an endpoint to access OSS-HDFS](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_endpoint_configuration.md). 

Job-level configuration

Configure JindoSDK parameters when you submit Spark jobs. Sample code:

spark-submit --conf spark.hadoop.fs.AbstractFileSystem.oss.impl=com.aliyun.jindodata.oss.OSS --conf spark.hadoop.fs.oss.impl=com.aliyun.jindodata.oss.JindoOssFileSystem --conf spark.hadoop.fs.oss.accessKeyId=xxx  --conf spark.hadoop.fs.oss.accessKeySecret=xxx

4. Use Spark to access OSS.

Create a table.

create table test\_oss (c1 string) location "oss://bucket.endpoint/dir";

Insert data into the table.

insert into table test\_oss values ("testdata");

Query data in the table.

select \* from test\_oss;

5. Perform parameter tuning.

JindoSDK contains specific advanced tuning parameters. For information about the parameters and how to configure the parameters, see Configuration of JindoSDK advanced parameters.