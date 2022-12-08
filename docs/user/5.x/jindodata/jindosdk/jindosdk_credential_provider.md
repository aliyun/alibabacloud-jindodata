# 配置 OSS/OSS-HDFS Credential Provider

## 基本配置方式
您可以将 OSS/OSS-HDFS 的`Access Key ID`、`Access Secret`、`Endpoint`预先配置在 Hadoop 的`core-site.xml`。EMR 集群在 Hadoop-Common 页面选择`配置` > `core-site.xml`。

配置项如下：
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
## 使用 Hadoop Credential Providers 存储 AccessKey 信息
上面的`fs.oss.accessKeyId`和`fs.oss.accessKeySecret`将会明文配置在`core-site.xml`中，可以将其以加密对方式存储至 Hadoop Credential Providers文件中。<br />
使用Hadoop提供的命令，存储AccessKey和SecurityToken信息至Credential文件中。命令格式如下。

```
hadoop credential <subcommand> [options]
```
例如，存储AccessKey和Token信息至JECKS文件中，除了使用文件权限保护该文件外，您也可以指定密码加密存储信息，如果不指定密码则使用默认字符串加密。
```
hadoop credential create fs.oss.accessKeyId -value AAA -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.accessKeySecret -value BBB -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.securityToken -value CCC -provider jceks://file/root/oss.jceks
```
生成Credential文件后，您需要配置下面的参数来指定Provider的类型和位置。
```xml
<configuration>
    <property>
        <name>fs.oss.security.credential.provider.path</name>
        <value>jceks://file/root/oss.jceks</value>
        <description>配置存储AK的Credential文件。例如，jceks://file/${user.home}/oss.jceks为HOME下的oss.jceks文件</description>
    </property>
</configuration>
```
## 使用 JindoSDK OSS/OSS-HDFS Credential Provider
默认会配置 SimpleCredentialsProvider，EnvironmentVariableCredentialsProvider，CommonCredentialsProvider 这三个Credential Provider, 按照先后顺序读取Credential直至读到有效的Credential。
```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider,com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider,com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
        <description>配置com.aliyun.jindodata.oss.auth.JindoCredentialsProvider，多个类时使用英文逗号（, ）隔开，按照先后顺序读取Credential直至读到有效的Credential。Provider详情请参见Credential Provider类型。</description>
    </property>
</configuration>
```
您可以根据情况，选择不同的Credential Provider，支持如下Provider：
### 1. TemporaryCredentialsProvider 适合使用有时效性的 AccessKey 和 SecurityToken 访问 OSS/OSS-HDFS 的情况。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.TemporaryCredentialsProvider</value>
    </property>
</configuration>
```

* 配置 OSS/OSS-HDFS AK：

```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>OSS/OSS-HDFS的AccessKey Id</value>
    </property>
    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>OSS/OSS-HDFS的AccessKey Secret</value>
    </property>
    <property>
        <name>fs.oss.securityToken</name>
        <value>OSS/OSS-HDFS的SecurityToken（临时安全令牌)</value>
    </property>
</configuration>
```

### 2. SimpleCredentialsProvider 适合使用长期有效的 AccessKey 访问 OSS/OSS-HDFS 的情况。
* 配置 Provider 类型：

```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider</value>
    </property>
</configuration>
```

* 配置 OSS/OSS-HDFS AK：

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

### 3. EnvironmentVariableCredentialsProvider 在环境变量中获取 AK。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider</value>
    </property>
</configuration>
```

* 配置 OSS/OSS-HDFS AK，需要在环境变量中配置以下参数：

| 参数                                    | 参数说明             |
| ------------------------------------------| ----------------- |
| OSS_ACCESS_KEY_ID                      | OSS/OSS-HDFS 的 AccessKey Id |
| OSS_ACCESS_KEY_SECRET                  | OSS/OSS-HDFS 的 AccessKey Secret |
| OSS_SECURITY_TOKEN                     | OSS/OSS-HDFS 的 SecurityToken（临时安全令牌）。说明 仅配置有时效 Token 时需要。|


### 4. CommonCredentialsProvider 为通用配置。
* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
    </property>
</configuration>
```

* 配置 OSS/OSS-HDFS AK：

```xml
<configuration>
    <property>
        <name>jindo.common.accessKeyId</name>
        <value>OSS/OSS-HDFS 的 AccessKey Id</value>
    </property>
    <property>
        <name>jindo.common.accessKeySecret</name>
        <value>OSS/OSS-HDFS 的 AccessKey Secret</value>
    </property>
    <property>
        <name>jindo.common.securityToken</name>
        <value>OSS/OSS-HDFS 的SecurityToken（临时安全令牌)。说明 仅配置有时效 Token 时需要。</value>
    </property>
</configuration>
```

### 5. CustomCredentialsProvider 对接定制的免密服务。

* 配置Provider类型：

```xml
<configuration>
    <property>
        <name>fs.oss.credentials.provider</name>
        <value>com.aliyun.jindodata.oss.auth.CustomCredentialsProvider</value>
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

### JindoSDK 还支持不同的 OSS/OSS-HDFS bucket 配置不同的 Credential Provider

详情参考[按 bucket 配置 OSS/OSS-HDFS Credential Provider](jindosdk_credential_provider_bucket.md)。

