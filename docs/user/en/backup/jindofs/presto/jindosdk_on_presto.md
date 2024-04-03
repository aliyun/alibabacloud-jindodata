Use JindoSDK with Presto to query data stored in OSS-HDFS
Presto is an open source distributed SQL query engine. It is used to run interactive analytic queries. This topic describes how to use JindoSDK with Presto to access OSS-HDFS. 
Procedure
1. Install a required JAR package.
[Download](https://github.com/aliyun/alibabacloud-jindodata/blob/latest/docs/user/en/jindosdk/jindosdk_download.md) the latest version of the jindosdk-x.x.x.tar.gz package, decompress the package, and then install the JAR files that are contained in the package to each node on which Presto is deployed. 
cp jindosdk-x.x.x/lib/*.jar  $PRESTO_HOME/plugin/hive-hadoop2/
2. Configure the implementation class of OSS-HDFS.
Configure the implementation class of OSS-HDFS in the core-site.xml configuration file of Hadoop on each node on which Presto is deployed. 
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
3. Specify an AccessKey pair that you want to use to access OSS-HDFS.
Configure the AccessKey ID, AccessKey secret, and endpoint that are used to access the bucket for which OSS-HDFS is enabled in the core-site.xml configuration file of Hadoop on each node on which Presto is deployed in advance. 
<configuration>
<property>
<name>fs.oss.accessKeyId</name>
<value>xxx</value>
</property>

<property>
<name>fs.oss.accessKeySecret</name>
<value>xxx</value>
</property>
</configuration>
JindoSDK also allows you to use other methods to configure an AccessKey pair. For more information, see [Configure credential providers in JindoSDK](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/security/jindosdk_credential_provider.md). 
4. Configure an endpoint for OSS-HDFS.
To access OSS-HDFS, you must configure an endpoint in the cn-xxx.oss-dls.aliyuncs.com format. The endpoint of OSS-HDFS is different from the endpoint that is used to access OSS. The endpoint that is used to access OSS is in the oss-cn-xxx.aliyuncs.com format. JindoSDK accesses OSS-HDFS or OSS based on the endpoint that you configure. 
If you want to access OSS-HDFS, we recommend that you specify an access path in the oss://<Bucket>.<Endpoint>/<Object> format.
Example: oss://dls-chenshi-test.cn-shanghai.oss-dls.aliyuncs.com/Test. 
The preceding access path contains the endpoint of OSS-HDFS. JindoSDK accesses OSS-HDFS based on the endpoint in the access path. JindoSDK also allows you to use other methods to configure the endpoint that is used to access OSS-HDFS. For more information, see [Configure an endpoint to access OSS-HDFS](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_endpoint_configuration.md). 
5. Restart all components of Presto for the configurations to take effect. 
Example
In the following example, a Hive catalog is used. You can use Presto to create a schema for OSS-HDFS and execute SQL statements to query data that is stored in OSS-HDFS. You must install and deploy JindoSDK in Hive because Presto depends on Hive Metastore. For more information, see [Use JindoSDK with Hive to process data stored in Alibaba Cloud OSS-HDFS (JindoFS)](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/hive/jindosdk_on_hive.md). 

- Run the following command to log on to the Presto console:

presto --server <presto_server_address>:<presto_server_port> --catalog hive

- Execute the following statements to create and use a schema in OSS-HDFS:

create schema testDB with (location='oss://<Bucket>.<Endpoint>/<schema_dir>');
use testDB;

- Execute the following statements to create a table and query data from the table:

create table tbl (key int, val int);
insert into tbl values (1,666);
select * from tbl;

