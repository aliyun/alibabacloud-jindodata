# 加速OSS上数据

使用 JindoRuntime 流程简单，在准备好基本 K8S 和 OSS 环境的条件下，您只需要耗费10分钟左右时间即可部署好需要的 JindoRuntime 环境，您可以按照下面的流程进行部署。

## 1、创建命名空间
```shell
kubectl create ns fluid-system
```
## 2、[下载 Fluid 安装包](../jindo_fluid_download.md)

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

### 5、创建 dataset 和 JindoRuntime

创建一个 resource.yaml 文件里面包含两部分：

- 首先包含数据集及 ufs 的 dataset 信息，创建一个 Dataset CRD 对象，其中描述了数据集的来源，如例子中的 test-bucket。
- 接下来需要创建一个 JindoRuntime，相当于启动一个 JindoFSx 的集群来提供缓存服务。


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
      path: /
  accessModes:
    - ReadWriteMany
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  master:
    disabled: true
  worker:
    disabled: true
```


- mountPoint：表示挂载UFS的路径，路径中不需要包含 endpoint 信息。
- fs.oss.accessKeyId/fs.oss.accessKeySecret：oss bucket的AK信息，有权限访问该 bucket。
- fs.oss.endpoint：oss bucket的endpoint信息，公网或内网地址皆可，如您的 bucket 在杭州 Region，那么公网地址为 oss-cn-hangzhou.aliyuncs.com，内网地址为 oss-cn-hangzhou-internal.aliyuncs.com(内网地址使用条件为您的 K8S 集群所在区域和oss区域相同)。


创建 JindoRuntime


```shell
kubectl create -f resource.yaml
```

查看 PV，PVC 创建情况，JindoRuntime部署过程中会自动创建PV和PVC
```shell
kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/hadoop   100Gi      RWX            Retain           Bound    default/hadoop                           58m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/hadoop   Bound    hadoop   100Gi      RWX                           58m
```

## 6、环境清理

- 删除应用和应用容器
- 删除 JindoRuntime

```shell
kubectl delete jindoruntime hadoop
```

- 删除 dataset

```shell
kubectl delete dataset hadoop
```
