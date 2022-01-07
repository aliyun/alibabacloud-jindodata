# JindoSDK 和 Hadoop OSS Connector 性能对比测试

## 测试环境

* 规格：ecs.c6.8xlarge
* 配置：32 vCPU 64 GiB
* 网卡：10.00Gbps

## 软件版本

* jindosdk-4.0.0
* hadoop-aliyun-3.2.1 (捆绑了aliyun-sdk-oss-3.4.1）

## 测试步骤

我们需要从不同的角度进行测试，需要覆盖读、写、移动、删除等一些操作，并且需要分别测试大文件、小文件下的性能。因此我们准备了3组数据集。
* 数据集1：1 个 5gb 的随机文件
* 数据集2：1000 个 6bytes 的文件（用于模拟小文件场景）
* 数据集3：1000 个 5mb 的文件

## 测试结果

| 		操作 | Hadoop OSS Connector（秒） | JindoSDK 4.0.0（秒） | JindoSDK提升（百分比） |
| --- | --- | --- | --- |
| 		put 5gb file | 33.94 | 10.53 | 68% |
| 		get 5gb file | 93.08 | 23.95 | 74% |
| 		mv 5gb file | 1.25 | 0.97 | 21% |
| 		delete 5gb file | 1.39 | 1.09 | 21% |
|       cp 5gb file | 85.81 | 44.60 | 48% |
| 		<br /> | <br /> | <br /> | <br /> |
| 		put 1000 small files (6bytes) | 120.67 | 77.26 | 35% |
| 		get 1000 small files | 10.72 | 9.49 | 11% |
| 		mv 1000 small files | 3.8 | 2.51 | 33% |
| 		delete 1000 small files | 4.48 | 2.57 | 42% |
|       cp 1000 small files | 125.08 | 87.46 | 30% |
| 		<br /> | <br /> | <br /> | <br /> |
| 		put 1000 files (5mb) | 310.07 | 271.93 | 12% |
| 		get 1000 files | 144.26 | 100.96 | 30% |
| 		mv 1000 files | 49.2 | 16.39 | 66% |
| 		delete 1000 files | 58.97 | 17.6 | 70% |
|       cp 1000 files | 438.62 | 359.97 | 18% |


分析测试结果，可以看出，JindoSDK 在 put、get、mv、delete、cp 操作上性能均显著好于 Hadoop OSS Connector。
