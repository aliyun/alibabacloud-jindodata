# 6.4.1，2024-10-23

## 版本概要

发布 JindoSDK 6.4.1 正式版的功能

## 介绍

- JindoSDK 更新 [6.4.1 的 Maven 仓库](oss-maven.md)。
- 修复偶现 Signer V4 签名问题，将签名时间戳转换函数 localtime、gmtime 替换为 localtime_r、gmtime_r 避免并发问题。
- 修复 libjindosdk_c.so 访问 OSS-HDFS 偶现 crash 问题，及重试失败问题。
- 提升 JindoSDK 对于低于 Hadoop 2.8.x 版本接口的兼容性，如 `CallerContext` 及 `FsServerDefaults`。
- 修复 hudi 读写 log 时，对 AlreadyBeingCreatedException 异常处理。
- 修复 JindoOssFileSystem 的 Delegation Token Renew 机制。
- 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- 优化 classloader 加载，在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 `java.io.tmpdir` 时，默认解压至配置目录下。