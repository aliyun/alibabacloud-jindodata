# 使用 JindoFuse 访问 JindoFS 服务

JindoFS 服务（OSS-HDFS 服务）通过 JindoFuse 提供 POSIX 支持。JindoFuse 可以把 JindoFS 服务上的文件挂载到本地文件系统中，让您能够像操作本地文件系统一样操作该服务上的文件。

# 基本使用

## 配置客户端

### 配置目录
* 配置`JINDOSDK_HOME`, `LD_LIBRARY_PATH`

解压下载的安装包，以安装包内容解压在`/usr/lib/jindosdk-4.0.0`目录为例：
```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-4.0.0
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${JINDOSDK_HOME}/lib/native/
```

### 配置文件
使用 INI 风格配置文件，配置文件的文件名为`jindosdk.cfg`，示例如下：

```
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# 已开启HDFS服务的Bucket对应的Endpoint。以华东1（杭州）为例，填写为cn-hangzhou.oss-dls.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
# 用于访问JindoFS服务的AccessKey ID和AccessKey Secret。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```

## 挂载 JindoFuse

在完成对 JindoSDK 的配置后。
### 创建一个挂载点， 命令如下：

```
mkdir -p <mount_point>
```
### 挂载 Fuse, 命令如下：
```
jindo-fuse <mount_point> -ouri=[<oss_path>]
```
-ouri 需配置为待映射的 oss 路径，路径可以为 Bucket 根目录或者子目录。
这个命令会启动一个后台的守护进程，将指定的 <oss_path> 挂载到本地文件系统的 <mount_point>。

## 访问 JindoFuse

如果将 JindoFS 服务挂载到了本地 /mnt/oss/，可以执行以下命令访问 JindoFuse。

1. 列出/mnt/oss/下的所有目录：

   ```
   ls /mnt/oss/
   ```

2. 创建目录：

   ```
   mkdir /mnt/oss/dir1
   ls /mnt/oss/
   ```

3. 写入文件：

   ```
   echo "hello world" > /mnt/oss/dir1/hello.txt
   ```

4. 读取文件：

   ```
   cat /mnt/oss/dir1/hello.txt
   ```

   显示`hello world`。

5. 删除目录：

   ```
   rm -rf /mnt/oss/dir1/
   ```
   
## 卸载 JindoFuse

想卸载之前挂载的挂载点，可以使用如下命令：

```
umount <mount_point>
```


## 自动卸载 JindoFuse

可以使用 `-oauto_unmount` 参数，自动卸载挂载点。

使用该参数后，可以支持  `kill `pidof jindof-fuse``发送 SIGINT 给 jindo-fuse 进程，进程退出前会自动卸载挂载点。

# 特性支持

目前 JindoFuse 已经支持以下 POSIX API：

| 特性            | 说明                                     |
| --------------- | ---------------------------------------- |
| getattr()       | 查询文件属性，类似 ls                    |
| mkdir()         | 创建目录，类似 mkdir                     |
| rmdir()         | 删除目录，类似 rm -rf                    |
| unlink()        | 删除文件，类似 unlink                    |
| rename()        | 重命名，类似 mv                          |
| read()          | 顺序读取                                 |
| pread()         | 随机读                                   |
| write()         | 顺序写                                   |
| flush()         | 刷新内存到内核缓冲区                     |
| fsync()         | 刷新内存到磁盘                           |
| release()       | 关闭文件                                 |
| readdir()       | 读取目录                                 |
| create()        | 创建文件                                 |
| open() O_APPEND | 通过追加写的方式打开文件                 |
| open() O_TRUNC  | 通过覆盖写的方式打开文件                 |
| ftruncate()     | 对打开的文件进行截断                     |
| truncate()      | 对未打开的文件进行截断，类似 truncate -s |
| chmod()         | 修改文件权限，类似 chmod                 |
| access()        | 查询文件权限                             |
| utimes()        | 修改文件的存取时间和更改时间                |

# 高阶使用

## 挂载选项

| 参数名称         | 必选 | 参数说明                                                     | 使用范例             |
| ---------------- | ---- | ------------------------------------------------------------ | -------------------- |
| uri              | ✓    | 配置需要映射的 oss 路径。路径可以是根目录，也可以是子目录。例如：oss://bucket/ 或 oss://bucket/subdir。 | -ouri=oss://bucket/  |
| f                |      | 在前台启动进程。默认使用守护进程方式后台启动。使用该参数时，推荐开启终端日志。 | -f                   |
| d                |      | 使用 Debug 模式，在前台启动进程。使用该参数时，推荐开启终端日志。 | -d                   |
| auto_unmount     |      | fuse进程退出后自动umount挂载节点。                           | -oauto_unmount       |
| ro               |      | 只读挂载，启用参数后不允许写操作。                           | -oro                 |
| direct_io        |      | 开启后，读写文件可以绕过page cache。                         | -odirect_io          |
| kernel_cache     |      | 开启后，利用内核缓存优化读性能。                             | -okernel_cache       |
| auto_cache       |      | 默认开启，与kernel_cache 二选一，与kernel_cache不同的是，如果文件大小或修改时间发生变化，缓存就会失效。 |                      |
| entry_timeout    |      | 默认值，0.1。文件名读取缓存保留时间（秒），用于优化性能。0表示不缓存。 | -oentry_timeout=60   |
| attr_timeout     |      | 默认值，0.1。文件属性缓存保留时间（秒），用于优化性能。0表示不缓存。 | -oattr_timeout=60    |
| negative_timeout |      | 默认值，0.1。文件名读取失败缓存保留时间（秒），用于优化性能。0表示不缓存。 | -onegative_timeout=0 |

## 配置选项

| 配置项                 | 默认值           | 说明                                                         |
| ---------------------- | ---------------- | ------------------------------------------------------------ |
| logger.dir             | /tmp/bigboot-log | 日志目录，不存在会创建                                       |
| logger.sync            | false            | 是否同步输出日志，false表示异步输出                          |
| logger.consolelogger   | false            | 打印日志到终端                                               |
| logger.level           | 2                | 输出大于等于该等级的日志，等级范围为0-6，分别表示：TRACE、DEBUG、INFO、WARN、ERROR、CRITICAL、OFF |
| logger.verbose         | 0                | 输出大于等于该等级的VERBOSE日志，等级范围为0-99，0表示不输出 |
| logger.cleaner.enable  | false            | 是否开启日志清理                                             |
| fs.oss.endpoint        |                  | 访问 JindoFS 服务的地址，如cn-xxx.oss-dls.aliyuncs.com       |
| fs.oss.accessKeyId     |                  | 访问 JindoFS 服务需要的 accessKeyId                          |
| fs.oss.accessKeySecret |                  | 访问 JindoFS 服务需要的 accessKeySecret                      |

更多参数可见[相关文档](../configuration/jindosdk_configuration_list.md)。

# 常见问题

## Failed to exec fusermount3

一般情况下，系统中默认安装了 fuse 所需依赖，但 `-oauto_unmount` 参数需要依赖 fusermount3。所以可能出现如下报错：

```
fuse: failed to exec fusermount3: No such file or directory
```

fuse3 可以通过以下命令安装：

```
# CentOS
yum install -y fuse3
# Debian
apt install -y fuse3
```

## Input/Output error

不像使用 JindoSDK 调用 API 可以获取更为具体的 ErrorMsg，JindoFuse 只能显示操作系统预设的错误信息，比如以下错误就非常常见：

```
$ ls /mnt/oss/
ls: /mnt/oss/: Input/output error
```

如果需要定位具体的错误原因，可以根据 JindoSDK 配置中的`logger.dir`，在指定路径下的`jindosdk.log`文件中，寻找具体的错误。

下面展示的这个错误就来自`jindosdk.log`，根据报错信息可见，这个错误是常见的鉴权问题。
```

EMMDD HH:mm:ss jindofs_connectivity.cpp:13] Please check your Endpoint/Bucket/RoleArn. Failed test connectivity, operation: mkdir, errMsg:  [RequestId]: 618B8183343EA53531C62B74 [HostId]: oss-cn-shanghai-internal.aliyuncs.com [ErrorMessage]: [E1010]HTTP/1.1 403 Forbidden ...
```
