# HBase 使用 JindoSDK 访问 OSS

## 1. 安装 jar 包
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](../jindosdk_download.md))，将sdk包安装到hadoop的classpath下。
```
cp ./jindofs-sdk-*.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/jindofs-sdk.jar
```

## 2. 配置 JindoSDK OSS 实现类
将 JindoSDK OSS 实现类配置到 Hadoop 的 `core-site.xml` 中。
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.OSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```
## 3. 配置 OSS Access Key
将 OSS 的`Access Key ID`、`Access Key Secret`、`Endpoint`等预先配置在 Hadoop 的 `core-site.xml` 中。
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.endpoint</name>
      	<!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoSDK Credential Provider 配置](../security/jindosdk_credential_provider.md)。<br />

## 指定 HBase 的存储路径
指定 HBase 的存储路径，是需要修改`hbase-site`配置文件中的参数`hbase.rootdir`的值为 OSS 地址，修改参数`hbase.wal.dir`的值为本地的 HDFS 地址，通过本地 HDFS 集群存储 WAL 文件。如果要释放集群，需要先 Disable table，确保 WAL 文件已经完全更新到 HFile。

| 参数 | 描述 |
| --- | --- |
| hbase.rootdir | 指定 HBase 的 ROOT 存储目录到 OSS。参数值为`oss://bucket/hbase-root-dir`|
| hbase.wal.dir | 指定 HBase 的 WAL 存储目录到本地 HDFS 集群。</br> 参数值为：</br> HA集群：`hdfs://emr-cluster/hbase` </br> 非HA集群：`hdfs://emr-header-1:9000/hbase` |