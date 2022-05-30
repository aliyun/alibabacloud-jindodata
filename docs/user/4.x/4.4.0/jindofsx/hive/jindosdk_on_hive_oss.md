# Hive 处理阿里云 OSS 上的数据 + JindoFSx 透明加速

Hive 是大数据的常用工具之一，很多用户使用 Hive 搭建离线数仓。随着数据量不断增长，传统的基于 HDFS 存储的数仓可能无法以较低成本满足用户的需求，结合对象存储等云存储使用 Hive 也是一种常见做法。如果要使用 OSS 作为 Hive 数仓的底层存储，使用 JindoSDK 可以获得更好的读写性能以及更强大的技术支持。

## 安装配置

### 1. 在 Hive 客户端或服务所在结点安装 JindoSDK。

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))，解压后将sdk包安装到 Hive 的 classpath下。

````
cp jindosdk-x.x.x/lib/*.jar  $HIVE_HOME/lib/
````

### 2. 配置 JindoSDK

* 配置 OSS 实现类

将 JindoSDK OSS 实现类配置到 Hadoop 的配置文件 `core-site.xml` 中。

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

    <property>
        <name>fs.xengine</name>
        <value>jindofsx</value>
    </property>
</configuration>
```

* 配置 OSS Access Key

将 OSS Bucket 对应的`Access Key ID`、`Access Key Secret`、`Endpoint`等预先配置在 Hadoop 的`core-site.xml`中。

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

    <property>
        <name>fs.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```

JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoSDK OSS Credential Provider 配置](/docs/user/4.x/4.4.0/oss/security/jindosdk_credential_provider_oss.md)。

* 配置 JindoFSx Namespace 服务地址

配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- Namespace service 地址 -->
        <name>fs.jindofsx.namespace.rpc.address</name>
        <value>headerhost:8101</value>
    </property>
</configuration>
```
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.4.0/jindofsx/deploy/deploy_raft_ns.md)

* 开启缓存。

配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- 数据缓存开关 -->
        <name>fs.jindofsx.data.cache.enable</name>
        <value>true</value>
    </property>
</configuration>
```
更多缓存优化相关参数，请参考 [缓存优化相关参数](../configuration/jindosdk_configuration_list.md)

### 3. 重启 Hive 所有服务，使配置生效。

### 4. Hive on MR

当使用 Hive on MR 方式执行 Hive 作业时，还应保证集群所有结点均安装了 JindoSDK，需要把 `jindosdk-${version}.jar` 放到 `$HADOOP_CLASSPATH` 并重启 YARN 服务。并重启 Hive 所有服务。

### 5. Hive on Tez

当使用 Hive on Tez 方式执行 Hive 作业时，还应保证配置 `tez.lib.uris` 所指向路径中包含 `jindosdk-${version}.jar`。

### 6. Hive on Spark

当使用 Hive on Spark 方式执行 Hive 作业时，请参考[Spark 使用 JindoSDK 访问 OSS](../spark/jindosdk_on_spark.md)同时配置好 Spark。

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

JindoSDK包含一些高级调优参数，配置方式以及配置项参考文档[JindoSDK 配置项列表](../configuration/jindosdk_configuration_list.md)。
