# JindoData 6.2.0 版本说明

## 版本概要

发布 JindoSDK 6.2.0 正式版的功能

## 介绍

- JindoSDK 更新 [6.2.0 的 Maven 仓库](oss-maven.md)
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

## 下载

JindoData 6.2.0 [下载页面](jindodata_download.md)
