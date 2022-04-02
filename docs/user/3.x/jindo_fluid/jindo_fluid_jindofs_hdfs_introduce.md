## 拥抱云原生，Fluid结合JindoFSx 存储加速系统：加速 HDFS 使用指南

JindoFSx 提供了访问 HDFS 的能力，可以通过配置 HDFS 作为 JindoFSx 的后端存储，使 Fluid 通过 JindoFSx 来访问 HDFS 上的数据，同时 JindoFSx 也提供了对 HDFS 上的数据以及元数据的缓存加速功能。

本文档展示了如何在 Fluid 中以声明式的方式完成 JindoFSx 部署，对接 HDFS 数据源。

## 前提条件

- [Fluid](https://github.com/fluid-cloudnative/fluid)
- 可与 Kubernetes 环境网络连通的 HDFS 集群

## 安装使用流程

### 1、创建命名空间
```shell
$ kubectl create ns fluid-system
```
### 2、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/351/fluid-0.6.0.tgz)

### 3、使用 Helm 安装 Fluid

```shell
$ helm install --set runtime.jindo.enabled=true fluid fluid-0.6.0.tgz
```
#### 自定义镜像
解压 `fluid-0.6.0.tgz`，修改默认`values.yaml`文件
```yaml
runtime:
  mountRoot: /runtime-mnt
  jindo:
    enabled: true
    smartdata:
      image: registry.cn-shanghai.aliyuncs.com/jindofs/smartdata:3.5.1
    fuse:
      image: registry.cn-shanghai.aliyuncs.com/jindofs/jindo-fuse:3.5.1
    controller:
      image: registry.cn-shanghai.aliyuncs.com/jindofs/jindoruntime-controller:v0.6.0-d90f9e5
```
可以修改`jindo`相关的默认`image`内容，如放到自己的`repo`上，修改完成后重新使用`helm package fluid`打包，使用如下命令更新`fluid`版本
```shell
helm upgrade --install fluid fluid-0.6.0.tgz
```
#### 更新JindoRuntime的crd
```shell
$ hdfscache kubectl get crd      
NAME                                             CREATED AT
alluxiodataloads.data.fluid.io                   2021-03-26T09:05:01Z
alluxioruntimes.data.fluid.io                    2021-03-26T09:05:01Z
databackups.data.fluid.io                        2021-03-26T09:05:01Z
dataloads.data.fluid.io                          2021-03-26T09:05:01Z
datasets.data.fluid.io                           2021-03-26T09:05:01Z
jindoruntimes.data.fluid.io                      2021-03-31T02:30:29Z
```
删除已有的jindoruntime的crd
```shell
kubectl delete crd jindoruntimes.data.fluid.io
```
解压`fluid-0.6.0.tgz`
```shell
$ ls -l fluid/
total 32
-rw-r--r--  1 frank  staff   489B  3 31 10:27 CHANGELOG.md
-rw-r--r--  1 frank  staff   270B  3 31 10:27 Chart.yaml
-rw-r--r--  1 frank  staff   2.1K  3 31 10:27 VERSION
drwxr-xr-x  8 frank  staff   256B  3 31 10:29 crds
drwxr-xr-x  6 frank  staff   192B  3 31 10:29 templates
-rw-r--r--  1 frank  staff   1.2K  3 31 13:18 values.yaml
```
创建新的crd
```shell
kubectl apply -f crds/data.fluid.io_jindoruntimes.yaml
```


### 4、查看 Fluid 的运行状态

```shell
$ kubectl get pod -n fluid-system
NAME                                         READY   STATUS    RESTARTS   AGE
csi-nodeplugin-fluid-2mfcr                   2/2     Running   0          108s
csi-nodeplugin-fluid-l7lv6                   2/2     Running   0          108s
dataset-controller-5465c4bbf9-5ds5p          1/1     Running   0          108s
jindoruntime-controller-654fb74447-cldsv     1/1     Running   0          108s
```

其中 csi-nodeplugin-fluid-xx 的数量应该与 K8S 集群中节点node的数量相同。

### 5、创建 dataset 和 JindoRuntime

#### 5.1、HA 集群从 HDFS 配置文件创建ConfigMap，非 HA 集群请略过此步骤
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

#### 5.2、创建 Dataset 资源对象

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

#### 通过节点亲和性指定数据缓存的节点
查看全部结点
```shell
$ kubectl get nodes
NAME                       STATUS   ROLES    AGE     VERSION
cn-shanghai.192.168.1.146   Ready    <none>   7d14h   v1.16.9-aliyun.1
cn-shanghai.192.168.1.147   Ready    <none>   7d14h   v1.16.9-aliyun.1
```
使用标签标识结点

```shell
$ kubectl label nodes cn-shanghai.192.168.1.146 jindo-cache=true
```

我们将使用`NodeSelector` 来管理集群中存放数据的位置，所以在这里标记期望的结点在该`Dataset`资源对象的`spec`属性中。我们定义了一个`nodeSelectorTerm`的子属性，该子属性要求数据缓存必须被放置在具有 `jindo-cache=true` 标签的节点之上。
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: hdfs://emr-cluster/
      name: hadoop
  nodeAffinity:
    required:
      nodeSelectorTerms:
        - matchExpressions:
            - key: jindo-cache
              operator: In
              values:
                - "true"
```

#### 5.3、创建 Dataset

```shell
$ kubectl create -f dataset.yaml
```

#### 5.4 创建 JindoRuntime 资源对象
创建一个 JindoRuntime 对应的 JindoFSx 集群，有 Cache 和 NoCache 两种模式。Cache 模式提供对远端 HDFS 上数据缓存以及元数据缓存；NoCache 模式即简单作为 HDFS connector 提供访问 HDFS 的能力。

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
        low: "0.8"
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
        low: "0.8"
  hadoopConfig: hdfsconfig
  fuse:
    properties:
      jfs.cache.data-cache.enable: "false"
      jfs.cache.meta-cache.enable: "false"
      jfs.cache.data-cache.slicecache.enable: "false"
```


* `replicas`：表示创建 JindoFSx 集群的 worker 的数量。
* `mediumtype`： JindoFSx 暂只支持HDD/SSD/MEM中的其中一种。
* `path`：存储路径，支持一块盘或者多块盘
* `quota`：缓存最大容量，单位G。

多块盘多个quota请参考
```yaml
path: /mnt/disk1/,/mnt/disk2/,/mnt/disk3/
quotaList: 290G,290G,290G
```
其中 path 和 quota 的数量应该相同。
* `high`：水位上限比例 / `low`： 水位下限比例。

* `configmap-name`须为之前创建的 ConfigMap 资源对象，该 ConfigMap 必须与创建的 JindoRuntime 同处于同一Namespace。其中必须包含以`hdfs-site.xml`为键的键值对，Fluid会检查
该ConfigMap中的内容，并从中选取`hdfs-site.xml`的键值对并以文件的形式挂载到 JindoFSx 的各个 Pod 中。

#### 5.5、创建 JindoRuntime

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
hadoop    Ready           Ready           Ready     62m
```

### 6、小文件缓存加速优化
Cache 模式下 JindoFSx 自动对小文件打开了缓存优化，从 HDFS 集群读取文件数据后能够自动缓存到本地磁盘。推荐通过以下预加载命令将需要读取的数据目录进行元数据缓存和数据缓存的预加载，以更好地提升后续作业的性能。
* 登陆到 JindoFSx 集群 master 所在的 pod 上
```shell
kubectl exec -ti hadoop-jindofs-master-0  -- /bin/bash
```
* 进行元数据预加载
```shell
jindo jfs -metaSync -R jfs://hadoop/dir/
```
等待执行完毕即可完成元数据缓存

* 进行小文件数据预加载
```shell
jindo jfs -cache -s -l jfs://hadoop/dir/
```
等待执行完毕即可完成数据缓存，可使用 Fuse / PV 加载数据进行机器学习训练

### 7、指定 FUSE 用户
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
        low: "0.8"
  hadoopConfig: hdfsconfig
  user: hadoop
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```
### 8、通过 nodeselector 指定节点部署 master
可以通过`nodeselector`来指定具有特定`label`属性的节点，并在其上部署master的pod。比如选择具有`kubernetes.io/hostname: cn-shanghai.192.168.0.1`的label节点。
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
        low: "0.8"
  hadoopConfig: hdfsconfig
  user: hadoop
  master:
    nodeSelector:
      kubernetes.io/hostname: cn-shanghai.192.168.0.1
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```

### 9、使用 placement 模式部署多个dataset
您可以使用`placement`在同一个节点上部署多个不同的dataset，如
创建一个名为`hadoop`的dataset
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: hdfs://emr-cluster/
      name: hadoop
  placement: "Shared"
```
创建dataset
```shell
kubectl create -f hadoop-dataset.yaml
```
创建另一个名为`hbase`的dataset
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hbase
spec:
  mounts:
    - mountPoint: hdfs://emr-cluster/
      name: hbase
  placement: "Shared"
```
创建dataset
```shell
kubectl create -f hbase-dataset.yaml
```
定义一个绑定`hadoop`的JindoRuntime
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
        low: "0.8"
  hadoopConfig: hdfsconfig
  user: hadoop
  master:
    nodeSelector:
      kubernetes.io/hostname: cn-shanghai.192.168.0.1
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```
创建runtime
```shell
kubectl create -f hadoop-runtime.yaml
```
确认runtime的状态
```shell
$ kubectl get jindoruntime hadoop                      
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hadoop   Ready          Ready          Ready        50m
```

### 确认master/worker/fuse都处于`ready`状态后，再进行下一个runtime的Create流程，暂时不能同时create多个runtime

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hbase
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1/
        quota: 290G
        high: "0.9"
        low: "0.8"
  hadoopConfig: hdfsconfig
  user: hadoop
  master:
    nodeSelector:
      kubernetes.io/hostname: cn-shanghai.192.168.0.2
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```
确认上一个`runtime`处于`ready`状态后，创建新的runtime
```shell
kubectl create -f hbase-runtime.yaml
```
确认runtime的状态
```shell
$ kubectl get jindoruntime hbase                      
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hbase    Ready          Ready          Ready        30m
```