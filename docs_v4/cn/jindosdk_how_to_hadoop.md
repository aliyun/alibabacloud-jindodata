# Hadoop 使用 JindoSDK 访问 OSS

JindoSDK 是一个简单易用面向 Hadoop、Spark 生态的 OSS 客户端，为阿里云 OSS 提供高度优化的 Hadoop FileSystem 实现。

JindoSDK 4.0 版本新支持了开启 HDFS 服务的 OSS bucket，即 Data Lake Storage，以下简称 DLS。支持 DLS 的 JindoSDK 全面兼容 Hadoop FileSystem 接口，提供了更好的兼容性和易用性，又能兼具OSS的性能和成本优势。

当然您也可以使用 JindoSDK 仅仅作为 OSS 客户端，相对于 Hadoop 社区 OSS 客户端实现，您仍然可以获得更好的性能和阿里云 E-MapReduce 产品技术团队更专业的支持。

目前支持市面上大部分 Hadoop 版本，在 Hadoop 2.3 及以上的版本上验证通过（2.3 以前版本暂未测试，如有问题请 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈）<br />关于 JindoSDK 和 Hadoop 社区 OSS connector的性能对比，请参考文档 [JindoSDK和Hadoop-OSS-SDK性能对比测试](/docs_v4/cn/jindosdk_vs_hadoop_sdk.md)。<br />

## 步骤

### 1. 权限设置

#### 1.1. OSS 服务账号授权

首次使用 DLS 功能时，需要先在 [RAM 控制台](https://ram.console.aliyun.com/overview) 完成以下授权，以便 OSS 服务账号能够管理 Bucket 中的数据块。 

#### a. 新建名为AliyunOSSDlsDefaultRole的角色

在创建角色对话框里面选择可信实体类型为“阿里云服务”，点击下一步；
角色类型选择“普通服务角色”，角色名称固定为“AliyunOSSDlsDefaultRole”，备注可以为“DLS默认使用此角色来访问用户数据”，受信服务选择“对象存储”。

#### b. 新建名为AliyunOSSDlsRolePolicy的权限策略

该权限策略的内容为：
```json
{
  "Version": "1",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "oss:ListObjects",
      "Resource": [
        "acs:oss:*:*:*"
      ]
    },
    {
      "Effect": "Allow",
      "Action": "oss:*",
      "Resource": [
        "acs:oss:*:*:*/.dlsdata",
        "acs:oss:*:*:*/.dlsdata*"
      ]
    }
  ]
}

```

#### c. 对新建的角色AliyunOSSDlsDefaultRole进行授权

对刚创建的角色AliyunOSSDlsDefaultRole进行“精确授权”，权限类型为自定义策略，策略名称为：“AliyunOSSDlsRolePolicy”

#### 1.2. 客户端授权访问 DLS

注意：如您使用 子账号 或者 服务角色（如 [EMR 服务角色](https://help.aliyun.com/document_detail/28072.html)）的方式访问 DLS，客户端 AK 或者 服务角色 需要以下策略，设置方式可参考[《通过RAM对OSS进行权限管理》](https://help.aliyun.com/document_detail/58905.html)。

```json
{
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "oss:*",
            "Resource": [
                "acs:oss:*:*:*/.dlsdata",
                "acs:oss:*:*:*/.dlsdata*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "oss:GetBucketInfo",
                "oss:PostDataLakeStorageFileOperation"
            ],
            "Resource": "*"
        }
    ],
    "Version": "1"
}
```


### 2. 安装 jar 包
下载最新的 jar 包 jindosdk-x.x.x.jar ([下载页面](/docs_v4/cn/jindosdk_download.md))，将sdk包安装到 hadoop 的 classpath 下。
```bash
cp ./jindosdk-*.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/jindosdk-xxx.jar
```

### 3. 配置 DLS 实现类及 Access Key

将 JindoSDK DLS 实现类配置到 Hadoop 的 core-site.xml 中。

```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.dls.DLS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.dls.JindoDlsFileSystem</value>
    </property>
</configuration>
```
将 DLS bucket 对应 的 Access Key、Access Key Secret、Endpoint 等预先配置在 Hadoop 的 core-site.xm l中。
```xml
<configuration>
    <property>
        <name>fs.dls.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.dls.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.dls.endpoint</name>
        <value>cn-xxx.oss-dls.aliyuncs.com</value>
    </property>
</configuration>
```
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考 [JindoSDK OSS AccessKey 配置](/docs_v4/cn/jindosdk_credential_provider.md)。<br />

### 3. （可选）配置 OSS Access Key

通过 JindoSDK 也可以访问普通的 OSS bucket，只需将对应的 Access Key、Access Key Secret、Endpoint 等预先配置在 Hadoop 的 core-site.xm l中。

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
      	<!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考 [JindoSDK OSS AccessKey 配置](/docs_v4/cn/jindosdk_credential_provider.md)。<br />

### 4. 使用 JindoSDK 访问 OSS
用 Hadoop Shell 访问 OSS，下面列举了几个常用的命令。

* put 操作
```bash
hadoop fs -put <path> oss://<bucket>/
```

* ls 操作
```bash
hadoop fs -ls oss://<bucket>/
```

* mkdir 操作
```bash
hadoop fs -mkdir oss://<bucket>/<path>
```

* rm 操作
```bash
hadoop fs rm oss://<bucket>/<path>
```

### 5. 参数调优
JindoSDK包含一些高级调优参数，配置方式以及配置项参考文档  [JindoSDK 配置项列表](/docs_v4/cn/jindosdk_configuration_list.md) 
