# EMR 新版集群 JindoCache 升级文档

若已有新版管控平台创建的 E-MapReduce EMR-5.6.0/EMR-3.40.0 及以上版本集群。在使用过程中遇到了问题，或者需要使用 JindoCache 的新功能, 可以根据下面的步骤完成 JindoCache 升级。

### 1、停止已有的 JindoCache 集群
在EMR集群的 JindoCache 服务页面，选择右上角的`更多操作` > `停止`。

### 2、准备软件包和升级脚本、配置

登录EMR集群的Master节点，并将下载的patch包放在emr-user用户的HOME目录下，将patch包解压缩后，使用emr-user用户执行操作。

```bash
su - emr-user
cd /home/emr-user/
wget http://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindocache-patches.tar.gz
tar zxf jindocache-patches.tar.gz
```

下载 jindocache 软件包 jindocache-{VERSION}-{PLATFORM}.tar.gz，解压后到 jindocache-patches目录下。

以将新版集群中的 jindocache 升级到 6.5.3 版本，linux x86 平台为例:

```bash
cd jindocache-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.5.3/jindocache-6.5.3-linux.tar.gz

tar -zxvf jindocache-6.5.3-linux.tar.gz

ls -l
```

jindocache-patches 内容示例如下：
```bash
-rw-rw-r--  1 emr-user emr-user        29 6月   6 11:21 hosts
drwxr-xr-x  7 root     root          4096 8月   8 16:19 jindocache-6.5.3-linux
-rw-r--r--  1 root     root     276406433 8月  12 14:32 jindocache-6.5.3-linux.tar.gz
-rwxr-xr-x  1 emr-user emr-user      1241 6月   6 11:18 upgrade.sh
```

### 3、配置升级节点信息

编辑 patch 包下的 hosts 文件，添加集群所有节点的 hostname，如 master-1-1 或 core-1-1，文件内容以行分割。

```bash
cd jindocache-patches
vim hosts
```

hosts文件内容示例如下：
```bash
master-1-1
core-1-1
core-1-2
```

可尝试使用脚本获取全部节点信息，如果 `hosts` 获取失败，需要手动补全

```bash
cat  /usr/local/taihao-executor-all/data/cache/.cluster_context | jq --raw-output '.nodes[].hostname.alias[]' > hosts
```

### 4、执行升级

通过 upgrade.sh 脚本执行修复操作。

```bash
bash upgrade.sh $NEW_jindocache_VERSION $PLATFORM
```

以将新版集群中的 jindocache 升级到 linux 平台 6.5.3 版本为例:

```bash
bash upgrade.sh 6.5.3 linux
```

脚本执行完成后，返回如下提示信息。

```
Start upgrade JindoCache Service to 6.5.3 with platform linux
[1] 11:14:25 [SUCCESS] core-1-2
[2] 11:14:26 [SUCCESS] core-1-1
[3] 11:14:27 [SUCCESS] master-1-1
[1] 11:14:52 [SUCCESS] core-1-2
[2] 11:14:52 [SUCCESS] master-1-1
[3] 11:14:52 [SUCCESS] core-1-1
[1] 11:14:55 [SUCCESS] core-1-2
[2] 11:14:55 [SUCCESS] master-1-1
[3] 11:14:55 [SUCCESS] core-1-1
[1] 11:14:56 [SUCCESS] core-1-1
[2] 11:14:56 [SUCCESS] core-1-2
[3] 11:14:58 [SUCCESS] master-1-1
[1] 11:14:58 [SUCCESS] core-1-2
[2] 11:14:58 [SUCCESS] core-1-1
[3] 11:15:00 [SUCCESS] master-1-1
[1] 11:15:01 [SUCCESS] master-1-1
[2] 11:15:01 [SUCCESS] core-1-2
[3] 11:15:02 [SUCCESS] core-1-1
[1] 11:15:02 [SUCCESS] core-1-2
[2] 11:15:02 [SUCCESS] core-1-1
[3] 11:15:02 [SUCCESS] master-1-1
Successfully upgrade JindoCache Service to 6.5.3 with platform linux, Please restart JindoCache Service
```

### 5、确认升级成功

```bash
[root@master-1-1]# jindocache -version
Version: 6.5.3
CommitId: ######
```

### 6、升级后启动 JindoCache 服务

在EMR集群的 JindoCache 服务页面，选择右上角的`更多操作` > `启动`。

启动完成后，确认集群服务正常
```bash
[root@master-1-1]# jindocache -report
Namespace Address: master-1-1:8101
Rpc Port: 8101
Started: Thu Jun  6 11:06:05 2024
Version: 6.5.3
Live Nodes: 2
Decommission Nodes: 0
Total Disk Capacity: 625.519GB
Used Disk Capacity: 0B
Total MEM Capacity: 0B
Used MEM Capacity: 0B
```
