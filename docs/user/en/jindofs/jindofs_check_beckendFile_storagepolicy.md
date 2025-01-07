# Checking Archive/Restore Task Status using JindoFS Command Line Tool

## Overview

The JindoFS command-line tool is an executable program for interacting with OSS-HDFS, supporting common file metadata operations and reads/writes, as well as service-specific functionalities. For more details, instructions on obtaining and configuring the tool, refer to the [JindoFS Command-Line Tool User Guide](./jindofs_client_tools.md). To learn about the OSS-HDFS service, see the [OSS-HDFS Service Overview](https://help.aliyun.com/document_detail/405089.htm).

This document explains how to use the JindoFS command line tool to check the status of archive/restore tasks for files/directories in OSS-HDFS.

## Introduction to Archive/Restore Tasks

The JindoFS command line tool provides commands to implement cold-hot tiered storage (see [JindoFS Command Line Tool Implementation of Cold-Hot Tiered Storage](./jindofs_storagepolicy_restore.md)). Note that archive/restore tasks are executed asynchronously and may take some time before results are visible.

Archive tasks are completed by OSS lifecycle rules. First, the OSS-HDFS service tags object files with specific tags; then, the OSS lifecycle periodically changes the storage state of objects with these tags. After all object files have their storage states changed, the archiving task is considered complete.

Restore tasks can be either temporary or permanent. Temporary restoring converts archived files into a readable state, which takes some time. After a specified period, temporary restoring expires, and the file becomes unreadable again.

Permanent restoring first temporarily restores the file, making it readable. Once temporary restoring is complete, the storage state of the OSS object file is converted to standard through a COPY operation.

## Checking Archive Task Status

For files originally stored in "Standard" type, after triggering archiving, you can query the archive task status with the following command:
```text
jindofs fs -checkBeStoragePolicy -path <path>
```
Here, `<path>` can be a file or directory.

If the archiving task is in the tagging phase, it will return the following message:
```text
The status storage policy transfer job for <path> [Standard->Archive] : SETTING_TAG
```
If the archiving task has been tagged but not yet completed, it will return the following message:
```text
The status storage policy transfer job for <path> [Standard->Archive] : WAITTING_LIFECYCLE
```
If the archiving task is complete, it will return the following message:
```text
The status storage policy transfer job for <path> : FINISHED
```

## Checking Temporary Restore Task Status

For files originally stored in "Archive" type, after triggering temporary restoring, you can query the archive task status with the following command:
```text
jindofs fs -checkBeStoragePolicy -path <path>
```
Here, `<path>` can be a file or directory.

If the temporary restore task has not started or has expired, it will return the following message:
```text
The status storage policy transfer job for <path> [Archive->ArchiveRestored] : PENDING
```
If the temporary restore task is in progress, it will return the following message:
```text
The status storage policy transfer job for <path> [Archive->ArchiveRestored] : RESTORING
```
If the temporary restore task is complete, it will return the following message:
```text
The status storage policy transfer job for <path> : FINISHED
```

## Checking Permanent Restore Task Status

For files originally stored in "Archive" type, after triggering permanent restoring, you can query the archive task status with the following command:
```text
jindofs fs -checkBeStoragePolicy -path <path>
```
Here, `<path>` can be a file or directory.

If the permanent restore task is in the pre-temporary restore stage, it will return the following message:
```text
The status storage policy transfer job for <path> [Archive->Standard] : PENDING
```
If the permanent restore task is in the temporary restore execution stage, it will return the following message:
```text
The status storage policy transfer job for <path> [Archive->Standard] : RESTORING
```
If the permanent restore task is in the copy stage, it will return the following message:
```text
The status storage policy transfer job for <path> [Archive->Standard] : COPYING
```
If the permanent restore task is complete, it will return the following message:
```text
The status storage policy transfer job for <path> : FINISHED
```

## Checking Archive/Restore Task Status for Directories

For directories, the jidnofs command line tool recursively checks the archive/restore task status of all files under the directory. When subdirectories/subfiles do not match the workflow of the specified directory, the jindofs command line tool skips checking them and returns the following message in the output result:
```text
The status storage policy transfer job for <path> : FINISHED
Files under [<path>/subDir] are ignored as their storage policy were set separately. You can check them individually.
```
In this case, you can check the `<path>/subDir` directory separately.