# JindoData 6.0.1 版本说明

## 版本概要

发布 JindoSDK 6.0.1 正式版的功能

## 介绍

- JindoSDK 更新 [6.0.1 的 Maven 仓库](oss-maven.md)
- JindoSDK 支持 isFileClosed 方法，用于判断访问 OSS-HDFS 的读写流是否已关闭。
- JindoSDK 对 flush 降频做了优化，在降频配置生效时，对第一次有数据的flush操作保证执行，防止flush出一个空文件。降频配置，可参考[Flume 使用 JindoSDK 写入 OSS](../jindosdk/flume/jindosdk_on_flume.md)

## 下载

JindoData 6.0.1 [下载页面](jindodata_download.md)
