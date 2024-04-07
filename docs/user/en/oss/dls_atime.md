# Alibaba Cloud OSS-HDFS Service (JindoFS Service): AccessTime Usage Guide

## Introduction

OSS-HDFS supports AccessTime for files. By default, the precision of AccessTime is configured to one hour, controlled by the following configuration setting:
```properties
namespace.file.accesstime.precision.millisecond = 3600000
```
AccessTime precision refers to the threshold: if the difference between the current time and the file's current AccessTime exceeds this value, then the AccessTime is updated; otherwise, it remains unchanged. Setting too small a precision can lead to frequent updates of AccessTime, potentially impacting file read performance.

## Retrieving AccessTime

File AccessTime in OSS-HDFS can only be viewed through metadata export functionality. Please refer to the documentation on [Metadata Export](dls_dump_inventory_howto.md) for details on how to retrieve this information.