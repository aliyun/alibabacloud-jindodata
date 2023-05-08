# Flink 使用 JindoSDK 处理阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据

## 环境要求

在集群上有开源版本 Flink 软件，版本不低于 1.10.1。

## 为什么 Flink 需要使用 JindoSDK 访问 OSS-HDFS 服务

目前，开源版本 Flink 对流式写入 OSS-HDFS 服务并不支持，更不能支持 “严格一次”（EXACTLY_ONCE）语义，如有该需求则需要使用 JindoSDK。

## JindoSDK 配置

需要在所有 Flink 节点进行配置。在每个节点 Flink 根目录下的 lib 文件夹，放置下列 .jar 文件：
* jindo-flink-${version}-full.jar

该 jar 包含在 jindosdk-${version}.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))，解压缩后可在 plugins/flink/ 目录下找到。

## 如何使用

配置好 SDK 后，无需额外配置，以常规 Flink 流式作业方法使用即可，注意使用正确的路径。写入 OSS-HDFS 服务须以 oss:// 为前缀。JindoFS 服务与阿里云 OSS 使用相同的前缀，JindoSDK 内会自动进行识别。二者的联系，参考 [JindoFS 服务介绍](/docs/user/4.x/4.6.x/4.6.7/jindofs/jindo_dls_quickstart.md) 。

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
String outputPath = "oss://<user-defined-bucket.dls-endpoint>/<user-defined-dir>"
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

目前支持通过配置开启 “熵注入” (entropy injection) 功能。

#### 熵注入

“熵注入” (entropy injection) 功能的目的是，将写入路径的一段特定字符串匹配出来，用一段随机的字符串进行替换，以削弱所谓 “片区” (sharding) 效应，提高写入效率。配置参数：
```
oss.entropy.key=<user-defined-key>
oss.entropy.length=<user-defined-length>
```
如此，每次打开写一个新文件时，路径中与`<user-defined-key>`相同的字符串会被替换为一个随机字符串，随机串的长度为`<user-defined-length>`。配置该参数时，`<user-defined-length>`必须大于零。
