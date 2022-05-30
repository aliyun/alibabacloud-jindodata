# JindoData 4.2.0 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。
JindoData 4.2.0 版本大幅完善 JindoFSx 存储加速系统，添加对 Apache HDFS 和阿里云 NAS 存储产品的缓存加速，增强和提供 JindoFuse、JindoDistCp 和JindoTable 等工具。

## 主要功能

### JindoFSx 存储加速系统
- 支持对阿里云 Apache HDFS 透明缓存加速（保持 `hdfs://` 不变）和统一挂载加速（fsx://）。
- 支持对阿里云 NAS 存储产品提供统一挂载加速（fsx://）。
- 全面对接和支持阿里云 OSS-HDFS 服务（JindoFS 服务），完善写入路径支持。

### JindoSDK 和工具支持
- JindoSDK C/C++ 支持，首次支持 C/C++ 版本的 JindoSDK，提供类似 POSIX 的接口方法。
- JindoFuse POSIX 支持，改进和完善 JindoFuse 工具，基于 JindoSDK C/C++ 版本构建。
- JindoDistCp 数据迁移支持，重构和改进 JindoDistCp 工具，简化和去除 3.x 版本中的不常用功能，增强易用性和健壮性。
- JindoTable 工具支持，重构和改进 JindoTable 工具，简化和去除 3.x 版本中的不常用功能，增强易用性和健壮性。
