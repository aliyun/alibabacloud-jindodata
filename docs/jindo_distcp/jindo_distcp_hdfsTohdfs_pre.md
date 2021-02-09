### 使用前须知
* 请参考 [Jindo DistCp 介绍](jindo_distcp_overview.md) 文章内容进行环境适配和工具包下载
* 检查两个 HDFS 集群的网络情况，保证两个集群的 namenode 和 datanode 的相关端口（默认端口 8020/50010/50070等）可以互通访问，如环境为线下 IDC 需要保证上述端口从防火墙移除
* 检查域名情况，将目的 HDFS 集群的 hosts 信息配置在源 HDFS 的 hosts 文件中

### 1、拷贝数据到 HDFS 上
您可以使用如下命令将 hdfs 上的目录拷贝到 OSS 上
```
hadoop jar jindo-distcp-3.4.0.jar --src /srchdfs/dir --dest hdfs://<ip/hostname>:port/dir/ --parallelism 10
```
* --src：源hdfs的路径
* --dest：目标hdfs的路径，支持 hdfs://<ip/hostname>:port/dir/ 形式
* --parallelism：任务并发大小，根据集群资源可调整

### 2、增量拷贝文件
如果 Distcp 任务因为各种原因中间失败了，而此时您想进行断点续传，只Copy剩下未Copy成功的文件。或者源端文件新增了部分文件，此时需要您在进行上一次 Distcp 任务完成后进行如下操作：
##### 使用 --diff 命令，获得增量的文件列表
```
hadoop jar jindo-distcp-3.4.0.jar --src /srchdfs/dir --dest hdfs://<ip/hostname>:port/dir/ --diff
```
如果所有文件都传输完成，则会提示如下信息。
```
INFO distcp.JindoDistCp: distcp has been done completely.
```
##### 增量的文件列表会被写入到本地的 manifest 文件里，默认生成在当前提交任务的路径下，您可以使用如下命令进行剩余文件的Copy
```
hadoop jar jindo-distcp-3.4.0.jar --src /srchdfs/dir --dest hdfs://<ip/hostname>:port/dir/ --previousManifest=file:///opt/manifest-2020-04-17.gz --copyFromManifest --parallelism 20
```
* --copyFromManifest：表示从文件本地文件列表中读取文件
* --previousManifest：需要拷贝的文件列表，通过 --diff 生成

### 3、YARN 队列及带宽选择
如您需要对 DistCp 作业使用的 YARN 队列和带宽进行限定，可用如下命令
```
hadoop jar jindo-distcp-3.4.0.jar --src /srchdfs/dir --dest hdfs://<ip/hostname>:port/dir/ --queue yarnQueue --bandwidth 100 --parallelism 10
```
* --queue：指定 YARN 队列的名称
* --bandwidth：指定限流带宽的大小，单位 MB

### 4、其他功能
如您需要其他使用其他功能，请参考
* [Jindo DistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md)
* [Jindo DistCp 场景化使用指南](jindo_distcp_scenario_guidance.md)


