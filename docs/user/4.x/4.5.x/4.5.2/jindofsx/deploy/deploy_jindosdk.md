# 部署 JindoSDK

## 1. 下载 JindoSDK 包
下载最新的 Release 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 解压
```
tar -xzvf jindosdk-x.x.x.tar.gz
```
并将 JindoSDK 安装包部署到所有节点。

## 3. 安装 jar 包
解压下载的安装包，将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.5.2 为例:
```
cp jindosdk-4.5.2/lib/jindo-core-4.5.2.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.5.2/lib/jindo-sdk-4.5.2.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

* 配置 **core-site.xml**

配置在所有节点的 Hadoop 配置文件的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- Client端访问的Namespace地址 -->
        <name>fs.jindofsx.namespace.rpc.address</name>
        <value>headerhost:8101</value>
    </property>
</configuration>
```

并将环境变量配置更新到所有节点。
