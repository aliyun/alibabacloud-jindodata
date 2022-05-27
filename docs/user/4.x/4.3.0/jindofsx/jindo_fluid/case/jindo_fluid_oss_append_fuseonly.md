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


其中 csi-nodeplugin-fluid-xx 的数量应该与 K8S 集群中节点node的数量相同。
## 5、测试数据源准备


### 5.1 准备 OSS 服务
您需要开通阿里云对象存储服务(Object Storage Service，简称OSS)，按照使用文档进行bucket创建([https://www.aliyun.com/product/oss](https://www.aliyun.com/product/oss))


### 5.2 准备测试样例数据
我们可以使用 Apache 镜像站点上的 Spark 相关资源作为演示中使用的远程文件。这个选择并没有任何特殊之处，你可以将这个远程文件修改为任意你喜欢的远程文件。

- 现在远程资源文件到本地
```shell
mkdir tmp
cd tmp
wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.7/spark-2.4.7-bin-hadoop2.7.tgz 
wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz 
```

- 上传本地文件到 OSS 上

您可以使用 OSS 提供的客户端 [ossutil](https://helpcdn.aliyun.com/document_detail/50452.html) 或者阿里云EMR团队提供的 [JindoSDK](../../outline.md)，按照使用说明将本地下载的文件上传到远程 OSS 的bucket上。如您的bucket名字为 test-bucket 且使用 JindoSDK 作为客户端工具，可执行如下命令上传文件到 OSS 上面。
```shell
hadoop fs -put spark-2.4.7-bin-hadoop2.7.tgz oss://test-bucket/spark/spark-2.4.7/spark-2.4.7-bin-hadoop2.7.tgz 
hadoop fs -put spark-3.1.1-bin-hadoop3.2.tgz oss://test-bucket/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz
```

### 5.3 创建 dataset 和 JindoRuntime

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
  fuse:
    args:
      - -oattr_timeout=0
      - -oentry_timeout=0
      - -onegative_timeout=0
    properties:
      fs.oss.append.enable: "true"
      fs.oss.flush.interval.millisecond: "1000"
      fs.oss.read.buffer.size: "262144"
      fs.oss.write.buffer.size: "262144"
      fs.oss.upload.thread.concurrency: "32"
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
