# 使用 PyJindo 访问 OSS

---

## 环境要求

支持 Linux 与 Mac OS 环境，Python 3.5 至 3.9。Windows 及其他操作系统环境、Python2、Python3 其他版本暂未支持。

## 什么是 PyJindo

Aliyun Object Storage Service (Aliyun OSS) 是阿里云旗下的对象存储服务，而 JindoFS SDK 则提供了高效的访问能力。PyJindo 则是 JindoFS SDK 的 Python 版本，目的是让 Python 用户高效访问 OSS。

## 为什么使用 PyJindo

PyJindo 基于 JindoFS SDK，对 OSS 文件操作提供了多种优化支持，效率较高，尤其适合于大文件读的场景。

## PyJindo 使用

用户只需下载 PyJindo 扩展包安装文件 (.whl)，然后用 pip 安装即可：
```
pip install <package-file.whl>
```
其中 <package-file.whl> 就是下载得到的安装文件，[下载页面](/docs/jindofs_sdk_download.md)。其中：
* pyjindo-${version}-cp35-abi3-linux_x86_64.whl 用于 Linux 环境
* pyjindo-${version}-cp35-abi3-macosx_10_15_x86_64.whl 用于 Mac OS 环境

详细使用文档参见 [PyJindo 介绍及 API 文档](jindosdk_python_api.md)
