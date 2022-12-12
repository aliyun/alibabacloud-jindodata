# JindoFSx P2P分布式下载配置指南

JindoFSx客户端P2P可以被视作一种本地缓存（LocalCache）。与原有的LocalCache相比，P2P缓存中的本地数据块会优先从其他持有该数据的客户端拉取，只有无法向其他客户端请求时，才会从STS或远端读取。本文为您介绍P2P分布式下载缓存的使用方法。

## 前提条件

* EMR 环境

   已在E-MapReduce上创建EMR-3.42.0及后续版本，EMR-5.6.0及后续版本的集群，且勾选 **JINDODATA** 服务，具体操作请参见[创建集群](https://help.aliyun.com/document_detail/28088.htm#concept-olg-vq3-y2b)。


* 非 EMR 环境

   请参考 [JindoFSx 缓存加速系统使用指南 - 非EMR环境](docs/../../../../4.x/4.6.x/4.6.2/jindofsx/outline.md)

**说明** 本文以EMR-3.42.0版本为例介绍。


## 操作流程

1.  步骤一：配置服务端
    
2.  步骤二：配置JindoSDK

3.  步骤三：使用方法

## 步骤一：配置服务端

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
        |  服务端配置  |  jindofsx.p2p.tracker.thread.number  |  P2P 协调节点的处理线程数，通常设置为 1，如果客户端数量超过 1000 可以考虑更大的值。小于 1 则关闭 P2P 功能。  |
        | 服务端配置 |  jindofsx.p2p.file.prefix  |  使用P2P下载的前缀列表。当包含多个文件路径时，使用半角逗号（,）隔开，文件路径只有匹配到其中任一个前缀，才会以P2P方式下载。在应用层使用统一挂载路径进行下载时，此处仍应配置为真实的对象路径。 例如，oss://bucket1/data-dir1/,oss://bucket2/data-dir2/。  |
        |  客户端配置  |  fs.jindofsx.p2p.cache.capacity.limit  |  P2P下载在客户端侧占用的内存缓存大小限制，单位为字节，默认为5 GB，最小值为1 GB。 |
        | 客户端配置 |  fs.jindofsx.p2p.download.parallelism.per.file  |  P2P下载单个文件使用的并发数。例如，取值为5。  |
        | 客户端配置 |  fs.jindofsx.p2p.download.thread.pool.size  |  P2P下载使用的线程池总大小。例如，取值为5。  |
        
    3.  单击**确定**。
        
    4.  在**确认修改配置**对话框中，输入执行原因，单击**确定**。
    
3.  重启服务。
    
    1.  在JindoData服务页面，选择**更多操作 > 重启**。
    
    2.  在**重启JindoData服务**对话框中，输入执行原因，单击**确定**。
    
    3.  在**确认**对话框中，单击**确定**。

## 步骤二：配置JindoSDK

**注意** 此配置为客户端配置，无需重启JindoData服务。

1.  进入配置页面。
    
    1.  进入HDFS服务页面。
        
        1.  [登录EMR on ECS控制台](https://emr-next.console.aliyun.com/#/region/cn-hangzhou/resource/all/ecs/list)。
            
        2.  在顶部菜单栏处，根据实际情况选择地域和资源组。
            
        3.  单击上方的**集群管理**页签。
            
        4.  在**集群管理**页面，单击相应集群所在行的**详情**。
            
        5.  在左侧导航栏，选择**集群服务 > Hadoop-Common**。
            
    2.  在**Hadoop-Common**服务页面，单击**配置**页签。
        
    3.  在**服务配置**区域，单击**core-site**页签。
        
2.  修改以下配置项。
    
    修改配置项的具体操作，请参见[修改组件参数](https://help.aliyun.com/document_detail/106171.htm#section-onk-ip0-00r)。

    |  内容  |  参数  |  描述  |
    | --- | --- | --- |
    |  配置OSS实现类  |  fs.AbstractFileSystem.oss.impl  |  固定值为com.aliyun.jindodata.oss.OSS  |
    | 配置OSS实现类 |  fs.oss.impl  |  固定值为com.aliyun.jindodata.oss.JindoOssFileSystem  |
    |  配置xenging类型|  fs.xengine  |  固定值为jindofsx  |
    |  配置JindoFSx Namespace服务地址  |  fs.jindofsx.namespace.rpc.address  |  格式为${headerhost}:8101 <br/>例如，master-1-1:8101<br/> 说明: 如果使用高可用NameSpace，配置详情请参见[高可用JindoFSx Namespace配置和使用](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_raft_ns.md)  |
    |  启用缓存加速功能  |  fs.jindofsx.data.cache.enable  |  数据缓存开关：<br/> false（默认值）：禁用数据缓存。 <br/>true：启用数据缓存。<br/> 说明: 启用缓存会利用本地磁盘对访问的热数据块进行缓存，默认状态为false，即可以直接访问OSS/OSS-HDFS上的数据  |

3.  保存配置。
    
    1.  单击**服务配置**区域的**保存**。
        
    2.  在**确认修改**对话框中，输入执行原因，开启**自动更新配置**，单击**确定**。

## 步骤三：使用方法

按上述步骤配置之后，根据 jindofsx.p2p.file.prefix 配置项，所有匹配的读请求都会进入 P2P 功能，无需调用额外的接口。
例如，使用 Hadoop shell 命令 get 文件到本地，如果文件的路径符合前缀匹配，则会自动启用 P2P 下载模式。

如果您希望进一步验证对某个特定文件的读请求是否使用了 P2P 功能，您可以考虑查询日志进行验证。
如果您的程序对于客户端打印过 INFO 级别的日志，那么 P2P 读将产生下列字样的记录：
```
P2P record for path:
```
如果您找到了这样的记录，就可以确定对该文件的读请求使用了 P2P 功能。
