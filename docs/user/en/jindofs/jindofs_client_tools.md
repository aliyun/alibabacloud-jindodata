## JindoFS Command-Line Tool User Guide

### Introduction

The JindoFS command-line tool is an executable program for accessing OSS-HDFS with functionality similar to Alibaba Cloud's ossutil. It supports common operations on file metadata and read/write streams, such as creating directories and copying files, as well as OSS-HDFS-specific methods like export listings. Implemented natively, its goal is to provide efficient and user-friendly command-line support for OSS-HDFS.

Compared to the JindoSDK command-line tool, JindoFS not only offers commands comparable to Hdfs shell but also includes unique features of OSS-HDFS. However, it is designed specifically for OSS-HDFS and serves as a specialized tool for that purpose. For information about the JindoSDK command-line tool, refer to the [Jindo CLI User Guide](../jindosdk/jindosdk_cli_options.md).

### Acquisition

Currently, only a Linux version of the command-line tool is available. You can download it from the [link](./jindofs_cli_download.md). After downloading and extracting the archive, you'll find the binary file named `jindofs` in the `bin/` directory.

### Configuration

The JindoFS command-line tool uses the `JINDOSDK_CONF_DIR` environment variable. Set this variable to point to a local directory containing the configuration file `jindofs.cfg`, which provides parameters for the tool. Here's an example `jindofs.cfg` content:
```text
[client]
fs.oss.accessKeyId = <access key>
fs.oss.accessKeySecret = <access secret>
fs.oss.endpoint = <OSS-HDFS endpoint>
fs.oss.data.lake.storage.enable = true
# fs.oss.provider.endpoint = ECS_ROLE  # EMR Credential provider for password-free access
# fs.oss.provider.format = JSON
```
A sample configuration file `jindofs.cfg.template` can be found in the `conf/` directory after extraction.

### Usage

Execute the `jindofs` binary (e.g., `./jindofs` or `./jindofs -help` from the directory) to display all supported commands:
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
All commands support providing configuration parameters via `--extraConf key=value`, valid only for the current execution, equivalent to specifying them in the config file.

### Examples

#### Uploading a File

```shell
./jindofs fs -put /local/file/path oss://<bucket-name>.<oss-hdfs-endpoint>/remote/file/path
```
This command uploads the local file `/local/file/path` to the remote location `oss://<bucket-name>.<oss-hdfs-endpoint>/remote/file/path`.

#### Exporting a Listing

```shell
./jindofs admin -dumpInventory oss://<bucket-name>.<oss-hdfs-endpoint>/  --extraConf fs.oss.accessKeyId=<access key>  --extraConf fs.oss.accessKeySecret=<access secret>
```
This command initiates an inventory export task in OSS-HDFS and supplies additional configuration parameters using `--extraConf`. If successful, the output will resemble:
```shell
=============Dump Inventory=============
Job Id: <job id>
Data Location: <oss-hdfs path to inventory file>
.......................
FINISHED.
```
You can then access the exported inventory file at the Data Location indicated path using standard file access methods.