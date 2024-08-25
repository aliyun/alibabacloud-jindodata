# 6.6.0，2024-08-25

## 版本概要

发布 JindoSDK 6.6.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.6.0 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 优化 io 性能，使用协程异步化，相同配置下支持更高并发。
- nextarch classifier 支持单独指定 io 超时时间。
- nextarch classifier 支持 metrics 框架。
- 修复 nextarch classifier 对 JindoCache 支持问题。
- jindo-fuse 默认改为使用 nextarch 实现。