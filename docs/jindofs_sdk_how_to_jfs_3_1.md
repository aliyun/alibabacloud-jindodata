# Hadoop 使用 Jindo SDK 访问 JindoFS
[English Version](./jindofs_sdk_how_to_en.md)


# 访问 JindoFS（作为 JindoFS 客户端）

JindoFS SDK 是一个简单易用面向 Hadoop/Spark 生态的 JindoFS / OSS客户端，为阿里云 JindoFS / OSS 提供高度优化的 Hadoop FileSystem 实现。

使用同一 JindoFS SDK 不仅可以作为 OSS 客户端，获得更好的性能和技术团队更专业的支持，同时也可以在 JindoFS 集群外部，访问 JindoFS。

 JindoFS SDK 目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈）<br />



# 访问 JindoFS Cache/Block模式集群

当您已经创建了一个E-MapReduce JindoFS集群，**您使用的 JindoFS 为3.1 及之后的版本**，并且希望在另外一个集群或ECS机器上访问JindoFS集群时，可以使用该方法。

### 前提条件

1. 已经创建了一个 E-MapReduce JindoFS 集群
2. 已知 JindoFS 集群header节点地址，另外一个集群或 ECS 的网络可以访问该地址

### 1. 安装jar包
下载 3.x 最新版本的 JindoSDK 并将 sdk 包安装到 hadoop 的 classpath 下。方法同上。

关于版本兼容性。3.1 及之后的版本推荐使用最新版本的 3.x SDK 版本。


### 2. 创建客户端配置文件
可以通过 Hadoop 配置 core-site.xml 或者 bigboot.cfg  配置的方式配置 Namespace 地址：

**（推荐）配置 Hadoop core-site.xml**

将下面配置添加到现有 core-site 文件中：

```xml
    <property>
        <name>fs.jfs.namespace.rpc-address</name>
        <!-- JindoFS 集群的 namespace rpc 地址，如您未使用Raft 配置，添加一个 rpc-address 即可 -->
        <value>172.16.xx.xx:8101,172.16.xx.xx:8101,172.16.xx.xx:8101</value>
    </property>

    <property>
        <name>jindo.common.accessKeyId</name>
        <!-- 访问 JindoFS OSS 后端存储的 accessKeyId  -->
        <value>您的ak</value>
    </property>

    <property>
        <name>jindo.common.accessKeySecret</name>
        <!-- 访问 JindoFS OSS 后端存储的 accessKeySecret  -->
        <value>您的Secret</value>
    </property>

    <property>
        <name>jindo.common.oss.endpoint</name>
        <!-- 访问 JindoFS OSS 后端存储的 Endpoint  -->
        <value>oss-cn-hangzhou.aliyuncs.com</value>
    </property>
```

配置完成后，可进行下一步，**如果您希望使用过去的方式配置 B2SDK  bigboot.cfg **，可参考 3.1 之前的 [配置方式](jindofs_sdk_how_to_jfs_3_0.md) 。


### 3. 访问 JindoFS 集群

然后就可以用以下方式访问集群
```bash
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
    conf.set("fs.jfs.namespace.rpc-address","172.16.xx.xx:8101,172.16.xx.xx:8101,172.16.xx.xx:8101");
    conf.set("jindo.common.accessKeyId","your oss accessKeyId");
    conf.set("jindo.common.accessKeySecret","your oss accessKeySecret");
    conf.set("jindo.common.oss.endpoint","oss-cn-xxxx.aliyuncs.com");
    FileSystem fs = FileSystem.get(URI.create("jfs://<namespace>/"), conf);
    FSDataInputStream in = fs.open(new Path("/uttest/file1"));
    in.read();
    in.close();
  }
}
```


<br />

## 附录: SDK配置项列表

JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档  [JindoFS SDK 配置项列表](jindofs_sdk_configuration_list_3_x.md) 【注：3.0 以下版本此 [参考配置项列表](./jindofs_sdk_configuration_list.md)】
<br />

