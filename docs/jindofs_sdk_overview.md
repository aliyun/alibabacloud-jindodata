### 环境要求
* JDK 1.8 及以上

* Hadoop 2.3+ 版本，请下载 [jindofs-sdk.jar](/docs/jindofs_sdk_download.md)，该版本基于 native 代码实现，支持功能较丰富

### 什么是 JindoFS SDK

JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现。

通过 JindoFS SDK，可以在 Hadoop 环境中直接使用 `oss://bucket/` 的方式访问阿里云 OSS 上的内容。

例如：

````bash
hadoop dfs -ls oss://bucket/dir
````

### 为什么使用 JindoFS SDK

- 优异的性能表现：和开源版本的 Hadoop-OSS-SDK 进行对比，各项操作性能均显著好于Hadoop-OSS-SDK（详细测试见 [相关文章](jindofs_sdk_overview.md#相关文章) ）；

- 良好的兼容性：兼容市面上大部分 Hadoop 版本，JindoFS SDK 在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindofs/issues/new) 向我们反馈）；

- 专业团队维护：阿里云 EMR Hadoop 团队维护，JindoFS Hadoop SDK 在阿里云 EMR 等产品中广泛使用；

- 功能更新快：及时跟进 OSS 最新特性和优化，版本更新及时。



### 部署 JindoFS SDK 访问 OSS

* [Hadoop](jindofs_sdk_how_to_hadoop.md)
* [Hive](jindosdk_on_hive.md)
* [Spark](spark/jindosdk_on_spark.md)
* [Presto](jindosdk_on_presto.md)
* [Flink](flink/jindofs_sdk_on_flink_for_oss.md)
* [Flume](flume/jindofs_sdk_on_flume_for_oss.md)
* [Sqoop](kitesdk_on_sqoop.md)
* [Druid](jindosdk_on_druid.md)
* [Impala](impala/jindosdk_on_impala.md)
* [CDH](jindofs_sdk_how_to_hadoop_cdh.md)

### 部署 JindoFS SDK 访问 JindoFS

- [Hadoop](jindofs_sdk_how_to_jfs.md)
- [Flume](flume/jindofs_sdk_on_flume_for_jfs.md)
- [Flink](flink/jindofs_sdk_on_flink_for_jfs.md)

### 相关文章

* [使用 JindoFS SDK 大幅提升 OSS 文件各项操作性能](https://developer.aliyun.com/article/767222)
