# Hive 使用 JindoSDK 访问 OSS

## 前置

* 您的 Hive 集群已经配置 HADOOP 环境，并正确配置了 core-site.xml

## 安装配置

1. 在 Hive 客户端或服务所在节点安装 JindoSDK。

下载[地址](jindofs_sdk_download.md)

```
cp jindofs-sdk-${version}.jar  $HIVE_HOME/lib/
```

2. 在运行 hivecli 或 HiveServer2 或 Hive Metastore 服务的节点修改 core-site.xml 配置 JindoSDK 访问 OSS

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

    <property>
        <name>fs.jfs.cache.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.endpoint</name>
        <!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

如需更多的OSS Credential配置方式，请参考[Credential Provider 使用](jindosdk_credential_provider.md)

然后重启 Hive 所有服务，使配置生效。

3. 当使用 Hive on MR 方式执行 Hive 作业时，还应保证集群所有结点均安装了 JindoSDK，需要把 `jindofs-sdk-${version}.jar` 放到 `$HADOOP_CLASSPATH` 并重启 YARN 服务。也可以把 `jindofs-sdk-${version}.jar` 设置到 *hive-env.sh* 的 `HIVE_AUX_JARS_PATH` 变量中，并重启 Hive 所有服务。

4. 当使用 Hive on Tez 方式执行 Hive 作业时，还应保证配置 `tez.lib.uris` 所指向路径中包含 `jindofs-sdk-${version}.jar`。

5. 当使用 Hive on Spark 方式执行 Hive 作业时，请参考[Spark 使用 JindoSDK 访问 OSS](/docs/spark/jindosdk_on_spark.md)同时配置好 Spark。

## 使用

### OSS 用于表的存储

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

### 给已有表添加位于 OSS 的分区

```sql
ALTER TABLE existed_table ADD PARTITION (dt='2021-03-01', country='cn') location 'oss://bucketname/path/to/us/part210301cn'
```
