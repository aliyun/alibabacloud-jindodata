# 6.9.1，2025-06-04

## 版本概要

发布 JindoSDK 6.9.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.9.1 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- JindoSDK 修复湖表格式预读策略，修复了`fs.oss.read.profile.enable=true`时在部分场景有性能回退的问题。
- JindoFuse 修复 OSS 场景多次 Append/Flush 不支持 2G 以上文件的问题。
- JindoSDK 优化 JindoInputStream 日志，可以指定 `log4j.logger.com.aliyun.jindodata.common.JindoInputStream` 等级以减少 readVectored 场景日志数量。