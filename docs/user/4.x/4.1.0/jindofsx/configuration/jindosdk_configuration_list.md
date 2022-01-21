# JindoSDK 高级参数配置

## 缓存优化相关参数

可根据情况将以下配置添加到 Hadoop 的`core-site.xml`中。
```xml
<configuration>

    <property>
        <!-- 数据缓存开关 -->
        <name>fs.fsx.data.cache.enable</name>
        <value>false</value>
    </property>
  
    <property>
        <!-- 元数据缓存开关 -->
        <name>fs.fsx.meta.cache.enable</name>
        <value>false</value>
    </property>
  
    <property>
        <!-- 小文件缓存优化开关 -->
        <name>fs.fsx.slice.cache.enable</name>
        <value>小文件缓存优化开关</value>
    </property>

    <property>
        <!-- 内存缓存开关 -->
        <name>fs.fsx.ram.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 短路读开关，在缓存结点与客户端在同一台机器上的时候可以直接读本地文件 -->
        <name>fs.fsx.short.circuit.enable</name>
        <value>true</value>
    </property>
  
</configuration>
```

## RPC 优化相关参数

可根据情况将以下配置添加到 Hadoop 的`core-site.xml`中。
```xml
<configuration>

    <property>
      	<!-- brpc线程池大小 -->
        <name>fs.fsx.rpc.backup.threads</name>
        <value>100</value>
    </property>

    <property>
      	<!-- 并发线程数量 -->
        <name>fs.fsx.rpc.thread.concurrency</name>
        <value>10</value>
    </property>

    <property>
        <!-- 消息体大小（毫秒）-->
        <name>fs.fsx.rpc.body.size</name>
        <value>256 * 1024 * 1024</value>
    </property>

    <property>
      	<!-- 超时时间大小（毫秒）-->
        <name>fs.fsx.rpc.timeout</name>
        <value>12 * 60 * 60 * 1000</value>
    </property>

    <property>
      	<!-- 消息队列大小 -->
        <name>fs.fsx.rpc.max.unwritten.size</name>
        <value>256 * 1024 * 1024</value>
    </property>
  
</configuration>
```

## 缓存 Metrics 相关参数

可根据情况将以下配置添加到 Hadoop 的`core-site.xml`中。
```xml
<configuration>

    <property>
        <!-- 客户端metrics开关 -->
        <name>fs.fsx.client.metrics.enable</name>
        <value>false</value>
    </property>

</configuration>
```

【注：4.1.0 版本以上版本支持】

