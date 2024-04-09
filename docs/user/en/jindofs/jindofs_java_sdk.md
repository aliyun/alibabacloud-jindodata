# JindoFS Java SDK User Guide

## Introduction

The JindoFS Java SDK provides a Java API for accessing the OSS-HDFS data lake storage service. It supports common filesystem metadata interfaces and stream-based data operations, such as creating directories, reading, writing, and deleting files, along with OSS-HDFS-specific methods like inventory exports.

Unlike JindoSDK, the JindoFS Java SDK does not offer a native implementation compatible with Hadoop's Compatible FileSystem (HCFS) interface and doesn't read configurations from Hadoop's `core-site.xml`.

This guide is based on JindoFS Java SDK version 6.2.5.

## Differences from JindoSDK

There are three primary differences between JindoFS Java SDK and JindoSDK:
1. JindoFS Java SDK doesn't cater to the Hadoop ecosystem natively; it doesn't provide an HCFS implementation and doesn't read configurations from Hadoop.
2. JindoFS Java SDK is specific to OSS-HDFS, while JindoSDK supports OSS-HDFS, regular OSS, S3, and HDFS.
3. JindoFS Java SDK has some OSS-HDFS-specific interfaces that might not be present in JindoSDK, e.g., inventory exports.

For HCFS usage, refer to the [JindoSDK Quick Start](../jindosdk/jindosdk_quickstart.md), and for Hadoop's HCFS, consult the Apache Hadoop documentation.

## Availability

The JindoFS Java SDK is currently limited to the Linux x86 platform. Download it from the [link](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.2.5/jindofs-sdk-6.2.5-linux.tar.gz).
- Note: For MacOS, Ubuntu, or other architectures, contact the JindoFS team for support.

After extraction, you'll find the SDK resources in the `lib/` folder:
- `jindofs-core-${version}.jar`
- `jindofs-sdk-${version}.jar`

## Maven Dependency

Public Maven repositories are not currently available. You can either depend on these two `.jar` files locally or add them to your accessible Maven repository.

Add the following dependencies to your `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>com.aliyun.jindodata.jindofs</groupId>
        <artifactId>jindofs-core</artifactId>
        <version>${version}</version>
    </dependency>
    <dependency>
        <groupId>com.aliyun.jindodata.jindofs</groupId>
        <artifactId>jindofs-sdk</artifactId>
        <version>${version}</version>
    </dependency>
</dependencies>
```
Replace `${version}` with the actual version number.

## Usage

The JindoFS Java SDK provides the `JfsFileSystem` interface and an implementation called `JfsFileSystemImpl`. Initialize a client instance as follows:
```java
JfsFileSystem fs = new JfsFileSystemImpl();
fs.initialize(new URI("oss://<your-oss-hdfs-bucket>.<oss-hdfs-endpoint>/<init-path>"), "user", conf);
```
Here:
1. `<your-oss-hdfs-bucket>` and `<oss-hdfs-endpoint>` represent your bucket and endpoint, respectively.
2. `"user"` is the username representing the client.
3. `conf` is a `JfsConfiguration` object.

`JfsConfiguration` is the sole way to configure the client, and the SDK itself doesn't read any configuration files. Use it to set various configuration parameters, with `JfsConfiguration.JfsAccessInfo` holding essential access info for OSS-HDFS.

Create a `JfsConfiguration.JfsAccessInfo` and build a `JfsConfiguration` like this:
```java
JfsConfiguration.JfsAccessInfo accessInfo = new JfsConfiguration.JfsAccessInfo();
accessInfo.accessKeyId = "your access key ID";
accessInfo.accessKeySecret = "your access key secret";
accessInfo.endpoint = "oss-hdfs-endpoint";

JfsConfiguration conf = new JfsConfiguration.Builder()
        .setDefaultAccessInfo(accessInfo)
        .build();
```

## Note

- Unlike Hadoop's convention, there's no global sharing mechanism for `JfsFileSystem` instances, nor is there a method to get an instance from a path. Each newly created `JfsFileSystem` must be closed explicitly after use to avoid resource leaks. On the other hand, unclosed `JfsInputStream` and `JfsOutputStream` objects won't leak resources.

## Example

Here's a complete code example demonstrating how to initialize a `JfsFileSystem` object and create a directory in OSS-HDFS:
```java
package com.aliyun.jindodata.jfssdk.demo;

import com.aliyun.jindodata.jfssdk.JfsConfiguration;
import com.aliyun.jindodata.jfssdk.JfsFileSystem;
import com.aliyun.jindodata.jfssdk.JfsFileSystemImpl;
import java.io.IOException;
import java.net.URI;

public class Demo {
    public static void main(String[] args) throws IOException {
        JfsConfiguration.JfsAccessInfo accessInfo = new JfsConfiguration.JfsAccessInfo();
        accessInfo.accessKeyId = "your access key ID";
        accessInfo.accessKeySecret = "your access key secret";
        accessInfo.endpoint = "oss-hdfs-endpoint";

        JfsConfiguration conf = new JfsConfiguration.Builder()
                .setDefaultAccessInfo(accessInfo)
                .build();

        URI uri = new URI("oss://<your-oss-hdfs-bucket>.<oss-hdfs-endpoint>/<init-uri>");
        try (JfsFileSystem fs = new JfsFileSystemImpl()) {
            fs.initialize(uri, "hadoop", conf);
            fs.mkdirs("oss://<your-oss-hdfs-bucket>.<oss-hdfs-endpoint>/a-new-dir");
        }
    }
}
```
Replace placeholders with actual values (`your-oss-hdfs-bucket`, `your access key ID`, `your access key secret`, and `oss-hdfs-endpoint`).