# P2P 分布式下载缓存使用

JindoFSx 客户端 P2P 可以被视作一种本地缓存（LocalCache）。与原有的 LocalCache 相比，P2P 缓存中的本地数据块会优先从其他持有该数据的客户端拉取，只有无法向其他客户端请求时，才会从 STS 或远端读取。从概念上来讲，可以将 P2P 缓存视作 “客户端之间可以交互传输数据” 的 “本地缓存”。

## 前提条件：
* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.6.x/4.6.6/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.6.x/4.6.6/jindofsx/deploy/deploy_jindosdk.md)

## 服务端配置
* 在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg。

```ini
[jindofsx-common]
jindofsx.p2p.tracker.thread.number = 1 # TrackerService 的处理线程数，若要开启 P2P 功能，则必须设置大于 1 的值。如果小于 1，就不会创建 TrackerService，也就不会开启 P2P 功能。
jindofsx.p2p.file.prefix = oss://bucket1/data-dir1/,oss://bucket2/data-dir2/ # 使用 P2P 下载的前缀列表，用半角逗号隔开，文件路径只有匹配到其中任一个前缀，才会以 P2P 方式下载。在应用层使用统一挂载路径进行下载时，这里仍应配置为真实的对象路径。
```

并将配置文件更新到所有服务节点（Namespace Service 与 Storage Service 所在节点）。

* 重启 JindoFSx 服务

重启 JindoFSx 服务，使得 P2P 配置生效。在 master 节点执行以下脚本。

```shell
cd jindofsx-x.x.x
sh sbin/stop-service.sh
sh sbin/start-service.sh
```

## 客户端配置
在 `$JINDOSDK_HOME/conf` 文件夹下修改配置 jindofsx.cfg 文件，示例如下：

```ini
[jindosdk]
# P2P 下载最多占用的缓存大小，单位为字节，默认为 5GB，最小值为 1GB
fs.jindofsx.p2p.cache.capacity.limit = 5 * 1024 * 1024 * 1024

# 下载单个文件使用的并发数。
fs.jindofsx.p2p.download.parallelism.per.file = 5

# P2P 下载使用的线程池总大小。
fs.jindofsx.p2p.download.thread.pool.size = 5
```

并将配置更新到所需节点。

完成以上配置后，作业自动使用 P2P 的方式读取。
