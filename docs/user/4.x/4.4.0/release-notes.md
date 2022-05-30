# JindoData 4.4.0 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。

JindoFS 存储系统上实现分层存储和归档功能，利用阿里云 OSS 的分层存储能力兼容 HDFS 分层存储策略。这项功能可以让用户选择较低成本的存储策略来存储访问频率较低的数据，从而降低总的存储成本。
与此同时，JindoFS 新增支持了 HDFS AuditLog 功能，大幅提升了 Apache HDFS 接口实现、兼容性和功能对齐程度。在 OSS 数据快速导入和半托管 JindoFS 迁移支持上也做了进一步完善。 

在 JindoFSx 存储加速系统上，这个版本增加支持客户端本地缓存（Local cache），提供了纯客户端的缓存加速的能力，大幅改进和优化了在元数据缓存上的能力，完善了对阿里云 NAS 的缓存加速上的支持。

在SDK与生态组件方面，大幅提升了多个操作上的性能和吞吐，增加支持了 ObjectSDK，在兼容 OSS 对象存储 API 的同时大幅提升各项操作性能，同时无缝对接 JindoFSx 加速能力。 
推出 JindoDistJob 工具，支持半托管 JindoFS 全量和增量迁移文件元数据， 支持用户在不迁移数据块的同时平稳切换到JindoFS 服务化的方案上。大幅增强 JindoDistCp 迁移工具，
实现 Apache HDFS 到 JindoFS 服务的无损迁移，在拷贝数据的同时保证文件元数据的拷贝。

## 主要功能
### JindoFS 存储系统
- JindoFS 支持分层存储和归档，兼容 HDFS 存储策略。
- JindoFS 支持 BatchImport，提供了文件数据批量导入的能力。
- JindoFS 支持 HDFS AuditLog 审计日志。
- JindoFS 支持 Concat 和 SymLink 接口。
- JindoFS 优化了文件数据的后台清理能力。
- JindoFS 优化了 lease 和 lock 的性能。

### JindoFSx 存储加速系统
- JindoFSx 支持缓存插件和并提供客户端缓存模式。
- JindoFSx 完成对鉴权插件化的支持，默认情况下无须安装 KRB5 和 SASL 库依赖。
- JindoFSx 大幅优化了元数据缓存性能，完善了对阿里云 NAS 的缓存加速支持。

### JindoSDK 和工具支持
- JindoSDK 完善了对 HTTPS 的支持，改进了对弱网环境的。
- JindoSDK 改进了部署，默认情况下去除了对 KRB5 和 SASL 库的依赖。
- JindoSDK 增加支持 OSS 对象存储 API，大幅提升操作性能并无缝对接 JindoFSx 缓存加速能力。
- 新增 JindoDistJob 工具，支持半托管 JindoFS Block模式数据快速迁移到 JindoFS 服务。
- JindoDistCp 大幅完善对 Apache HDFS 到 JindoFS 服务的数据迁移能力，文件元数据一起无损迁移。

### JindoFuse POSIX 支持
- JindoFuse 优化了大文件顺序读性能。