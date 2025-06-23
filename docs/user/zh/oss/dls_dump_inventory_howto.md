# 阿里云 OSS-HDFS 服务（JindoFS 服务）元数据导出使用说明
*(从 4.6.0 开始支持)*

## 介绍
使用元数据导出功能，可以将当前 OSS-HDFS bucket 下的文件元数据清单导出到 OSS 上的，格式为 json 文件，方便用户对元数据进行统计分析    

* 配置 JindoFS 命令行工具，配置对应 OSS-HDFS bucket 的访问密钥，参考 [JindoFS 命令行工具使用说明](../jindofs/jindofs_client_tools.md) 

* 执行导出命令
```bash
./jindofs admin -dumpInventory oss://<hdfs_bucket>/
```

此时可以观察到输出路径
```bash
============Dump Inventory=============
Job Id: 2ce40fba-5704-45c4-8720-d92a891d5cfd
Data Location: oss://<oss_bucket>/.dlsdata/.sysinfo/meta_analyze/inventory/1666584461201.2ce40fba-5704-45c4-8720-d92a891d5cfd
.....................................................................................................................
FINISHED.
```
该命令为阻塞命令，请耐心等待10秒钟~10分钟（根据元数据量大小），知道最后输出```FINISHED```表示导出成功。

* 下载结果文件
使用 [jindo 命令行工具](usages/oss_jindo_cli.md)（配置普通OSS的key非OSS-HDFS） 、或[ossutil](https://help.aliyun.com/document_detail/50452.html)、或使用Hadoop fs 命令、或在OSS控制台页面，下载结果文件。

```bash
ossutil cp oss://<oss_bucket>/.dlsdata/.sysinfo/meta_analyze/inventory/1666584461201.2ce40fba-5704-45c4-8720-d92a891d5cfd ./
```
下载到本地，使用vi/vim打开即可。

示例结果参考
```json
{"id":16385,"path":"/","type":"directory","size":0,"user":"admin","group":"supergroup","atime":0,"mtime":1666581702933,"permission":511,"state":1}
{"id":6246684106789500068,"path":"/dls-1000326249","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}
{"id":6246684106789500069,"path":"/dls-1000326249/benchmark","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}
{"id":6246684106789500070,"path":"/dls-1000326249/benchmark/n1","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}
{"id":6246684106789500071,"path":"/dls-1000326249/benchmark/n1/490747449","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660895613953,"permission":511,"state":0}
```


### 进阶使用

#### 1. 指定元数据输出字段
*(从 6.9.1 开始支持)*

该功能用于指定所需文件信息字段，默认输出所有字段。

用法：
```bash
## -field field : 指定元数据字段
## path为必选字段，另外还需指定一个及以上字段
./jindofs admin -dumpInventory oss://<hdfs_bucket>/ -field path -field mtime
```

示例结果参考
```json
{"path":"/","mtime":1666581702933}
{"path":"/dls-1000326249","mtime":1660889124590}
{"path":"/dls-1000326249/benchmark","mtime":1660889124590}
{"path":"/dls-1000326249/benchmark/n1","mtime":1660889124590}
{"path":"/dls-1000326249/benchmark/n1/490747449","mtime":1660895613953}
```
