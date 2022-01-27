# JindoFSx 统一挂载(fsx://) Credential 配置

## 全局默认配置

您可以将 OSS 的`Access Key ID`、`Access Secret`、`Endpoint`预先配置在 Hadoop 的`core-site.xml`，配置项如下：
```xml
<configuration>
    <property>
        <name>fs.fsx.oss.accessKeyId</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.fsx.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.fsx.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```

## 按 bucket 配置

在 Hadoop 的`core-site.xml`配置单个 bucket 的`Access Key ID`、`Access Secret`、`Endpoint`。
```xml
<configuration>
    <property>
        <name>fs.fsx.oss.bucket.XXX.accessKeyId</name>
        <value>bucket XXX 的AccessKey Id</value>
    </property>
    <property>
        <name>fs.fsx.oss.bucket.XXX.accessKeySecret</name>
        <value>bucket XXX 的AccessKey Secret</value>
    </property>
    <property>
        <name>fs.fsx.oss.bucket.XXX.endpoint</name>
        <value>bucket XXX 的 endpoint</value>
    </property>
</configuration>
```
说明：XXX 为 或 OSS 或 OSS-HDFS 服务 bucket的名称。



