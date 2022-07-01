# JindoFSx 存储加速系统用户文档

## 快速入门
* [JindoFSx 存储加速系统快速入门](/docs/user/4.x/4.4.0/jindofsx/jindofsx_quickstart.md)

## 集群部署
* [快速部署一个简单的 JindoFSx 存储加速系统](/docs/user/4.x/4.4.0/jindofsx/deploy/deploy_jindofsx.md)
* [部署高可用 JindoFSx 存储加速系统](/docs/user/4.x/4.4.0/jindofsx/deploy/deploy_raft_ns.md)
* [基于 Prometheus + Grafana 搭建 JindoFSx 存储加速系统可视化指标观测平台](/docs/user/4.x/4.4.0/jindofsx/jindofsx_metrics.md)

## 基本功能
* [统一命名空间混合挂载](/docs/user/4.x/4.4.0/jindofsx/multi-cloud/jindofsx_on_multi_cloud_mount.md)
* [JindoFSx 缓存使用说明](/docs/user/4.x/4.4.0/jindofsx/jindofsx_cache.md)

#### 阿里云 OSS 缓存加速
* [阿里云 OSS 透明缓存加速](/docs/user/4.x/4.4.0/jindofsx/oss/jindofsx_on_oss.md)
* [阿里云 OSS 统一挂载缓存加速](/docs/user/4.x/4.4.0/jindofsx/oss/jindofsx_on_oss_jindo.md)

#### 阿里云 OSS-HDFS 服务（JindoFS 服务）缓存加速
* [阿里云 OSS-HDFS 服务（JindoFS 服务）透明缓存加速](/docs/user/4.x/4.4.0/jindofsx/jindofs/jindofsx_on_jindofs.md)
* [阿里云 OSS-HDFS 服务（JindoFS 服务）统一挂载缓存加速](/docs/user/4.x/4.4.0/jindofsx/jindofs/jindofsx_on_jindofs_jindo.md)

#### Apache HDFS 缓存加速
* [Apache HDFS 透明缓存加速](/docs/user/4.x/4.4.0/jindofsx/hdfs/jindofsx_on_hdfs.md)
* [Apache HDFS 统一挂载缓存加速](/docs/user/4.x/4.4.0/jindofsx/hdfs/jindofsx_on_hdfs_jindo.md)

#### 阿里云文件存储 NAS 缓存加速
* [阿里云文件存储 NAS 统一挂载缓存加速](/docs/user/4.x/4.4.0/jindofsx/nas/jindofsx_on_nas_jindo.md)

#### 多云存储支持 <span style="color:red">*[NEW]*</span>
* [S3、COS、OBS 多云存储支持](/docs/user/4.x/4.4.0/jindofsx/multi-cloud/jindofsx_on_multi_cloud.md)

#### JindoFSx 客户端侧 P2P 加速
* [P2P 分布式下载缓存使用说明](/docs/user/4.x/4.4.0/jindofsx/jindofsx_p2p.md)
  
#### JindoShell CLI 支持
* [JindoShell CLI 支持 JindoFSx 使用说明](/docs/user/4.x/4.4.0/jindofsx/jindoshell_jindofsx_howto.md)

### JindoFSx 支持 Kerberos + Ranger 鉴权
* [阿里云 OSS 使用 Ranger 的鉴权方案](/docs/user/4.x/4.4.0/jindofsx/permission/jindofsx_ranger.md)

## 性能测试

## 大数据生态
#### Hadoop 组件
* [Hadoop 访问阿里云 OSS + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/hadoop/hadoop_with_oss.md)
* [Hadoop 访问阿里云 OSS-HDFS 服务（JindoFS 服务）+ JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/hadoop/hadoop_with_dls.md)
* [Hadoop 访问 JindoFSx 统一挂载的数据](/docs/user/4.x/4.4.0/jindofsx/hadoop/hadoop_with_jindo.md)

#### Spark 组件
* [Spark 处理阿里云 OSS 上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/spark/jindosdk_on_spark_oss.md)
* [Spark 处理阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/spark/jindosdk_on_spark_dls.md)
* [Spark 处理 JindoFSx 统一挂载的数据](/docs/user/4.x/4.4.0/jindofsx/spark/jindosdk_on_spark_jindo.md)

#### Hive 组件
* [Hive 处理阿里云 OSS 上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/hive/jindosdk_on_hive_oss.md)
* [Hive 处理阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/hive/jindosdk_on_hive_dls.md)
* [Hive 处理 JindoFSx 统一挂载的数据](/docs/user/4.x/4.4.0/jindofsx/hive/jindosdk_on_hive_jindo.md)

#### Presto 组件
* [Presto 查询阿里云 OSS 上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/presto/jindosdk_on_presto_oss.md)
* [Presto 查询阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/presto/jindosdk_on_presto_dls.md)
* [Presto 查询 JindoFSx 统一挂载的数据](/docs/user/4.x/4.4.0/jindofsx/presto/jindosdk_on_presto_jindo.md)

#### Impala 组件
* [Impala 查询阿里云 OSS 上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/impala/jindosdk_on_impala_oss.md)
* [Impala 查询阿里云 OSS-HDFS 服务（JindoFS 服务）上的数据 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/impala/jindosdk_on_impala_dls.md)
* [Impala 查询 JindoFSx 统一挂载的数据](/docs/user/4.x/4.4.0/jindofsx/impala/jindosdk_on_impala_jindo.md)

#### HBase 组件
* [HBase 使用阿里云 OSS 作为底层存储 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/hbase/jindosdk_on_hbase_oss.md)
* [HBase 使用阿里云 OSS-HDFS 服务（JindoFS 服务）作为底层存储 + JindoFSx 透明加速](/docs/user/4.x/4.4.0/jindofsx/hbase/jindosdk_on_hbase_dls.md)
* [HBase 使用 JindoFSx 统一挂载的数据](/docs/user/4.x/4.4.0/jindofsx/hbase/jindosdk_on_hbase_jindo.md)

## AI 生态
#### JindoFuse 使用
* [JindoFuse 读写 JindoFSx 缓存(oss://路径挂载)](/docs/user/4.x/4.4.0/jindofsx/jindo_fuse/jindo_fuse_on_oss.md)
* [JindoFuse 读写 JindoFSx 缓存(jindo://路径挂载)](/docs/user/4.x/4.4.0/jindofsx/jindo_fuse/jindo_fuse_on_jindo.md)

#### Fluid + JindoFSx 存储加速系统云原生
* [Fluid 结合 JindoFSx 存储加速系统介绍和使用](jindo_fluid/jindo_fluid_overview.md)

## 最佳实践
#### 使用 JindoTable 缓存 Hive 表或分区的数据
* [使用 JindoTable 缓存 Hive 表或分区的数据](jindotable/table_cache.md)