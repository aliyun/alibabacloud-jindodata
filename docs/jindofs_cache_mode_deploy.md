# JindoFS 兼容模式部署

JindoFS 提供兼容模式，通过分布式缓存服务在计算集群上为访问 OSS 提供缓存加速，以满足大规模的分析和训练吞吐需求，提升作业访问OSS的效率。
本文将详细说明如何在计算集群上部署 Jindo 缓存服务，以及如何利用缓存服务实现对 OSS 的缓存加速。

## JindoFS 缓存服务架构概述
下图为 JindoFS 缓存服务架构图，利用本地存储介质，构成一个分布式缓存系统，可对 OSS 实现缓存加速，主要包含以下3个组件模块，包括两个服务组件（Namespace Service 和 Storage Service）以及一个客户端：
* Jindo Namespace Service：负责缓存块元数据管理，以及提供整个分布式缓存系统的中心管理服务；
* Jindo Storage Service：部署在各个存储节点上，管理维护数据缓存；
* Jindo SDK：提供标准 Hadoop Filesystem 客户端访问 OSS，并且能够连接缓存服务实现缓存加速。
<img src="../pic/jindofs_cache_mode_deploy_1.png" alt="title" width="700"/>

## 部署配置 JindoFS SDK

### 安装jar包
下载并安装最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](/docs/jindofs_sdk_download.md))，针对各常用大数据组件的 JindoFS SDK 安装说明详见([Hadoop/Spark 生态使用 JindoFS SDK](/docs/jindofs_sdk_overview.md))。


## 部署 Jindo 缓存服务
### 下载最新安装包

下载最新 Release 包 b2smartdata-x.x.x.tar.gz ([下载页面](/docs/jindofs_sdk_download.md))。

### 解压
```
tar -xzvf b2smartdata-x.x.x.tar.gz
```
并将安装目录部署到所有所需节点上。

### 配置 bigboot.cfg
在 b2smartdata-x.x.x/conf 文件夹下修改配置文件 bigboot.cfg  包含以下主要内容，并将配置文件部署到所有所需节点上:
```
[bigboot]
logger.dir = /tmp/bigboot-log # 日志目录

[bigboot-client] # 客户端配置
client.storage.rpc.port = 6101 # Storage service 端口
client.namespace.rpc.address = emr-header-1:8101 # Namespace service 地址及端口
jfs.cache.data-cache.enable = true # 缓存开关

[bigboot-storage] # Storage service 配置
storage.rpc.port = 6101 # Storage service 监听端口
storage.data-dirs = /mnt/disk1/bigboot,/mnt/disk2/bigboot,/mnt/disk3/bigboot,/mnt/disk4/bigboot # 缓存磁盘目录
storage.data-dirs.capacities = 527371075584,527371075584,527371075584,527371075584 # 缓存磁盘大小
storage.namespace.rpc.address = emr-header-1:8101 # Namespace service 地址及端口
storage.watermark.high.ratio=0.4 # 缓存上水位
storage.watermark.low.ratio=0.2 # 缓存下水位

[bigboot-namespace] # Namespace service 配置
namespace.rpc.port = 8101 # Namespace service 监听端口
namespace.meta-dir = /mnt/disk1/bigboot # 元数据存放目录

[bigboot-manager] # Manager service 配置
manager.address=localhost
manager.rpc.port=8104 # Manager serivce 监听端口
manager.namespace.rpc.address=emr-header-1:8101 # Namespace service 地址及端口
```
必要配置说明：
1. 需要将[bigboot-client] 中的参数 client.namespace.rpc.address、 [bigboot-storage] 中的参数 storage.namespace.rpc.address 以及 [bigboot-manager] 中的参数 manager.namespace.rpc.address 修改为 JindoFS 集群的 Namespace 服务的所在节点地址（hostname 或者 ip 均可），其中 8101 为 Namespace 服务 RPC 的端口号， 需要与 [bigboot-namespace] 中的参数 namespace.rpc.port 中的端口号一致；
2. [bigboot-storage] 中需配置好数据缓存相关参数，storage.data-dirs 为缓存目录，可以指定多个，以逗号隔开；storage.data-dirs.capacities 为每块磁盘大小，数量应与 storage.data-dirs 一致；storage.watermark.high.ratio 表示磁盘使用量的上水位比例（0到1之间的小数），每块数据盘的缓存数据目录占用的磁盘空间到达上水位即会触发清理，并且 Storage Service 会确保缓存空间占用不超过上水位；storage.watermark.low.ratio 表示使用量的下水位比例（0到1之间的小数，需小于上水位），触发清理后会自动清理冷数据，将缓存数据目录占用空间清理到下水位。


### 配置环境变量
以 b2smartdata-3.6.0 安装在 /usr/lib 为例：
```
export BIGBOOT_SMARTDATA_HOME=/usr/lib/b2smartdata-3.6.0/
export BIGBOOT_JINDOSDK_HOME=/usr/lib/b2smartdata-3.6.0/
export B2SDK_CONF_DIR=/usr/lib/b2smartdata-3.6.0/conf
export SMARTDATA_CONF_DIR=/usr/lib/b2smartdata-3.6.0/conf
```
可将环境变量配置到 /etc/profile.d/bigboot.sh 中，并确认部署到所有节点。

### 启动 JindoFS 缓存服务
在 master 节点（启动 Namespace Service 的节点）编辑 sbin/nodes 文件，配置上所有需要启动 Storage Service 的节点列表，例如：
```
worker-1
worker-2
worker-3
```

在 master 节点执行以下脚本（注意：由于脚本通过 pssh 命令批量执行远程节点的服务启停，需要保证 master 节点到所有其他节点 ssh 的免密连通）
```
cd b2smartdata-x.x.x
sh sbin/start-service.sh
```
执行完成后，master 节点将会启动 Namespace Service，nodes 配置的所有节点将会启动 Storage Service。

另外，可以通过以下命令停止所有缓存服务：
```
cd b2smartdata-x.x.x
sh sbin/stop-service.sh
```

### 缓存服务状态检查
缓存服务的进程可以通过以下命令查看，日志文件可在 logger.dir 配置的目录下查看
```
ps -aux | grep b2-storage
ps -aux | grep b2-namespace
```
另外，JindoFS Manager 服务提供了 Web 页面可以更加直观地查看缓存服务的状态信息，可以通过以下脚本启动 Manager 服务：
```
cd b2smartdata-x.x.x
sh sbin/start-manager.sh
```
Manager 服务监听在 manager.rpc.port 配置的端口上（默认为 8104），可以通过 http://{server_address}:8104 访问 Web UI 功能（页面详情可参考[访问JindoFS Web UI](https://help.aliyun.com/document_detail/213351.html?spm=a2c4g.11186623.6.1111.460f6194PFUUQf))。

## 缓存使用
完成以上配置后，作业访问 OSS 即可利用上缓存，JindoFS 兼容模式提供了透明缓存的使用方式，作业访问 OSS 的方式无需做任何修改，参照[Hadoop/Spark 生态使用 JindoFS SDK](/docs/jindofs_sdk_overview.md)的方式访问 OSS 即可。
作业读取 OSS 上的数据后，会自动缓存到 JindoFS 缓存系统中，后续访问相同的数据就能够命中缓存，可以通过 Web UI 查看缓存目录的使用情况。

## 使用 Prometheus + Grafana 可视化指标观测平台
在完成缓存集群的启动后，JindoFS 兼容模式提供基于 Prometheus + Grafana 的可视化指标观测平台，你可以参考如下文档进行安装和使用该功能

[基于 Prometheus + Grafana 的可视化指标观测平台](./jindofs_cache_metric.md)
