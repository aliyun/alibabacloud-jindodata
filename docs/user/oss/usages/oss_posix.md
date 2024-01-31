# 通过 POSIX 访问 OSS/OSS-HDFS

## 环境准备

*   EMR环境中，默认已安装 JindoSDK，可以直接使用。注意：
    
    *   访问 OSS-HDFS，需创建EMR-3.42.0及以上版本或EMR-5.8.0及以上版本的集群。
        
*   非 EMR 环境，请先安装部署 JindoSDK。部署方式请参考 [《在 Hadoop 环境中部署 JindoSDK》](/docs/user/6.x/jindosdk/jindosdk_deployment_hadoop.md)。
    
    *   访问 OSS-HDFS，需部署 JindoSDK 4.x 及以上版本。
        
## 依赖准备

* EMR-3.44.0及以上版本或EMR-5.10.0及以上版本，已默认安装所需依赖，可跳过本章节。

* JindoSDK 4.5.0 及之前版本

```bash
# CentOS
yum install -y fuse3 fuse3-devel
# Debian
apt install -y fuse3 libfuse3-dev
```

* JindoSDK 4.5.1 及以上版本，需依赖libfuse 3.7+，以安装fuse-3.11为例：
```bash
# build fuse required meson & ninja, for debian: apt install -y pkg-config meson ninja-build
sudo yum install -y meson ninja-build

# compile fuse required newer g++ (only CentOS)
sudo yum install -y scl-utils
sudo yum install -y alinux-release-experimentals
sudo yum install -y devtoolset-8-gcc devtoolset-8-gdb devtoolset-8-binutils devtoolset-8-make devtoolset-8-gcc-c++
sudo su -c "echo 'source /opt/rh/devtoolset-8/enable' > /etc/profile.d/g++.sh"
source /opt/rh/devtoolset-8/enable
sudo ln -s /opt/rh/devtoolset-8/root/bin/gcc /usr/local/bin/gcc
sudo ln -s /opt/rh/devtoolset-8/root/bin/g++ /usr/local/bin/g++

# compile & install libfuse
wget https://github.com/libfuse/libfuse/releases/download/fuse-3.11.0/fuse-3.11.0.tar.xz
xz -d fuse-3.11.0.tar.xz
tar xf fuse-3.11.0.tar
cd fuse-3.11.0/
mkdir build; cd build
meson ..
sudo ninja install
```

## 挂载 JindoFuse

在完成对 JindoSDK 的配置后。

*   创建一个挂载点， 命令如下：

```bash
mkdir -p <mountpoint>
```
*   挂载 Fuse, 命令如下：
    
```bash
jindo-fuse <mount_point> -ouri=[<osspath>]
```

这个命令会启动一个后台的守护进程，将指定的 `<oss_path>` 挂载到本地文件系统的 `<mount_point>`。

`<mount_point>` 需替换为一个本地路径。

`<oss_path>` 需替换为待映射的 OSS/OSS-HDFS 路径，路径可以为 Bucket 根目录或者子目录。如 oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir/

挂载 OSS 和 OSS-HDFS 路径的方式基本相同，仅路径中的 endpoint 略有不同。根路径示例可参见：[《OSS/OSS-HDFS 快速入门》](../oss_quickstart.md)

*   确认挂载成功，jindo-fuse 进程存在，且启动参数与预期一致
    
```bash
ps -ef | grep jindo-fuse
```

## 访问 JindoFuse

如果将 JindoFS 服务挂载到了本地 /mnt/oss/，可以执行以下命令访问 JindoFuse。

1.  列出/mnt/oss/下的所有目录：
    
```bash
ls /mnt/oss/
```

2.  创建目录：
    
```bash
mkdir /mnt/oss/dir1ls /mnt/oss/
```

3.  写入文件：
    
```bash
echo "hello world" > /mnt/oss/dir1/hello.txt
```

4.  读取文件：
    
```bash
cat /mnt/oss/dir1/hello.txt
```

显示`hello world`。

5.  删除目录：
    
```bash
rm -rf /mnt/oss/dir1/
```

## 卸载 JindoFuse

想卸载之前挂载的挂载点，可以使用如下命令：

```bash
umount <mount_point>
```

## 自动卸载 JindoFuse

可以使用 `-oauto_unmount` 参数，自动卸载挂载点。

使用该参数后，可以支持 `killall -9 jindo-fuse` 发送 SIGINT 给 jindo-fuse 进程，进程退出前会自动卸载挂载点。

## 附录

更多命令及描述，详见[《JindoFUSE 使用指南》](../../jindodata/jindosdk/jindofuse_quickstart.md)