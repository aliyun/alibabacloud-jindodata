# Best Practices for Optimizing Performance with OSS/OSS-HDFS

As more users build data lakes, they increasingly rely on Alibaba Cloud's Object Storage Service (OSS) for its high data durability and availability. With a choice of storage classes, it helps optimize storage costs.

During uploads and downloads to/from OSS, applications can achieve thousands of transactions per second. Each partition in an OSS bucket can handle at least 3,500 PUT/COPY/POST/DELETE requests and 5,500 GET/HEAD requests per second, with unlimited prefix numbers. Thus, by reading or writing with multiple prefixes, you can scale read/write performance. For instance, if you create 10 prefixes in an OSS bucket for parallel uploading, you could boost write throughput up to 35,000 PUT requests per second.

Alibaba Cloud's OSS-HDFS service (also known as JindoFS service) is a cloud-deployed form of the JindoFS storage system, seamlessly integrated with OSS and requiring no maintenance on EMR clusters. Learn more about [OSS-HDFS Overview](https://help.aliyun.com/document_detail/405089.htm).

## Best Practices for Managing Slow HTTP Responses

When accessing OSS/OSS-HDFS data, follow these guidelines to improve HTTP request response times:

1. **Deploy ECS instances in the same region as OSS/OSS-HDFS**: Minimize network latency and reduce data transfer costs.
2. **Horizontal scaling and concurrent requests**: Achieve high throughput through horizontal expansion.
3. **Timeout retries for latency-sensitive apps**: Implement retry logic for timeouts.
4. **Use caching for frequently accessed content**: Speed up access.
5. **Use the latest version of JindoSDK**: Benefit from optimized configurations and algorithms.

Choose and apply the most suitable practices based on your specific use case. The following sections detail each practice.

### Deploying ECS Instances in the Same Region

Create your OSS buckets within the same region as your ECS instances to minimize latency and reduce costs. Refer to [ECS Accessing OSS Resources Through Intranet Address](https://help.aliyun.com/document_detail/39584.htm#concept-39584-zh).

### Horizontal Scaling and Concurrent Requests

Since OSS is a massive distributed system, distribute load across multiple paths by horizontally scaling out requests to its endpoints. OSS-HDFS leverages this principle by distributing files across multiple partitions using metadata services for optimal read-write performance.

For high throughput transfers, adjust concurrency settings in threads or instances according to your ECS specifications and application requirements.

#### Measuring Performance Metrics

Evaluate different ECS instance types based on CPU and network throughput needs. See [ECS Instance Types](https://help.aliyun.com/document_detail/25374.htm#section-be7-kzc-o1f). Use HTTP profiling tools during performance testing to monitor DNS lookup time, latency, and transfer speed.

When adjusting concurrent request counts, measure performance first to identify bottlenecks and potential concurrency levels. For example, if processing one request uses 10% CPU capacity, you might support up to 10 concurrent requests.

#### Adjusting Concurrency Parameters

Modify `fs.oss.download.thread.concurrency` and `fs.oss.upload.thread.concurrency` to control upload/download concurrency within a single process.

For MapReduce jobs, use `mapreduce.job.maps` and `mapreduce.job.reduces`. For Spark jobs, set `--num-executors` or Spark parameter `spark.executor.instances`.

### Timeout Retries for Latency-Sensitive Apps

OSS throttles control API requests like GetService (ListBuckets), PutBucket, GetBucketLifecycle, etc., returning HTTP 503 errors if rates exceed limits. Retry after brief pauses if such errors occur.

Contact [Technical Support](https://selfservice.console.aliyun.com/ticket/createIndex) to increase account-level QPS limits beyond the default of 10,000/s. Even if overall QPS doesn't exceed this threshold, excessive requests on specific partitions may trigger throttling. Distribute requests across multiple prefixes to allow automatic partition expansion and avoid 503 errors.

Adjust `fs.oss.retry.count` for retry attempts and `fs.oss.retry.interval.millisecond` for increasing retry intervals. Modify `fs.oss.timeout.millisecond` for timeout durations to implement appropriate retry logic.

### Caching Frequently Accessed Content

Accelerate access to static files with JindoCache, which stores files' blocks distributively in cache services, reducing repetitive pulls from OSS/OSS-HDFS and improving latency.

### Using the Latest Version of JindoSDK

Stay updated with the newest JindoSDK releases for improved adaptive configuration, prefetching algorithms, and error-handling retries.

Refer to the [Download Link](../../jindosdk/jindosdk_download.md) for the latest version.