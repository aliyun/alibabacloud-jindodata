# Data Migration from HDFS to OSS/OSS-HDFS

### Preparations
Before starting, please follow the guidelines in the [JindoDistCp Introduction](jindodistcp_quickstart.md) article for environment setup and tool download. If you encounter issues during usage, refer to the [JindoDistCp Troubleshooting Guide](jindodistcp_faq.md).

### 1. Copy Data to Alibaba Cloud OSS/OSS-HDFS
You can use the following command to copy a directory from HDFS to OSS/OSS-HDFS:

```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --parallelism 10
```

| Argument | Description | Example |
| --- | --- | --- |
| --src | Source path in HDFS. | `/data` |
| --dest | Target path in OSS/OSS-HDFS. | `oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/` |
| --hadoopConf | Specify OSS/OSS-HDFS Access Key ID and Secret. | `-hadoopConf fs.oss.accessKeyId=yourkey -hadoopConf fs.oss.accessKeySecret=yoursecret` |
| --parallelism | Task concurrency, adjust according to cluster resources. | `10` |

### 2. Incremental Copying
If the Distcp job fails for any reason and you want to resume where it left off, or if new files have been added to the source since the last Distcp run, perform the following steps after the previous Distcp attempt:

#### Use the --update Command to Get a List of Remaining Files
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --update --parallelism 20
```

### 3. Selecting YARN Queue and Bandwidth Limit
To specify the YARN queue and bandwidth limit for the DistCp job, use:

```shell
hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf mapreduce.job.queuename=yarnQueue --bandWidth 100 --parallelism 10
```

* `-hadoopConf mapreduce.job.queuename=yarnQueue`: Specify the YARN queue name.
* `--bandWidth`: Specify the single-node bandwidth limit in MB.

### 4. Configuring OSS/OSS-HDFS Access Keys
Instead of specifying the OSS/OSS-HDFS Access Key and Secret in the command every time, you can preconfigure them in Hadoop's `core-site.xml` file to avoid repetitive input:

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
</configuration>
```

### 5. Configuring the OSS-HDFS Endpoint

When accessing OSS-HDFS, you need to configure the Endpoint (`cn-xxx.oss-dls.aliyuncs.com`) differently from the OSS object interface Endpoint (`oss-cn-xxx-internal.aliyuncs.com`). JindoSDK accesses OSS-HDFS or OSS object interfaces based on the configured Endpoint.

When using OSS-HDFS, it's recommended to use the format `oss://<Bucket>.<Endpoint>/<Object>` for paths, e.g., `oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test`. This way, JindoSDK accesses the corresponding OSS-HDFS interface based on the Endpoint in the path.

JindoSDK also supports additional ways to configure Endpoints. For details, refer to [OSS/OSS-HDFS Endpoint Configuration](../jindosdk/jindosdk_endpoint_configuration.md).

**Note**:
Starting from version 4.4.0, JindoSDK defaults to domain separation for data reads and writes, using the standard internal OSS domain. If running Distcp commands outside of the Alibaba Cloud intranet, you'll need to configure the public OSS endpoint for data flow access in Hadoop's `core-site.xml`:

```xml
<configuration>
    <property>
        <name>fs.oss.data.endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

### 6. Other Features
For more features and usage instructions, refer to:
* [JindoDistCp User Guide](jindodistcp_quickstart.md)