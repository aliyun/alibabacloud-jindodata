# 版本说明

JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

## JindoData 4.6.x 版本

### 版本概要
JindoData 4.6.x 版本推出平滑迁移功能，支持HDFS 到 OSS-HDFS平滑迁移。可以极大的简化用户的数据迁移流程。JindoFS 存储系统支持文件清单功能，用户可以基于文件清单更好了解数据的分布以及归属。在性能优化方面，JindoFS 存储系统通过存量以及增量的方式优化Du/Count性能，显著提升Du/Count操作性能。 对于JindoSDK 而言，JindoSDK 4.6.x 版本支持文件以及数据块的级别的校验，提高JindoSDK 写入链路的稳定性。 此外JindoSDK 还支持多路径访问协议，支持不同协议模式访问同一后端路径。

### JindoData 4.6.2 版本
JindoData 4.6.2 版本是在4.6.1版本基础上做了大量的修复。
#### JindoFS 存储系统
- 修复分层存储 STD 转 STD 时导致服务卡住问题。
- 修复分层存储产生空manifest导致服务卡住问题。
- 加速分层存储任务执行速度。
- 修复 RootPolicy 功能逻辑。
- 修复 setAcl 偶发服务 crash 问题。
- 修复低概率发生DB manifest文件占满磁盘的问题。
- 修复迁移服务的批量元数据导入功能。

### JindoData 4.6.1 版本
#### JindoFS 存储系统
- JindoFS 减少一些冗余日志打印。
- 修复元数据清单导出没有close的文件大小不对问题。

#### JindoFSx 存储加速系统
- JindoFSx 支持缓存临时目录实现自清理。

#### JindoSDK 和工具支持
- 优化 JindoSDK 输出体积过大问题。
- du/count 默认开启走服务端优化路径。
- 降低 sts 更新频率，避免频繁发送请求导致限流。
- 免密URL的ram改为小写，避免 ecs 免密服务内部刷新 token 失败。

### JindoData 4.6.0 版本
#### JindoFS 存储系统
- JindoFS 支持OSS-HDFS 文件清单导出功能，用户可以基于文件清单功能更好的了解数据分布以及二次开发。
- JindoFS 通过服务端存量加增量的优化，显著提高Du/Count 操作的性能。
- JindoFS 支持HDFS 到 OSS-HDFS平滑迁移功能，可以极大的简化 HDFS 到 OSS-HDFS数据迁移流程。
- JindoFS 支持多路径协议访问，用户可以使用不同访问协议访问同一后端路径。

#### JindoFSx 存储加速系统
- 修复JindoFSx 客户端写缓存时导致客户端异常退出问题。
- 修复JindoFSx 客户端Metrics 上报导致客户端异常退出问题。
- 修复JindoFSx Ranger使用过程中内存泄漏问题。

#### JindoSDK 和工具支持
- JindoSDK 支持CRC/MD5 Checksum校验， 支持文件级别以及数据块级别的写入校验。
- 支持Jindo Sync 数据同步工具， 用户可以不依赖Hadoop环境进行数据同步。
- JindoSDK 支持OSS-HDFS TensorFlow Connector。

# JindoData 4.5.2 版本说明
JindoData 4.5.2 版本是在4.5.1版本基础上做了大量的修复。

# JindoData 4.5.1 版本说明
## 版本概要


这个版本是对 4.5.0 的小幅升级。上一版本发布以来，我们积累了大量的客户经验，主动发现以及客户反馈了一些真实问题，我们对此做了重要的修复和改进。JindoFS 改进了服务稳定性以及一些异常情况处理。JindoFS 和 JindoFSx 进一步改进自适应预读算法，提高预读效率。JindoDistCp 做了大量修复和优化，增强了数据拷贝过程的稳定性。JindoFuse采用了新的底层设计，进一步提高了性能。

## 主要功能
### JindoFS 存储系统
- JindoFS 改进了内存使用问题。
- JindoFS 添加 ASSUME_ROLE 错误的异常处理和日志告警。
- JindoFS 支持重试时更新动态AK。
- JindoFS 进一步改进自适应预读算法，提升预读效率。
- JindoFS 文件随机写场景的读写路径修复。
- JindoFS 支持 checkAccess 接口。

### JindoFSx 存储加速系统
- JindoFSx 进一步改进自适应预读算法，提升预读效率。
- JindoFSx 支持路径带空格。
- JindoFSx 改进多副本读可能存在热点的问题。

### JindoSDK 和工具支持
- Jindo 命令提供完整的 Hadoop 命令覆盖。
- Jindo 命令增加对 HDFS 的纯 native 支持，大幅提升性能和用户使用体验。
- JindoDistCp 支持对接阿里云监控（CloudMonitor）。
- JindoDistCp 支持 OSS->HDFS 路径的 checksum 检查。
- JindoDistCp 支持 Job 切分参数.
- JindoDistCp 修复拷贝过程中源文件删除错误处理逻辑。
- JindoSDK 优化随机读的内存占用。

### JindoFuse POSIX 支持
- JindoFuse 使用 low-level API 重新设计，大幅提高 readdir 等操作的性能。
- JindoFuse 修复挂载 JindoFSx 后出现异常程序 list 根目录问题。

# JindoData 4.5.0 版本说明
## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

JindoFS 存储系统上着重优化元数据的操作性能使得相关元数据性能得到显著的提升。 完善JindoFS分层存储功能，支持低频以及冷归档存储类型。支持批量写入功能，优化大规模ETL作业性能。在SDK与生态组件方面，提供纯粹的去Hadoop依赖的Java SDK。


## 主要功能
### JindoFS 存储系统
- JindoFS 元数据操作性能优化，相关元数据操作性能显著提升。
- JindoFS 完善分层存储功能，支持低频以及冷归档存储类型。
- JindoFS 支持批量写入功能，优化大规模ETL作业性能。
- JindoFS 修复服务端授权错误时访问OSS时会导致服务异常问题。

### JindoFSx 存储加速系统
- JindoFSx 修复Storage服务文件句柄泄漏问题
- JindoFSx 修复客户端metrics上报线程安全问题。
- JindoFSx 优化递归创建父目录性能。
- JindoFSx 优化路径改写功能性能。

### JindoSDK 和工具支持
- JindoSDK 支持自适应预读算法，提升预读效率。
- JindoSDK 支持基于表格存储原子Rename功能。
- JindoDistCp 优化diff功能， 支持输出Diff文件。
- JindoSDK 统一处理重试错误，解决服务端IP变化导致的客户端重现失败问题。
- JindoSDK 提供纯粹的去Hadoop依赖的Java SDK，与HadoopSDK、ObjectSDK平级。

### JindoFuse POSIX 支持
- JindoFuse 修复JindoFSx开启缓存List操作导致的内存泄漏问题。

# JindoData 4.4.x 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。

JindoFS 存储系统上实现分层存储和归档功能，利用阿里云 OSS 的分层存储能力兼容 HDFS 分层存储策略。这项功能可以让用户选择较低成本的存储策略来存储访问频率较低的数据，从而降低总的存储成本。
与此同时，JindoFS 新增支持了 HDFS AuditLog 功能，大幅提升了 Apache HDFS 接口兼容、功能对齐和数据迁移能力。在 OSS 数据快速导入和半托管 JindoFS 迁移支持上也做了进一步完善。 
备注：JindoFS 功能目前是通过阿里云 OSS-HDFS 服务对外提供，具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

在 JindoFSx 存储加速系统上，这个版本增加支持客户端本地缓存（local cache），提供了纯客户端的缓存加速的能力，大幅改进和优化了在元数据缓存上的能力，完善了对阿里云 NAS 的缓存加速上的支持。

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
- JindoFS 优化了 lease 和 lock 相关操作的性能。

### JindoFSx 存储加速系统
- JindoFSx 支持缓存插件和并提供客户端缓存模式。
- JindoFSx 完成对鉴权插件化的支持，默认情况下无须安装 KRB5 和 SASL 库依赖。
- JindoFSx 大幅优化了元数据缓存性能，完善了对阿里云 NAS 的缓存加速支持。

### JindoSDK 和工具支持
- JindoSDK 完善了对 HTTPS 的支持，改进了对弱网环境的容错能力。
- JindoSDK 改进了部署，默认情况下去除了对 KRB5 和 SASL 库的依赖。
- JindoSDK 增加支持 OSS 对象存储 API，大幅提升操作性能并无缝对接 JindoFSx 缓存加速能力。
- 新增 JindoDistJob 工具，支持半托管 JindoFS Block模式数据快速迁移到 JindoFS 服务。
- JindoDistCp 大幅完善对 Apache HDFS 到 JindoFS 服务的数据迁移能力，文件元数据一起无损迁移。

### JindoFuse POSIX 支持
- JindoFuse 优化了大文件顺序读性能。

# JindoData 4.3.x 版本说明

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


# JindoData 4.2.x 版本说明

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

# JindoData 4.1.x 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。
JindoData 4.1.0 版本在阿里云 OSS-HDFS 服务（JindoFS 服务）上支持随机写等重要特性，另外添加 JindoFSx 存储加速系统，支持对原生阿里云 OSS 和 OSS-HDFS 服务（JindoFS 服务）提供分布式缓存。

## 主要功能

### JindoFS 存储系统
#### 1. JindoFS 服务能力
- 支持文件随机写，文件可修改写入。
- 支持 HDFS 回收站，系统后台按过期时间清理回收站文件。
- 完善 HDFS 快照功能，快照支持随机修改文件。
- 改进目录删除机制，大幅提升操作性能。
- 实现 NsWorker 框架，支持元数据服务将一些繁重处理卸载到 follower/learner 节点上去执行。

#### 2. JindoShell CLI 支持
- 支持用户使用命令设定 HDFS 回收站过期时间。
- 支持快照功能 snapshotDiff 命令，查看两个快照之间的差异。
- 改进 dumpFile 命令，输出随机写文件相关信息。

#### 3. JindoFuse POSIX 支持
- 支持文件随机修改（seek and write）。

### JindoFSx 存储加速系统
#### 1. JindoFSx 核心能力
- 支持对阿里云 OSS 透明缓存加速（保持 `oss://` 不变）。
- 支持对阿里云 OSS-HDFS 服务（JindoFS 服务）透明缓存加速（保持 `oss://` 不变）。
- 统一命名空间功能，支持把 OSS 或 OSS-HDFS 挂载到同一个命名空间，使用 `fsx://` 前缀进行统一操作。
- 支持大规模文件元数据缓存加速。
- 支持小文件训练加速。
- 支持 P2P 加速，对大量训练节点同时预热加载模型文件场景，大幅提升缓存读取性能。

#### 2. JindoSDK Hadoop 支持
- 提供 `JindoOssFileSystem` 支持 OSS 与 OSS-HDFS 的透明缓存加速使用。
- 提供 `JindoFsxFileSystem` 支持统一名字空间方式使用。

#### 3. JindoShell CLI 支持
- 支持 JindoFSx 数据缓存命令。
- 支持 JindoFSx 元数据缓存命令。
- 支持 JindoFSx 统一命名空间管理命令。

#### 4. JindoFuse POSIX 支持
- 支持 `oss://` 路径 fuse 挂载，读写 JindoFSx 缓存。
- 支持 `fsx://` 路径 fuse 挂载，读写 JindoFSx 缓存。

# JindoData 4.0.x 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。
JindoData 4.0.0 是原阿里云 EMR SmartData 自研组件（大版本到 3.8.0）架构升级之后的首次版本发布，重点对接和支持了阿里云 OSS 存储产品和阿里云 OSS-HDFS 服务（JindoFS 服务）。后续版本会陆续支持更多的存储系统和服务。注意，JindoData 4.0.0 版本暂未发布 JindoFSx 存储加速系统。

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
