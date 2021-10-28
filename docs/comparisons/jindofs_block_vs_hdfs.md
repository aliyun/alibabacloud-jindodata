
## JindoFS 存储模式和 HDFS 比较

## 综合比较

从各个方面对 JindoFS 存储模式和 HDFS进行对比，我们可以总结为一个表格，参见

| | HDFS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| JindoFS |
| --- | --- | --- |
| 元数据存储 | 内存 | Raft+RocksDB ( + OTS ) |
| 多个namespace | 支持 | 支持 |
| namespace启动时间 | 分钟级 | 秒级 |
| 元数据规模 | 4亿 | 10亿+ |
| 数据规模 | 100P | 不限 |
| 存储后端 | 本地 | OSS + 本地缓存 |
| 分布式缓存 | 支持 | 支持 |
| 快照 | 支持 | 开发中 |
| 弹性伸缩 | 不支持 | 支持 |
| 冷热分层 | 支持 | 支持 |
| 权限（Ranger) | 支持 | 支持 |
| 审计 | 支持 | 支持 |
| 数据加密 | 支持 | 支持 |

#### 元数据服务

1. JindoFS 的元数据信息会异步实时同步到 OTS 服务上，也可以通过命令将元信息上传到 OSS 。集群如果需要升级/重建/恢复，运维操作十分简单，可以从 OTS 和 OSS 上即时恢复数据。对比HDFS这方面的操作，整个运维操作步长和复杂度都比较高。
1. JindoFS高可用是通过 rocksdb+raft 实现，而HDFS的高可用需要依赖多个组件 ZooKeeper / JournalNode/ ZKFC 实现，需要运维多个被 HDFS 依赖的服务，所以相比较高可用运维方面 JindoFS 优势明显。
1. JindoFS 的元数据服务为 native 实现，无 JVM GC 影响。不需要担心因为 GC 对文件系统的元信息服务的性能带来影响，也不需要针对 GC 进行 JVM 参数调优等运维操作。
1. JindoFS 支持更大规模的元数据，对于10亿+级别文件数的规模，元数据服务性能表现稳定。对比 HDFS 需要进行配置Federation 才能提供超大规模的文件系统的元信息服务，而且在 Federation 架构下需要维护多个 Namenode 实例，运维复杂的成倍提升。 我们对 JindoFS 的元数据服务进行压测，分析其性能和稳定性，可以参考文章 [《10亿+文件数压测，阿里云 JindoFS 轻松应对》](./jindofs_block_vs_hdfs_metaservice.md)。

#### 数据存储

1. 在 JindoFS 存储模式下，数据有1备份存储在 OSS 上, 稳定性有SLA保证。出现坏盘、坏节点时候的运维操作非常简单，只需要下线坏盘或者坏的节点，无须进行手工 rebalance。
1. 支持对冷热数据进行分层存储，只需对目录级别进行冷热设置，自动完成对应数据的冷热分层存储。
1. 在弹性方面，HDFS 需要手动扩容，每次需要扩容都是增加一定量的空间，数据会逐渐存进来。如果扩的容量少，就要经常扩，工作量很大。如果扩的多，资源利用率就低，造成成本上升。JindoFS 支持在线平滑伸缩，存储和计算可以分别进行伸缩。

## Hbase 比较
* Hbase 是 HDFS 之上很重要的一个使用场景，拥有广泛的用户。Hbase 同样可以使用 JindoFS 进行存储，这种方式一方面可以利用上述 JindoFS 的诸多优势，另一方面可以获得跟 HDFS 不相上下的性能。可以参考文章[《HBase on JindoFS 存储模式性能测试》](./jindofs_block_vs_hdfs_hbase.md)。
