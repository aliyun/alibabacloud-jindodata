# 统一命名空间缓存加速配置指南

JindoFSx 提供统一命名空间挂载功能，为应用程序提供统一命名空间（jindo:///）。 通过统一命名空间的抽象，应用程序可以通过统一命名空间和接口来访问多个独立的存储系统。 与其与每个独立的存储系统进行通信，应用程序可以只连接到 JindoFSx 并委托 JindoFSx 来与不同的底层存储通信。

![image](https://alidocs.oss-accelerate.aliyuncs.com/res/YpLdn52Qe66Wqo83/img/aeb54bbe-ae15-4278-bbc4-67008d673eaf.png)

## 前提条件
* EMR 环境

   已在E-MapReduce上创建EMR-3.42.0及后续版本，EMR-5.6.0及后续版本的集群，且勾选 **JINDODATA** 服务，具体操作请参见[创建集群](https://help.aliyun.com/document_detail/28088.htm#concept-olg-vq3-y2b)。

   *   如果数据源为 OSS/OSS-HDFS/Apache HDFS  请先完成如下文档配置要求：

       [OSS/OSS-HDFS 透明缓存加速配置指南](./jindo_cache_oss_hdfs_tutorial.md)

       [Apache HDFS 透明缓存加速配置指南](./jindo_cache_hdfs_tutorial.md)

   *   如果数据源是阿里云 NAS 则需要保证挂载在各个节点的相同路径下（NS 和 STS 服务所在节点）

* 非 EMR 环境

   请参考 [JindoFSx 缓存加速系统使用指南 - 非EMR环境](docs/../../../../4.x/4.6.x/4.6.2/jindofsx/outline.md)

**说明** 本文以EMR-3.42.0版本为例介绍。

## 通过 jindo 命令挂载

您可以使用如下命令进行各种数据源的挂载

*   语法

```shell
jindo admin -mount <path> <realpath>
```

*   挂载 OSS / OSS-HDFS 路径
    
```shell
jindo admin -mount /jindooss oss://<yourBucketName>/<dir>
```

执行以下命令

```shell
hdfs dfs -ls jindo://emr-header-1:8101/jindooss
```

即访问
```shell
jindo://emr-header-1:8101/jindooss/
```

等价于访问`oss://<yourBucketName>/<dir>`

*   挂载 HDFS 路径
```shell    
jindo admin -mount /hdfs hdfs://<ip>:<port>/dir
```

执行以下命令，返回信息为`jindo://emr-header-1:8101/hdfs`。

```shell
hdfs dfs -ls jindo://emr-header-1:8101/hdfs
```

即访问`jindo://emr-header-1:8101/hdfs/`等价于访问 `hdfs://<ip>:<port>/dir`

*   挂载阿里云 NAS 路径
```shell
jindo admin -mount /nas local:///mnt/nas
```
其中 `/mnt/nas` 为阿里云 NAS 在物理机上的挂载点路径，这里需要加上 `local:///` 前缀。

执行以下命令，返回信息为`jindo://emr-header-1:8101/nas`。
```shell
hdfs dfs -ls jindo://emr-header-1:8101/nas
````
即访问 `jindo://emr-header-1:8101/nas/` 等价于访问 `/mnt/nas` 路径下的文件。
