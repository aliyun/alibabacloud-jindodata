# JindoSDK OSS Credential Provider 按 OSS bucket 配置使用说明

## 按照 bucket 配置 Credential Provider

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleAliyunCredentialsProvider,com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider,com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
        <description>配置com.aliyun.jindodata.oss.auth.AliyunCredentialsProvider的实现类，多个类时使用英文逗号（, ）隔开，按照先后顺序读取Credential直至读到有效的Credential。Provider详情请参见Credential Provider类型。</description>
    </property>
</configuration>
```
说明 XXX 为 OSS bucket 名称。

## Credential Provider 类型

### 1. TemporaryCredentialsProvider 适合使用有时效性的 AccessKey 和 SecurityToken 访问 OSS 的情况。
* 配置 Provider 类型：

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.TemporaryCredentialsProvider</value>
    </property>
</configuration>
```

* 配置 OSS AK：

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.accessKeyId</name>
        <value>OSS bucket的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.bucket.XXX.accessKeySecret</name>
        <value>OSS bucket的AccessKey Secret</value>
    </property>
    <property>
        <name>fs.oss.bucket.XXX.securityToken</name>
        <value>OSS bucket的SecurityToken（临时安全令牌)</value>
    </property>
</configuration>
```

### 2. SimpleCredentialsProvider 适合使用长期有效的 AccessKey 访问 OSS 的情况。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider</value>
    </property>
</configuration>
```

* 配置OSS AK：

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.accessKeyId</name>
        <value>OSS bucket的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.bucket.XXX.accessKeySecret</name>
        <value>OSS bucket的AccessKey Secret</value>
    </property>
</configuration>
```

### 3. EnvironmentVariableCredentialsProvider 在环境变量中获取 AccessKey。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider</value>
    </property>
</configuration>
```

* 配置OSS AK，需要在环境变量中配置以下参数：

| 参数                                    | 参数说明             |
| ------------------------------------------| ----------------- |
| OSS_ACCESS_KEY_ID                      | OSS bucket的AccessKey Id |
| OSS_ACCESS_KEY_SECRET                  | OSS bucket的AccessKey Secret |
| OSS_SECURITY_TOKEN                     | OSS bucket的SecurityToken（临时安全令牌）。说明 仅配置有时效Token时需要。|


### 4. CommonCredentialsProvider为通用配置。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
    </property>
</configuration>
```

* 配置OSS AK：

```xml
<configuration>
    <property>
        <name>jindo.common.accessKeyId</name>
        <value>OSS bucket 的 AccessKey Id</value>
    </property>
    <property>
        <name>jindo.common.accessKeySecret</name>
        <value>OSS bucket 的 AccessKey Secret</value>
    </property>
    <property>
        <name>jindo.common.securityToken</name>
        <value>OSS bucket 的 SecurityToken（临时安全令牌)。说明 仅配置有时效 Token 时需要。</value>
    </property>
</configuration>
```
