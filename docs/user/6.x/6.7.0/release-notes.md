# 6.7.0，2024-09-29

## 版本概要

发布 JindoSDK 6.7.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.0 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 支持统一管理内存，使用 `fs.jdo.memory.pool.size.max.mb` 配置最大使用内存，并支持内存实时使用 metrics。
- nextarch classifier 支持 io buffer 内存使用控制，使用 `fs.jdo.memory.io.buffer.size.max.ratio` 配置最大使用内存池的比例。
- nextarch classifier 支持 Golang SDK。
- nextarch classifier 支持 OSS 对象存储深冷归档。
- nextarch classifier 优化 pread 接口，内存零拷贝。
- nextarch classifier 优化 sendfile 在写小文件场景的使用。
- nextarch classifier 优化 metrics 框架，支持每次输出到单独文件，方便采集。
- nextarch classifier 优化 distjob 框中，排除 log4j 相关依赖。
- nextarch classifier 优化 classloader 加载，在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 `java.io.tmpdir` 时，默认解压至配置目录下。
- nextarch classifier 修复 OSS-HDFS StoragePolicy 相关接口。
- nextarch classifier 修复 OSS 对象存储，ListDirectory 返回结果的 Mtime 单位。
- nextarch classifier 修复 JindoCommitter 在 Cleanup 阶段，不应抛出异常。
- nextarch classifier 修复 DLF 相关 CredentialProvider 使用 MagicCommitter 时的权限问题。
- nextarch classifier 修复 JindoCache 的若干问题。