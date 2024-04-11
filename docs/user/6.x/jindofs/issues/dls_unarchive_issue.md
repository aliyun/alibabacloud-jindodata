### 问题描述
用户调用解归档将数据从归档/冷归档恢复标准操作后，有时还是会出现解归档的文件不可读取情况，出现如下报错
```shell
 Read from oss://<DLS_BUCKET>.<DLS_ENDPOINT>/<FILE> with error message: Caused by error 30005: IO error: IO error:  [RequestId]: XXXXXXXXXXXXXX [HostId]: <OSS_ENDPOINT> [ErrorMessage]: [E1010]HTTP/1.1 403 Forbidden: <?xml version="1.0" encoding="UTF-8"?><Error>  <Code>InvalidObjectState</Code>  <Message>The operation is not valid for the object's state</Message>  <RequestId>XXXXXXXXXXXXXXX</RequestId>  <HostId><DLS_BUCKET>.<OSS_ENDPOINT></HostId>  <ObjectName>.dlsdata/xxxxxxxxxxxx</ObjectName></Error> [ErrorCode]: 1010 [RequestId]: XXXXXXXXXXXXXXX
```

### 问题根因
出现以上情况大概率由于用户数据块过大，导致OSS数据块在转换存储类型时出现数据的拷贝超时，任务解归档最终出现失败。永久解归档存在解冻限制，推荐用户使用临时解冻功能，详细细节可以参考 [文档](../jindofs_storagepolicy_restore.md)。

### 修复流程
出现这种情况用户如何进行数据修复，需要依赖JindoFsck命令来修复，JindoFsck 命令 [下载链接](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.3.0/jindofs-sdk-6.3.0-linux.tar.gz)， JindoFsck命令使用如下
```shell
[root@master-1-1(192.168.0.182) ~]# ./jindofsck
        jindofsck
                [-help]
                [-version]
                [-restoreFileBlocks -dlsUri <path>
                                            [-restoreDays <restoreDays>]]
                [-restoreBlock -dlsUri <path> -block <block>
                                            [-restoreDays <restoreDays>]]
                [-restoreBlocks -dlsUri <path> [-days <days>]
                                            [-restoreDays <restoreDays>]]
```

JindoFsck 现在支持三种类型数据的修复，分别对应解冻单个数据块，解冻单个文件，解冻特定时间内永久解冻失败的数据块。用户可以根据需要执行上述命令去临时解冻数据。

##### 解冻单个数据块
```shell
./jindofsck -restoreBlock -dlsUri oss://<DLS_BUCKET>.<DLS_ENDPOINT>/ -block .dlsdata/hosts24 -restoreDays
```
参数说明
| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-dlsUri` | OSS-HDFS Bucket地址。 | 是|
| `-block` | 需要恢复的block的地址，采用相对路径，不能oss://  或者 / 开头， 格式 .dlsdata/xxxxxx | 是|
| `-restoreDays` | 指定临时解冻天数，对于归档类型最多解冻 7 天，冷归档类型最多解冻 365 天， 默认解冻归档7天，冷归档30天。 | 否 |

##### 解冻单个文件

```shell
./jindofsck -restoreFileBlocks -dlsUri oss://<DLS_BUCKET>.<DLS_ENDPOINT>/<PATH> -restoreDays 2
```
参数说明
| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-dlsUri` | OSS-HDFS Bucket地址。 | 是|
| `-restoreDays` | 指定临时解冻天数，对于归档类型最多解冻 7 天，冷归档类型最多解冻 365 天， 默认解冻归档7天，冷归档30天。 | 否 |

##### 解冻指定时间段失败的数据
```shell
./jindofsck -restoreBlocks -dlsUri oss://<DLS_BUCKET>.<DLS_ENDPOINT>/<PATH> -days 2 -restoreDays 2
```

参数说明
| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-dlsUri` | OSS-HDFS Bucket地址。 | 是|
| `-days` | 表示恢复几天内永久解归档失败的数据，默认值为 2， 表示恢复两天内永久解归档失败的数据。 | 否|
| `-restoreDays` | 指定临时解冻天数，对于归档类型最多解冻 7 天，冷归档类型最多解冻 365 天， 默认解冻归档7天，冷归档30天。 | 否 |
