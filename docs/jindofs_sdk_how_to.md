# JindoFS SDK 使用
[English Version](./jindofs_sdk_how_to_en.md)


# 访问OSS（作为 OSS 客户端）

JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云OSS提供高度优化的Hadoop FileSystem实现。

即使您使用JindoFS SDK仅仅作为OSS客户端，相对于Hadoop社区OSS客户端实现，您还可以获得更好的性能和阿里云E-MapReduce产品技术团队更专业的支持。

目前支持的Hadoop版本包括Hadoop 2.7+和Hadoop 3.x。有问题请反馈，开PR，我们会及时处理。<br />
<br />关于JindoFS SDK和Hadoop社区OSS connector的性能对比，请参考文档[JindoFS SDK和Hadoop-OSS-SDK性能对比测试](./jindofs_sdk_vs_hadoop_sdk.md)。<br />

## 步骤

### 1. 安装jar包
下载最新的jar包 jindofs-sdk-x.x.x.jar ，将sdk包安装到hadoop的classpath下
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

# 附录: SDK配置项列表

配置文件名为bigboot.cfg，该文件会使用2个section，其中bigboot是日志相关配置，bigboot-client是客户端相关配置。配置模板如下

```
[bigboot]
logger.dir = /tmp/bigboot-log
logger.sync = false

[bigboot-client]
client.storage.rpc.port = 6101
client.namespace.rpc.address = header-1:8101

```


以下是重要的配置项列表
| 配置项                                    | section          | 默认值           | 说明                                                            |
| ----------------------------------------- | ---------------- | ---------------- | --------------------------------------------------------------- |
| logger.dir                                | [bigboot]        | /tmp/bigboot-log | 日志目录                                                        |
| logger.sync                               | [bigboot]        | false            | 是否同步flush日志文件                                           |
| client.storage.rpc.port                   | [bigboot-client] | 6101             | 本地storage服务进程端口号                                       |
| client.namespace.rpc.address              | [bigboot-client] | localhost:8101   | 访问JindoFS集群的namespace服务地址<br />（block/cache模式必填） |
| client.oss.retry                          | [bigboot-client] | 5                | 访问oss失败重试次数                                             |
| client.oss.upload.threads                 | [bigboot-client] | 5                | 单个文件oss并发上传线程数                                       |
| client.oss.upload.queue.size              | [bigboot-client] | 5                | oss并发上传任务队列大小                                         |
| client.oss.upload.max.parallelism         | [bigboot-client] | 16               | 进程内oss最大并发上传任务数                                     |
| client.oss.timeout.millisecond            | [bigboot-client] | 30000            | 请求oss超时时间（毫秒）                                         |
| client.oss.connection.timeout.millisecond | [bigboot-client] | 3000             | 连接oss超时时间（毫秒）                                         |
| client.read.oss.readahead.buffer.size     | [bigboot-client] | 1048576          | 预读oss的buffer大小                                             |
| client.read.oss.readahead.buffer.count    | [bigboot-client] | 4                | 同时预读oss的buffer个数                                         |
| jfs.cache.data-cache.enable               | [bigboot-client] | false            | (仅用于cache模式)cache模式开启缓存功能                          |

<br />


# 发布日志

### v3.1.1
日期：20201207<br />文件：[jindofs-sdk-3.1.1.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-3.1.1.jar)<br />更新内容：

1. 修复多个bugs.
2. 小的性能优化.
3. 改善兼容性.


### v3.0.0
日期：20201016<br />文件：[jindofs-sdk-3.0.0.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-3.0.0.jar)<br />更新内容：

1. 修复多个bugs.


### v3.0.0
日期：20201016<br />文件：[jindofs-sdk-3.0.0.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-3.0.0.jar)<br />更新内容：

1. 修复多个bugs.


### v2.7.401
日期：20190914<br />文件：[jindofs-sdk-2.7.401.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-2.7.401.jar)<br />更新内容：

1. 修复多个bugs.


### v2.7.1
日期：20190619<br />文件：[jindofs-sdk-2.7.1.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-2.7.1.jar)<br />更新内容：

1. 支持访问OSS （作为OSS客户端）
1. 支持访问JindoFS Cache模式集群
1. 支持访问JindoFS Block模式集群

<br />
