# JindoData 4.6.11 版本说明
## 版本概要

JindoData 4.6.11 版本升级了 JindoTable，并修复若干问题。

## JindoTable 升级

- JindoTable 对 OSS 上的表或分区进行解冻的功能，新增支持设置解冻天数，详情请参考
[JindoTable 归档和解冻 OSS 上的表或分区](/docs/user/4.x/4.6.x/4.6.11/oss/jindotable/jindotable_archive.md)

## 修复介绍

- JindoSDK 修复了使用 JindoCommitter 在 Aliyun EMR Hadoop 2.8.5 环境下使用老的 mapred 接口写数据的问题