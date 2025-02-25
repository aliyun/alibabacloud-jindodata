# 6.8.1，2025-02-25

## 版本概要

发布 JindoSDK 6.8.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.1 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 升级 yalantinglibs 到 [lts1.0.1](https://github.com/alibaba/yalantinglibs/tree/lts1.0.1)
- nextarch classifier 在 oss maven 仓库中将作为默认版本号发布。
- JindoSDK 修复对 summary metrics 的支持。
- JindoCache 修复读时落缓存预读块不对齐，落缓存失败问题。
- JindoCache 修复客户端读相关metrics收集问题。
- JindoCache 支持OSS-HDFS UGI信息。
- JindoCache 支持客户端超时回退。
- JindoFuse 优化了元数据缓存占用，预计在海量小文件场景降低内存占用 50%。