# 使用Jindo DistCp
[English Version](./jindo_distcp_how_to_en.md)
<a name="3baNh"></a>
# 介绍

---

Jindo DistCp（分布式文件拷贝工具）是用于大规模集群内部和集群之间拷贝文件的工具。 它使用MapReduce实现文件分发，错误处理和恢复，把文件和目录的列表作为map/reduce任务的输入，每个任务会完成源列表中部分文件的拷贝。全量支持hdfs->oss，hdfs->hdfs，oss->hdfs，oss->oss的数据拷贝场景，提供多种个性化拷贝参数和多种拷贝策略。重点优化hdfs到oss的数据拷贝，通过定制化CopyCommitter，实现No-Rename拷贝，并保证数据拷贝落地的一致性。功能全量对齐S3 DistCp和HDFS DistCp，性能较HDFS DistCp有较大提升，目标提供高效、稳定、安全的数据拷贝工具。 

您可以使用Jindo DistCp进行hdfs和OSS之间的数据迁移，相对于 Hadoop 社区 DistCp 实现，您可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。<br />
<br />目前支持的 Hadoop 版本包括 Hadoop 2.7+ 和 Hadoop 3.x。有问题请反馈，开 PR，我们会及时处理。<br />
<br />关于 Jindo Distcp 和 Hadoop DistCp 的性能对比，请参考文档[Jindo DistCp和Hadoop DistCp 性能对比测试](./jindo_distcp_vs_hadoop_distcp.md)。<br />

<a name="CLFRq"></a>
# 使用要求

---

1、如您使用的是自建ECS集群，您需要具备Hadoop 2.7+/3.X 环境以及进行MapReduce作业的能力。<br />2、如您使用的是阿里云 E-MapReduce 的产品，您可以在 EMR3.28.0/bigboot2.7.0 版本及以上利用shell命令的方式使用Jindo DistCp，您可以参考该 [文档使用链接](https://help.aliyun.com/document_detail/170712.html?spm=a2c4g.11174283.6.883.759b3d79hrweeG)，如您使用的是 EMR3.28.0 以下的版本，可能会存在一定的兼容性问题，您可以通过内部工单联系我们进行处理。<br />

<a name="dikmJ"></a>
# 下载链接

---

如您使用的是Hadoop 2.7+，请[下载](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp-3.0/Hadoop2.7New/jindo-distcp-3.0.0.jar)<br />
<br />如您使用的是Hadoop 3.x，请[下载](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp-3.0/Hadoop3.xNew/jindo-distcp-3.0.0.jar)<br />

<a name="A1S3E"></a>
# 使用指南

---

Jindo DistCp提供jar包形式使用，您可以使用hadoop jar命令配合一系列参数来完成distcp操作。<br />

<a name="AnGTa"></a>
#### 1、使用--help
```bash
[root@emr-header-1 opt]# hadoop jar jindo-distcp-3.0.0.jar --help
     --help   -   Print help text
     --src=VALUE   -   Directory to copy files from
     --dest=VALUE   -   Directory to copy files to
     --parallelism=VALUE   -   Copy task parallelism
     --outputManifest=VALUE   -   The name of the manifest file
     --previousManifest=VALUE   -   The path to an existing manifest file
     --requirePreviousManifest=VALUE   -   Require that a previous manifest is present if specified
     --copyFromManifest   -   Copy from a manifest instead of listing a directory
     --srcPrefixesFile=VALUE   -   File containing a list of source URI prefixes
     --srcPattern=VALUE   -   Include only source files matching this pattern
     --deleteOnSuccess   -   Delete input files after a successful copy
     --outputCodec=VALUE   -   Compression codec for output files
     --groupBy=VALUE   -   Pattern to group input files by
     --targetSize=VALUE   -   Target size for output files
     --enableBalancePlan   -   Enable plan copy task to make balance
     --enableDynamicPlan   -   Enable plan copy task dynamically
     --enableTransaction   -   Enable transation on Job explicitly
     --diff   -   show the difference between src and dest filelist
     --ossKey=VALUE   -   Specify your oss key if needed
     --ossSecret=VALUE   -   Specify your oss secret if needed
     --ossEndPoint=VALUE   -   Specify your oss endPoint if needed
     --policy=VALUE   -   Specify your oss storage policy
     --cleanUpPending   -   clean up the incomplete upload when distcp job finish
     --queue=VALUE   -   Specify yarn queuename if needed
     --bandwidth=VALUE   -   Specify bandwidth per map/reduce in MB if needed
     --s3Key=VALUE   -   Specify your s3 key
     --s3Secret=VALUE   -   Specify your s3 Sercet
     --s3EndPoint=VALUE   -   Specify your s3 EndPoint
     --enableBatch   -   enbale batch transfer
     --perNum=VALUE   -   batch transfer num size
     --byte=VALUE   -   batch transfer num size
```


<a name="7K1ae"></a>
#### 2、使用--src和--dest (必选)
--src     表示指定源文件的路径<br />--dest   表示目标文件的路径<br />
<br />如从HDFS上/opt/tmp目录拷贝到OSS bucket，可以执行如下语句
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /opt/tmp --dest oss://yang-hhht/tmp
```
您可以通过指定dest路径来确定拷贝后的文件层次，如您需要将/opt/tmp下的文件拷贝到yang-hhht这个bucket下的tmp目录下，则可以使用上述语句来完成。此处和Hadoop的distcp行为有所不同，jindo distcp会默认将src目录下的所有文件拷贝到您指定dest路径下，并不包括当前的根目录名称，您可以在dest中指定拷贝路径的根目录，如果不存在会自动创建。<br />

<a name="L7ugZ"></a>
#### 3、使用--parallelism
parallelism参数用来指定MR任务里的 mapreduce.job.reduces 参数，该参数在EMR环境中默认为7，您可以根据集群的资源情况自定义 parallelism 的大小来控制distcp任务的并发度。<br />
<br />如从HDFS上/opt/tmp目录拷贝到OSS bucket，可以执行<br />

```bash
hadoop jar jindo-distcp-3.0.0.jar --src /opt/tmp --dest oss://yang-hhht/tmp --parallelism 20
```


<a name="IWvV9"></a>
#### 4、使用--srcPattern
srcPattern参数用来使用正则表达式来选择或者过滤需要copy的文件，您可以编写自定义的正则表达式来完成过滤操作，这里的正则表达式必须为全路径正则匹配

如需要copy HDFS 上/data/incoming/hourly_table/2017-02-01/03下所有log文件
```bash
[root@emr-header-1 opt]# hdfs dfs -ls /data/incoming/hourly_table/2017-02-01/03
Found 6 items
-rw-r-----   2 root hadoop       2252 2020-04-17 20:42 /data/incoming/hourly_table/2017-02-01/03/000151.sst
-rw-r-----   2 root hadoop       4891 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/1.log
-rw-r-----   2 root hadoop       4891 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/2.log
-rw-r-----   2 root hadoop       4891 2020-04-17 20:42 /data/incoming/hourly_table/2017-02-01/03/OPTIONS-000109
-rw-r-----   2 root hadoop       1016 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/emp01.txt
-rw-r-----   2 root hadoop       1016 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/emp06.txt
```
可以通过指定srcPattern的正则表达式来进行数据copy
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --srcPattern .*\.log --parallelism 20
```
检查一下目标bucket里的内容是否只copy了以log结尾的文件
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht/hourly_table/2017-02-01/03
Found 2 items
-rw-rw-rw-   1       4891 2020-04-17 20:52 oss://yang-hhht/hourly_table/2017-02-01/03/1.log
-rw-rw-rw-   1       4891 2020-04-17 20:52 oss://yang-hhht/hourly_table/2017-02-01/03/2.log
```


<a name="oXyAI"></a>
#### 5、使用--deleteOnSuccess
有时候我们是想要移动数据而不是复制数据，我们可以使用 --deleteOnSuccess 选项。此选项类似于mv操作，首先复制文件，然后从源位置删除文件。

示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --deleteOnSuccess --parallelism 20
```
执行完语句后即可从源位置删除文件<br />

<a name="d5KKs"></a>
#### 6、使用--outputCodec
原始文件通常以未压缩的文本格式进入OSS或者HDFS。无论是存储成本还是对该数据进行分析，此格式都不甚理想。jindo distcp 可以使用 --outputCodec 选项帮助您在线高效地存储数据和压缩文件

命令示例如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --outputCodec=gz --parallelism 20
```
jindo distcp 的当前版本支持编解码器 gzip、gz、lzo、lzop 和 snappy 以及关键字 **none** 和 **keep**（默认值）。这些关键字含义如下：

- **"none"**– 保存为未压缩的文件。如果文件已压缩，则 jindo distcp 会将其解压缩。
- **"keep"**–不更改文件压缩形态，按原样复制。


<br />我们检查一下目标文件夹中的文件，这些文件现在已经用 gz 编解码器压缩了：
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht/hourly_table/2017-02-01/03
Found 6 items
-rw-rw-rw-   1        938 2020-04-17 20:58 oss://yang-hhht/hourly_table/2017-02-01/03/000151.sst.gz
-rw-rw-rw-   1       1956 2020-04-17 20:58 oss://yang-hhht/hourly_table/2017-02-01/03/1.log.gz
-rw-rw-rw-   1       1956 2020-04-17 20:58 oss://yang-hhht/hourly_table/2017-02-01/03/2.log.gz
-rw-rw-rw-   1       1956 2020-04-17 20:58 oss://yang-hhht/hourly_table/2017-02-01/03/OPTIONS-000109.gz
-rw-rw-rw-   1        506 2020-04-17 20:58 oss://yang-hhht/hourly_table/2017-02-01/03/emp01.txt.gz
-rw-rw-rw-   1        506 2020-04-17 20:58 oss://yang-hhht/hourly_table/2017-02-01/03/emp06.txt.gz
```
如您在开源Hadoop集群环境中使用lzo压缩功能，则您需要去安装gplcompression的native库和hadoop-lzo包，如您缺少相关环境，建议使用其他压缩方式进行压缩。

<a name="azhZZ"></a>
#### 7、使用--outputManifest和--requirePreviousManifest
在使用过程中我们可以指定生成dictcp的清单文件，用来记录copy过程中的目标文件、源文件、数据量大小等信息，如需只生成这样一个清单文件还需要指定requirePreviousManifest参数为flase。当前outputManifest文件默认且必须为gz类型压缩文件，您可以按照自己的业务需求来命名其前缀。
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --outputManifest=manifest-2020-04-17.gz --requirePreviousManifest=false --parallelism 20
```
可以查看生成的outputManifest文件内容
```bash
[root@emr-header-1 opt]# hadoop fs -text oss://yang-hhht/hourly_table/manifest-2020-04-17.gz > before.lst
[root@emr-header-1 opt]# cat before.lst 
{"path":"oss://yang-hhht/hourly_table/2017-02-01/03/000151.sst","baseName":"2017-02-01/03/000151.sst","srcDir":"oss://yang-hhht/hourly_table","size":2252}
{"path":"oss://yang-hhht/hourly_table/2017-02-01/03/1.log","baseName":"2017-02-01/03/1.log","srcDir":"oss://yang-hhht/hourly_table","size":4891}
{"path":"oss://yang-hhht/hourly_table/2017-02-01/03/2.log","baseName":"2017-02-01/03/2.log","srcDir":"oss://yang-hhht/hourly_table","size":4891}
{"path":"oss://yang-hhht/hourly_table/2017-02-01/03/OPTIONS-000109","baseName":"2017-02-01/03/OPTIONS-000109","srcDir":"oss://yang-hhht/hourly_table","size":4891}
{"path":"oss://yang-hhht/hourly_table/2017-02-01/03/emp01.txt","baseName":"2017-02-01/03/emp01.txt","srcDir":"oss://yang-hhht/hourly_table","size":1016}
{"path":"oss://yang-hhht/hourly_table/2017-02-01/03/emp06.txt","baseName":"2017-02-01/03/emp06.txt","srcDir":"oss://yang-hhht/hourly_table","size":1016}
```


<a name="RtNQn"></a>
#### 8、使用--outputManifest和--previousManifest
在实际操作中，上游进程会以某种节奏产生新的文件。例如，新文件可能每小时或每分钟创建一次。可以配置下游进程，按不同的日程安排接收文件。假设数据传输到 OSS上，我们希望每天在 HDFS 上对其进行处理。每次复制所有文件并不能很好地扩展。Jindo distcp 内置了应对此问题的解决方案，对于此解决方案，我们使用清单文件。<br />如在源文件夹中新增加了两个文件，以下是命令示例：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --outputManifest=manifest-2020-04-18.gz --previousManifest=oss://yang-hhht/hourly_table/manifest-2020-04-17.gz --parallelism 20
```
该命令将两个清单文件视为参数：outputManifest 和 previousManifest。第一个包含所有已复制文件（旧文件和新文件）的列表，第二个包含之前复制的文件的列表。如此一来，我们可以重新创建完整的操作历史记录，并查看每次运行期间复制了哪些文件：
```bash
[root@emr-header-1 opt]# hadoop fs -text oss://yang-hhht/hourly_table/manifest-2020-04-18.gz > current.lst
[root@emr-header-1 opt]# diff before.lst current.lst 
3a4,5
> {"path":"oss://yang-hhht/hourly_table/2017-02-01/03/5.log","baseName":"2017-02-01/03/5.log","srcDir":"oss://yang-hhht/hourly_table","size":4891}
> {"path":"oss://yang-hhht/hourly_table/2017-02-01/03/6.log","baseName":"2017-02-01/03/6.log","srcDir":"oss://yang-hhht/hourly_table","size":4891}
```
jindo distcp 使用路径 /tmp/mymanifest.gz 在本地文件系统中创建文件。复制操作完成后，它会将清单文件移到dest目录下<br />

<a name="BkPxT"></a>
#### 9、使用--copyFromManifest
使用outputManifest功能生成manifest文件后，您可以通过指定固定的manifest文件来进行文件的拷贝，manifest文件中记录了相关文件信息，从而不需要去从src目录中获取数据信息，您只需要把dest目录指定为需要拷贝的目录即可，即可把上次拷贝生成的manifest文件中包含的文件拷贝到新的目录下。

示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --previousManifest=oss://yang-hhht/hourly_table/manifest-2020-04-17.gz --copyFromManifest --parallelism 20
```


<a name="Cw3ym"></a>
#### 10、使用--**srcPrefixesFile**
假设我们需要复制多个文件夹。通常，我们运行的复制作业与需要复制的文件夹一样多。使用 jindo distcp，可以一次性完成复制。我们只需准备一个带有前缀列表的文件，并将其用作工具参数。<br />命令示例如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --srcPrefixesFile file:///opt/folders.txt --parallelism 20
```
folders文件的内容如下：
```bash
[root@emr-header-1 opt]# cat folders.txt 
hdfs://emr-header-1.cluster-50466:9000/data/incoming/hourly_table/2017-02-01
hdfs://emr-header-1.cluster-50466:9000/data/incoming/hourly_table/2017-02-02
```
最终结果将满足前缀列表里的所有文件复制过来
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht/hourly_table
Found 4 items
drwxrwxrwx   -          0 1970-01-01 08:00 oss://yang-hhht/hourly_table/2017-02-01
drwxrwxrwx   -          0 1970-01-01 08:00 oss://yang-hhht/hourly_table/2017-02-02
```
<br />

#### 11、使用--groupBy和-targetSize
经过优化后，Hadoop 可以从HDFS 中读取较少数量的大文件，而不再读取大量小文件。您可以使用 jindo distcp将小文件聚合为较少的指定大小的大文件，这样可以优化分析性能和成本。<br />在下面的示例中，我们将小文件合并为较大的文件。为此，我们使用带有 --groupBy 选项的正则表达式。

如我们需要将所有txt按照最大10M为一个文件进行合并，示例命令如下
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --targetSize=10 --groupBy='.*/([a-z]+).*.txt' --parallelism 20
```
原文件夹中有两个txt类型的文件
```bash
[root@emr-header-1 opt]# hdfs dfs -ls /data/incoming/hourly_table/2017-02-01/03
Found 8 items
-rw-r-----   2 root hadoop       2252 2020-04-17 20:42 /data/incoming/hourly_table/2017-02-01/03/000151.sst
-rw-r-----   2 root hadoop       4891 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/1.log
-rw-r-----   2 root hadoop       4891 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/2.log
-rw-r-----   2 root hadoop       4891 2020-04-17 21:08 /data/incoming/hourly_table/2017-02-01/03/5.log
-rw-r-----   2 root hadoop       4891 2020-04-17 21:08 /data/incoming/hourly_table/2017-02-01/03/6.log
-rw-r-----   2 root hadoop       4891 2020-04-17 20:42 /data/incoming/hourly_table/2017-02-01/03/OPTIONS-000109
-rw-r-----   2 root hadoop       1016 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/emp01.txt
-rw-r-----   2 root hadoop       1016 2020-04-17 20:47 /data/incoming/hourly_table/2017-02-01/03/emp06.txt
```
经过合并后，可以看到两个txt文件被合并成了一个文件
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht/hourly_table/2017-02-01/03/
Found 1 items
-rw-rw-rw-   1       2032 2020-04-17 21:18 oss://yang-hhht/hourly_table/2017-02-01/03/emp2
```
如您所见，我们将两个txt数据文件合并为一个不超过 10MB（要求的大小）的文件。其他文件已被筛选掉，因为 --groupBy 模式的工作方式与 --srcPattern 类似。我们建议文件大小超过默认的数据块大小，最终文件的名称由 --groupBy 中使用的正则表达式中的组加某些数字组成，以确保名称的唯一性，所以使用该模式必须至少定义一个组。您偶尔可能会收到以下错误：
```bash
20/04/17 21:24:24 INFO distcp.JindoDistCp: Created 0 files to copy 0 files 
20/04/17 21:24:24 INFO distcp.JindoDistCp: Found 0 source files cost 22 ms
20/04/17 21:24:24 INFO common.AbstractJindoFileSystem: Jboot log name is /var/log/bigboot/jboot-INFO-1587129864421-
20/04/17 21:24:24 INFO oss.JindoOssFileSystem: Filesystem support for magic committers is enabled
20/04/17 21:24:24 INFO common.FsStats: cmd=mkdirs, src=oss://yang-hhht/hourly_table, dst=null, size=0, parameter=FsPermission:rwxrwxrwx, time-in-ms=269, version=2.6.0
20/04/17 21:24:24 INFO distcp.JindoDistCp: use CopyOutputFormat with CopyCommitter
20/04/17 21:24:24 INFO distcp.JindoDistCp: Reducer number: 7
20/04/17 21:24:24 INFO client.RMProxy: Connecting to ResourceManager at emr-header-1.cluster-50466/192.168.1.228:8032
20/04/17 21:24:25 INFO mapreduce.JobSubmitter: Cleaning up the staging area /tmp/hadoop-yarn/staging/root/.staging/job_1587045773511_0035
20/04/17 21:24:25 INFO distcp.JindoDistCp: Try to recursively delete hdfs:/tmp/e5604566-8f3b-4a04-9787-ff26c75df440/tempspace
Exception in thread "main" java.lang.RuntimeException: Error running job
     at com.aliyun.emr.jindo.distcp.JindoDistCp.run(JindoDistCp.java:718)
     at com.aliyun.emr.jindo.distcp.JindoDistCp.run(JindoDistCp.java:518)
     at org.apache.hadoop.util.ToolRunner.run(ToolRunner.java:76)
     at com.aliyun.emr.jindo.distcp.Main.main(Main.java:37)
```
在本例中，重要信息包含在distcp.JindoDistCp: Created 0 files to copy 0 files 中。jindo distcp 找不到要复制的文件，因为 --groupBy 选项中的正则表达式与源位置的任何文件都不匹配。

<a name="zJOc9"></a>
#### 12、使用--enableBalancePlan
在您要拷贝的数据整体大小比较均匀的情况下，您可以指定--enableBalancePlan参数来更改Jindo distcp的作业分配计划。使用该计划让各个task的处理的数据量均匀，使其达到更好的distcp性能。<br />
<br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableBalancePlan --parallelism 20
```

<br />该参数不支持和--groupby和--targetSize参数一起使用。<br />

<a name="oM2mZ"></a>
#### 13、使用--enableDynamicPlan
在您要拷贝的数据大小分化严重，小文件数据较多的场景下，您可以指定--enableDynamicPlan参数来更改Jindo distcp的作业分配计划，使其达到更好的distcp性能。<br />
<br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableDynamicPlan --parallelism 20
```
该参数不支持和--groupby和--targetSize参数一起使用。<br />

<a name="ePqFH"></a>
#### 14、使用--**enableTransaction**
Jindo distcp默认使用task级别完整性，如您需要保证Job级别的完整性以及保证Job之间的事务支持，您可以使用--enableTransaction参数。<br />
<br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableTransaction --parallelism 20
```


<a name="pjlfG"></a>
#### 15、使用--diff
在您的distcp任务完成后您可以通过指定src和dest来查看当前distcp的文件差异，如果src的文件未能同步到dest上，则会在当前目录下生成一个manifest文件，您可以配合使用--copyFromManifest参数来进行剩余文件的拷贝，从而完成数据大小和文件个数的校验。如您的distcp任务包含压缩或者解压缩则--diff不能显示正确的文件差异，因为压缩或者解压缩会改变文件的大小。<br />
<br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --diff
```


如果全部任务完成则会提示如下信息，否则在执行目录下生成manifest文件

```bash
INFO distcp.JindoDistCp: distcp has been done completely
```


如果您的--dest为HDFS路径，现支持/path，hdfs://hostname:ip/path ，hdfs://headerIp:ip/path的写法，暂不支持hdfs:///path，hdfs:/path和其他自定义写法。

对于生成的manifest文件，您可以使用--copyFromManifest和--previousManifest命令进行剩余文件的copy

示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --dest oss://yang-hhht/hourly_table --previousManifest=file:///opt/manifest-2020-04-17.gz --copyFromManifest --parallelism 20
```
<br />

#### 16、查看Distcp Counters
您可以在MapReduce的Counter信息中找到Distcp Counters的信息如：
```bash
Distcp Counters
          Bytes Destination Copied=11010048000
          Bytes Source Read=11010048000
          Files Copied=1001
    
Shuffle Errors
          BAD_ID=0
          CONNECTION=0
          IO_ERROR=0
          WRONG_LENGTH=0
          WRONG_MAP=0
          WRONG_REDUCE=0
```
如您的distcp操作中包含压缩或者解压缩文件，那么Bytes Destination Copied和Bytes Source Read的大小可能是不相等的<br />


#### 17、使用OSS AK
在EMR外或者免密服务出现问题的情况下，您可以通过指定AK来获得访问OSS的权限。您可以在命令中使用<br />--ossKey、--ossSecret、--ossEndPoint选项来指定AK。<br />
<br />示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --parallelism 20
```


您也可以将oss的ak、secret、endpoint预先配置在 hadoop的 core-site.xml 文件里 ，避免每次使用时临时填写ak。
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss-accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss-accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss-endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

<br />另外，我们推荐配置[免密功能](https://help.aliyun.com/document_detail/156418.html)，避免明文保存accessKey，提高安全性。<br />

<a name="IMjaY"></a>
#### 18、以归档/低频写入OSS
在您的distcp任务写入OSS时，您可以通过--archive和--ia来分别指定以归档和低频的模式写入OSS，进行数据存储。<br />
<br />使用归档（archive）示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --archive --parallelism 20
```
使用低频（ia）示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ia --parallelism 20
```
如您不指定则默认以standard及标准模式写入，不进行归档和低频操作<br />

<a name="UEx2K"></a>
#### 19、清理残留文件
在您的distcp完成过程中，可能因为多种原因在您的目标目录下产生未正确上传的文件，这部分文件通过uploadId的方式由OSS管理，并且对用户不可见，您可以通过指定--cleanUpPending选项进行指定distcp任务结束时进行清理残留文件，或者您也可以通过OSS控制台来进行清理。<br />
<br />示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --cleanUpPending --parallelism 20
```


<a name="7WRjP"></a>
#### 20、使用--queue
在您的distcp过程中，您可以指定本次distcp任务所在的yarn队列的名称，您可以使用--queue来指定

示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --queue yarnqueue
```


<a name="0TVlg"></a>
#### 21、使用--bandwidth
在您的distcp过程中，您可以指定本次distcp任务所用的带宽(以MB为单位)，避免占用过大带宽<br />
<br />示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --bandwidth 6
```


<a name="UjU6Y"></a>
#### 22、使用s3作为数据源
您可以指定数据源为s3，目前支持前缀s3a/s3n/s3，您可以在命令中使用--s3Key、--s3Secret、--s3EndPoint选项来指定连接s3的相关信息。您也可以只指定s3EndPoint来使用s3的免密功能。<br />
<br />示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src s3a://yourbucket/ --dest oss://yang-hhht/hourly_table --s3Key yourkey --s3Secret yoursecret --s3EndPoint s3-us-west-1.amazonaws.com 
```

<br />您也可以将s3的key、secret、endpoint预先配置在 hadoop的 core-site.xml 文件里 ，避免每次使用时临时填写ak。
```xml
<configuration>
    <property>
        <name>fs.s3a.access.key</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.s3a.secret.key</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.s3.endpoint</name>
        <value>s3-us-west-1.amazonaws.com</value>
    </property>
</configuration>
```

<br />如使用S3免密则示例命令如下，您无需指定AK，但需要指定endPoint<br />

```bash
hadoop jar /tmp/jindo-distcp-3.0.0.jar --src s3://smartdata1/ --dest s3://smartdata1/tmp --s3EndPoint  s3-us-west-1.amazonaws.com
```
<a name="tqzlD"></a>
#### <br />
<a name="h9wI9"></a>
#### 23、使用低版本的JDK
当前 Jindo DistCp 默认使用的JDK版本是1.8，如您使用1.8以下的JDK，您可以尝试使用指定YARN JDK包的方式来使用Jindo DistCp

1、下载JDK 8<br />for Linux 64x<br />[链接](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/JDK8/jdk-8u251-linux-x64.tar.gz)

2、解压jdk-8u251-linux-x64.tar.gz，指定JAVA_HOME

```bash
[ec2-user@ip jdk1.8.0_251]$ pwd
/home/ec2-user/jdk1.8.0_251
[ec2-user@ip jdk1.8.0_251]$ export JAVA_HOME=`pwd`
```
将jdk-8u251-linux-x64.tar.gz放到tmp/下
```bash
[ec2-user@ip]$ cp jdk-8u251-linux-x64.tar.gz /tmp
```


3、修改mapred-site.xml

```bash
</configuration>
  <property>
    <name>mapred.child.env</name>
    <value>JAVA_HOME=./jdk-8u251-linux-x64.tar.gz/jdk1.8.0_251</value>
  </property>
    <property>
    <name>yarn.app.mapreduce.am.env</name>
    <value>JAVA_HOME=./jdk-8u251-linux-x64.tar.gz/jdk1.8.0_251</value>
  </property>
    <property>
    <name>tmparchives</name>
    <value>file:///tmp/jdk-8u251-linux-x64.tar.gz</value>
  </property>
</configuration>
```

#### 24、使用--enableBatch，--perNum，--byte
在上传文件到OSS时，JindoDistCp默认使用的magicJobCommiter对小文件传输不太友好，当您的传输文件数据量较大且小文件数量较多时可以使用enableBatch参数来分批传输小文件，使用MR自带的jobCommiter进行小文件的传输，而对大文件依然使用优化的后的magicJobCommiter<br/>

示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableBatch --parallelism 20
```

您也可以指定--perNum参数来指定分批次传输的批次大小，默认为100000。通过--byte参数来指定小于多大的文件是属于小文件范畴，默认小于8M为小文件，单位M

示例命令如下：
```bash
hadoop jar jindo-distcp-3.0.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableBatch --perNum 100 --byte 6 --parallelism 20
```

<a name="WwYXi"></a>
# 发布版本

---

<a name="TqRR6"></a>
### v3.0.0
日期：20210120
