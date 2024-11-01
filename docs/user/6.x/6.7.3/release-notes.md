# 6.7.3，2024-11-01

## 版本概要

发布 JindoSDK 6.7.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.3 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 打包时带入 jindoauth plugin，支持升级 JindoSDK 后仍兼容老版本 EMR-Ranger，具体参见[升级文档](https://aliyun.github.io/alibabacloud-jindodata/upgrade/emr2_upgrade_jindosdk/#41-emr-ranger)。
- nextarch classifier 修复 hudi 读写 log 时，对 AlreadyBeingCreatedException 异常处理。
- nextarch classifier 修复在使用 http 请求更新 STS 时，偶现卡住问题。
- nextarch classifier 修复开启 Ranger 后持续写入一个 OSS-HDFS 文件超过 1 小时后，文件无法继续写入的问题。