# 在 Hadoop 环境中部署 JindoSDK

## 部署 JindoSDK

1.  下载并解压 JindoSDK TAR 包。
    
执行以下命令，下载6.8.3版本JindoSDK TAR包。以大多数linux x86环境为例。其他平台部署参见[《在多平台环境安装部署 JindoSDK》](jindosdk_deployment_multi_platform.md)
        
```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.8.3/jindosdk-6.8.3-linux.tar.gz
```

2.  执行以下命令，解压 JindoSDK TAR 包。
    
```bash
tar zxvf jindosdk-6.8.3-linux.tar.gz
```

3.  配置环境变量。
    
以安装包内容解压在 /usr/lib/jindosdk-6.8.3-linux 目录为例：

```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-6.8.3-linux
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
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
