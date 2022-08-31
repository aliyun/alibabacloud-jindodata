# 已知问题

1. JINDOSDK 4.3.0 （EMR-3.40.0/EMR-5.6.0）由于显示目录时间会导致 ls 性能出现一定程度退化，暂不显示目录时间，如需显示时间需更新到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](emr_upgrade.md)；
2. JINDOSDK 4.3.0 （EMR-3.40.0/EMR-5.6.0）使用 MagicCommitter 时，会有频繁调用 uploadPart 问题，出现 “Part number must be an integer between 1 and 10000” 异常，如需解决此问题需要更新到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoSDK 升级流程](emr_upgrade.md)；
3. JindoFSx 4.3.0 服务端读取缓存数据在部分路径下存在出错处理异常，相关错误未能正确返回客户端，导致客户端返回错误的数据内容，需更新JindoFSx到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](emr_upgrade_jindodata.md)；
4. JindoFSx 4.3.0 服务端处理内存缓存预加载命令存在问题，导致加载到内存中的数据内容可能发生错误，造成后续读取到错误的数据内容，需更新JindoFSx到4.3.1及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](emr_upgrade_jindodata.md)；
5. JindoFSx 4.3.0/4.3.1 服务端存在文件句柄泄漏问题，长时间运行后可能达到操作系统规定的进程上限，导致服务无法打开新的文件句柄造成服务不可用，需更新JindoFSx到4.3.2及以上版本，EMR集群升级可参考[EMR 集群 JindoData 升级流程](emr_upgrade_jindodata.md)。
