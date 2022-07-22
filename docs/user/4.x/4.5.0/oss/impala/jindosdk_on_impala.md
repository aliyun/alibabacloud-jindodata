# Impala 使用 JindoSDK 查询 OSS 上的数据

JindoSDK 是一个简单易用面向 Hadoop/Spark 生态的 OSS 客户端，为阿里云OSS提供高度优化的 Hadoop FileSystem 实现, Impala 使用 JindoSDK 相对于使用 Hadoop 社区 OSS 客户端，可以获得更好的性能,同时还能获得阿里云 E-MapReduce 产品技术团队更专业的支持。

## 步骤

### 1. Impala 已经配置 HADOOP 相关配置
确保Impala的配置文件中包含了 HADOOP 相关的配置文件。

### 2. 在所有 Impala 节点安装 JindoSDK
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))，解压后将sdk包安装到 Impala 的 classpath 下。

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
        <value>com.aliyun.jindodata.oss.OSS</value>
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

    <property>
        <name>fs.oss.endpoint</name>
        <value>cn-xxx.oss.aliyuncs.com</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考[JindoSDK OSS Credential Provider 配置](../security/jindosdk_credential_provider_oss.md)。


### 4. 使用 Impala 访问 OSS。

下载测试数据集[链接](/docs/user/4.x/4.0.0/oss/impala/test_data/customer_demographics/part-00000-2ac0f56e-0834-45b5-b27a-9e2e6babc6be-c000.snappy.parquet)

上传数据
 ```  bash
hadoop fs -put test_data oss://bucket/dir
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
LOCATION 'oss://bucket/dir'; 
 ```

查询 OSS 表
 ```  sql
select * from customer_demographics;
 ```

### 5. 参数调优
JindoSDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](/docs/user/4.x/4.5.0/oss/configuration/jindosdk_configuration_list.md)。
