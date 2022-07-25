# 半托管JindoFS迁移到 OSS-HDFS 服务（JindoFS 服务）
## 0.背景
阿里云 OSS-HDFS 服务（JindoFS 服务）是 OSS 推出新的存储空间类型，兼容HDFS接口, 支持目录以及目录层级。如果要从JindoFS半托管集群迁移到OSS-HDFS服务。首先需要确保在半托管JindoFS集群对应的OSS Bucket上开通OSS-HDFS服务，同时需要在半托管JindoFS集群开启AuditLog。

## 步骤1. 全量导入
全量导入模式会负责将半托管JindoFS集群中的一个目录中的元数据一次性全量迁移导入JindoFS服务中的某一个目录中。目前只支持导入到JindoFS服务中的一级子目录。
### 命令格式
```bash
jindo distjob -migrateImport -srcPath <srcPath> -destPath <desPth> -backendLoc <backendLoc>
```
参数说明：

`-srcPath`：待迁移的半托管JindoFS集群的源路径（必填）

`-destPath`：导入的JindoFS服务的目标路径（必填）

`-backendLoc`： 半托管集群的源数据块对应的OSS路径（必填）

### 示例
假如在半托管JindoFS集群中有一个目录 jfs://my-cluster/foo。要将这个目录迁移到JindoFS服务化集群中的bar目录中。假设JindoFS服务化的bucket名为dlsbucket，则目标目录为oss://dlsbucket/bar。在这种情况下，全量导入的命令为：

```bash
jindo distjob -migrateImport -srcPath jfs://my-cluster/foo -destPath oss://dlsbucket/bar/
```

## 步骤2. 生成Change Log
如果需要增量迁移导入半托管JindoFS集群到JindoFS服务。需要先运行jindo工具可以将半托管JindoFS的AuditLog转换成对应目录的变更日志（Change Log）。具体命令格式为：

#### 命令格式
```bash
jindo distjob -mkchangelog -auditLogDir <auditLogDir> -changeLogDir <changeLogDir> -startTime <startTime>
```

参数说明：

`-auditLogDir`：Auditlog的路径

`-changeLogDir`：变更日志的输出路径

`-startTime`：开始处理Auditlog的起始时间

#### 示例
假如在半托管JindoFS集群中AuditLog的路径为oss://samplebuket/sysinfo/auditlog。想要输出的目录的变更日志存放在oss://samplebuket/sysinfo/changelog下。并且只处理从2022年1月1日开始的AuditLog。则命令如下：

```bash
jindo distjob -mkchangelog -auditLogDir oss://samplebuket/sysinfo/auditlog -changeLogDir oss://samplebuket/sysinfo/changelog -startTime 2022-01-01-12:00:00
```

## 步骤3. 增量导入
通过步骤2生成了Change Log之后，就能运行增量导入命令。此模式下，半托管JindoFS集群产生的元数据的增量更新，会通过对应的AuditLog转换为ChangeLog再经过处理迁移至JindoFS服务中。具体的命令格式如下。
#### 命令格式
```bash
jindo distjob -migrateImport -srcPath <srcPath> -destPath <desPth> -changeLogDir <auditLogDir> -backendLoc <backendLoc> -update
```

参数说明：

`-srcPath`：待迁移的JindoFS半托管集群的源路径（必填）

`-destPath`：导入的服务化JindoFS集群的目标路径（必填）

`-changeLogDir`： Change Log所在的目录，与步骤2的命令参数含义相同（必填）

`-backendLoc`： 半托管集群的源数据块对应的OSS路径（必填）

`-update`: 开启增量导入（默认模式为全量导入）

#### 示例
假如在半托管JindoFS集群中有一个目录 jfs://my-cluster/foo。要将这个目录迁移到DLS中的bar目录中。假设JindoFS服务化集群的bucket名为dlsbucket，则目标目录为oss://dlsbucket/bar。并且Directory Change Log的目录为oss://logBucket/logDir/。在这种情况下，增量导入的命令为：

```bash
jindo distjob -migrateImport -srcPath jfs://my-cluster/foo -destPath oss://dlsbucket/bar/ -changeLogDir oss://logBucket/logDir/ -update
```
## 步骤4。多次增量导入
如果需要多次增量导入半托管JindoFS集群到JindoFS服务。可以通过修改步骤2中开始处理Auditlog的起始时间`-startTime`参数，然后多次运行步骤2和步骤3的命令完成。
