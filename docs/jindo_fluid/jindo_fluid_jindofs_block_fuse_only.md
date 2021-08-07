## JindoRuntime 支持文件列表缓存

### 1、更新 Fluid 和 CRD 版本
1、下载 [fluid-0.6.0.tgz](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/FuseOnly/fluid-0.6.0.tgz)

2、更新 Fluid 组建
```shell
helm install fluid fluid-0.6.0.tgz
```

### 2、创建 dataset 和 JindoRuntime
创建 resource.yaml

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
stringData:
  AccessKeyId: xxx
  AccessKeySecret: xxx
---
apiVersion: data.fluid.io/v1alpha1
kind: Dataset
metadata:
  name: hadoop
spec:
  mounts:
    - mountPoint: <IP1>:8101,<IP2>:8101,<IP3>:8101
      name: hadoop
---
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 1
  secret: mysecret
  fuse:
    global: true
```
其中：
* AccessKeyId 和 AccessKeySecret 为集群后端 OSS 访问 AK信息，必填项。
* mountPoint：JindoFS Block 模式集群后端访问地址，格式为：NameSpace服务IP地址+端口模式，端口默认为8101，建议 K8S 集群和 Block 模式集群在同一个 VPC 下，IP 地址可为内网 IP，K8S 集群所在节点必须可以访问 Block 模式集群 NameSpace 节点。
* fuse.global：全局部署 fuse 客户端
  
创建资源文件
```shell
kubectl create -f resource.yaml
```

```shell
kubectl get pod | grep fuse
NAME                        READY   STATUS    RESTARTS   AGE
hadoop-jindofs-fuse-87t4l   1/1     Running   0          43s
hadoop-jindofs-fuse-dlxr5   1/1     Running   0          43s
hadoop-jindofs-fuse-kklpj   1/1     Running   0          43s
```

验证挂载成功
```shell
kubectl exec -ti hadoop-jindofs-fuse-87t4l bash
root@iZbp13oy0cp0ic6kbdmf8nZ:/# ls /jfs/test/
a.txt
```
其中 test 为后端挂载 Block 模式的 ns 的名称