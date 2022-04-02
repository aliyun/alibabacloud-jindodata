# 部署 JindoSDK

## 1. 下载 JindoSDK 包
下载最新的 Release 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 解压
```
tar -xzvf jindosdk-x.x.x.tar.gz
```
并将 JindoSDK 安装包部署到所有节点。

## 3. 配置环境变量

* 配置`JINDOSDK_HOME`

以安装包内容解压在`/usr/lib/jindosdk-4.3.0`目录为例：

```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-4.3.0
```

* 配置`HADOOP_CLASSPATH`

```bash
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
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
