# Druid 使用 JindoFS SDK 访问 OSS

# 前置

---
* 您的 Druid 集群已经配置 HADOOP 相关参数，并且加载HDFS Deep Storage 扩展.

* 确保$DRUID_HOME/conf/cluster/_common目录下包含hadoop配置文件, 可以将core-site.xml, hdfs-site.xml等hadoop的配置文件拷贝到$DRUID_HOME/conf/cluster/_common目录下.

# 使用

* 前往[地址](jindofs_sdk_download.md)下载JindoFS SDK最新版本， 下载jindofs-sdk-${version}.jar对应的jar包.

* 在所有 Druid 节点安装 JindoFS SDK.

````
cp jindofs-sdk-${version}.jar  $DRUID_HOME/extensions/druid-hdfs-storage/
````

* 配置Druid HDFS的扩展使用JindoFS SDK 访问OSS，编辑 $DRUID_HOME/conf/cluster/_common/core-site.xml.

````
<configuration>

    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.OSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.JindoOssFileSystem</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss.endpoint</name>
        <!-- ECS 环境推荐使用内网 OSS Endpoint，即 oss-cn-xxx-internal.aliyuncs.com -->
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>

````
JindoFS还支持更多的OSS AccessKey的配置方式，详情参考[JindoFS SDK OSS AccessKey 配置](./jindosdk_credential_provider.md)。<br />

* 配置 Druid 使用 OSS 作为Deep Storage, 编辑 $DRUID_HOME/conf/druid/cluster/_common/common.runtime.properties.

````
druid.storage.type = hdfs
druid.storage.storageDirectory = oss://xxxx/xxxx
````

* 重启 Druid 所有服务，使配置生效。

# 验证

* 将 $DRUID_HOME/quickstart/tutorial/wikiticker-2015-09-12-sampled.json.gz 上传到测试OSS路径oss://{YOUR_BUCKET}/druid-oss/， 实际使用中替换{YOUR_BUCKET}为测试OSS BUCKET名称。

* 编辑Druid 的SPEC文件oss.json， 替换下面SPEC文件中{YOUR_BUCKET}为测试OSS BUCKET名称。

````
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "OSSWikipedia",
      "dimensionsSpec" : {
        "dimensions" : [
          "channel",
          "cityName",
          "comment",
          "countryIsoCode",
          "countryName",
          "isAnonymous",
          "isMinor",
          "isNew",
          "isRobot",
          "isUnpatrolled",
          "metroCode",
          "namespace",
          "page",
          "regionIsoCode",
          "regionName",
          "user",
          { "name": "added", "type": "long" },
          { "name": "deleted", "type": "long" },
          { "name": "delta", "type": "long" }
        ]
      },
      "timestampSpec": {
        "column": "time",
        "format": "iso"
      },
      "metricsSpec" : [],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : false
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "inputSource" : {
        "type" : "hdfs",
        "paths": "oss://{YOUR_BUCKET}/druid-oss//wikiticker-2015-09-12-sampled.json.gz"
      },
      "inputFormat" :  {
        "type": "json"
      },
      "appendToExisting" : false
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000
    }
  }
}
````

* 提交Druid 任务，可以通过Druid Console提交任务或者以下命令提交任务， 替换下面命令中{YOUR_ROUTER}为Druid Router组件的地址。

````
curl -X 'POST' -H 'Content-Type:application/json' -d @oss.json http://{YOUR_ROUTER}/druid/indexer/v1/task
````

* 检查Druid 的DataSource, 可以通过Druid Console或者以下命令检查OSSWikipedia的DataSoure是否生成，替换下面命令中{YOUR_ROUTER}为Druid Router组件的地址。

````
curl -X GET -H "Content-type: application/json" http://{YOUR_ROUTER}/druid/coordinator/v1/metadata/datasources/OSSWikipedia
````
