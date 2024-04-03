Getting started with Alibaba Cloud OSS-HDFS (JindoFS)
OSS-HDFS is a new storage service that is developed by Alibaba Cloud. OSS-HDFS is compatible with Hadoop Distributed File System (HDFS) APIs and supports multi-level storage directories. JindoSDK allows Apache Hadoop-based computing and analysis applications, such as MapReduce, Hive, Spark, and Flink, to access OSS-HDFS. This topic describes how to modify the core-site.xml file in an existing Hadoop environment, Hadoop cluster, or Hadoop client to access OSS-HDFS. 
1. Enable OSS-HDFS.
For information about how to enable OSS-HDFS for a bucket, see [Enable OSS-HDFS and grant access permissions](https://help.aliyun.com/document_detail/419505.html).
2. Obtain the endpoint of HDFS.
To access OSS-HDFS, you must configure an endpoint in the cn-xxx.oss-dls.aliyuncs.com format. The endpoint of OSS-HDFS is different from the endpoint that is used to access OSS. The endpoint that is used to access OSS is in the oss-cn-xxx.aliyuncs.com format. JindoSDK accesses OSS-HDFS or OSS based on the endpoint that you configure. On the Overview page of your bucket in the OSS console, obtain the endpoint of HDFS. 
![](https://intranetproxy.alipay.com/skylark/lark/0/2024/png/8042/1711970094953-afcdb47c-48d5-425e-b9e1-65f10e90c2a5.png#)
3. Download the JindoSDK package.
[Download](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/jindodata_download.md) the latest version of the jindosdk-x.x.x.tar.gz package. 
4. Install the JAR package.
Decompress the downloaded JindoSDK JAR package and install the following JAR files that are contained in the package to the path specified by classpath of Hadoop:

- jindo-core-x.x.x.jar
- jindo-sdk-x.x.x.jar

Sample code for JindoSDK 4.6.12:
cp jindosdk-4.6.12/lib/jindo-core-4.6.12.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.6.12/lib/jindo-sdk-4.6.12.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
5. Configure the implementation class of OSS-HDFS and an AccessKey pair used to access OSS-HDFS.
Configure the implementation class of OSS in the core-site.xml file of Hadoop. 
<configuration>
<property>
<name>fs.AbstractFileSystem.oss.impl</name>
<value>com.aliyun.jindodata.oss.OSS</value>
</property>

<property>
<name>fs.oss.impl</name>
<value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
</property>
</configuration>
Configure an AccessKey ID and AccessKey secret that are used to access the bucket for which OSS-HDFS is enabled in the core-site.xml configuration file of Hadoop in advance. 
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
JindoSDK also allows you to use other methods to configure an AccessKey pair. For more information, see [Configure credential providers in JindoSDK](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/security/jindosdk_credential_provider_dls.md). 
Basic operations
After you enable and configure OSS-HDFS, you can run hdfs dfs commands to perform operations related to files or directories.
Create a directory
Create a directory in OSS-HDFS. Sample command: hdfs dfs -mkdir oss://<Bucket>.<Endpoint>/Test/subdir
[root@emr-header-1 ~]# hdfs dfs -mkdir oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/subdir
[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test
Found 1 items
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/subdir
Create a file
Run the hdfs dfs -put command to upload an on-premises file to OSS-HDFS. Sample command: hdfs dfs -put <localfile> oss://<Bucket>.<Endpoint>/Test
[root@emr-header-1 ~]# hdfs dfs -put /etc/hosts oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/
[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test
Found 2 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/subdir
View information about a file or directory
You can view the information about a file or a directory in a specific path after the file or directory is created. You cannot run this command to access a specific directory. You must specify the absolute path of the file or directory that you want to access. Command: lsSample command: hdfs dfs -ls oss://<Bucket>.<Endpoint>/Test
[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test
Found 2 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/subdir
View the size of a file or directory
View the size of an existing file or directory.Sample command: hdfs dfs -du oss://<Bucket>.<Endpoint>/Test
[root@emr-header-1 ~]# hdfs dfs -du oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test
5824  oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/hosts
0     oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/subdir
View the content of a file
If you want to view the content of a file in OSS-HDFS, you can run the hdfs dfs command to allow the content of the file to be displayed on the screen. After you run the command, the content of the file that you want to view is displayed on the screen in plain text. If the content is encoded, use the HDFS API for Java to read and decode the content.Sample command: hdfs dfs -cat oss://<Bucket>.<Endpoint>/Test/helloworld.txt
[root@emr-header-1 ~]# hdfs dfs -cat  oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/helloworld.txt
hello world!
Copy a file or directory
If you want to copy a file or directory in OSS-HDFS without changing the structure and content of the file or directory, you can run the following command: Sample command: hdfs dfs -cp oss://<Bucket>.<Endpoint>/Test/subdir oss://<Bucket>.<Endpoint>/TestTarget/sudir2
[root@emr-header-1 ~]# hdfs dfs -cp oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/subdir oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/TestTarget/subdir1
[root@emr-header-1 ~]# hdfs dfs -ls  oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/TestTarget/
Found 1 items
drwxr-x--x   - root supergroup          0 2021-12-01 20:37 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/TestTarget/subdir1
Move a file or a directory
During big data processing, you may need to write files to a temporary directory and then move the directory to another position. If you want to move a directory and files in the directory without retaining the content and structure of the source files and directory, you can run the following command: hdfs dfs -mv oss://<Bucket>.<Endpoint>/Test/subdir oss://<Bucket>.<Endpoint>/Test/subdir1
[root@emr-header-1 ~]# hdfs dfs -mv  oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/subdir  oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/newdir
[root@emr-header-1 ~]# hdfs dfs -ls  oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test
Found 3 items
-rw-r-----   1 root supergroup         13 2021-12-01 20:33 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/helloworld.txt
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/hosts
drwxr-x--x   - root supergroup          0 2021-12-01 20:19 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/newdir
Download a file to the on-premises file system
If you want to download specific files in OSS-HDFS to your on-premises machine for processing or viewing, you can run the following command: hdfs dfs -get oss://<Bucket>.<Endpoint>/Test/helloworld.txt <localpath>
[root@emr-header-1 ~]# hdfs dfs -get oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/helloworld.txt /tmp/
[root@emr-header-1 ~]# ll /tmp/helloworld.txt
-rw-r----- 1 root root 13 December  1 20:44 /tmp/helloworld.txt
Remove a file or directory
If you no longer require a temporary or deprecated file in OSS-HDFS, you can run the following command: hdfs dfs -rm oss://<Bucket>.<Endpoint>/Test/helloworld.txt
[root@emr-header-1 ~]# hdfs dfs -rm oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/helloworld.txt
21/12/01 20:46:44 INFO fs.TrashPolicyDefault: Moved: 'oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/helloworld.txt' to trash at: oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/user/root/.Trash/Current/Test/helloworld.txt

[root@emr-header-1 ~]# hdfs dfs -rm -r oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/newdir
21/12/01 20:47:16 INFO fs.TrashPolicyDefault: Moved: 'oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/newdir' to trash at: oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/user/root/.Trash/Current/Test/newdir

[root@emr-header-1 ~]# hdfs dfs -ls oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/
Found 1 items
-rw-r-----   1 root supergroup       5824 2021-12-01 20:24 oss://dls-chenshi-test.cn-xxx.oss-dls.aliyuncs.com/Test/hosts

