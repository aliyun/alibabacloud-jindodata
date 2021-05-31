# Flume 使用 JindoFS SDK 写入 JindoFS Block 模式

---

## 环境要求

已有 EMR JindoFS Block 模式集群，已部署 SMARTDATA 3.1 以上版本。

## 为什么 Flume 需要使用 JindoFS SDK 写入 JindoFS Block 模式

Flume 通过 flush() 调用保证事务性写入，JindoFS Block 在 SMARTDATE 3.2 以上版本默认支持 Flush 功能，直接配置 Sink 即可。

## SDK 配置

需要在 Flume 节点进行配置 JindoFS SDK，参考 [《JindoFS 客户端部署》](../jindofs_sdk_how_to_jfs.md) 。

在每个节点 Flume 根目录下的 lib 文件夹，放置 JindoSDK：

* jindofs-sdk-${version}.jar

[下载页面](/docs/jindofs_sdk_download.md)



## Sink 配置示例



```properties
# 配置 JFS Sink
xxx.sinks.jfs_sink.hdfs.path = jfs://${your_ns_name}/flume_dir/%Y-%m-%d/%H

# Sink参数，batchSize 需要设置大一些，推荐每次 Flush 的量在 32MB以上，否则会影响性能
xxx.sinks.jfs_sink.hdfs.batchSize = 100000

...
xxx.sinks.jfs_sink.hdfs.round = true
xxx.sinks.jfs_sink.hdfs.roundValue = 15
xxx.sinks.jfs_sink.hdfs.Unit = minute
xxx.sinks.jfs_sink.hdfs.filePrefix = your_topic
xxx.sinks.jfs_sink.rollSize = 3600
xxx.sinks.jfs_sink.threadsPoolSize = 30
...

```



