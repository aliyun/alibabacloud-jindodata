# Quick Start with JindoSDK in a Hadoop Environment

JindoSDK fully complies with the Hadoop FileSystem interface, offering better compatibility and usability while preserving the performance and cost benefits of Alibaba Cloud Object Storage Service (OSS).

It is currently compatible with most Hadoop versions and has been tested on versions 2.3 and above (earlier versions haven't been tested; please create a [new ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues/new) if you encounter any problems).

This document assumes you've already installed and deployed Hadoop in a Linux x86 environment. For other platforms, refer to [Deploying JindoSDK on Multiple Platforms](jindosdk_deployment_multi_platform.md).

# Steps

## Step 1: Install and Deploy JindoSDK

Download the latest tar.gz package `jindosdk-x.x.x.tar.gz` from the [download page](jindosdk_download.md).

For complete deployment instructions, see [Documentation Link](jindosdk_deployment_hadoop.md).

For minimal deployment, see [Documentation Link](jindosdk_deployment_lite_hadoop.md).

## Step 2: Access OSS with JindoSDK

Use Hadoop Shell commands to interact with OSS. Here are some common examples:

### Put Operation
```bash
hadoop fs -put <path> oss://<bucket>/
```

### List Operation
```bash
hadoop fs -ls oss://<bucket>/
```

### Make Directory Operation
```bash
hadoop fs -mkdir oss://<bucket>/<path>
```

### Remove Operation
```bash
hadoop fs -rm oss://<bucket>/<path>
```
![](https://docs.user/4.x/4.0.0/oss/pic/jindofs_sdk_cmd.png#center)

For more Hadoop shell commands, consult [Accessing OSS/OSS-HDFS via Hadoop Shell Commands](../oss/usages/oss_hadoop_shell.md).

## Step 3: Parameter Tuning

JindoSDK offers advanced tuning parameters. Refer to [JindoSDK Configuration Options](jindosdk_configuration.md) for configuration methods and available settings.