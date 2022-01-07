# 使用 JindoTable 将 MaxCompute 内表保存为 Hadoop 集群的 Parquet 表

MaxCompute 内表没有可见的文件系统，所以需要借助工具把数据导出到 Hadoop 集群。

## 安装说明

1. 如果需要把数据放在 OSS 上，需要配置jindofs-sdk。如果使用 HDFS 可以跳过此步骤。将 jindofs-sdk.jar 与 Hadoop 集成，配置方法可以参考这篇[文档](../hadoop/jindosdk_on_hadoop.md) 。

2. 下载[压缩包](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/JindoTable-moveto/JindoTable-MoveTo.zip)并解压，保持 *b2jindosdk-current* 的目录结构并放置于集群 master 节点，然后配置路径：

```shell
export BIGBOOT_JINDOSDK_HOME=/path/to/b2jindosdk-current
export PATH=$PATH:BIGBOOT_JINDOSDK_HOME/bin 
```

3. Hadoop 与 Hive 环境：
* 确保 `hadoop classpath` 能够返回合理结果
* 确保客户端环境变量 $HIVE_HOME 与 $HIVE_CONF_DIR 正确配置

## 使用说明

使用 jindo table -help dumpmc 查看参数配置。

## 使用示例

1. 提前创建好用于存放表的db：

```sql
CREATE DATABASE IF NOT EXISTS testdb1;
```

2. 使用命令同步表 mc_table，并自动在 Hive Metastore 创建元数据。

```shell
jindo table -dumpmc -ms 10000 -project mc_project -table mc_table -t http://dt.cn-shanghai.maxcompute.aliyun-inc.com -k AKxxxxxxxxxxx -i IDXXXXXXXXXXXXXXXX -o /path/to/table -f parquet -hive testdb1.mc_table
```

这里10000表示每个mapper处理多少条记录，也就是多少条记录写成一个文件。

如果mc_table是分区表，并且没有指定partition，则 dumpmc 工具会逐个拷贝这些分区。可以用`-p`指定单个分区。指定的hive表不需要提前创建，如果表`testdb1.mc_table`已存在，那么这个表不会再次被创建，拷贝的分区一定要不存在，否则会报错。

## 其他

目前还是 beta 阶段，如遇到问题或有更多需求，请联系 EMR 产品。

