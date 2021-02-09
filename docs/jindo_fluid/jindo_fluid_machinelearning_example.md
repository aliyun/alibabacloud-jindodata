# 拥抱云原生，Fluid结合JindoFS：加速机器学习训练

在 [拥抱云原生，Fluid结合JindoFS ：阿里云OSS加速利器](jindo_fluid_introduce.md) 这篇文章中，我们已经介绍了 Fluid 以及 JindoRuntime 的基本概念和使用流程，这篇文章我们将继续介绍如何使用 Fluid 和 JindoRuntime 部署[阿里云 OSS](https://cn.aliyun.com/product/oss) 云端 [ImageNet](http://www.image-net.org/) 数据集到 Kubernetes 集群，并使用 [Arena](https://github.com/kubeflow/arena) 在此数据集上训练 ResNet-50 模型(本文以四机八卡测试环境为例)。


## 1、前提条件
[Fluid](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/332cache/fluid-0.5.0.tgz) (version 0.5.0)
[Arena](https://github.com/kubeflow/arena)（version >= 0.4.0）
[Horovod](https://github.com/horovod/horovod) (version=0.18.1)
[Benchmark](https://github.com/tensorflow/benchmarks/tree/cnn_tf_v1.14_compatible)
> **注意**：
> 1. 本文要求在Kubernetes集群中已安装好 Fluid，如果您还没部署 Fluid，请参考 [拥抱云原生，Fluid结合JindoFS ：阿里云OSS加速利器](https://developer.aliyun.com/article/781656?spm=a2c6h.13148508.0.0.59544f0emzSkc5%E5%B7%B2%E5%8F%91) 在您的Kubernetes集群上安装Fluid。
> 2. `Arena`是一个方便数据科学家运行和监视机器学习任务的CLI, 本文使用`Arena`提交机器学习任务，安装教程可参考 [Arena安装教程](https://github.com/kubeflow/arena/blob/master/docs/installation/INSTALL_FROM_BINARY.md)。
> 3. 本演示中使用的Horovod， TensorFlow和Benchmark代码均已经开源，您可以从上述链接中获取。


## 
## 2、用 Fluid 部署云端数据集
如下的文件中定义了一个 Secret 和 DataSet，并---符号将它们的定义分割。数据集存储在 [阿里云OSS](https://cn.aliyun.com/product/oss) ，为保证 JindoFS 能够成功挂载OSS上的数据集，请确保以下文件中设置了正确的 mountPoint、fs.oss.accessKeyId、fs.oss.accessKeySecret 和 fs.oss.endpoint。


> 如果您希望自己准备数据集，可以访问ImageNet官方网站 [http://image-net.org/download-images](http://image-net.org/download-images)。
> 如果你希望使用我们提供的数据集重现这个实验，请在社区开Issue申请数据集下载。



本文档以阿里云的V100四机八卡为例，所以设置runtime中 spec.replicas=4。为了保证数据被缓存在V100机器上，配置了 nodeAffinity。
首先填写以下 dataset.yaml 的相关内容
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
stringData:
  fs.oss.accessKeyId: xxx
  fs.oss.accessKeySecret: xxx
---
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
  nodeAffinity:
    required:
      nodeSelectorTerms:
        - matchExpressions:
            - key: aliyun.accelerator/nvidia_name
              operator: In
              values:
                - Tesla-V100-SXM2-16GB
```
然后创建 JindoRuntime
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 4
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /var/lib/docker/jindo
        quota: 180Gi
        high: "0.99"
        low: "0.8"
```
创建 Dataset和 JindoRuntime：
```shell
kubectl create -f dataset.yaml
kubectl create -f runtime.yaml
```
查看此时的 dataset 状态
```shell
$ kubectl get dataset
NAME     UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hadoop   143.67GiB        0.00B    648.00GiB        0.0%                Bound   5m
```
检查 JindoRuntime 状态
```shell
$ kubectl get jindoruntime
NAME     MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hadoop   Ready          Ready          Ready        4m45s
```
检查 pv 和 pvc，名为 hadoop 的 pv 和 pvc 被成功创建：
```shell
$ kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM            STORAGECLASS   REASON   AGE
persistentvolume/hadoop   100Gi      RWX            Retain           Bound    default/hadoop                           52m

NAME                           STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/hadoop   Bound    hadoop   100Gi      RWX                           52m
```


到此 OSS 云端数据集已成功部署到 Kubernetes 集群中。


## 3、使用Arena提交深度学习任务
Arena提供了便捷的方式帮助用户提交和监控机器学习任务。在本文中，我们使用Arena简化机器学习任务的部署流程，首先安装Arena
```shell
wget http://kubeflow.oss-cn-beijing.aliyuncs.com/arena-installer-0.4.0-829b0e9-linux-amd64.tar.gz
tar -xzvf arena-installer-0.4.0-829b0e9-linux-amd64.tar.gz
sh ./arena-installer/install.sh
```
如果您已经安装Arena，并且云端数据集已成功部署到本地集群中，只需要简单执行以下命令便能提交ResNet50四机八卡训练任务：
```shell
arena submit mpi     --name horovod-resnet50     --gpus=8     --workers=4     --working-dir=/horovod-demo/tensorflow-demo/     --data hadoop:/data     -e DATA_DIR=/data/hadoop     -e num_batch=1000     -e datasets_num_private_threads=8     --image=registry.cn-hangzhou.aliyuncs.com/tensorflow-samples/horovod-benchmark-dawnbench-v2:0.18.1-tf1.14.0-torch1.2.0-mxnet1.5.0-py3.6     ./launch-example.sh 4 8
```


Arena参数说明：
* --name：指定job的名字
* --workers：指定参与训练的节点（worker）数
* --gpus：指定每个worker使用的GPU数
* --working-dir：指定工作路径
* --data：挂载Volume imagenet到worker的/data目录
* -e DATA_DIR：指定数据集位置
* launch-example.sh 4 8：运行脚本启动四机八卡测试


检查任务是否正常执行：
```shell
$ arena get horovod-resnet50
STATUS: RUNNING
NAMESPACE: default
PRIORITY: N/A
TRAINING DURATION: 56m

NAME              STATUS   TRAINER  AGE  INSTANCE                         NODE
horovod-resnet50  RUNNING  MPIJOB   56m  horovod-resnet50-launcher-xs7h4  192.168.0.15
horovod-resnet50  RUNNING  MPIJOB   56m  horovod-resnet50-worker-0        192.168.0.15
horovod-resnet50  RUNNING  MPIJOB   56m  horovod-resnet50-worker-1        192.168.0.14
horovod-resnet50  RUNNING  MPIJOB   56m  horovod-resnet50-worker-2        192.168.0.12
horovod-resnet50  RUNNING  MPIJOB   56m  horovod-resnet50-worker-3        192.168.0.13
```
如果您看到4个处于RUNNING状态的worker，说明您已经成功启动训练。
如果您想知道训练进行到哪一步了，请检查Arena的log：
```shell
arena logs --tail 100 -f horovod-resnet50
```
最后进行环境环境清理
```shell
kubectl delete -f dataset hadoop
kubectl delete -f jindoruntime hadoop
```


