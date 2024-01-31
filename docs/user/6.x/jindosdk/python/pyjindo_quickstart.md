# 使用 pyjindo 访问阿里云 OSS-HDFS

## 背景

本文指导如何使用 Python 的工具包 pyjindo 操作 OSS-HDFS。

## 部署环境

1.  下载对应 Python 版本的 pyjindo 安装包
    
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/6.x/6.3.0/jindodata_download.md))。

部署方式，参见[部署文档](/docs/user/6.x/jindosdk/jindosdk_deployment_muti_platform.md)

whl安装包位于 `jindosdk-x.x.x/lib/site-packages/` 子目录中
        
2.  确认存在以下环境变量用以读取配置（其中JINDOSDK\_CONF\_DIR、HADOOP\_HOME为范例路径，以 EMR 环境为例）
    

    export JINDOSDK_CONF_DIR=/etc/taihao-apps/jindosdk-conf
    export HADOOP_CONF_DIR=/etc/taihao-apps/hadoop-conf

非EMR中配置方式参见：[https://help.aliyun.com/zh/emr/emr-on-ecs/user-guide/deploy-jindosdk-in-an-environment-other-than-emr](https://help.aliyun.com/zh/emr/emr-on-ecs/user-guide/deploy-jindosdk-in-an-environment-other-than-emr)

3.  使用如下命令，安装 pyjindo 库：

以 Python3.8 环境安装最新版本的 pyjindo-6.3.0 为例

更新pip并安装

    python3.8 -m ensurepip
    python3.8 -m pip install pip --upgrade --trusted-host mirrors.aliyun.com -i http://mirrors.aliyun.com/pypi/simple/
    python3.8 -m pip install pyjindo-6.3.0-cp38-abi3-linux_x86_64.whl

如需覆盖安装，请执行

    python3.8 -m pip install pyjindo-6.3.0-cp38-abi3-linux_x86_64.whl  --force-reinstall --upgrade

### 编写 python 测试程序

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

### 执行测试

    python3.8 libtest.py

### 执行结果

    写入的数据为b'hello world, pyjindo'.
    目录文件为[<FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/.sysinfo/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/apps/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/flume/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hbase/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hello.txt': type=File, size=20>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyarrowtest/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/spark-history/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/tmp/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/user/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/yarn/': type=Directory>].
    移动后的目录文件为[<FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyjindotest/hello.txt': type=File, size=20>].
    删除文件后的pyjindotest目录下文件为[].

### 日志等级

调整JINDOSDK\_CONF\_DIR下的jindosdk.cfg配置，emr上对应/etc/taihao-apps/jindosdk-conf

    [common]
    logger.dir = /var/log/emr/jindosdk
    logger.level = 2
    logger.verbose = 0
    logger.sync = false
    logger.jnilogger = true
    logger.consolelogger = false
    logger.cleaner.enable = true

logger.dir，日志输出目录。

logger.level，推荐为2，level<=1，表示WARN；level>1，表示INFO。

logger.verbose，详细日志等级，范围为0-99，值越大日志越详细。

logger.sync，推荐为false。true表示同步输出日志。

logger.jnilogger，使用jni时生效，与pyjindo无关。

logger.consolelogger，终端输出时生效，与pyjindo无关。

logger.cleaner.enable，推荐为true，false表示关闭日志自动清理。

### API详情

### Config 类型

|  成员函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  set(key, val)  |  无  |  设置字符串类型配置  |
|  get(key, default='')  |  str  |  获取字符串类型配置  |
|  setBool(key, val)  |  无  |  设置布尔类型配置  |
|  getBool(key, val)  |  bool  |  获取布尔类型配置  |
|  contains(key)  |  bool  |  是否存在配置  |

配置说明参见：[https://gitee.com/mirrors\_aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/jindosdk/jindosdk\_configuration.md](https://gitee.com/mirrors_aliyun/alibabacloud-jindodata/blob/master/docs/user/6.x/jindosdk/jindosdk_configuration.md)

### FileType 枚举

|  枚举类型  |  枚举值  |  描述  |
| --- | --- | --- |
|  Unknown  |  0  |   |
|  Directory  |  1  |  目录  |
|  File  |  2  |  文件  |

### FileInfo 类型

|  成员属性  |  返回值类型  |  描述  |
| --- | --- | --- |
|  type  |  FileType  |  文件类型  |
|  is\_file  |  bool  |  是否为文件  |
|  is\_dir  |  bool  |  是否为目录  |
|  path  |  bool  |  路径  |
|  user  |  str  |  用户  |
|  group  |  str  |  用户组  |
|  size  |  int  |  文件大小  |
|  perm  |  int  |  文件权限  |
|  atime  |  datetime  |  文件最后访问时间  |
|  mtime  |  datetime  |  文件最后修改时间  |

### FileStream 类型

|  成员函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  readable()  |  bool  |  是否可读  |
|  writable()  |  bool  |  是否可写  |
|  seekable()  |  bool  |  是否可seek  |
|  closed()  |  bool  |  是否已关闭  |
|  close()  |  无，失败时抛出IOError  |  关闭文件  |
|  size()  |  int, 失败时抛出IOError  |  文件大小（仅可读时使用）  |
|  tell()  |  int, 失败时抛出IOError  |  文件流位置  |
|  flush()  |  无，失败时抛出IOError  |  刷新缓存  |
|  write(data)  |  无，失败时抛出IOError  |  写数据，data为bytes类型，写写入buffer  |
|  read(nbytes)  |  bytes, 失败时抛出IOError  |  读数据，nbytes为int，为读取大小  |
|  pread(nbytes, offset)  |  bytes, 失败时抛出IOError  |  随机读数据，nbytes为int，表读取大小；offset为int，表文件位移  |
|  readall()  |  bytes, 失败时抛出IOError  |  读取整个文件  |
|  download(stream\_or\_path, buffer\_size)  |  无, 失败时抛出IOError  |  下载到本地stream或路径  |
|  upload(stream, buffer\_size)  |  无，失败时抛出IOError  |  上传本地到远端  |

### FileSystem 类型

|  成员函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  mkdir(path, recursive)  |  bool，失败时抛出IOError  |  创建目录，recursive为bool，表是否递归创建父目录  |
|  rename(src, dest)  |  bool，失败时抛出IOError  |  rename文件，把src路径移动到dest路径，src和dest路径为str  |
|  get\_file\_info(path)  |  FileInfo，失败时抛出IOError  |  获取文件信息  |
|  exists(path)  |  bool，失败时抛出IOError  |  文件是否存在  |
|  listdir(path, recursive)  |  FileInfo列表，失败时抛出IOError  |  列举文件，recursive为bool，表是否递归  |
|  chmod(path, perm)  |  bool，失败时抛出IOError  |  类似setPermission，perm为权限，如0o777  |
|  chown(path, owner, group)  |  bool，失败时抛出IOError  |  类似setOwner，owner为str，表用户名，group为str，表用户组  |
|  open(path, mode, buffer\_size)  |  FileStream，失败时抛出IOError  |  打开文件，mode支持'rb', 'wb'  |
|  download(path, stream, buffer\_size)  |  无, 失败时抛出IOError  |  下载  |
|  upload(path, stream, buffer\_size)  |  无, 失败时抛出IOError  |  上传    |

### util

|  全局函数  |  返回值类型  |  描述  |
| --- | --- | --- |
|  read\_config()  |  Config  |  读取配置 1.  检查环境变量JINDOSDK\_CONF\_DIR，如存在，则从$JINDOSDK\_CONF\_DIR/jindosdk.cfg中读取配置。      2.  检查环境变量HADOOP\_CONF\_DIR，如存在且jindosdk.cfg中未配置hadoopConf.enable为false，则从$HADOOP\_CONF\_DIR/core-sites.xml中读取配置。       |
|  connect(uri, user, config)  |  FileSystem，失败时抛出IOError  |  初始化 FileSystem  |