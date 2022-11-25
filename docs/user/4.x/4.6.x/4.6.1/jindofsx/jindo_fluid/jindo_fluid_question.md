# 问题诊断及处理

如您的环境出现问题，可开 [ISSUE](https://github.com/aliyun/alibabacloud-jindodata/issues) 给我们，会第一时间处理

##
您可能会在部署、开发Fluid的过程中遇到各种问题，而查看日志可以协助我们定位问题原因。但在分布式环境下，Fluid底层的分布式缓存引擎（Runtime）运行在不同主机的容器上，手动收集这些容器的日志效率低下。
因此，Fluid提供了shell脚本，帮助使用者快速收集Fluid系统和Runtime容器的日志信息。

## 如何使用脚本收集日志

1. 下载诊断脚本

   ```shell
   # JindoFS:
   wget https://raw.githubusercontent.com/fluid-cloudnative/fluid/master/tools/diagnose-fluid-jindo.sh
   ```

2. 确保shell脚本有运行权限
    ```bash
    $ chmod a+x diagnose-fluid-jindo.sh
    ```
   
3. 查看帮助信息

    ```bash
    $ ./diagnose-fluid-jindo.sh
    Usage:
        ./diagnose-fluid-jindo.sh COMMAND [OPTIONS]
    COMMAND:
        help
            Display this help message.
        collect
            Collect pods logs of controller and runtime.
    OPTIONS:
        -r, --name name
            Set the name of runtime.
        -n, --namespace name
            Set the namespace of runtime.
    ```

4. 收集日志信息

    运行`diagnose-fluid-jindo.sh`，`--name`指定了Runtime的name，`--namespace`指定了Runtime的namespace
    
    ```bash
    $ ./diagnose-fluid-jindo.sh collect --name hadoop --namespace default
    ```
    
    shell脚本会将收集的日志信息打包到执行路径下的一个压缩包里，解压该压缩包即可得到相关日志信息和yaml文件


## Fluid 安装使用相关问题

### 1. 为什么我使用Helm安装fluid失败了？

**回答**:推荐按照安装文档依次确认Fluid组件是否正常运行。

Fluid安装文档是以`Helm 3`为例进行部署的。如果您使用`Helm 3`以下的版本部署Fluid，
并且遇到了`CRD没有正常启动`的情况，这可能是因为`Helm 3`及其以上版本会在`helm install`的时候自动安装CRD，
而低版本的Helm则不会。
参见[Helm官方文档说明](https://helm.sh/docs/chart_best_practices/custom_resource_definitions/)。

在这种情况下，您需要手动安装CRD：
```bash
$ kubectl create -f fluid/crds
```

### 2. 为什么我无法删除Runtime？

**回答**:请检查相关Pod运行状态和Runtime的Events。

只要有任何活跃Pod还在使用Fluid创建的Volume，Fluid就不会完成删除操作。

如下的命令可以快速地找出这些活跃Pod，使用时把`<dataset_name>`和`<dataset_namespace>`换成自己的即可：
```bash
kubectl describe pvc <dataset_name> -n <dataset_namespace> | \
	awk '/^Mounted/ {flag=1}; /^Events/ {flag=0}; flag' | \
	awk 'NR==1 {print $3}; NR!=1 {print $1}' | \
	xargs -I {} kubectl get po {} | \
	grep -E "Running|Terminating|Pending" | \
	cut -d " " -f 1
```

### 3. 为什么我在创建任务挂载 Runtime 创建的 PVC 的时候出现 `driver name fuse.csi.fluid.io not found in the list of registered CSI drivers` 错误？

**回答**:请查看任务被调度节点所在的 kubelet 配置是否为默认`/var/lib/kubelet`。

首先通过命令查看Fluid的CSI组件是否正常

如下的命令可以快速地找出Pod，使用时把`<node_name>`和`<fluid_namespace>`换成自己的即可：
```bash
kubectl get pod -n <fluid_namespace> | grep <node_name>

# <pod_name> 为上一步pod名
kubectl logs <pod_name> node-driver-registrar -n <fluid_namespace>
kubectl logs <pod_name> plugins -n <fluid_namespace>
```

如果上述步骤的Log无错误，请查看csidriver对象是否存在：
```
kubectl get csidriver
```
如果csidriver对象存在，请查看查看csi注册节点是否包含`<node_name>`：
```
kubectl get csinode | grep <node_name>
```
如果上述命令无输出，查看任务被调度节点所在的 kubelet 配置是否为默认`/var/lib/kubelet`。因为Fluid的CSI组件通过固定地址的socket注册到kubelet，默认为`--csi-address=/var/lib/kubelet/csi-plugins/fuse.csi.fluid.io/csi.sock --kubelet-registration-path=/var/lib/kubelet/csi-plugins/fuse.csi.fluid.io/csi.sock`。


### 4. 为什么更新了fluid后，使用 `kubectl get` 查询更新前创建的dataset，发现相比新建的dataset缺少了某些字段？

**回答**:由于我们在fluid的升级过程中可能更新了CRD，你在旧版本创建的dataset，会将CRD中新增的字段设置为空
例如，如果你从v0.4或更早版本升级，那时候的dataset没有FileNum字段
更新fluid后，如果你使用 `kubectl get` 命令，无法查询到该dataset的FileNum

你可以重建dataset，新建的dataset会正常显示这些字段

### 5. 为什么在应用程序中使用 PVC 时会产生了 Volume Attachment 超时问题？
**回答**:Volume Attachment 超时问题是 Kubelet 进行请求 CSI Driver 时未收到 CSI Driver 的响应而造成的超时。
该问题是由于 Fluid 的 CSI Driver 没有安装，或者kubelet没有访问 CSI Driver 的权限导致的。
由于 CSI Driver 是由 Kubelet 进行回调，但是如果 Fluid 没有安装 CSI Driver 或者 Kubelet 没有权限查看 CSI Driver，就会导致 CSI Plugin 没有被正确触发。

首先需要使用命令`kubectl get csidriver`查看是否安装了 CSI Driver。
如果没有安装，使用命令`kubectl apply -f charts/fluid/fluid/templates/csi/driver.yaml`进行安装，然后观察 PVC 是否成功挂载到应用程序中。
如果仍未能解决，使用`export KUBECONFIG=/etc/kubernetes/kubelet.conf && kubectl get csidriver`来查看 Kubelet 能够具有权限看到 CSI Driver
