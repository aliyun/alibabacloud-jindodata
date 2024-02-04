# JindoSDK 高级参数配置

## SDK配置项列表

可根据情况将以下配置添加到 Hadoop 的 `core-site.xml` 中。

```xml
<configuration>
  
    <property>
      	<!-- 客户端写入时的临时文件目录，可配置多个（逗号隔开），会轮流写入，多用户环境需配置可读写权限 -->
        <name>fs.oss.tmp.data.dirs</name>
        <value>/tmp/</value>
    </property>
  
    <property>
      	<!-- 临时文件自清理服务 -->
        <name>fs.oss.tmp.data.cleaner.enable</name>
        <value>true</value>
    </property>
  
    <property>
      	<!-- 访问 oss 失败重试次数 -->
        <name>fs.oss.retry.count</name>
        <value>5</value>
    </property>

    <property>
      	<!-- 请求 oss 超时时间（毫秒） -->
        <name>fs.oss.timeout.millisecond</name>
        <value>30000</value>
    </property>

    <property>
      	<!-- 连接 oss 超时时间（毫秒） -->
        <name>fs.oss.connection.timeout.millisecond</name>
        <value>3000</value>
    </property>
  
    <property>
      	<!-- 单个文件 oss 并发上传线程数 -->
        <name>fs.oss.upload.thread.concurrency</name>
        <value>5</value>
    </property>

    <property>
      	<!-- oss 并发上传任务队列大小 -->
        <name>fs.oss.upload.queue.size</name>
        <value>5</value>
    </property>

    <property>
      	<!-- 进程内 oss 最大并发上传任务数 -->
        <name>fs.oss.upload.max.pending.tasks.per.stream</name>
        <value>16</value>
    </property>

    <property>
      	<!-- oss 并发下载任务队列大小 -->
        <name>fs.oss.download.queue.size</name>
        <value>5</value>
    </property>

    <property>
      	<!-- 进程内 oss 最大并发下载任务数 -->
        <name>fs.oss.download.thread.concurrency</name>
        <value>16</value>
    </property>
  
    <property>
      	<!-- 预读 oss 的 buffer 大小 -->
        <name>fs.oss.read.readahead.buffer.size</name>
        <value>1048576</value>
    </property>
  
    <property>
      	<!-- 同时预读 oss 的 buffer 个数 -->
        <name>fs.oss.read.readahead.buffer.count</name>
        <value>4</value>
    </property>

    <property>
        <!--oss 刷新缓冲区间隔（毫秒），小于 0 时不生效 -->
        <name>fs.oss.flush.interval.millisecond</name>
        <value>-1</value>
    </property>

    <property>
        <!-- 文件级别 crc64 完整性校验，默认打开 -->
        <name>fs.oss.checksum.crc64.enable</name>
        <value>true</value>
    </property>

    <property>
        <!--请求级别 md5 完整性校验，默认关闭 -->
        <name>fs.oss.checksum.md5.enable</name>
        <value>false</value>
    </property>

  
</configuration>
```

【注：4.5版本以上版本支持方式】

