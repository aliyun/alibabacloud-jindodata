# JindoDistCp API 使用说明

## 下载 JindoSDK 包

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 安装依赖包

目前 JindoSDK/JindoDistCP 支持主流 Intel X86 的 Linux 和 Mac（不支持 Windows系统）

以4.6.7版本为例，在 maven `pom.xml` 中添加 JindoDistCP 的依赖

```xml

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-distcp-example</artifactId>
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

    conf.set("fs.oss.impl", "com.aliyun.jindodata.oss.JindoOssFileSystem");
    conf.set("fs.AbstractFileSystem.oss.impl", "com.aliyun.jindodata.oss.OSS");
    // set accessKey, secret, endpoint and so on.
    conf.set("fs.oss.credentials.provider", "com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider");
    conf.set("fs.oss.accessKeyId", "XXX");
    conf.set("fs.oss.accessKeySecret", "XXX");
    conf.set("fs.oss.endpoint", "oss-cn-xxx.aliyuncs.com");
    
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

## 请求参数

相关方法的具体描述可参考[《使用Jindo DistCp》](jindo_distcp_how_to.md)

| 方法                                                                 | 参数类型 | 说明            | 默认值     | 兼容     |
|--------------------------------------------------------------------| -------- |---------------|---------|--------|
| void setSrc(String src)                                            | 必选     | 设置源目录，支持的前缀有<br/>hdfs://<br/>oss://<br/>s3://<br/>cos://<br/>obs:// | -       | 4.3.0+ |
| void setDest(String dest)                                          | 必选     | 设置目标目录，支持的前缀有<br/>hdfs://<br/>oss://<br/>s3://<br/>cos://<br/>obs:// | -       | 4.3.0+ |
| void setConf(Configuration conf)                                   | 必选     | 设置 Configuration | -       | 4.3.0+ |
| void setMode(DistCpMode mode)                                      | 可选     | 设置DistCp模式，支持的模式有<br/>COPY（拷贝）<br/>DIFF（差异比较）<br/>UPADTE（增量拷贝） | DistCpMode.COPY | 4.3.0+ |
| void setNumWorkers(int numWorkers)                                 | 可选     | 设置DistCp任务的并发度，对应MR任务中的 mapreduce.job.maps | 10      | 4.3.0+ |
| void setBandWidthLimit(int bandWidthLimit)                         | 可选     | 设置单个节点的带宽限制，单位M | -1      | 4.3.0+ |
| void setWorkTempDir(String workTempDir)                            | 可选     | 设置临时目录        | /tmp    | 4.3.0+ |
| void setOutputCodec(String outputCodec)                            | 可选     | 设置压缩类型，支持编解码器有 gzip、gz、lzo、lzop、lzop、snappy | keep（不更改压缩类型） | 4.3.0+ |
| void setEnableCMS(boolean enableCMS)                               | 可选     | 是否开启监控告警      | false   | 4.3.0+ |
| void setDisableChecksum(boolean disableChecksum)                   | 可选     | 设置是否关闭Checksum检查 | false   | 4.3.0+ |
| void setDeleteOnSuccess(boolean deleteOnSuccess)                   | 可选     | 设置是否删除源文件，用于移动数据 | false   | 4.3.0+ |
| void setIncludeMatcherFile(String includeMatcherFile)              | 可选     | 设置包含符合规则的文件   | -       | 4.3.0+ |
| void setExcludeMatcherFile(String excludeMatcherFile)              | 可选     | 设置包含过滤规则的文件   | -       | 4.3.0+ |
| void setIgnoreFailures(boolean ignoreFailures)                     | 可选     | 设置是否忽略拷贝任务中抛出的异常，避免中断任务 | False   | 4.3.0+ |
| void setStorageClass(String storageClass)                          | 可选     | 设置目标存储策略，支持Standard、IA、Archive、ColdArchive | Standard | 4.3.0+ |
| void setEnableTransaction(boolean enableTransaction)               | 可选     | 设置是否开启事务，以保证Job级别的原子性 | false   | 4.3.0+ |
| void setEnablePreserveMetaStatus(boolean enablePreserveMetaStatus) | 可选     | 设置是否开启保存元数据信息 | false   | 4.4.0+ |

## 响应参数

| 方法                           | 说明    | 默认值 |
|------------------------------|-------|------ |
| int getErrorCode()           | 获取错误码 | 0     |
| String getErrorMsg()         | 获取错误原因 | -     |
| long getStartTime()          | 获取任务的开始时间，单位秒 | -     |
| long getEndTime()            | 获取任务的结束时间，单位秒 | -     |
| getCount(String key)         | 获取计数器值，key为计数器名称， | -     |

### 计数器说明

| 任务计数器          | 说明                                        |
|----------------| ------------------------------------------- |
| COPY_FAILED    | copy失败的文件数  |
| CHECKSUM_DIFF  | checksum校验失败的文件数，并计入COPY_FAILED |
| FILES_EXPECTED | 预期的copy文件数量   |
| BYTES_EXPECTED | 预期的copy字节数 |
| FILES_COPIED   | copy成功的文件数   |
| BYTES_COPIED   | copy成功的字节数 |
| FILES_SKIPPED  | update增量更新时跳过的文件数  |
| BYTES_SKIPPED  | update增量更新时跳过的字节数  |
| DIFF_FILES     | 不相同的文件数 |
| SAME_FILES     | 经校验完全相同的文件数 |
| DST_MISS       | 目标路径不存在的文件数，并计入DIFF_FILES |
| LENGTH_DIFF    | 源文件和目标文件大小不一致的数量，并计入DIFF_FILES |
| CHECKSUM_DIFF  | checksum校验失败的文件数，并计入DIFF_FILES |
| DIFF_FAILED    | diff操作异常的文件数，具体报错参见job日志 |






