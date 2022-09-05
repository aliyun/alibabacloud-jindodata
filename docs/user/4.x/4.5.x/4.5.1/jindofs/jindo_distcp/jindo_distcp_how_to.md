# 使用 JindoDistCp
## 介绍

JindoDistCp（分布式文件拷贝工具）是用于大规模集群内部和集群之间拷贝文件的工具。 它使用 MapReduce 实现文件分发，错误处理和恢复，把文件和目录的列表作为 MapReduce 任务的输入，每个任务会完成源列表中部分文件的拷贝。全量支持 HDFS->OSS-HDFS 服务，HDFS->HDFS，OSS-HDFS 服务->HDFS，OSS-HDFS 服务->OSS-HDFS 服务，提供多种个性化拷贝参数和多种拷贝策略。重点优化 HDFS 到 OSS-HDFS 服务的数据拷贝，通过定制化CopyCommitter，实现No-Rename拷贝，并保证数据拷贝落地的一致性。功能全量对齐S3 DistCp和HDFS DistCp，性能较HDFS DistCp有较大提升，目标提供高效、稳定、安全的数据拷贝工具。

您可以使用 JindoDistCp 进行 HDFS 和 OSS-HDFS 服务之间的数据迁移，相对于 Hadoop 社区 DistCp 实现，您可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

## 使用指南

JindoDistCp 提供 jar 包形式使用，您可以使用 hadoop jar 命令配合一系列参数来完成 distcp 操作。
### 1、使用--src和--dest (必选)
* --src 表示指定源文件的路径

* --dest 表示目标文件的路径

如从HDFS上/opt/tmp目录拷贝到OSS-HDFS 服务 bucket，可以执行如下语句

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /opt/tmp --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/tmp
```
您可以通过指定dest路径来确定拷贝后的文件层次，如您需要将/opt/tmp下的文件拷贝到yang-hhht这个bucket下的tmp目录下，则可以使用上述语句来完成。此处和Hadoop的distcp行为有所不同，JindoDistCp会默认将src目录下的所有文件拷贝到您指定dest路径下，并不包括当前的根目录名称，您可以在dest中指定拷贝路径的根目录，如果不存在会自动创建。

注意：如需拷贝单个文件, dest需指定为目录。
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /test.txt --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/tmp
```

### 2、使用--bandWidth
在您的distcp过程中，您可以指定本次distcp任务所用的单机带宽(以MB为单位)，避免单机占用过大带宽<br /><br />示例命令如下：

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --bandWidth 6
```

### 3、使用--codec
原始文件通常以未压缩的文本格式进入OSS或者HDFS。无论是存储成本还是对该数据进行分析，此格式都不甚理想。JindoDistCp 可以使用 --codec 选项帮助您在线高效地存储数据和压缩文件

命令示例如下：
```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --codec gz --parallelism 20
```
JindoDistCp 的当前版本支持编解码器 gzip、gz、lzo、lzop 和 snappy 以及关键字 *none* 和 *keep*（默认值）。这些关键字含义如下：

- *"none"*– 保存为未压缩的文件。如果文件已压缩，则 JindoDistCp 会将其解压缩。
- *"keep"*–不更改文件压缩形态，按原样复制。

### 4、使用--filters
在您的distcp任务写入OSS时，您可以通过--filters来指定有过滤规则的文件。

示例命令如下：

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --filters filter.txt
```
假设 filter.txt 的内容如下：
```bash
.*test.*
```
则不会将路径中带有`test`的文件拷贝到OSS。

### 5、使用--srcPrefixesFile
在您的distcp任务写入OSS时，您可以通过--srcPrefixesFile来指定有符合规则的文件。

示例命令如下：

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --srcPrefixesFile prefixes.txt
```
假设 prefixes.txt 的内容如下：
```bash
.*test.*
```
则只有路径中带有`test`的文件才会拷贝到OSS。

### 6、使用--parallelism
parallelism 参数用来指定MR任务里的 mapreduce.job.reduces 参数，该参数在EMR环境中默认为7，您可以根据集群的资源情况自定义 parallelism 的大小来控制 distcp 任务的并发度。

如从HDFS上/opt/tmp目录拷贝到OSS bucket，可以执行

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /opt/tmp --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/tmp --parallelism 20
```

### 7、使用--taskBatch
在您的distcp任务写入OSS时，您可以通过--taskBatch来指定每个distcp task处理的文件数量，默认为10。

示例命令如下：

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --taskBatch 10
```

### 8、使用--tmp
您可以通过tmp参数指定一个临时目录在hdfs上传存放临时数据，默认值为/tmp，即hdfs:///tmp/。<br /><br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --parallelism 20 --tmp /tmp
```

### 9、使用 OSS-HDFS 服务 AK
* 在 EMR 外或者免密服务出现问题的情况下，您可以通过指定AK来获得访问 OSS-HDFS 服务的权限。您可以在命令中使用`--hadoopConf`选项来指定AK。
  <br />示例命令如下：

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --parallelism 20
```

您也可以将 OSS-HDFS 服务的 ak、secret 预先配置在 hadoop 的`core-site.xml`文件里 ，避免每次使用时临时填写ak。

```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
</configuration>
```

### 10、使用--disableChecksum
在您的distcp任务写入OSS时，您可以通过--disableChecksum关闭检查文件checksum。

示例命令如下：
```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --disableChecksum
```

### 11、使用--deleteOnSuccess
有时候我们是想要移动数据而不是复制数据，我们可以使用 --deleteOnSuccess 选项。此选项类似于mv操作，首先复制文件，然后从源位置删除文件。

示例命令如下：
```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --deleteOnSuccess --parallelism 20
```
执行完语句后即可从源位置删除文件<br />


<br />我们检查一下目标文件夹中的文件，这些文件现在已经用 gz 编解码器压缩了：
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table/2017-02-01/03
Found 6 items
-rw-rw-rw-   1        938 2020-04-17 20:58 oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table/2017-02-01/03/000151.sst.gz
-rw-rw-rw-   1       1956 2020-04-17 20:58 oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table/2017-02-01/03/1.log.gz
-rw-rw-rw-   1       1956 2020-04-17 20:58 oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table/2017-02-01/03/2.log.gz
-rw-rw-rw-   1       1956 2020-04-17 20:58 oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table/2017-02-01/03/OPTIONS-000109.gz
-rw-rw-rw-   1        506 2020-04-17 20:58 oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table/2017-02-01/03/emp01.txt.gz
-rw-rw-rw-   1        506 2020-04-17 20:58 oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table/2017-02-01/03/emp06.txt.gz
```
如您在开源Hadoop集群环境中使用lzo压缩功能，则您需要去安装gplcompression的native库和hadoop-lzo包，如您缺少相关环境，建议使用其他压缩方式进行压缩。

### 12、使用--enableTransaction
JindoDistCp默认使用task级别完整性，如您需要保证Job级别的完整性以及保证Job之间的事务支持，您可以使用--enableTransaction参数。<br /><br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --enableTransaction --parallelism 20
```

### 13、使用--ignore

您可以通过ignore参数，忽略数据迁移期间发生的异常，相关报错不会中断任务，并最终以DistCp Counter的形式透出。（如果开启CMS的话，也会以指定方式进行通知。）<br /><br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --ignore --parallelism 20
```

### 14、使用--diff
在您的distcp任务完成后您可以通过指定src和dest来查看当前distcp的文件差异, 如果src的文件未能同步到dest上，则会在当前目录下生成一个包含文件内容差异的文件。如您的distcp任务包含压缩或者解压缩则--diff不能显示正确的文件差异，因为压缩或者解压缩会改变文件的大小。<br /><br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --diff
```

如果文件有差异，则会在当前目录下生成一个包含文件内容差异的文件，并会提示如下信息：

```bash
	JindoCounter
		DIFF_FILES=1
```

如果您的--dest为HDFS路径，现支持`/path`，`hdfs://hostname:ip/path` ，`hdfs://headerIp:ip/path`的写法，暂不支持`hdfs:///path`，`hdfs:/path`和其他自定义写法。

如果想查看文件元数据的差异，您可以使用 --diff --preserveMeta 命令：

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --diff --preserveMeta
```

如果文件元数据有差异，则会在当前目录下生成一个包含文件元数据差异的文件，并会提示如下信息：

```bash
	JindoCounter
		META_FILE_DIFF=1
```

### 15、使用--update

您可以通过update参数使用增量同步功能，跳过完全相同的文件和目录，直接将src中新增或发生改变的文件和目录同步到dest上。<br /><br />示例命令如下：<br />

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/hourly_table --update --parallelism 20
```

### 16、使用--preserveMeta

您可以通过preserveMeta参数, 使得迁移数据的同时迁移包括 Owner, Group, Permission, Atime, Mtime, Replication, BlockSize, XAttrs, ACL 在内的元数据信息。

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /opt/tmp --dest oss://yang-hhht.cn-xxx.oss-dls.aliyuncs.com/tmp --preserveMeta
```

### 17、使用--jobBatch
在您的distcp任务写入OSS时，您可以通过--jobBatch来指定每个distcp job处理的文件数量，默认为10000。

示例命令如下：

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --jobBatch 50000
```