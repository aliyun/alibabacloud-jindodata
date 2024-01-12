# 加速 OSS-HDFS 上数据

使用 JindoRuntime 流程简单，在准备好基本 K8S 和 OSS-HDFS 环境的条件下，您只需要耗费10分钟左右时间即可部署好需要的 JindoRuntime 环境，您可以按照下面的流程进行部署。

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


### 5.1 准备 OSS-HDFS 服务
您需要开通阿里云对象存储服务(Object Storage Service，简称OSS)，按照使用文档进行bucket创建，并在创建时指定bucket为支持 OSS-HDFS 模式，访问链接 [https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_dls_quickstart.md](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_dls_quickstart.md)


### 5.2 准备测试样例数据
我们可以使用 Apache 镜像站点上的 Spark 相关资源作为演示中使用的远程文件。这个选择并没有任何特殊之处，你可以将这个远程文件修改为任意你喜欢的远程文件。

- 现在远程资源文件到本地
```shell
mkdir tmp
cd tmp
wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.7/spark-2.4.7-bin-hadoop2.7.tgz 
wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz 
```

- 上传本地文件到 OSS-HDFS 上

您可以使用 [JindoSDK](https://github.com/aliyun/alibabacloud-jindodata/blob/master/docs/user/4.x/4.6.x/4.6.12/jindofs/jindo_dls_quickstart.md#3-%E4%B8%8B%E8%BD%BD-jindosdk-%E5%8C%85)，按照使用说明将本地下载的文件上传到远程 OSS-HDFS 的bucket上。如您的bucket名字为 test-bucket 且使用 JindoSDK 作为客户端工具，可执行如下命令上传文件到 OSS-HDFS 上面。
```shell
hadoop fs -put spark-2.4.7-bin-hadoop2.7.tgz oss://test-bucket/spark/spark-2.4.7/spark-2.4.7-bin-hadoop2.7.tgz 
hadoop fs -put spark-3.1.1-bin-hadoop3.2.tgz oss://test-bucket/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz
```

### 5.3 创建 dataset 和 JindoRuntime

创建一个 resource.yaml 文件里面包含两部分：

- 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源，如例子中的 test-bucket。
- 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFSx 的集群来提供缓存服务。


```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
stringData:
  fs.oss.accessKeyId: <OSS_HDFS_ACCESS_KEY_ID>
  fs.oss.accessKeySecret: <OSS_HDFS_ACCESS_KEY_SECRET>
---
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: oss://test-bucket/dir/
      options:
        fs.oss.endpoint: <OSS_HDFS_ENDPOINT>
      name: hadoop
      path: /
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
  accessModes:
    - ReadOnlyMany 
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
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


- mountPoint：表示挂载UFS的路径，路径中不需要包含 endpoint 信息。
- accessModes: 可选 ReadOnlyMany / ReadWriteMany，前者代表只读，后者代表可读写
- fs.oss.accessKeyId/fs.oss.accessKeySecret：oss bucket的AK信息，有权限访问该 bucket。
- fs.oss.endpoint：OSS-HDFS bucket的endpoint信息，访问 OSS Bucket 上 OSS-HDFS 服务需要配置 Endpoint（cn-xxx.oss-dls.aliyuncs.com），与 OSS 对象接口的 Endpoint（oss-cn-xxx.aliyuncs.com）不同，如您的服务在 hangzhou，那么对应的 endpoint 为：cn-hangzhou.oss-dls.aliyuncs.com
- replicas：表示创建 JindoFSx 集群节点的数量。
- mediumtype： JindoFSx 暂只支持HDD/SSD/MEM中的其中一种。
- path：存储路径，暂只支持一块盘，当选择MEM做缓存也需要一块盘来存储log等文件。
- quota：缓存最大容量，100Gi 表示最大可用 100GB 大小的磁盘。
- high：水位上限大小 / low： 水位下限大小。



创建 JindoRuntime


```shell
kubectl create -f resource.yaml
```


查看部署的 JindoRuntime 情况，其中 fuse 会 lazy 启动，根据应用挂载情况来相应的节点上启动
```shell
kubectl get jindoruntime hadoop
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hadoop    Ready           Ready                     62m
```


查看 dataset 的情况，现实 Bound 状态表示 dataset 绑定成功。


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   HCFS URL                                TOTAL FILES     CACHE HIT RATIO   AGE
hadoop   [Calculating]    0.00B    200.00GiB                            Bound   hadoop-jindofs-master-0.default:18844   [Calculating]                     6m28s
```


查看 PV，PVC 创建情况，JindoRuntime部署过程中会自动创建PV和PVC
```shell
kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/hadoop   100Gi      RWX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/hadoop   Bound    hadoop   100Gi      RWX                           58m
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
        claimName: hadoop
```


使用kubectl完成创建


```shell
kubectl create -f app.yaml
```


查看文件大小


```shell
$ kubectl exec -it demo-app -- bash
$ du -sh /data/hadoop/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2 
210M	/data/hadoop/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2 
```


进行文件的cp观察时间消耗了18s


```shell
$ time cp /data/hadoop/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2 /dev/null

real	0m18.386s
user	0m0.002s
sys	  0m0.105s
```

此时我们对 oss 上文件进行一次数据预热，将 oss 上文件加载到本地

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: hadoop-dataload
spec:
  loadMetadata: true
  dataset:
    name: hadoop
    namespace: default
```
执行
```shell
kubectl create -f dataload.yaml
```
观察 dataload 任务的执行情况
```shell
$ kubectl get dataload spark-dataload
NAME             DATASET     PHASE       AGE
hadoop-dataload   hadoop     Completed   2m13s
```

查看此时 dataset 的缓存情况，发现210MB的数据已经都缓存到了本地。


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop   210.00MiB       210.00MiB    180.00GiB        100.0%           Bound   1h
```


为了避免其他因素(比如page cache)对结果造成影响，我们将删除之前的容器，新建相同的应用，尝试访问同样的文件。由于此时文件已经被 JindoFSx 缓存，可以看到第二次访问所需时间远小于第一次。


```shell
kubectl delete -f app.yaml && kubectl create -f app.yaml
```


进行文件的拷贝观察时间，发现消耗48ms，整个拷贝的时间缩短了300倍


```shell
$ time cp /data/hadoop/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2  /dev/null

real	0m0.048s
user	0m0.001s
sys	  0m0.046s
```


## 7、环境清理

- 删除应用和应用容器
- 删除 JindoRuntime

```shell
kubectl delete jindoruntime hadoop
```

- 删除 dataset

```shell
kubectl delete dataset hadoop
```
