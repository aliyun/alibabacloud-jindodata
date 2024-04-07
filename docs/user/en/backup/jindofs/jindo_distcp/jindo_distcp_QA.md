# FAQ about Jindo DistCp

1. What do I do if objects are listed at a low speed?

Problem description

When I use Jindo DistCp, objects are listed at a low speed, and the following message is returned:

Successfully list objects with prefix xxx/yyy/ in bucket xxx recursive 0 result 315 dur 100036.615031MS

In the message, "dur 100036.615031MS" indicates the time taken to list objects, in milliseconds. In normal cases, 1,000 Object Storage Service (OSS) objects can be listed within 1s. You can determine whether the time taken to list objects in a directory is normal based on the normal speed. For example, the preceding message shows that 100 seconds are taken to list 315 objects in a directory. This is abnormal. 

Solution

Run the following command to increase the memory of your client:

export HADOOP\_CLIENT\_OPTS="$HADOOP\_CLIENT\_OPTS -Xmx4096m"

2. What do I do if a checksum-related error occurs?

Problem description

The following error message is returned when Jindo DistCp is used:

Failed to get checksum store.

Solution

By default, OSS-HDFS uses the checksum algorithm COMPOSITE\_CRC. If the dfs.checksum.combine.mode parameter of Hadoop Distributed File System (HDFS) is set to MD5MD5CRC, you must change the value of the fs.oss.checksum.combine.mode parameter of OSS-HDFS to MD5MD5CRC. Sample command: 

hadoop jar jindo-distcp-${version}.jar --src /data --dest oss://destBucket/ --hadoopConf fs.oss.checksum.combine.mode=MD5MD5CRC