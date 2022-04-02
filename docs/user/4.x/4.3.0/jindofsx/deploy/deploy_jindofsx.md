# 快速部署一个简单的 JindoFSx 存储加速系统

## 1. 下载 JindoFSx 包
下载最新的 Release 包 jindofsx-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 解压
```
tar -xzvf jindofsx-x.x.x.tar.gz
```
并将安装目录部署到所有所需节点上。

## 3. 配置 jindofsx.cfg
在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg。并将配置文件部署到所需节点上（NamespaceService 和 StorageService 所在节点）。

```
[jindofsx-common]
logger.jnilogger=false # jni log 开关
metric.report.on = true # metrics 开关
logger.dir = /tmp/jindofsx-log # 日志目录

[jindofsx-storage]# Storage service 配置
storage.rpc.port=6101 # Storage service 端口
storage.data-dirs = /mnt/disk1/jindofsx,/mnt/disk2/jindofsx,/mnt/disk3/jindofsx,/mnt/disk4/jindofsx # 缓存磁盘目录
storage.data-dirs.capacities = 527371075584,527371075584,527371075584,527371075584 # 缓存磁盘大小
storage.namespace.rpc.address = emr-header-1:8101 # Namespace service 地址及端口
storage.watermark.high.ratio=0.4 # 缓存上水位
storage.watermark.low.ratio=0.2 # 缓存下水位

[jindofsx-namespace]# Namespace service 配置
namespace.rpc.port = 8101 # Namespace service 监听端口
namespace.meta-dir = /tmp/jindofsx/server # 元数据存放目录
```
必要配置说明：
1. [jindofsx-storage] 中的参数 storage.namespace.rpc.address 修改为 JindoFSx 集群的 NamespaceService 服务的所在节点地址（hostname 或者 ip 均可），其中 8101 为 NamespaceService 服务 RPC 的端口号， 需要与 [jindofsx-namespace] 中的参数 namespace.rpc.port 中的端口号一致；
2. [jindofsx-storage] 中需配置好数据缓存相关参数，storage.data-dirs 为缓存目录，可以指定多个，以逗号隔开；storage.data-dirs.capacities 为每块磁盘大小，数量应与 storage.data-dirs 一致；storage.watermark.high.ratio 表示磁盘使用量的上水位比例（0到1之间的小数），每块数据盘的缓存数据目录占用的磁盘空间到达上水位即会触发清理，并且 StorageService 会确保缓存空间占用不超过上水位；storage.watermark.low.ratio 表示使用量的下水位比例（0到1之间的小数，需小于上水位），触发清理后会自动清理冷数据，将缓存数据目录占用空间清理到下水位。
3. [jindofsx-common] 中需配置日志、metrics、credentials、endpoint 相关信息，加速底层存储所需要的 credentials 和 endpoint 配置方法见相应 credential provider [配置文档](../security/jindofsx_credential_provider.md)。

## 4. 配置环境变量
以 jindofsx-4.3.0 安装在 /usr/lib 为例：
```
export JINDOFSX_CONF_DIR=/usr/lib/jindofsx-4.3.0/conf/
```
并确认部署到所有节点。

## 5. 启动 JindoFSx 缓存服务
在 master 节点（启动 JindoFSx NamespaceService 的节点）编辑 sbin/nodes 文件，配置上所有需要启动 StorageService 的节点列表，例如：
```
worker-1
worker-2
worker-3
```

在 master 节点执行以下脚本（注意：由于脚本通过 pssh 命令批量执行远程节点的服务启停，需要保证 master 节点到所有其他节点 ssh 的免密连通）
```
cd jindofsx-x.x.x
sh sbin/start-service.sh
```
执行完成后，master 节点将会启动 Namespace Service，nodes 配置的所有节点将会启动 StorageService。

另外，可以通过以下命令停止所有缓存服务：
```
cd jindofsx-x.x.x
sh sbin/stop-service.sh
```

## 6. 缓存服务状态检查
缓存服务的进程可以通过以下命令查看，日志文件可在 logger.dir 配置的目录下查看
```
ps -aux | grep jindofsx-namespaceservice
ps -aux | grep jindofsx-storageservice
```

如需部署高可用 JindoFSx 存储加速系统, 请参考 [部署高可用 JindoFSx 存储加速系统](../deploy/deploy_raft_ns.md)