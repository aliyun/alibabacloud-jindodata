# Hadoop 使用 Jindo SDK 访问 JindoFS
[English Version](./jindofs_sdk_how_to_en.md)


# 访问 JindoFS（作为 JindoFS 客户端）

JindoFS SDK 是一个简单易用面向 Hadoop/Spark 生态的 JindoFS / OSS客户端，为阿里云 JindoFS / OSS 提供高度优化的 Hadoop FileSystem 实现。

使用同一 JindoFS SDK 不仅可以作为 OSS 客户端，获得更好的性能和技术团队更专业的支持，同时也可以在 JindoFS 集群外部，访问 JindoFS。

 JindoFS SDK 目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈）<br />



# 访问 JindoFS Cache/Block模式集群

当您已经创建了一个E-MapReduce JindoFS集群，并且希望在另外一个集群或ECS机器上访问JindoFS集群时，可以使用该方法。

* 您使用的是  [JindoFS 3.3 及以上版本](jindofs_sdk_how_to_jfs_3_3.md) 

* 您使用的是  [JindoFS 3.2 及以下版本](jindofs_sdk_how_to_jfs_3_2.md) 

