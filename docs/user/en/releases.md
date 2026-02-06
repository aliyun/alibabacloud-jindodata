# JindoSDK Release History

## 6.10.4, 2026-02-06

### Release Summary

Release of JindoSDK 6.10.4 official version features

### Introduction

- JindoSDK updates [Maven repository for 6.10.4](jindosdk/oss-maven.md) and [download links](jindosdk/jindosdk_download.md).
- Added CSDK async interfaces to support Lance format integration requirements.
- Support for DLF Cache integration.
- Fixed OSS-HDFS bucket-level endpoint configuration support.
- Fixed memory leak in JindoFuse readdir interface calls.
- Fixed memory leaks in GoSDK GetFileInfo, ListDir, and IO interface calls.

## 6.10.3, 2025-12-09

### Version Summary

Release of official JindoSDK 6.10.3 features.

### Introduction

- Updated JindoSDK [Maven repository for version 6.10.3](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.3/oss-maven.md) and [download links](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.3/jindodata_download.md).
- JindoFuse supports read-only mount with `-oro_mount`.
- Optimized the implementation of Hadoop FileSystem close().
- Improved PyJindo compatibility with fsspec interface.
- Fixed PyJindo issue where opening a non-existent file would cause a crash.
- Fixed JindoDistributedFileSystem support for `void rename(final Path src, final Path dst, final Options.Rename... options)`.
- Added logging for iterative list related operations.

## 6.10.2, 2025-10-09

### Version Summary

- Updated JindoSDK [Maven repository for version 6.10.2](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.2/oss-maven.md) and [download links](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.2/jindodata_download.md).
- Jindo CLI, JindoFs CLI, and Jindo Lite artifacts now support the Windows platform.
- Improved task planning efficiency in JindoDistCp.
- Exposed the `readBytes` interface in JindoInputStream.
- Fixed an issue where the `getServerDefaults` function would hang when Ranger is enabled for accessing OSS-HDFS.

## 6.10.1, 2025-09-22

### Version Summary

- JindoSDK updates [Maven repository for 6.10.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.1/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.1/jindodata_download.md).
- Upgraded yalantinglibs to [lts1.2.1](https://github.com/alibaba/yalantinglibs/tree/lts1.2.1). Fixed occasional crash issue when concurrently calling summary during initialization.
- JindoSDK supports qos latency metrics.
- JindoFS CLI fixes support for `-count -q` parameter.

## 6.10.0, 2025-08-04

### Version Summary

- JindoSDK updates [Maven repository for 6.10.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.0/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.10.0/jindodata_download.md).
- Supports OSS-HDFS metadata access batch interfaces: renameBatch, removeBatch (deleteBatch), getFileInfoBatch (getFileStatusBatch), getContentSummaryBatch, listDirectoryBatch, significantly improving metadata access performance in small file scenarios.
- Supports listFiles(Path f, boolean recursive).
- JindoDistributedFileSystem supports getServerDefaults(Path f).
- JindoSDK optimizes default prefetch length for lake table scenarios, changing default value from 16M to 64M.
- JindoCLI and Tensorflow Collector are implemented using nextarch.
- JindoSDK optimizes pread logging, changing default level to debug.
- Optimizes default concurrency in container scenarios, which can be specified via environment variable `JINDO_DEFAULT_PROCS`.
- Fixes authentication requirement for getServerDefaults function when accessing OSS-HDFS.
- Fixes umask support when creating directories/files when accessing OSS-HDFS.
- Fixes libjindosdk_c.so support for scenarios with one-time reads of buffers larger than 2G.

## 6.9.1, 2025-06-04

- JindoSDK updated [Maven repository for version 6.9.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.9.1/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.9.1/jindodata_download.md).
- JindoSDK fixed lake table format prefetching strategy, resolving performance regression issues in some scenarios when `fs.oss.read.profile.enable=true`.
- JindoFuse fixed OSS scenario issue with multiple Append/Flush operations not supporting files larger than 2G.
- JindoSDK optimized JindoInputStream logs to reduce the volume of logs in readVectored scenarios by allowing specification of `log4j.logger.com.aliyun.jindodata.common.JindoInputStream` log level.

## 6.9.0, 2025-05-21

- Updated JindoSDK [Maven repository for version 6.9.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.9.0/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.9.0/jindodata_download.md).
- Support for the [openFile()](https://issues.apache.org/jira/browse/HADOOP-15229) interface, allowing specification of read policy (requires Hadoop 3.3.0+).
- Support for [Vectored IO](https://issues.apache.org/jira/browse/HADOOP-18103) interface (requires Hadoop 3.3.6+).
- Optimized lake table format prefetching strategy, achieving a 30% improvement in heavy IO read scenarios (can be disabled by configuring `fs.oss.read.profile.enable=false` to revert to old version behavior).
- Optimization of OSS-HDFS append merging strategy, supporting ComposedBlock.
- Optimization of OSS-HDFS InputStream locking.
- Enhancement of the OSS thawing interface, enabling the setting of OSS thaw priority.
- Resolution of occasional deadlocks during high-concurrency writes to OSS/OSS-HDFS close operations.
- Fixed several issues in JindoDistcp and added support for `--syncSourceDelete`. For details, see [JindoDistCp User Guide](jindotools/jindodistcp_quickstart.md).
- Fixes for several issues in JindoCache.
- Implementation of JindoFS CLI using nextarch.

## 6.8.5, 2025-04-29

- Updated JindoSDK [Maven repository for version 6.8.5](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.5/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.5/jindodata_download.md).
- Fixed issue where using Ranger with root policy to access OSS-HDFS.
- Supported authentication methods for accessing OSS-HDFS.
- Fixed potential deadlock caused by high concurrency initialization of clients in JindoCache.
- Added concurrent control for getattr in JindoFuse.
- Fixed incorrect judgment of illegal filenames in JindoFuse.
- Fixed rename_while_write issue in JindoFuse.

## 6.8.3, 2025-04-15
- Updated JindoSDK [Maven repository for version 6.8.3](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.3/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.3/jindodata_download.md).
- Added configuration `fs.oss.list.fallback.iterative`, set to `true` to automatically fall back to iterative access for large directories in listStatus.
- Added Golang SDK documentation, supporting iterative list interface.
- Supported authorization plugins for accessing OSS in JindoSDK.
- Optimized binary size of JindoFS CLI.

## 6.8.2, 2025-03-24

- Updated JindoSDK [Maven repository for version 6.8.2](jindosdk/oss-maven.md) and [download link](jindosdk/jindosdk_download.md).
- JindoSDK fixes the issue of inaccurate error messages, correctly returning `Request timeout` instead of `Connection timed out`.
- JindoSDK fixes the problem that may occur when the append-close optimization is enabled, where abnormal closure could lead to errors. Specifically, if `fs.oss.append.threshold.size` is not `0`, it might fail to append after closing (default configuration is unaffected).
- JindoSDK fixes the issue where listStatusIterator does not support ListObjectV2. Configuring `fs.oss.list.type` to `2` could cause a listStatusIterator infinite loop (default configuration is unaffected).
- JindoSDK fixes an occasional hang-on problem when accessing JindoCache.
- JindoSDK optimizes the compatibility of IO request timeout configurations, defaulting to the maximum value between `fs.oss.io.timeout.millisecond` and `fs.oss.timeout.millisecond`.
- JindoSDK adds configurations `fs.oss.read.use-pread` and `fs.oss.pread.cache.enable` to optimize read performance in data lake table scenarios.

## 6.8.1, 2025-03-06

- Updated JindoSDK [Maven repository for version 6.8.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.1/oss-maven.md) and [download link](ttps://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.1/jindodata_download.md).
- Upgraded yalantinglibs to [lts1.0.2](https://github.com/alibaba/yalantinglibs/tree/lts1.0.2).
- The nextarch classifier will be published as the default version number in the OSS Maven repository.
- Fixed support for summary metrics in JindoSDK.
- Fixed support for `fs.oss.upload.async.concurrency` and `fs.oss.upload.thread.concurrency` in JindoSDK.
- Fixed misaligned pre-read blocks during caching and failed caching issues in JindoCache.
- Fixed client-side metric collection issues related to reads in JindoCache.
- Supported OSS-HDFS UGI information in JindoCache.
- Supported client timeout fallback in JindoCache.
- Optimized metadata cache usage in JindoFuse, expected to reduce memory consumption by 50% in large volumes of small files scenarios.

## 6.8.0, 2025-01-24

- Updated JindoSDK [Maven repository for version 6.8.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.0/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.0/jindodata_download.md).
- Optimized nextarch classifier to reduce OSS-HDFS request numbers, decreasing metadata requests by 17% in read scenarios.
- Improved the frequency at which Java TimedBuffer retrieves timestamps to lower CPU usage.
- Optimized the size of JindoMagicCommitter PendingSet.
- Enhanced pre-fetching algorithm with new configurations to control whether fully-read prefetch memory should be immediately cleared.
- Added several metrics that can be accessed through Java interfaces.
- Introduced logging-related configurations to support persistent log instances.
- Fixed support for fs.jdo prefix configuration in oss-hdfs.
- When nextarch classifier sets XAttr on OSS, it defaults x-oss-metadata-directive to REPLACE.
- Resolved compatibility issues when accessing jindocache server.
- Optimized JindoFuse to reduce metadata request numbers in OSS access scenarios, supporting metadata requests at intervals defined by attr_timeout.
- Fixed time window issues in jindo-fuse metrics.

## 6.7.8, 2024-12-30

Released features of the official version JindoSDK 6.7.8.

- JindoSDK has updated the [Maven repository for 6.7.8](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.8/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.8/jindodata_download.md).
- Upgraded yalantinglibs to [0.3.8.1](https://github.com/alibaba/yalantinglibs/tree/0.3.8.1).
- The nextarch classifier optimized high CPU load issues with Timed Buffer.
- The nextarch classifier has optimized the deletion strategy when accessing OSS, introducing the new configuration `fs.oss.delete.quiet.enable`, which defaults to false and uses simple mode deletion. For details on simple mode, see the OSS documentation [《Deleting Multiple Files Using DeleteMultipleObjects》](https://help.aliyun.com/zh/oss/developer-reference/deletemultipleobjects).
- The nextarch classifier fixed an issue where the legacy pre-read algorithm could not be set.
- The nextarch classifier resolved a potential stack overflow issue when using the S3 protocol with `fs.s3.upload.sendfile.enable=true` enabled.

## 6.7.7, 2024-12-20

Released features of the official version JindoSDK 6.7.7.

- JindoSDK has updated the [Maven repository for 6.7.7](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.7/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.7/jindodata_download.md).
- The nextarch classifier fixed a V4 signature issue.

## 6.5.6, 2024-12-12

Released features of the official version JindoSDK 6.5.6.

- JindoSDK has updated the [Maven repository for 6.5.6](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.6/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.6/jindodata_download.md).
- The nextarch classifier fixed an issue where JindoCommitter did not clean up temporary directories left by incomplete tasks when accessing OSS-HDFS.
- The nextarch classifier resolved permission issues related to using the DLF-related CredentialProvider with MagicCommitter.
- The nextarch classifier addressed several issues with JindoCache.
- By default, jindo-fuse now uses the nextarch implementation and supports accessing DLF Volume.

## 6.5.5, 2024-12-06

Released features of the official version JindoSDK 6.5.5.

- JindoSDK has updated the [Maven repository for 6.5.5](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.5/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.5/jindodata_download.md).
- The nextarch classifier has added configurations `fs.oss.append.threshold.size` and `fs.oss.flush.merge.threshold.size` to optimize issues with frequent Close-To-Append or Flush generating numerous small blocks; see [Client Common Configurations](jindosdk/jindosdk_configuration.md) for details.
- Fixed an issue where continuous writing to an OSS-HDFS file for over one hour after enabling Ranger results in the file being unable to continue writing.
- Resolved additional dependency issues introduced by `AlreadyBeingCreatedException`.
- Improved handling of `AlreadyBeingCreatedException` when reading/writing logs with Hudi.
- Fixed an issue where flushing in an OSS-HDFS write scenario resulted in small block problems.
- Added support for opening the root policy configuration for JindoCommitter.
- Fixed an issue where JindoCommitter could not recognize paths containing special characters.
- Addressed occasional freezing during STS Token renewal in ECS passwordless scenarios.
- Fixed an issue where JindoCommitter did not clean up temporary directories left by uncompleted tasks when accessing OSS-HDFS.
- Corrected the unit of `Mtime` returned by `ListDirectory` in OSS object storage.
- Optimized classloader loading to resolve leftover `.so` issues when multiple classloaders load JindoSDK, and defaults to extracting to the configured directory when `java.io.tmpdir` is set.

## 6.7.6, 2024-11-27

Released features of the official version JindoSDK 6.7.6.

- JindoSDK has updated the [Maven repository for 6.7.6](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.6/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.6/jindodata_download.md).
- The nextarch classifier has added configurations `fs.oss.append.threshold.size` and `fs.oss.flush.merge.threshold.size` to optimize the issue of frequent Close-To-Append or Flush generating a large number of small blocks. Details of these configurations can be found in [Common Client Configuration](jindosdk/jindosdk_configuration.md).
- jindo-fuse now supports accessing DLF Volume.

## 6.7.5, 2024-11-15

Released features of the official version JindoSDK 6.7.5.

- JindoSDK has updated the [Maven repository for 6.7.5](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.5/oss-maven.md) and [download link](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.5/jindodata_download.md).
- The nextarch classifier fixed an issue where the STS token occasionally fails to update during ECS passwordless scenarios.
- The nextarch classifier now supports enabling the root policy configuration for JindoCommitter.
- The nextarch classifier optimizes the process of appending metrics to files by automatically creating directories if they do not exist.
- Additional metrics indicators have been added to the nextarch classifier.

## 6.7.4，2024-11-08

Release of official version features for JindoSDK 6.7.4.

- Updated the [Maven Repository for version 6.7.4](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.4/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.4/jindodata_download.md) for JindoSDK.
- Fixed the issue with the dependency order of pthread hooks encountered while fixing the libjindosdk_c.so dynamic library.
- Added new configuration `oss.serializer.read.auto.compatible=true` to FlinkConnector to address compatibility issues when recovering checkpoints written in 3.x versions to 4.x/6.x versions.
- Fixed additional dependency issues introduced by `AlreadyBeingCreatedException` in the nextarch classifier.
- Resolved the issue where JindoCommitter failed to recognize paths containing special characters in the nextarch classifier.
- Fixed the issue where some RDMA parameters were not taking effect when accessing JindoCache in RDMA mode.

## 6.7.3，2024-11-01

Release of official version features for JindoSDK 6.7.3.

- Updated the [Maven Repository for version 6.7.3](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.3/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.3/jindodata_download.md) for JindoSDK.
- The jindoauth plugin was included during packaging; after upgrading JindoSDK, ensure that `fs.jdo.plugin.dir` points to the jindosdk plugins directory.
- The nextarch classifier fixed the handling of `AlreadyBeingCreatedException` exceptions when reading and writing logs in Hudi.
- The nextarch classifier fixed an occasional hang issue when using HTTP requests to update STS.
- The nextarch classifier fixed a problem where a file could no longer be written to after continuous writes exceeding one hour on OSS-HDFS when Ranger was enabled.

## 6.4.1，2024-10-23

Release of official version features for JindoSDK 6.4.1.

- Updated the [Maven Repository for version 6.4.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.1/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.1/jindodata_download.md) for JindoSDK.
- Fix occasional issues with Signer V4 signature by replacing timestamp conversion functions localtime, gmtime with thread-safe versions localtime_r, gmtime_r.
- The `nextarch classifier` fixes the handling of `AlreadyBeingCreatedException` when reading and writing Hudi logs.
- A fix has been implemented for the Delegation Token Renew mechanism in JindoOssFileSystem.
- Fixes an issue with `JindoCommitter` where it fails to clean up temporary directories left by tasks when accessing OSS-HDFS.
- Enhance compatibility of JindoSDK with interfaces below Hadoop 2.8.x versions, such as `CallerContext` and `FsServerDefaults`.
- Improved classloader loading mechanisms to resolve issues with residual shared objects when multiple classloaders load jindosdk, and decompresses files to the specified directory via `java.io.tmpdir`.

## 6.7.2，2024-10-21

Release of official version features for JindoSDK 6.7.2.

- Updated the [Maven Repository for version 6.7.2](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.2/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.2/jindodata_download.md) for JindoSDK.
- The `nextarch classifier` has been optimized for metrics statistics.
- The `nextarch classifier` has fixed the issue of small blocks appearing after flush in the OSS-HDFS write scenario.
- The `nextarch classifier` has fixed the occasional crash caused by the background metrics thread.
- Pyjindo has fixed the `invalid pointer` issue in the 6.7.x version. For versions 6.7.0 and 6.7.1, the issue can be bypassed by setting the environment variable `JINDO_STAT_MEMORY=0`.

## 6.7.1，2024-10-15

Release of official version features for JindoSDK 6.7.1.

- Update the [Maven repository for version 6.7.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.1/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.1/jindodata_download.md) for JindoSDK.
- The `nextarch classifier` optimizes write performance under default concurrency.
- The `nextarch classifier` fixes the handling of `AlreadyBeingCreatedException` when reading and writing Hudi logs.
- fixes an issue with `JindoCommitter` where it fails to clean up temporary directories left by tasks when accessing OSS-HDFS.
- `jindo-fuse` fixes support for append writes on S3 scheme.

## 6.7.0，2024-09-29

Release of official version features for JindoSDK 6.7.0.

- Update the [Maven repository for version 6.7.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.0/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.0/jindodata_download.md) for JindoSDK.
- The `nextarch classifier` now supports unified memory management with `fs.jdo.memory.pool.size.max.mb` to configure the maximum memory usage and provides real-time memory usage metrics.
- The `nextarch classifier` now supports controlling IO buffer memory usage with `fs.jdo.memory.io.buffer.size.max.ratio`.
- The `nextarch classifier` now supports the Golang SDK.
- The `nextarch classifier` now supports for deep archive storage in OSS object storage.
- The `nextarch classifier` optimized the `pread` interface for zero-copy memory operations.
- The `nextarch classifier` improved `sendfile` usage for writing small files.
- The `nextarch classifier` enhanced the metrics framework to output each metric to separate files for easier collection.
- The `nextarch classifier` excluded log4j dependencies in the distjob framework.
- The `nextarch classifier` optimized OSS-HDFS nnbench performance when `readAfterOpen` is set to false.
- The `nextarch classifier` improved classloader loading mechanisms to resolve issues with residual shared objects when multiple classloaders load jindosdk, and decompresses files to the specified directory via `java.io.tmpdir`.
- The `nextarch classifier` fixed OSS-HDFS StoragePolicy interfaces.
- The `nextarch classifier` fixed the Mtime unit returned by ListDirectory in OSS object storage.
- The `nextarch classifier` ensured that JindoCommitter does not throw exceptions during the Cleanup phase.
- The `nextarch classifier` resolved permission issues with DLF-related CredentialProviders when using MagicCommitter.
- The `nextarch classifier` fixed several issues with JindoCache.

## 6.6.3，2024-09-14

Release of official version features for JindoSDK 6.6.3.

- Update the [Maven repository for version 6.6.3](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.3/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.3/jindodata_download.md) for JindoSDK.
- The nextarch classifier fixes a rare issue where errors due to sendfile were not retried in write scenarios, leading to failures when closing files. 

## 6.6.2，2024-09-12

Release of official version features for JindoSDK 6.6.2.

- Update the [Maven repository for version 6.6.2](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.1/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.1/jindodata_download.md) for JindoSDK.
- The nextarch classifier now supports Deep Archive Storage when accessing OSS.
- Fixed permission issues in the nextarch classifier related to using DLF-related CredentialProviders with MagicCommitter.

## 6.6.1，2024-09-03

Release of official version features for JindoSDK 6.6.1.

- Update the [Maven repository for version 6.6.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.1/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.1/jindodata_download.md) for JindoSDK.
- Optimized handling of SO file residues in the nextarch classifier when multiple classloaders load jindosdk, defaulting to unpacking files into the directory specified by `java.io.tmpdir`.
- Fixed several issues related to accessing JindoCache within the nextarch classifier.
- Further optimized support for metrics frameworks.

## 6.6.0，2024-08-25

Release of official version features for JindoSDK 6.6.0.

- Update the [Maven repository for version 6.6.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.0/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.0/jindodata_download.md) for JindoSDK.
- Enhanced IO performance in the nextarch classifier through coroutine-based asynchrony, enabling higher concurrency at identical configuration levels. For write scenarios, sendfile is used to achieve zero-copy optimization, which saves memory and improves performance.
- Added support for specifying individual IO timeout durations in the nextarch classifier.
- Integrated metrics framework support into the nextarch classifier.
- Resolved issues regarding JindoCache compatibility within the nextarch classifier.
- Default implementation of jindo-fuse now utilizes the nextarch implement.

## 6.5.4，2024-08-20

### Release Summary

Release of official version features for JindoSDK 6.5.4.

### Introduction

- Update the [Maven repository for version 6.5.4](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.4/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.4/jindodata_download.md) for JindoSDK.
- Optimize flink connector, shade some of the dependencies.

## 6.5.3，2024-08-15

### Release Summary

Release of official version features for JindoSDK 6.5.3.

### Introduction

- Update the [Maven repository for version 6.5.3](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.3/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.3/jindodata_download.md) for JindoSDK.
- A fix has been implemented for the Delegation Token Renew mechanism in JindoOssFileSystem.

## 6.5.2, August 12, 2024

### Release Summary

Release of official version features for JindoSDK 6.5.2.

### Introduction

- Update the [Maven repository for version 6.5.2](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.2/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.2/jindodata_download.md) for JindoSDK.
- Fix getfacl command missing entries.

## 6.5.1, August 5, 2024

### Release Summary

Release of official version features for JindoSDK 6.5.1.

### Introduction

- Update the [Maven repository for version 6.5.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.1/oss-maven.md) and the [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.1/jindodata_download.md) for JindoSDK.
- Enhance compatibility of JindoSDK with interfaces below Hadoop 2.8.x versions, such as `CallerContext` and `FsServerDefaults`.
- Optimize the performance of JindoCommitter.
- Optimize the prefetch algorithm under conditions of low memory availability.
- Optimize plugin load，`fs.jdo.plugin.dir` support multiple path，such as `/dir1,/dir2`.
- Fix an issue where listing objects from storage might include itself when paths contain `//`.
- Address security concerns in `jindo-dependence-shaded.jar`, removing dependencies on log4j and Apache Commons Text.
- Resolve several issues encountered while running the [hadoop-compat-bench](https://github.com/apache/hadoop/blob/trunk/hadoop-tools/hadoop-compat-bench/src/site/markdown/HdfsCompatBench.md) with the nextarch classifier.
- Fix problems related to support for RootPolicy by the nextarch classifier.

## Version 6.5.0, July 12, 2024

### Release Summary

Release of official version features for JindoSDK 6.5.0.

### Introduction

- The [Maven repository for JindoSDK 6.5.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.0/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.0/jindodata_download.md) has been updated.
- Fix occasional issues with Signer V4 signature by replacing timestamp conversion functions localtime, gmtime with thread-safe versions localtime_r, gmtime_r.
- Fix occasional hang-on issue in the prefetching algorithm, avoiding multiple initializations of the prefetching singleton module (affects versions 6.2.0+).
- Resolve sporadic crashes when accessing OSS-HDFS via `libjindosdk_c.so`, as well as retry failure problems.
- Support for newer kernel versions in the `nextarch` classifier within the `jindo-core.jar` package.
- Optimize the prefetch algorithm under conditions of low memory availability.
- Committer now supports setting extended attributes (`setXAttr`).

## Version 6.3.5, June 28, 2024

### Release Summary

Release of official version features for JindoSDK 6.3.5.

### Introduction

- The [Maven repository for JindoSDK 6.3.5](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.5/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.5/jindodata_download.md) has been updated.
- Fix occasional issues with Signer V4 signature by replacing timestamp conversion functions localtime, gmtime with thread-safe versions localtime_r, gmtime_r.
- Resolve sporadic crashes when accessing OSS-HDFS via `libjindosdk_c.so`, as well as retry failure problems.

## Version 6.4.0, May 16, 2024

### Release Summary

This release introduces the official version of JindoSDK 6.4.0.

### Introduction

- The [Maven repository for JindoSDK 6.4.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.0/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.0/jindodata_download.md) has been updated.
- JindoSDK now includes support for CallerContext functionality.
- Introduces Write-through flushing, allowing data to be flushed immediately during write operations.
- Concatenation issues have been resolved.
- Support for accessing JindoCache using Remote Direct Memory Access (RDMA) has been added.
- Reading performance from AppendObject objects has been significantly optimized.

## Version 6.1.7, April 12, 2024

### Release Summary

This release introduces the official version of JindoSDK 6.1.7.

### Introduction

- The [Maven repository for JindoSDK 6.1.7](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.7/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.7/jindodata_download.md) has been updated.
- Fixed several issues.
- JindoSDK now supports accessing DLF with both JindoCache and JindoAuth integration.

## Version 6.3.4, April 11, 2024

### Release Summary

This release introduces the official version of JindoSDK 6.4.0.

### Introduction

- The [Maven repository for JindoSDK 6.3.4](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.4/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.4/jindodata_download.md) has been updated.
- JindoFuse now supports specifying `metrics_ip` and `metrics_port` to designate the Prometheus listening IP address and port.
- A fix has been implemented for the Delegation Token Renew mechanism in JindoOssFileSystem.
- An issue has been resolved where `fs.accessPolicies.discovery` without a trailing `/` caused an error with `getTrashRoot` being empty (affects only version 6.3.3).
- A bug has been fixed where `listLocatedStatus` did not support ListObjectV2 when `fs.oss.list.type` was set to `2`, which could lead to a infinite loop (default configuration is unaffected).

## Version 6.3.3 - March 20, 2024

### Summary

Release of JindoSDK version 6.3.3 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.3.3](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.3/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.3/jindodata_download.md) has been updated.
- Fix for symlink creation logic in JindoFuse, supporting CSI plugin scenarios
- Support for accessing object storage through a reverse proxy

## Version 6.3.2 - February 26, 2024

### Summary

Release of JindoSDK version 6.3.2 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.3.2](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.2/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.2/jindodata_download.md) has been updated.
- Fix for reading symlinks in JindoFuse, converting symlink targets to relative paths for CSI plugin compatibility
- Fix for fs.oss.signer.version support in JindoCache
- Fix for the '-version' argument in the jindo CLI tool

## Version 6.3.1 - February 21, 2024

### Summary

Release of JindoSDK version 6.3.1 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.3.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.1/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.1/jindodata_download.md) has been updated.
- Fix for reverse seeks causing prefetching to fail on OSS-HDFS files
- Fix for pread errors on OSS-HDFS files generated by random writes
- Improvement to the getRealPath interface
- Root policy path rewrite now supports one-to-many replacements based on subpaths

## Version 6.3.0 - January 31, 2024

### Summary

Release of JindoSDK version 6.3.0 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.3.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.0/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.0/jindodata_download.md) has been updated.
- Support for accessing OSS/OSS-HDFS using V4 signatures
- Introduction of Python SDK for accessing OSS and OSS-HDFS
- Fix for excessive requests when deleting directories in OSS-HDFS via Jindo command-line tool
- JindoTable hierarchical storage tool now directly archives data to cold archive
- JindoTable MoveTo tool now supports migrating entire Iceberg tables
- Fix for failed symlink reads in JindoFuse

## Version 6.1.6 - December 28, 2023

### Summary

Release of JindoSDK version 6.1.6 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.1.6](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.6/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.6/jindodata_download.md) has been updated.
- Support for accessing OSS/OSS-HDFS using V4 signatures

## Version 6.2.0 - December 22, 2023

### Summary

Release of JindoSDK version 6.2.0 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.2.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.2.0/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.2.0/jindodata_download.md) has been updated.
- New asynchronous CSDK library `libjindosdk_c.so`; deprecation of the old `libjindo-csdk.so`
- Path validity check fix, supporting paths with '*' characters
- Improved error message when JindoFuse encounters invalid names in OSS paths
- Jindo command-line tool now supports setting storage policies for OSS-HDFS thawing
- New prefetch algorithm improves sequential read performance for large files:
  * Configuration options:
    * `fs.oss.read.readahead.prefetcher.version`: Switch for prefetching implementation (default: legacy, new: default)
    * `fs.oss.read.readahead.prefetch.size.max`: Maximum prefetch size (in bytes)
  * Memory pool configuration for new prefetch algorithm:
    * `fs.oss.memory.buffer.size.max.mb`: Total memory pool capacity (in MB)
    * `fs.oss.memory.buffer.size.watermark`: Percentage of memory pool used for prefetching
- Fix for authentication issue in HiveServer2 with Kerberos & LDAP dual authentication scenario
- JindoSDK Java JNI ignores AddressSanitizer false positives

## Version 6.1.5 - December 19, 2023

### Summary

Release of JindoSDK version 6.1.5 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.1.5](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.5/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.5/jindodata_download.md) has been updated.
- Optimizations for OSS-HDFS read and write I/O operations

## Version 6.1.4 - December 14, 2023

### Summary

Release of JindoSDK version 6.1.4 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.1.4](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.4/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.4/jindodata_download.md) has been updated.
- Support for HiveServer2 in Kerberos & LDAP dual authentication scenarios
- JindoSDK Java JNI ignores AddressSanitizer false positives

## Version 6.1.3 - December 1, 2023

### Summary

Release of JindoSDK version 6.1.3 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.1.3](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.3/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.3/jindodata_download.md) has been updated.
- Fixed several issues with accessing jindofs using the `dls://` prefix.

## Version 6.1.2 - November 21, 2023

### Summary

Release of JindoSDK version 6.1.2 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.1.2](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.2/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.2/jindodata_download.md) has been updated.
- Supports ListObjectV2 via `fs.oss.list.type=2` configuration to avoid timeout issues when accessing multi-version buckets.
- Adds support for using the `dls://` prefix to access jindofs.
- Fixes an issue where closing a read stream would cause it to hang.
- Fixes a NullPointerException (NPE) issue that occurred after a single write failure, preventing subsequent writes.

## Version 6.1.1 - October 20, 2023

### Summary

Release of JindoSDK version 6.1.1 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.1.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.1/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.1/jindodata_download.md) has been updated.
- Supports configuring `fs.oss.legacy.version=3.8` for compatibility with JindoSDK 3.8.x configurations.

## Version 6.1.0 - September 28, 2023

### Summary

Release of JindoSDK version 6.1.0 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.1.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.0/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.0/jindodata_download.md) has been updated.
- JindoSDK now allows read-write streams to remain usable even after the FileSystem is closed.

## Version 6.0.1 - August 18, 2023

### Summary

Release of JindoSDK version 6.0.1 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.0.1](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.1/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.1/jindodata_download.md) has been updated.
- JindoSDK adds `isFileClosed()` method to determine if an OSS-HDFS read-write stream is closed.
- Optimization for reducing flush frequency, ensuring the first flush operation with data is executed to prevent empty files being flushed. Configuration details can be found in the [Flume guide using JindoSDK to write to OSS](jindosdk/flume/jindosdk_on_flume.md)

## Version 6.0.0 - August 15, 2023

### Summary

Release of JindoSDK version 6.0.0 with official features.

#### Introduction

- The [Maven repository for JindoSDK 6.0.0](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.0/oss-maven.md) and [Download Url](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.0/jindodata_download.md) has been updated.
- Merged jindo-mapreduce (including JindoCommitterFactory) into the main jindo-sdk module.
- JindoSDK bug fixes include flush frequency reduction, improved garbage collection, and compatibility with OSS-HDFS trash.
- Added support for multiple platforms, including MACOS and mainstream Linux scenarios, with specific extensions required for CentOS 6, Ubuntu22, and Alibaba Cloud Qiantu (ARM) machines, as well as Intel/M1 Macs.
- Upgraded to g++ compiler version 10.4 and adopted C++20 standard.
- Introduced support for [yalangtinglibs](https://github.com/alibaba/yalantinglibs) to access OSS/OSS-HDFS.