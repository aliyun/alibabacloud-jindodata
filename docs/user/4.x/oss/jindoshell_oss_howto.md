# JindoShell CLI 支持阿里云 OSS 使用说明
JindoShell CLI 支持阿里云 OSS archive, unarchive, restore 等命令。

## 前提条件
### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

### 2. 配置环境变量

* 配置`JINDOSDK_HOME`

解压下载的安装包，以安装包内容解压在`/usr/lib/jindosdk-4.0.0`目录为例：
```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-4.0.0
```
* 配置`HADOOP_CLASSPATH`

```bash
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```

## Archive 命令
Archive命令可以归档OSS上的数据至低频访问存储或者归档存储上。

```
jindo oss -archive [-i|c] <path>
```

-i参数可以归档数据至OSS低频存储类型。-c参数可以归档数据至OSS冷归档存储类型。默认可以归档数据至OSS归档存储类型。

## Unarchive 命令
Unarchive命令可以将数据从归档存储类型恢复到低频存储或者标准存。

```
jindo oss -unarchive [-i|a] <path>
```

-i参数可以恢复数据至OSS低频存储类型。-a参数可以恢复数据至OSS归档存储类型。默认可以恢复数据至OSS标准存储。

## Restore 命令
Restore 命令可以解冻归档类型（Archive）或冷归档（Cold Archive）的目录。

```
jindo oss -restore [-days <days>] <path>
```