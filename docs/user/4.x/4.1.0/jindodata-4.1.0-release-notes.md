# JindoData 4.1.0 版本说明

## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。
JindoData 4.1.0 版本在阿里云 OSS-HDFS 服务（JindoFS 服务）上支持随机写等重要特性，另外添加 JindoFSx 缓存系统，支持对原生阿里云 OSS 和 OSS-HDFS 服务（JindoFS 服务）提供分布式缓存。

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

### JindoFSx 缓存系统
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
