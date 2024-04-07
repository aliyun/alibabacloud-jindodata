# OSS/OSS-HDFS 的性能优化最佳实践

越来越多的用户构建数据湖，并借助 OSS 来存储数据。OSS 可提供99.9999999999%（12个9）的数据持久性，99.995%的数据可用性。多种存储类型供选择，全面优化存储成本。

从 OSS 上传和下载数据时，您的应用程序可以轻松实现每秒数千个事务的请求性能。您的应用程序可以实现存储空间（Bucket）中每个分区（OSS 按对象名的字母序排列对象，并按照数据量和请求 QPS 来拆分分区）每秒至少 3,500 个 PUT/COPY/POST/DELETE 请求和 5,500 个 GET/HEAD 请求，而存储空间中的前缀数量是没有限制的，因此您可以通过并行读取来提高读取或写入性能。也就是说，如果您的请求的文件前缀设计合理，在某个 OSS Bucket 中创建了10个前缀来并行上传，则可以将数据写入性能扩展到每秒 35,000 个 PUT 请求。

阿里云 OSS-HDFS 服务（JindoFS 服务) 是 JindoFS 存储系统在阿里云上的服务化部署形态，和阿里云 OSS 深度融合，开箱即用，无须在 EMR 集群中部署维护 JindoFS，免运维。OSS-HDFS 服务具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm) 。

## 管理 HTTP 速度缓慢响应的最佳实践

访问 OSS/OSS-HDFS 数据时，您可以使用以下方法提高 HTTP 请求的响应速度：

*   将 OSS/OSS-HDFS 和 ECS 实例配置在同一地域
    
*   通过水平扩展和并行请求实现高吞吐量
    
*   对时延敏感的应用程序进行超时重试
    
*   对频繁访问的内容使用缓存
    
*   使用最新版本 JindoSDK
    

我们建议您根据自己的使用场景选择并应用最适合的选项，以优化 OSS/OSS-HDFS 上的数据处理。在以下各节中，我们将介绍每种方法的最佳实践。

### 将 OSS/OSS-HDFS 和 ECS 实例配置在同一地域

OSS存储空间名称是全局唯一的，您在创建存储空间时必须指定地域，且创建以后不能更改名称和地域。为了优化性能，我们建议您尽可能从同一阿里云地域中的 ECS 实例访问 OSS 存储空间，从而降低网络延迟和数据传输成本。详情请参见[ECS实例通过OSS内网地址访问OSS资源](https://help.aliyun.com/document_detail/39584.htm#concept-39584-zh)。

### 通过水平扩展和并发/并行请求实现高吞吐量

OSS 是一个超大的分布式系统。为了帮助您利用其规模，建议您将并行请求水平扩展到 OSS 服务终端节点，这种扩展方式有助于通过网络将负载分布在多个路径上。

OSS-HDFS 使用了上述最佳实践，通过元数据服务来支持将通过文件块的方式，将文件水平扩展到 多个 OSS 服务终端上，以获取最佳的读写性能。

对于高吞吐量传输，您可以根据您的 ECS 规格和应用程序，调整特定参数，在多个线程或多个实例中控制上传和下载数据的并发/并行度来优化吞吐量。

#### 测量性能指标

根据CPU、网络吞吐量的要求来评估不同的 ECS 实例规格。有关实例规格的更多信息，请参见[ECS实例规格](https://help.aliyun.com/document_detail/25374.htm#section-be7-kzc-o1f)。此外，建议您在测量性能时使用HTTP分析工具查看DNS查询时间、时延和数据传输速度

当您要调整并发的请求数时，性能测量非常重要。建议从单个请求开始，测量当前的网络带宽以及其他资源的使用情况，从而识别瓶颈资源（即使用率最高的资源），以及可能的并发请求数。例如，如果一次处理一个请求导致CPU使用率为10%，则表明最多可以支持10个并发请求。

#### 调整并发与并行的参数

您可以通过修改 `fs.oss.download.thread.concurrency` 和 `fs.oss.upload.thread.concurrency` 调整单个进程内上传和下载的并发数。

如果您在运行 MapReduce 任务时或者 Spark 任务，您还有以下选择：

*   对于提交 MapReduce 任务，可以通过 hadoop 参数 mapreduce.job.maps 和 参数mapreduce.job.reduces 控制并行的执行程序数量。
    
*   对于提交 Spark 执行程序，可以通过选项 --num-executors 或者 spark 参数 spark.executor.instance 控制并行的执行程序数量。
    

### 对时延敏感的应用程序进行超时重试

OSS 针对管控类API，如GetService（ListBuckets）、PutBucket、GetBucketLifecycle等进行QPS限制。如果应用程序产生高请求速率，可能会收到 HTTP 503 减速响应。如果发生这类错误，建议您延迟几秒后进行重试。

OSS会对账号级的总访问QPS做默认限制（账号级10,000/s），如果您需要更高的QPS，请联系[技术支持](https://selfservice.console.aliyun.com/ticket/createIndex)。注意，在整体访问QPS未超过上述阈值的情况下，如果请求集中在特定分区，服务端也可能会因为超过单分区的服务能力而限流并返回503；如果请求前缀合理打散（参见[OSS性能与扩展性最佳实践](https://help.aliyun.com/document_detail/64945.htm#concept-xtt-pln-vdb)），OSS会自动扩展分区数量来支持更高QPS的访问，您只需要等待并重试即可。

您可以通过调整 `fs.oss.retry.count` 控制访问 OSS/OSS-HDFS 的重试次数，`fs.oss.retry.interval.millisecond` 控制访问 OSS/OSS-HDFS 的重试间隔（间隔会随重试次数的增加而倍增）。也可以根据网络情况来调整 `fs.oss.timeout.millisecond` 超时时间，进行合理的超时重试。

### 对频繁访问的内容使用缓存加速

如果您的应用程序需要支持同一地区大量访问同一静态文件的场景，可以使用 JindoCache 缓存服务进行加速。JindoCache 缓存服务将文件以块的形式分散存储在分布式缓存服务中，从而避免重复从 OSS/OSS-HDFS 反复拉取数据，有效减少访问时延，增加计算资源的利用率。

### 使用最新版本 JindoSDK

最新版本的 JindoSDK 提供了优化的自适应配置和预读算法，并定期更新以遵循新的最佳实践。例如，处理不同网络错误的重试，自适应的并发控制等。

* [下载链接](../../jindosdk/jindosdk_download.md)