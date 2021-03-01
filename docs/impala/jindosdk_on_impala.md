# Impala 使用 JindoSDK 访问 OSS

# 前置

---
* 您的 Impala 集群已经配置 HADOOP 相关参数，并正确配置了 core-site.xml

# 使用

### 在所有 Impala 节点安装 JindoSDK
[下载地址](jindofs_sdk_download.md)
````
cp jindofs-sdk-${version}.jar  $IMPALA_HOME/lib/
````


### 通过配置 IMPALA 使用的 core-site.xml 配置 JindoSDK  访问 OSS
* 配置 JindoFS OSS 实现类
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.OSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```
* 配置 OSS Access Key
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
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](../jindofs_sdk_credential_provider.md)。<br />


### 重启 Impala 所有服务，使配置生效。

### 参数调优
JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档 [JindoFS SDK配置项列表](../jindofs_sdk_configuration_list.md)。
<br />
