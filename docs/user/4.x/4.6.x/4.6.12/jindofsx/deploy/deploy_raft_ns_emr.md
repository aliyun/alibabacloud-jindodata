# 在 EMR 集群中配置高可用 JindoFSx Namespace

## 服务端配置高可用 JindoFSx Namespace
JindoFSx Namespace 高可用部署需要 3 个节点分别启动 NamespaceService 服务组成 1 个 Raft 实例。

### 2. 配置本地 raft 后端

#### 2.1 在 JindoData 服务页面选择`配置` > `namespace` > `新增配置项`，更新如下参数。

| 参数             | 值              | 说明                                                |                                           
| ----------------------------------- |--------|---------------------------------------------------|
| namespace.backend.type  | raft            | 设置namespace后端存储类型，支持 rocksdb 和 raft, 默认为 rocksdb。 |
| namespace.backend.raft.initial-conf  | master-1-1:8103:0,master-1-2:8103:0,master-1-3:8103:0 | 部署raft实例的3个Namespace地址。                           |

#### 2.2 重启 JindoFSx Namespace 服务。

## 客户端配置

在 Hadoop-Common 页面选择`配置` > `core-site.xml`， 更新如下参数。
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