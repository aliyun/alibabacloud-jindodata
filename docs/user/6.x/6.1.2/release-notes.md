# JindoData 6.1.2 版本说明

## 版本概要

发布 JindoSDK 6.1.2 正式版的功能

## 介绍

- JindoSDK 更新 [6.1.2 的 Maven 仓库](oss-maven.md)
- 通过配置 `fs.oss.list.type=2` 支持ListObjectV2，以避免访问开启了多版本bucket时的操作超时问题。
- 支持使用 `dls://` 前缀访问 jindofs。
- 修复读流 close 会卡住的问题。
- 修复写流出现一次 write 失败后，后续 write 出现的 NPE 问题。


## 下载

JindoData 6.1.2 [下载页面](jindodata_download.md)
