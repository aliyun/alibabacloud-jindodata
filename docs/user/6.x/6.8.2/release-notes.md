# 6.8.2，2025-03-24

## 版本概要

发布 JindoSDK 6.8.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.2 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- JindoSDK 修复错误信息不准确的问题，正确返回请求超时 `Request timeout` 而不是 `Connection timed out`。
- JindoSDK 修复 append-close 优化打开时，异常关闭可能导致报错的问题。即 `fs.oss.append.threshold.size` 不为 `0` 时，可能导致 close 后再次 append 失败（默认配置不影响）。
- JindoSDK 修复 listStatusIterator 不支持 ListObjectV2。即配置 `fs.oss.list.type` 为 `2`，可能导致 listStatusIterator 死循环（默认配置不影响）。
- JindoSDK 修复访问 JindoCache 时偶现的 hang on 问题。
- JindoSDK 优化对 IO 请求超时配置的兼容性，默认为 `fs.oss.io.timeout.millisecond` 与 `fs.oss.timeout.millisecond` 两者的最大值。
- JindoSDK 新增配置 `fs.oss.read.use-pread` 以及 `fs.oss.pread.cache.enable`，以优化湖表场景读性能。