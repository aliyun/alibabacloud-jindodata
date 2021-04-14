# Hadoop 使用 Jindo SDK 访问 JindoFS
[English Version](./jindofs_sdk_how_to_en.md)


# 访问 JindoFS（作为 JindoFS 客户端）

JindoFS SDK 是一个简单易用面向 Hadoop/Spark 生态的 JindoFS / OSS客户端，为阿里云 JindoFS / OSS 提供高度优化的 Hadoop FileSystem 实现。

使用同一 JindoFS SDK 不仅可以作为 OSS 客户端，获得更好的性能和技术团队更专业的支持，同时也可以在 JindoFS 集群外部，访问 JindoFS。

 JindoFS SDK 目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈）<br />



# 访问 JindoFS Cache/Block模式集群

当您已经创建了一个E-MapReduce JindoFS集群，**您使用的 JindoFS 为3.0 及之前的版本**，并且希望在另外一个集群或ECS机器上访问JindoFS集群时，可以使用该方法。

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

JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档  [JindoFS SDK 配置项列表](jindofs_sdk_configuration_list_3_x.md) 【注：3.0 以下版本此 [参考配置项列表](./jindofs_sdk_configuration_list.md)】
<br />

