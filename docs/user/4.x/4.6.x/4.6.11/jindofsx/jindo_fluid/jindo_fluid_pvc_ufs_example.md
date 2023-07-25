# 加速PVC上数据

使用 JindoRuntime 流程简单，在准备好基本 K8S 和 PVC 数据卷环境的条件下，您只需要耗费10分钟左右时间即可部署好需要的 JindoRuntime 环境，您可以按照下面的流程进行部署。

## 1、创建命名空间
```shell
kubectl create ns fluid-system
```
## 2、[下载 Fluid 安装包](jindo_fluid_download.md)

## 3、使用 Helm 安装 Fluid

```shell
helm install --set runtime.jindo.enabled=true fluid fluid-<version>.tgz
```
## 4、查看 Fluid 的运行状态


```shell
$ kubectl get pod -n fluid-system
NAME                                         READY   STATUS    RESTARTS   AGE
csi-nodeplugin-fluid-2mfcr                   2/2     Running   0          108s
csi-nodeplugin-fluid-l7lv6                   2/2     Running   0          108s
dataset-controller-5465c4bbf9-5ds5p          1/1     Running   0          108s
jindoruntime-controller-654fb74447-cldsv     1/1     Running   0          108s
```


其中 csi-nodeplugin-fluid-xx 的数量应该与 K8S 集群中节点node的数量相同。
## 5、测试数据源准备

### 5.1 创建 dataset 和 JindoRuntime

创建一个 resource.yaml 文件里面包含两部分：

- 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源为已存在的PVC，如例子中的 testPVC。
- 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFSx 的集群来提供缓存服务。


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: pvc
spec:
  mounts:
    - mountPoint: pvc://testPVC
      name: data
      path: /
  accessModes:
    - ReadOnlyMany
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: pvc
spec:
  replicas: 2
  tieredstore:
    levels:
      - mediumtype: HDD
        path: /mnt/disk1
        quota: 100Gi
        high: "0.9"
        low: "0.8"
```


- mountPoint：表示挂载 pvc 的路径，`pvc://`为挂载本地volume前缀，`testPVC`表示需要加速的PVC的名字，需要有访问权限。 
- accessModes: 可选 ReadOnlyMany / ReadWriteMany，前者代表只读，后者代表可读写
- replicas：表示创建 JindoFSx 集群节点的数量。
- mediumtype： JindoFSx 暂只支持HDD/SSD/MEM中的其中一种。
- path：存储路径，暂只支持一块盘，当选择MEM做缓存也需要一块盘来存储log等文件。
- quota：缓存最大容量，100Gi 表示最大可用 100GB 大小的磁盘。
- high：水位上限大小 / low： 水位下限大小。



创建 JindoRuntime


```shell
kubectl create -f resource.yaml
```


查看部署的 JinRuntime 情况，其中 fuse 会 lazy 启动，根据应用挂载情况来相应的节点上启动
```shell
kubectl get jindoruntime pvc
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
pvc       Ready           Ready                     62m
```


查看 dataset 的情况，现实 Bound 状态表示 dataset 绑定成功。


```shell
$ kubectl get dataset pvc
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
pvc        511MiB       0.00B    180.00GiB              0.0%          Bound   1h
```


查看 PV，PVC 创建情况，JindoRuntime部署过程中会自动创建PV和PVC
```shell
kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/pvc   100Gi      ROX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/pvc   Bound    hadoop   100Gi      RWX                           58m
```

## 6、数据预热

### 设置数据加载时的缓存副本数量
可以进行数据预热提前将文件缓存到本地，假设需要加速该 pvc 下面的两个子目录 /data1 和 /data1 同时分别指定缓存副本数量，可以参考如下dataload.yaml：
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: test
spec:
  dataset:
    name: pvc
    namespace: default
  loadMetadata: true
  target:
    - path: /data1/
      replicas: 1
    - path: /data2/
      replicas: 2
```

```
kubectl create dataload.yaml
```
观察 dataload 状态
```
kubectl get dataload test
```

具体 dataload 的功能可参考 [数据预加载](jindo_fluid_dataload.md)

## 7、环境清理

- 删除应用和应用容器
- 删除 JindoRuntime 和 dataset
```shell
kubectl delete dataset pvc
```
