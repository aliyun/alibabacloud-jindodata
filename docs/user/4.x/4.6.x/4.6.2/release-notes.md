# JindoData 4.6.2 版本说明
## 版本概要

JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

JindoData 4.6.0 版本推出平滑迁移功能，支持HDFS 到 OSS-HDFS平滑迁移。可以极大的简化用户的数据迁移流程。JindoFS 存储系统支持文件清单功能，用户可以基于文件清单更好了解数据的分布以及归属。在性能优化方面，JindoFS 存储系统通过存量以及增量的方式优化Du/Count性能，显著提升Du/Count操作性能。 对于JindoSDK 而言，JindoSDK 4.6.2版本支持文件以及数据块的级别的校验，提高JindoSDK 写入链路的稳定性。 此外JindoSDK 还支持多路径访问协议，支持不同协议模式访问同一后端路径。

JindoData 4.6.2 版本是在4.6.0版本基础上做了大量的修复。
## 主要功能
### JindoFS 存储系统
- 修复分层存储 STD 转 STD 时导致服务卡住问题。
- 修复分层存储产生空manifest导致服务卡住问题。
- 加速分层存储任务执行速度。
- 修复 RootPolicy 功能逻辑。
- 修复 setAcl 偶发服务 crash 问题。
- 修复低概率发生DB manifest文件占满磁盘的问题。
- 修复迁移服务的批量元数据导入功能。

