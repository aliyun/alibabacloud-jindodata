# Hive 使用 JindoFS SDK 访问 OSS

Hive 是大数据的常用工具之一，很多用户使用 Hive 搭建离线数仓。随着数据量不断增长，传统的基于 HDFS 存储的数仓可能无法以较低成本满足用户的需求，结合对象存储等云存储使用 Hive 也是一种常见做法。如果要使用 OSS 作为 Hive 数仓的底层存储，使用 JindoFS SDK 可以获得更好的读写性能以及更强大的技术支持。

## 安装配置

### 1. 在 Hive 客户端或服务所在结点安装 JindoSDK。

下载[地址](jindofs_sdk_download.md)

```
cp jindofs-sdk-${version}.jar $HIVE_HOME/lib/
```

### 2. 配置 JindoFS OSS 实现类

将 JindoFS OSS 实现类配置到 Hadoop 的配置文件 *core-site.xml* 中。

```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.OSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```

### 3. 配置 OSS Access Key

将OSS的Access Key、Access Key Secret、Endpoint等预先配置在Hadoop的core-site.xml中。

```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.accessKeySecret</name>
        <value>xxxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](./jindofs_sdk_credential_provider.md)。

最后重启 Hive 所有服务，使配置生效。

### 4. Hive on MR

当使用 Hive on MR 方式执行 Hive 作业时，还应保证集群所有结点均安装了 JindoSDK，需要把 `jindofs-sdk-${version}.jar` 放到 `$HADOOP_CLASSPATH` 并重启 YARN 服务。并且把 `jindofs-sdk-${version}.jar` 设置到 *hive-env.sh* 的 `HIVE_AUX_JARS_PATH` 变量中，并重启 Hive 所有服务。

### 5. Hive on Tez

当使用 Hive on Tez 方式执行 Hive 作业时，还应保证配置 `tez.lib.uris` 所指向路径中包含 `jindofs-sdk-${version}.jar`。

### 6. Hive on Spark

当使用 Hive on Spark 方式执行 Hive 作业时，请参考[Spark 使用 JindoSDK 访问 OSS](/docs/spark/jindosdk_on_spark.md)同时配置好 Spark。

## OSS 用于表的存储

在创建数据库和表时，可以指定 OSS 路径，把数据库或表的数据默认保存到 OSS 上：

```sql
CREATE DATABASE db_on_oss1 LOCATION 'oss://bucketname/path/to/db1';
CREATE TABLE db2.table_on_oss ... LOCATION 'oss://bucketname/path/to/db2/tablepath';
```

也可以在 Hive Metastore 的 *hive-site.xml* 配置中设置 `hive.metastore.warehouse.dir` 到 OSS 路径，并重启 Hive Metastore，则后续创建的数据库和这些数据库下的表都会默认存储于 OSS。

```xml
<configuration>

    <property>
        <name>hive.metastore.warehouse.dir</name>
        <value>oss://bucketname/path/to/warehouse</value>
    </property>

</configuration>
```

## 给已有表添加位于 OSS 的分区

```sql
ALTER TABLE existed_table ADD PARTITION (dt='2021-03-01', country='cn') LOCATION 'oss://bucketname/path/to/us/part210301cn';
```

## 参数调优

JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档[JindoFS SDK配置项列表](./jindofs_sdk_configuration_list.md)。
