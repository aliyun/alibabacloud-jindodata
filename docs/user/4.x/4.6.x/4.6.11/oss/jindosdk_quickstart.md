# JindoSDK + 阿里云 OSS 快速入门

JindoSDK 全面兼容 Hadoop FileSystem 接口，提供了更好的兼容性和易用性，又能兼具OSS的性能和成本优势。
相对于 Hadoop 社区 OSS 客户端实现，您仍然可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈）。
关于 JindoSDK 和 Hadoop 社区 OSS connector 的性能对比，请参考文档[JindoSDK和Hadoop-OSS-SDK性能对比测试](/docs/user/4.x/4.0.0/oss/hadoop/jindosdk_vs_hadoop_sdk.md)。

## 步骤

### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

### 2. 安装 jar 包
解压下载的安装包，将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.6.11 为例:
```
cp jindosdk-4.6.11/lib/jindo-core-4.6.11.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.6.11/lib/jindo-sdk-4.6.11.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

### 3. 配置 OSS 实现类及 Access Key

将 JindoSDK OSS 实现类配置到 Hadoop 的 `core-site.xml` 中。

```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.oss.OSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```
将 OSS bucket 对应 的 `Access Key ID`、`Access Key Secret`、`Endpoint` 等预先配置在 Hadoop 的`core-site.xml`中。
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
        <!-- 阿里云 ECS 环境下推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考 [JindoSDK OSS Credential Provider 配置](/docs/user/4.x/4.6.x/4.6.11/oss/security/jindosdk_credential_provider_oss.md)。

### 4. 使用 JindoSDK 访问 OSS
用 Hadoop Shell 访问 OSS，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> oss://<bucket>/
```

* ls 操作
```
hadoop fs -ls oss://<bucket>/
```

* mkdir 操作
```
hadoop fs -mkdir oss://<bucket>/<path>
```

* rm 操作
```
hadoop fs -rm oss://<bucket>/<path>
```

<img src="/docs/user/4.x/4.0.0/oss/pic/jindofs_sdk_cmd.png#pic_center" />

### 5. 清理回收站
Hadoop 通过将删除的文件或目录放入回收站来防止误删文件或文件夹。当使用 Hadoop Shell 删除 OSS 的文件或目录时，
若开启了回收站功能，会在 OSS 的 bucket 上存储被删除的文件或目录，回收站的目录为`oss://buckect-name/user/<username>/.Trash`。可以通过配置 OSS 所使用的回收站目录的生命周期来清理回收站。
关于如何设置 OSS 生命周期，请参考 [设置生命周期规则](https://help.aliyun.com/document_detail/31904.html)

若要跳过回收站，直接删除，则可以指定`-skipTrash`参数（慎用）。

```
hadoop fs -rm -skipTrash oss://<bucket>/<path>
```

### 6. 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](/docs/user/4.x/4.6.x/4.6.11/oss/configuration/jindosdk_configuration_list.md)

### 7. 权限管理

* 自建集群，请参考 [阿里云 OSS 使用 Ranger 的鉴权方案](/docs/user/4.x/4.6.x/4.6.11/jindofsx/permission/jindofsx_ranger.md)
* 阿里云 E-MapReduce 集群，请参考 [EMR 集群中阿里云 OSS 使用 Ranger 的鉴权方案](/docs/user/4.x/4.6.x/4.6.11/jindofsx/permission/jindofsx_ranger_emr.md)