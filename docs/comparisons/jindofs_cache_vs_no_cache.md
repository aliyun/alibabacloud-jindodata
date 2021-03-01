# JindoFS 缓存系统（Cache 模式）性能比较

## 概述

JindoFS Cache模式主要兼容现有OSS使用方式，文件以对象的形式存储在OSS上，每个文件根据实际访问情况会在本地进行数据和元数据的缓存，从而提高访问数据以及元数据的性能，Cache模式提供不同元数据同步策略以满足用户在不同场景下的需求。Cache模式最大的特点就是兼容性，保持了OSS原有的对象语义，集群中仅做缓存，因此和OSS客户端等其他的各种OSS交互程序是完全兼容的，对原有OSS上的存量数据也不需要做任何的迁移、转换工作即可使用，同时集群中的数据和元数据缓存也能一定程度上提升数据访问性能。

本文主要分析数据缓存对于作业访问OSS所带来的性能提升，通过对比测试在启用数据缓存（w/ cache）和不启用数据缓存（w/o cache）的情况下的计算任务执行情况，验证JindoFS Cache模式对于加速OSS计算性能的作用。
<br />

## 测试用例
TPC-DS采用星型、雪花型等多维数据模式。它包含7张事实表，17张纬度表，平均每张表含有18列。其工作负载包含99个SQL查询，覆盖SQL99和2003的核心部分以及OLAP。这个测试集包含对大数据集的统计、报表生成、联机查询、数据挖掘等复杂应用，测试用的数据和值是有倾斜的，与真实数据一致。可以说TPC-DS是与真实场景非常接近的一个测试集，也是难度较大的一个测试集。Tpcds的99个SQL查询比较耗时，该测试的目的主要是探究JindoFS数据缓存对于加速读OSS数据的作用，因此本测试选择8个具有IO相对较高的SQL查询进行测试，使用Spark计算引擎。

## 测试环境

### 硬件配置

|  |  |
| --- | --- |
| Cluster | 1 master + 5 core |
| ECS实例 | Centos 7.4 64位  <br /> CPU: 64 核 <br /> 内存: 256G <br /> ECS 规格: ecs.i2g.16xlarge <br /> 数据盘配置: 高 I/O 型本地盘 1788GB X 4块 <br /> 系统盘配置: 高效云盘 120GB X 1块 |
| OSS带宽 | 10Gb/20Gb/50Gb |

### 软件配置

|  |  |
| --- | --- |
|spark.driver.cores | 6 |
| spark.driver.memory | 60g |
|spark.executor.memory|20g|
|spark.executor.cores|12|
|spark.executor.instances|50|
|spark.yarn.executor.memoryOverhead|4096|

## 测试结果及分析

以下对TPC-DS的8个IO较重的SQL进行测试，对比数据缓存开关情况下的整体作业执行时长。分别测试OSS带宽为10Gb/20Gb/50Gb三种场景下的性能对比，OSS带宽表示OSS服务端提供给某个用户的带宽上限。在每个场景下分别对比100GB和1TB的TPC-DS数据集。


### 测试结果
* OSS带宽为10Gb下测试结果

<img src="/pic/jindofs_cache_vs_no_cache_1.png" alt="title" width="700"/>

* OSS带宽为20Gb下测试结果

<img src="/pic/jindofs_cache_vs_no_cache_2.png" alt="title" width="700"/>

* OSS带宽为50Gb下测试结果

<img src="/pic/jindofs_cache_vs_no_cache_3.png" alt="title" width="700"/>

### 测试分析

#### OSS服务端流量分析

以10Gb带宽场景测试为例，下图所示为OSS服务端监控的流量情况，可以看到缓存禁用情况下存在较高的流量，打开缓存第一次执行由于本地没有缓存数据所以需要向OSS进行一次拉取，另外由于整体任务执行存在数据重复读取，因此缓存加载之后后续的任务就可以利用到缓存，所以存在一定的OSS流量，但总体流量明显小于缓存禁用的场景。最后当数据已经全部缓存之后，再重复执行测试作业，可以看到完全没有OSS流量，说明所有数据读取均通过了缓存读取。

<img src="/pic/jindofs_cache_vs_no_cache_4.png" alt="title" width="700"/>

同时也可以观察到在OSS访问压力较大的一些时间触发了OSS流控，流控是OSS服务端为避免过大的压力而对客户端请求实施的限流措施，触发流控也会对客户端的OSS访问性能产生较为明显的影响。

<img src="/pic/jindofs_cache_vs_no_cache_5.png" alt="title" width="700"/>

#### ECS端IO分析

以10Gb计算任务Core节点上的IOPS为例，最高为2.9k。测试所用的磁盘为IO增强型最高IOPS可达1788GiB，可以看到IO并不是本地计算性能的瓶颈因素。

<img src="/pic/jindofs_cache_vs_no_cache_6.png" alt="title" width="700"/>

#### ECS端磁盘分析

以10Gb计算任务Task节点上的磁盘平均BPS来看，读写最高为613Mbit/s，磁盘读写任务也很流畅。

<img src="/pic/jindofs_cache_vs_no_cache_7.png" alt="title" width="700"/>

## 总结

利用JindoFS Cache模式进行数据缓存对于OSS读取具有显著的加速作用，在OSS带宽越低的情况下加速作用越明显，测试中在10Gb的OSS带宽中最高可到47%的性能提升。在OSS带宽提高时，提升效果有所降低，是因为在测试用的集群规模下，OSS带宽提升之后逐渐接近集群本地缓存的磁盘带宽，不再是整体作业的关键性瓶颈。当然在实际生产环境下，更大的集群规模和任务并发度也就意味着更高的OSS带宽压力，此时缓存带来的作用尤为显著，可以利用本地的磁盘带宽有效地加速OSS读取，另外也能极大地降低对OSS的访问压力，确保整体表现更加平稳可靠。