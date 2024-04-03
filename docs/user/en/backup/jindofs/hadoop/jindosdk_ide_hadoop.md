Use JindoSDK for Hadoop development
JindoSDK can be used for only a Linux or macOS operating system that uses the Intel X86 architecture. Windows operating systems are not supported.
Add JindoSDK dependencies to the pom.xml file of the Maven project.
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 [http://maven.apache.org/xsd/maven-4.0.0.xsd">](http://maven.apache.org/xsd/maven-4.0.0.xsd%22%3E)
<modelVersion>4.0.0</modelVersion>
<groupId>com.aliyun.jindodata</groupId>
<artifactId>jindo-sdk-example</artifactId>
<version>1.0</version>
<properties>
<jindodata.version>4.6.12</jindodata.version>
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
Compile Java programs to use JindoSDK.
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
FileSystem fs = FileSystem.get(URI.create("oss://<Bucket>.<HDFS_Endpoint>/"), conf);
FSDataInputStream in = fs.open(new Path("/uttest/file1"));
in.read();
in.close();
}
}
Dependencies required for other platforms
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

