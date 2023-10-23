# EMR 集群 JindoSDK 升级流程（旧版控制台）

## 背景

在旧版管控平台创建的 E-MapReduce EMR-5.6.0/EMR-3.40.0 及以上版本集群中遇到[已知问题](../jindodata/jindodata_known_issues.md)或需要使用[新功能](../jindodata/jindodata_release_notes.md)。

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

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.1.1/jindosdk-6.1.1-linux.tar.gz

ls -l
```

jindosdk-patches 内容示例如下：
```bash
-rwxrwxr-x 1 hadoop hadoop      1263 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 hadoop hadoop      6840 May 01 00:00 apply.sh
-rw-rw-r-- 1 hadoop hadoop        40 May 01 00:00 hosts
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.1.1-linux.tar.gz

```

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
./apply_all.sh 6.1.1
```

脚本执行完成后，返回如下提示信息。

```
>>> updating ...  emr-worker-1
>>> updating ...  emr-worker-2
### DONE
```

**说明** 对于已经在运行的YARN作业（Application，例如，Spark Streaming或Flink作业），需要停止作业后，批量滚动重启YARN NodeManager。

### 3. 升级后重启服务

Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark 和 Zeppelin 等组件需要重启之后才能完全升级。

以Hive组件为例，在EMR集群的Hive服务页面，选择右上角的`操作` > `重启 All Components`。


## 场景二： 新建集群和扩容已有集群

新建EMR集群时在EMR控制台添加引导操作，或扩容已有集群时可以自动升级修复。具体操作步骤如下：

### 1. 制作引导升级包

下载的 jindosdk-patches.tar.gz ，jindosdk-6.1.1-linux.tar.gz 和 [bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh),

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.1.1/jindosdk-6.1.1-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.1.1-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

执行命令制作升级包

```bash
bash bootstrap_jindosdk.sh -gen 6.1.1
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

​	可看到返回内容示例如下：

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
