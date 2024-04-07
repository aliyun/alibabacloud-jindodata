# Hive 使用 JindoSDK 处理阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据

Hive 是大数据的常用工具之一，很多用户使用 Hive 搭建离线数仓。随着数据量不断增长，传统的基于 HDFS 存储的数仓可能无法以较低成本满足用户的需求，结合对象存储等云存储使用 Hive 也是一种常见做法。如果要使用 OSS-HDFS 服务作为 Hive 数仓的底层存储，使用 JindoSDK 可以获得更好的读写性能以及更强大的技术支持。

## 安装配置

### 1. 在 Hive 客户端或服务所在结点安装 JindoSDK。

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](../jindosdk_download.md))，解压后将sdk包安装到 Hive 的 classpath下。

````
cp jindosdk-x.x.x/lib/*.jar  $HIVE_HOME/lib/
````

### 2. 配置 OSS-HDFS 服务实现类及 Access Key

* 配置 JindoSDK OSS-HDFS 服务实现类

将 JindoSDK OSS-HDFS 服务实现类配置到 Hadoop 的配置文件 `core-site.xml` 中。

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

* 配置 OSS-HDFS 服务 Access Key

将已开启 OSS-HDFS 服务的 Bucket 对应的`Access Key ID`、`Access Key Secret`等预先配置在 Hadoop 的`core-site.xml`中。

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

JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考[JindoSDK OSS-HDFS 服务 Credential Provider 配置](../jindosdk_credential_provider.md)。

### 3. 配置 OSS-HDFS 服务 Endpoint

访问 OSS/OSS-HDFS Bucket时需要配置 Endpoint。推荐访问路径格式为 oss://`<Bucket>`.`<Endpoint>`/`<Object>`，例如 oss://examplebucket.cn-hangzhou.oss-dls.aliyuncs.com/exampleobject.txt。配置完成后，JindoSDK会根据访问路径中的 Endpoint 访问对应的 OSS/OSS-HDFS 服务接口。

此外，您也可以通过以下方式配置默认 Endpoint，以简化访问路径格式为 oss://`<Bucket>`/`<Object>`，例如 oss://examplebucket/exampleobject.txt
```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```

这种方式在访问路径中包含 OSS-HDFS 服务的 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应的 OSS-HDFS 服务接口。 JindoSDK 还支持更多的 Endpoint 配置方式，详情参考 [OSS-HDFS 服务 Endpoint 配置](../jindosdk_endpoint_configuration.md)。

最后重启 Hive 所有服务，使配置生效。

### 4. Hive on MR

当使用 Hive on MR 方式执行 Hive 作业时，还应保证集群所有结点均安装了 JindoSDK，需要把 `jindosdk-${version}.jar` 放到 `$HADOOP_CLASSPATH` 并重启 YARN 服务。并重启 Hive 所有服务。

### 5. Hive on Tez

当使用 Hive on Tez 方式执行 Hive 作业时，还应保证配置 `tez.lib.uris` 所指向路径中包含 `jindosdk-${version}.jar`。

### 6. Hive on Spark

当使用 Hive on Spark 方式执行 Hive 作业时，请参考[Spark 使用 JindoSDK 访问 OSS](../spark/jindosdk_on_spark.md)同时配置好 Spark。

## OSS-HDFS 服务用于表的存储

在创建数据库和表时，可以指定 OSS-HDFS 服务路径，把数据库或表的数据默认保存到 OSS-HDFS 服务上：

```sql
CREATE DATABASE db_on_oss1 LOCATION 'oss://bucket_name.endpoint_name/path/to/db1';
CREATE TABLE db2.table_on_oss ... LOCATION 'oss://bucket_name.endpoint_name/path/to/db2/tablepath';
```

也可以在 Hive Metastore 的 *hive-site.xml* 配置中设置 `hive.metastore.warehouse.dir` 到 OSS-HDFS 服务路径，并重启 Hive Metastore，则后续创建的数据库和这些数据库下的表都会默认存储于 OSS-HDFS 服务。

```xml
<configuration>

    <property>
        <name>hive.metastore.warehouse.dir</name>
        <value>oss://bucket_name.endpoint_name/path/to/warehouse</value>
    </property>

</configuration>
```

## 给已有表添加位于 OSS 的分区

```sql
ALTER TABLE existed_table ADD PARTITION (dt='2021-03-01', country='cn') LOCATION 'oss://bucket_name.endpoint_name/path/to/us/part210301cn';
```

## 参数调优

JindoSDK包含一些高级调优参数，配置方式以及配置项参考文档[JindoSDK 配置项列表](../jindosdk_configuration.md)。