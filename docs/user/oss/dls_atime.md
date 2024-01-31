# 阿里云 OSS-HDFS 服务（JindoFS 服务）AccessTime使用说明

## 介绍
OSS-HDFS 支持文件 AccessTime， 文件 AccessTime 精度默认配置为1小时，具体配置项为
```
namespace.file.accesstime.precision.millisecond = 3600000
```
AccessTime 精度表示如果当前时间和文件当前的 AccessTime 之前的差值超过配置的精度值，则更新文件的AccessTime，否则保持文件AccessTime不变。AccessTime精度过小会导致频繁更新文件的AccessTime，影响文件的读取性能。


## 获取 AccessTime

OSS-HDFS 文件的AccessTime 只能通过元数据导出查看，元数据导出功能参考
[元数据导出](dls_dump_inventory_howto.md).