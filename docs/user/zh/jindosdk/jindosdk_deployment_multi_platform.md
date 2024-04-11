# 在多平台环境安装部署 JindoSDK

## JindoSDK 多平台支持现状

JindoSDK 目前支持大部分常见的 Linux 发行版，并支持 x86/aarch64 平台。

支持 MacOS 操作系统，所有版本均支持 x86 平台，部分版本支持 M1/M2 芯片的 arm 平台。

暂不支持 Windows 操作系统。

暂不支持未兼容 glibc 的 linux 发行版，如默认使用 musl 的发行版。

### 产出物

产出物包含两类：完整产出物、lite产出物。以下 x.y.z 表示版本号。

### 完整产出物说明

在 Aliyun EMR 中默认安装了完整产出物，安装路径为/opt/apps/JINDOSDK/jindosdk-current/。

完整产出物是一个tar包，名称为 `jindosdk-x.y.z-<平台名称>.tar.gz`。

完整产出物包含 hadoop sdk，jindo-fuse, jindo-distcp, jindotable，jindo flink connector, jindocli 等工具，可以部署使用。也包含二次开发所需的so等依赖。

如果希望安装完整的 JindoSDK，可以部署安装这类产出物，但该产出物一般较大。

### lite 产出物

如果希望在 Hadoop 环境中仅使用 JindoSDK 中的 hadoop sdk，可以使用此类产出物。

使用Java SDK时，Linux X86平台需要依赖 `jindo-sdk-x.y.z.jar` 和 `jindo-core-x.y.z.jar`，其他平台需要依赖 `jindo-sdk-x.y.z.jar` 和 `jindo-core-<平台名称>-x.y.z`.jar。

安装方式的话，即可以通过maven在pom.xml中配置依赖，如[6.3.4-oss-maven](oss-maven.md)。也可以通过oss地址直接下载部署，如[6.3.4-download](jindosdk_download.md)。

### 产物物平台说明表

|  产出物名称  |  说明  |
| --- | --- |
|  jindosdk-x.y.z-linux.tar.gz  |  支持大多数 linux x86 发行版的完整产出物，兼容 glibc 2.17+ 版本  |
|  jindosdk-x.y.z-linux-el7-aarch64.tar.gz  | 支持大多数 linux aarch64 发行版的完整产出物，兼容 glibc 2.17+ 版本  |
|  jindosdk-x.y.z-linux-el6-x86_64.tar.gz |  支持 centos6 x86 的完整产出物，兼容 glibc 2.12+ 版本  |
|  jindosdk-x.y.z-linux-ubuntu22-x86_64.tar.gz |  支持 ubuntu22 x86 的完整产出物，兼容 glibc 2.34+ 版本  |
|  jindosdk-x.y.z-macos-11_0-x86_64.tar.gz |  支持 macos x86 的完整产出物，兼容 macos 11.0 以上版本  |
|  jindosdk-x.y.z-macos-11_0-aarch64.tar.gz |  支持 macos m1/m2 的完整产出物，兼容 macos 11.0 以上版本  |
|  jindo-sdk-x.y.z.jar |  支持所有平台的 lite 产出物，纯 java 实现，不包含 native 实现  |
|  jindo-core-x.y.z.jar |  支持大多数 linux x86 发行版的 lite 产出物，包含 native 实现，兼容 glibc 2.17+ 版本  |
|  jindo-core-linux-el7-aarch64-x.y.z.jar |  支持大多数 linux aarch64 发行版的 lite 产出物，包含 native 实现，兼容 glibc 2.17+ 版本  |
|  jindo-core-linux-el6-x86_64-x.y.z.jar |  支持 centos6 x86 的 lite 产出物，兼容 glibc 2.12+ 版本  |
|  jindo-core-linux-ubuntu22-x86_64-x.y.z.jar |  支持 ubuntu22 x86 的 lite 产出物，兼容 glibc 2.34+ 版本  |
|  jindo-core-macos-11_0-x86_64-x.y.z.jar |  支持 macos x86 的 lite 产出物，兼容 macos 11.0 以上版本  |
|  jindo-core-macos-11_0-aarch64-x.y.z.jar |  支持 macos m1/m2 的 lite 产出物，兼容 macos 11.0 以上版本  |

## JindoSDK 部署示例

以下 6.3.4 版本为例。

### Linux x86 环境示例

1. 完整安装，以安装包内容解压在 /usr/lib/jindosdk-6.3.4-linux 目录为例：

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.3.4/jindosdk-6.3.4-linux.tar.gz
tar zxvf jindosdk-6.3.4-linux.tar.gz -C /usr/lib/
export JINDOSDK_HOME=/usr/lib/jindosdk-6.3.4-linux
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```

2. lite安装，以安装包内容下载到 <HADOOP_HOME>/share/hadoop/hdfs/lib/ 目录为例：

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-sdk/6.3.4/jindo-sdk-6.3.4.jar
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-core/6.3.4/jindo-core-6.3.4.jar
cp jindo-core-6.3.4.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-6.3.4.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

### Linux aarch64 环境示例

1. 完整安装，以安装包内容解压在 /usr/lib/jindosdk-6.3.4-linux-el7-aarch64 目录为例：

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/x.y.z/jindosdk-6.3.4-linux-el7-aarch64.tar.gz
tar zxvf jindosdk-6.3.4-linux-el7-aarch64.tar.gz -C /usr/lib/
export JINDOSDK_HOME=/usr/lib/jindosdk-6.3.4-linux-el7-aarch64
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```

2. lite安装，以安装包内容下载到 <HADOOP_HOME>/share/hadoop/hdfs/lib/ 目录为例：

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-sdk/6.3.4/jindo-sdk-6.3.4.jar
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-core-linux-el7-aarch64/6.3.4/jindo-core-linux-el7-aarch64-6.3.4.jar
cp jindo-core-linux-el7-aarch64-6.3.4.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-6.3.4.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

# 附录
## 完整产出物示例
以下 x.y.z 表示版本号。
```
.
├── bin
│   ├── fusermount3
│   ├── jindo
│   ├── jindo-fuse
│   ├── jindobench
│   ├── jindodiag
│   ├── jindosync
│   └── jindotable
├── conf
│   ├── core-site.xml.template
│   ├── jindosdk.cfg.template
│   └── log4j.properties.template
├── include
│   ├── jdo_api.h
│   ├── jdo_common.h
│   ├── jdo_content_summary.h
│   ├── jdo_data_types.h
│   ├── jdo_defines.h
│   ├── jdo_error.h
│   ├── jdo_file_status.h
│   ├── jdo_list_dir_result.h
│   ├── jdo_option_keys.h
│   └── jdo_options.h
├── lib
│   ├── jindo-core-x.y.z.jar
│   ├── jindo-core-linux-el7-aarch64-x.y.z.jar
│   ├── jindo-sdk-x.y.z.jar
│   ├── native
│   │   ├── libfuse3.so.3
│   │   ├── libjemalloc.so
│   │   ├── libjindo-csdk.so
│   │   ├── libjindo-tensorflow1.15.so
│   │   ├── libjindo-tensorflow2.8.so
│   │   ├── libjindosdk.so
│   │   ├── libjindosdk_c.so -> libjindosdk_c.so.x
│   │   ├── libjindosdk_c.so.x -> libjindosdk_c.so.x.y.z
│   │   └── libjindosdk_c.so.x.y.z
│   └── site-packages
│       ├── pyjindo-x.y.z-cp310-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp311-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp312-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp36-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp37-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp38-abi3-linux_x86_64.whl
│       └── pyjindo-x.y.z-cp39-abi3-linux_x86_64.whl
├── plugins
│   └── flink
│       ├── jindo-flink-x.y.z-full.jar
│       └── jindo-flink-x.y.z.jar
│   ├── spark2
│   │   └── jindo-spark2-x.y.z.jar
│   └── spark3
│       └── jindo-spark3-x.y.z.jar
├── tools
│   ├── jindo-dependence-shaded-x.y.z.jar
│   ├── jindo-distcp-tool-x.y.z.jar
│   ├── jindo-distjob-tool-x.y.z.jar
│   ├── jindotable-hive-tool-x.y.z.jar
│   └── jindotable-shell-x.y.z.jar
└── versions
    ├── JINDOSDK_VERSION
    ├── JINDOTABLE_VERSION
    └── JINDO_CONNECTORS_VERSION

9 directories, 29 files
```