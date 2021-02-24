# Flink 使用 JindoFS SDK 访问 JindoFS

---

## SDK 配置

配置方法参见《Flink 使用 JindoFS SDK》中 [SDK 配置](/docs/flink/jindofs_sdk_on_flink.md#sdk-配置)

## 如何使用

配置好 SDK 后，无需额外配置，以常规 Flink 流式作业方法使用即可，注意使用正确的路径。写入 JindoFS 须以 jfs:// 为前缀。

JindoFS 介绍和使用参见文档首页 [关于 JindoFS](/README.md#关于-jindofs)

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

下文中，outputStream 是一个预先形成的 DataStream&lt;String&gt; 对象，并期望将其写入 JindoFS，那么可以这样添加 sink：
```
String outputPath = "jfs://<user-defined-jfs-namespace>/<user-defined-jfs-dir>"
StreamingFileSink<String> sink = StreamingFileSink.forRowFormat(
        new Path(outputPath),
        new SimpleStringEncoder<String>("UTF-8")
).build();
outputStream.addSink(sink);
```
最后用 env.execute() 执行 Flink 作业即可。

## 高级使用：自定义参数

用户在提交 Flink 作业时，可以自定义配置一些参数，以开启或控制特定功能。例如，以 yarn-cluster 模式提交时，可以通过 -yD 进行配置，方式类似于：
```
<flink_home>/bin/flink run -m yarn-cluster -yD key1=value1 -yD key2=value2 ...
```

目前支持通过配置开启 “熵注入” (entropy injection) 功能：

#### 熵注入

“熵注入” (entropy injection) 功能的目的是，将写入路径的一段特定字符串匹配出来，用一段随机的字符串进行替换，以削弱所谓 “片区” (sharding) 效应，提高写入效率。配置参数：
```
jfs.entropy.key=<user-defined-key>
jfs.entropy.length=<user-defined-length>
```
如此，每次打开写一个新文件时，路径中与 &lt;user-defined-key&gt; 相同的字符串会被替换为一个随机字符串，随机串的长度为 &lt;user-defined-length&gt;。配置该参数时，&lt;user-defined-length&gt; 必须大于零。
