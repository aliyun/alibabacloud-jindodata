# 已知问题

1. JindoSDK 4.5.1 和 JindoFSx 4.5.1：Kerberos 集群配置使用 fs.oss.credentials.provider=com.aliyun.jindodata.oss.auth.RangerCredentialsProvider 时，JindoFSx Namespace Service 内存泄露问题，需更新 JindoFSx 和 JINDOSDK 到 4.6.2 及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](../emr_upgrade_jindodata_emr-next.md)
   和 [EMR 集群 JindoSDK 升级流程](/docs/user/4.x/4.5.x/emr_upgrade_jindosdk_emr-next.md)；