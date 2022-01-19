# JindoSDK Credential Provider 配置

## 基本配置方式
您可以将 OSS 的`Access Key ID`、`Access Secret`、`Endpoint`预先配置在 Hadoop 的`core-site.xml`，配置项如下：
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
## 使用 Hadoop Credential Providers 存储 AccessKey 信息
上面的`fs.oss.accessKeyId`和`fs.oss.accessKeySecret`将会明文配置在`core-site.xml`中，可以将其以加密对方式存储至 Hadoop Credential Providers文件中。<br />
使用Hadoop提供的命令，存储AccessKey和SecurityToken信息至Credential文件中。命令格式如下。
```
hadoop credential <subcommand> [options]
```
例如，存储 AccessKey 和 Token 信息至 JECKS 文件中，除了使用文件权限保护该文件外，您也可以指定密码加密存储信息，如果不指定密码则使用默认字符串加密。
```
hadoop credential create fs.oss.accessKeyId -value AAA -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.accessKeySecret -value BBB -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.securityToken -value  CCC -provider jceks://file/root/oss.jceks
```
生成 Credential 文件后，您需要配置下面的参数来指定 Provider 的类型和位置。
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.security.credential.provider.path</name>
        <value>jceks://file/root/oss.jceks</value>
        <description>配置存储AK的Credential文件。例如，jceks://file/${user.home}/oss.jceks为HOME下的oss.jceks文件</description>
    </property>
</configuration>
```
## 使用 JindoSDK OSS Credential Provider
默认会配置`SimpleAliyunCredentialsProvider，EnvironmentVariableCredentialsProvider，JindoCommonCredentialsProvider`这三个 Credential Provider, 按照先后顺序读取 Credential 直至读到有效的 Credential。
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.SimpleAliyunCredentialsProvider,com.aliyun.emr.fs.auth.EnvironmentVariableCredentialsProvider，com.aliyun.emr.fs.auth.JindoCommonCredentialsProvider</value>
        <description>配置com.aliyun.emr.fs.auth.AliyunCredentialsProvider的实现类，多个类时使用英文逗号（, ）隔开，按照先后顺序读取Credential直至读到有效的Credential。</description>
    </property>
</configuration>
```
您可以根据情况，选择不同的 Credential Provider，支持如下 Provider：
### 1. TemporaryAliyunCredentialsProvider适合使用有时效性的AccessKey和SecurityToken访问OSS的情况。
* 配置 Provider 类型：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.TemporaryAliyunCredentialsProvider</value>
    </property>
</configuration>
```
* 配置 OSS AK：
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
* 也可以使用 Hadoop Aliyun OSS 的配置 AK 的方式：
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

### 2. SimpleAliyunCredentialsProvider 适合使用长期有效的 AccessKey 访问 OSS 的情况。
* 配置Provider类型：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.SimpleAliyunCredentialsProvider</value>
    </property>
</configuration>
```
* 配置 OSS AK：
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
* 也可以使用 Hadoop Aliyun OSS 的配置 AK 的方式：
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

### 3. EnvironmentVariableCredentialsProvider 在环境变量中获取 AK。
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


### 4. JindoCommonCredentialsProvider 为通用配置。
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

### 5. CustomCredentialsProvider 对接定制的免密服务。

* 配置Provider类型：
```xml
<configuration>
    <property>
        <name>fs.jfs.cache.oss.credentials.provider</name>
        <value>com.aliyun.emr.fs.auth.JindoCommonCredentialsProvider</value>
    </property>
</configuration>
```

* 配置免密服务地址
```xml
<configuration>
    <property>
        <name>aliyun.oss.provider.url</name>
        <value>免密服务地址</value>
    </property>
</configuration>
```

`aliyun.oss.provider.url` 支持 http(s) 协议 和 secrets 协议：

**a. http(s) 协议**

http(s) 协议免密服务地址格式为`http://localhost:1234/sts`, http 免密协议要求返回结果为 Json 格式，如果您需要对接您的 http 免密服务，免密服务的设计参考 [《通过API使用实例RAM角色#临时授权Token》](https://help.aliyun.com/document_detail/61178.html#title-t3j-zsg-1fh)

```
{
"AccessKeyId" : "XXXXXXXXX",
"AccessKeySecret" : "XXXXXXXXX",
"Expiration" : "2020-11-01T05:20:01Z",
"SecurityToken" : "XXXXXXXXX",
"LastUpdated" : "2020-10-31T23:20:01Z",
"Code" : "Success"
}
```

**b. Secrets 协议**

Secrets 协议免密服务地址格式为`secrets:///local_path_prefix`，常见使用于 [k8s 场景](https://kubernetes.io/docs/concepts/configuration/secret/#consuming-secret-values-from-volumes ) ，

其中`local_path_prefix`为路径前缀，如果`local_path_prefix`为`secrets:///secret/JindoOss`，则会在节点上查找如下 AccessKey 等文件

`/secret/JindoOssAccessKeyId`
`/secret/JindoOssAccessKeySecret`
`/secret/JindoOssSecurityToken`

如果`local_path_prefix`为`secrets:///secret/JindoOss/`，则会在节点上查找如下 AccessKey 等文件

`/secret/JindoOss/AccessKeyId`
`/secret/JindoOss/AccessKeySecret`
`/secret/JindoOss/SecurityToken`

### JindoSDK 还支持不同的 OSS bucket 配置不同的 Credential Provider

详情参考[JindoSDK Credential Provider 按 OSS bucket 配置使用说明](jindosdk_credential_provider-bucket.md)。
