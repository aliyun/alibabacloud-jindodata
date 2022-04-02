# JindoFuse 统一挂载缓存加速

 统一挂载缓存加速可以利用计算集群的闲置存储资源进行数据缓存来加速计算服务，避免了计算集群/服务占用核心集群过多带宽。JindoFuse 使用统一挂载的方式 (jindo://) 挂载到本地文件系统中，让您能够像操作本地文件系统一样操作该服务上的文件，同时通过缓存加速访问速度。

# 基本使用

## 前提条件：
* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.3.0/jindofsx/deploy/deploy_jindofsx.md)

## 配置服务端

在 `JINDOFSX_CONF_DIR` 文件夹下修改配置 jindofsx.cfg 文件, 配置缓存加速的 OSS bucket 对应的`Access Key ID`、`Access Key Secret`、`Endpoint`，
并更新到所需要节点上（Namespace Service 和 Storage Service 所在节点）。

```ini
[jindofsx-filestore]
jindofsx.oss.bucket.XXX.accessKeyId = xxx
jindofsx.oss.bucket.XXX.accessKeySecret = xxx
jindofsx.oss.bucket.XXX.endpoint = oss-cn-xxx-internal.aliyuncs.com

jindofsx.oss.bucket.YYY.accessKeyId = xxx
jindofsx.oss.bucket.YYY.accessKeySecret = xxx
jindofsx.oss.bucket.YYY.endpoint = cn-xxx.oss-dls.aliyuncs.com
```

说明: XXX 和 YYY 为 OSS bucket 名称。

## 重启 JindoFSx 服务
重启 JindoFSx 服务，使得配置的 OSS bucket 的`Access Key ID`、`Access Key Secret`、`Endpoint`生效。在 master 节点执行以下脚本。
```
cd jindofsx-x.x.x
sh sbin/stop-service.sh
sh sbin/start-service.sh
```

## 配置客户端

### 配置目录
* 配置`JINDOSDK_CONF_DIR`指定配置文件所在目录

以配置文件`jindosdk.cfg`在`/usr/lib/jindosdk-4.3.0/conf`目录为例：
```bash
export JINDOSDK_CONF_DIR=/usr/lib/jindosdk-4.3.0/conf
```

### 配置文件
使用 INI 风格配置文件，配置文件的文件名为`jindosdk.cfg`，示例如下：

```ini
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# 配置已开启 HDFS 服务的 Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为cn-hangzhou.oss-dls.aliyuncs.com。
# 或配置阿里云 OSS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为oss-cn-hangzhou-internal.aliyuncs.com。
fs.oss.endpoint= <your_endpoint>
# 用于访问 OSS 或 OSS-HDFS 服务的 AccessKey ID 和AccessKey Secret。阿里云账号 AccessKey 拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>

# 配置Namespace service 地址
fs.jindofsx.namespace.rpc.address = <hostname>:<port>

# 数据缓存开关
fs.jindofsx.data.cache.enable = false
```

## 挂载 OSS 或 OSS-HDFS 服务目录

* 挂载命令

```
jindo fsxadmin -mount <path> <realpath>
```

例：

```
jindo fsxadmin -mount /jindooss oss://<Bucket>/
```

执行如上命令后，则 /jindooss 目录下真正挂载的文件路径是 `oss://<Bucket>/`

## 挂载 JindoFuse

在完成对 JindoSDK 的配置后。
### 创建一个挂载点， 命令如下：

```
mkdir -p <mount_point>
```
### 挂载 Fuse, 命令如下：
```
jindo-fuse <mount_point> -ouri=[<jindo_path>]
```
-ouri 需配置为待映射的 jindo 路径，路径可以为统一挂载，比如之前挂载的`jindo:///jindooss`。
这个命令会启动一个后台的守护进程，将指定的 <jindo_path> 挂载到本地文件系统的 <mount_point>。

## 访问 JindoFuse

如果将 jindo 路径（jindo://）挂载到了本地 /mnt/jindo/，可以执行以下命令访问 JindoFuse。

* 列出/mnt/jindo/下的所有目录：

   ```
   ls /mnt/jindo/
   ```

* 创建目录：

   ```
   mkdir /mnt/jindo/dir1
   ls /mnt/jindo/
   ```

* 写入文件：

   ```
   echo "hello world" > /mnt/jindo/dir1/hello.txt
   ```

* 读取文件：

   ```
   cat /mnt/jindo/dir1/hello.txt
   ```

   显示`hello world`。

* 删除目录：

   ```
   rm -rf /mnt/jindo/dir1/
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
jindo-fuse <mount_point> -ouri=[<jindo_path>] -ofs.jindofsx.data.cache.enable=false
```