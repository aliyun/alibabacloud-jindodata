# 使用 JindoSDK C/C++ 访问阿里云 OSS

提供类似 POSIX 的接口方法访问阿里云 OSS。

## 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。
解压缩后可在`include/`目录下找到`jindosdk.h`接口文件。

## 配置环境变量

* 配置`JINDOSDK_HOME`, `LD_LIBRARY_PATH`

解压下载的安装包，以安装包内容解压在`/usr/lib/jindosdk-4.2.0`目录为例：
```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-4.2.0
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${JINDOSDK_HOME}/lib/native/
```

## 配置文件
在`$JINDOSDK_HOME/conf`目录下添加配置文件, 文件名为`jindosdk.cfg`，使用 INI 风格配置文件，示例如下：

```
[common]
logger.dir = /tmp/jindosdk-log

[jindosdk]
# 已创建的Bucket对应的Endpoint。以华东1（杭州）为例，填写为oss-cn-hangzhou.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
# 用于访问OSS的AccessKey ID和AccessKey Secret。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```

## 支持的接口

目前 JindoSDK C/C++ 已经支持以下 API 访问阿里云 OSS：

| 接口            | 说明                                     |
| --------------- | ---------------------------------------|
| jindo_set_confdir()         | 设置配置文件所在目录         |
| jindo_create_filesystem()   | 创建文件系统               |
| jindo_set_conf()            | 配置文件系统参数            |
| jindo_init()                | 文件系统初始化             |
| jindo_free_filesystem()     | 释放文件系统               |
| jindo_mkdir()               | 创建目录                  |
| jindo_rename()              | 重命名文件或目录           |
| jindo_remove()              | 删除文件或目录             |
| jindo_getattr()             | 获取文件状态               |
| jindo_exists()              | 判断文件是否存在            |
| jindo_listdir()             | 获取目录下文件列表          |
| jindo_open()                | 打开文件                  |
| jindo_close()               | 关闭文件                  |
| jindo_write()               | 写文件                   |
| jindo_pread()               | 读文件                   |
| jindo_seek()                | 移动文件读取指针到指定位置   |
| jindo_flush()               | 缓冲写入文件              |

## 示例
编写程序使用 JindoSDK 访问 OSS。

```
#include <iostream>
#include <string>
#include "jindosdk.h"

using namespace std;

int main(int argc, char *argv[]) {
    const char *test_fs_oss_uri = "oss://example-bucket/jindosdk/";
    auto fs = jindo_create_filesystem();
    jindo_set_conf(fs, "fs.oss.endpoint", "oss-cn-shanghai.aliyuncs.com");
    jindo_set_conf(fs, "fs.oss.accessKeyId", "your_key_id");
    jindo_set_conf(fs, "fs.oss.accessKeySecret", "your_key_secret");
    int ret = jindo_init(fs, test_fs_oss_uri, "admin");
    if (ret != 0) {
        cerr << "failed to init filesystem: " << jindo_get_last_error() << endl;
        return ret;
    }
    const char *path = "oss://example-bucket/jindosdk/testMkdir";
    ret = jindo_mkdir(fs, path, 0777);
    if (ret != 0) {
        cerr << "failed to mkdir: " << jindo_get_last_error() << endl;;
        return ret;
    }
    path = "oss://example-bucket/jindosdk/testSimpleFile";
    auto fh = jindo_open(fs, path, "w", 0777, false);
    if (fh == nullptr) {
        cerr << "failed to open: " << jindo_get_last_error() << endl;
        return -1;
    }
    string data = "hello world!";
    auto wsize = jindo_write(fs, fh, data.data(), data.size());
    if (data.size() != wsize) {
        cerr << "failed to write: " << jindo_get_last_error() << endl;
    }
    ret = jindo_close(fs, fh);
    if (ret != 0) {
        cerr << "failed to close: " << jindo_get_last_error() << endl;;
        return ret;
    }

    fh = jindo_open(fs, path, "r", 0777, false);
    if (fh == nullptr) {
        cerr << "failed to open: " << jindo_get_last_error() << endl;
        return -1;
    }
    char buf[80];
    auto rsize = jindo_pread(fs, fh, buf, data.length(), 0);
    if (data.size() != rsize) {
        cerr << "failed to read: " << jindo_get_last_error() << endl;
    }
    ret = jindo_close(fs, fh);
    if (ret != 0) {
        cerr << "failed to close: " << jindo_get_last_error() << endl;;
        return ret;
    }
    jindo_free_filesystem(fs);
    return 0;
}
```
