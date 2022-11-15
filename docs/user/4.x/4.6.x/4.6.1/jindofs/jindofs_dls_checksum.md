# 阿里云 OSS-HDFS 服务（JindoFS 服务）文件内容校验
通过 JindoDistCp 从 HDFS 迁移到 OSS-HDFS 的文件，可以通过以下方式校验文件内容。

## 步骤1. 
计算输出通过 JindoDistCp 迁移的目标目录的文件 checksum。

### 命令格式
```bash
jindo distjob -checksum --src <src> --dest <dest> --blockSize <blockSize> --recalculate
```
参数说明：

`--src <src>`：待文件内容校验的 OSS-HDFS 的路径（必填）。注：src 只能为 OSS-HDFS 或 HDFS 路径。

`--dest <dest>`：输出 checksum 文件的目标路径（必填）。

`--blockSize <blockSize>`：源文件写入的 blockSize（可选)，默认为 134217728。

`--recalculate`：是否读取文件重新计算 checksum（可选)，默认不会重新计算，从 OSS-HDFS 读取写入时计算的 checksum。注：该参数只支持 --src 为 OSS-HDFS。

### 示例
假设在 OSS-HDFS 的 bucket 名为 dlsbucket，需要文件内容校验的目录为 oss://dlsbucket/test，输出 checksum 文件到 OSS 目录 oss://ossbucket/test-dls。在这种情况下命令为：

```bash
jindo distjob -checksum --src oss://dlsbucket/test --dest oss://ossbucket/test-dls --recalculate
```

## 步骤2.
计算输出 JindoDistCp 迁移的源目录的文件 checksum。

### 示例
假设 JindoDistCp 的源目录为 hdfs:///test，输出 checksum 文件到 OSS 目录 oss://ossbucket/test-hdfs。在这种情况下命令为：

```bash
jindo distjob -checksum --src hdfs:///test --dest oss://ossbucket/test-hdfs
```

## 步骤3.
通过 JindoDistCp --diff 对比步骤1和步骤2输出的文件 checksum 目录

#### 示例
```bash
hadoop jar jindo-distcp-tool-${version}.jar --src oss://ossbucket/test-hdfs --dest oss://ossbucket/test-dls --diff
```