# JindoSDK 快速入门

JindoSDK 全面兼容 Hadoop FileSystem 接口，提供了更好的兼容性和易用性，又能兼具OSS的性能和成本优势。
相对于 Hadoop 社区 OSS 客户端实现，您仍然可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) 向我们反馈）。

## 步骤

### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz，[下载页面](/docs/user/6.x/6.0.0/jindodata_download.md)。

在非 EMR 环境中完整安装部署 JindoSDK，参见 [文档链接](/docs/user/6.x/jindosdk/jindosdk_deployment.md)

在 Hadoop 环境最简安装部署 JindoSDK，参见 [文档链接](/docs/user/6.x/jindosdk/jindosdk_deployment_lite_hadoop.md)

在多平台环境安装部署 JindoSDK，参见 [文档链接](/docs/user/6.x/jindosdk/jindosdk_deployment_multi_platform.md)

### 2. 配置 OSS 实现类及 Access Key

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
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考 [JindoSDK OSS Credential Provider 配置](/docs/user/6.x/jindosdk/jindosdk_credential_provider.md)。

### 3. 使用 JindoSDK 访问 OSS
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

更多 Hadoop 命令参见 [《通过 Hadoop Shell 命令访问 OSS/OSS-HDFS》](/docs/user/6.x/oss/usages/oss_hadoop_shell.md)

### 5. 清理回收站
Hadoop 通过将删除的文件或目录放入回收站来防止误删文件或文件夹。当使用 Hadoop Shell 删除 OSS 的文件或目录时，
若开启了回收站功能，会在 OSS 的 bucket 上存储被删除的文件或目录，回收站的目录为`oss://buckect-name/user/<username>/.Trash`。可以通过配置 OSS 所使用的回收站目录的生命周期来清理回收站。
关于如何设置 OSS 生命周期，请参考 [设置生命周期规则](https://help.aliyun.com/document_detail/31904.html)

若要跳过回收站，直接删除，则可以指定`-skipTrash`参数（慎用）。

```
hadoop fs -rm -skipTrash oss://<bucket>/<path>
```

### 6. 参数调优
JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](/docs/user/6.x/jindosdk/jindosdk_configuration.md)