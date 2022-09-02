# JindoData 4.5.1 版本说明
## 版本概要
JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 是原阿里云 EMR SmartData 组件的升级版本。具体介绍请参考 [OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)。

这个版本是对 4.5.0 的小幅升级。上一版本发布以来，我们积累了大量的客户经验，主动发现以及客户反馈了一些真实问题，我们对此做了重要的修复和改进。JindoFS 改进了服务稳定性以及一些异常情况处理。JindoFS 和 JindoFSx 进一步改进自适应预读算法，提高预读效率。JindoDistCp 做了大量修复和优化，增强了数据拷贝过程的稳定性。JindoFuse采用了新的底层设计，进一步提高了性能。


## 主要功能
### JindoFS 存储系统
- JindoFS 改进了内存使用问题。
- JindoFS 添加 ASSUME_ROLE 错误的异常处理和日志告警。
- JindoFS 支持重试时更新动态AK。
- JindoFS 进一步改进自适应预读算法，提升预读效率。
- JindoFS 文件随机写场景的读写路径修复。
- JindoFS 支持 checkAccess 接口。

### JindoFSx 存储加速系统
- JindoFSx 进一步改进自适应预读算法，提升预读效率。
- JindoFSx 支持路径带空格。
- JindoFSx 改进多副本读可能存在热点的问题。

### JindoSDK 和工具支持
- Jindo 命令提供完整的 Hadoop 命令覆盖。
- Jindo 命令增加对 HDFS 的纯 native 支持，大幅提升性能和用户使用体验。
- JindoDistCp 支持对接阿里云监控（CloudMonitor）。
- JindoDistCp 支持 OSS->HDFS 路径的 checksum 检查。
- JindoDistCp 支持 Job 切分参数.
- JindoDistCp 修复拷贝过程中源文件删除错误处理逻辑。
- JindoSDK 优化随机读的内存占用。

### JindoFuse POSIX 支持
- JindoFuse 使用 low-level API 重新设计，大幅提高 readdir 等操作的性能。
- JindoFuse 修复挂载 JindoFSx 后出现异常程序 list 根目录问题。