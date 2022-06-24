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
* 已创建E-MapReduce EMR-5.6.0/EMR-3.40.0或以上版本的高安全集群(启用 Kerberos 认证)，并且选择了 Ranger 服务。

### 创建高安全集群
  <img src="../pic/jindofsx_oss_ranger_1.png" width="800"/>
  创建集群时，在软件配置页面的高级设置区域中，打开Kerberos集群模式开关。

## 1. Ranger 启用 OSS。
  <img src="../pic/jindofsx_oss_ranger_2.png" width="800"/>

## 2. 部署客户端配置。
在 HDFS 服务页面，单击右上角的部署客户端配置。

## 3. 重启 JindoFSx Namespace 服务。
  <img src="../pic/jindofsx_oss_ranger_4.png" width="800"/>

## 4. 重启HiveServer2。
  <img src="../pic/jindofsx_oss_ranger_5.png" width="800"/>

## 5. 创建用户 Principal。
### a. 通过SSH方式连接集群的emr-header-1节点。
### b. 执行如下命令，进入Kerberos的admin工具。
```
sh /usr/lib/has-current/bin/admin-local.sh /etc/ecm/has-conf -k /etc/ecm/has-conf/admin.keytab
```
本示例密码设置为 123456。
```
addprinc -pw 123456 test
```
##### 说明: 需要记录用户名和密码，在创建TGT时会用到。如果您不想记录用户名和密码，则可以执行下一步，把Principal的用户名和密码导入到keytab文件中。
### c. 可选：执行如下命令，生成keytab文件。

```
ktadd -k /root/test.keytab test
```

执行 quit 命令，可以退出 Kerberos 的 admin 工具。

## 6. 创建 TGT。
创建 TGT 的机器，可以是任意一台需要访问 OSS 的机器。
### a. 使用 root 用户执行以下命令，创建 test 用户。

```
useradd test
```

### b. 执行以下命令，切换为 test 用户。

```
su test
```

### c. 生成TGT。
* 方式一：使用用户名和密码方式，创建 TGT。
  执行 kinit 命令，回车后输入 test 的密码 123456。

* 方式二：使用 keytab 文件，创建 TGT。
  在步骤 4 中的 test.keytab 文件，已经保存在 emr-header-1 机器的`/root/`目录下，需要使用 scp 命令拷贝到当前机器的`/home/test/`目录下。

```
kinit -kt /home/test/test.keytab test
```

### d. 查看TGT。
使用 klist 命令，如果出现如下信息，则说明TGT创建成功，即可以访问OSS了。

```
Ticket cache: FILE:/tmp/krb5cc_1012
Default principal: test@EMR.23****.COM

Valid starting       Expires              Service principal
03/27/2022 23:20:44  03/28/2022 23:20:44  krbtgt/EMR.238075.COM@EMR.238075.COM
renew until 03/28/2022 23:20:44
```

注意: 需要记录下回显信息 `EMR.23****.COM` 中的数字 `23****`，即为 cluster_id 的值，后面访问 OSS 时需要。

## 7. 在 Ranger WebUI 配置 OSS 权限
<img src="../pic/jindofsx_oss_ranger_6.png" width="800"/>

### Ranger 规则示例
例：配置用户test拥有访问`oss://bucket-test-hangzhou/user/test`目录的所以权限的步骤：
#### a. 配置 test 用户访问`oss://bucket-test-hangzhou/user/test`目录的访问权限为 ALL。
<img src="../pic/jindofsx_oss_ranger_7.png" width="800"/>

##### 说明：
* 规则配置页面中，配置的 path 没有`oss://`的前缀。
* recursive按钮不可关闭

#### b. 需要配置访问路径的父目录`oss://bucket-test-hangzhou/user`的权限为 Execute。
<img src="../pic/jindofsx_oss_ranger_8.png" width="800"/>

## 8. 访问OSS。
若用户访问 Ranger 没有授权的路径，则会报如下错误：
```
org.apache.hadoop.security.AccessControlException: Permission denied: user=test, access=READ_EXECUTE, resourcePath="bucket-test-hangzhou/"
```