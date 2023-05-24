# 阿里云 OSS-HDFS 服务（JindoFS 服务）统一挂载缓存加速

本文主要介绍 JindoFSx 支持阿里云 OSS-HDFS 服务（JindoFS 服务）统一挂载缓存加速的使用方式。

## 前提条件：
* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.6.x/4.6.8/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.6.x/4.6.8/jindofsx/deploy/deploy_jindosdk.md)

## 服务端配置 OSS-HDFS 服务 AccessKey
在`JINDOFSX_CONF_DIR`文件夹下修改配置 jindofsx.cfg 文件, 配置缓存加速的 OSS-HDFS 服务 bucket 对应的`Access Key ID`、`Access Key Secret`、`Endpoint`，
并更新到所需要节点上（Namespace Service 和 Storage Service 所在节点）。

```
[jindofsx-common]
jindofsx.oss.bucket.XXX.accessKeyId = xxx
jindofsx.oss.bucket.XXX.accessKeySecret = xxx
jindofsx.oss.bucket.XXX.endpoint = cn-xxx.oss-dls.aliyuncs.com

jindofsx.oss.bucket.YYY.accessKeyId = xxx
jindofsx.oss.bucket.YYY.accessKeySecret = xxx
jindofsx.oss.bucket.YYY.endpoint = oss-cn-xxx-internal.aliyuncs.com

jindofsx.oss.bucket.YYY.user = xxx #Storage Service 访问 OSS-HDFS 服务使用的用户名
```

说明: XXX 和 YYY 为 OSS-HDFS 服务 bucket 的名称。

## 重启 JindoFSx 服务
重启 JindoFSx 服务，使得配置的 OSS-HDFS 服务 bucket 的`Access Key ID`、`Access Key Secret`、`Endpoint`生效。在 master 节点执行以下脚本。
```
cd jindofsx-x.x.x
sh sbin/stop-service.sh
sh sbin/start-service.sh
```

## 配置 JindoSDK

* 配置统一名字空间使用的实现类

将 JindoFSx 统一名字空间使用的实现类配置到 Hadoop 的`core-site.xml`中。

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

* 配置 OSS-HDFS 服务 AccessKey

将 OSS-HDFS 服务 Bucket 对应的`Access Key ID`、`Access Key Secret`等预先配置在 Hadoop 的`core-site.xml`中。
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
JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoSDK OSS-HDFS 服务 Credential Provider 配置](/docs/user/4.x/4.6.x/4.6.8/jindofs/security/jindosdk_credential_provider_dls.md)。

* 配置 OSS-HDFS 服务 Endpoint

访问 OSS Bucket 上 OSS-HDFS 服务需要配置 Endpoint（`cn-xxx.oss-dls.aliyuncs.com`），与 OSS 对象接口的 Endpoint（`oss-cn-xxx-internal.aliyuncs.com`）不同。JindoSDK 会根据配置的 Endpoint 访问 OSS-HDFS 服务或 OSS 对象接口。

使用 OSS-HDFS 服务时，推荐访问路径格式为：`oss://<Bucket>.<Endpoint>/<Object>`

如: `oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test`。

这种方式在访问路径中包含 OSS-HDFS 服务的 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应的 OSS-HDFS 服务接口。 JindoSDK 还支持更多的 Endpoint 配置方式，详情参考 [OSS-HDFS 服务 Endpoint 配置](/docs/user/4.x/4.6.x/4.6.8/jindofs/configuration/jindosdk_endpoint_configuration.md)。。

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
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.6.x/4.6.8/jindofsx/deploy/deploy_raft_ns.md)

## 挂载 OSS-HDFS 服务

* 挂载 OSS-HDFS 服务目录

```
jindo fsxadmin -mount <path> <realpath>
```

例：

```
jindo fsxadmin -mount /jindodls oss://<Bucket>.<Endpoint>/
```

执行如上命令后，则/jindodls 目录下真正挂载的文件路径是 `oss://<Bucket>.<Endpoint>/`

```shell
[root@emr-header-1 ~]# hdfs dfs -ls jindo://emr-header-1:8101/
Found 1 items
----------   1          0 1970-01-01 08:00 jindo://emr-header-1:8101/jindodls
```
即访问`jindo://emr-header-1:8101/jindodls/`等价于访问`oss://<Bucket>.<Endpoint>/`

## 配置缓存加速
可以根据需要配置元数据缓存及数据缓存，配置方式参考文档 [JindoFSx 缓存使用说明](../jindofsx_cache.md)

## 访问 OSS-HDFS 服务
完成上述步骤后作业在数据缓存开关打开时通过`jindo://`前缀读取 OSS-HDFS 服务上的数据后，会自动缓存到 JindoFSx 存储加速系统中，后续通过`jindo://`访问相同的数据就能够命中缓存。

## 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](configuration/jindosdk_configuration_list.md)