# JindoFS SDK User Guide
[中文版](./jindofs_sdk_how_to.md)


# Access to OSS (as an OSS client)

JindoFS SDK is an easy-to-use OSS client for Hadoop/Spark ecosystem, providing highly optimized Hadoop FileSystem implementation for Ali Cloud OSS.<br />
<br />Even if you use JindoFS SDK only as an OSS client, you can get better performance and more professional support from the Aliyun E-MapReduce technical team than the OSS client implementation of Hadoop community.<br />
<br />Currently JindoFS SDK supported Hadoop 2.7+ and Hadoop 3.x versions. If you have any questions, please give feedback. Open PR, and we will deal with it in time.<br />
<br />For a performance comparison between the JindoFS SDK and the Hadoop Community OSS Connector, refer to the documentation [Performance Comparison of JindoFS SDK and Hadoop-OSS-SDK](./jindofs_sdk_vs_hadoop_sdk_en.md).<br />

## Steps

### 1. Deploy JindoFS SDK jar
Download the latest JindoFS SDK jar package jindofs-sdk-x.x.x.jar and install the SDK package under the Hadoop classpath.
```
cp ./jindofs-sdk-*.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/jindofs-sdk.jar
```

Note： currently, JindoFS SDK only supports Linux and MacOS operating systems.<br />

### 2. Access to OSS

```
hadoop fs -ls oss://<ak>:<secret>@<bucket>.<endpoint>/
```

#### 2.1 （optional）Pre-configure Access Key

You can also pre-configure ak, secret and endpoint of OSS to hadoop core-site.xml to avoid filling in these each time you use it：
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
For the detail configurations of OSS Credential, please refer to the document [Credential Provider User Guide](jindosdk_credential_provider.md).<br />
Then OSS can then be accessed in the following way:
```
hadoop fs -ls oss://<bucket>/
```
<br />

# Access to JindoFS Cache/Block Mode Cluster

When you have already created an E-MapReduce JindoFS cluster, and you need access to the JindoFS cluster in another cluster or in ECS node, you can use this approach.

### Prerequisite

1. Already have an E-MapReduce JindoFS cluster
2. Already knows the JindoFS header node(s) address, and the other cluster or ECS node is able to connect to the address.

### 1. Deploy JindoFS SDK jar
Install the SDK package under the Hadoop classpath. The method is same as above.

About the version compatibility. The SDK and the server are compatible if their versions differ within minor number. You can get SDK package that has exactly same version as the server from the directory /usr/lib/b2jindosdk-current/lib/ in the E-MapReduce cluster.


### 2. Create a client configuration file
Add the following environment variable to /etc/profile
```
export B2SDK_CONF_DIR=/etc/jindofs-sdk-conf
```
Create a file /etc/jindofs-sdk-conf/bigboot.cfg with following contents
```
[bigboot]
logger.dir = /tmp/bigboot-log

[bigboot-client]
client.storage.rpc.port = 6101
client.namespace.rpc.address = localhost:8101
```
You need set client.namespace.rpc.address to the address of JindoFS namespace service.


### 3. Access to JindoFS Cluster

Then JindoFS cluster can then be accessed in the following way:
```
hadoop fs -ls jfs://<namespace>/
```

<br />

# Using SDK in IDE

Add local JindoFS SDK jars to maven dependencies.
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
            <!-- Please set ${version} to specific number -->
        </dependency>
```
Then you can write a Java program using the JindoFS SDK.
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
Note: make sure the B2SDK_CONF_DIR environment variable is set in an IDE environment.

<br />

# Appendix: SDK Configuration

configuration file name is bigboot.cfg, the file has 2 sections: bigboot section about logs，bigboot-client about client configurations. The following is a template:

```
[bigboot]
logger.dir = /tmp/bigboot-log
logger.sync = false

[bigboot-client]
client.storage.rpc.port = 6101
client.namespace.rpc.address = header-1:8101

```


The followings are some important configurations:
| Key                                    | Section          | Default           | Description                                                            |
| ----------------------------------------- | ---------------- | ---------------- | --------------------------------------------------------------- |
| logger.dir                                | [bigboot]        | /tmp/bigboot-log | log destination                                                        |
| logger.sync                               | [bigboot]        | false            | if should flush to disk on every log                                           |
| client.storage.rpc.port                   | [bigboot-client] | 6101             | local storage service port                                       |
| client.namespace.rpc.address              | [bigboot-client] | localhost:8101   | the namespace service address wanted to <br />access to（block/cache mode） |
| client.oss.retry                          | [bigboot-client] | 5                | max retry count if oss request failure                                             |
| client.oss.upload.threads                 | [bigboot-client] | 5                | oss concurrent upload threads                                       |
| client.oss.upload.queue.size              | [bigboot-client] | 5                | oss upload queue size                                         |
| client.oss.upload.max.parallelism         | [bigboot-client] | 16               | oss upload parallelism per process                                     |
| client.oss.timeout.millisecond            | [bigboot-client] | 30000            | oss request timeout in millseconds                                         |
| client.oss.connection.timeout.millisecond | [bigboot-client] | 3000             | oss connection timeout in millseconds                                         |
| client.read.oss.readahead.buffer.size     | [bigboot-client] | 1048576          | buffer size for oss readahead per file                                             |
| client.read.oss.readahead.buffer.count    | [bigboot-client] | 4                | buffer counts for oss readahead                                         |
| jfs.cache.data-cache.enable               | [bigboot-client] | false            | (only for cache mode)enable data cache                          |

<br />


# Release Notes

### v3.1.1
Date：20201207<br />文件：[jindofs-sdk-3.1.1.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-3.1.1.jar)<br />

1. Fix some bugs.
2. Minor performance improvement.
3. Compatibility improvement.


### v3.0.0
Date：20201016<br />File：[jindofs-sdk-3.0.0.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-3.0.0.jar)<br />Major fixes：

1. Fix some bugs.


### v2.7.401
Date：20190914<br />File：[jindofs-sdk-2.7.401.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-2.7.401.jar)<br />Major fixes：

1. Fix many bugs.


### v2.7.1
Date：20190619<br />File：[jindofs-sdk-2.7.1.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/jindofs-sdk-2.7.1.jar)<br />Major Features：

1. Supports access to OSS (as an OSS client)
1. Supports access to JindoFS Cache mode cluster
1. Supports access to JindoFS Block mode cluster



