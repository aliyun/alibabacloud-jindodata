Use Shell commands in Alibaba Cloud OSS-HDFS (JindoFS)
(This topic applies to JindoSDK 4.5.0 and later.)
Use commands that are relevant to Hadoop proxy users
AddProxyUser
Adds a proxy user. 
jindo admin -addProxyUser
[-dlsUri <uri>]
[-proxyUser <proxyUser>]
[-users <user1,user2...>]|[-groups <group1,group2...>]
[-hosts <host1,host2...>]
In a command, you can configure either the users or groups parameter. Example:
jindo admin -addProxyUser
-dlsUri oss://jindosdk-dls-unit-test/
-proxyUser hive
-groups group1,group2
-hosts host1,host2
You can run the preceding command to specify the user hive as a proxy user for all users who belong to group1 or group2 and send requests from host1 or host2. 
DeleteProxyUser
Deletes a proxy user. 
jindo admin -deleteProxyUser
[-dlsUri <uri>]
[-proxyUser <proxyUser>]
Example:
jindo admin -deleteProxyUser
-dlsUri oss://jindosdk-dls-unit-test/
-proxyUser hive
You can run the preceding command to delete a proxy user named hive. This way, hive cannot be used as a proxy user for any users. 
ListProxyUsers
Queries all proxy users and proxy information. 
jindo admin -listProxyUsers
[-dlsUri <uri>]
[-maxKeys <maxKeys>]
[-marker <marker>]
The -maxKeys option specifies the number of proxy users that you want to query. The -marker option filters proxy users whose names contain a specific string. The -maxKeys and-marker options are optional. Example:
jindo admin -listProxyUsers
-dlsUri oss://jindosdk-dls-unit-test/
-maxKeys 10
-marker hive
You can run the preceding command to query information about 10 proxy users whose names contain the hive string in the oss://jindosdk-dls-unit-test/ path. 
Use commands that are relevant to user group mappings
AddUserGroupsMapping
Maps a user to user groups. 
jindo admin -addUserGroupsMapping
[-dlsUri <uri>]
[-user <user>]
[-groups <group1,group2...>]
Example:
jindo admin -addUserGroupsMapping
-dlsUri oss://jindosdk-dls-unit-test/
-user user1
-groups group1,group2
You can run the preceding command to map user1 to group1 and group2. 
DeleteUserGroupsMapping
Unmaps a user from user groups. 
jindo admin -deleteUserGroupsMapping
[-dlsUri <uri>]
[-user <user>]
Example:
jindo admin -deleteUserGroupsMapping
-dlsUri oss://jindosdk-dls-unit-test/
-user user1
You can run the preceding command to unmap user1 from user groups. 
ListUserGroupsMappings
Queries information about all user groups. 
jindo admin -listUserGroupsMappings
[-dlsUri <dlsUri>]
[-maxKeys <maxKeys>]
[-marker <marker>]
The -maxKeys option specifies the number of users that you want to query. The -marker option filters users whose names contain a specific string. The -maxKeys and -marker options are optional. Example:
jindo admin -listUserGroupsMappings
-dlsUri oss://jindosdk-dls-unit-test/
-maxKeys 10
-marker user1
You can run the preceding command to query the group information of 10 users whose names contain the user1 string in the oss://jindosdk-dls-unit-test/ path. 
Use commands that are relevant to snapshots
AllowSnapshot
Enables the snapshot feature for a directory. 
jindo admin -allowSnapshot [-dlsUri <dlsSnapshotDir>]
Example:
jindo admin -allowSnapshot
-dlsUri oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow
You can run the preceding command to enable the snapshot feature for the oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow directory. 
DisallowSnapshot
Disables the snapshot feature for a directory. 
jindo admin -disallowSnapshot [-dlsUri <dlsSnapshotDir>]
Example:
jindo admin -disallowSnapshot
-dlsUri oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow
You can run the preceding command to disable the snapshot feature for the oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow directory. 
SnapshotDiff
Compares the differences between two snapshots in the same directory. 
jindo admin -snapshotDiff
[-dlsUri <dlsSnapshotDir>]
[-fromSnapshot <fromSnapshot>]
[-toSnapshot <toSnapshot>]
Example:
jindo admin -snapshotDiff
-dlsUri oss://jindosdk-dls-unit-test/testSnapshotDir/
-fromSnapshot S1
-toSnapshot S2
S1 and S2 are snapshots that are created for the test directory by using commands in the Shell CLI of Hadoop Distributed File System (HDFS). 
Use commands that are relevant to RootPolicy
SetRootPolicy
Registers an access address that contains a custom prefix for a bucket. 
jindo admin -setRootPolicy [<dlsRootPath>] [<accessRootPath>]
Example:
jindo admin -setRootPolicy oss://jindosdk-dls-unit-test/ hdfs://myufs/
After you run the preceding command, you can access the oss://jindosdk-dls-unit-test/ directory by using hdfs://myufs/. 
UnsetRootPolicy
Deletes all registered access addresses that contain a custom prefix specified for a bucket. 
jindo admin -unsetRootPolicy [<dlsRootPath>] [<accessRootPath>]
Example:
jindo admin -unsetRootPolicy oss://jindosdk-dls-unit-test/ hdfs://myufs/
After you run the preceding command, you can no longer access the oss://jindosdk-dls-unit-test/ directory by using hdfs://myufs/. 
ListAccessPolicies
Queries all registered access addresses that contain a custom prefix specified for a bucket. 
jindo admin -listAccessPolicies [<dlsRootPath>]
Example:
jindo admin -listAccessPolicies oss://jindosdk-dls-unit-test/
You can run the preceding command to query all access paths of the oss://jindosdk-dls-unit-test/ directory. 
Use other commands
DumpFile
Queries detailed storage information of a file. 
jindo admin -dumpFile [-dlsUri <dlsUri>]
Example:
jindo admin -dumpFile
-dlsUri oss://jindosdk-dls-unit-test/testDumpFile/dumpfile_simple_file
After you run the preceding command, output similar to the following information is returned:
=============Dump result=============
File /testDumpFile/dumpfile_simple_file
length: 11
blockSize: 33554432
modification time: 1657626266711
access time: 1657626266481
permission: 420
owner: root
group: supergroup
file id: 4180702376518228901
=============Block 0
block id: 8792388394945610760
offset: 0
numBytes: 11
gs: 1032
type: CLOUD
loc: .dlsdata
underConstruction: 0
isLastBlockComplete: 1

