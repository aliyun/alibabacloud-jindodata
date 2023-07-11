# OSS-HDFS 开启 Ranger 鉴权性能报告

## 测试环境

* 规格：ecs.g6.8xlarge

* 配置：32 vCPU 128 GiB

* 网卡：10.00Gbps

## 软件版本

* jindosdk-5.0.0

* jindoauth-5.0.0

## 测试步骤

我们需要从不同的角度进行测试，需要覆盖读、写、移动、删除等一些操作，并且需要分别测试大文件、小文件下的性能。因此我们准备了2组数据集。

* 数据集1：1 个 5gb 的随机文件

* 数据集2：1000 个 5bytes 的文件（用于模拟小文件场景）

## 测试结果

|  操作  |  开启Ranger鉴权  |  不开启Ranger鉴权  |
| --- | --- | --- |
|  put 5gb file  |  20.238s  |  19.541s  |
|  get 5gb file  |  16.075s  |  15.304s  |
|  put 1000 small files  |  57.721s  |  53.317s  |
|  get 1000 small files  |  19.455s  |  18.912s  |
|  dfsio -write -nrFiles 64 –fileSize 1GB  |  Throughput mb/sec: 23.25  |  Throughput mb/sec: 24.79  |
|  dfsio -read -nrFiles 64 –fileSize 1GB  |  Throughput mb/sec: 21.03  |  Throughput mb/sec: 23.13  |
|  dfsio -write -nrFiles 1024 –fileSize 100M  |  Throughput mb/sec: 4.48  |  Throughput mb/sec: 4.76  |
|  dfsio -read -nrFiles 1024 –fileSize 100M  |  Throughput mb/sec: 4.41  |  Throughput mb/sec: 4.81  |

从测试结果可以看出，OSS-HDFS 开启 Ranger 鉴权后，性能损失在10%之内。