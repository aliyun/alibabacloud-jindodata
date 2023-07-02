# jindoauth\_emr-next

# 阿里云 OSS 使用 Ranger 的鉴权方案（EMR 新版控制台）

本文介绍在 E-MapReduce 新版控制台( EMR-5.6.0/EMR-3.40.0 及以上版本非 Kerberos 集群)配置阿里云 OSS 和 OSS-HDFS 服务使用 Ranger 的鉴权方案。

## 背景

Apache Ranger 提供集中式的权限管理框架，可以对 Hadoop 生态中的多个组件进行细粒度的权限访问控制。当用户将数据存放在阿里云 OSS 时，则是通过在阿里云 RAM 产品创建或管理 RAM 用户，实现对 OSS 资源的访问控制。 为维持大数据客户的使用习惯，通过 JindoAuth 接入 Ranger 的客户端，方便用户统一管理大数据组件权限。

## 前提条件

*   已创建 E-MapReduce EMR-5.9.1/EMR-3.43.1 或以上版本集群，并且选择了 Ranger 服务，若为 E-MapReduce EMR-5.11.0/EMR-3.45.0 或以上版本集群，则需要选择 Ranger 和 Ranger-plugin 服务。
    
*   已部署 JindoAuth 服务。
    
*   若使用集群都是可信用户，则可不启用 Kerberos 认证。本文为非 Kerberos 集群的使用说明。
    

## 配置 JindoSDK。

### 1. 配置 JindoAuth 服务地址

在 Hadoop 的 core-site.xml 中添加或更新如下参数。

|  参数  |  值  |  说明  |
| --- | --- | --- |
|  fs.oss.jindoauth.rpc.address  |  hostname1:port1,hostname2:port2,hostname3:port3  |  访问 JindoAuth 服务的 rpc 地址列表。  |

注： fs.oss.jindoauth.rpc.address 中的 hostname 为 JindoAuth 服务所在的节点地址列表，客户端会根据这个列表轮询访问 JindoAuth 服务。

### 2. 开启 Ranger 鉴权

在Hadoop 的 core-site.xml 中添加或更新如下参数，以下两个参数，用户可以根据使用场景选择一个配置。支持统一配置和按 bucket 配置两种方式。

#### a. 对 OSS 和 OSS-HDFS 的全部 bucket 鉴权

在 Hadoop-Common 页面选择`配置` > `core-site.xml`， 新增或更新如下参数。

|  参数  |  值  |  说明  |
| --- | --- | --- |
|  fs.oss.authorization.method  |  ranger  |  对 OSS-HDFS 鉴权。  |
|  fs.oss.credentials.provider   |  RangerCredentialsProvider  |  配置获取 Credential 的方式，通过 Ranger 鉴权后才能获取访问 OSS-HDFS 的 AK。  |

注意以下几种可能出现的情况： 

*   配置了 fs.oss.authorization.method 为 ranger，没有配置 fs.oss.credentials.provider 为 RangerCredentialsProvider：对访问 OSS-HDFS 的操作进行 Ranger 访问控制，若没有权限，则直接报错，若有权限，JindoSDK 会根据 fs.oss.credentials.provider 的配置获取 AK 访问 OSS-HDFS（比如通过 EcsRole 免密获取 AK），即 JindoAuth 只负责鉴权，不负责颁发 Security Token;
    
*   配置了 fs.oss.credentials.provider 为 RangerCredentialsProvider， fs.oss.authorization.method 配置成任何值(空，ranger，或没有配置这个参数)，JindoAuth 都会负责 Ranger 鉴权和颁发 Security Token;
    

#### b. 对 OSS 或 OSS-HDFS 的部分 bucket 鉴权

在 Hadoop-Common 页面选择`配置` > `core-site.xml`， 新增或更新如下参数。

|  参数  |  值  |
| --- | --- |
|  fs.oss.bucket.XXX.authorization.method  |  ranger  |
|  fs.oss.bucket.XXX.credentials.provider  |  RangerCredentialsProvider  |

说明：XXX 为 或 OSS 或 OSS-HDFS 服务 bucket的名称。

### 3. 重启 HiveServer2 等所有常驻服务，使配置生效。

### 4. 访问OSS。

若用户访问 Ranger 没有授权的路径，则会报如下错误：

    org.apache.hadoop.security.AccessControlException: Permission denied: user=test, access=READ_EXECUTE, resourcePath="bucket-test-hangzhou/"