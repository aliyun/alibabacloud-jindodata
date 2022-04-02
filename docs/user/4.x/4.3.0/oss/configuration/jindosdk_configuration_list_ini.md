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
# 请求 oss 超时时间（毫秒）
fs.oss.timeout.millisecond=30000
# 连接 oss 超时时间（毫秒）
fs.oss.connection.timeout.millisecond=3000
# 单个文件 oss 并发上传线程数
fs.oss.upload.thread.concurrency=5
# oss 并发上传任务队列大小
fs.oss.upload.queue.size=5
# 进程内 oss 最大并发上传任务数
fs.oss.upload.max.pending.tasks.per.stream=16
# oss 并发下载任务队列大小
fs.oss.download.queue.size=5
# 进程内 oss 最大并发下载任务数
fs.oss.download.thread.concurrency=16
# 预读 oss 的 buffer 大小
fs.oss.read.readahead.buffer.size=1048576
# 同时预读 oss 的 buffer 个数
fs.oss.read.readahead.buffer.count=4  
```

【注：4.0版本以上版本支持方式】

