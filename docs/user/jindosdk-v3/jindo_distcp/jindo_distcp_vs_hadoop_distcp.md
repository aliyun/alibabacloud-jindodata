# Jindo DistCp vs Hadoop DistCp 性能测试
# Jindo DistCp介绍

---

Jindo DistCp（分布式文件拷贝工具）是用于大规模集群内部和集群之间拷贝文件的工具。 它使用MapReduce实现文件分发，错误处理和恢复，把文件和目录的列表作为map/reduce任务的输入，每个任务会完成源列表中部分文件的拷贝。全量支持hdfs->oss，hdfs->hdfs，oss->hdfs，oss->oss的数据拷贝场景，提供多种个性化拷贝参数和多种拷贝策略。重点优化hdfs到oss的数据拷贝，通过定制化CopyCommitter，实现No-Rename拷贝，并保证数据拷贝落地的一致性。功能全量对齐S3 DistCp和HDFS DistCp，性能较HDFS DistCp有较大提升，目标提供高效、稳定、安全的数据拷贝工具。 

<a name="j4COM"></a>
# 使用Jindo DistCp

---

Jindo DistCp目前发布在[github repo](jindo_distcp_how_to.md)，我们可以在上面下载jar包进行使用，并且根据上面的使用文档，进行distcp功能使用。

<a name="KWgRo"></a>
# 测试环境

---

master * 1 <br />CPU: 32 核 | 内存: 128G<br />ECS 规格: ecs.g6.8xlarge<br />数据盘配置: ESSD云盘 80GB X 1块<br />系统盘配置: ESSD云盘 120GB X 1块<br />
<br />worker * 2<br />CPU: 32 核 | 内存: 128G<br />ECS 规格: ecs.g6.8xlarge<br />数据盘配置: ESSD云盘 1000GB X 4块<br />系统盘配置: ESSD云盘 120GB X 1块

<a name="Bgty9"></a>
# 软件版本

---

Hadoop版本：2.8.5<br />
<br />jindo-distcp-2.7.3<br />hadoop-distcp-2.8.5

YARN参数

| 参数 | 大小 |
| --- | --- |
| mapreduce.map.memory.mb | 3095 |
| mapreduce.reduce.memory.mb | 3095 |

<a name="zlYH3"></a>
#### 
<a name="t3bHe"></a>
# 测试数据集

---

利用Hadoop自带的测试数据集TestDFSIO分别生成1000个10M、1000个500M、1000个1G大小的文件进行distcp的测试过程。<br />

<a name="KW47P"></a>
# 测试结果

---


<br />从HDFS上拷贝到OSS上，不同场景distcp的性能对比如下<br />


| 单位/s   | 1000 * 10M | 1000 * 500M | 1000 * 1G |
| ---  | --- | --- | --- |
| Jindo DistCp  | 132 | 420 | 813 |
| Hadoop DistCp  | 229 | 1089 | 1900 |
| Jindo DistCp 提升倍数  | 0.75x | 1.59x | 1.33x |


<br />分析测试结果，可以看出Jindo DistCp相比Hadoop DistCp具有较大的性能提升
