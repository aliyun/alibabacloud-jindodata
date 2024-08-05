# 6.5.0，2024-07-12

## 版本概要

发布 JindoSDK 6.5.0 正式版的功能

## 介绍

- JindoSDK 更新 [6.5.0 的 Maven 仓库](oss-maven.md)。
- 修复偶现 Signer V4 签名问题，将签名时间戳转换函数 localtime、gmtime 替换为 localtime_r、gmtime_r 避免并发问题。
- 修复 libjindosdk_c.so 访问 OSS-HDFS 偶现 crash 问题，及重试失败问题。
- jindo-core.jar nextarch classifer 支持新版内核。
- 优化内存不足时的预读算法。
- commiter 支持 setXAttr。