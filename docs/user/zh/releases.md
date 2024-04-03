# JindoSDK 版本记录

## 6.3.3，2024-03-20

### 版本概要

发布 JindoSDK 6.3.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.3 的 Maven 仓库](jindosdk/oss-maven.md)
- 修复 JindoFuse 创建 symlink 逻辑，支持 csi plugin 场景使用
- 支持通过设置的反向代理访问对象存储

## 6.3.2，2024-02-26

### 版本概要

发布 JindoSDK 6.3.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.2 的 Maven 仓库](jindosdk/oss-maven.md)
- 修复 JindoFuse 读取 symlink 逻辑，symlink target 改为相对路径以支持 csi plugin
- 修复 JindoCache 对 fs.oss.signer.version 的支持
- 修复 jindo CLI 参数 -version

## 6.3.1，2024-02-21

### 版本概要

发布 JindoSDK 6.3.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.1 的 Maven 仓库](jindosdk/oss-maven.md)
- 修复在 OSS-HDFS 文件上进行反向 seek 导致预读失效的问题
- 修复在由随机写产生的 OSS-HDFS 文件上 pread 预读错误的问题
- 修复 getRealPath 接口
- Root Policy 路径改写支持根据子路径区分的一对多替换

## 6.3.0，2024-01-31

### 版本概要

发布 JindoSDK 6.3.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.0 的 Maven 仓库](jindosdk/oss-maven.md)
- 支持使用 V4 签名访问 OSS/OSS-HDFS
- 提供 Python SDK，支持访问 OSS 和 OSS-HDFS
- 修复 Jindo 命令行删除 OSS-HDFS 目录时发起过多请求的问题
- JindoTable 分层存储工具支持归档数据直接转冷归档
- JindoTable MoveTo 工具支持 Iceberg 表整体迁移
- 修复 JindoFuse 读取 symlink 失败

## 6.1.6，2023-12-28

### 版本概要

发布 JindoSDK 6.1.6 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.6 的 Maven 仓库](jindosdk/oss-maven.md)
- 支持使用 V4 签名访问 OSS/OSS-HDFS

## 6.2.0，2023-12-22

### 版本概要

发布 JindoSDK 6.2.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.2.0 的 Maven 仓库](jindosdk/oss-maven.md)
- 新增支持异步的 CSDK `libjindosdk_c.so`，老版本 `libjindo-csdk.so` 逐渐弃用。
- 修复路径合法性检查，支持带 `*` 的路径
- 优化 JindoFuse 访问 oss 时有非法名称的路径时的报错信息
- Jindo 命令行支持 OSS-HDFS setStoragePolicy 解冻相关策略
- 采用新的预读算法，顺序读大文件的性能提高
  > 新预读算法可配置参数如下
  > - `fs.oss.read.readahead.prefetcher.version = default` 预读实现切换开关
      >   - 可选值：`legacy` 原预读算法, `default` 新预读算法
  > - `fs.oss.read.readahead.prefetch.size.max = 268435456` 预读最大长度（单位：byte）
  >
  > 新预读算法可能使用更多内存。若配置了新预读算法后发生性能下降，可能是由于内存池容量不足导致预读的块在被访问到之前就被提前逐出。为了避免该情况发生，可以考虑缩减最大预读长度，或允许预读使用更多内存。内存池配置相关参数如下
  > - `fs.oss.memory.buffer.size.max.mb = 6144` 内存池总容量（单位：MB）
  > - `fs.oss.memory.buffer.size.watermark = 0.3` 内存池用于预读的容量比例
- 修复 Hive 使用 LDAP + Kerberos + OSS-HDFS 时的认证问题

## 6.1.5，2023-12-19

### 版本概要

发布 JindoSDK 6.1.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.5 的 Maven 仓库](jindosdk/oss-maven.md)
- OSS-HDFS 读写 IO 优化

## 6.1.4，2023-12-14

### 版本概要

发布 JindoSDK 6.1.4 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.4 的 Maven 仓库](jindosdk/oss-maven.md)
- 支持 HiveServer2 在 Kerberos & LDAP 双认证场景下使用
- JindoSDK Java Jni 忽略 AddressSanitizer 误报

## 6.1.3，2023-12-01

### 版本概要

发布 JindoSDK 6.1.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.3 的 Maven 仓库](jindosdk/oss-maven.md)
- 修复 `dls://` 前缀访问 jindofs 的若干问题。

## 6.1.2，2023-11-21

### 版本概要

发布 JindoSDK 6.1.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.2 的 Maven 仓库](jindosdk/oss-maven.md)
- 通过配置 `fs.oss.list.type=2` 支持ListObjectV2，以避免访问开启了多版本bucket时的操作超时问题。
- 支持使用 `dls://` 前缀访问 jindofs。
- 修复读流 close 会卡住的问题。
- 修复写流出现一次 write 失败后，后续 write 出现的 NPE 问题。

## 6.1.1，2023-10-20

### 版本概要

发布 JindoSDK 6.1.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.1 的 Maven 仓库](jindosdk/oss-maven.md)
- 支持通过配置 fs.oss.legacy.version 为 3.8，兼容 JindoSDK 3.8.x 配置

## 6.1.0，2023-09-28

### 版本概要

发布 JindoSDK 6.1.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.0 的 Maven 仓库](jindosdk/oss-maven.md)
- JindoSDK 支持 FileSystem close 后仍支持使用读写流进行读写

## 6.0.1，2023-08-18

### 版本概要

发布 JindoSDK 6.0.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.0.1 的 Maven 仓库](jindosdk/oss-maven.md)
- JindoSDK 支持 isFileClosed 方法，用于判断访问 OSS-HDFS 的读写流是否已关闭。
- JindoSDK 对 flush 降频做了优化，在降频配置生效时，对第一次有数据的flush操作保证执行，防止flush出一个空文件。降频配置，可参考[Flume 使用 JindoSDK 写入 OSS](/docs/user/jindosdk/flume/jindosdk_on_flume.md)

## 6.0.0，2023-08-15

### 版本概要

发布 JindoSDK 6.0.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.0.0 的 Maven 仓库](jindosdk/oss-maven.md)，JindoSDK 合并 jindo-mapreduce (JindoCommitterFactory等) 到 jindo-sdk
- JindoSDK 新增若干修复，包含 flush 降频，优化 GC 回收，适配 OSS-HDFS 回收站等
- 多平台支持 MACOS 和 主流 linux 场景，其中 CentOS 6, Ubuntu22, 阿里云倚天机型(ARM)，MacOS (Intel/M1) 需要使用特定的扩展包
- 升级 g++ 编译器到 10.4，使用 c++20 标准
- 支持 [yalangtinglibs](https://github.com/alibaba/yalantinglibs) 访问 OSS/OSS-HDFS