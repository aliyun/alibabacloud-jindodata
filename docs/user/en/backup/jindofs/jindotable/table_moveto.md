# Use JindoTable to migrate Hive tables and partitions to OSS-HDFS (JindoFS)

Hadoop Distributed File System (HDFS) cannot be infinitely scaled out due to limits on the cluster size and scaling costs. As a result, capacity bottlenecks occur in HDFS. Object Storage Service (OSS)-HDFS serves as an alternative or supplement to HDFS to expand the storage capabilities of a Hadoop platform in the cloud. JindoTable allows you to filter Hive data based on the partition key and migrate partitions between HDFS and OSS-HDFS. 

Prerequisites

JindoSDK is deployed.

For more information, see [Getting Started with Alibaba Cloud OSS-HDFS (JindoFS)](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_dls_quickstart.md). 

Hadoop and Hive are deployed.

*   The hadoop classpath command can return a valid path.
    
*   The client environment variables $HIVE\_HOME and $HIVE\_CONF\_DIR are correctly configured.
    

Specify an HDFS directory for the moveTo tool to store lock files

Add the jindotable.moveto.tablelock.base.dir configuration item to the core-site.xml or hdfs-site.xml configuration file of Hadoop. The configuration files reside in the $HADOOP\_CONF\_DIR directory.

The value of the configuration item must point to an HDFS directory. The directory is used to store the lock files that are automatically created when the moveTo tool is running. Make sure that only the moveTo tool has permissions to access the directory. If you do not configure the configuration item, the hdfs:///tmp/jindotable-lock/ directory is used. If the moveTo tool does not have the permissions to access the default directory, an error is reported. 

Usage notes

Obtain help information

Run the following command to obtain help information about the moveTo tool: 

jindotable -help moveTo

Parameters

jindotable -moveTo -t <dbName.tableName\> -d <destination path\> \[-c "<condition>" | \-fullTable\] \[-b/-before <before days\>\] \[-p/-parallel <parallelism\>\] \[-s/-storagePolicy <OSS storage policy\>\] \[-o/-overWrite\] \[-r/-removeSource\] \[-skipTrash\] \[-e/-explain\] \[-q/-queue <yarn queue\>\] \[-w/-workingDir <working directory\>\]\[-l/-logDir <log directory\>\]

|  Parameter  |  Description  |  Required  |
| --- | --- | --- |
|  \-t <dbName.tableName>  |  The table that you want to migrate.   |  Yes  |
|  \-d <destination path>  |  The destination directory to which you want to migrate the table. The directories of partitions are automatically created in this directory.   |  Yes  |
|  \-c "<condition>" / -fullTable  |  The expression of the filter condition for partitions. Basic operators are supported. User-defined functions (UDFs) are not supported.   |  No  |
|  \-b/before <before days>  |  The time condition for migrating partitions. Unit: days. Only the partitions that were created the specified number of days ago are migrated.   |  No  |
|  \-p/-parallel <parallelism>  |  The maximum parallelism of the task that is run by using the moveTo tool. Default value: 1.   |  No  |
|  \-s/-storagePolicy <OSS storage policy>  |  This parameter is unavailable for OSS-HDFS.  |  No  |
|  \-o/-overWrite  |  Specifies whether to overwrite data in the destination directories. Only data in the directories of migrated partitions are overwritten. The directories of partitions that are not migrated remain unchanged.   |  No  |
|  \-r/-removeSource  |  Specifies whether to delete the source data after the migration is complete.   |  No  |
|  \-skipTrash  |  Specifies whether to skip the data in the trash bin when the system deletes source data.   |  No  |
|  \-e/-explain  |  Specifies whether to use the explain mode. In explain mode, the system displays the partitions to be migrated but does not migrate the partitions.   |  No  |
|  \-q/-queue <yarn queue  |  The YARN queue for distributed copy.   |  No  |
|  \-w/-workingDir  |  The temporary working directory for distributed copy.   |  No  |
|  \-l/-logDir <log directory>  |  The on-premises log directory. Default value: /tmp/<current user>/.  |  No  |

Example

1.  Create a Hive partitioned table in HDFS. The following figure shows information about the table.
    

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/2M9qPBXQQoJDl015/img/ff147ad9-837f-46b6-a16d-7e7747f0b500.png?x-oss-process=image/crop,x_0,y_0,w_1453,h_675)

1.  Before you migrate partitions to OSS-HDFS, add the -e or -explain parameter to the moveTo command to check whether the partitions meet your expectations. In this example, the bbb and ccc partitions need to be migrated.
    

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/2M9qPBXQQoJDl015/img/b74e8555-e3f2-4681-a47f-3edf01496855.png?x-oss-process=image/crop,x_0,y_0,w_1663,h_305)

1.  Remove the -e parameter and run the command to migrate the partitions.
    

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/2M9qPBXQQoJDl015/img/3ffbddf0-f477-4542-88e2-ffa326104d3b.png?x-oss-process=image/crop,x_0,y_0,w_1626,h_396)

1.  After you run the command, check whether the partitions exist in OSS-HDFS.
    

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/2M9qPBXQQoJDl015/img/3f7e8269-ad48-4f54-9c91-6c361aa63621.png?x-oss-process=image/crop,x_0,y_0,w_1060,h_649)

1.  Migrate the partitions back to HDFS. The migration fails because the destination directories are not empty. You can add the -overWrite parameter to overwrite data in the destination directories.
    

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/2M9qPBXQQoJDl015/img/71b9f091-bbdd-419c-9f94-d3c95f604aa8.png?x-oss-process=image/crop,x_0,y_0,w_1329,h_348)

1.  Add the -overWrite parameter to forcefully migrate the partitions by overwriting data in the destination directories.
    

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/2M9qPBXQQoJDl015/img/a91e20ea-dc61-4531-8ab9-82557b454d5c.png?x-oss-process=image/crop,x_0,y_0,w_1458,h_395)

Exception handling

To ensure data security and prevent dirty data, the jindotable -moveTo command automatically checks a destination directory to ensure that no other command is run to copy data to the same directory. If another command is run to copy data to the same directory, the moveTo command for migrating a table or partitions fails. In this case, you must manually stop the command that is run to copy data to the destination directory and clear the directory. Then, rerun the moveTo command. 

For a non-partitioned table, the destination directory is the directory in which the table is stored. For a partitioned table, each partition is copied or moved to its own destination directory. You need to only clear the directories for partitions that you want to copy or move. 

Causes

If a moveTo command is unexpectedly aborted, manual intervention may be required. If a command is aborted, the copying process is not complete, and the source data and the metadata of the table remain unchanged. Therefore, the data is still in the safe state. Common causes that lead to unexpected command aborting:

*   The command process is killed by a user before the running of the command ends.
    
*   An exception such as memory overflow occurs. As a result, the command process is terminated.
    

If the copy fails and Conflicts found is displayed during the copying process of a table or partition, manual intervention is required. 

Solutions

*   Make sure that no other commands, especially distributed copy commands such as DistCp or JindoDistCp, are run to copy data to the same directory.  
    
*   Delete the destination directories. For non-partitioned tables, delete the table-level directory. For partitioned tables, delete the partition-level directory that conflicts. Do not delete the source directory.