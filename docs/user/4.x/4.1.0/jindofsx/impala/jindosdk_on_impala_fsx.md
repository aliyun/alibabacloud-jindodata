# Impala 查询 JindoFSx 统一挂载的数据

JindoSDK 是一个简单易用面向 Hadoop/Spark 生态的 OSS 客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现, Impala 使用 JindoSDK 相对于使用 Hadoop 社区 OSS 客户端，可以获得更好的性能,同时还能获得阿里云 E-MapReduce 产品技术团队更专业的支持。

## 步骤

### 1. Impala 已经配置 HADOOP 相关配置
确保Impala的配置文件中包含了 HADOOP 相关的配置文件。

### 2. 在所有 Impala 节点安装 JindoSDK
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))，解压后将 sdk 包安装到 Impala 的 classpath 下。

````
cp jindosdk-x.x.x/lib/*.jar  $IMPALA_HOME/lib/
````

### 3. 配置 JindoSDK  
#### 配置 IMPALA 使用的 `core-site.xml` 配置 JindoSDK 访问 OSS
* 配置 JindoSDK OSS 实现类
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.fsx.impl</name>
        <value>com.aliyun.jindodata.fsx.FSX</value>
    </property>

    <property>
        <name>fs.fsx.impl</name>
        <value>com.aliyun.jindodata.fsx.JindoFsxFileSystem</value>
    </property>
</configuration>
```
* 配置 OSS Access Key
```xml
<configuration>
    <property>
        <name>fs.fsx.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.fsx.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 AccessKey 的配置方式，详情参考 [JindoFSx 统一挂载(fsx://) Credential 配置](../security/jindosdk_credential.md)。

* 配置 OSS 或 OSS-HDFS 服务 Endpoint
```
<configuration>
    <property>
        <name>fs.fsx.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```
如果统一挂载的为 OSS-HDFS 服务目录，配置 Endpoint 请参考 [JindoFSx 缓存系统配置 OSS-HDFS 服务 Endpoint](../configuration/dls_endpoint_configuration.md)。

* 配置 JindoFSx Namespace 服务地址

配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- Namespace service 地址 -->
        <name>fs.fsx.namespace.rpc.address</name>
        <value>headerhost:8101</value>
    </property>
</configuration>
```
若使用高可用 Namespace, 请参考 [高可用 JindoFSx Namespace 配置和使用](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_raft_ns.md)

* 开启缓存。

配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- 数据缓存开关 -->
        <name>fs.fsx.data.cache.enable</name>
        <value>true</value>
    </property>
</configuration>
```
更多缓存优化相关参数，请参考 [缓存优化相关参数](../configuration/jindosdk_configuration_list.md)

* 挂载 OSS 或 OSS-HDFS 服务目录

```
jindo fsxadmin -mount <path> <realpath>
```

例：
```
jindo fsxadmin -mount /jindooss oss://<Bucket>.<Endpoint>/
```

执行如上命令后，则 /jindooss 目录下真正挂载的文件路径是 `oss://<Bucket>.<Endpoint>/`

### 4. 使用 Impala 访问 OSS。

创建表
 ```  sql
CREATE EXTERNAL TABLE customer_demographics (
  `cd_demo_sk` INT,
  `cd_gender` STRING,
  `cd_marital_status` STRING,
  `cd_education_status` STRING,
  `cd_purchase_estimate` INT,
  `cd_credit_rating` STRING,
  `cd_dep_count` INT,
  `cd_dep_employed_count` INT,
  `cd_dep_college_count` INT)
STORED AS PARQUET
LOCATION 'fsx://headerhost:8101/jindooss/dir'; 
 ```

查询 OSS 表
 ```  sql
select * from customer_demographics;
 ```

### 5. 参数调优
JindoSDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](../configuration/jindosdk_configuration_list.md)。
