# JindoDistCp 数据迁移

## 环境要求
* JDK 1.8及以上
* Hadoop 2.3+版本，请下载最新版的`jindo-distcp-x.x.x.jar`，该 jar 包含在`jindosdk-${version}.tar.gz`内，解压缩后可在`tools/`目录下找到，[下载页面](/docs/user/4.x/jindodata_download.md) 。
该版本基于 native 代码实现，支持功能较丰富
(2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈)

## 什么是 JindoDistCp
随着阿里云 JindoSDK 的全面放开使用，基于 JindoSDK 的阿里云数据迁移利器 JindoDistCp 现在也全面面向用户开放使用。JindoDistCp 是阿里云 E-MapReduce 团队开发的大规模集群内部和集群之间分布式文件拷贝的工具。其使用 MapReduce 实现文件分发，错误处理和恢复，把文件和目录的列表作为 map/reduce 任务的输入，每个任务会完成源列表中部分文件的拷贝。目前全量支持 HDFS/OSS/S3 之间的的数据拷贝场景，提供多种个性化拷贝参数和多种拷贝策略。重点优化 HDFS 到 OSS 的数据拷贝，通过定制化 CopyCommitter，实现 No-Rename 拷贝，并保证数据拷贝落地的一致性。功能全量对齐 S3 DistCp 和 HDFS DistCp，性能较 HDFS DistCp 有较大提升，目标提供高效、稳定、安全的数据拷贝工具。

<div align=center>
<img src="../pic/jindo_distcp.png" width = "370" height = "390" />
</div>

## JindoDistCp 使用场景

* [数据从 HDFS 迁移到 OSS 上](jindo_distcp_on_hdfs_to_oss.md)
* [数据在 OSS 不同 bucket 之间迁移](jindo_distcp_on_oss_to_oss.md)

## JindoDistCp 问题排查指南
我们总结了一些在不同环境使用 JindoDistCp 用户可能会遇到的一些问题，您可以参照下文进行常见问题排查和解决
* [JindoDistCp 问题排查指南](jindo_distcp_QA.md)

## JindoDistCp 系列文章
* [《再出王牌：阿里云 JindoDistCp 全面开放使用，成为阿里云数据迁移利器》](https://developer.aliyun.com/article/767803)

* [JindoDistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md) (高效、稳定、安全的数据拷贝工具)
