# 6.8.5，2025-04-29

## 版本概要

发布 JindoSDK 6.8.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.5 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- JindoSDK 新增配置 `fs.oss.list.fallback.iterative`，为 `true` 时，支持 listStatus 对大目录自动 fallback 到迭代访问。
- JindoSDK 新增 [Golang SDK 文档](https://aliyun.github.io/alibabacloud-jindodata/jindosdk/golang/jindosdk_golang_quickstart/)，并支持迭代 list 接口。
- JindoSDK 访问 OSS 支持 authorization 插件。
- JindoFS CLI 优化 binary 大小。