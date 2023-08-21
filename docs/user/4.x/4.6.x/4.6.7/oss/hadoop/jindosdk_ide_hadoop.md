# Hadoop 使用 JindoSDK 在 IDE 开发调试

目前 JindoSDK 支持主流 Intel X86 的 Linux 和 Mac（不支持 Windows系统）

在 maven pom.xml 中添加 JindoSDK 的依赖


```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>4.6.7</jindodata.version>
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
        <!-- add jindo-core -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
              
        <!-- add jindo-core-extended-jar if you need support other platform -->
        
        <!-- add jindo-hadoop-sdk -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindosdk</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- add hadoop dependency. -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
        </dependency>
    </dependencies>
</project>
```

然后您可以编写Java程序使用SDK
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
    // set accessKey, secret, endpoint and so on.
    FileSystem fs = FileSystem.get(URI.create("oss://<bucket>/"), conf);
    FSDataInputStream in = fs.open(new Path("/uttest/file1"));
    in.read();
    in.close();
  }
}
```

其他平台依赖

```xml

        <!-- add jindo-core-extended-jar for centos6 or el6 -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-el6-x86_64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- add jindo-core-extended-jar for ubuntu22 -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-ubuntu22-x86_64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- add jindo-core-extended-jar for aliyun yitian & alios (beta)-->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-el7-aarch64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>


```

