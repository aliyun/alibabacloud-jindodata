# JindoDistCp 使用说明

## JindoDistCp 介绍

JindoDistCp 是阿里云数据湖存储团队开发的大规模集群内部和集群之间分布式文件拷贝的工具。其使用 MapReduce 实现文件分发，错误处理和恢复，把文件和目录的列表作为 MapReduce 任务的输入，每个任务会完成源列表中部分文件的拷贝。目前全量支持 HDFS/OSS-HDFS/OSS/S3 之间的的数据拷贝场景，提供多种个性化拷贝参数和多种拷贝策略。重点优化 HDFS 到 OSS-HDFS 的数据拷贝，通过定制化 CopyCommitter，实现 No-Rename 拷贝，并保证数据拷贝落地的一致性。功能全量对齐 S3 DistCp 和 HDFS DistCp，性能较 HDFS DistCp 有较大提升，致力于提供高效、稳定、安全的数据拷贝工具。

## 环境要求

*   JDK 1.8及以上

*   Hadoop 2.3+ 版本，请下载最新版的 jindo-distcp-tool-x.x.x.jar，该 jar 包含在 jindosdk-${version}.tar.gz 内，解压缩后可在 tools/ 目录下找到，[下载页面](../jindosdk/jindosdk_download.md)。

注：E-MapReduce EMR-5.6.0/EMR-3.40.0 或以上版本集群在集群中已部署 JindoDistCp，可在 /opt/apps/JINDOSDK/jindosdk-current/tools 目录下找到 jindo-distcp-tool-x.x.x.jar。

## 参数说明
JindoDistCp 提供 jar 包形式使用，您可以使用 hadoop jar 命令配合一系列参数来完成迁移操作。

| 参数                           | 参数类型 | 说明                                                                                      | 默认值             | 版本     |  OSS  |  OSS-HDFS  |
|------------------------------| -------- |-----------------------------------------------------------------------------------------|-----------------|--------| --- | --- |
| --src <srcDir>               | 必选     | 设置源目录，支持的前缀有<br/>hdfs://<br/>oss://<br/>s3://<br/>cos://<br/>obs://                     | -               | 4.3.0+ |  支持  |  支持  |
| --dest <destDir>             | 必选     | 设置目标目录，支持的前缀有<br/>hdfs://<br/>oss://<br/>s3://<br/>cos://<br/>obs://                    | -               | 4.3.0+ |  支持  |  支持  |
| --bandWidth <bandWidth>      | 可选     | 设置单个节点的带宽限制，单位 M                                                                        | -1              | 4.3.0+ |  支持  |  支持  |
| --codec <codec>              | 可选     | 设置压缩类型，支持编解码器有 gzip、gz、lzo、lzop、lzop、snappy                                             | keep（不更改压缩类型）   | 4.3.0+ |  支持  |  支持  |
| --policy <policy>            | 可选     | 设置目标存储策略，支持 Standard、IA、Archive、ColdArchive                                             | Standard        | 4.3.0+ |  支持  |  不支持  |
| --filters <filters>          | 可选     | 设置包含过滤规则的文件                                                                             | -               | 4.3.0+ |  支持  |  支持  |
| --srcPrefixesFile <prefixes> | 可选     | 设置包含符合规则的文件                                                                             | -               | 4.3.0+ |  支持  |  支持  |
| --parallelism <parallelism>  | 可选     | 设置DistCp任务的并发度，对应MR任务中的 mapreduce.job.maps                                              | 10              | 4.3.0+ |  支持  |  支持  |
| --jobBatch <jobBatch>        | 可选     | 设置每个 distcp job 处理的文件数量                                                                 | 10000           | 4.5.1+ |  支持  |  支持  |
| --taskBatch <taskBatch>      | 可选     | 设置每个 distcp task 处理的文件数量                                                                | 1               | 4.3.0+ |  支持  |  支持  |
| --tmp <tmpDir>               | 可选     | 设置临时目录                                                                                  | /tmp            | 4.3.0+ |  支持  |  支持  |
| --hadoopConf <key=value>     | 可选     | 设置 Configuration                                                                        | -               | 4.3.0+ |  支持  |  支持  |
| --disableChecksum            | 可选     | 设置是否关闭 Checksum 检查                                                                      | false           | 4.3.0+ |  支持  |  支持  |
| --deleteOnSuccess            | 可选     | 设置是否删除源文件，用于移动数据                                                                        | false           | 4.3.0+ |  支持  |  支持  |
| --enableTransaction          | 可选     | 设置是否开启事务，以保证 Job 级别的原子性                                                                 | false           | 4.3.0+ |  支持  |  支持  |
| --ignore                     | 可选     | 设置是否忽略拷贝任务中抛出的异常，避免中断任务                                                                 | false           | 4.3.0+ |  支持  |  支持  |
| --enableCMS                  | 可选     | 是否开启监控告警                                                                                | false           | 4.5.1+ |  支持  |  支持  |
| --diff                       | 可选     | 设置 DistCp 模式为 DistCpMode.DIFF , 查看 src 和 dest 的文件差异                                     | DistCpMode.COPY | 4.3.0+ |  支持  |  支持  |
| --update                     | 可选     | 设置 DistCp 模式为 DistCpMode.UPDATE, 指定增量同步功能，跳过完全相同的文件和目录，直接将 src 中新增或发生改变的文件和目录同步到 dest 上 | DistCpMode.COPY | 4.3.0+ |  支持  |  支持  |
| --overwrite                  | 可选     | 设置 DistCp 模式为 DistCpMode.OVERWRITE, 直接将 src 中文件和目录覆盖同步到 dest 上                          | DistCpMode.COPY | 4.3.0+ |  支持  |  支持  |
| --preserveMeta               | 可选     | 设置是否开启保存元数据信息                                                                           | false           | 4.4.0+ |  不支持  |  支持  |
| --syncSourceDelete           | 可选     | 设置是否开启同步源端子目录下的删除操作                                                                           | false           | 6.9.0+ |  支持  |  支持  |

### 1、使用 --src 和 --dest (必选)

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --src 表示指定源文件的路径。

*   --dest 表示目标文件的路径。

示例命令如下：
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /data/hourly_table  --dest oss://example-oss-bucket/hourly_table
```

您可以通过指定 dest 路径来确定拷贝后的文件层次，如您需要将 /data/hourly_table 下的文件拷贝到 example-oss-bucket 这个 bucket 下的 hourly_table 目录下，则可以使用上述语句来完成。此处和 Hadoop 的 DistCp 行为有所不同，JindoDistCp 会默认将 src 目录下的所有文件拷贝到您指定 dest 路径下，并不包括当前的根目录名称，您可以在 dest 中指定拷贝路径的根目录，如果不存在会自动创建。

注意：如需拷贝单个文件, dest 需指定为目录。
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /test.txt --dest oss://example-oss-bucket/tmp
```

### 2、使用 --bandWidth

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --bandWidth 表示本次distcp任务所用的单机带宽(以MB为单位)，避免单机占用过大带宽。

示例命令如下：
```shell
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --bandWidth 6
```

### 3、使用 --codec

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

原始文件通常以未压缩的文本格式进入 OSS/OSS-HDFS。无论是存储成本还是对该数据进行分析，此格式都不甚理想。JindoDistCp 可以使用 --codec 选项帮助您在线高效地存储数据和压缩文件。

*   --codec 指定文件压缩编解码器。

    *    支持 gzip、gz、lzo、lzop 和 snappy 关键字。

    *    none  保存为未压缩的文件。如果文件已压缩，则 JindoDistCp 会将其解压缩。

    *    keep(默认值) 不更改文件压缩形态，按原样复制。

命令示例如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --codec gz
```

我们检查一下目标文件夹中的文件，这些文件现在已经用 gz 编解码器压缩了：

    [root@emr-header-1 opt]# hdfs dfs -ls oss://example-oss-bucket/hourly_table/2017-02-01/03
    Found 6 items
    -rw-rw-rw-   1        938 2020-04-17 20:58 oss://example-oss-bucket/hourly_table/2017-02-01/03/000151.sst.gz
    -rw-rw-rw-   1       1956 2020-04-17 20:58 oss://example-oss-bucket/hourly_table/2017-02-01/03/1.log.gz
    -rw-rw-rw-   1       1956 2020-04-17 20:58 oss://example-oss-bucket/hourly_table/2017-02-01/03/2.log.gz
    -rw-rw-rw-   1       1956 2020-04-17 20:58 oss://example-oss-bucket/hourly_table/2017-02-01/03/OPTIONS-000109.gz
    -rw-rw-rw-   1        506 2020-04-17 20:58 oss://example-oss-bucket/hourly_table/2017-02-01/03/emp01.txt.gz
    -rw-rw-rw-   1        506 2020-04-17 20:58 oss://example-oss-bucket/hourly_table/2017-02-01/03/emp06.txt.gz

如您在开源Hadoop集群环境中使用lzo压缩功能，则您需要安装 gplcompression 的 native 库和hadoop-lzo包，如您缺少相关环境，建议使用其他压缩方式进行压缩。

### 4、使用 --filters

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --filters 指定有过滤规则的文件。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --filters filter.txt
```

假设 filter.txt 的内容如下：
```
    .*test.*
```

则不会将路径中带有 test 字符的文件拷贝到OSS。

### 5、使用 --srcPrefixesFile

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --srcPrefixesFile 指定有符合规则的文件。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --srcPrefixesFile prefixes.txt
```

假设 prefixes.txt 的内容如下：
```
    .*test.*
```
则只有路径中带有 test 字符的文件才会拷贝到OSS。

### 6、使用 --parallelism

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --parallelism 指定MR任务里的 mapreduce.job.reduces 参数，该参数在EMR环境中默认为7，您可以根据集群的资源情况自定义 parallelism 的大小来控制 distcp 任务的并发度。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /opt/tmp --dest oss://example-oss-bucket/tmp --parallelism 20
```

### 7、使用 --taskBatch

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --taskBatch 指定每个 distcp task 处理的文件数量，默认为10。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --taskBatch 10
```

### 8、使用 --tmp

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --tmp 指定一个临时目录在hdfs上传存放临时数据，默认值为/tmp，即hdfs:///tmp/。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table  --tmp /tmp
```

### 9、配置访问 OSS/OSS-HDFS 的 AK

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

在 EMR 外或免密服务出现问题的情况下，您可以通过指定AK来获得访问 OSS/OSS-HDFS 服务的权限。您可以在命令中使用`--hadoopConf`选项来指定 AK。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret
```

您也可以将 OSS/OSS-HDFS 服务的 ak、secret 预先配置在 Hadoop 的 core-site.xml 文件里 ，避免每次使用时临时填写 ak。您可以在 E-MapReduce 控制台 Hadoop-Common 服务的 core-site.xml 页面配置。

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

### 10、使用 --disableChecksum

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --disableChecksum 关闭检查文件 checksum。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --disableChecksum
```

### 11、使用 --deleteOnSuccess

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --deleteOnSuccess 指定移动数据而不是复制数据。此选项类似于 mv 操作，首先复制文件，然后从源位置删除文件。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --deleteOnSuccess
```

### 12、使用 --enableTransaction

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  不支持  |  支持  |

*   --enableTransaction JindoDistCp 默认使用 Task 级别完整性，如您需要保证 Job 级别的完整性以及保证 Job 之间的事务支持，您可以使用该参数指定。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --enableTransaction
```

### 13、使用 --ignore

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --ignore 忽略数据迁移期间发生的异常，相关报错不会中断任务，并最终以 JindoCounter 的形式透出。（如果开启CMS的话，也会以指定方式进行通知。）

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --ignore
```

### 14、使用 --diff

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --diff 查看 src 和 dest 的文件差异。如果 src 的文件未能同步到 dest 上，则会在当前目录下生成一个包含文件内容差异的文件。如您的 JindoDistCp 任务包含压缩或者解压缩则 --diff 不能显示正确的文件差异，因为压缩或者解压缩会改变文件的大小。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --diff
```

如果文件有差异，则会在当前目录下生成一个包含文件内容差异的文件，并会提示如下信息：
```
        JindoCounter
        DIFF_FILES=1
```
如果您的--dest为HDFS路径，现支持 `/path`，`hdfs://hostname:ip/path` ，`hdfs://headerIp:ip/path`的写法，暂不支持`hdfs:///path`，`hdfs:/path`和其他自定义写法。

如果想查看文件元数据的差异，您可以使用 --diff --preserveMeta 命令：

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --diff --preserveMeta
```

如果文件元数据有差异，则会在当前目录下生成一个包含文件元数据差异的文件，并会提示如下信息：
```
        JindoCounter
        META_FILE_DIFF=1
```

### 15、使用 --update

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  支持  |

*   --update 指定增量同步功能，跳过完全相同的文件和目录，直接将 src 中新增或发生改变的文件和目录同步到 dest 上。

如果 JindoDistCp 任务因为各种原因中间失败了，而此时您想进行断点续传，只 Copy 剩下未 Copy 成功的文件。或者源端文件新增了部分文件，此时需要您在进行上一次 JindoDistCp 任务完成后进行指定该参数。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --update
```

### 16、使用冷归档/归档/低频写入OSS

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.3.0 及以上版本  |  支持  |  不支持  |

*   --update 指定以冷归档、归档和低频的模式写入 OSS，进行数据存储。

1）使用冷归档写入OSS（coldArchive）

目前只在部分region可用，具体参见[OSS存储类型介绍](https://help.aliyun.com/document_detail/51374.html?utm_content=g_1000230851&spm=5176.20966629.toubu.3.f2991ddcpxxvD1#title-o8q-tl3-j65)，使用示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-bucket/hourly_table --policy coldArchive --parallelism 20
```

2）使用归档写入OSS（archive）

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-bucket/hourly_table --policy archive --parallelism 20
```

3）使用低频写入OSS（ia）

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-bucket/hourly_table --policy ia --parallelism 20
```

如您不指定则默认以 standard 标准模式写入，不进行冷归档、归档和低频操作。

### 17、使用 --preserveMeta

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.4.0 及以上版本  |  不支持  |  支持  |

*   --preserveMeta 指定迁移数据的同时迁移包括 Owner, Group, Permission, Atime, Mtime, Replication, BlockSize, XAttrs, ACL 在内的元数据信息。

```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --preserveMeta
```

### 18、使用 --jobBatch

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.5.1 及以上版本  |  支持  |  支持  |

*   --jobBatch 在您的distcp任务写入OSS时，您可以通过--jobBatch来指定每个distcp job处理的文件数量，默认为10000。

示例命令如下：
```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --jobBatch 50000
```

### 19、 使用 --enableCMS

|  版本  |  OSS  |  OSS-HDFS  |
| --- | --- | --- |
|  4.5.1 及以上版本  |  支持  |  支持  |

*   --enableCMS 开启云监控告警功能，具体参见[《JindoDistcp使用CMS进行告警》](jindodistcp_how_to_cms.md)。

### 20、 使用 --syncSourceDelete

| 版本          |  OSS  |  OSS-HDFS  |
|-------------| --- | --- |
| 6.9.0 及以上版本 |  支持  |  支持  |

*   --syncSourceDelete 将同步源端子目录及多级子目录（如--src指定为 /data，则 /data/hourly_table 为子目录）中的文件删除操作。

## JindoDistCp Counters 说明
| 任务计数器          | 说明                                        |
|----------------| ------------------------------------------- |
| COPY_FAILED    | copy失败的文件数  |
| CHECKSUM_DIFF  | checksum校验失败的文件数，并计入COPY_FAILED |
| FILES_EXPECTED | 预期的copy文件数量   |
| BYTES_EXPECTED | 预期的copy字节数 |
| FILES_COPIED   | copy成功的文件数   |
| BYTES_COPIED   | copy成功的字节数 |
| FILES_SKIPPED  | update增量更新时跳过的文件数  |
| BYTES_SKIPPED  | update增量更新时跳过的字节数  |
| DIFF_FILES     | 不相同的文件数 |
| SAME_FILES     | 经校验完全相同的文件数 |
| DST_MISS       | 目标路径不存在的文件数，并计入DIFF_FILES |
| LENGTH_DIFF    | 源文件和目标文件大小不一致的数量，并计入DIFF_FILES |
| CHECKSUM_DIFF  | checksum校验失败的文件数，并计入DIFF_FILES |
| DIFF_FAILED    | diff操作异常的文件数，具体报错参见job日志 |