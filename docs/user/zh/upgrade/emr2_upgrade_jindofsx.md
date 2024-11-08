# EMR 新版集群 JindoFSX 升级文档
## 场景一：升级已有新版集群

若已有新版管控平台创建的 E-MapReduce EMR-5.11.0/EMR-3.40.0 及以下版本集群。在使用过程中遇到了问题，或者需要使用 JindoFSX 的新功能, 可以根据下面的步骤完成 JindoFSX 升级。

### 1、停止已有的 JindoFSX 集群
在EMR集群的 JindoData 服务页面，选择右上角的`更多操作` > `停止`。

### 2、准备软件包和升级脚本、配置

登录EMR集群的Master节点，并将下载的patch包放在emr-user用户的HOME目录下，将patch包解压缩后，使用emr-user用户执行操作。

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindofsx-patches.tar.gz
tar zxf jindofsx-patches.tar.gz
```

下载 Jindofsx 软件包 jindofsx-{VERSION}-{PLATFORM}.tar.gz，解压到 jindofsx-patches 目录下。

以将新版集群中的 Jindofsx 升级到 4.6.12 版本，linux x86 平台为例:

```bash
cd jindofsx-patches

wget wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/4.6.12/jindofsx-4.6.12-linux.tar.gz

tar -zxvf jindofsx-4.6.12-linux.tar.gz

ls -l
```

jindofsx-patches 内容示例如下：
```bash
-rw-r--r-- 1 emr-user emr-user        29 Sep 13 17:24 hosts
drwxr-xr-x 8 emr-user emr-user      4096 Sep 27  2023 jindofsx-4.6.12-linux
-rw-rw-r-- 1 emr-user emr-user 331179396 Nov  8 11:11 jindofsx-4.6.12-linux.tar.gz
-rwxr-xr-x 1 emr-user emr-user       774 Nov  8 11:09 upgrade_one.sh
-rwxr-xr-x 1 emr-user emr-user      1010 Nov  8 11:06 upgrade.sh
```

### 3、配置升级节点信息

编辑 patch 包下的 hosts 文件，添加集群所有节点的 hostname，如 master-1-1 或 core-1-1，文件内容以行分割。

```bash
cd jindofsx-patches
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
bash upgrade.sh $NEW_JINDOFSX_VERSION $PLATFORM
```

以将新版集群中的 Jindofsx 升级到 linux 平台 6.1.8 版本为例:

```bash
bash upgrade.sh 4.6.12 linux
```

脚本执行完成后，返回如下提示信息。

```
Start upgrade JindoFSX Service to 4.6.12 with platform linux
[1] 11:14:46 [SUCCESS] core-1-2
[2] 11:14:47 [SUCCESS] core-1-1
[3] 11:14:47 [SUCCESS] master-1-1
[1] 11:15:23 [SUCCESS] core-1-1
[2] 11:15:23 [SUCCESS] core-1-2
[3] 11:15:24 [SUCCESS] master-1-1
[1] 11:15:26 [SUCCESS] core-1-1
[2] 11:15:26 [SUCCESS] core-1-2
[3] 11:15:27 [SUCCESS] master-1-1
[1] 11:15:29 [SUCCESS] core-1-2
[2] 11:15:30 [SUCCESS] core-1-1
[3] 11:15:30 [SUCCESS] master-1-1
[1] 11:15:31 [SUCCESS] master-1-1
[2] 11:15:31 [SUCCESS] core-1-1
[3] 11:15:31 [SUCCESS] core-1-2
Successfully upgrade JindoFSX Service to 4.6.12 with platform linux, Please restart JindoFSX Service
```

### 6、升级后启动 JindoFSX 服务

在EMR集群的 JindoData 服务页面，选择右上角的`更多操作` > `启动`。

启动完成后，确认集群服务正常
```bash
[root@master-1-1]# jindo fs -report
Namespace Address: master-1-1:8101
Rpc Port: 8101
Started: Fri Nov  8 11:17:10 2024
Version: 4.6.12
Live Nodes: 2
Decommission Nodes: 0
Total Disk Capacity: 625.956GB
Used Disk Capacity: 0B
Total MEM Capacity: 0B
Used MEM Capacity: 0B
```
确认版本为 Version: 4.6.12，即为升级成功