# 6.10.7，2025-05-26

## 版本概要

发布 JindoSDK 6.10.7 正式版的功能

### 介绍

- JindoSDK 更新 [6.10.7 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 支持 OSS 服务端加密。
- 修改 OSS-HDFS 配置默认值。 `fs.oss.flush.merge.threshold.size` 默认为 0，即默认关闭合并小块。
- 支持 bucket 级别 custom provider。
- 新增配置 `fs.oss.list.iterative.first`，打开后，适合在扁平目录场景用 listIterative 代替 listDirectory，以节省内存。
- JindoCommitter 新增对 legacy hadoop 的兼容。