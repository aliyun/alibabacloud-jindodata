# EMR 集群 JindoSDK 升级流程

## 前提条件

* 已创建E-MapReduce EMR-5.9.1/EMR-3.43.1 或以上版本的集群。

## 准备软件包和升级脚本

登录EMR集群的Master节点，并将下载的patch包放在emr-user用户的HOME目录下，将patch包解压缩后，使用emr-user用户执行操作。

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
```

下载 JindoSDK 软件包 jindosdk-{VERSION}.tar.gz ，放在解压后的目录。

```bash
cd jindosdk-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/4.6.5/jindosdk-4.6.5-linux.tar.gz

ls -l
```

jindosdk-patches 内容示例如下：
```bash
-rwxrwxr-x 1 emr-user emr-user       575 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 emr-user emr-user      4047 May 01 00:00 apply.sh
-rw-rw-r-- 1 emr-user emr-user        40 May 01 00:00 hosts
-rw-r----- 1 emr-user emr-user xxxxxxxxx May 01 00:00 jindosdk-4.6.5-linux.tar.gz
```

## 配置升级节点信息

编辑patch包下的hosts文件，添加集群所有节点的host name，如master-1-1或core-1-1，文件内容以行分割。

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

## 执行升级

通过apply_all.sh 脚本执行修复操作。

```bash
./apply_all.sh $NEW_JINDOSDK_VERSION $OLD_JINDOSDK_VERSION
```

如 

```bash
./apply_all.sh 4.6.5 4.5.1
```

脚本执行完成后，返回如下提示信息。

```
>>> updating ...  master-1-1
>>> updating ...  core-1-1
>>> updating ...  core-1-2
### DONE
```

**说明** 对于已经在运行的YARN作业（Application，例如，Spark Streaming或Flink作业），需要停止作业后，批量滚动重启YARN NodeManager。

## 升级后重启服务

Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark 和 Zeppelin 等组件需要重启之后才能完全升级。

以Hive组件为例，在EMR集群的Hive服务页面，选择右上角的`操作` > `重启 All Components`。


## 新建集群和扩容已有集群

新建EMR集群时在EMR控制台添加引导操作，或扩容已有集群时可以自动升级修复。具体操作步骤如下：

### 制作引导升级包

下载的 jindosdk-patches.tar.gz ，jindosdk-4.6.5-linux.tar.gz 和 [bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh),

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/4.6.5/jindosdk-4.6.5-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh

ls -l
```

内容示例如下：

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-4.6.5-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

执行命令制作升级包

支持三个参数：
* `-gen` 包含完整的 JindoSDK 功能;
* `-gen-lite` 仅包含 HadoopSDK 功能，支持 EMR on X86 架构，在弹性伸缩场景使用可以加快启动速度;
* `-gen-lite-with-arm` 包含 HadoopSDK 功能，并支持 EMR on X86/倚天arm架构。

```bash
bash bootstrap_jindosdk.sh -gen $NEW_JINDOSDK_VERSION $OLD_JINDOSDK_VERSION
```

如

```bash
bash bootstrap_jindosdk.sh -gen 4.6.5 4.5.1
```

或 


```bash
bash bootstrap_jindosdk.sh -gen-lite 4.6.5 4.5.1
```

成功后可以看到如下：

```bash

Generated patch at /home/emr-user/jindo-patch/jindosdk-bootstrap-patches.tar.gz

```

制作完成，得到patch包： `jindosdk-bootstrap-patches.tar.gz`。

### 上传引导升级包

将 patch包 和 bootstrap脚本上传到OSS上。

EMR 集群内可以通过 hadoop 命令上传，或者通过 oss 控制台、ossutil 或 OSS Browser 等工具。

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

cd /home/hadoop/patch/
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

### 在EMR控制台添加引导操作

在EMR控制台添加引导操作，详细信息请参见[管理引导操作](https://help.aliyun.com/document_detail/28108.htm#concept-q52-vln-y2b).

在**添加引导操作**对话框中，填写配置项。

| 参数             | 描述                                                         | 示例                                                         |
| :--------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOSDK。                        | update_jindosdk                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。             | `-bootstrap oss://<bucket-name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz` |
| **执行范围**     | 选择**集群**。                                               |                                                              |
| **执行时间**     | 选择**组件启动后**。                                         |                                                              |
| **执行失败策略** | 选择**继续执行**。                                           |                                                              |

### 确保加载到最新的修复
* 如果是新建集群，则需要重启YARN、Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark和Zeppelin等组件。
* 如果是扩容新节点，则需要重启对应节点上的 YARN、Hive、Presto、Impala、Druid、Flink、Solr、Ranger、Storm、Oozie、Spark和Zeppelin等组件。
  
#### 对于弹性扩容场景，或者是要避免扩容后重启服务的情况

可以使用 **执行范围** 为`组件安装前`, 同时配合可直连的 http 地址下载`jindosdk-bootstrap-patches.tar.gz`，`bootstrap_jindosdk.sh`会自动检测 URL 并使用 wget 方式下载，具体步骤如下：
  
a. 如果使用 OSS 对象存储存放补丁包，需要开放`对应补丁包文件`的读权限（注意：不要开放bucket的权限，详见[Object ACL](https://help.aliyun.com/document_detail/100676.html)），验证资源是否可以下载：
````
  wget http://<bucket-name>.<internal-endpoint>/path/to/patch/jindosdk-bootstrap-patches.tar.gz
````
  
b. 在**添加引导操作**对话框中，填写配置项。
| 参数             | 描述                                                         | 示例                                                         |
| :--------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **名称**         | 引导操作的名称。例如，升级JINDOSDK。                        | update_jindosdk                                              |
| **脚本位置**     | 选择脚本所在OSS的位置。脚本路径格式必须为oss://**/*.sh格式。 | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **参数**         | 引导操作脚本的参数，指定脚本中所引用的变量的值。             | `-bootstrap http://<bucket-name>.<internal-endpoint>/path/to/patch/jindosdk-bootstrap-patches.tar.gz` |
| **执行范围**     | 选择**集群**。                                               |                                                              |
| **执行时间**     | 选择**组件安装前**。                                         |                                                              |
| **执行失败策略** | 选择**继续执行**。                                           |                                                              |
