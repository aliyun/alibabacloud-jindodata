# Common Client Configurations

## OSS/OSS-HDFS

| Configuration | Type | Default Value | Description | Since Version |
| --- | --- | --- | --- | --- |
| fs.oss.tmp.data.dirs | String | /tmp/ | Temporary file directory used during client writes, can specify multiple (comma-separated); requires read-write permission in a multi-user environment | 4.3.0+ |
| fs.oss.tmp.data.cleaner.enable | Boolean | true | Enables automatic cleanup of temporary files | 4.3.0+ |
| fs.oss.retry.count | Integer | 5 | Retry attempts when accessing OSS/OSS-HDFS fails | 4.3.0+ |
| fs.oss.retry.interval.millisecond | Integer | 500 | Retry interval in milliseconds when accessing OSS/OSS-HDFS fails | 4.3.0+ |
| fs.oss.timeout.millisecond | Integer | 30000 | Request timeout for OSS/OSS-HDFS in milliseconds | 4.3.0+ |
| fs.oss.connection.timeout.millisecond | Integer | 3000 | Connection timeout for OSS/OSS-HDFS in milliseconds | 4.3.0+ |
| fs.oss.max.connections.per.host | Integer | 100 | Maximum connections per host in the connection pool for OSS (short-lived connections beyond the limit) | 4.3.0+ |
| fs.oss.upload.thread.concurrency | Integer | MAX(cpu cores, 16) | Concurrent upload threads per single file to OSS/OSS-HDFS | 4.3.0+ |
| fs.oss.upload.queue.size | Integer | MAX(cpu cores, 16) | Size of the concurrent upload task queue for OSS/OSS-HDFS | 4.3.0+ |
| fs.oss.upload.max.pending.tasks.per.stream | Integer | 10 | Maximum concurrent upload tasks per process for OSS | 4.3.0+ |
| fs.oss.download.thread.concurrency | Integer | MAX(cpu cores, 16) | Maximum concurrent download tasks per process for OSS | 4.3.0+ |
| fs.oss.read.readahead.max.buffer.count | Integer | 48 | Maximum number of buffers to prefetch while reading from OSS | 4.5.1+ |
| fs.oss.read.buffer.size | Integer | 1048576 | Read buffer size for OSS in bytes | 4.3.0+ |
| fs.oss.write.buffer.size | Integer | 1048576 | Write buffer size for OSS in bytes | 4.3.0+ |
| fs.oss.flush.interval.millisecond | Integer | -1 | Buffer flush interval in milliseconds (-1 to disable) | 4.3.0+ |
| fs.oss.checksum.crc64.enable | Boolean | true | Enables file-level CRC64 integrity checks; may significantly impact write performance on OSS-HDFS | 4.6.0+ |
| fs.oss.checksum.md5.enable | Boolean | false | Enables request-level MD5 integrity checks | 4.6.0+ |
| fs.oss.blocklet.size.mb | Integer | 8 | Block size for chunked uploads to OSS in MB (max 10,000 blocks); default limits files to ~80GB | 4.5.2+ |

For credential provider-related configurations, please refer to [Configuring OSS/OSS-HDFS Credential Providers](./jindosdk_credential_provider.md).