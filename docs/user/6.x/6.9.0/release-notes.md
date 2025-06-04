# 6.9.0，2025-05-21

## 版本概要

发布 JindoSDK 6.9.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.9.0 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 支持 [openFile()](https://issues.apache.org/jira/browse/HADOOP-15229) 接口，支持指定 read policy（需 Hadoop 3.3.0+）。
- 支持 [Vectored IO](https://issues.apache.org/jira/browse/HADOOP-18103) 接口（需 Hadoop 3.3.6+）。
- 优化湖表格式预读策略，重 IO 读场景提升 30%（可以配置`fs.oss.read.profile.enable=false`关闭，回退老版本行为）。
- 优化 OSS-HDFS append 合并策略，支持 ComposedBlock。
- 优化 OSS-HDFS InputStream 锁。
- 优化 OSS 解冻接口，支持设置 OSS 解冻优先级。
- 修复高并发写 OSS/OSS-HDFS close 偶现死锁问题。
- 修复 JindoDistcp 若干问题，支持 `--syncSourceDelete`，详见[JindoDistCp 使用说明](https://aliyun.github.io/alibabacloud-jindodata/jindotools/jindodistcp_quickstart/)。
- 修复 JindoCache 若干问题。
- JindoFS CLI 使用 nextarch 实现。