# JindoFS SDK 和Hadoop-OSS-SDK 性能对比测试
[English Version](./jindofs_sdk_vs_hadoop_sdk_en.md)

<a name="6ZORZ"></a>
## JindoFS SDK介绍

JindoFS SDK是一个简单易用的JindoFS客户端，在初期主要用在E-Mapreduce集群内，提供JindoFS集群访问能力和读写OSS文件的能力。JindoFS SDK重新实现了OSS客户端，相比于Hadoop-OSS-SDK做了很多的性能优化。现在，JindoFS SDK对外开放使用，我们可以使用该SDK来获得读写OSS的能力，并获得更好的性能。

<a name="4sw1q"></a>
## 使用 JindoFS SDK

JindoFS SDK目前发布在[github repo](./jindofs_sdk_how_to.md)，我们可以在上面下载jar包进行安装，并且根据上面的使用文档，进行安装部署。<br />
<br />

<a name="MUSUo"></a>
## 测试环境

规格：ecs.c6.8xlarge<br />配置：32 vCPU 64 GiB<br />网卡：10.00Gbps<br />
<br />

<a name="SwQNF"></a>
## 软件版本

jindofs-sdk-2.7.1<br />hadoop-aliyun-3.2.1 (捆绑了aliyun-sdk-oss-3.4.1）<br />
<br />

<a name="zyr0w"></a>
## 测试步骤

我们需要从不同的角度进行测试，需要覆盖读、写、移动、删除等一些操作，并且需要分别测试大文件、小文件下的性能。因此我们准备了3组数据集。<br />数据集1：1个5gb的随机文件<br />数据集2：1000个2bytes的文件 （用于模拟小文件场景）<br />数据集3：1000个5mb的文件<br />
<br />

<a name="ntIJV"></a>
## 测试结果

| 		操作 | Hadoop-OSS-SDK（秒） | JindoFS SDK（秒） | JindoFS SDK提升（百分比） |
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


<br />分析测试结果，可以看出，JindoFS SDK在put、get、mv、delete操作上性能均显著好于Hadoop-OSS-SDK。
