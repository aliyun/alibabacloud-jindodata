# 6.7.2，2024-10-15

## 版本概要

发布 JindoSDK 6.7.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.2 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 优化 metrics 统计信息。
- nextarch classifier 修复在写 OSS-HDFS 场景 flush 后出现小块的问题。
- nextarch classifier 修复后台 metrics 线程引发的偶现 crash 问题。
- Pyjindo 修复 6.7.x 版本报 `invalid pointer` 问题，6.7.0 及 6.7.1 版本可以通过配置环境变量 `JINDO_STAT_MEMORY=0` 绕过。