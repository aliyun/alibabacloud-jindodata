# 使用Jindo DistCp SDK

# 快速入手

## 兼容性

目前仅支持[jindo-distcp-3.6.0.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/Jar/native/jindo-distcp-3.6.0.jar)版本

## 安装

您可以通过以下两种方式安装SDK。

* 方式一：在Maven项目中加入依赖项（推荐方式）

  以3.6.0版本为例，步骤如下：

```
        <dependency>
            <groupId>bigboot</groupId>
            <artifactId>jindo-distcp</artifactId>
            <version>3.6.0</version>
            <scope>system</scope>
            <systemPath>/your/path/to/jindo-distcp-3.6.0.jar</systemPath>
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

* 方式二：在Intellij IDEA项目中导入JAR包

  以3.6.0版本为例，步骤如下：

  1. 下载[jindo-distcp-3.6.0.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/Jar/native/jindo-distcp-3.6.0.jar)。
  2. 将jindo-distcp-3.6.0.jar文件拷贝到您的项目中。
  3. 在Intellij IDEA中选择您的工程，右键选择***\*File\** > \**Project Structure\** > \**Modules\** > \**Dependencies\** > \**+\** > \**JARs or directories\**** 。
  4. 选中拷贝的所有JAR文件，导入到External Libraries中。

## 示例代码

```java
import com.aliyun.emr.jindo.api.distcp.DistCpService;
import com.aliyun.emr.jindo.api.distcp.DistCpRequest;
import com.aliyun.emr.jindo.api.distcp.DistCpResponse;
import com.aliyun.emr.jindo.distcp.JindoDistCp;
import org.apache.log4j.Level;

public class Main {
    static public void main(String[] args) {
        // Setting log4j
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.setLevel(Level.WARN);
        org.apache.log4j.Logger.getLogger("org.apache.hadoop.mapreduce").setLevel(Level.ERROR);

        // Jindo DistCp Service
        JindoDistCp service = new JindoDistCp();
        
        DistCpRequest request = new DistCpRequest();
        request.setRequestId("requestId001");
        request.setSrc("/user/root/random-data");
        request.setDest("oss://yang-hhht/tmp");

        DistCpResponse response = service.distCp(request);
        printResponse(response);
    }
    
    static public void printResponse(DistCpResponse response) {
        System.out.println("ErrorCode: " + response.getErrorCode());
        System.out.println("ErrorMsg: " + response.getErrorMsg());
        System.out.println("Id: " + response.getRequestId());
        System.out.println("Src: " + response.getSrc());
        System.out.println("Dest: " + response.getDest());
        System.out.println("Counter: ");
        for(String key : response.getCounters().keySet()){
            Long value = response.getCounters().get(key);
            System.out.println(key + " : " + value);
        }
        System.out.println("SuccessList: ");
        for(String path : response.getSuccessPathList()) {
            System.out.println(path);
        }
        System.out.println("FailedList: ");
        for(String key : response.getFailedPathList().keySet()){
            String value = response.getFailedPathList().get(key);
            System.out.println(key + " : " + value);
        }
    }
}
```

输出：

```
ErrorCode: 0
ErrorMsg: success
Id: requestId001
Src: hdfs://localhost:9000/user/root/random-data
Dest: oss://yang-hhht/tmp
Counter:
FILES_COPIED : 11
BYTES_COPIED : 10000
FILES_EXPECTED : 11
BYTES_EXPECTED : 10000
SuccessList:
hdfs://localhost:9000/user/root/random-data/part-m-00003
hdfs://localhost:9000/user/root/random-data/part-m-00002
hdfs://localhost:9000/user/root/random-data/part-m-00005
hdfs://localhost:9000/user/root/random-data/part-m-00004
hdfs://localhost:9000/user/root/random-data/part-m-00001
hdfs://localhost:9000/user/root/random-data/part-m-00000
hdfs://localhost:9000/user/root/random-data/_SUCCESS
hdfs://localhost:9000/user/root/random-data/part-m-00007
hdfs://localhost:9000/user/root/random-data/part-m-00006
hdfs://localhost:9000/user/root/random-data/part-m-00009
hdfs://localhost:9000/user/root/random-data/part-m-00008
FailedList:
```

# 开发指南

## 接口描述

```
public interface DistCpService {
    DistCpResponse distCp(DistCpRequest request);
}
```

**所有已知实现类: ** com.aliyun.emr.jindo.distcp.JindoDistCp

## 请求参数

相关方法的具体描述可参考[《使用Jindo DistCp》](jindo_distcp_how_to.md)

| 方法                                                         | 参数类型 | 说明                                                         | 默认值                 |
| ------------------------------------------------------------ | -------- | ------------------------------------------------------------ | ---------------------- |
| void setRequestId(String requestId)                          | 必选     | 设置请求ID                                                   | -                      |
| void setSrc(String src)                                      | 必选     | 设置源目录，支持的前缀有<br />file://<br/>hdfs://<br/>oss://<br/>jfs://<br/>s3:// | -                      |
| void setDest(String dest)                                    | 必选     | 设置目标目录，支持的前缀有<br/>file://<br/>hdfs://<br/>oss://<br/>jfs://<br/>s3:// | -                      |
| void setMode(DistCpMode mode)                                | 可选     | 设置DistCp模式，支持一下模式<br>COPY（拷贝）<br/>DIFF（差异比较）<br/>UPADTE（增量拷贝） | DistCpMode.COPY        |
| void setCopyFromManifest(boolean copyFromManifest)           | 可选     | 设置是否开启从manifest文件中获取源文件列表                   | false                  |
| void setPreviousManifest(String previousManifest)            | 可选     | 设置manifest文件目录，以获取源文件列表                       | null                   |
| void setRequirePreviousManifest(boolean requirePreviousManifest) | 可选     | 与setOutputManifest配合使用用生成manifest文件                | false                  |
| void setOutputManifest(String outputManifest)                | 可选     | 设置输出的manifest文件路径，用于记录结果文件的清单           | null                   |
| void setSrcPattern(String srcPattern)                        | 可选     | 设置一个正则表达式，来选择需要进行拷贝的文件                 | null                   |
| void setSrcPrefixesFile(String srcPrefixesFile)              | 可选     | 设置一个带有前缀列表的文件路径，用于拷贝多个文件夹           | null                   |
| void setFilters(String filters)                              | 可选     | 设置一个带有正则表达式列表的文件路径，用于过滤符合正则的文件 | null                   |
| void setStorageClass(String storageClass)                    | 可选     | 设置目标存储策略，支持Standard、IA、Archive、ColdArchive     | Standard               |
| void setDisableChecksum(boolean disableChecksum)             | 可选     | 设置是否关闭Checksum检查                                     | false                  |
| void setOutputCodec(String outputCodec)                      | 可选     | 设置压缩类型，支持编解码器有 gzip、gz、lzo、lzop、lzop、snappy | keep（不更改压缩类型） |
| void setDeleteOnSuccess(boolean deleteOnSuccess)             | 可选     | 设置是否删除源文件，用于移动数据                             | false                  |
| void setCleanUpPending(boolean cleanUpPending)               | 可选     | 设置是否清理残留文件                                         | false                  |
| void setEnableDynamicPlan(boolean enableDynamicPlan)         | 可选     | 设置是否开启作业动态分配计划，以优化存在大量小文件的场景     | false                  |
| void setEnableBalancePlan(boolean enableBalancePlan)         | 可选     | 设置是否开启作业平衡分配计划，用数据整体大小较均衡的场景     | false                  |
| void setEnableTransaction(boolean enableTransaction)         | 可选     | 设置是否开启事务，以保证Job级别的原子性                      | false                  |
| void setQueue(String queue)                                  | 可选     | 设置DistCp任务所在的yarn队列名称                             | null                   |
| void setAppendToLastFile(boolean appendToLastFile)           | 可选     | 设置是否开启合并文件                                         | false                  |
| void setTargetSize(Integer targetSize)                       | 可选     | 设置目标文件合并后的大小限制，单位M                          | null                   |
| void setSizeLimit(Long sizeLimit)                            | 可选     | 设置文件大小限制，单位M，将符合要求的小文件聚合为一个大文件  | 8L                     |
| void setLineLimit(Long lineLimit)                            | 可选     | 设置文件行数限制，将符合要求的小文件聚合为一个大文件         | 100000L                |
| void setGroupBy(String groupBy)                              | 可选     | 设置一个正则表达式，将符合要求的小文件聚合为一个大文件       | 10null                 |
| void setParallelism(int parallelism)                         | 可选     | 设置DistCp任务的并发度，对应MR任务中的mapreduce.job.reduces  | 10                     |
| void setBandWidth(int bandWidth)                             | 可选     | 设置单个节点的带宽限制，单位M                                | -1                     |
| void setOssEndpoint(String ossEndpoint)                      | 可选     | 设置OSS的Endpoint                                            | null                   |
| void setOssKey(String ossKey)                                | 可选     | 设置OSS的AccessKey ID                                        | null                   |
| void setOssSecret(String ossSecret)                          | 可选     | 设置OSS的AccessKey Secret                                    | null                   |
| void setS3Endpoint(String s3Endpoint)                        | 可选     | 设置S3的Endpoint                                             | null                   |
| void setS3Key(String s3Key)                                  | 可选     | 设置S3的Acesss Key ID                                        | null                   |
| void setS3Secret(String s3Secret)                            | 可选     | 设置S3的Secret Access Key                                    | null                   |
| void setEnableCMS(boolean enableCMS)                         | 可选     | 是否开启监控告警                                             | false                  |

## 响应参数

| 方法                                    | 参数类型 | 说明                       | 默认值 |
| --------------------------------------- | -------- | -------------------------- | ------ |
| String getRequestId()                   | 必选     | 获取请求ID                 | -      |
| int getErrorCode()                      | 必选     | 获取错误码                 | 0      |
| String getErrorMsg()                    | 必选     | 获取错误原因               | -      |
| String getSrc()                         | 必选     | 获取请求的源目录           | -      |
| String getDest()                        | 必选     | 获取请求的目标目录         | -      |
| long getJobStartTime()                  | 必选     | 获取任务的开始时间，单位秒 | -      |
| long getJobEndTime()                    | 必选     | 获取任务的结束时间，单位秒 | -      |
| Map<String, Long> getCounters(）        | 必选     | 获取计数器                 | -      |
| List<String> getSuccessPathList()       | 可选     | 获取成功文件列表           | null   |
| Map<String, String> getFailedPathList() | 可选     | 获取失败文件列表           | null   |

### 错误码说明

| 错误码 | 说明                     |
| ------ | ------------------------ |
| 0      | 成功                     |
| 30001  | 源目录非法               |
| 30002  | 目标目录非法             |
| 30003  | 源目录与目标目录相同     |
| 30004  | 目标目录是源目录的子目录 |
| 31000  | 源文件系统非法           |
| 31001  | TargetSize参数非法       |
| 31002  | Manifest文件非法         |
| 31003  | GroupBy参数非法          |
| 31004  | 源文件列表获取失败       |
| 32000  | 目标文件系统非法         |
| 33000  | 任务执行失败             |
| -1     | 未知错误                 |

### 计数器说明

| 任务计数器     | 说明                                        |
| -------------- | ------------------------------------------- |
| COPY_FAILED    | copy失败的文件数  |
| CHECKSUM_DIFF  | checksum校验失败的文件数，并计入COPY_FAILED |
| FILES_EXPECTED | 预期的copy文件数量   |
| BYTES_EXPECTED | 预期的copy字节数 |
| FILES_COPIED   | copy成功的文件数   |
| BYTES_COPIED | copy成功的字节数 |
| FILES_SKIPPED  | update增量更新时跳过的文件数  |
| BYTES_SKIPPED  | update增量更新时跳过的字节数  |
| DIFF_FILES | 不相同的文件数 |
| SAME_FILES | 经校验完全相同的文件数 |
| DST_MISS | 目标路径不存在的文件数，并计入DIFF_FILES |
| LENGTH_DIFF | 源文件和目标文件大小不一致的数量，并计入DIFF_FILES |
| CHECKSUM_DIFF | checksum校验失败的文件数，并计入DIFF_FILES |
| DIFF_FAILED | diff操作异常的文件数，具体报错参见job日志 |







