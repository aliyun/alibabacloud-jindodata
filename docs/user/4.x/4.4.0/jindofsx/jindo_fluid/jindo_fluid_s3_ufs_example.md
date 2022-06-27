# 拥抱云原生，Fluid 结合 JindoFSx 存储加速系统：加速对象存储使用 S3 协议

JindoFSx 提供了访问 S3 协议的能力，可以通过配置 S3 作为 JindoFSx 的后端存储，使 Fluid 通过 JindoFSx 来访问 S3 上的数据，同时 JindoFSx 也提供了对 S3 上的数据以及元数据的缓存加速功能。

本文档展示了如何在 Fluid 中以声明式的方式完成 JindoFSx 部署，对接 S3 数据源（包括提供 S3 兼容的所有对象存储）。
## 创建命名空间
```shell
kubectl create ns fluid-system
```
## [下载 Fluid 安装包](jindo_fluid_download.md)

## 使用 Helm 安装 Fluid


```shell
helm install --set runtime.jindo.enabled=true fluid fluid-<version>.tgz
```
## 查看 Fluid 的运行状态


```shell
$ kubectl get pod -n fluid-system
NAME                                         READY   STATUS    RESTARTS   AGE
csi-nodeplugin-fluid-2mfcr                   2/2     Running   0          108s
csi-nodeplugin-fluid-l7lv6                   2/2     Running   0          108s
dataset-controller-5465c4bbf9-5ds5p          1/1     Running   0          108s
jindoruntime-controller-654fb74447-cldsv     1/1     Running   0          108s
```


其中 csi-nodeplugin-fluid-xx 的数量应该与 K8S 集群中节点node的数量相同。
## 创建 dataset 和 JindoRuntime

创建一个 resource.yaml 文件里面包含两部分：

- 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源。
- 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFSx 的集群来提供缓存服务。


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: s3
spec:
  mounts:
    - mountPoint: s3://<bucket>/<dir>
      options:
        fs.s3.accessKeyId: xxx
        fs.s3.accessKeySecret: xxx
        fs.s3.endpoint: s3.ap-east-1.amazonaws.com
        fs.s3.region: ap-east-1
      name: s3
      path: /
  accessModes:
    - ReadOnlyMany
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: s3
spec:
  replicas: 2
  tieredstore:
    levels:
      - mediumtype: MEM
        path: /dev/shm
        quota: 100Gi
        high: "0.9"
        low: "0.8"
```


- mountPoint：表示挂载 s3 的路径，支持标准 s3 协议。
- accessModes: 可选 ReadOnlyMany / ReadWriteMany，前者代表只读，后者代表可读写
- replicas：表示创建 JindoFSx 集群的 worker 的数量。
- mediumtype： JindoFSx 暂只支持HDD/SSD/MEM中的其中一种。
- path：存储路径，当选择MEM做缓存也需要一块盘来存储log等文件。
- quota：缓存最大容量，单位Gi。
- high：水位上限大小 / low： 水位下限大小。



创建 JindoRuntime


```shell
kubectl create -f resource.yaml
```


查看 dataset 的情况


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
s3        210MiB       0.00B    180.00GiB              0.0%          Bound   1h
```


执行 create 命令


```shell
$ kubectl create -f runtime.yaml
```


## 查看 dataset 状态


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
s3     180.01GiB      0.00B      261.00GiB            0.0%          Bound   62m
```


## 检查 PV，PVC 创建情况


```shell
$ kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/s3   100Gi      RWX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/s3   Bound    hadoop   100Gi      RWX                           58m
```




