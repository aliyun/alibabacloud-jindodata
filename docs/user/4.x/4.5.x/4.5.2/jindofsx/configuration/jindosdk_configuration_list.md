# JindoSDK 高级参数配置

## 缓存优化相关参数

可根据情况将以下配置添加到 Hadoop 的`core-site.xml`中。
```xml
<configuration>

    <property>
        <!-- xengine 开关 -->
        <name>fs.xengine</name>
        <value>jindofsx</value>
    </property>

    <property>
        <!-- 数据缓存开关 -->
        <name>fs.jindofsx.data.cache.enable</name>
        <value>false</value>
    </property>
  
    <property>
        <!-- 元数据缓存开关 -->
        <name>fs.jindofsx.meta.cache.enable</name>
        <value>false</value>
    </property>
  
    <property>
        <!-- 小文件缓存优化开关 -->
        <name>fs.jindofsx.slice.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 内存缓存开关 -->
        <name>fs.jindofsx.ram.cache.enable</name>
        <value>false</value>
    </property>

    <property>
        <!-- 是否直接连接本地 storageservice -->
        <name>fs.jindofsx.storage.connect.enable</name>
        <value>true</value>
    </property>

    <property>
        <!-- 短路读开关，在缓存结点与客户端在同一台机器上的时候可以直接读本地文件，要求打开本地 storageservice 连接 -->
        <name>fs.jindofsx.short.circuit.enable</name>
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
        <name>fs.jindofsx.rpc.backup.threads</name>
        <value>100</value>
    </property>

    <property>
      	<!-- 并发线程数量 -->
        <name>fs.jindofsx.rpc.thread.concurrency</name>
        <value>10</value>
    </property>

    <property>
        <!-- 消息体大小 -->
        <name>fs.jindofsx.rpc.body.size</name>
        <value>256 * 1024 * 1024</value>
    </property>

    <property>
      	<!-- 超时时间大小（毫秒）-->
        <name>fs.jindofsx.rpc.timeout</name>
        <value>12 * 60 * 60 * 1000</value>
    </property>

    <property>
      	<!-- 消息队列大小 -->
        <name>fs.jindofsx.rpc.max.unwritten.size</name>
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
        <name>fs.jindofsx.client.metrics.enable</name>
        <value>false</value>
    </property>

</configuration>
```

【注：4.5.2 版本以上版本支持】

