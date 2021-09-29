JindoFS cache 模式 metrics 收集展示文档
### 环境要求

* Smartdata 3.7.2

* Prometheus >= 2.10
 
* Grafana >= 7.3.7

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

2、修改默认 `prometheus.yml`
修改解压安装包内的 `prometheus.yml` 文件，替换为

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
    metrics_path: /jindometrics/prometheus
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

<img src="../pic/prometheus_example.png">

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

<img src="../pic/grafana-login.png">

添加 prometheus 数据源

<img src="../pic/grafana-1.png">

<img src="../pic/grafana-2.png">

填写 Prometheus 的 HTTP URL，进行 Save&Test，测试通过即为添加成功

<img src="../pic/grafana-3.png">

添加 JindoFS cache 模式的 JSON 展示模版，[点击这里下载 JSON 文件](http://smartdata-binary.oss-cn-shanghai.aliyuncs.com/fluid/370/fluid-jindo-prometheus-grafana-monitor.json)，下载到本地后，点击 `Upload JSON file`

<img src="../pic/grafana-5.png">
<img src="../pic/grafana-6.png">
<img src="../pic/grafana-7.png">

点击 import 后即可看到 cache 模式的 UI

<img src="../pic/grafana-8.png">