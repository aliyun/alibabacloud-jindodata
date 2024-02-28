# Flume 使用 JindoSDK 写入 OSS

## 环境要求

在集群上已经部署 Flume，已部署 JindoSDK 6.0 以上版本。

## JindoSDK 安装部署

需要在 Flume 节点进行配置。在每个节点 Flume 根目录下的 lib 文件夹，放置 JindoSDK。

下载并解压最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/6.x/6.3.2/jindodata_download.md))

````
cp jindosdk-x.x.x/lib/*.jar  $FLUME_HOME/jars/
````

## 配置

### Flume Sink 配置

```properties
# 配置OSS Sink。your_bucket填写为已开启OSS-HDFS服务的Bucket。
xxx.sinks.oss_sink.hdfs.path = oss://${your_bucket}/flume_dir/%Y-%m-%d/%H

# 写入Flume事务中最大的event数量。推荐每次Flush的量在32 MB以上，避免Flush过小影响整体性能以及产生大量的staging文件。
# batchSize单位为event数量（即日志行数），设置时需要先评估event平均大小（例如200 Byte），假设每次Flush的大小预期为32 MB，则batchSize约为160000（32 MB / 200 Byte）。
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

### OSS-HDFS 场景 core-sites.xml 配置

推荐每次 Flush 的量在 32 MB以上，避免 Flush 过于频繁影响整体性能以及产生小块。

```xml
<configuration>

    <property>
        <!-- false表示不执行hflush，仅依靠close来确保执行文件写入，不推荐关闭 -->
        <name>fs.oss.hflush.enable</name>
        <value>true</value>
    </property>

    <property>
        <!-- 当前文件执行hflush的次数达到该数量后，才触发一次真正的flush（0表示不根据该数量来触发Flush） -->
        <name>fs.oss.hflush.interval.count</name>
        <value>0</value>
    </property>

    <property>
        <!-- 当前文件写入达到该大小后，才触发一次真正的flush（0表示不根据写入大小来触发Flush），单位：字节-->
        <name>fs.oss.hflush.interval.size</name>
        <value>0</value>
    </property>
</configuration>
```

ps: 如 `fs.oss.hflush.interval.count` 和 `fs.oss.hflush.interval.size` 均不为0，则两个条件均满足时才执行hflush。
