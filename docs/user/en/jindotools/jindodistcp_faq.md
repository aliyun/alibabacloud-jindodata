# JindoDistCp Troubleshooting Guide

## 1. Low List Performance

### Phenomenon
If you encounter slow listing performance when using JindoDistCp, such as seeing this message:
```
Successfully list objects with prefix xxx/yyy/ in bucket xxx recursive 0 result 315 dur 100036.615031MS
```
where `dur 100036.615031MS` represents the duration of the list operation in milliseconds (in this case, 100 seconds). A normal list operation on OSS for around 1000 files takes less than one second. You can assess whether the list operation is abnormally slow based on the number of files in the current directory. In the given example, listing 315 files took 100 seconds, which is unusual.

### Solution
Increase client memory by executing the following command:
```bash
export HADOOP_CLIENT_OPTS="$HADOOP_CLIENT_OPTS -Xmx4096m"
```

## 2. Checksum Error

### Phenomenon
During the use of JindoDistCp, if you come across this message:
```
Failed to get checksum store.
```

### Solution
By default, OSS-HDFS uses the COMPOSITE_CRC checksum algorithm. If HDFS has its `dfs.checksum.combine.mode` configured as MD5MD5CRC, change the OSS-HDFS configuration `fs.oss.checksum.combine.mode` to MD5MD5CRC:
```bash
hadoop jar jindo-distcp-${version}.jar --src /data --dest oss://destBucket/ --hadoopConf fs.oss.checksum.combine.mode=MD5MD5CRC
```

## 3. Verify Checksum Failed When Copying from OSS to OSS-HDFS

### Phenomenon
When using JindoDistCp, if you receive this error:
```
Exception raised while copying data file, verify checksum failed
```

### Solution
If the files in OSS were not migrated from HDFS using JindoDistCp, skip the checksum check by adding the `--disableChecksum` flag:
```bash
hadoop jar jindo-distcp-${version}.jar --src oss://ossBucket/ --dest oss://dlsBucket/ --disableChecksum
```

## 4. Verifying JindoDistCp Success

Without the `--ignore` option, JindoDistCp will exit with an error if issues occur during execution. If the `--ignore` option was added, examine the JindoDistCp counters, such as COPY_FAILED and CHECKSUM_DIFF, as explained in the JindoDistCp Counters documentation.