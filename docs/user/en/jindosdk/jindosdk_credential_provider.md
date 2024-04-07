# Configuring OSS/OSS-HDFS Credential Providers

## Basic Configuration Method

You can preconfigure the `Access Key ID`, `Access Secret`, and `Endpoint` for OSS/OSS-HDFS in Hadoop's `core-site.xml`. On an EMR cluster, navigate to the Hadoop-Common page, then select **Configuration** > **core-site.xml**.

Here are the configuration properties:

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

## Storing AccessKey Information with Hadoop Credential Providers

The above `fs.oss.accessKeyId` and `fs.oss.accessKeySecret` will be stored plaintext in the `core-site.xml`. Instead, you can store them securely using Hadoop Credential Providers.

Use the provided Hadoop command to store AccessKey and SecurityToken information in a Credential file. The command syntax is as follows:

```sh
hadoop credential <subcommand> [options]
```

For example, store AccessKey, KeySecret, and Token information in a JCEKS file. Besides protecting the file with file permissions, you can also encrypt the information using a password (if no password is specified, a default string will be used):

```sh
hadoop credential create fs.oss.accessKeyId -value AAA -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.accessKeySecret -value BBB -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.securityToken -value CCC -provider jceks://file/root/oss.jceks
```
After generating the Credential file, configure the following property to specify the provider type and location:

```xml
<configuration>
    <property>
        <name>fs.oss.security.credential.provider.path</name>
        <value>jceks://file/root/oss.jceks</value>
        <description>Configure the Credential file storing AK. E.g., jceks://file/${user.home}/oss.jceks refers to the oss.jceks file under HOME.</description>
    </property>
</configuration>
```

## Using JindoSDK OSS/OSS-HDFS Credential Provider

By default, it configures SimpleCredentialsProvider, EnvironmentVariableCredentialsProvider, and CommonCredentialsProvider. These providers read credentials until they find valid ones in order of precedence.

```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider,com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider,com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
        <description>Configure com.aliyun.jindodata.oss.auth.JindoCredentialsProvider. Multiple classes separated by commas (,) indicate the order of reading credentials until one is found valid. See details about Credential Provider types.</description>
    </property>
</configuration>
```
Choose different Credential Providers based on your needs. Supported providers include:

### 1. TemporaryCredentialsProvider suitable for scenarios where you use time-limited AccessKeys and SecurityTokens to access OSS/OSS-HDFS.
#### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.TemporaryCredentialsProvider</value>
    </property>
</configuration>
```
#### Configure OSS/OSS-HDFS AK:
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>OSS/OSS-HDFS AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>OSS/OSS-HDFS AccessKey Secret</value>
    </property>
    <property>
        <name>fs.oss.securityToken</name>
        <value>OSS/OSS-HDFS SecurityToken</value>
    </property>
</configuration>
```

### 2. SimpleCredentialsProvider suitable for scenarios where you use long-term valid AccessKeys to access OSS/OSS-HDFS.
#### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider</value>
    </property>
</configuration>
```
#### Configure OSS/OSS-HDFS AK:
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>OSS/OSS-HDFS 的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>OSS/OSS-HDFS 的AccessKey Secret</value>
    </property>
</configuration>
```

### 3. EnvironmentVariableCredentialsProvider retrieves AK from environment variables.
#### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider</value>
    </property>
</configuration>
```
#### Configure OSS/OSS-HDFS AK in environment variables:
| Parameter                         | Description                     |
|----------------------------------|--------------------------------|
| OSS_ACCESS_KEY_ID                 | OSS/OSS-HDFS AccessKey Id      |
| OSS_ACCESS_KEY_SECRET             | OSS/OSS-HDFS AccessKey Secret |
| OSS_SECURITY_TOKEN                | OSS/OSS-HDFS SecurityToken。Only required when configuring时效 Token.|


### 4. CommonCredentialsProvider is a general configuration option.
#### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
    </property>
</configuration>
```
#### Configure OSS/OSS-HDFS AK:
```xml
<configuration>
    <property>
        <name>jindo.common.accessKeyId</name>
        <value>OSS/OSS-HDFS AccessKey Id</value>
    </property>
    <property>
        <name>jindo.common.accessKeySecret</name>
        <value>OSS/OSS-HDFS AccessKey Secret</value>
    </property>
    <property>
        <name>jindo.common.securityToken</name>
        <value>OSS/OSS-HDFS SecurityToken</value>
    </property>
</configuration>
```

### 5. CustomCredentialsProvider for connecting to custom token services without passwords.
#### Configure Provider Type:
```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.CustomCredentialsProvider</value>
    </property>
</configuration>
```
#### Configure token service URL:
```xml
<configuration>
    <property>
        <name>aliyun.oss.provider.url</name>
        <value>token service url</value>
    </property>
</configuration>
```
`aliyun.oss.provider.url` supports HTTP(S) and secrets protocols:

- **HTTP(S)** protocol: The token service URL format is `http://localhost:1234/sts`. If you need to connect your own HTTP token service, design your service according to [Using Instance RAM Role through API](https://help.aliyun.com/document_detail/61178.html#title-t3j-zsg-1fh). The response should be JSON formatted like this:

  ```json
  {
      "AccessKeyId": "XXXXXXXXX",
      "AccessKeySecret": "XXXXXXXXX",
      "Expiration": "2020-11-01T05:20:01Z",
      "SecurityToken": "XXXXXXXXX",
      "LastUpdated": "2020-10-31T23:20:01Z",
      "Code": "Success"
  }
  ```

- **Secrets** protocol: The token service URL format is `secrets:///local_path_prefix`. This is commonly used in Kubernetes scenarios. For example, if `local_path_prefix` is `secrets:///secret/JindoOss`, look for these AccessKey files on the node:

  `/secret/JindoOssAccessKeyId`
  `/secret/JindoOssAccessKeySecret`
  `/secret/JindoOssSecurityToken`

If `local_path_prefix` is `secrets:///secret/JindoOss/`, look for these AccessKey files instead:

`/secret/JindoOss/AccessKeyId`
`/secret/JindoOss/AccessKeySecret`
`/secret/JindoOss/SecurityToken`

### JindoSDK also supports configuring different Credential Providers for different OSS/OSS-HDFS buckets.

Refer to [Configuring OSS/OSS-HDFS Credential Provider by Bucket](jindosdk_credential_provider_bucket.md) for details.