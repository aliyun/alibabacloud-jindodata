## 1、创建命名空间
```shell
kubectl create ns fluid-system
```
## 2、下载安装包
[点击下载](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/431/fluid-0.7.0.tgz)

## 3、使用 Helm 安装 Fluid

```shell
helm install --set runtime.jindo.enabled=true fluid fluid-0.7.0.tgz
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

- 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源。
- 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFSx 的集群来提供缓存服务。


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: nas
spec:
  mounts:
    - mountPoint: local:///mnt/nas
      name: nasData
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: nas
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


- mountPoint：表示挂载 NAS 的路径，`local://`为本地路径前缀，`/mnt/nas`表示本地真实路径，该路径需要在宿主机上可见。
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
kubectl get jindoruntime nas
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
nas       Ready           Ready                     62m
```

查看 PV，PVC 创建情况，JindoRuntime部署过程中会自动创建PV和PVC
```shell
kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/nas   100Gi      RWX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/nas   Bound    hadoop   100Gi      RWX                           58m
```


此时我们对 oss 上文件进行一次数据预热，将 NAS 上文件加载到本地

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: test
spec:
  dataset:
    name: nas
    namespace: default
  target:
    - path: /nasData
      replicas: 1
```
其中 `/nasData` 中 `nasData` 为 dataset 中挂载点的 name，可以使用 `/nasData/xxx` 来预热其中的子目录。`replicas`表示缓存的备份数，默认是1。

执行
```shell
kubectl create -f dataload.yaml
```
观察 dataload 任务的执行情况
```shell
$ kubectl get dataload test-dataload
NAME             DATASET     PHASE       AGE
dataload         nas       Completed    2m13s
```

## 6、创建应用容器体验加速效果


您可以通过创建应用容器来使用 JindoFSx 加速服务，或者进行提交机器学习作业来进行体验相关功能。
接下来，我们创建一个应用容器 app.yaml 来使用该数据集，我们将多次访问同一数据，并比较访问时间来展示JindoRuntime 的加速效果。


```yaml
apiVersion: v1
kind: Pod
metadata:
  name: demo-app
spec:
  containers:
    - name: demo
      image: nginx
      volumeMounts:
        - mountPath: /data
          name: hadoop
  volumes:
    - name: hadoop
      persistentVolumeClaim:
        claimName: nas
```


使用kubectl完成创建


```shell
kubectl create -f app.yaml
```
访问挂载点对应的底层文件

```shell
$ kubectl exec -it demo-app -- bash
$ du -sh /data/hadoop/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2 
210M	/data/hadoop/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2 
```

## 7、环境清理

- 删除应用和应用容器
- 删除 JindoRuntime

```shell
kubectl delete jindoruntime nas
```

- 删除 dataset

```shell
kubectl delete dataset nas
```
