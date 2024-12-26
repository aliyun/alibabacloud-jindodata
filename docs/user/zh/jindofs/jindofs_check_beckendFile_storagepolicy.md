# 使用 JindoFS 命令行工具检查归档/解冻任务状态

## 概述

JindoFS 命令行工具是访问 OSS-HDFS 的可执行程序，支持常见文件元数据及读写操作，以及服务特有方法。更多介绍，以及工具的获取和配置方法，参见[JindoFS 命令行工具使用指南](./jindofs_client_tools.md)

OSS-HDFS 服务的介绍，参见[OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)

本文介绍如何使用 JindoFS 命令行工具检查 OSS-HDFS 文件/目录的归档/解冻任务状态。

## 归档/解冻任务介绍

JindoFS 命令行工具提供命令实现冷热分层存储功能（详见[JindoFS 命令行工具实现冷热分层存储](./jindofs_storagepolicy_restore.md)）。但需注意，归档/解冻任务是异步执行的，需要等待一段时间才能看到结果。

其中，归档任务由OSS生命周期规则完成。首先，OSS-HDFS服务会将文件块，即OSS对象文件标记特定tag；然后OSS生命周期会定期将有特定tag的OSS对象文件转换存储状态；将所有OSS对象文件存储状态转换后，归档任务完成。

解冻任务分为临时解冻和永久解冻。临时解冻会将归档文件转换为可读，转换过程需要一段时间。临时解冻完成指定时间后过期，文件重新变为不可读。

永久解冻首先会将文件临时解冻，将文件转换为可读；临时解冻完成后，通过COPY将OSS对象文件存储状态转换为standard。

## 归档任务状态查询

对于原本存储类型为 “标准” 的文件，触发归档后，可以通过以下命令查询归档任务状态：
```text
jindofs fs -checkBeStoragePolicy -path <path>
```
其中： `<path>` 可以是文件或目录。

如果归档任务处于标记tag阶段，将返回如下信息：
```text
The status storage policy transfer job for <path> [Standard->Archive] : SETTING_TAG
```
如果归档任务已经标记好tag，但未完成归档，将返回如下信息：
```text
The status storage policy transfer job for <path> [Standard->Archive] : WAITTING_LIFECYCLE
```
如果归档任务已完成，将返回如下信息：
```text
The status storage policy transfer job for <path> : FINISHED
```

## 临时解冻任务状态查询

对于原本存储类型为 “归档” 的文件，触发临时解冻后，可以通过以下命令查询归档任务状态：
```text
jindofs fs -checkBeStoragePolicy -path <path>
```
其中： `<path>` 可以是文件或目录。

如果临时解冻任务还未开始或已经过期，将返回如下信息：
```text
The status storage policy transfer job for <path> [Archive->ArchiveRestored] : PENDING
```
如果临时解冻任务正在进行，将返回如下信息：
```text
The status storage policy transfer job for <path> [Archive->ArchiveRestored] : RESTORING
```
如果临时解冻任务已完成，将返回如下信息：
```text
The status storage policy transfer job for <path> : FINISHED
```

## 永久解冻任务状态查询

对于原本存储类型为 “归档” 的文件，触发永久解冻后，可以通过以下命令查询归档任务状态：
```text
jindofs fs -checkBeStoragePolicy -path <path>
```
其中： `<path>` 可以是文件或目录。

如果永久解冻任务处于临时解冻未开始阶段，将返回如下信息：
```text
The status storage policy transfer job for <path> [Archive->Standard] : PENDING
```
如果永久解冻任务处于临时解冻执行阶段，将返回如下信息：
```text
The status storage policy transfer job for <path> [Archive->Standard] : RESTORING
```
如果永久解冻任务处于复制阶段，将返回如下信息：
```text
The status storage policy transfer job for <path> [Archive->Standard] : COPYING
```
如果永久解冻任务完成，将返回如下信息：
```text
The status storage policy transfer job for <path> : FINISHED
```

## 检查目录归档/解冻任务状态

对于目录，jidnofs命令行工具会递归地检查目录下所有文件的归档/解冻任务状态。当存在子目录/子文件与指定目录的workflow不匹配时，jindofs命令行工具会跳过检查，并在输出结果中返回：
```text
The status storage policy transfer job for <path> : FINISHED
Files under [<path>/subDir] are ignored as their storage policy were set separately. You can check them individually.
```
此时可以单独检查`<path>/subDir`目录。