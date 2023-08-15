# Hadoop 使用 JindoSDK 访问 OSS

JindoSDK 是一个简单易用面向 Hadoop/Spark 生态的 OSS 客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现。

即使您使用 JindoSDK 仅仅作为 OSS 客户端，相对于 Hadoop 社区 OSS 客户端实现，您还可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈）。

## 步骤

### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz，[下载页面](/docs/user/6.x/6.0.0/jindodata_download.md)。

[《在非 EMR 环境中完整安装部署 JindoSDK》](/docs/user/6.x/jindosdk/jindosdk_deployment.md)

[《在 Hadoop 环境最简安装部署 JindoSDK》](/docs/user/6.x/jindosdk/jindosdk_deployment_lite_hadoop.md)

[《在多平台环境安装部署 JindoSDK》](/docs/user/6.x/jindosdk/jindosdk_deployment_multi_platform.md)

### 2. 使用 JindoSDK 访问 OSS
用Hadoop Shell访问OSS，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> oss://<bucket>/
```

* ls 操作
```
hadoop fs -ls oss://<bucket>/
```

* mkdir 操作
```
hadoop fs -mkdir oss://<bucket>/<path>
```

* rm 操作
```
hadoop fs -rm oss://<bucket>/<path>
```

<img src="/docs/user/4.x/4.0.0/oss/pic/jindofs_sdk_cmd.png#pic_center" />

更多 Hadoop 命令参见 [《通过 Hadoop Shell 命令访问 OSS/OSS-HDFS》](/docs/user/6.x/oss/usages/oss_hadoop_shell.md)

### 3. 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](/docs/user/6.x/jindosdk/jindosdk_configuration.md)