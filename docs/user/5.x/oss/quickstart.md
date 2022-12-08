# OSS/OSS-HDFS 快速入门

## 前置条件

访问 OSS/OSS-HDFS 前，需要创建对应的存储空间（Bucket）。

*   [开通 OSS 服务](https://help.aliyun.com/document_detail/31884.html)。
    
*   [创建 OSS Bucket](https://www.alibabacloud.com/help/zh/object-storage-service/latest/oss-console-create-buckets)。 
    
*   确认 OSS 授权。
    
    *   在 EMR 集群（新版控制台）中使用， 已默认开通。如遇问题，详见 [角色授权](https://help.aliyun.com/document_detail/379951.html)。
        
    *   在 EMR 集群（旧版控制台）中使用， 已默认开通。如遇问题，详见 [角色授权](https://help.aliyun.com/document_detail/28072.html)。
        
    *   在非 EMR 环境中使用，授权方式请参考 [《OSS/OSS-HDFS 授权》](/docs/user/5.x/oss/ram_policy.md)
    
*   开通并授权访问 OSS-HDFS 服务（可选项，推荐开通）：
    
    *   [OSS-HDFS 服务使用前须知](https://www.alibabacloud.com/help/zh/object-storage-service/latest/usage-instructions-of-oss-hdfs)。
        
    *   [开通并授权访问 OSS-HDFS](https://help.aliyun.com/document_detail/419505.html)。
    
*   确认 JindoSDK 部署版本：
    
    *   在 EMR 集群中，已默认部署 JindoSDK。
        
        *   访问 OSS-HDFS，需创建 EMR-3.42.0 及以上版本或 EMR-5.8.0 及以上版本的集群。
        
    *   在非 EMR 集群中使用，请参考[《非 EMR 环境中部署 JindoSDK》](/docs/user/5.x/jindodata/jindosdk/deployment.md)。
        
        *   访问 OSS-HDFS，需部署 JindoSDK 4.x 及以上版本。
            

## 路径说明

|  存储系统  |  根路径示例  |  描述  |
| --- | --- | --- |
|  OSS  |  oss://examplebucket.oss-cn-shanghai-internal.aliyuncs.com/  |  以在上海区域已创建了名为examplebucket的 OSS Bucket，并使用内网 endpoint 访问为例。 注：在 EMR 集群中未挂载公网的节点，默认不支持访问 OSS 公网 endpoint，即默认不支持跨区域访问。  |
|  OSS-HDFS  |  oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/  |  以在上海区域已创建了名为examplebucket的 OSS-HDFS Bucket。 注：OSS-HDFS 目前只支持内网访问，即默认不支持跨区域访问。  |

访问 OSS 和 OSS-HDFS 的方式，除路径中的 endpoint 外，使用方式均相同。

## 访问方式

|  访问方式  |  示例  |  描述  |
| --- | --- | --- |
|  Hadoop Shell 命令  |      hadoop fs -ls oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/  |  JindoSDK 中的JindoOssFileSystem 是 Hadoop FileSystem 的一种实现。 在执行 Hadoop Shell 可以通过识别路径中的 endpoint 来访问 OSS/OSS-HDFS。 更多使用方式，详见 [《通过 Hadoop Shell 命令访问 OSS/OSS-HDFS》](/docs/user/5.x/oss/usages/hadoop_shell.md)  |
|  Jindo CLI 命令  |      jindo fs -ls oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/  |  Jindo CLI 实现了类似 Hadoop Shell 的方式来访问 OSS/OSS-HDFS。 此外，还支持了更多的功能，比如归档/缓存/错误分析等。详见 [《通过 Jindo CLI 命令访问 OSS/OSS-HDFS》](/docs/user/5.x/oss/usages/jindo_cli.md)  |
|  POSIX 命令  |      mkdir -p /mnt/oss     jindo-fuse /mnt/oss -ouri=oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/     ls /mnt/oss  |  通过实现 FUSE API，jindo-fuse 可以通过挂载 OSS/OSS-HDFS 路径到本地路径的方式，使用户可以像访问本地文件一样访问 OSS/OSS-HDFS。 详见[《通过 POSIX 访问 OSS/OSS-HDFS》](/docs/user/5.x/oss/usages/)  |
|  OSS 控制台  |  ![image](https://alidocs.oss-accelerate.aliyuncs.com/res/AmPdnp5J3dDpqw98/img/92a7fe0e-28fd-44fa-bf78-9af8155636d0.png)  |  可以通过打开 [OSS 控制台](https://oss.console.aliyun.com/) -> 文件列表 -> OSS文件/HDFS 来访问 OSS/OSS-HDFS  |