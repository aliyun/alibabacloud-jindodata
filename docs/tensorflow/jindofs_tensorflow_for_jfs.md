# 使用 JindoFS Tensorflow 访问 JindoFS Cache/Block 模式集群

当您已经创建了一个JindoFS集群，并且希望在另外一个集群或ECS机器上访问JindoFS集群时，可以使用该方法。

## 前提条件

1. 已经创建了一个JindoFS集群
2. 已知JindoFS集群header节点(即namespace服务)地址，另外一个集群或ECS的网络可以访问该地址

## 1. 下载安装包
下载最新的release包 jindofs-tensorflow-x.x.x.tar.gz ([下载页面](/docs/jindofs_sdk_download.md)，并解压。

```
mkdir -p /path/to/jindofs-tensorflow-x.x.x/
tar xzvf jindo-distcp-3.4.0.jar -C /path/to/jindofs-tensorflow-x.x.x/
```

关于版本兼容性。JindoFS Tensorflow 依赖的 Jindo SDK 版本和JindoFS服务端之间跨小版本保持相互兼容，跨大版本不一定保证兼容。大版本为前两位，例如3.1.x。您可以从E-MapReduce集群的/usr/lib/b2jindosdk-current/lib/目录，获取与服务端版本完全相同的fuse程序。

## 2. 设置环境变量
export LD_LIBRARY_PATH="/path/to/jindofs-tensorflow-x.x.x/":$LD_LIBRARY_PATH

## 3. 使用 JindoFs Tensorflow

以加载jindofs-tensorflow2.3

### 3.1 通过load_library添加collector

```
import tensorflow as tf

tf.load_library('/path/to/libjindofs-tensorflow2.3.so')
```