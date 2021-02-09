# 拥抱云原生，Fluid结合JindoFS：进阶使用指南

本篇文章将介绍在 Fluid 上使用 JindoRuntime 的一些进阶使用，如节点扩容，参数调配，元数据缓存和FUSE 调优等，进一步丰富 JindoRuntime 的使用场景
### 1、节点扩容
如您需要对通过 Fluid 创建出来的 JindoFS 集群进行节点伸缩，可使用修改 JindoRuntime 参数的方式来进行
查看当前 JindoFS 集群节点的情况，通过如下命令列举worker/fuse的pod个数，即为当前通过Fluid部署的JindoFS的节点个数，可以看到当前是有3个节点。
```shell
$ kubectl get pod | grep jindofs
hadoop-jindofs-fuse-f6778     1/1     Running   0          8m9s
hadoop-jindofs-fuse-hsn7l     1/1     Running   0          8m9s
hadoop-jindofs-fuse-l2jpc     1/1     Running   0          8m9s
hadoop-jindofs-master-0       1/1     Running   0          8m30s
hadoop-jindofs-worker-ctm2x   1/1     Running   0          8m9s
hadoop-jindofs-worker-fcjhq   1/1     Running   0          8m9s
hadoop-jindofs-worker-k2969   1/1     Running   0          8m9s
```
修改JindoRuntime中的 spec.replicas 参数来节点个数，比如修改 spec.replicas = 4
```yaml
$ kubectl edit jindoruntime hadoop
 
# Please edit the object below. Lines beginning with a '#' will be ignored,
# and an empty file will abort the edit. If an error occurs while saving this file will be
# reopened with the relevant failures.
#
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  creationTimestamp: "2021-02-08T09:22:04Z"
  finalizers:
  - jindo-runtime-controller-finalizer
  generation: 2
  managedFields:
  - apiVersion: data.fluid.io/v1alpha1
    fieldsType: FieldsV1
    fieldsV1:
      f:spec:
        .: {}
        f:replicas: {}
        f:tieredstore:
          .: {}
          f:levels: {}
    manager: kubectl
    operation: Update
    time: "2021-02-08T09:22:04Z"
  - apiVersion: data.fluid.io/v1alpha1
    fieldsType: FieldsV1
    ...
    manager: jindoruntime-controller
    operation: Update
    time: "2021-02-08T09:22:53Z"
  name: hadoop
  namespace: default
  ownerReferences:
  - apiVersion: data.fluid.io/v1alpha1
    kind: Dataset
    name: hadoop
    uid: 66569abe-7635-4be4-8829-602ace814ce2
  resourceVersion: "245220094"
  selfLink: /apis/data.fluid.io/v1alpha1/namespaces/default/jindoruntimes/hadoop
  uid: d4181c95-a255-41b4-b15c-d23b8b87841e    
spec:
  fuse:
    resources: {}
  jindoVersion: {}
  master:
    resources: {}
  replicas: 4
  tieredstore:
    levels:
    - high: "0.99"
      low: "0.8"
      mediumtype: SSD
      path: /var/lib/docker/jindo
      quota: 180Gi
  worker:
    resources: {}
status:
  cacheStates:
    cacheCapacity: 648.00GiB
    cached: 153.09GiB
    cachedPercentage: 100.0%
  conditions:
  ...
  currentFuseNumberScheduled: 3
  currentMasterNumberScheduled: 1
  currentWorkerNumberScheduled: 3
  desiredFuseNumberScheduled: 3
  desiredMasterNumberScheduled: 1
  desiredWorkerNumberScheduled: 3
  fuseNumberAvailable: 3
  fuseNumberReady: 3
  fusePhase: Ready
  masterNumberReady: 1
  masterPhase: Ready
  valueFile: hadoop-jindo-values
  workerNumberAvailable: 3
  workerNumberReady: 3
  workerPhase: Ready
```
修改后wq保存，然后观察节点的扩容情况，可以看到当前是扩容为4个节点
```shell
$ kubectl get pod | grep jindofs
hadoop-jindofs-fuse-2f76v     1/1     Running   0          5m9s
hadoop-jindofs-fuse-f6778     1/1     Running   0          8m9s
hadoop-jindofs-fuse-hsn7l     1/1     Running   0          8m9s
hadoop-jindofs-fuse-l2jpc     1/1     Running   0          8m9s
hadoop-jindofs-master-0       1/1     Running   0          8m30s
hadoop-jindofs-worker-ctm2x   1/1     Running   0          8m9s
hadoop-jindofs-worker-fcjhq   1/1     Running   0          8m9s
hadoop-jindofs-worker-k2969   1/1     Running   0          8m9s
hadoop-jindofs-worker-zb7hk   1/1     Running   0          5m9s
```


### 2、调整预读等参数
如您想对数据进行预读和调整 OSS 连接相关参数，可通过增加以下参数来调整
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: HDD
        path: /var/lib/docker/jindo
        quota: 180Gi
        high: "0.99"
        low: "0.8"
  fuse:
    properties:
      client.read.oss.readahead.buffer.size: "8388608"
      client.read.oss.readahead.buffer.count: "8"
      client.oss.download.thread.concurrency: "500"
      client.oss.download.queue.size: "1000"
      client.oss.connection.timeout.millisecond: "12000"
      client.oss.timeout.millisecond: "120000"
```

-  client.read.oss.readahead.buffer.size：预读数据块大小，默认值 1048576（1M）
- client.read.oss.readahead.buffer.count：预读数据块个数，默认值 4，表示预读 4 块
-  client.oss.download.thread.concurrency：读取 OSS 使用的线程池大小，默认为 5
-  client.oss.download.queue.size：读取 OSS 使用的队列大小，默认为 5
-  client.oss.connection.timeout.millisecond：连接 OSS 超时时间，默认 3000 (3秒)，单位微秒
-  client.oss.timeout.millisecond：OSS HTTP连接的返回超时时间，默认 30000（30秒），单位微秒



以上参数可单独或者配合使用

### 3、元数据缓存


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: HDD
        path: /var/lib/docker/jindo
        quota: 180Gi
        high: "0.99"
        low: "0.8"
  master:
    properties:
      namespace.cache.sync.load-type: once
  fuse:
    properties:
      jfs.cache.meta-cache.enable: "1"
```

-  namespace.cache.sync.load-type：once 代表加载一次，适合机器学习等只读场景
-  jfs.cache.meta-cache.enable：1代表打开，0代表关闭元数据缓存
### 

### 4、FUSE调优
如您想对 FUSE 客户端的参数进行调整，可在 fuse.args 里填写相关参数，使用自己调整的 FUSE 参数
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: HDD
        path: /var/lib/docker/jindo
        quota: 180Gi
        high: "0.99"
        low: "0.8"
  fuse:
    args:
      - -okernel_cache 
      - -oattr_timeout=9000 
      - -oentry_timeout=9000
```



