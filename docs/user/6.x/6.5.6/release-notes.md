# 6.5.6，2024-12-12

## 版本概要

发布 JindoSDK 6.5.6 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.6 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifer 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- nextarch classifier 修复 DLF 相关 CredentialProvider 使用 MagicCommitter 时的权限问题。
- nextarch classifier 修复 JindoCache 的若干问题。
- jindo-fuse 默认改为使用 nextarch 实现，并支持访问 DLF Volume。
