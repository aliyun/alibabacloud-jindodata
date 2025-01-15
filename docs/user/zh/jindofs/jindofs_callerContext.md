# OSS-HDFS 审计日志支持 CallerContext

## 使用简介

参见[JindoFS 命令行工具使用指南](./jindofs_client_tools.md)

CallerContext的开通需要用到的是 -putConfig 与 -getConfig 命令，具体用法将在下文介绍。
## 配置访问 AK
CallerContext需要配置连接 OSS-HDFS 所需的 AK 信息。有两种方法可以进行配置：

### 配置文件（推荐）
设置环境变量 JINDOSDK_CONF_DIR 指向某个目录，然后在目录下创建名为 jindofs.cfg 的文件，然后在文件里写入如下内容：
```yaml
[client]
fs.oss.accessKeyId = <key>                               # 对所有 Bucket 生效的默认 AK key
fs.oss.accessKeySecret = <secret>                   # 对所有 Bucket 生效的默认 AK secret
fs.oss.bucket.<bucket>.accessKeyId = <key>  # 仅对某 Bucket 生效的 AK key，优先级高于默认
fs.oss.bucket.<bucket>.accessKeySecret = <secret>  # 仅对某 Bucket 生效的 AK secret
```
### --extraConf
命令行工具的所有指令均可通过 --extraConf 选项提供配置参数，效果等同于配置文件，例如：
```bash
jindofs admin -getConfig -dlsUri <path> -name <keys>          \
              --extraConf fs.oss.accessKeyId=<AK key>         \
              --extraConf fs.oss.accessKeySecret=<AK secret>
```
将给命令提供额外的 AK key 与 AK secret 配置。
## 打开CallerContext管理
命令：
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf namespace.caller.context.enabled=true
```


## 查看CallerContext管理
命令：
```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name namespace.caller.context.enabled
```
上述命令返回了名为 \<bucket\>（endpoint 为 \<oss-hdfs-endpoint\>）的桶的CallerContext的标签
