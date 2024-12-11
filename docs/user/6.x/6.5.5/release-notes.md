# 6.5.5，2024-12-06

## 版本概要

发布 JindoSDK 6.5.5 正式版的功能

### 介绍

- JindoSDK 更新 [6.5.5 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- nextarch classifier 新增配置 fs.oss.append.threshold.size 和 fs.oss.flush.merge.threshold.size，优化频繁 Close-To-Append 或者 Flush 产生大量小块的问题，配置描述详见 客户端常用配置。
- nextarch classifier 修复开启 Ranger 后持续写入一个 OSS-HDFS 文件超过 1 小时后，文件无法继续写入的问题。
- nextarch classifier 修复 AlreadyBeingCreatedException 引入的额外依赖问题。
- nextarch classifier 修复 hudi 读写 log 时，对 AlreadyBeingCreatedException 异常处理。
- nextarch classifier 修复在写 OSS-HDFS 场景 flush 后出现小块的问题。
- nextarch classifier 支持 JindoCommitter 打开 root policy 配置。
- nextarch classifier 修复 JindoCommitter 访问含特殊字符路径时无法识别的问题。
- nextarch classifier 修复 ECS 免密场景，偶现的 STS Token 失效更新时卡死的问题。
- 修复 JindoCommitter 访问 OSS-HDFS 未清理 task 残留的临时目录。
- nextarch classifier 优化 classloader 加载，在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 java.io.tmpdir 时，默认解压至配置目录下。
- nextarch classifier 修复 OSS 对象存储，ListDirectory 返回结果的 Mtime 单位。
- nextarch classifier 优化在多个 classloader 加载 jindosdk 时的 so 残留问题，并且在配置了 java.io.tmpdir 时，默认解压至配置目录下。
- nextarch classifier 优化 distjob 框中，排除 log4j 相关依赖。