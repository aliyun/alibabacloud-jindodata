# EMR 新版集群 JindoCache 升级文档
## 场景一：升级已有新版集群

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

以将新版集群中的 jindocache 升级到 6.7.5 版本，linux x86 平台为例:

```bash
cd jindocache-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.7.5/jindocache-6.7.5-linux.tar.gz

tar -zxvf jindocache-6.7.5-linux.tar.gz

ls -l
```

jindocache-patches 内容示例如下：
```bash
-rw-rw-r--  1 emr-user emr-user            29 6月   6 11:21 hosts
drwxr-xr-x  7 emr-user emr-user          4096 8月   8 16:19 jindocache-6.7.5-linux
-rw-r--r--  1 emr-user emr-user     276406433 8月  12 14:32 jindocache-6.7.5-linux.tar.gz
-rwxr-xr-x  1 emr-user emr-user          1241 6月   6 11:18 upgrade.sh
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

以将新版集群中的 jindocache 升级到 linux 平台 6.7.5 版本为例:

```bash
bash upgrade.sh 6.7.5 linux
```

脚本执行完成后，返回如下提示信息。

```
Start upgrade JindoCache Service to 6.7.5 with platform linux
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
Successfully upgrade JindoCache Service to 6.7.5 with platform linux, Please restart JindoCache Service
```

### 5、确认升级成功

```bash
[root@master-1-1]# jindocache -version
Version: 6.7.5
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
Version: 6.7.5
Live Nodes: 2
Decommission Nodes: 0
Total Disk Capacity: 625.519GB
Used Disk Capacity: 0B
Total MEM Capacity: 0B
Used MEM Capacity: 0B
```
## 场景二：扩容已有集群

若扩容已有集群时需要使用新版 JindoCache, 可以通过在EMR控制台添加引导操作，完成新建集群或扩容已有集群时自动升级。具体参照如下操作步骤完成 JindoCache 升级。

### 1. 制作引导升级包

下载的 jindocache-patches.tar.gz, jindocache-{VERSION}-{PLATFORM}.tar.gz 和 [bootstrap_jindocache.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindocache.sh),

以将新版集群中的 jindocache 升级到 6.7.5 版本，linux x86平台为例:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindocache-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.7.5/jindocache-6.7.5-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindocache.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindocache.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindocache-6.7.5-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindocache-patches.tar.gz
```

执行命令制作升级包

```bash
bash bootstrap_jindocache.sh -gen $NEW_JINDOCACHE_VERSION
```

以将新版集群中的 jindocache 升级到 6.7.5 版本为例:

```bash
bash bootstrap_jindocache.sh -gen 6.7.5
```

成功后可以看到如下：

```bash

Generated patch at /home/emr-user/jindo-patch/jindocache-bootstrap-patches.tar.gz

```

制作完成，得到 patch 包： `jindocache-bootstrap-patches.tar.gz`。

### 2. 上传引导升级包

将 patch包 和 bootstrap 脚本上传到 OSS 上。

EMR 集群内可以通过 hadoop 命令上传，或者通过 OSS 控制台、ossutil 或 OSS Browser 等工具。

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

hadoop dfs -put jindocache-bootstrap-patches.tar.gz oss://<bucket-name>/path/to/patch/
hadoop dfs -put bootstrap_jindocache.sh oss://<bucket-name>/path/to/patch/

hadoop dfs -ls oss://<bucket-name>/path/to/patch/
```

可看到返回内容示例如下：

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket-name>/.../bootstrap_jindocache.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket-name>/.../jindocache-bootstrap-patches.tar.gz
```

例如，上传到OSS的路径为`oss://<bucket-name>/path/to/jindocache-bootstrap-patches.tar.gz`和`oss://<bucket-name>/path/to/bootstrap_jindocache.sh`。

### 3. 在EMR控制台添加引导操作

在 EMR 控制台添加引导操作，详细信息请参见[管理引导操作](https://help.aliyun.com/document_detail/398732.html).

在**添加引导操作**对话框中，填写配置项。

| 参数             | 描述                                                         | 示例                                                         |
| :--------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOCACHE。                        | update_jindocache                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindocache.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。             | -bootstrap oss://<bucket-name>/path/to/patch/jindocache-bootstrap-patches.tar.gz |
| **执行范围**     | 选择**集群**。                                               |                                                              |
| **执行时间**     | 选择**组件启动后**。                                         |                                                              |
| **执行失败策略** | 选择**继续执行**。                                           |                                                              |

## 场景三：新建集群

若新建 EMR 集群时需要使用新版 jindocache, 可以通过在EMR控制台添加引导操作，完成新建集群或扩容已有集群时自动升级修复。具体参照如下操作步骤完成 jindocache 升级。

### 1. 制作引导升级包

下载的 jindocache-patches.tar.gz ，jindocache-{VERSION}-{PLATFORM}.tar.gz 和 [bootstrap_jindocache.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindocache.sh),

以将新版集群中的 jindocache 升级到 6.7.5 版本，linux x86平台为例:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindocache-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.7.5/jindocache-6.7.5-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindocache.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindocache.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindocache-6.7.5-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindocache-patches.tar.gz
```

执行命令制作升级包

```bash
bash bootstrap_jindocache.sh -gen $NEW_JINDOCACHE_VERSION
```

以将新版集群中的 Jindocache 升级到 6.7.5 版本为例:

```bash
bash bootstrap_jindocache.sh -gen 6.7.5
```

成功后可以看到如下：

```bash

Generated patch at /home/emr-user/jindo-patch/jindocache-bootstrap-patches.tar.gz

```

制作完成，得到 patch 包： `jindocache-bootstrap-patches.tar.gz`。

### 2. 上传引导升级包

将 patch包 和 bootstrap 脚本上传到 OSS 上。

EMR 集群内可以通过 hadoop 命令上传，或者通过 OSS 控制台、ossutil 或 OSS Browser 等工具。

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

hadoop dfs -put jindocache-bootstrap-patches.tar.gz oss://<bucket-name>/path/to/patch/
hadoop dfs -put bootstrap_jindocache.sh oss://<bucket-name>/path/to/patch/

hadoop dfs -ls oss://<bucket-name>/path/to/patch/
```

可看到返回内容示例如下：

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket-name>/.../bootstrap_jindocache.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket-name>/.../jindocache-bootstrap-patches.tar.gz
```


例如，上传到OSS的路径为`oss://<bucket-name>/path/to/jindocache-bootstrap-patches.tar.gz`和`oss://<bucket-name>/path/to/bootstrap_jindocache.sh`。

### 3. 在EMR控制台添加引导操作

在 EMR 控制台添加引导操作，详细信息请参见[管理引导操作](https://help.aliyun.com/document_detail/398732.html).

在**添加引导操作**对话框中，填写配置项。

| 参数             | 描述                                                         | 示例                                                         |
| :--------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOCACHE。                        | update_jindocache                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindocache.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。             | -bootstrap oss://<bucket-name>/path/to/patch/jindocache-bootstrap-patches.tar.gz |
| **执行范围**     | 选择**集群**。                                               |                                                              |
| **执行时间**     | 选择**组件启动后**。                                         |                                                              |
| **执行失败策略** | 选择**继续执行**。                                           |                                                              |
