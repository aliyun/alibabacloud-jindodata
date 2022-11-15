# JindoFuse 透明缓存加速 NAS

JindoFuse 透明缓存加速可以利用计算集群的闲置存储资源进行数据缓存来加速计算服务，避免了计算集群/服务占用核心集群过多带宽。JindoFuse 使用 NAS 的地址  (local://) 挂载到本地文件系统中，让您能够像操作本地文件系统一样操作该服务上的文件，同时通过缓存加速访问速度。

# 基本使用

## 环境准备

```bash
# CentOS
yum install -y fuse3 fuse3-devel
# Debian
apt install -y fuse3 libfuse3-dev
```

## 前提条件：

* 集群已挂载 NAS 文件系统

挂载步骤请参考 [Linux系统挂载NFS文件系统](https://help.aliyun.com/document_detail/90529.htm?spm=a2c4g.11186623.0.0.763d4c93XQH1Zc#table-bcw-ioo-ery)

* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.6.x/4.6.1/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.6.x/4.6.1/jindofsx/deploy/deploy_jindosdk.md)

## 配置客户端

### 配置目录
* 配置`JINDOSDK_CONF_DIR`指定配置文件所在目录

以配置文件`jindosdk.cfg`在`/usr/lib/jindosdk-4.6.1/conf`目录为例：
```bash
export JINDOSDK_CONF_DIR=/usr/lib/jindosdk-4.6.1/conf
```

### 配置文件
使用 INI 风格配置文件，配置文件的文件名为`jindosdk.cfg`，示例如下：

```ini
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# 配置Namespace service 地址
fs.jindofsx.namespace.rpc.address = <hostname>:<port>

# 数据缓存开关
fs.jindofsx.data.cache.enable = false
```

## 挂载 JindoFuse

在完成对 JindoSDK 的配置后。
### 创建一个挂载点， 命令如下：

```
mkdir -p <mount_point>
```
### 挂载 Fuse, 命令如下：
```
jindo-fuse <mount_point> -ouri=[<nas_path>] -oxengine=jindofsx
```
-ouri 需配置为待映射的 nas 路径，路径可以为 nas 根目录或者子目录，比如`local:///tmp/nas/`。。
这个命令会启动一个后台的守护进程，将指定的 <nas_path> 挂载到本地文件系统的 <mount_point>。

## 访问 JindoFuse

如果将 nas 路径挂载到了本地 /mnt/nas/，可以执行以下命令访问 JindoFuse。

* 列出/mnt/nas/下的所有目录：

   ```
   ls /mnt/nas/
   ```

* 创建目录：

   ```
   mkdir /mnt/nas/dir1
   ls /mnt/nas/
   ```

* 写入文件：

   ```
   echo "hello world" > /mnt/nas/dir1/hello.txt
   ```

* 读取文件：

   ```
   cat /mnt/nas/dir1/hello.txt
   ```

   显示`hello world`。

* 删除目录：

   ```
   rm -rf /mnt/nas/dir1/
   ```

## 卸载 JindoFuse

想卸载之前挂载的挂载点，可以使用如下命令：

```
umount <mount_point>
```

## 自动卸载 JindoFuse

可以使用 `-oauto_unmount` 参数，自动卸载挂载点。

使用该参数后，可以支持  `killall -9 jindo-fuse` 发送 SIGINT 给 jindo-fuse 进程，进程退出前会自动卸载挂载点。

# 高阶使用
## 配置选项

更多 jindofsx 配置节参数可见[相关文档](../configuration/jindosdk_configuration_list_ini.md)。

* 支持将 jindofsx 配置节参数与挂载选项一同在挂载时指定（挂载时指定参数的优先级高于配置文件），如:

```
jindo-fuse <mount_point> -ouri=[<nas_path>] -ofs.jindofsx.data.cache.enable=false
```