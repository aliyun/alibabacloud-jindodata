为了保证应用在访问数据时的性能，可以通过**数据预加载**提前将远程存储系统中的数据拉取到靠近计算结点的分布式缓存引擎中，使得消费该数据集的应用能够在首次运行时即可享受到缓存带来的加速效果。


为此，我们提供了DataLoad CRD, 该CRD让你可通过简单的配置就能完成整个数据预加载过程，并对数据预加载的许多行为进行自定义控制。


本文档通过以下两个例子演示了DataLoad CRD的使用方法：


- [DataLoad快速使用](#DataLoad%E5%BF%AB%E9%80%9F%E4%BD%BF%E7%94%A8)
- [DataLoad进阶配置](#DataLoad%E8%BF%9B%E9%98%B6%E9%85%8D%E7%BD%AE)

以下 `spec.target.path` 的值均为 `mountpoint` 挂载点下的相对路径。比如当前的挂载点为 `oss://test/`
原始路径下有以下文件
```shell
oss://test/user/sample.txt
oss://test/data/fluid.tgz
```
那么 `target.path` 可定义为
```yaml
target:
    - path: /user
    - path: /data
```


## 前提条件


- [Fluid](https://github.com/fluid-cloudnative/fluid)(version >= 0.6.0)



请参考[Fluid安装文档](jindo_fluid_install.md)完成安装


## 新建工作环境


```yaml
$ mkdir <any-path>/warmup
$ cd <any-path>/warmup
```


## DataLoad快速使用


**配置待创建的Dataset和Runtime对象**


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: spark
spec:
  mounts:
    - mountPoint: oss://test-bucket/
      options:
        fs.oss.accessKeyId: <OSS_ACCESS_KEY_ID>
        fs.oss.accessKeySecret: <OSS_ACCESS_KEY_SECRET>
        fs.oss.endpoint: <OSS_ENDPOINT> 
      name: hbase
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: spark
spec:
  replicas: 2
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1/
        quota: 2G
        high: "0.8"
        low: "0.7"
```




在这里，我们将要创建一个kind为`Dataset`的资源对象(Resource object)。`Dataset`是Fluid所定义的一个Custom Resource Definition(CRD)，该CRD被用来告知Fluid在哪里可以找到你所需要的数据。Fluid将该CRD对象中定义的`mountPoint`属性挂载到JindoFSx之上。在本示例中，为了简单，我们使用 OSS 进行演示。
**创建Dataset和Runtime对象**


```
kubectl create -f dataset.yaml
```


**等待Dataset和Runtime准备就绪**


```
kubectl get datasets spark
```


如果看到类似以下结果，说明Dataset和Runtime均已准备就绪：


```
NAME    UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
spark   1.92GiB          0.00B    4.00GiB          0.0%                Bound   4m4s
```


**配置待创建的DataLoad对象**


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: spark-dataload
spec:
  loadMetadata: true
  dataset:
    name: spark
    namespace: default
```


`spec.dataset`指明了需要进行数据预加载的目标数据集，在该例子中，我们的数据预加载目标为`default`命名空间下名为`spark`的数据集，如果该配置与你所在的实际环境不符，请根据你的实际环境对其进行调整。


**默认情况下，上述DataLoad配置将会尝试加载整个数据集中的全部数据**，如果你希望进行更细粒度的控制(例如：仅加载数据集下指定路径的数据)，请参考[DataLoad进阶配置](#DataLoad%E8%BF%9B%E9%98%B6%E9%85%8D%E7%BD%AE)


**创建DataLoad对象**


```yaml
kubectl create -f dataload.yaml
```


**查看创建的DataLoad对象状态**


```yaml
kubectl get dataload spark-dataload
```


上述命令会得到类似以下结果：


```yaml
NAME             DATASET   PHASE     AGE
spark-dataload   spark     Loading   2m13s
```


你也可以通过`kubectl describe`获取有关该DataLoad的更多详细信息：


```
kubectl describe dataload spark-dataload
```


得到以下结果：


```
Name:         spark-dataload
Namespace:    default
Labels:       <none>
Annotations:  <none>
API Version:  data.fluid.io/v1alpha1
Kind:         DataLoad
...
Spec:
  Dataset:
    Name:       spark
    Namespace:  default
Status:
  Conditions:
  Phase:  Loading
Events:
  Type    Reason              Age   From      Message
  ----    ------              ----  ----      -------
  Normal  DataLoadJobStarted  80s   DataLoad  The DataLoad job spark-dataload-loader-job started
```


上述数据加载过程根据你所在的网络环境不同，可能会耗费数分钟


**等待数据加载过程完成**


```
kubectl get dataload spark-dataload
```


你会看到该DataLoad的`Phase`状态已经从`Loading`变为`Complete`，这表明整个数据加载过程已经完成


```
NAME             DATASET   PHASE      AGE
spark-dataload   spark     Complete   5m17s
```


此时再次查看Dataset对象的缓存状态：


```
kubectl get dataset spark
```


可发现，远程存储系统中的全部数据均已成功缓存到分布式缓存引擎中


```
NAME    UFS TOTAL SIZE   CACHED    CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
spark   1.92GiB          1.92GiB   4.00GiB          100.0%              Bound   7m41s
```
## DataLoad进阶配置


除了上述示例中展示的数据预加载功能外，通过一些简单的配置，你可以对数据预加载进行更加细节的调整，这些调整包括：


- 指定一个或多个数据集子目录进行加载
- 设置数据加载时的缓存副本数量
- 数据加载前首先进行元数据同步



### 指定一个或多个数据集子目录进行加载


进行数据加载时可以加载指定的子目录(或文件)，而不是整个数据集，例如：


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: spark-dataload
spec:
  dataset:
    name: spark
    namespace: default
  loadMetadata: true
  target:
    - path: /spark/spark-2.4.7
    - path: /spark/spark-3.0.1/pyspark-3.0.1.tar.gz
```


上述DataLoad仅会加载`/spark/spark-2.4.7`目录下的全部文件，以及`/spark/spark-3.0.1/pyspark-3.0.1.tar.gz`文件


### 设置数据加载时的缓存副本数量


进行数据加载时,你也可以通过配置控制加载的数据副本数量，例如：


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: spark-dataload
spec:
  dataset:
    name: spark
    namespace: default
  loadMetadata: true
  target:
    - path: /spark/spark-2.4.7
      replicas: 1
    - path: /spark/spark-3.0.1/pyspark-3.0.1.tar.gz
      replicas: 2
```


上述DataLoad在进行数据加载时，对于`/spark/spark-2.4.7`目录下的文件仅会在分布式缓存引擎中保留**1份**数据缓存副本，而对于文件`/spark/spark-3.0.1/pyspark-3.0.1.tar.gz`，分布式缓存引擎将会保留**2份**缓存副本。


### 数据加载前首先进行元数据同步(建议默认开启)


在许多场景下，底层存储系统中的文件可能发生了变化，对于分布式缓存引擎来说，需要重新进行文件元信息的同步才能感知到底层存储系统中的变化。因此在进行数据加载前，你也可以通过设置DataLoad对象的`spec.loadMetadata`来预先完成元信息的同步操作，例如：


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: spark-dataload
spec:
  dataset:
    name: spark
    namespace: default
  loadMetadata: true
  target:
    - path: /
      replicas: 1
```

### 预热HDFS上数据

若您的数据在远程 HDFS 上，首先按照[加速HDFS上数据](jindo_fluid_hdfs_ufs_example.md)文档进行 JindoRuntime 启动。这里需要将您在 runtime 中配置的 `spec.hadoopConfig` 的 configmap 名称，配置到dataload 中的 `spec.hdfsConfig` 中，如您的 runtime 中配置了 `spec.hadoopConfig: hdfsconfig`，那么相应的 dataload 任务您需要添加如下的参数

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: Hadoop-dataload
spec:
  dataset:
    name: hadoop
    namespace: default
  loadMetadata: true
  hdfsConfig: hdfsconfig
  target:
    - path: /user
    - path: /data
```


### 缓存数据到内存


在内存模式下可以缓存数据到内存里，需要开启 `loadMemoryData: true` 选项


```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: spark-dataload
spec:
  dataset:
    name: spark
    namespace: default
  loadMetadata: true
  loadMemoryData: true
  target:
    - path: /
      replicas: 1
```

### 缓存进度及LOG查看

```shell
$ kubectl get dataload spark-dataload
NAME             DATASET   PHASE     AGE
spark-dataload   spark     Loading   2m13s
```

查看当前缓存的相关信息

```shell
kubectl logs spark-dataload
```


## 环境清理


```shell
$ kubectl delete -f .
```
