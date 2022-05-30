# JindoData 4.3.0 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。
JindoData 4.3.0 版本彻底支持多云架构，成为业界第一个有能力同时具备多云、多存储、多种加速扩展、多协议和多种开发语言支持的数据湖存储解决方案。JindoFS 存储系统在 POSIX 支持上做了大幅改进，JindoFSx 系统首次支持 Kerberos + Ranger 安全扩展，JindoSDK 和生态工具在测试覆盖上也做了大幅提升。

## 主要功能

### JindoFS 存储系统
- JindoFS 支持 POSIX Lock、Fallocate 能力。
- JindoFS 支持老版本 JindoFS Block 模式集群升级。

### JindoFSx 存储加速系统
- JindoFSx 后端支持多云存储，包括 S3、COS、OBS。
- JindoFSx 优化数据缓存及元数据缓存。
- JindoFSx 支持 Kerberos + Ranger 鉴权方案。
- JindoFSx 大幅完善可观测性指标。
- JindoFSx 完成与 Fluid 的对接。

### JindoSDK 和工具支持
- JindoSDK 支持多云存储，包括S3、COS、OBS。
- JindoSDK 提供 JindoTable 工具。
- JindoSDK 优化 Flink Connector 插件。
- JindoSDK 完善 JindoDistCp。

### JindoFuse POSIX 支持
- JindoFuse 新增 XAttr 相关接口支持，包括 setxattr、getxattr、listxattr、removexattr。
- JindoFuse 支持 Lock/Fallocate 相关接口。
- JindoFuse 支持 OSS 可追加写对象，包括 append、flush、边写边读功能。
