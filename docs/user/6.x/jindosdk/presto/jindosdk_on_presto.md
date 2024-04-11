# Presto 使用 JindoSDK 查询 OSS-HDFS 服务上的数据

Presto 是一个开源的分布式 SQL 查询引擎，适用于交互式分析查询。本文介绍如何配置 Presto 通过 JindoSDK 访问阿里云OSS数据湖存储。

## 步骤

### 1. 安装 jar 包

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](../jindosdk_download.md))，解压后将sdk包安装到所有 Presto 节点。

````
cp jindosdk-x.x.x/lib/*.jar  $PRESTO_HOME/plugin/hive-hadoop2/
````

### 2. 配置 JindoSDK OSS 实现类
将 JindoSDK OSS 实现类配置到所有 Presto 节点上的 Hadoop 的 `core-site.xml`中。
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

### 3. 配置 OSS Access Key
将已开启 HDFS 服务的 Bucket 对应的`Access Key ID`、`Access Key Secret`、`Endpoint` 等预先配置在所有 Presto 节点上的 Hadoop 的 `core-site.xml` 中。
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
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考[JindoSDK Credential Provider 配置](../jindosdk_credential_provider.md)。

### 4. 配置 OSS-HDFS 服务 Endpoint

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

### 5. 重启 Presto 所有服务，使配置生效。

## 使用示例
以下以最常用的 Hive catalog 为例，使用 Presto 创建一个 OSS 上的 schema，并执行一些简单的 sql 示例。由于依赖 Hive Metastore，Hive 服务也需要安装部署 JindoSDK，请参考 [Hive 使用 JindoSDK 访问 OSS](../hive/jindosdk_on_hive.md)。

* 执行命令，进入 Presto 控制台

```bash
presto --server <presto_server_address>:<presto_server_port> --catalog hive
```

* 创建并使用一个 location 位于 OSS 上的 schema

```sql
create schema testDB with (location='oss://<Bucket>.<Endpoint>/<schema_dir>');
use testDB;
```

* 创建table，执行sql测试验证

```sql
create table tbl (key int, val int);
insert into tbl values (1,666);
select * from tbl;
```