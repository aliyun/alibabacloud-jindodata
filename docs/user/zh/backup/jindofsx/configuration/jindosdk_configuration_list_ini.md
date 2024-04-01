# JindoSDK 高级参数配置

## 缓存优化相关参数

可根据情况将以下配置添加到`jindosdk.cfg`中。
```ini
# 选择 jindofsx 作为 xengine 实现
fs.xengine=jindofsx
# 数据缓存开关
fs.jindofsx.data.cache.enable=false
# 元数据缓存开关
fs.jindofsx.meta.cache.enable=false
#  小文件缓存优化开关
fs.jindofsx.slice.cache.enable=false
# 内存缓存开关
fs.jindofsx.ram.cache.enable=false
# 是否直接连接本地 storageservice
fs.jindofsx.storage.connect.enable=true
# 短路读开关，在缓存结点与客户端在同一台机器上的时候可以直接读本地文件，要求打开本地 storageservice 连接
fs.jindofsx.short.circuit.enable=true
```

## RPC 优化相关参数

可根据情况将以下配置添加到`jindosdk.cfg`中。
```ini
# brpc线程池大小
fs.jindofsx.rpc.backup.threads=100
# 并发线程数量
fs.jindofsx.rpc.thread.concurrency=10
# 消息体大小
fs.jindofsx.rpc.body.size=268435456
# 超时时间大小（毫秒）
fs.jindofsx.rpc.timeout=43200000
# 消息队列大小
fs.jindofsx.rpc.max.unwritten.size=268435456
```

## 缓存 Metrics 相关参数

可根据情况将以下配置添加到`jindosdk.cfg`中。
```ini
# 客户端metrics开关
fs.jindofsx.client.metrics.enable=false
```

【注：4.3.0 版本以上版本支持】

