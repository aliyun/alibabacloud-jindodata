# JindoFS SDK 使用
[English Version](./jindofs_sdk_how_to_en.md)


# 访问OSS（作为 OSS 客户端）

JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云OSS提供高度优化的Hadoop FileSystem实现。

即使您使用JindoFS SDK仅仅作为OSS客户端，相对于Hadoop社区OSS客户端实现，您还可以获得更好的性能和阿里云E-MapReduce产品技术团队更专业的支持。

目前支持的Hadoop版本包括Hadoop 2.7+和Hadoop 3.x。有问题请反馈，开PR，我们会及时处理。<br />
<br />关于JindoFS SDK和Hadoop社区OSS connector的性能对比，请参考文档[JindoFS SDK和Hadoop-OSS-SDK性能对比测试](./jindofs_sdk_vs_hadoop_sdk.md)。<br />

## 步骤

### 1. 安装jar包
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](/docs/jindofs_sdk_download.md))，将sdk包安装到hadoop的classpath下
```
cp ./jindofs-sdk-*.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/jindofs-sdk.jar
```

注意： 目前SDK只支持Linux、MacOS操作系统<br />

### 2. 访问OSS
然后就可以用以下方式访问OSS

```
hadoop fs -ls oss://<ak>:<secret>@<bucket>.<endpoint>/
```

#### 2.1 （可选）配置AK

上述方式，即在每个uri路径临时指定ak的方式比较繁琐，容易产生安全问题。您可以将oss的ak、secret、endpoint预先配置在hadoop的core-site.xml，配置项如下：
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
然后就可以用以下方式访问OSS
```
hadoop fs -ls oss://<bucket>/
```

<br />

# 访问JindoFS Cache/Block模式集群

当您已经创建了一个E-MapReduce JindoFS集群，并且希望在另外一个集群或ECS机器上访问JindoFS集群时，可以使用该方法。

### 前提条件

1. 已经创建了一个E-MapReduce JindoFS集群
2. 已知JindoFS集群header节点地址，另外一个集群或ECS的网络可以访问该地址

### 1. 安装jar包
下载并将sdk包安装到hadoop的classpath下。方法同上。

关于版本兼容性。SDK版本和JindoFS服务端之间跨小版本保持相互兼容，跨大版本不一定保证兼容。大版本为前两位，例如3.1.x。您可以从E-MapReduce集群的/usr/lib/b2jindosdk-current/lib/目录，获取与服务端版本完全相同的SDK包。


### 2. 创建客户端配置文件
将下面环境变量添加到/etc/profile文件中
```
export B2SDK_CONF_DIR=/etc/jindofs-sdk-conf
```
创建文件 /etc/jindofs-sdk-conf/bigboot.cfg  包含以下主要内容
```
[bigboot]
logger.dir = /tmp/bigboot-log

[bigboot-client]
client.storage.rpc.port = 6101
client.namespace.rpc.address = localhost:8101
```
需要将client.namespace.rpc.address修改为JindoFS集群的namespace服务的所在节点地址


### 3. 访问JindoFS集群

然后就可以用以下方式访问集群
```
hadoop fs -ls jfs://<namespace>/
```

<br />

# 在IDE工程中使用SDK开发调试代码

在maven中添加本地sdk jar包的依赖
```xml
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>2.8.5</version>
        </dependency>
        <dependency>
            <groupId>bigboot</groupId>
            <artifactId>jindofs</artifactId>
            <version>0.0.1</version>
            <scope>system</scope>
            <systemPath>/Users/xx/xx/jindofs-sdk-${version}.jar</systemPath>
            <!-- 请将${version}替换为具体的版本号 -->
        </dependency>
```
然后您可以编写Java程序使用SDK
```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

public class TestJindoSDK {
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(URI.create("oss://<bucket>/"), conf);
    FSDataInputStream in = fs.open(new Path("/uttest/file1"));
    in.read();
    in.close();
  }
}
```
注意，在IDE环境下，也要确保B2SDK_CONF_DIR环境变量已经设置。

<br />

## 附录: SDK配置项列表

jindofs-fuse可以进行一些参数调整，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](./jindofs_sdk_configuration_list.md)。
<br />

