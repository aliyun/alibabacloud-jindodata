# Deploying JindoSDK in a Hadoop Environment

## Installing JindoSDK

1. Download and extract the JindoSDK TAR archive.

Run the following command to download the v6.5.1 JindoSDK TAR package for most Linux x86 systems. For other platform installations, refer to [Deploying JindoSDK on Multi-platform Environments](jindosdk_deployment_multi_platform.md):
```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.5.1/jindosdk-6.5.1-linux.tar.gz
```

2. Unpack the JindoSDK TAR archive.
```bash
tar zxvf jindosdk-6.5.1-linux.tar.gz
```

3. Set environment variables.

Assuming the extracted folder is `/usr/lib/jindosdk-6.5.1-linux`:

```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-6.5.1-linux
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```
**Important**: Ensure that these installation paths and environment variables are set on all relevant nodes.

## Configuring Hadoop

1. Configure the OSS/OSS-HDFS implementation class and AccessKey.

Edit Hadoop's `core-site.xml` configuration file:
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

When accessing an OSS/OSS-HDFS Bucket, you'll need to configure the Endpoint. The recommended path format is `oss://<Bucket>.<Endpoint>/<Object>`, e.g., `oss://examplebucket.cn-hangzhou.oss-dls.aliyuncs.com/exampleobject.txt`. After setting this up, JindoSDK will use the Endpoint in the access path to reach the corresponding OSS/OSS-HDFS service interface.

Alternatively, you can configure a default Endpoint to simplify the path format to `oss://<Bucket>/<Object>`, e.g., `oss://examplebucket/exampleobject.txt`:
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