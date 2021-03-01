# Druid 使用 JindoFS SDK 访问 OSS

Druid是Apache社区提供一款高性能的实时分析数据库软件，开源社区的Druid版本并不支持访问阿里云OSS数据湖存储，本文介绍如何配置Druid通过JindoFS SDK访问阿里云OSS数据湖存储。
## 前置

* Druid 集群已经配置 HADOOP 相关参数，并且加载HDFS Deep Storage 扩展。

* 确保$DRUID_HOME/conf/cluster/_common目录下包含hadoop配置文件, 可以将core-site.xml, hdfs-site.xml等hadoop的配置文件拷贝到$DRUID_HOME/conf/cluster/_common目录下。

## 安装步骤

### 1. 安装JindoFS SDK
前往[地址](jindofs_sdk_download.md)下载JindoFS SDK最新版本， 下载jindofs-sdk-${version}.jar对应的jar包，在所有 Druid 节点安装 JindoFS SDK。

````
cp jindofs-sdk-${version}.jar  $DRUID_HOME/extensions/druid-hdfs-storage/
````

### 2. 配置JindoFS OSS的实现
编辑 $DRUID_HOME/conf/cluster/_common/core-site.xml, 添加OSS 实现类的相关配置。

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

</configuration>

````

### 3. 配置JindoFS OSS 访问秘钥
编辑 $DRUID_HOME/conf/cluster/_common/core-site.xml，配置OSS Access Key。

````
<configuration>

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

### 4. 配置 Druid Deep Storage 使用 JindoFS OSS
编辑 $DRUID_HOME/conf/druid/cluster/_common/common.runtime.properties。配置 HDFS扩展使用JindoFS OSS。

````
druid.storage.type = hdfs
druid.storage.storageDirectory = oss://xxxx/xxxx
````

### 5. 重启 Druid 所有服务
重启Druid 服务使配置生效。

## 验证

### 1. 准备Druid OSS 测试数据
将 $DRUID_HOME/quickstart/tutorial/wikiticker-2015-09-12-sampled.json.gz 上传到OSS测试路径oss://{YOUR_BUCKET}/druid-oss/，实际使用中替换{YOUR_BUCKET}为测试OSS BUCKET名称。

### 2. 编辑Druid 的SPEC文件
Druid SPEC文件内容如下，替换下面SPEC文件中{YOUR_BUCKET}为测试OSS BUCKET名称，可以通过Druid 控制台编辑SPEC文件或者直接编辑文件oss.json。

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
        "paths": "oss://{YOUR_BUCKET}/druid-oss/wikiticker-2015-09-12-sampled.json.gz"
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

### 4. 提交Druid 任务
可以通过Druid Console提交任务或者以下命令提交任务， 替换下面命令中{YOUR_ROUTER}为Druid Router组件的地址。

````
curl -X 'POST' -H 'Content-Type:application/json' -d @oss.json http://{YOUR_ROUTER}/druid/indexer/v1/task
````

### 5. 检查Druid 的DataSource
可以通过Druid Console或者以下命令检查OSSWikipedia的DataSoure是否生成，替换下面命令中{YOUR_ROUTER}为Druid Router组件的地址。

````
curl -X GET -H "Content-type: application/json" http://{YOUR_ROUTER}/druid/coordinator/v1/metadata/datasources/OSSWikipedia
````
