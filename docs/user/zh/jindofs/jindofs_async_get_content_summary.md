# OSS-HDFS 异步获取内容摘要

## 概述

JindoSDK 支持异步执行 `getContentSummary` 请求。本文介绍如何开启服务端和客户端的异步 `getContentSummary` 功能。

## 前提条件

JindoSDK 版本为 6.10.7 及以上。

## 开启服务端异步 `getContentSummary` 功能

使用 [JindoFS 命令行工具](./jindofs_client_tools.md)，执行以下命令，开启服务端异步 `getContentSummary` 功能：

```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf namespace.get-content-summary.async.enabled=true
```

执行以下命令，检查配置是否生效：

```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name namespace.get-content-summary.async.enabled
```

## 开启客户端异步 `getContentSummary` 功能

客户端异步 `getContentSummary` 功能对应的配置项如下：

| 配置项 | 类型 | 默认值 | 说明 |
|:---|:---|:---|:---|
| `fs.oss.getContentSummaryAsync.enable` | Boolean | `false` | 设置为 `true`，开启客户端异步 `getContentSummary` 功能。 |

### JindoFS CLI 客户端

在 `JINDOSDK_CONF_DIR` 环境变量指定目录下的 `jindosdk.cfg` 文件中新增以下配置：

```ini
[jindosdk]
fs.oss.getContentSummaryAsync.enable = true
```

### JindoSDK 客户端

在 Hadoop 的 `core-site.xml` 文件中新增以下配置：

```xml
<configuration>
    <property>
        <name>fs.oss.getContentSummaryAsync.enable</name>
        <value>true</value>
    </property>
</configuration>
```

服务端和客户端的异步 `getContentSummary` 功能均开启后，调用 `getContentSummary` 接口时会执行异步流程。
