# Hive with JindoSDK for Handling Data on Alibaba Cloud OSS-HDFS Service (JindoFS Service)

Hive is a popular tool in big data processing, commonly used to build offline data warehouses. As data volumes grow, traditional HDFS-based warehouses might become cost-prohibitive. Integrating with cloud storage services like OSS-HDFS Service can provide better scalability and performance. By leveraging JindoSDK with Hive, users can achieve enhanced read-write efficiency and technical support.

## Installation & Configuration

### 1. Install JindoSDK on Hive Clients or Nodes

Download the latest tar.gz package `jindosdk-x.x.x.tar.gz` from the [download page](../jindosdk_download.md), extract it, and copy the SDK jars to Hive's classpath.

```bash
cp jindosdk-x.x.x/lib/*.jar $HIVE_HOME/lib/
```

### 2. Configure OSS-HDFS Service Implementation & Access Key

#### Implement JindoSDK's OSS-HDFS Service Class

Update Hadoop's `core-site.xml` with JindoSDK's OSS-HDFS service implementation classes.

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

#### Configure Access Key for OSS-HDFS Service

Add the `Access Key ID` and `Access Key Secret` associated with your OSS-HDFS service-enabled bucket in Hadoop's `core-site.xml`.

```xml
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
```
Refer to [JindoSDK OSS-HDFS Service Credential Provider Configuration](../jindosdk_credential_provider.md) for alternative credential setup methods.

### 3. Configure the OSS-HDFS Service Endpoint

Optionally, set a default endpoint in `core-site.xml` to simplify access paths:

```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```
For detailed endpoint configuration options, see [OSS-HDFS Service Endpoint Configuration](../jindosdk_endpoint_configuration.md).

Finally, restart all Hive services to apply the changes.

### 4. Hive on MapReduce (MR)

Ensure JindoSDK is installed on all cluster nodes. Add `jindosdk-${version}.jar` to `$HADOOP_CLASSPATH` and restart YARN services before restarting Hive services.

### 5. Hive on Tez

Include `jindosdk-${version}.jar` in the `tez.lib.uris` configuration pointing to its location.

### 6. Hive on Spark

Follow the instructions in [Spark Using JindoSDK to Access OSS](../spark/jindosdk_on_spark.md) and configure Spark accordingly.

### Storing Tables on OSS-HDFS Service

Create databases or tables with their data stored on OSS-HDFS Service by specifying the location:

```sql
CREATE DATABASE db_on_oss1 LOCATION 'oss://bucket_name.endpoint_name/path/to/db1';
CREATE TABLE db2.table_on_oss ... LOCATION 'oss://bucket_name.endpoint_name/path/to/db2/tablepath';
```

Alternatively, set `hive.metastore.warehouse.dir` in Hive Metastore's *hive-site.xml* to an OSS-HDFS Service path, then restart Hive Metastore to store new databases and tables there by default:

```xml
<configuration>
    <property>
        <name>hive.metastore.warehouse.dir</name>
        <value>oss://bucket_name.endpoint_name/path/to/warehouse</value>
    </property>
</configuration>
```

### Adding Partitions Located on OSS to Existing Tables

```sql
ALTER TABLE existed_table ADD PARTITION (dt='2021-03-01', country='cn') LOCATION 'oss://bucket_name.endpoint_name/path/to/us/part210301cn';
```

### Tuning Parameters

For advanced parameter tuning, refer to the [JindoSDK Configuration Options List](../jindosdk_configuration.md).