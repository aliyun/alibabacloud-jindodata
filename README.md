
![](logo/JindoFS.png)

简体中文

## Jindo 介绍

Jindo 是阿里云大数据平台 EMR (E-MapReduce) 的核心自研组件，主要包含三个部分：JindoFS、JindoTable 与 JindoFuse/Python，为各种大数据引擎提供统一的存储优化、缓存优化、计算加速，以及多种扩展功能，涵盖数据访问、数据治理和数据安全。Jindo 在远端存储与计算引擎之间构成一层加速层，与二者的关系如图：

![](pic/jindo_introduction.png)

## JindoFS 介绍

JindoFS 作为阿里云基于 OSS 的一揽子数据湖存储优化方案，完全兼容 Hadoop/Spark 生态，并针对 Spark、Hive、Flink、Presto 等大数据组件和 AI 生态实现了大量扩展和优化。JindoFS 项目包括 JindoFS OSS 支持、JindoFS 分布式缓存系统（JindoFS 兼容模式）和 JindoFS 分布式存储优化系统（JindoFS 存储模式）。JindoSDK 是各个计算组件可以用来使用JindoFS 这些优化扩展功能和模式的套件，包括 Hadoop Java SDK、Python SDK 和 Fuse/POSIX 支持。JindoSDK 在阿里云 E-MapReduce 产品中被深度集成，同时也开放给非 EMR 产品用户在各种 Hadoop/Spark 环境上使用，欢迎大家反馈问题和提供最佳实践。关于JindoFS 请参考 [JindoFS 介绍和使用](https://help.aliyun.com/document_detail/199488.html)。JindoSDK 下载请访问[JindoSDK 下载页面](/docs/jindofs_sdk_download.md)。

## JindoTable 介绍

JindoTable 是 JindoFS 结合计算引擎的使用推出的一套解决方案，支持 Spark、Hive、Presto 等引擎，以及表格式数据的管理功能。目前 JindoTable 提供的功能包括计算加速和基于表分区的分层存储功能。关于JindoTable 请参考 [JindoTable 介绍和使用](/docs/jindotable/jindotable.md)。

## JindoFS SDK 生产使用

* [Hadoop/Spark 生态使用 JindoFS SDK](docs/jindofs_sdk_overview.md)

* [JindoFS Python SDK](docs/pyjindo/jindosdk_python_sdk.md)

* [JindoFS Fuse](docs/jindofs_fuse/jindofs_fuse_overview.md)

* [JindoFS SDK 和 Hadoop OSS Connector 性能对比测试](docs/jindofs_sdk_vs_hadoop_sdk.md)

* [JindoFS Fuse 性能测试](docs/jindofs_fuse/jindofs_fuse_benchmark.md)

## JindoFS SDK 开发使用(IDE)

* [Hadoop 使用 JindoFS SDK 在 IDE 开发调试](docs/jindofs_sdk_ide_hadoop.md)

* [Spark 使用 JindoFS SDK 在 IDE 开发调试](docs/spark/jindofs_sdk_ide_spark.md)

## 数据迁移最佳实践

* [Jindo Distcp 数据迁移](docs/jindo_distcp/jindo_distcp_overview.md)

* [JindoTable 数仓迁移](docs/jindotable/jindotable_data_migration.md)

## JindoFS 云原生

* [Fluid 结合 JindoFS 介绍和使用](docs/jindo_fluid/jindo_fluid_overview.md)

## JindoFS 兼容模式

* [JindoFS 兼容模式部署](docs/jindofs_cache_mode_deploy.md)

* [JindoFS 兼容模式性能测试](docs/comparisons/jindofs_cache_vs_no_cache.md)

## JindoFS 存储模式

* [JindoFS 存储模式部署](docs/jindofs_block_mode_deploy.md)

* [JindoFS 存储模式和 HDFS 比较](docs/comparisons/jindofs_block_vs_hdfs.md)

## JindoFS 相关文章

* [JindoFS 相关文章](docs/jindofs_articles.md)

