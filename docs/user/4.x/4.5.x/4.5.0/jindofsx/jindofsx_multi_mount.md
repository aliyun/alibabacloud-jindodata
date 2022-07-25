# 统一命名空间混合挂载
JindoFSx 存储加速系统提供了对多种数据源的缓存加速功能，而且可以将不同数据源统一管理，放到同一个命名空间下统一访问。

### 前提
1、已经部署 jindofsx 服务，且在服务端配置好各种数据源的访问密钥。<br/>
[JindoFSx 存储加速系统快速入门](/docs/user/4.x/4.5.0/jindofsx/jindofsx_quickstart.md)

2、在 core-site 里添加 jindo 统一命名空间参数
```xml
<property>
    <name>fs.jindo.impl</name>
    <value>com.aliyun.jindodata.jindo.JindoFileSystem</value>
</property>

<property>
    <name>fs.AbstractFileSystem.jindo.impl</name>
    <value>com.aliyun.jindodata.jindo.JINDO</value>
</property>

<property>
    <name>fs.xengine</name>
    <value>jindofsx</value>
</property>
```

### 挂载 OSS 数据源
1、已经将 OSS 相关 ak/endpoint 信息配置到 core-site.xml 里<br/>
可参考 [JindoFSx 挂载 OSS 数据源](/docs/user/4.x/4.5.0/jindofsx/oss/jindofsx_on_oss_jindo.md)

2、执行挂载命令

```shell
jindo admin -mount /oss oss://<bucket>/<dir>
```
* /oss：挂载到统一命名空间下的访问路径

同样可以把多个 OSS bucket 挂载到不同的路径下面。

### 挂载 OSS-HDFS 服务数据源
1、已经将 OSS-HDFS 访问信息配置到 core-site.xml 里<br/>
可参考 [JindoFSx 挂载 OSS-HDFS 数据源](/docs/user/4.x/4.5.0/jindofsx/jindofs/jindofsx_on_jindofs_jindo.md)

2、执行挂载命令

```shell
jindo admin -mount /oss-hdfs oss://<bucket>/<dir>
```

* /oss-hdfs：挂载到统一命名空间下的访问路径

同样可以把多个 OSS-HDFS bucket 挂载到不同的路径下面。

### 使用统一命名空间访问
```shell
$ hadoop fs -ls jindo:///
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///oss
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///hdfs
$ hadoop fs -ls jindo:///oss/
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///oss/test_a_dir
-rwxrwxr-x   0 root root   19564111 2022-04-11 10:25 jindo:///oss/tmp.txt
$ hadoop fs -ls jindo:///hdfs/
drwxrwxr-x   - root root          0 1970-01-01 00:00 jindo:///hdfs/test_a_dir
-rwxrwxr-x   0 root root   19564111 2022-04-11 10:25 jindo:///hdfs/tmp.txt
```


我们还支持其他其他数据源的挂载，比如 S3、HDFS、NAS等，可参考以下文档

[JindoFSx 挂载 S3 数据源](/docs/user/4.x/4.5.0/jindofsx/multi-cloud/jindofsx_on_multi_cloud.md)

[JindoFSx 挂载 HDFS 数据源](/docs/user/4.x/4.5.0/jindofsx/hdfs/jindofsx_on_hdfs_jindo.md)

[JindoFSx 挂载 NAS 数据源](/docs/user/4.x/4.5.0/jindofsx/nas/jindofsx_on_nas_jindo.md)

