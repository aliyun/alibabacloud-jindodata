### 什么是 Jindo DistCp
随着阿里云 JindoFS SDK 的全面放开使用，基于 JindoFS SDK 的阿里云数据迁移利器 Jindo DistCp 现在也全面面向用户开放使用。Jindo DistCp 是阿里云 E-MapReduce 团队开发的大规模集群内部和集群之间分布式文件拷贝的工具。其使用 MapReduce 实现文件分发，错误处理和恢复，把文件和目录的列表作为map/reduce任务的输入，每个任务会完成源列表中部分文件的拷贝。目前全量支持 HDFS/OSS/S3 之间的的数据拷贝场景，提供多种个性化拷贝参数和多种拷贝策略。重点优化 HDFS 到 OSS 的数据拷贝，通过定制化 CopyCommitter，实现 No-Rename 拷贝，并保证数据拷贝落地的一致性。功能全量对齐 S3 DistCp 和 HDFS DistCp，性能较 HDFS DistCp 有较大提升，目标提供高效、稳定、安全的数据拷贝工具。
<div align=center>
<img src="../pic/distcp.png#pic_center" width = "370" height = "390" />
</div>

### Jindo DistCp 使用场景

* [数据从 HDFS 迁移到 OSS](jindo_distcp_hdfsToOss_pre.md)
* [数据从 OSS 迁移到 OSS](jindo_distcp_ossToOss_pre.md)
* [数据从 S3 迁移到 OSS](jindo_distcp_s3ToOss_pre.md)

### Jindo DistCp 性能测试
我们利用 Hadoop 自带的测试数据集 TestDFSIO 分别生成 1000个10M、1000个500M、1000个1G 大小的文件进行 distcp 性能测试，把数据从 HDFS 上拷贝到 OSS 上，测试发现 Jindo DistCp 相比 Hadoop DistCp 有近 1.6x 的性能提升，详细测试可参阅以下文档。
* [Jindo DistCp 和 Hadoop DistCp 性能对比结果](docs/jindo_distcp_vs_hadoop_distcp.md)

### Jindo DistCp 系列文章
* [《再出王牌：阿里云 Jindo DistCp 全面开放使用，成为阿里云数据迁移利器》](https://developer.aliyun.com/article/767803)

* [Jindo DistCp 进行数据迁移的使用说明](docs/jindo_distcp_how_to.md) (高效、稳定、安全的数据拷贝工具)

* [Jindo DistCp 场景化使用指南](docs/jindo_distcp_scenario_guidance.md)