# 阿里云 OSS-HDFS 服务（JindoFS 服务）Shell 命令使用说明

## 设置 Hadoop Proxy User 命令

### AddProxyUser 命令
AddProxyUser 命令可以添加代理用户。
```bash
jindo dlsadmin -addProxyUser
                 [-dlsUri <uri>]
                 [-proxyUser <proxyUser>]
                 [-users <user1,user2...>]|[-groups <group1,group2...>]
                 [-hosts <host1,host2...>] 
```
在同一条命令中，参数 users 和 groups 不可同时使用，任选其一。 
例：

```bash
jindo dlsadmin -addProxyUser
                 -dlsUri oss://jindosdk-dls-unit-test/
                 -proxyUser hive
                 -groups group1,group2
                 -hosts host1,host2
```
执行上述命令，用户 hive 可以代理属于 group1 或 group2 且从 host1 或 host2 发送请求的用户。
### DeleteProxyUser 命令
DeleteProxyUser 命令可以删除代理用户。
```bash
jindo dlsadmin -deleteProxyUser
                [-dlsUri <uri>]
                [-proxyUser <proxyUser>]
```
例：
```bash
jindo dlsadmin -deleteProxyUser
                 -dlsUri oss://jindosdk-dls-unit-test/
                 -proxyUser hive
```
执行上述命令，用户 hive 不可以代理任何用户。
### ListProxyUsers 命令
ListProxyUsers 可以查看所有的代理用户和代理信息。
```bash
jindo dlsadmin -listProxyUsers
                [-dlsUri <uri>]
                [-maxKeys <maxKeys>]
                [-marker <marker>]
```
maxKeys 指定查看的代理用户个数，marker 用来筛选名称中含有特殊标记的代理用户，这两项参数均可省略。
例：

```bash
jindo dlsadmin -listProxyUsers
                -dlsUri oss://jindosdk-dls-unit-test/
                -maxKeys 10
                -marker hive
```
执行上述命令，将优先显示 oss://jindosdk-dls-unit-test/ 下名称中带 hive 标记的10个代理用户信息。
## 设置 User Groups Mapping 命令
### AddUserGroupsMapping 命令
AddUserGroupsMapping 命令映射用户和组的关系。
```bash
jindo dlsadmin -addUserGroupsMapping
                [-dlsUri <uri>]
                [-user <user>]
                [-groups <group1,group2...>]
```
例：
```bash
jindo dlsadmin -addUserGroupsMapping
                -dlsUri oss://jindosdk-dls-unit-test/
                -user user1
                -groups group1,group2
```
执行上述命令，用户 user1 将属于 group1 或 group2。
### DeleteUserGroupsMapping 命令
DeleteUserGroupsMapping 命令可以删除用户和组的映射关系。
```bash
jindo dlsadmin -deleteUserGroupsMapping
                 [-dlsUri <uri>]
                 [-user <user>]
```
例：
```bash
jindo dlsadmin -deleteUserGroupsMapping
                -dlsUri oss://jindosdk-dls-unit-test/
                -user user1
```
执行上述命令，将删除 user1 与对应组的映射关系。
### ListUserGroupsMappings 命令
ListUserGroupsMappings 可以查看所有的用户的组。
```bash
jindo dlsadmin -listUserGroupsMappings
                [-dlsUri <dlsUri>]
                [-maxKeys <maxKeys>]
                [-marker <marker>]
```
maxKeys 指定查看的用户个数，marker 用来筛选名称中含有特殊标记的用户，这两项参数均可省略。
例：
```bash
jindo dlsadmin -listUserGroupsMappings
                -dlsUri oss://jindosdk-dls-unit-test/
                -maxKeys 10
                -marker user1
```
执行上述命令，将优先显示 oss://jindosdk-dls-unit-test/ 下名称中带 user1 标记的10个用户的组信息。
## 管理 Snapshot 命令
### AllowSnapshot 命令
AllowSnapshot 命令允许创建目录快照。
```bash
jindo dlsadmin -allowSnapshot [-dlsUri <dlsSnapshotDir>]
```
例：
```bash
jindo dlsadmin -allowSnapshot
                -dlsUri oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow
```
执行上面的命令后，将允许 oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow 目录创建快照。
### DisallowSnapshot 命令
DisallowSnapshot 命令可以禁止创建目录快照。
```bash
jindo dlsadmin -disallowSnapshot [-dlsUri <dlsSnapshotDir>]
```
例：
```bash
jindo dlsadmin -disallowSnapshot
                -dlsUri oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow
```
执行上面的命令后，将不允许 oss://jindosdk-dls-unit-test/testSnapshot/snapshot_allow 目录创建快照。
### SnapshotDiff 命令
SnapshotDiff 命令用来比较同一目录两份快照的不同。
```bash
jindo dlsadmin -snapshotDiff
                [-dlsUri <dlsSnapshotDir>]
                [-fromSnapshot <fromSnapshot>]
                [-toSnapshot <toSnapshot>]
```
例：
```bash
jindo dlsadmin -snapshotDiff
                -dlsUri oss://jindosdk-dls-unit-test/testSnapshotDir/
                -fromSnapshot S1
                -toSnapshot S2
```
S1 和 S1 是事先通过 HDFS 的 Shell 命令行工具为测试目录创建的两份快照。
## 其他
### DumpFile 命令
DumpFile 命令可以查看文件的详细存储信息。
```bash
jindo dlsadmin -dumpFile [-dlsUri <dlsUri>]
```
例：
```bash
jindo dlsadmin -dumpFile
                -dlsUri oss://jindosdk-dls-unit-test/testDumpFile/dumpfile_simple_file
```
执行上述命令后，将显示类似如下结果：
```
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
```
