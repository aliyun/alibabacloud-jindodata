# Using JindoDistCp with CMS Alerting

JindoDistCp is a distributed file copy tool designed for large-scale intra-cluster and inter-cluster file transfers using MapReduce for file distribution, error handling, and recovery. It treats lists of files and directories as inputs for map/reduce tasks, with each task responsible for copying a portion of the source list.

Cloud Monitoring Service (CMS) collects monitoring metrics for Alibaba Cloud resources or user-defined metrics, monitors service availability, and sets up alarms based on these metrics. It helps you gain comprehensive insights into resource usage and business health, enabling timely action on faulty resources to maintain business continuity.

With JindoDistCp, you can specify whether to trigger CMS alerts after a task completes, reporting failure information and configuring alerting through the CMS console. The alarm conditions are based on [alarm task counters](#alarm-task-counters).

## Steps

### Step 1: Create Alarm Contacts and Groups

Follow the instructions in the [Creating Alarm Contacts or Alarm Contact Groups](https://help.aliyun.com/document_detail/104004.html?spm=a2c4g.11186623.6.672.1a493b70h9Bgby) documentation.

### Step 2: Obtain the Alarm Token

Go to "Alarm Services" -> "Alarm Contacts" -> switch to the upper tab "Alarm Contact Groups" -> select the relevant contact group -> click "Integrate External Alarms" on the right side.

Observe the content under `Alarm Service Call Address`, extracting the part between `token=` and `&`. For instance, if you see:
```
https://metrichub-cms-cn-hangzhou.aliyuncs.com/event/notify?token=XXXX&level=CRITICAL
```
the `cmsToken` would be `XXXX`.

### Step 3: Configure Environment Variables

| Environment Variable | Description |
| --- | --- |
| cmsAccessKeyId | Set your account's Access Key ID |
| cmsAccessSecret | Set your account's Access Key Secret |
| cmsRegion | Set the CMS region, which must match the region of other related cloud resources like ECS, OSS, EMR |
| cmsLevel | Set the alarm notification method; if not set, defaults to WARN. INFO for email+DingTalk robot, WARN for SMS+email+DingTalk robot, CRITICAL for phone+SMS+email+DingTalk robot |

Example configuration:
```bash
export cmsAccessKeyId=<your_key_id>
export cmsAccessSecret=<your_key_secret>
export cmsRegion=cn-hangzhou
export cmsToken=<your_cms_token>
export cmsLevel=WARN
```

### Step 4: Enable CMS Alerts for Failed Tasks

#### Copy Operation

```bash
hadoop jar jindo-distcp-tool-${version}.jar \
--src /data/incoming/hourly_table \
--dest oss://example-oss-bucket/hourly_table \
--enableCMS
```

#### Diff Operation

```bash
hadoop jar jindo-distcp-tool-${version}.jar \
--src /data/incoming/hourly_table \
--dest oss://example-oss-bucket/hourly_table \
--diff \
--enableCMS
```

#### Update Operation

```bash
hadoop jar jindo-distcp-tool-${version}.jar \
--src /data/incoming/hourly_table \
--dest oss://example-oss-bucket/hourly_table \
--update \
--enableCMS
```

## Alarm Content Format

The alarm content structure looks like this:
```
{
	"JobTime": "2021-03-09 19:45:27~2021-03-09 19:45:28",
	"SrcPath": "hdfs://cluster-xxx:9000//data/incoming/hourly_table",
	"DestPath": "oss://example-oss-bucket/hourly_table",
	"Counter": {
		"COUNTER_1": XXX,
		"COUNTER_2": XXX,
		...
	}
}
...
```

## Alarm Task Counters

The following counters are used in the alarm content:

### Copy Operation

| Counter Name | Description |
| --- | --- |
| COPY_FAILED | Number of failed copies; **triggers an alarm if non-zero** |
| CHECKSUM_DIFF | Number of files with checksum failures, counted in COPY_FAILED |
| FILES_EXPECTED | Expected number of files to copy |
| FILES_COPIED | Number of successfully copied files |
| FILES_SKIPPED | Number of skipped files in UPDATE incremental updates |
| BYTES_SKIPPED | Number of skipped bytes in UPDATE incremental updates |

### Diff Operation

| Counter Name | Description |
| --- | --- |
| DIFF_FILES | Number of different files; **triggers an alarm if non-zero** |
| SAME_FILES | Number of files verified as exactly the same |
| DST_MISS | Number of missing files at the destination, counted in DIFF_FILES |
| LENGTH_DIFF | Number of files with differing sizes, counted in DIFF_FILES |
| CHECKSUM_DIFF | Number of files with checksum mismatches, counted in DIFF_FILES |
| DIFF_FAILED | Number of files with errors during the diff operation; see job logs for specifics |