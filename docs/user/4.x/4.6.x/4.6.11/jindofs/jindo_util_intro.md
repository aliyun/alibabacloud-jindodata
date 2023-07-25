# jindo 命令使用方法
*(从 4.5.0 开始支持)*

## 下载 jindo 命令行工具包（当前只支持 Linux 环境使用）
1、下载 [jindosdk-4.6.11.tar.gz](/docs/user/4.x/jindodata_download.md)

2、解压 tar 包 
```shell
tar -zxvf jindosdk-4.6.11.tar.gz
```

3、在 `/jindosdk-4.6.11/bin/` 目录下找到 `jindo-util` 二进制文件
```shell
chmod 700 jindo-util
mv jindo-util jindo
```

4、查看 jindo 所有支持的命令和使用方法
```shell
# ./jindo fs -help
Usage:
	jindo fs
		[-appendToFile <localsrc> ... <dst>]
		[-archive [-i] [-c] <path>]
		[-cat <src> ...]
		[-count [-p] [-r] <path> ...]
		[-cp [-f] <src> ... <dst>]
		[-checksum <src> ...]
		[-chgrp [-R] GROUP PATH...]
		[-chmod [-R] <MODE[,MODE]... | OCTALMODE> PATH...]
		[-chown [-R] [OWNER][:[GROUP]] PATH...]
		[-copyFromLocal [-f] <localsrc> ... <dst>]
		[-copyToLocal [-f] <src> ... <localdst>]
		[-createSnapshot <snapshotDir> [<snapshotName>]]
		[-checkStoragePolicy -path <path>]
		[-deleteSnapshot <snapshotDir> <snapshotName>]
		[-du [-s] [-p] [-r] <path> ...]
		[-decommission -hosts <hosts>]
		[-formatCache]
		[-get [-f] <src> ... <localdst>]
		[-getfacl [-R] <path>]
		[-getfattr [-R] {-n name | -d} <path>]
		[-getStoragePolicy -path <path>]
		[-ls [-R] <path>]
		[-listPolicies]
		[-load [-meta] [-data] [-s] [-m] [-R] [-replica value] <path>]
		[-mkdir <path>]
		[-mv <src> ... <dst>]
		[-moveFromLocal <localsrc> ... <dst>]
		[-moveToLocal <src> <localdst>]
		[-metaDiff <path>]
		[-put [-f] <localsrc> ... <dst>]
		[-rm [-f] [-r|-R] <src> ...]
		[-rmdir <src> ...]
		[-renameSnapshot <snapshotDir> <oldName> <newName>]
		[-restore [-days value] <path>]
		[-stat [format] <path> ...]
		[-setStoragePolicy -path <path> -policy <policy>]
		[-setfacl [-R] [{-b|-k} {-m|-x <acl_spec>} <path>]|[--set <acl_spec> <path>]]
		[-setfattr {-n name [-v value] | -x name} <path>]
		[-sync [-update] [-thread thread_num] <localsrc> <dst>
		[-touchz <path> ...]
		[-truncate [-w] <length> <path> ...]
		[-unarchive [-i] [-a] <path>]
		[-uncache <path>]
		[-unsetStoragePolicy -path <path>]
	jindo admin
		[-mount <mountpoint> <path>]
		[-report]
		[-reportMetrics]
		[-umount <mountpoint>]
	jindo admin
		[-addProxyUser -dlsUri <path> -proxyUser <value> -users|-groups <value> -hosts <value>] 
		[-allowSnapshot -dlsUri <path>]
		[-addUserGroupsMapping -dlsUri <path> -user <value> -groups <value>]
		[-disallowSnapshot -dlsUri <path>]
		[-deleteProxyUser -dlsUri <path> -proxyUser <value>]
		[-deleteUserGroupsMapping -dlsUri <path> -user <value>]
		[-dumpFile -dlsUri <path>]
		[-dumpBlockFiles -in <path> -out <path>]
		[-listProxyUsers -dlsUri <path> [-maxKeys <value>] [-marker <value>]]
		[-listUserGroupsMappings -dlsUri <path> [-maxKeys <value>] [-marker <value>]]
		[-snapshotDiff -dlsUri <path> -fromSnapshot <value> -toSnapshot <value>]
	jindo benchmark
		[-io -read -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-memCheck]]
		[-io -pread -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-seekTimes <value>] [-memCheck]]
		[-io -write -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-memCheck]]
		[-meta -mkdir -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-memCheck]]
		[-meta -listDirectory -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
		[-meta -getFileStatus -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
		[-meta -renameFile -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
```

## 使用 jindo 命令访问标准 OSS 或者 OSS-HDFS
### 全局配置
1、新建配置文件 `jindosdk.cfg`
```shell
[common]
logger.dir = /tmp/jindo-util/
logger.sync = false
logger.consolelogger = false
logger.level = 0
logger.verbose = 0
logger.cleaner.enable = true
hadoopConf.enable = false

[jindosdk]
fs.oss.endpoint = <ENDPOINT>      
fs.oss.accessKeyId = <ACCESS_KEYID>   
fs.oss.accessKeySecret = <ACCESS_KEYSECRET>                                        
```

* < ENDPOINT>: 需要访问的 OSS 或者 OSS-HDFS 的 endpoint
* <ACCESSKEY_ID> : 需要访问的 OSS 或者 OSS-HDFS 的 AccessKeyId
* <ACCESSKEY_SECRET> : 需要访问的 OSS 或者 OSS-HDFS 的 AccessKeySecret

2、添加环境变量
```shell 
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>
```
* <JINDOSDK_CFG_DIR> : jindosdk.cfg 配置文件所在的绝对路径，比如 /etc/

3、使用 jindo 命令访问 OSS
```shell
./jindo fs -ls oss://<bucket>/<dir>
```

### bucket 级别配置
1、新建配置文件 `jindosdk.cfg`
```shell
[common]
logger.dir = /tmp/jindo-util/
logger.sync = false
logger.consolelogger = false
logger.level = 0
logger.verbose = 0
logger.cleaner.enable = true
hadoopConf.enable = false

[jindosdk]
fs.oss.bucket.<BUCKET>.accessKeyId =  <ACCESS_KEYID>   
fs.oss.bucket.<BUCKET>.accessKeySecret = <ACCESS_KEYSECRET>
fs.oss.bucket.<BUCKET>.endpoint = <ENDPOINT>                                                      
```

* < ENDPOINT>: 需要访问的 OSS 或者 OSS-HDFS 的 endpoint
* <ACCESSKEY_ID> : 需要访问的 OSS 或者 OSS-HDFS 的 AccessKeyId
* <ACCESSKEY_SECRET> : 需要访问的 OSS 或者 OSS-HDFS 的 AccessKeySecret

2、添加环境变量
```shell 
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>
```
* <JINDOSDK_CFG_DIR> : jindosdk.cfg 配置文件所在的绝对路径，比如 /etc/

3、使用 jindo 命令访问 OSS-HDFS 服务
```shell
./jindo fs -ls oss://<bucket>/<dir>
```


## 使用 jindo sync 同步数据
1、新建配置文件 `jindosdk.cfg`
```shell
[common]
logger.dir = /tmp/jindo-util/
logger.sync = false
logger.consolelogger = false
logger.level = 0
logger.verbose = 0
logger.cleaner.enable = true
hadoopConf.enable = false

[jindosdk]
fs.oss.endpoint = <ENDPOINT>      
fs.oss.accessKeyId = <ACCESS_KEYID>   
fs.oss.accessKeySecret = <ACCESS_KEYSECRET>                                        
```

* < ENDPOINT>: 需要访问的 OSS 或者 OSS-HDFS 的 endpoint
* <ACCESSKEY_ID> : 需要访问的 OSS 或者 OSS-HDFS 的 AccessKeyId
* <ACCESSKEY_SECRET> : 需要访问的 OSS 或者 OSS-HDFS 的 AccessKeySecret

2、添加环境变量
```shell 
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>
```
* <JINDOSDK_CFG_DIR> : jindosdk.cfg 配置文件所在的绝对路径，比如 /etc/

3、使用 jindo 命令进行数据传输
```shell
./jindo fs -sync -thread 10 /local/dir/ oss://<bucket>/<dir>
```
* thread: 使用线程数量

4、使用端点续传功能
```shell
./jindo fs -sync -update -thread 10 /local/dir/ oss://<bucket>/<dir>
```
* 因为需要做文件比较，使用断点续传可能对传输性能有一定的影响
  