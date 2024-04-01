# JindoFS 命令行工具使用指南

## 介绍

JindoFS 命令行工具是访问 OSS-HDFS 的可执行程序，功能类似于阿里云 OSS 提供的 ossutil。既支持常用的文件元数据和读写流操作，如创建目录、拷贝文件；也支持 OSS-HDFS 特有的方法，如清单导出等。采用了 Native 实现，目标是对 OSS-HDFS 提供高效易用的命令行支持。

与 JindoSDK 提供的命令行工具相比，JindoFS 命令行工具不仅提供对标 Hdfs shell 的常用命令，还提供 OSS-HDFS 的特有方法。然而，JindoFS 命令行工具只支持 OSS-HDFS，主要作为 OSS-HDFS 的特有工具使用。JindoSDK 的命令行工具参见 [《Jindo CLI 使用指南》](../jindosdk/jindosdk_cli_options.md)

## 获取

目前只提供了 Linux 版本的命令行工具，[下载链接](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.2.5/jindofs-sdk-6.2.5-linux.tar.gz)

下载后解压缩，在 `bin/` 目录下即可找到命令行工具的二进制文件，文件名为 `jindofs`。

## 配置

JindoFS 命令行工具使用 `JINDOSDK_CONF_DIR` 为环境变量。使该变量指向一个本地目录，在目录下放置配置文件 `jindofs.cfg`，便可以为命令行工具提供配置参数。`jindofs.cfg` 的内容示例如下：
```text
[client]
fs.oss.accessKeyId = <access key>
fs.oss.accessKeySecret = <access secret>
fs.oss.endpoint = <OSS-HDFS endpoint>
fs.oss.data.lake.storage.enable = true
# fs.oss.provider.endpoint = ECS_ROLE  # EMR Credential provider for password-free access
# fs.oss.provider.format = JSON
```

在解压后的 `conf/` 目录下也可以找到配置示例文件 `jindofs.cfg.template`。

## 用法

在命令行中执行 `jindofs` 二进制文件（例如，`cd` 至所在目录，执行 `./jindofs` 或者 `./jindofs -help`），将显示所有支持的命令：
```shell
jindofs
        [-help]
        [-version]
jindofs admin
        [-help]
        [-addProxyUser -dlsUri <path> -proxyUser <value> -users|-groups <value> -hosts <value>] 
        [-allowSnapshot -dlsUri <path>]
        [-addUserGroupsMapping -dlsUri <path> -user <value> -groups <value>]
        [-disallowSnapshot -dlsUri <path>]
        [-deleteProxyUser -dlsUri <path> -proxyUser <value>]
        [-deleteUserGroupsMapping -dlsUri <path> -user <value>]
        [-dumpMetaInfo -dlsUri <path>]
        [-dumpInventory <path>]
        [-getJobProgress <jobId> -dlsUri <path>]
        [-listJobs -dlsUri <path> [-stages <stages>] [-createTime <createTime>] [-type <type>]]
        [-listProxyUsers -dlsUri <path> [-maxKeys <value>] [-marker <value>]]
        [-listUserGroupsMappings -dlsUri <path> [-maxKeys <value>] [-marker <value>]]
        [-snapshotDiff -dlsUri <path> -fromSnapshot <value> -toSnapshot <value>]
        [-setRootPolicy <dlsRootPath> <accessRootPath>]
        [-unsetRootPolicy <dlsRootPath> <accessRootPath>]
        [-listAccessPolicies <dlsRootPath>]
        [-putConfig -dlsUri <path> -conf <key1=value1> -conf <key2=value2> ...]
        [-getConfig -dlsUri <path> -name <keys>]
jindofs fs
        [-help]
        [-cat <path>]
        [-count [-h] <path>]
        [-cp [-f] <src> <dst>]
        [-checksum [-mode COMPOSITE_CRC(default)/MD5MD5CRC] [-blockSize <size in bytes (only used in MD5MD5CRC mode, 128MB by default)>] <src> ...]
        [-chgrp [-R] <group> <path>]
        [-chmod [-R] <mode> <path>]
        [-chown [-R] [owner][:[group]] <path>]
        [-copyFromLocal [-f] <localsrc> <dst>]
        [-copyToLocal [-f] <src> <localdst>]
        [-createSnapshot <snapshotDir> [<snapshotName>]]
        [-checkStoragePolicy -path <path>]
        [-deleteSnapshot <snapshotDir> <snapshotName>]
        [-du [-s] [-h] <path> ...]
        [-expunge [-immediate] <path>]
        [-get [-f] <src> <localdst>]
        [-getfacl [-R] <path>]
        [-getfattr [-R] {-n name | -d} <path>]
        [-getStoragePolicy -path <path>]
        [-ls [-R] <path>]
        [-listPolicies]
        [-mkdir [-p] <path>]
        [-mv <src> ... <dst>]
        [-moveFromLocal <localsrc> ... <dst>]
        [-moveToLocal <src> <localdst>]
        [-put [-f] <localsrc> <dst>]
        [-rm [-skipTrash] [-f] [-r|-R] [-safely] <path>]
        [-rmdir <path>]
        [-test -[defsz] <path>]
        [-renameSnapshot <snapshotDir> <oldName> <newName>]
        [-stat [format] <path>]
        [-setStoragePolicy -path <path> -policy <policy> [-restoreDays <restoreDays>]]
        [-setfacl [-R] [{-b|-k} {-m|-x <acl_spec>} <path>]|[--set <acl_spec> <path>]]
        [-setfattr {-n name [-v value] | -x name} <path>]
        [-touchz <path>]
        [-truncate [-w] <length> <path>]
        [-unsetStoragePolicy -path <path>]
```

另外，所有命令均支持 `--extraConf key=value` 的方式提供配置参数，仅对该次执行有效，效果等同于写在配置文件中。

## 示例

#### 上传文件

```shell
./jindofs fs -put /local/file/path oss://<bucket-name>.<oss-hdfs-endpoint>/remote/file/path
```

作用是把本地文件 `/local/file/path` 上传到远端路径 `oss://<bucket-name>.<oss-hdfs-endpoint>/remote/file/path`。

#### 清单导出

```shell
./jindofs admin -dumpInventory oss://<bucket-name>.<oss-hdfs-endpoint>/  --extraConf fs.oss.accessKeyId=<access key>  --extraConf fs.oss.accessKeySecret=<access secret>
```

上述命令向 OSS-HDFS 服务发起了一次清单导出任务，并且还通过 `--extraConf key=value` 的方式为本次执行提供了额外的两条配置信息。JindoFS 的所有命令均支持 `--extraConf` 参数。

如果命令成功执行，将返回：
```shell
=============Dump Inventory=============
Job Id: <job id>
Data Location: <oss-hdfs path to inventory file>
.......................
FINISHED.
```

随后可在 Data Location 指向的 OSS-HDFS 路径找到清单导出的结果文件，按普通文件访问即可。
