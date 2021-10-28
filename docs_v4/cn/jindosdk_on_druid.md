# Druid 使用 JindoSDK 访问 OSS

Druid是Apache社区提供一款高性能的实时分析数据库软件，开源社区的Druid版本并不支持访问阿里云OSS数据湖存储，本文介绍如何配置Druid通过JindoSDK访问阿里云OSS数据湖存储。
## 环境要求

* Druid 集群已经配置 HADOOP 相关参数，并且加载HDFS Deep Storage 扩展。

* 确保$DRUID_HOME/conf/cluster/_common目录下包含hadoop配置文件, 可以将core-site.xml, hdfs-site.xml等hadoop的配置文件拷贝到$DRUID_HOME/conf/cluster/_common目录下。

## 安装步骤

### 1. 安装JindoSDK
前往[地址](jindosdk_download.md)下载JindoSDK最新版本， 下载jindosdk-${version}.jar对应的jar包，在所有 Druid 节点安装 JindoSDK。

````bash
cp jindosdk-${version}.jar  $DRUID_HOME/extensions/druid-hdfs-storage/
````

### 2. 配置JindoSDK OSS实现类
编辑 $DRUID_HOME/conf/cluster/_common/core-site.xml, 添加OSS 实现类的相关配置。

````xml
<configuration>

    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.dls.DLS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.dls.JindoDlsFileSystem</value>
    </property>

</configuration>

````

### 3. 配置JindoSDK OSS 访问秘钥
编辑 $DRUID_HOME/conf/cluster/_common/core-site.xml，配置OSS Access Key。

````xml
<configuration>

    <property>
        <name>fs.dls.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.dls.accessKeySecret</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.dls.endpoint</name>
        <value>cn-xxx.oss-dls.aliyuncs.com</value>
    </property>
</configuration>

````
JindoSDK 还支持更多的 OSS AccessKey 的配置方式，详情参考[JindoSDK OSS AccessKey 配置](./jindosdk_credential_provider.md)。<br />

### 4. 配置 Druid Deep Storage 使用 JindoSDK OSS
编辑 $DRUID_HOME/conf/druid/cluster/_common/common.runtime.properties。配置 HDFS扩展使用JindoSDK OSS。

````properties
druid.storage.type = hdfs
druid.storage.storageDirectory = oss://xxxx/xxxx
````

### 5. 重启 Druid 所有服务
重启Druid 服务使配置生效。

## 验证使用

### 1. 准备Druid OSS 测试数据
将 $DRUID_HOME/quickstart/tutorial/wikiticker-2015-09-12-sampled.json.gz 上传到OSS测试路径oss://{YOUR_BUCKET}/druid-oss/，实际使用中替换{YOUR_BUCKET}为测试OSS BUCKET名称。

### 2. 编辑Druid 的SPEC文件
Druid SPEC文件内容如下，替换下面SPEC文件中{YOUR_BUCKET}为测试OSS BUCKET名称，可以通过Druid 控制台编辑SPEC文件或者直接编辑文件oss.json。

````json
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

````bash
curl -X 'POST' -H 'Content-Type:application/json' -d @oss.json http://{YOUR_ROUTER}/druid/indexer/v1/task
````

### 5. 检查Druid 的DataSource
可以通过Druid Console或者以下命令检查OSSWikipedia的DataSoure是否生成，替换下面命令中{YOUR_ROUTER}为Druid Router组件的地址。

````bash
curl -X GET -H "Content-type: application/json" http://{YOUR_ROUTER}/druid/coordinator/v1/metadata/datasources/OSSWikipedia
````
