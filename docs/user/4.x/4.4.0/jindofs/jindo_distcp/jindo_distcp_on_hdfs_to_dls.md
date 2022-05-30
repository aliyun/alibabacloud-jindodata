# 数据从 HDFS 迁移到 OSS-HDFS 服务上

### 使用前须知
* 请参考 [JindoDistCp 介绍](jindo_distcp_overview.md) 文章内容进行环境适配和工具包下载
* 如您在使用过程中遇到问题可参考 [JindoDistCp 问题排查指南](jindo_distcp_QA.md) 进行解决，也可 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈

### 1、拷贝数据到阿里云 OSS-HDFS 服务（JindoFS 服务）上
您可以使用如下命令将 HDFS 上的目录拷贝到 OSS-HDFS 服务上
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --parallelism 10
```

| 参数 | 描述 | 示例 |
| --- | --- | --- |
| --src | HDFS 的源路径。| /data |
| --dest | OSS-HDFS 服务的目标路径。| oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/ |
| --hadoopConf | 指定 OSS-HDFS 服务的 `Access Key ID`,`Access Key Secret` |  *  配置 OSS-HDFS 服务的 AccessKeyId:</br>  --hadoopConf fs.oss.accessKeyId=yourkey</br>  * 配置 OSS-HDFS 服务 的 AccessKeySecret:</br>  --hadoopConf fs.oss.accessKeySecret=yoursecret |
| --parallelism | 任务并发大小，根据集群资源可调整。| 10 |

### 2、增量拷贝文件
如果 Distcp 任务因为各种原因中间失败了，而此时您想进行断点续传，只Copy剩下未Copy成功的文件。或者源端文件新增了部分文件，此时需要您在进行上一次 Distcp 任务完成后进行如下操作：
##### 使用 --update 命令，获得增量的文件列表
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --update --parallelism 20
```
如果所有文件都传输完成，则会提示如下信息。

```
INFO distcp.JindoDistCp: JindoDistCp job exit with 0.
```
### 3、YARN 队列及带宽选择
如您需要对 DistCp 作业使用的 YARN 队列和带宽进行限定，可用如下命令
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf mapreduce.job.queuename=yarnQueue --bandwidth 100 --parallelism 10
```
* --hadoopConf mapreduce.job.queuename=yarnQueue：指定 YARN 队列的名称
* --bandwidth：指定单机限流带宽的大小，单位 MB

### 4、免密及密钥固定存储
通常您需要将 OSS-HDFS 服务 AccessKey/AccessSecret 信息写在参数里，但是JindoDistCp可以将 其预先写在 Hadoop 的`core-site.xml`文件里 ，以避免使用时多次填写的问题。
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
</configuration>
```

### 5、配置 OSS-HDFS 服务 Endpoint

访问 OSS Bucket 上 OSS-HDFS 服务需要配置 Endpoint（`cn-xxx.oss-dls.aliyuncs.com`），与 OSS 对象接口的 Endpoint（`oss-cn-xxx-internal.aliyuncs.com`）不同。JindoSDK 会根据配置的 Endpoint 访问 OSS-HDFS 服务 或 OSS 对象接口。

使用 OSS-HDFS 服务时，推荐访问路径格式为：`oss://<Bucket>.<Endpoint>/<Object>`

如: `oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test`。

这种方式在访问路径中包含 OSS-HDFS 服务的 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应的 JindoFS 接口。 JindoSDK 还支持更多的 Endpoint 配置方式，详情参考 [OSS-HDFS 服务 Endpoint 配置](/docs/user/4.x/4.4.0/jindofs/configuration/jindosdk_endpoint_configuration.md)。

### 6、保存元数据信息

使用preserveMeta参数, 使得迁移数据的同时迁移包括 Owner, Group, Permission, Atime, Mtime, Replication, BlockSize, XAttrs, ACL 在内的元数据信息。

```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /opt/tmp --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --preserveMeta
```

### 7、其他功能
如您需要其他使用其他功能，请参考
* [JindoDistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md)