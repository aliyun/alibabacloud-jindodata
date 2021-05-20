## 拥抱云原生，Fluid结合JindoFS：加速对象存储使用 S3 协议

JindoFS 提供了访问 S3 协议的能力，可以通过配置 S3 作为 JindoFS 的后端存储，使 Fluid 通过 JindoFS 来访问 S3 上的数据，同时 JindoFS 也提供了对 S3 上的数据以及元数据的缓存加速功能。

本文档展示了如何在 Fluid 中以声明式的方式完成 JindoFS 部署，对接 S3 数据源（包括提供 S3 兼容的所有对象存储）。

## 前提条件

- [Fluid](https://github.com/fluid-cloudnative/fluid)

## 安装使用流程

### 1、创建命名空间
```shell
$ kubectl create ns fluid-system
```
### 2、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/zuoyebang/fluid-0.6.0.tgz)

### 3、使用 Helm 安装 Fluid

如果是在原有集群上进行升级，请先更新`dataload`的`crd`

```shell
$ k get crd                              
NAME                                             CREATED AT
databackups.data.fluid.io                        2021-04-14T06:36:38Z
dataloads.data.fluid.io                          2021-05-20T03:59:14Z
datasets.data.fluid.io                           2021-04-14T06:36:38Z
jindoruntimes.data.fluid.io                      2021-04-14T06:36:38Z
```

找到 `dataloads.data.fluid.io ` 

```shell
$ k delete crd                            
NAME                                             CREATED AT
databackups.data.fluid.io                        2021-04-14T06:36:38Z
dataloads.data.fluid.io                          2021-05-20T03:59:14Z
datasets.data.fluid.io                           2021-04-14T06:36:38Z
jindoruntimes.data.fluid.io                      2021-04-14T06:36:38Z

$ k delete crd dataloads.data.fluid.io
```

删除完之后，需要重新apply新的`dataload`，解压 `fluid-0.6.0.tgz`

```shell
$ tar -zxvf fluid-0.6.0.tgz
```

```shell
$ kubectl apply -f fluid/crds/data.fluid.io_dataloads.yaml
```
升级相关 `contrller`
```shell
$ helm install --set runtime.jindo.enabled=true fluid fluid-0.6.0.tgz
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

其中 csi-nodeplugin-fluid-xx 的数量应该与k8s集群中节点node的数量相同。

### 5、创建 dataset 和 JindoRuntime
在创建 dataset 之前，我们可以创建一个 secret 来保存 s3 的` key` 和` secret` 信息，避免明文暴露出来，k8s会对已创建的 secret 使用加密编码，将key和secret信息填入mySecret.yaml文件中。
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
stringData:
  fs.s3.accessKeyId: xxx
  fs.s3.accessKeySecret: xxx
```
生成secret
```yaml
kubectl create -f mySecret.yaml
```
创建一个 resource.yaml 文件里面包含两部分：
* 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源。
* 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFS 的集群来提供缓存服务。
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: s3://xxx.cos.ap-shanghai.myqcloud.com/
      name: hadoop
      encryptOptions:
        - name: fs.s3.accessKeyId
          valueFrom:
            secretKeyRef:
              name: s3secret
              key: fs.s3.accessKeyId
        - name: fs.s3.accessKeySecret
          valueFrom:
            secretKeyRef:
              name: s3secret
              key: fs.s3.accessKeySecret
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: MEM
        path: /var/lib/docker/jindo
        quota: 20G
        high: "0.9"
        low: "0.8"
  master:
    replicas: 1
    properties:
      namespace.cache.sync.load-type: "never"
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
```

* mountPoint：表示挂载 s3 的路径，支持标准 s3 协议。
* spec.replicas：表示创建 JindoFS 集群的 worker 的数量。
* spec.master.replicas: 表示创建master节点的个数，高可用模式请使用`spec.master.replicas=3`，表示创建3个master，通过raft协议来进行选举节点
* mediumtype： JindoFS 暂只支持HDD/SSD/MEM中的其中一种。
* path：存储路径，当选择MEM做缓存也需要一块盘来存储log等文件。
* quota：缓存最大容量，单位G。
* high：水位上限大小 / low： 水位下限大小。

创建 JindoRuntime
```shell
kubectl create -f resource.yaml
```
查看 dataset 的情况
```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop        210MiB       0.00B    180.00GiB              0.0%          Bound   1h
```

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

### 数据预热

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: fluid-dataload
spec:
  dataset:
    name: hadoop
    namespace: default
  loadMetadata: true
  loadMemoryData: true
  atomicCache: true
  target:
    - path: /path1
      replicas: 3
    - path: /path2
      replicas: 2
```

* loadMetadata: 进行元数据缓存，true代表打开
* loadMemoryData: 表示进行数据内存缓存，true代表打开
* atomicCache: 原子性cache，true代表打开
* path: 代表挂载点下的相对路径
* replicas: 缓存的副本个数，默认为1

执行数据预热
```shell
$ kubectl create -f dataload.yaml
```

查看数据预热的进度
```shell
$ kubectl get dataload                   
NAME             DATASET   PHASE      AGE   DURATION
fluid-dataload   hadoop    Complete   19m   12s
```
`Complete` 代表完成全部缓存流程

也可找到数据预热的pod查看详细信息，如
```shell
$ kubectl logs -f fluid-dataload-loader-job-zxxxx
```