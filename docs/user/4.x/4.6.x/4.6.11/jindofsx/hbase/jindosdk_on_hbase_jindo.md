# HBase 使用 JindoFSx 统一挂载的数据
HBase 是 Hadoop 生态中的实时数据库，有很高的写入性能。 OSS-HDFS 服务是阿里云推出新的存储空间类型，兼容 HDFS 接口, JindoSDK 支持 HBase 使用 OSS-HDFS 服务作为底层存储实现存算分离，相对于本地HDFS存储，使用更加灵活，减少运维成本。

## 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 安装 jar 包
解压下载的安装包，将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.6.11 为例:
```
cp jindosdk-4.6.11/lib/jindo-core-4.6.11.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.6.11/lib/jindo-sdk-4.6.11.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

## 3. 配置 JindoSDK

* 配置实现类

将统一名字空间实现类配置到 Hadoop 的配置文件 `core-site.xml` 中。

将 JindoSDK OSS 实现类配置到 Hadoop 的`core-site.xml`中。

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

* 配置 OSS 或 OSS-HDFS 服务 Access Key

将 OSS Bucket 对应的`Access Key ID`、`Access Key Secret`等预先配置在 Hadoop 的`core-site.xml`中。
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

OSS Bucket 参考 [JindoSDK OSS Credential Provider 配置](/docs/user/4.x/4.6.x/4.6.11/oss/security/jindosdk_credential_provider_oss.md)。

OSS-HDFS 服务 Bucket 参考 [JindoSDK OSS-HDFS 服务 Credential Provider 配置](/docs/user/4.x/4.6.x/4.6.11/jindofs/security/jindosdk_credential_provider_dls.md)。

* 配置 OSS 或 OSS-HDFS 服务 Endpoint
```
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```
如果统一挂载的为 OSS-HDFS 服务目录，配置 Endpoint 请参考 [OSS-HDFS 服务 Endpoint 配置](/docs/user/4.x/4.6.x/4.6.11/jindofs/configuration/jindosdk_endpoint_configuration.md)。

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
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.6.x/4.6.11/jindofsx/deploy/deploy_raft_ns.md)

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

* 挂载 OSS 或 OSS-HDFS 服务目录

```
jindo fsxadmin -mount <path> <realpath>
```

例：
```
jindo fsxadmin -mount /jindodls oss://<Bucket>.<Endpoint>/
```

执行如上命令后，则 /jindodls 目录下真正挂载的文件路径是 `oss://<Bucket>.<Endpoint>/`

## 指定 HBase 的存储路径
需要修改`hbase-site`配置文件中的参数`hbase.rootdir`的值为 FSX 地址，修改参数`hbase.wal.dir`的值为本地的 HDFS 地址，通过本地 HDFS 集群存储 WAL 文件。如果要释放集群，需要先 Disable table，确保 WAL 文件已经完全更新到 HFile。

| 参数 | 描述 |
| --- | --- |
| hbase.rootdir | 指定 HBase 的 ROOT 存储目录到 FSX。参数值为`jindo://headerhost:8101/jindooss/hbase-root-dir`|
| hbase.wal.dir | 指定 HBase 的 WAL 存储目录到 FSX 或本地 HDFS。参数值为：</br>  HA集群：`hdfs://${nameservice}/hbase`</br>  非HA集群：`hdfs://${namenode_rpc_address}:${namenode_rpc_port}/hbase`</br>  |
