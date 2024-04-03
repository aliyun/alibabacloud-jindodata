# Migrate data from semi-managed JindoFS to OSS-HDFS (JindoFS)

(This topic applies to JindoSDK 4.5.0 or later.)

Background information

OSS-HDFS (JindoFS) is a new storage service released by Alibaba Cloud. OSS-HDFS is compatible with HDFS API operations and supports multi-level storage directories. If you want to migrate data from semi-managed JindoFS to OSS-HDFS, you must enable OSS-HDFS for the Object Storage Service (OSS) bucket that stores data of semi-managed JindoFS. You must also enable the audit logging feature for semi-managed JindoFS. 

Step 1: Migrate full data

In full data migration mode, you can migrate the metadata in a directory from semi-managed JindoFS to a directory in OSS-HDFS at a time. You can migrate data to only first-level subdirectories in OSS-HDFS. 

Command syntax

jindo distjob -migrateImport -srcPath <srcPath\> -destPath <desPth\> -backendLoc <backendLoc\>

Parameters:

\-srcPath: the source path of semi-managed JindoFS. This parameter is required.

\-destPath: the destination path of OSS-HDFS. This parameter is required.

\-backendLoc: the OSS path that stores the source data of semi-managed JindoFS. This parameter is required.

Example

Semi-managed JindoFS contains a directory named jfs://my-cluster/foo. OSS-HDFS contains a directory named bar. The bucket of OSS-HDFS is named dlsbucket and the destination directory is oss://dlsbucket/bar. Run the following command to migrate full data from the jfs://mycluster/foo directory in semi-managed JindoFS to the bar directory in OSS-HDFS:

jindo distjob -migrateImport -srcPath jfs://my-cluster/foo -destPath oss://dlsbucket/bar/

Step 2: Generate change logs

To migrate incremental data of semi-managed JindoFS to OSS-HDFS, you must run the jindo tool to convert the audit logs of semi-managed JindoFS into change logs.  

Command syntax

jindo distjob -mkchangelog -auditLogDir <auditLogDir\> -changeLogDir <changeLogDir\> -startTime <startTime\>

Parameters:

\-auditLogDir: the path that stores the audit logs of semi-managed JindoFS.

\-changeLogDir: the path that stores the generated change logs.

\-startTime: the start time to convert audit logs.

Example

The audit logs of semi-managed JindoFS are stored in the oss://samplebuket/sysinfo/auditlog path. Change logs need to be stored in the oss://samplebuket/sysinfo/changelog path. Only audit logs that are generated from January 1, 2022 need to be converted into change logs. Run the following command to convert the audit logs of semi-managed JindoFS into change logs:

jindo distjob -mkchangelog -auditLogDir oss://samplebuket/sysinfo/auditlog -changeLogDir oss://samplebuket/sysinfo/changelog -startTime 2022-01-01-12:00:00

Step 3: Migrate incremental data

After you convert the audit logs into change logs in Step 2, you can run a command to migrate incremental data of semi-managed JindoFS. You can migrate incremental metadata of semi-managed JindoFS to OSS-HDFS as change logs.  

Command syntax

jindo distjob -migrateImport -srcPath <srcPath\> -destPath <desPth\> -changeLogDir <auditLogDir\> -backendLoc <backendLoc\> -update

Parameters:

\-srcPath: the source path of semi-managed JindoFS. This parameter is required.

\-destPath: the destination path of OSS-HDFS. This parameter is required.

\-changeLogDir: the directory to store change logs. The directory must be the same as the directory you specified in Step 2.

\-backendLoc: the OSS path that stores the source data of semi-managed JindoFS. This parameter is required.

\-update: indicates that incremental data is migrated. By default, full data is migrated.

Example

Semi-managed JindoFS contains a directory named jfs://my-cluster/foo. OSS-HDFS contains a directory named bar. The bucket for which OSS-HDFS is enabled is named dlsbucket and the destination directory is oss://dlsbucket/bar. The directory to store change logs is oss://logBucket/logDir/. Run the following command to migrate incremental data of semi-managed JindoFS:

jindo distjob -migrateImport -srcPath jfs://my-cluster/foo -destPath oss://dlsbucket/bar/ -changeLogDir oss://logBucket/logDir/ -update

Step 4: Migrate incremental data multiple times.

 To migrate incremental data from semi-managed JindoFS to OSS-HDFS multiple times, you can configure the \-startTime parameter to specify the time range to convert audit logs. Then, repeat Step 2 and Step 3.