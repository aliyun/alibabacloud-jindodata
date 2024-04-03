Use JindoFuse to access Alibaba Cloud OSS-HDFS (JindoFS)
Alibaba Cloud OSS-HDFS (JindoFS) uses JindoFuse to provide POSIX support. JindoFuse allows you to mount OSS-HDFS objects to an on-premises file system. This way, you can perform operations on OSS-HDFS objects in the same manner as you perform operations on files in the on-premises file system. 
Basic usage
Prepare an environment
# CentOS
yum install -y fuse3 fuse3-devel
# Debian
apt install -y fuse3 libfuse3-dev
Configure a client
Configure a directory

- Configure the JINDOSDK_CONF_DIR parameter to specify the directory in which you want to store a configuration file.

The following example shows how to configure the /usr/lib/jindosdk-4.6.12/conf directory as the storage directory of the file jindosdk.cfg.
export JINDOSDK_CONF_DIR=/usr/lib/jindosdk-4.6.12/conf
Prepare a configuration file
Prepare a configuration file in the .ini format. In this example, the name of the file is jindosdk.cfg. The following code shows the configuration items in the file:
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# Configure the endpoint of the bucket for which OSS-HDFS is enabled. For example, if the bucket is created in the China (Hangzhou) region, the endpoint is cn-hangzhou.oss-dls.aliyuncs.com. 
fs.oss.endpoint = <your_endpoint>
# Configure the AccessKey ID and AccessKey secret that you use to access OSS-HDFS. The AccessKey pair of an Alibaba Cloud account has permissions on all API operations. Using these credentials to perform operations in OSS-HDFS is a high-risk operation. We recommend that you use a RAM user to call API operations or perform routine O&M. To create a RAM user, log on to the RAM console. 
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
Access OSS-HDFS without a password
Prerequisites: An Alibaba Cloud Elastic Compute Service (ECS) instance is used and is assigned a required role. Sample code:
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# Configure the endpoint of the bucket for which OSS-HDFS is enabled. For example, if the bucket is created in the China (Hangzhou) region, the endpoint is cn-hangzhou.oss-dls.aliyuncs.com. 
fs.oss.endpoint = <your_endpoint>
fs.oss.provider.endpoint = ECS_ROLE
fs.oss.provider.format = JSON
Mount JindoFuse
After you configure JindoSDK, you can perform the following operations. 
Create a mount point
mkdir -p <mount_point>
Mount JindoFuse
jindo-fuse <mount_point> -ouri=[<oss_path>]
You must set the -ouri parameter to the OSS path that you want to map. The path can be the root directory or a subdirectory of the bucket. After you run the command, a daemon process starts in the background to mount the OSS path that is specified by the oss_path parameter to the mount point that is specified by the mount_point parameter for the on-premises file system. 
Access JindoFuse
If you mount OSS-HDFS to an on-premises directory, you can run the following commands to perform related operations in JindoFuse. In this example, OSS-HDFS is mounted to /mnt/oss/. 

- List all directories in /mnt/oss/
- ls /mnt/oss/
- Create a directory
- mkdir /mnt/oss/dir1
- ls /mnt/oss/
- Write data to a file
- echo "hello world" > /mnt/oss/dir1/hello.txt
- Read data from a file
- cat /mnt/oss/dir1/hello.txt

hello world is displayed. 

- Delete the directory
- rm -rf /mnt/oss/dir1/

Unmount JindoFuse
Run the following command to unmount a mount point:
umount <mount_point>
Implement automatic unmounting of JindoFuse
You can configure the -oauto_unmount parameter to implement automatic unmounting of a mount point. 
After you configure the -oauto_unmount parameter, you can run the killall -9 jindo-fuse command to send SIGINT to the jindo-fuse process. This way, JindoFuse is automatically unmounted before the process exits. 
Supported POSIX-based API operations
The following table describes the POSIX-based API operations that JindoFuse allows you to call.

| Operation | Description |
| --- | --- |
| getattr() | Queries the attributes of an object. This operation is similar to the ls command. |
| mkdir() | Creates a directory. This operation is similar to the mkdir command. |
| rmdir() | Deletes a directory. This operation is similar to the rm -rf command. |
| unlink() | Deletes a file. This operation is similar to the unlink command. |
| rename() | Renames a file or a directory. This operation is similar to the mv command. |
| read() | Reads data in sequence. |
| pread() | Reads data at random. |
| write() | Writes data in sequence. |
| pwrite() | Writes data at random. |
| flush() | Flushes data from the memory to the kernel cache. |
| fsync() | Flushes data from the memory to disks. |
| release() | Releases a file. |
| readdir() | Reads a directory. |
| create() | Creates a file. |
| open() O_APPEND | Opens a file in append mode. |
| open() O_TRUNC | Opens a file in overwrite mode. |
| ftruncate() | Truncates an opened file. |
| truncate() | Truncates an unopened file. This operation is similar to the truncate -s command. |
| lseek() | Specifies the read and write locations in an opened file.  |
| chmod() | Modifies the permissions on a file. This operation is similar to the chmod command. |
| access() | Queries the permissions on a file. |
| utimes() | Modifies the access time and modification time of a file. The new time must be later than the original time. |
| setxattr() | Modifies the xattr attributes of a file. |
| getxattr() | Queries the xattr attributes of a file. |
| listxattr() | Lists the xattr attributes of a file. |
| removexattr() | Removes the xattr attributes of a file. |
| lock() | Supports POSIX locks. This operation is similar to the fcntl command. |
| fallocate() | Pre-allocates physical space to a file. |
| symlink() | Creates a symbolic link. Symbolic links are available only in OSS-HDFS and do not support cache-based acceleration. |
| readlink() | Reads a symbolic link. |

Advanced usage
Mount parameters

| Parameter | Required | Description | Example |
| --- | --- | --- | --- |
| uri | ✓ | Specifies the OSS path that you want to map. The path can be the root directory or a subdirectory. Example: oss://bucket.endpoint/ or oss://bucket.endpoint/subdir.  | -ouri=oss://examplebucket.cn-beijing.oss-dls.aliyuncs.com/ |
| f |  | Starts a JindoFuse process in the foreground. By default, a daemon process is used to start the JindoFuse process in the background. If you use this option, we recommend that you enable terminal logs.  | -f |
| d |  | Enables the debug mode. If you enable the debug mode, the JindoFuse process starts in the foreground. If you use this option, we recommend that you enable terminal logs.  | -d |
| auto_unmount |  | Implements automatic unmounting of the mount point after the JindoFuse process exits.  | -oauto_unmount |
| ro |  | Mounts files from OSS-HDFS in read-only mode. After you enable this option, you cannot perform write operations.  | -oro |
| direct_io |  | Allows you to read and write files without the need for page cache.  | -odirect_io |
| kernel_cache |  | Optimizes the read performance by using the kernel cache.  | -okernel_cache |
| auto_cache |  | By default, the auto caching feature is enabled. You can configure the kernel_cache or auto_cache parameter. If the size or modification time of a file changes, this parameter does not take effect.  |  |
| entry_timeout |  |  Specifies the retention period of the cached directories. This parameter is used to optimize performance. Default value: 60. Unit: seconds. The value 0 indicates that the directories are not cached.  | -oentry_timeout=60 |
| attr_timeout |  |  Specifies the retention period of the cached file attributes. This parameter is used to optimize performance. Default value: 60. Unit: seconds. The value 0 indicates that the file attributes are not cached.  | -oattr_timeout=60 |
| negative_timeout |  |  Specifies the retention period of the cached file names that fail to be read. This parameter is used to optimize performance. Default value: 60. Unit: seconds. The value 0 indicates that the file names are not cached.  | -onegative_timeout=0 |
| jindo_entry_size |  |  The number of directories that are cached. This parameter is used to optimize readdir performance. Default value: 5000. The value 0 indicates that the directories are not cached.  | -ojindo_entry_size=5000 |
| jindo_attr_size |  |  The number of file attributes that are cached. This parameter is used to optimize getattr performance. Default value: 50000. The value 0 indicates that the attributes are not cached.  | -ojindo_attr_sizet=50000 |
| max_idle_threads |  |  The maximum number of idle threads. Default value: 10.  | -omax_idle_threads=10 |
| metrics_port |  |  Enables the HTTP port to return metrics, such as [http://localhost:9090/brpc_metrics](http://localhost:9090/brpc_metrics). Default value: 9090. | -ometrics_port=9090 |
| enable_pread |  | Calls the pread operation to read files.  | -oenable_pread |

Configuration parameters

| Parameter | Default value | Configuration section | Description |
| --- | --- | --- | --- |
| logger.dir | /tmp/fuse-log | common | Specifies the log directory. If the specified log directory does not exist, the directory is automatically created. |
| logger.sync | false | common | Specifies whether to export logs in synchronous mode. The value false indicates that the logs are exported in asynchronous mode. |
| logger.consolelogger | false | common | Specifies whether to display logs on the terminal. |
| logger.level | 2 | common | Returns logs whose levels are higher than or equal to the value of this parameter. If you enable terminal logs, the valid values for log levels range from 0 to 6. The values indicate TRACE, DEBUG, INFO, WARN, ERROR, CRITICAL, and OFF respectively. If you disable terminal logs and use file logs, a log level that is lower than or equal to 1 indicates WARN, and a log level that is higher than 1 indicates INFO.  |
| logger.verbose | 0 | common | Returns Verbose logs whose levels are higher than or equal to the value of this parameter. Valid values: 0 to 99. The value 0 indicates that no Verbose logs are returned. |
| logger.cleaner.enable | false | common | Specifies whether to enable log cleanup. |
| fs.oss.endpoint |  | jindosdk | Specifies the endpoint that is used to access OSS-HDFS. Example: cn-hangzhou.oss-dls.aliyuncs.com. |
| fs.oss.accessKeyId |  | jindosdk | The AccessKey ID that is used to access OSS-HDFS. |
| fs.oss.accessKeySecret |  | jindosdk | The AccessKey secret that is used to access OSS-HDFS. |

For more information, see [Documentation](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_configuration_list_ini.md). 

- You can specify both JindoSDK configuration parameters and mount parameters when you mount JindoFuse. The parameters that you configure when you mount JindoFuse have a higher priority than those in the configuration file. Example:

jindo-fuse <mount_point> -ouri=[<oss_path>] -ofs.oss.endpoint=[<your_endpoint>] -ofs.oss.accessKeyId=[<your_key_id>] -ofs.oss.accessKeySecret=[<your_key_secret>]
FAQ
Input/Output error
If you use JindoSDK to call API operations and the API operations fail, you can receive detailed error messages. If you call JindoFuse API operations and the API operations fail, the system sends you only error messages that are preset in the operating system. Sample common errors:
$ ls /mnt/oss/
ls: /mnt/oss/: Input/output error
To identify the cause of an error, view the jindosdk.log file in the path that is specified by the logger.dir parameter of JindoSDK. 
For example, the following error message in the jindosdk.log file indicates that a common authentication error occurs. 

EMMDD HH:mm:ss jindofs_connectivity.cpp:13] Please check your Endpoint/Bucket/RoleArn. Failed test connectivity, operation: mkdir, errMsg:  [RequestId]: 618B8183343EA53531C62B74 [HostId]: oss-cn-shanghai-internal.aliyuncs.com [ErrorMessage]: [E1010]HTTP/1.1 403 Forbidden ...

