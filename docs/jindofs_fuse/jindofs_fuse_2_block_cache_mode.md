
## 使用 JindoFS Fuse 访问 JindoFS Cache/Block 模式集群

当您已经创建了一个JindoFS集群，并且希望在另外一个集群或ECS机器上访问JindoFS集群时，可以使用该方法。

### 前提条件

1. 已经创建了一个JindoFS集群
2. 已知JindoFS集群header节点(即namespace服务)地址，另外一个集群或ECS的网络可以访问该地址

### 1. 下载安装包
下载最新的release包 jindofs-fuse-x.x.x.tar.gz 并解压

关于版本兼容性。Fuse(SDK)版本和JindoFS服务端之间跨小版本保持相互兼容，跨大版本不一定保证兼容。大版本为前两位，例如3.1.x。您可以从E-MapReduce集群的/usr/lib/b2jindosdk-current/bin/目录，获取与服务端版本完全相同的fuse程序。


### 2. 创建客户端配置文件
将下面环境变量添加到/etc/profile文件中
```
export B2SDK_CONF_DIR=/etc/jindofs-sdk-conf
```
创建文件 /etc/jindofs-sdk-conf/bigboot.cfg  包含以下主要内容
```
[bigboot]
logger.dir = /tmp/bigboot-log

[bigboot-client]
client.storage.rpc.port = 6101
client.namespace.rpc.address = localhost:8101
```
需要将client.namespace.rpc.address修改为JindoFS集群的namespace服务的所在节点地址

### 3. 创建挂载点目录

```
mkdir /mnt/jfs
```

### 4. 运行fuse挂载

```
jindofs-fuse /mnt/jfs
```

此时，您可以在/mnt/jfs路径下看到JindoFS集群namespace列表，并且像本地磁盘一样操作JindoFS集群上的文件。

<br />

## 附录: SDK配置项列表

jindofs-fuse可以进行一些参数调整，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](../jindofs_sdk_configuration_list.md)。
<br />
