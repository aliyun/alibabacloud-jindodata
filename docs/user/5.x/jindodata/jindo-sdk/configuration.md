# 客户端常用配置

## OSS/OSS-HDFS

|  配置项  |  类型  |  默认值  |  说明  |  版本  |
| --- | --- | --- | --- | --- |
|  fs.oss.tmp.data.dirs  |  字符串  |  /tmp/  |  客户端写入时的临时文件目录，可配置多个（逗号隔开），会轮流写入，多用户环境需配置可读写权限  |  4.3.0及以上版本  |
|  fs.oss.tmp.data.cleaner.enable  |  布尔值  |  true  |  临时文件自清理服务  |  4.3.0及以上版本  |
|  fs.oss.retry.count  |  整型  |  5  |  访问 OSS/OSS-HDFS 失败重试次数  |  4.3.0及以上版本  |
|  fs.oss.retry.interval.millisecond  |  整型  |  500  |  访问 OSS/OSS-HDFS 失败重试间隔（毫秒）  |  4.3.0及以上版本  |
|  fs.oss.timeout.millisecond  |  整型  |  30000  |  请求 OSS/OSS-HDFS 超时时间（毫秒）  |  4.3.0及以上版本  |
|  fs.oss.connection.timeout.millisecond  |  整型  |  3000  |  连接 OSS/OSS-HDFS 超时时间（毫秒）  |  4.3.0及以上版本  |
|  fs.oss.max.connections.per.host  |  整型  |  100  |  连接 oss 的连接池对每个host的最大连接数（超过阈值外的连接会使用短连接）  |  4.3.0及以上版本  |
|  fs.oss.upload.thread.concurrency  |  整型  |  5  |  单个文件 OSS/OSS-HDFS 并发上传线程数  |  4.3.0及以上版本  |
|  fs.oss.upload.queue.size  |  整型  |  5  |  OSS/OSS-HDFS 并发上传任务队列大小  |  4.3.0及以上版本  |
|  fs.oss.upload.max.pending.tasks.per.stream  |  整型  |  16  |  进程内 oss 最大并发上传任务数  |  4.3.0及以上版本  |
|  fs.oss.download.thread.concurrency  |  整型  |  16  |  进程内 oss 最大并发下载任务数  |  4.3.0及以上版本  |
|  fs.oss.read.readahead.max.buffer.count  |  整型  |  48  |  最大同时预读 oss 的 buffer 个数  |  4.5.1及以上版本  |
|  fs.oss.read.buffer.size  |  整型  |  1048576  |  oss 读缓冲区大小（字节）  |  4.3.0及以上版本  |
|  fs.oss.write.buffer.size  |  整型  |  1048576  |  oss 写缓冲区大小（字节）  |  4.3.0及以上版本  |
|  fs.oss.flush.interval.millisecond  |  整型  |  \-1  |  oss 刷新缓冲区间隔（毫秒），小于 0 时不生效  |  4.3.0及以上版本  |
|  fs.oss.checksum.crc64.enable  |  布尔值  |  true  |  文件级别 crc64 完整性校验  |  4.6.0及以上版本  |
|  fs.oss.checksum.md5.enable  |  布尔值  |  false  |  请求级别 md5 完整性校验  |  4.6.0及以上版本  |
|  ~~fs.oss.download.queue.size~~  |  ~~整型~~  |  ~~5~~  |  ~~oss 并发下载任务队列大小~~  |  ~~4.3.0至4.5.0版本~~  |
|  ~~fs.oss.read.readahead.buffer.size~~  |  ~~整型~~  |  ~~1048576~~  |  ~~预读 oss 的 buffer 大小~~  |  ~~4.3.0至4.5.0版本~~  |
|  ~~fs.oss.read.readahead.buffer.count~~  |  ~~整型~~  |  ~~4~~  |  ~~同时预读 oss 的 buffer 个数~~  |  ~~4.3.0至4.5.0版本~~  |

Credential Provider 相关配置，详见[《配置 OSS/OSS-HDFS Credential Provider》](/docs/user/5.x/jindodata/jindo-sdk/oss_credential_provider.md)

## JindoCache

服务端常见配置，详见xxx

## JindoAuth

服务端常见配置，详见xxx

## JindoShift

服务端常见配置，详见xxx