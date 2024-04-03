Configuration of JindoSDK advanced parameters
JindoSDK configuration items
You can add the following configuration items to the core-site.xml configuration file of Hadoop based on your business requirements: 
<configuration>
<property>
<!-- The directory to which the client writes temporary files. If you want to configure multiple directories, separate the directories with commas (,). The client writes temporary files to the directories in turn. Read and write permissions must be granted in environments that involve multiple users. -->
<name>fs.oss.tmp.data.dirs</name>
<value>/tmp/</value>
</property>
<property>
<!-- Specifies whether to enable automatic cleaning of temporary files. -->
<name>fs.oss.tmp.data.cleaner.enable</name>
<value>true</value>
</property>
<property>
<!-- The number of retries on failed access to Object Storage Service (OSS). -->
<name>fs.oss.retry.count</name>
<value>5</value>
</property>

<property>
<!-- The timeout period to access OSS. Unit: milliseconds. -->
<name>fs.oss.timeout.millisecond</name>
<value>30000</value>
</property>

<property>
<!-- The timeout period to connect to OSS. Unit: milliseconds. -->
<name>fs.oss.connection.timeout.millisecond</name>
<value>3000</value>
</property>
<property>
<!-- The number of concurrent threads that are used to upload a single object to OSS. -->
<name>fs.oss.upload.thread.concurrency</name>
<value>5</value>
</property>

<property>
<!-- The number of concurrent tasks that are initiated to upload objects to OSS. -->
<name>fs.oss.upload.queue.size</name>
<value>5</value>
</property>

<property>
<!-- The maximum number of concurrent tasks that are initiated to upload objects to OSS in a process. -->
<name>fs.oss.upload.max.pending.tasks.per.stream</name>
<value>16</value>
</property>

<property>
<!-- The number of concurrent tasks that are initiated to download objects from OSS. -->
<name>fs.oss.download.queue.size</name>
<value>5</value>
</property>

<property>
<!-- The maximum number of concurrent tasks that are initiated to download objects from OSS in a process. -->
<name>fs.oss.download.thread.concurrency</name>
<value>16</value>
</property>
<property>
<!-- The size of the buffer that is used to prefetch data from OSS. -->
<name>fs.oss.read.readahead.buffer.size</name>
<value>1048576</value>
</property>
<property>
<!-- The number of buffers that are used to prefetch data from OSS at the same time. -->
<name>fs.oss.read.readahead.buffer.count</name>
<value>4</value>
</property>

<property>
<!-- The interval at which data in a buffer is flushed before the data is written to OSS. Unit: milliseconds. If the value of this parameter is less than 0, this parameter does not take effect. -->
<name>fs.oss.flush.interval.millisecond</name>
<value>-1</value>
</property>

<property>
<!-- Specifies whether to enable file-level data integrity check by using CRC-64. By default, this configuration item is set to true. -->
<name>fs.oss.checksum.crc64.enable</name>
<value>true</value>
</property>

<property>
<!--Specifies whether to enable request-level data integrity check by using MD5. By default, this configuration item is set to false. -->
<name>fs.oss.checksum.md5.enable</name>
<value>false</value>
</property>

</configuration>
Note: This topic is applicable to JindoSDK of a version later than 4.5.

