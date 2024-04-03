Configure an endpoint to access OSS-HDFS (JindoFS)
You can use one of the following methods to configure an endpoint to access OSS-HDFS (JindoFS). By default, JindoSDK searches for an endpoint based on the following priorities:
Method 1: (Recommended) Configure an endpoint in the access path  
We recommend that you access OSS-HDFS by using an access path in the oss://<Bucket>.<Endpoint>/<Object> format. Example: oss://dls-chenshi-test.cn-shanghai.oss-dls.aliyuncs.com/Test. The access path contains the endpoint that you can use to access OSS-HDFS. JindoSDK accesses OSS-HDFS based on the endpoint in the path. 
Method 2: Configure a bucket-level endpoint 
If you use an access path in the oss://<Bucket>/<Object> format to access OSS-HDFS, JindoSDK searches for a bucket-level endpoint in the configurations. In this case, you can configure a bucket-level endpoint in the core-site.xml configuration file of Hadoop to point to OSS-HDFS. 
<configuration>
<property>
<name>fs.oss.bucket.XXX.endpoint</name>
<value>cn-xxx.oss-dls.aliyuncs.com</value>
</property>
</configuration>
Note: XXX indicates the name of the bucket for which OSS-HDFS is enabled. 
Method 3: Configure a global endpoint 
If you use an access path in the oss://<Bucket>/<Object> format to access OSS-HDFS and you do not configure a bucket-level endpoint, JindoSDK uses a global endpoint to access OSS-HDFS. If you want to access OSS-HDFS by default, you can use this method. You can configure a global endpoint in the core-site.xml configuration file of Hadoop. 
<configuration>
<property>
<name>fs.oss.endpoint</name>
<value>cn-xxx.oss-dls.aliyuncs.com</value>
</property>
</configuration>

