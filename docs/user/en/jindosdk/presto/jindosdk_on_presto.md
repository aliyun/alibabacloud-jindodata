# Presto Querying Data on Alibaba Cloud OSS-HDFS Service with JindoSDK

Presto is an open-source distributed SQL query engine suitable for interactive analytics. This guide explains how to configure Presto to access data stored in Alibaba Cloud's OSS data lake storage using JindoSDK.

# Steps

### Step 1: Install the Jar Packages

Download the latest tar.gz package `jindosdk-x.x.x.tar.gz` from the [download page](../jindosdk_download.md), unpack it, and install the SDK packages on all Presto nodes.

```bash
cp jindosdk-x.x.x/lib/*.jar $PRESTO_HOME/plugin/hive-hadoop2/
```

### Step 2: Configure JindoSDK OSS Implementation Classes

Add the JindoSDK OSS implementation classes to each Presto node's Hadoop `core-site.xml`.

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

### Step 3: Configure Access Key for OSS

Preconfigure the `Access Key ID`, `Access Key Secret`, and `Endpoint` associated with your OSS-enabled bucket in Hadoop's `core-site.xml` on all Presto nodes.

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
Refer to the [JindoSDK Credential Provider Configuration](../jindosdk_credential_provider.md) documentation for alternative ways to configure access keys.

### Step 4: Configure the OSS-HDFS Service Endpoint

Configure the endpoint for accessing OSS/OSS-HDFS buckets. The recommended format is `oss://<Bucket>.<Endpoint>/<Object>`, e.g., `oss://examplebucket.cn-hangzhou.oss-dls.aliyuncs.com/exampleobject.txt`. Once configured, JindoSDK uses the endpoint in the access path to connect to the appropriate OSS/OSS-HDFS service.

Alternatively, you can set a default endpoint to simplify the path format to `oss://<Bucket>/<Object>`:

```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```
For more endpoint configuration options, consult the [OSS-HDFS Service Endpoint Configuration](../jindosdk_endpoint_configuration.md) guide.

### Step 5: Restart All Presto Services

Restart all Presto services to activate the new configurations.

## Usage Example

Here's an example demonstrating how to use Presto with a Hive catalog and execute some basic SQL queries against data stored in OSS. Since it relies on Hive Metastore, make sure you've also installed and configured JindoSDK for Hive as described in [Hive Using JindoSDK Accessing OSS](../hive/jindosdk_on_hive.md).

* Start a Presto console session:

```bash
presto --server <presto_server_address>:<presto_server_port> --catalog hive
```

* Create a schema located on OSS:

```sql
create schema testDB with (location='oss://<Bucket>.<Endpoint>/<schema_dir>');
use testDB;
```

* Create a table and perform some test queries:

```sql
create table tbl (key int, val int);
insert into tbl values (1,666);
select * from tbl;
```

These steps should enable Presto to interact with data stored in Alibaba Cloud's OSS data lake storage using JindoSDK.