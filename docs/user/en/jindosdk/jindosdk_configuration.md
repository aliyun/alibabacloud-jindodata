## Common Client Configuration

### OSS/OSS-HDFS

#### Log Configuration Items

| Configuration Item                        | Type       | Default Value       | Description                                                                                                                                                     | Version                 |
|------------------------------------------| ---------- | ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------| ----------------------- |
| `logger.dir`                              | String     | `/tmp/bigboot-log`  | Log directory; if it doesn't exist, it will be created. Effective only when both `logger.consolelogger` and `logger.jnilogger` are set to `false`.               | 4.3.0+                 |
| `logger.sync`                             | Boolean    | `false`             | Whether to synchronize log output; `false` indicates asynchronous output.                                                                                        | 4.3.0+                 |
| `logger.consolelogger`                    | Boolean    | `false`             | Print logs to the terminal.                                                                                                                                     | 4.3.0+                 |
| `logger.jnilogger`                        | Boolean    | `false`             | Print logs to log4j.                                                                                                                                            | 4.3.0+                 |
| `logger.level`                            | Integer    | `2`                 | When using file logs: log level <= 1 means WARN; log level > 1 means INFO. For console logs, the log level ranges from 0-6, representing TRACE, DEBUG, INFO, WARN, ERROR, CRITICAL, OFF respectively. | 4.3.0+                 |
| `logger.verbose`                          | Integer    | `0`                 | Output VERBOSE logs greater than or equal to this level; range is 0-99, where 0 means no output.                                                                | 4.3.0+                 |
| `logger.cleaner.enable`                   | Boolean    | `false`             | Whether to enable log cleaning.                                                                                                                                  | 4.3.0+                 |

#### Setting log4j.properties For Java SDK
```
log4j.logger.com.aliyun.jindodata=INFO
log4j.logger.com.aliyun.jindodata.common.FsStats=INFO
```

#### I/O Related Configuration Items

| Configuration Item                        | Type       | Default Value       | Description                                                                                                                                                   | Version                 |
|------------------------------------------| ---------- | ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------- |
| `fs.oss.tmp.data.dirs`                    | String     | `/tmp/`             | Temporary file directory for client writes; can specify multiple directories (comma-separated), which will be used in rotation. Requires read/write permissions in multi-user environments. | 4.3.0+                 |
| `fs.oss.tmp.data.cleaner.enable`          | Boolean    | `true`              | Temporary file auto-cleaning service.                                                                                                                        | 4.3.0+                 |
| `fs.oss.retry.count`                      | Integer    | `5`                 | Number of retries for failed OSS/OSS-HDFS requests.                                                                                                           | 4.3.0+                 |
| `fs.oss.retry.interval.millisecond`       | Integer    | `500`               | Interval between retry attempts for failed OSS/OSS-HDFS requests (in milliseconds).                                                                           | 4.3.0+                 |
| `fs.oss.timeout.millisecond`              | Integer    | `30000`             | Timeout for OSS/OSS-HDFS requests (in milliseconds).                                                                                                          | 4.3.0+                 |
| `fs.oss.connection.timeout.millisecond`   | Integer    | `3000`              | Connection timeout for OSS/OSS-HDFS requests (in milliseconds).                                                                                                | 4.3.0+                 |
| `fs.oss.max.connections.per.host`         | Integer    | `100`               | Maximum number of connections per host in the connection pool for OSS/OSS-HDFS (connections exceeding this limit will use short-lived connections).            | 4.3.0+                 |
| `fs.oss.upload.thread.concurrency`        | Integer    | `MAX(cpu cores, 16)` | Number of concurrent upload threads within the process for OSS/OSS-HDFS.                                                                                    | 4.3.0+ (deprecated)    |
| `fs.oss.upload.queue.size`                | Integer    | `MAX(cpu cores, 16)` | Size of the concurrent upload task queue within the process for OSS/OSS-HDFS.                                                                               | 4.3.0+ (deprecated)    |
| `fs.oss.upload.max.pending.tasks.per.stream` | Integer    | `10`                | Maximum number of concurrent upload tasks per file for OSS/OSS-HDFS.                                                                                          | 4.3.0+ (deprecated)    |
| `fs.oss.download.thread.concurrency`      | Integer    | `MAX(cpu cores, 16)` | Maximum number of concurrent download tasks within the process for OSS/OSS-HDFS.                                                                              | 4.3.0+ (deprecated)    |
| `fs.oss.read.readahead.max.buffer.count`  | Integer    | `48`                | Maximum number of buffers to pre-read simultaneously from OSS/OSS-HDFS.                                                                                      | 4.3.0+ (deprecated)    |
| `fs.oss.read.buffer.size`                 | Integer    | `1048576`           | Read buffer size for OSS/OSS-HDFS (in bytes).                                                                                                                 | 4.3.0+                 |
| `fs.oss.write.buffer.size`                | Integer    | `1048576`           | Write buffer size for OSS/OSS-HDFS (in bytes).                                                                                                                | 4.3.0+                 |
| `fs.oss.flush.interval.millisecond`       | Integer    | `-1`                | Flush interval for OSS/OSS-HDFS buffers (in milliseconds); values less than 0 mean no effect.                                                                  | 4.3.0+                 |
| `fs.oss.blocklet.size.mb`                 | Integer    | `8`                 | Block size (in MB) for multipart uploads to OSS; since the maximum number of blocks is 10,000, the default file size cannot exceed 80 GB. If individual files exceed 80 GB, consider increasing this setting and also increasing the request timeout for OSS. For unknown or very large files (e.g., over 160 GB), consider using OSS-HDFS (no file size limit). | 4.5.2+                 |
| `fs.oss.checksum.crc64.enable`            | Boolean    | `true`              | File-level CRC64 integrity check; currently has a significant impact on write performance to OSS-HDFS, so it can be disabled in performance-critical scenarios. | 4.6.0+                 |
| `fs.oss.checksum.md5.enable`              | Boolean    | `false`             | Request-level MD5 integrity check.                                                                                                                            | 4.6.0+                 |
| `fs.oss.read.readahead.prefetcher.version`| String     | `default`           | Optional values: `legacy` for the original prefetch algorithm, `default` for the new prefetch algorithm. The new algorithm may use more memory; if performance drops after enabling the new algorithm, it might be due to insufficient memory pool capacity causing prefetched blocks to be evicted before they are accessed. To avoid this, consider reducing the maximum prefetch length or allowing more memory for prefetching. | 6.2.0+                 |
| `fs.oss.read.readahead.prefetch.size.max` | Integer    | `268435456`         | Maximum prefetch length (in bytes).                                                                                                                           | 6.2.0+                 |
| `fs.oss.signer.version`                   | Integer    | `0`                 | Signature algorithm version; optional values [0, 1, 4]. Recommended value is 0, indicating the default algorithm version. Starting from version 6.3.0, V4 signature algorithm is used by default to improve access to OSS/OSS-HDFS. For special scenarios requiring a specific signature algorithm version, set it to 1 or 4. | 6.3.0+                 |
| `fs.oss.io.timeout.millisecond`           | Integer    | `90000`             | Timeout for read/write operations to OSS/OSS-HDFS (in milliseconds).                                                                                           | 6.6.0+ nextarch        |
| `fs.oss.async.executor.number`            | Integer    | `cpu cores`          | Number of asynchronous I/O threads within the process for OSS/OSS-HDFS.                                                                                        | 6.6.0+ nextarch        |
| `fs.oss.upload.async.concurrency`         | Integer    | `MAX(cpu cores, 16)` | Number of concurrent asynchronous uploads within the process for OSS/OSS-HDFS.                                                                                 | 6.6.0+ nextarch        |
| `fs.oss.download.async.concurrency`       | Integer    | `MAX(cpu cores, 16)` | Number of concurrent asynchronous downloads within the process for OSS/OSS-HDFS.                                                                               | 6.6.0+ nextarch        |
| `fs.oss.array.block.enable`               | Boolean    | `false`              | Default is off. When enabled, client writes prefer memory buffers aligned with `fs.oss.blocklet.size.mb`. If memory is insufficient, data will be written to disk. Not recommended for writing large files (>8M), as it may cause out-of-memory issues. | 6.7.0+ nextarch        |
| fs.oss.append.threshold.size               |  Integer  |  0         | When the client performs an append write, it checks if the last block is smaller than `fs.oss.append.threshold.size`. If the last block is smaller, then the write appends to the end of this last block; otherwise, a new block is created for writing. If the previous block is a virtual block, a new block is also created.    | 6.7.6+ nextarch    |
| fs.oss.flush.merge.threshold.size               |  Integer  |  1048576         | When enabled, during a flush, virtual blocks will merge slices that are smaller than `fs.oss.flush.merge.threshold.size`. | 6.7.6+ nextarch    |

#### Memory Configuration Items

| Configuration Item                        | Type       | Default Value | Description                    | Version                 |
|------------------------------------------| ---------- | ------------- | ------------------------------| ----------------------- |
| `fs.oss.memory.buffer.size.max.mb`       | Integer    | `6124`       | Total memory pool capacity (in MB). | 4.3.0+ (deprecated)    |
| `fs.oss.memory.buffer.size.watermark`    | Float      | `0.3`        | Proportion of memory pool used for pre-reading.          | 4.3.0+ (deprecated)    |
| `fs.jdo.memory.pool.size.max.mb`         | Integer    | `6124`       | Total memory pool capacity (in MB). | 6.7.0+ nextarch        |
| `fs.jdo.memory.io.buffer.size.max.ratio` | Float      | `0.8`        | Maximum proportion of memory pool used for IO buffers.   | 6.7.0+ nextarch        |

#### Metrics Configuration Items

| Configuration Item                        | Type       | Default Value       | Description                                                                                      | Version              |
|------------------------------------------| ---------- | ------------------- | --------------------------------------------------------------------------------------------- | -------------------- |
| `fs.jdo.metrics.level`                   | Integer    | `1`                 | Metrics collection level: 0 for none, 1 for static metrics, 2 for dynamic metrics including bucket information, 3 for all metrics. | 6.6.0+ nextarch      |
| `fs.jdo.metrics.file.enable`             | Boolean    | `false`             | Controls whether to output metrics to a file.                                                    | 6.6.0+ nextarch      |
| `fs.jdo.metrics.file.dir`                | String     | `/tmp/metrics`      | Directory for outputting metrics to a file.                                                     | 6.6.0+ nextarch      |
| `fs.jdo.metrics.file.pid.append.enable`  | Boolean    | `false`             | Controls how metrics are output to a file; if true, uses PID as a subdirectory, resulting in a directory structure of `(file_dir)/pid/jindosdk_(timestamp).metrics`. | 6.7.0+ nextarch      |
| `fs.jdo.metrics.file.number`             | Integer    | `16`                | Controls the maximum number of files in a single directory.                                      | 6.6.0+ nextarch      |
| `fs.jdo.metrics.interval.sec`            | Integer    | `15`                | Interval for outputting metrics to a file (in seconds).                                         | 6.6.0+ nextarch      |
| `fs.jdo.metrics.file.clean.enable`       | Boolean    | `false`             | Controls whether to automatically clean up output metric files; if true, cleans up every 24 hours. | 6.6.0+ nextarch      |

For configuration related to Credential Providers, see [Configuring OSS/OSS-HDFS Credential Provider](./jindosdk_credential_provider.md).