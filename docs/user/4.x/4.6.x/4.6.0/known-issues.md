# 已知问题

1. JindoSDK 4.6.0：在 EMR 集群使用免密访问 OSS-HDFS 出现等待 Token 更新，导致部分作业卡住的问题，如需解决此问题，使用固定 AK 或者更新到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；
2. JindoSDK 4.6.0 和 JindoFSx 4.6.0：Kerberos 集群配置使用 fs.oss.credentials.provider=com.aliyun.jindodata.oss.auth.RangerCredentialsProvider 时，JindoFSx Namespace Service 内存泄露问题，需更新 JindoFSx 和 JINDOSDK 到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindodata_emr-next.md)
   和 [EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.6.x/emr_upgrade_jindosdk_emr-next.md)；