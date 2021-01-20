# JindoDistCp场景化使用指南

### 阅读使用前须知


1、此文档为用户场景化使用JindoDistCp提供指导，如您已知悉JindoDistCp的所有功能请忽略

2、如您已购买EMR产品并且支持JindoDistCp，您可以使用 jindo distcp 命令来替换以下场景中出现的 hadoop jar jindo-distcp-<version>.jar 命令即无需下载jindo-distcp-<version>.jar，且EMR环境支持OSS免密，关于OSS AK的相关要求可选择不填（如您的EMR版本较老，您也可以选择下载最新的Jar包）


3、环境要求
     1、JDK 1.8+
     2、Hadoop环境
     3、下载jindo-distcp-<version>.jar
如您使用的是Hadoop 2.7+，请[下载](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp-3.0/Hadoop2.7New/jindo-distcp-3.0.0.jar)
如您使用的是Hadoop 3.x，请[下载](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp-3.0/Hadoop3.xNew/jindo-distcp-3.0.0.jar)


### 场景预览


场景1、我想从HDFS导数据到OSS，该使用哪些参数？如果我的数据量很大，文件很多（百万千万级别）该使用哪些参数来优化？

场景2、使用JindoDistCp成功导完数据，我怎么验证数据完整性？

场景3、我想从HDFS导数据到OSS但是我的DistCp任务可能随时失败，想支持断点续传，该使用哪些参数？

场景4、我想从HDFS导数据到OSS，我的DistCp任务能执行成功，但是我的数据不断增量增加，在distcp过程中可能已经产生了新文件，该使用哪些参数？

场景5、我想指定distcp作业在yarn上的队列以及可用带宽，该使用哪些参数？

场景6、我想以低频或者归档形式写到OSS上，该使用哪些参数？


场景7、我大概了解我的数据源情况，比如小文件比例和文件大小情况，该使用哪些参数来优化传输速度？


场景8、我使用S3作为数据源，该使用哪些参数？


场景9、我想把我的文件写入到OSS上并进行压缩(lzo,gz格式等)，该使用哪些参数？


场景10、我想把本次copy中符合特定规则或者同一个父目录下的部分子目录作为copy对象，该使用哪些参数？


场景11、我想把copy的文件中符合一定规则的文件进行合并，减少文件个数，该使用哪些参数？


场景12、我想copy完文件，把原文件都删除，只保留目标文件，该使用哪些参数？


场景13、像OSS AK这种参数，不想执行的时候写在命令行里怎么办？


### 场景1、我想从HDFS导数据到OSS，该使用哪些参数？


如果您不再EMR环境里，那么从HDFS上往OSS传输数据要满足以下几点
1、HDFS可访问，有读数据权限
2、需要提供OSS的AK即accessKey和accessSecret，以及endPoint信息且该AK具有写目标bucket的权限
3、OSS bucket不能为归档类型
4、机器环境可提交MapReduce任务
5、已下载JindoDistCp jar包


一键执行命令：
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --parallelism 10
```


参数解释
--src：源数据目标路径，您可以使用`hdfs://ip:port/path`或者以`/`代表 HDFS 的根目录


--dest：目标路径，您需要手动设定目标路径的根目录，如您需要将HDFS的`/data`目录copy到`oss//bucket/data`下，您需要手动指定`--dest oss://bucket/data`，否则默认将HDFS上的`/data`下所有文件copy到`oss://bucket/`下


`--ossKey`/`--ossSecret`/`--ossEndPoint`：OSS的AK和endpoint信息


`--parallelism`：并行任务的个数，即MapReduce任务的task数量，如您的资源充足可适当调大


如果您的数据量很大，文件数量很多，比如百万千万级别，这个时候您除了可以增大parallelism加大并发度，还可以开启`--enableBatch`参数来进行优化


一键执行命令：
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --parallelism 500 --enableBatch
```




### 场景2、使用JindoDistCp成功导完数据，我怎么验证数据完整性？


使用distcp copy完数据后，如果您想验证下数据是否传输成功，JindoDistCp提供了两种方式帮助您验证数据的完整性，当然您也可以使用其他方式来验证

1、JindoDistCp Counters


您可以在MapReduce任务结束的Counter信息中找到Distcp Counters的信息如：
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
其中
Files Copied：表示成功copy的文件数，
Bytes Source Read：表示源端读文件的字节数大小
Bytes Destination Copied：表示目标端写文件的字节数大小


2、JindoDistCp --diff
您可以使用--diff命令来进行源端和目标端的文件比对，该命令会对文件名和文件大小进行比较，将遗漏或者未成功传输到文件进行记录，存储在提交命令的当前目录下，生成一个manifest文件


在场景一的基础上增加--diff参数即可
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --diff
```


如果全部文件传输成功，您会看到如下提示
```bash
INFO distcp.JindoDistCp: distcp has been done completely
```
### 
### 场景3、我想从HDFS导数据到OSS但是我的DistCp任务可能随时失败，想支持断点续传，该使用哪些参数？


在使用场景1的基础上，如果您的distcp任务因为各种原因中间失败了，而此时您想支持断点续传，只copy剩下未copy成功的文件，此时需要您在进行上一次distcp任务完成后进行如下操作：


```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --diff
```


即src和dest目录不变，增加一个--diff命令


此时如果所有文件都传输完成，那么会提示如下信息，否则在执行目录下生成一个manifest文件

```bash
INFO distcp.JindoDistCp: distcp has been done completely
```


对于生成的manifest文件，您可以使用`--copyFromManifest`和`--previousManifest`命令进行剩余文件的copy

示例命令如下：
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --dest oss://yang-hhht/hourly_table --previousManifest=file:///opt/manifest-2020-04-17.gz --copyFromManifest --parallelism 20
```


即在场景一的基础上增加`--previousManifest=file:///opt/manifest-2020-04-17.gz --copyFromManifest`这两个参数，其中`file:///opt/manifest-2020-04-17.gz`为您当前执行命令的本地路径

### 场景4、我想从HDFS导数据到OSS，我的DistCp任务能执行成功，但是我的数据不断增量增加，在distcp过程中可能已经产生了新文件，该使用哪些参数？


在实际操作中，上游进程会以某种节奏产生新的文件。例如，新文件可能每小时或每分钟创建一次。可以配置下游进程，按不同的日程安排接收文件。假设数据传输到 OSS上，我们希望每天在 HDFS 上对其进行处理。每次复制所有文件并不能很好地扩展。JindoDistCp提供了outputManifest方式来记录当前已完成copy的文件信息。


此时在场景一的基础上，您可以按照如下步骤来完成增量copy


1、未产生上一次copy的文件信息，需要指定生成一个manifest文件，里面记录已完成的文件信息


```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --outputManifest=manifest-2020-04-17.gz --requirePreviousManifest=false --parallelism 20
```


在场景一的基础上需要增加：--outputManifest=manifest-2020-04-17.gz --requirePreviousManifest=false两个信息
--outputManifest：指定生成的manifest文件，文件名称自定义但必须以gz结尾，如manifest-2020-04-17.gz，该文件会存放在--dest指定的目录下
--requirePreviousManifest：无已生成的历史manifest文件信息

2、当上一次distcp任务结束后，源目录可能已经产生了新文件，这时候需要使用一下命令来进行新文件的增量同步


```bash
hadoop jar jindo-distcp-2.7.3.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --outputManifest=manifest-2020-04-18.gz --previousManifest=oss://yang-hhht/hourly_table/manifest-2020-04-17.gz --parallelism 10
```
在场景一的基础上需要增加两个参数：
`--outputManifest=manifest-2020-04-18.gz`  记录本次distcp成功copy的文件
`--previousManifest=oss://yang-hhht/hourly_table/manifest-2020-04-17.gz` 指定上一次distcp成功的文件，本次distcp会过滤掉该文件中的文件


3、重复执行第2步，不断进行增量文件的同步


### 场景5、我想指定distcp作业在yarn上的队列以及可用带宽，该使用哪些参数？


JindoDistCp任务在yarn上提交，如果您想指定特定的yarn队列以及可使用的带宽，可以使用如下命令
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --queue yarnqueue --bandwidth 6 --parallelism 10
```
在场景一的基础上需要增加两个参数，两个参数可以配合使用，也可以单独使用
`--queue`：指定yarn队列的名称
`--bandwidth`：指定带宽的大小，单位为MB大小
### 场景6、我想以低频或者归档形式写到OSS上，该使用哪些参数？


OSS提供归档或者低频类型的文件存储，如果您想将文件以低频或者归档的形式写到OSS上，可以使用如下命令来完成


使用归档示例命令如下：
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --archive --parallelism 20
```
在场景一的基础上增加`--archive`参数


使用低频示例命令如下：
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --ia --parallelism 20
```
在场景一的基础上增加`--ia`参数
### 场景7、我大概了解我的数据源情况，比如小文件比例和文件大小情况，该使用哪些参数来优化传输速度？


1、小文件较多，大文件较大


如果您对要传输的数据源的情况比较了解，比如要copy的所有文件中小文件的占比较高，大文件较少但是单个但文件数据较大的情况下，在正常流程中是按照随机方式来进行copy文件分配，这时如果不去做优化很可能造成一个copy进程分配到大文件的同时也分配到很多小文件，这时候不能发挥最好的性能
这种场景下建议开启`--enableDynamicPlan`参数进行优化
使用命令如下
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --enableDynamicPlan --parallelism 10
```
在场景一的基础上增加--enableDynamicPlan开启优化选项，但不能和--enableBalancePlan一起使用


2、文件总体均衡，大小差不多

如果您要copy的数据里文件大小总体差不多，比较均衡，那么建议开启--enableBalancePlan进行优化
这种场景下建议开启`--enableBalanceDynamicPlan`参数进行优化
使用命令如下
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --enableBalancePlan --parallelism 10
```
在场景一的基础上增加--enableBalancePlan开启优化选项，但不能和--enableDynamicPlan一起使用


### 场景8、我使用S3作为数据源，该使用哪些参数？


如果您想使用AWS S3作为数据源，那么您需要指定AWS S3的相关连接参数，如果您在AWS EMR环境里那么JindoDistCp也支持S3的免密功能


使用命令如下：
```bash
hadoop jar jindo-distcp-<version>.jar --src s3a://yourbucket/ --dest oss://yang-hhht/hourly_table --s3Key yourkey --s3Secret yoursecret --s3EndPoint s3-us-west-1.amazonaws.com --parallelism 10
```
其中需要在场景一的基础上将OSS的相关AK和endPoint参数转换成S3参数
`--s3Key`：连接S3的accessKey
`--s3Secret`：连接S3的accessSecret
`--s3EndPoint`：连接S3的endPoint信息


### 场景9、我想把我的文件写入到OSS上并进行压缩(lzo,gz格式等)，该使用哪些参数？
如果您想将写入的目标文件进行压缩，比如进行lzo，gz等格式，以降低目标文件的存储空间，您可以使用`--outputCodec`参数来完成


使用命令示例如下：
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --outputCodec=gz --parallelism 10
```
需要在场景一的基础上增加--outputCodec参数


JindoDistCp 支持编解码器 gzip、gz、lzo、lzop 和snappy以及关键字 none 和 keep（默认值）。这些关键字含义如下：
"none"– 保存为未压缩的文件。如果文件已压缩，则 jindo distcp 会将其解压缩。
"keep"–不更改文件压缩形态，按原样复制。


如您在开源Hadoop集群环境中使用lzo压缩功能，则您需要去安装gplcompression的native库和hadoop-lzo包，如您缺少相关环境，建议使用其他压缩方式进行压缩。


### 场景10、我想把本次copy中符合特定规则或者同一个父目录下的部分子目录作为copy对象，该使用哪些参数？


如果您想将copy列表中符合一定规则的文件才进行copy，那么您可以使用如下命令
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --srcPattern .*\.log --parallelism 10
```
其中在场景一的基础上需要增加`--srcPattern`参数
`--srcPattern`：进行过滤的正则表达式，符合规则进行copy，否则抛弃


如果您想copy同一个父目录下的部分子目录，那么您可以使用如下命令
```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --srcPrefixesFile file:///opt/folders.txt --parallelism 20
```
在场景一的基础上需要增加`--srcPrefixesFile`参数
`--srcPrefixesFile`：存储需要copy的同父目录的文件夹列表的文件


示例中的folders.txt内容如下
```bash
hdfs://emr-header-1.cluster-50466:9000/data/incoming/hourly_table/2017-02-01
hdfs://emr-header-1.cluster-50466:9000/data/incoming/hourly_table/2017-02-02
```
最终结果将满足前缀列表里的所有文件copy过来

### 场景11、我想把copy的文件中符合一定规则的文件进行合并，减少文件个数，该使用哪些参数？


如果您想把copy的文件按照一定的规则进行合并，并且按照指定的大小合并，从而减少文件个数，那么您可以使用如下命令

```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --targetSize=10 --groupBy='.*/([a-z]+).*.txt' --parallelism 20
```
其中需要在场景一的基础上增加如下参数：
`--targetSize`：合并文件的最大大小，单位M
`--groupBy`：合并规则，正则表达式


### 场景12、我想copy完文件，把原文件都删除，只保留目标文件，该使用哪些参数？


如果您想把distcp任务执行完成后的原文件都删除，那么您可以使用如下命令


```bash
hadoop jar jindo-distcp-<version>.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --deleteOnSuccess --parallelism 10
```
需要在场景一的基础上增加`--deleteOnSuccess`参数


### 场景13、像OSS AK这种参数，不想执行的时候写在命令行里怎么办？


正常情况下您需要将OSS AK和endPoint信息写在参数里，但是JindoDistcp也提供了写在将oss的ak、secret、endpoint预先写在 hadoop的 core-site.xml 文件里 ，避免使用时多次填写AK等


如果您需要保存OSS的Ak相关信息，您需要将以下信息保存在core-site.xml里
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
如果您需要保存S3的AK相关信息，您需要将以下信息保存在core-site.xml里


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


更多场景会后续补充完善，有问题可以通过开 ISSUE 获得支持
