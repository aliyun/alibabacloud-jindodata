# 6.7.4，2024-11-08

## 版本概要

发布 JindoSDK 6.7.4 正式版的功能

### 介绍

- JindoSDK 更新 [6.7.4 的 Maven 仓库](oss-maven.md) 和 [下载地址](jindodata_download.md)。
- 升级 brpc 到 [1.11.0](https://github.com/apache/brpc/releases/tag/1.11.0)
- 修复 libjindosdk_c.so 动态库时遇到的 pthread hook 依赖顺序问题。
- FlinkConnector 新增配置 `oss.serializer.read.auto.compatible=true`，修复从 3.x 版本写的 checkpoint recover 到 4.x/6.x 版本实现时遇到的兼容性问题。
- nextarch classifier 修复 AlreadyBeingCreatedException 引入的额外依赖问题。
- nextarch classifier 修复 JindoCommitter 访问含特殊字符路径时无法识别的问题。
- 修复访问 JindoCache RDMA 模式，部分 RDMA 参数未生效的问题。