# Presto 查询 JindoFSx 统一挂载的数据

Presto 是一个开源的分布式 SQL 查询引擎，适用于交互式分析查询。本文介绍如何配置 Presto 通过 JindoSDK 访问阿里云OSS数据湖存储。

## 步骤

### 1. 安装 jar 包

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))，解压后将sdk包安装到所有 Presto 节点。

````
cp jindosdk-x.x.x/lib/*.jar  $PRESTO_HOME/plugin/hive-hadoop2/
````

### 2. 配置 JindoSDK
* 配置实现类

将 JindoFSx 统一名字空间使用的实现类配置到所有 Presto 节点上的 Hadoop 的 `core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.jindo.impl</name>
        <value>com.aliyun.jindodata.jindo.JINDO</value>
    </property>

    <property>
        <name>fs.jindo.impl</name>
        <value>com.aliyun.jindodata.jindo.JindoFileSystem</value>
    </property>

    <property>
        <name>fs.xengine</name>
        <value>jindofsx</value>
    </property>
</configuration>
```

* 配置 Access Key

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
JindoSDK 还支持更多的 AccessKey 的配置方式。

OSS Bucket 参考 [JindoSDK OSS Credential Provider 配置](/docs/user/4.x/4.6.x/4.6.9/oss/security/jindosdk_credential_provider_oss.md)。

OSS-HDFS 服务 Bucket 参考 [JindoSDK OSS-HDFS 服务 Credential Provider 配置](/docs/user/4.x/4.6.x/4.6.9/jindofs/security/jindosdk_credential_provider_dls.md)。

* 配置 OSS 或 OSS-HDFS 服务 Endpoint

```
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```
如果统一挂载的为 OSS-HDFS 服务目录，配置 Endpoint 请参考 [OSS-HDFS 服务 Endpoint 配置](/docs/user/4.x/4.6.x/4.6.9/jindofs/configuration/jindosdk_endpoint_configuration.md)。

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
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.6.x/4.6.9/jindofsx/deploy/deploy_raft_ns.md)

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

### 3. 重启 Presto 所有服务，使配置生效。

### 4. 挂载 OSS 或 OSS-HDFS 服务目录

* 挂载命令

```
jindo fsxadmin -mount <path> <realpath>
```

例：

```
jindo fsxadmin -mount /jindooss oss://<Bucket>.<Endpoint>/
```

执行如上命令后，则 /jindooss 目录下真正挂载的文件路径是 `oss://<Bucket>.<Endpoint>/`

## 使用示例
以下以最常用的 Hive catalog 为例，使用 Presto 创建一个 OSS 上的 schema，并执行一些简单的 sql 示例。由于依赖 Hive Metastore，Hive 服务也需要安装部署 JindoSDK，请参考 [Hive 使用 JindoSDK 访问 OSS](../hive/jindosdk_on_hive_oss.md)。

* 执行命令，进入 Presto 控制台

```bash
presto --server <presto_server_address>:<presto_server_port> --catalog hive
```

* 创建并使用一个 location 位于 OSS 上的 schema

```sql
create schema testDB with (location='jindo://emr-header-1:8101/jindooss/<schema_dir>');
use testDB;
```

* 创建table，执行sql测试验证

```sql
create table tbl (key int, val int);
insert into tbl values (1,666);
select * from tbl;
```