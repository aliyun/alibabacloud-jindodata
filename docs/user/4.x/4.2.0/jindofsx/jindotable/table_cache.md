# 使用 JindoTable 缓存 Hive 表或分区的数据

JindoTable 可以使用 JindoFSx 缓存系统缓存 Hive 表或分区的数据，从而提高访问速度。

## 前提条件
### 已部署 JindoFSx 缓存系统

关于如何部署 JindoFSx 缓存系统，请参考 [部署 JindoFSx（缓存系统)](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindofsx.md)

### 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindosdk.md)

### 已部署 Hadoop 与 Hive 环境

* 确保`hadoop classpath`能够返回合理结果

* 确保客户端环境变量 $HIVE_HOME 与 $HIVE_CONF_DIR 正确配置

## cache 命令

#### 功能

表示缓存指定表或分区的数据至集群本地磁盘上。

表或分区的路径需要位于 OSS 或 OSS-HDFS 服务。

#### 获取帮助信息

执行以下命令，获取帮助信息。

```
jindotable -help cache
```

#### 参数说明

```
jindotable -cache  [-pin] [-s] [-l] [-m] [-r replica] [-b batchsize] -t <dbName.tableName> [-p <partitionSpec>]
```

| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-t <dbName.tableName>` | 指定要缓存的表 | 是 |
| `-p <partitionSpec>` | 指定要缓存的分区，使用partitionCol1=1,partitionCol2=2,...的格式 | 否 |
| `-pin` | 在缓存空间不足时尽量不删除相关数据。 | 否 |
| `-s` | 是否同步 | 否 |
| `-l` | 是否是小文件 | 否 |
| `-m` | 是否在内存缓存 | 否 |
| `-r replica` | 缓存的副本数 | 否 |
| `-b batchsize` | 缓存的并发数 | 否 |

#### 示例

缓存2020-03-16日`db1.t1`表的数据至本地磁盘上。
```
jindotable -cache -t db1.t1 -p date=2020-03-16
```

## uncache 命令

#### 功能

表示删除集群本地磁盘上指定表或分区的缓存数据。

对应的路径需要位于 OSS 或 OSS-HDFS 服务。

#### 获取帮助信息

执行以下命令，获取帮助信息。

```
jindotable -help uncache
```

#### 参数说明

```
jindotable -uncache [-s] -t <dbName.tableName>  [-p <partitionSpec>]
```

| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-t <dbName.tableName>` | 指定要删除缓存的表 | 是 |
| `-p <partitionSpec>` | 指定要删除缓存的分区，使用partitionCol1=1,partitionCol2=2,...的格式 | 否 |

#### 示例

删除集群本地磁盘上表`db1.t2`的缓存数据。
```
jindotable -uncache -t db1.t2
```
删除集群本地磁盘上表`db1.t1`中指定分区的缓存数据。
```
jindotable -uncache -t db1.t1 -p date=2020-03-16,category=1
```