# JindoFS服务化 快照功能使用介绍
对于JindoFS服务化场景中使用快照功能，可以参考下面的命令。
为了方便下面命令的解释，我们假设oss://oss-dfs-test这个Bucket已经开启JindoFS服务化。下面所有的例子都会针对这个bucket进行操作。
## 开启快照功能
假设我们先在oss://oss-dfs-test下创建一个目录TestSnapshot。
```bash
hadoop fs -mkdir oss://oss-dfs-test/TestSnapshot
```
默认情况下，目录的快照功能是关闭的。需要开启和关闭目录的快照功能，需要使用JindoFS的shell命令行。具体的开启快照的格式为
```bash
jindo dlsadmin -allowSnapshot -dlsUri <path>
```
如果我们要开启前面创建的TestSnapshot的快照功能。可以输入如下的命令：
```bash
jindo dlsadmin -allowSnapshot -dlsUri oss://oss-dfs-test/TestSnapshot
```
## 创建快照
在一个目录开启快照功能后，可以通过hadoop的shell命令。具体格式为：
```bash
hadoop fs -createSnapshot <path> [<snapshotName>]
```
例如上面例子的中的TestSnapshot，我们进行了一些目录和文件的操作。然后可以创建一个快照对此刻的快照的状态进行保存。假设我们命名快照名为S1。
```bash
#下面这些为创建测试的文件和目录
hadoop fs -mkdir oss://oss-dfs-test/TestSnapshot/dir1
hadoop fs -mkdir oss://oss-dfs-test/TestSnapshot/dir2
hadoop fs -touchz oss://oss-dfs-test/TestSnapshot/dir1/file1
hadoop fs -touchz oss://oss-dfs-test/TestSnapshot/dir3/file2

#创建快照S1
hadoop fs -createSnapshot oss://oss-dfs-test/TestSnapshot S1
```
## 访问快照中的目录和文件
### 快照目录的格式
为了区别于文件系统现有的目录和文件，访问快照中的文件和目录需要添加快照的信息来指出自己要访问的快照名。对于一个开启了快照的目录，我们要访问它下面某个快照的目录和文件，我们可以下面的格式来访问：
​

```bash
<snapshotRoot>/.snapshot/<snapshotName>/<actual subPath>
```
​

其中snapshotRoot为快照的根目录，也就是开启快照命令中dlsUri参数制定的路径。snapshotName是之前创建的快照名。制定了快照名之后，之后的路径就是原来快照根目录（snapshotRoot）下要访问的路径。
比如上面的例子TestSnapshot目录，我们如果要列出oss://oss-dfs-test/TestSnapshot/dir1下的文件。可以用常规的ls命令。
```bash
hadoop fs -ls oss://oss-dfs-test/TestSnapshot/dir1
```
​

由于我们在TestSnapshot下开启的快照，并创建了快照S1。我们也可以通过下面的路径来列出快照S1下的目录和文件。
```bash
hadoop fs -ls oss://oss-dfs-test/TestSnapshot/.snapshot/S1/dir1
```
其中.snapshot/S1 就是用来表明目录的快照的信息。
## 重命名快照
对于一个已经创建的快照，可以通过下面的命令进行重命名
```bash
hadoop fs -renameSnapshot <path> <oldName> <newName>
```
例如上面例子的中的TestSnapshot的快照S1，我们可以用下面的命令进行重命名为S100
​

```bash
hadoop fs -renameSnapshot oss://oss-dfs-test/TestSnapshot S1 S100
```
## 删除快照
当不需要快照的时候，可以运行下面的命令来删除制定的快照。
```bash
hadoop fs -deleteSnapshot <path> <snapshotName>
```
在上面的例子中，我们可以运行下面的命令来删除之前创建并重命名的快照S100
```bash
hadoop fs -deleteSnapshot oss://oss-dfs-test/TestSnapshot S100
```
## 关闭快照功能
当我们需要关闭目录的快照功能的时候，我们同样需要使用JindoFS的shell命令行。具体的关闭快照的格式为
```bash
jindo dlsadmin -disallowSnapshot <path>
```
### 注意事项
如果需要关闭目录的快照功能，首先要确保该目录下的所有快照已经被删除。删除快照的命令可以参考前面的删除快照命令。如果该目录下还存在快照，关闭快照功能会报错。
对于我们前面的例子TestSnapshot，假设我们已经删除了所有快照。我们可以用下面的命令来关闭目录的快照功能。
```bash
jindo dlsadmin -disallowSnapshot -dlsUri oss://oss-dfs-test/TestSnapshot
```
