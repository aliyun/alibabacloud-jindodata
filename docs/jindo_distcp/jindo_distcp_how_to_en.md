# Jindo DistCp User Guide
[中文版](jindo_distcp_how_to.md)
<a name="3baNh"></a>
# Introduction

---

Jindo DistCp (distributed file copy tool) is a tool for copying files within and between large-scale clusters. It uses MapReduce to implement file distribution, error handling and recovery, and takes the list of files and directories as the input of map/reduce tasks. Each task will copy some files in the source list. Fully support hdfs->oss, hdfs->hdfs, oss->hdfs, oss->oss data copy scenarios, and provide a variety of personalized copy parameters and multiple copy strategies. Focus on optimizing the data copy from hdfs to oss with customized CopyCommitter、realize No-Rename copy and ensure the consistency of data copy. The functions are fully aligned with S3 DistCp and HDFS DistCp, However Jindo DistCp's performance is greatly improved compared with HDFS DistCp. it aims to provide efficient, stable and safe tool for data copy.<br />
<br />You can use Jindo DistCp to migrate data between hdfs and OSS. Compared with the implementation of DistCp in the Hadoop community, you can get better performance and more professional support from the Alibaba Cloud E-MapReduce team.<br />
<br />Currently supported Hadoop versions include Hadoop 2.7+ and Hadoop 3.x. If you have any questions, please give us feedback and open a PR, we will deal with it in time.<br />
<br />For the performance comparison between Jindo Distcp and Hadoop DistCp, please refer to the document [Jindo DistCp and Hadoop DistCp Performance Comparison Test](./jindo_distcp_vs_hadoop_distcp.md).<br />

<a name="CLFRq"></a>
# Requirements

---

1. If you are using a self-built ECS cluster, you need to have a Hadoop 2.7+/3.X environment and the ability to perform MapReduce operations.<br />
2. If you are using Alibaba Cloud E-MapReduce products, you can use Jindo DistCp in EMR3.28.0/bigboot2.7.0 version and above by using shell commands. You can refer to the document using the [link](https://help.aliyun.com/document_detail/170712.html?spm=a2c4g.11174283.6.883.759b3d79hrweeG), if you are using Versions below EMR3.28.0 may have certain compatibility issues. You can contact us through an internal ticket for processing.<br />

<a name="dikmJ"></a>
# Download link

---


<br />If you are using Hadoop 2.7+, please [download](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp-3.0/Hadoop2.7New/jindo-distcp-3.4.0.jar)<br />
<br />If you are using Hadoop 3.x, please [download](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp-3.0/Hadoop3.xNew/jindo-distcp-3.4.0.jar)<br />

<a name="WpDlv"></a>
# User Guide

---

Jindo DistCp provides a jar package for use. You can use the hadoop jar command with a series of parameters to complete the distcp operation.
<a name="AnGTa"></a>
#### 
<a name="LOd8o"></a>
#### 1、use --help
```bash
[root@emr-header-1 opt]# hadoop jar jindo-distcp-3.4.0.jar --help
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
     --perNum=VALUE   -   batch transfer num size
     --byte=VALUE   -   batch transfer num size
     --disableChecksum   -   disable checksum
```


<a name="7K1ae"></a>
#### 2、use --src and --dest (required)

<br />--src     Indicates the path of the specified source path<br />--dest   Indicates the path of the target path<br />
If you copy the/opt/tmp directory from HDFS to an OSS bucket, run the following statement:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /opt/tmp --dest oss://yang-hhht/tmp
```

You can specify the dest path to determine the level of the copied file. For example, if you need to copy the files under /opt/tmp to the oss://yang-hhht/tmp bucket, you can use the example statement. This is different from the distcp behavior of Hadoop. jindo distcp copies all files in the src directory to the dest path you specify by default, excluding the current root directory name, you can specify the root directory of the copy path in dest. If it does not exist, it is automatically created.<br />

<a name="L7ugZ"></a>
#### 3、use --parallelism
<br />The parameter is used to specify the map or reduce parallelism num, which defaults to 7 in the EMR environment. You can set the parallelism based on the cluster resources to control the concurrency of distcp tasks.<br />

<br />If you copy the/opt/tmp directory from HDFS to an OSS bucket, you can run<br />

```bash
hadoop jar jindo-distcp-3.4.0.jar --src /opt/tmp --dest oss://yang-hhht/tmp --parallelism 20
```


<a name="IWvV9"></a>
#### 4、use --srcPattern

<br />The srcPattern parameter is used to use a regular expression to select or filter files to be copied. You can write a custom regular expression to complete the filtering operation. The regular expression must be a full-path regular match.

if you want to copy all log files under /data/incoming/hourly_table/2017-02-01/03 on HDFS，you can run
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
You can copy data by specifying the regular expression of srcPattern.
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --srcPattern .*\.log --parallelism 20
```
Check whether only files ending with log are copied in the target bucket.
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht/hourly_table/2017-02-01/03
Found 2 items
-rw-rw-rw-   1       4891 2020-04-17 20:52 oss://yang-hhht/hourly_table/2017-02-01/03/1.log
-rw-rw-rw-   1       4891 2020-04-17 20:52 oss://yang-hhht/hourly_table/2017-02-01/03/2.log
```


<a name="oXyAI"></a>
#### 5、use --deleteOnSuccess
Sometimes we want to move data instead of copying data. We can use the -- deleteOnSuccess option. This option is similar to mv operation. First, copy the file and then delete the file from the source location.

The sample command as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --deleteOnSuccess --parallelism 20
```
After the statement is executed, the file can be deleted from the source location.<br />

<a name="d5KKs"></a>
#### 6、use --outputCodec
<br />RAW files are usually writen into OSS or HDFS in uncompressed text format. This format is not ideal for both storage costs and data analysis. jindo distcp can use the outputCodec option to help you store data and compress files efficiently.

The command example as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --outputCodec=gz --parallelism 20
```
The current version of jindo distcp supports codecs gzip, gz, lzo, lzop, snappy, none and keep (default). These keywords have the following meanings:

- "none"-save as an uncompressed file. If the file has been compressed, jindo distcp decompresses it.
- "keep"-copy as is without changing the file compression form.


<br />Let's check the files in the target folder. These files are now compressed with gz codec:
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
<br />If you use the lzo compression feature in an open-source Hadoop cluster environment, you need to install the native library of gplcompression and the hadoop-lzo package. If you don't have the relevant environment, we recommend that you use other compression methods.

<a name="azhZZ"></a>
#### 7、use --outputManifest and --requirePreviousManifest

You can specify to generate a manifest file to record information such as the source file, target file and data size during the copy process. To generate manifest file, you must specify the requirePreviousManifest parameter as flase. The current outputManifest file will be a gz-type compressed file by default. You can name its prefix based on your own demand.
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --outputManifest=manifest-2020-04-17.gz --requirePreviousManifest=false --parallelism 20
```
You can view the content of the generated outputManifest file.
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
#### 8、use --outputManifest and --previousManifest

<br />In practice, the upstream process generates new files at a certain speed. For example, a new file may be created every hour or every minute. You can configure downstream processes to receive files according to different schedules. If data is transmitted to HDFS and we want to process it on OSS every day. Copying all files at a time does not work well. Jindo distcp has a built-in solution to this problem. For this solution, we use the manifest file.<br />
<br />If two new files are added to the source folder, the following command example is used:<br />

```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --outputManifest=manifest-2020-04-18.gz --previousManifest=oss://yang-hhht/hourly_table/manifest-2020-04-17.gz --parallelism 20
```
This command treats two manifest files as parameters: outputManifest and previousManifest. The first contains a list of all copied files (old files and new files), and the second contains a list of previously copied files. In this way, we can recreate the complete operation history and view which files were copied during each distcp job:
```bash
[root@emr-header-1 opt]# hadoop fs -text oss://yang-hhht/hourly_table/manifest-2020-04-18.gz > current.lst
[root@emr-header-1 opt]# diff before.lst current.lst 
3a4,5
> {"path":"oss://yang-hhht/hourly_table/2017-02-01/03/5.log","baseName":"2017-02-01/03/5.log","srcDir":"oss://yang-hhht/hourly_table","size":4891}
> {"path":"oss://yang-hhht/hourly_table/2017-02-01/03/6.log","baseName":"2017-02-01/03/6.log","srcDir":"oss://yang-hhht/hourly_table","size":4891}
```

jindo distcp uses the path /tmp/manifest.gz to create files in the local file system. After the copy operation is completed, it moves the manifest file to the dest directory.<br />

<a name="BkPxT"></a>
#### 9、use --copyFromManifest
<br />After you use the outputManifest option to generate a manifest file, you can specify this manifest file to copy files. The manifest file records the relevant file information, therefore, you do not need to obtain data information from the src Directory. You only need to specify the dest directory as the directory to be copied, you can copy the files to a new directory with files that contained in the manifest file generated from the last copy.

The sample command is as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --previousManifest=oss://yang-hhht/hourly_table/manifest-2020-04-17.gz --copyFromManifest --parallelism 20
```


<a name="Cw3ym"></a>
#### 10、use --**srcPrefixesFile**
<br />Assume that we need to copy multiple folders. Generally, we run as many replication jobs as we need to copy folders. jindo distcp allows you to copy data at one time. We only need to prepare a file with a prefix list and use it as a parameter.

The command example as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --srcPrefixesFile file:///opt/folders.txt --parallelism 20
```
The content of folders as follows:
```bash
[root@emr-header-1 opt]# cat folders.txt 
hdfs://emr-header-1.cluster-50466:9000/data/incoming/hourly_table/2017-02-01
hdfs://emr-header-1.cluster-50466:9000/data/incoming/hourly_table/2017-02-02
```
<br />The final result copies all files in the prefix list.
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht/hourly_table
Found 4 items
drwxrwxrwx   -          0 1970-01-01 08:00 oss://yang-hhht/hourly_table/2017-02-01
drwxrwxrwx   -          0 1970-01-01 08:00 oss://yang-hhht/hourly_table/2017-02-02
```
<br />

#### 11、use --groupBy and --targetSize
After optimization, Hadoop can read a small number of large files from HDFS instead of a large number of small files. You can use jindo distcp to aggregate small files into large files of a specified size, which can optimize analysis performance and cost.
<br />In the following example, we merge small files into larger ones. To do this, we use a regular expression with the -- groupBy option.

If you want to merge all txt files into one file at a maximum of 10MB, the sample command as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --targetSize=10 --groupBy='.*/([a-z]+).*.txt' --parallelism 20
```
<br />The original folder contains two txt files.
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
<br />After merging, you can see that two txt files are merged into one file.
```bash
[root@emr-header-1 opt]# hdfs dfs -ls oss://yang-hhht/hourly_table/2017-02-01/03/
Found 1 items
-rw-rw-rw-   1       2032 2020-04-17 21:18 oss://yang-hhht/hourly_table/2017-02-01/03/emp2
```

As you can see, we merge two txt data files into a file of no more than 10MB (the required size). Other files have been filtered out because the groupBy mode works in a similar way to the srcPattern mode. We recommend that the file size exceed the default block size. The final file name consists of groups and numbers in the regular expression used in groupBy to ensure the uniqueness of the name, therefore, at least one group must be defined to use this mode. You may occasionally receive the following error:
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
In this example, important information is included in distcp.JindoDistCp: Created 0 files to copy 0 files. jindo distcp cannot find the file to be copied because the regular expression in the -- groupBy option does not match any file in the source location.
<a name="zJOc9"></a>
#### 12、use --enableBalancePlan

If the overall size of the data you want to copy is relatively uniform, you can specify the enableBalancePlan parameter to change the job execute plan of Jindo distcp. Use this plan to make the data volume of each task balance and achieve better distcp performance.<br />
<br />The sample command as follows:<br />

```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableBalancePlan --parallelism 20
```

<br />This parameter cannot be used with the --groupby and --targetSize parameters.<br />

<a name="oM2mZ"></a>
#### 13、--enableDynamicPlan

In scenarios where the size of the data you want to copy is highly differentiated such as there are many small files, you can specify the --enableDynamicPlan parameter to change the job execute plan of Jindo distcp, to achieve better distcp performance.<br />
<br />
The sample command is as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableDynamicPlan --parallelism 20
```
<br />This parameter cannot be used with the --groupby and --targetSize parameters.<br />

<a name="ePqFH"></a>
#### 14、use --enableTransaction

Jindo distcp uses task-level integrity by default. To ensure Job-level integrity and transaction support between jobs, you can use the --enableTransaction parameter.<br />
<br />The sample command is as follows:<br />

```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --enableTransaction --parallelism 20
```


<a name="pjlfG"></a>
#### 15、use --diff

After your distcp task is completed, you can specify src and dest to check the file differences of the current distcp job. If the src file cannot be synchronized to dest, A manifest file is generated in the current directory. You can use the copyFromManifest parameter to copy the remaining files to verify the data size and the number of files. If your distcp task contains compression or decompression, diff cannot display the correct file difference, because compression or decompression will change the file size.<br />
<br />The sample command is as follows:<br />

```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --diff
```
<br />If all tasks are completed, the following message is displayed. Otherwise, a manifest file is generated in the execution directory.

```bash
INFO distcp.JindoDistCp: distcp has been done completely
```


If your -- dest is The HDFS path, you can write/path, hdfs://hostname:ip/path, hdfs://headerIp:ip/path. Currently, you cannot write hdfs:/// path, hdfs:/path, and other custom writing methods.<br />For the generated manifest file, you can use the --copyFromManifest and --previousManifest commands to copy the remaining files.<br />The sample command is as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --dest oss://yang-hhht/hourly_table --previousManifest=file:///opt/manifest-2020-04-17.gz --copyFromManifest --parallelism 20
```
<br />

#### 16、use Distcp Counters
<br />You can find the information of Distcp Counters in the Counter information of MapReduce, such:
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
 If your distcp operation contains compressed or decompressed files, the size of Bytes Destination Copied and Bytes Source Read may be different.<br />


#### 17、use OSS AK
If there is a problem with the password-free service or your cluster is outside the EMR, you can specify an AK to get access to OSS. You can use the --ossKey, --ossSecret, and --ossEndPoint options are used to specify the AK.<br />
<br />The sample command is as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-hangzhou.aliyuncs.com --parallelism 20
```

<br />You can also pre-configure the oss ak, secret, and endpoint in the hadoop core-site.xml file to avoid filling in the ak each time.
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

<br />In addition, we recommend that you configure the [password-free feature](https://help.aliyun.com/document_detail/156418.html) to avoid saving accesskeys in plaintext and improve security.<br />

<a name="IMjaY"></a>
#### 18、Write data to OSS by coldArchive, archive or low frequency
<br />When writing a distcp task to OSS, you can use --policy to specify whether to write the task to OSS in coldArchive, archive or low frequency mode for data storage.

Use the coldArchive (version 3.4.1) sample command as follows:

```bash
hadoop jar jindo-distcp-3.4.1.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --policy coldArchive --parallelism 20
```

Use the archive sample command as follows:

```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --policy archive --parallelism 20
```
Use the ia sample command as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --policy ia --parallelism 20
```

If you do not specify this parameter, the data is written in standard or standard mode by default, and no archiving or low-frequency operations are performed.<br />

<a name="UEx2K"></a>
#### 19、Clean up residual files
<br />During the completion of your distcp, files that are not uploaded correctly may be generated in your destination directory for various reasons. These files are managed by OSS through uploadId, you can specify the cleanUpPending option to clear the remaining files when the distcp task finish, or you can use the OSS console to clean the remaining files.<br />
<br />The sample command is as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --cleanUpPending --parallelism 20
```


<a name="7WRjP"></a>
#### 20、use --queue
During the distcp process, you can specify the name of the yarn queue where the distcp task is resigned. You can use --queue to specify this option<br />The sample command as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --queue yarnqueue
```


<a name="0TVlg"></a>
#### 21、use --bandwidth
During the distcp process, you can specify the bandwidth (in MB) used for this distcp task to avoid excessive bandwidth usage.<br />The sample command is as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src /data/incoming/hourly_table --dest oss://yang-hhht/hourly_table --bandwidth 6
```


<a name="UjU6Y"></a>
#### 22、Use s3 as the data source

You can specify the data source as s3. Currently, the prefix s3a/s3n/s3 is supported. You can use the --s3Key, --s3Secret, and --s3EndPoint options in the command to specify the information about connecting to s3. You can also specify only s3EndPoint to use S3.<br />
<br />The sample command is as follows:
```bash
hadoop jar jindo-distcp-3.4.0.jar --src s3a://yourbucket/ --dest oss://yang-hhht/hourly_table --s3Key yourkey --s3Secret yoursecret --s3EndPoint s3-us-west-1.amazonaws.com 
```

<br />You can also pre-configure the key, secret, and endpoint of s3 in the core-site.xml file of hadoop to avoid filling in the ak for each use.
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

<br />If you are in the S3 password-free environment, the sample command as follows. You do not need to specify an AK, but you need to specify an endPoint.<br />

```bash
hadoop jar /tmp/jindo-distcp-3.4.0.jar --src s3://smartdata1/ --dest s3://smartdata1/tmp --s3EndPoint  s3-us-west-1.amazonaws.com
```
<a name="tqzlD"></a>
#### 
<a name="h9wI9"></a>
#### 23、Use a lower version of JDK
<br />The default JDK version used by Jindo DistCp is 1.8. If you use a JDK earlier than 1.8, you can use the specified YARN JDK package to use Jindo DistCp.<br />
<br />1、Download JDK 8<br />for Linux 64x<br />[link](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/JDK8/jdk-8u251-linux-x64.tar.gz)<br />
<br />2、Decompression jdk-8u251-linux-x64.tar.gz，Specified JAVA_HOME<br />

```bash
[ec2-user@ip jdk1.8.0_251]$ pwd
/home/ec2-user/jdk1.8.0_251
[ec2-user@ip jdk1.8.0_251]$ export JAVA_HOME=`pwd`
```
put jdk-8u251-linux-x64.tar.gz to tmp/
```bash
[ec2-user@ip]$ cp jdk-8u251-linux-x64.tar.gz /tmp
```

<br />3、vim mapred-site.xml<br />

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

<a name="WwYXi"></a>
# <br />Release version

---

<a name="TqRR6"></a>
### v3.3.0
date：20210121

