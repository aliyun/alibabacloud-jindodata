# EMR 集群中阿里云 OSS 使用 Ranger 的鉴权方案

本文介绍在 EMR 集群配置阿里云 OSS 和 OSS-HDFS 服务使用 Ranger 的鉴权方案。

## 背景
Apache Ranger 提供集中式的权限管理框架，可以对 Hadoop 生态中的多个组件进行细粒度的权限访问控制。当用户将数据存放在阿里云 OSS 时，则是通过阿里云 RAM 产品创建或管理 RAM 用户，对RAM用户实现对 OSS 资源的访问控制。
为维持大数据客户的使用习惯，通过 JindoFSx Namespace 接入 Ranger 的客户端，方便用户统一管理大数据组件权限。

## 访问 OSS 鉴权流程
* OSS 的访问密钥 AccessKey（AK）统一在 JindoFSx Namespace 中设置，避免用户在客户端配置明文密钥，建议只允许管理员操作和管理 JindoFSx Namespace 服务；
* JindoSDK 提供标准 Hadoop Filesystem 客户端，将访问 OSS 的请求会发送至 Namespace 服务；
* JindoFSx Namespace Service 负责集成 Ranger 客户端，周期性将权限策略从 Ranger 服务端同步到本地；
* Namespace 服务在收到 JindoSDK 的鉴权请求后进行细粒度的权限校验；
* 通过权限校验后 JindoSDK 则可以使用 JindoFSx Namespace 服务颁发的临时 AK 访问 OSS。

  <img src="../pic/jindofsx_oss_ranger_0.png" alt="title" width="700"/>

注：若 Namespace 部署的节点为阿里云 ECS 节点，可以通过配置安全组，限制访问 Namespace 的客户端。

## 前提条件
* 已创建 E-MapReduce EMR-5.9.1/EMR-3.43.1 或以上版本集群，并且选择了 Ranger 服务。
* 启用 Kerberos 认证满足认证需求。
* 若使用集群都是可信用户，则可不启用 Kerberos 认证。

注意：EMR 集群开启 Kerberos 后，将无法关闭 Kerberos 认证，请谨慎选择。开启 Kerberos 后，集群所有大数据组件的服务均需要经过 Kerberos 认证，大数据作业提交到集群会先经过身份认证。Kerberos 详情信息，请参见 [高安全类型集群](https://help.aliyun.com/document_detail/459040.html)

## 1. 配置 JindoFSx Namespace 服务。

### 1.1 配置 Jindo SASL 插件
该功能依赖 Jindo SASL 动态库，需要配置路径以加载动态库，动态库在 `/opt/apps/JINDODATA/jindodata-current/plugins` 路径下。
在 JindoData 服务页面选择`配置` > `namespace` > `新增配置项`，添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| namespace.plugin.dir | /opt/apps/JINDODATA/jindodata-current/plugins |

### 1.2 启用 Ranger 鉴权
在 JindoData 服务页面选择`配置` > `namespace` > `新增配置项`，添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| namespace.oss.authorization.method  | ranger  |


### 1.3 配置使用 Assume Role 颁发临时 AK（可选，默认使用 ECS_ROLE 免密）

在 JindoData 服务页面选择`配置` > `namespace` > `新增配置项`，添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| namespace.oss.credential.provider  | ASSUME_ROLE  |

在阿里云 RAM 创建用户颁发临时 AK 的 Role, 选择可信实体类型为"阿里云账号", 并为角色授权。
在 JindoData 服务页面选择`配置` > `common` > `新增配置项`，添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| default.credential.provider  | ASSUME_ROLE  |
| sts.access.key  | STS 的 accessKey  |
| sts.access.secret  | STS 的 accessKeySecret  |
| sts.access.endpoint  | STS 的 endpoint, 如： sts.cn-shanghai.aliyuncs.com  |
| sts.role.name  | 在 RAM 中创建的角色， 如 JindoFsxTestRole  |
| sts.role.arn  |  角色的ARN，如：acs:ram::xxx:role/JindoFsxTestRole  |


### 1.4 配置 Principal 和 Keytab（适用于 Kerberos 集群）
#### a. 创建 principal 和 keytab
如果您是使用root用户，登录KDC（Kerberos的服务端程序）所在的master-1-1节点，则可以执行以下命令，直接进入admin工具。
```
kadmin.local
```
当返回信息中包含如下信息时，表示已进入admin.local命令行。
```
Authenticating as principal hadoop/admin@EMR.C-85D4B8D74296****.COM with password.
kadmin.local:
```
执行以下命令，创建用户名为 jindodata 的 principal, 格式为 jindodata/<hostname>.<region>.emr.aliyuncs.com。
```
addprinc -randkey jindodata/master-1-1.c-9e4fc2dcc****.cn-shanghai.emr.aliyuncs.com
```
导出 keytab 文件。
```
ktadd -k jindodata.keytab jindodata/master-1-1.c-9e4fc2dcc****.cn-shanghai.emr.aliyuncs.com
```

#### b. 配置
在 JindoData 服务页面选择`配置` > `namespace` > `新增配置项`，添加如下参数。

| 参数             | 值                                              |
| ----------------------------------- |------------------------------------------------|
| namespace.authentication.enable  | true                                           |
| namespace.authentication.keytab  | 配置 keytab 所在的路径， 如： /opt/apps/JINDODATA/jindodata-current/conf/jindodata.keytab      |
| namespace.authentication.principal  | 配置 keytab 中的 principal, 如： jindodata/master-1-1.c-9e4fc2dcc****.cn-shanghai.emr.aliyuncs.com@EMR.C-85D4B8D74296****.COM  |

### 1.5. 重启 JindoFSx Namespace 服务。

## 2. 配置 JindoSDK。

### 2.1 配置 xengine
在 Hadoop-Common 页面选择`配置` > `core-site.xml`， 更新如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.xengine  | jindofsx  |

### 2.2 配置 Jindo SASL 插件
该功能依赖 Jindo SASL 动态库，需要配置路径以加载动态库，动态库在 `/opt/apps/JINDOSDK/jindosdk-current/plugins/` 路径下，
在 Hadoop-Common 页面选择`配置` > `core-site.xml` > `新增配置项`， 添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.jdo.plugin.dir  | /opt/apps/JINDOSDK/jindosdk-current/plugins  |

### 2.3 启用 Ranger 鉴权

支持统一配置和按 bucket 配置两种方式。

#### a. 对 OSS 和 OSS-HDFS 的全部 bucket 鉴权

在 Hadoop-Common 页面选择`配置` > `core-site.xml`， 新增或更新如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.oss.authorization.method         | ranger  |
| fs.oss.credentials.provider         | com.aliyun.jindodata.oss.auth.RangerCredentialsProvider  |

#### b. 对 OSS 或 OSS-HDFS 的部分 bucket 鉴权

在 Hadoop-Common 页面选择`配置` > `core-site.xml`， 新增或更新如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.oss.bucket.XXX.authorization.method        | ranger  |
| fs.oss.bucket.XXX.credentials.provider        | com.aliyun.jindodata.oss.auth.RangerCredentialsProvider  |

说明：XXX 为 或 OSS 或 OSS-HDFS 服务 bucket的名称。

### 2.4. 重启 HiveServer2 等所有常驻服务，使配置生效。

## 3. 创建用户 Principal。
如果您是使用root用户，登录KDC（Kerberos的服务端程序）所在的 master-1-1 节点，则可以执行以下命令，直接进入admin工具。
```
kadmin.local
```
当返回信息中包含如下信息时，表示已进入admin.local命令行。
```
Authenticating as principal hadoop/admin@EMR.C-85D4B8D74296****.COM with password.
kadmin.local:
```
执行以下命令，创建用户名为 test 的 principal。
```
addprinc -pw 123456 test
```

## 4. 创建 TGT。
创建 TGT 的机器，可以是任意一台需要访问 OSS 的机器。
### a. 使用 root 用户执行以下命令，创建 test 用户。

```
useradd test
```
##### 说明: 需要记录用户名和密码，在创建TGT时会用到。如果您不想记录用户名和密码，则可以执行下一步，把 principal 的用户名和密码导入到 keytab 文件中。
### b. 可选：执行如下命令，生成keytab文件。

```
ktadd -k /root/test.keytab test
```

### c. 执行以下命令，切换为 test 用户。

```
su test
```

### d. 生成TGT。
* 方式一：使用用户名和密码方式，创建 TGT。
  执行 kinit 命令，回车后输入 test 的密码 123456。

* 方式二：使用 keytab 文件，创建 TGT。
  在步骤 4 中的 test.keytab 文件，已经保存在 emr-header-1 机器的`/root/`目录下，需要使用 scp 命令拷贝到当前机器的`/home/test/`目录下。

```
kinit -kt /home/test/test.keytab test
```

### e. 查看TGT。
使用 klist 命令，如果出现如下信息，则说明TGT创建成功，即可以访问OSS了。

```
Ticket cache: FILE:/tmp/krb5cc_1012
Default principal: test@EMR.C-85D4B8D74296****.COM

Valid starting     Expires            Service principal
11/15/22 15:22:57  11/16/22 15:22:57  krbtgt/EMR.EMR.C-85D4B8D74296****.COM@EMR.C-85D4B8D74296****.COM
	renew until 11/22/22 15:22:57
```

## 5. 在 Ranger WebUI 配置 OSS 权限
### a. 创建一个名为 emr-oss 的 service， 并添加下列配置。

| 参数             | 值         |
| ----------------------------------- |-----------|
| policy.download.auth.users       | jindodata |

<img src="../pic/jindofsx_oss_ranger_9.png" width="800"/>

创建完成后如下，后续就可以在该Service下创建Policy。

### b. Ranger 规则示例
例：配置用户test拥有访问`oss://bucket-test-hangzhou/user/test`目录的所以权限的步骤：
#### 配置 test 用户访问`oss://bucket-test-hangzhou/user/test`目录的访问权限为 ALL。
<img src="../pic/jindofsx_oss_ranger_7.png" width="800"/>

##### 说明：
* 规则配置页面中，配置的 path 没有`oss://`的前缀。
* recursive按钮不可关闭。
* 路径末尾无需带正斜线（/）。

## 6. 访问OSS。
若用户访问 Ranger 没有授权的路径，则会报如下错误：
```
org.apache.hadoop.security.AccessControlException: Permission denied: user=test, access=READ_EXECUTE, resourcePath="bucket-test-hangzhou/"
```