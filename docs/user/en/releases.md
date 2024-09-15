# JindoSDK Release History

## 6.6.3，2024-09-14

Release of official version features for JindoSDK 6.6.2.

- Update the [Maven repository for version 6.6.3](jindosdk/oss-maven.md) and the [Download Url](jindosdk/jindosdk_download.md) for JindoSDK.
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
- A bug has been fixed where `listStatusIterator` did not support ListObjectV2 when `fs.oss.list.type` was set to `2`, which could lead to a infinite loop (default configuration is unaffected).

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