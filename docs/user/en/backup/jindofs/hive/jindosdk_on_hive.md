Use JindoSDK with Hive to process data stored in OSS-HDFS (JindoFS)
Hive is a commonly used big data tool. You can use Hive to build offline data warehouses. Traditional HDFS-based data warehouses may not be able to meet your requirements for processing the growing amount of data at low costs. In this case, you can use Hive together with cloud services such as Object Storage Service (OSS) for storage. If you want to use OSS-HDFS as the underlying storage of Hive-based data warehouses, you can use JindoSDK to achieve better read and write performance and obtain better technical support. 
Installation and configuration
1. Install JindoSDK on the Hive client or the node on which Hive is deployed. 
[Download](https://github.com/aliyun/alibabacloud-jindodata/blob/latest/docs/user/en/jindosdk/jindosdk_download.md) the latest version of the jindosdk-x.x.x.tar.gz package, decompress the package, and then install the files that are contained in the package to the path specified by classpath of Hive. 
cp jindosdk-x.x.x/lib/*.jar  $HIVE_HOME/lib/
2. Configure the implementation class of OSS-HDFS and specify an AccessKey pair that you want to use to access OSS-HDFS.

- Configure the implementation class of OSS-HDFS.

Configure the implementation class of OSS-HDFS in the core-site.xml configuration file of Hadoop. 
<configuration>
<property>
<name>fs.AbstractFileSystem.oss.impl</name>
<value>com.aliyun.jindodata.oss.OSS</value>
</property>

<property>
<name>fs.oss.impl</name>
<value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
</property>
</configuration>

- Specify an AccessKey pair that you want to use to access OSS-HDFS.

Configure the AccessKey ID and AccessKey secret that are used to access the bucket for which OSS-HDFS is enabled in the core-site.xml configuration file of Hadoop in advance. 
<configuration>
<property>
<name>fs.oss.accessKeyId</name>
<value>xxx</value>
</property>

<property>
<name>fs.oss.accessKeySecret</name>
<value>xxxx</value>
</property>

</configuration>
JindoSDK also allows you to use other methods to configure an AccessKey pair. For more information, see [Configure credential providers in JindoSDK](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/security/jindosdk_credential_provider_dls.md). 
3. Configure an endpoint for OSS-HDFS.
To access OSS-HDFS, you must configure an endpoint in the cn-xxx.oss-dls.aliyuncs.com format. The endpoint of OSS-HDFS is different from the endpoint that is used to access OSS. The endpoint that is used to access OSS is in the oss-cn-xxx.aliyuncs.com format. JindoSDK accesses OSS-HDFS or OSS based on the endpoint that you configure. 
If you want to access OSS-HDFS, we recommend that you specify an access path in the oss://<Bucket>.<Endpoint>/<Object> format.
Example: oss://dls-chenshi-test.cn-shanghai.oss-dls.aliyuncs.com/Test. 
The preceding access path contains the endpoint of OSS-HDFS. JindoSDK accesses OSS-HDFS based on the endpoint in the access path. JindoSDK also allows you to use other methods to configure the endpoint that is used to access OSS-HDFS. For more information, see [Configure an endpoint to access OSS-HDFS](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_endpoint_configuration.md). 
Restart all components of Hive for the configurations to take effect. 
4. Use Hive on MR.
If you run a Hive job in Hive on MR mode, make sure that JindoSDK is installed on each node of your cluster. You must install the jindosdk-${version}.jar package in the path specified by $HADOOP_CLASSPATH and restart the YARN service. You must also restart all components of Hive. 
5. Use Hive on Tez.
If you run a Hive job in Hive on Tez mode, make sure that the path that is pointed to by tez.lib.uris contains jindosdk-${version}.jar. 
6. Use Hive on Spark.
If you run a Hive job in Hive on Spark mode, refer to [Use JindoSDK with Spark to process data stored in Alibaba Cloud OSS-HDFS (JindoFS)](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/spark/jindosdk_on_spark.md) to configure settings to access OSS-HDFS and configure Spark. 
Use OSS-HDFS for table storage
When you create a database or a table, you can specify an OSS-HDFS path and configure settings to allow the data in the database or table to be stored in OSS-HDFS by default.
CREATE DATABASE db_on_oss1 LOCATION 'oss://bucket_name.endpoint_name/path/to/db1';
CREATE TABLE db2.table_on_oss ... LOCATION 'oss://bucket_name.endpoint_name/path/to/db2/tablepath';
You can also set the hive.metastore.warehouse.dir parameter in the hive-site.xml configuration file of Hive Metastore to an OSS-HDFS path and restart Hive Metastore. This way, new databases and tables in the databases are stored in OSS-HDFS by default. 
<configuration>

<property>
<name>hive.metastore.warehouse.dir</name>
<value>oss://bucket_name.endpoint_name/path/to/warehouse</value>
</property>

</configuration>
Add partitions stored in OSS-HDFS to an existing table
ALTER TABLE existed_table ADD PARTITION (dt='2021-03-01', country='cn') LOCATION 'oss://bucket_name.endpoint_name/path/to/us/part210301cn';
Parameter tuning
JindoSDK contains specific advanced tuning parameters. For information about the parameters and how to configure the parameters, see [Configuration of JindoSDK advanced parameters](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_configuration_list.md). 

