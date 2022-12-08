# JindoFSx P2P分布式下载配置指南

JindoFSx客户端P2P可以被视作一种本地缓存（LocalCache）。与原有的LocalCache相比，P2P缓存中的本地数据块会优先从其他持有该数据的客户端拉取，只有无法向其他客户端请求时，才会从STS或远端读取。本文为您介绍P2P分布式下载缓存的使用方法。

## 前提条件

* EMR 环境

   已在E-MapReduce上创建EMR-3.42.0及后续版本，EMR-5.6.0及后续版本的集群，且勾选 **JINDODATA** 服务，具体操作请参见[创建集群](https://help.aliyun.com/document_detail/28088.htm#concept-olg-vq3-y2b)。


* 非 EMR 环境

   请参考 [JindoFSx 缓存加速系统使用指南 - 非EMR环境](docs/../../../../4.x/4.6.x/4.6.2/jindofsx/outline.md)

**说明** 本文以EMR-3.42.0版本为例介绍。


## 操作流程

1.  进入JindoData服务的**common**页签。
    
    1.  [登录EMR on ECS控制台](https://emr-next.console.aliyun.com/#/region/cn-hangzhou/resource/all/ecs/list)。
        
    2.  在顶部菜单栏处，根据实际情况选择地域和资源组。
        
    3.  在**集群管理**页面，单击目标集群操作列的**集群服务**。
        
    4.  单击JindoData服务区域的**配置**。
        
    5.  在JindoData服务的**服务配置**区域，单击**common**页签。
        
2.  新增配置。
    
    1.  单击**自定义配置**。
        
    2.  在**新增配置项**对话框中，新增以下配置项。
        

新增配置项的具体操作，请参见[添加配置项](https://help.aliyun.com/document_detail/379879.htm#section-st3-dhw-qnx)。

|  配置项  |  参数  |  描述  |
| --- | --- | --- |
|  服务端配置  |  jindofsx.p2p.tracker.thread.number  |  TrackerService的处理线程数。 如果要开启P2P功能，则该参数值必须设置大于1。如果小于等于1，则不会创建TrackerService，也不会开启P2P功能。  |
| 服务端配置 |  jindofsx.p2p.file.prefix  |  使用P2P下载的前缀列表。当包含多个文件路径时，使用半角逗号（,）隔开，文件路径只有匹配到其中任一个前缀，才会以P2P方式下载。在应用层使用统一挂载路径进行下载时，此处仍应配置为真实的对象路径。 例如，oss://bucket1/data-dir1/,oss://bucket2/data-dir2/。  |
|  客户端配置  |  fs.jindofsx.p2p.cache.capacity.limit  |  P2P下载最大占用的缓存大小，单位为字节，默认为5 GB，最小值为1 GB。 例如，取值为 5 \* 1024 \* 1024 \* 1024。  |
| 客户端配置 |  fs.jindofsx.p2p.download.parallelism.per.file  |  P2P下载单个文件使用的并发数。 例如，取值为5。  |
| 客户端配置 |  fs.jindofsx.p2p.download.thread.pool.size  |  P2P下载使用的线程池总大小。 例如，取值为5。  |

3.  单击**确定**。
    
4.  在**确认修改配置**对话框中，输入执行原因，单击**确定**。
    
5.  重启服务。
    
6.  在JindoData服务页面，选择**更多操作 > 重启**。
    
7.  在**重启JindoData服务**对话框中，输入执行原因，单击**确定**。
    
8.  在**确认**对话框中，单击**确定**。