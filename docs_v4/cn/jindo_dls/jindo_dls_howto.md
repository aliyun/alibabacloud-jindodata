# JindoFS服务（OSS-HDFS服务）快速入门

JindoFS服务（OSS-HDFS服务）是OSS新推出新的存储空间类型，兼容HDFS接口, 支持目录以及目录层级，通过JindoFS SDK 4.0.0 可以兼容访问JindoFS服务（OSS-HDFS服务）， Apache Hadoop的计算分析应用（例如MapReduce、Hive、Spark、Flink等）可在不修改代码或者编译的情况下，直接使用OSS存储空间（Bucket），用户如何创建JindoFS服务（OSS-HDFS服务），详细步骤请参考[链接](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs_v4/cn/jindosdk_how_to_hadoop.md)


# 基本操作示例
JindoFS服务（OSS-HDFS服务）创建以及配置完成后，可以通过hdfs dfs 命令进行相关操作
### 新建目录
在JindoFS服务（OSS-HDFS服务）服务上创建目录
指令: mkdir
用例: hdfs dfs -mkdir oss://\<bucket\>/Test/subdir

```shell
[root@emr-header-1 ~]# hdfs dfs -mkdir oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test/Test
Found 1 items
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]#
```

### 新建文件
利用hdfs dfs -put命令上传本地文件到JindoFS服务（OSS-HDFS服务）
指令： put
用例：hdfs dfs -put \<localfile\> oss://\<bucket\>/Test
```shell
[root@emr-header-1 ~]# hdfs dfs -put /etc/hosts oss://dls-chenshi-test/Test/
[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test/Test
Found 2 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]#
```
### 查看文件或者目录信息
在文件或者目录创建完之后，可以查看指定路径下的文件/目录信息。hdfs dfs没有进入某个目录下的概念。在查看目录和文件的信息的时候需要给出文件/目录的绝对路径。
指令：ls
用例：hdfs dfs -ls oss://\<bucket\>/Test
```shell
[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test/Test
Found 2 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]#
```

### 查看文件或者目录的大小
查看已有文件或者目录的大小
指令：du
用例： hdfs dfs -du oss://\<bucket\>/Test
```shell
[root@emr-header-1 ~]# hdfs dfs -du oss://dls-chenshi-test/Test
5824  oss://dls-chenshi-test/Test/hosts
0     oss://dls-chenshi-test/Test/subdir
```

### 查看文件的内容
有时候我们需要查看一下在JindoFS服务（OSS-HDFS服务）文件的内容。hdfs dfs命令支持我们将文件内容打印在屏幕上。（请注意，文件内容将会以纯文本形式打印出来，如果文件进行了特定格式的编码，请使用HDFS的JavaAPI将文件内容读取并进行相应的解码获取文件内容）
指令：cat
用例： hdfs dfs -cat oss://\<bucket\>/Test/helloworld.txt

```shell
[root@emr-header-1 ~]# hdfs dfs -cat  oss://dls-chenshi-test/Test/helloworld.txt
hello world!
```

### 复制目录/文件
有时候我们需要将JindoFS服务（OSS-HDFS服务）的一个文件/目录拷贝到另一个位置，并且保持源文件和目录结构和内容不变。
指令：cp
用例：hdfs dfs -cp oss://\<bucket\>/Test/subdir oss://\<bucket\>/TestTarget/sudir2
```shell
[root@emr-header-1 ~]# hdfs dfs -cp oss://dls-chenshi-test/Test/subdir oss://dls-chenshi-test/TestTarget/subdir1
[root@emr-header-1 ~]# hdfs dfs -ls  oss://dls-chenshi-test/TestTarget/
Found 1 items
drwxr-x--x   - root supergroup          0 2021-12-01 20:37 oss://dls-chenshi-test/TestTarget/subdir1
```

### 移动目录/文件
在很多大数据处理的例子中，我们会将文件写入一个临时目录，然后将该目录移动到另一个位置作为最终结果。源文件和目录结构和内容不做保留。下面的命令可以完成这些操作。
指令：mv
用例：hdfs dfs -mv oss://\<bucket\>/Test/subdir oss://\<bucket\>/Test/subdir1

```shell
[root@emr-header-1 ~]# hdfs dfs -mv  oss://dls-chenshi-test/Test/subdir  oss://dls-chenshi-test/Test/newdir
[root@emr-header-1 ~]# hdfs dfs -ls  oss://dls-chenshi-test/Test
Found 3 items
-rw-r-----   1 root supergroup         13 2021-12-01 20:33 oss://dls-chenshi-test/Test/helloworld.txt
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/newdir
```

### 下载文件到本地文件系统
某些情况下，我们需要将OSS文件系统中的某些文件下载到本地，再进行处理或者查看内容。这个可以用下面的命令完成。
指令：get
用例：hdfs dfs -get oss://\<bucket\>/Test/helloworld.txt \<localpath\>
```shell
[root@emr-header-1 ~]# hdfs dfs -get oss://dls-chenshi-test/Test/helloworld.txt /tmp/
[root@emr-header-1 ~]# ll /tmp/helloworld.txt
-rw-r----- 1 root root 13 12月  1 20:44 /tmp/helloworld.txt
```

### 删除目录/文件
在很多情况下，我们在完成工作后，需要删除在JindoFS服务（OSS-HDFS服务）上的某些临时文件或者废弃文件。这些可以通过下面的命令完成。
指令：-rm/-rm -r
用例：hdfs dfs -rm oss://\<bucket\>/Test/helloworld.txt
```shell
[root@emr-header-1 ~]# hdfs dfs -rm oss://dls-chenshi-test/Test/helloworld.txt
21/12/01 20:46:44 INFO fs.TrashPolicyDefault: Moved: 'oss://dls-chenshi-test/Test/helloworld.txt' to trash at: oss://dls-chenshi-test/user/root/.Trash/Current/Test/helloworld.txt

[root@emr-header-1 ~]# hdfs dfs -rm -r oss://dls-chenshi-test/Test/newdir
21/12/01 20:47:16 INFO fs.TrashPolicyDefault: Moved: 'oss://dls-chenshi-test/Test/newdir' to trash at: oss://dls-chenshi-test/user/root/.Trash/Current/Test/newdir

[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test/Test/
Found 1 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
```

除了上述基本操作之外，JindoFS服务（OSS-HDFS服务）还支持Acl，attr/xattr，snapshot, checksum等功能。