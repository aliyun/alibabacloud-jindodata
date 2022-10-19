# JindoData 4.6.0 版本说明
## 版本概要

JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

JindoData 4.6.0 版本推出平滑迁移功能，支持HDFS 到 OSS-HDFS平滑迁移。可以极大的简化用户的数据迁移流程。JindoFS 存储系统支持文件清单功能，用户可以基于文件清单更好了解数据的分布以及归属。在性能优化方面，JindoFS 存储系统通过存量以及增量的方式优化Du/Count性能，显著提升Du/Count操作性能。 对于JindoSDK 而言，JindoSDK 4.6.0版本支持文件以及数据块的级别的校验，提高JindoSDK 写入链路的稳定性。 此外JindoSDK 还支持多路径访问协议，支持不同协议模式访问同一后端路径。

## 主要功能
### JindoFS 存储系统
- JindoFS 支持OSS-HDFS 文件清单导出功能，用户可以基于文件清单功能更好的了解数据分布以及二次开发。
- JindoFS 通过服务端存量加增量的优化，显著提高Du/Count 操作的性能。
- JindoFS 支持HDFS 到 OSS-HDFS平滑迁移功能，可以极大的简化 HDFS 到 OSS-HDFS数据迁移流程。
- JindoFS 支持多路径协议访问，用户可以使用不同访问协议访问同一后端路径。

### JindoFSx 存储加速系统
- 修复JindoFSx 客户端写缓存时导致客户端异常退出问题。
- 修复JindoFSx 客户端Metrics 上报导致客户端异常退出问题。
- 修复JindoFSx Ranger使用过程中内存泄漏问题。

### JindoSDK 和工具支持
- JindoSDK 支持CRC/MD5 Checksum校验， 支持文件级别以及数据块级别的写入校验。
- 支持Jindo Sync 数据同步工具， 用户可以不依赖Hadoop环境进行数据同步。
- JindoSDK 支持OSS-HDFS TensorFlow Connector。
