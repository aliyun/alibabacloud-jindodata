# 使用 JindoTable 将 Hive 表和分区数据迁移到阿里云 OSS-HDFS 服务

HDFS 受限于集群规模和成本因素，无法无限扩展，容量存在瓶颈。阿里云 OSS-HDFS 服务可以作为 HDFS 的替代或补充，扩展云上 Hadoop 平台的存储能力。
本工具用于把 Hive 数据根据分区键规则筛选，在 HDFS 和 阿里云 OSS-HDFS 服务之间转移分区。

## 前提条件
### 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [阿里云 OSS-HDFS 服务（JindoFS 服务）快速入门](/docs/user/4.x/4.0.0/jindofs/jindo_dls_quickstart.md) 。

### 已部署 Hadoop 与 Hive 环境

* 确保`hadoop classpath`能够返回合理结果

* 确保客户端环境变量 $HIVE_HOME 与 $HIVE_CONF_DIR 正确配置

## 配置 MoveTo 工具在 HDFS 下的锁目录：

在 Hadoop 配置文件 `core-site.xml` 或 `hdfs-site.xml`（任一即可，在`$HADOOP_CONF_DIR`目录下）新增配置项：`jindotable.moveto.tablelock.base.dir`

该配置的值应指向一个 HDFS 目录，目的是存放 MoveTo 工具在运行时自动创建的锁文件。需确保该目录只会被 MoveTo 工具访问，并且有访问权限。如果不配置，则使用缺省值 `hdfs:///tmp/jindotable-lock/`，无权限则报错。

## 使用说明

#### 获取帮助信息

执行以下命令，获取帮助信息。

```
jindotable -help moveTo
```

#### 参数说明

```shell
jindotable -moveTo -t <dbName.tableName> -d <destination path> [-c "<condition>" | -fullTable] [-b/-before <before days>] [-p/-parallel <parallelism>] [-s/-storagePolicy <OSS storage policy>] [-o/-overWrite] [-r/-removeSource] [-skipTrash] [-e/-explain] [-q/-queue <yarn queue>] [-w/-workingDir <working directory>][-l/-logDir <log directory>]
```

| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-t <dbName.tableName>` | 要移动的表。 | 是|
| `-d <destination path>` | 目标路径，为表级别的路径，分区路径会在这个路径下自动创建。 | 是 |
| `-c "<condition>" / -fullTable` | 分区过滤条件表达式，支持基本运算符，不支持udf。 | 否 |
| `-b/before <before days>` | 根据分区创建时间，创建时间超过给定天数的分区才进行移动。 | 否 |
| `-p/-parallel <parallelism>` | 整个moveTo任务的最大task并发度，默认为1。 | 否 |
| `-s/-storagePolicy <OSS storage policy>` | OSS-HDFS 服务不支持该参数 | 否 |
| `-o/-overWrite` | 是否覆盖最终目录。分区级别覆盖，不会覆盖本次移动不涉及的分区。 | 否 |
| `-r/-removeSource` | 是否在移动完成后删除源路径。 | 否 |
| `-skipTrash` | 如果删除源路径，是否跳过Trash。 | 否 |
| `-e/-explain`| 如果指定explain模式，不会触发实际操作，仅打印会同步的分区。 | 否 |
| `-q/-queue <yarn queue` | 指定分布式拷贝的yarn队列。 | 否 |
| `-w/-workingDir` | 指定分布式拷贝的工作临时目录。 | 否 |
| `-l/-logDir <log directory>` | 本地日志目录，默认为`/tmp/<current user>/` | 否 |