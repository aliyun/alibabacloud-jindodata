# JindoData 5.0.0 版本说明

## 版本概要

发布 JindoData 5.0.0 版本, 包含 JindoSDK, JindoAuth, JindoCache, JindoShift 等

## 介绍

- JindoAuth 5.0.0 提供 OSS-HDFS/OSS 认证和授权管理，发布 [部署文档](jindoauth/jindoauth_deploy.md)和[使用文档](jindoauth/jindoauth_emr-next.md)
- JindoSDK 更新 [5.0.0 的 Maven 仓库](oss-maven.md)，JindoSDK 合并 jindo-mapreduce (JindoCommitterFactory等) 到 jindo-sdk
- JindoSDK 新增若干修复，包含 flush 降频，优化 GC 回收，适配 OSS-HDFS 回收站等
- 多平台支持 MACOS 和 主流 linux 场景，其中 CentOS 6, Ubuntu22, 阿里云倚天机型(ARM)，MacOS (Intel/M1) 需要使用特定的扩展包
- 更多文档增加中...

## 下载

JindoData 5.0.0 [下载页面](jindodata_download.md)
