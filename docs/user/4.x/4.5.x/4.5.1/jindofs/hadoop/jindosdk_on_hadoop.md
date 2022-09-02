# Hadoop 使用 JindoSDK 访问阿里云 OSS-HDFS 服务（JindoFS 服务）

JindoSDK 为 Apache Hadoop的计算分析应用（例如MapReduce、Hive、Spark、Flink等）提供了访问 OSS-HDFS 服务功能。

## 步骤

### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

### 2. 安装 jar 包
解压下载的安装包，将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.5.1 为例:
```
cp jindosdk-4.5.1/lib/jindo-core-4.5.1.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.5.1/lib/jindo-sdk-4.5.1.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

### 3. 配置 OSS-HDFS 服务实现类及 Access Key

将 JindoSDK OSS-HDFS 服务实现类配置到 Hadoop 的`core-site.xml`中。

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
将已开启 HDFS 服务的 Bucket 对应的`Access Key ID`、`Access Key Secret`等预先配置在 Hadoop 的`core-site.xml`中。
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
</configuration>
```
JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoSDK Credential Provider 配置](../security/jindosdk_credential_provider_dls.md)。

### 4. 配置 OSS-HDFS 服务 Endpoint
访问 OSS Bucket 上 OSS-HDFS 服务需要配置 Endpoint（`cn-xxx.oss-dls.aliyuncs.com`），与 OSS 对象接口的 Endpoint（`oss-cn-xxx.aliyuncs.com`）不同。JindoSDK 会根据配置的 Endpoint 访问 OSS-HDFS 服务 或 OSS 对象接口。

使用 OSS-HDFS 服务时，推荐访问路径格式为：`oss://<Bucket>.<Endpoint>/<Object>`

如: `oss://dls-chenshi-test.cn-shanghai.oss-dls.aliyuncs.com/Test`。

这种方式在访问路径中包含 OSS-HDFS 服务的 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应的 JindoFS 接口。 JindoSDK 还支持更多的 Endpoint 配置方式，详情参考 [OSS-HDFS 服务 Endpoint 配置](/docs/user/4.x/4.5.x/4.5.1/jindofs/configuration/jindosdk_endpoint_configuration.md)。

### 5. 使用 JindoSDK 访问 OSS-HDFS 服务
用 Hadoop Shell 访问 JindoFS，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> oss://<Bucket>.<Endpoint>/
```

* ls 操作
```
hadoop fs -ls oss://<Bucket>.<Endpoint>/
```

* mkdir 操作
```
hadoop fs -mkdir oss://<Bucket>.<Endpoint>/<path>
```

* rm 操作
```
hadoop fs -rm oss://<Bucket>.<Endpoint>/<path>
```

### 6. 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](/docs/user/4.x/4.5.x/4.5.1/jindofs/configuration/jindosdk_configuration_list.md)