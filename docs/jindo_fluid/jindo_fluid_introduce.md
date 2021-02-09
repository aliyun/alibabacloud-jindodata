# 拥抱云原生，Fluid结合JindoFS ：阿里云OSS加速利器

### 1、什么是 Fluid
[Fluid](https://github.com/fluid-cloudnative/fluid) 是一个开源的 Kubernetes 原生的分布式数据集编排和加速引擎，主要服务于云原生场景下的数据密集型应用，例如大数据应用、AI应用等。通过 Kubernetes 服务提供的数据层抽象，可以让数据像流体一样在诸如 HDFS、OSS、Ceph 等存储源和 Kubernetes 上层云原生应用计算之间灵活高效地移动、复制、驱逐、转换和管理。而具体数据操作对用户透明，用户不必再担心访问远端数据的效率、管理数据源的便捷性，以及如何帮助 Kuberntes 做出运维调度决策等问题。用户只需以最自然的 Kubernetes 原生数据卷方式直接访问抽象出来的数据，剩余任务和底层细节全部交给 Fluid 处理。Fluid 项目当前主要关注数据集编排和应用编排这两个重要场景。数据集编排可以将指定数据集的数据缓存到指定特性的 Kubernetes 节点，而应用编排将指定该应用调度到可以或已经存储了指定数据集的节点上。这两者还可以组合形成协同编排场景，即协同考虑数据集和应用需求进行节点资源调度。
<div align=center>
<img src="../../pic/fluid.png#pic_center" />
</div>
然后介绍 Fluid 中 Dataset 的概念，数据集是逻辑上相关的一组数据的集合，会被运算引擎使用，比如大数据的 Spark，AI场景的 TensorFlow，而关于数据集智能的应用和调度会创造工业界的核心价值。Dataset 的管理实际上也有多个维度，比如安全性，版本管理和数据加速。


我们希望从数据加速出发，对于数据集的管理提供支持。在 Dataset上面我们通过定义 Runtime 这样一个执行引擎来实现数据集安全性，版本管理和数据加速等能力，Runtime 定义了一系列生命周期的接口，可以通过实现这些接口来支持数据集的管理和加速，目前 Fluid 中支持的 Runtime 有 AlluxioRuntime 和 JindoRuntime 两种。Fluid 的目标是为 AI 与大数据云原生应用提供一层高效便捷的数据抽象，将数据从存储抽象出来从而达到如下功能：
1、通过数据亲和性调度和分布式缓存引擎加速，实现数据和计算之间的融合，从而加速计算对数据的访问。
2、将数据独立于存储进行管理，并且通过 Kubernetes 的命名空间进行资源隔离，实现数据的安全隔离。
3、将来自不同存储的数据联合起来进行运算，从而有机会打破不同存储的差异性带来的数据孤岛效应。


### 2、什么是 JindoRuntime


如果要了解Fluid的JindoRuntime，先要介绍JindoFS。它是JindoRuntime的引擎层。


JindoFS 是阿里云针对 OSS 开发的自研大数据存储优化引擎，完全兼容 Hadoop 文件系统接口，给客户带来更加灵活、高效的计算存储方案，目前已验证支持阿里云 EMR 中所有的计算服务和引擎：Spark、Flink、Hive、MapReduce、Presto、Impala 等。JindoFS 有两种使用模式，块存储（Block）模式和缓存（Cache）模式。Block 模式将文件内容以数据块的形式存放在 OSS 上并在本地可选择使用数据备份来进行缓存加速，使用本地的namespace 服务管理元数据，从而通过本地元数据以及块数据构建出文件数据。Cache 模式将文件存储在 OSS上，该模式兼容现有的 OSS 文件系统，用户可以通过 OSS 访问原有的目录结构以及文件，同时该模式提供数据以及元数据的缓存，加速用户读写数据的性能。使用该模式的用户无需迁移数据到 OSS，可以无缝对接现有 OSS 上的数据，在元数据同步方面用户可以根据不同的需求选择不同的元数据同步策略。


在 Fluid 中 JindoRuntime 也是使用 JindoFS 的 Cache 模式进行远端文件的访问和缓存，如您需要在其他环境单独使用 JindoFS 获得访问 OSS 的能力，您也可以下载我们的 [JindoFS SDK](https://github.com/aliyun/alibabacloud-jindo-sdk) 按照使用文档进行部署使用。 JindoRuntime 来源于阿里云EMR团队自研 JindoFS 分布式系统，是支撑 Dataset 数据管理和缓存的执行引擎实现。Fluid 通过管理和调度 Jindo Runtime 实现数据集的可见性、弹性伸缩、数据迁移、计算加速等。在 Fluid 上使用和部署 JindoRuntime 流程简单、兼容原生 k8s 环境、可以开箱即用。深度结合对象存储特性，使用 navite 框架优化性能，并支持免密、checksum 校验等云上数据安全功能。


### 3、JindoRuntime 的优势
JindoRuntime 提供对 Aliyun OSS 对象存储服务的访问和缓存加速能力，并且利用 FUSE 的 POSIX 文件系统接口实现可以像本地磁盘一样轻松使用 OSS 上的海量文件，具有以下特点：

#### 1、性能卓越
- **OSS的读写性能突出:** 深度结合 OSS 进行读写效率和稳定性的增强，通过 native 层优化对 OSS 访问接口，优化冷数据访问性能，特别是小文件读写
- **分布式缓存策略丰富:** 支持单TB级大文件缓存、元数据缓存策略等。在大规模AI训练和数据湖场景实测中有突出的性能优势。

#### 2、安全可靠
- **认证安全:** 支持阿里云上 STS 免密访问和K8s原生的秘钥加密
- **数据安全:** checksum 校验、客户端数据加密等安全策略，保护云上数据安全和用户信息等。

#### 3、简单易用
- 支持原生 k8s 环境，利用自定义资源定义，对接数据卷概念。使用部署流程简单，可以开箱即用。

#### 4、轻量级
- 底层基于 c++ 代码，整体结构轻量化，各种 OSS 访问接口额外开销较小。

### 4、JindoRuntime 性能怎么样
我们使用 [ImageNet](http://www.image-net.org/) 数据集基于 Kubernetes 集群并使用 [Arena](https://github.com/kubeflow/arena) 在此数据集上训练 ResNet-50 模型，基于 JindoFS 的 JindoRuntime 在开启本地缓存的情况下性能大幅度优于开源 OSSFS，训练耗时缩短了76%，该测试场景会在后续文章中进行详细介绍。
<div align=center>
<img src="../../pic/fluid_jindofs_resnet50_result.png#pic_center" />
</div>
### 5、如何快速上手使用 JindoRuntime
使用 JindoRuntime 流程简单，在准备好基本 k8s 和 OSS 环境的条件下，您只需要耗费10分钟左右时间即可部署好需要的 JindoRuntime 环境，您可以按照下面的流程进行部署。
#### 1、创建命名空间
```shell
kubectl create ns fluid-system
```
#### 2、下载 [fluid-0.5.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/332cache/fluid-0.5.0.tgz)
#### 3、使用 Helm 安装 Fluid
```shell
helm install --set runtime.jindo.enabled=true fluid fluid-0.5.0.tgz
```
#### 4、查看 Fluid 的运行状态
```shell
$ kubectl get pod -n fluid-system
NAME                                         READY   STATUS    RESTARTS   AGE
csi-nodeplugin-fluid-2mfcr                   2/2     Running   0          108s
csi-nodeplugin-fluid-l7lv6                   2/2     Running   0          108s
dataset-controller-5465c4bbf9-5ds5p          1/1     Running   0          108s
jindoruntime-controller-654fb74447-cldsv     1/1     Running   0          108s
```
其中 csi-nodeplugin-fluid-xx 的数量应该与k8s集群中节点node的数量相同。
#### 5、创建 dataset 和 JindoRuntime
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
创建一个 resource.yaml 文件里面包含两部分：
1、首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源。
2、接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFS 的集群来提供缓存服务。
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
        high: "0.99"
        low: "0.8"
```
1、mountPoint：oss://<oss_bucket>/<bucket_dir> 表示挂载UFS的路径，路径中不需要包含endpoint信息。
2、fs.oss.endpoint：oss bucket的endpoint信息，公网或内网地址皆可。
3、replicas：表示创建 JindoFS 集群的 worker 的数量。
4、mediumtype： JindoFS 暂只支持HDD/SSD/MEM中的其中一种。
5、path：存储路径，暂只支持一块盘，当选择MEM做缓存也需要一块盘来存储log等文件。
6、quota：缓存最大容量，单位Gi。
7、high：水位上限大小 / low： 水位下限大小。
```shell
kubectl create -f resource.yaml
```
查看 dataset 的情况
```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop        210MiB       0.00B    180.00GiB              0.0%          Bound   1h
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
$ du -sh /data/hadoop/spark-3.0.1-bin-hadoop2.7.tgz 
210M	/data/hadoop/spark-3.0.1-bin-hadoop2.7.tgz
```
进行文件的cp观察时间消耗了18s
```shell
$ time cp /data/hadoop/spark-3.0.1-bin-hadoop2.7.tgz /dev/null

real	0m18.386s
user	0m0.002s
sys	0m0.105s
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
$ time cp /data/hadoop/spark-3.0.1-bin-hadoop2.7.tgz /dev/null

real	0m0.048s
user	0m0.001s
sys	0m0.046s
```
#### 7、环境清理
1、删除应用和应用容器
2、删除 JindoRuntime
```shell
kubectl delete jindoruntime hadoop
```
3、删除 dataset
```shell
kubectl delete dataset hadoop
```
以上通过一个简单的例子完成 JindoFS on Fluid 的入门体验和理解，并最后进行环境的清理，更多Fluid JindoRuntime 的功能使用后续文章会进行详细介绍
