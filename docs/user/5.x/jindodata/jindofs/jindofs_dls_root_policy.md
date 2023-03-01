# 使用 RootPolicy 访问 OSS-HDFS 服务（JindoFS 服务）
*(从 5.0.0-beta1 开始支持)*


## 背景

JindoFS 支持 RootPolicy 规则，通过 RootPolicy 可以为 OSS-HDFS（JindoFS 服务）设置自定义前缀，这样用户原有的访问 `hdfs://` 前缀的作业，可以不经修改直接运行在 JindoFS 服务上。

## 1、设置 RootPolicy
我们提供了一组 Shell 命令，帮助用户向特定的bucket注册自定义地址。通过[SetRootPolicy命令](jindofs_dls_shell_howto.md#setrootpolicy_cmd)，可以为特定 bucket 注册自定义前缀的访问地址。如：

```shell
jindo admin -setRootPolicy oss://<bucket_name>.<dls_endpoint>/ hdfs://<your_ns_name>/
```

例如
````shell
jindo admin -setRootPolicy oss://dls-bucket.cn-shanghai.oss-dls.aliyuncs.com/ hdfs://hdfs-ns1/
````

## 2、配置 access policy 发现地址和 scheme 实现类
在 Hadoop 的 `core-site.xml` 文件中添加如下配置项
```xml
<configuration>
    <property>
        <name>fs.accessPolicies.discovery</name>
        <value>oss://<bucket_name>.<dls_endpoint>/</value>
    </property>
    <property>
        <name>fs.AbstractFileSystem.hdfs.impl</name>
        <value>com.aliyun.jindodata.hdfs.HDFS</value>
    </property>
    <property>
        <name>fs.hdfs.impl</name>
        <!-- Hadoop2 使用 -->
        <value>com.aliyun.jindodata.hdfs.v28.JindoDistributedFileSystem</value>
        <!-- Hadoop3 使用 -->
        <value>com.aliyun.jindodata.hdfs.v3.JindoDistributedFileSystem</value>
    </property>
</configuration>
```
如果是多个bucket，用`,`分隔。

## 3、验证和使用

配置完成后，可以通过 `hadoop` 命令对配置进行简单验证。

```shell
hadoop fs -ls hdfs://<your_ns_name>/
```

验证通过后，重启相关服务，如 Hive/Spark 等，即可使用自定义前缀访问 OSS-HDFS 服务。

## 4、其他功能

### 4.1、删除 RootPolicy 规则
通过[UnsetRootPolicy命令](jindofs_dls_shell_howto.md#unsetrootpolicy_cmd)可以删除相应地址。
```shell
jindo admin -unsetRootPolicy oss://<bucket_name>.<dls_endpoint>/ hdfs://<your_ns_name>/
```

### 4.2、列举所有 RootPolicy 规则
使用[ListAccessPolicies命令](jindofs_dls_shell_howto.md#listaccesspolicies_cmd)，可以列出特定 bucket 当前注册的所有前缀地址。
```shell
jindo admin -listAccessPolicies oss://<bucket_name>.<dls_endpoint>/
```

## 5、高级场景

### 在 EMR 结合 ViewFS 使用 RootPolicy 代理 HDFS 访问

#### 修改集群配置

* 更新 HDFS 的 `hdfs-site.xml`

````
dfs.internal.nameservices
hdfs-cluster

dfs.nameservice.id
hdfs-cluster
````

保存 并 下发配置

* 更新 HADOOP-COMMON 的 `core-site.xml`

````
fs.defaultFS
viewfs://jindo-cluster/

fs.viewfs.mounttable.jindo-cluster.link./data
hdfs://root-policy-1/data

fs.viewfs.mounttable.jindo-cluster.link./new_data
hdfs://root-policy-2/data

fs.accessPolicies.discovery	
oss://$bucket1.cn-shanghai.oss-dls.aliyuncs.com,oss://$bucket.cn-shanghai.oss-dls.aliyuncs.com

fs.hdfs.impl
com.aliyun.jindodata.hdfs.v3.JindoDistributedFileSystem

fs.AbstractFileSystem.hdfs.impl
com.aliyun.jindodata.hdfs.v3.HDFS

fs.viewfs.mounttable.jindo-cluster.link./tmp
hdfs://hdfs-cluster/tmp

fs.viewfs.mounttable.jindo-cluster.link./user
hdfs://hdfs-cluster/user

fs.viewfs.mounttable.jindo-cluster.link./apps
hdfs://hdfs-cluster/apps

fs.viewfs.mounttable.jindo-cluster.link./spark-history
hdfs://hdfs-cluster/spark-history
````

保存 并 下发配置
