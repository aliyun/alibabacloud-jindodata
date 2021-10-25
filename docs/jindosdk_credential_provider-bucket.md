# JindoSDK Credential Provider 按 OSS bucket 配置使用说明

## 按照 bucket 配置 Credential Provider

```xml
<configuration>
    <property>
        <name>fs.dls.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.auth.SimpleAliyunCredentialsProvider,com.aliyun.jindodata.auth.EnvironmentVariableCredentialsProvider,com.aliyun.jindodata.auth.JindoCommonCredentialsProvider,com.aliyun.jindodata.auth.EcsStsCredentialsProvider</value>
        <description>配置com.aliyun.jindodata.auth.AliyunCredentialsProvider的实现类，多个类时使用英文逗号（, ）隔开，按照先后顺序读取Credential直至读到有效的Credential。Provider详情请参见Credential Provider类型。</description>
    </property>
</configuration>
```
说明 XXX为OSS bucket名称。

## Credential Provider 类型

### 1. TemporaryAliyunCredentialsProvider适合使用有时效性的AccessKey和SecurityToken访问OSS的情况。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.dls.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.auth.TemporaryAliyunCredentialsProvider</value>
    </property>
</configuration>
```

* 配置OSS AK：

```xml
<configuration>
    <property>
        <name>fs.dls.bucket.XXX.accessKeyId</name>
        <value>OSS bucket的AccessKey Id</value>
    </property>
    <property>
        <name>fs.dls.bucket.XXX.accessKeySecret</name>
        <value>OSS bucket的AccessKey Secret</value>
    </property>
    <property>
        <name>fs.dls.bucket.XXX.securityToken</name>
        <value>OSS bucket的SecurityToken（临时安全令牌)</value>
    </property>
</configuration>
```

### 2. SimpleAliyunCredentialsProvider适合使用长期有效的AccessKey访问OSS的情况。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.dls.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.auth.SimpleAliyunCredentialsProvider</value>
    </property>
</configuration>
```

* 配置OSS AK：

```xml
<configuration>
    <property>
        <name>fs.dls.bucket.XXX.accessKeyId</name>
        <value>OSS bucket的AccessKey Id</value>
    </property>
    <property>
        <name>fs.dls.bucket.XXX.accessKeySecret</name>
        <value>OSS bucket的AccessKey Secret</value>
    </property>
</configuration>
```

### 3. EnvironmentVariableCredentialsProvider在环境变量中获取AK。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.dls.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.auth.EnvironmentVariableCredentialsProvider</value>
    </property>
</configuration>
```

* 配置OSS AK，需要在环境变量中配置以下参数：

| 参数                                    | 参数说明             |
| ------------------------------------------| ----------------- |
| ALIYUN_ACCESS_KEY_ID                      | OSS bucket的AccessKey Id |
| ALIYUN_ACCESS_KEY_SECRET                  | OSS bucket的AccessKey Secret |
| ALIYUN_SECURITY_TOKEN                     | OSS bucket的SecurityToken（临时安全令牌）。说明 仅配置有时效Token时需要。|


### 4. JindoCommonCredentialsProvider为通用配置。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.dls.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.auth.JindoCommonCredentialsProvider</value>
    </property>
</configuration>
```

* 配置OSS AK：

```xml
<configuration>
    <property>
        <name>jindo.common.accessKeyId</name>
        <value>OSS bucket的AccessKey Id</value>
    </property>
    <property>
        <name>jindo.common.accessKeySecret</name>
        <value>OSS bucket的AccessKey Secret</value>
    </property>
    <property>
        <name>jindo.common.securityToken</name>
        <value>OSS bucket的SecurityToken（临时安全令牌)。说明 仅配置有时效Token时需要。</value>
    </property>
</configuration>
```
