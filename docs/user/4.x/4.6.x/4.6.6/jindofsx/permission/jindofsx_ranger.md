# 阿里云 OSS 使用 Ranger 的鉴权方案

本文介绍在自建集群配置阿里云 OSS 和 OSS-HDFS 服务使用 Ranger 的鉴权方案。

## 背景
Apache Ranger 提供集中式的权限管理框架，可以对 Hadoop 生态中的多个组件进行细粒度的权限访问控制。当用户将数据存放在阿里云 OSS 时，则是通过阿里云 RAM 产品创建或管理 RAM 用户，对RAM用户实现对 OSS 资源的访问控制。
为维持大数据客户的使用习惯，通过 JindoFSx Namespace 接入 Ranger 的客户端，方便用户统一管理大数据组件权限。

## 前提条件

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.6.x/4.6.6/jindofsx/deploy/deploy_jindosdk.md)

* 已部署 JindoFSx Namespace

关于如何部署 JindoFSx Namespace，请参考 [快速部署一个简单的 JindoFSx 存储加速系统(仅 Namespace)](/docs/user/4.x/4.6.x/4.6.6/jindofsx/deploy/deploy_jindofsx_nsonly.md)

* 安装 Kerberos 和 Sasl2 相关依赖

如果您的环境没有 Kerberos 和 Sasl2 相关依赖，请安装相关依赖 [安装说明](/docs/user/4.x/install_dependeny_jindodata.md)

## 1. 配置 JindoFSx Namespace 服务。

### 1.1 配置 Jindo SASL 插件
该功能依赖 Jindo SASL 动态库，需要配置路径以加载动态库，动态库在 `jindofsx-4.6.6/plugins/` 路径下。
在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg， 在 [jindofsx-namespace] section 下添加动态库的绝对路径。

| 参数             | 值                          |
| ----------------------------------- | --------|
| namespace.plugin.dir | ${JINDDATA_HOME}/plugins/ |

### 1.2 启用 Ranger 鉴权
在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg， 在 [jindofsx-namespace] section 下添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| namespace.oss.authorization.method  | ranger  |

注意： 将 jindofsx.cfg 配置文件部署到 NamespaceService 所在的所有节点。

### 1.3 配置使用 Assume Role 颁发临时 AK
在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg。
在 [jindofsx-namespace] section 下添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| namespace.oss.credential.provider  | ASSUME_ROLE  |

在阿里云 RAM 创建用户颁发临时 AK 的 Role, 选择可信实体类型为"阿里云账号", 并为角色授权。
在 [jindofsx-common] section 下添加或更改如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| default.credential.provider  | ASSUME_ROLE  |
| sts.access.key  | STS 的 accessKey  |
| sts.access.secret  | STS 的 accessKeySecret  |
| sts.access.endpoint  | STS 的 endpoint, 如： sts.cn-shanghai.aliyuncs.com  |
| sts.role.name  | 在 RAM 中创建的角色， 如 JindoFsxTestRole  |
| sts.role.arn  |  角色的ARN，如：acs:ram::xxx:role/JindoFsxTestRole  |

注意： 将 jindofsx.cfg 配置文件部署到 NamespaceService 所在的所有节点。

### 1.4 配置 Principal 和 Keytab（适用于 Kerberos 集群）
在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg。
在 [jindofsx-namespace] section 下添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| namespace.authentication.enable  | true  |
| namespace.authentication.keytab  | 配置 keytab 所在的路径， 如： /tmp/jindofsx.keytab  |
| namespace.authentication.principal  | 配置 keytab 中的 principal, 如： jindofsx/localhost  |

## 2. 重启 JindoFSx Namespace 服务。
在 master 节点执行以下脚本：

```shell
cd jindofsx-x.x.x
sh sbin/stop-service.sh
sh sbin/start-service.sh
```

## 3. 配置 JindoSDK。

### 3.1 配置 xengine
在 Hadoop 的 `core-site.xml` 中 添加如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.xengine  | jindofsx  |

### 3.2 配置 Jindo SASL 插件
该功能依赖 Jindo SASL 动态库，需要配置路径以加载动态库，动态库在 `jindosdk-4.6.6/plugins/` 路径下，将该路径的绝对路径添加到 Hadoop 的`core-site.xml`中。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.jdo.plugin.dir  | ${JINDOSDK_HOME}/plugins/  |

### 3.3 启用 Ranger 鉴权

支持统一配置和按 bucket 配置两种方式。

#### a. 对 OSS 和 OSS-HDFS 的全部 bucket 鉴权

在Hadoop 的`core-site.xml`中添加或更新如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.oss.authorization.method         | ranger  |
| fs.oss.credentials.provider         | com.aliyun.jindodata.oss.auth.RangerCredentialsProvider  |

#### b. 对 OSS 或 OSS-HDFS 的部分 bucket 鉴权

在Hadoop 的`core-site.xml`中添加或更新如下参数。

| 参数             | 值                          |
| ----------------------------------- | --------|
| fs.oss.bucket.XXX.authorization.method        | ranger  |
| fs.oss.bucket.XXX.credentials.provider        | com.aliyun.jindodata.oss.auth.RangerCredentialsProvider  |

说明：XXX 为 或 OSS 或 OSS-HDFS 服务 bucket的名称。

## 4. 重启 Hive 所有服务，使配置生效。

## 5. 安装支持配置 OSS 权限的 Ranger 插件
Ranger 组件需要安装新插件以支持对 OSS 或 OSS-HDFS 服务对鉴权。

### 5.1 下载和拷贝插件
根据集群中安装的 Ranger 版本下载对应的插件版本。

* 1.x 版本
[下载地址](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/ranger/ranger-oss-plugin-1.2.0.jar)
* 2.x 版本
[下载地址](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/ranger/ranger-oss-plugin-2.1.0.jar)

```
cd /xxx/ranger-admin-current/ews/webapp/WEB-INF/classes/ranger-plugins
mkdir oss
cp ranger-oss-plugin-1.2.0.jar /xxx/ranger-admin-current/ews/webapp/WEB-INF/classes/ranger-plugins/oss
```
注意：需要安装在部署 Ranger Admin 的所有节点。

### 5.2 安装插件

创建如下ranger-oss.json文件：
```
{
  "id": 500,
  "name": "oss",
  "implClass": "org.apache.ranger.services.oss.RangerServiceOSS",
  "label": "OSS",
  "description": "OSS",
  "guid": "0d047247-bafe-4cf8-8e9b-d5d377284b2h",
  "resources": [
    {
      "itemId": 1,
      "name": "path",
      "type": "path",
      "level": 10,
      "parent": "",
      "mandatory": true,
      "lookupSupported": true,
      "recursiveSupported": true,
      "excludesSupported": false,
      "matcher": "org.apache.ranger.plugin.resourcematcher.RangerPathResourceMatcher",
      "matcherOptions": {
        "wildCard": true,
        "ignoreCase": false
      },
      "validationRegEx": "",
      "validationMessage": "",
      "uiHint": "",
      "label": "Path",
      "description": "OSS Path, Should Not Start With /, exp. dir/test.txt"
    }
  ],
  "accessTypes": [
    {
      "itemId": 1,
      "name": "read",
      "label": "Read"
    },
    {
      "itemId": 2,
      "name": "write",
      "label": "Write"
    },
    {
      "itemId": 3,
      "name": "execute",
      "label": "Execute"
    }
  ],
  "configs": [
  ],
  "enums": [
  ],
  "contextEnrichers": [],
  "policyConditions": []
}
```

执行如下命令装载 ranger-oss，注意其中 json 文件的路径和 ranger admin 的 url，adminUser 默认为 admin，adminPasswd 默认为 admin：
```
curl -v -u${adminUser}:${adminPasswd} -X POST -H "Accept:application/json" -H "Content-Type:application/json" -d @ranger-oss.json http://emr-header-1:6080/service/plugins/definitions
```
插件成功安装后，可以在 Ranger UI 上看到新增项：

<img src="../pic/ranger_oss_plugin_1.png" width="800"/>

创建一个Service，例如emr-oss，创建完成后如下，后续就可以在该Service下创建Policy：
<img src="../pic/ranger_oss_plugin_2.png" width="800"/>

## 6. 在 Ranger WebUI 配置 OSS 或 OSS-HDFS 服务权限

### Ranger 规则示例
例：配置用户test拥有访问`oss://bucket-test-hangzhou/user/test`目录的所以权限的步骤：
#### a. 配置 test 用户访问`oss://bucket-test-hangzhou/user/test`目录的访问权限为 ALL。
<img src="../pic/jindofsx_oss_ranger_7.png" width="800"/>

##### 说明：
* 规则配置页面中，配置的 path 没有`oss://`的前缀。
* recursive按钮不可关闭。
* 路径末尾无需带正斜线（/）。

#### b. 需要配置访问路径的父目录`oss://bucket-test-hangzhou/user`的权限为 Execute。
<img src="../pic/jindofsx_oss_ranger_8.png" width="800"/>

## 7. 访问OSS。
若用户访问 Ranger 没有授权的路径，则会报如下错误：
```
org.apache.hadoop.security.AccessControlException: Permission denied: user=test, access=READ_EXECUTE, resourcePath="bucket-test-hangzhou/"
```

## 注意事项
针对阿里云 OSS-HDFS 服务（JindoFS 服务）的鉴权，目前使用的是对象存储的鉴权方式（通过匹配路径），对象存储没有目录或文件的 owner 和 permission 信息，如需对 OSS-HDFS 服务使用`chmod` ,`chown`等操作，只能通过 `hadoop` 用户执行。