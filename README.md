
![](logo/JindoFS.png)

简体中文 | [English(WIP)](#)

## 介绍

JindoFS 作为阿里云基于 OSS 的一揽子数据湖存储优化方案，完全兼容 Hadoop/Spark 生态，并针对 Spark、Hive、Flink、Presto 等大数据组件和 AI 生态实现了大量扩展和优化。JindoFS 项目包括 JindoFS OSS 支持、JindoFS 分布式缓存系统（JindoFS Cache 模式）和 JindoFS 分布式存储优化系统（JindoFS Block 模式）。JindoSDK 是各个计算组件可以用来使用JindoFS 这些优化扩展功能和模式的套件，包括 Hadoop Java SDK、Python SDK 和 Fuse/POSIX 支持。JindoSDK 在阿里云 E-MapReduce 产品中被深度集成，同时也开放给非 EMR 产品用户在各种 Hadoop/Spark 环境上使用，欢迎大家反馈问题和提供最佳实践。

## JindoFS SDK 生产使用

* [Hadoop/Spark 生态使用 JindoFS SDK](docs/jindofs_sdk_overview.md)

* [JindoFS Python SDK](docs/pyjindo/jindosdk_python_sdk.md)

* [JindoFS Fuse](docs/jindofs_fuse/jindofs_fuse_overview.md)

## JindoFS SDK 开发使用(IDE)

* [Hadoop 使用 JindoFS SDK 在 IDE 开发调试](docs/jindofs_sdk_ide_hadoop.md)

* [Spark 使用 JindoFS SDK 在 IDE 开发调试](docs/jindofs_sdk_ide_spark.md)

## 数据迁移最佳实践

* [Jindo Distcp 介绍和使用](docs/jindo_distcp/jindo_distcp_overview.md)

## JindoFS 云原生

* [Fluid 结合 JindoFS 介绍和使用](docs/jindo_fluid/jindo_fluid_overview.md)

## JindoFS 比较

* [JindoFS SDK 和 Hadoop OSS Connector 比较](#)(*Coming Soon*)

* [JindoFS 缓存系统（Cache 模式）性能比较](#) (*Coming Soon*)

* [JindoFS 存储系统（Block 模式）和 HDFS 比较](docs/comparisons/jindofs_block_vs_hdfs.md)

## 关于 JindoFS

* [JindoFS 介绍和使用](https://help.aliyun.com/document_detail/199488.html)

## JindoFS 相关文章

* [JindoFS 相关文章](docs/jindofs_articles.md)

