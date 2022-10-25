# JindoSDK 高级参数配置

## SDK配置项列表

可根据情况将以下配置添加到 `jindosdk.cfg` 中。

```ini
[jindosdk]
# 客户端写入时的临时文件目录，可配置多个（逗号隔开），会轮流写入，多用户环境需配置可读写权限
fs.oss.tmp.data.dirs=/tmp/
# 临时文件自清理服务
fs.oss.tmp.data.cleaner.enable=true
# 访问 oss 失败重试次数
fs.oss.retry.count=5
# 访问 oss 失败重试间隔（毫秒）
fs.oss.retry.interval.millisecond=500
# 请求 oss 超时时间（毫秒）
fs.oss.timeout.millisecond=30000
# 连接 oss 超时时间（毫秒）
fs.oss.connection.timeout.millisecond=3000
# 连接 oss 的连接池对每个host的最大连接数（超过阈值外的连接会使用短连接）
fs.oss.max.connections.per.host=100
# 单个文件 oss 并发上传线程数
fs.oss.upload.thread.concurrency=5
# oss 并发上传任务队列大小
fs.oss.upload.queue.size=5
# 进程内 oss 最大并发上传任务数
fs.oss.upload.max.pending.tasks.per.stream=16
# 进程内 oss 最大并发下载任务数
fs.oss.download.thread.concurrency=16
# 最大同时预读 oss 的 buffer 个数
fs.oss.read.readahead.max.buffer.count=48
# 使用ECS免密服务，避免配置AK（不推荐，建议使用固定AK方式访问）
fs.oss.provider.endpoint=ECS_ROLE
# oss 读取缓冲区大小（字节）
fs.oss.read.buffer.size=1048576
# oss 写缓冲区大小（字节）
fs.oss.write.buffer.size=1048576
# oss 刷新缓冲区间隔（毫秒），小于 0 时不生效
fs.oss.flush.interval.millisecond=-1
# 文件级别 crc64 完整性校验，默认打开
fs.oss.checksum.crc64.enable=true
# 请求级别 md5 完整性校验，默认关闭
fs.oss.checksum.md5.enable=false


# oss 并发下载任务队列大小 (弃用)
fs.oss.download.queue.size=5
# 预读 oss 的 buffer 大小 (弃用)
fs.oss.read.readahead.buffer.size=1048576
# 同时预读 oss 的 buffer 个数 (弃用)
fs.oss.read.readahead.buffer.count=4
```

【注：4.5版本以上版本支持方式】

