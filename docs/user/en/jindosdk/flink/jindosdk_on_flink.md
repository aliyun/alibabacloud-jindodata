# Processing Data on OSS with Flink Using JindoSDK

## System Requirements

Ensure that you have an open-source Flink installation, version 1.10.1 or newer.

**Note:** Compatibility with Flink versions 1.16.0 and above has not been verified yet.

## Why Flink Needs JindoSDK for Accessing OSS

When requiring Flink to maintain **EXACTLY_ONCE** semantics while streaming writing data to Alibaba Cloud OSS, JindoSDK is necessary.

Apache Flink is a popular open-source stream processing engine supporting **EXACTLY_ONCE** write semantics for some storage mediums. However, the open-source Flink does not natively support **EXACTLY_ONCE** for streaming writes to Aliyun Object Storage Service (OSS). Therefore, using JindoSDK becomes essential if this guarantee is needed.

**Note:** Starting from Flink 1.16.0, native support for streaming writes to Aliyun OSS was introduced. The main differences between Flink's built-in implementation and JindoSDK are:
- JindoSDK supports both Aliyun OSS object storage and Aliyun OSS-HDFS file storage. Learn more about [OSS-HDFS](https://help.aliyun.com/document_detail/405089.htm).
- JindoSDK uses a native approach in its implementation.

## Configuring JindoSDK

You need to configure JindoSDK on all Flink nodes. Place `.jar` files in each node's Flink `lib` folder:
* `jindo-flink-${version}-full.jar`

This jar comes bundled in `jindosdk-${version}.tar.gz` available at the [download page](../jindosdk_download.md). After extracting, you'll find it inside the `plugins/flink/` directory.

Additionally, if there's an existing Flink-provided Flink-OSS Connector present in your cluster's `lib` directory or `plugins/oss-fs-hadoop`, remove it:
* `flink-oss-fs-hadoop-${flink-version}.jar`

JindoSDK currently offers multi-platform support. For details on supported platforms and additional resources needed, refer to [Installing and Deploying JindoSDK on Multi-platform Environments](../jindosdk_deployment_multi_platform.md).

## Usage

Once configured, you can use Flink as usual, just make sure to specify correct paths. Prefix paths with `oss://` when writing to OSS (Alibaba Cloud Object Storage Service).

## Example Program

Assuming you've correctly set up the SDK, the following simple program should run successfully:

(1) General Configuration

* Enable Flink checkpoints. For example, given a `StreamExecutionEnvironment` called `env`, you can enable checkpoints as follows:
```java
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.enableCheckpointing(<userDefinedCheckpointInterval>, CheckpointingMode.EXACTLY_ONCE);
```
* Use a replayable source like Kafka.

(2) Sample Program

Suppose you have a pre-formed `DataStream<String>` called `outputStream` that you want to write to OSS. You would add a sink like this:
```java
String outputPath = "oss://<user-defined-bucket>/<user-defined-dir>";
StreamingFileSink<String> sink = StreamingFileSink.forRowFormat(
        new Path(outputPath),
        new SimpleStringEncoder<String>("UTF-8"))
        .build();
outputStream.addSink(sink);
```
Finally, execute your Flink job using `env.execute()`.

## Advanced Usage: Custom Parameters

Users can customize certain parameters when submitting Flink jobs. For instance, when submitting a job in `yarn-cluster` mode, you can do so with `-yD` flags:
```
<flink_home>/bin/flink run -m yarn-cluster -yD key1=value1 -yD key2=value2 ...
```

Currently, you can configure features like "Entropy Injection" and control the parallelism of "Part Upload".

### Entropy Injection

"Entropy Injection" replaces a specific string pattern in the write path with a random string to mitigate "sharding" effects and improve write efficiency. To enable this feature, set these configuration parameters:
```
oss.entropy.key=<user-defined-key>
oss.entropy.length=<user-defined-length>
```
When a new file is opened for writing, any occurrence of `<user-defined-key>` in the path will be replaced with a random string of length `<user-defined-length>`. `<user-defined-length>` must be greater than zero.

### Part Upload Parallelism

Writing to OSS utilizes efficient "Part Upload" (Multipart Upload) where data is split into parts and uploaded separately before being combined. You can control the parallelism of part uploads using `oss.upload.max.concurrent.uploads`. A higher value may increase write throughput but also consumes more resources. By default, this value equals the number of available processors.