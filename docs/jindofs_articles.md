
# OverView
- [JindoFS 相关文章](#jindofs-%E7%9B%B8%E5%85%B3%E6%96%87%E7%AB%A0)
  - [JindoFS 基础介绍](#jindofs-%E5%9F%BA%E7%A1%80%E4%BB%8B%E7%BB%8D)
  - [数据湖 JindoFS + OSS  36讲（持续更新中）](#%E6%95%B0%E6%8D%AE%E6%B9%96-jindofs--oss--36%E8%AE%B2%E6%8C%81%E7%BB%AD%E6%9B%B4%E6%96%B0%E4%B8%AD)
  - [JindoFS 云原生场景加速](#jindofs-%E4%BA%91%E5%8E%9F%E7%94%9F%E5%9C%BA%E6%99%AF%E5%8A%A0%E9%80%9F)
  - [JindoFS 数据湖加速](#jindofs-%E6%95%B0%E6%8D%AE%E6%B9%96%E5%8A%A0%E9%80%9F)

# JindoFS 相关文章

## JindoFS 基础介绍
JindoFS 作为阿里云基于 OSS 的一揽子数据湖存储优化方案，完全兼容 Hadoop/Spark 生态，并针对 Spark、Hive、Flink、Presto 等大数据组件和 AI 生态实现了大量扩展和优化。JindoFS 项目包括 JindoFS OSS 支持、JindoFS 分布式缓存系统（JindoFS Cache 模式）和 JindoFS 分布式存储优化系统（JindoFS Block 模式）。JindoSDK 是各个计算组件可以用来使用JindoFS 这些优化扩展功能和模式的套件，包括 Hadoop Java SDK、Python SDK 和 Fuse/POSIX 支持。JindoSDK 在阿里云 E-MapReduce 产品中被深度集成，同时也开放给非 EMR 产品用户在各种 Hadoop/Spark 环境上使用。
* [《JindoFS 概述 - 云原生的大数据计算存储分离方案》](https://developer.aliyun.com/article/720081)

* [《JindoFS 解析 - 云上大数据高性能数据湖存储方案》](https://developer.aliyun.com/article/720312)
  
## 数据湖 JindoFS + OSS  36讲（持续更新中）
为了让更多开发者了解并使用 JindoFS+OSS，由阿里云JindoFS+OSS 团队打造的专业公开课【数据湖JindoFS+OSS 实操干货36讲】会在 每周二16：00 准时直播开讲！从数据迁移、OSS访问加速、JindoFS缓存加速、AI训练加速、JindoTable计算加速，五大版块入手，带你玩转数据湖。文章和直播详情请访问如下链接获取：

* [数据湖 JindoFS + OSS  36讲](./jindofs_article/datalake_JindoFS_OSS_36_all.md)
  
## JindoFS 云原生场景加速
在云原生环境中，通过 [CNCF Fluid](https://github.com/fluid-cloudnative/fluid) 和 JindoFS 相结合来提供对 K8S 上集群的访问加速能力。JindoRuntime 使用 JindoFS 的 Cache 模式进行远端文件的访问和缓存，支持 OSS、HDFS、标准 S3 协议等多种存储产品的访问和缓存加速。在 Fluid 上使用和部署 JindoRuntime 流程简单、兼容原生 K8s 环境、可以开箱即用。深度结合对象存储特性，使用 Navite 框架优化性能，并支持免密、checksum 校验等云上数据安全功能。
* [拥抱云原生，Fluid 结合 JindoFS ：阿里云 OSS 加速利器](https://developer.aliyun.com/article/781935)
* [微博海量深度学习模型训练效率跃升的秘密](https://www.infoq.cn/article/FClx4Cco6b1jomi6UZSy) 
  
## JindoFS 数据湖加速
### 基于 JindoFS 和 OSS 构建数据湖
* [《基于 JindoFS+OSS 构建高效数据湖》](https://developer.aliyun.com/article/772305)
* [《基于 OSS 的 EB 级数据湖》](https://developer.aliyun.com/article/772300)
* [《10亿+文件数压测，阿里云 JindoFS 轻松应对》](https://developer.aliyun.com/article/781801)

### JindoFS 数据湖加速 
* [《数据湖架构，为什么需要“湖加速”？》](https://developer.aliyun.com/article/774556)
* [《JindoFS 缓存加速数据湖上的机器学习训练》](https://developer.aliyun.com/article/772307)
  
### JindoTable 加速计算分析
* [《JindoFS - 分层存储》](https://developer.aliyun.com/article/766586)
* [《JindoTable 数据湖优化与查询加速》](https://developer.aliyun.com/article/772311)
* [《JindoSpark Relational Cache 实现亚秒级响应的交互式分析》](https://developer.aliyun.com/article/725413)

### 更多文章
* [更多数据湖系列文章...](https://developer.aliyun.com/group/datalakeformation)