# 使用 Jindo Tensorflow Collector 访问 OSS

Jindo Tensorflow Collector 支持使用 Tensoflow 原生 API 支持访问 OSS。

### 1. 下载 JindoSDK 包

下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/6.x/6.3.0/jindodata_download.md))。

部署方式，参见[部署文档](/docs/user/6.x/jindosdk/jindosdk_deployment_ai.md)

## 2. 修改配置

### 2.1 安装 tensorflow
目前仅支持 tensorflow 1.15 或 tensorflow 2.8 版本，以下以 tensorflow 2.8 为例。
```
pip3 install tensorflow==2.8
```

### 2.2 添加环境变量

#### 2.2.1 JindoSDK 环境变量

确认 LD_LIBRARY_PATH 包含 /path/to/jindosdk-x.x.x/lib/native

#### 2.2.2 依赖的 Tensorflow 环境变量

同时，LD_LIBRARY_PATH 需要添加 tensorflow 所在的依赖路径，不同的 python 环境安装路径可能不同，如：


```bash
# tensorflow1.15
export LD_LIBRARY_PATH="/usr/local/lib/python3.9/site-packages/tensorflow/":$LD_LIBRARY_PATH
# tensorflow2.8
export LD_LIBRARY_PATH="/usr/local/lib/python3.9/site-packages/tensorflow/":$LD_LIBRARY_PATH
```

### 2.3 配置文件
使用 INI 风格配置文件，配置文件的文件名为`jindosdk.cfg`。可以选择固定或者免密两种配置方式。

#### 2.3.1 固定 AK 访问
示例如下：

```
[common]
logger.dir = /tmp/tfio-log

[jindosdk]
# 已创建的Bucket对应的Endpoint。以华东1（杭州）为例，填写为oss-cn-hangzhou.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
# 用于访问OSS的AccessKey ID和AccessKey Secret。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```

## 配置选项

| 配置项                  | 默认值            | 配置节    | 说明                                                         |
| ---------------------- | ---------------- | -------- |------------------------------------------------------------ |
| logger.dir             | /tmp/tfio-log    | common   | 日志目录，不存在会创建                                       |
| logger.sync            | false            | common   | 是否同步输出日志，false表示异步输出                          |
| logger.consolelogger   | false            | common   | 打印日志到终端                                               |
| logger.level           | 2                | common   |输出大于等于该等级的日志。<br/>开启终端日志时，日志等级范围为0-6，分别表示：TRACE、DEBUG、INFO、WARN、ERROR、CRITICAL、OFF。<br/>关闭终端日志，使用文件日志时：日志等级<=1，表示WARN；日志等级>1，表示INFO。 |
| logger.verbose         | 0                | common   | 输出大于等于该等级的VERBOSE日志，等级范围为0-99，0表示不输出 |
| logger.cleaner.enable  | false            | common   | 是否开启日志清理                                             |
| fs.oss.endpoint        |                  | jindosdk | 访问 OSS-HDFS 服务的地址，如cn-xxx.oss-dls.aliyuncs.com       |
| fs.oss.accessKeyId     |                  | jindosdk | 访问 OSS-HDFS 服务需要的 accessKeyId                          |
| fs.oss.accessKeySecret |                  | jindosdk | 访问 OSS-HDFS 服务需要的 accessKeySecret                      |

更多 jindosdk 配置节参数可见[相关文档](../configuration/jindosdk_configuration_list_ini.md)。

#### 2.3.2 免密访问
前提：使用的是阿里云 ECS，并且该机器已绑定过角色授权。
示例如下：

```
[common]
logger.dir = /tmp/tensorflow-log

[jindosdk]
# 已创建的Bucket对应的Endpoint。以华东1（杭州）为例，填写为oss-cn-hangzhou.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
fs.oss.provider.endpoint = ECS_ROLE
```

## 3. 使用 Jindo Tensorflow

以加载 jindo-tensorflow2.8 为例

### 3.1 通过 load_library 添加 collector

```
import tensorflow as tf

tf.load_library("/path/to/jindosdk-x.x.x/lib/native/libjindo-tensorflow2.8.so")
```

### 3.2 通过 gfile 访问 oss-hdfs

为避免与 tfio 中的 oss 前缀冲突，jindo-tensorflow 使用 joss 前缀。

```
# 目录操作
tf.io.gfile.mkdir("joss://<bucket>/test_mkdir")
content = tf.io.gfile.listdir("joss://<bucket>/test_mkdir")

# 读
gmnist = tf.io.gfile.GFile("joss://<bucket>/mnist.npz", "rb")
gmnist_file = gmnist.read()

# 写
fh = gfile.Open(f, mode="w")
content = "file content"
fh.write(content)
fh.close()
```

### 3.3 读取 TFRecordDataset

```
tfRecordOssPath = "joss://<bucket>/test.tfrecord"
filenames = [tfRecordOssPath]
raw_dataset = tf.data.TFRecordDataset(filenames)
```

#### 3.4 完整示例

```
import tensorflow as tf
import numpy as np

tf.load_library("/path/to/jindosdk-x.x.x/lib/native/libjindo-tensorflow2.8.so")

gmnist = tf.io.gfile.GFile("joss://<bucket>/mnist.npz", 'rb')
print(gmnist.size())

with np.load(gmnist) as f:
    x_train, y_train = f['x_train'], f['y_train']
    x_test, y_test = f['x_test'], f['y_test']
    model = tf.keras.models.Sequential([
       tf.keras.layers.Flatten(input_shape=(28, 28)),
       tf.keras.layers.Dense(128, activation='relu'),
       tf.keras.layers.Dropout(0.2),
       tf.keras.layers.Dense(10, activation='softmax')
    ])
    model.compile(optimizer='adam',
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])
    model.fit(x_train, y_train, epochs=5)
```
