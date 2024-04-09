# jindo-util 使用方法
## 下载 jindo-util 工具（当前只支持 Linux 环境使用）
1、下载 [jindofsx-4.5.2.tar.gz](/docs/user/4.x/jindodata_download.md)

2、解压 tar 包 
```shell
tar -zxvf jindofsx-4.5.2.tar.gz
```

3、在 `/jindofsx-4.5.2/bin/` 目录下找到 `jindo-util` 二进制文件
```shell
chmod 700 jindo-util
mv jindo-util jindo
```

4、查看 jindo-util 所有支持的命令和使用方法
```shell
# jindo fs -help
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
		[-touchz <path> ...]
		[-truncate [-w] <length> <path> ...]
		[-unarchive [-i] [-a] <path>]
		[-uncache <path>]
		[-unsetStoragePolicy -path <path>]
	jindo benchmark
		[-io -read -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-memCheck]]
		[-io -pread -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-seekTimes <value>] [-memCheck]]
		[-io -write -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-memCheck]]
		[-meta -mkdir -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-memCheck]]
		[-meta -listDirectory -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
		[-meta -getFileStatus -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
		[-meta -renameFile -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
```

## 使用 jindo-util 访问标准 OSS
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
fs.oss.endpoint = <OSS_ENDPOINT>   
fs.oss.accessKeyId = <OSS_ACCESSKEY_ID>   
fs.oss.accessKeySecret = <OSS_ACCESSKEY_SECRET>                                                            
```

* <OSS_ENDPOINT> : 需要访问的 OSS 的 endpoint
* <OSS_ACCESSKEY_ID> : 需要访问的 OSS 的 AccessKeyId
* <OSS_ACCESSKEY_SECRET> : 需要访问的 OSS 的 AccessKeySecret

2、添加环境变量
```shell 
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>
```
* <JINDOSDK_CFG_DIR> : jindosdk.cfg 配置文件所在的绝对路径，比如 /etc/

3、使用 jindo-util 访问 OSS
```shell
jindo fs -ls oss://<bucket>/<dir>
```

## 使用 jindo-util 访问 OSS-HDFS
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
fs.oss.bucket.<OSS_HDFS_BUCKET>.accessKeyId =  <OSS_HDFS_ACCESSKEY_ID>   
fs.oss.bucket.<OSS_HDFS_BUCKET>.accessKeySecret = <OSS_HDFS_ACCESSKEY_SECRET>
fs.oss.bucket.<OSS_HDFS_BUCKET>.endpoint = <OSS_HDFS_ENDPOINT>   
fs.oss.bucket.<OSS_HDFS_BUCKET>.data.lake.storage.enable = true                                                        
```

* <OSS_HDFS_BUCKET> : 需要访问的 OSS-HDFS 的 bucket
* <OSS_HDFS_ENDPOINT> : 需要访问的 OSS-HDFS 的 endpoint
* <OSS_HDFS_ACCESSKEY_ID> : 需要访问的 OSS-HDFS 的 AccessKeyId
* <OSS_HDFS_ACCESSKEY_SECRET> : 需要访问的 OSS-HDFS 的 AccessKeySecret

2、添加环境变量
```shell 
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>
```
* <JINDOSDK_CFG_DIR> : jindosdk.cfg 配置文件所在的绝对路径，比如 /etc/

3、使用 jindo-util 访问 OSS-HDFS 服务
```shell
jindo fs -ls oss://<bucket>/<dir>
```