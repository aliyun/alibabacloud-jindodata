# JindoData 4.6.5 版本说明
## 版本概要

JindoData 4.6.5 版本修复若干问题。

## 修复介绍

- 添加 oss scheme 的 ServiceLoader 指向 JindoOssFileSystem
- 优化 isDirectory() 异常逻辑，针对带 Path `*` 目录，isDirectory() 接口返回 false，替换原来返回`IllegalPath `异常
- 优化 HadoopSDK 在部分场景下，会报出 Hadoop Config 并发修改异常`ConcurrentModificationException`
- 优化临时目录异常或者出现坏盘时 JindoMagicCommitter 客户端写 OSS 的重试逻辑，最大程度保证作业写入成功，避免 `InvalidPart`异常 (One or more of the specified parts could not be found or the specified entity tag might not have matched the part's entity tag.)
