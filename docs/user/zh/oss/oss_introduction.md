# OSS/OSS-HDFS 概述

## 概述

阿里云对象存储OSS（Object Storage Service）是一款海量、安全、低成本、高可靠的云存储服务，可提供99.9999999999%（12个9）的数据持久性，99.995%的数据可用性。多种存储类型供选择，全面优化存储成本。详情请见 [什么是对象存储OSS](https://help.aliyun.com/document_detail/31817.html)。

OSS-HDFS服务（JindoFS服务）是一款云原生数据湖存储产品。基于统一的元数据管理能力，在完全兼容HDFS文件系统接口的同时，提供充分的POSIX能力支持，能更好地满足大数据和AI等领域的数据湖计算场景。详情请见 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.html)。

JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 套件基于统一架构和内核实现，主要包括 JindoFS 存储系统（原 JindoFS Block 模式），JindoFSx 存储加速系统（原 JindoFS Cache 模式），JindoSDK 大数据万能 SDK 和全面兼容的生态工具（JindoFuse、JindoDistCp）、插件支持。详情请见 [《JindoData 概述》](../jindodata/jindodata_introduction.md)。 

## 使用

在 EMR 环境中，已经默认部署 JindoSDK，您可以直接通过 JindoSDK 来访问 OSS/OSS-HDFS。

在 非 EMR 环境中，您也可以通过下载最新版本的 JindoSDK ，自行部署使用，Hadoop 场景部署方式参见 [《在 Hadoop 环境中部署 JindoSDK》](/docs/user/jindosdk/jindosdk_deployment_hadoop.md), AI场景部署方式参见 [《在 AI 环境中部署 JindoSDK》](/docs/user/jindosdk/jindosdk_deployment_ai.md)。
。

## 优点

使用 OSS/OSS-HDFS 作为底层存储有以下优势：

*   即插即用。OSS/OSS-HDFS 都是云原生存储服务，通过Restful API提供服务，本身不需部署。在阿里云 EMR 集群中，已经默认安装 JindoSDK，您可以通过 JindoSDK 直接访问。
    
*   节省成本。使用 OSS/OSS-HDFS 存储数据可以有效节省成本，并结合低频/归档/冷归档等方式，可以进一步优化冷数据的存储成本。
    
*   可扩展性。OSS/OSS-HDFS 有着更好的可扩展性，不受硬盘容量限制，无需人工扩容。
    

## 特性

通过 JindoSDK 使用 OSS/OSS-HDFS 的特性对比如下：

|  场景  |  支持特性  |  OSS  |  OSS-HDFS  |
| --- | --- | --- | --- |
|  大数据场景 （Hadoop）  |  支持目录、文件语义和操作  |  支持  |  支持  |
|  |  添加目录、文件权限  |  不支持  |  支持  |
|  |  目录原子性、rename性能  |  支持，但性能不佳  |  支持，毫秒级  |
|  |  通过 setTimes 设置时间  |  不支持  |  支持  |
|  |  扩展属性 XAttrs  |  不支持  |  支持  |
|  |  ACL  |  不支持  |  支持  |
|  |  本地读缓存加速  |  支持  |  支持  |
|  |  快照 Snapshot  |  不支持  |  支持  |
|  |  文件 append/flush/sync 操作  |  不支持  |  支持  |
|  |  文件 truncate  |  不支持  |  支持  |
|  |  校验和 Checksum  |  支持  |  支持  |
|  |  HDFS回收站自动清理  |  不支持  |  支持  |
|  AI 场景（POSIX）  |  元数据一致性  |  弱  |  强  |
|  |  文件 append/flush/sync 操作  |  支持，但有[使用限制](https://help.aliyun.com/document_detail/31981.html)  |  支持  |
|  |  文件 truncate 操作  |  不支持  |  支持  |
|  |  随机写  |  不支持  |  支持  |