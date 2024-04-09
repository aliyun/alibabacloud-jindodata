# Using JindoFS CLI for Tiered Storage Management

## Overview

The JindoFS command-line tool is an executable program for interacting with OSS-HDFS, supporting common file metadata operations and reads/writes, as well as service-specific functionalities. For more details, instructions on obtaining and configuring the tool, refer to the [JindoFS Command-Line Tool User Guide](./jindofs_client_tools.md). To learn about the OSS-HDFS service, see the [OSS-HDFS Service Overview](https://help.aliyun.com/document_detail/405089.htm).

This article explains how to leverage the JindoFS command-line tool to manage tiered storage in the OSS-HDFS service, optimizing data governance and cost management.

## Storage Types

OSS-HDFS currently supports six storage types:

| Storage Type       | Description                                                                                           |
|--------------------|------------------------------------------------------------------------------------------------------|
| CLOUD_STD          | Standard storage, default type for hot data, highest storage cost                                      |
| CLOUD_IA           | Infrequent Access, colder data, directly accessible, lower storage cost, higher access cost             |
| CLOUD_AR           | Archive, coldest data, accessible only after temporary thaw, lowest storage cost                        |
| CLOUD_COLD_AR      | Cold Archive, even colder than Archive, accessible only after temporary thaw, very low storage cost     |
| CLOUD_AR_RESTORED  | Thawed Archive, has an expiration time                                                                |
| CLOUD_COLD_AR_RESTORED | Thawed Cold Archive, has an expiration time                                                   |

## Archiving Data

To initiate archiving data originally stored as "Standard" or "Infrequent Access," execute the following command:
```text
jindofs fs -setStoragePolicy -path <path> -policy <policy>
```
Where:
* `<path>` is a file or directory, similar to HDFS' `setStoragePolicy`.
* `<policy>` is the target storage type, `CLOUD_AR` for "Archive" and `CLOUD_COLD_AR` for "Cold Archive."

Upon successful execution, you'll receive:
```text
Successfully Set StoragePolicy for <path> with policy: <policy>
```

## Checking Archival Status

The previous command triggers a background archival job. Monitor its progress with:
```text
jindofs fs -checkStoragePolicy -path <path>
```
Possible return statuses include:
* `FINALIZED`: Completed
* `PENDING`: Pending
* `PROCESSING`: In Progress
* `SUBMITTED`: Submitted

Wait until the status is `FINALIZED`.

## Thawing Data

Archived data needs to be temporarily thawed before access. Trigger a thaw operation with:
```text
jindofs fs -setStoragePolicy -path <path> -policy <policy> -restoreDays <restoreDays>
```
Where:
* If data was originally archived (`CLOUD_AR`), `<policy>` should be `CLOUD_AR_RESTORED`; if cold archived (`CLOUD_COLD_AR`), it should be `CLOUD_COLD_AR_RESTORED`.
* `<restoreDays>` specifies the thaw duration. Archives allow 1-7 days, while cold archives allow 1-365 days. Default is 1 day.
* Check the thaw status with `jindofs fs -checkStoragePolicy -path <path>`.

Note the restrictions when restoring from archive or cold archive to infrequent access or standard storage:
* The total size of submitted restore requests cannot exceed 5TB, and the total size of data under processing cannot exceed 50TB.

Temporary thaw considerations:
* There must be at least a 2-day gap between archiving and thawing.
* Thawed data typically becomes readable within minutes for archives and hours for cold archives.
* Data remains in a thawed state for the specified `<restoreDays>`, after which it reverts to being inaccessible.
* While in a thawed state, another thaw can be initiated but with a minimum 2-day interval since the last one.