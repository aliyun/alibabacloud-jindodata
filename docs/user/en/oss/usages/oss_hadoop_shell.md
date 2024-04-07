# Accessing OSS/OSS-HDFS via Hadoop Shell Commands

## Preparation

*   In EMR environments, JindoSDK is pre-installed by default. Note:
    *   To access OSS-HDFS, create an EMR cluster with version EMR-3.42.0 or higher.
*   In non-EMR environments, install and deploy JindoSDK first. Follow the instructions in [Deploying JindoSDK in a Hadoop Environment](../../jindosdk/jindosdk_deployment_hadoop.md).
    *   For OSS-HDFS access, use JindoSDK version 4.x or higher.

## Accessing via Hadoop Shell Commands

Accessing OSS and OSS-HDFS through Hadoop shell commands is generally similar, with slight differences in endpoint URLs. Root path examples can be found in the [OSS/OSS-HDFS Quick Start](../oss_quickstart.md).

*   Upload a File

Upload `examplefile.txt` from the local root directory to the `examplebucket` in OSS-HDFS:

```bash
hadoop fs -put examplefile.txt oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

* Create a Directory

Create a directory called `dir/` under `examplebucket` in OSS-HDFS:

```bash
hadoop fs -mkdir oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/dir/
```

* List Files or Directories

List files or directories in `examplebucket` in OSS-HDFS:

```bash
hadoop fs -ls oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

* Check File or Directory Size

Get the size of all files and directories in `examplebucket` in OSS-HDFS:

```bash
hadoop fs -du oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

* View File Content

View the contents of a file named `localfile.txt` in `examplebucket` in OSS-HDFS:

```bash
hadoop fs -cat oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/localfile.txt
```
**Important:** When viewing file content, it will be printed to the console as plain text. If the file content is encoded in a specific format, use HDFS Java APIs to read and decode the content.

* Copy Directories or Files

Copy the contents of `subdir1` under `examplebucket` to another location `subdir2/subdir1`, preserving the structure:

```bash
hadoop fs -cp oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir1  oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir2/subdir1
```

* Move Directories or Files

Move the contents of `srcdir` under `examplebucket` to another location `destdir`:

```bash
hadoop fs -mv oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/srcdir  oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/destdir
```

* Download a File

Download `exampleobject.txt` from `examplebucket` to the local `/tmp/` directory:

```bash
hadoop fs -get oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/exampleobject.txt  /tmp/
```

* Remove Directories or Files

Delete the entire `destfolder/` including its contents under `examplebucket`:

```bash
hadoop fs -rm oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/destfolder/
```