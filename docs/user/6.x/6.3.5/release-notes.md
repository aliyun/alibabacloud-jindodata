# 6.3.5，2024-06-28

## 版本概要

发布 JindoSDK 6.3.5 正式版的功能

## 介绍

- JindoSDK 更新 [6.3.5 的 Maven 仓库](oss-maven.md)。
- 修复偶现 Signer V4 签名问题，将签名时间戳转换函数 localtime、gmtime 替换为 localtime_r、gmtime_r 避免并发问题。
- 修复 libjindosdk_c.so 访问 OSS-HDFS 偶现 crash 问题，及重试失败问题。