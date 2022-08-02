# 数据在 OSS 不同 bucket 之间迁移

### 使用前须知
* 请参考 [JindoDistCp 介绍](jindo_distcp_overview.md) 文章内容进行环境适配和工具包下载
* 如您在使用过程中遇到问题可参考 [JindoDistCp 问题排查指南](jindo_distcp_QA.md) 进行解决，也可 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈

### 1、拷贝数据到 OSS 上
您可以使用如下命令将 OSS 上的目录拷贝到 OSS 上。

* 如果您的两个要拷贝的 bucket 都在同一个 region 下，且可用同一个 ossKey 和 ossSecret 进行读写，您可使用如下命令

```shell
hadoop jar jindo-distcp-${version}.jar --src oss://srcBucket/ --dest oss://destBucket/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com --parallelism 10
```

| 参数 | 描述 | 示例 |
| --- | --- | --- |
| --src | OSS 的源路径。| oss://srcBucket/ |
| --dest | OSS 的目标路径。| oss://destBucket/ |
| --hadoopConf | 指定 OSS `Access Key ID`,`Access Key Secret`,`Endpoint`|  *  配置 OSS 的 AccessKeyId:</br>  --hadoopConf fs.oss.accessKeyId=yourkey</br>  * 配置 OSS 的 AccessKeySecret:</br>  --hadoopConf fs.oss.accessKeySecret=yoursecret</br>  * 配置 OSS 的 Endpoint 信息:</br>  fs.oss.endpoint=oss-cn-xxx.aliyuncs.com |
| --parallelism | 任务并发大小，根据集群资源可调整。| 10 |

* 如果您的 src 和 dest 的 bucket 不在同一个 region 或者不能使用同一个 ossKey 和 ossSecret 进行读写，那么您可以使用如下命令

```shell
hadoop jar jindo-distcp-${version}.jar --src oss://srcbucket.oss-cn-xxx.aliyuncs.com/ --dest oss://destBucket.oss-cn-xxx.aliyuncs.com/ --hadoopConf fs.oss.bucket.srcbucket.accessKeyId=yourkey --hadoopConf fs.oss.bucket.srcbucket.accessKeySecret=yoursecret --hadoopConf fs.oss.bucket.srcbucket.endpoint=oss-cn-xxx.aliyuncs.com --hadoopConf fs.oss.bucket.destBucket.accessKeyId=yourkey --hadoopConf fs.oss.bucket.destBucket.accessKeySecret=yoursecret --hadoopConf fs.oss.bucket.destBucket.endpoint=oss-cn-xxx.aliyuncs.com --parallelism 10
```
| 参数 | 描述 | 示例 |
| --- | --- | --- |
| --hadoopConf | 给 src 和 dest 的 bucket 指定不同的`Access Key ID`,`Access Key Secret`,`Endpoint`| * 配置 XXX bucket 的 AccessKeyId:</br>  --hadoopConf fs.oss.bucket.XXX.accessKeyId=yourkey</br>  * 配置 XXX bucket 的 的 AccessKeySecret:</br>  --hadoopConf fs.oss.bucket.XXX.accessKeySecret=yoursecret</br>  * 配置 XXX bucket 的 Endpoint 信息:</br>  fs.oss.bucket.XXX.endpoint=oss-cn-xxx.aliyuncs.com |

### 2、增量拷贝文件
如果 Distcp 任务因为各种原因中间失败了，而此时您想进行断点续传，只Copy剩下未Copy成功的文件。或者源端文件新增了部分文件，此时需要您在进行上一次 Distcp 任务完成后进行如下操作：
##### 使用 --update 命令，获得增量的文件列表
```shell
hadoop jar jindo-distcp-${version}.jar --src oss://srcBucket/ --dest oss://destBucket/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com --update --parallelism 20
```
如果所有文件都传输完成，则会提示如下信息。
```
INFO distcp.JindoDistCp: JindoDistCp job exit with 0.
```
### 3、文件冷备份
如您想对写入到 OSS 上的文件进行冷备，如转化成冷归档（coldArchive）、归档（archive）和低频（ia）文件，可利用 JindoDistCp 直接进行该流程

##### 写入冷归档文件

```shell
hadoop jar jindo-distcp-${version}.jar --src oss://srcBucket/ --dest oss://destBucket/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com --policy coldArchive --parallelism 10
```

* --policy coldArchive：表示写入到 OSS 文件以冷归档文件形式存放，冷归档目前只在部分region可用，具体参见[OSS存储类型介绍](https://help.aliyun.com/document_detail/51374.html?utm_content=g_1000230851&spm=5176.20966629.toubu.3.f2991ddcpxxvD1#title-o8q-tl3-j65)

##### 写入归档文件

```shell
hadoop jar jindo-distcp-${version}.jar --src oss://srcBucket/ --dest oss://destBucket/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com --policy archive --parallelism 10
```
* --policy archive：表示写入到 OSS 文件以归档文件形式存放
##### 写入低频文件
```shell
hadoop jar jindo-distcp-${version}.jar --src oss://srcBucket/ --dest oss://destBucket/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com --policy ia --parallelism 10
```
* --policy ia：表示写入到 OSS 文件以低频文件形式存放

### 4、YARN 队列及带宽选择
如您需要对 DistCp 作业使用的 YARN 队列和带宽进行限定，可用如下命令
```shell
hadoop jar jindo-distcp-${version}.jar --src oss://srcBucket/ --dest oss://destBucket/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com --queue yarnQueue --bandWidth 100 --parallelism 10
```
* --queue：指定 YARN 队列的名称
* --bandWidth：指定单机限流带宽的大小，单位 MB

### 5、免密及密钥固定存储
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
</configuration>
```
### 6、其他功能
如您需要其他使用其他功能，请参考
* [JindoDistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md)

