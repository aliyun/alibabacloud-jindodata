# Alibaba Cloud OSS-HDFS Service (JindoFS Service) Trash Usage Guide \(Supported Since JindoData 4.5.1\)

## Introduction

When files are deleted from the OSS-HDFS Service, they are not immediately erased. Instead, they are moved to the directory:
```bash
/user/<username>/.Trash/Current
```
After 30 minutes, the `Current` directory is relocated to:
```bash
/user/<username>/.Trash/<timestamp>
```
Files deleted during a specific time period are grouped into a timestamped directory, serving as a checkpoint. These directories are permanently deleted after **7 days**. Thus, within this timeframe, you can find and restore deleted files by moving them out of the `.Trash` directory.

Note: This feature relies on both client-side and server-side components. The server manages the scheduled cleanup of `/user/<username>/.Trash`, with this cleanup being enabled by default. Clients are responsible for moving files to be deleted into the `.Trash` directory.

## Using Trash with Hadoop FileSystem Shell

When running the Hadoop Shell command:
```bash
hadoop fs -rm oss://bucket/a/b/c
```
the trash feature is disabled by default. To enable it, add this configuration to your `core-site.xml`:
```xml
  <property>
    <name>fs.trash.interval</name>
    <value>1440</value>
  </property>
```
(The value should be greater than zero.) Now, the client automatically converts the `rm` command into:
```bash
hadoop fs -mv oss://bucket/a/b/c /user/<username>/.Trash/Current/a/b/c
```
The server handles cleanup. To bypass trash and immediately delete a file, use the `-skipTrash` parameter.

## Using Trash with Hadoop Ecosystem Components

Components like Hive, Spark, Flink don't natively recognize the OSS-HDFS Service trash feature. When using their FileSystem (HCFS) delete interfaces, files are deleted instantly. To leverage trash, explicitly call FileSystem's rename interface to move target files manually to:
```bash
/user/<username>/.Trash/Current
```
The OSS-HDFS Service then takes care of scheduled deletion, adopting a strategy similar to open-source Hadoop.