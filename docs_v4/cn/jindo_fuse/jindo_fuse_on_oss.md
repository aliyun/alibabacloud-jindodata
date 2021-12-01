
## 使用 JindoFS Fuse 访问 OSS

### 1. 下载安装包
下载最新的release包 jindofs-fuse-x.x.x.tar.gz ([下载页面](../jindosdk_download.md))，并解压


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
./jindo-fuse -oonly_sdk /mnt/jfs
```


此时，您可以在/mnt/jfs路径下看到挂载的Bucket列表，并且像本地磁盘一样操作OSS上面的文件。

### 5. 其他挂载参数

您也可以在 `jindo-fuse` 和 `挂载目录`之间，通过 `-o` 传递更过参数：

| 参数名                         | 参数说明                                                     | 示例                                                         |
| ------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| credential_provider            | (3.4.1 及以上版本) 当 /etc/passwd-ossfs 中没有配置 OSS的 accessKeyId 和 accessKeySecret 时，会通过免密方式访问 OSS，默认为 ECS_ROLE 免密，您可以通过授权 对应节点 Ecs Role 访问 OSS。同时也支持[其他免密服务](../jindosdk_credential_provider.md)，如 http 免密服务和 k8s secrets 免密功能等。 | `-ocredential_provider=ECS_ROLE`  <br/> 或者<br/> `-ocredential_provider=secrets:///secrets_prefix/` |
| root_ns                        | (3.4.1 及以上版本) 默认fuse挂载路径会自动添加一层ns的子目录，通过指定root_ns参数可以去除这一层子目录。 | -oroot_ns=<ns_name><br/><ns_name>为当前jfs的ns名称           |
| kernel_cache                   | 开启内核读缓存                                               | –okernel_cache                                               |
| entry_timeout<br/>attr_timeout | 延长元数据有效时间                                           | –oentry_timeout=9000 –oattr_timeout=9000                     |



### 6. 参数调优

JindoFS Fuse 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK配置项列表](../jindosdk_configuration_list.md)。

