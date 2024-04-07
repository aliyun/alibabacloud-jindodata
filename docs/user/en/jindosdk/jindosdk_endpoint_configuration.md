# Configuring the Alibaba Cloud OSS-HDFS Service (JindoFS Service) Endpoint

There are three ways to specify the Endpoint, and JindoSDK follows this priority order when searching for it:

## Method One: Specifying the Endpoint in the Access Path (Recommended)

When accessing the OSS-HDFS service, it's recommended to use the following format for the access path: `oss://<Bucket>.<Endpoint>/<Object>`, such as `oss://example-oss-bucket.cn-shanghai.oss-dls.aliyuncs.com/Test`. This way, JindoSDK uses the Endpoint specified in the path to connect to the corresponding service interface.

## Method Two: Setting a Bucket-Level Endpoint

If you use the `oss://<Bucket>/<Object>` format without specifying the Endpoint, JindoSDK looks for a bucket-level Endpoint in the configuration. To configure a bucket-level Endpoint in Hadoop's `core-site.xml`, do the following:
```xml
<configuration>
    <property>
        <name>fs.oss.bucket.XXX.endpoint</name>
        <value>cn-xxx.oss-dls.aliyuncs.com</value>
    </property>
</configuration>
```
Note: Replace `XXX` with the name of your OSS-HDFS service bucket.

## Method Three: Configuring a Global Default Endpoint

If you use `oss://<Bucket>/<Object>` and there's no bucket-level Endpoint configured, JindoSDK falls back to the global default Endpoint. To set a default Endpoint for accessing the OSS-HDFS service, add the following to Hadoop's `core-site.xml`:
```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value>cn-xxx.oss-dls.aliyuncs.com</value>
    </property>
</configuration>
```
Remember to replace `cn-xxx.oss-dls.aliyuncs.com` with the actual Endpoint for your service region.