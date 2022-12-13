# JindoFSx 高可用服务配置指南

## 前提条件

* EMR 环境

   已在E-MapReduce上创建EMR-3.42.0及后续版本，EMR-5.6.0及后续版本的集群，且勾选 **JINDODATA** 服务和**高可用集群模式**，具体操作请参见[创建集群](https://help.aliyun.com/document_detail/28088.htm#concept-olg-vq3-y2b)。

   * 如果数据源为 OSS/OSS-HDFS/Apache HDFS/NAS  请先完成如下文档配置要求：
    
     [OSS/OSS-HDFS 透明缓存加速配置指南](./jindocache_oss_quickstart.md)

     [Apache HDFS 透明缓存加速配置指南](./jindocache_hdfs_quickstart.md)

     [统一命名空间缓存加速配置指南](./jindocache_global_namespace_quickstart.md)


* 非 EMR 环境

   请参考 [JindoFSx 缓存加速系统使用指南 - 非EMR环境](docs/../../../../4.x/4.6.x/4.6.2/jindofsx/outline.md)

**说明** 本文以EMR-3.42.0版本为例介绍。

## 使用限制

该功能适用的集群版本：EMR-3.40.0及后续版本、EMR-5.6.0及后续版本。

## 操作流程

1.  步骤一：配置JindoData
    
2.  步骤二：配置JindoSDK
    

## 步骤一：配置JindoData

1.  进入JindoData服务的**namespace**页签。
    
    1.  [登录EMR on ECS控制台](https://emr-next.console.aliyun.com/#/region/cn-hangzhou/resource/all/ecs/list)。
        
    2.  在顶部菜单栏处，根据实际情况选择地域和资源组。
        
    3.  在**集群管理**页面，单击目标集群操作列的**集群服务**。
        
    4.  单击JindoData服务区域的**配置**。
        
    5.  单击**namespace**页签。
        
2.  新增配置。
    
    1.  单击**新增配置项**。
        
    2.  在**新增配置项**对话框中，新增以下配置项。
        
        新增配置项的具体操作，请参见[添加配置项](https://help.aliyun.com/document_detail/379879.htm#section-st3-dhw-qnx)。全局方式和按照Bucket方式配置可**任选其中一种**即可。
        
        *   全局方式配置（所有Bucket使用同一种方式）
        
        |  参数  |  描述  |
        | --- | --- |
        |  namespace.backend.type  |  固定值为：raft <br>表示开启三个master高可用模式  |
        |  namespace.backend.raft.initial-conf  |  Raft服务的初始化地址，格式为：{hostname-1}:{port}:0,{hostname-2}:{port}:0,{hostname-3}:{port}:0 <br>如：master-1-1:8103:0,master-1-2:8103:0,master-1-3:8103:0  |
    
    3.  单击**确定**。
    
3.  重启服务。
    
    1.  在JindoData服务页面，选择右上角的**操作 > 重启All Components**。
    
    2.  在**执行集群操作**对话框中，输入执行原因（其他参数保持默认），单击**确定**。
    
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
    
    修改配置项的具体操作，请参见[修改组件参数](https://help.aliyun.com/document_detail/106171.htm#section-onk-ip0-00r)。(必填)

    |  内容  |  参数  |  描述  |
    | --- | --- | --- |
    |  配置JindoFSx Namespace服务地址  |  fs.jindofsx.namespace.rpc.address  |  格式为:{headerhost-1}:8101,{headerhost-2}:8101,{headerhost-3}:8101 <br>如：master-1-1:8101,master-1-2:8101,master-1-3:8101|

3.  保存配置。
    
    1.  单击**服务配置**区域的**保存**。
        
    2.  在**确认修改**对话框中，输入执行原因，开启**自动更新配置**，单击**确定**。