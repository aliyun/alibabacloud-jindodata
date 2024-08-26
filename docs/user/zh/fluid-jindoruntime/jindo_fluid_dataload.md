# 数据预加载

- [配置待创建的DataLoad对象](#%E9%85%8D%E7%BD%AE%E5%BE%85%E5%88%9B%E5%BB%BA%E7%9A%84dataload%E5%AF%B9%E8%B1%A1)
  - [创建DataLoad对象](#%E5%88%9B%E5%BB%BAdataload%E5%AF%B9%E8%B1%A1)
  - [查看创建的DataLoad对象状态](#%E6%9F%A5%E7%9C%8B%E5%88%9B%E5%BB%BA%E7%9A%84dataload%E5%AF%B9%E8%B1%A1%E7%8A%B6%E6%80%81)
  - [等待数据加载过程完成](#%E7%AD%89%E5%BE%85%E6%95%B0%E6%8D%AE%E5%8A%A0%E8%BD%BD%E8%BF%87%E7%A8%8B%E5%AE%8C%E6%88%90)
- [DataLoad进阶配置](#dataload%E8%BF%9B%E9%98%B6%E9%85%8D%E7%BD%AE)
  - [指定一个或多个数据集子目录进行加载](#%E6%8C%87%E5%AE%9A%E4%B8%80%E4%B8%AA%E6%88%96%E5%A4%9A%E4%B8%AA%E6%95%B0%E6%8D%AE%E9%9B%86%E5%AD%90%E7%9B%AE%E5%BD%95%E8%BF%9B%E8%A1%8C%E5%8A%A0%E8%BD%BD)
  - [设置数据加载时的缓存副本数量](#%E8%AE%BE%E7%BD%AE%E6%95%B0%E6%8D%AE%E5%8A%A0%E8%BD%BD%E6%97%B6%E7%9A%84%E7%BC%93%E5%AD%98%E5%89%AF%E6%9C%AC%E6%95%B0%E9%87%8F)
  - [原子性缓存](#%E5%8E%9F%E5%AD%90%E6%80%A7%E7%BC%93%E5%AD%98)
  - [单独进行元数据缓存，不做数据缓存](#%E5%8D%95%E7%8B%AC%E8%BF%9B%E8%A1%8C%E5%85%83%E6%95%B0%E6%8D%AE%E7%BC%93%E5%AD%98%E4%B8%8D%E5%81%9A%E6%95%B0%E6%8D%AE%E7%BC%93%E5%AD%98)
- [缓存进度及LOG查看](#%E7%BC%93%E5%AD%98%E8%BF%9B%E5%BA%A6%E5%8F%8Alog%E6%9F%A5%E7%9C%8B)
  - [环境清理](#%E7%8E%AF%E5%A2%83%E6%B8%85%E7%90%86)

为了保证应用在访问数据时的性能，可以通过**数据预加载**提前将远程存储系统中的数据拉取到靠近计算结点的分布式缓存引擎中，使得消费该数据集的应用能够在首次运行时即可享受到缓存带来的加速效果。


为此，我们提供了DataLoad CRD, 该CRD让你可通过简单的配置就能完成整个数据预加载过程，并对数据预加载的许多行为进行自定义控制。


假设我们已经有如下一个name为`spark`的dataset：

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: spark
spec:
  mounts:
    - mountPoint: oss://test-bucket/spark
      name: sparkV1
  accessModes:
    - ReadWriteMany
```
接下来对这个dataset进行预热，请注意下文中的 `/sparkV1` 为 `mounts[].name` 的值，如果您的 `mounts[].path` 为 / , 那么下文中的 `/sparkV1` 可以直接改为 `/`

## 配置待创建的DataLoad对象


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
  target:
    - path: /sparkV1/
```


`spec.dataset`指明了需要进行数据预加载的目标数据集，在该例子中，我们的数据预加载目标为`default`命名空间下名为`spark`的数据集，如果该配置与你所在的实际环境不符，请根据你的实际环境对其进行调整。

### 创建DataLoad对象

```yaml
kubectl create -f dataload.yaml
```

### 查看创建的DataLoad对象状态

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


### 等待数据加载过程完成


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
    - path: /sparkV1/spark-2.4.7
    - path: /sparkV1/spark-3.0.1/pyspark-3.0.1.tar.gz
```

上述DataLoad仅会加载`/sparkV1/spark-2.4.7`目录下的全部文件，以及`/sparkV1/spark-3.0.1/pyspark-3.0.1.tar.gz`文件


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
    - path: /sparkV1/spark-2.4.7
      replicas: 1
    - path: /sparkV1/spark-3.0.1/pyspark-3.0.1.tar.gz
      replicas: 2
```


上述DataLoad在进行数据加载时，对于`/sparkV1/spark-2.4.7`目录下的文件仅会在分布式缓存引擎中保留**1份**数据缓存副本，而对于文件`/sparkV1/spark-3.0.1/pyspark-3.0.1.tar.gz`，分布式缓存引擎将会保留**2份**缓存副本。

### 原子性缓存
如您想保持缓存一致性和原子性，那么您可以使用如下 yaml 来完成这个功能

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
  options:
    atomicCache: "true"
```

### 单独进行元数据缓存，不做数据缓存
在进行 dataload 预热时，默认是会做数据缓存，原数据缓存是可选，但如果您只想元数据缓存不想做数据缓存的话，您可以使用如下 yaml 打开只做元数据缓存的开关

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
  options:
    loadMetadataOnly: "true"
```

### 通过通配符进行缓存过滤

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
    - path: /sparkV1/spark-2.4.7
      replicas: 1
  options:
    filter: "*.tgz"
    cacheThread: "2"
    dryrun: "false"
```

### 使用列表文件进行缓存

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
  options:
    accessKeyId: "xxx"
    accessKeySecret: "xxx"
    endpoint: "oss-cn-shanghai-internal.aliyuncs.com"
    url: "oss://test-bucket/filelist"
    dryrun: "false"
```


## 缓存进度及LOG查看

```shell
$ kubectl get dataload spark-dataload
NAME             DATASET   PHASE     AGE
spark-dataload   spark     Loading   2m13s
```

查看当前缓存的相关信息

```shell
kubectl logs spark-dataload
```


### 环境清理


```shell
$ kubectl delete dataload spark-dataload
```
