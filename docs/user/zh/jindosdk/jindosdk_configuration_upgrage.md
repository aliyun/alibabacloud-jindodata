# JindoSDK配置更新

## 配置介绍
常用配置介绍可以参考[配置](./jindosdk_configuration.md)。

## 配置升级
JindoSDK配置配置在Hadoop配置文件，用户需要提前确认Hadoop配置文件core-site.xml位置， 用户可以查看HADOOP_CONF_DIR或者HADOOP_HOME环境变量指定的路径，core-site.xml配置i文件一般在上面环境变量指定路径当中，以fs.oss.timeout.millisecond配置为例:  

```xml
<configuration>
    <property>
        <name>fs.oss.timeout.millisecond</name>
        <value>91000</value>
    </property>
</configuration>
```

## 服务重启
对于已经在运行的YARN作业（Application，例如：Spark Streaming或Flink作业），需要停止作业后，批量滚动重启YARN NodeManager。Hive、Presto、Impala、Flink、Ranger、Spark和Zeppelin等服务需要重启后才能完成升级。