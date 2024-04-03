Use Jindo commands
(This topic applies to JindoSDK 4.5.0 or later.)
Download the Jindo CLI package (supported only in Linux environments)
1. Download the [jindosdk-4.6.12.tar.gz](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/jindodata_download.md) package.
2. Decompress the package.
tar -zxvf jindosdk-4.6.12.tar.gz
3. Find the binary file jindo-util in the /jindosdk-4.6.12/bin/ directory.
chmod 700 jindo-util
mv jindo-util jindo
4. View all Jindo commands.
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
Use Jindo commands to access OSS or OSS-HDFS
Global configurations
1. Create a configuration file named jindosdk.cfg.
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

- <ENDPOINT>: the endpoint of Object Storage Service (OSS) or OSS-HDFS.
- <ACCESSKEY_ID>: the AccessKey ID that is used to access OSS or OSS-HDFS.
- <ACCESSKEY_SECRET>: the AccessKey secret that is used to access OSS or OSS-HDFS.

2. Add an environment variable.
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>

- <JINDOSDK_CFG_DIR>: the absolute path of the jindosdk.cfg configuration file. Example: /etc/.

3. Run the following Jindo command to access OSS.
./jindo fs -ls oss://<bucket>/<dir>
Bucket-level configurations
1. Create a configuration file named jindosdk.cfg.
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

- <ENDPOINT>: the endpoint of OSS or OSS-HDFS.
- <ACCESSKEY_ID>: the AccessKey ID that is used to access OSS or OSS-HDFS.
- <ACCESSKEY_SECRET>: the AccessKey secret that is used to access OSS or OSS-HDFS.

2. Add an environment variable.
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>

- <JINDOSDK_CFG_DIR>: the absolute path of the jindosdk.cfg configuration file. Example: /etc/.

3. Run the following Jindo command to access OSS-HDFS:
./jindo fs -ls oss://<bucket>/<dir>
Run jindo sync commands to synchronize data
1. Create a configuration file named jindosdk.cfg.
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

- <ENDPOINT>: the endpoint of OSS or OSS-HDFS.
- <ACCESSKEY_ID>: the AccessKey ID that is used to access OSS or OSS-HDFS.
- <ACCESSKEY_SECRET>: the AccessKey secret that is used to access OSS or OSS-HDFS.

2. Add an environment variable.
export JINDOSDK_CONF_DIR=<JINDOSDK_CFG_DIR>

- <JINDOSDK_CFG_DIR>: the absolute path of the jindosdk.cfg configuration file. Example: /etc/.

3. Run the following Jindo command to transmit data:
./jindo fs -sync -thread 10 /local/dir/ oss://<bucket>/<dir>

- thread: the number of threads that you use.

4. Run the following command to enable resumable upload:
./jindo fs -sync -update -thread 10 /local/dir/ oss://<bucket>/<dir>

- During resumable upload, the system first finds the files that failed to be uploaded. This may affect the performance of data transmission.

