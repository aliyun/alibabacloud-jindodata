# Flink 使用 JindoSDK 访问 OSS 与 JindoFS

---

## 环境要求

在集群上有开源版本 Flink 软件，版本不低于 1.10.1

## 为什么 Flink 需要使用 JindoSDK 访问 OSS 与 JindoFS

Apache Flink 是一种当前业界流行的开源大数据流式计算引擎，支持 “严格一次”（EXACTLY_ONCE）语义写入部分存储介质。Aliyun Object Storage Service (Aliyun OSS) 与 JindoFS 则属于阿里云旗下的核心产品，提供对象存储或文件系统等语义，凭借优异的性能和安全性已服务于海量用户。

目前，开源版本 Flink 对流式写入 Aliyun OSS 与 JindoFS 尚不能支持 EXACTLY_ONCE 语义，如有该需求则需要使用 JindoSDK。

## SDK 配置

需要在所有 Flink 节点进行配置。在每个节点 Flink 根目录下的 lib 文件夹，放置两个 .jar 文件：
* jindo-flink-sink.jar
* jindofs-sdk-${version}.jar

[下载页面](/docs/jindofs_sdk_download.md)

## 如何使用

如果已经按照上一节配置好 SDK，则可以直接使用。例如，如果有一个写入 HDFS 的流式作业，若要改为写入 OSS，则只需将路径改写为 OSS 路径即可，无需做其他额外修改或配置。写入 OSS 以 oss:// 为前缀，写入 JindoFS 以 jfs:// 为前缀。

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

下文中，outputStream 是一个预先形成的 DataStream&lt;String&gt; 对象，并期望将其写入 JindoFS，那么用户可以这样添加 sink：
```
String outputPath = "jfs://<user-defined-jfs-namespace>/<user-defined-jfs-dir>"
StreamingFileSink<String> sink = StreamingFileSink.forRowFormat(
        new Path(outputPath),
        new SimpleStringEncoder<String>("UTF-8")
).build();
outputStream.addSink(sink);
```
最后用 env.execute() 执行 Flink 作业即可。
如果是写入 OSS，则只需将路径改写为：
```
String outputPath = "oss://<user-defined-oss-bucket>/<user-defined-oss-dir>"
```

## 高级设置：自定义参数

用户在提交 Flink 作业时，可以自定义配置一些参数，以开启或控制特定功能。例如，以 yarn-cluster 模式提交时，可以通过 -yD 进行配置，方式类似于：
```
<flink_home>/bin/flink run -m yarn-cluster -yD key1=value1 -yD key2=value2 ...
```

目前支持通过配置开启 “熵注入” (entropy injection) 功能，或者控制 “分片上传” 的并行度。

#### 熵注入

“熵注入” (entropy injection) 功能的目的是，将写入路径的一段特定字符串匹配出来，用一段随机的字符串进行替换，以削弱所谓 “片区” (sharding) 效应，提高写入效率。如果是写入 JindoFS (Block 或 Cache 模式)，则需提供下列配置：
```
jfs.entropy.key=<user-defined-key>
jfs.entropy.length=<user-defined-length>
```
如此，每次打开写一个新文件时，路径中与 <user-defined-key> 相同的字符串会被替换为一个随机字符串，随机串的长度为 <user-defined-length>。配置该参数时，<user-defined-length> 必须大于零。
如果是写入 OSS，则配置项需变更为：
```
oss.entropy.key=<user-defined-key>
oss.entropy.length=<user-defined-length>
```
#### 分片上传并行度

当写入场景为 OSS 或 JindoFS Cache 模式时（Block 模式不在此列），本文介绍的功能会自动调用高效的 “分片上传” (Multipart Upload) 机制，将待上传的文件分为多个数据块 (part) 分别上传，最后组合。目前支持配置参数 oss.upload.max.concurrent.uploads，用来控制上传数据块 (part) 的并行度，如果设置较高的数值则可能会提高写入效率（但也会占用更多资源）。默认情况下，该值为当前可用的处理器数量。
