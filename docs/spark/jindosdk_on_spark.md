# Spark 使用 JindoSDK 访问 OSS

# 前置

---
* 您的 Spark 已经配置 HADOOP 相关参数，并正确配置了 core-site.xml

# 使用

## 在 Spark CLASSPATH 中添加 JindoSDK
下载[地址](jindofs_sdk_download.md)
````
cp jindofs-sdk-${version}.jar  $SPARK_HOME/jars/
````

## 配置 JindoSDK 访问 OSS
* 使用修改core-site.xml的方式配置 JindoSDK 访问 OSS

````
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
        <!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>

````       
* 修改 Spark 启动命令方式灵活配置 JindoSDK 访问 OSS(示例)
 ```  
spark-submit --conf spark.hadoop.fs.AbstractFileSystem.oss.impl=com.aliyun.emr.fs.oss.OSS --conf spark.hadoop.fs.oss.impl=com.aliyun.emr.fs.oss.JindoOssFileSystem --conf spark.hadoop.fs.jfs.cache.oss.accessKeyId=xxx  --conf spark.hadoop.fs.jfs.cache.oss.accessKeySecret=xxx --conf spark.hadoop.fs.jfs.cache.oss.endpoint=oss-cn-xxx.aliyuncs.com 
 ``` 

## 启动 Spark 即可访问 OSS。

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
import org.apache.spark.sql.SparkSession;

public class TestJindoSDK {

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .config("spark.hadoop.fs.AbstractFileSystem.oss.impl", "com.aliyun.emr.fs.oss.OSS")
                .config("spark.hadoop.fs.oss.impl", "com.aliyun.emr.fs.oss.JindoOssFileSystem")
                .config("spark.hadoop.fs.jfs.cache.oss.accessKeyId", "xxx")
                .config("spark.hadoop.fs.jfs.cache.oss.accessKeySecret", "xxx")
                .appName("TestJindoSDK")
                .getOrCreate();

        spark.read().parquet("oss://xxx").count();

        spark.stop();
    }
}
```
<br />
