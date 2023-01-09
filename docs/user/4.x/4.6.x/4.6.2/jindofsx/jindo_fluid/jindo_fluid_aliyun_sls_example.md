# 使用阿里云 sls 日志收集服务

JindoRuntime 默认日志打在容器 std 输出里面，可以使用阿里云 sls 日志收集服务将日志收集起来。

## 1、ack 集群绑定 sls 服务
您可以在创建集群时绑定 sls 服务

<img src="../pic/jindo_fluid_sls_example_1.png">

也可以在创建集群后绑定阿里云 sls 服务

* 选择集群信息 > 集群资源 > 日志服务 Project

## 2、创建 JindoRuntime 集群

可参考文章 [加速OSS上数据](./jindo_fluid_oss_ufs_example.md) 创建 JindoRuntime 集群。

## 3、配置 sls 收集

您可以选择在 ack 控制台操作，或者直接操作集群 K8S 资源

### ack 控制台操作

您可以登陆 ack 控制台 > 工作负载
* 有状态（master/worker）
<img src="../pic/jindo_fluid_sls_ack_1.png">

* 守护进程集（fuse）
<img src="../pic/jindo_fluid_sls_ack_2.png">

* 点击**编辑**


* 添加日志收集
<img src="../pic/jindo_fluid_sls_ack_3.png">

其中日志库名称可自定义填写，该名称即为 sls 上日志库的名字，容器内日志路径为**stdout**

* 点击右侧更新
<img src="../pic/jindo_fluid_sls_ack_4.png">

### 直接操作 K8S 资源
* 以收集 fuse 日志为例，执行
```shell
kubectl edit daemonset test-jindofs-fuse
```
修改 **spec.env**
<img src="../pic/jindo_fluid_sls_ack_5.png">
增加
```yaml
- name: aliyun_logs_jindo-fuse
  value: stdout
```
name 格式为：**aliyun_logs_<logstore_name>**，前缀为 aliyun_logs_ <br/>
value 为固定值 stdout

* 如果想修改 master/worker，可执行
```shell
kubectl edit statefulset test-jindofs-master
```
```shell
kubectl edit statefulset test-jindofs-worker
```
进行对应的编辑

### 4、登陆阿里云 sls 日志服务查看相关日志情况
以 jindofuse 日志收集为例，日志库名称为 **jindo-fuse**,在sls上找到对应的日志库

<img src="../pic/jindo_fluid_sls_ack_6.png">