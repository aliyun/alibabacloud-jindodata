# JindoFuse 支持 JindoFSx 配置指南

JindoFuse透明缓存加速可以利用计算集群的闲置存储资源进行数据缓存来加速计算服务，避免了计算集群或服务占用核心集群过多带宽。JindoFuse使用原来的地址（oss://）可以把JindoFS服务上的文件挂载到本地文件系统中，让您能够像操作本地文件系统一样操作JindoFS服务中的文件，同时通过缓存加速访问速度。

## 前提条件

已在E-MapReduce上创建EMR-3.40.0及后续版本，EMR-5.6.0及后续版本的集群，具体操作请参见[创建集群](https://help.aliyun.com/document_detail/343457.htm#task-2136630)。

如果数据源为 OSS/OSS-HDFS/Apache HDFS/NAS  请先完成如下文档配置要求：
    
[OSS/OSS-HDFS 透明缓存加速配置指南](./jindo_cache_oss_hdfs_tutorial.md)

[Apache HDFS 透明缓存加速配置指南](./jindo_cache_hdfs_tutorial.md)

[统一命名空间缓存加速配置指南](./jindo_cache_global_namespace_tutorial.md)

## 步骤一：配置客户端

1.  使用SSH方式登录集群，详情请参见[登录集群](https://help.aliyun.com/document_detail/345645.htm#task-2508490)。
    
2.  执行以下命令，进入配置文件目录。
```shell    
cd /etc/ecm/jindosdk-conf/
```
3.  执行以下命令，修改jindosdk.cfg配置文件。
```shell
vim jindosdk.cfg
```

需修改内容如下。
```shell
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# 配置阿里云OSS/OSS-HDFS Bucket对应的Endpoint。
fs.oss.endpoint= <yourEndpoint>
# 用于访问OSS或OSS-HDFS服务的AccessKey ID和AccessKey Secret。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <yourAccessKeyId>
fs.oss.accessKeySecret = <yourAccessKeySecret>
# 配置Namespace Service地址。
fs.jindofsx.namespace.rpc.address = <hostname>:<port>
# 数据缓存开关。
fs.jindofsx.data.cache.enable = true
# 元数据缓存开关
fs.jindofsx.meta.cache.enable = false
# 临时文件目录。
fs.jindofsx.tmp.data.dir = /tmp
```

## 步骤二：挂载JindoFuse

1.  执行以下命令，创建一个挂载点。
```shell
mkdir -p <mount_point>
```

2.  执行以下命令，挂载JindoFuse。
```shell    
jindo-fuse <mount_point> -ouri=<oss_path/jindo_path> -oxengine=jindofsx
```

-ouri 需配置为待映射的 OSS 路径，路径可以为 Bucket 根目录或者子目录。 该命令会启动一个后台的守护进程，将指定的 `oss_path` 挂载到本地文件系统的 `mount_point`。或者也可挂载统一命名空间的路径，比如 `jindo://<ip>:<port>/<mountpoint>` 。


## 步骤三：访问JindoFuse
例如，如果将OSS服务挂载到了本地/mnt/oss/目录，则可以执行以下命令访问JindoFuse。

* 查看/mnt/oss/下的所有目录
```shell    
ls /mnt/oss/
```

* 创建目录
```shell    
mkdir /mnt/oss/dir1
```

* 写入文件
```shell    
echo "hello world" > /mnt/oss/dir1/hello.txt
```
* 读取文件
```shell    
cat /mnt/oss/dir1/hello.txt
```
显示hello world。

* 删除目录
```shell    
rm -rf /mnt/oss/dir1/
```

## 步骤四：卸载JindoFuse

如果您想卸载之前挂载的挂载点，可以使用如下命令。
```shell
umount <mount_point>
```
您也可以使用 `-oauto_unmount` 参数，自动卸载挂载点。

使用该参数后，支持 `killall -9 jindo-fuse` 发送 `SIGINT` 给 `jindo-fuse` 进程，该进程退出前会自动卸载挂载点。
