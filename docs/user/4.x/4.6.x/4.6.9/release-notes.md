# JindoData 4.6.9 版本说明
## 版本概要

JindoData 4.6.9 版本修复若干问题。

## 修复介绍

- JindoFS 支持设置客户端 flush 间隔次数。
- JindoSDK 优化了 FileSystem native 对象的回收。
- JindoSDK 优化了 FileStatus 序列化的性能。
- JindoFuse 修复了posix锁。
- JindoFSx 修复缓存预热的小bug。
- JindoDistCp 修复了源checksum为空的情况。