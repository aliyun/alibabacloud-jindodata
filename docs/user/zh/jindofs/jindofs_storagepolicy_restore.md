# 使用 JindoFS 命令行工具实现冷热分层存储

## 概述

JindoFS 命令行工具是访问 OSS-HDFS 的可执行程序，支持常见文件元数据及读写操作，以及服务特有方法。更多介绍，以及工具的获取和配置方法，参见[JindoFS 命令行工具使用指南](./jindofs_client_tools.md)

OSS-HDFS 服务的介绍，参见[OSS-HDFS服务概述](https://help.aliyun.com/document_detail/405089.htm)

本文介绍如何使用 JindoFS 命令行工具实现 OSS-HDFS 服务的冷热分层存储功能，以助于更好地治理数据，管理成本。

## 存储类型

OSS-HDFS 服务目前支持六种存储类型，依次为：

| 存储类型                     | 描述                                |
|:-------------------------|:----------------------------------|
| `CLOUD_STD`              | 标准存储，默认的存储类型，热数据，存储费用最高           |
| `CLOUD_IA`               | 低频存储，偏冷的数据，可直接访问，存储费用较低，访问费用较高    |
| `CLOUD_AR`               | 归档存储，冷数据，只有临时解冻后才能访问，存储费用低        |
| `CLOUD_COLD_AR`          | 冷归档存储，比归档更冷的类型，只有临时解冻后才能访问，存储费用很低 |
| `CLOUD_AR_RESTORED`      | 归档存储临时解冻后的类型，有过期时间                |
| `CLOUD_COLD_AR_RESTORED` | 冷归档存储临时解冻后的类型，有过期时间               |

## 数据归档

对于原本存储类型为 “标准” 或 “低频” 的数据，可以通过如下命令触发对数据的归档指令：
```text
jindofs fs -setStoragePolicy -path <path> -policy <policy>
```
其中：
 * `<path>` 可以是文件或目录，与开源 HDFS 的 `setStoragePolicy` 功能一致。
 * `<policy>` 为目标存储类型，`CLOUD_AR` 为 “归档”，`CLOUD_COLD_AR` 为 “冷归档”。

如果命令正确执行，将返回如下信息：
```text
Successfully Set StoragePolicy for <path> with policy: <policy>
```

## 检查归档任务

上一则命令触发了数据归档的后台任务。可以通过下列指令检查这个任务是否完成：
```text
jindofs fs -checkStoragePolicy -path <path>
```
返回的内容示例为：
```text
The status storage policy set/unset job for <path> is FINALIZED
```
其中 `FINALIZED` 即为 “已完成”。其他可能的状态还有 `PENDING`、`PROCESSING`、`SUBMITTED`，均为未完成状态，需继续等待。

## 数据解冻

对于归档的数据，如果需要访问，必须先进行解冻。可以通过如下命令触发对数据的临时解冻：
```text
jindofs fs -setStoragePolicy -path <path> -policy <policy> -restoreDays <restoreDays>
```
其中：
 * 如果数据原本为归档（`CLOUD_AR`），那么 `<policy>` 必须为 `CLOUD_AR_RESTORED`；如果原本为冷归档（`CLOUD_COLD_AR`），则 `<policy>` 必须为 `CLOUD_COLD_AR_RESTORED`。
 * `<restoreDays>` 指定解冻的天数。归档支持 `1 - 7` 天，冷归档支持 `1 - 365` 天。不指定则默认 `1` 天。
 * 可以用 `jindofs fs -checkStoragePolicy -path <path>` 查看临时解冻任务是否完成。
 * 数据从归档（`CLOUD_AR`）或者冷归档(`CLOUD_COLD_AR`) 恢复成低频（`CLOUD_IA`）或者标准 (`CLOUD_STD`) 存在限制，单次提交数据量不能超过5TB， 同时处于执行状态的数据量不能超过50TB。

临时解冻有下列注意事项：
 * 用 `CLOUD_AR` 或者 `CLOUD_COLD_AR` 的策略进行归档之后，必须间隔超过 `2` 天才可进行解冻。
 * 临时解冻任务完成后，数据仍不能立刻可读。通常归档类型需要数分钟后可读，冷归档类型则需要数小时。
 * 临时解冻有天数限制，由 `<restoreDays>` 指定，超出则回到不可读状态。
 * 处于临时解冻状态时，仍可以再次进行解冻，但是间隔必须超过 `2` 天以上。

## 临时解冻转标准/低频
从6.13.0版本开始，临时解冻支持转低频/标准存储类型。即支持以下类型转换：
* CLOUD_AR_RESTORED -> CLOUD_STD
* CLOUD_AR_RESTORED -> CLOUD_IA
* CLOUD_COLD_AR_RESTORED -> CLOUD_STD
* CLOUD_COLD_AR_RESTORED -> CLOUD_IA
* CLOUD_DEEP_COLD_AR_RESTORED -> CLOUD_STD
* CLOUD_DEEP_COLD_AR_RESTORED -> CLOUD_IA
