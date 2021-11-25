JindoFS 提供了访问 HDFS 的能力，可以通过配置 HDFS 作为 JindoFS 的后端存储，使 Fluid 通过 JindoFS 来访问 HDFS 上的数据，同时 JindoFS 也提供了对 HDFS 上的数据以及元数据的缓存加速功能。
本文档展示了如何在 Fluid 中以声明式的方式完成 JindoFS 部署，对接 HDFS 数据源。

#### 创建命名空间
```shell
kubectl create ns fluid-system
```
#### [下载 Fluid 安装包](./jindo_fluid_download.md)

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


其中 csi-nodeplugin-fluid-xx 的数量应该与k8s集群中节点node的数量相同。

### 1、创建 dataset 和 JindoRuntime


#### 1.1、HA 集群从 HDFS 配置文件创建ConfigMap，非 HA 集群请略过此步骤


从 HDFS 集群上找到 hdfs-site.xml 配置文件（阿里云EMR集群在 /etc/ecm/hadoop-conf 路径下），需要将其中 rpc 的访问地址中域名的部分参数，修改成可访问的内网 IP 地址，例如：


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


创建 ConfigMap


```shell
$ kubectl create configmap hdfsconfig --from-file=/path/to/hdfs-site.xml
```


#### 1.2、创建 Dataset 资源对象


首先创建一个名为 dataset.yaml 的文件，针对 HDFS 集群分为 HA 集群和非 HA 集群两种不同的情况。


#### HA 集群


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: hdfs://emr-cluster/
      name: hadoop
```


其中 `mountPoint` 为需要访问的 HDFS 路径（阿里云EMR集群默认名字为 emr-cluster，如下所示）


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
  name: hadoop
spec:
  mounts:
    - mountPoint: hdfs://<namenodeIP>:<port>/
      name: hadoop
```


其中 `mountPoint` 为 HDFS 集群的 namenode 的 IP 地址和端口的组合
#### 
#### 1.3、创建 Dataset


```shell
$ kubectl create -f dataset.yaml
```


#### 1.4 创建 JindoRuntime 资源对象


创建一个 JindoRuntime 对应的 JindoFS 集群，有 Cache 和 NoCache 两种模式。Cache 模式提供对远端 HDFS 上数据缓存以及元数据缓存；NoCache 模式即简单作为 HDFS connector 提供访问 HDFS 的能力。


#### Cache 模式


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1/
        quota: 290G
        high: "0.9"
        low: "0.2"
  hadoopConfig: hdfsconfig
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```


#### NoCache 模式


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1/
        quota: 290G
        high: "0.9"
        low: "0.2"
  hadoopConfig: hdfsconfig
  fuse:
    properties:
      jfs.cache.data-cache.enable: "false"
      jfs.cache.meta-cache.enable: "false"
      jfs.cache.data-cache.slicecache.enable: "false"
```


- `replicas`：表示创建 JindoFS 集群的 worker 的数量。
- `mediumtype`： JindoFS 暂只支持HDD/SSD/MEM中的其中一种。
- `path`：存储路径，支持一块盘或者多块盘
- `quota`：缓存最大容量，单位G。



多块盘多个quota请参考


```yaml
path: /mnt/disk1/,/mnt/disk2/,/mnt/disk3/
quotaList: 290G,290G,290G
```


其中 path 和 quota 的数量应该相同。


- `high`：水位上限比例 / `low`： 水位下限比例。
- `configmap-name`须为之前创建的 ConfigMap 资源对象，该 ConfigMap 必须与创建的 JindoRuntime 同处于同一Namespace。其中必须包含以`hdfs-site.xml`为键的键值对，Fluid会检查
该ConfigMap中的内容，并从中选取`hdfs-site.xml`的键值对并以文件的形式挂载到 JindoFS 的各个 Pod 中。



#### 1.5、创建 JindoRuntime


执行 create 命令


```shell
$ kubectl create -f runtime.yaml
```


#### 查看 dataset 状态


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop     180.01GiB      0.00B      261.00GiB            0.0%          Bound   62m
```


#### 检查 PV，PVC 创建情况


```shell
$ kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/hadoop   100Gi      RWX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/hadoop   Bound    hadoop   100Gi      RWX                           58m
```


#### 检查 jindoruntime 部署情况


```shell
$ kubectl get jindoruntime hadoop
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hadoop    Ready           Ready                     62m
```


### 2、指定 FUSE 用户


使用`user`参数指定通过fuse读写时用户信息，如指定通过hadoop用户来进行访问。


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1/
        quota: 290G
        high: "0.9"
        low: "0.2"
  hadoopConfig: hdfsconfig
  user: hadoop
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```
