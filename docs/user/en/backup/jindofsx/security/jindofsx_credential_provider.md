# JindoSDK OSS Credential Provider 配置

## 基本配置方式

在缓存数据时，JindoFSx 服务端需要能访问被缓存的后端存储系统，获取所需数据。根据您可以根据访问的存储类型，您可以参考相应的文档对 JindoFSx 服务访问后端存储的 Credential 和 Endpoint 进行配置，配置支持明确到 bucket 级别。

### 服务端访问阿里云 OSS

JindoFSx 服务端支持对 OSS 协议的所有访问配置统一的 Credential 信息，如下所示：
```
[jindofsx-common]
jindofsx.oss.accessKeyId = xxx
jindofsx.oss.accessKeySecret = xxx
jindofsx.oss.endpoint = oss-cn-xxx-internal.aliyuncs.com
```

您也可以为每个 bucket 配置独立的 Credential 信息和 Endpoint：
```
jindofsx.oss.bucket.XXX.accessKeyId = xxx
jindofsx.oss.bucket.XXX.accessKeySecret = xxx
jindofsx.oss.bucket.XXX.endpoint = oss-cn-xxx-internal.aliyuncs.com

jindofsx.oss.bucket.YYY.accessKeyId = xxx
jindofsx.oss.bucket.YYY.accessKeySecret = xxx
jindofsx.oss.bucket.YYY.endpoint = oss-cn-xxx-internal.aliyuncs.com
```

### 服务端访问阿里云 OSS-HDFS 服务（JindoFS 服务）

JindoFSx 服务端支持对 OSS 协议的所有访问配置统一的 Credential 信息，如下所示：
```
[jindofsx-common]
jindofsx.oss.accessKeyId = xxx
jindofsx.oss.accessKeySecret = xxx
jindofsx.oss.endpoint = oss-cn-xxx-internal.aliyuncs.com
jindofsx.oss.user = user #Storage Service 访问 OSS-HDFS 服务使用的用户名
```

您也可以为每个 bucket 配置独立的 Credential 信息和 Endpoint，尤其是在同时使用 OSS 服务与 OSS-HDFS 服务时，需要分别配置不同的 bucket：
```
[jindofsx-common]
jindofsx.oss.bucket.XXX.accessKeyId = xxx
jindofsx.oss.bucket.XXX.accessKeySecret = xxx
jindofsx.oss.bucket.XXX.endpoint = cn-xxx.oss-dls.aliyuncs.com

jindofsx.oss.bucket.YYY.accessKeyId = xxx
jindofsx.oss.bucket.YYY.accessKeySecret = xxx
jindofsx.oss.bucket.YYY.endpoint = cn-xxx.oss-dls.aliyuncs.com

jindofsx.oss.bucket.YYY.user = user #Storage Service 访问 OSS-HDFS 服务使用的用户名
```

### 服务端访问 Apache HDFS

JindoFSx 服务端需要与 HDFS 集群进行网络通信，并根据相应 HDFS 集群的配置设置 HDFS 集群的访问信息。如果访问单 master 的 HDFS 集群，需要配置访问用户名：

```
[jindofsx-common]
jindofsx.hdfs.user = user
```

如需访问 nameservice 配置为 mycluster 的 HDFS HA 集群，则配置如下所示：

```
[jindofsx-common]
jindofsx.hdfs.mycluster.dfs.ha.namenodes = nn1,nn2
jindofsx.hdfs.mycluster.dfs.namenode.rpc-address.nn1 = <nn1-host>:8998
jindofsx.hdfs.mycluster.dfs.namenode.rpc-address.nn2 = <nn2-host>:8999
jindofsx.hdfs.user = user
```

### 服务端访问 NAS
JindoFSx 与 JindoSDK 要求 NAS 服务已经 mount 到所有节点的本地文件系统，并保证服务和客户端都对挂载目录有读权限。

### 服务端访问其他云存储（S3/COS/OBS等）
JindoFSx 服务端支持对某种协议的所有访问配置统一的 Credential 信息，如下所示：
```
[jindofsx-common]
jindofsx.s3.accessKeyId = xxx
jindofsx.s3.accessKeySecret = xxx
jindofsx.s3.endpoint = s3.us-west-1.amazonaws.com

jindofsx.cos.accessKeyId = xxx
jindofsx.cos.accessKeySecret = xxx
jindofsx.cos.endpoint = cos.ap-shanghai.myqcloud.com

jindofsx.obs.accessKeyId = xxx
jindofsx.obs.accessKeySecret = xxx
jindofsx.obs.endpoint = obs.cn-east-3.myhuaweicloud.com
```

您也可以为每个 bucket 配置独立的 Credential 信息和 Endpoint，以下以 S3 为例：
```
jindofsx.s3.bucket.XXX.accessKeyId = xxx
jindofsx.s3.bucket.XXX.accessKeySecret = xxx
jindofsx.s3.bucket.XXX.endpoint = s3.us-west-1.amazonaws.com

jindofsx.s3.bucket.YYY.accessKeyId = xxx
jindofsx.s3.bucket.YYY.accessKeySecret = xxx
jindofsx.s3.bucket.YYY.endpoint = s3.ap-east-1.amazonaws.com
```

### 服务端访问统一命名空间下的存储
根据对应的实际存储系统，进行配置即可。

### 服务端使用 JindoSDK OSS Credential Provider
当前版本暂不支持在 JindoFSx 服务端使用 JindoSDK OSS Credential Provider。
