# Using JindoTable for Hierarchical Storage Management of Structured Data on OSS-HDFS

## Introduction

JindoTable is a structured data management tool built on top of JindoSDK, aiming to provide easy data access and manipulation at the "table" or "partition" level. With JindoTable, you can conveniently migrate and manage hierarchical storage for structured data stored on OSS-HDFS.

This guide explains how to use JindoTable to implement hierarchical storage management for structured data on OSS-HDFS.

## Acquisition

You can obtain JindoTable from the [JindoData Download Page](../jindosdk/jindosdk_download.md):

1. Download `jindosdk-${version}-${platform}.tar.gz` according to your system platform.
2. The minimum required version for this feature is `${version}` 6.1.2.

## Basic Usage

After downloading and decompressing the tarball, you'll find the `jindotable` script in the `bin/` folder. Execute the script to utilize the various commands provided by JindoTable.

Run `jindotable -usage` to view all available commands.

Some considerations:
- Maintain the extracted directory structure when executing.
- Make sure your cluster has Hadoop and Hive environments with their respective environment variables properly configured.

## Hierarchical Storage Management Command

JindoTable supports hierarchical storage management for structured data on OSS-HDFS with the `-setStoragePolicy` command. Run `jindotable -help setStoragePolicy` to display the help for this command.

#### Command Parameters

```shell
jindotable -setStoragePolicy -t <dbName.tableName> -std/-ia/-archive/-coldArchive/-once [-c "<condition>" | -fullTable] [-d/-restoreDays <restore days>] [-b/-before <before days>] [-p/-parallel <parallelism>] [-mr/-mapReduce] [-e/-explain] [-w/-workingDir <working directory>] [-l/-logDir <log directory>]
```

| Parameter                                    | Description                                                                                                                                                                                                                           | Required |
|--------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:------:|
| `-t <dbName.tableName>`                    | Target table name, formatted as database_name.table_name. Can be a non-partitioned or partitioned table.                                                                                                                                                 |  Yes   |
| `-std/-ia/-archive/-coldArchive/-once`     | Target storage tier. Supports: -std (standard), -ia (infrequent access), -archive (archival), -coldArchive (cold archival), -once (temporary thaw).                                                                                             |  Yes   |
| `-c "<condition>" / -fullTable`            | Either `-fullTable` or `-c "<condition>"`, not both. `-fullTable` moves the whole table, while `-c "<condition>"` specifies a filter condition for partitions (using basic operators). For example, if column ds is of string type and you want partitions greater than 'd', use `-c "ds > 'd'"`. |  Yes   |
| `-d/-restoreDays <restore days>`           | Specifies duration for temporary thaw (-once), applies only to `-once`.                                                                                                                                                                |   No   |
| `-b/before <before days>`                  | Only tables/partitions created before a certain number of days ago will be executed.                                                                                                                                                        |   No   |
| `-p/-parallel <parallelism>`               | Degree of parallelism for command execution.                                                                                                                                                                                              |   No   |
| `-mr/-mapReduce`                           | Use Hadoop MapReduce instead of local multi-threading for command execution.                                                                                                                                                               |   No   |
| `-e/-explain`                              | If present, runs in explain mode, displaying the list of partitions that would be operated on without actually executing the command.                                                                                                                  |   No   |
| `-w/-workingDir <working directory>`       | Used only in MapReduce jobs as the working directory (must have read-write permission). Temporary files will be created and cleaned up upon job completion.                                                                                      |   No   |
| `-l/-logDir <log directory>`               | Specifies a directory for log files.                                                                                                                                                                                                    |   No   |

#### Considerations

JindoTable's mentioned functionality closely aligns with the hierarchical storage management capabilities offered by the online OSS-HDFS service. Here are some points to note:
1. Supports only tables or partitions with OSS-HDFS storage. For hierarchical storage management of regular OSS data, use JindoTable's `archiveTable` and `unarchiveTable` commands.
2. After successfully executing a `-std/-ia/-archive/-coldArchive/-once` storage type conversion command, wait at least `2` days before converting again.
3. Standard and infrequent access tiers cannot be temporarily thawed with `-once`.
4. Archival or cold archival tiers, or those already thawed, can be thawed but with a minimum interval of `2` days.
5. Once a temporary thaw command returns, data may not immediately become readable; it takes a few minutes for archived data and hours for cold archived data to complete the thaw process.
6. Temporary thaw lasts until a predefined period specified by `-d/restoreDays <restore days>` (default is `1` day) after which data reverts to an unreadable state.