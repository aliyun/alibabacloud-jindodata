# Presto 使用 JindoFS SDK 访问 OSS

Presto 是一个开源的分布式 SQL 查询引擎，适用于交互式分析查询。本文介绍如何配置 Presto 通过 JindoFS SDK 访问阿里云OSS数据湖存储。

## 步骤

### 1. 安装 jar 包
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](/docs/jindofs_sdk_download.md))，然后在所有 Presto 节点安装 JindoFS SDK。

````
cp jindofs-sdk-${version}.jar  $PRESTO_HOME/plugin/hive-hadoop2/
````

### 2. 配置 JindoFS OSS 实现类
将 JindoFS OSS 实现类配置到所有 Presto 节点上的 Hadoop 的 core-site.xml中。
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
将 OSS 的 Access Key、Access Key Secret、Endpoint 等预先配置在所有 Presto 节点上的 Hadoop 的 core-site.xml 中。
```xml
<configuration>
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
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](./jindofs_sdk_credential_provider.md)。<br />

### 4. 重启 Presto 所有服务，使配置生效。

## 使用示例
以下以最常用的 Hive catalog 为例，使用 Presto 创建一个 OSS 上的 schema，并执行一些简单的 sql 示例。由于依赖 Hive Metastore，Hive 服务也需要安装部署 JindoFS SDK，请参考[Hive 使用 JindoSDK 访问 OSS](jindosdk_on_hive.md)。
* 执行命令，进入 Presto 控制台
````
presto --server <presto_server_address>:<presto_server_port> --catalog hive
````
* 创建并使用一个 location 位于 OSS 上的 schema
````
create schema testDB with (location='oss://<bucket>/<schema_dir>');
use testDB;
````
* 创建table，执行sql测试验证
````
create table tbl (key int, val int);
insert into tbl values (1,666);
select * from tbl;
````