# JindoFuse Usage Guide

## Overview

JindoFuse enables POSIX support for OSS/OSS-HDFS, allowing you to mount OSS/OSS-HDFS files locally as if they were part of the local file system.

## Deployment and Installation

Download the latest tar.gz package named `jindosdk-x.y.z.tar.gz` (where x.y.z represents the version), from the [download page](../jindosdk/jindosdk_download.md).

Follow the instructions in the [documentation link](../jindosdk/jindosdk_deployment_ai.md) to install and deploy JindoSDK.

## Mounting JindoFuse

After configuring JindoSDK:

1. Create a mount point with the command below:

```bash
mkdir -p <mountpoint>
```
2. Mount JindoFuse using:

```bash
jindo-fuse <mount_point> -ouri=<osspath>
```

This command starts a background daemon process that mounts `<oss_path>` to `<mount_point>` on the local file system.

Replace `<mount_point>` with a local path, and `<oss_path>` with the desired OSS/OSS-HDFS path, which can be either a bucket root directory or a subdirectory. For example: `oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir/`. Refer to [OSS/OSS-HDFS Quick Start](../oss/oss_quickstart.md) for root path examples.

The way to mount paths is similar between OSS and OSS-HDFS, except for slight differences in endpoints.

1. Verify successful mounting by checking that the jindo-fuse process exists and has the expected parameters:

```bash
ps -ef | grep jindo-fuse
```

## Accessing JindoFuse

Assuming you've mounted JindoFS service at `/mnt/oss/`, perform these operations:

1. List all directories in `/mnt/oss/`:

```bash
ls /mnt/oss/
```

2. Create a directory:

```bash
mkdir /mnt/oss/dir1
ls /mnt/oss/
```

3. Write a file:

```bash
echo "hello world" > /mnt/oss/dir1/hello.txt
```

4. Read the file:

```bash
cat /mnt/oss/dir1/hello.txt
```

It displays `hello world`.

5. Remove the directory:

```bash
rm -rf /mnt/oss/dir1/
```

## Unmounting JindoFuse

To unmount the previously mounted point, run:

```bash
umount <mount_point>
```

## Automatic Unmounting JindoFuse

Use the `-oauto_unmount` option to automatically unmount when the mount point is no longer needed.

With this parameter, sending `killall -9 jindo-fuse` sends a SIGINT signal to the jindo-fuse process, which will safely unmount before exiting.

# Feature Support

JindoFuse currently supports the following POSIX APIs:

| Feature | Description | OSS | OSS-HDFS |
| --- | --- | --- | --- |
| getattr() | Query file attributes (like ls) | ✅ | ✅ |
| mkdir() | Create directory (like mkdir) | ✅ | ✅ |
| rmdir() | Delete directory (like rm -rf) | ✅ | ✅ |
| unlink() | Delete file (like unlink) | ✅ | ✅ |
| rename() | Rename (like mv) | ✅ | ✅ |
| read() | Sequential read | ✅ | ✅ |
| pread() | Random read | ❌ | ✅ |
| write() | Sequential write | ✅ | ✅ |
| pwrite() | Random write | ❌ | ✅ |
| flush() | Flush memory to kernel buffer | Only for append-write opened files | ✅ |
| fsync() | Flush memory to disk | Only for append-write opened files | ✅ |
| release() | Close file | ✅ | ✅ |
| readdir() | Read directory | ✅ | ✅ |
| create() | Create file | ✅ | ✅ |
| open() O_APPEND | Open file for appending | Supported but with [usage restrictions](https://help.aliyun.com/document_detail/31981.html) | ✅ |
| open() O_TRUNC | Open file for overwrite | ✅ | ✅ |
| ftruncate() | Truncate an open file | ❌ | ✅ |
| truncate() | Truncate an unopened file (like truncate -s) | ❌ | ✅ |
| lseek() | Seek in an open file | ❌ | ✅ |
| chmod() | Modify file permissions (like chmod) | ❌ | ✅ |
| access() | Check file permissions | ✅ | ✅ |
| utimes() | Modify file timestamps | ❌ | ✅ |
| setxattr() | Modify file xattr properties | ❌ | ✅ |
| getxattr() | Get file xattr properties | ❌ | ✅ |
| listxattr() | List file xattr properties | ❌ | ✅ |
| removexattr() | Remove file xattr property | ❌ | ✅ |
| lock() | Support POSIX locks (like fcntl) | ❌ | ✅ |
| fallocate() | Preallocate physical space for file | ❌ | ✅ |
| symlink() | Create symbolic link | ❌ | Currently only used internally by OSS-HDFS; caching acceleration not supported |
| readlink() | Read symbolic link | ❌ | Currently only used internally by OSS-HDFS; caching acceleration not supported |

# Advanced Usage

## Mount Options

| Option Name | Required? | Version | Description | Example |
| --- | --- | --- | --- | --- |
| uri | Yes | 4.3.0+ | Configure the OSS path to map. Can be root or subdirectory. Ex: oss://examplebucket/ or oss://examplebucket/subdir/. | `-ouri=oss://examplebucket/` |
| f | No | 4.3.0+ | Run process in foreground. Defaults to running as a daemon. | `-f` |
| d | No | 4.3.0+ | Debug mode; runs in foreground. Recommended with terminal logging. | `-d` |
| auto_unmount | No | 4.3.0+ | Automatically umount upon fuse process exit. | `-oauto_unmount` |
| ro | No | 4.3.0+ | Mount as read-only; disallows write operations. | `-oro` |
| ro_mount | No | 6.10.3+ | Mount as read-only when configured. | `-oro_mount` |
| direct_io | No | 4.3.0+ | Bypass page cache during reads/writes. | `-odirect_io` |
| kernel_cache | No | 4.3.0+ | Use kernel cache for optimized read performance. | `-okernel_cache` |
| auto_cache | No | 4.3.0+ | Enabled by default; invalidates cache if file size or modification time changes. | N/A |
| entry_timeout | No | 4.3.0+ | File name cache retention time (seconds). 0 disables caching. | `-oentry_timeout=60` |
| attr_timeout | No | 4.3.0+ | File attribute cache retention time (seconds). 0 disables caching. | `-oattr_timeout=60` |
| negative_timeout | No | 4.3.0+ | Negative file lookup cache retention time (seconds). 0 disables caching. | `-onegative_timeout=60` |
| max_idle_threads | No | 4.3.0+ | Pool of idle threads to handle kernel callbacks. | `-omax_idle_threads=10` |
| xengine | No | 4.3.0+ | Enable caching. | `-oxengine` |
| pread | No | 4.5.1+ | Use random reads instead of sequential reads when enabled, suitable for scenarios where random reads far outnumber sequential ones. | `-opread` |
| no_symlink | No | 4.5.1+ | Disable symlink functionality. | `-ono_symlink` |
| no_writeback | No | 4.5.1+ | Disable writeback functionality. | `-ono_writeback` |
| no_flock | No | 4.5.1+ | Disable flock functionality. | `-ono_flock` |
| no_xattr | No | 4.5.1+ | Disable xattr functionality. | `-ono_xattr` |

# Configuration Options

| Setting | Default Value | Description |
| --- | --- | --- |
| logger.dir | /tmp/bigboot-log | Log directory; creates if it doesn't exist |
| logger.sync | false | Whether logs are written synchronously |
| logger.consolelogger | false | Print logs to terminal |
| logger.level | 2 | Output logs equal or greater than this level; levels range from 0-6: TRACE, DEBUG, INFO, WARN, ERROR, CRITICAL, OFF |
| logger.verbose | 0 | Output VERBOSE logs equal or greater than this level; levels range from 0-99; 0 means no output |
| logger.cleaner.enable | false | Enable log cleaning |
| fs.oss.endpoint | N/A | Address for JindoFS service, e.g., oss-cn-xxx.aliyuncs.com |
| fs.oss.accessKeyId | N/A | Access key ID required for JindoFS access |
| fs.oss.accessKeySecret | N/A | Access key secret required for JindoFS access |

For more options, see [Common Client Configurations](../jindosdk/jindosdk_configuration.md).

*   Supports specifying jindosdk configuration settings alongside mount options (options passed at mount time take precedence over configurations), e.g.:

```bash
jindo-fuse <mount_point> -ouri=<osspath> -ofs.oss.endpoint=<your_endpoint> -ofs.oss.accessKeyId=<your_key_id> -ofs.oss.accessKeySecret=<your_key_secret>
```

# Common Issues

## Input/Output Error

Unlike using JindoSDK APIs that provide detailed ErrorMsg, JindoFuse shows generic operating system errors such as:

```bash
$ ls /mnt/oss/
ls: /mnt/oss/: Input/output error
```

To troubleshoot specific issues, look in the log file located at the path specified by `logger.dir` (`jindosdk.log`). For instance, an authorization issue might appear as:

> EMMDD HH:mm:ss jindofs_connectivity.cpp:13] Please check your Endpoint/Bucket/RoleArn. Failed test connectivity, operation: mkdir, errMsg: [RequestId]: 618B8183343EA53531C62B74 [HostId]: oss-cn-shanghai-internal.aliyuncs.com [ErrorMessage]: [E1010]HTTP/1.1 403 Forbidden ...