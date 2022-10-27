# JindoDistCp API 使用说明

## 下载 JindoSDK 包

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 安装依赖包

解压下载的安装包，maven 中添加本地`jindo-distcp-tool-${version}.jar`依赖。 以4.5.2版本为例，在项目的`pom.xml`文件中加入以下依赖：

```xml
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-distcp</artifactId>
            <version>${version}</version>
            <scope>system</scope>
            <systemPath>/your/path/to/jindo-distcp-tool-${version}.jar</systemPath>
        </dependency>
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





