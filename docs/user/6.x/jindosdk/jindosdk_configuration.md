# 客户端常用配置

## OSS/OSS-HDFS

### 日志配置项

| 配置项                                        |  类型  | 默认值              | 说明                                                                                                            | 版本     |
|--------------------------------------------| --- |------------------|---------------------------------------------------------------------------------------------------------------|--------|
| logger.dir                                 |  字符串 | /tmp/bigboot-log | 日志目录，不存在会创建，logger.consolelogger 和 logger.jnilogger 均为 false 时生效。                                             | 4.3.0+ |
| logger.sync                                |  布尔值 | false            | 是否同步输出日志，false表示异步输出                                                                                          | 4.3.0+ |
| logger.consolelogger                       |  布尔值 | false            | 打印日志到终端                                                                                                       | 4.3.0+ |
| logger.jnilogger                           |  布尔值 | false            | 打印日志到log4j                                                                                                    | 4.3.0+ |
| logger.level                               |  整型  | 2                | 关闭终端日志，使用文件日志时：日志等级<=1，表示WARN；日志等级>1，表示INFO。开启终端日志时，日志等级范围为0-6，分别表示：TRACE、DEBUG、INFO、WARN、ERROR、CRITICAL、OFF。 | 4.3.0+ |
| logger.verbose                             |  整型  | 0                | 输出大于等于该等级的VERBOSE日志，等级范围为0-99，0表示不输出                                                                          | 4.3.0+ |
| logger.cleaner.enable                      |  布尔值 | false            | 是否开启日志清理                                                                                                      | 4.3.0+ |
| logger.single.instance.enable              |  布尔值 | false            | 是否开启日志单实例，仅适用与常驻进程场景，多 classloader 场景不建议开启。                                                                   | 6.7.8+ |

#### Java SDK 设置 log4j.properties
```
log4j.logger.com.aliyun.jindodata=INFO
log4j.logger.com.aliyun.jindodata.common.FsStats=INFO
```

### I/O 相关配置项

#### I/O 场景通用配置项

| 配置项                                                      |  类型  | 默认值           | 说明                                                                                                                                                   | 版本                 |
|----------------------------------------------------------| --- |---------------|------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| fs.oss.tmp.data.dirs                                     |  字符串 | /tmp/         | 客户端写入时的临时文件目录，可配置多个（逗号隔开），会轮流写入，多用户环境需配置可读写权限                                                                                                        | 4.3.0+             |
| fs.oss.tmp.data.cleaner.enable                           |  布尔值 | true          | 临时文件自清理服务                                                                                                                                            | 4.3.0+             |
| fs.oss.retry.count                                       |  整型  | 5             | 访问 OSS/OSS-HDFS 失败重试次数                                                                                                                               | 4.3.0+             |
| fs.oss.retry.interval.millisecond                        |  整型  | 500           | 访问 OSS/OSS-HDFS 失败重试间隔（毫秒）                                                                                                                           | 4.3.0+             |
| fs.oss.timeout.millisecond                               |  整型  | 30000         | 请求 OSS/OSS-HDFS 超时时间（毫秒）                                                                                                                             | 4.3.0+             |
| fs.oss.connection.timeout.millisecond                    |  整型  | 3000          | 连接 OSS/OSS-HDFS 超时时间（毫秒）                                                                                                                             | 4.3.0+             |
| fs.oss.max.connections.per.host                          |  整型  | 100           | 连接 OSS/OSS-HDFS 的连接池对每个host的最大连接数（超过阈值外的连接会使用短连接）                                                                                                    | 4.3.0+             |
| fs.oss.signer.version                                    |  整型  | 0             | 签名算法版本，可选值[0，1，4]。推荐使用0，表示默认算法版本，6.3.0版本之后默认使用 V4 签名算法提高访问 OSS/OSS-HDFS，若特殊场景希望指定签名算法版本，请指定 1 或 4。                                                   | 6.3.0+             |
| fs.oss.io.timeout.millisecond                            |  整型  | 90000         | 请求 OSS/OSS-HDFS 读写操作超时时间（毫秒）                                                                                                                         | 6.6.0+ nextarch    |
| fs.oss.async.executor.number                             |  整型  | cpu核数         | 进程内 OSS/OSS-HDFS 异步io线程数                                                                                                                             | 6.6.0+ nextarch    |
| fs.oss.list.fallback.iterative                           |  布尔值  | false         | 打开后，listStatus 对于大目录 list 自动 fallback 到迭代实现。                                                                                                         | 6.8.3+ nextarch    |

#### 写场景相关配置项

| 配置项                                                      |  类型  | 默认值           | 说明                                                                                                                                                   | 版本                 |
|----------------------------------------------------------| --- |---------------|------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| fs.oss.upload.thread.concurrency                         |  整型  | MAX(cpu核数,16) | 进程内 OSS/OSS-HDFS 并发上传线程数                                                                                                                             | 4.3.0+(deprecated) |
| fs.oss.upload.queue.size                                 |  整型  | MAX(cpu核数,16) | 进程内 OSS/OSS-HDFS 并发上传任务队列大小                                                                                                                          | 4.3.0+(deprecated) |
| fs.oss.upload.max.pending.tasks.per.stream               |  整型  | 10            | 单个文件 OSS/OSS-HDFS 最大并发上传任务数                                                                                                                          | 4.3.0+(deprecated) |
| fs.oss.write.buffer.size                                 |  整型  | 1048576       | OSS/OSS-HDFS 写缓冲区大小（字节）                                                                                                                              | 4.3.0+             |
| fs.oss.flush.interval.millisecond                        |  整型  | -1            | OSS/OSS-HDFS 刷新缓冲区间隔（毫秒），小于 0 时不生效                                                                                                                   | 4.3.0+             |
| fs.oss.blocklet.size.mb                                  |  整型  | 8             | OSS 分块上传时的块大小（MB）由于分块数量最多为10000块，因此默认写入文件不能超过80GB。如果有个别文件超过80G，建议根据文件大小单独调大本配置，并同时调大请求 oss 的超时时间。如文件大小未知，或者远远超过80G（如超过160G），建议考虑使用 OSS-HDFS（无文件大小限制） | 4.5.2+             |
| fs.oss.checksum.crc64.enable                             |  布尔值 | true          | 文件级别 crc64 完整性校验，目前对写OSS-HDFS性能有较大影响，性能优先场景可以考虑关闭。                                                                                                   | 4.6.0+             |
| fs.oss.checksum.md5.enable                               |  布尔值 | false         | 请求级别 md5 完整性校验                                                                                                                                       | 4.6.0+             |
| fs.oss.upload.async.concurrency                          |  整型  | MAX(cpu核数,16) | 进程内 OSS/OSS-HDFS 异步上传并发数                                                                                                                             | 6.6.0+ nextarch    |
| fs.oss.array.block.enable                                |  布尔值  | false         | 默认关闭。打开后。客户端写入优先使用内存 buffer，单个buffer大小与 `fs.oss.blocklet.size.mb` 对齐，若内存不足，则写入磁盘。不建议在写大文件（>8M）场景使用，可能会造成内存不足。                                        | 6.7.0+ nextarch    |
| fs.oss.append.threshold.size                             |  整型  | 0             | 客户端 append 写入 OSS-HDFS 时会检查最后一个 block 是否小于`fs.oss.append.threshold.size`。若小于，则从最后一个 block 末尾追加写；否则新建一个 block 开始写。若上一个 block 是虚拟块，则新建块。               | 6.7.8+ nextarch    |
| fs.oss.flush.merge.threshold.size                        |  整型  | 1048576       | 打开后，OSS-HDFS 执行 flush 时虚拟块会合并小于`fs.oss.flush.merge.threshold.size`的 slice。                                                                           | 6.7.8+ nextarch    |

#### 读场景配置项

##### 读场景通用配置项

| 配置项                                                      |  类型  | 默认值           | 说明                                                                                                                                     | 版本                 |
|----------------------------------------------------------| --- |---------------|----------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| fs.oss.download.thread.concurrency                       |  整型  | MAX(cpu核数,16) | 进程内 OSS/OSS-HDFS 最大并发下载任务数                                                                                                             | 4.3.0+(deprecated) |
| fs.oss.download.async.concurrency                        |  整型  | MAX(cpu核数,16) | 进程内 OSS/OSS-HDFS 异步下载并发数                                                                                                               | 6.6.0+ nextarch    |

##### 默认读场景配置项

| 配置项                                           |  类型  | 默认值           | 说明                                                                                                                                     | 版本                 |
|-----------------------------------------------| --- |---------------|----------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| fs.oss.read.readahead.prefetcher.version      |  字符串 | default       | 可选值：`legacy` 原预读算法, `default` 新预读算法。新预读算法可能使用更多内存。若配置了新预读算法后发生性能下降，可能是由于内存池容量不足导致预读的块在被访问到之前就被提前逐出。为了避免该情况发生，可以考虑缩减最大预读长度，或允许预读使用更多内存。 | 6.2.0+             |
| fs.oss.read.readahead.max.buffer.count        |  整型  | 48            | 最大同时预读 OSS/OSS-HDFS 的 buffer 个数（legacy 预读算法）                                                                                           | 4.3.0+(deprecated) |
| fs.oss.read.buffer.size                       |  整型  | 1048576       | OSS/OSS-HDFS 读缓冲区大小（字节）（legacy 预读算法）                                                                                                   | 4.3.0+(deprecated) |
| fs.oss.read.readahead.pread.enable            |  布尔值 | false         | 控制随机读接口是否开启预读                                                                                                                          | 6.2.0+             |
| fs.oss.read.readahead.prefetch.size.max       |  整型  | 268435456     | 预读最大长度（单位：byte）                                                                                                                        | 6.2.0+             |
| fs.oss.read.readahead.download.block.size.min |  整型  | 1048576     | 预读单个请求最小长度（单位：byte）                                                                                                                    | 6.2.0+             |
| fs.oss.read.readahead.download.block.size.max |  整型  | 4194304     | 预读单个请求最大长度（单位：byte）                                                                                                                    | 6.2.0+             |

##### 湖表读场景相关配置项

判定为湖表文件的方式，有以下三种：

1. FileSystem 由 paimon/iceberg/hudi 创建。

2. 打开文件时，使用了 [openfile 方法（hadoop3.3.1+）](https://hadoop.apache.org/docs/r3.4.1/hadoop-project-dist/hadoop-common/filesystem/fsdatainputstreambuilder.html#Option:_fs.option.openfile.read.policy)，并指定了文件类型是columnar/parquet/orc

3. 文件末尾是 `.parquet` 或者 `.orc`

| 配置项                                                      |  类型  | 默认值           | 说明                                                                   | 版本                 |
|----------------------------------------------------------| --- |---------------|----------------------------------------------------------------------|--------------------|
| fs.oss.read.profile.enable                               |  布尔值  | true          | OSS/OSS-HDFS 在读取湖表格式文件时，默认开启针对湖表文件优化的预读算法。                           | 6.9.0+ nextarch    |
| fs.oss.read.profile.columnar.readahead.pread.enable      |  布尔值 | true          | 控制湖表文件随机读接口是否开启预读                                                    | 6.9.0+ nextarch    |
| fs.oss.read.profile.columnar.readahead.prefetch.size.max |  整型  | 67108864      | 湖表格式文件预读最大长度（单位：byte）                                                | 6.9.0+ nextarch    |
| fs.oss.read.profile.columnar.readahead.download.block.size.min |  整型  | 1048576     | 预读单个请求最大长度（单位：byte）                                                  | 6.9.0+ nextarch    |
| fs.oss.read.profile.columnar.readahead.download.block.size.max |  整型  | 1048576     | 预读单个请求最大长度（单位：byte）                                                  | 6.9.0+ nextarch    |
| fs.oss.vectored.read.min.seek.size                       |  整型  | 16384         | OSS/OSS-HDFS 在 readVectored 操作期间，对多个 FileRange 合并的最小合理寻址范围（字节）。      | 6.9.0+ nextarch    |
| fs.oss.vectored.read.max.merged.size                     |  整型  | 2097152       | OSS/OSS-HDFS 在 readVectored 操作期间，对多个 FileRange 合并的最大长度（字节），为 0 时不合并。 | 6.9.0+ nextarch    |

### 内存相关配置项
| 配置项                                    |  类型  | 默认值 | 说明                    | 版本                 |
|----------------------------------------| --- |-----|-----------------------|--------------------|
| fs.oss.memory.buffer.size.max.mb       |  整型  | 6124 | 内存池总容量（单位：MB）         | 4.3.0+(deprecated) |
| fs.oss.memory.buffer.size.watermark    |  浮点型 | 0.3 | 内存池用于预读的容量比例          | 4.3.0+(deprecated) |
| fs.oss.memory.pool.size.max.mb         |  整型  | 6124 | 内存池总容量（单位：MB）         | 6.7.0+ nextarch    |
| fs.oss.memory.io.buffer.size.max.ratio |  浮点型 | 0.8 | 内存池最大用于IO buffer的容量比例 | 6.7.0+ nextarch    |

### Metrics 相关配置项
| 配置项                                   |  类型  | 默认值          | 说明                                                                                      | 版本              |
|---------------------------------------| --- |--------------|-----------------------------------------------------------------------------------------|-----------------|
| fs.jdo.metrics.level                  |  整型  | 1            | mertrics 收集等级，0是不收集，1是静态metrics，2是包含 bucket 信息的动态 metrics，3包含所有 metrics                 | 6.6.0+ nextarch |
| fs.jdo.metrics.file.enable            |  布尔值 | false        | 控制是否输出 metrics 到文件                                                                      | 6.6.0+ nextarch |
| fs.jdo.metrics.file.dir               |  字符串 | /tmp/metrics | 输出 metrics 到文件的所在目录                                                                     | 6.6.0+ nextarch |
| fs.jdo.metrics.file.pid.append.enable |  布尔值 | false        | 控制输出 metrics 到文件的方式，true 代表以 pid 作为子目录，目录层级为(file_dir)/pid/jindosdk_(timestamp).metrics | 6.7.0+ nextarch |
| fs.jdo.metrics.file.number            |  整型 | 16           | 控制单个目录下最大文件的个数                                                                          | 6.6.0+ nextarch |
| fs.jdo.metrics.interval.sec           |  整型  | 15           | 输出 metrics 到文件的间隔时间，单位秒                                                                 | 6.6.0+ nextarch |
| fs.jdo.metrics.file.clean.enable      |  布尔值 | false        | 控制是否自动清理输出 metrics 的文件, true 代表打开清理，默认24h清理一次                                           | 6.6.0+ nextarch |

Credential Provider 相关配置，详见[《配置 OSS/OSS-HDFS Credential Provider》](./jindosdk_credential_provider.md)
