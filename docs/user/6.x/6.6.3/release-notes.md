# 6.6.3，2024-09-14

## 版本概要

发布 JindoSDK 6.6.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.6.3 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 修复写场景小概率因 sendfile 出现错误未重试，导致文件 close 时报失败的情况。