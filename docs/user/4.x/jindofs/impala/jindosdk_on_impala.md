# Impala 使用 JindoSDK 查询阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据

JindoSDK 是一个简单易用面向 Hadoop/Spark 生态的 OSS 客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现, Impala 使用 JindoSDK 相对于使用 Hadoop 社区 OSS 客户端，可以获得更好的性能,同时还能获得阿里云 E-MapReduce 产品技术团队更专业的支持。

## 步骤

### 1. Impala 已经配置 HADOOP 相关配置
确保Impala的配置文件中包含了 HADOOP 相关的配置文件。

### 2. 在所有 Impala 节点安装 JindoSDK
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](../jindosdk_download.md))，解压后将 sdk 包安装到 Impala 的 classpath 下。

````
cp jindosdk-x.x.x/lib/*.jar  $IMPALA_HOME/lib/
````

### 3. 配置 JindoSDK  
#### 配置 IMPALA 使用的 `core-site.xml` 配置 JindoSDK 访问 OSS
* 配置 JindoSDK OSS 实现类
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```
* 配置 OSS Access Key
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
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考[JindoSDK Credential Provider 配置](../security/jindosdk_credential_provider.md)。

* 配置 JindoFS 服务 Endpoint

访问 OSS Bucket 上 JindoFS 服务需要配置 Endpoint（`cn-xxx.oss-dls.aliyuncs.com`），与 OSS 对象接口的 Endpoint（`oss-cn-xxx.aliyuncs.com`）不同。JindoSDK 会根据配置的 Endpoint 访问 JindoFS 服务 或 OSS 对象接口。

使用 JindoFS 服务时，推荐访问路径格式为：`oss://<Bucket>.<Endpoint>/<Object>`

如: `oss://dls-chenshi-test.cn-shanghai.oss-dls.aliyuncs.com/Test`。

这种方式在访问路径中包含 JindoFS 服务的 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应的 JindoFS 服务接口。 JindoSDK 还支持更多的 Endpoint 配置方式，详情参考[JindoFS 服务 Endpoint 配置](configuration/jindosdk_endpoint_configuration.md)。

### 4. 使用 Impala 访问 OSS。

下载测试数据集[链接](test_data/customer_demographics/part-00000-2ac0f56e-0834-45b5-b27a-9e2e6babc6be-c000.snappy.parquet)

上传数据
 ```  bash
hadoop fs -put test_data oss://bucket.endpoint/dir
 ```

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
LOCATION 'oss://bucket.endpoint/dir'; 
 ```

查询 OSS 表
 ```  sql
select * from customer_demographics;
 ```

### 5. 参数调优
JindoSDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](../configuration/jindosdk_configuration_list.md)。
<br />
