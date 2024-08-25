# Using AliCloud OSS-HDFS Service (JindoFS Service) as Underlying Storage for HBase

HBase is a real-time database within the Hadoop ecosystem, known for its high write performance. The OSS-HDFS Service offered by Alibaba Cloud is a new storage space type that is compatible with the HDFS interface. With JindoSDK, HBase can utilize the OSS-HDFS Service for both its underlying storage and Write-Ahead Log (WAL) files, enabling a decoupling of compute and storage and providing greater flexibility and reduced operational costs compared to local HDFS storage.

## 1. Download the JindoSDK Package

Download the latest tar.gz package named `jindosdk-x.x.x.tar.gz` from the [download page](../jindosdk_download.md).

## 2. Install the Jar Files

Unzip the downloaded package and copy the following jar files to the Hadoop classpath:

* `jindo-core-x.x.x.jar`
* `jindo-sdk-x.x.x.jar`

For example, with jindosdk-6.6.0:

```bash
cp jindosdk-6.6.0/lib/jindo-core-6.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-6.6.0/lib/jindo-sdk-6.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

## 3. Configure the OSS-HDFS Service Implementation and Access Key

Add the JindoSDK OSS implementation classes to Hadoop's `core-site.xml`.

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
Configure the `Access Key ID` and `Access Key Secret` for the OSS-HDFS service-enabled bucket in Hadoop's `core-site.xml`.

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
JindoSDK also supports various ways to configure credentials; for more details, refer to [JindoSDK OSS-HDFS Service Credential Provider Configuration](../jindosdk_credential_provider.md).

## 4. Configure the OSS-HDFS Service Endpoint

Set the endpoint for accessing the OSS/OSS-HDFS bucket in the `core-site.xml`. You can either specify the endpoint in the access path format, such as `oss://examplebucket.cn-hangzhou.oss-dls.aliyuncs.com/exampleobject.txt`, or configure a default endpoint to simplify the path to `oss://examplebucket/exampleobject.txt`.

For the simplified path, add this to the `core-site.xml`:

```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```
For more information on endpoint configuration, refer to [OSS-HDFS Service Endpoint Configuration](../jindosdk_endpoint_configuration.md).

## 5. Specify HBase Storage Paths

Modify the `hbase-site` configuration file to set the parameters `hbase.rootdir` and `hbase.wal.dir` to the OSS-HDFS service addresses, defining the storage paths for HBase data and WAL files. Before doing so, disable the table to ensure that all WAL files have been converted to HFiles.

| Parameter | Description |
| --- | --- |
| hbase.rootdir | Specifies the root storage directory for HBase on the OSS-HDFS service, with a value like `oss://bucket.endpoint/hbase-root-dir`. |
| hbase.wal.dir | Specifies the storage directory for HBase's WAL files on the OSS-HDFS service, with a value like `oss://bucket.endpoint/hbase-wal-dir`. |

Remember to re-enable the table once the configuration changes are complete and all necessary migrations have taken place.