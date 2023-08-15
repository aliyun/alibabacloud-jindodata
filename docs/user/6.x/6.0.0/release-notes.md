# JindoData 6.0.0 版本说明

## 版本概要

发布 JindoSDK 6.0.0 正式版的功能

## 介绍

- JindoSDK 更新 [6.0.0 的 Maven 仓库](oss-maven.md)，JindoSDK 合并 jindo-mapreduce (JindoCommitterFactory等) 到 jindo-sdk
- JindoSDK 新增若干修复，包含 flush 降频，优化 GC 回收，适配 OSS-HDFS 回收站等
- 多平台支持 MACOS 和 主流 linux 场景，其中 CentOS 6, Ubuntu22, 阿里云倚天机型(ARM)，MacOS (Intel/M1) 需要使用特定的扩展包
- 升级 g++ 编译器到 10.4，使用 c++20 标准
- 支持 [yalangtinglibs](https://github.com/alibaba/yalantinglibs) 访问 OSS/OSS-HDFS

## 下载

JindoData 6.0.0 [下载页面](jindodata_download.md)
