# 通过 POSIX 访问 OSS/OSS-HDFS

## 环境准备

*   EMR环境中，默认已安装 JindoSDK，可以直接使用。注意：
    
    *   访问 OSS-HDFS，需创建EMR-3.42.0及以上版本或EMR-5.8.0及以上版本的集群。
        
*   非 EMR 环境，请先安装部署 JindoSDK。部署方式请参考 [《非 EMR 环境中部署 JindoSDK》](/docs/user/5.x/jindodata/jindo-sdk/deployment.md)。
    
    *   访问 OSS-HDFS，需部署 JindoSDK 4.x 及以上版本。
        

## 挂载 JindoFuse

在完成对 JindoSDK 的配置后。

*   创建一个挂载点， 命令如下：

```bash
mkdir -p <mountpoint>
```
*   挂载 Fuse, 命令如下：
    
```bash
jindo-fuse <mount_point> -ouri=[<osspath>]
```

这个命令会启动一个后台的守护进程，将指定的 `<oss_path>` 挂载到本地文件系统的 `<mount_point>`。

`<mount_point>` 需替换为一个本地路径。

`<oss_path>` 需替换为待映射的 OSS/OSS-HDFS 路径，路径可以为 Bucket 根目录或者子目录。如 oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir/

挂载 OSS 和 OSS-HDFS 路径的方式基本相同，仅路径中的 endpoint 略有不同。根路径示例可参见：[《OSS/OSS-HDFS 快速入门》](/docs/user/5.x/oss/quickstart.md)

*   确认挂载成功，jindo-fuse 进程存在，且启动参数与预期一致
    
```bash
ps -ef | grep jindo-fuse
```

## 访问 JindoFuse

如果将 JindoFS 服务挂载到了本地 /mnt/oss/，可以执行以下命令访问 JindoFuse。

1.  列出/mnt/oss/下的所有目录：
    
```bash
ls /mnt/oss/
```

2.  创建目录：
    
```bash
mkdir /mnt/oss/dir1ls /mnt/oss/
```

3.  写入文件：
    
```bash
echo "hello world" > /mnt/oss/dir1/hello.txt
```

4.  读取文件：
    
```bash
cat /mnt/oss/dir1/hello.txt
```

显示`hello world`。

5.  删除目录：
    
```bash
rm -rf /mnt/oss/dir1/
```

## 卸载 JindoFuse

想卸载之前挂载的挂载点，可以使用如下命令：

```bash
umount <mount_point>
```

## 自动卸载 JindoFuse

可以使用 `\-oauto\_unmount` 参数，自动卸载挂载点。

使用该参数后，可以支持 `killall -9 jindo-fuse` 发送 SIGINT 给 jindo-fuse 进程，进程退出前会自动卸载挂载点。

# 特性支持

目前 JindoFuse 已经支持以下 POSIX API：

|  特性  |  说明  |  OSS  |  OSS-HDFS  |
| --- | --- | --- | --- |
|  getattr()  |  查询文件属性，类似 ls  |  ✅  |  ✅  |
|  mkdir()  |  创建目录，类似 mkdir  |  ✅  |  ✅  |
|  rmdir()  |  删除目录，类似 rm -rf  |  ✅  |  ✅  |
|  unlink()  |  删除文件，类似 unlink  |  ✅  |  ✅  |
|  rename()  |  重命名，类似 mv  |  ✅  |  ✅  |
|  read()  |  顺序读取  |  ✅  |  ✅  |
|  pread()  |  随机读  |  ✅  |  ✅  |
|  write()  |  顺序写  |  ✅  |  ✅  |
|  pwrite()  |  随机写  |  ✅  |  ✅  |
|  flush()  |  刷新内存到内核缓冲区  |  仅支持以最追加写方式打开的文件  |  ✅  |
|  fsync()  |  刷新内存到磁盘  |  仅支持以最追加写方式打开的文件  |  ✅  |
|  release()  |  关闭文件  |  ✅  |  ✅  |
|  readdir()  |  读取目录  |  ✅  |  ✅  |
|  create()  |  创建文件  |  ✅  |  ✅  |
|  open() O\_APPEND  |  通过追加写的方式打开文件  |  ✅  |  ✅  |
|  open() O\_TRUNC  |  通过覆盖写的方式打开文件  |  ✅  |  ✅  |
|  ftruncate()  |  对打开的文件进行截断  |  ❌  |  ✅  |
|  truncate()  |  对未打开的文件进行截断，类似 truncate -s  |  ❌  |  ✅  |
|  lseek()  |  指定打开文件中的读写位置。  |  ❌  |  ✅  |
|  chmod()  |  修改文件权限，类似 chmod  |  ❌  |  ✅  |
|  access()  |  查询文件权限  |  ✅  |  ✅  |
|  utimes()  |  修改文件的存取时间和更改时间  |  ❌  |  ✅  |
|  setxattr()  |  修改文件 xattr 属性  |  ❌  |  ✅  |
|  getxattr()  |  获取文件 xattr 属性  |  ❌  |  ✅  |
|  listxattr()  |  列举文件 xattr 属性  |  ❌  |  ✅  |
|  removexattr()  |  删除文件 xattr 属性  |  ❌  |  ✅  |
|  lock()  |  支持 posix 锁，类似 fcntl  |  ❌  |  ✅  |
|  fallocate()  |  为文件预分配物理空间  |  ❌  |  ✅  |
|  symlink()  |  创建软连接  |  ❌  |  目前仅支持 OSS-HDFS 内部使用，且不支持缓存加速  |
|  readlink()  |  读取软连接  |  ❌  |  目前仅支持 OSS-HDFS 内部使用，且不支持缓存加速  |

# 高阶使用

## 挂载选项

|  参数名称  |  必选  |  版本  |  参数说明  |  使用范例  |
| --- | --- | --- | --- | --- |
|  uri  |  ✓  |  \>=4.3.0  |  配置需要映射的 oss 路径。路径可以是根目录，也可以是子目录。例如：oss://examplebucket/ 或 oss://examplebucket/subdir。  |  \-ouri=oss://examplebucket/  |
|  f  |   |  \>=4.3.0  |  在前台启动进程。默认使用守护进程方式后台启动。使用该参数时，推荐开启终端日志。  |  \-f  |
|  d  |   |  \>=4.3.0  |  使用 Debug 模式，在前台启动进程。使用该参数时，推荐开启终端日志。  |  \-d  |
|  auto\_unmount  |   |  \>=4.3.0  |  fuse进程退出后自动umount挂载节点。  |  \-oauto\_unmount  |
|  ro  |   |  \>=4.3.0  |  只读挂载，启用参数后不允许写操作。  |  \-oro  |
|  direct\_io  |   |  \>=4.3.0  |  开启后，读写文件可以绕过page cache。  |  \-odirect\_io  |
|  kernel\_cache  |   |  \>=4.3.0  |  开启后，利用内核缓存优化读性能。  |  \-okernel\_cache  |
|  auto\_cache  |   |  \>=4.3.0  |  默认开启，与kernel_cache 二选一，与kernel_cache不同的是，如果文件大小或修改时间发生变化，缓存就会失效。  |   |
|  entry\_timeout  |   |  \>=4.3.0  |  默认值，60。文件名读取缓存保留时间（秒），用于优化性能。0表示不缓存。  |  \-oentry\_timeout=60  |
|  attr\_timeout  |   |  \>=4.3.0  |  默认值，60。文件属性缓存保留时间（秒），用于优化性能。0表示不缓存。  |  \-oattr\_timeout=60  |
|  negative\_timeout  |   |  \>=4.3.0  |  默认值，60。文件名读取失败缓存保留时间（秒），用于优化性能。0表示不缓存。  |  \-onegative\_timeout=0  |
|  max\_idle\_threads  |   |  \>=4.3.0  |  默认值，10。处理内核回调的空闲线程池。  |  \-omax\_idle\_threads=10  |
|  xengine  |   |  \>=4.3.0  |  打开缓存  |  \-oxengine  |
|  pread  |   |  \>=4.5.1  |  默认使用顺序读。打开后，使用随机读代替顺序读，适用于随机读远多于顺序读的场景。  |  \-opread  |
|  no\_symlink  |   |  \>=4.5.1  |  配置后，关闭symlink功能。  |  \-ono\_symlink  |
|  no\_writeback  |   |  \>=4.5.1  |  配置后，关闭writeback功能。  |  \-ono\_writeback  |
|  no\_flock  |   |  \>=4.5.1  |  配置后，关闭flock功能。  |  \-ono\_flock  |
|  no\_xattr  |   |  \>=4.5.1  |  配置后，关闭xttar功能。  |  \-ono\_xattr  |

## 配置选项

|  配置项  |  默认值  |  说明  |
| --- | --- | --- |
|  logger.dir  |  /tmp/bigboot-log  |  日志目录，不存在会创建  |
|  logger.sync  |  false  |  是否同步输出日志，false表示异步输出  |
|  logger.consolelogger  |  false  |  打印日志到终端  |
|  logger.level  |  2  |  输出大于等于该等级的日志，等级范围为0-6，分别表示：TRACE、DEBUG、INFO、WARN、ERROR、CRITICAL、OFF  |
|  logger.verbose  |  0  |  输出大于等于该等级的VERBOSE日志，等级范围为0-99，0表示不输出  |
|  logger.cleaner.enable  |  false  |  是否开启日志清理  |
|  fs.oss.endpoint  |   |  访问 JindoFS 服务的地址，如oss-cn-xxx.aliyuncs.com  |
|  fs.oss.accessKeyId  |   |  访问 JindoFS 服务需要的 accessKeyId  |
|  fs.oss.accessKeySecret  |   |  访问 JindoFS 服务需要的 accessKeySecret  |

更多参数可见[《客户端常用配置》](/docs/user/5.x/jindodata/jindo-sdk/configuration.md)。

*   支持将 jindosdk 配置节参数与挂载选项一同在挂载时指定（挂载时指定参数的优先级高于配置文件），如:
    
```bash
jindo-fuse <mount_point> -ouri=[<oss_path>] -ofs.oss.endpoint=[<your_endpoint>] -ofs.oss.accessKeyId=[<your_key_id>] -ofs.oss.accessKeySecret=[<your_key_secret>]
```

# 常见问题

## Input/Output error

不像使用 JindoSDK 调用 API 可以获取更为具体的 ErrorMsg，JindoFuse 只能显示操作系统预设的错误信息，比如以下错误就非常常见：

```bash
$ ls /mnt/oss/
ls: /mnt/oss/: Input/output error
```

如果需要定位具体的错误原因，可以根据 JindoSDK 配置中的 logger.dir，在指定路径下的`jindosdk.log`文件中，寻找具体的错误。

下面展示的这个错误就来自 `jindosdk.log`，根据报错信息可见，这个错误是常见的鉴权问题。

> EMMDD HH:mm:ss jindofs\_connectivity.cpp:13\] Please check your Endpoint/Bucket/RoleArn. Failed test connectivity, operation: mkdir, errMsg:  \[RequestId\]: 618B8183343EA53531C62B74 \[HostId\]: oss-cn-shanghai-internal.aliyuncs.com \[ErrorMessage\]: \[E1010\]HTTP/1.1 403 Forbidden ...