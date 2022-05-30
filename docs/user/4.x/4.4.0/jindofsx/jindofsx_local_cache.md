# 客户端本地缓存使用说明

数据缓存加速作为JindoFSx的核心功能之一，4.4.0版本引入了客户端本地缓存（Local cache）的缓存使用模式，提供一种纯客户端的缓存加速能力。

## 客户端本地缓存简介

JindoFSx客户端提供了两种数据缓存使用模式，分布式缓存（Dist cache）和客户端本地缓存（Local cache）。分布式缓存作为JindoFSx的默认使用模式，通过访问JindoFSx分布式缓存服务实现对缓存数据的读写；
4.4.0额外引入了本地缓存的使用模式，在客户端进程内嵌了缓存管理模块，不需要依赖分布式服务，直接利用本地的磁盘或者内存对数据进行缓存加速。

<img src="pic/jindofsx_local_cache.png" width="800"/>

## 使用客户端本地缓存

本文以 JindoFSx 支持阿里云 OSS 透明缓存加速的使用方式为例。

* 配置 OSS 实现类

将 JindoFSx 服务 OSS 实现类配置到 Hadoop 的`core-site.xml`中。

```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.oss.OSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
    </property>

    <property>
        <name>fs.xengine</name>
        <value>jindofsx</value>
    </property>
</configuration>
```

* 配置 AccessKey

将 OSS 的 Bucket 对应的`Access Key ID`、`Access Key Secret`、`Endpoint`等预先配置在 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.oss.endpoint</name>
        <value>oss-cn-xxx-internal.aliyuncs.com</value>
    </property>
</configuration>
```

* 配置本地缓存动态库路径

客户端本地缓存功能通过插件形式提供动态库，需要配置路径以加载动态库，动态库在jindosdk-4.4.0/plugins/路径下，将该路径的绝对路径添加到 Hadoop 的`core-site.xml`中。
```xml
<configuration>
    <property>
        <name>fs.jdo.plugin.dir</name>
        <value>${JINDOSDK_HOME}/plugins</value>
    </property>
</configuration>
```

* 启用客户端本地缓存

将以下配置添加到 Hadoop 的`core-site.xml`中，启用并配置客户端本地缓存的必要配置。
```xml
<configuration>

    <property>
        <!-- 数据缓存总开关 -->
        <name>fs.jindofsx.data.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 客户端本地缓存开关 -->
        <name>fs.jindofsx.data.cache.local.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 本地缓存磁盘路径 -->
        <name>fs.jindofsx.data.cache.local.data-dirs</name>
        <value>/mnt/disk1/localcache,/mnt/disk2/localcache</value>
    </property>

    <property>
        <!-- 本地缓存磁盘大小 -->
        <name>fs.jindofsx.data.cache.local.data-dirs.capacities</name>
        <value>50G,50G</value>
    </property>

    <property>
        <!-- 磁盘使用量的上水位比例 -->
        <name>jindofsx.data.cache.local.watermark.high.ratio</name>
        <value>0.8</value>
    </property>

    <property>
        <!-- 磁盘使用量的下水位比例 -->
        <name>jindofsx.data.cache.local.watermark.low.ratio</name>
        <value>0.4</value>
    </property>

</configuration>
```

## 注意事项

* 当前版本客户端本地缓存为进程独占磁盘路径，同一个路径只能由一个进程占用，且不同进程间不共享缓存数据；
* 客户端本地缓存适用于一些常驻进程的服务中使用，进程独立享有缓存。
