# Hadoop 使用 JindoFS SDK 访问 OSS
[English Version](./jindofs_sdk_how_to_en.md)

# 使用 JindoFS SDK 访问 OSS

JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云OSS提供高度优化的Hadoop FileSystem实现。

即使您使用 JindoFS SDK 仅仅作为 OSS 客户端，相对于 Hadoop 社区 OSS 客户端实现，您还可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈）<br />关于JindoFS SDK和Hadoop社区 OSS connector的性能对比，请参考文档[JindoFS SDK和Hadoop-OSS-SDK性能对比测试](./jindofs_sdk_vs_hadoop_sdk.md)。<br />

## 步骤

### 1. 安装jar包
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](/docs/jindofs_sdk_download.md))，将sdk包安装到hadoop的classpath下
```
cp ./jindofs-sdk-*.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/jindofs-sdk.jar
```

注意： 目前SDK只支持Linux、MacOS操作系统<br />

### 2. 配置

#### 2.1 配置JindoFS OSS实现类
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
然后就可以用以下方式访问OSS：

```
hadoop fs -ls oss://<ak>:<secret>@<bucket>.<endpoint>/
```
上述方式，即在每个uri路径临时指定ak的方式比较繁琐，容易产生安全问题。您可以将oss的ak、secret、endpoint预先配置在hadoop的core-site.xml，详见2.2。
#### 2.2 配置OSS Access Key
OSS Access Key 配置在hadoop的core-site.xml中。
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
然后就可以用以下方式访问OSS：
```
hadoop fs -ls oss://<bucket>/
```

## 附录: SDK配置项列表

JindoFS SDK可以进行一些参数调整，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](./jindofs_sdk_configuration_list.md)。
<br />

