# 通过 Hadoop Shell 命令访问 OSS/OSS-HDFS

## 环境准备

*   EMR环境中，默认已安装 JindoSDK，可以直接使用。注意：
    
    *   访问 OSS-HDFS，需创建EMR-3.42.0及以上版本或EMR-5.8.0及以上版本的集群。
        
*   非 EMR 环境，请先安装部署 JindoSDK。部署方式请参考 [《在 Hadoop 环境中部署 JindoSDK》](/docs/user/jindosdk/jindosdk_deployment_hadoop.md)。
    
    *   访问 OSS-HDFS，需部署 JindoSDK 4.x 及以上版本。
        

## 通过 Hadoop Shell 命令访问

通过 hadoop 命令访问 OSS 和 OSS-HDFS 的方式基本相同，仅路径中的 endpoint 略有不同。根路径示例可参见：[《OSS/OSS-HDFS 快速入门》](../oss_quickstart.md)

*   上传文件
    

将本地根目录下的examplefile.txt文件上传至examplebucket，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -put examplefile.txt oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

*   新建目录
    
在examplebucket下创建名为dir/的目录，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -mkdir oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/dir/
```

*   查看文件或目录信息
    
查看examplebucket下的文件或目录信息，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -ls oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

*   获取文件或目录大小
    
获取examplebucket下所有文件或目录的大小，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -du oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/
```

*   查看文件内容
    
查看examplebucket下名为localfile.txt的文件内容，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -cat oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/localfile.txt
```

**重要** 查看文件内容时，文件内容将以纯文本形式打印到屏幕上。如果文件内容进行了特定格式的编码，请使用HDFS的Java API读取并解码文件内容。

*   拷贝目录或文件
    
例如，将examplebucket下根目录subdir1拷贝到目录subdir2下，且根目录subdir1所在的位置、根目录下的文件和子目录结构和内容保持不变，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -cp oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir1  oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir2/subdir1
```

*   移动目录或文件
    
例如，将examplebucket根目录srcdir及其包含的文件或者子目录移动至另一个根目录destdir下，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -mv oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/srcdir  oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/destdir
```

*   下载文件
    
将examplebucket下的exampleobject.txt下载到本地根目录文件夹/tmp，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -get oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/exampleobject.txt  /tmp/
```

*   删除目录或文件
    
删除examplebucket下destfolder/目录及其目录下的所有文件，访问 OSS-HDFS 示例如下：

```bash
hadoop fs -rm oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/destfolder/
```