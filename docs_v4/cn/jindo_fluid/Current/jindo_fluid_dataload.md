为了保证应用在访问数据时的性能，可以通过**数据预加载**提前将远程存储系统中的数据拉取到靠近计算结点的分布式缓存引擎中，使得消费该数据集的应用能够在首次运行时即可享受到缓存带来的加速效果。


为此，我们提供了DataLoad CRD, 该CRD让你可通过简单的配置就能完成整个数据预加载过程，并对数据预加载的许多行为进行自定义控制。


本文档通过以下两个例子演示了DataLoad CRD的使用方法：
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

### 指定文件列表进行缓存
#### 指定 OSS 上文件作为文件列表
您可以使用 OSS 来存储缓存文件列表，您需要使用如下 yaml 文件和指定相应的参数来使用该功能

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
    accessKeyId: xxx
    accessKeySecret: xxx
    endpoint: oss-cn-<region>.aliyuncs.com
    url: oss://<bucket>/<dir>/filepath.txt
```
* accessKeyId/accessKeySecret：访问文件存储 OSS 的 AK 信息
* endpoint：OSS 所在的 region 信息，需要注意内网和公网地址的区别，确认可访问
* url：该文件在 OSS 上的路径，比如 `oss://xyz/dir/filepath.txt`

### 原子性缓存
如您想保持缓存一致性和原子性，那么您可以使用如下 yaml 来完成这个功能，需要注意的是使用原子性缓存功能，您需要在runtime里开启元数据缓存开关，具体为
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
        path: /mnt/disk1
        quota: 10G
        high: "0.9"
        low: "0.8"
  fuse:
    properties:
      jfs.cache.meta-cache.enable: "true"
```
* jfs.cache.meta-cache.enable: "true" 打开元数据缓存开关

然后您可以使用如下 yaml 来完成原子性缓存的功能

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
$ kubectl delete -f .
```
