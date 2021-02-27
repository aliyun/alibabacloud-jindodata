# Presto 使用 JindoFS SDK 访问 OSS

# 安装部署

* 前往[地址](jindofs_sdk_download.md)下载 JindoFS SDK 最新版本， 下载 jindofs-sdk-${version}.jar 对应的jar包。

* 在所有 Presto 节点安装 JindoFS SDK。
````
cp jindofs-sdk-${version}.jar  $PRESTO_HOME/plugin/hive-hadoop2/
````

* 在所有 Presto 节点修改 core-site.xml 配置 JindoFS SDK 访问 OSS。

````
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

````
JindoFS 还支持更多的 OSS Access Key 的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](./jindosdk_credential_provider.md)。<br />

* 重启 Presto 所有服务，使配置生效。

# 使用示例
以下以最常用的 Hive catalog 为例，使用 Presto 创建一个 OSS 上的 schema，并执行一些简单的 sql 示例。由于依赖 Hive Metastore，Hive 服务也需要安装部署 JindoFS SDK，请参考 Hive 相关文档。
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