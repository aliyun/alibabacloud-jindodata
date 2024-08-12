# 6.5.1，2024-08-05

## 版本概要

发布 JindoSDK 6.5.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.1 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 提升 JindoSDK 对于低于 Hadoop 2.8.x 版本接口的兼容性，如 `CallerContext` 及 `FsServerDefaults`。
- 优化 JindoCommitter 性能。
- 优化内存不足时的预读算法，回收不使用的内存。
- 优化 plugin 加载方式，`fs.jdo.plugin.dir`支持以多路径加载，如`/dir1,/dir2`
- 修复 list 对象存储时，路径中带有`//`，可能会list出自身的问题
- 修复 jindo-dependence-shaded.jar 安全性问题，去除 log4j 及 apache commons text 依赖
- 修复 nextarch classifier 跑 [hadoop-compat-bench](https://github.com/apache/hadoop/blob/trunk/hadoop-tools/hadoop-compat-bench/src/site/markdown/HdfsCompatBench.md) 若干问题。
- 修复 nextarch classifier 对 RootPolicy 支持问题。