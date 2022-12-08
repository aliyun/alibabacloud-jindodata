# Jindo CLI 支持 JindoFSx 使用指南

JindoShell CLI支持操作JindoFSx数据缓存、元数据缓存和统一命名空间等命令。

## 背景信息

本文为您介绍以下内容：

* [数据缓存命令]
* [元数据缓存命令]  
* [清理缓存命令]
* [统一命名空间命令] 
* [其他命令]
    

## 数据缓存命令

数据缓存命令可以备份对应路径的数据至本集群的磁盘，以便于后续可以读取本地数据，无需读取OSS等后端上的数据。
```shell
jindo fsx -load -data <options> <path>
```
|  **参数**  |  **描述**  |
| --- | --- |
|  options  |  各种可选参数：<br/> -s：表示缓存过程同步执行。即缓存完成前命令不退出，日志直接打印在控制台上，推荐开启。<br/>      -replica：缓存副本数量，默认缓存1个副本。<br/>      -R：递归缓存文件，当 path 是文件夹时需开启。      <br/> -m：加载数据到内存。       |
|  path  |  数据缓存路径。  |

推荐使用以下组合命令。

```shell
jindo fs -load -data -s -R <path>
```

## 元数据缓存命令

元数据缓存命令可以备份远端文件的元数据信息，从而后续无需从OSS等后端读取文件元数据信息。

```shell
jindo fs -load -meta <options> <path>
```

|  **参数**  |  **描述**  |
| --- | --- |
|  options  |  各种可选参数： <br/>-s：表示缓存过程同步执行，即缓存完成前命令不退出，日志直接打印在控制台上，推荐开启。<br/> -R：递归缓存文件，当path是文件夹时需开启。 |
|  path  |  元数据缓存路径。  |

推荐使用以下组合命令。

```shell
jindo fs -load -meta -s -R <path>
```

数据缓存和元数据缓存可以组合使用，当需要同时进行二者缓存时，可以搭配可选参数使用。推荐使用以下组合命令。

```shell
jindo fs -load -meta -data -s -R <path>
```
## 清理缓存命令

uncache命令可以删除本地集群中的本地备份，只存储数据在OSS标准存储上，以便于后续读取OSS上的数据。

    jindo fs -uncache <path>

## 统一命名空间命令

* 获取所有挂载点
```shell
jindo fs -mount
```
*   添加一个挂载点 
```shell
jindo fs -mount <path> <realpath>
```

*   移除挂载点
```shell    
jindo fs -unmount <path>
```
## 其他命令

执行以下命令，输出当前缓存系统的信息，例如缓存大小，缓存容量等。
```shell
jindo fs -report
```
输出信息如下
```shell
Namespace Address: 127.0.0.1:8101
Rpc Port: 8101
Started: Mon Jan 10 15:23:51 2022
Version: 4.1.0
Live Nodes: 2
Decommission Nodes: 0
Total Disk Capacity: 438.17GB
Used Disk Capacity: 5120.00MB
Total MEM Capacity: 4096.00MB
Used MEM Capacity: 0B
```
