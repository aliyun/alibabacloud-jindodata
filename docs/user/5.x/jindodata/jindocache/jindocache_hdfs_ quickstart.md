# Apache HDFS 透明缓存加速配置指南

Apache HDFS透明缓存加速可以利用计算集群的闲置存储资源对远端HDFS集群进行数据缓存，避免了计算集群或服务占用核心集群过多带宽。适用于在HDFS集群和计算集群分离，HDFS集群访问性能不及预期时，您可以通过在计算集群或靠近计算集群的地方缓存数据来进行加速。

## 前提条件

* EMR 环境

   已在E-MapReduce上创建EMR-3.40.0及后续版本，EMR-5.6.0及后续版本的集群，且勾选 **JINDODATA** 服务，具体操作请参见[创建集群](https://help.aliyun.com/document_detail/28088.htm#concept-olg-vq3-y2b)。


* 非 EMR 环境

   请参考 [JindoFSx 缓存加速系统使用指南 - 非EMR环境](docs/../../../../4.x/4.6.x/4.6.2/jindofsx/outline.md)

**说明** 本文以EMR-3.40.0版本为例介绍。


## 操作流程

1.  步骤一：配置服务端
    
2.  步骤二：配置JindoSDK
    
3.  步骤三：访问HDFS
    

## 步骤一：配置服务端

1.  进入新增配置项页面。
    
    1.  进入JindoData服务页面。
        
        1.  登录[阿里云E-MapReduce控制台](https://emr.console.aliyun.com/)。
            
        2.  在顶部菜单栏处，根据实际情况选择地域和资源组。
            
        3.  单击上方的**集群管理**页签。
            
        4.  在**集群管理**页面，单击相应集群所在行的**详情**。
            
        5.  在左侧导航栏，选择**集群服务 > JindoData**。
            
    2.  在JindoData服务页面，单击**配置**页签。
        
    3.  在**服务配置**区域，单击**common**页签。
        
2.  新增配置。
    
    1.  单击**自定义配置**。
        
    2.  在**新增配置项**对话框中，新增以下配置项。
        

新增配置项的具体操作，请参见[添加组件参数](https://help.aliyun.com/document_detail/106171.htm#section-f5l-r4w-mm2)。

**说明** 根据您集群的类型，新增相应的配置项。XXX为集群中配置 hdfs-site.xml 的 **dfs.nameservices** 参数值，如 hdfs-cluster

|  集群类型  |  参数  |  描述  |
| --- | --- | --- |
| 普通集群  |  jindofsx.hdfs.user  |  访问HDFS使用的用户名，如hadoop  |
| HA集群  |  jindofsx.hdfs.XXX.dfs.ha.namenodes  |  说明：hdfs-site.xml中dfs.ha.namenodes.XXX的值  <br/>  如：nn1,nn2,nn3       |
| HA集群 |  jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn1  |  说明：hdfs-site.xml中dfs.namenode.rpc-address.XXX.nn1的值 <br/>  如：master-1-1:8020       |
| HA集群 |  jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn2  |  说明：hdfs-site.xml中dfs.namenode.rpc-address.XXX.nn2的值 <br/> 如：master-1-2:8020       |
| HA集群 |  jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn3  |  说明：hdfs-site.xml中dfs.namenode.rpc-address.XXX.nn3的值 <br/>  如：master-1-3:8020       |

3.  单击**确定**。
    
4.  重启服务。
    
5.  在JindoData服务页面，选择右上角的**操作 > 重启All Components**。
    
6.  在**执行集群操作**对话框中，输入执行原因（其他参数保持默认），单击**确定**。
    
7.  在**确认**对话框中，单击**确定**。
    

## 步骤二：配置JindoSDK

**注意** 此配置为客户端配置，无需重启JindoData服务。

1.  进入配置页面。
    
    1.  进入HDFS服务页面。
        
        1.  [登录EMR on ECS控制台](https://emr-next.console.aliyun.com/#/region/cn-hangzhou/resource/all/ecs/list)。
            
        2.  在顶部菜单栏处，根据实际情况选择地域和资源组。
            
        3.  单击上方的**集群管理**页签。
            
        4.  在**集群管理**页面，单击相应集群所在行的**详情**。
            
        5.  在左侧导航栏，选择**集群服务 > Hadoop-Common**。
            
    2.  在 **Hadoop-Common** 服务页面，单击**配置**页签。
        
    3.  在**服务配置**区域，单击**core-site**页签。
        
2.  新增和修改配置项。
    

新增配置项的具体操作，请参见[添加组件参数](https://help.aliyun.com/document_detail/106171.htm#section-f5l-r4w-mm2)。修改配置项的具体操作，请参见[修改组件参数](https://help.aliyun.com/document_detail/106171.htm#section-onk-ip0-00r)。

|  内容  |  参数  |  描述  |
| --- | --- | --- |
|  配置统一名字空间使用的实现类（必选）  |  fs.hdfs.impl  |  固定值为com.aliyun.jindodata.hdfs.JindoHdfsFileSystem。  |
| 配置统一名字空间使用的实现类（必选） |  fs.AbstractFileSystem.hdfs.impl  |  固定值为com.aliyun.jindodata.hdfs.HDFS。  |
| 配置xenging类型（必选） |  fs.xengine  |  固定值为jindofsx。  |
| 配置HA Namenodes<br/> 说明：如果为HA集群，则需要配置该类参数。 （可选） |  fs.jindofsx.hdfs.XXX.dfs.ha.namenodes  |  说明：hdfs-site.xml中dfs.ha.namenodes.XXX的值  <br/> 如：nn1,nn2,nn3       |
| 配置HA Namenodes<br/> 说明： 如果为HA集群，则需要配置该类参数。 （可选） |  fs.jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn1  |  说明：hdfs-site.xml中dfs.namenode.rpc-address.XXX.nn1的值 <br/>   如：master-1-1:8020       |
| 配置HA Namenodes<br/> 说明：如果为HA集群，则需要配置该类参数。 （可选） |  fs.jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn2  |  说明：hdfs-site.xml中dfs.namenode.rpc-address.XXX.nn2的值 <br/>   如：master-1-2:8020       |
| 配置HA Namenodes<br/> 说明：如果为HA集群，则需要配置该类参数。 （可选） |  fs.jindofsx.hdfs.XXX.dfs.namenode.rpc-address.nn3  |  说明：hdfs-site.xml中dfs.namenode.rpc-address.XXX.nn3的值 <br/>   如：master-1-3:8020       |
|  配置JindoFSx Namespace服务地址（必选）  |  fs.jindofsx.namespace.rpc.address  |  格式为${headerhost}:8101。<br/> 例如，master-1-1:8101。<br/> 说明: 如果使用高可用NameSpace，配置详情请参见[高可用JindoFSx Namespace配置和使用](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_raft_ns.md)。  |
|  开启缓存加速（必选）   |  fs.jindofsx.data.cache.enable  |  数据缓存开关：<br> false（默认值）：禁用数据缓存。 <br/> true：启用数据缓存。 |
| 开启元数据缓存 （可选）|  fs.jindofsx.meta.cache.enable  |  元数据缓存开关：<br/> false（默认值）：禁用元数据缓存。<br/> true：启用元数据缓存。 |
| 开启小文件优化缓存（可选） |  fs.jindofsx.slice.cache.enable |  小文件缓存优化开关：<br/> false（默认值）：禁用小文件缓存。<br/> true：启用小文件缓存。  |
| 开启短路读（可选） |  fs.jindofsx.short.circuit.enable  |  短路读开关：<br/> true（默认值）：打开。 <br/>false：关闭。  |

## 相关文档

JindoSDK包含一些高级调优参数，配置方式以及配置项的详细信息，请参见[JindoSDK高级参数配置](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.3.0/jindofsx/configuration/jindosdk_configuration_list.md)。

## 步骤三：磁盘空间水位控制

缓存启用后，JindoFSx服务会自动管理本地缓存备份，通过水位清理本地缓存，请您根据需求配置一定的比例用于缓存。JindoFSx后端基于HDFS，可以提供海量的存储，但是本地盘的容量是有限的，因此JindoFSx会自动淘汰本地较冷的数据备份。您可以通过修改 **storage.watermark.high.ratio** 和 **storage.watermark.low.ratio** 两个参数来调节本地存储的使用容量，值均为0～1的小数，表示使用磁盘空间的比例。

1.  修改磁盘水位配置。
    

在JindoData服务**配置**页面的**服务配置**区域，单击**storage**页签，修改以下参数。![image](https://alidocs.oss-accelerate.aliyuncs.com/res/E8K4nyLyAWeYlLbj/img/d5e36678-8bdb-4e1a-94c6-ebb57b20ae40.png)

|  参数  |  描述  |
| --- | --- |
|  storage.watermark.low.ratio  |  表示使用量的下水位比例，触发清理后会自动清理冷数据，将缓存数据目录占用空间清理到下水位。默认值：0.2  |
|  storage.watermark.high.ratio  |  表示磁盘使用量的上水位比例，每块数据盘的缓存数据目录占用的磁盘空间到达上水位即会触发清理。默认值：0.4  |

**说明** 修改该参数时，下水位必须小于上水位，设置合理的值即可。

2.  保存配置。
    
    1.  单击**服务配置**区域的**保存**。
        
    2.  在**确认修改**对话框中，输入执行原因，开启**自动更新配置**，单击**确定**。
        
3.  重启服务。
    
    1.  在JindoData服务页面，选择右上角的**操作 > 重启All Components**。
        
    2.  在**执行集群操作**对话框中，输入执行原因（其他参数保持默认），单击**确定**。
        
    3.  在**确认**对话框中，单击**确定**。
        

## 步骤四：访问HDFS

您通过hdfs://前缀读取HDFS上的数据后，在数据缓存开关打开时，会自动缓存到JindoFSx存储加速系统中，后续通过hdfs://访问相同的数据就能够命中缓存。