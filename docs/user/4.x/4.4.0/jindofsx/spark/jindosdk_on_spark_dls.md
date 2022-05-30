# Spark 处理阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据 + JindoFSx 透明加速

JindoSDK是一个简单易用面向 Hadoop/Spark 生态的 OSS 客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现, Spark 使用 JindoSDK 相对于使用 Hadoop 社区 OSS 客户端，可以获得更好的性能，同时还能获得阿里云 E-MapReduce 产品技术团队更专业的支持。

## 步骤

### 1. Spark 已经配置 HADOOP 相关配置
确保 Spark 的配置文件中包含了 HADOOP 相关的配置文件。

### 2. 在 Spark CLASSPATH 中添加 JindoSDK
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))，解压后将sdk包安装到 Spark 的 classpath下。

```
cp jindosdk-x.x.x/lib/*.jar  $SPARK_HOME/jars/
```

### 3. 配置 JindoSDK 
#### 全局配置
修改 Spark 使用的 `core-site.xml`。
* 配置 JindoSDK OSS-HDFS 服务实现类

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

    <property>
        <name>fs.xengine</name>
        <value>jindofsx</value>
    </property>
</configuration>
```
* 配置 OSS-HDFS 服务 Access Key

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

JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoSDK OSS-HDFS 服务 Credential Provider 配置](/docs/user/4.x/4.4.0/jindofs/security/jindosdk_credential_provider_dls.md)。

* 配置 OSS-HDFS 服务 Endpoint

访问 OSS Bucket 上 OSS-HDFS 服务需要配置 Endpoint（`cn-xxx.oss-dls.aliyuncs.com`），与 OSS 对象接口的 Endpoint（`oss-cn-xxx-internal.aliyuncs.com`）不同。JindoSDK 会根据配置的 Endpoint 访问 OSS-HDFS 服务或 OSS 对象接口。

使用 OSS-HDFS 服务时，推荐访问路径格式为：`oss://<Bucket>.<Endpoint>/<Object>`

如: `oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test`。

这种方式在访问路径中包含 OSS-HDFS 服务的 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应的 OSS-HDFS 服务接口。 JindoSDK 还支持更多的 Endpoint 配置方式，详情参考 [OSS-HDFS 服务 Endpoint 配置](/docs/user/4.x/4.4.0/jindofs/configuration/jindosdk_endpoint_configuration.md)。

* 配置 JindoFSx Namespace 服务地址。

```xml
<configuration>
    <property>
        <!-- Namespace service 地址 -->
        <name>fs.jindofsx.namespace.rpc.address</name>
        <value>headerhost:8101</value>
    </property>
</configuration>
```
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.4.0/jindofsx/deploy/deploy_raft_ns.md)

* 开启缓存。

```xml
<configuration>
    <property>
        <!-- 数据缓存开关 -->
        <name>fs.jindofsx.data.cache.enable</name>
        <value>true</value>
    </property>
</configuration>
```
更多缓存优化相关参数，请参考 [缓存优化相关参数](../configuration/jindosdk_configuration_list.md)

#### 任务级别配置
使用参数在 Spark 任务提交的时候设置 JindoSDK, 以下为示例:
```  
spark-submit --conf spark.hadoop.fs.AbstractFileSystem.oss.impl=com.aliyun.jindodata.oss.OSS --conf spark.hadoop.fs.oss.impl=com.aliyun.jindodata.oss.JindoOssFileSystem --conf spark.hadoop.fs.oss.accessKeyId=xxx  --conf spark.hadoop.fs.oss.accessKeySecret=xxx --conf spark.hadoop.fs.jindofsx.namespace.rpc.address=hostname:port --conf spark.hadoop.fs.jindofsx.data.cache.enable=true
```

### 4. 使用 Spark 访问 OSS
创建表
```  
create table test_oss (c1 string) location "oss://test-bucket.<Endpoint>/<path>";
```
插入数据
```  
insert into table test_oss values ("testdata");
```

查询 OSS 表
```  
select * from test_oss;
```

### 5. 参数调优
JindoSDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](../configuration/jindosdk_configuration_list.md)。
