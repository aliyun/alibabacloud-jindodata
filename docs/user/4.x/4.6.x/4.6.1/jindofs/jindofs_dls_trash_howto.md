# 阿里云 OSS-HDFS 服务（JindoFS 服务）回收站使用说明
*(从 4.5.1 开始支持)*


## 介绍
当从JindoFS删除文件时，文件不是立即被立即删除。被删除的文件被挪到了    
```bash
/user/<username>/.Trash/Current
```
目录下。当经过30分钟后，Current目录会被挪到    
 ```bash
/user/<username>/.Trash/<timestamp>
```
也就是说某一时间段内被删除的文件，会被归类到一个带时间戳的目录下，表示是在这个时间内被删除的，相当于一次checkpoint。当经过**7天**后，这个<timestamp>目录将会被永远删除。因此在7天内，您有机会从.Trash目录下找到对应时刻被删除的文件，将其从.Trash挪出，从而恢复它。

注意：这项功能，是由客户端和服务端配合，方能形成回收站的功能。服务端只负责维护 ```/user/<username>/.Trash``` 的定时清理，服务端的定时清理是默认开启的。客户端负责将待删除文件挪到.Trash目录下。

## Hadoop FileSystem Shell 使用回收站功能
```bash
hadoop fs -rm oss://bucket/a/b/c
```
客户端的Hadoop Shell命令默认不开启Trash功能，因此需要在core-site.xml里添加一条配置
```xml
  <property>
    <name>fs.trash.interval</name>
    <value>1440</value>
  </property>
```
(该值只需大于0即可)   
此时在客户端会自动将rm命令转换为一条 ```hadoop fs -mv oss://bucket/a/b/c /user/<username>/.Trash/Current/a/b/c```
命令，因此，您不需要感知回收站功能的存在，服务端会负责清理。    
如果您想立即删除该文件，释放空间，可以添加 ```-skipTrash``` 参数，此时将立即删除该文件。

## Hadoop 生态组件使用回收站功能
Hive/Spark/Flink 等组件并不感知 JindoFS 回收站功能的存在，使用 FileSystem(HCFS) 的 delete 接口意味着立即删除。因此如果想要使用回收站功能，需要显式地调用 FileSystem 的 rename 接口，将目标文件手动挪到 ```/user/<username>/.Trash/Current``` 目录下，由 JindoFS 服务端负责定期删除。这一点上 JindoFS 采取了跟开源 Hadoop 相似的策略。
