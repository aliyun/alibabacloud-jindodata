# Apache HDFS 统一挂载缓存加速

当计算集群使用 Apache HDFS 存储，并且与使用的 HDFS 存储集群分离时，访问 HDFS 集群的网络带宽可能受到限制。Apache HDFS 统一挂载缓存加速可以利用计算集群的闲置存储资源进行数据缓存来加速计算服务，避免了计算集群/服务占用核心集群过多带宽。计算集群或服务使用统一挂载的方式 (jindo://) 访问原 HDFS 集群，同时通过缓存加速访问速度。

## 前提条件：
* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.5.x/4.5.0/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.5.x/4.5.0/jindofsx/deploy/deploy_jindosdk.md)

## 服务端配置
可根据情况在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg。

* 配置 HDFS 集群的缓存加速

```
[jindofsx-common]
jindofsx.hdfs.user = xxx #Storage Service 访问 HDFS 使用的用户名
```

* 配置 HA HDFS 集群的缓存加速（可选）

```
[jindofsx-common]
jindofsx.hdfs.XXX.dfs.ha.namenodes = nn1,nn2
jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn1 = nn1-host1:nn1-rpc-port
jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn2 = nn2-host1:nn2-rpc-port
```
说明: XXX 为 HDFS 集群中配置的`dfs.nameservices`参数值。

并将配置文件更新到所有服务所在节点上。

## 重启 JindoFSx 服务

重启 JindoFSx 服务，使得配置生效。在 master 节点执行以下脚本。
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

* 配置 HA Namenodes (可选)

若为 HA HDFS集群，则将下面参数配置到 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.jindofsx.hdfs.XXX.dfs.ha.namenodes</name>
        <value>nn1,nn2</value>
    </property>

    <property>
        <name>fs.jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn1</name>
        <value>nn1-host1:nn1-rpc-port</value>
    </property>

    <property>
        <name>fs.jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn2</name>
        <value>nn1-host1:nn1-rpc-port</value>
    </property>
</configuration>
```
说明: XXX 为 HDFS 集群中配置的`dfs.nameservices`参数值。

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
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.5.x/4.5.0/jindofsx/deploy/deploy_raft_ns.md)

## 挂载 HDFS

* 挂载 HDFS 集群目录

```
jindo fsxadmin -mount <path> <realpath>
```

例：

```
jindo fsxadmin -mount /jindohdfs hdfs://emr-header-1:9000/
```

执行如上命令后，则`/jindohdfs`目录下真正挂载的文件路径是`hdfs://emr-header-1:9000/`

```shell
[root@emr-header-1 ~]# hdfs dfs -ls jindo://emr-header-1:8101/
Found 2 items
----------   1          0 1970-01-01 08:00 jindo://emr-header-1:8101/jindohdfs
```
即访问`jindo://emr-header-1:8101/jindohdfs/`等价于访问`hdfs://emr-header-1:9000/`

## 配置缓存加速
可以根据需要配置元数据缓存及数据缓存，配置方式参考文档 [JindoFSx 缓存使用说明](../jindofsx_cache.md)

## 访问 HDFS
完成上述步骤后作业在数据缓存开关打开时通过`jindo://`前缀读取 HDFS 上的数据后，会自动缓存到 JindoFSx 存储加速系统中，后续通过`jindo://`访问相同的数据就能够命中缓存。

