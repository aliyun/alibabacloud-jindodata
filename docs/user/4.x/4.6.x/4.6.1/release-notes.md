# JindoData 4.6.1 版本说明
## 版本概要

JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

JindoData 4.6.0 版本推出平滑迁移功能，支持HDFS 到 OSS-HDFS平滑迁移。可以极大的简化用户的数据迁移流程。JindoFS 存储系统支持文件清单功能，用户可以基于文件清单更好了解数据的分布以及归属。在性能优化方面，JindoFS 存储系统通过存量以及增量的方式优化Du/Count性能，显著提升Du/Count操作性能。 对于JindoSDK 而言，JindoSDK 4.6.1版本支持文件以及数据块的级别的校验，提高JindoSDK 写入链路的稳定性。 此外JindoSDK 还支持多路径访问协议，支持不同协议模式访问同一后端路径。

JindoData 4.6.1 版本是在4.6.1版本基础上做了大量的修复。
## 主要功能
### JindoFS 存储系统
- JindoFS 减少一些冗余日志打印。
- 修复元数据清单导出没有close的文件大小不对问题。

### JindoFSx 存储加速系统
- JindoFSx 支持缓存临时目录实现自清理。

### JindoSDK 和工具支持
- 优化 JindoSDK 输出体积过大问题。
- du/count 默认开启走服务端优化路径。
- 降低 sts 更新频率，避免频繁发送请求导致限流。
- 免密URL的ram改为小写，避免 ecs 免密服务内部刷新 token 失败。
