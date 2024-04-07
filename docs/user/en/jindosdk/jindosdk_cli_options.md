# Jindo CLI User Guide

## Command Summary

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
		[-setfacl [-R] [{-b|-k} {-m|-x <acl_spec>} <path>|[--set <acl_spec> <path>]]
		[-setfattr {-n name [-v value] | -x name} <path>]
		[-sync [-update] [-thread thread_num] <localsrc> <dst>
		[-touchz <path> ...]
		[-truncate [-w] <length> <path> ...]
		[-unarchive [-i] [-a] <path>]
		[-uncache <path>]
		[-unsetStoragePolicy -path <path>]
```

## Subcommands for FS

### archive

```shell
jindo fs -archive oss://<bucket>/<dir>
```
Sets the file type to archived on OSS. Optional flags:

| Flag | Description |
| --- | --- |
| `-i` | IA (Infrequent Access) |
| `-c` | Cold Archive |

Examples:
```shell
jindo fs -archive -i oss://<bucket>/<dir>
```
```shell
jindo fs -archive -c oss://<bucket>/<dir>
```
Applies to: Standard OSS

### cat

```shell
jindo fs -cat oss://<bucket>/<dir>
```
Displays text content without options.

Applies to: Standard OSS, OSS-HDFS

### count

Shows file sizes and counts. Optional flag:

| Flag | Description |
| --- | --- |
| `-h` | Display sizes in human-readable format |

Example:
```shell
jindo fs -count -h oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### cp

Copies files with optional flag:

| Flag | Description |
| --- | --- |
| `-f` | Force overwrite |

Example:
```shell
jindo fs -cp -f oss://<bucket>/<sourcedir> oss://<bucket>/<targetdir>
```
Applies to: Standard OSS, OSS-HDFS

### checksum

Calculates CRC32 CheckSum size of a file.

Example:
```shell
jindo fs -checksum oss://<bucket>/file
```
Applies to: OSS-HDFS

### chgrp

Changes the file's group ownership.

Example:
```shell
jindo fs -chgrp <groupname> oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### chmod

Changes file access permissions.

Example:
```shell
jindo fs -chmod <mode> oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### chown

Changes the file's owner.

Example:
```shell
jindo fs -chown <username> oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### copyFromLocal

Copies files from local to OSS with optional flag:

| Flag | Description |
| --- | --- |
| `-f` | Force overwrite |

Example:
```shell
jindo fs -copyFromLocal -f <localdir> oss://<bucket>/<targetdir>
```
Applies to: Standard OSS, OSS-HDFS

### copyToLocal

Copies files from OSS to local with optional flag:

| Flag | Description |
| --- | --- |
| `-f` | Force overwrite |

Example:
```shell
jindo fs -copyToLocal -f oss://<bucket>/<targetdir> <localdir>
```
Applies to: Standard OSS, OSS-HDFS

### createSnapshot

Creates a snapshot before enabling snapshot functionality.

Example:
```shell
jindo fs -createSnapShot <snapshotDir> [<snapshotName>] 
```
Applies to: Standard OSS, OSS-HDFS

### checkStoragePolicy

Displays the storage policy of a file.

Example:
```shell
jindo fs -checkStoragePolicy -path oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### deleteSnapshot

Deletes a snapshot.

Example:
```shell
jindo fs -deleteSnapshot oss://<bucket>/<dir> <snapshotName>
```
Applies to: Standard OSS, OSS-HDFS

### du

Lists file sizes in a directory. Optional flags:

| Flag | Description |
| --- | --- |
| `-s` | Summarize sizes |
| `-h` | Human-readable format |

Example:
```shell
jindo fs -du oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### get

Downloads a file to local with optional flag:

| Flag | Description |
| --- | --- |
| `-f` | Force overwrite |

Example:
```shell
jindo fs -get -f oss://<bucket>/<dir> <localdst>
```
Applies to: Standard OSS, OSS-HDFS

### getfacl

Displays the ACL of a file or directory with optional flag:

| Flag | Description |
| --- | --- |
| `-R` | Recursively display |

Example:
```shell
jindo fs -getfacl -R oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### getfattr

Displays extended attributes of a file or directory with optional flags:

| Flag | Description |
| --- | --- |
| `-R` | Recursively display |
| `-n` | Display attribute by name |
| `-d` | Display all attributes |

Example:
```shell
jindo fs -getfattr [-R] {-n user.myAttr | -d} oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### getStoragePolicy

Displays the storage policy of a file.

Example:
```shell
jindo fs -getStoragePolicy -path oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### ls

Lists files in a directory. Optional flag:

| Flag | Description |
| --- | --- |
| `-R` | Recursively list |

Example:
```shell
jindo fs -ls [-R] oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### listPolicies

Lists supported storage policies.

Example:
```shell
jindo fs -listPolicies
```
Applies to: OSS-HDFS

### load

Loads data from paths into disk cache. Optional flags include:

| Flag | Description |
| --- | --- |
| `-s` | Synchronous execution (recommended) |
| `-replica <num>` | Number of replicas to cache (default: 1) |
| `-R` | Recursively cache directories |

Example:
```shell
jindo fs -load [-meta] [-data] [-s] [-m] [-R] [-replica value] oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### mkdir

Creates a directory.

Example:
```shell
jindo fs -mkdir oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### mv

Moves files.

Example:
```shell
jindo fs -mv oss://<bucket>/<srcdir> oss://<bucket>/<dstdir>
```
Applies to: Standard OSS, OSS-HDFS

### moveFromLocal

Moves files from local to remote, deleting them locally after transfer.

Example:
```shell
jindo fs -moveFromLocal <localsrc> oss://<bucket>/<dstdir>
```
Applies to: Standard OSS, OSS-HDFS

### moveToLocal

Moves files from remote to local, deleting them remotely after transfer.

Example:
```shell
jindo fs -moveToLocal oss://<bucket>/<srcdir>  <localdst>
```
Applies to: Standard OSS, OSS-HDFS

### metaDiff

Compares metadata between cached and remote data.

Example:
```shell
jindo fs -metaDiff oss://<bucket>/<srcdir>
```
Applies to: Standard OSS, OSS-HDFS

### put

Uploads local files to remote directory with optional flag:

| Flag | Description |
| --- | --- |
| `-f` | Force overwrite |

Example:
```shell
jindo fs -put -f <localsrc> oss://<bucket>/<targetdir>
```
Applies to: Standard OSS, OSS-HDFS

### rm

Deletes files. Optional flags:

| Flag | Description |
| --- | --- |
| `-f` | Force deletion |
| `-r` or `-rmr` or `-R` | Recursive deletion |

Example:
```shell
jindo fs -rm -f oss://<bucket>/<dir>
```
```shell
jindo fs -rm -R oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### rmdir

Deletes a directory.

Example:
```shell
jindo fs -rmdir oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### test

Performs basic file tests. Flags include:

| Flag | Description |
| --- | --- |
| `-d` | Tests if it's a directory |
| `-e` | Tests if it exists |
| `-f` | Tests if it's a file |
| `-s` | Tests if it's an empty directory |
| `-z` | Tests if its length is zero |

Example:
```shell
jindo fs -test -d oss://<bucket>/<dir>/file
```
Applies to: Standard OSS, OSS-HDFS

### renameSnapshot

Renames a snapshot.

Example:
```shell
jindo fs -renameSnapshot oss://<bucket>/<dir> <oldName> <newName>
```
Applies to: Standard OSS, OSS-HDFS

### restore

Restores an archived or cold file back to standard status. Requires `-days` parameter.

Example:
```shell
jindo fs -restore -days value oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### stat

Displays file status.

Example:
```shell
jindo fs -stat oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### setStoragePolicy

Sets the storage policy for a file or directory.

Example:
```shell
jindo fs -setStoragePolicy -path oss://<bucket>/<dir> -policy <policy>
```
Applies to: OSS-HDFS

### setfacl

Modifies file access control lists with various options:

| Flag | Description |
| --- | --- |
| `-b` | Deletes all ACL entries |
| `-k` | Deletes default ACL entries |
| `-m` | Modifies an ACL entry by name |
| `-x` | Deletes an ACL entry by name |
| `-set` | Sets an ACL entry |

Example:
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
Applies to: OSS-HDFS

### setfattr

Sets or deletes extended attributes of a file or directory with various options:

| Flag | Description |
| --- | --- |
| `-n` | Sets attribute by name |
| `-v` | Sets attribute value |
| `-x` | Deletes an attribute by name |

Example:
```shell
jindo fs -setfattr {-n name [-v value] | -x name} oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### sync

Syncs local or remote paths to another remote path. Optional flags include:

| Flag | Description |
| --- | --- |
| `-update` | Skip files newer than source files |

Example:
```shell
jindo fs -sync [-update] -thread thread_num <localsrc> oss://<bucket>/<dir>
```

```shell
jindo fs -sync [-update] -thread thread_num oss://<bucket>/<dir1> oss://<bucket>/<dir2>
```
Applies to: Standard OSS, OSS-HDFS

### touchz

Creates an empty file.

Example:
```shell
jindo fs -touchz oss://<bucket>/<dir>
```
Applies to: Standard OSS, OSS-HDFS

### truncate

Truncates a file to specified length.

Example:
```shell
jindo fs -truncate <length> oss://<bucket>/<dir>
```
Applies to: OSS-HDFS

### unarchive

Reverts an archived or cold file back to standard status.

Example:
```shell
jindo fs -unarchive oss://<bucket>/<dir>
```
Applies To: Standard OSS, OSS-HDFS

### uncache

Removes cached files from the caching system.
```shell
jindo fs -uncache oss://<bucket>/<dir>
```
Applies To: Standard OSS, OSS-HDFS

### unsetStoragePolicy

Resets the storage policy.
```shell
jindo fs -unsetStoragePolicy -path oss://<bucket>/<dir>
```
Applies To: OSS-HDFS