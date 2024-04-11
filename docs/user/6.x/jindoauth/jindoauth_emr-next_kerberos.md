# 阿里云 OSS 使用 Ranger 的鉴权方案（高安全 EMR 集群）

本文介绍在 E-MapReduce 新版控制台( EMR-5.15.0/EMR-3.49.0 及以上版本集群)配置阿里云 OSS 和 OSS-HDFS 服务使用 Ranger 的鉴权方案。

## 背景

Apache Ranger 提供集中式的权限管理框架，可以对 Hadoop 生态中的多个组件进行细粒度的权限访问控制。当用户将数据存放在阿里云 OSS 时，则是通过在阿里云 RAM 产品创建或管理 RAM 用户，实现对 OSS 资源的访问控制。 为维持大数据客户的使用习惯，通过 JindoAuth 接入 Ranger 的客户端，方便用户统一管理大数据组件权限。

## 前提条件

*   已创建 E-MapReduce EMR-5.15.0/EMR-3.49.0 或以上版本集群，并且选择了 Ranger 和 Ranger-plugin 服务。
    
*   启用 Kerberos 认证满足认证需求。
    
*   若使用集群都是可信用户，则可不启用 Kerberos 认证。
    

注意：EMR 集群开启 Kerberos 后，将无法关闭 Kerberos 认证，请谨慎选择。开启 Kerberos 后，集群所有大数据组件的服务均需要经过 Kerberos 认证，大数据作业提交到集群会先经过身份认证。Kerberos 详情信息，请参见 [高安全类型集群](https://help.aliyun.com/document_detail/459040.html)

## 1. 进入集群服务页面。
* a. 登录EMR on ECS控制台。
* b. 顶部菜单栏处，根据实际情况选择地域和资源组。
* c. 在集群管理页面，单击目标集群操作列的集群服务。
## 2. Ranger启用OSS。
* a. 在集群服务页面，单击Ranger-plugin服务区域的状态。
* b. 在服务概述区域，打开enableOSS开关。
* c. 在弹出的对话框中，单击确定。
## 3. 部署客户端配置。
* a. 在集群服务页面，单击 HADOOP-COMMON 服务页面。
* b. 单击配置。
* c. 单击部署客户端配置。
## 4. 重启 HiveServer2 等常驻服务。
* a. 在集群服务页面，选择more > Hive。
* b. 在组件列表区域，单击HiveServer操作列的重启。
* c. 在弹出的对话框中，输入执行原因，单击确定。
* d. 在确认对话框中，单击确定。

## 5. 权限配置示例。
Ranger启用OSS后，已默认添加好了OSS Service。
<img src="images/ranger_oss_1.png" width="800"/>

例：配置用户test拥有访问`oss://jindoauth-runjob-cn-shanghai/user/test`目录的所以权限的步骤：

#### 配置 test 用户访问`oss://jindoauth-runjob-cn-shanghai/user/test`目录的访问权限为 ALL。

<img src="images/ranger_oss_2.png" width="800"/>

##### 说明：

*   规则配置页面中，配置的 path 没有`oss://`的前缀。
    
*   recursive按钮不可关闭。
    
*   路径末尾无需带正斜线（/）。

## 6. 访问OSS。

若用户访问 Ranger 没有授权的路径，则会报如下错误：

    org.apache.hadoop.security.AccessControlException: Permission denied: user=test, access=READ_EXECUTE, resourcePath="bucket-test-hangzhou/"