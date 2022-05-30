# Presto 使用 JindoSDK 查询 OSS 上的数据

Presto 是一个开源的分布式 SQL 查询引擎，适用于交互式分析查询。本文介绍如何配置 Presto 通过 JindoSDK 访问阿里云OSS数据湖存储。

## 步骤

### 1. 安装 jar 包

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))，解压后将sdk包安装到所有 Presto 节点。

````
cp jindosdk-x.x.x/lib/*.jar  $PRESTO_HOME/plugin/hive-hadoop2/
````

### 2. 配置 JindoSDK OSS 实现类
将 JindoSDK OSS 实现类配置到所有 Presto 节点上的 Hadoop 的 `core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```

### 3. 配置 OSS Access Key
将 OSS 的 `Access Key ID`、`Access Key Secret`、`Endpoint` 等预先配置在所有 Presto 节点上的 Hadoop 的 `core-site.xml` 中。
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
        <value>cn-xxx.oss.aliyuncs.com</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考[JindoSDK Credential Provider 配置](../security/jindosdk_credential_provider.md)。

### 4. 重启 Presto 所有服务，使配置生效。

## 使用示例
以下以最常用的 Hive catalog 为例，使用 Presto 创建一个 OSS 上的 schema，并执行一些简单的 sql 示例。由于依赖 Hive Metastore，Hive 服务也需要安装部署 JindoSDK，请参考 [Hive 使用 JindoSDK 访问 OSS](../hive/jindosdk_on_hive.md)。

* 执行命令，进入 Presto 控制

```bash
presto --server <presto_server_address>:<presto_server_port> --catalog hive
```

* 创建并使用一个 location 位于 OSS 上的 schema

```sql
create schema testDB with (location='oss://<bucket>/<schema_dir>');
use testDB;
```

* 创建table，执行sql测试验证

```sql
create table tbl (key int, val int);
insert into tbl values (1,666);
select * from tbl;
```