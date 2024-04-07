# Migrating Data from Third-party Cloud Object Storage to OSS/OSS-HDFS

### Preparations
Before starting, familiarize yourself with the [Introduction to JindoDistCp](jindodistcp_quickstart.md) and refer to the [JindoDistCp Troubleshooting Guide](jindodistcp_faq.md) for assistance.

### 1. Copy Data to Alibaba Cloud OSS/OSS-HDFS

Use the following command to copy a directory from third-party cloud object storage to OSS/OSS-HDFS:

```shell
hadoop jar jindo-distcp-tool-${version}.jar s3://example-s3-bucket/data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --parallelism 10
```

| Argument | Description | Example |
| --- | --- | --- |
| --src | Path in the third-party cloud object storage. | s3://example-s3-bucket/data/ |
| --dest | Destination path in OSS/OSS-HDFS. | oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/ |
| --hadoopConf | Specify the Access Key ID and Secret for OSS/OSS-HDFS/S3/COS/OBS. | --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret |
| --parallelism | Task concurrency, adjustable based on cluster resources. | 10 |

### 2. Incremental File Copying

If the Distcp job fails for any reason and you want to continue where it left off or if new files have been added to the source since the last Distcp run, do the following after completing the previous Distcp task:

#### Use the `--update` Command to Get a List of Remaining Files

```shell
hadoop jar jindo-distcp-tool-${version}.jar --src s3://example-s3-bucket/data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --update --parallelism 20
```

### 3. YARN Queue and Bandwidth Selection

Limit the YARN queue and bandwidth for the DistCp job with the following command:

```shell
hadoop jar jindo-distcp-tool-${version}.jar --src s3://example-s3-bucket/data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf mapreduce.job.queuename=yarnQueue --bandWidth 100 --parallelism 10
```

* `--hadoopConf mapreduce.job.queuename=yarnQueue`: Specify the YARN queue name.
* `--bandWidth`: Specify the single-node bandwidth limit in MB.

### 4. Configuring Access Keys for Third-party Cloud Object Storage

Provide access keys to obtain permissions to access third-party cloud object storage. You can specify these keys using the `--hadoopConf` option:

S3 example:
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src s3://example-s3-bucket/data/hourly_table --dest oss://example-oss-bucket/hourly_table --hadoopConf fs.s3.accessKeyId=yourkey --hadoopConf fs.s3.accessKeySecret=yoursecret
```

COS example:
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src cos://example-cos-bucket/data/hourly_table --dest oss://example-oss-bucket/hourly_table --hadoopConf fs.cos.accessKeyId=yourkey --hadoopConf fs.cos.accessKeySecret=yoursecret
```

OBS example:
```shell
hadoop jar jindo-distcp-tool-${version}.jar --src obs://example-obs-bucket/data/hourly_table --dest oss://example-oss-bucket/hourly_table --hadoopConf fs.obs.accessKeyId=yourkey --hadoopConf fs.obs.accessKeySecret=yoursecret
```

Alternatively, you can pre-configure these keys in Hadoop's `core-site.xml` to avoid repeatedly entering them:

```xml
<configuration>
    <property>
        <name>fs.s3.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.s3.accessKeySecret</name>
        <value>xxx</value>
    </property>
</configuration>
```

For OSS/HDFS access keys, configure them similarly but replace `s3` with `oss`:

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

When accessing OSS-HDFS, specify the endpoint (`cn-xxx.oss-dls.aliyuncs.com`) which differs from the OSS object interface endpoint (`oss-cn-xxx-internal.aliyuncs.com`). JindoSDK will access either OSS-HDFS or OSS object interfaces based on the configured endpoint.

When using OSS-HDFS, recommend specifying the path format as follows: `oss://<Bucket>.<Endpoint>/<Object>`.

For example: `oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test`.

JindoSDK supports multiple endpoint configuration methods; refer to [OSS/OSS-HDFS Endpoint Configuration](../jindosdk/jindosdk_endpoint_configuration.md) for details.

**Note:** Starting from version 4.4.0, JindoSDK uses domain separation by default for data read/write operations. If running outside of Alibaba Cloud's intranet, configure the public OSS endpoint for data flow access in Hadoop's `core-site.xml`:

```xml
<configuration>
    <property>
        <name>fs.oss.data.endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

### 6. Additional Features

Refer to the [JindoDistCp User Guide](jindodistcp_quickstart.md) for more features and usage instructions.