# Hive 使用 JindoSDK 访问 OSS

## 前置

* 您的 Hive 集群已经配置 HADOOP 环境，并正确配置了 core-site.xml

## 使用

1. 在 Hive 客户端或服务所在节点安装 JindoSDK。

下载[地址](jindofs_sdk_download.md)

```
cp jindofs-sdk-${version}.jar  $HIVE_HOME/lib/
```

2. 在运行 hivecli 或 HiveServer2 或 Hive Metastore 服务的节点修改 core-site.xml 配置 JindoSDK 访问 OSS

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
        <!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

如需更多的OSS Credential配置方式，请参考[Credential Provider 使用](jindosdk_credential_provider.md)

然后重启 Hive 所有服务，使配置生效。

3. 当使用 Hive on MR 方式执行 Hive 作业时，还应保证集群所有结点均安装了 JindoSDK，需要把 `jindofs-sdk-${version}.jar` 放到 `$HADOOP_CLASSPATH` 并重启 YARN 服务。也可以把 `jindofs-sdk-${version}.jar` 设置到 *hive-env.sh* 的 `HIVE_AUX_JARS_PATH` 变量中，并重启 Hive 所有服务。

4. 当使用 Hive on Tez 方式执行 Hive 作业时，还应保证配置 `tez.lib.uris` 所指向路径中包含 `jindofs-sdk-${version}.jar`。

5. 当使用 Hive on Spark 方式执行 Hive 作业时，请参考[Spark 使用 JindoSDK 访问 OSS](/docs/spark/jindosdk_on_spark.md)同时配置好 Spark。
