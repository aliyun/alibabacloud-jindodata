# 部署 JindoSDK

## 1. 下载 JindoSDK 包
下载最新的 Release 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 解压
```
tar -xzvf jindosdk-x.x.x.tar.gz
```

## 3. 安装 jar 包

将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.4.0 为例:
```
cp jindosdk-4.4.0/lib/jindo-core-4.4.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.4.0/lib/jindo-sdk-4.4.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```
并将 JindoSDK 安装包部署到所有节点。

## 4. 配置 **core-site.xml**

配置在所有节点的 Hadoop 配置文件的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- Client端访问的Namespace地址 -->
        <name>fs.fsx.namespace.rpc.address</name>
        <value>headerhost:8101</value>
    </property>
</configuration>
```
