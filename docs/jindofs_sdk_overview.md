### 环境要求
* JDK 1.8及以上

* Hadoop 2.3+ 版本，请下载 [jindofs-sdk.jar](/docs/jindofs_sdk_download.md),
  该版本基于 native 代码实现，支持功能较丰富
(2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈)

* 如果您的 Linux 版本较低，出现 glibc 或其他不兼容问题，请下载 [jindofs-sdk-lite.jar](/docs/jindofs_sdk_download.md), 该版本基于 Java 代码实现，支持基本功能

### 什么是 JindoFS SDK

JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现。

通过 JindoFS SDK，可以在 Hadoop 环境中直接使用 `oss://bucket/` 的方式访问阿里云 OSS 上的内容。

例如:
```
hadoop dfs -ls oss://bucket/dir
```


### 为什么使用 JindoFS SDK

* [使用 JindoFS SDK 大幅提升 OSS 文件各项操作性能](https://developer.aliyun.com/article/767222)

* [JindoFS OSS SDK 和 Hadoop-OSS-SDK 性能对比结果](jindofs_sdk_vs_hadoop_sdk.md)

### JindoFS SDK 使用

* [Hadoop 使用 JindoFS SDK](jindofs_sdk_how_to.md) 

* [Presto 使用 JindoFS SDK](jindosdk_on_presto.md) 

* [Flink 使用 JindoFS SDK](/docs/flink/jindofs_sdk_on_flink.md) 

* [Sqoop 使用 kiteSDK](kitesdk_on_sqoop.md)
