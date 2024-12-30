# 6.7.8，2024-11-27

## 版本概要

发布 JindoSDK 6.7.8 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.8 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 升级 yalantinglibs 到 [0.3.8.1](https://github.com/alibaba/yalantinglibs/tree/0.3.8.1) 
- nextarch classifer 优化 Timed Buffer CPU 负载高的问题。
- nextarch classifer 优化了访问 OSS 时的删除策略，新增配置 `fs.oss.delete.quiet.enable`，默认为false，使用简单模式删除。简单模式说明，详见 OSS 文档[《调用 DeleteMultipleObjects 删除多个文件》](https://help.aliyun.com/zh/oss/developer-reference/deletemultipleobjects)。
- nextarch classifer 修复了无法设置 legacy 预读算法的问题。
- nextarch classifer 修复了使用 S3 协议时，打开 `fs.s3.upload.sendfile.enable=true` 后可能引发爆栈的问题。 