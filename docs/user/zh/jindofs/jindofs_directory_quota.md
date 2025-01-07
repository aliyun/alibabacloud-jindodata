### 背景
OSS-HDFS 支持目录配额功能，包含目录容量配额以及数量配额功能，用户可以对OSS-HDFS 设置/清理容量配额以及数量配额，用来限制目录容量使用和目录下的文件/目录数量，避免单个目录过大。
### 开通目录配额功能

参见[JindoFS 命令行工具使用指南](./jindofs_client_tools.md)

权限管理的开通需要用到的是 -putConfig 与 -getConfig 命令，具体用法将在下文介绍。
### 配置访问 AK
权限管理需要配置连接 OSS-HDFS 所需的 AK 信息。有两种方法可以进行配置：

#### 配置文件（推荐）
设置环境变量 JINDOSDK_CONF_DIR 指向某个目录，然后在目录下创建名为 jindofs.cfg 的文件，然后在文件里写入如下内容：
```yaml
[client]
fs.oss.accessKeyId = <key>                               # 对所有 Bucket 生效的默认 AK key
fs.oss.accessKeySecret = <secret>                   # 对所有 Bucket 生效的默认 AK secret
fs.oss.bucket.<bucket>.accessKeyId = <key>  # 仅对某 Bucket 生效的 AK key，优先级高于默认
fs.oss.bucket.<bucket>.accessKeySecret = <secret>  # 仅对某 Bucket 生效的 AK secret
```
#### --extraConf
命令行工具的所有指令均可通过 --extraConf 选项提供配置参数，效果等同于配置文件，例如：
```bash
jindofs admin -getConfig -dlsUri <path> -name <keys>          \
              --extraConf fs.oss.accessKeyId=<AK key>         \
              --extraConf fs.oss.accessKeySecret=<AK secret>
```
将给命令提供额外的 AK key 与 AK secret 配置。
### 打开目录配额功能
命令：
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf namespace.directory.quota.enable=true
```


### 查看目录配额功能
命令：
```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name namespace.directory.quota.enable
```
上述命令返回了名为 \<bucket\>（endpoint 为 \<oss-hdfs-endpoint\>）的桶的目录配额过的标签

### 查看目录配额
命令：
```bash
jindofs fs -count -q -v -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/<path>
```


### 设置目录配额

设置目录数量配额命令：
```bash
jindofs admin -setQuota -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/<path> -q 1000
```

设置目录容量配额命令：
```bash
jindofs admin -setSpaceQuota -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/<path> -q 1000
```


### 清除目录配额

清除目录数量配额命令：
```bash
jindofs admin -clearQuota -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/<path>
```

清楚目录容量配额命令：
```bash
jindofs admin -setSpaceQuota -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/<path>
```

配额注意事项：

* 当前目录配额的限制不是硬限制，主要由于目录配额的使用量统计具有一定延时，正常情况下10分钟之内统计最新目录配额使用量，因此存在目录的使用量超过设置的配额。
