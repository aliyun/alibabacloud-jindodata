### 环境要求
* JDK 1.8及以上
* Hadoop 2.3+版本(2.3版本以下暂未测试，如果问题请开issue反馈)，请下载 [jindo-distcp-3.4.0.jar](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/Jar/native/jindo-distcp-3.4.0.jar)

* 如果您的环境出现环境glibc版本低等其他不兼容问题，请下载 [jindo-distcp-3.4.0-lite.jar](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/Jindo-distcp/Jar/lite/jindo-distcp-3.4.0-lite.jar)

### 1、拷贝数据到 OSS 上
您可以使用如下命令将 oss 上的目录拷贝到 OSS 上，如果您的两个要拷贝的 bucket 都在同一个 region 下，且可用同一个 ossKey 和 ossSecret 进行读写，您可使用如下命令
```
hadoop jar jindo-distcp-3.4.0.jar --src oss://srcBucket/ --dest oss://destBucket/ --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-xxx.aliyuncs.com --parallelism 10
```
* --src：oss 的源路径
* --dest：oss 的目标路径
* --ossKey：oss 的 AccessKey
* --ossSecret：oss 的 AccessSecret
* --ossEndPoint：oss 的 endpoint 信息，可以公网或者内网的 endpoint
* --parallelism：任务并发大小，根据集群资源可调整

如果您的 src 和 dest 的 bucket 不在同一个 region 或者不能使用同一个 ossKey 和 ossSecret 进行读写，那么您可以使用如下命令

```
hadoop jar jindo-distcp-3.4.0.jar --src oss://ossKey1@srcbucket.oss-cn-xxx.aliyuncs.com/ --dest oss://ossKey2@destBucket.oss-cn-xxx.aliyuncs.com/ --parallelism 10
```
* 其中需要将 --src 和 --dest 都写成 oss://osskey@bucket.endpoint/dir 的拼接形式

### 2、增量拷贝文件
如果 Distcp 任务因为各种原因中间失败了，而此时您想进行断点续传，只Copy剩下未Copy成功的文件。或者源端文件新增了部分文件，此时需要您在进行上一次 Distcp 任务完成后进行如下操作：
##### 使用 --diff 命令，获得增量的文件列表
```
hadoop jar jindo-distcp-3.4.0.jar --src oss://srcBucket/ --dest oss://destBucket/ --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-xxx.aliyuncs.com --diff
```
如果所有文件都传输完成，则会提示如下信息。
```
INFO distcp.JindoDistCp: distcp has been done completely.
```
##### 增量的文件列表会被写入到本地的 manifest 文件里，默认生成在当前提交任务的路径下，您可以使用如下命令进行剩余文件的Copy
```
hadoop jar jindo-distcp-3.4.0.jar --src oss://srcBucket/ --dest oss://destBucket/ --dest oss://destBucket/ --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-xxx.aliyuncs.com --previousManifest=file:///opt/manifest-2020-04-17.gz --copyFromManifest --parallelism 20
```
* --copyFromManifest：表示从文件本地文件列表中读取文件
* --previousManifest：需要拷贝的文件列表，通过 --diff 生成

### 3、文件冷备份
如您想对写入到 OSS 上的文件进行冷备，如转化成归档（archive）和低频（ia）文件，可利用 Jindo DistCp 直接进行该流程

##### 写入归档文件
```
hadoop jar jindo-distcp-3.4.0.jar --src oss://srcBucket/ --dest oss://destBucket/ --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-xxx.aliyuncs.com --policy archive --parallelism 10
```
* --policy archive：表示写入到 OSS 文件以归档文件形式存放
##### 写入低频文件
```
hadoop jar jindo-distcp-3.4.0.jar --src oss://srcBucket/ --dest oss://destBucket/ --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-xxx.aliyuncs.com --policy ia --parallelism 10
```
* --policy ia：表示写入到 OSS 文件以低频文件形式存放

### 4、YARN 队列及带宽选择
如您需要对 DistCp 作业使用的 YARN 队列和带宽进行限定，可用如下命令
```
hadoop jar jindo-distcp-3.4.0.jar --src oss://srcBucket/ --dest oss://destBucket/ --ossKey yourkey --ossSecret yoursecret --ossEndPoint oss-cn-xxx.aliyuncs.com --queue yarnQueue --bandwidth 100 --parallelism 10
```
* --queue：指定 YARN 队列的名称
* --bandwidth：指定限流带宽的大小，单位 MB

### 5、免密及密钥固定存储
通常您需要将 OSS AccessKey/AccessSecret/EndPoint 信息写在参数里，但是Jindo DistCp可以将 其预先写在 Hadoop 的core-site.xml文件里 ，以避免使用时多次填写的问题。
如果您需要保存 OSS 的相关信息，您需要将以下信息保存在core-site.xml中，可省去在命令行里填写 ossKey/ossSecret/ossEndPoint 等信息。
```
<configuration>
    <property>
        <name>fs.jfs.cache.oss-accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss-accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss-endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
### 6、其他功能
如您需要其他使用其他功能，请参考
* [Jindo DistCp 进行数据迁移的使用说明](docs/jindo_distcp_how_to.md)
* [Jindo DistCp 场景化使用指南](docs/jindo_distcp_scenario_guidance.md)


