通过JindoFS和[Fuse](https://github.com/libfuse/libfuse)，Fluid为用户提供了一种更为简单的文件访问接口，使得任意运行在Kubernetes集群上的程序能够像访问本地文件一样轻松访问存储在远程文件系统中的文件。Fluid 针对数据集进行全生命周期的管理和隔离，尤其对于短生命周期应用（e.g 数据分析任务、机器学习任务），用户可以在集群中大规模部署。


本文档通过一个简单的例子演示了上述功能特性
## 前提条件


在运行该示例之前，请参考[安装文档](./jindo_fluid_install.md)完成安装完成安装，并检查Fluid各组件正常运行：


```shell
$ kubectl get pod -n fluid-system
NAME                                  READY   STATUS    RESTARTS   AGE
alluxioruntime-controller-5b64fdbbb-84pc6   1/1     Running   0          8h
csi-nodeplugin-fluid-fwgjh                  2/2     Running   0          8h
csi-nodeplugin-fluid-ll8bq                  2/2     Running   0          8h
dataset-controller-5b7848dbbb-n44dj         1/1     Running   0          8h
jindoruntime-controller-654fb74447-cldsv    1/1     Running   0          8h
```


通常来说，你会看到一个名为`dataset-controller`的Pod、一个名为`jindoruntime-controller`的Pod和多个名为`csi-nodeplugin`的Pod正在运行。其中，`csi-nodeplugin`这些Pod的数量取决于你的Kubernetes集群中结点的数量。


## 运行示例


**对某个节点打标签**


```shell
$ kubectl  label node cn-beijing.192.168.0.199 fluid=multi-dataset
```


> 在接下来的步骤中，我们将使用 `NodeSelector` 来管理Dataset调度的节点，这里仅做试验使用。



**查看待创建的Dataset资源对象**

- dataset.yaml
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hbase
spec:
  mounts:
    - mountPoint: oss://test-bucket/
      options:
        fs.oss.accessKeyId: <OSS_ACCESS_KEY_ID>
        fs.oss.accessKeySecret: <OSS_ACCESS_KEY_SECRET>
        fs.oss.endpoint: <OSS_ENDPOINT> 
      name: hbase
  nodeAffinity:
    required:
      nodeSelectorTerms:
        - matchExpressions:
            - key: fluid
              operator: In
              values:
                - "multi-dataset"
  placement: "Shared" // 设置为 Exclusive 或者为空则为独占节点数据集

```
- dataset1.yaml
  
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: spark
spec:
  mounts:
    - mountPoint: - mountPoint: oss://test-bucket-1/
      options:
        fs.oss.accessKeyId: <OSS_ACCESS_KEY_ID>
        fs.oss.accessKeySecret: <OSS_ACCESS_KEY_SECRET>
        fs.oss.endpoint: <OSS_ENDPOINT> 
      name: spark
  nodeAffinity:
    required:
      nodeSelectorTerms:
        - matchExpressions:
            - key: fluid
              operator: In
              values:
                - "multi-dataset"
  placement: "Shared" 
```


**创建Dataset资源对象**


```shell
$ kubectl apply -f dataset.yaml
dataset.data.fluid.io/hbase created
$ kubectl apply -f dataset1.yaml
dataset.data.fluid.io/spark created
```


**查看Dataset资源对象状态**


```shell
$ kubectl get dataset
NAME    UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE      AGE
hbase                                                                  NotBound   6s
spark                                                                  NotBound   4s
```


如上所示，`status`中的`phase`属性值为`NotBound`，这意味着该`Dataset`资源对象目前还未与任何`JindoRuntime`资源对象绑定，接下来，我们将创建一个`JindoRuntime`资源对象。


**查看待创建的JindoRuntime资源对象**

- runtime.yaml
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hbase
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk1
        quota: 2G
        high: "0.8"
        low: "0.7"
```  

- runtime-1.yaml
  
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: spark
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /mnt/disk2/
        quota: 4G
        high: "0.8"
        low: "0.7"
```


**创建JindoRuntime资源对象**


```shell
$ kubectl create -f runtime.yaml
jindoruntime.data.fluid.io/hbase created

# 等待 Dataset hbase 全部组件 Running 
$ kubectl get pod -o wide | grep hbase
NAME                              READY   STATUS    RESTARTS   AGE   IP              NODE                       NOMINATED NODE   READINESS GATES
hbase-jindofs-fuse-jl2g2           1/1     Running   0          2m24s   192.168.0.199   cn-beijing.192.168.0.199   <none>           <none>
hbase-jindofs-master-0             2/2     Running   0          2m55s   192.168.0.200   cn-beijing.192.168.0.200   <none>           <none>
hbase-jindofs-worker-g89p8         2/2     Running   0          2m24s   192.168.0.199   cn-beijing.192.168.0.199   <none>           <none>

$ kubectl create -f runtime1.yaml
jindoruntime.data.fluid.io/spark created
```


**检查JindoRuntime资源对象是否已经创建**


```shell
$ kubectl get jindoruntime
NAME    MASTER PHASE   WORKER PHASE   FUSE PHASE   AGE
hbase   Ready          Ready          Ready        2m14s
spark   Ready          Ready          Ready        58s
```


`JindoRuntime`是另一个Fluid定义的CRD。一个`JindoRuntime`资源对象描述了在Kubernetes集群中运行一个JindoFS实例所需要的配置信息。


等待一段时间，让JindoRuntime资源对象中的各个组件得以顺利启动，你会看到类似以下状态：


```shell
$ kubectl get pod -o wide
NAME                        READY   STATUS    RESTARTS   AGE     IP              NODE                       NOMINATED NODE   READINESS GATES
hbase-jindofs-fuse-jl2g2     1/1     Running   0          2m24s   192.168.0.199   cn-beijing.192.168.0.199   <none>           <none>
hbase-jindofs-master-0       2/2     Running   0          2m55s   192.168.0.200   cn-beijing.192.168.0.200   <none>           <none>
hbase-jindofs-worker-g89p8   2/2     Running   0          2m24s   192.168.0.199   cn-beijing.192.168.0.199   <none>           <none>
spark-jindofs-fuse-5z49p     1/1     Running   0          19s     192.168.0.199   cn-beijing.192.168.0.199   <none>           <none>
spark-jindofs-master-0       2/2     Running   0          50s     192.168.0.200   cn-beijing.192.168.0.200   <none>           <none>
spark-jindofs-worker-96ksn   2/2     Running   0          19s     192.168.0.199   cn-beijing.192.168.0.199   <none>           <none>
```


注意上面的不同的 Dataset 的 worker 和 fuse 组件可以正常的调度到相同的节点 `cn-beijing.192.168.0.199`。


**再次查看Dataset资源对象状态**


```shell
$ kubectl get dataset 
NAME    UFS TOTAL SIZE   CACHED   CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hbase   443.89MiB        0.00B    2.00GiB          0.0%                Bound   11m
spark   1.92GiB          0.00B    4.00GiB          0.0%                Bound   9m38s
```


因为已经与一个成功启动的JindoRuntime绑定，该Dataset资源对象的状态得到了更新，此时`PHASE`属性值已经变为`Bound`状态。通过上述命令可以获知有关资源对象的基本信息


**查看JindoRuntime状态**


```shell
$ kubectl get jindoruntime -o wide
NAME    READY MASTERS   DESIRED MASTERS   MASTER PHASE   READY WORKERS   DESIRED WORKERS   WORKER PHASE   READY FUSES   DESIRED FUSES   FUSE PHASE   AGE
hbase   1               1                 Ready          1               1                 Ready          1             1               Ready        11m
spark   1               1                 Ready          1               1                 Ready          1             1               Ready        9m52s
```


`JindoRuntime`资源对象的`status`中包含了更多更详细的信息


**查看与远程文件关联的PersistentVolume以及PersistentVolumeClaim**


```shell
$ kubectl get pv
NAME    CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM           STORAGECLASS   REASON   AGE
hbase   100Gi      RWX            Retain           Bound    default/hbase                           4m55s
spark   100Gi      RWX            Retain           Bound    default/spark                           51s
```


```shell
$ kubectl get pvc
NAME    STATUS   VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
hbase   Bound    hbase    100Gi      RWX                           4m57s
spark   Bound    spark    100Gi      RWX                           53s
```


`Dataset`资源对象准备完成后（即与JindoFS实例绑定后），与该资源对象关联的PV, PVC已经由Fluid生成，应用可以通过该PVC完成远程文件在Pod中的挂载，并通过挂载目录实现远程文件访问


## 远程文件访问


**查看待创建的应用**

- nginx.yaml

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-hbase
spec:
  containers:
    - name: nginx
      image: nginx
      volumeMounts:
        - mountPath: /data
          name: hbase-vol
  volumes:
    - name: hbase-vol
      persistentVolumeClaim:
        claimName: hbase
  nodeName: cn-beijing.192.168.0.199
```

- nginx1.yaml

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-spark
spec:
  containers:
    - name: nginx
      image: nginx
      volumeMounts:
        - mountPath: /data
          name: hbase-vol
  volumes:
    - name: hbase-vol
      persistentVolumeClaim:
        claimName: spark
  nodeName: cn-beijing.192.168.0.199
```


**启动应用进行远程文件访问**


```shell
$ kubectl create -f nginx.yaml
$ kubectl create -f nginx1.yaml
```


登录Nginx hbase Pod:


```shell
$ kubectl exec -it nginx-hbase -- bash
```


查看远程文件挂载情况：


```shell
$ ls -lh /data/hbase
total 444M
-r--r----- 1 root root 193K Sep 16 00:53 CHANGES.md
-r--r----- 1 root root 112K Sep 16 00:53 RELEASENOTES.md
-r--r----- 1 root root  26K Sep 16 00:53 api_compare_2.2.6RC2_to_2.2.5.html
-r--r----- 1 root root 211M Sep 16 00:53 hbase-2.2.6-bin.tar.gz
-r--r----- 1 root root 200M Sep 16 00:53 hbase-2.2.6-client-bin.tar.gz
-r--r----- 1 root root  34M Sep 16 00:53 hbase-2.2.6-src.tar.gz
```


登录Nginx spark Pod:


```shell
$ kubectl exec -it nginx-spark -- bash
```


查看远程文件挂载情况：


```shell
$ ls -lh /data/spark/
total 1.0K
dr--r----- 1 root root 7 Oct 22 12:21 spark-2.4.7
dr--r----- 1 root root 7 Oct 22 12:21 spark-3.0.1
$ du -h /data/spark/
999M	/data/spark/spark-3.0.1
968M	/data/spark/spark-2.4.7
2.0G	/data/spark/
```


登出Nginx Pod:


```shell
$ exit
```


正如你所见，WebUFS上所存储的全部文件,可以和本地文件完全没有区别的方式存在于某个Pod中，并且可以被该Pod十分方便地访问


## 远程文件访问加速


为了演示在访问远程文件时，你能获得多大的加速效果，我们提供了一个测试作业的样例:


**查看待创建的测试作业**

- app.yaml
```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: fluid-copy-test-hbase
spec:
  template:
    spec:
      restartPolicy: OnFailure
      containers:
        - name: busybox
          image: busybox
          command: ["/bin/sh"]
          args: ["-c", "set -x; time cp -r /data/hbase ./"]
          volumeMounts:
            - mountPath: /data
              name: hbase-vol
      volumes:
        - name: hbase-vol
          persistentVolumeClaim:
            claimName: hbase
      nodeName: cn-beijing.192.168.0.199
```

- app1.yaml

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: fluid-copy-test-spark
spec:
  template:
    spec:
      restartPolicy: OnFailure
      containers:
        - name: busybox
          image: busybox
          command: ["/bin/sh"]
          args: ["-c", "set -x; time cp -r /data/spark ./"]
          volumeMounts:
            - mountPath: /data
              name: spark-vol
      volumes:
        - name: spark-vol
          persistentVolumeClaim:
            claimName: spark
      nodeName: cn-beijing.192.168.0.199
```


**启动测试作业**


```shell
$ kubectl create -f app.yaml
job.batch/fluid-copy-test-hbase created
$ kubectl create -f app1.yaml
job.batch/fluid-copy-test-spark created
```


hbase任务程序会执行`time cp -r /data/hbase ./`的shell命令，其中`/data/hbase`是远程文件在Pod中挂载的位置，该命令完成后会在终端显示命令执行的时长。


spark任务程序会执行`time cp -r /data/spark ./`的shell命令，其中`/data/spark`是远程文件在Pod中挂载的位置，该命令完成后会在终端显示命令执行的时长。


等待一段时间,待该作业运行完成,作业的运行状态可通过以下命令查看:


```shell
$ kubectl get pod -o wide | grep copy 
fluid-copy-test-hbase-r8gxp   0/1     Completed   0          4m16s   172.29.0.135    cn-beijing.192.168.0.199   <none>           <none>
fluid-copy-test-spark-54q8m   0/1     Completed   0          4m14s   172.29.0.136    cn-beijing.192.168.0.199   <none>           <none>
```


如果看到如上结果,则说明该作业已经运行完成


> 注意: `fluid-copy-test-hbase-r8gxp`中的`r8gxp`为作业生成的标识,在你的环境中,这个标识可能不同,接下来的命令中涉及该标识的地方请以你的环境为准



**查看测试作业完成时间**


```shell
$ kubectl  logs fluid-copy-test-hbase-r8gxp
+ time cp -r /data/hbase ./
real    3m 34.08s
user    0m 0.00s
sys     0m 1.24s
$ kubectl  logs fluid-copy-test-spark-54q8m
+ time cp -r /data/spark ./
real    3m 25.47s
user    0m 0.00s
sys     0m 5.48s
```


可见，第一次远程文件的读取hbase耗费了接3m34s的时间，读取spark耗费接近3m25s时间。


**查看Dataset资源对象状态**


```shell
$ kubectl get dataset
NAME    UFS TOTAL SIZE   CACHED      CACHE CAPACITY   CACHED PERCENTAGE   PHASE   AGE
hbase   443.89MiB        443.89MiB   2.00GiB          100.0%              Bound   30m
spark   1.92GiB          1.92GiB     4.00GiB          100.0%              Bound   28m
```


现在，所有远程文件都已经被缓存在了JindoFS中


**再次启动测试作业**


```shell
$ kubectl delete -f app.yaml
$ kubectl create -f app.yaml
$ kubectl delete -f app1.yaml
$ kubectl create -f app1.yaml
```


由于远程文件已经被缓存，此次测试作业能够迅速完成：


```shell
$ kubectl get pod -o wide| grep fluid
fluid-copy-test-hbase-sf5md   0/1     Completed   0          53s   172.29.0.137    cn-beijing.192.168.0.199   <none>           <none>
fluid-copy-test-spark-fwp57   0/1     Completed   0          51s   172.29.0.138    cn-beijing.192.168.0.199   <none>           <none>
```


```shell
$ kubectl  logs fluid-copy-test-hbase-sf5md
+ time cp -r /data/hbase ./
real    0m 0.36s
user    0m 0.00s
sys     0m 0.36s
$ kubectl  logs fluid-copy-test-spark-fwp57
+ time cp -r /data/spark ./
real    0m 1.57s
user    0m 0.00s
sys     0m 1.57s
```


同样的文件访问操，hbase仅耗费了0.36s，spark仅耗费了1.57s。


这种大幅度的加速效果归因于JindoFS所提供的强大的缓存能力，这种缓存能力意味着，只要你访问某个远程文件一次，该文件就会被缓存在JindoFS中，你的所有接下来的重复访问都不再需要进行远程文件读取，而是从JindoFS中直接获取数据，因此对于数据的访问加速也就不难解释了。
## 环境清理


```shell
$ kubectl delete -f .
$ kubectl label node cn-beijing.192.168.0.199 fluid-
```
