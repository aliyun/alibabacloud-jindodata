# 使用 JindoTable 将 Hive 数据分区归档和分层到OSS

HDFS 受限于集群规模和成本因素，无法无限扩展，容量存在瓶颈。云厂商提供的对象存储可以作为 HDFS 的替代或补充，扩展云上 Hadoop 平台的存储能力。
本工具用于把 Hive 数据根据分区键规则筛选，在 HDFS 和 OSS 之间转移分区。

## 安装说明

1. 将 jindofs-sdk-{version}.jar 与 Hadoop 集成，配置方法参考这篇[文档](https://github.com/aliyun/alibabacloud-jindofs/blob/master/docs/jindofs_sdk_how_to_hadoop.md) 。

2. 下载[压缩包](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/JindoTable-moveto/JindoTable-MoveTo.zip)并解压，保持 *b2jindosdk-current* 的目录结构并放置于集群 master 节点，然后配置路径：

```shell
export BIGBOOT_JINDOSDK_HOME=/path/to/b2jindosdk-current
export PATH=$BIGBOOT_JINDOSDK_HOME/bin:$PATH
```

3. Hadoop 与 Hive 环境：
* 确保 `hadoop classpath` 能够返回合理结果
* 确保客户端环境变量 $HIVE_HOME 与 $HIVE_CONF_DIR 正确配置

4. 配置 MoveTo 工具在 HDFS 下的锁目录：

    在 Hadoop 配置文件 core-site.xml 或 hdfs-site.xml（任一即可，在 $HADOOP_CONF_DIR 目录下）新增配置项：`jindotable.moveto.tablelock.base.dir`

    该配置的值应指向一个 HDFS 目录，目的是存放 MoveTo 工具在运行时自动创建的锁文件。需确保该目录只会被 MoveTo 工具访问，并且有访问权限。如果不配置，则使用缺省值 `hdfs:///tmp/jindotable-lock/`，无权限则报错。


## 使用说明
使用 jindo table -help moveTo 查看参数配置。

```shell
jindo table -moveTo -t <dbName.tableName> -d <destination path> [-c "<condition>" | -fullTable] [-b/-before <before days>] [-p/-parallel <parallelism>] [-s/-storagePolicy <OSS storage policy>] [-o/-overWrite] [-r/-removeSource] [-skipTrash] [-e/-explain] [-q/-queue <yarn queue>] [-w/-workingDir <working directory>][-l/-logDir <log directory>]
```

```shell
  <dbName.tableName>      要移动的表。
  <destination path>      目标路径，为表级别的路径，分区路径会在这个路径下自动创建。
  <condition>             分区过滤条件表达式，支持基本运算符，不支持udf。
  <before days>           根据分区创建时间，创建时间超过给定天数的分区才进行移动。
  <parallelism>           整个moveTo任务的最大task并发度，默认为1。
  <OSS storage policy>    拷贝至 OSS 时数据文件的存储策略，支持 Standard（默认）、IA、Archive、ColdArchive。
  -o/-overWrite           是否覆盖最终目录。分区级别覆盖，不会覆盖本次移动不涉及的分区。
  -r/-removeSource        是否在移动完成后删除源路径。
  -skipTrash              如果删除源路径，是否跳过Trash。
  -e/-explain             如果指定explain模式，不会触发实际操作，仅打印会同步的分区。
  <yarn queue>            指定分布式拷贝的yarn队列。
  <working directory>     指定分布式拷贝的工作临时目录。
  <log directory>         本地日志目录，默认为/tmp/<current user>/
```

## 使用示例

1. 有一个 HDFS 上的 Hive 分区表，如下图所示：

![image.png](../../pic/tools_table_moveto_1.png)

2. 想把 bbb 和 ccc 分区移动到 OSS，先用 explain 模式看看会移动的分区是否符合预期：

![image.png](../../pic/tools_table_moveto_2.png)

3. 去掉 explain，真正移动分区

![image.png](../../pic/tools_table_moveto_3.png)

4. 执行完成后，检查数据，数据已经在 OSS ：

![image.png](../../pic/tools_table_moveto_4.png)

5. 再移回 HDFS，结果失败了，会显示失败原因，原来是目标目录非空，提示可以使用 -overWrite 清空目标目录

![image.png](../../pic/tools_table_moveto_5.png)

6. 使用 -overWrite 强制覆盖

![image.png](../../pic/tools_table_moveto_6.png)
