### 环境要求
* JDK 1.8及以上
* Hadoop 2.3+版本，请下载 [jindo-distcp-3.4.0.jar](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/Jar/native/jindo-distcp-3.4.0.jar), 该版本基于 native 代码实现，支持功能较丰富
(2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈)

* 如果您的 Linux 版本较低，出现 glibc 或其他不兼容问题，请下载 [jindo-distcp-3.4.0-lite.jar](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/Jar/lite/jindo-distcp-3.4.0-lite.jar), 该版本基于 Java 代码实现，支持基本功能

### 什么是 Jindo DistCp
随着阿里云 JindoFS SDK 的全面放开使用，基于 JindoFS SDK 的阿里云数据迁移利器 Jindo DistCp 现在也全面面向用户开放使用。Jindo DistCp 是阿里云 E-MapReduce 团队开发的大规模集群内部和集群之间分布式文件拷贝的工具。其使用 MapReduce 实现文件分发，错误处理和恢复，把文件和目录的列表作为 map/reduce 任务的输入，每个任务会完成源列表中部分文件的拷贝。目前全量支持 HDFS/OSS/S3 之间的的数据拷贝场景，提供多种个性化拷贝参数和多种拷贝策略。重点优化 HDFS 到 OSS 的数据拷贝，通过定制化 CopyCommitter，实现 No-Rename 拷贝，并保证数据拷贝落地的一致性。功能全量对齐 S3 DistCp 和 HDFS DistCp，性能较 HDFS DistCp 有较大提升，目标提供高效、稳定、安全的数据拷贝工具。
<div align=center>
<img src="../../pic/distcp.png#pic_center" width = "370" height = "390" />
</div>

### Jindo DistCp 使用场景

* [数据从 HDFS 迁移到 OSS 上](jindo_distcp_hdfsToOss_pre.md)
* [数据从 HDFS 迁移到 JindoFS Block 模式上](jindo_distcp_hdfsToJfsblock_pre.md)
* [数据在 OSS 不同 bucket 之间迁移](jindo_distcp_ossToOss_pre.md)
* [数据从 S3 迁移到 OSS 上](jindo_distcp_s3ToOss_pre.md)
* [数据从 HDFS 迁移到 HDFS 上](jindo_distcp_hdfsTohdfs_pre.md)

### Jindo DistCp 问题排查指南
我们总结了一些在不同环境使用 Jindo DistCp 用户可能会遇到的一些问题，您可以参照下文进行常见问题排查和解决
* [Jindo DistCp 问题排查指南](jindo_distcp_QA_pre.md)

### Jindo DistCp 性能测试
我们利用 Hadoop 自带的测试数据集 TestDFSIO 分别生成 1000个10M、1000个500M、1000个1G 大小的文件进行 DistCp 性能测试，把数据从 HDFS 上拷贝到 OSS 上，测试发现 Jindo DistCp 相比 Hadoop DistCp 有近 1.6x 的性能提升，详细测试可参阅以下文档。
* [Jindo DistCp 和 Hadoop DistCp 性能对比结果](jindo_distcp_vs_hadoop_distcp.md)

### Jindo DistCp 系列文章
* [《再出王牌：阿里云 Jindo DistCp 全面开放使用，成为阿里云数据迁移利器》](https://developer.aliyun.com/article/767803)

* [Jindo DistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md) (高效、稳定、安全的数据拷贝工具)

* [Jindo DistCp 场景化使用指南](jindo_distcp_scenario_guidance.md)