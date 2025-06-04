# 在 Hadoop 环境最简安装部署 JindoSDK

## 部署 JindoSDK

1.  下载 JindoSDK JAR 包。
    
执行以下命令，下载6.9.1版本JindoSDK JAR包。以大多数linux x86环境为例。其他平台部署参见[《在多平台环境安装部署 JindoSDK》](jindosdk_deployment_multi_platform.md)
        
```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-sdk/6.9.1/jindo-sdk-6.9.1.jar
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-core/6.9.1/jindo-core-6.9.1.jar
```

2.  安装 JindoSDK JAR 包
将下载的 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

以 jindosdk-6.9.1 为例:
```
cp jindo-core-6.9.1.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-6.9.1.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

**重要** 请将安装目录和环境变量部署到所有所需节点上。

## Hadoop 配置文件

1.  配置 OSS/OSS-HDFS 实现类及AccessKey。
    
执行以下命令，进入Hadoop的core-site.xml配置文件。
        
```bash
vim <HADOOP_HOME>/etc/hadoop/core-site.xml
```

2.  将 OSS/OSS-HDFS 实现类配置到Hadoop的core-site.xml中。
    
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```

3.  将 OSS/OSS-HDFS 的Bucket对应的accessKeyId、accessKeySecret预先配置在Hadoop的core-site.xml中。
    
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

4.  配置 OSS/OSS-HDFS 服务 Endpoint。
    
访问 OSS/OSS-HDFS Bucket时需要配置 Endpoint。推荐访问路径格式为 oss://`<Bucket>`.`<Endpoint>`/`<Object>`，例如 oss://examplebucket.cn-hangzhou.oss-dls.aliyuncs.com/exampleobject.txt。配置完成后，JindoSDK会根据访问路径中的 Endpoint 访问对应的 OSS/OSS-HDFS 服务接口。

此外，您也可以通过以下方式配置默认 Endpoint，以简化访问路径格式为 oss://`<Bucket>`/`<Object>`，例如 oss://examplebucket/exampleobject.txt
```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```

5.  更多配置方式。
[配置 OSS/OSS-HDFS Credential Provider](./jindosdk_credential_provider.md)
[按 bucket 配置 OSS/OSS-HDFS Credential Provider](./jindosdk_credential_provider_bucket.md)
[访问 OSS/OSS-HDFS 时 AK 相关常见问题](./jindosdk_credential_provider_faq.md)
