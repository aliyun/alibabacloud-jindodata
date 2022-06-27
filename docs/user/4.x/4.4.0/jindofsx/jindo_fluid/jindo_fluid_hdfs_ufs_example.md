# 加速HDFS上数据

JindoFSx 提供了访问 HDFS 的能力，可以通过配置 HDFS 作为 JindoFSx 的后端存储，使 Fluid 通过 JindoFSx 来访问 HDFS 上的数据，同时 JindoFSx 也提供了对 HDFS 上的数据以及元数据的缓存加速功能。
本文档展示了如何在 Fluid 中以声明式的方式完成 JindoFSx 部署，对接 HDFS 数据源。

#### 创建命名空间
```shell
kubectl create ns fluid-system
```
#### [下载 Fluid 安装包](jindo_fluid_download.md)

#### 使用 Helm 安装 Fluid


```shell
helm install --set runtime.jindo.enabled=true fluid fluid-<version>.tgz
```
#### 查看 Fluid 的运行状态


```shell
$ kubectl get pod -n fluid-system
NAME                                         READY   STATUS    RESTARTS   AGE
csi-nodeplugin-fluid-2mfcr                   2/2     Running   0          108s
csi-nodeplugin-fluid-l7lv6                   2/2     Running   0          108s
dataset-controller-5465c4bbf9-5ds5p          1/1     Running   0          108s
jindoruntime-controller-654fb74447-cldsv     1/1     Running   0          108s
```


其中 csi-nodeplugin-fluid-xx 的数量应该与 K8S 集群中节点node的数量相同。

### 1、创建 dataset 和 JindoRuntime


#### 1.1、HA 集群从 HDFS 配置文件中相关信息，非 HA 集群请略过此步骤


从 HDFS 集群上找到 hdfs-site.xml 配置文件
* 阿里云EMR集群在 /etc/ecm/hadoop-conf 路径下
* CDH 集群可参考 [What-is-the-Path-of-hdfs-site-xml-core-xml](https://community.cloudera.com/t5/Support-Questions/What-is-the-Path-of-hdfs-site-xml-core-xml/m-p/15180) 
  
需要将其中 rpc 的访问地址中域名的部分参数，修改成可访问的内网 IP 地址，例如：


```xml
<property>
    <name>dfs.namenode.rpc-address.emr-cluster.nn1</name>
    <value>emr-header-1.cluster-xxx:8020</value>
</property>
<property>
    <name>dfs.namenode.rpc-address.emr-cluster.nn2</name>
    <value>emr-header-2.cluster-xxx:8020</value>
</property>
```


修改为


```xml
<property>
    <name>dfs.namenode.rpc-address.emr-cluster.nn1</name>
    <value>192.168.0.1:8020</value>
</property>
<property>
    <name>dfs.namenode.rpc-address.emr-cluster.nn2</name>
    <value>192.168.0.2:8020</value>
</property>
```

从配置文件里我们可以拿到如下几个信息

* 集群namespaceService名称：emr-cluster
* 两个namenode名字：nn1 和 nn2
* 两个namenode的访问地址：192.168.0.1:8020 和 192.168.0.2:8020 



#### 1.2、创建 Dataset 资源对象


首先创建一个名为 dataset.yaml 的文件，针对 HDFS 集群分为 HA 集群和非 HA 集群两种不同的情况。


#### HA 集群

将1.1中拿到的几个信息放到 mounts.options 里面，user 代表用户权限

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hdfs
spec:
  mounts:
    - mountPoint: hdfs://emr-cluster/
      name: hdfs
      options:
        fs.hdfs.emr-cluster.dfs.ha.namenodes = nn1,nn2
        fs.hdfs.emr-cluster.dfs.namenode.rpc-address.nn1 = 192.168.0.1:8020
        fs.hdfs.emr-cluster.dfs.namenode.rpc-address.nn2 = 192.168.0.2:8020
        fs.hdfs.user = hadoop
      path: /
  accessModes:
    - ReadOnlyMany
```


其中 `mountPoint` 为需要访问的 HDFS 路径（阿里云EMR集群默认名字为 emr-cluster，如下所示）
- accessModes: 可选 ReadOnlyMany / ReadWriteMany，前者代表只读，后者代表可读写

```xml
<property>
    <name>fs.defaultFS</name>
    <value>hdfs://emr-cluster</value>
</property>
```


#### 非 HA 集群


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hdfs
spec:
  mounts:
    - mountPoint: hdfs://<namenodeIP>:<port>/
      name: hdfs
      path: /
  accessModes:
    - ReadOnlyMany
```


其中 `mountPoint` 为 HDFS 集群的 namenode 的 IP 地址和端口的组合

- accessModes: 可选 ReadOnlyMany / ReadWriteMany，前者代表只读，后者代表可读写
#### 
#### 1.3、创建 Dataset


```shell
$ kubectl create -f dataset.yaml
```


#### 1.4 创建 JindoRuntime 资源对象


创建一个 JindoRuntime 对应的 JindoFSx 集群


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hdfs
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1/
        quota: 290Gi
        high: "0.9"
        low: "0.2"
```

多块盘多个quota请参考


```yaml
path: /mnt/disk1/,/mnt/disk2/,/mnt/disk3/
quotaList: 290G,290G,290G
```


其中 path 和 quota 的数量应该相同。


#### 1.5、创建 JindoRuntime


执行 create 命令


```shell
$ kubectl create -f runtime.yaml
```


#### 查看 dataset 状态


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hdfs     180.01GiB      0.00B      261.00GiB            0.0%          Bound   62m
```


#### 检查 PV，PVC 创建情况


```shell
$ kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/hdfs       100Gi      RWX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/hdfs      Bound    hadoop   100Gi      RWX                           58m
```


#### 检查 jindoruntime 部署情况


```shell
$ kubectl get jindoruntime hadoop
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hdfs     Ready           Ready                     62m
```