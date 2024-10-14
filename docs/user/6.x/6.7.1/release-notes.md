# 6.7.1，2024-10-14

## 版本概要

发布 JindoSDK 6.7.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.1 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 优化默认并发下的写性能。
- nextarch classifier 修复 hudi 读写 log 时，对 AlreadyBeingCreatedException 异常处理。
- nextarch classifier 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- jindo-fuse 修复在 s3 scheme 上追加写的支持。