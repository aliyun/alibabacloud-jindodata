# Jindo CLI 参数说明

# Jindo CLI 参数说明

## 用法汇总

```
    Usage:
    	jindo fs
    		[-appendToFile <localsrc> ... <dst>]
    		[-archive [-i] [-c] <path>]
    		[-cat <src> ...]
    		[-count <path> ...]
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
    	jindo benchmark
    		[-io -read -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-memCheck]]
    		[-io -pread -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-seekTimes <value>] [-memCheck]]
    		[-io -write -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-fileSize <value>] [-memCheck]]
    		[-meta -mkdir -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-memCheck]]
    		[-meta -listDirectory -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
    		[-meta -getFileStatus -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
    		[-meta -renameFile -basePath <path> [-n <value>] [-c <value>] [-d <value>] [-entryNum <value>] [-memCheck]]
```

## FS 子命令

## ADMIN 子命令

## BENCHMARK 子命令