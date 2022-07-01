# 统一命名空间混合挂载
JindoFSx 存储加速系统提供了对多种数据源的缓存加速功能，而且可以将不同数据源统一管理，放到同一个命名空间下统一访问。

### 前提
1、已经部署 jindofsx 服务，且在服务端配置好各种数据源的访问密钥。<br/>
[JindoFSx 存储加速系统快速入门](/docs/user/4.x/4.4.0/jindofsx/jindofsx_quickstart.md)

### 挂载 OSS 数据源
1、已经将 OSS 相关 ak/endpoint 信息配置到 core-site.xml 里

2、执行挂载命令

```shell
jindo admin -mount /oss oss://<bucket>/<dir>
```
* /oss：挂载到统一命名空间下的访问路径

### 挂载 HDFS 数据源
1、已经将 HDFS 信息配置到 core-site.xml 里

2、执行挂载命令

```shell
jindo admin -mount /oss hdfs://
```

### 挂载 OSS-HDFS 服务数据源

### 挂载 NAS 数据源



### 使用统一命名空间访问
```shell
$ hadoop fs -ls jindo:///
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///oss
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///s3
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///hdfs
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///oss-hdfs
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///nas
$ hadoop fs -ls jindo:///oss/
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///oss/test_a_dir
-rwxrwxr-x   0 root root   19564111 2022-04-11 10:25 jindo:///oss/tmp.txt
```