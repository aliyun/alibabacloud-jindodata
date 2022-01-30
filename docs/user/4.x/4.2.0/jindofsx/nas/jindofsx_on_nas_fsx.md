# 阿里云文件存储 NAS 统一挂载缓存加速
阿里云文件存储NAS（Apsara File Storage NAS）是一个可大规模共享访问，弹性扩展的高性能云原生分布式文件系统。支持智能冷热数据分层，有效降低数据存储成本。本文主要介绍 JindoFSx 支持阿里云文件存储 NAS 统一挂载缓存加速的使用方式。

## 前提条件：
* 集群已挂载 NAS 文件系统

挂载步骤请参考 [Linux系统挂载NFS文件系统](https://help.aliyun.com/document_detail/90529.htm?spm=a2c4g.11186623.0.0.763d4c93XQH1Zc#table-bcw-ioo-ery)

* 已部署 JindoFSx 缓存系统

关于如何部署 JindoFSx 缓存系统，请参考 [部署 JindoFSx（缓存系统)](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindosdk.md)

## 配置 JindoSDK

* 配置统一名字空间使用的实现类

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

* 启用缓存加速功能

启用缓存会利用本地磁盘对访问的热数据块进行缓存，默认状态为禁用，即所有OSS读取都直接访问OSS上的数据。

可根据情况将以下配置添加到 Hadoop 的`core-site.xml`中。
```xml
<configuration>

    <property>
        <!-- 数据缓存开关 -->
        <name>fs.fsx.data.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 元数据缓存开关 -->
        <name>fs.fsx.meta.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 小文件缓存优化开关 -->
        <name>fs.fsx.slice.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 内存缓存开关 -->
        <name>fs.fsx.ram.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 短路读开关 -->
        <name>fs.fsx.short.circuit.enable</name>
        <value>true</value>
    </property>

</configuration>
```

完成以上配置后，作业读取 NAS 上的数据后，会自动缓存到 JindoFSx 缓存系统中，后续访问相同的数据就能够命中缓存。
注意：此配置为客户端配置，不需要重启 JindoFSx 服务。

## 挂载 NAS 文件系统目录

```
jindo fsxadmin -mount <path> <realpath>
```

例：

```
jindo fsxadmin -mount /jindonas /mnt/nas
```

假设 NAS 文件系统挂载在服务器上的本地路径为`/mnt/nas`。执行如上命令后，则`/jindonas`目录下真正挂载的文件路径是`/mnt/nas`

```shell
[root@emr-header-1 ~]# hdfs dfs -ls fsx://emr-header-1:8101/
Found 2 items
----------   1          0 1970-01-01 08:00 fsx://emr-header-1:8101/jindonas
```
即访问`fsx://emr-header-1:8101/jindonas/`等价于访问`/mnt/nas/`

## 访问 NAS 文件系统
完成上述步骤后作业通过`fsx://`前缀读取 NAS 上的数据后，会自动缓存到 JindoFSx 缓存系统中，后续通过`fsx://`访问相同的数据就能够命中缓存。