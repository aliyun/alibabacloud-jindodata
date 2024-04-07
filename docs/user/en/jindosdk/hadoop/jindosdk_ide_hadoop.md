# Developing and Debugging Hadoop with JindoSDK in IDE

JindoSDK currently supports mainstream Intel X86-based Linux and macOS environments, but not Windows.

To integrate JindoSDK into your project using Maven, follow these steps:

1. Update your `pom.xml` file to include the necessary dependencies:

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
        <!-- Add jindo-core -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- Add jindo-core extended jars for other platforms -->

        <!-- Add jindo-hadoop-sdk -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-sdk</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- Add hadoop dependency -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
        </dependency>
    </dependencies>
</project>
```

2. Now you can write Java code that uses JindoSDK. Here's a sample program:

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

public class TestJindoSDK {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.oss.impl", "com.aliyun.jindodata.oss.JindoOssFileSystem");
        conf.set("fs.AbstractFileSystem.oss.impl", "com.aliyun.jindodata.oss.OSS");
        // Set accessKey, secret, endpoint, etc.
        FileSystem fs = FileSystem.get(URI.create("oss://<bucket>/"), conf);
        FSDataInputStream in = fs.open(new Path("/uttest/file1"));
        in.read();
        in.close();
    }
}
```
Remember to replace `<bucket>` with your actual bucket name.

3. If you need support for other platforms, add their respective dependencies accordingly:

```xml
<!-- MacOS 11.0 (Intel x86_64) -->
<dependency>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-core-macos-11_0-x86_64</artifactId>
    <version>${jindodata.version}</version>
</dependency>

<!-- MacOS 11.0 (Apple M1/aarch64) -->
<dependency>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-core-macos-11_0-aarch64</artifactId>
    <version>${jindodata.version}</version>
</dependency>

<!-- CentOS 6/EL6 (Intel x86_64) -->
<dependency>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-core-linux-el6-x86_64</artifactId>
    <version>${jindodata.version}</version>
</dependency>

<!-- Ubuntu 22 (Intel x86_64) -->
<dependency>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-core-linux-ubuntu22-x86_64</artifactId>
    <version>${jindodata.version}</version>
</dependency>

<!-- Aliyun Yitian & Alibaba OS (aarch64/beta) -->
<dependency>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-core-linux-el7-aarch64</artifactId>
    <version>${jindodata.version}</version>
</dependency>
```

After adding these dependencies, build your project and run the `TestJindoSDK` class within your IDE to interact with OSS using JindoSDK. Make sure you have valid credentials and endpoint configurations for accessing your OSS bucket.