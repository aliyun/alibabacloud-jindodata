# EMR 新版集群 JindoSDK 升级文档（arm/倚天环境）

## 场景一：升级已有新版集群

若已有新版管控平台创建的 E-MapReduce EMR-5.6.0/EMR-3.40.0 及以上版本集群。在使用过程中遇到了问题，或者需要使用 JindoSDK 的新功能，具体查看 [版本说明](../releases.md), 可以根据下面的步骤完成 JindoSDK 升级。

### 1. 准备软件包和升级脚本、配置

登录EMR集群的Master节点，并将下载的patch包放在emr-user用户的HOME目录下，将patch包解压缩后，使用emr-user用户执行操作。

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
```

下载 JindoSDK 软件包 jindosdk-{VERSION}-{PLATFORM}.tar.gz，放在解压后的目录。

以将新版集群中的 JindoSDK 升级到 6.9.1 版本，linux arm 平台为例:

```bash
cd jindosdk-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.9.1/jindosdk-6.9.1-linux-el7-aarch64.tar.gz

ls -l
```

jindosdk-patches 内容示例如下：
```bash
-rwxrwxr-x 1 emr-user emr-user       575 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 emr-user emr-user      4047 May 01 00:00 apply.sh
-rw-rw-r-- 1 emr-user emr-user        40 May 01 00:00 hosts
-rw-r----- 1 emr-user emr-user xxxxxxxxx May 01 00:00 jindosdk-6.9.1-linux-el7-aarch64.tar.gz
```

> **注意**：如果从4.6.8以下版本升级到4.6.9以上或6.x版本时，由于 JindoCommitter 默认使用的作业临时路径发生变化，需要在升级前
> 先设置 `fs.jdo.committer.allow.concurrent=false` （core-site.xml）
> 或在 Spark 配置中设置 `spark.hadoop.fs.jdo.committer.allow.concurrent=false`，确保升级期间不会出现丢数据的情况。
> 后续在包含GATEWAY节点的所有JindoSDK全部升级完成后，可以择机去掉该配置。

### 2. 配置升级节点信息

编辑 patch 包下的 hosts 文件，添加集群所有节点的 hostname，如 master-1-1 或 core-1-1，文件内容以行分割。

```bash
cd jindosdk-patches
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

### 3. 执行升级

通过 apply_all.sh 脚本执行修复操作。

```bash
./apply_all.sh $NEW_JINDOSDK_VERSION
```

以将新版集群中的 JindoSDK 升级到 6.9.1 版本为例:

```bash
./apply_all.sh 6.9.1
```

脚本执行完成后，返回如下提示信息。

```
>>> updating ...  master-1-1
>>> updating ...  core-1-1
>>> updating ...  core-1-2
### DONE
```

### 4. 确认升级成功

```bash
ls -l /opt/apps/JINDOSDK/jindosdk-current/lib
```

以从集群默认版本 6.2.0 升级为 6.9.1 版本为例，返回示例如下：
```bash
lrwxrwxrwx 1 root root 64 Apr 12 11:08 jindo-core-6.2.0.jar -> /opt/apps/JINDOSDK/jindosdk-6.9.1-linux/lib/jindo-core-6.9.1.jar
lrwxrwxrwx 1 root root 82 Apr 12 11:08 jindo-core-linux-el7-aarch64-6.2.0.jar -> /opt/apps/JINDOSDK/jindosdk-6.9.1-linux/lib/jindo-core-linux-el7-aarch64-6.9.1.jar
lrwxrwxrwx 1 root root 63 Apr 12 11:08 jindo-sdk-6.2.0.jar -> /opt/apps/JINDOSDK/jindosdk-6.9.1-linux/lib/jindo-sdk-6.9.1.jar
lrwxrwxrwx 1 root root 50 Apr 12 11:08 native -> /opt/apps/JINDOSDK/jindosdk-6.9.1-linux/lib/native
lrwxrwxrwx 1 root root 57 Apr 12 11:08 site-packages -> /opt/apps/JINDOSDK/jindosdk-6.9.1-linux/lib/site-packages
```

### 5. 升级后重启服务

**说明:** 对于已经在运行的YARN作业（Application，例如，Spark Streaming或Flink作业），需要停止作业后，批量滚动重启YARN NodeManager。

Hive、Presto、Impala、Flink、Ranger、Spark 和 Zeppelin 等组件需要重启之后才能完全升级。

以Hive组件为例，在EMR集群的Hive服务页面，选择右上角的`更多操作` > `重启`。


## 场景二：扩容已有集群

若扩容已有集群时需要使用新版 JindoSDK, 可以通过在EMR控制台添加引导操作，完成新建集群或扩容已有集群时自动升级修复。具体参照如下操作步骤完成 JindoSDK 升级。

### 1. 制作引导升级包

下载的 jindosdk-patches.tar.gz ，jindosdk-{VERSION}-{PLATFORM}.tar.gz 和 [bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh),

以将新版集群中的 JindoSDK 升级到 6.9.1 版本，linux aarch64平台为例:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz

wget  https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.9.1/jindosdk-6.9.1-linux-el7-aarch64.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.9.1-linux-el7-aarch64.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

执行命令制作升级包

```bash
bash bootstrap_jindosdk.sh -gen $NEW_JINDOSDK_VERSION
```

以将新版集群中的 JindoSDK 升级到 6.9.1 版本为例:

```bash
bash bootstrap_jindosdk.sh -gen 6.9.1
```
**参数说明：-gen生成lite升级包，-gen-full表示生成完整升级包。**

成功后可以看到如下：

```bash

Generated patch at /home/emr-user/jindo-patch/jindosdk-bootstrap-patches.tar.gz

```

制作完成，得到 patch 包： `jindosdk-bootstrap-patches.tar.gz`。

### 2. 上传引导升级包

将 patch包 和 bootstrap 脚本上传到 OSS 上。

EMR 集群内可以通过 hadoop 命令上传，或者通过 OSS 控制台、ossutil 或 OSS Browser 等工具。

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

cd /home/hadoop/patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://<bucket-name>/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://<bucket-name>/path/to/patch/

hadoop dfs -ls oss://<bucket-name>/path/to/patch/
```

可看到返回内容示例如下：

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket-name>/.../bootstrap_jindosdk.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket-name>/.../jindosdk-bootstrap-patches.tar.gz
```


例如，上传到OSS的路径为`oss://<bucket-name>/path/to/jindosdk-bootstrap-patches.tar.gz`和`oss://<bucket-name>/path/to/bootstrap_jindosdk.sh`。

### 3. 在EMR控制台添加引导操作

在 EMR 控制台添加引导操作，详细信息请参见[管理引导操作](https://help.aliyun.com/document_detail/398732.html).

在**添加引导操作**对话框中，填写配置项。

| 参数             | 描述                                     | 示例                                                         |
| :--------------- |:---------------------------------------| ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOSDK。                 | update_jindosdk                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。               | -bootstrap oss://<bucket-name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **执行范围**     | 选择**集群**。                              |                                                              |
| **执行时间**     | 选择**组件启动前**。                           |                                                              |
| **执行失败策略** | 选择**继续执行**。                            |                                                              |

## 场景三：新建集群

若新建 EMR 集群时需要使用新版 JindoSDK, 可以通过在EMR控制台添加引导操作，完成新建集群或扩容已有集群时自动升级修复。具体参照如下操作步骤完成 JindoSDK 升级。

### 1. 制作引导升级包

下载的 jindosdk-patches.tar.gz ，jindosdk-{VERSION}-{PLATFORM}.tar.gz 和 [bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh),

以将新版集群中的 JindoSDK 升级到 6.9.1 版本，linux aarch64平台为例:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz

wget  https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.9.1/jindosdk-6.9.1-linux-el7-aarch64.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.9.1-linux-el7-aarch64.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

执行命令制作升级包

```bash
bash bootstrap_jindosdk.sh -gen-full $NEW_JINDOSDK_VERSION
```

以将新版集群中的 JindoSDK 升级到 6.9.1 版本为例:

```bash
bash bootstrap_jindosdk.sh -gen-full 6.9.1
```
**参数说明：-gen生成lite升级包，-gen-full表示生成完整升级包。**

成功后可以看到如下：

```bash

Generated patch at /home/emr-user/jindo-patch/jindosdk-bootstrap-patches.tar.gz

```

制作完成，得到 patch 包： `jindosdk-bootstrap-patches.tar.gz`。

### 2. 上传引导升级包

将 patch包 和 bootstrap 脚本上传到 OSS 上。

EMR 集群内可以通过 hadoop 命令上传，或者通过 OSS 控制台、ossutil 或 OSS Browser 等工具。

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

cd /home/hadoop/patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://<bucket-name>/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://<bucket-name>/path/to/patch/

hadoop dfs -ls oss://<bucket-name>/path/to/patch/
```

可看到返回内容示例如下：

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket-name>/.../bootstrap_jindosdk.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket-name>/.../jindosdk-bootstrap-patches.tar.gz
```


例如，上传到OSS的路径为`oss://<bucket-name>/path/to/jindosdk-bootstrap-patches.tar.gz`和`oss://<bucket-name>/path/to/bootstrap_jindosdk.sh`。

### 3. 在EMR控制台添加引导操作

在 EMR 控制台添加引导操作，详细信息请参见[管理引导操作](https://help.aliyun.com/document_detail/398732.html).

在**添加引导操作**对话框中，填写配置项。

| 参数             | 描述                                     | 示例                                                         |
| :--------------- |:---------------------------------------| ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOSDK。                 | update_jindosdk                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。               | -bootstrap oss://<bucket-name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **执行范围**     | 选择**集群**。                              |                                                              |
| **执行时间**     | 选择**组件启动前**。                           |                                                              |
| **执行失败策略** | 选择**继续执行**。                            |                                                              |

## 场景四：回滚 JindoSDK 到集群初始版本

若已有新版管控平台创建的 E-MapReduce EMR-5.6.0/EMR-3.40.0 及以上版本集群。在升级过程中遇到了问题，需要回滚到集群初始JindoSDk版本

### 1. 准备回滚脚本

登录EMR集群的Master节点，并将下载的patch包放在emr-user用户的HOME目录下，将patch包解压缩后，使用emr-user用户执行操作。

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
cd jindosdk-patches
ls -l
```

jindosdk-patches 内容示例如下：
```bash
-rwxrwxr-x 1 emr-user emr-user      2439 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 emr-user emr-user      7315 May 01 00:00 apply.sh
-rw-rw-r-- 1 emr-user emr-user        40 May 01 00:00 hosts
-rwxrwxr-x 1 emr-user emr-user      1112 May 01 00:00 revert_all.sh
-rwxrwxr-x 1 emr-user emr-user      2042 May 01 00:00 revert.sh
```

### 2. 配置回滚节点信息

编辑 patch 包下的 hosts 文件，添加集群所有节点的 hostname，如 master-1-1 或 core-1-1，文件内容以行分割。

```bash
cd jindosdk-patches
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

### 3. 执行回滚

通过 revert_all.sh 脚本执行修复操作。

```bash
./revert_all.sh
```

脚本执行完成后，返回如下提示信息。

```
>>> updating ...  master-1-1
>>> updating ...  core-1-1
>>> updating ...  core-1-2
### DONE
```

### 4. 确认回滚成功

```bash
ls -l /opt/apps/JINDOSDK/jindosdk-current/lib
```

以回滚为 6.2.0 版本为例，返回示例如下：
```bash
-rw-r--r-- 1 root root  1253740 Apr 24 17:40 jindo-core-6.2.0.jar
-rw-r--r-- 1 root root 13110547 Apr 24 17:40 jindo-core-linux-el7-aarch64-6.2.0.jar
-rw-r--r-- 1 root root  4432227 Apr 24 17:40 jindo-sdk-6.2.0.jar
drwxr-xr-x 2 root root     4096 Apr 24 17:40 native
```

### 5. 升级后重启服务

**说明:** 对于已经在运行的YARN作业（Application，例如，Spark Streaming或Flink作业），需要停止作业后，批量滚动重启YARN NodeManager。

Hive、Presto、Impala、Flink、Ranger、Spark 和 Zeppelin 等组件需要重启之后才能完全升级。

以Hive组件为例，在EMR集群的Hive服务页面，选择右上角的`更多操作` > `重启`。