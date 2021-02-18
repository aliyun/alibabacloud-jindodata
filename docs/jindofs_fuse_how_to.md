# JindoFS Fuse 使用
[English Version](./jindofs_fuse_how_to_en.md)


# 访问OSS（作为 OSS 客户端）

Fuse是Linux系统内核提供的一种挂载文件系统的方式。通过JindoFS的Fuse客户端，将对象存储OSS的存储空间（Bucket）挂载到本地文件系统中，您能够像操作本地文件一样操作OSS的对象（Object）。

即使您使用JindoFS Fuse仅仅作为OSS客户端，相对于开源的[ossfs客户端](https://github.com/aliyun/ossfs)实现，您还可以获得更好的性能和阿里云E-MapReduce产品技术团队更专业的支持。

## 步骤

### 1. 下载安装包
下载最新的release包 jindofs-fuse-x.x.x.tar.gz 并解压

注意： 目前jindofs-fuse只支持Linux操作系统。大多数Linux发行版已经内置了Fuse模块，没有的话你需要安装或者编译Fuse模块。 <br />

### 2. 配置账号访问信息
将Bucket名称以及具有此Bucket访问权限的AccessKeyId/AccessKeySecret信息存放在/etc/passwd-ossfs文件中。每一行为一个bucket的配置信息，包含bucket名字、key、secret三项，用：号分割。如果您配置了免密功能，那么key、secret可以放空。
配置示例如下：

```
bucket1:accessKeyId1:accessKeySecret1
bucket2::
```

### 3. 创建挂载点目录

```
mkdir /mnt/jfs
```

### 4. 运行fuse挂载

```
jindofs-fuse -oonly_sdk /mnt/jfs
```

此时，您可以在/mnt/jfs路径下看到挂载的Bucket列表，并且像本地磁盘一样操作OSS上面的文件。

<br />

# 访问JindoFS Cache/Block模式集群

当您已经创建了一个E-MapReduce JindoFS集群，并且希望在另外一个集群或ECS机器上访问JindoFS集群时，可以使用该方法。

### 前提条件

1. 已经创建了一个E-MapReduce JindoFS集群
2. 已知JindoFS集群header节点地址，另外一个集群或ECS的网络可以访问该地址

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

# 附录: SDK配置项列表

jindofs-fuse可以进行一些参数调整，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](./jindofs_sdk_how_to.md#附录-sdk配置项列表)。
<br />



# 发布日志

### v3.4.0
日期：20210210<br />文件：[jindofs-fuse-3.4.0.tar.gz](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-fuse-3.4.0.tar.gz)<br />更新内容：

1. 支持访问OSS。
2. 支持访问JindoFS Block/Cache模式集群。

<br />
