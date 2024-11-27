# 6.7.6，2024-11-27

## 版本概要

发布 JindoSDK 6.7.6 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.6 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 新增配置 `fs.oss.append.threshold.size` 和 `fs.oss.flush.merge.threshold.size`，优化频繁 Close-To-Append 或者 Flush 产生大量小块的问题，配置描述详见[客户端常用配置](https://aliyun.github.io/alibabacloud-jindodata/jindosdk/jindosdk_configuration/)。
- jindo-fuse 支持访问 DLF Volume。
