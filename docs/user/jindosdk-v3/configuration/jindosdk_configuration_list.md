# JindoSDK 高级参数配置

## SDK配置项列表

可根据情况将以下配置添加到 Hadoop 的 `core-site.xml` 中。

```xml
<configuration>
  
    <property>
      	<!-- 客户端写入时的临时文件目录，可配置多个（逗号隔开），会轮流写入，多用户环境需配置可读写权限 -->
        <name>client.temp-data-dirs</name>
        <value>/tmp/</value>
    </property>
  
    <property>
      	<!-- 临时文件自清理服务 -->
        <name>tmpfile.cleaner.enable</name>
        <value>true</value>
    </property>
  
    <property>
      	<!-- 访问 oss 失败重试次数 -->
        <name>client.oss.retry</name>
        <value>5</value>
    </property>

    <property>
      	<!-- 请求 oss 超时时间（毫秒） -->
        <name>client.oss.timeout.millisecond</name>
        <value>30000</value>
    </property>

    <property>
      	<!-- 连接 oss 超时时间（毫秒） -->
        <name>client.oss.connection.timeout.millisecond</name>
        <value>30000</value>
    </property>
  
    <property>
      	<!-- 单个文件 oss 并发上传线程数 -->
        <name>client.oss.upload.thread.concurrency</name>
        <value>5</value>
    </property>

    <property>
      	<!-- oss 并发上传任务队列大小 -->
        <name>client.oss.upload.queue.size</name>
        <value>5</value>
    </property>

    <property>
      	<!-- 进程内 oss 最大并发上传任务数 -->
        <name>client.oss.upload.max-pending-tasks-per-stream</name>
        <value>16</value>
    </property>

    <property>
      	<!-- oss 并发下载任务队列大小 -->
        <name>client.oss.download.queue.size</name>
        <value>5</value>
    </property>

    <property>
      	<!-- 进程内 oss 最大并发下载任务数 -->
        <name>client.oss.download.thread.concurrency</name>
        <value>16</value>
    </property>
  
    <property>
      	<!-- 预读 oss 的 buffer 大小 -->
        <name>client.read.oss.readahead.buffer.size</name>
        <value>1048576</value>
    </property>
  
    <property>
      	<!-- 同时预读 oss 的 buffer 个数 -->
        <name>client.read.oss.readahead.buffer.count</name>
        <value>4</value>
    </property>
  
    <property>
      	<!-- rename 操作内部的最大并发度 -->
        <name>jfs.cache.rename.concurrency</name>
        <value>100</value>
    </property>
  
</configuration>
```

【注：3.7 及以上版本支持方式，3.1~3.6 版本参考此 [配置项列表](jindosdk_configuration_list_3_6.md)，3.0 及以下版本参考此 [配置项列表](jindosdk_configuration_list_3_x.md)

