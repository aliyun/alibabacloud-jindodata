# Migrate data between different buckets for which OSS-HDFS is enabled

Usage notes

*   Make sure that the required environments are prepared and the related tools are downloaded. For more information, see Jindo DistCp overview.
    
*   If issues occur when you migrate data between buckets for which OSS-HDFS is enabled, refer to FAQ about Jindo DistCp to resolve the issues. You can also [create an issue](https://github.com/aliyun/alibabacloud-jindodata/issues/new) to provide feedback.
    

1. Copy data to OSS-HDFS

You can run the following command to copy data between different buckets: 

*   If the source and destination buckets reside in the same region and can be accessed by using the same AccessKey pair, you can run the following command to migrate data:
    

hadoop jar jindo-distcp-tool-${version}.jar --src oss://srcBucket.cn-xxx.oss-dls.aliyuncs.com/ --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --parallelism 10

|  Parameter  |  Description  |  Example  |
| --- | --- | --- |
|  \--src  |  The source path of the OSS-HDFS service.   |  oss://srcBucket.cn-xxx.oss-dls.aliyuncs.com/  |
|  \--dest  |  The destination path of the OSS-HDFS service.   |  oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/  |
|  \--hadoopConf  |  The AccessKey ID and AccessKey secret that are used to access OSS-HDFS.  |  \* Specify an AccessKey ID that is used to access OSS-HDFS:--hadoopConf fs.oss.accessKeyId=yourkey\* Specify an AccessKey secret that is used to access OSS-HDFS:--hadoopConf fs.oss.accessKeySecret=yoursecret  |
|  \--parallelism  |  The task parallelism. You can adjust the value of this parameter based on the cluster resources.   |  10  |

*   If the source and destination buckets reside in different regions or cannot be accessed by using the same AccessKey pair, you can run the following command to migrate data:
    

hadoop jar jindo-distcp-tool-${version}.jar --src oss://srcbucket.oss-cn-xxx.aliyuncs.com/ --dest oss://destBucket.oss-cn-xxx.aliyuncs.com/ --hadoopConf fs.oss.bucket.srcbucket.accessKeyId=yourkey --hadoopConf fs.oss.bucket.srcbucket.accessKeySecret=yoursecret --hadoopConf --hadoopConf fs.oss.bucket.destBucket.accessKeyId=yourkey --hadoopConf fs.oss.bucket.destBucket.accessKeySecret=yoursecret --parallelism 10

|  Parameter  |  Description  |  Example  |
| --- | --- | --- |
|  \--hadoopConf  |  The AccessKey IDs and AccessKey secrets that are used to access the source and destination buckets.  |  \* Specify an AccessKey ID that is used to access Bucket XXX:--hadoopConf fs.oss.bucket.XXX.accessKeyId=yourkey\* Specify an AccessKey secret that is used to access Bucket XXX:--hadoopConf fs.oss.bucket.XXX.accessKeySecret=yoursecret  |

2. Copy incremental files

If a Jindo DistCp job is interrupted, and some files fail to be copied to the destination directory, you can use the --update parameter to copy these files. If specific files are added to the source directory, you can also use the --update parameter to copy the incremental files to the destination directory.

hadoop jar jindo-distcp-tool-${version}.jar --src oss://srcBucket.oss-cn-xxx.aliyuncs.com/ --dest oss://destBucket.oss-cn-xxx.aliyuncs.com/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --update --parallelism 20

3. Specify a YARN queue and a bandwidth

You can run the following command to specify a YARN queue and a bandwidth for a Jindo DistCp job based on your business requirements:

hadoop jar jindo-distcp-tool-${version}.jar --src oss://srcBucket.oss-cn-xxx.aliyuncs.com/ --dest oss://destBucket.oss-cn-xxx.aliyuncs.com/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf mapreduce.job.queuename=yarnQueue --bandWidth 100 --parallelism 10

*   \-- hadoopConf mapreduce.job.queuename=yarnQueue: the name of the YARN queue.
    
*   \-- bandWidth: the bandwidth of a single instance. Unit: MB.
    

4. Store data in password-free mode or by using an AccessKey pair

In most cases, you need to specify the AccessKey ID and AccessKey secret of OSS-HDFS in the corresponding parameters. JindoDistCp allows you to specify the AccessKey ID and AccessKey secret in the core-site.xml file of Hadoop. This way, you do not need to repeatedly specify the AccessKey ID and AccessKey secret. 

<configuration\>

<property\>

<name\>fs.oss.accessKeyId</name\>

<value\>xxx</value\>

</property\>

<property\>

<name\>fs.oss.accessKeySecret</name\>

<value\>xxx</value\>

</property\>

</configuration\>

5. Configure the endpoint of OSS-HDFS

To access OSS-HDFS, you must configure an endpoint in the cn-xxx.oss-dls.aliyuncs.com format. The endpoint of OSS-HDFS is different from the endpoint that is used to access OSS. The endpoint that is used to access OSS is in the oss-cn-xxx-internal.aliyuncs.com format. JindoSDK accesses OSS-HDFS or OSS based on the endpoint that you configure. 

If you want to access OSS-HDFS, we recommend that you specify the access path in the oss://<Bucket>.<Endpoint>/<Object> format.

Example: oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test. 

The preceding directory contains the endpoint of OSS-HDFS. JindoSDK accesses OSS-HDFS based on the endpoint in the directory. JindoSDK allows you to use other methods to configure the endpoint of OSS-HDFS. For more information, see [Configure an endpoint to access OSS-HDFS](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_endpoint_configuration.md). 

6. Save metadata information

Use the preserveMeta parameter to allow metadata to be migrated when you migrate data. The metadata includes Owner, Group, Permission, Atime, Mtime, Replication, BlockSize, XAttrs, and ACL. 

hadoop jar jindo-distcp-tool-${version}.jar --src oss://srcBucket.oss-cn-xxx.aliyuncs.com/ --dest oss://destBucket.oss-cn-xxx.aliyuncs.com/ --preserveMeta

7. Other features

For information about how to use Jindo DistCp to migrate data, refer to the following topic:

*   [Usage notes of data migration by using Jindo DistCp](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_distcp/jindo_distcp_how_to.md)