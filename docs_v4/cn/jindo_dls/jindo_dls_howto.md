# OSS DLS存储空间（Bucket）
阿里云对象存储服务（Object Storage Service，OSS）是一种海量、安全、低成本、高可靠的云存储服务，适合存放任意类型的文件关于OSS产品的相关介绍可详见[产品官网](https://help.aliyun.com/product/31815.html?spm=5176.7933691.J_7985555940.2.19b94c59dVm8gi)。

OSS DLS存储空间（Bucket）是OSS新推出新的存储空间类型，全面兼容Hadoop FileSystem接口, 支持目录以及目录层级，通过JindoFS SDK 4.0.0 可以兼容访问OSS DLS存储空间(Bucket)， Apache Hadoop的计算分析应用（例如MapReduce、Hive、Spark、Flink等）可在不修改代码或者编译的情况下，直接使用OSS存储空间（Bucket），用户如何创建OSS DLS存储空间，详细步骤请参考[链接](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs_v4/cn/jindosdk_how_to_hadoop.md)


# 基本操作示例
OSS DLS存储空间(Bucket)创建以及配置完成后，可以通过hadoop fs命令进行OSS相关操作
### 新建目录
在OSS创建目录
指令: mkdir
用例： hadoop fs -mkdir oss://\<bucket\>/Test/subdir
  ```shell
[root@emr-header-1 ~]# hadoop fs -mkdir oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]# hadoop fs -ls oss://dls-chenshi-test/Test
Found 1 items
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]#
  ```

### 新建文件
利用hadoop fs -put命令上传本地文件到OSS
指令： put
用例：hadoop fs -put <localfile> oss://\<bucket\>/Test
```shell
[root@emr-header-1 ~]# hadoop fs -put /etc/hosts oss://dls-chenshi-test/Test/
[root@emr-header-1 ~]# hadoop fs -ls oss://dls-chenshi-test/Test
Found 2 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]#
```
### 查看文件或者目录信息
在文件或者目录创建完之后，可以查看指定路径下的文件/目录信息。hadoop fs没有进入某个目录下的概念。在查看目录和文件的信息的时候需要给出文件/目录的绝对路径。
指令：ls
用例：hadoop fs -ls oss://\<bucket\>/Test
```shell
[root@emr-header-1 ~]# hadoop fs -ls oss://dls-chenshi-test/Test
Found 2 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/subdir
[root@emr-header-1 ~]#
```

### 查看文件或者目录的大小
查看已有文件或者目录的大小
指令：du
用例： hadoop fs -du oss://\<bucket\>/Test
```shell
[root@emr-header-1 ~]# hadoop fs -du oss://dls-chenshi-test/Test
5824  oss://dls-chenshi-test/Test/hosts
0     oss://dls-chenshi-test/Test/subdir
```

### 查看OSS文件的内容
有时候我们需要查看一下在OSS文件的内容。hadoop fs命令支持我们将文件内容打印在屏幕上。（请注意，文件内容将会以纯文本形式打印出来，如果文件进行了特定格式的编码，请使用HDFS的JavaAPI将文件内容读取并进行相应的解码获取文件内容）
指令：cat
用例： hadoop fs -cat oss://\<bucket\>/Test/helloworld.txt

```shell
[root@emr-header-1 ~]# hadoop fs -cat  oss://dls-chenshi-test/Test/helloworld.txt
hello world!
```

### 复制OSS目录/文件
有时候我们需要将OSS的一个文件/目录拷贝到另一个位置，并且保持源文件和目录结构和内容不变。
指令：cp
用例：hadoop fs -cp oss://\<bucket\>/Test/subdir oss://\<bucket\>/TestTarget/sudir2
```shell
[root@emr-header-1 ~]# hadoop fs -cp oss://dls-chenshi-test/Test/subdir oss://dls-chenshi-test/TestTarget/subdir1
[root@emr-header-1 ~]# hadoop fs -ls  oss://dls-chenshi-test/TestTarget/
Found 1 items
drwxr-x--x   - root supergroup          0 2021-12-01 20:37 oss://dls-chenshi-test/TestTarget/subdir1
```

### 移动OSS目录/文件
在很多大数据处理的例子中，我们会将文件写入一个临时目录，然后将该目录移动到另一个位置作为最终结果。源文件和目录结构和内容不做保留。下面的命令可以完成这些操作。
指令：mv
用例：hadoop fs -mv oss://\<bucket\>/Test/subdir oss://\<bucket\>/Test/subdir1

```shell
[root@emr-header-1 ~]# hadoop fs -mv  oss://dls-chenshi-test/Test/subdir  oss://dls-chenshi-test/Test/newdir
[root@emr-header-1 ~]# hadoop fs -ls  oss://dls-chenshi-test/Test
Found 3 items
-rw-r-----   1 root supergroup         13 2021-12-01 20:33 oss://dls-chenshi-test/Test/helloworld.txt
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test/Test/newdir
```

### 下载OSS文件到本地文件系统
某些情况下，我们需要将OSS文件系统中的某些文件下载到本地，再进行处理或者查看内容。这个可以用下面的命令完成。
指令：get
用例：hadoop fs -get oss://\<bucket\>/Test/helloworld.txt <localpath>
```shell
[root@emr-header-1 ~]# hadoop fs -get oss://dls-chenshi-test/Test/helloworld.txt /tmp/
[root@emr-header-1 ~]# ll /tmp/helloworld.txt
-rw-r----- 1 root root 13 12月  1 20:44 /tmp/helloworld.txt
```

### 删除OSS 目录/文件
在很多情况下，我们在完成工作后，需要删除在OSS上的某些临时文件或者废弃文件。这些可以通过下面的命令完成。
指令：rm/rmr
用例：hadoop fs -rm oss://\<bucket\>/Test/helloworld.txt
```shell
[root@emr-header-1 ~]# hadoop fs -rm oss://dls-chenshi-test/Test/helloworld.txt
21/12/01 20:46:44 INFO Configuration.deprecation: io.bytes.per.checksum is deprecated. Instead, use dfs.bytes-per-checksum
21/12/01 20:46:44 INFO fs.TrashPolicyDefault: Moved: 'oss://dls-chenshi-test/Test/helloworld.txt' to trash at: oss://dls-chenshi-test/user/root/.Trash/Current/Test/helloworld.txt

[root@emr-header-1 ~]# hadoop fs -rmr oss://dls-chenshi-test/Test/newdir
rmr: DEPRECATED: Please use '-rm -r' instead.
21/12/01 20:47:16 INFO Configuration.deprecation: io.bytes.per.checksum is deprecated. Instead, use dfs.bytes-per-checksum
21/12/01 20:47:16 INFO fs.TrashPolicyDefault: Moved: 'oss://dls-chenshi-test/Test/newdir' to trash at: oss://dls-chenshi-test/user/root/.Trash/Current/Test/newdir

[root@emr-header-1 ~]# hadoop fs -ls oss://dls-chenshi-test/Test/
Found 1 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test/Test/hosts
```