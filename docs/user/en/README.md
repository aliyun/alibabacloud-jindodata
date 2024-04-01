# JindoData 使用文档

JindoData 是阿里云开源大数据团队自研的数据湖存储加速套件，面向大数据和 AI 生态，为阿里云和业界主要数据湖存储系统提供全方位访问加速解决方案。JindoData 套件基于统一架构和内核实现，主要包括 JindoFS 存储系统（原 JindoFS Block 模式），JindoCache 存储加速系统（原 JindoFS Cache 模式），JindoSDK 大数据万能 SDK 和全面兼容的生态工具（JindoFuse、JindoDistCp）、插件支持。

![JindoData](../image/jindodata-arch.png)

## 下载安装 JindoSDK

JindoSDK 是访问 JindoData 组件的标准客户端，请参考 [JindoSDK 下载](jindosdk/jindosdk_download.md) 和
[JindoSDK 快速入门](jindosdk/jindosdk_quickstart.md) 进行安装和验证。

关于多版本支持，请参考[JindoSDK 多版本支持](jindosdk/jindosdk_deployment_multi_platform.md)。

## 升级 JindoSDK

JindoSDK 是活跃更新的客户端，持续为用户带来阿里云EMR数据湖的最新功能和最佳性能。
因此我们推荐客户使用新版 JindoSDK，以获取持续支持和更好的使用体验。
我们提供了便捷的脚本帮助用户升级集群里的 JindoSDK，请参考 [JindoSDK升级文档](upgrade/emr2_upgrade_jindosdk.md)。

## 分场景使用说明

[在 Hadoop 生态使用JindoSDK](jindosdk/jindosdk_deployment_hadoop.md)

[在 AI 生态使用JindoSDK](jindosdk/jindosdk_deployment_ai.md)

[Jindo Python SDK 使用](jindosdk/python/pyjindo_quickstart.md)

[Jindo TensorFlow Connector 使用](jindosdk/tensorflow/jindosdk_on_tensorflow.md)

[JindoFuse 使用](jindofuse/jindofuse_quickstart.md)

[Jindo Flink Sink 使用](jindosdk/flink/jindosdk_on_flink.md)

[在 Fluid 中使用 JindoRuntime](fluid-jindoruntime/jindo_fluid_overview.md)

[JindoData 鉴权方案](jindoauth/jindoauth_emr-next_kerberos.md)

[数据拷贝工具 JindoDistCp](jindotools/jindodistcp_quickstart.md)

[数仓迁移工具 JindoTable MoveTo](jindotools/jindotable_moveto.md)

[数仓分层存储管理工具 JindoTable SetStorage](jindotools/jindotable_set_storage_class.md)

[OSS-HDFS服务（JindoFS）客户端工具](jindofs/jindofs_client_tools.md)

## 常见问题

请参见[JindoData 常见问题](faq.md)

## 历史版本

请参见[JindoSDK 版本记录](releases.md)。
