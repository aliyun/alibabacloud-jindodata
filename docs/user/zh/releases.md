# JindoSDK 版本记录

## 6.9.1，2025-06-04

### 版本概要

发布 JindoSDK 6.9.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.9.1 的 Maven 仓库](jindosdk/oss-maven.md) 和 [下载地址](jindosdk/jindosdk_download.md)。
- JindoSDK 修复湖表格式预读策略，修复了`fs.oss.read.profile.enable=true`时在部分场景有性能回退的问题。
- JindoFuse 修复 OSS 场景多次 Append/Flush 不支持 2G 以上文件的问题。
- JindoSDK 优化 JindoInputStream 日志，可以指定 `log4j.logger.com.aliyun.jindodata.common.JindoInputStream` 等级以减少 readVectored 场景日志数量。

## 6.9.0，2025-05-21

### 版本概要

发布 JindoSDK 6.9.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.9.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.9.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.9.0/jindodata_download.md)。
- 支持 [openFile()](https://issues.apache.org/jira/browse/HADOOP-15229) 接口，支持指定 read policy（需 Hadoop 3.3.0+）。
- 支持 [Vectored IO](https://issues.apache.org/jira/browse/HADOOP-18103) 接口（需 Hadoop 3.3.6+）。 
- 优化湖表格式预读策略，重 IO 读场景提升 30%（可以配置`fs.oss.read.profile.enable=false`关闭，回退老版本行为）。
- 优化 OSS-HDFS append 合并策略，支持 ComposedBlock。 
- 优化 OSS-HDFS InputStream 锁。
- 优化 OSS 解冻接口，支持设置 OSS 解冻优先级。
- 修复高并发写 OSS/OSS-HDFS close 偶现死锁问题。
- 修复 JindoDistcp 若干问题，支持 `--syncSourceDelete`，详见[JindoDistCp 使用说明](jindotools/jindodistcp_quickstart.md)。
- 修复 JindoCache 若干问题。
- JindoFS CLI 使用 nextarch 实现。

## 6.8.5，2025-04-29

### 版本概要

发布 JindoSDK 6.8.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.5 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.5/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.5/jindodata_download.md)。
- 修复接入 ranger 使用 root policy 访问 OSS-HDFS。 
- 支持使用 auth method 访问 OSS-HDFS。
- JindoFS Cli 支持 recoverLease 命令。
- JindoCache 修复高并发初始化客户端可能导致的死锁。
- JindoFuse 支持 getattr 并发控制。
- JindoFuse 修复写时 rename。
- JindoFuse 修复对非法文件名的判断。

## 6.8.3，2025-04-15

### 版本概要

发布 JindoSDK 6.8.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.3 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.3/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.3/jindodata_download.md)。
- JindoSDK 新增配置 `fs.oss.list.fallback.iterative`，为 `true` 时，支持 listStatus 对大目录自动 fallback 到迭代访问。
- JindoSDK 新增 [Golang SDK 文档](jindosdk/golang/jindosdk_golang_quickstart.md)，并支持迭代 list 接口。
- JindoSDK 访问 OSS 支持 authorization 插件。
- JindoFS CLI 优化 binary 大小。

## 6.8.2，2025-03-24

### 版本概要

发布 JindoSDK 6.8.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.2 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.2/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.2/jindodata_download.md)。
- JindoSDK 修复错误信息不准确的问题，正确返回请求超时 `Request timeout` 而不是 `Connection timed out`。
- JindoSDK 修复 append-close 优化打开时，异常关闭可能导致报错的问题。即 `fs.oss.append.threshold.size` 不为 `0` 时，可能导致 close 后再次 append 失败（默认配置不影响）。
- JindoSDK 修复 listStatusIterator 不支持 ListObjectV2。即配置 `fs.oss.list.type` 为 `2`，可能导致 listStatusIterator 死循环（默认配置不影响）。
- JindoSDK 修复访问 JindoCache 时偶现的 hang on 问题。
- JindoSDK 优化对 IO 请求超时配置的兼容性，默认为 `fs.oss.io.timeout.millisecond` 与 `fs.oss.timeout.millisecond` 两者的最大值。
- JindoSDK 新增配置 `fs.oss.read.use-pread` 以及 `fs.oss.pread.cache.enable`，以优化湖表场景读性能。

## 6.8.1，2025-03-06

### 版本概要

发布 JindoSDK 6.8.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.1/jindodata_download.md)。
- 升级 yalantinglibs 到 [lts1.0.2](https://github.com/alibaba/yalantinglibs/tree/lts1.0.2)
- nextarch classifier 在 oss maven 仓库中将作为默认版本号发布。
- JindoSDK 修复对 summary metrics 的支持。
- JindoSDK 修复对 `fs.oss.upload.async.concurrency` 和 `fs.oss.upload.thread.concurrency` 的支持。 (该问题在 6.7.1 版本引入，可能在写较大文件时，并发过大，导致占用过多链接资源，引发 `Connection timed out`)。
- JindoCache 修复读时落缓存预读块不对齐，落缓存失败问题。
- JindoCache 修复客户端读相关metrics收集问题。
- JindoCache 支持OSS-HDFS UGI信息。
- JindoCache 支持客户端超时回退。
- JindoFuse 优化了元数据缓存占用，预计在海量小文件场景降低内存占用 50%。

## 6.8.0，2025-01-24

### 版本概要

发布 JindoSDK 6.8.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.8.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.8.0/jindodata_download.md)。
- nextarch classifier 优化访问 OSS-HDFS 请求数，读场景元数据请求降低17%。
- nextarch classifier 优化 Java TimedBuffer 获取时间戳频率，降低 CPU 占用。
- nextarch classifier 优化 JindoMagicCommitter PendingSet 大小。
- nextarch classifier 优化预读算法。并新增配置，控制是否立即清除完全读取的预读内存。
- nextarch classifier 新增 metrics 若干，支持通过 java 接口获取 metrics。
- nextarch classifier 新增日志相关配置， 支持日志实例常驻。
- nextarch classifier 修复 OSS-HDFS 对 fs.jdo 前缀配置的支持。
- nextarch classifier 修复访问 jindocache 服务端兼容性问题。
- nextarch classifier 访问 OSS 设置 XAttr 时, 默认设置 x-oss-metadata-directive 为 REPLACE。
- JindoFuse 优化了访问 oss 场景的元数据请求数，支持以 attr_timeout 为时间间隔做元数据请求。
- JindoFuse 修复 jindo-fuse metrics 时间窗口。

## 6.7.8，2024-12-30

### 版本概要

发布 JindoSDK 6.7.8 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.8 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.8/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.8/jindodata_download.md)。
- 升级 yalantinglibs 到 [0.3.8.1](https://github.com/alibaba/yalantinglibs/tree/0.3.8.1)
- nextarch classifer 优化了 Timed Buffer CPU 负载高的问题。
- nextarch classifer 优化了访问 OSS 时的删除策略，新增配置 `fs.oss.delete.quiet.enable`，默认为false，使用简单模式删除。简单模式说明，详见 OSS 文档[《调用 DeleteMultipleObjects 删除多个文件》](https://help.aliyun.com/zh/oss/developer-reference/deletemultipleobjects)。
- nextarch classifer 修复了无法设置 legacy 预读算法的问题。
- nextarch classifer 修复了使用 S3 协议时，打开 `fs.s3.upload.sendfile.enable=true` 后可能引发爆栈的问题。

## 6.7.7，2024-12-20

### 版本概要

发布 JindoSDK 6.7.7 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.7 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.7/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.7/jindodata_download.md)。
- nextarch classifer 修复 V4 签名缺陷。

## 6.5.6，2024-12-12

### 版本概要

发布 JindoSDK 6.5.6 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.6 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.6/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.6/jindodata_download.md)。
- nextarch classifer 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- nextarch classifier 修复 DLF 相关 CredentialProvider 使用 MagicCommitter 时的权限问题。
- nextarch classifier 修复 JindoCache 的若干问题。
- jindo-fuse 默认改为使用 nextarch 实现，并支持访问 DLF Volume。

## 6.5.5，2024-12-06

### 版本概要

发布 JindoSDK 6.5.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.5 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.5/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.5/jindodata_download.md)。
- nextarch classifier 新增配置 `fs.oss.append.threshold.size` 和 `fs.oss.flush.merge.threshold.size`，优化频繁 Close-To-Append 或者 Flush 产生大量小块的问题，配置描述详见 [客户端常用配置](jindosdk/jindosdk_configuration.md)。
- nextarch classifier 修复开启 Ranger 后持续写入一个 OSS-HDFS 文件超过 1 小时后，文件无法继续写入的问题。
- nextarch classifier 修复 AlreadyBeingCreatedException 引入的额外依赖问题。
- nextarch classifier 修复 hudi 读写 log 时，对 AlreadyBeingCreatedException 异常处理。
- nextarch classifier 修复在写 OSS-HDFS 场景 flush 后出现小块的问题。
- nextarch classifier 支持 JindoCommitter 打开 root policy 配置。
- nextarch classifier 修复 JindoCommitter 访问含特殊字符路径时无法识别的问题。
- nextarch classifier 修复 ECS 免密场景，偶现的 STS Token 失效更新时卡死的问题。
- nextarch classifier 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- nextarch classifier 修复 OSS 对象存储，ListDirectory 返回结果的 Mtime 单位。
- nextarch classifier 优化 classloader 加载，在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 `java.io.tmpdir` 时，默认解压至配置目录下。
- nextarch classifier 优化 distjob 框中，排除 log4j 相关依赖。

## 6.7.6，2024-11-27

### 版本概要

发布 JindoSDK 6.7.6 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.6 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.6/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.6/jindodata_download.md)。
- nextarch classifier 新增配置 `fs.oss.append.threshold.size` 和 `fs.oss.flush.merge.threshold.size`，优化频繁 Close-To-Append 或者 Flush 产生大量小块的问题，配置描述详见 [客户端常用配置](jindosdk/jindosdk_configuration.md)。
- jindo-fuse 支持访问 DLF Volume。

## 6.7.5，2024-11-15

### 版本概要

发布 JindoSDK 6.7.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.5 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.5/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.5/jindodata_download.md)。
- nextarch classifier 修复 ECS 免密场景，偶现的 STS Token 失效更新时卡死的问题。
- nextarch classifier 支持 JindoCommitter 打开 root policy 配置。
- nextarch classifier 优化 metrics append 到文件时，目录不存在时自动创建。
- nextarch classifier 增加部分 metrics 指标。

## 6.7.4，2024-11-08

### 版本概要

发布 JindoSDK 6.7.4 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.4 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.4/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.4/jindodata_download.md)。
- 升级 brpc 到 [1.11.0](https://github.com/apache/brpc/releases/tag/1.11.0)
- 修复 libjindosdk_c.so 动态库时遇到的 pthread hook 依赖顺序问题。
- FlinkConnector 新增配置 `oss.serializer.read.auto.compatible=true`，修复从 3.x 版本写的 checkpoint recover 到 4.x/6.x 版本实现时遇到的兼容性问题。
- nextarch classifier 修复 AlreadyBeingCreatedException 引入的额外依赖问题。
- nextarch classifier 修复 JindoCommitter 访问含特殊字符路径时无法识别的问题。
- 修复访问 JindoCache RDMA 模式，部分 RDMA 参数未生效的问题。

## 6.7.3，2024-11-01

### 版本概要

发布 JindoSDK 6.7.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.3 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.3/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.3/jindodata_download.md)。
- 打包时带入 jindoauth plugin，支持升级 JindoSDK 后仍兼容老版本 EMR-Ranger，具体参见[升级文档](https://aliyun.github.io/alibabacloud-jindodata/upgrade/emr2_upgrade_jindosdk/#41-emr-ranger)。
- nextarch classifier 修复 hudi 读写 log 时，对 AlreadyBeingCreatedException 异常处理。
- nextarch classifier 修复在使用 http 请求更新 STS 时，偶现卡住问题。
- nextarch classifier 修复开启 Ranger 后持续写入一个 OSS-HDFS 文件超过 1 小时后，文件无法继续写入的问题。

## 6.4.1，2024-10-23

### 版本概要

发布 JindoSDK 6.4.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.4.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.1/jindodata_download.md)。
- 修复偶现 Signer V4 签名问题，将签名时间戳转换函数 localtime、gmtime 替换为 localtime_r、gmtime_r 避免并发问题。
- 修复 libjindosdk_c.so 访问 OSS-HDFS 偶现 crash 问题，及重试失败问题。
- 修复 hudi 读写 log 时，对 AlreadyBeingCreatedException 异常处理。
- 修复 JindoOssFileSystem 的 Delegation Token Renew 机制。
- 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- 提升 JindoSDK 对于低于 Hadoop 2.8.x 版本接口的兼容性，如 `CallerContext` 及 `FsServerDefaults`。
- 优化 classloader 加载，在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 `java.io.tmpdir` 时，默认解压至配置目录下。

## 6.7.2，2024-10-21

### 版本概要

发布 JindoSDK 6.7.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.2 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.2/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.2/jindodata_download.md)。
- nextarch classifier 优化 metrics 统计信息。
- nextarch classifier 修复在写 OSS-HDFS 场景 flush 后出现小块的问题。
- nextarch classifier 修复后台 metrics 线程引发的偶现 crash 问题。
- Pyjindo 修复 6.7.x 版本报 `invalid pointer` 问题，6.7.0 及 6.7.1 版本可以通过配置环境变量 `JINDO_STAT_MEMORY=0` 绕过。

## 6.7.1，2024-10-15

### 版本概要

发布 JindoSDK 6.7.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.1/jindodata_download.md)。
- nextarch classifier 优化默认并发下的写性能。
- 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- jindo-fuse 修复在 s3 scheme 上追加写的支持。

## 6.7.0，2024-09-29

### 版本概要

发布 JindoSDK 6.7.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.7.0/jindodata_download.md)。
- nextarch classifier 支持统一管理内存，使用 `fs.jdo.memory.pool.size.max.mb` 配置最大使用内存，并支持内存实时使用 metrics。
- nextarch classifier 支持 io buffer 内存使用控制，使用 `fs.jdo.memory.io.buffer.size.max.ratio` 配置最大使用内存池的比例。
- nextarch classifier 支持 Golang SDK。
- nextarch classifier 支持 OSS 对象存储深冷归档。
- nextarch classifier 优化 pread 接口，内存零拷贝。
- nextarch classifier 优化 sendfile 在写小文件场景的使用。
- nextarch classifier 优化 metrics 框架，支持每次输出到单独文件，方便采集。
- nextarch classifier 优化 distjob 框中，排除 log4j 相关依赖。
- nextarch classifier 优化 classloader 加载，在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 `java.io.tmpdir` 时，默认解压至配置目录下。
- nextarch classifier 修复 OSS-HDFS StoragePolicy 相关接口。
- nextarch classifier 修复 OSS 对象存储，ListDirectory 返回结果的 Mtime 单位。
- nextarch classifier 修复 JindoCommitter 在 Cleanup 阶段，不应抛出异常。
- nextarch classifier 修复 DLF 相关 CredentialProvider 使用 MagicCommitter 时的权限问题。
- nextarch classifier 修复 JindoCache 的若干问题。

## 6.6.3，2024-09-14

### 版本概要

发布 JindoSDK 6.6.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.6.3 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.3/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.3/jindodata_download.md)。
- nextarch classifier 修复写场景小概率因 sendfile 出现错误未重试，导致文件 close 时报失败的情况。

## 6.6.2，2024-09-12

### 版本概要

发布 JindoSDK 6.6.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.6.2 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.2/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.2/jindodata_download.md)。
- nextarch classifier 访问 OSS 支持深冷归档
- nextarch classifier 修复 DLF 相关 CredentialProvider 使用 MagicCommitter 时的权限问题。

## 6.6.1，2024-09-03

### 版本概要

发布 JindoSDK 6.6.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.6.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.2/jindodata_download.md)。
- nextarch classifier 优化在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 `java.io.tmpdir` 时，默认解压至配置目录下。
- nextarch classifier 修复访问 JindoCache 的若干问题。
- nextarch classifier 优化 metrics 框架。

## 6.6.0，2024-08-25

### 版本概要

发布 JindoSDK 6.6.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.6.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.6.0/jindodata_download.md)。
- nextarch classifier 优化 io 性能，使用协程异步化，相同配置下支持更高并发。写场景使用 sendfile 实现零拷贝优化，节省内存，并提升性能。
- nextarch classifier 支持单独指定 io 超时时间。
- nextarch classifier 支持 metrics 框架。
- 修复 nextarch classifier 对 JindoCache 支持问题。
- jindo-fuse 默认改为使用 nextarch 实现。

## 6.5.4，2024-08-20

### 版本概要

发布 JindoSDK 6.5.4 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.4 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.4/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.4/jindodata_download.md)。
- 优化 flink connector 依赖，对部分依赖进行 shade。

## 6.5.3，2024-08-15

### 版本概要

发布 JindoSDK 6.5.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.3 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.3/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.3/jindodata_download.md)。
- 修复 JindoOssFileSystem 的 Delegation Token Renew 机制。

## 6.5.2，2024-08-12

### 版本概要

发布 JindoSDK 6.5.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.2 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.2/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.2/jindodata_download.md)。
- 修复 getfacl 缺少 entries 问题。

## 6.5.1，2024-08-05

### 版本概要

发布 JindoSDK 6.5.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.1/jindodata_download.md)。
- 提升 JindoSDK 对于低于 Hadoop 2.8.x 版本接口的兼容性，如 `CallerContext` 及 `FsServerDefaults`。
- 优化 JindoCommitter 性能。
- 优化内存不足时的预读算法，回收不使用的内存。
- 优化 plugin 加载方式，`fs.jdo.plugin.dir`支持以多路径加载，如`/dir1,/dir2`
- 修复 list 对象存储时，路径中带有`//`，可能会list出自身的问题
- 修复 jindo-dependence-shaded.jar 安全性问题，去除 log4j 及 apache commons text 依赖
- 修复 nextarch classifier 跑 [hadoop-compat-bench](https://github.com/apache/hadoop/blob/trunk/hadoop-tools/hadoop-compat-bench/src/site/markdown/HdfsCompatBench.md) 若干问题。
- 修复 nextarch classifier 对 RootPolicy 支持问题。

## 6.5.0，2024-07-12

### 版本概要

发布 JindoSDK 6.5.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.5.0/jindodata_download.md)。
- 修复偶现 Signer V4 签名问题，将签名时间戳转换函数 localtime、gmtime 替换为 localtime_r、gmtime_r 避免并发问题。
- 修复 libjindosdk_c.so 访问 OSS-HDFS 偶现 crash 问题，及重试失败问题。
- jindo-core.jar nextarch classifer 支持新版内核。
- 优化内存不足时的预读算法，防止内存过度抢占。
- commiter 支持 setXAttr。

## 6.3.5，2024-06-28

### 版本概要

发布 JindoSDK 6.3.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.5 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.5/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.5/jindodata_download.md)。
- 修复偶现 Signer V4 签名问题，将签名时间戳转换函数 localtime、gmtime 替换为 localtime_r、gmtime_r 避免并发问题。
- 修复 libjindosdk_c.so 访问 OSS-HDFS 偶现 crash 问题，及重试失败问题。

## 6.4.0，2024-05-16

### 版本概要

发布 JindoSDK 6.4.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.4.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.4.0/jindodata_download.md)。
- JindoSDK 支持 CallerContext。
- JindoSDK 支持写时 flush。
- 修复 concat 问题。
- 支持 RDMA 访问 JindoCache。
- 优化了读 AppendObject 对象的性能。

## 6.1.7，2024-04-12

### 版本概要

发布 JindoSDK 6.1.7 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.7 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.7/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.7/jindodata_download.md)。
- 修复若干问题
- JindoSDK 支持 JindoCache + JindoAuth 访问 DLF。

## 6.3.4，2024-04-11

### 版本概要

发布 JindoSDK 6.3.4 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.4 的 Maven 仓库](jindosdk/oss-maven.md) 和 [下载地址](jindosdk/jindosdk_download.md)。
- JindoFuse 支持指定 metrics_ip、metrics_port 指定 promethues 监听 ip 和 端口。
- 修复 JindoOssFileSystem 的 Delegation Token Renew 机制。
- 修复 `fs.accessPolicies.discovery` 末尾不带 `/` 时，getTrashRoot 为空报错（仅影响 6.3.3 版本）。
- 修复 listLocatedStatus 不支持 ListObjectV2。即配置 `fs.oss.list.type` 为 `2`，可能导致 listLocatedStatus 死循环（默认配置不影响）。

## 6.3.3，2024-03-20

### 版本概要

发布 JindoSDK 6.3.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.3 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.3/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.3/jindodata_download.md)
- 修复 JindoFuse 创建 symlink 逻辑，支持 csi plugin 场景使用
- 支持通过设置的反向代理访问对象存储

## 6.3.2，2024-02-26

### 版本概要

发布 JindoSDK 6.3.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.2 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.2/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.2/jindodata_download.md)
- 修复 JindoFuse 读取 symlink 逻辑，symlink target 改为相对路径以支持 csi plugin
- 修复 JindoCache 对 fs.oss.signer.version 的支持
- 修复 jindo CLI 参数 -version

## 6.3.1，2024-02-21

### 版本概要

发布 JindoSDK 6.3.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.1/jindodata_download.md)
- 修复在 OSS-HDFS 文件上进行反向 seek 导致预读失效的问题
- 修复在由随机写产生的 OSS-HDFS 文件上 pread 预读错误的问题
- 修复 getRealPath 接口
- Root Policy 路径改写支持根据子路径区分的一对多替换

## 6.3.0，2024-01-31

### 版本概要

发布 JindoSDK 6.3.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.3.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.3.0/jindodata_download.md)
- 支持使用 V4 签名访问 OSS/OSS-HDFS
- 提供 Python SDK，支持访问 OSS 和 OSS-HDFS
- 修复 Jindo 命令行删除 OSS-HDFS 目录时发起过多请求的问题
- JindoTable 分层存储工具支持归档数据直接转冷归档
- JindoTable MoveTo 工具支持 Iceberg 表整体迁移
- 修复 JindoFuse 读取 symlink 失败

## 6.1.6，2023-12-28

### 版本概要

发布 JindoSDK 6.1.6 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.6 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.6/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.6/jindodata_download.md)
- 支持使用 V4 签名访问 OSS/OSS-HDFS

## 6.2.0，2023-12-22

### 版本概要

发布 JindoSDK 6.2.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.2.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.2.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.2.0/jindodata_download.md)
- 新增支持异步的 CSDK `libjindosdk_c.so`，老版本 `libjindo-csdk.so` 逐渐弃用。
- 修复路径合法性检查，支持带 `*` 的路径
- 优化 JindoFuse 访问 oss 时有非法名称的路径时的报错信息
- Jindo 命令行支持 OSS-HDFS setStoragePolicy 解冻相关策略
- 采用新的预读算法，顺序读大文件的性能提高
  > 新预读算法可配置参数如下
  > - `fs.oss.read.readahead.prefetcher.version = default` 预读实现切换开关
      >   - 可选值：`legacy` 原预读算法, `default` 新预读算法
  > - `fs.oss.read.readahead.prefetch.size.max = 268435456` 预读最大长度（单位：byte）
  >
  > 新预读算法可能使用更多内存。若配置了新预读算法后发生性能下降，可能是由于内存池容量不足导致预读的块在被访问到之前就被提前逐出。为了避免该情况发生，可以考虑缩减最大预读长度，或允许预读使用更多内存。内存池配置相关参数如下
  > - `fs.oss.memory.buffer.size.max.mb = 6144` 内存池总容量（单位：MB）
  > - `fs.oss.memory.buffer.size.watermark = 0.3` 内存池用于预读的容量比例
- 修复 Hive 使用 LDAP + Kerberos + OSS-HDFS 时的认证问题

## 6.1.5，2023-12-19

### 版本概要

发布 JindoSDK 6.1.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.5 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.5/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.5/jindodata_download.md)
- OSS-HDFS 读写 IO 优化

## 6.1.4，2023-12-14

### 版本概要

发布 JindoSDK 6.1.4 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.4 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.4/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.4/jindodata_download.md)
- 支持 HiveServer2 在 Kerberos & LDAP 双认证场景下使用
- JindoSDK Java Jni 忽略 AddressSanitizer 误报

## 6.1.3，2023-12-01

### 版本概要

发布 JindoSDK 6.1.3 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.3 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.3/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.3/jindodata_download.md)
- 修复 `dls://` 前缀访问 jindofs 的若干问题。

## 6.1.2，2023-11-21

### 版本概要

发布 JindoSDK 6.1.2 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.2 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.2/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.2/jindodata_download.md)
- 通过配置 `fs.oss.list.type=2` 支持ListObjectV2，以避免访问开启了多版本bucket时的操作超时问题。
- 支持使用 `dls://` 前缀访问 jindofs。
- 修复读流 close 会卡住的问题。
- 修复写流出现一次 write 失败后，后续 write 出现的 NPE 问题。

## 6.1.1，2023-10-20

### 版本概要

发布 JindoSDK 6.1.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.1/jindodata_download.md)
- 支持通过配置 fs.oss.legacy.version 为 3.8，兼容 JindoSDK 3.8.x 配置

## 6.1.0，2023-09-28

### 版本概要

发布 JindoSDK 6.1.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.1.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.1.0/jindodata_download.md)
- JindoSDK 支持 FileSystem close 后仍支持使用读写流进行读写

## 6.0.1，2023-08-18

### 版本概要

发布 JindoSDK 6.0.1 正式版的功能

### 介绍

- JindoSDK 更新 [6.0.1 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.1/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.1/jindodata_download.md)
- JindoSDK 支持 isFileClosed 方法，用于判断访问 OSS-HDFS 的读写流是否已关闭。
- JindoSDK 对 flush 降频做了优化，在降频配置生效时，对第一次有数据的flush操作保证执行，防止flush出一个空文件。降频配置，可参考[Flume 使用 JindoSDK 写入 OSS](jindosdk/flume/jindosdk_on_flume.md)

## 6.0.0，2023-08-15

### 版本概要

发布 JindoSDK 6.0.0 正式版的功能

### 介绍

- JindoSDK 更新 [6.0.0 的 Maven 仓库](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.0/oss-maven.md) 和 [下载地址](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/6.0.0/jindodata_download.md)，JindoSDK 合并 jindo-mapreduce (JindoCommitterFactory等) 到 jindo-sdk
- JindoSDK 新增若干修复，包含 flush 降频，优化 GC 回收，适配 OSS-HDFS 回收站等
- 多平台支持 MACOS 和 主流 linux 场景，其中 CentOS 6, Ubuntu22, 阿里云倚天机型(ARM)，MacOS (Intel/M1) 需要使用特定的扩展包
- 升级 g++ 编译器到 10.4，使用 c++20 标准
- 支持 [yalangtinglibs](https://github.com/alibaba/yalantinglibs) 访问 OSS/OSS-HDFS
