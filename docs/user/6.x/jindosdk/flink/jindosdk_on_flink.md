# Flink 使用 JindoSDK 处理 OSS 上的数据

## 环境要求

在集群上有开源版本 Flink 软件，版本不低于 1.10.1。

注：Flink 1.16.0 及更高版本的可用性尚未得到验证。

## 为什么 Flink 需要使用 JindoSDK 访问 OSS

若需要 Flink 保持 EXACTLY_ONCE 语义流式写入 Aliyun OSS，则需要使用 JindoSDK。

Apache Flink 是一种当前业界流行的开源大数据流式计算引擎，支持 “严格一次”（EXACTLY_ONCE）语义写入部分存储介质。Aliyun Object Storage Service (Aliyun OSS) 则是阿里云旗下的核心产品之一，提供云原生对象存储服务，凭借优异的性能和安全性已服务于海量用户。

目前，开源版本 Flink 对流式写入 Aliyun OSS 尚不能支持 EXACTLY_ONCE 语义，如有该需求则需要使用 JindoSDK。

注：Apache Flink 自 1.16 版本起原生提供了流式写入 Aliyun OSS 对象存储的支持。JindoSDK 提供的实现与 Apache Flink 的自有实现有两个主要区别：
 - JindoSDK 的实现能够同时支持 Aliyun OSS 对象存储和 Aliyun OSS-HDFS 文件存储。对 OSS-HDFS 的介绍参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)
 - JindoSDK 的实现底层采用了 Native 方案。

## JindoSDK 配置

需要在所有 Flink 节点进行配置。在每个节点 Flink 根目录下的 lib 文件夹，放置 .jar 文件：
* jindo-flink-${version}-full.jar

该 jar 包含在 jindosdk-${version}.tar.gz ([下载页面](/docs/user/6.x/6.1.1/jindodata_download.md))，解压缩后可在 plugins/flink/ 目录下找到。

另外，如果集群内存在 Apache Flink 提供的自带 Flink OSS Connector，需移除。移除方法为，从 Flink 集群的 lib 目录或 plugins/oss-fs-hadoop 移走如下 jar 包：
* `flink-oss-fs-hadoop-${flink-version}.jar`

注：JindoSDK 目前提供多平台支持，当前支持的平台以及需要额外放置的资源，参考 [在多平台环境安装部署 JindoSDK](/docs/user/6.x/jindosdk/jindosdk_deployment_multi_platform.md)

## 如何使用

配置好 SDK 后，无需额外配置，以常规 Flink 流式作业方法使用即可，注意使用正确的路径。写入 OSS (Aliyun Object Storage Service) 须以 oss:// 为前缀。

## 示例

下面是一个简单的示例程序，如果已经按照上一节配置好 SDK，则下列程序应能正确运行：

(1) 通用配置

* 打开 Flink 的 “检查点”（Checkpoint）。例如，env 是一个已经建立的 StreamExecutionEnvironment，可以通过下面的方式建立：
```
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
```
则可以这样启用 checkpoint：
```
env.enableCheckpointing(<userDefinedCheckpointInterval>, CheckpointingMode.EXACTLY_ONCE);
```

* 使用可重发的数据源，例如 Kafka

(2) 示例程序

下文中，outputStream 是一个预先形成的` DataStream<String>`对象，并期望将其写入 OSS，那么可以这样添加 sink：
```
String outputPath = "oss://<user-defined-bucket>/<user-defined-dir>"
StreamingFileSink<String> sink = StreamingFileSink.forRowFormat(
        new Path(outputPath),
        new SimpleStringEncoder<String>("UTF-8")
).build();
outputStream.addSink(sink);
```
最后用 `env.execute()` 执行 Flink 作业即可。

## 高级使用：自定义参数

用户在提交 Flink 作业时，可以自定义配置一些参数，以开启或控制特定功能。例如，以 yarn-cluster 模式提交时，可以通过 -yD 进行配置，方式类似于：
```
<flink_home>/bin/flink run -m yarn-cluster -yD key1=value1 -yD key2=value2 ...
```

目前支持通过配置开启 “熵注入” (entropy injection) 功能，或者控制 “分片上传” 的并行度。

#### 熵注入

“熵注入” (entropy injection) 功能的目的是，将写入路径的一段特定字符串匹配出来，用一段随机的字符串进行替换，以削弱所谓 “片区” (sharding) 效应，提高写入效率。配置参数：
```
oss.entropy.key=<user-defined-key>
oss.entropy.length=<user-defined-length>
```
如此，每次打开写一个新文件时，路径中与`<user-defined-key>`相同的字符串会被替换为一个随机字符串，随机串的长度为`<user-defined-length>`。配置该参数时，`<user-defined-length>`必须大于零。

#### 分片上传并行度

写入 OSS 会自动调用高效的 “分片上传” (Multipart Upload) 机制，将待上传的文件分为多个数据块 (part) 分别上传，最后组合。目前支持配置参数`oss.upload.max.concurrent.uploads`，用来控制上传数据块 (part) 的并行度，如果设置较高的数值则可能会提高写入效率（但也会占用更多资源）。默认情况下，该值为当前可用的处理器数量。
