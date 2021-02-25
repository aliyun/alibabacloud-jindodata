# Druid 使用 JindoFS SDK 访问 OSS

# 前置

---
* 您的 Druid 集群已经配置 HADOOP 相关参数，并且加载HDFS Deep Storage 扩展.

* 确保$DRUID_HOME/conf/cluster/_common目录下包含hadoop配置文件, 可以将core-site.xml, hdfs-site.xml等hadoop的配置文件拷贝到$DRUID_HOME/conf/cluster/_common目录下.

# 使用

* 下载JindoFS SDK最新版本， 下载[地址](jindofs_sdk_download.md).

* 在所有 Druid 节点安装 JindoFS SDK.

````
cp jindofs-sdk-${version}.jar  $DRUID_HOME/extensions/druid-hdfs-storage/
````

* 配置Druid HDFS的扩展使用JindoFS SDK 访问OSS，编辑 $DRUID_HOME/conf/cluster/_common/core-site.xml.

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

* 配置 Druid 使用 OSS 作为Deep Storage, 编辑 $DRUID_HOME/conf/druid/cluster/_common/common.runtime.properties.

````
druid.storage.type = hdfs
druid.storage.storageDirectory = oss://xxxx/xxxx
````

* 重启 Druid 所有服务，使配置生效。
