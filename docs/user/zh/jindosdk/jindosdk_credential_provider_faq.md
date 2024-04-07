# 访问 OSS/OSS-HDFS 时 AK 相关常见问题

## 1. EMR 集群内如何免密访问 OSS/OSS-HDFS
JindoSDK 会使用创建集群时绑定的 ECS 应用角色（默认为 AliyunECSInstanceForEMRRole）获取 Security Token 访问 OSS/OSS-HDFS。
   ![image.png](pic/jindosdk_ecs_role.png)

在 Hadoop-Common 页面选择`配置` > `core-site.xml`中更新下列参数：

| 参数             | 值      |
| --------------- | --------|
| fs.oss.credentials.provider  | com.aliyun.jindodata.oss.auth.EcsStsCredentialsProvider  |

## 2. 如何检查 EMR 集群内免密服务是否可用？

### 方法一：

执行下面命令：
```
curl http://100.100.100.200/latest/meta-data/Ram/Security-credentials/AliyunECSInstanceForEMRRole
```
如果返回下面格式的内容，则正常。

```
"AccessKeyId" : "STS.NUreXXXXXX",
"AccessKeySecret" : "BsmbnDoXXXXXXXX",
"Expiration" : "2022-11-22T11:27:39Z",
"SecurityToken" : "CAISlwJ1q6FXXXXXXX",
"LastUpdated" : "2022-11-22T05:27:39Z",
"Code" : "Success"
```

### 方法二：
配置为 ECS 免密：
在 Hadoop-Common 页面选择`配置` > `core-site.xml`中更新下列参数：

| 参数             | 值      |
| --------------- | --------|
| fs.oss.credentials.provider  | com.aliyun.jindodata.oss.auth.EcsStsCredentialsProvider  |

使用 HDFS shell 访问 OSS/OSS-HDFS bucket, 若能正常访问，则确认免密正常。

## 3. 常驻服务每 5-6 个小时访问 OSS/OSS-HDFS 失败
某些版本有已知免密问题，请查看[已知问题](../faq.md)

### 方法一：使用固定 AK
配置使用`SimpleCredentialsProvider`, 请参考 [配置 OSS/OSS-HDFS Credential Provider](jindosdk_credential_provider.md)

### 方法二：升级 JindoSDK 为最新版本

* 旧版控制台升级，请参考[EMR 集群 JindoSDK 升级流程](../upgrade/emr_upgrade_jindosdk.md)

* 新版控制台升级，请参考[EMR 集群 JindoSDK 升级流程](../upgrade/emr2_upgrade_jindosdk.md)

* E-MapReduce EMR-5.5.0/EMR-3.39.0 以前版本的集群，请参考[EMR 集群 JindoSDK 升级流程](../upgrade/emr_upgrade_smartdata.md)

## 4. 访问 OSS/OSS-HDFS 路径中包含 AK 信息报错
报错信息如下：
```
The Filesystem URI contains login details. This authentication mechanism is no longer supported.
```
为了防止 AK 泄露等安全问题，JindoSDK 4.0.0 开始不支持路径中携带 AK(AccessKeyId,AccessKeySecret等）信息。

### 方法一：
访问路径中去除 AK 信息；

### 方法二：
如果一定要这样使用，可以通过下面参数开启。在 Hadoop-Common 页面选择`配置` > `core-site.xml`中新增下列参数：

| 参数             | 值    |
| --------------- |------|
| fs.oss.uri-with-secrets.enable | true |

