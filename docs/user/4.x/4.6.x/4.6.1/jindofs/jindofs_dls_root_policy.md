# 通过设置 RootPolicy 实现通过自定义前缀路径访问 JindoFS

## 0. 背景

JindoFS 支持 RootPolicy 规则，通过 RootPolicy 可以为 OSS-HDFS（JindoFS 服务）设置自定义前缀，这样用户原有的访问 `hdfs://` `s3://` `jfs://` 等前缀的作业，可以不经修改直接运行在 JindoFS 服务上。

## 1. 设置和管理 RootPolicy

我们提供了一组 Shell 命令，帮助用户向特定的bucket注册自定义地址。通过[SetRootPolicy命令](jindofs_dls_shell_howto.md#setrootpolicy_cmd)，可以为特定 bucket 注册自定义前缀的访问地址。如：

```shell
jindo admin -setRootPolicy oss://mybucket1/ hdfs://ns/
jindo admin -setRootPolicy oss://mybucket2/ oss://test/
```

通过[UnsetRootPolicy命令](jindofs_dls_shell_howto.md#unsetrootpolicy_cmd)可以删除相应地址。

使用 [ListAccessPolicies命令](jindofs_dls_shell_howto.md#listaccesspolicies_cmd)，可以列出特定 bucket 当前注册的所有前缀地址。

## 2. 配置 SDK

在注册了自定义地址后，我们需要对客户端的配置进行修改，来读取服务端的 RootPolicy 并根据规则使用自定义前缀访问。

### 2.1 配置访问 JindoFS 服务的必要配置

首先，我们要确保JindoSDK 能够用 OSS 前缀，直接访问到相应的 JindoFS 服务，请参考[阿里云 OSS-HDFS 服务（JindoFS 服务）快速入门](jindo_dls_quickstart.md#basicconfig)

### 2.2 配置 access policy 发现地址

还需配置一个`,`分隔的列表，用于在客户端初始化时读取注册好的规则：

```xml
<configuration>
    <property>
        <name>fs.accessPolicies.discovery</name>
        <value>oss://mybucket1/,oss://mybucket2/</value>
    </property>
</configuration>
```

注意，如果`fs.oss.endpoint`和`fs.oss.bucket.mybucket1.endpoint`均未配置为JindoFS endpoint的情况下，上面例子中的`oss://mybucket1/`需要写成`oss://<bucket>.<endpoint>`的形式，如`oss://mybucket1.cn-xxx.oss-dls.aliyuncs.com/`。

### 2.3 配置相应 sheme 的实现类

最后，如果设置的自定义路径不是 `oss://` 前缀，比如使用了 `hdfs://` 前缀，那么还需要为相应前缀配置 JAVA FS 实现类：

```xml
<configuration>
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

目前，由于 JAVA 实现类的限制，在访问时，仅支持使用`oss`、`hdfs`、`s3`、`s3a`、`cos`、`cosn`、`obs`、`jindo`的前缀。

## 3. 验证和使用

配置完成后，可以通过 `hadoop` 命令对配置进行简单验证。

```shell
hadoop fs -ls hdfs://ns/
```

验证通过后，重启相关服务，如 Hive/Spark 等，即可使用自定义前缀访问 JindoFS 服务。
