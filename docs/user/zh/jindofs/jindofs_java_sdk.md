# JindoFS Java SDK 使用指南

## 介绍

JindoFS Java SDK 提供了访问 OSS-HDFS 数据湖存储服务的 Java API。它支持常见的文件系统元数据接口和流式数据操作，如创建目录、读写或删除文件；同时也支持 OSS-HDFS 特有的方法，如清单导出等。

JindoFS Java SDK 并未提供基于 Hadoop-Compatible FileSystem（HCFS）的实现。如果希望在 Hadoop 生态中以 HCFS 标准接口访问 OSS-HDFS，请参考 [《Hadoop 环境使用 JindoSDK 快速入门》](../jindosdk/jindosdk_quickstart.md)

本文档基于 JindoFS Java SDK 6.2.5 版本编写。

## 与 JindoSDK 的区别

JindoSDK 同样提供了访问 OSS-HDFS 服务的 Java API。与之相比，JindoFS Java SDK 有三个主要区别：
1. JindoFS Java SDK 不考虑原生支持 Hadoop 生态，不提供基于 HCFS 的实现，也不会从 Hadoop core-site 读取配置。
2. JindoFS Java SDK 只支持 OSS-HDFS，而 JindoSDK 除了支持 OSS-HDFS 以外，还可以支持普通 OSS、S3、HDFS 等多种存储服务。
3. JindoFS Java SDK 提供了一些 OSS-HDFS 特有的接口，这些接口可能是 JindoSDK 未提供的，例如清单导出等。

如果希望使用 HCFS 的文件系统，请参考 [《Hadoop 环境使用 JindoSDK 快速入门》](../jindosdk/jindosdk_quickstart.md)，对 Hadoop 生态 HCFS 的介绍请参阅 Apache Hadoop 文档。

## 获取

JindoFS Java SDK 随着 JindoSDK 一同发布，见 [下载页面](/docs/user/zh/jindosdk/jindosdk_download.md)

下载后解压缩，在 `tools/` 目录下即可找到 JindoFS Java SDK 的资源：
 - `jindofs-core-${jindofs-javasdk-version}.jar`
 - `jindofs-sdk-${jindofs-javasdk-version}.jar`

注意：JindoFS Java SDK 的版本号，即上述 `jar` 文件中的 `${jindofs-javasdk-version}` 部分，与 JindoSDK 的版本号可能并不相同，
这是因为 JindoFS Java SDK 使用了单独的版本号管理，因此两者的版本原本就是可以不一致的。请始终使用同一个 JindoSDK 版本当中的资源，
无需关心其内部的 JindoFS Java SDK 的版本号。

JindoFS Java SDK 同样提供了多平台支持，您可以在上述下载页面下载对应平台的 JindoSDK，并在 `tools/` 目录下找到 JindoFS Java SDK。
此时，`tools` 目录下的 JindoFS Java SDK 资源可能是：
 - `jindofs-core-${jindofs-javasdk-version}.jar`
 - `jindofs-core-${platform}-${jindofs-javasdk-version}.jar`
 - `jindofs-sdk-${jindofs-javasdk-version}.jar`

其中，`${platform}` 代表了适用的系统环境，例如 `linux-ubuntu22-x86_64` 等。使用时，需要同时引入上述三个 `jar` 文件。关于 JindoSDK 支持的系统环境，可参考
[《在多平台环境安装部署 JindoSDK》](../jindosdk/jindosdk_deployment_multi_platform.md)

## Maven

目前暂未提供可以公共访问的 Maven 仓库。您可以本地依赖这两个 `jar` 文件，或者将它们放置在一个您方便访问的 Maven 仓库中。

Maven 依赖写作：

```xml
<dependencies>
    <dependency>
        <groupId>com.aliyun.jindodata.jindofs</groupId>
        <artifactId>jindofs-core</artifactId>
        <version>${jindofs-javasdk-version}</version>
    </dependency>
    <dependency>
        <groupId>com.aliyun.jindodata.jindofs</groupId>
        <artifactId>jindofs-sdk</artifactId>
        <version>${jindofs-javasdk-version}</version>
    </dependency>
</dependencies>
```
其中 `${jindofs-javasdk-version}` 根据 JindoFS Java SDK 实际的版本号填写。

注：若您的系统环境需要添加多平台支持，则需要额外增加下列依赖：
```xml
    <dependency>
        <groupId>com.aliyun.jindodata.jindofs</groupId>
        <artifactId>jindofs-core-${platform}</artifactId>
        <version>${jindofs-javasdk-version}</version>
    </dependency>
```
其中 `${platform}` 根据您实际获得的 `jar` 文件名称填写。

## 使用

JindoFS Java SDK 提供了 `JfsFileSystem` 的接口，以及一个 `JfsFileSystemImpl` 的实现。客户端初始化的方式为：
```
JfsFileSystem fs = new JfsFileSystemImpl();
fs.initialize(uri, user, conf);
```
其中：
1. `uri` 指向 OSS-HDFS 的初始化地址，例如 `oss://<your-oss-hdfs-bucket>.<oss-hdfs-endpoint>/init-path`
2. `user` 表示客户端代表的用户名称。
3. `conf` 是一个 `JfsConfiguration` 对象。

`JfsConfiguration` 是客户端接受配置的唯一途径，JindoFS Java SDK 本身不会读取任何配置文件。通过 `JfsConfiguration` 可以设置多种配置参数，其中 `JfsConfiguration.JfsAccessInfo` 包含访问 OSS-HDFS 必要的信息，其他配置项均可以采用默认值。

构造 `JfsConfiguration.JfsAccessInfo` 并进一步创建 `JfsConfiguration` 的方法如下：
```
JfsConfiguration.JfsAccessInfo accessInfo = new JfsConfiguration.JfsAccessInfo();
accessInfo.accessKeyId = "your access key ID";
accessInfo.accessKeySecret = "your access key secret";
accessInfo.endpoint = "oss-hdfs-endpoint";

JfsConfiguration conf = new JfsConfiguration.Builder()
        .setDefaultAccessInfo(accessInfo)
        .build();
```

## 注意事项

 - 与 Hadoop 生态的使用习惯相悖的是，`JfsFileSystem` 的实例没有全局共享的机制，也不提供从路径获取 `JfsFileSystem` 实例的方法。并且，每一个新创建的 `JfsFileSystem` 实例在使用完之后都需要主动调用 `close` 关闭，否则可能会产生资源泄露。另一方面，对于读写流 `JfsInputStream` 与 `JfsOutputStream` 的对象，如果不调用 `close` 并没有资源泄露之虞。

## 示例

下面是一个完整的代码示例，用来演示如何初始化一个 `JfsFileSystem` 对象，并且在 OSS-HDFS 中创建一个目录：
```
package com.aliyun.jindodata.jfssdk.demo;

import com.aliyun.jindodata.jfssdk.JfsConfiguration;
import com.aliyun.jindodata.jfssdk.JfsFileSystem;
import com.aliyun.jindodata.jfssdk.JfsFileSystemImpl;

import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        JfsConfiguration.JfsAccessInfo accessInfo = new JfsConfiguration.JfsAccessInfo();
        accessInfo.accessKeyId = "your access key ID";
        accessInfo.accessKeySecret = "your access key secret";
        accessInfo.endpoint = "oss-hdfs-endpoint";

        JfsConfiguration conf = new JfsConfiguration.Builder()
                .setDefaultAccessInfo(accessInfo)
                .build();

        String uri = "oss://<your-oss-hdfs-bucket>.<oss-hdfs-endpoint>/init-uri";
        try (JfsFileSystem fs = new JfsFileSystemImpl()) {
            fs.initialize(uri, "hadoop", conf);
            fs.mkdirs("oss://<your-oss-hdfs-bucket>.<oss-hdfs-endpoint>/a-new-dir");
        }
    }
}
```
其中，`your-oss-hdfs-bucket`、`your access key ID`、`your access key secret`、`oss-hdfs-endpoint` 根据实际值填写。