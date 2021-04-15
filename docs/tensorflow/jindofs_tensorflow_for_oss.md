# 使用 JindoFS Tensorflow 访问 OSS

当您已经希望直接从本地通过Tensorflow API读取OSS数据时，可以使用本方法。

## 前提条件

1. 已经创建了一个OSS bucket，并拥有能读写该bucket的AK信息。
2. 下载了最新版本的 jindofs-tensorflow 。

## 1. 下载安装包
下载最新的release包 jindofs-tensorflow-x.x.x.tar.gz ([下载页面](/docs/jindofs_sdk_download.md))，并解压。

```
mkdir -p /path/to/jindofs-tensorflow-x.x.x/
tar xzvf jindofs-tensorflow-x.x.x.tar.gz -C /path/to/jindofs-tensorflow-x.x.x/
```

关于版本兼容性。JindoFS Tensorflow 依赖的 Jindo SDK 版本和JindoFS服务端之间跨小版本保持相互兼容，跨大版本不一定保证兼容。大版本为前两位，例如3.1.x。您可以从E-MapReduce集群的/usr/lib/b2jindosdk-current/lib/目录，获取与服务端版本完全相同的fuse程序。

## 2. 设置环境变量
```
export OSS_ACCESS_ID=<your_oss_access_id>
export OSS_ACCESS_KEY=<you_oss_access_key>
export LD_LIBRARY_PATH="/path/to/jindofs-tensorflow-x.x.x/":$LD_LIBRARY_PATH
```

## 3. 使用 JindoFs Tensorflow

以加载jindofs-tensorflow2.3

### 3.1 通过 load_library 添加 collector

```
import tensorflow as tf

tf.load_library("/path/to/libjindofs-tensorflow2.3.so")
```

### 3.2 通过 gfile 访问 jindofs

```
# 目录操作
tf.io.gfile.mkdir("joss://bucket.")
content = tf.io.gfile.listdir("joss://bucket/path/to")

# 读
gmnist = tf.io.gfile.GFile("joss://bucket/path/to/mnist.npz", "rb")
gmnist_file = gmnist.read()

# 写
fh = gfile.Open(f, mode="w")
content = "file content"
fh.write(content)
fh.close()
```

### 3.3 读取TFRecordDataset

```
tfRecordJfsPath = "joss://bucket/path/to/test.tfrecord"
filenames = [tfRecordJfsPath]
raw_dataset = tf.data.TFRecordDataset(filenames)
```

#### 3.4 完整示例

```
import tensorflow as tf
import numpy as np

tf.load_library("/path/to/libjindofs-tensorflow2.3.so")

gmnist = tf.io.gfile.GFile("joss://bucket/path/to/mnist.npz", 'rb')
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

