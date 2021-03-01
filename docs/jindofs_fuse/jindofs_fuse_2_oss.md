
## 使用 JindoFS Fuse 访问 OSS

### 1. 下载安装包
下载最新的release包 jindofs-fuse-x.x.x.tar.gz ([下载页面](/docs/jindofs_sdk_download.md))，并解压


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
./jindofs-fuse -oonly_sdk /mnt/jfs
```

<img src="/pic/jindofs_fuse_2_oss_1.png" alt="title" width="500"/>


此时，您可以在/mnt/jfs路径下看到挂载的Bucket列表，并且像本地磁盘一样操作OSS上面的文件。

<img src="/pic/jindofs_fuse_2_oss_2.png" alt="title" width="500"/>

### 5. 参数调优
JindoFS Fuse 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](../jindofs_sdk_configuration_list.md)。
<br />
