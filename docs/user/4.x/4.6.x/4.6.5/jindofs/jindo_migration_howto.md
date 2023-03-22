# JindoFS 迁移服务
*(从 4.6.5 开始支持)*

JindoData 迁移服务是新推出的重磅功能，用户支持线下HDFS 或者JindoFS 半托管集群数据迁移到全托管的OSS-HDFS服务。 使用JindoData 迁移服务后，用户无需关系数据如何从线下HDFS 集群或者JindoFS 半托管集群迁移到全托管的OSS-HDFS，用户可以通过极少的操作就可以完成数据的迁移。极大的简化用户上云以及架构演进的迁移流程。在JindoData 4.6.4 版本中支持HDFS 集群无缝迁移到OSS-HDFS集群中，后续陆续支持半托管JindoFS集群 到 OSS-HDFS服务的迁移，下面主要介绍HDFS 到 OSS-HDFS服务迁移操作步骤。

## HDFS集群到OSS-HDFS服务

### 准备阶段
创建EMR 迁移服务集群以及需要迁移的的OSS-HDFS桶，修改迁移服务相关配置，主要包括迁移服务的Namespace服务以及Storage服务相关配置的修改，具体如下：

```
[namespace]
# 代理 HDFS 集群地址
namespace.proxy.fs.defaultFS = hdfs://${HDFS_CLUSTER}/

# 代理OSS-HDFS 相关配置
namespace.proxy.oss-hdfs.bucket = oss://${OSS_HDFS_BUCKET}/
namespace.proxy.oss-hdfs.access.key = ${OSS_HDFS_ACCESS_KEY}
namespace.proxy.oss-hdfs.access.secret = ${OSS_HDFS_ACCESS_SECRET} 
namespace.proxy.oss-hdfs.endpoint = ${OSS_HDFS_ENDPOINT}
```

```
[storage]
# 代理的 OSS-HDFS 相关配置
storage.proxy.oss-hdfs.bucket = oss://${OSS_HDFS_BUCKET}/
storage.proxy.oss-hdfs.access.key = ${OSS_HDFS_ACCESS_KEY}
storage.proxy.oss-hdfs.access.secret = ${OSS_HDFS_ACCESS_SECRET} 
storage.proxy.oss-hdfs.endpoint = ${OSS_HDFS_ACCESS_ENDPOINT} 
```

在该阶段迁移服务处于纯代理HDFS阶段用户可以通过以下命令检查迁移服务状态

```
jindo-util admin -getMnsState -msUri ${MNS_HTTP_ENDPOINT}
```
该命令输出显示迁移服务处于PROXY_SOURCE阶段。

修改所有访问原来HDFS 集群客户端HDFS地址，修改客户端节点fs.defaultFS配置指向迁移服务RPC地址
```
# MNS_RPC_ADDRESS 为迁移服务Namespace 地址，格式hdfs://emr-header-1.cluster-342330:22000， 其中 22000 为迁移服务监听端口
<property>
    <name>fs.defaultFS</name>
    <value>{MNS_RPC_ADDRESS}</value>
  </property>
```

**对于原有使用HDFS hflush/hsync语义的用户，需要使用JindoSDK 替换已有的开源HDFS 服务以便在迁移服务上支持 hflush/hsync语义，替换原有客户端请参考[链接](../../emr_upgrade_jindosdk.md)** 。

### 迁移阶段
该阶段主要负责将已有的数据后台迁移到OSS-HDFS服务中，直到所有数据迁移完成。首先执行迁移开始命令，具体命令如下
```
 jindo-util admin -startMigrate -msUri ${MNS_HTTP_ENDPOINT}
```
在执行完上述命令，用户可以执行以下命令检查当前迁移服务状态，确保迁移服务处于迁移阶段
```
jindo-util admin -getMnsState -msUri ${MNS_HTTP_ENDPOINT}
```
上述命令输出状态为MIGRATION，表示迁移服务已经在迁移数据阶段。

在迁移过程可以通过以下命令来查看整个数据的迁移进度， 直到整个数据迁移完成进入生产阶段

```
jindo-util admin -checkMigrateProgress -msUri ${MNS_HTTP_ENDPOINT}
```
输出如下：

```
The migration progress is finishedDirs/totalDirs: 0/1691 finishedFiles/totalFiles: 0/1580 finishedSize/totalSize: 0/3362206125
```

### 生产阶段

该阶段用户数据已经迁移完毕，所有数据都存在OSS-HDFS集群，用户可以跳过迁移服务通过AccessPolicy直接访问OSS-HDFS集群，具体参考[链接](jindofs/jindofs_dls_root_policy.md) 。