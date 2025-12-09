# 6.10.3，2025-12-09

## 版本概要

发布 JindoSDK 6.10.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.10.3 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- JindoFuse 支持只读挂载 `-oro_mount`。
- 优化 Hadoop FileSystem close() 的实现。
- 优化 PyJindo 对 fsspec 接口的兼容性。
- 修复 PyJindo 打开文件异常，如文件不存在时，会 crash 的问题。
- 修复 JindoDistributedFileSystem 对 `void rename(final Path src, final Path dst, final Options.Rename... options)` 的支持。
- 添加 iterative list 相关操作的日志。