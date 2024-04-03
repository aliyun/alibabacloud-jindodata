# Migrate data from HDFS to OSS-HDFS

Usage notes

*   Make sure that the required environments are prepared and the related tools are downloaded. For more information, see Use Jindo DistCp.
    
*   If issues occur when you migrate data from Hadoop Distributed File System (HDFS) to OSS-HDFS, refer to FAQ about Jindo DistCp to resolve the issues. You can also [create an issue](https://github.com/aliyun/alibabacloud-jindodata/issues/new) to provide feedback.
    

1. Copy data to Alibaba Cloud OSS-HDFS (JindoFS)

You can run the following command to copy a data storage directory from HDFS to OSS-HDFS:

hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --parallelism 10

|  Parameter  |  Description  |  Example  |
| --- | --- | --- |
|  \--src  |  The source data storage directory of HDFS.   |  /data  |
|  \--dest  |  The destination OSS-HDFS directory.   |  oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/  |
|  \--hadoopConf  |  Specifies an AccessKey ID and AccessKey secret that are used to access OSS-HDFS.  |  \* Specify an AccessKey ID that is used to access OSS-HDFS:--hadoopConf fs.oss.accessKeyId=yourkey\* Specify an AccessKey secret that is used to access OSS-HDFS:--hadoopConf fs.oss.accessKeySecret=yoursecret  |
|  \--parallelism  |  The task parallelism. You can adjust the value of this parameter based on the cluster resources.   |  10  |

2. Copy incremental files

If a Jindo DistCp job is interrupted, and some files fail to be copied to the destination directory, you can use the --update parameter to copy these files. If specific files are added to the source directory, you can also use the --update parameter to copy the incremental files to the destination directory.

hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --update --parallelism 20

3. Specify a YARN queue and a bandwidth

Run the following command to specify a YARN queue and a bandwidth for a Jindo DistCp job based on your business requirements:

hadoop jar jindo-distcp-tool-${version}.jar --src /data --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --hadoopConf fs.oss.accessKeyId=yourkey --hadoopConf fs.oss.accessKeySecret=yoursecret --hadoopConf mapreduce.job.queuename=yarnQueue --bandWidth 100 --parallelism 10

*   \--hadoopConf mapreduce.job.queuename=yarnQueue: the name of the YARN queue.
    
*   \--bandWidth: the bandwidth for a single ECS instance, in MB/s.
    

4. Store an AccessKey pair

In most cases, you must specify the AccessKey ID and AccessKey secret that are used to access OSS-HDFS in parameters. Jindo DistCp allows you to write the AccessKey ID and AccessKey secret to the core-site.xml configuration file of Hadoop in advance. This way, you do not need to repeatedly specify the AccessKey pair. 

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

If you want to access OSS-HDFS, we recommend that you specify an access path in the oss://<Bucket>.<Endpoint>/<Object> format.

Example: oss://mydlsbucket.cn-shanghai.oss-dls.aliyuncs.com/Test. 

The preceding access path contains the endpoint of OSS-HDFS. JindoSDK accesses OSS-HDFS based on the endpoint in the access path. JindoSDK also allows you to use other methods to configure an endpoint that is used to access OSS-HDFS. For more information, see [Configure an endpoint to access OSS-HDFS](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/configuration/jindosdk_endpoint_configuration.md). 

Note: By default, JindoSDK 4.4.0 or later uses different domain names to access OSS-HDFS in different scenarios, and the domain name that you use when you read and write data is a standard OSS domain name. If you run the distcp command in a public network environment, you must configure a public endpoint of OSS in the core-site.xml configuration file of Hadoop to access data. 

<configuration\>

<property\>

<name\>fs.oss.data.endpoint</name\>

<value\>oss-cn-xxx.aliyuncs.com</value\>

</property\>

</configuration\>

6. Save metadata information

Use the preserveMeta parameter to allow metadata to be migrated when you migrate data. The metadata includes Owner, Group, Permission, Atime, Mtime, Replication, BlockSize, XAttrs, and ACL. 

hadoop jar jindo-distcp-tool-${version}.jar --src /opt/tmp --dest oss://destBucket.cn-xxx.oss-dls.aliyuncs.com/dir/ --preserveMeta

7. Use other features

For information about how to use Jindo DistCp to migrate data, refer to the following topic:

*   [Usage notes of data migration by using Jindo DistCp](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_distcp/jindo_distcp_how_to.md)