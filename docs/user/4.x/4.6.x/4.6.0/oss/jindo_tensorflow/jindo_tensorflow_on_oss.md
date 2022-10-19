# 使用 Jindo Tensorflow Collector 访问 OSS

Jindo Tensorflow Collector 支持使用 Tensoflow 原生 API 支持访问 OSS。

### 1. 下载 JindoSDK 包
下载最新的 tar.gz 包 jindosdk-x.x.x.tar.gz ([下载页面](/docs/user/4.x/jindodata_download.md))。

## 2. 修改配置

### 2.1 添加环境变量
```
export LD_LIBRARY_PATH="/path/to/jindosdk-x.x.x/lib/native":$LD_LIBRARY_PATH
export JINDOSDK_CONF_DIR=/path/to/jindosdk-x.x.x/conf
```

### 2.2 配置文件
使用 INI 风格配置文件，配置文件的文件名为`jindosdk.cfg`，示例如下：

```
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# 已创建的Bucket对应的Endpoint。以华东1（杭州）为例，填写为oss-cn-hangzhou.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
# 用于访问OSS的AccessKey ID和AccessKey Secret。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```
更多 jindosdk 配置节参数可见[相关文档](../configuration/jindosdk_configuration_list_ini.md)。

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
