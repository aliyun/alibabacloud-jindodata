# Common Configuration for OSS/OSS-HDFS Client

## General I/O Configuration Items

| Configuration Item | Type | Default Value | Description | Version |
| --- | --- | --- | --- | --- |
| fs.oss.tmp.data.dirs | String | /tmp/ | Temporary file directory for client writes. Multiple directories can be configured (separated by commas) and will be written to in turn. Read-write permissions need to be configured in multi-user environments. | 4.3.0+ |
| fs.oss.tmp.data.cleaner.enable | Boolean | true | Temporary file self-cleaning service | 4.3.0+ |
| fs.oss.retry.count | Integer | 5 | Number of retries when accessing OSS/OSS-HDFS fails | 4.3.0+ |
| fs.oss.retry.interval.millisecond | Integer | 500 | Retry interval (milliseconds) when accessing OSS/OSS-HDFS fails | 4.3.0+ |
| fs.oss.timeout.millisecond | Integer | 30000 | Request timeout (milliseconds) for OSS/OSS-HDFS | 4.3.0+ |
| fs.oss.connection.timeout.millisecond | Integer | 3000 | Connection timeout (milliseconds) for OSS/OSS-HDFS | 4.3.0+ |
| fs.oss.max.connections.per.host | Integer | 100 | Maximum number of connections per host for the connection pool connecting to OSS/OSS-HDFS (connections exceeding the threshold will use short connections) | 4.3.0+ |
| fs.oss.signer.version | Integer | 0 | Signature algorithm version, optional values [0, 1, 4]. It is recommended to use 0, which represents the default algorithm version. After version 6.3.0, the V4 signature algorithm is used by default to improve access to OSS/OSS-HDFS. If you want to specify the signature algorithm version in special scenarios, please specify 1 or 4. | 6.3.0+ |
| fs.oss.io.timeout.millisecond | Integer | 90000 | Timeout (milliseconds) for read/write operations on OSS/OSS-HDFS | 6.6.0+ nextarch |
| fs.oss.async.executor.number | Integer | Number of CPU cores | Number of asynchronous I/O threads for OSS/OSS-HDFS within the process | 6.6.0+ nextarch |
| fs.oss.list.fallback.iterative | Boolean | false | When enabled, `listStatus` automatically falls back to iterative implementation for large directory listings. | 6.8.3+ nextarch |

## Write Scenario Configuration Items

| Configuration Item | Type | Default Value | Description | Version |
| --- | --- | --- | --- | --- |
| fs.oss.upload.thread.concurrency | Integer | MAX(number of CPU cores, 16) | Number of concurrent upload threads for OSS/OSS-HDFS within the process | 4.3.0+ (deprecated) |
| fs.oss.upload.queue.size | Integer | MAX(number of CPU cores, 16) | Size of the concurrent upload task queue for OSS/OSS-HDFS within the process | 4.3.0+ (deprecated) |
| fs.oss.upload.max.pending.tasks.per.stream | Integer | 10 | Maximum number of concurrent upload tasks for a single file OSS/OSS-HDFS | 4.3.0+ (deprecated) |
| fs.oss.write.buffer.size | Integer | 1048576 | Write buffer size (bytes) for OSS/OSS-HDFS | 4.3.0+ |
| fs.oss.flush.interval.millisecond | Integer | -1 | Buffer flush interval (milliseconds) for OSS/OSS-HDFS, does not take effect when less than 0 | 4.3.0+ |
| fs.oss.blocklet.size.mb | Integer | 8 | Block size (MB) for OSS multipart upload. Since the maximum number of blocks is 10,000, the default maximum file size that can be written is 80GB. If individual files exceed 80G, it is recommended to increase this configuration based on file size and simultaneously increase the request timeout for OSS. If the file size is unknown or far exceeds 80G (such as exceeding 160G), it is recommended to consider using OSS-HDFS (no file size limit) | 4.5.2+ |
| fs.oss.checksum.crc64.enable | Boolean | true | File-level crc64 integrity check, currently has a significant impact on OSS-HDFS write performance. In performance-priority scenarios, consider disabling it. | 4.6.0+ |
| fs.oss.checksum.md5.enable | Boolean | false | Request-level md5 integrity check | 4.6.0+ |
| fs.oss.upload.async.concurrency | Integer | MAX(number of CPU cores, 16) | Asynchronous upload concurrency for OSS/OSS-HDFS within the process | 6.6.0+ nextarch |
| fs.oss.array.block.enable | Boolean | false | Disabled by default. When enabled, the client prioritizes using memory buffers for writing, with individual buffer sizes aligned with `fs.oss.blocklet.size.mb`. If memory is insufficient, it writes to disk. Not recommended for writing large files (>8M) as it may cause memory insufficiency. | 6.7.0+ nextarch |
| fs.oss.append.threshold.size | Integer | 0 | When the client appends writes to OSS-HDFS, it checks whether the last block is smaller than `fs.oss.append.threshold.size`. If smaller, it appends writes from the end of the last block; otherwise, it starts writing a new block. If the previous block is a virtual block, a new block is created. | 6.7.8+ nextarch |
| fs.oss.flush.merge.threshold.size | Integer | 1048576 | When enabled, virtual blocks in OSS-HDFS will merge slices smaller than `fs.oss.flush.merge.threshold.size` during flush operations. | 6.7.8+ nextarch |

## Read Scenario Configuration Items

### General Read Scenario Configuration Items

| Configuration Item | Type | Default Value | Description | Version |
| --- | --- | --- | --- | --- |
| fs.oss.download.thread.concurrency | Integer | MAX(number of CPU cores, 16) | Maximum number of concurrent download tasks for OSS/OSS-HDFS within the process | 4.3.0+ (deprecated) |
| fs.oss.download.async.concurrency | Integer | MAX(number of CPU cores, 16) | Asynchronous download concurrency for OSS/OSS-HDFS within the process | 6.6.0+ nextarch |

### Default Read Scenario Configuration Items

| Configuration Item | Type | Default Value | Description | Version |
| --- | --- | --- | --- | --- |
| fs.oss.read.readahead.prefetcher.version | String | default | Optional values: `legacy` original prefetch algorithm, `default` new prefetch algorithm. The new prefetch algorithm may use more memory. If performance degradation occurs after configuring the new prefetch algorithm, it may be due to insufficient memory pool capacity causing prefetched blocks to be evicted before being accessed. To avoid this situation, consider reducing the maximum prefetch length or allowing prefetch to use more memory. | 6.2.0+ |
| fs.oss.read.readahead.max.buffer.count | Integer | 48 | Maximum number of buffers for simultaneous prefetching of OSS/OSS-HDFS (legacy prefetch algorithm) | 4.3.0+ (deprecated) |
| fs.oss.read.buffer.size | Integer | 1048576 | Read buffer size (bytes) for OSS/OSS-HDFS (legacy prefetch algorithm) | 4.3.0+ (deprecated) |
| fs.oss.read.readahead.pread.enable | Boolean | false | Controls whether to enable prefetch for random read interface | 6.2.0+ |
| fs.oss.read.readahead.prefetch.size.max | Integer | 268435456 | Maximum prefetch length (unit: byte) | 6.2.0+ |
| fs.oss.read.readahead.download.block.size.min | Integer | 1048576 | Minimum length of a single prefetch request (unit: byte) | 6.2.0+ |
| fs.oss.read.readahead.download.block.size.max | Integer | 4194304 | Maximum length of a single prefetch request (unit: byte) | 6.2.0+ |

### Lake Table Read Scenario Configuration Items

There are three ways to identify lake table files:

1. FileSystem created by paimon/iceberg/hudi.

2. When opening a file, the [openfile method (hadoop3.3.1+)](https://hadoop.apache.org/docs/r3.4.1/hadoop-project-dist/hadoop-common/filesystem/fsdatainputstreambuilder.html#Option:_fs.option.openfile.read.policy) is used, and the file type is specified as columnar/parquet/orc.

3. The file ends with `.parquet` or `.orc`.

| Configuration Item | Type | Default Value | Description | Version |
| --- | --- | --- | --- | --- |
| fs.oss.read.profile.enable | Boolean | true | When reading lake table format files, OSS/OSS-HDFS enables the optimized prefetch algorithm for lake table files by default. | 6.9.0+ nextarch |
| fs.oss.read.profile.columnar.readahead.pread.enable | Boolean | true | Controls whether to enable prefetch for lake table file random read interface | 6.9.0+ nextarch |
| fs.oss.read.profile.columnar.readahead.prefetch.size.max | Integer | 16777216 | Maximum prefetch length for lake table format files (unit: byte) | 6.9.0+ nextarch |
| fs.oss.read.profile.columnar.readahead.download.block.size.min | Integer | 1048576 | Minimum length of a single prefetch request (unit: byte) | 6.9.0+ nextarch |
| fs.oss.read.profile.columnar.readahead.download.block.size.max | Integer | 1048576 | Maximum length of a single prefetch request (unit: byte) | 6.9.0+ nextarch |
| fs.oss.vectored.read.min.seek.size | Integer | 16384 | The minimum reasonable seek range (bytes) for merging multiple FileRanges during readVectored operations in OSS/OSS-HDFS. | 6.9.0+ nextarch |
| fs.oss.vectored.read.max.merged.size | Integer | 2097152 | The maximum length (bytes) for merging multiple FileRanges during readVectored operations in OSS/OSS-HDFS. When 0, no merging occurs. | 6.9.0+ nextarch |

## Memory Configuration Items

| Configuration Item | Type | Default Value | Description | Version |
| --- | --- | --- | --- | --- |
| fs.oss.memory.buffer.size.max.mb | Integer | 6124 | Total memory pool capacity (unit: MB) | 4.3.0+ (deprecated) |
| fs.oss.memory.buffer.size.watermark | Float | 0.3 | Memory pool capacity ratio used for prefetching | 4.3.0+ (deprecated) |
| fs.oss.memory.pool.size.max.mb | Integer | 6124 | Total memory pool capacity (unit: MB) | 6.7.0+ nextarch |
| fs.oss.memory.io.buffer.size.max.ratio | Float | 0.8 | Maximum capacity ratio of memory pool used for IO buffer | 6.7.0+ nextarch |