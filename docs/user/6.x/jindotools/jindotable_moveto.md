# 使用 JindoTable 将 Hive 表和分区数据迁移到 OSS/OSS-HDFS

HDFS 受限于集群规模和成本因素，无法无限扩展，容量存在瓶颈。云厂商提供的 OSS/OSS-HDFS 可以作为 HDFS 的替代或补充，扩展云上 Hadoop 平台的存储能力。
本工具用于把 Hive 数据根据分区键规则筛选，在 HDFS 和 OSS/OSS-HDFS 之间转移分区。

## 前提条件
### 已部署 JindoSDK

*   EMR 环境中，默认已安装 JindoSDK，可以直接使用。注意：

    *   访问 OSS-HDFS，需创建EMR-3.42.0及以上版本或EMR-5.8.0及以上版本的集群。

*   非 EMR 环境，请先安装部署 JindoSDK。部署方式请参考 [《在 Hadoop 环境中部署 JindoSDK》](../jindosdk/jindosdk_deployment_hadoop.md)。

    *   访问 OSS-HDFS，需部署 JindoSDK 4.x 及以上版本。

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
| `-s/-storagePolicy <OSS storage policy>` | 拷贝至 OSS 时数据文件的存储策略，支持 Standard（默认）、IA、Archive、ColdArchive。 | 否 |
| `-o/-overWrite` | 是否覆盖最终目录。分区级别覆盖，不会覆盖本次移动不涉及的分区。 | 否 |
| `-r/-removeSource` | 是否在移动完成后删除源路径。 | 否 |
| `-skipTrash` | 如果删除源路径，是否跳过Trash。 | 否 |
| `-e/-explain`| 如果指定explain模式，不会触发实际操作，仅打印会同步的分区。 | 否 |
| `-q/-queue <yarn queue` | 指定分布式拷贝的yarn队列。 | 否 |
| `-w/-workingDir` | 指定分布式拷贝的工作临时目录。 | 否 |
| `-iceberg` | 指定所迁移表为Iceberg。目前仅支持Iceberg整表迁移，需配合`-fullTable`使用。(6.3.0以上版本) | 否 |
| `-l/-logDir <log directory>` | 本地日志目录，默认为`/tmp/<current user>/` | 否 |

## 使用示例

1. 有一个 HDFS 上的 Hive 分区表，如下图所示：

![image.png](pic/jindotable_moveto_oss_1.png)

2. 想把 bbb 和 ccc 分区移动到 OSS，先用 explain 模式看看会移动的分区是否符合预期，参数为 -e 或 -explain：

![image.png](pic/jindotable_moveto_oss_2.png)

3. 去掉参数 -e，真正移动分区：

![image.png](pic/jindotable_moveto_oss_3.png)

4. 执行完成后，检查数据，数据已经在 OSS：

![image.png](pic/jindotable_moveto_oss_4.png)

5. 再移回 HDFS，结果失败了，会显示失败原因，原来是目标目录非空，提示可以使用 -overWrite 清空目标目录：

![image.png](pic/jindotable_moveto_oss_5.png)

6. 使用 -overWrite 强制覆盖：

![image.png](pic/jindotable_moveto_oss_6.png)

## 异常状态处理

为了尽可能保证数据安全，防止污染，该命令会自动检查目的地目录，确保不存在另一命令向相同目录拷贝。
如果无法排除，则该表或分区的拷贝命令将执行失败。
此时，需要主动干预，确保没有另一命令正在执行拷贝之后，清理目的地目录，则可以重新执行该命令。

对于非分区表的拷贝或迁移，目的地目录即为表级别目录；对于分区表，目的地目录为待拷贝或移动分区的分区级目录，
只需对待拷贝的分区进行清理。

#### 出现原因

如果该命令被异常中止，则可能出现需要主动干预的情况。此时拷贝还未完成，源数据与表的元信息均未改变，
数据仍处于安全状态。常见的异常中止有两种情况：
* 用户在命令尚未结束时，主动杀掉命令进程
* 由于内存溢出等异常，进程异常中止

如果拷贝表或分区时拷贝失败并显示 ``Conflicts found``，则属于异常状态，需主动干预。

#### 处理方法

* 确保没有另一命令向相同的目标路径拷贝数据。特别是，确保没有 DistCp/JindoDistCp 等分布式拷贝命令。
* 删除目的地目录。对于非分区表，删除表一级目录。对于分区表，删除存在冲突的分区级目录。切勿删除源目录。