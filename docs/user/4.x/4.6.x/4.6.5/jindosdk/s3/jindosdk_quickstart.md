# JindoSDK + S3 快速入门

JindoSDK 全面兼容 Hadoop FileSystem 接口，提供了更好的兼容性和易用性。
相对于 Hadoop 社区 S3 客户端实现，您仍然可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

## 步骤

### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

### 2. 安装 jar 包
解压下载的安装包，将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.6.5 为例:
```
cp jindosdk-4.6.5/lib/jindo-core-4.6.5.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.6.5/lib/jindo-sdk-4.6.5.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

### 3. 配置 JindoSDK S3 实现类

将 JindoSDK S3 实现类配置到 Hadoop 的 `core-site.xml` 中。

```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.s3.impl</name>
        <value>com.aliyun.jindodata.s3.S3</value>
    </property>

    <property>
        <name>fs.s3.impl</name>
        <value>com.aliyun.jindodata.s3.JindoS3FileSystem</value>
    </property>
</configuration>
```

### 4. 配置 S3 Access Key

将 Access Key 配置到 Hadoop 的 `core-site.xml` 中。

#### 配置 S3 Access Key

将 S3 bucket 对应 的`Access Key ID`、`Access Key Secret`、`Endpoint` 等预先配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.s3.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.s3.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.s3.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```

#### 按照 bucket 配置 Access Key

JindoSDK 还支持不同的 S3 bucket 配置不同的 Access Key。

```xml
<configuration>
    <property>
        <name>fs.s3.bucket.XXX.accessKeyId</name>
        <value>S3 bucket 的 AccessKey Id</value>
    </property>
    <property>
        <name>fs.s3.bucket.XXX.accessKeySecret</name>
        <value>S3 bucket 的 AccessKey Secret</value>
    </property>
</configuration>
```
说明 XXX 为 S3 bucket 名称。

### 5. 使用 JindoSDK 访问 S3
用 Hadoop Shell 访问 S3，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> s3://<bucket>/
```

* ls 操作
```
hadoop fs -ls s3://<bucket>/
```

* mkdir 操作
```
hadoop fs -mkdir s3://<bucket>/<path>
```

* rm 操作
```
hadoop fs -rm s3://<bucket>/<path>
```