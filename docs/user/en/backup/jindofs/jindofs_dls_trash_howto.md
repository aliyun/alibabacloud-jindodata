Use the trash feature of Alibaba Cloud OSS-HDFS (JindoFS)
(This topic applies to JindoSDK 4.5.1 and later.)
Overview
When you delete a file from JindoFS, the file is not immediately deleted. Instead, the file is moved to the following directory:
/user/<username>/.Trash/Current
 After 30 minutes, the file is moved from the Current directory to the following directory:
/user/<username>/.Trash/<timestamp>
Files that are deleted within a specific period of time are moved to a directory that has a timestamp. The timestamp indicates the time when the files were deleted and functions as a checkpoint. The directory is permanently deleted after **seven days**. Within seven days after you delete a file, you can find the file in the .Trash directory based on the time when the file was deleted. Then, you can restore the file by moving the file out of the .Trash directory. 
Take note that the trash feature depends on the cooperation between the client and the server. By default, the server periodically deletes files from the /user/<username>/.Trash directory. The client moves the files that you want to delete to the .Trash directory. 
Use the trash feature in Hadoop FileSystem Shell
hadoop fs -rm oss://bucket/a/b/c
By default, the trash feature is not enabled for Hadoop FileSystem Shell on the client. To enable the trash feature, add the following configuration to the core-site.xml configuration file:
<property>
<name>fs.trash.interval</name>
<value>1440</value>
</property>
The value must be greater than 0.Then, the client converts the preceding rm command to hadoop fs -mv oss://bucket/a/b/c /user/<username>/.Trash/Current/a/b/c. The trash feature works without your awareness. The server periodically clears files in the trash. If you want to immediately delete a file to free up storage space, add the -skipTrash parameter to the rm command. 
Use the trash feature in Hadoop ecosystem components
Components such as Hive, Spark, and Flink are not aware of the trash feature of JindoFS. When you run the delete command of the Hadoop-compatible file system (HCFS) to delete a file, the file is immediately deleted. JindoFS adopts a similar policy to open source Hadoop. To use the trash feature in Hadoop ecosystem components, you must explicitly run the rename command of FileSystem to move the file that you want to delete to the /user/<username>/.Trash/Current directory. The server periodically clears files from the trash.  

