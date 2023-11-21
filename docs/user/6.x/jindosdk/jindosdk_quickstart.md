# Hadoop 环境使用 JindoSDK 快速入门

JindoSDK 全面兼容 Hadoop FileSystem 接口，提供了更好的兼容性和易用性，又能兼具OSS的性能和成本优势。

目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈）。

本文档假设您在 Linux x86 环境中，已经安装部署好了 Hadoop 环境，如需在其他平台使用，请参考[《在多平台环境安装部署 JindoSDK》](/docs/user/6.x/jindosdk/jindosdk_deployment_multi_platform.md)。

## 步骤

### 1. 安装部署 JindoSDK

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz，[下载页面](/docs/user/6.x/6.1.2/jindodata_download.md)。

完整部署 JindoSDK，参见 [文档链接](/docs/user/6.x/jindosdk/jindosdk_deployment_hadoop.md)

最简部署 JindoSDK，参见 [文档链接](/docs/user/6.x/jindosdk/jindosdk_deployment_lite_hadoop.md)

### 2. 使用 JindoSDK 访问 OSS
用 Hadoop Shell 访问 OSS，下面列举了几个常用的命令。

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