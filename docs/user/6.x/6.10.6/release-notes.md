# 6.10.6，2025-05-26

## 版本概要

发布 JindoSDK 6.10.6 正式版的功能

### 介绍

- JindoSDK 更新 [6.10.6 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- PyJindo 兼容在 Hadoop 环境中对 Kerberos + Ranger 的支持。
- 优化 Hadoop FileSystem close() 的实现，在 close() 时提前对在 FileSystem CACHE 中的对象解引用。
- JindoFuse 修复若干潜在问题。