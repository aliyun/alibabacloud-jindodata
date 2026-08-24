# Asynchronous Content Summary Retrieval for OSS-HDFS

## Overview

JindoSDK supports asynchronous `getContentSummary` requests. This document describes how to enable asynchronous `getContentSummary` on both the server and client sides.

## Prerequisites

JindoSDK 6.10.7 or later is installed.

## Enable Asynchronous `getContentSummary` on the Server

Use the [JindoFS command-line tool](./jindofs_client_tools.md) to run the following command and enable asynchronous `getContentSummary` on the server:

```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf namespace.get-content-summary.async.enabled=true
```

Run the following command to verify that the configuration has taken effect:

```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name namespace.get-content-summary.async.enabled
```

## Enable Asynchronous `getContentSummary` on the Client

The following configuration property controls asynchronous `getContentSummary` on the client:

| Property | Type | Default | Description |
|:---|:---|:---|:---|
| `fs.oss.getContentSummaryAsync.enable` | Boolean | `false` | Set this property to `true` to enable asynchronous `getContentSummary` on the client. |

### JindoFS CLI Client

Add the following configuration to the `jindosdk.cfg` file in the directory specified by the `JINDOSDK_CONF_DIR` environment variable:

```ini
[jindosdk]
fs.oss.getContentSummaryAsync.enable = true
```

### JindoSDK Client

Add the following configuration to the Hadoop `core-site.xml` file:

```xml
<configuration>
    <property>
        <name>fs.oss.getContentSummaryAsync.enable</name>
        <value>true</value>
    </property>
</configuration>
```

After asynchronous `getContentSummary` is enabled on both the server and client sides, calls to the `getContentSummary` API use the asynchronous workflow.
