# Flink 使用 JindoFS SDK

---

## 环境要求

在集群上有开源版本 Flink 软件，版本不低于 1.10.1。

## 为什么 Flink 需要使用 JindoFS SDK

若需要 Flink 保持 EXACTLY_ONCE 语义流式写入 Aliyun OSS 与 JindoFS，则需要使用 JindoFS SDK。

Apache Flink 是一种当前业界流行的开源大数据流式计算引擎，支持 “严格一次”（EXACTLY_ONCE）语义写入部分存储介质。Aliyun Object Storage Service (Aliyun OSS) 与 JindoFS 则属于阿里云旗下的核心产品，提供对象存储或文件系统等语义，凭借优异的性能和安全性已服务于海量用户。

目前，开源版本 Flink 对流式写入 Aliyun OSS 与 JindoFS 尚不能支持 EXACTLY_ONCE 语义，如有该需求则需要使用 JindoFS SDK。

## SDK 配置

需要在所有 Flink 节点进行配置。在每个节点 Flink 根目录下的 lib 文件夹，放置两个 .jar 文件：
* jindo-flink-sink-${version}.jar
* jindofs-sdk-${version}.jar

[下载页面](/docs/jindofs_sdk_download.md)

## 如何使用

如果已经按照上一节配置好 SDK，则无需额外配置，在流式作业中直接使用合适的路径即可。写入 OSS 以 oss:// 为前缀，写入 JindoFS 以 jfs:// 为前缀。具体使用方法及示例参考：

* [Flink 使用 JindoFS SDK 访问 OSS](/docs/flink/jindofs_sdk_on_flink_for_oss.md)

* [Flink 使用 JindoFS SDK 访问 JindoFS](/docs/flink/jindofs_sdk_on_flink_for_jfs.md)
