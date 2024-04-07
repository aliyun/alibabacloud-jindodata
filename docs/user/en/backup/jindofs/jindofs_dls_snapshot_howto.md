# Use the snapshot feature of Alibaba Cloud OSS-HDFS (JindoFS)

(This topic applies to JindoSDK 4.0.0 or later.)

OSS-HDFS (JindoFS) is a new storage service that is developed based on Object Storage Service (OSS). OSS-HDFS is compatible with Hadoop Distributed File System (HDFS) APIs and supports multi-level storage directories. You can use JindoSDK 4.X to access OSS-HDFS. For information about how to enable OSS-HDFS for a bucket and use the basic features of OSS-HDFS, see [Getting started with OSS-HDFS (JindoFS)](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_dls_quickstart.md). 

You can use the snapshot feature of OSS-HDFS in the same manner as the snapshot feature of HDFS. This topic describes the common operations that you can perform when you use the snapshot feature of OSS-HDFS. In the following sample commands, a bucket named oss-dfs-test is used, and OSS-HDFS is enabled for the bucket. 

Enable the snapshot feature

You can run the following command to create a directory named TestSnapshot in the oss-dfs-test bucket: 

hdfs dfs -mkdir oss://oss-dfs-test.<Endpoint\>/TestSnapshot

By default, the snapshot feature is disabled for a directory. To enable and disable the snapshot feature for a directory, you must run JindoSDK Shell commands. To enable the snapshot feature for a directory, run the following JindoSDK Shell command:

jindo admin -allowSnapshot -dlsUri <path\>

For example, you can run the following command to enable the snapshot feature for the TestSnapshot directory:  

jindo admin -allowSnapshot -dlsUri oss://oss-dfs-test.<Endpoint\>/TestSnapshot

Create a snapshot

After you enable the snapshot feature for a directory, you can run the following HDFS Shell command to create a snapshot for the directory:  

hdfs dfs -createSnapshot <path\> \[<snapshotName\>\]

For example, after you perform operations on the TestSnapshot directory and files in the directory, you can create a snapshot named S1 for the TestSnapshot directory to save the status of the directory.  

# Run the following commands to create subdirectories and files in the TestSnapshot directory for testing:

hdfs dfs -mkdir oss://oss-dfs-test.<Endpoint\>/TestSnapshot/dir1

hdfs dfs -mkdir oss://oss-dfs-test.<Endpoint\>/TestSnapshot/dir2

hdfs dfs -touchz oss://oss-dfs-test.<Endpoint\>/TestSnapshot/dir1/file1

hdfs dfs -touchz oss://oss-dfs-test.<Endpoint\>/TestSnapshot/dir3/file2

# Run the following command to create a snapshot named S1 for the TestSnapshot directory:

hdfs dfs -createSnapshot oss://oss-dfs-test.<Endpoint\>/TestSnapshot S1

Access directories and files in a snapshot

Format of a directory or file in a snapshot

To distinguish original directories and files in a bucket from directories and files in a snapshot, you must specify the snapshot name to access directories and files in the snapshot. If the snapshot feature is enabled for a directory, and you want to access a directory or file in a snapshot of the directory, specify the path of the directory or file in the following format:

<snapshotRoot\>/.snapshot/<snapshotName\>/<actual subPath\>

In the preceding path, the snapshotRoot parameter specifies the root directory of a snapshot. The root directory is the path specified by the dlsUri parameter in the command that is used to enable the snapshot feature. The snapshotName parameter specifies the name of the snapshot. The actual subPath parameter specifies the path of the directory or file to be accessed in the root directory of the snapshot. In this example, the root directory is TestSnapshot. If you want to query files in the oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir1 directory, you can run the following Is command:  

hdfs dfs -ls oss://oss-dfs-test.<Endpoint\>/TestSnapshot/dir1

 In this example, you can also run the following command to query the directories and files in the S1 snapshot: 

hdfs dfs -ls oss://oss-dfs-test.<Endpoint\>/TestSnapshot/.snapshot/S1/dir1

In the preceding command, .snapshot/S1 specifies the snapshot that you want to access. 

Use a snapshot to restore data

You can use snapshots to back up and restore data. If you accidentally delete important data, you can use a snapshot to restore the data. You can access data in a snapshot by using the path format described in the preceding section. Then, you can restore the data based on your business requirements. 

For example, you accidentally delete the oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir1 directory.

hdfs dfs -rm -r oss://oss-dfs-test.<Endpoint\>/TestSnapshot/dir1

You can run the following command to use the S1 snapshot that you created for the /TestSnapshot directory to restore the deleted directory: 

hdfs dfs -cp oss://oss-dfs-test.<Endpoint\>/TestSnapshot/.snapshot/S1/dir1 oss://oss-dfs-test.<Endpoint\>/TestSnapshot

Then, you can run the following command to check whether the directory is restored: 

hdfs dfs -ls oss://oss-dfs-test.<Endpoint\>/TestSnapshot/dir1

Rename a snapshot

You can run the following command to rename an existing snapshot:

hdfs dfs -renameSnapshot <path\> <oldName\> <newName\>

For example, you can run the following command to rename the S1 snapshot to S100:

hdfs dfs -renameSnapshot oss://oss-dfs-test.<Endpoint\>/TestSnapshot S1 S100

Delete a snapshot

If you no longer require a snapshot, you can run the following command to delete the snapshot: 

hdfs dfs -deleteSnapshot <path\> <snapshotName\>

For example, you can run the following command to delete the S100 snapshot that you obtained by creating and renaming the S1 snapshot:

hdfs dfs -deleteSnapshot oss://oss-dfs-test.<Endpoint\>/TestSnapshot S100

Disable the snapshot feature

To disable the snapshot feature for a directory, you must also run a JindoSDK Shell command. Command format:

jindo admin -disallowSnapshot -dlsUri <path\>

Compare snapshots

To view the differences between two snapshots, run the following command:

jindo dls -snapshotDiff -dlsUri <uri\> -fromSnapshot <fromSnapshot\> -toSnapshot <toSnapshot\>

Notes

Make sure that all snapshots of a directory are deleted before you disable the snapshot feature for the directory. For more information about how to delete snapshots, see the "Delete a snapshot" section in this topic. If the directory still contains snapshots, an error occurs when you disable the snapshot feature for the directory. For example, you can run the following command to disable the snapshot feature for the TestSnapshot directory after all snapshots of the directory are deleted:  

jindo admin -disallowSnapshot -dlsUri oss://oss-dfs-test.<Endpoint\>/TestSnapshot