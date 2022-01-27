# Hadoop 访问 JindoFSx 统一挂载的数据

JindoSDK 为 JindoFSx 缓存系统系统提供了 Apache Hadoop 支持。

## 前提条件：
* 已部署 JindoFSx 缓存系统

关于如何部署 JindoFSx 缓存系统，请参考 [部署 JindoFSx 缓存系统](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindosdk.md)

## 配置 JindoSDK

* 配置实现类

将 JindoFSx 统一名字空间使用的实现类配置到 Hadoop 的`core-site.xml`中。

```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.fsx.impl</name>
        <value>com.aliyun.jindodata.fsx.FSX</value>
    </property>

    <property>
        <name>fs.fsx.impl</name>
        <value>com.aliyun.jindodata.fsx.JindoFsxFileSystem</value>
    </property>
</configuration>
```

* 配置 OSS 或 OSS-HDFS 服务 AccessKey

将 OSS 或 OSS-HDFS 服务 Bucket 对应的`Access Key ID`、`Access Key Secret`等预先配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.fsx.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.fsx.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoFSx 统一挂载(fsx://) Credential 配置](../security/jindosdk_credential.md)。

* 配置 OSS 或 OSS-HDFS 服务 Endpoint

将 OSS 或 OSS-HDFS 服务 Endpoint 配置到 Hadoop 的`core-site.xml`中。
```
<configuration>
    <property>
        <name>fs.fsx.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```

如果统一挂载的为 OSS-HDFS 服务目录，配置 Endpoint 请参考 [JindoFSx 缓存系统配置 OSS-HDFS 服务 Endpoint](../configuration/dls_endpoint_configuration.md)。

* 配置 JindoFSx Namespace 服务地址

配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- Namespace service 地址 -->
        <name>fs.fsx.namespace.rpc.address</name>
        <value>headerhost:8101</value>
    </property>
</configuration>
```
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_raft_ns.md)

* 开启数据缓存。

配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- 数据缓存开关 -->
        <name>fs.fsx.data.cache.enable</name>
        <value>true</value>
    </property>
</configuration>
```
更多缓存优化相关参数，请参考 [缓存优化相关参数](../configuration/jindosdk_configuration_list.md)

## 挂载 OSS 或 OSS-HDFS 服务目录

* 挂载命令

```
jindo fsxadmin -mount <path> <realpath>
```

例：

```
jindo fsxadmin -mount /jindooss oss://<Bucket>.<Endpoint>/
```

执行如上命令后，则 /jindooss 目录下真正挂载的文件路径是 `oss://<Bucket>.<Endpoint>/`

## 使用 JindoSDK 访问 OSS 或 OSS-HDFS 服务

用 Hadoop Shell 访问 OSS 或 OSS-HDFS 服务，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> fsx://emr-header-1:8101/jindooss/
```

* ls 操作
```
hadoop fs -ls fsx://emr-header-1:8101/jindooss/
```

* mkdir 操作
```
hadoop fs -mkdir fsx://emr-header-1:8101/jindooss/<path>
```

* rm 操作
```
hadoop fs rm fsx://emr-header-1:8101/jindooss/<path>
```

## 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](../configuration/jindosdk_configuration_list.md)