# 使用 RootPolicy 访问 OSS-HDFS 服务（JindoFS 服务）
*(从 4.6.0 开始支持)*


## 背景

JindoFS 支持 RootPolicy 规则，通过 RootPolicy 可以为 OSS-HDFS（JindoFS 服务）设置自定义前缀，这样用户原有的访问 `hdfs://` 前缀的作业，可以不经修改直接运行在 JindoFS 服务上。

## 1、 设置 RootPolicy
我们提供了一组 Shell 命令，帮助用户向特定的bucket注册自定义地址。通过[SetRootPolicy命令](../../5.x/jindodata/jindosdk/jindosdk_cli_options.md)，可以为特定 bucket 注册自定义前缀的访问地址。如：

```shell
jindo admin -setRootPolicy oss://<bucket_name>.<dls_endpoint>/ hdfs://<your_ns_name>/
```

* <bucket_name>: OSS-HDFS 服务 bucket 的名字，当前版本仅支持根目录。
* <dls_endpoint>: OSS-HDFS 服务的 endpoint，如杭州region为`cn-hangzhou.oss-dls.aliyuncs.com`。如果 Hadoop 的`core-site.xml`文件中有如下配置项
```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value><dls_endpoint></value>
    </property>
    或
    <property>
        <name>fs.oss.bucket.<bucket_name>.endpoint</name>
        <value><dls_endpoint></value>
    </property>
</configuration>
```
上面例子中的`oss://<bucket_name>.<dls_endpoint>/`可以写成`oss://<bucket_name>/`的形式，下文中需要 endpoint 的地方都可省略不填。
* <your_ns_name>: 自定义访问 hdfs 服务的 nsname，为任意非空字符串，如`test`，当前版本仅支持根目录。

## 2、 配置 access policy 发现地址 和 scheme 实现类
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
        <value>com.aliyun.jindodata.hdfs.JindoHdfsFileSystem</value>
    </property>
</configuration>
```
如果是多个bucket，用`,`分隔。


## 3、 验证和使用

配置完成后，可以通过 `hadoop` 命令对配置进行简单验证。

```shell
hadoop fs -ls hdfs://<your_ns_name>/
```

验证通过后，重启相关服务，如 Hive/Spark 等，即可使用自定义前缀访问 OSS-HDFS 服务。

## 4、其他功能
### 4.1 删除 RootPolicy 规则
通过[UnsetRootPolicy命令](../../5.x/jindodata/jindosdk/jindosdk_cli_options.md)可以删除相应地址。
```shell
jindo admin -unsetRootPolicy oss://<bucket_name>.<dls_endpoint>/ hdfs://<your_ns_name>/
```

### 4.2 列举所有 RootPolicy 规则
使用[ListAccessPolicies命令](../../5.x/jindodata/jindosdk/jindosdk_cli_options.md)，可以列出特定 bucket 当前注册的所有前缀地址。
```shell
jindo admin -listAccessPolicies oss://<bucket_name>.<dls_endpoint>/
```
