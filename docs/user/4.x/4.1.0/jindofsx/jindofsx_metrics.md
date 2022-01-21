## 基于 Prometheus + Grafana 搭建 JindoFSx 缓存系统可视化指标观测平台
### 环境要求

* JindoFSx >= 4.1.0

* Prometheus >= 2.10
 
* Grafana >= 7.3.7

### 前提条件：
* 已部署 JindoFSx 缓存系统。

关于如何部署 JindoFSx 缓存系统，请参考 [部署 JindoFSx 缓存系统](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindofsx.md)

* 已部署 JindoSDK。

关于如何部署 JindoSDK，请参考 [部署 JindoSDK](/docs/user/4.x/4.1.0/jindofsx/deploy/deploy_jindosdk.md)

### 1、Prometheus 安装
首先需要在环境里安装 Prometheus 和 Grafana 服务，下面以 centos7 环境为例
* [Prometheus 相关介绍](https://prometheus.io/)
* [Grafana 相关介绍](https://grafana.com/)

1、安装 Prometheus 服务

```bash
#下载安装包
wget https://github.com/prometheus/prometheus/releases/download/v2.30.1/prometheus-2.30.1.linux-amd64.tar.gz
#解压安装包
tar -zxvf prometheus-2.30.1.linux-amd64.tar.gz 
#重命名
mv prometheus-2.30.1.linux-amd64 prometheus-server
```

2、修改解压安装包内的 `prometheus.yml` 文件，替换为

```yaml
global:
  scrape_interval:     15s # By default, scrape targets every 15 seconds.

  # Attach these labels to any time series or alerts when communicating with
  # external systems (federation, remote storage, Alertmanager).
  external_labels:
    monitor: 'codelab-monitor'

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'namespace-prometheus'
    scrape_interval: 5s
    metrics_path: /metrics/prometheus
    static_configs:
      - targets: ['<ns Ip>:<ns Port>'] # your ns Ip and ns port eg:localhost:8101
```

简单使用您只需填写`targets: ['<ns Ip>:<ns Port>']`即可：
如 `targets: ['localhost:8101']`，保证网络和端口可访问

3、启动 prometheus 服务
```bash
#安装目录下
cd prometheus-server
#后台启动server
nohup ./prometheus > myout.file 2>&1 &
```
prometheus 默认启动在9090端口
```bash
$ netstat -ntlp | grep prometheus
tcp        0      0 0.0.0.0:9090            0.0.0.0:*               LISTEN      17448/./prometheus
```
从浏览器打开`prometheus`UI界面

<img src="pic/prometheus_example.png">

到此，prometheus 服务安装成功，接下来我们通过 Grafana 服务来对接 Prometheus 数据源进行 metrics 的展示工作

### 2、Grafana 安装
1、下载安装 Grafana 服务
```bash
# 下载 grafana 7.3.7
wget https://dl.grafana.com/enterprise/release/grafana-enterprise-7.3.7.linux-amd64.tar.gz
# 解压
tar -zxvf grafana-enterprise-7.3.7.linux-amd64.tar.gz
```
2、启动 grafana 服务 

最小化启动 grafana 服务，其他选项可参考 grafana 官方文档

```bash
cd grafana-7.3.7/bin
```
```bash
# ls -l
总用量 85416
-rwxr-xr-x 1 root root 25691632 1月  14 2021 grafana-cli
-rw-r--r-- 1 root root       33 1月  14 2021 grafana-cli.md5
-rwxr-xr-x 1 root root 61760192 1月  14 2021 grafana-server
-rw-r--r-- 1 root root       33 1月  14 2021 grafana-server.md5
```
后台启动 grafana 服务
```bash
nohup ./grafana-server > myout.file 2>&1 &
```
打开 grafana UI 界面，默认 `3000` 端口
```bash
$ netstat -ntlp | grep grafana
tcp        0      0 0.0.0.0:3000            0.0.0.0:*               LISTEN      27223/./grafan-ser 
```
如打开 `localhost:3000`，初始化用户名/密码：admin/admin，点击 login 登陆

<img src="pic/grafana-login.png">

添加 prometheus 数据源

<img src="pic/grafana-1.png">

<img src="pic/grafana-2.png">

填写 Prometheus 的 HTTP URL，进行 Save&Test，测试通过即为添加成功

<img src="pic/grafana-3.png">

添加 JindoFS cache 模式的 JSON 展示模版，[点击这里下载 JSON 文件](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/370/Jindofsx-grafana-json)，下载到本地后，点击 `Upload JSON file`

<img src="pic/grafana-5.png">
<img src="pic/grafana-6.png">
<img src="pic/grafana-7.png">

点击 import 后即可看到 cache 模式的 UI

<img src="pic/common_grafana_playfront.png">


### Prometheus 指标预览(更新中)
下面对 JindoRuntime 当前收集的指标进行预览和描述，您可以基于已收集指标在 Grafana 中进行组合和图表展示，如您需要额外的基本 metrics 展示，请开 issue 联系我们
```yaml
# HELP jindofsx_ns_backend_read_bytes_time_total_window
# TYPE jindofsx_ns_backend_read_bytes_time_total_window gauge
jindofsx_ns_backend_read_bytes_time_total_window 20367553  #一分钟内底层读取总数据量大小所用时间大小，单位微秒
# HELP jindofsx_ns_backend_read_bytes_total
# TYPE jindofsx_ns_backend_read_bytes_total gauge
jindofsx_ns_backend_read_bytes_total 166703347021 #总底层读取数据量大小，单位Byte
# HELP jindofsx_ns_backend_read_bytes_total_window
# TYPE jindofsx_ns_backend_read_bytes_total_window gauge
jindofsx_ns_backend_read_bytes_total_window 1174671374 #一分钟内底层读取总数据量大小，单位Byte
# HELP jindofsx_ns_backend_read_time_total
# TYPE jindofsx_ns_backend_read_time_total gauge
jindofsx_ns_backend_read_time_total 8717632094 #底层读取数据所用总时间大小，单位微秒
# HELP jindofsx_ns_backend_readop_num_total
# TYPE jindofsx_ns_backend_readop_num_total gauge
jindofsx_ns_backend_readop_num_total 181951 #底层总读取数据次数，对应 JindoFS 中 Block 个数
# HELP jindofsx_ns_local_read_bytes_time_total_window
# TYPE jindofsx_ns_local_read_bytes_time_total_window gauge
jindofsx_ns_local_read_bytes_time_total_window 0 #一分钟内短路读时间大小，单位微秒
# HELP jindofsx_ns_local_read_bytes_total
# TYPE jindofsx_ns_local_read_bytes_total gauge
jindofsx_ns_local_read_bytes_total 0  #总短路读取数据量大小，单位Byte
# HELP jindofsx_ns_local_read_bytes_total_window
# TYPE jindofsx_ns_local_read_bytes_total_window gauge
jindofsx_ns_local_read_bytes_total_window 0 #一分钟内短路读所用时间大小，单位微秒
# HELP jindofsx_ns_local_read_time_total
# TYPE jindofsx_ns_local_read_time_total gauge
jindofsx_ns_local_read_time_total 0 #短路读取数据所用总时间大小，单位微秒
# HELP jindofsx_ns_local_readop_num_total
# TYPE jindofsx_ns_local_readop_num_total gauge
jindofsx_ns_local_readop_num_total 0 #短路读总读取数据次数，对应 JindoFS 中 Block 个数
# HELP jindofsx_ns_remote_read_bytes_time_total_window
# TYPE jindofsx_ns_remote_read_bytes_time_total_window gauge
jindofsx_ns_remote_read_bytes_time_total_window 73714663 #一分钟内远端读所用时间大小，单位微秒
# HELP jindofsx_ns_remote_read_bytes_total
# TYPE jindofsx_ns_remote_read_bytes_total gauge
jindofsx_ns_remote_read_bytes_total 112108045498 #总远端读取数据量大小，单位Byte
# HELP jindofsx_ns_remote_read_bytes_total_window
# TYPE jindofsx_ns_remote_read_bytes_total_window gauge
jindofsx_ns_remote_read_bytes_total_window 13380884043 #一分钟内远端读取总数据量大小，单位Byte
# HELP jindofsx_ns_remote_read_time_total
# TYPE jindofsx_ns_remote_read_time_total gauge
jindofsx_ns_remote_read_time_total 765272359 #总远端读取时间，单位微秒
# HELP jindofsx_ns_remote_readop_num_total
# TYPE jindofsx_ns_remote_readop_num_total gauge
jindofsx_ns_remote_readop_num_total 107314 #远端读总读取数据次数，对应 JindoFS 中 Block 个数
# HELP jindofsx_ns_slicelet_read_bytes_time_total_window
# TYPE jindofsx_ns_slicelet_read_bytes_time_total_window gauge
jindofsx_ns_slicelet_read_bytes_time_total_window 0 #一分钟内小文件读所用时间大小，单位微秒
# HELP jindofsx_ns_slicelet_read_bytes_total
# TYPE jindofsx_ns_slicelet_read_bytes_total gauge
jindofsx_ns_slicelet_read_bytes_total 0 #总小文件读取数据量大小，单位Byte
# HELP jindofsx_ns_slicelet_read_bytes_total_window
# TYPE jindofsx_ns_slicelet_read_bytes_total_window gauge
jindofsx_ns_slicelet_read_bytes_total_window 0 #一分钟内小文件读取总数据量大小，单位Byte
# HELP jindofsx_ns_slicelet_read_time_total
# TYPE jindofsx_ns_slicelet_read_time_total gauge
jindofsx_ns_slicelet_read_time_total 0 #小文件读取数据所用总时间大小，单位微秒
# HELP jindofsx_ns_slicelet_readop_num_total
# TYPE jindofsx_ns_slicelet_readop_num_total gauge
jindofsx_ns_slicelet_readop_num_total 0 #小文件读总读取数据次数，对应 JindoFS 中 Block 个数
# HELP jindofsx_ns_total_disk_cap
# TYPE jindofsx_ns_total_disk_cap gauge
jindofsx_ns_total_disk_cap 840739848192 #磁盘缓存总容量大小，单位Byte
# HELP jindofsx_ns_total_mem_cap
# TYPE jindofsx_ns_total_mem_cap gauge
jindofsx_ns_total_mem_cap 0 #内存缓存总容量大小，单位Byte
# HELP jindofsx_ns_total_stsnodes_num
# TYPE jindofsx_ns_total_stsnodes_num gauge
jindofsx_ns_total_stsnodes_num 3 #alive worker 节点的数量
# HELP jindofsx_ns_total_used_disk_cap
# TYPE jindofsx_ns_total_used_disk_cap gauge
jindofsx_ns_total_used_disk_cap 153423446016 #已用磁盘缓存容量大小，单位Byte
# HELP jindofsx_ns_total_used_mem_cap
# TYPE jindofsx_ns_total_used_mem_cap gauge
jindofsx_ns_total_used_mem_cap 0 #已用内存缓存容量大小，单位Byte
```
