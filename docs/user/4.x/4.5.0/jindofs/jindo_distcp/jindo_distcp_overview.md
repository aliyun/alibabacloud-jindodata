# JindoDistCp 数据迁移

## 环境要求
* JDK 1.8及以上
* Hadoop 2.3+版本，请下载最新版的`jindo-distcp-tool-x.x.x.jar`，该 jar 包含在`jindosdk-${version}.tar.gz`内，解压缩后可在`tools/`目录下找到，[下载页面](/docs/user/4.x/jindodata_download.md) 。
该版本基于 native 代码实现，支持功能较丰富
(2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈)

## JindoDistCp 使用场景

* [数据从 HDFS 迁移到 OSS-HDFS 服务上](jindo_distcp_on_hdfs_to_dls.md)
* [数据在 OSS-HDFS 服务不同 bucket 之间迁移](jindo_distcp_on_dls_to_dls.md)

## JindoDistCp 问题排查指南
我们总结了一些在不同环境使用 JindoDistCp 用户可能会遇到的一些问题，您可以参照下文进行常见问题排查和解决
* [JindoDistCp 问题排查指南](jindo_distcp_QA.md)

## JindoDistCp 系列文章
* [《再出王牌：阿里云 JindoDistCp 全面开放使用，成为阿里云数据迁移利器》](https://developer.aliyun.com/article/767803)

* [JindoDistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md) (高效、稳定、安全的数据拷贝工具)
