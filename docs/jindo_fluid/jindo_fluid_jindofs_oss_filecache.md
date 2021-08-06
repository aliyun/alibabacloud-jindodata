## JindoRuntime 支持文件列表缓存

### 1、更新 Fluid 和 CRD 版本
1、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/filecache/fluid-0.6.0.tgz)

2、更新 Fluid 组建
```shell
helm upgrade --install fluid fluid-0.6.0.tgz
```

3、更新 dataload crd
```shell
kubectl delete crd dataloads.data.fluid.io
```
解压 `fluid-0.6.0.tgz`

```shell
k apply -f fluid/crds/data.fluid.io_dataloads.yaml
```

### 2、创建 dataset 和 JindoRuntime

假设我们的`testbucket`下有如下文件
```shell
hadoop fs -ls oss://test-bucket/
-rw-rw-rw-   1   21990300 2021-06-29 03:33 oss://test-bucket/part-m-00000
-rw-rw-rw-   1   21990200 2021-06-29 03:33 oss://test-bucket/part-m-00001
-rw-rw-rw-   1   21990200 2021-06-29 03:33 oss://test-bucket/part-m-00002
-rw-rw-rw-   1   21990300 2021-06-29 03:33 oss://test-bucket/part-m-00003
-rw-rw-rw-   1   21990200 2021-06-29 03:33 oss://test-bucket/part-m-00004
-rw-rw-rw-   1   21990200 2021-06-29 03:33 oss://test-bucket/part-m-00005
-rw-rw-rw-   1   21990300 2021-06-29 03:33 oss://test-bucket/part-m-00006
-rw-rw-rw-   1   21990200 2021-06-29 03:33 oss://test-bucket/part-m-00007
```
创建dataset和JindoRuntime

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
### 3、自定义文件列表来做缓存

#### 3.1 将需要缓存的文件路径（挂载点后的相对路径），按行写在`filepath.txt`文件中（文件名称固定，后续挂载到容器里）
如 filepath.txt 的内容为：
```shell
/part-m-00000
/part-m-00001
/part-m-00002
```
表示我们需要将这三个文件进行缓存

#### 3.2 将 `filepath.txt` 转换成 configmap，confimap的名字可以自定义，比如 cachelist
```shell
kubectl create configmap cachelist --from-file=filepath.txt
```

#### 通过 dataload 的 yaml 指定 configmap
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: filelist
spec:
  dataset:
    name: hadoop
    namespace: default
  loadMetadata: true
  cacheList: cachelist
  cacheListReplica: 1
```
* cacheList: 文件列表文件 `filepath.txt` 所对应的configmap的名字
* cacheListReplica: 文件列表缓存的副本数量，默认为1

执行

```shell
kubectl create -f dataload.yaml
```

### 3、指定文件列表位置
1、将文件列表的文件放到 OSS 上，名称为`filepath.txt`不变，可以使用 OSS 客户端工具等来完成

2、通过 dataload 的 yaml 指定文件存放在 OSS 上的访问信息
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: DataLoad
metadata:
  name: dataload
spec:
  dataset:
    name: hadoop
    namespace: default
  loadMetadata: true
  cacheListFile:
    accessKeyId: xxx
    accessKeySecret: xxx
    endpoint: oss-cn-<region>.aliyuncs.com
    url: oss://<bucket>/<dir>/filepath.txt
```
执行
```shell
kubectl create -f dataload.yaml
```