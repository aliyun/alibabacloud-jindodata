# 6.10.0，2025-08-04

## 版本概要

发布 JindoSDK 6.10.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.10.0 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 支持 OSS-HDFS 元数据访问 Batch 接口：renameBatch、removeBatch（deleteBatch）、getFileInfoBatch（getFileStatusBatch）、getContentSummaryBatch、listDirectoryBatch，大幅提升小文件场景元数据访问性能。
- 支持 listFiles(Path f, boolean recursive)。
- JindoDistributedFileSystem 支持 getServerDefaults(Path f)。
- JindoSDK 优化湖表场景默认预读长度，默认值从 16M 改为 64M。
- JindoCLI、Tensorflow Collector 使用 nextarch 实现。
- JindoSDK 优化 pread 日志，默认改成 debug。
- 优化容器场景默认并发数，可以通过环境变量 `JINDO_DEFAULT_PROCS` 指定。
- 修复访问 OSS-HDFS 时，getServerDefaults 函数不需要鉴权。
- 修复访问 OSS-HDFS 时，创建目录/文件时，对 umask 的支持。
- 修复 libjindosdk_c.so 对一次性读大于 2G buffer 场景的支持。