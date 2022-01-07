# Jindo DistCp vs Hadoop DistCp Preformance Result
[中文版](jindo_distcp_vs_hadoop_distcp.md)
<a name="3baNh"></a>
# Jindo DistCp Introduction

---

Jindo DistCp (distributed file copy tool) is a tool used to copy files within and between large-scale clusters. It uses MapReduce to distribute files, handle errors, and restore files. The list of files and directories is used as the input of map/reduce tasks. Each task copies some files in the source list. Full support for hdfs->oss,hdfs->hdfs,oss->hdfs,oss->oss data copy scenarios, provides a variety of personalized copy parameters and multiple copy policies. Optimize the data copy from hdfs to oss. Customize CopyCommitter to implement No-Rename copy and ensure the consistency of data copy. Full alignment of S3 DistCp and HDFS DistCp improves performance compared with HDFS DistCp, and aims to provide efficient, stable, and secure data copy tools.

<a name="j4COM"></a>
# Use Jindo DistCp

---

Jindo DistCp is currently released in [github repo](jindo_distcp_how_to_en.md). You can download the jar package to use it, and use the distcp function according to the preceding documentation.

<a name="KWgRo"></a>
# Environment

---

master * 1<br />CPU: 32 cores | Memory: 128GB<br />ECS specification: ecs.g6.8xlarge<br />Data disk configuration: ESSD disk 80GB X 1<br />System disk configuration: ESSD disk 120GB X 1<br />
<br />worker * 2<br />CPU: 32 cores | Memory: 128GB<br />ECS specification: ecs.g6.8xlarge<br />Data disk configuration: ESSD cloud disk 1000GB X 4<br />System disk configuration: ESSD disk 120GB X 1

<a name="Bgty9"></a>
## Software version

---

Hadoop version：2.8.5<br />
<br />jindo-distcp-2.7.3<br />hadoop-distcp-2.8.5

YARN Parameter

| parameter | value |
| --- | --- |
| mapreduce.map.memory.mb | 3095 |
| mapreduce.reduce.memory.mb | 3095 |

<a name="zlYH3"></a>
#### 
<a name="t3bHe"></a>
## Test dataset

---

Use the test dataset TestDFSIO provided by Hadoop to generate 1000 * 10M, 1000 * 500M, and 1000 * 1G files respectively for distcp testing

<a name="KW47P"></a>
## Test results

---

<br />The performance comparison of distcp in different scenarios is as follows:<br />


| Unit/s  | 1000 * 10M | 1000 * 500M | 1000 * 1G |
| ---  | --- | --- | --- |
| Jindo Distcp   | 132 | 420 | 813 |
| Hadoop Distcp  | 229 | 1089 | 1900 |
| Jindo DistCp Increase percentage | 0.75x | 1.59x | 1.33x |



Analysis of the test results shows that Jindo DistCp has a greater performance improvement than Hadoop DistCp
