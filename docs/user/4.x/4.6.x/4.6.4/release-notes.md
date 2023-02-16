# JindoData 4.6.4 版本说明
## 版本概要

JindoData 4.6.4 版本新增多平台支持。

## 多平台支持介绍

- 具体支持平台详见 [下载页面](../../jindodata_download.md) 。


- Java 的多平台通过部署多个 jindo-core 实现多平台支持， `jindo-core` 默认支持主流 linux，如果需要其他平台支持，要额外引入对应平台的扩展包。

例如：

> 在主流 linux 上的 hadoop 集群部署 ，需要 `jindo-core-4.6.4.jar` 和 `jindo-sdk-4.6.4.jar` 到指定的classpath
>
> 在macos上运行和调试，需要 `jindo-core-4.6.4.jar` 和 `jindo-sdk-4.6.4.jar`，同时还需要 `jindo-core-macos-10_14-x86_64-4.6.4.jar`

- jindo maven repo 已同步上传多平台支持依赖，以访问 OSS 为例，maven 依赖使用参考 [jindosdk_ide_hadoop.md](oss/hadoop/jindosdk_ide_hadoop.md) 

