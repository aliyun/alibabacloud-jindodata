### 背景
OSS-HDFS 归档直读当前可以通过命令行命令的方式进行操作。将来还会通过产品界面的方式，提供更加便捷易用的查看和设置方法。

### 归档直读功能描述
用户需要读取归档类型的文件时候，通常需要通过解冻操作将归档文件解冻到可读取的状态，这种解冻操作可能比较耗时，OSS-HDFS 归档直读功能可以避免解冻操作，直接读取归档文件

### JindoFS 命令行简介
参见[JindoFS 命令行工具使用指南](./jindofs_client_tools.md)

归档直读功能需要用到的是 -putConfig 与 -getConfig 命令，具体用法将在下文介绍。
### 配置访问 AK
归档直读功能需要配置连接 OSS-HDFS 所需的 AK 信息。有两种方法可以进行配置：

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
### 设置归档直读
命令：
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf namespace.archive.directread.enable=true
```


### 查看归档直读
命令：
```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name namespace.archive.directread.enable
```
上述命令返回了名为 \<bucket\>（endpoint 为 \<oss-hdfs-endpoint\>）的桶的归档直读标签
