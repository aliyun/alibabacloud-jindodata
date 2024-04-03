# Use RootPolicy to access OSS-HDFS (JindoFS)

(This topic applies to JindoSDK 4.6.0 or later.)

Background information

OSS-HDFS allows you to use RootPolicy to configure a custom prefix for OSS-HDFS. This way, you can run jobs on OSS-HDFS without the need to modify the original access prefix hdfs://. 

1. Register an access address that contains a custom prefix for a bucket

You can register a custom access address for a specific bucket by running a Shell command that is provided by E-MapReduce (EMR). You can run the [setRootPolicy](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindofs_dls_shell_howto.md) command to register an access address that contains a custom prefix for a bucket. Sample command:

jindo admin -setRootPolicy oss://<bucket\_name\>.<dls\_endpoint\>/ hdfs://<your\_ns\_name\>/

*   <bucket\_name>: the name of the bucket for which OSS-HDFS is enabled. Only the root directory of the bucket is supported. 
    
*   <dls\_endpoint>: the endpoint of OSS-HDFS. For example, the endpoint of OSS-HDFS in the China (Hangzhou) region is cn-hangzhou.oss-dls.aliyuncs.com. If the core-site.xml file of Hadoop contains one of the following configuration items, you do not need to repeatedly specify the endpoint in subsequent operations.
    

<configuration\>

<property\>

<name\>fs.oss.endpoint</name\>

<value\><dls\_endpoint\></value\>

</property\>

Or

<property\>

<name\>fs.oss.bucket.<bucket\_name\>.endpoint</name\>

<value\><dls\_endpoint\></value\>

</property\>

</configuration\>

In the preceding sample code, oss://<bucket\_name>.<dls\_endpoint>/ can be simplified into oss://<bucket\_name>/. This way, the endpoint information that is required in the following operations can be omitted. 

*   <your\_ns\_name>: the name of the namespace in which OSS-HDFS resides. The value is a non-empty string, such as test. Only the root directory of the namespace is supported. 
    

2. Configure the discovery address of an access policy and the implementation class of a scheme

You must add the following configuration items to the core-site.xml file of Hadoop:

<configuration\>

<property\>

<name\>fs.accessPolicies.discovery</name\>

<value\>oss://<bucket\_name\>.<dls\_endpoint\>/</value\>

</property\>

<property\>

<name\>fs.AbstractFileSystem.hdfs.impl</name\>

<value\>com.aliyun.jindodata.hdfs.v28.HDFS</value\>

</property\>

<property\>

<name\>fs.hdfs.impl</name\>

<value\>com.aliyun.jindodata.hdfs.v28.JindoDistributedFileSystem</value\>

</property\>

</configuration\>

If you want to configure the discovery address of the access policy and scheme implementation class for multiple buckets, separate the buckets with commas (,). 

3. Verify the RootPolicy configurations and use RootPolicy

Run the following hadoop command to verify the configurations of RootPolicy: 

hadoop fs -ls hdfs://<your\_ns\_name\>/

After the verification is passed, restart the related service, such as Hive or Spark, to access OSS-HDFS by using an access address that contains a custom prefix. 

4. Other features

4.1. Delete registered access addresses that contain a custom prefix specified for a bucket

You can run the [UnsetRootPolicy](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindofs_dls_shell_howto.md) command to delete all registered access addresses that contain a custom prefix specified for a bucket. 

jindo admin -unsetRootPolicy oss://<bucket\_name\>.<dls\_endpoint\>/ hdfs://<your\_ns\_name\>/

4.2. Query registered access addresses that contain a custom prefix specified for a bucket

You can run the [listAccessPolicies](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindofs_dls_shell_howto.md) command to query all registered access addresses that contain a custom prefix specified for a bucket. 

jindo admin -listAccessPolicies oss://<bucket\_name\>.<dls\_endpoint\>/