# 数据从 S3 迁移到 OSS

### 使用前须知
* 请参考 [JindoDistCp 介绍](jindo_distcp_overview.md) 文章内容进行环境适配和工具包下载
* 如您在使用过程中遇到问题可参考 [JindoDistCp 问题排查指南](jindo_distcp_QA.md) 进行解决，也可 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈

### 1、拷贝数据到 OSS 上
您可以使用如下命令将 S3 上的目录拷贝到 OSS 上。

* 通常情况下，您可使用如下命令执行 S3 到 OSS 的同步

```shell
hadoop jar jindo-distcp-tool-${version}.jar --src s3://srcS3Bucket/ --dest oss://destOssBucket/ \
    --hadoopConf fs.oss.accessKeyId=yourOsskey --hadoopConf fs.oss.accessKeySecret=yourOssSecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com \
    --hadoopConf fs.s3.accessKeyId=yourkey --hadoopConf fs.s3.accessKeySecret=yoursecret --hadoopConf fs.s3.endpoint=s3.xxx.amazonaws.com \
    --parallelism 10
```

| 参数 | 描述 | 示例 |
| --- | --- | --- |
| --src | S3 的源路径。| s3://srcS3Bucket/ |
| --dest | OSS 的目标路径。| oss://destOssBucket/ |
| --hadoopConf | 指定 OSS 和 S3 `Access Key ID`,`Access Key Secret`,`Endpoint`|  *  配置 OSS 的 AccessKeyId:</br>  --hadoopConf fs.oss.accessKeyId=yourkey</br>  * 配置 OSS 的 AccessKeySecret:</br>  --hadoopConf fs.oss.accessKeySecret=yoursecret</br>  * 配置 OSS 的 Endpoint 信息:</br> --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com </br> 配置 S3 的 AccessKeyId:</br>  --hadoopConf fs.s3.accessKeyId=yourkey</br>  * 配置 S3 的 AccessKeySecret:</br>  --hadoopConf fs.s3.accessKeySecret=yoursecret</br>  * 配置 S3 的 Endpoint 信息:</br>  --hadoopConf fs.s3.endpoint=s3.xxx.amazonaws.com |
| --parallelism | 任务并发大小，根据集群资源可调整。| 10 |

### 2、增量拷贝文件
如果 Distcp 任务因为各种原因中间失败了，而此时您想进行断点续传，只Copy剩下未Copy成功的文件。或者源端文件新增了部分文件，此时需要您在进行上一次 Distcp 任务完成后进行如下操作：
##### 使用 --update 命令，获得增量的文件列表
```shell
hadoop jar jindo-distcp-tool-${version}.jar  --src s3://srcS3Bucket/ --dest oss://destOssBucket/ \
    --hadoopConf fs.oss.accessKeyId=yourOsskey --hadoopConf fs.oss.accessKeySecret=yourOssSecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com \
    --hadoopConf fs.s3.accessKeyId=yourkey --hadoopConf fs.s3.accessKeySecret=yoursecret --hadoopConf fs.s3.endpoint=s3.xxx.amazonaws.com \
    --update --parallelism 20
```

### 2、YARN 队列及带宽选择
如您需要对 DistCp 作业使用的 YARN 队列和带宽进行限定，可用如下命令
```shell
hadoop jar jindo-distcp-tool-${version}.jar  --src s3://srcS3Bucket/ --dest oss://destOssBucket/ \
    --hadoopConf fs.oss.accessKeyId=yourOsskey --hadoopConf fs.oss.accessKeySecret=yourOssSecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com \
    --hadoopConf fs.s3.accessKeyId=yourkey --hadoopConf fs.s3.accessKeySecret=yoursecret --hadoopConf fs.s3.endpoint=s3.xxx.amazonaws.com \
    --hadoopConf mapreduce.job.queuename=yarnQueue --bandWidth 100 --parallelism 10
```
* --hadoopConf mapreduce.job.queuename=yarnQueue：指定 YARN 队列的名称
* --bandWidth：指定单机限流带宽的大小，单位 MB

### 3、免密及密钥固定存储
通常您需要将 OSS AccessKey/AccessSecret/EndPoint 信息写在参数里，但是JindoDistCp可以将 其预先写在 Hadoop 的`core-site.xml`文件里 ，以避免使用时多次填写的问题。
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.oss.endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>


    <property>
        <name>fs.s3.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.s3.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.s3.endpoint</name>
        <value>s3.xxx.amazonaws.com</value>
    </property>
</configuration>
```

### 4、其他功能
如您需要其他使用其他功能，请参考
* [JindoDistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md)
