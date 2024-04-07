# Accessing OSS/OSS-HDFS via POSIX Interface

## Preparation

* In EMR environments, JindoSDK is pre-installed and ready to use. Note:
    * To access OSS-HDFS, create an EMR cluster with version EMR-3.42.0 or higher.
* In non-EMR environments, install and deploy JindoSDK first. Follow the instructions in [Deploying JindoSDK in a Hadoop Environment](../../jindosdk/jindosdk_deployment_hadoop.md).
    * For OSS-HDFS access, use JindoSDK version 4.x or later.

## Dependency Setup

* For EMR versions EMR-3.44.0 and higher or EMR-5.10.0 and higher, necessary dependencies are installed by default.

* For JindoSDK versions up to 4.5.0:

```bash
# On CentOS
yum install -y fuse3 fuse3-devel
# On Debian
apt install -y fuse3 libfuse3-dev
```

* For JindoSDK versions starting from 4.5.1, you need libfuse version 3.7+. Here's how to install fuse-3.11 as an example:

```bash
# Build fuse requires meson & ninja, for Debian: apt install -y pkg-config meson ninja-build
sudo yum install -y meson ninja-build

# Compile fuse requires a newer g++ (only for CentOS)
sudo yum install -y scl-utils
sudo yum install -y alinux-release-experimentals
sudo yum install -y devtoolset-8-gcc devtoolset-8-gdb devtoolset-8-binutils devtoolset-8-make devtoolset-8-gcc-c++
sudo su -c "echo 'source /opt/rh/devtoolset-8/enable' > /etc/profile.d/g++.sh"
source /opt/rh/devtoolset-8/enable
sudo ln -s /opt/rh/devtoolset-8/root/bin/gcc /usr/local/bin/gcc
sudo ln -s /opt/rh/devtoolset-8/root/bin/g++ /usr/local/bin/g++

# Compile & install libfuse
wget https://github.com/libfuse/libfuse/releases/download/fuse-3.11.0/fuse-3.11.0.tar.xz
xz -d fuse-3.11.0.tar.xz
tar xf fuse-3.11.0.tar
cd fuse-3.11.0/
mkdir build; cd build
meson ..
sudo ninja install
```

# Mounting JindoFuse

After configuring JindoSDK:

1. Create a mount point:
```bash
mkdir -p <mountpoint>
```
2. Mount Fuse:
```bash
jindo-fuse <mount_point> -ouri=<osspath>
```
This command starts a background daemon that mounts the specified `<oss_path>` to the local filesystem at `<mount_point>`.

Replace `<mountpoint>` with a local path.

Replace `<osspath>` with the OSS/OSS-HDFS path to be mounted, either the bucket root or a subdirectory. E.g., `oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/subdir/`

Mounting OSS and OSS-HDFS paths follows the same procedure with slightly different endpoints. Refer to [OSS/OSS-HDFS Quick Start](../oss_quickstart.md) for root path examples.

1. Verify successful mounting by checking the running jindo-fuse process and its parameters:
```bash
ps -ef | grep jindo-fuse
```

# Accessing JindoFuse

Assuming you've mounted JindoFS service at `/mnt/oss`, perform the following operations:

1. List all directories in `/mnt/oss`:
```bash
ls /mnt/oss/
```
2. Create a directory:
```bash
mkdir /mnt/oss/dir1
```
3. Write a file:
```bash
echo "hello world" > /mnt/oss/dir1/hello.txt
```
4. Read the file:
```bash
cat /mnt/oss/dir1/hello.txt
```
It displays `hello world`.

5. Delete the directory:
```bash
rm -rf /mnt/oss/dir1/
```

# Unmounting JindoFuse

To unmount the previously mounted point, run:
```bash
umount <mount_point>
```

# Auto-unmounting JindoFuse

You can use `-oauto_unmount` option to automatically unmount upon exit.

With this option, sending SIGINT (e.g., `killall -9 jindo-fuse`) to the jindo-fuse process will cause it to safely unmount before exiting.

# Appendix

For more information on commands and descriptions, see the [JindoFUSE User Guide](../../jindofuse/jindofuse_quickstart.md).