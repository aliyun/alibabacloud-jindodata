# 客户端常用配置

## OSS/OSS-HDFS

|  配置项  |  类型  | 默认值           |  说明  | 版本     |
| --- | --- |---------------| --- |--------|
|  fs.oss.tmp.data.dirs  |  字符串  | /tmp/         |  客户端写入时的临时文件目录，可配置多个（逗号隔开），会轮流写入，多用户环境需配置可读写权限  | 4.3.0+ |
|  fs.oss.tmp.data.cleaner.enable  |  布尔值  | true          |  临时文件自清理服务  | 4.3.0+ |
|  fs.oss.retry.count  |  整型  | 5             |  访问 OSS/OSS-HDFS 失败重试次数  | 4.3.0+ |
|  fs.oss.retry.interval.millisecond  |  整型  | 500           |  访问 OSS/OSS-HDFS 失败重试间隔（毫秒）  | 4.3.0+ |
|  fs.oss.timeout.millisecond  |  整型  | 30000         |  请求 OSS/OSS-HDFS 超时时间（毫秒）  | 4.3.0+ |
|  fs.oss.connection.timeout.millisecond  |  整型  | 3000          |  连接 OSS/OSS-HDFS 超时时间（毫秒）  | 4.3.0+ |
|  fs.oss.max.connections.per.host  |  整型  | 100           |  连接 oss 的连接池对每个host的最大连接数（超过阈值外的连接会使用短连接）  | 4.3.0+ |
|  fs.oss.upload.thread.concurrency  |  整型  | MAX(cpu核数,16) |  单个文件 OSS/OSS-HDFS 并发上传线程数  | 4.3.0+ |
|  fs.oss.upload.queue.size  |  整型  | MAX(cpu核数,16) |  OSS/OSS-HDFS 并发上传任务队列大小  | 4.3.0+ |
|  fs.oss.upload.max.pending.tasks.per.stream  |  整型  | 10            |  进程内 oss 最大并发上传任务数  | 4.3.0+ |
|  fs.oss.download.thread.concurrency  |  整型  | MAX(cpu核数,16) |  进程内 oss 最大并发下载任务数  | 4.3.0+ |
|  fs.oss.read.readahead.max.buffer.count  |  整型  | 48            |  最大同时预读 oss 的 buffer 个数  | 4.5.1+ |
|  fs.oss.read.buffer.size  |  整型  | 1048576       |  oss 读缓冲区大小（字节）  | 4.3.0+ |
|  fs.oss.write.buffer.size  |  整型  | 1048576       |  oss 写缓冲区大小（字节）  | 4.3.0+ |
|  fs.oss.flush.interval.millisecond  |  整型  | -1            |  oss 刷新缓冲区间隔（毫秒），小于 0 时不生效  | 4.3.0+ |
|  fs.oss.memory.buffer.size.max.mb  |  整型  | 6124          |  内存池总容量（单位：MB） | 4.3.0+ |
|  fs.oss.memory.buffer.size.watermark  |  浮点型  | 0.3          |  内存池用于预读的容量比例 | 4.3.0+ |
|  fs.oss.blocklet.size.mb  |  整型  | 8             |  oss 分块上传时的块大小（MB）由于分块数量最多为10000块，因此默认写入文件不能超过80GB。如果有个别文件超过80G，建议根据文件大小单独调大本配置，并同时调大请求 oss 的超时时间。如文件大小未知，或者远远超过80G（如超过160G），建议考虑使用 OSS-HDFS（无文件大小限制）  | 4.5.2+ |
|  fs.oss.checksum.crc64.enable  |  布尔值  | true          |  文件级别 crc64 完整性校验，目前对写OSS-HDFS性能有较大影响，性能优先场景可以考虑关闭。  | 4.6.0+ |
|  fs.oss.checksum.md5.enable  |  布尔值  | false         |  请求级别 md5 完整性校验  | 4.6.0+ |
|  fs.oss.read.readahead.prefetcher.version  |  字符串  | default       |  可选值：`legacy` 原预读算法, `default` 新预读算法。新预读算法可能使用更多内存。若配置了新预读算法后发生性能下降，可能是由于内存池容量不足导致预读的块在被访问到之前就被提前逐出。为了避免该情况发生，可以考虑缩减最大预读长度，或允许预读使用更多内存。  | 6.2.0+ |
|  fs.oss.read.readahead.prefetch.size.max |  整型  | 268435456       |  预读最大长度（单位：byte） | 6.2.0+ |
|  fs.oss.signer.version |  整型  | 0       |  签名算法版本，可选值[0，1，4]。推荐使用0，表示默认算法版本，6.3.0版本之后默认使用 V4 签名算法提高访问 OSS/OSS-HDFS，若特殊场景希望指定签名算法版本，请指定 1 或 4。 | 6.3.0+ |


Credential Provider 相关配置，详见[《配置 OSS/OSS-HDFS Credential Provider》](./jindosdk_credential_provider.md)
