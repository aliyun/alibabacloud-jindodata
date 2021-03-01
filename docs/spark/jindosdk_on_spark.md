# Spark 使用 JindoSDK 访问 OSS

# 前置

---
* 您的 Spark 已经配置 HADOOP 相关参数，并正确配置了 core-site.xml

# 使用

### 在 Spark CLASSPATH 中添加 JindoSDK
[下载地址](jindofs_sdk_download.md)
````
cp jindofs-sdk-${version}.jar  $SPARK_HOME/jars/
````

### 通过配置 Spark 使用的 core-site.xml 配置 JindoSDK  访问 OSS(作用于使用这个 core-site.xml 的所有 Spark 程序)
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
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](../jindofs_sdk_credential_provider.md)。<br />

### 通过修改 Spark 启动命令方式配置 JindoSDK 访问 OSS(作用于当前任务)
以下为示例:
 ```  
spark-submit --conf spark.hadoop.fs.AbstractFileSystem.oss.impl=com.aliyun.emr.fs.oss.OSS --conf spark.hadoop.fs.oss.impl=com.aliyun.emr.fs.oss.JindoOssFileSystem --conf spark.hadoop.fs.jfs.cache.oss.accessKeyId=xxx  --conf spark.hadoop.fs.jfs.cache.oss.accessKeySecret=xxx --conf spark.hadoop.fs.jfs.cache.oss.endpoint=oss-cn-xxx.aliyuncs.com 
 ``` 
### 启动 Spark, 即可访问 OSS。

### 参数调优
JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](../jindofs_sdk_configuration_list.md)。
<br />
