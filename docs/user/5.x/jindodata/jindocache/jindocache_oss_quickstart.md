# OSS 透明缓存加速配置指南

JindoFSx存储加速系统提供了透明缓存的使用方式，兼容原生OSS/OSS-HDFS存储方式，文件以对象的形式存储在OSS/OSS-HDFS上，每个文件根据实际访问情况会在本地进行缓存，提升访问OSS/OSS-HDFS的效率，同时兼容了原有OSS/OSS-HDFS文件形式，数据访问上能够与其他OSS/OSS-HDFS客户端完全兼容，作业访问OSS/OSS-HDFS的方式无需做任何修改。

## 前提条件

* EMR 环境

   已在E-MapReduce上创建EMR-3.42.0及后续版本，EMR-5.6.0及后续版本的集群，且勾选 **JINDODATA** 服务，具体操作请参见[创建集群](https://help.aliyun.com/document_detail/28088.htm#concept-olg-vq3-y2b)。


* 非 EMR 环境

   请参考 [JindoFSx 缓存加速系统使用指南 - 非EMR环境](docs/../../../../4.x/4.6.x/4.6.2/jindofsx/outline.md)

**说明** 本文以EMR-3.42.0版本为例介绍。

## 使用限制

该功能适用的集群版本：EMR-3.40.0及后续版本、EMR-5.6.0及后续版本。

## 操作流程

1.  步骤一：配置AccessKey
    
2.  步骤二：配置JindoSDK
    
3.  步骤三：磁盘空间水位控制
    

## 步骤一：配置AccessKey

1.  进入JindoData服务的**common**页签。
    
    1.  [登录EMR on ECS控制台](https://emr-next.console.aliyun.com/#/region/cn-hangzhou/resource/all/ecs/list)。
        
    2.  在顶部菜单栏处，根据实际情况选择地域和资源组。
        
    3.  在**集群管理**页面，单击目标集群操作列的**集群服务**。
        
    4.  单击JindoData服务区域的**配置**。
        
    5.  单击**common**页签。
        
2.  新增配置。
    
    1.  单击**新增配置项**。
        
    2.  在**新增配置项**对话框中，新增以下配置项。
        

新增配置项的具体操作，请参见[添加配置项](https://help.aliyun.com/document_detail/379879.htm#section-st3-dhw-qnx)。全局方式和按照Bucket方式配置可**任选其中一种**即可。

*   全局方式配置（所有Bucket使用同一种方式）
    

|  参数  |  描述  |
| --- | --- |
|  jindofsx.oss.accessKeyId  |  OSS/OSS-HDFS 的 AccessKey ID  |
|  jindofsx.oss.accessKeySecret  |  OSS/OSS-HDFS 的 AccessKey Secret  |
|  jindofsx.oss.endpoint  |  OSS/OSS-HDFS的Endpoint。如： <br/>OSS: oss-cn-\*\*\*-internal.aliyuncs.com<br/> OSS-HDFS:   cn-\*\*\*.oss-dls.aliyuncs.com  |

*   按照Bucket配置
    

|  参数  |  描述  |
| --- | --- |
|  jindofsx.oss.bucket.XXX.accessKeyId  |  XXX的Bucket的AccessKey ID  |
|  jindofsx.oss.bucket.XXX.accessKeySecret  |  XXX的Bucket的AccessKey Secret  |
|  jindofsx.oss.bucket.XXX.endpoint  |  XXX的Bucket的Endpoint。如：<br/> OSS: oss-cn-\*\*\*-internal.aliyuncs.com<br/> OSS-HDFS: cn-\*\*\*.oss-dls.aliyuncs.com  |

**说明:** XXX为OSS/OSS-HDFS Bucket的名称。

1.  单击**确定**。
    
2.  重启服务。
    
3.  在JindoData服务页面，选择右上角的**操作 > 重启All Components**。
    
4.  在**执行集群操作**对话框中，输入执行原因（其他参数保持默认），单击**确定**。
    
5.  在**确认**对话框中，单击**确定**。
    

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
        
4.  在**core-site**页签，新增配置项。
    
    1.  单击**服务配置**区域的**自定义配置**。
        
    2.  在**新增配置项**对话框中，新增以下配置项。
        

新增配置项的具体操作，请参见[添加组件参数](https://help.aliyun.com/document_detail/106171.htm#section-f5l-r4w-mm2)。

|  内容  |  参数  |  描述  |
| --- | --- | --- |
| 配置AccesskeyID（必填）  |  fs.oss.accessKeyId  |  OSS/OSS-HDFS 的 AccessKeyID  |
| 配置AccesskeySecret（必填）  |  fs.oss.accessKeySecret  |  OSS/OSS-HDFS 的 AccessKeySecret  |
| 配置Endpoint（必填）  |  fs.oss.endpoint  |  OSS/OSS-HDFS 的 Endpoint。如：<br/> OSS: oss-cn-\*\*\*-internal.aliyuncs.com <br/>OSS-HDFS:  cn-\*\*\*.oss-dls.aliyuncs.com  |
|  元缓存加速功能（可选）  |  fs.jindofsx.meta.cache.enable  |  元数据缓存开关： <br/>false（默认值）：禁用元数据缓存<br/> true：启用元数据缓存  |
| 小文件缓存加速功能（可选） |  fs.jindofsx.slice.cache.enable  |  小文件缓存优化开关：<br/> false（默认值）：禁用小文件缓存 <br/>true：启用小文件缓存  |
|  短路读功能（可选）|  fs.jindofsx.short.circuit.enable  |  短路读开关：<br/> true（默认值）：打开短路读开关 <br/>false：关闭短路读开关  |

5.  单击**确定**。
    

## 步骤三：磁盘空间水位控制

缓存启用后，JindoFSx服务会自动管理本地缓存备份，通过水位清理本地缓存，请您根据需求配置一定的比例用于缓存。JindoFSx后端基于OSS/OSS-HDFS，可以提供海量的存储，但是本地盘的容量是有限的，因此JindoFSx会自动淘汰本地较冷的数据备份。您可以通过修改 **storage.watermark.high.ratio** 和 **storage.watermark.low.ratio** 两个参数来调节本地存储的使用容量，值均为0～1的小数，表示使用磁盘空间的比例。

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
        

## 相关文档

JindoSDK包含一些高级调优参数，配置方式以及配置项的详细信息，请参见[JindoSDK高级参数配置](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.3.0/jindofsx/configuration/jindosdk_configuration_list.md)。