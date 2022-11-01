# HBase 使用 OSS 作为底层存储
HBase 是 Hadoop 生态中的实时数据库，有很高的写入性能。JindoSDK 支持 HBase 使用 OSS 作为底层存储，但需通过本地 HDFS 集群存储 WAL 文件。

## 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 安装 jar 包
解压下载的安装包，将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.6.0 为例:
```
cp jindosdk-4.6.0/lib/jindo-core-4.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.6.0/lib/jindo-sdk-4.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

## 3. 配置 OSS 实现类及 Access Key

将 JindoSDK OSS 实现类配置到 Hadoop 的 `core-site.xml` 中。

```xml
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
```
将 OSS bucket 对应 的 `Access Key ID`、`Access Key Secret`、`Endpoint` 等预先配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.oss.endpoint</name>
        <!-- 阿里云 ECS 环境下推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考 [JindoSDK OSS Credential Provider 配置](../security/jindosdk_credential_provider_oss.md)。


## 指定 HBase 的存储路径
指定 HBase 的存储路径，需要修改`hbase-site`配置文件中的参数`hbase.rootdir`的值为 OSS 地址，指定 HBase 的存储路径和 WAL 文件的存储路径。如果要释放集群，需要先 Disable table，确保 WAL 文件已经完全更新到 HFile。

| 参数 | 描述                                                                                                                                                         |
| --- |------------------------------------------------------------------------------------------------------------------------------------------------------------|
| hbase.rootdir | 指定 HBase 的 ROOT 存储目录到 OSS。参数值为`oss://bucket/hbase-root-dir`                                                                                                |
