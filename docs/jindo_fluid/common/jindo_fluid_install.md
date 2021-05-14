## 安装使用流程


### 1、创建命名空间


```shell
$ kubectl create ns fluid-system
```


### 2、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/356/fluid-0.6.0.tgz)


### 3、使用 Helm 安装 Fluid

```shell
$ helm install --set runtime.jindo.enabled=true fluid fluid-0.6.0.tgz
```

### 3、查看 Fluid 的运行状态

```shell
$ kubectl get pod -n fluid-system
NAME                                         READY   STATUS    RESTARTS   AGE
csi-nodeplugin-fluid-2mfcr                   2/2     Running   0          108s
csi-nodeplugin-fluid-l7lv6                   2/2     Running   0          108s
dataset-controller-5465c4bbf9-5ds5p          1/1     Running   0          108s
jindoruntime-controller-654fb74447-cldsv     1/1     Running   0          108s
```

其中 csi-nodeplugin-fluid-xx 的数量应该与k8s集群中节点node的数量相同。

#### 到此 Fluid 已成功安装，如需自定义镜像和升级系统 crd 请参考如下说明(非必需)
### 4、自定义镜像

解压 `fluid-0.6.0.tgz`，修改默认`values.yaml`文件

```yaml
runtime:
  mountRoot: /runtime-mnt
  jindo:
    enabled: true
    smartdata:
      image: registry.cn-shanghai.aliyuncs.com/jindofs/smartdata:3.5.0
    fuse:
      image: registry.cn-shanghai.aliyuncs.com/jindofs/jindo-fuse:3.5.0
    controller:
      image: registry.cn-shanghai.aliyuncs.com/jindofs/jindoruntime-controller:v0.6.0-d90f9e5
```


可以修改`jindo`相关的默认`image`内容，如放到自己的`repo`上，修改完成后重新使用`helm package fluid`打包，使用如下命令更新`fluid`版本


```shell
helm upgrade --install fluid fluid-0.6.0.tgz
```


### 5、更新 crd


```shell
$ hdfscache kubectl get crd      
NAME                                             CREATED AT
alluxiodataloads.data.fluid.io                   2021-03-26T09:05:01Z
alluxioruntimes.data.fluid.io                    2021-03-26T09:05:01Z
databackups.data.fluid.io                        2021-03-26T09:05:01Z
dataloads.data.fluid.io                          2021-03-26T09:05:01Z
datasets.data.fluid.io                           2021-03-26T09:05:01Z
jindoruntimes.data.fluid.io                      2021-03-31T02:30:29Z
```


例如更新系统中已有的 `jindoruntime` 的crd，首先删除已有的 crd

```shell
kubectl delete crd jindoruntimes.data.fluid.io
```

解压 `fluid-0.6.0.tgz`

```shell
$ ls -l fluid/
total 32
-rw-r--r--  1 frank  staff   489B  3 31 10:27 CHANGELOG.md
-rw-r--r--  1 frank  staff   270B  3 31 10:27 Chart.yaml
-rw-r--r--  1 frank  staff   2.1K  3 31 10:27 VERSION
drwxr-xr-x  8 frank  staff   256B  3 31 10:29 crds
drwxr-xr-x  6 frank  staff   192B  3 31 10:29 templates
-rw-r--r--  1 frank  staff   1.2K  3 31 13:18 values.yaml
```

创建新的crd

```shell
kubectl apply -f crds/data.fluid.io_jindoruntimes.yaml
```
