# Hadoop 使用 JindoFS SDK 访问 OSS

JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云OSS提供高度优化的Hadoop FileSystem实现。

即使您使用 JindoFS SDK 仅仅作为 OSS 客户端，相对于 Hadoop 社区 OSS 客户端实现，您还可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈）<br />关于JindoFS SDK和Hadoop社区 OSS connector的性能对比，请参考文档[JindoFS SDK和Hadoop-OSS-SDK性能对比测试](./jindofs_sdk_vs_hadoop_sdk.md)。<br />

## 步骤

### 1. 安装 jar 包
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](/docs/jindofs_sdk_download.md))，将sdk包安装到hadoop的classpath下。
```
cp ./jindofs-sdk-*.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/jindofs-sdk.jar
```

### 2. 配置 JindoFS OSS 实现类
将 JindoFS OSS 实现类配置到Hadoop的core-site.xml中。
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
### 3. 配置 OSS Access Key
将OSS的Access Key、Access Key Secret、Endpoint等预先配置在Hadoop的core-site.xml中。
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
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](./jindofs_sdk_credential_provider.md)。<br />

### 4. 使用 JindoFS SDK 访问 OSS
用Hadoop Shell访问OSS，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> oss://<bucket>/
```

* ls 操作
```
hadoop fs -ls oss://<bucket>/
```

* mkdir 操作
```
hadoop fs -mkdir oss://<bucket>/<path>
```

* rm 操作
```
hadoop fs rm oss://<bucket>/<path>
```
<div align=center>
<img src="../pic/jindofs_sdk_cmd.png#pic_center" />
</div>

### 5. 参数调优
JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](./jindofs_sdk_configuration_list.md)。
<br />