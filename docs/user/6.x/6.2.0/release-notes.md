# JindoData 6.2.0 版本说明

## 版本概要

发布 JindoSDK 6.2.0 正式版的功能

## 介绍

- JindoSDK 更新 [6.2.0 的 Maven 仓库](oss-maven.md)
- 新增支持异步的 CSDK `libjindosdk_c.so`，老版本 `libjindo-csdk.so` 逐渐弃用。
- 修复路径合法性检查，支持带 `*` 的路径
- 优化 JindoFuse 访问 oss 时有非法名称的路径时的报错信息
- Jindo 命令行支持 OSS-HDFS setStoragePolicy 解冻相关策略
- 采用新的预读算法，顺序读大文件的性能提高
- 修复 Hive 使用 LDAP + Kerberos + OSS-HDFS 时的认证问题
- jindotable 支持 oss-hdfs 支持归档数据到冷归档

## 下载

JindoData 6.2.0 [下载页面](jindodata_download.md)
