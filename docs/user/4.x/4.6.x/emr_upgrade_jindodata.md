# EMR 集群 JindoData 升级流程

## 前提条件

* 已创建E-MapReduce EMR-5.6.0/EMR-3.40.0或以上版本的集群。

## 准备软件包和升级脚本

登录EMR集群的Master节点，并将下载的patch包放在hadoop用户的HOME目录下，将patch包解压缩后，使用hadoop用户执行操作。

```bash
su - hadoop
cd /home/hadoop/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindodata-patches.tar.gz
tar zxf jindodata-patches.tar.gz
```

下载 JindoData 软件包 jindofsx-{VERSION}.tar.gz ，放在解压后的目录。

```bash
cd jindodata-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/4.6.12/jindofsx-4.6.12-linux.tar.gz

ls -l
```

jindodata-patches 内容示例如下：
```bash
-rwxr-xr-x 1 hadoop hadoop       580 May 30 20:28 apply_all.sh
-rwxr-xr-x 1 hadoop hadoop       677 May 30 20:32 apply.sh
-rw-r--r-- 1 hadoop hadoop        40 May 14 12:38 hosts
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindofsx-4.6.12-linux.tar.gz
```

## 配置升级节点信息

编辑patch包下的hosts文件，添加集群所有节点的host name，如emr-header-1或emr-worker-1，文件内容以行分割。

```bash
cd jindodata-patches
vim hosts
```

hosts文件内容示例如下：
```bash
emr-header-1
emr-worker-1
emr-worker-2
```

## 执行升级

通过apply_all.sh 脚本执行修复操作。

```bash
./apply_all.sh $JINDODATA_VERSION
```

如

```bash
./apply_all.sh 4.6.12
```

脚本执行完成后，返回如下提示信息。

```
>>> updating ...  emr-worker-1
>>> updating ...  emr-worker-2
### DONE
```


## 升级后重启服务

升级完成后在EMR控制台上重启JindoData服务的所有组件，完成升级。

