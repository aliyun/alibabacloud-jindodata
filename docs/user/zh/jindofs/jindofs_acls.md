# OSS-HDFS 权限管理
## 背景
OSS-HDFS 实现了一个POSIX系统类似的文件和目录权限模型，每个目录和文件都存在owner、group、others 三类权限，目录和文件对于不同的用户以及用户组有着不同的权限。文件或目录的权限就是它的模式（mode）。OSS-HDFS采用了Unix表示和显示模式的习惯，包括使用八进制数来表示权限。

当用户访问OSS-HDFS的文件或者目录，OSS-HDFS会对其进行权限检查，检查流程如下：
- 访问用户与文件/目录owner一致，检查owner的权限
- 访问用户关联的组名在组名列表中出现，检查group对应访问权限
- 否则检查others 对应的访问权限
如果权限检查失败，则客户操作会失败

## 开通权限管理

参见[JindoFS 命令行工具使用指南](./jindofs_client_tools.md)

权限管理的开通需要用到的是 -putConfig 与 -getConfig 命令，具体用法将在下文介绍。
## 配置访问 AK
权限管理需要配置连接 OSS-HDFS 所需的 AK 信息。有两种方法可以进行配置：

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
## 打开权限管理
命令：
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf namespace.permissions.enabled=true
```


## 查看权限管理
命令：
```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name namespace.permissions.enabled
```
上述命令返回了名为 \<bucket\>（endpoint 为 \<oss-hdfs-endpoint\>）的桶的管理权限的标签
