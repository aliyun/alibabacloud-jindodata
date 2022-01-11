# 部署 JindoFS NameSpace Service

## 部署 JindoSDK
### 1. 安装 jar 包
下载最新的jar包 jindofs-sdk-x.x.x.jar ([下载页面](../jindosdk_download.md))，将sdk包安装到hadoop的classpath下。
```
cp ./jindofs-sdk-*.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/jindofs-sdk.jar
```

### 2. 配置 JindoFS JFS 实现类
将 JindoFS JFS 实现类配置到Hadoop的core-site.xml中。
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.jfs.impl</name>
        <value>com.aliyun.emr.fs.jfs.JFS</value>
    </property>

    <property>
        <name>fs.jfs.impl</name>
        <value>com.aliyun.emr.fs.jfs.JindoFileSystem</value>
    </property>

    <property>
        <name>fs.jfs.namespace.rpc-address</name>
        <!-- JindoFS 集群的 namespace rpc 地址，如您未使用 Raft 配置，添加一个 rpc-address 即可 -->
        <value>172.16.xx.xx:8101,172.16.xx.xx:8101,172.16.xx.xx:8101</value>
    </property>

    <property>
        <!-- 访问 JindoFS OSS 后端存储的 Endpoint  -->
        <name>jindo.common.oss.endpoint</name>
      	<!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

## 部署 NameSpace 服务
### 下载最新安装包

下载最新 Release 包 b2smartdata-x.x.x.tar.gz ([下载页面](../jindosdk_download.md))。

### 解压
```
tar -xzvf b2smartdata-x.x.x.tar.gz
```

### 配置 bigboot.cfg
在 b2smartdata-x.x.x/conf 文件夹下创建文件 bigboot.cfg  包含以下主要内容:
```
[bigboot]
logger.dir = /tmp/bigboot-log

[bigboot-client]
client.storage.rpc.port = 6101
client.namespace.rpc.address = localhost:8101

[bigboot-namespace]
namespace.rpc.port = 8101
```
需要将[bigboot-client] 中的参数 client.namespace.rpc.address 修改为 JindoFS 集群的 NameSpace 服务的所在节点地址，其中 8101 为 NameSpace RPC 的端口号， 需要与 [bigboot-namespace] 中的参数 namespace.rpc.port 中的端口号一致。
[bigboot-client] 高级参数配置请参考[JindoSDK 配置项列表](../configuration/jindosdk_configuration_list_3_x.md)

### 配置环境变量
以 b2smartdata-3.6.0 安装在 /usr/lib 为例：
```
export BIGBOOT_SMARTDATA_HOME=/usr/lib/b2smartdata-3.6.0/
export BIGBOOT_JINDOSDK_HOME=/usr/lib/b2smartdata-3.6.0/
export B2SDK_CONF_DIR=/usr/lib/b2smartdata-3.6.0/conf
export SMARTDATA_CONF_DIR=/usr/lib/b2smartdata-3.6.0/conf
```

### 启动 JindoFS NameSpace
```
cd b2smartdata-x.x.x
sh sbin/start-namespace.sh
```
注：若 NameSpace 部署的节点为阿里云 ECS 节点，可以通过配置安全组，限制访问NameSpace的客户端。

## 配置 NameSpace
### 添加 NameSpace
JindoFS支持多命名空间，本文命名空间以 test-cache-ranger 为例, 在 b2smartdata-x.x.x/conf/bigboot.cfg 文件的 [bigboot-namespace] section 下添加下面配置。

（ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com）

```
jfs.namespaces = test-cache-ranger
jfs.namespaces.test-cache-ranger.oss.uri = oss://BUCKET.oss-cn-shanghai.aliyuncs.com/ranger-test
jfs.namespaces.test-cache-ranger.mode = cache
jfs.namespaces.test-cache-ranger.oss.access.key = ACCESS_KEY
jfs.namespaces.test-cache-ranger.oss.access.secret = ACCESS_SECRET
```
### 重启 JindoFS NameSpace 服务
```
sh sbin/stop-namespace.sh
sh sbin/start-namespace.sh
```

## 配置高可用 NameSpace
NameSpace 高可用部署需要3个节点分别启动Namespace服务组成1个Raft实例。
### 暂停SmartData所有服务
```
sh sbin/stop-namespace.sh
```
### 配置本地 raft 后端
在 bigboot.cfg 文件的 [bigboot-namespace] section 下对应的 namespace 配置项中添加下面配置项。

| 参数 | 描述 | 示例 |
| :--- | :--- | :--- |
| namespace.backend.type | 设置namespace后端存储类型，支持：<br /> rocksdb <br /> ots <br /> raft <br /> 默认为rocksdb。 | raft |
| namespace.backend.raft.initial-conf | 部署raft实例的3个NameSpace地址 | emr-header-1:8103:0,emr-header-2:8103:0,emr-header-3:8103:0 |

变更 bigboot.cfg 文件中 [bigboot-client] section 下的配置项。

| 参数 | 描述 | 示例 |
| :--- | :--- | :--- |
| client.namespace.rpc.address | Client端访问raft实例的3个NameSpace地址 | emr-header-1:8101,emr-header-2:8101,emr-header-3:8101 |

需要将 namespace.backend.raft.initial-conf 和 client.namespace.rpc.address 中的 hostname 替换为 NameSpace 服务所在的节点地址。

### 同步 b2smartdata-x.x.x 文件夹
完成上述配置后，将 b2smartdata-x.x.x 同步至另两台机器的同一目录

### 同步环境变量
将下面的环境变量同步至另外两台机器，以 b2smartdata-3.6.0 安装在 /usr/lib 为例：
```
export BIGBOOT_SMARTDATA_HOME=/usr/lib/b2smartdata-3.6.0/
export BIGBOOT_JINDOSDK_HOME=/usr/lib/b2smartdata-3.6.0/
export B2SDK_CONF_DIR=/usr/lib/b2smartdata-3.6.0/conf
export SMARTDATA_CONF_DIR=/usr/lib/b2smartdata-3.6.0/conf
```
### 启动 JindoFS NameSpace
在所有的 NameSpace 节点启动 NameSpace 服务。
```
sh sbin/start-namespace.sh
```
