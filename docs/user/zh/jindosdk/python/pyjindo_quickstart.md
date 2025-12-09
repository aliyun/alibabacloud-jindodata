# 使用 PyJindo 访问阿里云 OSS/OSS-HDFS

## 背景

本文指导如何使用 Python 的工具包 PyJindo 操作 OSS/OSS-HDFS。PyJindo 兼容 python3.6 以上版本。

## 部署环境

### 下载对应 Python 版本的 PyJindo 安装包

1.  下载最新的 tar.gz 包 jindosdk-x.y.z.tar.gz（x.y.z表版本号），下载链接见 [下载页面](../jindosdk_download.md)。
    
2.  部署 jindosdk-6.10.3.tar.gz，whl安装包位于完整产出物 `jindosdk-x.x.x/lib/site-packages/` 的子目录中。多平台部署说明参见 [部署文档](../jindosdk_deployment_multi_platform.md)。

3.  以`Python3.6`版本为例，请安装pyjindo-x.y.z-cp`36`-abi3-linux_x86_64.whl
    
```
    .
    ├── bin
    │   ├── xxx
    ├── conf
    │   ├── xxx
    ├── include
    │   ├── xxx
    ├── lib
    │   ├── xxx
    │   ├── native
    │   │   ├── xxxx
    │   └── site-packages
    │       ├── pyjindo-x.y.z-cp310-abi3-linux_x86_64.whl
    │       ├── pyjindo-x.y.z-cp311-abi3-linux_x86_64.whl
    │       ├── pyjindo-x.y.z-cp312-abi3-linux_x86_64.whl
    │       ├── pyjindo-x.y.z-cp36-abi3-linux_x86_64.whl
    │       ├── pyjindo-x.y.z-cp37-abi3-linux_x86_64.whl
    │       ├── pyjindo-x.y.z-cp38-abi3-linux_x86_64.whl
    │       └── pyjindo-x.y.z-cp39-abi3-linux_x86_64.whl
    ├── plugins
    │   └── xxxx
    ├── tools
    │   ├── xxx
    └── versions
        ├── xxx
```

### 确认存在以下环境变量用以读取配置

```bash
export JINDOSDK_CONF_DIR=/etc/taihao-apps/jindosdk-conf
export HADOOP_CONF_DIR=/etc/taihao-apps/hadoop-conf
```

1.  在阿里云EMR环境中默认存在以上配置，无需配置。
    
2.  非EMR中配置方式参见：[《在非EMR集群中部署JindoSDK》](https://help.aliyun.com/zh/emr/emr-on-ecs/user-guide/deploy-jindosdk-in-an-environment-other-than-emr)。其中，Hadoop配置文件及HADOOP\_CONF\_DIR不是必须，仅为兼容HADOOP环境中的配置。

### 安装 PyJindo

以 Python3.8 环境安装最新版本的 pyjindo-6.10.3 为例

```bash
python3.8 -m ensurepip
python3.8 -m pip install pip --upgrade --trusted-host mirrors.aliyun.com -i http://mirrors.aliyun.com/pypi/simple/
python3.8 -m pip install pyjindo-6.10.3-cp38-abi3-linux_x86_64.whl
```

### 编写 python 测试程序

```python
from pyjindo import fs

bucket = "jindosdk-yanbin-sh"
endpoint = bucket + ".cn-shanghai.oss-dls.aliyuncs.com"
root_path = "oss://" + endpoint + "/"
sub_dir = root_path + "pyjindotest/"
file_path = root_path + "hello.txt"
file_path2 = sub_dir + "hello.txt"
config = fs.read_config()
fs = fs.connect(root_path, "root", config)
# open（path, mode）模式 w,文件不存在创建一个文件
out_file = fs.open(file_path,"wb")
out_file.write(str.encode("hello world, pyjindo")) #写
out_file.close()

in_file = fs.open(file_path, "rb")
data = in_file.read() # 读
print("写入的数据为%s."%(data))
in_file.close()

# 列出文件
ls_file = fs.listdir(root_path)
print("目录文件为%s." %(ls_file))

# 创建目录
fs.mkdir(sub_dir)
# 移动并重命名文件
fs.rename(file_path, file_path2)
# 列出文件
mv_file = fs.listdir(sub_dir)
print("移动后的目录文件为%s." %(mv_file))

# 删除测试文件，重新列出文件
fs.remove(file_path2)
de_file = fs.listdir(sub_dir)
print("删除文件后的pyjindotest目录下文件为%s." %(de_file))
```

### 执行测试

```bash
python3.8 fs_test.py
```

### 执行结果

```
写入的数据为b'hello world, pyjindo'.
目录文件为[<FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/.sysinfo/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/apps/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/flume/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hbase/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hello.txt': type=File, size=20>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyarrowtest/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/spark-history/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/tmp/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/user/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/yarn/': type=Directory>].
移动后的目录文件为[<FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyjindotest/hello.txt': type=File, size=20>].
删除文件后的pyjindotest目录下文件为[].
```

### 日志等级

调整JINDOSDK\_CONF\_DIR下的jindosdk.cfg配置，emr上对应/etc/taihao-apps/jindosdk-conf

```toml
[common]
logger.dir = /var/log/emr/jindosdk
logger.level = 2
logger.verbose = 0
logger.sync = false
logger.jnilogger = true
logger.consolelogger = false
logger.cleaner.enable = true
```

|  配置项 |  描述  |
| --- | --- |
| logger.dir | 日志输出目录。 |
| logger.level | 推荐为2，level<=1，表示WARN；level>1，表示INFO。 |
| logger.verbose | 详细日志等级，范围为0-99，值越大日志越详细。 |
| logger.sync | 推荐为false。true表示同步输出日志。 |
| logger.jnilogger | 使用jni时生效，与pyjindo无关。 |
| logger.consolelogger | 终端输出时生效，与pyjindo无关。 |
| logger.cleaner.enable | 推荐为true，false表示关闭日志自动清理。 |

### API 说明

### Config 类

|  成员函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  set(key, val)  |  无  |  设置字符串类型配置，key与val均为str  |
|  get(key, default='')  |  str  |  获取字符串类型配置  |
|  contains(key)  |  bool  |  是否存在配置key  |

配置说明参见：[客户端常用配置](../jindosdk_configuration.md)

### FileType 枚举

|  枚举类型  |  枚举值  |  描述  |
| --- | --- | --- |
|  Unknown  |  0  |  \-  |
|  Directory  |  1  |  目录  |
|  File  |  2  |  文件  |
|  Symlink  |  3  |  软链  |

### FileInfo 类

|  成员属性  | 返回值类型    |  描述  |
| --- |----------| --- |
|  type  | FileType |  文件类型  |
|  is\_file  | bool     |  是否为文件  |
|  is\_dir  | bool     |  是否为目录  |
|  is\_symlink  | bool     |  是否为软链  |
|  path  | str      |  路径  |
|  user  | str      |  用户  |
|  group  | str      |  用户组  |
|  size  | int      |  文件大小  |
|  perm  | int      |  文件权限  |
|  atime  | datetime |  文件最后访问时间  |
|  mtime  | datetime |  文件最后修改时间  |

### FileStream 类

|  成员函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  readable()  |  bool  |  是否可读  |
|  writable()  |  bool  |  是否可写  |
|  seekable()  |  bool  |  是否可seek  |
|  closed()  |  bool  |  是否已关闭  |
|  close()  |  无，失败时抛出IOError  |  关闭文件  |
|  size()  |  int, 失败时抛出IOError  |  文件大小（仅可读时使用）  |
|  tell()  |  int, 失败时抛出IOError  |  文件流位置  |
|  flush()  |  无，失败时抛出IOError  |  刷新缓存  |
|  write(data)  |  无，失败时抛出IOError  |  写数据，data为bytes类型，为写入buffer  |
|  read(nbytes)  |  bytes, 失败时抛出IOError  |  读数据，nbytes为int，为读取大小  |
|  pread(nbytes, offset)  |  bytes, 失败时抛出IOError  |  随机读数据，nbytes为int，表读取大小；offset为int，表文件位移  |
|  readall()  |  bytes, 失败时抛出IOError  |  读取整个文件  |
|  download(stream\_or\_path, buffer\_size)  |  无, 失败时抛出IOError  |  下载，读取当前文件，写入到本地路径或着目标流。stream\_or\_path可以是本地路径也可以文件流。  |
|  upload(stream, buffer\_size)  |  无，失败时抛出IOError  |  上传，读取stream流，写入当前文件。  |

### FileSystem 类型

|  成员函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  mkdir(path, recursive)  |  bool，失败时抛出IOError  |  创建目录，recursive为bool，表是否递归创建父目录  |
|  rename(src, dest)  |  bool，失败时抛出IOError  |  rename文件，把src路径移动到dest路径，src和dest路径为str  |
|  get\_file\_info(path)  |  FileInfo，失败时抛出IOError  |  获取文件信息  |
|  exists(path)  |  bool，失败时抛出IOError  |  文件是否存在  |
|  listdir(path, recursive)  |  FileInfo列表，失败时抛出IOError  |  列举文件，recursive为bool，表是否递归  |
|  chmod(path, perm)  |  bool，失败时抛出IOError  |  类似setPermission，perm为权限，如0o777  |
|  chown(path, owner, group)  |  bool，失败时抛出IOError  |  类似setOwner，owner为str，表用户名，group为str，表用户组  |
|  open(path, mode, buffer\_size=None)  |  FileStream，失败时抛出IOError  |  打开文件，mode支持`rb`, `wb`。buffer\_size默认为64k，为upload、download、copy\_file时的buffer大小。  |
|  download(path, stream\_or\_path, buffer\_size=None)  |  无, 失败时抛出IOError  |  下载，读取远端path的文件，到本地路径或着目标流。stream\_or\_path可以是本地路径也可以文件流。buffer\_size默认为64k，如配置中存在`fs.oss.read.buffer.size`，则以配置为准。  |
|  upload(path, stream, buffer\_size=None)  |  无, 失败时抛出IOError  |  上传stream流到远端path。buffer\_size默认为64k，如配置中存在`fs.oss.write.buffer.size`，则以配置为准。  |
|  copy\_file(src, dest, buffer\_size=None)  |  无, 失败时抛出IOError  |  拷贝文件，从src路径拷贝到dest路径。buffer\_size默认为64k，如配置中存在`fs.oss.read.buffer.size`，则以配置为准。  |

### fs 模块

|  全局函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  read\_config()  |  Config  |  读取配置 1.  检查环境变量JINDOSDK\_CONF\_DIR，如存在，则从$JINDOSDK\_CONF\_DIR/jindosdk.cfg中读取配置。      2.  检查环境变量HADOOP\_CONF\_DIR，如存在且jindosdk.cfg中未配置hadoopConf.enable为false，则从$HADOOP\_CONF\_DIR/core-sites.xml中读取配置。       |
|  connect(uri, user, config)  |  FileSystem，失败时抛出IOError  |  初始化 FileSystem  |

## API 兼容性

### ossfs 模块兼容 fsspec 接口

pyjindo 同样兼容 fsspec 同步接口，但仅兼容 python3.7 以上版本。仅接口兼容，读取配置、日志等级等使用方式不变。

#### 使用前需要安装依赖 fsspec

以 Python3.8 环境安装 fssepc 为例

```bash
python3.8 -m pip install fsspec --trusted-host mirrors.aliyun.com -i http://mirrors.aliyun.com/pypi/simple/
```

#### 编写 python 测试程序

```python
from pyjindo.ossfs import JindoOssFileSystem

bucket = "jindosdk-yanbin-sh"
endpoint = bucket + ".cn-shanghai.oss-dls.aliyuncs.com"
root_path = "oss://" + endpoint + "/"
sub_dir = root_path + "pyjindotest/"
file_path = root_path + "hello.txt"
file_path2 = sub_dir + "hello.txt"
fs = JindoOssFileSystem(root_path)
# open（path, mode）模式 w,文件不存在创建一个文件
out_file = fs.open(file_path,"wb")
out_file.write(str.encode("hello world, pyjindo")) #写
out_file.close()

in_file = fs.open(file_path, "rb")
data = in_file.read() # 读
print("写入的数据为%s."%(data))
in_file.close()

# 列出文件
ls_file = fs.ls(root_path, detail=False)
print("目录文件为%s." %(ls_file))
assert file_path in fs.glob(root_path + "*")

# 创建目录
fs.mkdir(sub_dir)
# 移动并重命名文件
fs.rename(file_path, file_path2)
# 列出文件
mv_file = fs.listdir(sub_dir, detail=False)
print("移动后的目录文件为%s." %(mv_file))

# 删除测试文件，重新列出文件
fs.rm(file_path2)
de_file = fs.ls(sub_dir)
print("删除文件后的pyjindotest目录下文件为%s." %(de_file))
```


### 执行测试

```bash
python3.8 ossfs_test.py
```

### 执行结果：

```
写入的数据为b'hello world, pyjindo'.
目录文件为['oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/.sysinfo/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/apps/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/flume/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hbase/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hello.txt', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyarrowtest/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyjindotest/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/spark-history/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/test/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/tmp/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/user/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/yarn/'].
移动后的目录文件为['oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyjindotest/hello.txt'].
删除文件后的pyjindotest目录下文件为[].
```

更多接口说明参见 fssepc [官网文档](https://filesystem-spec.readthedocs.io/en/latest/api.html#fsspec.spec.AbstractFileSystem)。