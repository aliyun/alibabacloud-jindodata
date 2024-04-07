# Configuring Credential Providers Per Bucket for OSS/OSS-HDFS

## Configuring a Credential Provider per Bucket

In the configuration XML snippet below, replace `XXX` with the name of your OSS/OSS-HDFS bucket:

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleAliyunCredentialsProvider,com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider,com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
        <description>Configure the implementation class of com.aliyun.jindodata.oss.auth.AliyunCredentialsProvider. Separate multiple classes with commas (,), and read credentials in the given order until a valid one is found. Refer to Credential Provider types for details.</description>
    </property>
</configuration>
```
Note that `XXX` represents the name of your specific OSS/OSS-HDFS bucket.

## Types of Credential Providers

## 1. TemporaryCredentialsProvider for Time-Limited AccessKey and SecurityToken
### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.TemporaryCredentialsProvider</value>
    </property>
</configuration>
```

### Configure OSS/OSS-HDFS Bucket AK:
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.accessKeyId</name>
        <value>OSS/OSS-HDFS bucket的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.bucket.XXX.accessKeySecret</name>
        <value>OSS/OSS-HDFS bucket的AccessKey Secret</value>
    </property>
    <property>
        <name>fs.oss.bucket.XXX.securityToken</name>
        <value>OSS/OSS-HDFS bucket的SecurityToken（临时安全令牌)</value>
    </property>
</configuration>
```

## 2. SimpleCredentialsProvider for Long-Term Valid AccessKey
### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider</value>
    </property>
</configuration>
```

### Configure OSS/OSS-HDFS Bucket AK:
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.accessKeyId</name>
        <value>OSS/OSS-HDFS bucket的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.bucket.XXX.accessKeySecret</name>
        <value>OSS/OSS-HDFS bucket的AccessKey Secret</value>
    </property>
</configuration>
```

## 3. EnvironmentVariableCredentialsProvider to Retrieve AccessKey from Environment Variables
### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider</value>
    </property>
</configuration>
```

### Configure OSS/OSS-HDFS Bucket AK in Environment Variables:

| Parameter                         | Description                       |
|----------------------------------|----------------------------------|
| OSS_ACCESS_KEY_ID                 | OSS/OSS-HDFS bucket的AccessKey Id      |
| OSS_ACCESS_KEY_SECRET             | OSS/OSS-HDFS bucket的AccessKey Secret   |
| OSS_SECURITY_TOKEN                | OSS/OSS-HDFS bucket的SecurityToken。Only needed when configuring a time-limited token.|


## 4. CommonCredentialsProvider as a General Configuration
### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
    </property>
</configuration>
```

### Configure OSS/OSS-HDFS Bucket AK:
```xml
<configuration>
    <property>
        <name>jindo.common.accessKeyId</name>
        <value>OSS/OSS-HDFS bucket AccessKey Id</value>
    </property>
    <property>
        <name>jindo.common.accessKeySecret</name>
        <value>OSS/OSS-HDFS bucket AccessKey Secret</value>
    </property>
    <property>
        <name>jindo.common.securityToken</name>
        <value>OSS/OSS-HDFS bucket的SecurityToken。Only needed when configuring a time-limited token.</value>
    </property>
</configuration>
```