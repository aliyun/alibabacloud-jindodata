# 阿里云 OSS-HDFS 服务（JindoFS 服务）Endpoint 配置

有三种方式可以指定 Endpoint，JindoSDK 会以下优先级查找 Endpoint：
## 方式一： 在访问路径中指定 Endpoint。（推荐）

访问 OSS-HDFS 服务时，推荐使用这种格式的访问路径`oss://<Bucket>.<Endpoint>/<Object>`, 如: `oss://example-oss-bucket.cn-shanghai.oss-dls.aliyuncs.com/Test`。
这种方式在访问路径中包含 Endpoint，JindoSDK 会根据路径中的 Endpoint 访问对应接口。

## 方式二： 配置 Bucket 级别的 Endpoint。

如果使用`oss://<Bucket>/<Object>`这种访问路径，即访问路径中没有 Endpoint, JindoSDK 则会在配置中查找 Bucket 级别的 Endpoint。
可在 Hadoop 的`core-site.xml`中配置 Bucket 级别的 Endpoint 指向 OSS-HDFS 服务的 Endpoint。
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.endpoint</name>
        <value>cn-xxx.oss-dls.aliyuncs.com</value>
    </property>
</configuration>
```
说明: XXX 为 OSS-HDFS 服务 Bucket 名称。（JindoSDK 6.5.0 ~ 6.10.3 版本不支持对 OSS-HDFS 指定 bucket 级别 endpoint）

## 方式三：配置全局默认 Endpoint。

如果使用`oss://<Bucket>/<Object>`这种访问路径而且没有配置 Bucket 级别的 Endpoint，则会用全局 Endpoint 访问，如果要默认使用访问 OSS-HDFS 服务，则可用该方法。
可在 Hadoop 的`core-site.xml`中配置全局默认 Endpoint。
```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>cn-xxx.oss-dls.aliyuncs.com</value>
    </property>
</configuration>
```
