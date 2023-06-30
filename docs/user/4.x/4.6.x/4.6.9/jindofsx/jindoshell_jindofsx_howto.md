# JindoShell CLI 支持 JindoFSx 存储加速系统使用说明
JindoShell CLI 支持操作 JindoFSx 数据缓存、元数据缓存和统一命名空间等命令。

## 前提条件：
* 已部署 JindoFSx 存储加速系统

关于如何部署 JindoFSx 存储加速系统，请参考 [部署 JindoFSx 存储加速系统](/docs/user/4.x/4.6.x/4.6.9/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.6.x/4.6.9/jindofsx/deploy/deploy_jindosdk.md)

## 数据缓存命令
数据缓存命令可以备份对应路径的数据至本集群的磁盘，以便于后续可以读取本地数据，无需读取OSS等后端上的数据。
```
jindo fs -load -data <options> <path>
```

* options 各种可选参数
* path 数据缓存路径

其中 options 可选参数有:

| 参数 | 说明 |
| --- | --- |
| -s | 表示缓存过程同步执行，打印进度和执行信息，推荐开启 |
| -replica <num> | 缓存副本数量，默认缓存1个副本 |
| -R | 递归缓存文件，当 path 是文件夹是开启 |
| -cachelist <file location> | 接受本地文件，文件内容为cache列表 |

推荐使用命令组合
```
jindo fs -load -data -s -R <path>
```

## 元数据缓存命令
元数据缓存命令可以备份远端文件的元数据信息，从而后续无需从OSS等后端读取文件元数据信息。
```
jindo fs -load -meta <options> <path>
```

* options 各种可选参数
* path 数据缓存路径

其中 options 可选参数有:

| 参数 | 说明 |
| --- | --- |
| -s | 表示元缓存过程同步执行，打印进度和执行信息，推荐开启 |
| -R | 递归缓存文件，当 path 是文件夹是开启 |

推荐使用命令组合
```
jindo fs -load -meta -s -R <path>
```

数据缓存和元数据缓存可以组合使用，当需要同时进行二者缓存时，可搭配上面参数使用。

推荐使用命令组合
```
jindo fs -load -meta -data -s -R <path>
```

## 通过通配符匹配来指定缓存文件

通过指定通配符来选择在具体的 path 下需要缓存的文件，类似白名单功能

```
jindo fs -load -meta -data -s -R -filter <wildcard> -dryrun <path>
```

| 参数 | 说明 |
| --- | --- |
| -filter | 表示具体的通配符，用来对path下的所有文件进行匹配。比如*.log,如果有多个通配符用,隔开,比如*.log,*.dat |
| -dryrun| 表示单纯输出符合匹配的结果，不真正进行缓存，用于进行判断通配符是否正确|
| -thread| 表示客户端对过滤出来的文件进行缓存的线程数，默认是10个|

使用示例

```
jindo fs -load -meta -data -s -R -filter *.log oss://bucket/dir
```


## 清理缓存命令
uncache命令可以删除本地集群中的本地备份，只存储数据在OSS标准存储上，以便于后续读取OSS上的数据。
```
jindo fs -uncache <path>
```

## 统一命名空间命令
添加一个挂载点。
```
jindo admin -mount <path> <realpath>
```

移除一个挂载点。
```
jindo admin -unmount <path>
```

## 其他命令
输出当前JindoFSx 存储加速系统的一些信息，比如缓存大小，缓存容量等。
```
jindo fs -report
```

输出格式如下：
```
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