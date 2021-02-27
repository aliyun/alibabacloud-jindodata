# JindoFS SDK OSS AccessKey 配置

## 基本配置方式
您可以将OSS的Access Key ID、Access Secret、Endpoint预先配置在Hadoop的core-site.xml，配置项如下：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.accessKeyId</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.jfs.cache.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.jfs.cache.oss.endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
## 使用 Hadoop Credential Providers 存储 AccessKey 信息
上面的 fs.jfs.cache.oss.accessKeyId 和 fs.jfs.cache.oss.accessKeySecret 将会明文配置在core-site.xml中，可以将其以加密对方式存储至Hadoop Credential Providers文件中。<br />
使用Hadoop提供的命令，存储AccessKey和SecurityToken信息至Credential文件中。命令格式如下。
```
hadoop credential <subcommand> [options]
```
例如，存储AccessKey和Token信息至JECKS文件中，除了使用文件权限保护该文件外，您也可以指定密码加密存储信息，如果不指定密码则使用默认字符串加密。
```
hadoop credential create fs.jfs.cache.oss.accessKeyId -value AAA -provider jceks://file/root/oss.jceks
hadoop credential create fs.jfs.cache.oss.accessKeySecret -value BBB -provider jceks://file/root/oss.jceks
hadoop credential create fs.jfs.cache.oss.securityToken -value  CCC -provider jceks://file/root/oss.jceks
```
生成Credential文件后，您需要配置下面的参数来指定Provider的类型和位置。
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.security.credential.provider.path</name>
        <value>jceks://file/root/oss.jceks</value>
        <description>配置存储AK的Credential文件。例如，jceks://file/${user.home}/oss.jceks为HOME下的oss.jceks文件</description>
    </property>
</configuration>
```
## 使用 JindoFS SDK OSS Credential Provider
默认会配置 SimpleAliyunCredentialsProvider，EnvironmentVariableCredentialsProvider，JindoCommonCredentialsProvider 这三个Credential Provider, 按照先后顺序读取Credential直至读到有效的Credential。
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.SimpleAliyunCredentialsProvider,com.aliyun.emr.fs.auth.EnvironmentVariableCredentialsProvider，com.aliyun.emr.fs.auth.JindoCommonCredentialsProvider</value>
        <description>配置com.aliyun.emr.fs.auth.AliyunCredentialsProvider的实现类，多个类时使用英文逗号（, ）隔开，按照先后顺序读取Credential直至读到有效的Credential。</description>
    </property>
</configuration>
```
您可以根据情况，选择不同的Credential Provider，支持如下Provider：
### 1. TemporaryAliyunCredentialsProvider适合使用有时效性的AccessKey和SecurityToken访问OSS的情况。
* 配置Provider类型：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.TemporaryAliyunCredentialsProvider</value>
    </property>
</configuration>
```
* 配置OSS AK：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.accessKeyId</name>
        <value>OSS的AccessKey Id</value>
    </property>
    <property>
        <name>fs.jfs.cache.oss.accessKeySecret</name>
        <value>OSS的AccessKey Secret</value>
    </property>
    <property>
        <name>fs.jfs.cache.oss.securityToken</name>
        <value>OSS的SecurityToken（临时安全令牌)</value>
    </property>
</configuration>
```
* 也可以使用Hadoop Aliyun OSS 的配置AK的方式：
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>OSS的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>OSS的AccessKey Secret</value>
    </property>
    <property>
        <name>fs.oss.securityToken</name>
        <value>OSS的SecurityToken（临时安全令牌)</value>
    </property>
</configuration>
```

### 2. SimpleAliyunCredentialsProvider适合使用长期有效的AccessKey访问OSS的情况。
* 配置Provider类型：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.SimpleAliyunCredentialsProvider</value>
    </property>
</configuration>
```
* 配置OSS AK：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.accessKeyId</name>
        <value>OSS的AccessKey Id</value>
    </property>
    <property>
        <name>fs.jfs.cache.oss.accessKeySecret</name>
        <value>OSS的AccessKey Secret</value>
    </property>
</configuration>
```
* 也可以使用Hadoop Aliyun OSS 的配置AK的方式：
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>OSS的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>OSS的AccessKey Secret</value>
    </property>
</configuration>
```

### 3. EnvironmentVariableCredentialsProvider在环境变量中获取AK。
* 配置Provider类型：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.EnvironmentVariableCredentialsProvider</value>
    </property>
</configuration>
```
* 配置OSS AK，需要在环境变量中配置以下参数：

| 参数                                    | 参数说明             |
| ------------------------------------------| ----------------- |
| ALIYUN_ACCESS_KEY_ID                      | OSS的AccessKey Id |
| ALIYUN_ACCESS_KEY_SECRET                  | OSS的AccessKey Secret |
| ALIYUN_SECURITY_TOKEN                     | OSS的SecurityToken（临时安全令牌）。说明 仅配置有时效Token时需要。|


### 4. JindoCommonCredentialsProvider为通用配置。
* 配置Provider类型：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.JindoCommonCredentialsProvider</value>
    </property>
</configuration>
```
* 配置OSS AK：
```xml
<configuration>
    <property>
        <name>jindo.common.accessKeyId</name>
        <value>OSS的AccessKey Id</value>
    </property>
    <property>
        <name>jindo.common.accessKeySecret</name>
        <value>OSS的AccessKey Secret</value>
    </property>
    <property>
        <name>jindo.common.securityToken</name>
        <value>OSS的SecurityToken（临时安全令牌)。说明 仅配置有时效Token时需要。</value>
    </property>
</configuration>
```

### JindoFS SDK 还支持不同的 OSS bucket 配置不同的 Credential Provider
  详情参考[JindoFS SDK Credential Provider 按 OSS bucket 配置使用说明](./jindofs_sdk_credential_provider-bucket.md)。

