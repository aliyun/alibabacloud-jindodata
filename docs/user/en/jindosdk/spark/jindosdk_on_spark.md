# Spark Processing Data on Alibaba Cloud OSS-HDFS Service with JindoSDK

JindoSDK is a user-friendly OSS client designed for the Hadoop/Spark ecosystem, offering optimized Hadoop FileSystem implementations for Alibaba Cloud OSS. When using Spark with JindoSDK instead of the community Hadoop OSS client, you can expect better performance and dedicated support from the Alibaba Cloud E-MapReduce product team.

# Steps

### Step 1: Configure HADOOP Settings for Spark
Make sure Spark's configuration includes the relevant HADOOP settings.

### Step 2: Add JindoSDK to Spark's CLASSPATH
Download the latest tar.gz package `jindosdk-x.x.x.tar.gz` from the [download page](../jindosdk_download.md), extract it, and copy the SDK jars to Spark's classpath.

```bash
cp jindosdk-x.x.x/lib/*.jar $SPARK_HOME/jars/
```

### Step 3: Configure JindoSDK

## Global Configuration: Modify Spark's `core-site.xml`
### Configure JindoSDK OSS-HDFS Service Implementation Classes
```xml
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
```
### Configure Access Key for OSS-HDFS Service
```xml
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
```
For alternative credential setup methods, refer to the [JindoSDK Credential Provider Configuration](../jindosdk_credential_provider.md).

### Configure the OSS-HDFS Service Endpoint
When accessing OSS/OSS-HDFS buckets, configure the endpoint. The recommended format is `oss://<Bucket>.<Endpoint>/<Object>`, e.g., `oss://examplebucket.cn-hangzhou.oss-dls.aliyuncs.com/exampleobject.txt`. After configuring, JindoSDK will use the endpoint in the access path to reach the corresponding OSS/OSS-HDFS service.

Alternatively, you can set a default endpoint to simplify the path format to `oss://<Bucket>/<Object>`, e.g., `oss://examplebucket/exampleobject.txt`.

```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```
JindoSDK also supports additional endpoint configuration options; refer to the [OSS-HDFS Service Endpoint Configuration](../jindosdk_endpoint_configuration.md) for details.

### Task-Level Configuration
You can set JindoSDK configurations at task submission time with the following example:
```bash
spark-submit --conf spark.hadoop.fs.abstractFileSystem.oss.impl=com.aliyun.jindodata.oss.OSS --conf spark.hadoop.fs.oss.impl=com.aliyun.jindodata.oss.JindoOssFileSystem --conf spark.hadoop.fs.oss.accessKeyId=xxx --conf spark.hadoop.fs.oss.accessKeySecret=xxx
```

### Use Spark to Access OSS
### Create a Table
```sql
create table test_oss (c1 string) location "oss://bucket.endpoint/dir";
```
### Insert Data
```sql
insert into table test_oss values ("testdata");
```
### Query the OSS Table
```sql
select * from test_oss;
```

### Parameter Tuning
For advanced optimization, refer to the [JindoSDK Configuration Options List](../jindosdk_configuration.md).