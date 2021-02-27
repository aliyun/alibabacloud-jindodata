# Spark 使用 JindoFS SDK 在 IDE 开发调试 

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