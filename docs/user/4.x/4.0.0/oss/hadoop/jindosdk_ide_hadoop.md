# Hadoop 使用 JindoSDK 在 IDE 开发调试

在maven中添加本地sdk jar包的依赖
```xml
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>2.8.5</version>
        </dependency>
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindosdk</artifactId>
            <version>4.0.0</version>
            <scope>system</scope>
            <systemPath>/Users/xx/xx/jindosdk-4.0.0.jar</systemPath>
        </dependency>
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-object-core</artifactId>
            <version>4.0.0</version>
            <scope>system</scope>
            <systemPath>/Users/xx/xx/jindo-object-core-4.0.0.jar</systemPath>
        </dependency>
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
    FileSystem fs = FileSystem.get(URI.create("oss://<bucket>/"), conf);
    FSDataInputStream in = fs.open(new Path("/uttest/file1"));
    in.read();
    in.close();
  }
}
```