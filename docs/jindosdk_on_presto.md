# Presto 使用 JindoSDK 访问 OSS

# 前置

---
* 您的 Presto 集群已经配置 HADOOP 相关参数，并正确配置了 core-site.xml

# 使用

* 在所有 Presto 节点安装 JindoSDK
下载[地址](jindofs_sdk_download.md)
````
cp jindofs-sdk-${version}.jar  $PRESTO_HOME/plugin/hive-hadoop2/
````

* 在所有 Presto 节点修改 core-site.xml 配置 JindoSDK 访问 OSS

````
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

````
如需更多的OSS Credential配置方式，请参考[Credential Provider 使用](jindosdk_credential_provider.md)

* 重启 Presto 所有服务，使配置生效。
