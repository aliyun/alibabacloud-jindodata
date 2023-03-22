# 使用 JindoTable 命令实现 Hive 表和分区的数据治理

JindoTable 提供多个命令行命令，可以便捷查询 Hive 表和分区的基本信息或空间占用，
或者实现数据的迁移、归档。本文介绍这些命令行命令的用法。

## 前提条件
### 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [阿里云 OSS-HDFS 服务（JindoFS 服务）快速入门](/docs/user/4.x/4.6.x/4.6.5/jindofs/jindo_dls_quickstart.md) 。

### 已部署 Hadoop 与 Hive 环境

* 确保`hadoop classpath`能够返回合理结果

* 确保客户端环境变量 $HIVE_HOME 与 $HIVE_CONF_DIR 正确配置

## 使用说明

#### 获取帮助信息

执行以下命令，查看 JindoTable 支持的所有命令：
```
jindotable -help
```

如果想查看命令的具体用法，执行：
```
jindotable -help <cmd>
```
其中，``<cmd>`` 可以是 ``showTable``、``moveTo``、``archiveTable`` 等。

#### showTable 命令

```shell
jindotable -showTable -t <dbName.tableName>
```
其中 ``<dbName.tableName>`` 为表名称，格式为``库名.表名``

* 功能：如果是分区表，则展示所有分区；如果是非分区表，则返回表的存储情况。
* 示例：展示 ``db1.t1`` 分区表的所有分区。
```shell
jindo table -showTable -t db1.t1
```

#### showPartition 命令

```shell
jindotable -showPartition -t <dbName.tableName> -p <partitionSpec>
```
其中 ``<dbName.tableName>`` 为表名称，格式为``库名.表名``；
``<partitionSpec>``为分区名称，格式为``col1=v1,col2=v2``

* 功能：返回分区的存储情况。
* 示例：返回分区表``db1.t1``在``2020-10-12``日的存储情况。
```shell
jindo table -showPartition -t db1.t1 -p date=2020-10-12
```

#### listTables 命令

```shell
jindotable -listTables [-db] <dbName>
```
其中 ``<dbName>`` 为数据库名称，可省略，省略则为默认数据库。

* 功能：展示指定数据库中的所有表。不指定``-db``时默认展示``default``库中的表。
* 示例：列出数据库``db1``中的表。
```shell
jindo table -listTables -db db1
```

#### moveTo 命令

请参考 [使用 JindoTable 将 Hive 表和分区数据迁移到阿里云 OSS-HDFS 服务](/docs/user/4.x/4.6.x/4.6.5/oss/jindotable/table_moveto.md)

#### archiveTable/unarchiveTable 命令

请参考 [JindoTable 归档和解冻 OSS 上的表或分区](/docs/user/4.x/4.6.x/4.6.5/oss/jindotable/jindotable_archive.md)

#### cacheTable/uncacheTable 命令

仅支持配备 JindoFSx 存储加速系统的场景，请参考 [使用 JindoTable 缓存 Hive 表或分区的数据](/docs/user/4.x/4.6.x/4.6.5/jindofsx/jindotable/table_cache.md)