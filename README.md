![AlibabaCloud JindoData](docs/image/jindo-logo.png)

# JindoData 介绍
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 套件基于统一架构和内核实现，主要包括 JindoFS 存储系统（原 JindoFS Block 模式），JindoFSx 缓存系统（原 JindoFS Cache 模式），JindoSDK 大数据万能 SDK和全面兼容的生态工具（JindoFuse、JindoDistCp）、插件支持。
![JindoData](docs/image/jindodata-arch.png)

## 主要组件
### JindoFS 存储系统
基于阿里云 OSS 的云原生存储系统，二进制兼容 Apache HDFS，并且基本功能对齐，提供优化的 HDFS 使用和平迁体验。是原 JindoFS Block 模式的全新升级版本。
### JindoFS 服务（OSS-HDFS 服务）
JindoFS 存储系统在阿里云上的服务化部署形态，和阿里云 OSS 深度融合，开箱即用，无须在自建集群部署维护 JindoFS，免运维。
### JindoFSx 缓存系统
面向大数据和 AI 生态的云原生数据湖缓存加速系统，同时提供透明缓存加速（保持原有 scheme 不变）和统一名字空间（<code>fsx://</code>）多挂载两种使用方式。原生优化支持阿里云 OSS，同时也支持业界主要多云对象存储和 Apache HDFS。是原 JindoFS Cache 模式的全新升级版本。
### JindoSDK Hadoop 支持
面向云时代的大数据 Hadoop SDK 和 HDFS 接口支持，内置优化访问阿里云 OSS，较 Hadoop 社区版本性能大幅提升；同时对接支持 JindoFS 存储系统包括服务、JindoFSx 缓存系统；支持多云对象存储。
### JindoShell CLI 支持
JindoData 除了对接支持 Hadoop/HDFS shell 命令，同时提供一套 jindo cli 命令，从功能、性能上大幅扩展和优化一些数据访问操作。
### JindoFuse POSIX 支持
JindoData 为阿里云 OSS、JindoFS 存储系统和服务、JindoFSx 缓存系统提供的 POSIX 支持。
### JindoDistCp 数据迁移支持
IDC 机房数据（HDFS）上云迁移和多云迁移利器，支持多种存储数据迁移到阿里云 OSS 和 JindoFS 服务，使用上类似Hadoop DistCp。
### 生态插件
除了默认提供 JindoSDK 支持 Hadoop，另外还支持 Flink Connector 等插件。

## 发布版本
#### JindoData 4.0.0，2022-01-4，[Release Note](docs/user/4.x/jindodata-4.0.0-release-notes.md)
#### JindoData 3.x 版本，[原阿里云 E-MapReduce SmartData](https://help.aliyun.com/document_detail/121090.html)

## 用户文档
#### JindoSDK + OSS [用户文档](docs/user/4.x/oss/outline.md)
#### JindoFS 服务（OSS-HDFS 服务）[用户文档](docs/user/4.x/jindofs/outline.md)

## 常见问题
进入[常见问题](docs/user/faq.md)
