# Minimal Installation and Deployment of JindoSDK in a Hadoop Environment

## Deploying JindoSDK

1. Download the JindoSDK JAR packages.

Execute the following commands to download the v6.5.6 JindoSDK JAR packages for most Linux x86 systems. For other platform deployments, refer to [Deploying JindoSDK on Multi-platform Environments](jindosdk_deployment_multi_platform.md):
```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-sdk/6.5.6/jindo-sdk-6.5.6.jar
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-core/6.5.6/jindo-core-6.5.6.jar
```

2. Install the JindoSDK JAR packages.

Copy the downloaded JAR files into Hadoop's classpath, specifically the `lib` directory for HDFS:

```bash
cp jindo-core-6.5.6.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindo-sdk-6.5.6.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```
**Important**: Make sure to deploy these installation directories and configurations across all necessary nodes.

## Configuring Hadoop

1. Configure OSS/OSS-HDFS implementation class and AccessKey.

Open Hadoop's `core-site.xml` configuration file:
```bash
vim <HADOOP_HOME>/etc/hadoop/core-site.xml
```

2. Add the OSS/OSS-HDFS implementation classes to `core-site.xml`.
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

3. Preconfigure the OSS/OSS-HDFS AccessKey ID and AccessKey Secret in `core-site.xml`.
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

4. Specify the OSS/OSS-HDFS service Endpoint.

When accessing an OSS/OSS-HDFS Bucket, configure the Endpoint. The recommended path format is `oss://<Bucket>.<Endpoint>/<Object>`, e.g., `oss://examplebucket.cn-hangzhou.oss-dls.aliyuncs.com/exampleobject.txt`. Once configured, JindoSDK will use the Endpoint in the access path to reach the appropriate OSS/OSS-HDFS service interface.

Alternatively, you can set a default Endpoint to simplify the path format to `oss://<Bucket>/<Object>`, e.g., `oss://examplebucket/exampleobject.txt`:
```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>xxx</value>
    </property>
</configuration>
```

5. Additional configuration options:
- [Configuring the OSS/OSS-HDFS Credential Provider](./jindosdk_credential_provider.md)
- [Configuring the OSS/OSS-HDFS Credential Provider by Bucket](./jindosdk_credential_provider_bucket.md)
- [Common Issues with Accessing OSS/OSS-HDFS Using AK](./jindosdk_credential_provider_faq.md)