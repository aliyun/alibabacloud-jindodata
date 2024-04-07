# Alibaba Cloud OSS-HDFS Service (JindoFS Service) Snapshot Usage Guide \(Supported Since JindoData 4.0.0\)

The Alibaba Cloud OSS-HDFS Service (JindoFS Service) is a new storage space type compatible with the HDFS interface and supporting directory hierarchies. It can be accessed via Jindo SDK 4.x. For basic functionality and usage of the OSS-HDFS Service, refer to the [link](oss_quickstart.md).

The snapshot feature in OSS-HDFS Service is fully compatible with HDFS snapshots. This guide focuses on common operations for using snapshots in the OSS-HDFS Service.

Assuming `oss://oss-dfs-test` is a bucket already enabled for the OSS-HDFS Service, all examples will operate on this bucket.

## Enable Snapshots
First, create a directory called TestSnapshot in `oss://oss-dfs-test`.
```bash
hdfs dfs -mkdir oss://oss-dfs-test.<Endpoint>/TestSnapshot
```
Snapshots are disabled by default. To enable or disable snapshot capabilities for a directory, use the JindoSDK shell commands. To enable snapshots:
```bash
jindo admin -allowSnapshot -dlsUri <path>
```
To enable snapshots for TestSnapshot:
```bash
jindo admin -allowSnapshot -dlsUri oss://oss-dfs-test.<Endpoint>/TestSnapshot
```

## Create Snapshots
After enabling snapshots, you can create them using standard HDFS shell commands:
```bash
hdfs dfs -createSnapshot <path> [<snapshotName>]
```
Let's say we've made some changes in TestSnapshot:
```bash
# Creating test directories and files
hdfs dfs -mkdir oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir1
hdfs dfs -mkdir oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir2
hdfs dfs -touchz oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir1/file1
hdfs dfs -touchz oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir3/file2

# Create a snapshot named S1
hdfs dfs -createSnapshot oss://oss-dfs-test.<Endpoint>/TestSnapshot S1
```

## Accessing Snapshot Contents
### Snapshot Path Format
To distinguish snapshot contents from live data, append snapshot information to the path. To access a snapshot's files and directories:

```bash
<snapshotRoot>/.snapshot/<snapshotName>/<actual subPath>
```
where `snapshotRoot` is the directory where snapshots were enabled, `snapshotName` is the snapshot's name, and `actual subPath` is the path within the snapshot root.

For example, to list the contents of `oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir1` at snapshot S1:
```bash
hdfs dfs -ls oss://oss-dfs-test.<Endpoint>/TestSnapshot/.snapshot/S1/dir1
```

## Restore Data with Snapshots
Snapshots can be used for data recovery. By accessing read-only copies of data, you can restore accidentally deleted items. Given our previous example, if we mistakenly delete dir1:
```bash
hdfs dfs -rm -r oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir1
```
We can recover it using S1:
```bash
hdfs dfs -cp oss://oss-dfs-test.<Endpoint>/TestSnapshot/.snapshot/S1/dir1 oss://oss-dfs-test.<Endpoint>/TestSnapshot
```
Now, listing dir1 shows the restored content:
```bash
hdfs dfs -ls oss://oss-dfs-test.<Endpoint>/TestSnapshot/dir1
```

## Rename Snapshots
Rename a snapshot with:
```bash
hdfs dfs -renameSnapshot <path> <oldName> <newName>
```
Renaming snapshot S1 in TestSnapshot to S100:
```bash
hdfs dfs -renameSnapshot oss://oss-dfs-test.<Endpoint>/TestSnapshot S1 S100
```

## Delete Snapshots
Delete a snapshot with:
```bash
hdfs dfs -deleteSnapshot <path> <snapshotName>
```
Deleting snapshot S100:
```bash
hdfs dfs -deleteSnapshot oss://oss-dfs-test.<Endpoint>/TestSnapshot S100
```

## Disable Snapshot Functionality
To disable snapshots for a directory:
```bash
jindo admin -disallowSnapshot -dlsUri <path>
```
Disabling snapshots for TestSnapshot (assuming all snapshots have been deleted):
```bash
jindo admin -disallowSnapshot -dlsUri oss://oss-dfs-test.<Endpoint>/TestSnapshot
```

## Compare Snapshots
View differences between two snapshots:
```bash
jindo dls -snapshotDiff -dlsUri <uri> -fromSnapshot <fromSnapshot> -toSnapshot <toSnapshot>
```

### Notes

When disabling the snapshot feature for a directory, make sure all snapshots under that directory have been deleted first. Refer to the previously mentioned command for deleting snapshots. If there are still snapshots present in the directory, attempting to disable the snapshot feature will result in an error.

For our earlier example, TestSnapshot, assuming all snapshots have been removed, you can use the following command to disable the snapshot functionality for the directory:
```bash
jindo admin -disallowSnapshot -dlsUri oss://oss-dfs-test.<Endpoint>/TestSnapshot
```
After executing this command, the TestSnapshot directory will no longer support creating new snapshots, and any previous snapshots will no longer be available for data recovery purposes. Be cautious with this step, as it may lead to irreversible data loss unless you have backed up the necessary data or snapshots elsewhere.