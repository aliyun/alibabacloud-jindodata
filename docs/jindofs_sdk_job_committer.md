# Jindo Job Committer 使用说明
本文主要介绍Jindo Job Committer的使用。Jindo Job Committer较早开始支持JindoOssCommitter。从JindoFS SDK 3.4.0版本开始，针对OSS bucket开启多版本功能多情况，增加支持了JindoOssDirectCommitter。

## 背景信息
Job Committer是MapReduce和Spark等分布式计算框架的一个基础组件，用来处理分布式任务写数据的一致性问题。
JindoOssCommitter是阿里云E-MapReduce针对OSS场景开发的高效Job Committer的实现，基于OSS的Multipart Upload接口，结合OSS Filesystem层的定制化支持。使用Jindo Job Committer时，Task数据直接写到最终目录中，在完成Job Commit前，中间数据对外不可见，彻底避免了Rename操作，同时保证数据的一致性。

注意:
* OSS拷贝数据的性能，针对不同的用户或Bucket会有差异，可能与OSS带宽以及是否开启某些高级特性等因素有关，具体问题可以咨询OSS的技术支持。
* 在所有任务都完成后，MapReduce Application Master或Spark Driver执行最终的Job Commit操作时，会有一个短暂的时间窗口。时间窗口的大小和文件数量线性相关，可以通过增大fs.oss.committer.threads可以提高并发处理的速度。
* Hive和Presto等没有使用Hadoop的Job Committer。
* E-MapReduce集群中默认打开Jindo Oss Committer的参数。

## 在 MapReduce 中使用 JindoOssCommitter
### 配置Job Committer
针对不同的Hadoop版本，在 mapred-site.xml中 配置以下参数。
* Hadoop 2.x版本
```xml
<configuration>
    <property>
        <name>mapreduce.outputcommitter.class</name>
        <value>com.aliyun.emr.fs.oss.commit.JindoOssCommitter</value>
    </property>
</configuration>
```
* Hadoop 3.x版本
```xml
<configuration>
   <property>
      <name>mapreduce.outputcommitter.factory.scheme.oss</name>
      <value>com.aliyun.emr.fs.oss.commit.JindoOssCommitterFactory</value>
   </property>
</configuration>
```
### 开启 Jindo Oss Magic Committer
在Hadoop的 core-site.xml 中配置下面的开关 fs.oss.committer.magic.enabled 便捷地控制所使用的Job Committer。当打开时，MapReduce任务会使用无需Rename操作的Jindo Oss Magic Committer，当关闭时，JindoOssCommitter和FileOutputCommitter行为一样。
```xml
<configuration>
   <property>
      <name>fs.oss.committer.magic.enabled</name>
      <value>true</value>
   </property>
</configuration>
```

## 在Spark SQL中使用JindoOssCommitter
### 配置Job Committer
在Spark服务的 spark-defaults.conf 中设置以下参数。下列参数分别用来设置写入数据到Spark DataSource表和Parquet表时使用的Job Committer。

| 参数                                       | 参数值            |
| ------------------------------------------| ----------------- |
| spark.sql.sources.outputCommitterClass    | com.aliyun.emr.fs.oss.commit.JindoOssCommitter |
| spark.sql.parquet.output.committer.class  | com.aliyun.emr.fs.oss.commit.JindoOssCommitter |

### 开启 Jindo Oss Magic Committer
在Hadoop的 core-site.xml 中配置开关 fs.oss.committer.magic.enabled 便捷地控制所使用的Job Committer。当打开时，Spark任务会使用无需Rename操作的Jindo Oss Magic Committer，当关闭时，JindoOssCommitter和FileOutputCommitter行为一样。
```xml
<configuration>
   <property>
      <name>fs.oss.committer.magic.enabled</name>
      <value>true</value>
   </property>
</configuration>
```

## 优化 JindoOssCommitter 性能
### 调整并发执行Commit任务的线程数
当MapReduce或Spark任务写大量文件的时候，您可以调整MapReduce Application Master或Spark Driver中并发执行Commit相关任务的线程数量，提升Job Commit性能。
在Hadoop的core-site.xml中配置下面参数：
```xml
<configuration>
   <property>
      <name>fs.oss.committer.threads</name>
      <value>8</value>
   </property>
</configuration>
```

### 清理临时目录历史版本
在数据湖场景下，可以通过开启 OSS 的多版本功能进行数据保护，防止数据勿删等情况的发生。在OSS Bucket开启多版本的情况下，如果频繁在相同目录下频繁创建和删除，会导致目录 List 性能下降。针对这一问题，JindoFS SDK 3.4.0版本做了更新：优化Jindo Oss Magic Committer，对于临时目录和文件，会进行连同历史版本一起删除，避免了冗余的临时目录影响目录list 的性能；使用Jindo Oss Magic Committer时，默认开启临时目录历史版本清理功能。<br />
您可以在作业或者Hadoop的 core-site.xml 中配置下面参数：
```xml
<configuration>
   <property>
      <name>fs.jfs.cache.oss.delete-marker.dirs</name>
      <value>_temporary,.staging,.hive-staging,__magic</value>
   </property>
</configuration>
```
如需关闭自动清理功能，可以将 fs.jfs.cache.oss.delete-marker.dirs 设置为空字符。

## 使用 JindoOssDirectCommitter
对于开启多版本的bucket也可以通过JindoOssDirectCommitter更好发挥OSS性能，JindoOssDirectCommitter可以避免临时文件的产生，直接输出到目标目录。可以通过修改Committer的配置为 com.aliyun.emr.fs.oss.commit.direct.JindoOssDirectCommitter 来使用。
### 在 MapReduce 中使用 JindoOssDirectCommitter
在mapred-site.xml中配置以下参数，目前只支持 Hadoop 2.x版本。
```xml
<configuration>
    <property>
        <name>mapreduce.outputcommitter.class</name>
        <value>com.aliyun.emr.fs.oss.commit.direct.JindoOssDirectCommitter</value>
    </property>
</configuration>
```

### 在Spark中使用 JindoOssDirectCommitter
在Spark服务的 spark-defaults.conf 中设置以下参数。这三个参数分别用来设置写入数据到Spark DataSource表、Spark Parquet格式的DataSource表和Hive表时使用的Job Committer。

| 参数                                       | 参数值            |
| ------------------------------------------| ----------------- |
| spark.sql.sources.outputCommitterClass    | com.aliyun.emr.fs.oss.commit.direct.JindoOssDirectCommitter |
| spark.sql.parquet.output.committer.class  | com.aliyun.emr.fs.oss.commit.direct.JindoOssDirectCommitter |
| spark.sql.hive.outputCommitterClass       | com.aliyun.emr.fs.oss.commit.direct.JindoOssDirectCommitter |
