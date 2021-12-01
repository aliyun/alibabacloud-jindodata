
![](v4/image/jindo_logo.png)

## JindoData 介绍

JindoData 是阿里云大数据平台 EMR (E-MapReduce) 的核心自研组件，为各种大数据引擎提供统一的存储优化、缓存优化、计算加速，以及多种扩展功能，涵盖数据访问、数据治理和数据安全。JindoData 在远端存储与计算引擎之间构成一层加速层，与二者的关系如图：

![](v4/image/jindo_introduction.png)

## JindoFS(存储系统) 介绍

JindoFS 作为阿里云基于 OSS 的一套完整的数据湖存储优化方案，完全兼容 Hadoop/Spark 生态，并针对 Spark、Hive、Flink、Presto 等大数据组件和 AI 生态实现了大量扩展和优化。

## JindoFSx(缓存系统) 介绍

JindoFSx 是一套为远端存储提供缓存服务和访问优化的系统，针对带宽瓶颈的场景，为大数据和 AI 场景提供性能保障。

JindoFSx 支持识别数据冷热，智能缓存热数据，提高缓存利用率，同时支持 OSS 分层存储，为用户节约存储成本。

JindoFSx 4.0 版本还支持权限管理和云原生可观测性，保障数据安全的同时，可以让用户直观地查看数据访问效率和缓存的情况。

## JindoSDK 介绍

JindoSDK 是一个简单易用面向 Hadoop、Spark 生态的 OSS 客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现。

JindoSDK 4.0 版本新支持了开启 HDFS 服务的 OSS bucket，即 Data Lake Storage，以下简称 DLS。支持 DLS 的 JindoSDK 全面兼容 Hadoop FileSystem 接口，提供了更好的兼容性和易用性，又能兼具OSS的性能和成本优势。

当然您也可以使用 JindoSDK 仅仅作为 OSS 客户端，相对于 Hadoop 社区 OSS 客户端实现，您仍然可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

## JindoSDK 使用

* [Hadoop/Spark 生态使用 JindoSDK](v4/cn/jindosdk_overview.md)

## JindoSDK 开发

* [Hadoop 使用 JindoSDK 在 IDE 开发调试](v4/cn/jindosdk_ide_hadoop.md)

* [Spark 使用 JindoSDK 在 IDE 开发调试](v4/cn/spark/jindosdk_ide_spark.md)

## 数据迁移最佳实践

* [Jindo Distcp 数据迁移](v4/cn/jindo_distcp/jindo_distcp_overview.md)

## JindoFS 云原生

* [Fluid 结合 JindoFS 介绍和使用](v4/cn/jindo_fluid/jindo_fluid_overview.md)

