# 使用 JindoSDK 访问 Fluid JindoRuntime 集群

## 步骤
前提：启动 Fluid JindoRuntime 集群

### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

### 2. 安装 jar 包
解压下载的安装包，将安装包内的以下 jar 文件安装到 hadoop 的 classpath 下：
* jindo-core-x.x.x.jar
* jindo-sdk-x.x.x.jar

jindosdk-4.6.1 为例:
```
cp jindosdk-4.6.1/lib/jindo-core-4.6.1.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-4.6.1/lib/jindo-sdk-4.6.1.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

### 3. 配置 core-site
登陆到 xxx-jindofs-master-0 容器里
```bash
kubectl exec -ti xxx-jindofs-master-0 bash
```
找到  /hdfs-3.2.1/etc/hadoop/core-site.xml 文件，将文件内容全部加入到 hadoop 的 classpath 下的 core-site 文件中

### 4. shell命令
可以执行 hadoop shell 命令来判断服务是否连接成功
```shell
hadoop fs -ls oss://<Bucket>/<Dir>
```

### 6. 参数调优
* JindoFSx 参数说明
  
  JindoFSx 包含一些缓存相关的参数，请参考文档 [JindoFSx 客户端配置列表](/docs/user/4.x/4.6.x/4.6.1/jindofsx/jindo_fluid/jindo_fluid_ways_to_use.md)
* JindoSDK 参数说明
  
  JindoSDK 包含一些高级调优参数，配置方式以及配置项参考文档 [JindoSDK 配置项列表](/docs/user/4.x/4.6.x/4.6.1/oss/configuration/jindosdk_configuration_list.md)

### 7. 权限管理

* 自建集群，请参考 [阿里云 OSS 使用 Ranger 的鉴权方案](/docs/user/4.x/4.6.x/4.6.1/jindofsx/permission/jindofsx_ranger.md)
* 阿里云 E-MapReduce 集群，请参考 [EMR 集群中阿里云 OSS 使用 Ranger 的鉴权方案](/docs/user/4.x/4.6.x/4.6.1/jindofsx/permission/jindofsx_ranger_emr.md)