Basic configuration method
You can configure the AccessKey ID, AccessKey secret, and endpoint of the bucket for which Alibaba Cloud OSS-HDFS is enabled in the core-site.xml configuration file of Hadoop in advance. The AccessKey ID, AccessKey secret, and endpoint are specified by the following configuration items:
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
<value>cn-xxx.oss-dls.aliyuncs.com</value>
</property>
</configuration>
Use a Hadoop credential provider to store an AccessKey pair
The values of the fs.oss.accessKeyId and fs.oss.accessKeySecret configuration items that you added are displayed in plaintext in the core-site.xml configuration file. You can encrypt and store the values of the configuration items in the credential file of a Hadoop credential provider. Use a command provided by Hadoop to store the AccessKey pair and security token in a credential file. Command syntax: 
hadoop credential <subcommand> [options]
For example, you can store the AccessKey pair and security token in a JCEKS file. You can protect the file by using file permissions or specify a password to encrypt the information that you want to store. If you do not specify a password, the default string is used for encryption. 
hadoop credential create fs.oss.accessKeyId -value AAA -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.accessKeySecret -value BBB -provider jceks://file/root/oss.jceks
hadoop credential create fs.oss.securityToken -value  CCC -provider jceks://file/root/oss.jceks
After a credential file is generated, you must configure the following parameter to specify the location of the credential provider: 
<configuration>
<property>
<name>fs.oss.security.credential.provider.path</name>
<value>jceks://file/root/oss.jceks</value>
<description>The path that is used to store the credential file that stores the AccessKey pair. For example, you can set this parameter to jceks://file/${user.home}/oss.jceks, which indicates that the oss.jceks file is stored in the home directory.</description>
</property>
</configuration>
Use credential providers in JindoSDK to access OSS-HDFS
By default, the following types of credential providers are configured: SimpleCredentialsProvider, EnvironmentVariableCredentialsProvider, and CommonCredentialsProvider. The system reads credential data from the credential providers in sequence until a valid credential is obtained. 
<configuration>
<property>
<name>fs.oss.credentials.provider</name>
<value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider,com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider,com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
<description>The types of com.aliyun.jindodata.oss.auth.JindoCredentialsProvider. Separate multiple credential providers with commas (,). The system reads credential data from the credential providers in sequence until a valid credential is obtained. For more information about credential providers, see the following descriptions. </description>
</property>
</configuration>
You can select credential providers based on your business requirements. The following types of credential providers are supported:
1. TemporaryCredentialsProvider: suitable for scenarios in which an AccessKey pair with a validity period and a security token with a validity period are used to access OSS-HDFS 

- Configure the credential provider.

<configuration>
<property>
<name>fs.oss.credentials.provider</name>
<value>com.aliyun.jindodata.oss.auth.TemporaryCredentialsProvider</value>
</property>
</configuration>

- Configure the AccessKey pair and security token that are used to access OSS-HDFS.

<configuration>
<property>
<name>fs.oss.accessKeyId</name>
<value>The AccessKey ID that is used to access OSS-HDFS.</value>
</property>
<property>
<name>fs.oss.accessKeySecret</name>
<value>The AccessKey secret that is used to access OSS-HDFS.</value>
</property>
<property>
<name>fs.oss.securityToken</name>
<value>The temporary security token that is used to access OSS-HDFS.</value>
</property>
</configuration>
2. SimpleCredentialsProvider: suitable for scenarios in which a permanently valid AccessKey pair is used to access OSS-HDFS 

- Configure the credential provider.

<configuration>
<property>
<name>fs.oss.credentials.provider</name>
<value>com.aliyun.jindodata.oss.auth.SimpleCredentialsProvider</value>
</property>
</configuration>

- Configure the AccessKey pair that is used to access OSS-HDFS.

<configuration>
<property>
<name>fs.oss.accessKeyId</name>
<value>The AccessKey ID that is used to access OSS-HDFS.</value>
</property>
<property>
<name>fs.oss.accessKeySecret</name>
<value>The AccessKey secret that is used to access OSS-HDFS.</value>
</property>
</configuration>
3. EnvironmentVariableCredentialsProvider: suitable for scenarios in which an AccessKey pair is obtained from the environment variables 

- Configure the credential provider.

<configuration>
<property>
<name>fs.oss.credentials.provider</name>
<value>com.aliyun.jindodata.oss.auth.EnvironmentVariableCredentialsProvider</value>
</property>
</configuration>

- Configure the AccessKey pair and security token that are used to access OSS-HDFS. To configure such information, you must configure the parameters that are described in the following table in the environment variable file.
| Parameter | Description |
| --- | --- |
| OSS_ACCESS_KEY_ID | The AccessKey ID that is used to access OSS-HDFS. |
| OSS_ACCESS_KEY_SECRET | The AccessKey secret that is used to access OSS-HDFS. |
| OSS_SECURITY_TOKEN | The temporary security token that is used to access OSS-HDFS. Note: This parameter is required only if you want to configure a token with a validity period.  |

4. CommonCredentialsProvider: suitable for common scenarios 

- Configure the credential provider.

<configuration>
<property>
<name>fs.oss.credentials.provider</name>
<value>com.aliyun.jindodata.oss.auth.CommonCredentialsProvider</value>
</property>
</configuration>

- Configure the AccessKey pair and security token that are used to access OSS-HDFS.

<configuration>
<property>
<name>jindo.common.accessKeyId</name>
<value>The AccessKey ID that is used to access OSS-HDFS.</value>
</property>
<property>
<name>jindo.common.accessKeySecret</name>
<value>The AccessKey secret that is used to access OSS-HDFS.</value>
</property>
<property>
<name>jindo.common.securityToken</name>
<value>The temporary security token that is used to access OSS-HDFS. This parameter is required only if you want to configure a token with a validity period. </value>
</property>
</configuration>
5. CustomCredentialsProvider: suitable for accessing password-free services 

- Configure the credential provider.

<configuration>
<property>
<name>fs.oss.credentials.provider</name>
<value>com.aliyun.jindodata.oss.auth.CustomCredentialsProvider</value>
</property>
</configuration>

- Configure the URL of a password-free service.

<configuration>
<property>
<name>aliyun.oss.provider.url</name>
<value>The URL of a password-free service.</value>
</property>
</configuration>
You can set the aliyun.oss.provider.url parameter to the URL of a password-free service that can be accessed over the HTTP, HTTPS, or Secrets protocol.
a. HTTP or HTTPS
The URL of a password-free service that can be accessed over HTTP or HTTPS is in a format similar to http://localhost:1234/sts. The return value for the URL of a password-free service that is accessed over HTTP must be in the JSON format.
{
"AccessKeyId" : "XXXXXXXXX",
"AccessKeySecret" : "XXXXXXXXX",
"Expiration" : "2020-11-01T05:20:01Z",
"SecurityToken" : "XXXXXXXXX",
"LastUpdated" : "2020-10-31T23:20:01Z",
"Code" : "Success"
}
b. Secrets
The URL of a password-free service that can be accessed over the Secrets protocol is in the secrets:///local_path_prefix format. This URL format is commonly used in [Kubernetes scenarios](https://kubernetes.io/docs/concepts/configuration/secret/#consuming-secret-values-from-volumes). 
local_path_prefix indicates the path prefix. If the URL is secrets:///secret/JindoOss, the system searches for the following files on the nodes of your EMR cluster:
/secret/JindoOssAccessKeyId, /secret/JindoOssAccessKeySecret, and /secret/JindoOssSecurityToken
If the URL is secrets:///secret/JindoOss/, the system searches for the following files on the nodes of your EMR cluster:
/secret/JindoOss/AccessKeyId, /secret/JindoOss/AccessKeySecret, and /secret/JindoOss/SecurityToken
JindoSDK allows you to configure different credential providers for different buckets for which OSS-HDFS is enabled.
For more information, see [Configure a credential provider of OSS-HDFS by bucket](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/security/jindosdk_credential_provider_bucket_dls.md). 

