# JindoSDK 和 Hadoop OSS Connector 性能对比测试

## 测试环境

* 规格：ecs.c6.8xlarge
* 配置：32 vCPU 64 GiB
* 网卡：10.00Gbps

## 软件版本

* jindofs-sdk-2.7.1
* hadoop-aliyun-3.2.1 (捆绑了aliyun-sdk-oss-3.4.1）

## 测试步骤

我们需要从不同的角度进行测试，需要覆盖读、写、移动、删除等一些操作，并且需要分别测试大文件、小文件下的性能。因此我们准备了3组数据集。
* 数据集1：1 个 5gb 的随机文件
* 数据集2：1000 个 2bytes 的文件（用于模拟小文件场景）
* 数据集3：1000 个 5mb 的文件

## 测试结果

| 		操作 | Hadoop OSS Connector（秒） | JindoSDK（秒） | JindoSDK提升（百分比） |
| --- | --- | --- | --- |
| 		put 5gb file | 27.48 | 25.94 | 6% |
| 		get 5gb file | 60.1 | 32.65 | 46% |
| 		mv 5gb file | 314.57 | 4.86 | 98% |
| 		delete 5gb file | 1.26 | 0.8 | 37% |
| 		<br /> | <br /> | <br /> | <br /> |
| 		put 1000 small files (2bytes) | 40.70 | 31.82 | 22% |
| 		get 1000 small files | 12.26 | 12.13 | 相当 |
| 		mv 1000 small files | 5.5 | 2.12 | 61% |
| 		delete 1000 small files | 1.76 | 1.31 | 26% |
| 		<br /> | <br /> | <br /> | <br /> |
| 		put 1000 files (5mb) | 150.64 | 140.62 | 7% |
| 		get 1000 files | 107.74 | 62.47 | 42% |
| 		mv 1000 files | 39.53 | 5.06 | 87% |
| 		delete 1000 files | 1.78 | 1.2 | 33% |


分析测试结果，可以看出，JindoSDK 在 put、get、mv、delete 操作上性能均显著好于 Hadoop OSS Connector
