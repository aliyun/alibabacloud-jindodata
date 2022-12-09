# 已知问题

## 4.6.x 版本

### 4.6.2 版本

1. JindoSDK 4.6.1 在 EMR 集群使用免密访问 OSS-HDFS 出现等待 Token 更新，导致部分作业卡住的问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

### 4.6.1 版本

1. JindoSDK 4.6.1 在 EMR 集群使用免密访问 OSS-HDFS 出现等待 Token 更新，导致部分作业卡住的问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

2. JindoSDK 4.6.1 在 EMR 集群使用免密下 JindoUtil 工具，会产生权限错误问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

### 4.6.0 版本

1. JindoSDK 4.6.0：在 EMR 集群使用免密访问 OSS-HDFS 出现等待 Token 更新，导致部分作业卡住的问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；
2. JindoSDK 4.6.0 和 JindoFSx 4.6.0：Kerberos 集群配置使用 fs.oss.credentials.provider=com.aliyun.jindodata.oss.auth.RangerCredentialsProvider 时，JindoFSx Namespace Service 内存泄露问题，需更新 JindoFSx 和 JINDOSDK 到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindodata_emr-next.md)
   和 [EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

3. JindoSDK 4.6.0 在 EMR 集群使用免密下 JindoUtil 工具，会产生权限错误问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

## 4.5.x 版本

### 4.5.2 版本

1. JindoSDK 4.5.2 和 JindoFSx 4.5.2：Kerberos 集群配置使用 fs.oss.credentials.provider=com.aliyun.jindodata.oss.auth.RangerCredentialsProvider 时，JindoFSx Namespace Service 内存泄露问题，需更新 JindoFSx 和 JINDOSDK 到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](../emr_upgrade_jindodata_emr-next.md)
   和 [EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.5.x/emr_upgrade_jindosdk_emr-next.md)；


2. JindoSDK 4.5.2 在 EMR 集群使用免密下 JindoUtil 工具，会产生权限错误问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

### 4.5.1 版本

1. JindoSDK 4.5.1 和 JindoFSx 4.5.1：Kerberos 集群配置使用 fs.oss.credentials.provider=com.aliyun.jindodata.oss.auth.RangerCredentialsProvider 时，JindoFSx Namespace Service 内存泄露问题，需更新 JindoFSx 和 JINDOSDK 到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](../emr_upgrade_jindodata_emr-next.md)
   和 [EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.5.x/emr_upgrade_jindosdk_emr-next.md)；

2. JindoSDK 4.5.1 在 EMR 集群使用免密下 JindoUtil 工具，会产生权限错误问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

### 4.5.0 版本

1. JindoSDK 4.5.0 和 JindoFSx 4.5.0：Kerberos 集群配置使用 fs.oss.credentials.provider=com.aliyun.jindodata.oss.auth.RangerCredentialsProvider 时，JindoFSx Namespace Service 内存泄露问题，需更新 JindoFSx 和 JINDOSDK 到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](../emr_upgrade_jindodata_emr-next.md)
   和 [EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.5.x/emr_upgrade_jindosdk_emr-next.md)；

2. JindoSDK 4.5.0 在 EMR 集群使用免密访问 OSS、OSS-HDFS 失败重试时无法进行 Token 更新，会导致部分作业卡住的问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

3. JindoSDK 4.5.0 在 EMR 集群使用免密下 JindoUtil 工具，会产生权限错误问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

## 4.4.x 版本

1. JindoSDK 4.4.0/4.4.1/4.4.2 和 JindoFSx 4.4.0/4.4.1/4.4.2：Kerberos 集群配置使用 fs.oss.credentials.provider=com.aliyun.jindodata.oss.auth.RangerCredentialsProvider 时，JindoFSx Namespace Service 内存泄露问题，需更新 JindoFSx 和 JINDOSDK 到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](/docs/user/4.x/4.4.x/emr_upgrade_jindodata_emr-next.md)
   和 [EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.4.x/emr_upgrade_jindosdk_emr-next.md)；

2. JindoSDK 4.4.0 在 EMR 集群使用免密高并发访问 OSS、OSS-HDFS 时，有概率出现coredump，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；

## 4.3.x 版本

1. JINDOSDK 4.3.0 （EMR-3.40.0/EMR-5.6.0）由于显示目录时间会导致 ls 性能出现一定程度退化，暂不显示目录时间，如需显示时间需更新到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](emr_upgrade.md)；
2. JINDOSDK 4.3.0 （EMR-3.40.0/EMR-5.6.0）使用 MagicCommitter 时，会有频繁调用 uploadPart 问题，出现 “Part number must be an integer between 1 and 10000” 异常，如需解决此问题需要更新到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](emr_upgrade.md)；
3. JindoFSx 4.3.0 服务端读取缓存数据在部分路径下存在出错处理异常，相关错误未能正确返回客户端，导致客户端返回错误的数据内容，需更新JindoFSx到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](emr_upgrade_jindodata.md)；
4. JindoFSx 4.3.0 服务端处理内存缓存预加载命令存在问题，导致加载到内存中的数据内容可能发生错误，造成后续读取到错误的数据内容，需更新JindoFSx到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](emr_upgrade_jindodata.md)；
5. JindoFSx 4.3.0/4.3.1 服务端存在文件句柄泄漏问题，长时间运行后可能达到操作系统规定的进程上限，导致服务无法打开新的文件句柄造成服务不可用，需更新JindoFSx到4.3.2及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](emr_upgrade_jindodata.md)。

## 4.2.x 版本

1. JINDOSDK 4.2.0 在大文件 SEEK 时存在溢出问题，会导致部分使用 SEEK 的任务出现读取 OSS 大文件失败。

## 4.1.x 版本

1. JINDOSDK 4.1.0 在大文件 SEEK 时存在溢出问题，会导致部分使用 SEEK 的任务出现读取 OSS 大文件失败。


## 4.0.x 版本

1. JINDOSDK 4.0.0 （EMR-3.39.0/EMR-5.5.0） 在大文件 SEEK 时存在溢出问题，会导致部分使用 SEEK 的任务出现读取 OSS 大文件失败。





