# JindoData 4.5.0 版本说明
## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

JindoFS 存储系统上着重优化元数据的操作性能使得相关元数据性能得到显著的提升，具体性能提升如下图所示。 完善JindoFS分层存储功能，支持低频以及冷归档存储类型。支持批量写入功能，优化大规模ETL作业性能。在SDK与生态组件方面，提供独立Java SDK可以支持访问 OSS、OSS-HDFS服务、JindoFSx等， 支持自适应预读算法，提升JindoSDK预读的效率。


## 主要功能
### JindoFS 存储系统
- JindoFS 元数据操作性能优化，相关元数据操作性能显著提升。
- JindoFS完善分层存储功能，支持低频以及冷归档存储类型。
- JindoFS 支持批量写入功能，优化大规模ETL作业性能。
- JindoFS 修复服务端授权错误时访问OSS时会导致服务异常问题

### JindoFSx 存储加速系统
- JindoFSx 修复Storage服务文件句柄泄漏问题
- JindoFSx 修复客户端metrics上报线程安全问题。
- JindoFSx 优化递归创建父目录性能。
- JindoFSx 优化路径改写功能性能。
### JindoSDK 和工具支持
- JindoObject 支持自适应预读算法，提升预读效率。
- JindoSDK 支持基于表格存储原子Rename功能。
- Jindo DistCP优化diff功能， 支持输出Diff文件。
- JindoSDK 统一处理重试错误，解决服务端IP变化导致的客户端重现失败问题。
- JindoSDK 提供独立的Java SDK， 与HadoopSDK、ObjectSDK平级，同时支持访问OSS、OSS-HDFS服务、JindoFSx等。

### JindoFuse POSIX 支持
- JindoFuse 修复JindoFSx开启缓存List操作导致的内存泄漏问题