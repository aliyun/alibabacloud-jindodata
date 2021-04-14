## 拥抱云原生，Fluid结合JindoFS：数据预加载

JindoFS 提供数据元数据缓存和数据缓存的能力，本文档介绍如何使用 JindoRunitime 进行数据预热。

## 前提条件

- [Fluid](https://github.com/fluid-cloudnative/fluid)
- 可与 Kubernetes 环境网络连通的 HDFS 集群

## 安装使用流程

### 1、创建命名空间
```shell
$ kubectl create ns fluid-system
```
### 2、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/352cache/fluid-0.6.0.tgz)

### 3、使用 Helm 安装 Fluid

```shell
$ helm install --set runtime.jindo.enabled=true fluid fluid-0.6.0.tgz
```
### 4、创建 secret

在创建 dataset 之前，我们可以创建一个 secret 来保存 OSS 的 fs.oss.accessKeyId 和fs.oss.accessKeySecret 信息，避免明文暴露出来，k8s会对已创建的 secret 使用加密编码，将key和secret信息填入mySecret.yaml文件中。

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
stringData:
  fs.oss.accessKeyId: xxx
  fs.oss.accessKeySecret: xxx
```

生成secret

```yaml
kubectl create -f mySecret.yaml
```

### 5、创建 dataset

申明一个如下的 dataset 资源，表示 UFS 的信息。

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: oss://<oss_bucket>/<bucket_dir>
      options:
        fs.oss.endpoint: <oss_endpoint>  
      name: hadoop
      encryptOptions:
        - name: fs.oss.accessKeyId
          valueFrom:
            secretKeyRef:
              name: mysecret
              key: fs.oss.accessKeyId
        - name: fs.oss.accessKeySecret
          valueFrom:
            secretKeyRef:
              name: mysecret
              key: fs.oss.accessKeySecret      
```

创建 dataset 资源

```shell
kubectl create -f dataset.yaml
```

### 6、创建 JindoRuntime

创建一个 JindoRuntime 对应的 JindoFS 集群，有 Cache 和 NoCache 两种模式。Cache 模式提供对远端 OSS 上数据缓存以及元数据缓存；NoCache 模式即简单作为 HDFS connector 提供访问 OSS 的能力。

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
  fuse:
    properties:
      jfs.cache.data-cache.enable: "true"
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```


* `replicas`：表示创建 JindoFS 集群的 worker 的数量。
* `mediumtype`： JindoFS 暂只支持HDD/SSD/MEM中的其中一种。
* `path`：存储路径，支持一块盘或者多块盘
* `quota`：缓存最大容量，单位G。

多块盘多个quota请参考
```yaml
path: /mnt/disk1/,/mnt/disk2/,/mnt/disk3/
quotaList: 290G,290G,290G
```
其中 path 和 quota 的数量应该相同。
* `high`：水位上限比例 / `low`： 水位下限比例。


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

### 7、数据预热
Cache 模式下 JindoFS 自动对文件打开了缓存优化，从 OSS 读取文件数据后能够自动缓存到本地磁盘。推荐通过以下预加载命令将需要读取的数据目录进行元数据缓存和数据缓存的预加载，以更好地提升后续作业的性能。
* 登陆到 JindoFS 集群 master 所在的 pod 上
```shell
kubectl exec -ti hadoop-jindofs-master-0  -- /bin/bash
```
* 进行元数据预加载
```shell
jindo jfs -metaSync -R jfs://hadoop/dir/
```
等待执行完毕即可完成元数据缓存

* 进行数据预加载
```shell
jindo jfs -cache -s jfs://hadoop/dir/
```
等待执行完毕即可完成数据缓存，可使用 Fuse / PV 加载数据进行机器学习训练
