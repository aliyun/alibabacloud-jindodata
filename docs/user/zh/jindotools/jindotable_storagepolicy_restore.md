# 使用 JindoTable 工具实现 OSS-HDFS 结构化数据的冷热分层存储

## 概述

JindoTable 是基于 JindoSDK 的结构化数据治理工具，目标是在 “表” 或者 “分区” 层面提供便捷的数据访问和操作。使用 JindoTable 工具可以方便地进行数据迁移和分层存储。

本文介绍如何使用 JindoTable 工具对存储在 OSS-HDFS 上的结构化数据进行冷热分层存储。

## 获取

可以从 [JindoData 下载页面](../jindosdk/jindosdk_download.md) 获取 JindoTable 工具：
1. 下载 `jindosdk-<version>-<platform>.tar.gz`，根据系统选择合适的 `<platform>`。
2. 本文介绍的功能最低版本要求为 `<version>` 6.1.2。

注：
 - JindoTable 工具对 OSS-HDFS 的临时解冻（`-once`）支持设置解冻参数（`-d/-restoreDays <restore days>`），版本要求不低于 6.3.5 或 6.4.0。若对该功能有需要，请使用不低于 6.3.5 或 6.4.0 的版本。

## 基本用法

按照上面的步骤获取压缩包后，解压缩，便可在 `bin/` 目录下找到名为 `jindotable` 的脚本。执行脚本，即可使用 JindoTable 提供的各种命令。

执行 `jindotable -usage` 可以显示工具支持的各种命令。

一些注意事项：
 - 执行时，须保持解压缩后的目录结构。
 - 需要集群上有 Hadoop 和 Hive 环境，相关的环境变量已经被正确配置。

## 分层存储命令

支持 OSS-HDFS 结构化数据的冷热分层存储功能，命令为 `-setStoragePolicy`，执行 `jindotable -help setStoragePolicy` 可以显示帮助。

#### 参数说明

```
jindotable -setStoragePolicy -t <dbName.tableName> -std/-ia/-archive/-coldArchive/-once [-c "<condition>" | -fullTable] [-d/-restoreDays <restore days>] [-b/-before <before days>] [-p/-parallel <parallelism>] [-mr/-mapReduce] [-e/-explain] [-w/-workingDir <working directory>] [-l/-logDir <log directory>]
```

| 参数                                     | 描述                                                                                                                                                                                                                                       | 是否必选 |
|:---------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----|
| `-t <dbName.tableName>`                | 目标表名称，格式为数据库名.表名。数据库和表名之间以半角句号（.）分隔。表可以是分区表或非分区表。                                                                                                                                                                                        | 是    |
| `-std/-ia/-archive/-coldArchive/-once` | 目标存储方式。支持如下类型：-std：标准存储，-ia：低频存储，-archive：归档存储，-coldArchive：冷归档存储，-once：临时解冻。                                                                                                                                                            | 是    |
| `-c "<condition>" / -fullTable`        | `-fullTable`和`-c "<condition>"`只需提供一个，即要么指定`-c "<condition>"`，要么指定`-fullTable`。指定`-fullTable`时，则为移动整表，既可以是非分区表也可以是分区表。指定`-c "<condition>"`时，则提供了一个过滤条件，用来选择希望移动的分区，支持常见运算符，例如大于号（>）。例如，数据类型为String的分区ds，希望分区名大于 'd'，则代码为-c " ds > 'd' "。 | 是    |
| `-d/-restoreDays <restore days>`       | 指定临时解冻的天数，仅对临时解冻 `-once` 生效。                                                                                                                                                                                                             | 否    |
| `-b/before <before days>`              | 只有创建时间距离现在超过一定天数的表或分区才会被执行。                                                                                                                                                                                                              | 否    |
| `-p/-parallel <parallelism>`           | 命令执行的并行度。                                                                                                                                                                                                                                | 否    |
| `-mr/-mapReduce`                       | 使用Hadoop MapReduce而非本地多线程来执行命令。                                                                                                                                                                                                          | 否    |
| `-e/-explain`                          | 如果出现该选项，则为解释（explain）模式，只会显示待操作的分区列表，而不会真正执行命令。                                                                                                                                                                                          | 否    |
| `-w/-workingDir <working directory>`   | 只在MapReduce作业时使用，为MapReduce作业的工作目录。必须有读写权限，工作目录可以非空（作业执行过程中会创建临时文件，执行完毕会清理临时文件）。                                                                                                                                                         | 否    |
| `-l/-logDir <log directory>`           | 指定Log文件的目录。                                                                                                                                                                                                                              | 否    |

#### 注意事项

JindoTable 提供的上述功能基本对应于 OSS-HDFS 线上服务的分层存储能力，有下列事项需要注意：
1. 仅支持 OSS-HDFS 存储的表或者分区。对普通 OSS 数据的分层存储，使用 JindoTable 提供的 `archiveTable` 与 `unarchiveTable` 命令。
2. 成功执行 `-std/-ia/-archive/-coldArchive/-once` 指定的转换存储类型命令后，必须等待 `2` 天才可再次转换存储类型。 
3. 标准和低频类型无法临时解冻（`-once`）。归档、冷归档，或者已经处于解冻状态时，可以解冻，但如上所述，间隔须超过 `2` 天。 
4. 临时解冻命令返回后，数据仍不能立即可读，需要等待远端服务完成数据的解冻。对于归档数据，通常需要数分钟；对于冷归档数据，通常需要数小时。 
5. 临时解冻有期限限制，通过 `-d/restoreDays <restore days>` 指定，不指定则默认 `1` 天，到期后数据将回到不可读状态。该功能要求版本不低于 6.3.5 或 6.4.0。
