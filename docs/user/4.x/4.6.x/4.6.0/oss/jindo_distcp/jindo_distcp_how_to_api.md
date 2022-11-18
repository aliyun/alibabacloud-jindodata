# JindoDistCp API 使用说明

## 下载 JindoSDK 包

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 安装依赖包

目前 JindoSDK/JindoDistCP 支持主流 Intel X86 的 Linux 和 Mac（不支持 Windows系统，Mac M1 系列也暂不支持）

以4.6.1版本为例，在 maven `pom.xml` 中添加 JindoDistCP 的依赖

```xml

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-distcp-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>4.6.2</jindodata.version>
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
        <!-- add jindo-distcp -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-distcp</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- add hadoop with provided scope, your runtime should install hadoop dependency. -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-mapreduce-client-core</artifactId>
            <version>${hadoop.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

## 示例代码

以拷贝 HDFS 文件夹到阿里云 OSS 为例。 

```java
import com.aliyun.jindodata.distcp.util.DistCpCounter;
import com.aliyun.jindodata.distjob.request.DistJobResponse;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;


public class DistcpExample {
  static public void main(String[] args) throws IOException {

    Configuration conf = new Configuration();
    // set accessKey, secret, endpoint and so on.
    DistCpRequest request = new DistCpRequest();
    request.setSrcDir("hdfs:///user/root/random-data");
    request.setDestDir("oss://oss-bucket/dest");
    request.setWorkTempDir("hdfs:///user/root/tmp");
    request.setConf(conf);

    // Jindo DistCp Service
    DistCpService service = new DistCpService();
    service.buildAllJobs(request);
    service.runAllJobs();

    DistJobResponse response = service.collectAllJobResult();
    printResponse(response);
  }

  static public void printResponse(DistJobResponse response) {
    System.out.println("ErrorCode: " + response.getErrorCode());
    System.out.println("ErrorMsg: " + response.getErrorMsg());
    System.out.println("FileCopied: " + response.getCount(DistCpCounter.FILES_COPIED.getName()));
    System.out.println("BytesCopied: " + response.getCount(DistCpCounter.BYTES_COPIED.getName()));
  }
}
```


## 常见问题

[maven 无法下载依赖](/docs/user/4.x/4.6.x/4.6.0/jindodata_QA.md)


