# HBase 使用 OSS 作为底层存储
HBase 是 Hadoop 生态中的实时数据库，有很高的写入性能。JindoSDK 支持 HBase 使用 OSS 作为底层存储，但需通过本地 HDFS 集群存储 WAL 文件。

## 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 配置环境变量
* 配置`JINDOSDK_HOME`

解压下载的安装包，以安装包内容解压在`/usr/lib/jindosdk-4.0.0`目录为例：
```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-4.0.0
```
* 配置`HADOOP_CLASSPATH`

```bash
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```

## 3. 配置 JindoFS服务实现类及 Access Key

将 JindoSDK OSS 实现类配置到 Hadoop 的 `core-site.xml` 中。

```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOSS</value>
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
        <value>cn-xxx.oss.aliyuncs.com</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考 [JindoSDK Credential Provider 配置](../security/jindosdk_credential_provider.md)。


## 指定 HBase 的存储路径
指定 HBase 的存储路径，是需要修改`hbase-site`配置文件中的参数`hbase.rootdir`的值为 OSS 地址，修改参数`hbase.wal.dir`的值为本地的 HDFS 地址，通过本地 HDFS 集群存储 WAL 文件。如果要释放集群，需要先 Disable table，确保 WAL 文件已经完全更新到 HFile。

| 参数 | 描述                                                                                                                                                         |
| --- |------------------------------------------------------------------------------------------------------------------------------------------------------------|
| hbase.rootdir | 指定 HBase 的 ROOT 存储目录到 OSS。参数值为`oss://bucket/hbase-root-dir`                                                                                                |
| hbase.wal.dir | 指定 HBase 的 WAL 存储目录到本地 HDFS 集群。</br> 参数值为：</br> HA集群：`hdfs://${nameservice}/hbase` </br> 非HA集群：`hdfs://${namenode_rpc_address}:${namenode_rpc_port}/hbase` |