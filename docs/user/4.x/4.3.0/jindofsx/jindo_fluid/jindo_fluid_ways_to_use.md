# Fluid + JindoFSx 存储加速系统高阶功能使用
- 使用列表
    - [挂载点在根目录下](#挂载点在根目录下)
    - [Secret 加密 AK 参数](#secret-加密-ak-参数)
    - [Raft 3 master 模式](#raft-3-master-模式)
    - [使用 Placement 部署多个 runtime](#使用-placement-部署多个-runtime)
    - [使用 NoseSelector 部署节点](#使用-noseselector-部署节点)
    - [使用 dataset nodeAffinity 功能](#使用-dataset-nodeaffinity-功能)
    - [Worker 个数扩缩容](#worker-个数扩缩容)
    - [使用 tolerations 功能](#使用-tolerations-功能)
    - [Resource 资源](#resource-资源)
    - [Fuse 回收策略](#fuse-回收策略)
    - [JindoFSx 客户端相关参数和使用](#jindofsx-客户端相关参数和使用)
    - [JindoFSx Fuse 客户端相关参数和使用](#jindofsx-fuse-客户端相关参数和使用)

### 挂载点在根目录下
默认使用 JindoRuntime 会在挂载点多一层 /jindo 的目录，如果想挂载在根目录下可以在 dataset 里进行参数指定

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
      path: /
```
指定 spec.mounts.path = /，则会将文件挂载在根目录下

### Secret 加密 AK 参数 
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
stringData:
  fs.oss.accessKeyId: <OSS_ACCESS_KEY_ID>
  fs.oss.accessKeySecret: <OSS_ACCESS_KEY_SECRET>
```
在 dataset 里使用 secret
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
  - mountPoint: oss://<OSS_BUCKET>/<OSS_DIRECTORY>/
    name: hadoop
    options:
      fs.oss.endpoint: <OSS_ENDPOINT>
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
```

### Raft 3 master 模式
JindoRuntime 模式启动一个 master，可以通过使用 master.replicas 来启动 3 个 master 进行 HA 转换。
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: HDD
        path: /mnt/disk1
        quota: 2G
        high: "0.8"
        low: "0.7"
  master:
    replicas: 3
```
* master.replicas = 3 : 启动 3 个 master pod


### 使用 Placement 部署多个 runtime
JindoRuntime 启动后，默认 worker 节点上只能启动一个 runtime 的worker，属于独占模式，如果想要在同一个节点上支持部署多个 worker，可以使用 shared 模式

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
  placement: "Shared"
```

### 使用 NoseSelector 部署节点
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
        quota: 10Gi
        high: "0.9"
        low: "0.8"
  master:
    nodeSelector:
      kubernetes.io/hostname: cn-hangzhou.10.1.1
  worker:
    nodeSelector:
      kubernetes.io/hostname: cn-hangzhou.10.1.1
  fuse:
    nodeSelector:
      kubernetes.io/hostname: cn-hangzhou.10.1.1
```

### 使用 dataset nodeAffinity 功能

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
            - key: hbase-cache
              operator: In
              values:
                - "true"
```

### Worker 个数扩缩容
使用`kubectl scale`对 Runtime 的 Worker 数量进行调整

```shell
$ kubectl scale jindoruntime <runtime_name> --replicas=<replica_num>
```
其中 
* runtime_name：runtime 的名字
* replica_num：表示扩所容的 Worker 数量

### 使用 tolerations 功能
您可以在 dataset 里定义 tolerations 指定 worker 节点的调度

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
  tolerations:
    - key: hbase 
      operator: Equal 
      value: "true" 
```
也可以为 master/worker/fuse 单独指定 tolerations 调度策略

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 3
  tieredstore:
    levels:
      - mediumtype: HDD
        path: /mnt/disk1
        quota: 2Gi
        high: "0.8"
        low: "0.7"
  master:
    tolerations:
    - key: hbase 
      operator: Equal 
      value: "true"
  worker:
    tolerations:
    - key: hbase 
      operator: Equal 
      value: "true" 
  fuse:
    tolerations:
    - key: hbase 
      operator: Equal 
      value: "true"  
```

### Resource 资源
可以指定 master/worker 等的 resource 资源
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
        path: /var/lib/docker/jindo
        quota: 200Gi
        high: "0.9"
        low: "0.8"
  master:
    resources:
      limits:
        cpu: "4"
        memory: "8Gi"
      requests:
        cpu: "2"
        memory: "3Gi"
  worker:
    resources:
      limits:
        cpu: "4"
        memory: "8Gi"
      requests:
        cpu: "2"
        memory: "3Gi"
```

### Fuse 回收策略

```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: oss
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /var/lib/docker/jindo
        quota: 200Gi
        high: "0.9"
        low: "0.8"
  fuse:
    cleanPolicy: OnDemand
```
* fuse.cleanPolicy: onDemand / OnRuntimeDeleted 分别表示按需启动，任务结束 fuse 也结束和在 runtime 销毁的时候和其他组件一起销毁掉

### JindoFSx 客户端相关参数和使用

下面介绍一些和 JindoFSx 缓存相关的参数，可以按照实际场景进行选择

| Parameter      |  Value | Description  |
| :---         |  :----:   |    :---   |
| fs.jindofsx.data.cache.enable      | true/false       | 是否打开数据缓存，默认为打开   |
| fs.jindofsx.meta.cache.enable   | true/false        | 是否打开元数据缓存，默认为关闭      |
| fs.jindofsx.slicelet.cache.enable   | true/false        | 是否打开小文件缓存优化(写磁盘)，默认为关闭   |
| fs.jindofsx.short.circuit.enable   | true/false        | 是否打开短路读，默认为打开     |

配置方式
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: oss
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /var/lib/docker/jindo
        quota: 200Gi
        high: "0.9"
        low: "0.8"
  fuse:
    properties:
      fs.jindofsx.data.cache.enable: "false"
      fs.jindofsx.meta.cache.enable: "true"
      fs.jindofsx.slicelet.cache.enable: "false"
      fs.jindofsx.short.circuit.enable: "false"
```
以 kv 方式写在 `spec.fuse.properties` 里，按照需要填写即可

### JindoFSx Fuse 客户端相关参数和使用
| 参数名称         | 参数说明                                                     | 使用范例             |
| ---------------- |------------------------------------------------------------ | -------------------- |
| auto_unmount  | fuse进程退出后自动umount挂载节点。                           | -oauto_unmount       |
| ro           | 只读挂载，启用参数后不允许写操作。                           | -oro                 |
| direct_io            | 开启后，读写文件可以绕过page cache。                         | -odirect_io          |
| kernel_cache          | 开启后，利用内核缓存优化读性能。                             | -okernel_cache      
| auto_cache            | 默认开启，与kernel_cache 二选一，与kernel_cache不同的是，如果文件大小或修改时间发生变化，缓存就会失效。 |   -oauto_cache                   |
| entry_timeout         | 默认值，0.1。文件名读取缓存保留时间（秒），用于优化性能。0表示不缓存。 | 默认-oentry_timeout=60   |
| attr_timeout          | 默认值，0.1。文件属性缓存保留时间（秒），用于优化性能。0表示不缓存。 | 默认-oattr_timeout=60    |
| negative_timeout       | 默认值，0.1。文件名读取失败缓存保留时间（秒），用于优化性能。0表示不缓存。 | 默认-onegative_timeout=30 |

配置方式
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: oss
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /var/lib/docker/jindo
        quota: 200Gi
        high: "0.9"
        low: "0.8"
  fuse:
    args:
      - -oro
      - -oattr_timeout=60
      - -oentry_timeout=60
      - -onegative_timeout=60
```

以数组方式写在 `spec.fuse.properties` 里，按照需要填写即可
