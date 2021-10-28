# CDH 集群 使用 JindoFS SDK 访问 OSS

CDH（Cloudera's Distribution, including Apache Hadoop）是众多Hadoop发行版本中的一种，本文以CDH 5.16.2为例介绍如何配置CDH支持使用JindoFS SDK访问OSS。

## 步骤

### 1. 安装 jar 包
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](/docs/jindofs_sdk_download.md))，将sdk包安装到hadoop的classpath下。
```
cp ./jindofs-sdk-*.jar /opt/cloudera/parcels/CDH/lib/hadoop/lib/jindofs-sdk.jar
```

### 2. 配置 JindoFS OSS 实现类
通过集群管理工具Cloudera Manager增加JindoFS OSS 实现类配置，并根据Cloudera Manager提示重启集群和部署客户端配置。

<img src="../pic/jindofs_sdk_cdh_oss_impl_config.png#pic_center" />

若没有Cloudera Manager管理的集群，可以配置core-site.xml中。
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
### 3. 配置 OSS Access Key
通过集群管理工具Cloudera Manager增加OSS的Access Key、Access Key Secret、Endpoint等配置，并根据Cloudera Manager提示重启集群和部署客户端配置。

<img src="../pic/jindofs_sdk_cdh_oss_impl_config.png#pic_center" />

若没有Cloudera Manager管理的集群，可以配置core-site.xml中。
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
      	<!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](./jindofs_sdk_credential_provider.md)。<br />

### 4. 使用 JindoFS SDK 访问 OSS
用Hadoop Shell访问OSS，下面列举了几个常用的命令。

* put 操作
```
hadoop fs -put <path> oss://<bucket>/
```

* ls 操作
```
hadoop fs -ls oss://<bucket>/
```

* mkdir 操作
```
hadoop fs -mkdir oss://<bucket>/<path>
```

* rm 操作
```
hadoop fs rm oss://<bucket>/<path>
```

<img src="../pic/jindofs_sdk_cdh_cmd.png#pic_center" />

### 5. 参数调优
JindoFS SDK包含一些高级调优参数，配置方式以及配置项参考文档  [JindoFS SDK 配置项列表](jindofs_sdk_configuration_list_3_x.md) 【注：3.0 以下版本此 [参考配置项列表](./jindofs_sdk_configuration_list.md)】
<br />