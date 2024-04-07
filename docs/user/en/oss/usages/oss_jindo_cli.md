# Accessing OSS/OSS-HDFS via Jindo CLI Commands

## Preparation

*   In EMR environments, JindoSDK is pre-installed, so you can directly use it. Note:
    *   To access OSS-HDFS, create an EMR cluster with version EMR-3.44.0 or higher.
*   In non-EMR environments, install and deploy JindoSDK first. Follow the guide in [Deploying JindoSDK in a Hadoop Environment](../../jindosdk/jindosdk_deployment_hadoop.md).
    *   For OSS-HDFS access, use JindoSDK version 4.6.x or later.

## Accessing via Jindo CLI Commands

Accessing OSS and OSS-HDFS through Jindo CLI commands is largely similar, with minor differences in endpoint URLs. Root path examples can be found in the [OSS/OSS-HDFS Quick Start](../oss_quickstart.md).

*   Upload a File

Upload `examplefile.txt` from the local root directory to `examplebucket` in OSS-HDFS:

```bash
jindo fs -put examplefile.txt oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

* Create a Directory

Create a directory named `dir/` under `examplebucket` in OSS-HDFS:

```bash
jindo fs -mkdir oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/dir/
```

* List Files or Directories

List files or directories in `examplebucket` in OSS-HDFS:

```bash
jindo fs -ls oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

* Check File or Directory Size

Get the size of all files and directories in `examplebucket` in OSS-HDFS:

```bash
jindo fs -du oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

* View File Content

View the contents of a file named `localfile.txt` in `examplebucket` in OSS-HDFS:

```bash
jindo fs -cat oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/localfile.txt
```
**Important:** When viewing file content, it will be displayed as plain text in the terminal. If the file content is encoded in a specific format, use HDFS Java APIs to read and decode the content.

* Download a File

Download `exampleobject.txt` from `examplebucket` to the local `/tmp/` directory:

```bash
jindo fs -get oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/exampleobject.txt  /tmp/
```

* Remove Directories or Files

Delete the complete `destfolder/` including its contents under `examplebucket`:

```bash
jindo fs -rm oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/destfolder/
```

## Appendix

For a comprehensive list of commands and descriptions, refer to the [Jindo CLI Options Documentation](../../jindosdk/jindosdk_cli_options.md).