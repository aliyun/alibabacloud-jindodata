# Alibaba Cloud OSS-HDFS Service (JindoFS Service) Metadata Export Instructions \(Supported since v4.6.0\)

## Overview

Using the metadata export feature, you can export a JSON-formatted inventory of file metadata from your current OSS-HDFS bucket to an OSS location for statistical analysis.

* Configure the JindoFS command-line tool with the access key for your OSS-HDFS bucket, as per the [JindoFS Command-Line Tool Usage Guide](usages/oss_jindo_cli.md).

* Execute the export command:
```bash
./jindofs admin -dumpInventory oss://<hdfs_bucket>/
```
During this process, observe the output path:
```bash
============Dump Inventory=============
Job Id: 2ce40fba-5704-45c4-8720-d92a891d5cfd
Data Location: oss://<oss_bucket>/.dlsdata/.sysinfo/meta_analyze/inventory/1666584461201.2ce40fba-5704-45c4-8720-d92a891d5cfd
.....................................................................................................................
FINISHED.
```
This command is blocking, so wait approximately 10 seconds to 10 minutes (depending on the amount of metadata) until it outputs `FINISHED`, indicating successful completion.

* Download the result file
  Use the [Jindo Command-Line Tool](usages/oss_jindo_cli.md) configured with regular OSS keys (not OSS-HDFS), [ossutil](https://help.aliyun.com/document_detail/50452.html), Hadoop's `fs` command, or download directly from the OSS console:

```bash
ossutil cp oss://<oss_bucket>/.dlsdata/.sysinfo/meta_analyze/inventory/1666584461201.2ce40fba-5704-45c4-8720-d92a891d5cfd ./
```
Save it locally and open it with vi/vim or another editor.

Here's an example of what the result might look like:
```json
{"id":16385,"path":"/","type":"directory","size":0,"user":"admin","group":"supergroup","atime":0,"mtime":1666581702933,"permission":511,"state":1}
{"id":6246684106789500068,"path":"/dls-1000326249","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}
{"id":6246684106789500069,"path":"/dls-1000326249/benchmark","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}
{"id":6246684106789500070,"path":"/dls-1000326249/benchmark/n1","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}
{"id":6246684106789500071,"path":"/dls-1000326249/benchmark/n1/490747449","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660895613953,"permission":511,"state":0}
```
Each entry represents a file or directory with its associated metadata properties such as ID, path, type, size, owner, group, access and modification times, permissions, and state.


### Advanced Instructions

#### 1. Specify metadata output fields\(Supported since v6.9.1\)

This function is used to specify the required file information fields. By default, all fields are output.

Usage：
```bash
## -field field : Specify metadata field
## path is a must field. You must specify another one or more fields.
## Optional fields : id type size user group atime mtime permission state storagePolicy storageConvertTime storageState
./jindofs admin -dumpInventory oss://<hdfs_bucket>/ -field path -field mtime
```

Here's an example of what the result might look like:
```json
{"path":"/","mtime":1666581702933}
{"path":"/dls-1000326249","mtime":1660889124590}
{"path":"/dls-1000326249/benchmark","mtime":1660889124590}
{"path":"/dls-1000326249/benchmark/n1","mtime":1660889124590}
{"path":"/dls-1000326249/benchmark/n1/490747449","mtime":1660895613953}
```
