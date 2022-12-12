# Jindo CLI 使用指南

## 用法汇总

```shell
    Usage:
	jindo fs
		[-archive [-i] [-c] <path>]
		[-cat <src> ...]
		[-count [-h] <path> ...]
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
		[-du [-s] <path> ...]
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
		[-test -[defsz] <path>]
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
		[-addProxyUser -dlsUri <path> -proxyUser <value> -users|-groups <value> -hosts <value>] 
		[-allowSnapshot -dlsUri <path>]
		[-fsck -dlsUri <path>]
		[-addUserGroupsMapping -dlsUri <path> -user <value> -groups <value>]
		[-disallowSnapshot -dlsUri <path>]
		[-deleteProxyUser -dlsUri <path> -proxyUser <value>]
		[-deleteUserGroupsMapping -dlsUri <path> -user <value>]
		[-dumpFile -dlsUri <path>]
		[-dumpInventory <path>]
		[-dumpBlockFiles -in <path> -out <path>]
		[-listProxyUsers -dlsUri <path> [-maxKeys <value>] [-marker <value>]]
		[-listUserGroupsMappings -dlsUri <path> [-maxKeys <value>] [-marker <value>]]
		[-snapshotDiff -dlsUri <path> -fromSnapshot <value> -toSnapshot <value>]
		[-setRootPolicy <dlsRootPath> <accessRootPath>]
		[-unsetRootPolicy <dlsRootPath> <accessRootPath>]
		[-listAccessPolicies <dlsRootPath>]
```

## FS 子命令

### archive

```shell
jindo fs -archive oss://<bucket>/<dir>
```
将 OSS 上文件设置为归档类型，可选参数：

| 参数    | Description  |
| ---  | --- |
| -i | ia 低频|
| -c | coldArchive 冷归档|
```shell
jindo fs -archive -i oss://<bucket>/<dir>
```
```shell
jindo fs -archive -c oss://<bucket>/<dir>
```
适用范围：标准OSS

### cat
```shell
jindo fs -cat oss://<bucket>/<dir>
```
显示文本内容，无可选参数
适用范围：标准OSS、OSS-HDFS

### count
显示文件大小以及文件数量，可选参数：
| 参数    | Description  |
| ---  | --- |
| -h | 显示文件大小单位|

```shell
jindo fs -count -h oss://<bucket>/<dir>
```

适用范围：标准OSS、OSS-HDFS

### cp
复制文件，可选参数：
| 参数    | Description  |
| ---  | --- |
| -f | 强制覆盖|
```shell
jindo fs -cp -f oss://<bucket>/<sourcedir> oss://<bucket>/<targetdir>
```
适用范围：标准OSS、OSS-HDFS

### checksum
计算文件的CRC32 CheckSum大小
```shell
jindo fs -checksum oss://<bucket>/file
```
适用范围：OSS-HDFS

### chgrp
改变文件的所属群组
```shell
jindo fs -chgrp <groupname> oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### chmod
改变文件的访问权限
```shell
jindo fs -chmod <mode> oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### chown
改变文件的所有人
```shell
jindo fs -chown <username> oss://<bucket>/<dir>
```
适用范围：OSS-HDFS

### copyFromLocal
从本地复制文件到oss，可选参数：
| 参数    | Description  |
| ---  | --- |
| -f | 强制覆盖|
```shell
jindo fs -copyFromLocal -f <localdir> oss://<bucket>/<targetdir>
```
适用范围：标准OSS、OSS-HDFS

### copyToLocal
从oss复制文件到本地，可选参数：
| 参数    | Description  |
| ---  | --- |
| -f | 强制覆盖|
```shell
jindo fs -copyToLocal -f oss://<bucket>/<targetdir> <localdir> 
```
适用范围：标准OSS、OSS-HDFS
### createSnapshot 
创建快照，使用前必须先允许快照功能
```shell
jindo fs -createSnapShot <snapshotDir> [<snapshotName>] 
```
适用范围：标准OSS、OSS-HDFS
### checkStoragePolicy
显示文件的存储策略
```shell
jindo fs -checkStoragePolicy -path oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### deleteSnapshot 
删除快照
```shell
jindo fs -deleteSnapshot oss://<bucket>/<dir> <snapshotName>
```
适用范围：OSS-HDFS
### du 
显示目录中所有文件的大小
可选参数：
| 参数    | Description  |
| ---  | --- |
| -s | 求目标文件夹的总和|
| -h | 标准单位显示|
```shell
jindo fs -du oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS

### get 
下载文件到本地，可选参数：
| 参数    | Description  |
| ---  | --- |
| -f | 强制覆盖|
```shell
jindo fs -get -f oss://<bucket>/<dir> <localdst>
```
适用范围：标准OSS、OSS-HDFS
### getfacl 
显示文件或目录的访问控制列表，可选参数：
| 参数    | Description  |
| ---  | --- |
| -R | 递归显示|
```shell
jindo fs -getfacl -R oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### getfattr
显示文件或者目录的扩展属性名称和值，可选参数：
| 参数    | Description  |
| ---  | --- |
| -R | 递归显示|
| -n | 按名称显示|
| -d | 显示所有|

```shell
jindo fs -getfattr [-R] {-n user.myAttr | -d} oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### getStoragePolicy 
显示存储策略
```shell
jindo fs -getStoragePolicy -path oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### ls 
列出目录下文件，可选参数：
| 参数    | Description  |
| ---  | --- |
| -R | 递归显示|
```shell
jindo fs -ls [-R] oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### listPolicies
显示支持的存储策略
```shell
jindo fs -listPolicies
```
适用范围：OSS-HDFS
### load 
数据缓存命令可以备份对应路径的数据至本集群的磁盘，以便于后续可以读取本地数据，无需读取OSS等后端上的数据，可选参数有。
| 参数 | 说明 |
| --- | --- |
| -s | 表示缓存过程同步执行，打印进度和执行信息，推荐开启 |
| -replica <num> | 缓存副本数量，默认缓存1个副本 |
| -R | 递归缓存文件，当 path 是文件夹时开启 |

```shell
jindo fs -load [-meta] [-data] [-s] [-m] [-R] [-replica value] oss://<bucket>/<dir>
```
### mkdir 
创建文件夹
```shell
jindo fs -mkdir oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### mv 
移动文件
```shell
jindo fs -mv oss://<bucket>/<srcdir> oss://<bucket>/<dstdir>
```
适用范围：标准OSS、OSS-HDFS
### moveFromLocal 
将文件从本地移动到远程路径上，，移动完成后会删除本地文件
```shell
jindo fs -moveFromLocal <localsrc> oss://<bucket>/<dstdir>
```
适用范围：标准OSS、OSS-HDFS
### moveToLocal 
将文件从远程路径上移动到本地，移动完成后会OSS上文件
```shell
jindo fs -moveToLocal oss://<bucket>/<srcdir>  <localdst>
```
适用范围：标准OSS、OSS-HDFS
### metaDiff
使用缓存加速系统后，显示本地元数据和远端的同步情况
```shell
jindo fs -metaDiff oss://<bucket>/<srcdir>
```
适用范围：标准OSS、OSS-HDFS

### put 
将本地文件复制到远程目录，可选参数：
| 参数    | Description  |
| ---  | --- |
| -f | 强制覆盖|
```shell
jindo fs -put -f <localsrc> oss://<bucket>/<targetdir>
```
适用范围：标准OSS、OSS-HDFS
### rm 
删除远程路径上的文件，可选参数：
| 参数    | Description  |
| ---  | --- |
| -f | 强制删除|
| -r/-rmr/-R | 递归删除|
```shell
jindo fs -rm -f oss://<bucket>/<dir>
```
```shell
jindo fs -rm -R oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### rmdir 
删除文件夹
```shell
jindo fs -rmdir oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### test
基本功能检测：
| 参数    | Description  |
| ---  | --- |
| -d | 是否是文件夹|
| -e | 是否存在路径|
| -f | 是否是文件|
| -s | 是否为空文件夹|
| -z | 文件长度是否为0|
```shell
jindo fs -test -d oss://<bucket>/<dir>/file
```
适用范围：标准OSS、OSS-HDFS
### renameSnapshot
为快照重命名
```shell
jindo fs -renameSnapshot oss://<bucket>/<dir> <oldName> <newName>
```
适用范围：OSS-HDFS
### restore 
将文件从归档状态恢复，冷归档文件和归档文件必须恢复后才能unarchive，必填参数：
| 参数    | Description  |
| ---  | --- |
| -days | 解冻的时长|
```shell
jindo fs -restore -days value oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### stat
显示文件状态
```shell
jindo fs -stat oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### setStoragePolicy
设置存储策略
```shell
jindo fs -setStoragePolicy -path oss://<bucket>/<dir> -policy <policy>
```
适用范围：OSS-HDFS
### setfacl 
设置文件访问策略，可选参数：
| 参数    | Description  |
| ---  | --- |
| -b | 删除所有访问策略|
| -k | 删除默认访问策略|
| -m | 按名称修改访问策略|
| -x | 按名称删除访问策略|
| -set | 设定访问策略| 
```shell
jindo fs -setfacl -b oss://<bucket>/<dir> 
```

```shell
jindo fs -setfacl -k oss://<bucket>/<dir> 
```
```shell
jindo fs -setfacl -m user:<username>:<acl> oss://<bucket>/<dir> 
```

```shell
jindo fs -setfacl -x user:<username> oss://<bucket>/<dir> 
```

```shell
jindo fs -setfacl -set user::<acl> oss://<bucket>/<dir> 
```
适用范围：OSS-HDFS

### setfattr 
设置文件或者目录的扩展属性名称和值，可选参数：
| 参数    | Description  |
| ---  | --- |
| -n | 按名称设置|
| -v | 设置属性值|
| -x | 按名称删除|

```shell
jindo fs -setfattr {-n name [-v value] | -x name} oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### sync
将本地文件上传到远程路径，可选参数：
| 参数    | Description  |
| ---  | --- |
| -update | 断点续传|
| -thread | 使用多线程|
```shell
jindo fs -sync [-update] [-thread thread_num] <localsrc> oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### touchz
生成一个大小为0的文件
```shell
jindo fs -touchz oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### truncate 
将文件裁剪到指定大小
```shell
jindo fs -truncate <length> oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### unarchive 
将文件恢复为标准文件，AR和COLD类型文件需要先restore
```shell
jindo fs -unarchive oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### uncache 
删除缓存系统的缓存文件
```shell
jindo fs -uncache oss://<bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS

### unsetStoragePolicy
取消存储策略
```shell
jindo fs -unsetStoragePolicy -path oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
## ADMIN 子命令
### mount 
挂载oss或oss-OSS-HDFS服务目录
```shell
jindo admin -mount <localdir> oss://<Bucket>/<dir>
```
适用范围：标准OSS、OSS-HDFS
### report
输出当前JindoFSx 存储加速系统的一些信息，比如缓存大小，缓存容量等。
```shell
jindo admin -report
```
适用范围：标准OSS、OSS-HDFS
### reportMetrics
输出当前JindoFSx 存储加速系统的一些metrics信息
```shell
jindo admin -reportMetrics
```
适用范围：标准OSS、OSS-HDFS

### umount 
取消挂载oss或oss-OSS-HDFS服务目录
```shell
jindo fsxadmin -unmount <localdir>
```
适用范围：标准OSS、OSS-HDFS
### addProxyUser
添加代理用户
```shell
jindo dlsadmin -addProxyUser -dlsUri oss://<bucket> -proxyUser <value> -users|-groups <value> -hosts <value>] 
```
适用范围：OSS-HDFS
### allowSnapshot 
允许使用快照
```shell
jindo admin -allowSnapshot -dlsUri oss://<bucket>/<dir>
```
适用范围：OSS-HDFS

### addUserGroupsMapping 
映射用户和组的关系
```shell
jindo admin -addUserGroupsMapping -dlsUri oss://<bucket> <path> -user <value> -groups <value>
```
适用范围：OSS-HDFS
### disallowSnapshot
不允许使用快照
```shell
jindo admin -disallowSnapshot -dlsUri oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### deleteProxyUser
删除代理用户
```shell
jindo admin -deleteProxyUser -dlsUri oss://<bucket>/<dir> -proxyUser <value> -users|-groups <value> -hosts <value>] 
```
适用范围：OSS-HDFS
### deleteUserGroupsMapping 
删除用户和组的关系
```shell
jindo admin -deleteUserGroupsMapping -dlsUri oss://<bucket> -user <value>
```
适用范围：OSS-HDFS
### dumpFile
查看文件的详细存储信息
```shell
jindo dlsadmin -dumpFile -dlsUri oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### dumpInventory 
导出文件元数据
```shell
jindo dlsadmin -dumpInventory -dlsUri oss://<bucket>/<dir>
```
适用范围：OSS-HDFS
### dumpBlockFiles 
将指定文件夹下面所有文件信息下载到本地
```shell
jindo dlsadmin -dumpBlockFiles -in oss://<bucket>/<dir> -out <localdir>
```
适用范围：OSS-HDFS
### listProxyUsers 
列出代理用户
```shell
jindo admin -listProxyUsers -dlsUri oss://<bucket> [-maxKeys <value>] [-marker <value>]
```
适用范围：OSS-HDFS
### listUserGroupsMappings 
列出所有用户和组的关系
```shell
jindo admin -listUserGroupsMappings -dlsUri oss://<bucket> [-maxKeys <value>] [-marker <value>]
```
适用范围：OSS-HDFS
### snapshotDiff 
查看快照之间的差别
```shell
jindo admin -snapshotDiff -dlsUri oss://<bucket>/<dir> -fromSnapshot <value> -toSnapshot <value>
```
适用范围：OSS-HDFS
### setRootPolicy 
允许为 bucket 设置任意前缀的访问路径
```shell										
jindo admin -setRootPolicy <dlsRootPath> <accessRootPath>
```
适用范围：OSS-HDFS
### unsetRootPolicy
取消为 bucket 设置的任意前缀的访问路径
```shell
jindo admin -unsetRootPolicy <dlsRootPath> <accessRootPath>
```
适用范围：OSS-HDFS
### listAccessPolicies
查看当前 bucket 支持的所有访问路径。
允许为 bucket 设置任意前缀的访问路径
```shell
jindo admin -listAccessPolicies <dlsRootPath> 
```
适用范围：OSS-HDFS