# JindoData 4.0.0 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。
JindoData 4.0.0 是阿里云 E-MapReduce 产品 SmartData 自研组件（大版本到 3.8.0）架构升级之后的首次版本发布，重点对接和支持了阿里云 OSS 存储产品和阿里云 OSS-HDFS 服务（JindoFS 服务）。后续版本会陆续支持更多的存储系统和服务。注意，JindoData 4.0.0 版本暂未发布 JindoFSx 存储加速系统。

## 主要功能

### 阿里云 OSS 服务
#### 1. JindoSDK Hadoop 支持
- 为阿里云 OSS 提供 Java Hadoop SDK，完全兼容 Hadoop OSS Connector，性能上大幅领先，之前版本有几千家客户线上生产使用。
- 支持多种 Credential Provider 设置方式，包括配置、ECS Role 和 EMR 免密机制。
- 支持写入时就归档，包括归档和深冷归档。

#### 2. JindoShell CLI 支持
- 对 Hadoop/HDFS shell 提供额外的命令扩展，为阿里云 OSS 提供面向 Hadoop 用户使用的操作方式。
- 支持 ls2 扩展命令，在标准 ls 命令基础上可以额外显示文件/对象在 OSS 上的存储状态（标准、低频还是归档）。
- 支持 archive 命令，允许用户指定目录进行转归档操作。
- 支持 restore 命令，允许用户指定目录做解冻操作。

#### 3. JindoFuse POSIX 支持
- 为阿里云 OSS 提供优化的 fuse 客户端，受益于完全 native 代码的开发实现，性能在业界大幅领先。

#### 4. JindoDistCp 数据迁移支持
- 支持用户将自建 HDFS 集群数据迁移到阿里云 OSS，针对大文件、大量小文件场景优化。

### 阿里云 OSS-HDFS 服务（JindoFS 服务）
#### 1. JindoFS 服务
- 为阿里云 OSS 产品增加一种新的 bucket 存储选项，提供元数据加速功能，二进制兼容且功能全面对齐 Apache HDFS，支持 HDFS 用户平迁上云。
- 原生支持文件系统目录语义，大幅优化目录操作，超大目录 rename 支持原子性和毫秒级能力。
- 原生支持文件系统文件语义，支持 HDFS 写租约、一写多读和边写边读。
- 支持文件 append 写入、flush/sync 和 truncate 操作。
- 支持 HDFS 快照，支持近乎无限次快照数量，方便数据备份、容灾和恢复。
- 支持文件权限。用户组信息支持用户通过 JindoShell 命令导入设定（UserGroupsMapping）。
- 支持 Hadoop Proxy User 访问控制机制。

#### 2. JindoSDK Hadoop 支持
- JindoSDK 内置支持访问阿里云 OSS-HDFS 服务（JindoFS 服务），提供全面的 HDFS 接口访问和使用体验。

#### 3. JindoShell CLI 支持
- 提供 Hadoop/HDFS shell 额外的命令扩展，为阿里云 OSS-HDFS 服务（JindoFS 服务）提供面向 Hadoop 用户使用的操作方式。
- 支持用户通过 HDFS 命令和 JindoShell 扩展命令使用 HDFS 快照功能。
- 支持用户使用命令导入（UserGroupsMapping），设定用户组信息。
- 支持用户使用命令设定 Hadoop Proxy User 规则。

#### 4. JindoFuse POSIX 支持
- 为阿里云 OSS-HDFS 服务（JindoFS 服务）提供优化的 fuse 客户端，受益于完全 native 代码的开发实现，性能在业界大幅领先；受益于底层文件系统的能力加持，POSIX 能力和兼容性上较业界类似方案大幅改进。


## 已知问题
1. JindoSDK 暂不支持 OSS 上超大文件写入（大于80GB）。
2. JindoSDK 暂不支持 OSS append 方式写入。
3. JindoSDK 暂不支持 OSS 客户端加密。
4. JindoSDK 暂不支持老版本 JindoFS Block 模式和 Cache 模式。
5. 阿里云 OSS-HDFS 服务（JindoFS 服务）暂不支持老版本 JindoFS Block 模式系统升级。需要用户通过 JindoDistCp 迁移工具把数据从老系统迁移到新服务。