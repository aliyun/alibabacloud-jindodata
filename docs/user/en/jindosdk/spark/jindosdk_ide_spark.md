# Spark Development with JindoSDK in IDEs

JindoSDK currently supports Linux (Intel X86) and macOS (excluding Windows). Here's how you can integrate JindoSDK into your Spark development environment using Maven.

First, update your `pom.xml` file with JindoSDK dependencies:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>

    <properties>
        <jindodata.version>6.3.3</jindodata.version>
        <hadoop.version>2.8.5</hadoop.version>
    </properties>

    <repositories>
        <!-- Add JindoData Maven Repository -->
        <repository>
            <id>jindodata</id>
            <url>https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- Add core Jindo dependency -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- Add extended Jindo core dependencies for different platforms if needed -->

        <!-- Add Spark-specific Jindo dependency -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-sdk</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- Add Hadoop dependency -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
        </dependency>
    </dependencies>
</project>
```

Now, create a Java program to use the SDK:

```java
import org.apache.spark.sql.SparkSession;

public class TestJindoSDK {
  public static void main(String[] args) throws Exception {
    SparkSession spark = SparkSession
        .builder()
        .config("spark.hadoop.fs.abstractFileSystem.oss.impl", "com.aliyun.jindodata.oss.OSS")
        .config("spark.hadoop.fs.oss.impl", "com.aliyun.jindodata.oss.JindoOssFileSystem")
        .config("spark.hadoop.fs.oss.accessKeyId", "xxx")
        .config("spark.hadoop.fs.oss.accessKeySecret", "xxx")
        // Add other configurations as needed
        .appName("TestJindoSDK")
        .getOrCreate();
    
    spark.read().parquet("oss://bucket.endpoint/xxx").count();
    spark.stop();
  }
}
```

For additional platform support, include the corresponding extended Jindo core dependencies in your `pom.xml`:

```xml
<dependencies>
    <!-- ... existing dependencies ... -->

    <!-- MacOS (Intel x86_64) -->
    <dependency>
        <groupId>com.aliyun.jindodata</groupId>
        <artifactId>jindo-core-macos-11_0-x86_64</artifactId>
        <version>${jindodata.version}</version>
    </dependency>

    <!-- MacOS (Apple Silicon/aarch64) -->
    <dependency>
        <groupId>com.aliyun.jindodata</groupId>
        <artifactId>jindo-core-macos-11_0-aarch64</artifactId>
        <version>${jindodata.version}</version>
    </dependency>

    <!-- CentOS 6 or EL6 -->
    <dependency>
        <groupId>com.aliyun.jindodata</groupId>
        <artifactId>jindo-core-linux-el6-x86_64</artifactId>
        <version>${jindodata.version}</version>
    </dependency>

    <!-- Ubuntu 22 -->
    <dependency>
        <groupId>com.aliyun.jindodata</groupId>
        <artifactId>jindo-core-linux-ubuntu22-x86_64</artifactId>
        <version>${jindodata.version}</version>
    </dependency>

    <!-- Alibaba Qiantu & AliOS (Beta) -->
    <dependency>
        <groupId>com.aliyun.jindodata</groupId>
        <artifactId>jindo-core-linux-el7-aarch64</artifactId>
        <version>${jindodata.version}</version>
    </dependency>
</dependencies>
```

After adding these dependencies, you should be able to develop and debug Spark applications using JindoSDK within your preferred IDE. Make sure your environment meets the supported platform requirements and has proper access to the configured OSS bucket.