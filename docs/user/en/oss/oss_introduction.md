# Overview of OSS and OSS-HDFS

## Overview

Alibaba Cloud Object Storage Service (OSS) is a massive, secure, cost-effective, and highly reliable cloud storage service offering 99.9999999999% (12 nines) data durability and 99.995% data availability. It provides various storage classes for optimized costs. Learn more in the [What Is Object Storage OSS](https://help.aliyun.com/document_detail/31817.html) documentation.

The OSS-HDFS Service (also known as JindoFS Service) is a native cloud data lake storage product. With unified metadata management, it offers full compatibility with the HDFS file system interface while providing POSIX capabilities to better serve big data and AI scenarios in data lakes. Read more about it in the [Overview of OSS-HDFS Service](https://help.aliyun.com/document_detail/405089.html).

JindoData is an open-source big data acceleration suite developed by Alibaba Cloud's Big Data team. Targeting big data and AI ecosystems, it provides comprehensive acceleration solutions for major cloud and industry data lake storage systems. Built on a unified architecture and kernel, it includes JindoFS storage system (originally JindoFS Block mode), JindoFSx storage accelerator (formerly JindoFS Cache mode), JindoSDK universal big data SDK, and ecosystem tools (such as JindoFuse and JindoDistCp) with plugin support. For more details, see the [JindoData Overview](/README.md).

# Usage

In an EMR environment, JindoSDK is pre-installed, so you can directly access OSS/OSS-HDFS through it.

Outside of EMR environments, you can download the latest version of JindoSDK and deploy it yourself. Deployment instructions for Hadoop scenarios are available in [Deploying JindoSDK in a Hadoop Environment](../jindosdk/jindosdk_deployment_hadoop.md), while those for AI scenarios can be found in [Deploying JindoSDK in an AI Environment](../jindosdk/jindosdk_deployment_ai.md).

# Advantages

Using OSS/OSS-HDFS as your underlying storage offers these benefits:

*   Plug-and-play convenience. Both services are cloud-native and provide RESTful APIs; no deployment is needed. In Alibaba Cloud EMR clusters, JindoSDK is installed by default.

*   Cost savings. Storing data in OSS/OSS-HDFS can reduce costs, and combining it with low-frequency, archive, or cold-archive options further optimizes storage expenses for cold data.

*   Scalability. Both services offer better scalability without constraints imposed by hard drive capacity and eliminate manual expansion requirements.


# Features

Here's a comparison of features when using OSS/OSS-HDFS through JindoSDK:

|Scenario| Supported Feature| OSS| OSS-HDFS|
|---|---|---|---|
|Big Data (Hadoop)| Directory/file semantics and operations| Supported| Supported|
|| Add directory/file permissions| Not supported| Supported|
|| Atomic directory creation/rename, rename performance| Supported but slow| Supported, millisecond-level|
|| Set timestamps with setTimes| Not supported| Supported|
|| Extended attributes (XAttrs)| Not supported| Supported|
|| Access Control Lists (ACLs)| Not supported| Supported|
|| Local read cache acceleration| Supported| Supported|
|| Snapshots (Snapshot)| Not supported| Supported|
|| File append/flush/sync operations| Not supported| Supported|
|| File truncate| Not supported| Supported|
|| Checksums| Supported| Supported|
|| HDFS Trash automatic cleanup| Not supported| Supported|
|AI (POSIX)| Metadata consistency| Weak| Strong|
|| File append/flush/sync operations| Supported with [usage restrictions](https://help.aliyun.com/document_detail/31981.html)| Supported|
|| File truncate operation| Not supported| Supported|
|| Random writes| Not supported| Supported|