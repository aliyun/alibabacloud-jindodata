# JindoDistCp User Guide

## Introduction to JindoDistCp

JindoDistCp is a large-scale cluster internal and inter-cluster file copy utility developed by Alibaba Cloud's Data Lake Storage team. It employs MapReduce for file distribution, error handling, and recovery, using a list of files and directories as input for MapReduce tasks where each task handles a portion of the source list. Currently, it fully supports data copying scenarios between HDFS, OSS-HDFS, OSS, S3, COS, OBS, offering various customizable copy parameters and strategies. It particularly optimizes HDFS-to-OSS-HDFS data copying through a customized CopyCommitter for no-rename copying while ensuring data consistency. Functionality-wise, it aligns with S3 DistCp and HDFS DistCp but delivers enhanced performance.

## Environment Requirements

* JDK 1.8 or later
* Hadoop 2.3+ version; download the latest `jindo-distcp-tool-x.x.x.jar` from within the unzipped `jindosdk-${version}.tar.gz`, found in the `tools/` directory. On EMR clusters starting from EMR-5.6.0 or EMR-3.40.0, JindoDistCp is pre-installed in `/opt/apps/JINDOSDK/jindosdk-current/tools`.

## Parameters Explanation

JindoDistCp provides a jar package for usage, which can be run with `hadoop jar` command along with several parameters for migration operations.

| Parameter                        | Type     | Description                                                                                   | Default Value | Version Support | OSS | OSS-HDFS |
|----------------------------------|----------|-----------------------------------------------------------------------------------------------|---------------|-----------------|-----|----------|
| `--src <srcDir>`                 | Required | Source directory, accepts prefixes: hdfs://, oss://, s3://, cos://, obs://                  | N/A           | >=4.3.0         | Yes | Yes      |
| `--dest <destDir>`               | Required | Destination directory, accepts prefixes: hdfs://, oss://, s3://, cos://, obs://              | N/A           | >=4.3.0         | Yes | Yes      |
| `--bandWidth <bandWidth>`        | Optional | Bandwidth limit per node in MB                                                                | -1            | >=4.3.0         | Yes | Yes      |
| `--codec <codec>`                | Optional | Compression codec: gzip, gz, lzo, lzop, snappy, keep (to retain original compression)   | keep          | >=4.3.0         | Yes | Yes      |
| `--policy <policy>`              | Optional | Storage policy: Standard, IA, Archive, ColdArchive                                          | Standard      | >=4.3.0         | Yes | No       |
| `--filters <filters>`            | Optional | File containing filter rules                                                                  | N/A           | >=4.3.0         | Yes | Yes      |
| `--srcPrefixesFile <prefixes>`   | Optional | File containing matching file patterns                                                         | N/A           | >=4.3.0         | Yes | Yes      |
| `--parallelism <parallelism>`    | Optional | Number of maps used in MR job, equivalent to mapreduce.job.maps                                 | 10            | >=4.3.0         | Yes | Yes      |
| `--jobBatch <jobBatch>`          | Optional | Number of files processed per distcp job                                                       | 10000         | >=4.5.1         | Yes | Yes      |
| `--taskBatch <taskBatch>`        | Optional | Number of files handled per task                                                              | 1             | >=4.3.0         | Yes | Yes      |
| `--tmp <tmpDir>`                 | Optional | Temporary directory on HDFS                                                                   | /tmp          | >=4.3.0         | Yes | Yes      |
| `--hadoopConf <key=value>`       | Optional | Configuration properties                                                                      | N/A           | >=4.3.0         | Yes | Yes      |
| `--disableChecksum`              | Optional | Disable file checksum verification                                                             | false         | >=4.3.0         | Yes | Yes      |
| `--deleteOnSuccess`              | Optional | Delete source files after successful copy (akin to a mv operation)                               | false         | >=4.3.0         | Yes | Yes      |
| `--enableTransaction`            | Optional | Enable transactional guarantees across tasks                                                   | false         | >=4.3.0         | Yes | Yes      |
| `--ignore`                       | Optional | Ignore errors during data migration, report via JindoCounter (and CMS if enabled)                | false         | >=4.3.0         | Yes | Yes      |
| `--diff`                         | Optional | Show differences between source and destination files                                           | N/A           | >=4.3.0         | Yes | Yes      |
| `--update`                       | Optional | Perform incremental sync, skipping identical files and directories                               | N/A           | >=4.3.0         | Yes | Yes      |
| `--preserveMeta`                 | Optional | Preserve metadata (Owner, Group, Permission, Atime, Mtime, Replication, BlockSize, XAttrs, ACL)| N/A (OSS unsupported) | >=4.4.0         | N/A  | Yes      |
| `--enableCMS`                    | Optional | Enable Cloud Monitoring Service (CMS) alerts                                                   | false         | >=4.5.1         | Yes | Yes      |
| `--syncSourceDelete`             | Optional | Sets whether to synchronize deletion operations from the source subdirectories to the destination    | false           | >=6.9.0=        |  Yes  |  Yes  |

## Examples:
# Examples:

## 1\. Using `--src` and `--dest` (Mandatory)

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--src`: Specifies the path of the source file.
* `--dest`: Specifies the target file path.

Example command:
```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table
```
When you specify the `dest` path, it determines the hierarchy of copied files in the destination. If you need to copy files from `/data/hourly_table` into a directory named `hourly_table` within the `example-oss-bucket`, use the given command. Unlike Hadoop's DistCp, JindoDistCp includes all files under the root directory (`/data/hourly_table`) and does not include the root itself in the destination.

If you want to copy a single file, specify a directory as the destination:
```bash
hadoop jar jindo-distcp-tool-${version}.jar --src /test.txt --dest oss://example-oss-bucket/tmp
```

## 2\. Using `--bandWidth`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--bandWidth`: Limits bandwidth per node (in MB).

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --bandWidth 6
```

## 3\. Using `--codec`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

Original files typically enter OSS/OSS-HDFS uncompressed. This format is suboptimal for storage cost and analysis efficiency. JindoDistCp allows you to compress data efficiently online using the `--codec` option.

* `--codec`: Specify a compression codec.
    * Options: gzip, gz, lzo, lzop, snappy
    * none: Store as uncompressed file (if already compressed, it will be decompressed).
    * keep (default): Retain original compression.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --codec gz
```
Check the target folder after running the command; files should now be compressed with the gz codec.

If using LZO compression on an open-source Hadoop cluster, ensure that GPL libraries and hadoop-lzo packages are installed. Otherwise, consider using other compression methods.

## 4\. Using `--filters`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--filters`: Specify a file containing filtering rules.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --filters filter.txt
```
Assuming `filter.txt` contains:
```
.*test.*
```
Files with "test" in their paths won't be copied to OSS.

## 5\. Using `--srcPrefixesFile`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--srcPrefixesFile`: Specify a file containing matching file patterns.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --srcPrefixesFile prefixes.txt
```
Suppose `prefixes.txt` contains:
```
.*test.*
```
Only files with "test" in their paths will be copied to OSS.

## 6\. Using `--parallelism`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--parallelism`: Specify the number of maps used in MR job, similar to `mapreduce.job.maps`.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /opt/tmp --dest oss://example-oss-bucket/tmp --parallelism 20
```

## 7\. Using `--taskBatch`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--taskBatch`: Number of files handled per task (default: 10).

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --taskBatch 10
```

## 8\. Using `--tmp`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--tmp`: Specify a temporary directory on HDFS (default: `/tmp`).

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --tmp /tmp
```

## 9\. Configuring Access Key for OSS/OSS-HDFS

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

To access OSS/OSS-HDFS with your credentials, add them using the `--hadoopConf` option.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table \
--hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret
```
Alternatively, configure these keys in your Hadoop's core-site.xml.

## 10\. Using `--disableChecksum`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--disableChecksum`: Disables file checksum verification.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --disableChecksum
```

## 11\. Using `--deleteOnSuccess`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--deleteOnSuccess`: Deletes source files after successful copy (akin to a `mv` operation).

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --deleteOnSuccess
```

## 12\. Using `--enableTransaction`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| Not supported | v4.3.0 and above |

* `--enableTransaction`: Enables transactional guarantees across tasks.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --enableTransaction
```

## 13\. Using `--ignore`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--ignore`: Ignores errors during data migration (errors reported via JindoCounter).

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --ignore
```

## 14\. Using `--diff`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |

* `--diff`: Shows differences between source and destination files.

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --diff
```
A file containing differences will be generated locally if there are any.

### 15\. Using --update

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | v4.3.0 and above |


*  The --update flag enables incremental synchronization functionality. It skips identical files and directories, copying only new or modified content from the source (src) to the destination (dest). This feature comes in handy when a previous JindoDistCp job was interrupted and you want to resume copying without reprocessing already transferred files, or when new files have been added to the source since the last synchronization.

Here's an example command demonstrating how to use this flag:

```shell
 jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --update
```

## 16\. Using Cold Archiving, Archiving, and Infrequent Access Writing to OSS

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.3.0 and above | Not supported |

* `--update`: Write data in coldArchive, archive, or infrequent access modes to OSS.

1\. Cold archiving (coldArchive):
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-bucket/hourly_table --policy coldArchive --parallelism 20
```

2\. Archiving (archive):
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-bucket/hourly_table --policy archive --parallelism 20
```

3\. Infrequent Access (ia):
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-bucket/hourly_table --policy ia --parallelism 20
```
Default is standard storage if not specified.

## 17\. Using `--preserveMeta`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| Not supported | v4.4.0 and above |

* `--preserveMeta`: Preserves metadata (Owner, Group, Permission, Atime, Mtime, Replication, BlockSize, XAttrs, ACL).

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --preserveMeta
```

## 18\. Using `--jobBatch`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.5.1 and above | v4.5.1 and above |

* `--jobBatch`: Number of files processed per distcp job (default: 10000).

Example command:
```bash
jindo-distcp-tool-${version}.jar --src /data/hourly_table --dest oss://example-oss-bucket/hourly_table --jobBatch 50000
```

## 19\. Using `--enableCMS`

### Supported Versions:

| OSS | OSS-HDFS |
| --- | --- |
| v4.5.1 and above | v4.5.1 and above |

* `--enableCMS`: Enables Cloud Monitoring Service (CMS) alerts.

For more information on using CMS with JindoDistCp, refer to the [JindoDistCp CMS Alert Guide](jindodistcp_how_to_cms.md).

## 20\. Using `--syncSourceDelete`

### Supported Versions:

| OSS              | OSS-HDFS         |
|------------------|------------------|
| v6.9.0 and above | v6.9.0 and above |

*   `--syncSourceDelete` synchronizes file deletion operations in the source subdirectories and multi-level subdirectories. For example, if `--src` is specified as `/data`, then `/data/hourly_table` is a subdirectory.

## JindoDistCp Counters Explanation

| Counter Name          | Description                                                                                   |
|----------------------|-----------------------------------------------------------------------------------------------|
| COPY_FAILED          | Number of failed copies                                                                      |
| CHECKSUM_DIFF         | Files with differing checksums (counted in COPY_FAILED)                                         |
| FILES_EXPECTED       | Expected number of files to copy                                                              |
| BYTES_EXPECTED       | Expected number of bytes to copy                                                              |
| FILES_COPIED         | Number of successfully copied files                                                           |
| BYTES_COPIED         | Number of successfully copied bytes                                                           |
| FILES_SKIPPED        | Number of skipped files during incremental updates                                              |
| BYTES_SKIPPED        | Number of skipped bytes during incremental updates                                              |
| DIFF_FILES           | Number of different files between source and destination                                       |
| SAME_FILES           | Number of files that were exactly the same after checking                                       |
| DST_MISS             | Number of missing destination files (counted in DIFF_FILES)                                      |
| LENGTH_DIFF          | Number of files with differing lengths (counted in DIFF_FILES)                                   |
| CHECKSUM_DIFF         | Number of files with differing checksums (counted in DIFF_FILES)                                 |
| DIFF_FAILED          | Number of files with failed diff operations (check job logs for errors)                            |

Please note that the `--preserveMeta` option is not supported in OSS but works with OSS-HDFS starting from version 4.4.0.
