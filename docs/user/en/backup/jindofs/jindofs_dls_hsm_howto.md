Use the tiered storage feature of Alibaba Cloud OSS-HDFS (JindoFS)
(This topic applies to JindoSDK 4.6.2 and later.)
Alibaba Cloud OSS-HDFS (JindoFS) is a storage service released by Alibaba Cloud and is compatible with the Hadoop Distributed File System (HDFS) API. OSS-HDFS supports multi-level storage directories. You can use JindoSDK 4.X to access OSS-HDFS. For information about how to enable OSS-HDFS for a bucket and use the basic features of OSS-HDFS, see [Getting started with OSS-HDFS (JindoFS)](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_dls_quickstart.md). 
This topic describes the common operations that you can perform when you use the tiered storage feature of OSS-HDFS. 
In the following sample commands, a bucket named oss-dfs-test in the oss:// directory is used, and OSS-HDFS is enabled for the bucket. 
Overview of tiered storage
The amount of data that is stored in a data lake continuously increases over time. However, not all data in the data lake is frequently accessed. You may no longer need to access old data, such as the data that is stored for more than one year, but you still need to keep the data stored in the data lake for compliance, archive, and other specific purposes. In this case, you can use the tiered storage feature to store data in different tiers to reduce the overall storage cost. You can select a low-cost storage medium to store infrequently accessed data. In most cases, the costs for accessing data that is stored in a low-cost storage medium are relatively high. 
OSS provides the following storage classes for various data storage scenarios: Standard, Infrequent Access (IA), Archive, and Cold Archive. JindoFS uses OSS as the storage backend of data blocks. You can use the tiered storage feature of JindoFS to specify storage classes for data blocks that are stored in OSS. 
Use tiered storage
Before you use the tiered storage feature, make sure that JindoSDK is correctly configured, and you can access JindoFS. The tiered storage feature of JindoFS is compatible with Hadoop-compatible file system (HCFS) API operations. Related API operations:
public abstract class FileSystem extends Configured implements Closeable {
// ......
    public void setStoragePolicy(Path src, String policyName) throws IOException;
    public BlockStoragePolicySpi getStoragePolicy(Path src) throws IOException;
    public Collection<?extends BlockStoragePolicySpi> getAllStoragePolicies() throws IOException;
}
JindoFS also allows you to run the following commands to specify storage classes:
jindo fs -listPolicies
jindo fs -setStoragePolicy -path <path> -policy <policy>
jindo fs -getStoragePolicy -path <path>
You can use the getAllStoragePolicies() method or the listPolicies command to obtain all available storage classes. The following storage classes are supported: CLOUD_STD (Standard), CLOUD_IA (IA), CLOUD_AR (Archive), and CLOUD_COLD_AR (Cold Archive). 
You can use the setStoragePolicy() method or the setStoragePolicy command to specify storage classes for data stored in specific paths. JindoFS changes the storage levels of objects at the backend based on the specified storage class. For example, you can run the following command to specify the Archive storage class for data in the oss://oss-dfs-test/dir1 directory:
jindo fs -setStoragePolicy -path oss://oss-dfs-test/dir1 -policy CLOUD_AR
Take note that the default storage class of an object or a directory is UNSPECIFIED, which indicates that no default storage class is specified. In this case, the storage class that is specified for a parent directory at the nearest level is used. For example, the CLOUD_STD storage class is specified for the oss://oss-dfs-test/dir2 directory. In this case, the CLOUD_STD storage class is also used for the oss://oss-dfs-test/dir2/subdir2/subsubdir2 directory if you do not specify a storage class for the oss://oss-dfs-test/dir2/subdir2/ or oss://oss-dfs-test/dir2/subdir2/subsubdir2 directory. 
You can use the getStoragePolicy() method or the getStoragePolicy command to query the final storage class that takes effect for data in a specific path. Example:
jindo fs -getStoragePolicy -path oss://oss-dfs-test/dir1/file1
If null is returned after the getStoragePolicy() method is called, the final storage class that takes effect for data in the specified path is UNSPECIFIED. In this case, the Standard storage class is used for the data in the path. 
Directory nesting
JindoFS **does not support nesting policies**. For example, the CLOUD_AR storage class is specified for the oss://oss-dfs-test/warehouse/dwd.db/dt=20220101 directory, and then the CLOUD_STD storage class is specified for the oss://oss-dfs-test/warehouse/dwd.db parent directory. In this case, you cannot unarchive and archive data. We recommend that **you modify only an existing storage class that is specified for a directory, but not modify the storage classes specified for the parent and child directories of the current directory.**
Examples of switchover between storage classes in various storage scenarios
Switchover from the Standard storage class to the IA storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_IA

Switchover from the Standard storage class to the Archive storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_AR

Switchover from the Standard storage class to the Cold Archive storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_COLD_AR

Switchover from the IA storage class to the Archive storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_AR

Switchover from the IA storage class to the Cold Archive storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_COLD_AR

Switchover from the IA storage class to the Standard storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_STD

Switchover from the Archive storage class to the Cold Archive storage class
Not supported

Switchover from the Cold Archive storage class to the Archive storage class
Not supported

Switchover from the Archive storage class to the Standard storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_STD

Switchover from the Cold Archive storage class to the Standard storage class
jindo fs -setStoragePolicy -path <path> -policy CLOUD_STD
Configure an OSS lifecycle rule
When you use the tiered storage feature to archive data that is stored in OSS-HDFS, you must use the OSS lifecycle rule feature.
jindo fs -setStoragePolicy -path oss://oss-dfs-test/dir1 -policy CLOUD_AR
You can run the preceding command to only add a tag to the data stored in a specific directory. The key-value pair of the tag is transition-storage-class:Archive. In this case, you must configure a lifecycle rule for data conversion. You can configure a rule of matching by the .dlsdata prefix. The following figure shows the settings of a lifecycle rule.
![](https://intranetproxy.alipay.com/skylark/lark/0/2024/png/8042/1711970140379-d1d16ea1-ac45-4261-9fcc-191ff4cd8956.png#)You can complete data archiving only after the lifecycle rule that you configured is executed. For more information about how to configure a lifecycle rule, see [Lifecycle rules based on the last modification time](https://help.aliyun.com/document_detail/31904.html).
Precautions

- If you want to use the Archive or Cold Archive storage class, you must first configure a lifecycle rule in the OSS console. Otherwise, the storage class of data blocks cannot be switched to the Archive or Cold Archive storage class. 
- The storage status that you queried in JindoFS only indicates that JindoFS has distributed the data to corresponding data blocks that are stored in OSS based on the preset lifecycle rule, but does not indicate that the storage class of the data blocks is switched. 
- Lifecycle management of data in OSS requires a long period of time. The objects for which the Archive or Cold Archive storage class is specified may finish archiving after a maximum of 24 hours. 
- You cannot access the data whose storage class is Archive or Cold Archive. If you want to access such data, switch the storage class to Standard. The switchover requires a specific period of time. You can run the checkStoragePolicy command to query the status of a switchover. 
- Additional fees are generated when you retrieve data whose storage class is Archive or Cold archive. We recommend that you do not specify the Archive or Cold Archive storage class for the data that you may occasionally access. 
- If you rename an object across directories for which different storage classes are specified, a background task is automatically generated to ensure that the object you renamed meets the storage class requirements of the destination directory. 
- The current version of JindoSDK does not allow you to create objects in a directory for which the IA, Archive, or Cold Archive storage class is specified. You can create and close an object in a directory for which the Standard storage class is specified. Then, rename the object to add the object to the directory for which the IA, Archive, or Cold Archive storage class is specified. 

