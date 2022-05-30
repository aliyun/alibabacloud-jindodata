# 半托管服务迁移
## 0.前置条件
如果要从JindoFS半托管集群迁移到服务化JindoFS集群。首先需要确保在JindoFS半托管服务对应的OSS Bucket上开通JindoFS服务化集群。同时需要在系统开启AuditLog。
## 1. 全量导入
全量导入模式会负责将半托管JindoFS集群一个目录中的元数据一次性全量导入JindoFS服务化集群中的某一个目录中。目前只支持导入到JindoFS服务化集群的一级子目录。
### 命令格式
```bash
jindo distjob -migrateImport -srcPath <srcPath> -destPath <desPth> -backendLoc <backendLoc>
```
参数说明：

`-srcPath`：待迁移的JindoFS半托管集群的源路径（必填）

`-destPath`：导入的服务化JindoFS集群的目标路径（必填）

`-backendLoc`： 半托管集群的源数据块对应的OSS路径（必填）

### 示例
假如在半托管JindoFS集群中有一个目录 jfs://my-cluster/foo。要将这个目录迁移到JindoFS服务化集群中的bar目录中。假设JindoFS服务化集群的bucket名为dlsbucket，则目标目录为oss://dlsbucket/bar。在这种情况下，全量导入的命令为：

```bash
jindo distjob -migrateImport -srcPath jfs://my-cluster/foo -destPath oss://dlsbucket/bar/
```

## 2.增量导入
### 2.1 生成Change Log
在增量导入的模式下，需要确保已经先在原来JindoFS半托管集群中开启了AuditLog。然后运行jindo工具可以将AuditLog转换成对于的目录的变更日志（Change Log）。

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

### 2.2 增量导入
在如2.1所描述的开启了生成Change Log的命令之后，就能开启增量导入模式。此模式下，对半托管JindoFS集群产生的操作，他的元数据变化会以增量形式陆续迁移至JindoFS服务化的目标集群中。具体的命令格式如下。
#### 命令格式
```bash
jindo distjob -migrateImport -srcPath <srcPath> -destPath <desPth> -auditLogDir <auditLogDir> -backendLoc <backendLoc> -update
```

参数说明：

`-srcPath`：待迁移的JindoFS半托管集群的源路径（必填）

`-destPath`：导入的服务化JindoFS集群的目标路径（必填）

`-auditLogDir`： Change Log所在的目录，与2.1中的命令参数含义相同（必填）

`-backendLoc`： 半托管集群的源数据块对应的OSS路径（必填）

`-update`: 开启增量导入（默认模式为全量导入）

#### 示例
假如在半托管JindoFS集群中有一个目录 jfs://my-cluster/foo。要将这个目录迁移到DLS中的bar目录中。假设JindoFS服务化集群的bucket名为dlsbucket，则目标目录为oss://dlsbucket/bar。并且Directory Change Log的目录为oss://logBucket/logDir/。在这种情况下，增量导入的命令为：

```bash
jindo distjob -migrateImport -srcPath jfs://my-cluster/foo -destPath oss://dlsbucket/bar/ -auditLogDir oss://logBucket/logDir/
```
