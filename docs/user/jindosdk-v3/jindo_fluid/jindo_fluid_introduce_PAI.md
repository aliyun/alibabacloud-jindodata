## JindoRuntime For PAI 使用指南
### 1、创建命名空间
```shell
kubectl create ns fluid-system
```
### 2、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/PAI/fluid-0.6.0.tgz)
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
其中 csi-nodeplugin-fluid-xx 的数量应该与 K8S 集群中节点node的数量相同。
### 5、创建 dataset 和 JindoRuntime
#### 5.1、创建 secret
在创建 dataset 之前，我们可以创建一个 secret 来保存 STS 的 AccessKeyId/AccessKeySecret/SecurityToken。
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
stringData:
  AccessKeyId: xxx
  AccessKeySecret: xxx
  SecurityToken: xxx
```
生成`secret`
```yaml
kubectl create -f mySecret.yaml
```
创建一个 `resource.yaml` 文件里面包含两部分：
* 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源。
* 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFSx 缓存系统集群来提供缓存服务。

#### 5.2、创建 dataset
- 默认挂载
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
```
- 根目录挂载
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
      path: /
```
#### 5.3、创建 JindoRuntime
创建如下的 runtime.yaml，默认开启缓存。
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1
        quota: 10G
        high: "0.9"
        low: "0.8"
  secret: mysecret
```
其中
* mountPoint：oss://<oss_bucket>/<bucket_dir> 表示挂载UFS的路径，路径中不需要包含endpoint信息。
* fs.oss.endpoint：oss bucket的endpoint信息，公网或内网地址皆可。
* replicas：表示创建 JindoFSx 集群的 worker 的数量。
* mediumtype： JindoFSx 暂只支持HDD/SSD/MEM中的其中一种。
* path：存储路径，暂只支持一块盘，当选择MEM做缓存也需要一块盘来存储log等文件。
* quota：缓存最大容量，单位G。
* high：水位上限大小 / low： 水位下限大小。
* secret：使用 STS 绑定的 secret 名称，在同一个 namespace 下。

创建 `JindoRuntime`
```shell
kubectl create -f resource.yaml
```
查看 `dataset` 的情况
```shell
$ kubectl get dataset hadoop
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop        210MiB       0.00B    180.00GiB              0.0%          Bound   1h
```
查看 `JindoRuntime` 的情况
```shell
$ kubectl get jindoruntime hadoop
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hadoop   Ready          Ready          Ready        34m
```

如您想关闭缓存，可使用如下的最小化`runtime.yaml`
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1
        quota: 10G
  secret: mysecret
  fuse:
    properties:
      jfs.cache.data-cache.enable: "0"
```
### 6、环境清理
* 删除应用和应用容器
* 删除 JindoRuntime
```shell
kubectl delete jindoruntime hadoop
```
* 删除 dataset
```shell
kubectl delete dataset hadoop
```