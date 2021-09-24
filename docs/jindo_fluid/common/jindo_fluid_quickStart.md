## 快速入门使用 JindoRuntime


使用 JindoRuntime 流程简单，在准备好基本 k8s 和 OSS 环境的条件下，您只需要耗费10分钟左右时间即可部署好需要的 JindoRuntime 环境，您可以按照下面的流程进行部署。


### 1、创建命名空间
```shell
kubectl create ns fluid-system
```
### 2、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/370/fluid-0.6.0.tgz)
### 3、使用 Helm 安装 Fluid


```shell
helm install --set runtime.jindo.enabled=true fluid fluid-0.6.0.tgz
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
### 5、测试数据源准备


#### 1、准备 OSS 服务
您需要开通阿里云对象存储服务(Object Storage Service，简称OSS)，按照使用文档进行bucket创建([https://www.aliyun.com/product/oss](https://www.aliyun.com/product/oss))


#### 2、准备测试样例数据
我们可以使用 Apache 镜像站点上的 Spark 相关资源作为演示中使用的远程文件。这个选择并没有任何特殊之处，你可以将这个远程文件修改为任意你喜欢的远程文件。

- 现在远程资源文件到本地
```shell
mkdir tmp
cd tmp
wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.7/spark-2.4.7-bin-hadoop2.7.tgz 
wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz 
```

- 上传本地文件到 OSS 上

您可以使用 OSS 提供的客户端 [ossutil](https://helpcdn.aliyun.com/document_detail/50452.html) 或者阿里云EMR团队提供的 [JindoFS SDK](https://github.com/aliyun/alibabacloud-jindofs/blob/master/docs/jindofs_sdk_overview.md)，按照使用说明将本地下载的文件上传到远程 OSS 的bucket上。如您的bucket名字为 test-bucket 且使用 JindoFS SDK 作为客户端工具，可执行如下命令上传文件到 OSS 上面。
```shell
hadoop fs -put spark-2.4.7-bin-hadoop2.7.tgz oss://test-bucket/spark/spark-2.4.7/spark-2.4.7-bin-hadoop2.7.tgz 
hadoop fs -put spark-3.1.1-bin-hadoop3.2.tgz oss://test-bucket/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz
```


#### 3、创建 dataset 和 JindoRuntime


创建一个 resource.yaml 文件里面包含两部分：

- 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源，如例子中的 test-bucket。
- 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFS 的集群来提供缓存服务。



```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: oss://test-bucket/
      options:
        fs.oss.accessKeyId: <OSS_ACCESS_KEY_ID>
        fs.oss.accessKeySecret: <OSS_ACCESS_KEY_SECRET>
        fs.oss.endpoint: <OSS_ENDPOINT> 
      name: hadoop
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
        quota: 100G
        high: "0.9"
        low: "0.8"
```


- mountPoint：表示挂载UFS的路径，路径中不需要包含 endpoint 信息。
- fs.oss.accessKeyId/fs.oss.accessKeySecret：oss bucket的AK信息，有权限访问该 bucket。
- fs.oss.endpoint：oss bucket的endpoint信息，公网或内网地址皆可，如您的 bucket 在杭州 Region，那么公网地址为 oss-cn-hangzhou.aliyuncs.com，内网地址为 oss-cn-hangzhou-internal.aliyuncs.com(内网地址使用条件为您的k8s集群所在区域和oss区域相同)。
- replicas：表示创建 JindoFS 集群节点的数量。
- mediumtype： JindoFS 暂只支持HDD/SSD/MEM中的其中一种。
- path：存储路径，暂只支持一块盘，当选择MEM做缓存也需要一块盘来存储log等文件。
- quota：缓存最大容量，单位G。
- high：水位上限大小 / low： 水位下限大小。



创建 JindoRuntime


```shell
kubectl create -f resource.yaml
```


查看部署的 JinRuntime 情况，显示都为 Ready 状态表示部署成功。
```shell
kubectl get jindoruntime hadoop
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hadoop    Ready           Ready           Ready     62m
```


查看 dataset 的情况，现实 Bound 状态表示 dataset 绑定成功。


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop        511MiB       0.00B    180.00GiB              0.0%          Bound   1h
```


查看 PV，PVC 创建情况，JindoRuntime部署过程中会自动创建PV和PVC
```shell
kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/hadoop   100Gi      RWX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/hadoop   Bound    hadoop   100Gi      RWX                           58m
```


#### 6、创建应用容器体验加速效果


您可以通过创建应用容器来使用 JindoFS 加速服务，或者进行提交机器学习作业来进行体验相关功能。
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


查看此时 dataset 的缓存情况，发现210MB的数据已经都缓存到了本地。


```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop   210.00MiB       210.00MiB    180.00GiB        100.0%           Bound   1h
```


为了避免其他因素(比如page cache)对结果造成影响，我们将删除之前的容器，新建相同的应用，尝试访问同样的文件。由于此时文件已经被 JindoFS 缓存，可以看到第二次访问所需时间远小于第一次。


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


#### 7、环境清理


- 删除应用和应用容器
- 删除 JindoRuntime



```shell
kubectl delete jindoruntime hadoop
```


- 删除 dataset



```shell
kubectl delete dataset hadoop
```


以上通过一个简单的例子完成 JindoFS on Fluid 的入门体验和理解，并最后进行环境的清理，更多Fluid JindoRuntime 的功能使用后续文章会进行详细介绍
