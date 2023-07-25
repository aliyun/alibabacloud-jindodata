# 阿里云文件存储 NAS 统一挂载缓存加速
阿里云文件存储NAS（Apsara File Storage NAS）是一个可大规模共享访问，弹性扩展的高性能云原生分布式文件系统。支持智能冷热数据分层，有效降低数据存储成本。本文主要介绍 JindoFSx 支持阿里云文件存储 NAS 统一挂载缓存加速的使用方式。

## 前提条件：
* 集群已挂载 NAS 文件系统

挂载步骤请参考 [Linux系统挂载NFS文件系统](https://help.aliyun.com/document_detail/90529.htm#table-bcw-ioo-ery)

* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.6.x/4.6.11/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.6.x/4.6.11/jindofsx/deploy/deploy_jindosdk.md)

## 配置 JindoSDK

* 配置统一名字空间使用的实现类

将 JindoFSx 统一名字空间使用的实现类配置到 Hadoop 的`core-site.xml`中。

```xml
<configuration>
    <property>
        <name>fs.jindo.impl</name>
        <value>com.aliyun.jindodata.jindo.JindoFileSystem</value>
    </property>

    <property>
        <name>fs.AbstractFileSystem.jindo.impl</name>
        <value>com.aliyun.jindodata.jindo.JINDO</value>
    </property>

    <property>
        <name>fs.xengine</name>
        <value>jindofsx</value>
    </property>
</configuration>
```

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

## 挂载 NAS 文件系统目录

```shell
jindo admin -mount <path> <realpath>
```

例：

```shell
jindo admin -mount /jindonas local:///mnt/nas
```

假设 NAS 文件系统挂载在服务器上的本地路径为`/mnt/nas`, 在进行挂载时需要加上 `local://` 前缀。执行如上命令后，则`/jindonas`目录下真正挂载的文件路径是`/mnt/nas`

```shell
[root@emr-header-1 ~]# hdfs dfs -ls jindo://emr-header-1:8101/
Found 2 items
----------   1          0 1970-01-01 08:00 jindo://emr-header-1:8101/jindonas
```

即访问`jindo://emr-header-1:8101/jindonas/`等价于访问`/mnt/nas/`

## 配置缓存加速
可以根据需要配置元数据缓存及数据缓存，配置方式参考文档 [JindoFSx 缓存使用说明](../jindofsx_cache.md)

## 访问 NAS 文件系统
完成上述步骤后作业在数据缓存开关打开时通过`jindo://`前缀读取 NAS 上的数据后，会自动缓存到 JindoFSx 存储加速系统中，后续通过`jindo://`访问相同的数据就能够命中缓存。