# Jindo DistCp使用CMS进行告警

Jindo DistCp（分布式文件拷贝工具）是用于大规模集群内部和集群之间拷贝文件的工具。使用MapReduce实现文件分发，错误处理和恢复，把文件和目录的列表作为map/reduce任务的输入，每个任务会完成源列表中部分文件的拷贝。

CMS（云监控服务），可用于收集阿里云资源的监控指标或用户自定义的监控指标，探测服务可用性，以及针对指标设置警报。使您全面了解阿里云上资源的使用情况和业务运行状况，并及时对故障资源进行处理，保证业务正常运行。

您可以指定本次Jindo DistCp任务结束后是否需要开启CMS，以上报任务失败信息，并使用CMS控制台来配置告警功能。告警条件参见[告警任务计数器](#告警任务计数器)。

## 步骤

### 1. 创建报警联系人和报警联系组

具体请参考文档[《创建报警联系人或报警联系组》](https://help.aliyun.com/document_detail/104004.html?spm=a2c4g.11186623.6.672.1a493b70h9Bgby)

### 2. 获取报警Token

点击左侧导航栏“报警服务”->"报警联系人"->切换上测页签到"报警联系组"->选择对应的报警联系组->点击右侧接入外部报警


### 3. 配置环境变量

| 环境变量 | 说明 |
| --- | --- |
| cmsAccessKeyId | 设置账号对应AccessKey ID |
| cmsAccessSecret | 设置账号对应Access Key Secret |
| cmsRegion | 设置CMS所在Region，需要与其他相关的云上资源一致，例如ECS，OSS，EMR所对应的Region |
| cmsLevel | 设置标识告警通知方式，如果未设置，默认为WARN。INFO为*邮件+钉钉机器人*，WARN为*短信+邮件+钉钉机器人*，CRITICAL为*电话+短信+邮件+钉钉机器人* |

示例如下

```bash
export cmsAccessKeyId=<your_key_id>
export cmsAccessSecret=<your_key_secret>
export cmsRegion=cn-hangzhou
export cmsToken=<your_cms_token>
export cmsLevel=WARN
```

### 4. 开启CMS对失败任务进行告警

#### copy操作

```bash
hadoop jar jindo-distcp-3.7.2.jar \
--src /data/incoming/hourly_table \
--dest oss://yang-hhht/hourly_table \
--enableCMS
```

#### diff操作

```bash
hadoop jar jindo-distcp-3.7.2.jar \
--src /data/incoming/hourly_table \
--dest oss://yang-hhht/hourly_table \
--diff \
--enableCMS
```

#### update操作

```bash
hadoop jar jindo-distcp-3.7.2.jar \
--src /data/incoming/hourly_table \
--dest oss://yang-hhht/hourly_table \
--update \
--enableCMS
```

## 告警内容

### 告警内容格式
```
{
	"JobTime": "2021-03-09 19:45:27~2021-03-09 19:45:28",
	"SrcPath": "hdfs://cluster-xxx:9000//data/incoming/hourly_table",
	"DestPath": "oss://yang-hhht/hourly_table",
	"Counter": {
		"COUNTER_1": XXX,
		"COUNTER_2": XXX,
		...
	}
}
...
```

### <span id="jump">告警任务计数器</span>

告警内容中的counter计数器有以下几种：

#### copy操作

| 任务计数器 | 说明 |
| --- | --- |
| COPY_FAILED | copy失败的文件数，**不为0时告警** |
| CHECKSUM_DIFF | checksum校验失败的文件数，并计入COPY_FAILED |
| FILES_EXPECTED | 预期的copy文件数量 |
| FILES_COPIED | copy成功的文件数 |
| FILES_SKIPPED | update增量更新时跳过的文件数 |
| BYTES_SKIPPED | update增量更新时跳过的字节数 |

#### diff操作

| 任务计数器 | 说明 |
| --- | --- |
| DIFF_FILES | 不相同的文件数，**不为0时告警** |
| SAME_FILES | 经校验完全相同的文件数 |
| DST_MISS | 目标路径不存在的文件数，并计入DIFF_FILES |
| LENGTH_DIFF | 源文件和目标文件大小不一致的数量，并计入DIFF_FILES |
| CHECKSUM_DIFF | checksum校验失败的文件数，并计入DIFF_FILES |
| DIFF_FAILED | diff操作异常的文件数，具体报错参见job日志 |
