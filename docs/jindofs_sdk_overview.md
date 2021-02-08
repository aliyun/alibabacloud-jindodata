### 环境要求
* JDK 1.8及以上

* Hadoop 2.3+ 版本，请下载 <a href="jindofs_sdk_how_to.md#发布日志" target="_blank">jindofs-sdk.jar</a>, 
  该版本基于 native 代码实现，支持功能较丰富
(2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈)

* 如果您的 Linux 版本较低，出现 glibc 或其他不兼容问题，请下载 [jindofs-sdk-lite.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-3.0.0-lite.jar), 该版本基于 Java 代码实现，支持基本功能

### 什么是 JindoFS SDK

JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现。

通过 JindoFS SDK，可以在 Hadoop 环境中直接使用 `oss://bucket/` 的方式访问阿里云 OSS 上的内容。

例如:
```
hadoop dfs -ls oss://bucket/dir
```
或者
````java
FileSystem fs = FileSystem.get(URI.create("oss://<bucket>/"), new Configuration());

// 创建文件夹
fs.mkdir(new Path("oss://<bucket>/test/dir1"));
    
// 读文件：
InputStream in = fs.open(new Path("oss://<bucket>/test/file1"));
in.read();
in.close();

...
````

### JindoFS SDK 使用

* [《阿里云 JindoFS SDK 全面开放使用，OSS 文件各项操作性能得到大幅提升》](https://developer.aliyun.com/article/767222)

* [自建 Hadoop 环境集成 JindoFS/OSS 的使用说明](jindofs_sdk_how_to.md) (支持 Hive, Spark, Presto, Impala, Hbase and Flink)

* [JindoFS OSS SDK 和 Hadoop-OSS-SDK 性能对比结果](jindofs_sdk_vs_hadoop_sdk.md) (JindoFS SDK在put、get、mv、delete操作上性能均显著好于Hadoop-OSS-SDK）

* [Presto 集群使用 JindoSDK 集成 OSS 访问](jindosdk_on_presto.md)

* [Flink 使用 JindoSDK (更新中)](#)