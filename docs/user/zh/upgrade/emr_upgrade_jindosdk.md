# EMR 集群 JindoSDK 升级流程（旧版控制台）

## 背景

在旧版管控平台创建的 E-MapReduce EMR-5.6.0/EMR-3.40.0 及以上版本集群，在使用过程中遇到了问题，或者需要使用 JindoSDK 的新功能，具体查看 [版本说明](../releases.md), 可以根据下面的步骤完成 JindoSDK 升级。

## 场景一：升级已有集群
### 1. 准备软件包和升级脚本

登录EMR集群的Master节点，并将下载的patch包放在hadoop用户的HOME目录下，将patch包解压缩后，使用hadoop用户执行操作。

```bash
su - hadoop
cd /home/hadoop/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
```

下载 JindoSDK 软件包 jindosdk-{VERSION}-{PLATFORM}.tar.gz（以下以linux x86平台为例），放在解压后的目录。

```bash
cd jindosdk-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.4.0/jindosdk-6.4.0-linux.tar.gz

ls -l
```

jindosdk-patches 内容示例如下：
```bash
-rwxrwxr-x 1 hadoop hadoop      1263 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 hadoop hadoop      6840 May 01 00:00 apply.sh
-rw-rw-r-- 1 hadoop hadoop        40 May 01 00:00 hosts
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.4.0-linux.tar.gz
```

> **注意**：如果从4.6.8以下版本升级到4.6.9以上或6.x版本时，由于 JindoCommitter 默认使用的作业临时路径发生变化，需要在升级前
> 先设置 `fs.jdo.committer.allow.concurrent=false` （core-site.xml）
> 或在 Spark 配置中设置 `spark.hadoop.fs.jdo.committer.allow.concurrent=false`，确保升级期间不会出现丢数据的情况。
> 后续在包含GATEWAY节点的所有JindoSDK全部升级完成后，可以择机去掉该配置。

### 2. 配置升级节点信息

编辑patch包下的hosts文件，添加集群所有节点的host name，如emr-header-1或emr-worker-1，文件内容以行分割。

```bash
cd jindosdk-patches
vim hosts
```

hosts文件内容示例如下：
```bash
emr-header-1
emr-worker-1
emr-worker-2
```

### 3. 执行升级

通过apply_all.sh 脚本执行修复操作。

```bash
./apply_all.sh $JINDOSDK_VERSION
```

如

```bash
./apply_all.sh 6.4.0
```

脚本执行完成后，返回如下提示信息。

```
>>> updating ...  emr-header-1
>>> updating ...  emr-worker-1
>>> updating ...  emr-worker-2
### DONE
```

### 4. 确认升级成功

```bash
ls -l /opt/apps/extra-jars/
```

以升级为 6.4.0 版本为例，返回示例如下：
```bash
drwxr-xr-x 2 root   root       4096 Apr 24 15:49 flink
-rw-r--r-- 1 hadoop hadoop   189081 Apr 24 15:20 hadoop-lzo-0.4.21-SNAPSHOT.jar
-rw-r--r-- 1 root   root   16264149 Apr 24 15:49 jindo-core-6.4.0.jar
-rw-r--r-- 1 root   root   14429862 Apr 24 15:49 jindo-core-linux-el7-aarch64-6.4.0.jar
-rw-r--r-- 1 root   root    4459297 Apr 24 15:49 jindo-sdk-6.4.0.jar
drwxr-xr-x 2 root   root       4096 Apr 24 15:49 spark
drwxr-xr-x 2 root   root       4096 Apr 24 15:49 spark3
```

### 5. 升级后重启服务

**说明** 对于已经在运行的YARN作业（Application，例如，Spark Streaming或Flink作业），需要停止作业后，批量滚动重启YARN NodeManager。

Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark 和 Zeppelin 等组件需要重启之后才能完全升级。

以Hive组件为例，在EMR集群的Hive服务页面，选择右上角的`操作` > `重启 All Components`。

## 场景二：扩容已有集群

扩容已有集群时可以自动升级修复。具体操作步骤如下：

### 1. 制作引导升级包

下载的 jindosdk-patches.tar.gz ，jindosdk-6.4.0-linux.tar.gz 和 bootstrap_jindosdk.sh。

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.4.0/jindosdk-6.4.0-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.4.0-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

执行命令制作升级包

```bash
bash bootstrap_jindosdk.sh -gen $JINDOSDK_VERSION
```

如

```bash
bash bootstrap_jindosdk.sh -gen 6.4.0
```
**参数说明：-gen生成lite升级包，-gen-full表示生成完整升级包。**

成功后可以看到如下：

```bash

Generated patch at /home/hadoop/jindo-patch/jindosdk-bootstrap-patches.tar.gz

```

制作完成，得到patch包： `jindosdk-bootstrap-patches.tar.gz`。

### 2. 上传引导升级包

将 patch包 和 bootstrap脚本上传到OSS上。

EMR 集群内可以通过 hadoop 命令上传，或者通过 oss 控制台、ossutil 或 OSS Browser 等工具。

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

cd /home/hadoop/jindo-patch/
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

在EMR控制台添加引导操作，详细信息请参见[管理引导操作](https://help.aliyun.com/document_detail/28108.htm#concept-q52-vln-y2b).

在**添加引导操作**对话框中，填写配置项。

| 参数             | 描述                                                         | 示例                                                         |
| :--------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOSDK。                        | update_jindosdk                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。             | -bootstrap oss://<bucket-name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **执行范围**     | 选择**集群**。                                               |                                                              |
| **执行时间**     | 选择**组件启动后**。                                         |                                                              |
| **执行失败策略** | 选择**继续执行**。                                           |                                                              |

### 4. 确保加载到最新的修复
* 如果是新建集群，则需要重启Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark和Zeppelin等组件。
* 如果是扩容新节点，则需要重启对应节点上的 Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark和Zeppelin等组件。

## 场景三：新建集群

新建EMR集群时在EMR控制台添加引导操作。具体操作步骤如下：

### 1. 制作引导升级包

下载的 jindosdk-patches.tar.gz ，jindosdk-6.4.0-linux.tar.gz 和 bootstrap_jindosdk.sh。

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.4.0/jindosdk-6.4.0-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.4.0-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

执行命令制作升级包

```bash
bash bootstrap_jindosdk.sh -gen-full $JINDOSDK_VERSION
```

如

```bash
bash bootstrap_jindosdk.sh -gen-full 6.4.0
```
**参数说明：-gen生成lite升级包，-gen-full表示生成完整升级包。**

成功后可以看到如下：

```bash

Generated patch at /home/hadoop/jindo-patch/jindosdk-bootstrap-patches.tar.gz

```

制作完成，得到patch包： `jindosdk-bootstrap-patches.tar.gz`。

### 2. 上传引导升级包

将 patch包 和 bootstrap脚本上传到OSS上。

EMR 集群内可以通过 hadoop 命令上传，或者通过 oss 控制台、ossutil 或 OSS Browser 等工具。

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

cd /home/hadoop/jindo-patch/
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

### 在EMR控制台添加引导操作

在EMR控制台添加引导操作，详细信息请参见[管理引导操作](https://help.aliyun.com/document_detail/28108.htm#concept-q52-vln-y2b).

在**添加引导操作**对话框中，填写配置项。

| 参数             | 描述                                                         | 示例                                                         |
| :--------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOSDK。                        | update_jindosdk                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。             | -bootstrap oss://<bucket-name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **执行范围**     | 选择**集群**。                                               |                                                              |
| **执行时间**     | 选择**组件启动后**。                                         |                                                              |
| **执行失败策略** | 选择**继续执行**。                                           |                                                              |

### 确保加载到最新的修复
* 如果是新建集群，则需要重启Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark和Zeppelin等组件。
* 如果是扩容新节点，则需要重启对应节点上的 Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark和Zeppelin等组件。


## 场景四：回滚 JindoSDK 到集群初始版本

### 1. 准备回滚脚本

登录EMR集群的Master节点，并将下载的patch包放在 hadoop 用户的HOME目录下，将patch包解压缩后，使用 hadoop 用户执行操作。

```bash
su - hadoop
cd /home/hadoop/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
cd jindosdk-patches
ls -l
```

jindosdk-patches 内容示例如下：
```bash
-rwxrwxr-x 1 hadoop hadoop      1263 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 hadoop hadoop      6840 May 01 00:00 apply.sh
-rw-rw-r-- 1 hadoop hadoop        40 May 01 00:00 hosts
-rwxrwxr-x 1 hadoop hadoop      1308 May 01 00:00 revert_all.sh
-rwxrwxr-x 1 hadoop hadoop      6326 May 01 00:00 revert.sh
```

### 2. 配置回滚节点信息

编辑patch包下的hosts文件，添加集群所有节点的host name，如emr-header-1或emr-worker-1，文件内容以行分割。

```bash
cd jindosdk-patches
vim hosts
```

hosts文件内容示例如下：
```bash
emr-header-1
emr-worker-1
emr-worker-2
```

### 3. 执行回滚

通过revert_all.sh 脚本执行修复操作。

```bash
./revert_all.sh
```

脚本执行完成后，返回如下提示信息。

```
>>> updating ...  emr-header-1
>>> updating ...  emr-worker-1
>>> updating ...  emr-worker-2
### DONE
```

### 4. 确认回滚成功

```bash
ls -l /opt/apps/extra-jars/
```

以回滚为 4.3.0 版本为例，返回示例如下：
```bash
drwxr-xr-x 2 root   root       4096 Apr 24 17:37 flink
-rw-r--r-- 1 hadoop hadoop   189081 Apr 24 17:21 hadoop-lzo-0.4.21-SNAPSHOT.jar
drwxr-xr-x 2 root   root       4096 Apr 24 17:37 hive
-rw-r--r-- 1 root   root   85025668 Apr 24 17:36 jindo-core-4.3.0.jar
-rw-r--r-- 1 root   root    3165000 Apr 24 17:36 jindo-sdk-4.3.0.jar
drwxr-xr-x 2 root   root       4096 Apr 24 17:37 native
drwxr-xr-x 2 root   root       4096 Apr 24 17:37 spark
drwxr-xr-x 2 root   root       4096 Apr 24 17:37 spark3
```

### 5. 回滚后重启服务

**说明** 对于已经在运行的YARN作业（Application，例如，Spark Streaming或Flink作业），需要停止作业后，批量滚动重启YARN NodeManager。

Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark 和 Zeppelin 等组件需要重启之后才能完全回滚。

以Hive组件为例，在EMR集群的Hive服务页面，选择右上角的`操作` > `重启 All Components`。