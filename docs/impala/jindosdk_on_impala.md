# Impala 使用 JindoFS SDK 访问 OSS


JindoFS SDK是一个简单易用面向Hadoop/Spark生态的OSS客户端，为阿里云OSS提供高度优化的Hadoop FileSystem实现, Impala 使用 JindoFS SDK 相对于使用 Hadoop 社区 OSS 客户端，可以获得更好的性能,同时还能获得阿里云 E-MapReduce 产品技术团队更专业的支持。

---

## 步骤

### 1. Impala 已经配置 HADOOP 相关配置
确保Impala的配置文件中包含了 HADOOP 相关的配置文件。

### 2. 在所有 Impala 节点安装 JindoFS SDK
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](../jindofs_sdk_download.md))，将sdk包安装到 Spark 的classpath下。
````
cp jindofs-sdk-${version}.jar  $IMPALA_HOME/lib/
````


### 3. 配置 JindoFS SDK  
#### 配置 IMPALA 使用的 core-site.xml 配置 JindoFS SDK  访问 OSS
* 配置 JindoFS OSS 实现类
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
* 配置 OSS Access Key
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
      	<!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](../jindofs_sdk_credential_provider.md)。<br />


### 4. 使用 Impala 访问 OSS。

下载测试数据集[链接](../test_data/customer_demographics/part-00000-2ac0f56e-0834-45b5-b27a-9e2e6babc6be-c000.snappy.parquet)

上传数据
 ```  
hadoop fs -put test_data oss://bucket/dir
 ```

创建表
 ```  
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
 ```  
select * from customer_demographics;
 ``` 

### 5. 参数调优
JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](../jindofs_sdk_configuration_list.md)。
<br />
