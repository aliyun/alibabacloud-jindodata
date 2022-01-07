# Flume 使用 JindoSDK 写入阿里云 OSS-HDFS 服务（JindoFS 服务）

## 环境要求

在集群上已经部署 Flume，已部署 JindoSDK 4.0 以上版本。

## 为什么 Flume 需要使用 JindoSDK 写入 JindoFS 服务

Flume 通过 flush() 调用保证事务性写入，通过 JindoSDK 写入 JindoFS 服务，可以让 flush 后的数据立刻可见，保证数据不丢失。

## SDK 配置

需要在 Flume 节点进行配置。在每个节点 Flume 根目录下的 lib 文件夹，放置 JindoSDK。

下载并解压最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](../jindosdk_download.md))

````
cp jindosdk-x.x.x/lib/*.jar  $FLUME_HOME/jars/
````

## Sink 配置示例

```properties
# 配置 OSS Sink
xxx.sinks.oss_sink.hdfs.path = oss://${your_bucket}/flume_dir/%Y-%m-%d/%H

# Sink参数，batchSize 需要设置大一些，推荐每次 Flush 的量在 32MB以上，否则会影响性能
xxx.sinks.oss_sink.hdfs.batchSize = 100000

...
xxx.sinks.oss_sink.hdfs.round = true
xxx.sinks.oss_sink.hdfs.roundValue = 15
xxx.sinks.oss_sink.hdfs.Unit = minute
xxx.sinks.oss_sink.hdfs.filePrefix = your_topic
xxx.sinks.oss_sink.rollSize = 3600
xxx.sinks.oss_sink.threadsPoolSize = 30
...

```

