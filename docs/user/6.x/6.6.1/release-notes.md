# 6.6.1，2024-09-03

## 版本概要

发布 JindoSDK 6.6.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.6.1 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 优化在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 `java.io.tmpdir` 时，默认解压至配置目录下。
- nextarch classifier 修复访问 JindoCache 的若干问题。
- nextarch classifier 优化 metrics 框架。