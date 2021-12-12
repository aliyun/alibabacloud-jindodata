# Flume 使用 JindoSDK 写入 OSS

---

## 环境要求

在集群上已经部署 Flume，已部署 JindoSDK 4.0 以上版本。

## 为什么 Flume 需要使用 JindoSDK 写入 OSS

Flume 通过 flush() 调用保证事务性写入，OSS 本身不支持 Flush 功能，通过 JindoSDK 写入 OSS，虽然不能让 flush 后的数据立刻可见，但是可以保证 flush() 后的数据不丢失，Flume 作业失败后，可以使用 JindoSDK 命令恢复 flush 过的数据。

## SDK 配置

需要在 Flume 节点进行配置。在每个节点 Flume 根目录下的 lib 文件夹，放置 JindoSDK：
* jindosdk-${version}.jar

[下载页面](../jindosdk_download.md)

## 如何使用

配置好 AK、Secret 等配置后，还需要添加如下配置到core-site.xml，同时写入 OSS (Aliyun Object Storage Service) 须以 oss:// 为前缀。

| 配置项                          | 建议值 | 描述                                                         |
| ------------------------------- | ------ | ------------------------------------------------------------ |
| fs.oss.flush.enable | true   | 是否开启Flush和Recover功能，开启时需要设置为true。           |
| fs.oss.flush.staging.path | /tmp   | 为Flush的数据和Manifest的暂存区，默认值为/tmp。例如：在使用默认值时，如果文件的写入路径是：`oss://test-bucket/dir1/file1`，则Staging的路径为`oss://test-bucket/tmp/dir1/file1`。 |



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



## Flush 文件恢复

**在 EMR 集群内**

可以使用 Recover 命令行恢复。

```bash
jindo jfs -recover [-R]
                   [-flushStagingPath {flushStagingPath}]
                   [-accessKeyId ${accessKeyId}]
                   [-accessKeySecret ${accessKeySecret}]
                   <path>
```



| 参数              | 描述                                                         |
| ----------------- | ------------------------------------------------------------ |
| -R                | 是否递归Recover，恢复文件夹时需要添加该参数。                |
| -flushStagingPath | 为Flush的数据和Manifest的暂存区，默认值为/tmp。</br>例如：在使用默认值时，如果文件的写入路径是：`oss://test-bucket/dir1/file1`，则Staging的路径为`oss://test-bucket/tmp/dir1/file1`。 |
| -accessKeyId      | 阿里云账号的AccessKey ID。                                   |
| -accessKeySecret  | 阿里云账号的AccessKey Secret。                               |
| path              | Flush的数据的写入路径。例如：`oss://test-bucket/dir1/file1`或`oss://test-bucket/dir1`。 |

注：如需递归恢复(-R)，建议先停止 Flume 任务，避免 Flume 任务运行异常。

**对于非 EMR 集群**

可通过 OssFileSystem 的 Recover 接口恢复文件或目录。本示例为恢复文件夹。

```java
OssFileSystem ossFileSystem = (OssFileSystem) fs;
boolean isFolder = true;
ossFileSystem.recover(path, isFolder);
```

