# 高可用 JindoFSx Namespace 配置和使用

## 服务端配置高可用 JindoFSx Namespace
JindoFSx Namespace 高可用部署需要 3 个节点分别启动 NamespaceService 服务组成 1 个 Raft 实例。

### 1. 暂停 JindoFSx 服务
```
cd jindofsx-x.x.x
sh sbin/stop-service.sh
```

### 2. 配置本地 raft 后端

变更配置文件 jindofsx.cfg 文件中 [jindofsx-namespace] section 下的配置项。

```
[jindofsx-namespace]# Namespace 配置
namespace.backend.type = raft #设置namespace后端存储类型，支持 rocksdb 和 raft, 默认为 rocksdb。
namespace.backend.raft.initial-conf = master-1-1:8103:0,master-1-2:8103:0,master-1-3:8103:0 #部署raft实例的3个Namespace地址
```

### 3. 配置 Namespace 地址
因为 3 个 Master 节点组成 1 个 Raft 实例，需更新 `jindofsx-x.x.x/sbin/headers` 文件，添加 3 个 Namespace 所在节点列表，例如：
```
master-1-1
master-1-2
master-1-3
```

### 4. 同步 jindofsx-x.x.x 文件夹
完成上述配置后，将 jindofsx-x.x.x 同步至另两台 Namespace 节点的同一目录。

### 5. 启动 JindoFSx 服务
```
cd jindofsx-x.x.x
sh sbin/start-service.sh
```

## 客户端配置

配置在所有节点的 Hadoop 配置文件的`core-site.xml`中。
```xml
<configuration>
    <property>
        <!-- Client端访问raft实例的3个Namespace地址 -->
        <name>fs.jindofsx.namespace.rpc.address</name>
        <value>master-1-1:8101,master-1-2:8101,master-1-3:8101</value>
    </property>
</configuration>
```

注：`fs.jindofsx.namespace.rpc.address` 中的 hostname 为 Namespace 服务所在的节点地址列表， 与`namespace.backend.raft.initial-conf`中的 hostname 一致。
