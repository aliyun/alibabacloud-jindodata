# 6.8.0，2025-01-24

## 版本概要

发布 JindoSDK 6.8.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.0 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 优化访问 OSS-HDFS 请求数，读场景元数据请求降低17%。
- nextarch classifier 优化 Java TimedBuffer 获取时间戳频率，降低 CPU 占用。
- nextarch classifier 优化 JindoMagicCommitter PendingSet 大小。
- nextarch classifier 优化预读算法。并新增配置，控制是否立即清除完全读取的预读内存。
- nextarch classifier 新增 metrics 若干，支持通过 java 接口获取 metrics。
- nextarch classifier 新增日志相关配置， 支持日志实例常驻。
- nextarch classifier 修复 OSS-HDFS 对 fs.jdo 前缀配置的支持。
- nextarch classifier 修复访问 jindocache 服务端兼容性问题。
- nextarch classifier 访问 OSS 设置 XAttr 时, 默认设置 x-oss-metadata-directive 为 REPLACE。
- JindoFuse 优化了访问 oss 场景的元数据请求数，支持以 attr_timeout 为时间间隔做元数据请求。
- JindoFuse 修复 jindo-fuse metrics 时间窗口。