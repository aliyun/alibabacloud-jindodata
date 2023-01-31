# JindoDistCp 问题排查指南

### 1、list 性能低
#### 现象
如您在使用 JindoDistCp 的过程中，发现 list 性能较慢，如遇到如下信息
```
Successfully list objects with prefix xxx/yyy/ in bucket xxx recursive 0 result 315 dur 100036.615031MS
```
其中 dur 100036.615031MS 代表此次 list 用时，单位毫秒，如上述一次 list 耗时 100 秒，OSS 上文件正常的 list 速度是 1000 文件在 1s 以下，您可根据当前目录下文件数量来判断该 list 耗时是否异常，如上述信息显示 list 315 个文件的目录需要 100s 显然是不太正常的。
#### 解决办法
增加客户端内存，执行以下命令增大客户端内存
```
export HADOOP_CLIENT_OPTS="$HADOOP_CLIENT_OPTS -Xmx4096m"
```

### 2. checksum 报错
#### 现象
如您在使用 JindoDistCp 的过程中，如遇到如下信息
```
Failed to get checksum store.
```
#### 解决办法
OSS-HDFS 默认的 checksum 算法是 COMPOSITE_CRC，如果 HDFS 配置的 dfs.checksum.combine.mode 为 MD5MD5CRC，则需要变更 OSS-HDFS 的 fs.oss.checksum.combine.mode 配置为 MD5MD5CRC。
```shell
hadoop jar jindo-distcp-${version}.jar --src /data --dest oss://destBucket/ --hadoopConf fs.oss.checksum.combine.mode=MD5MD5CRC
```

### 3. OSS 拷贝到 OSS-HDFS 时报 verify checksum failed
#### 现象
如您在使用 JindoDistCp 的过程中，如遇到如下信息
```
Exception raised while copying data file, verify checksum failed
```
#### 解决办法
如果 OSS 中的的文件不是通过 JindoDistCp 从 HDFS 迁移到 OSS的，则需要通过 --disableChecksum 跳过 checksum 检查。
```shell
hadoop jar jindo-distcp-${version}.jar --src oss://ossBucket/ --dest oss://dlsBucket/ --disableChecksum
```

