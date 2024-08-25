# Installing and Deploying JindoSDK Across Multiple Platforms

## JindoSDK Platform Support Overview

JindoSDK currently supports most common Linux distributions and works on both x86/aarch64 architectures.

It also supports macOS operating systems on x86 platforms and some versions on M1/M2 chips.

Windows OS is not currently supported.

Unsupported Linux distributions include those using incompatible glibc versions like those relying on musl.

## Outputs

There are two categories of outputs: Full Output and Lite Output. Here, x.y.z refers to the version number.

## Full Output Explanation

Full output is installed by default in Aliyun EMR at `/opt/apps/JINDOSDK/jindosdk-current/`.

A full output is a tarball named `jindosdk-x.y.z-<platform-name>.tar.gz`.

It includes Hadoop SDK, Jindo-Fuse, Jindo-DistCP, JindoTable, Jindo Flink Connector, and JindoCLI tools for deployment and usage, along with dependencies required for secondary development.

To install the complete JindoSDK, use this type of output, but be aware that it may be larger in size.

## Lite Output

If you only need the Hadoop SDK from JindoSDK in your Hadoop environment, use the lite output.

For Java SDK, on Linux x86, you'll require `jindo-sdk-x.y.z.jar` and `jindo-core-x.y.z.jar`. On other platforms, you'll need `jindo-sdk-x.y.z.jar`、`jindo-core-x.y.z.jar` and `jindo-core-<platform-name>-x.y.z.jar`.

Installation can be done through Maven by adding dependencies in `pom.xml` (refer to [6.6.0-OSS-Maven](oss-maven.md)) or directly downloading and deploying from OSS (see [6.6.0-Download](jindosdk_download.md)).

## Output Platform Table

| Output Name | Description |
| --- | --- |
| jindosdk-x.y.z-linux.tar.gz | Full output for most Linux x86 distributions, compatible with glibc 2.17+ |
| jindosdk-x.y.z-linux-el7-aarch64.tar.gz | Full output for most Linux aarch64 distributions, compatible with glibc 2.17+ |
| jindosdk-x.y.z-linux-el6-x86_64.tar.gz | Full output for CentOS6 x86, compatible with glibc 2.12+ |
| jindosdk-x.y.z-linux-ubuntu22-x86_64.tar.gz | Full output for Ubuntu22 x86, compatible with glibc 2.34+ |
| jindosdk-x.y.z-macos-11_0-x86_64.tar.gz | Full output for macOS x86, compatible with macOS 11.0+ |
| jindosdk-x.y.z-macos-11_0-aarch64.tar.gz | Full output for macOS M1/M2, compatible with macOS 11.0+ |
| jindo-sdk-x.y.z.jar | Lite output for all platforms, pure Java implementation without native code |
| jindo-core-x.y.z.jar | Lite output for most Linux x86 distributions, includes native code, compatible with glibc 2.17+ |
| jindo-core-linux-el7-aarch64-x.y.z.jar | Lite output for most Linux aarch64 distributions, includes native code, compatible with glibc 2.17+ |
| jindo-core-linux-el6-x86_64-x.y.z.jar | Lite output for CentOS6 x86, includes native code, compatible with glibc 2.12+ |
| jindo-core-linux-ubuntu22-x86_64-x.y.z.jar | Lite output for Ubuntu22 x86, includes native code, compatible with glibc 2.34+ |
| jindo-core-macos-11_0-x86_64-x.y.z.jar | Lite output for macOS x86, compatible with macOS 11.0+ |
| jindo-core-macos-11_0-aarch64-x.y.z.jar | Lite output for macOS M1/M2, compatible with macOS 11.0+ |

# JindoSDK Deployment Examples

Let's take version 6.6.0 as an example.

## Linux x86 Example

### Full Installation
Assuming the extracted folder is placed at `/usr/lib/jindosdk-6.6.0-linux`:

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.6.0/jindosdk-6.6.0-linux.tar.gz
tar zxvf jindosdk-6.6.0-linux.tar.gz -C /usr/lib/
export JINDOSDK_HOME=/usr/lib/jindosdk-6.6.0-linux
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```

### Lite Installation
Suppose the downloaded JAR files are placed in `<HADOOP_HOME>/share/hadoop/hdfs/lib/`:

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-sdk/6.6.0/jindo-sdk-6.6.0.jar
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-core/6.6.0/jindo-core-6.6.0.jar
cp jindo-core-6.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-6.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

## Linux aarch64 Example

### Full Installation
Assuming the extracted folder is placed at `/usr/lib/jindosdk-6.6.0-linux-el7-aarch64`:

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/x.y.z/jindosdk-6.6.0-linux-el7-aarch64.tar.gz
tar zxvf jindosdk-6.6.0-linux-el7-aarch64.tar.gz -C /usr/lib/
export JINDOSDK_HOME=/usr/lib/jindosdk-6.6.0-linux-el7-aarch64
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```

### Lite Installation
Suppose the downloaded JAR files are placed in `<HADOOP_HOME>/share/hadoop/hdfs/lib/`:

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-sdk/6.6.0/jindo-sdk-6.6.0.jar
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/com/aliyun/jindodata/jindo-core-linux-el7-aarch64/6.6.0/jindo-core-linux-el7-aarch64-6.6.0.jar
cp jindo-core-linux-el7-aarch64-6.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindo-core-6.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
cp jindosdk-6.6.0.jar <HADOOP_HOME>/share/hadoop/hdfs/lib/
```

## Appendix

### Full Output Example
Here's a directory structure representation for a full output (version numbers represented as x.y.z):

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