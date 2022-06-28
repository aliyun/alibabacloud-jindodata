# 快速部署一个简单的 JindoFSx 存储加速系统(仅 Namespace)

当无需进行数据缓存时，可以搭建一个仅包含 Namespace 服务的 JindoFSx 集群，而不需要部署 Storage 进程。

## 1. 下载 JindoFSx 包

下载最新的 Release 包 jindofsx-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 解压

```shell
tar -xzvf jindofsx-x.x.x.tar.gz
```

并将安装目录部署到 master 节点上。

## 3. 配置 jindofsx.cfg

在 jindofsx-x.x.x/conf 文件夹下修改配置文件 jindofsx.cfg。并将配置文件部署到 NamespaceService 所在节点。

```ini
[jindofsx-common]
logger.jnilogger=false # jni log 开关
metric.report.on = true # metrics 开关
logger.dir = /tmp/jindofsx-log # 日志目录

[jindofsx-namespace]# Namespace service 配置
namespace.rpc.port = 8101 # Namespace service 监听端口
namespace.meta-dir = /tmp/jindofsx/server # 元数据存放目录
```

必要配置说明：
1. `[jindofsx-common]` 中需配置日志、metrics、credentials、endpoint 相关信息，加速底层存储所需要的 credentials 和 endpoint 配置方法见相应 credential provider [配置文档](../security/jindofsx_credential_provider.md)。

## 4. 配置环境变量
以 jindofsx-4.4.0 安装在 `/usr/lib` 为例：

```shell
export JINDOFSX_CONF_DIR=/usr/lib/jindofsx-4.4.0/conf/
```

并确认部署到 Namespace 服务所在节点。

## 5. 启动 JindoFSx 缓存服务

在 master 节点执行以下脚本：

```shell
cd jindofsx-x.x.x
sh sbin/start-service.sh
```

执行完成后，master 节点将会启动 Namespace Service。

另外，可以通过以下命令停止所有缓存服务：

```shell
cd jindofsx-x.x.x
sh sbin/stop-service.sh
```

如需部署仅 Namespace 的高可用 JindoFSx 存储加速系统, 请参考 [部署高可用 JindoFSx 存储加速系统](../deploy/deploy_raft_ns.md)

## 6. 客户端配置

配置在所有节点的 Hadoop 配置文件的`core-site.xml`中。

```xml
<configuration>
    <property>
        <!-- Client端访问的Namespace地址 -->
        <name>fs.jindofsx.namespace.rpc.address</name>
        <value>hostname:8101</value>
    </property>
    <property>
        <name>fs.xengine</name>
        <value>jindofsx</value>
    </property>
</configuration>
```

注：`fs.jindofsx.namespace.rpc.address` 中的 hostname 为 Namespace 服务所在的节点地址。

可以用如下命令验证 JindoFSx 服务可用：

```shell
jindo admin -reportMetrics
```
