# JindoFSx 存储加速系统快速入门

JindoFSx 是 JindoData 提供的云数据访问中心，为用户管理云平台对于云存储的使用，针对各种访问场景，提供一站式的存储优化服务。JindoFSx 可以为用户同时管理多个后端存储系统。同时，JindoFSx 会为上层应用构建基于多种存储介质的数据缓存，并规划高效的数据访问策略，保障用户作业高速、稳定运行。

## 前提条件：
* 已部署 JindoFSx 存储加速系统。

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.6.x/4.6.10/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK。

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.6.x/4.6.10/jindofsx/deploy/deploy_jindosdk.md)

* 文件以对象的形式存储在 OSS 上。

本文以 JindoFSx 支持阿里云 OSS 透明缓存加速的使用方式为例。利用集群本身的存储资源缓存 OSS 文件，从而加速作业访问 OSS。

## 服务端配置阿里云 OSS AccessKey
JindoFSx 若要提供缓存加速服务，需配置具有访问 OSS 权限的 AccessKey。
在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg。 配置缓存加速的 OSS bucket 对应的`Access Key ID`、`Access Key Secret`、`Endpoint`。

```
[jindofsx-common]
jindofsx.oss.bucket.XXX.accessKeyId = xxx
jindofsx.oss.bucket.XXX.accessKeySecret = xxx
jindofsx.oss.bucket.XXX.endpoint = oss-cn-xxx-internal.aliyuncs.com

jindofsx.oss.bucket.YYY.accessKeyId = xxx
jindofsx.oss.bucket.YYY.accessKeySecret = xxx
jindofsx.oss.bucket.YYY.endpoint = oss-cn-xxx-internal.aliyuncs.com
```
说明: XXX 和 YYY 为 OSS bucket 名称。

将配置文件部署到所需节点上（Namespace Service 和 Storage Service 所在节点）。

## 重启 JindoFSx 服务
重启 JindoFSx 服务，使得配置的 OSS bucket 的`Access Key ID`、`Access Key Secret`、`Endpoint`生效。在 master 节点执行以下脚本。
```
cd jindofsx-x.x.x
sh sbin/stop-service.sh
sh sbin/start-service.sh
```

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

* 配置 AccessKey

将 OSS 的 Bucket 对应的`Access Key ID`、`Access Key Secret`、`Endpoint`等预先配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.jindofsx.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jindofsx.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.jindofsx.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoSDK OSS Credential Provider 配置](/docs/user/4.x/4.6.x/4.6.10/oss/security/jindosdk_credential_provider_oss.md)。

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
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.6.x/4.6.10/jindofsx/deploy/deploy_raft_ns.md)

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
更多缓存优化相关参数，请参考 [缓存优化相关参数](configuration/jindosdk_configuration_list.md)

完成以上配置后，作业访问 OSS 即可利用上缓存，JindoFSx 存储加速系统提供了透明缓存的使用方式，作业访问 OSS 的方式无需做任何修改。
作业读取 OSS 上的数据后，会自动缓存到 JindoFSx 存储加速系统中，后续访问相同的数据就能够命中缓存。

注意：此配置为客户端配置，不需要重启 JindoFSx 服务。

## 使用 Prometheus + Grafana 可视化指标观测平台
在完成 JindoFSx 缓存集群的启动后，JindoFSx 存储加速系统提供基于 Prometheus + Grafana 的可视化指标观测平台，你可以参考如下文档进行安装和使用该功能

[基于 Prometheus + Grafana 的可视化指标观测平台](jindofsx_metrics.md)

## 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](configuration/jindosdk_configuration_list.md)