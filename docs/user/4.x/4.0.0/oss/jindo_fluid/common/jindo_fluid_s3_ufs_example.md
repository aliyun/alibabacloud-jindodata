## 拥抱云原生，Fluid结合JindoFS：加速对象存储使用 S3 协议


JindoFS 提供了访问 S3 协议的能力，可以通过配置 S3 作为 JindoFS 的后端存储，使 Fluid 通过 JindoFS 来访问 S3 上的数据，同时 JindoFS 也提供了对 S3 上的数据以及元数据的缓存加速功能。


本文档展示了如何在 Fluid 中以声明式的方式完成 JindoFS 部署，对接 S3 数据源（包括提供 S3 兼容的所有对象存储）。


## 前提条件


- [Fluid](https://github.com/fluid-cloudnative/fluid)

- 请参考[安装文档](jindo_fluid_install.md)完成安装


### 创建 dataset 和 JindoRuntime


在创建 dataset 之前，我们可以创建一个 secret 来保存 s3 的`key` 和`secret` 信息，避免明文暴露出来，k8s会对已创建的 secret 使用加密编码，将key和secret信息填入mySecret.yaml文件中。


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


- 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源。
- 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFS 的集群来提供缓存服务。



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
  replicas: 2
  tieredstore:
    levels:
      - mediumtype: MEM
        path: /mnt/disk1
        quota: 100G
        high: "0.9"
        low: "0.8"
  worker:
    properties:
      storage.cache.filelet.worker.threads: "50"
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
```


- mountPoint：表示挂载 s3 的路径，支持标准 s3 协议。
- replicas：表示创建 JindoFS 集群的 worker 的数量。
- mediumtype： JindoFS 暂只支持HDD/SSD/MEM中的其中一种。
- path：存储路径，当选择MEM做缓存也需要一块盘来存储log等文件。
- quota：缓存最大容量，单位G。
- high：水位上限大小 / low： 水位下限大小。



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

### 检查服务是否正常！！！
此步骤建议用户创建成功后一定要进行验证

#### 检查底层存储端点是否成功连接
1、登陆到 master/worker pod上
```shell
kubectl get pod
NAME                              READY   STATUS      RESTARTS   AGE
hadoop-jindofs-fuse-svz4s         1/1     Running     0          23h
hadoop-jindofs-master-0           1/1     Running     0          23h
hadoop-jindofs-worker-2fpbk       1/1     Running     0          23h
```
```shell
kubectl exec -ti hadoop-jindofs-master-0 bash
hadoop fs -ls jfs://jindo/
```
观察是否可以正常list文件

2、登陆到 fuse pod上
```shell
kubectl exec -ti hadoop-jindofs-fuse-svz4s bash
ls /jfs/jindo/
```
观察是否可以正常list文件








