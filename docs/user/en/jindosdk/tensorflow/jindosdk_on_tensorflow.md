# Using Jindo TensorFlow Collector to Access OSS

Jindo TensorFlow Collector enables access to Alibaba Cloud Object Storage Service (OSS) using TensorFlow's native APIs.

## Step 1: Download the JindoSDK Package

Download the latest tar.gz package `jindosdk-x.y.z.tar.gz` (replace x.y.z with the actual version) from the [download page](../jindosdk_download.md). Follow the [deployment documentation](../jindosdk_deployment_ai.md) for installation.

## Step 2: Modify Configuration

### 2.1 Install TensorFlow

Currently, only TensorFlow versions 1.15 and 2.8 are supported. Here's an example for installing TensorFlow 2.8:

```bash
pip3 install tensorflow==2.8
```

### 2.2 Set Environment Variables

#### 2.2.1 JindoSDK Environment Variable

Ensure that the `LD_LIBRARY_PATH` includes `/path/to/jindosdk-x.x.x/lib/native`.

#### 2.2.2 TensorFlow Dependency Environment Variable

Add TensorFlow's dependency path to `LD_LIBRARY_PATH`. The path may vary depending on your Python environment, e.g.,:

```bash
# For TensorFlow 1.15
export LD_LIBRARY_PATH="/usr/local/lib/python3.9/site-packages/tensorflow/":$LD_LIBRARY_PATH
# For TensorFlow 2.8
export LD_LIBRARY_PATH="/usr/local/lib/python3.9/site-packages/tensorflow/":$LD_LIBRARY_PATH
```

### 2.3 Configuration File

Use an INI-style configuration file named `jindosdk.cfg`. There are two authentication methods available – fixed AK (Access Key) or IAM role-based access.

#### 2.3.1 Fixed AK Access

Example configuration:

```
[common]
logger.dir = /tmp/tfio-log

[jindosdk]
fs.oss.endpoint = <your_endpoint>
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```

**Configuration Options**

| Option                    | Default | Section | Description                                                                                           |
|---------------------------|---------|---------|-------------------------------------------------------------------------------------------------------|
| logger.dir                | /tmp/tfio-log | common | Log directory; created if it does not exist. |
| logger.sync               | false   | common | Synchronous log output if true, asynchronous otherwise. |
| logger.consolelogger      | false   | common | Print logs to console if true. |
| logger.level              | 2       | common | Log level >= this value is printed.<br/>When consolelogger is active, levels range from TRACE (0) to OFF (6). When disabled, levels <= WARN (1) or >= INFO (2) are printed. |
| logger.verbose            | 0       | common | VERBOSE log level >= this value is printed; range 0-99. |
| logger.cleaner.enable     | false   | common | Enable log cleaning. |
| fs.oss.endpoint           |         | jindosdk | Endpoint of the created Bucket, e.g., oss-cn-hangzhou.aliyuncs.com. |
| fs.oss.accessKeyId       |         | jindosdk | Access Key ID for accessing OSS-HDFS. |
| fs.oss.accessKeySecret    |         | jindosdk | Access Key Secret for accessing OSS-HDFS. |

See the [related documentation](../jindosdk_configuration.md) for more jindosdk section parameters.

#### 2.3.2 IAM Role-Based Access (ECS)

Prerequisite: You must be using an Alibaba Cloud Elastic Compute Service (ECS) instance with an attached role.

Example configuration:

```
[common]
logger.dir = /tmp/tensorflow-log

[jindosdk]
fs.oss.endpoint = <your_endpoint>
fs.oss.provider.endpoint = ECS_ROLE
```

## Step 3: Use Jindo TensorFlow

### 3.1 Load the Collector Library

```python
import tensorflow as tf

tf.load_library("/path/to/jindosdk-x.x.x/lib/native/libjindo-tensorflow2.8.so")
```

### 3.2 Access OSS-HDFS via GFile

To avoid conflicts with TFIO's oss prefix, Jindo TensorFlow uses the joss prefix.

```python
# Directory operations
tf.io.gfile.mkdir("joss://<bucket>/test_mkdir")
content = tf.io.gfile.listdir("joss://<bucket>/test_mkdir")

# Reading
gmnist = tf.io.gfile.GFile("joss://<bucket>/mnist.npz", "rb")
gmnist_file = gmnist.read()

# Writing
fh = tf.io.gfile.GFile("joss://<bucket>/test.txt", mode="w")
content = "file content"
fh.write(content)
fh.close()
```

### 3.3 Read TFRecordDataset

```python
tfRecordOssPath = "joss://<bucket>/test.tfrecord"
filenames = [tfRecordOssPath]
raw_dataset = tf.data.TFRecordDataset(filenames)
```

### 3.4 Complete Example

```python
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
In this example, we load MNIST data from an OSS location, create a simple neural network model, and train it using the loaded dataset.