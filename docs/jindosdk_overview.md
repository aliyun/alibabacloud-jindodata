### 环境要求
* JDK 1.8 及以上

* Hadoop 2.3+ 版本，请下载 [jindosdk-xxx.jar](jindosdk_download.md)，该版本基于 native 代码实现，支持功能较丰富

### 什么是 JindoSDK

JindoSDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现。

通过 JindoSDK，可以在 Hadoop 环境中直接使用 `oss://bucket/` 的方式访问阿里云 OSS 上的内容。

例如：

````bash
hadoop dfs -ls oss://bucket/dir
````

### 为什么使用 JindoSDK

- 优异的性能表现：和开源版本的 Hadoop-OSS-SDK 进行对比，各项操作性能均显著好于Hadoop-OSS-SDK（详细测试见 [相关文章](jindosdk_overview.md#相关文章) ）；

- 良好的兼容性：兼容市面上大部分 Hadoop 版本，JindoSDK 在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindofs/issues/new) 向我们反馈）；

- 专业团队维护：阿里云 EMR Hadoop 团队维护，JindoSDK Hadoop SDK 在阿里云 EMR 等产品中广泛使用；

- 功能更新快：及时跟进 OSS 最新特性和优化，版本更新及时。



### 部署 JindoSDK 访问 DLS

* [Hadoop](jindosdk_how_to_hadoop.md)
* [Hive](jindosdk_on_hive.md)
* [Spark](spark/jindosdk_on_spark.md)
* [Presto](jindosdk_on_presto.md)
* [Druid](jindosdk_on_druid.md)
* [Impala](impala/jindosdk_on_impala.md)

### 部署 JindoSDK 访问 OSS

- [Flume](flume/jindosdk_on_flume.md)
