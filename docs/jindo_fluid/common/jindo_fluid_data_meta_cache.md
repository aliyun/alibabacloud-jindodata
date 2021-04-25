本文介绍如何选择数据缓存和元数据缓存方面的参数。


### 打开/关闭数据缓存


在 JindoRuntime 中，数据缓存默认打开，也可通过在 Runtime 中指定 `jfs.cache.data-cache.enable: "false"` 来关闭缓存，如下


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
        path: /mnt/disk1/
        quota: 290G
        high: "0.9"
        low: "0.8"
  hadoopConfig: hdfsconfig
  user: hadoop
  fuse:
    properties:
      jfs.cache.data-cache.enable: "false"
```


### 打开元数据缓存


在 JindoRuntime 中，元数据缓存默认关闭，通过元数据缓存元数据信息，可通过在 Runtime 中指定 `jfs.cache.meta-cache.enable: "true"`  来打开元数据缓存，如下
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
        path: /mnt/disk1/
        quota: 290G
        high: "0.9"
        low: "0.8"
  hadoopConfig: hdfsconfig
  user: hadoop
  fuse:
    properties:
      jfs.cache.meta-cache.enable: "true"
      jfs.cache.data-cache.slicecache.enable: "true"
```


