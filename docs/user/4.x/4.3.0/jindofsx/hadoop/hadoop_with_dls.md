# Hadoop 访问阿里云 OSS-HDFS 服务（JindoFS 服务）+ JindoFSx 透明加速

JindoSDK 为 JindoFSx 存储加速系统系统提供了 Apache Hadoop 支持。

## 前提条件：
* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.3.0/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.3.0/jindofsx/deploy/deploy_jindosdk.md)

## 配置 JindoSDK

* 配置实现类

将 JindoFSx 服务 OSS 实现类配置到 Hadoop 的`core-site.xml`中。

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

* 配置 Access Key

将 OSS-HDFS 服务 Bucket 对应的`Access Key ID`、`Access Key Secret`等预先配置在 Hadoop 的 `core-site.xml` 中。

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

JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoSDK OSS-HDFS 服务 Credential Provider 配置](/docs/user/4.x/4.3.0/jindofs/security/jindosdk_credential_provider_dls.md)。

* 配置 OSS-HDFS 服务 Endpoint

访问 OSS Bucket 上 OSS-HDFS 服务需要配置 Endpoint（`cn-xxx.oss-dls.aliyuncs.com`），与 OSS 对象接口的 Endpoint（`oss-cn-xxx-internal.aliyuncs.com`）不同。JindoSDK 会根据配置的 Endpoint 访问 OSS-HDFS 服务 或 OSS 对象接口。

使用 OSS-HDFS 服务时，推荐访问路径格式为：`oss://<Bucket>.<Endpoint>/<Object>`

如: `oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test`。

这种方式在访问路径中包含 OSS-HDFS 服务的 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应的 JindoFS 接口。 JindoSDK 还支持更多的 Endpoint 配置方式，详情参考 [OSS-HDFS 服务 Endpoint 配置](/docs/user/4.x/4.3.0/jindofs/configuration/jindosdk_endpoint_configuration.md)。

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
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.3.0/jindofsx/deploy/deploy_raft_ns.md)

* 开启数据缓存。

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
更多缓存优化相关参数，请参考 [缓存优化相关参数](/docs/user/4.x/4.3.0/jindofsx/configuration/jindosdk_configuration_list.md)

## 使用 JindoSDK 访问 OSS-HDFS 服务
用 Hadoop Shell 访问 JindoFS，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> oss://<Bucket>.<Endpoint>/
```

* ls 操作
```
hadoop fs -ls oss://<Bucket>.<Endpoint>/
```

* mkdir 操作
```
hadoop fs -mkdir oss://<Bucket>.<Endpoint>/<path>
```

* rm 操作
```
hadoop fs -rm oss://<Bucket>.<Endpoint>/<path>
```

## 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](/docs/user/4.x/4.3.0/jindofsx/configuration/jindosdk_configuration_list.md)