# JindoTable 归档和解冻 OSS 上的表或分区

JindoTable 提供 archiveTable 和 unarchiveTable 命令，可以进行归档和解冻 OSS 上的表或分区的操作。本文为您介绍 archiveTable 和 unarchiveTable 命令的使用方法。

## 前提条件

* 待归档的数据必须是表数据（可以是分区表或非分区表），且已经位于阿里云对象存储 OSS。

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [JindoSDK + 阿里云 OSS 快速入门](/docs/user/4.x/4.6.x/4.6.9/oss/jindosdk_quickstart.md) 。

## archiveTable 命令

archiveTable 命令可以对 OSS 上的表或分区进行归档。

#### 获取帮助信息

执行以下命令，获取帮助信息。

```
jindotable -help archiveTable
```

#### 参数说明

```
jindotable -archiveTable -t <dbName.tableName> -i/-a/-ca [-c "<condition>" | -fullTable] [-b/-before <before days>] [-p/-parallel <parallelism>] [-mr/-mapReduce] [-e/-explain] [-w/-workingDir <working directory>][-l/-logDir <log directory>]
```


| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-t <dbName.tableName>` | 待归档的表名称，格式为数据库名.表名。数据库和表名之间以半角句号（.）分隔。表可以是分区表或非分区表。 | 是|
| `-i/-a/-ca` | 目标存储方式。支持如下方式：-a：归档（Archive）存储。-i：低频 （Infrequent Access，IA）存储。-ca：冷归档（Code Archive）存储。如果使用-i即表示低频存储，会跳过已经处于归档存储的文件。 | 是 |
| `-c "<condition>" / -fullTable` | `-fullTable`和`-c "<condition>"`只需提供一个，即要么指定`-c "<condition>"`，要么指定`-fullTable`。指定`-fullTable`时，则为移动整表，既可以是非分区表也可以是分区表。指定`-c "<condition>"`时，则提供了一个过滤条件，用来选择希望移动的分区，支持常见运算符，例如大于号（>）。例如，数据类型为String的分区ds，希望分区名大于 'd'，则代码为-c " ds > 'd' "。 | 否 |
| `-b/before <before days>` | 只有创建时间距离现在超过一定天数的表或分区才会被归档。 | 否 |
| `-p/-parallel <parallelism>` | 归档操作的并行度。 | 否 |
| `-mr/-mapReduce` | 使用Hadoop MapReduce而非本地多线程来归档数据。| 否 |
| `-e/-explain`| 如果出现该选项，则为解释（explain）模式，只会显示待移动的分区列表，而不会真正移动数据。 | 否 |
| `-w/-workingDir` | 只在MapReduce作业时使用，为MapReduce作业的工作目录。必须有读写权限，工作目录可以非空（作业执行过程中会创建临时文件，执行完毕会清理临时文件）。 | 否 |
| `-l/-logDir <log directory>` | 指定Log文件的目录。 | 否 |


## unarchiveTable 命令

unarchiveTable 命令与 archiveTable 命令格式基本一致，但效果相反。unarchiveTable 命令可以对 OSS 上的表或分区进行解冻。

#### 获取帮助信息

执行以下命令，获取帮助信息。

```
jindotable -help unarchiveTable
```

#### 参数说明

```
jindotable -unarchiveTable -t <dbName.tableName> [-i/-a/-o/-cr] [-notWait] [-c "<condition>" | -fullTable] [-b/-before <before days>] [-p/-parallel <parallelism>] [-mr/-mapReduce] [-e/-explain] [-w/-workingDir <working directory>][-l/-logDir <log directory>]
```

| 参数 | 描述 | 是否必选 |
| :--- | :--- | :--- |
| `-t <dbName.tableName>` | 待解冻的表名称，格式为数据库名.表名。数据库和表名之间以半角句号（.）分隔。表可以是分区表或非分区表。 | 是|
| `-i/-a/-o/-cr` | 如果不指定参数，则转换存储格式为标准（Standard）存储。支持如下方式：-i：则转换存储格式为低频（Infrequent Access，IA）存储，原本为标准存储的文件被跳过。-a：则转换存储格式归档（Archive）存储。-o: 仅做解冻（Restore）操作。原本为标准存储或低频存储的文件均被跳过。已经处于解冻状态的文件也会被跳过，即不会重复解冻。-cr：检查是否所有 restore 任务已完成。| 否 |
| `-notWait` | 只对 Restore 操作生效。如果出现该选项，则不会等 OSS 服务端完成 Restore 操作。否则，会等 Restore 操作完成或者超时（10分钟）才退出 | 否 |
| `-c "<condition>" / -fullTable` | `-fullTable`和`-c "<condition>"`只需提供一个，即要么指定`-c "<condition>"`，要么指定`-fullTable`。指定`-fullTable`时，则为移动整表，既可以是非分区表也可以是分区表。指定`-c "<condition>"`时，则提供了一个过滤条件，用来选择希望移动的分区，支持常见运算符，例如大于号（>）。例如，数据类型为String的分区ds，希望分区名大于 'd'，则代码为-c " ds > 'd' "。 | 否 |
| `-b/before <before days>` | 只有创建时间距离现在超过一定天数的表或分区才会被解冻。 | 否 |
| `-p/-parallel <parallelism>` | 解冻操作的并行度。 | 否 |
| `-mr/-mapReduce` | 使用Hadoop MapReduce而非本地多线程来解冻数据。| 否 |
| `-e/-explain`| 如果出现该选项，则为解释（explain）模式，只会显示待移动的分区列表，而不会真正移动数据。 | 否 |
| `-w/-workingDir` | 只在MapReduce作业时使用，为MapReduce作业的工作目录。必须有读写权限，工作目录可以非空（作业执行过程中会创建临时文件，执行完毕会清理临时文件）。 | 否 |
| `-l/-logDir <log directory>` | 指定Log文件的目录。 | 否 |