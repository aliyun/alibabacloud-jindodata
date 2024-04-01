### 背景
JindoFS 目录保护功能当前可以通过命令行命令的方式进行操作，可以显示正在保护的目录列表，或者修改保护的目录。将来还会通过产品界面的方式，提供更加便捷易用的查看和设置方法。

### 目录保护功能描述
JindoFS 目录保护功能，主要防止用户误删目录，被保护的目录只要该目录下存在目录或者文件，保护目录的删除以及重命名操作会报错，只有当被保护的目录下不存在任何目录或者文件，保护目录才会被允许删除或者重命名。

### JindoFS 命令行简介
参见[JindoFS 命令行工具使用指南](/docs/user/jindofs/jindofs_client_tools.md)

目录保护功能需要用到的是 -putConfig 与 -getConfig 命令，具体用法将在下文介绍。
### 配置访问 AK
目录保护功能需要配置连接 OSS-HDFS 所需的 AK 信息。有两种方法可以进行配置：

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
### 设置保护目录
命令：
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf fs.protected.directories=/path/to/dir1,/path/to/dir2
```
上述命令对名为 <bucket>（endpoint 为 <oss-hdfs-endpoint>）的桶配置了两个保护目录：
```bash
/path/to/dir1
/path/to/dir2
```
如示例中的格式，这些保护目录的路径需要写在一起，逗号隔开。路径不以 oss:// 开头，而是使用桶内以 / 开头的绝对路径。如果命令失败，将显示失败信息。如果没有任何信息显示，表明执行成功。命令执行成功之后，对这些目录的保护功能将在 30 秒内正式生效。
### 查看保护目录
命令：
```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name fs.protected.directories
```
上述命令返回了名为 <bucket>（endpoint 为 <oss-hdfs-endpoint>）的桶的保护目录列表，返回值示例：
```bash
fs.protected.directories: /path/to/dir1,/path/to/dir2
```
表明这些目录目前正在保护当中：
```bash
/path/to/dir1
/path/to/dir2
```
### 删除所有保护目录
命令：
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf fs.protected.directories=
```
上述命令删除了名为 <bucket>（endpoint 为 <oss-hdfs-endpoint>）的桶的所有保护目录，正式生效后（命令返回后 30 秒内），该桶将不再有任何目录受到保护。