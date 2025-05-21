# JindoDistCp API Usage Instructions

## Downloading the JindoSDK Package

Download the latest tar.gz package named `jindosdk-x.x.x.tar.gz` from the [download page](../jindosdk/jindosdk_download.md).

## Installing Required Dependencies

JindoSDK/JindoDistCP currently supports mainstream Intel X86-based Linux and Mac systems (Windows and Mac M1 series are not supported).

As an example for version 6.9.0, add JindoDistCP as a dependency in your Maven `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-distcp-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>6.9.0</jindodata.version>
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
        <!-- Add JindoDistCP -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-distcp</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- Add Hadoop with 'provided' scope; your runtime should provide the Hadoop dependency -->
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

## Sample Code

Here's an example demonstrating copying an HDFS directory to Alibaba Cloud OSS.

```java
import com.aliyun.jindodata.distcp.util.DistCpCounter;
import com.aliyun.jindodata.distjob.request.DistJobResponse;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

public class DistcpExample {
  public static void main(String[] args) throws IOException {

    Configuration conf = new Configuration();

    conf.set("fs.oss.impl", "com.aliyun.jindodata.oss.JindoOssFileSystem");
    conf.set("fs.AbstractFileSystem.oss.impl", "com.aliyun.jindodata.oss.OSS");
    // Set accessKey, secret, endpoint, etc.
    conf.set("fs.oss.credentials.provider", "com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider");
    conf.set("fs.oss.accessKeyId", "XXX");
    conf.set("fs.oss.accessKeySecret", "XXX");

    DistCpRequest request = new DistCpRequest();
    request.setSrcDir("hdfs:///user/root/random-data");
    request.setDestDir("oss://dls-bucket.cn-xxx.oss-dls.aliyuncs.com/dest");
    request.setWorkTempDir("hdfs:///user/root/tmp");
    request.setConf(conf);

    // Jindo DistCp Service
    DistCpService service = new DistCpService();
    service.buildAllJobs(request);
    service.runAllJobs();

    DistJobResponse response = service.collectAllJobResult();
    printResponse(response);
  }

  public static void printResponse(DistJobResponse response) {
    System.out.println("Error Code: " + response.getErrorCode());
    System.out.println("Error Message: " + response.getErrorMsg());
    System.out.println("Files Copied: " + response.getCount(DistCpCounter.FILES_COPIED.getName()));
    System.out.println("Bytes Copied: " + response.getCount(DistCpCounter.BYTES_COPIED.getName()));
  }
}
```

## Request Parameters

Refer to the detailed description in the [Using Jindo DistCp](jindodistcp_quickstart.md) guide for more information about the methods below:

| Method                                | Type   | Description                                                                                       | Default Value | Compatibility |
|---------------------------------------|--------|--------------------------------------------------------------------------------------------------|---------------|--------------|
| void setSrc(String src)               | Mandatory | Set the source directory; supports prefixes like hdfs://, oss://, s3://, cos://, obs://             | N/A           | 4.3.0+       |
| void setDest(String dest)             | Mandatory | Set the destination directory; supports prefixes like hdfs://, oss://, s3://, cos://, obs://         | N/A           | 4.3.0+       |
| void setConf(Configuration conf)      | Mandatory | Set the Configuration object                                                                      | N/A           | 4.3.0+       |
| void setMode(DistCpMode mode)         | Optional | Set DistCp mode; options are COPY (copy), DIFF (difference comparison), UPDATE (incremental copy) | DistCpMode.COPY | 4.3.0+       |
| void setBandWidthLimit(int bandWidthLimit) | Optional | Set bandwidth limit per node in MB                                                                | -1            | 4.3.0+       |
| void setOutputCodec(String outputCodec) | Optional | Set compression codec; options include gzip, gz, lzo, lzop, snappy                                    | keep (unchanged) | 4.3.0+       |
| void setStorageClass(String storageClass) | Optional | Set target storage class; options include Standard, IA, Archive, ColdArchive                        | Standard      | 4.3.0+       |
| void setIncludeMatcherFile(String includeMatcherFile) | Optional | Set a file containing inclusion rules for files                                                    | N/A           | 4.3.0+       |
| void setExcludeMatcherFile(String excludeMatcherFile) | Optional | Set a file containing exclusion rules for files                                                    | N/A           | 4.3.0+       |
| void setNumWorkers(int numWorkers)     | Optional | Set the number of DistCp tasks, equivalent to mapreduce.job.maps in MR jobs                         | 10            | 4.3.0+       |
| void setBatchPerJob(int batchPerJob)   | Optional | Set the number of files processed by each DistCp job                                               | 10            | 4.3.0+       |
| void setBatchPerTask(int batchPerTask) | Optional | Set the number of files processed by each DistCp task                                              | 10            | 4.3.0+       |
| void setWorkTempDir(String workTempDir) | Optional | Set the temporary directory                                                                       | /tmp          | 4.3.0+       |
| void setDisableChecksum(boolean disableChecksum) | Optional | Disable checksum checks if true                                                                  | false         | 4.3.0+       |
| void setDeleteOnSuccess(boolean deleteOnSuccess) | Optional | Delete source files upon successful transfer if true, used for moving data                          | false         | 4.3.0+       |
| void setEnableTransaction(boolean enableTransaction) | Optional | Enable transaction to ensure atomicity at the job level                                           | false         | 4.3.0+       |
| void setIgnoreFailures(boolean ignoreFailures) | Optional | Ignore exceptions during copy operations, preventing job interruption                                | False         | 4.3.0+       |
| void setEnableCMS(boolean enableCMS)   | Optional | Enable monitoring alerts                                                                         | false         | 4.3.0+       |
| void setEnablePreserveMetaStatus(boolean enablePreserveMetaStatus) | Optional | Preserve metadata status if true                                                                 | false         | 4.4.0+       |
| void setSyncSourceDeleteOnDest(boolean syncSourceDeleteOnDest)      | Optional       | Sets whether to synchronize deletion operations from the source subdirectories to the destination | false | 6.9.0+             |


## Response Parameters

| Method                              | Description                  | Default Value |
|-------------------------------------|------------------------------|---------------|
| int getErrorCode()                  | Retrieve the error code      | 0             |
| String getErrorMsg()                | Retrieve the error message   | N/A           |
| long getStartTime()                 | Get start time in seconds    | N/A           |
| long getEndTime()                   | Get end time in seconds      | N/A           |
| long getCount(String key)           | Get counter value by name    | N/A           |

### Counter Descriptions

| Job Counters                    | Description                                                  |
|--------------------------------|--------------------------------------------------------------|
| COPY_FAILED                     | Number of failed copies        |
| CHECKSUM_DIFF                   | Number of files with checksum differences, included in COPY_FAILED |
| FILES_EXPECTED                  | Expected number of files to copy |
| BYTES_EXPECTED                  | Expected number of bytes to copy |
| FILES_COPIED                    | Number of successfully copied files |
| BYTES_COPIED                    | Number of successfully copied bytes |
| FILES_SKIPPED                   | Number of skipped files in UPDATE incremental updates |
| BYTES_SKIPPED                   | Number of skipped bytes in UPDATE incremental updates |
| DIFF_FILES                      | Number of differing files |
| SAME_FILES                      | Number of identical files after verification |
| DST_MISS                        | Number of missing files at destination, included in DIFF_FILES |
| LENGTH_DIFF                     | Number of files with size differences, included in DIFF_FILES |
| CHECKSUM_DIFF                   | Number of files with checksum mismatches, included in DIFF_FILES |
| DIFF_FAILED                     | Number of files that failed during difference checking; see job logs for errors |