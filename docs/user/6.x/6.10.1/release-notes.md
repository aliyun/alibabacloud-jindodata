# 6.10.1，2025-09-22

## 版本概要

发布 JindoSDK 6.10.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.10.1 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 升级 yalantinglibs 到 [lts1.2.1](https://github.com/alibaba/yalantinglibs/tree/lts1.2.1)。修复初始化时，并发调用 summary 偶现 crash 问题。
- JindoSDK 支持 qos latency metrics。
- JindoFS CLI 修复对 `-count -q` 参数的支持。