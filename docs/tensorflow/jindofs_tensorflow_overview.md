# JindoFS Tensorflow

## 环境要求

* Linux操作系统。

* Python 3.6 及以上

* Tensorflow 1.15+ 或 Tensorflow 2.3+， 关于 Tensorflow 版本兼容性说明，请参考[官方文档](https://www.tensorflow.org/guide/versions?hl=zh-cn)。

## 什么是 JindoFS Tensorflow

JindoFS Tensorflow是一个面向tensorflow的插件，通过实现Tensorflow Filesystem，支持原生的Tensorflow IO接口。通过它您可以：

1. 直接访问OSS
2. 访问 JindoFS Block/Cache 模式集群

## 为什么 Tensorflow 需要使用 JindoFS SDK

* JindoFS Tensorflow，在访问OSS的能力上，相对于开源的[TensorFlow OSS Filesystem Extension](https://github.com/tensorflow/io/tree/master/tensorflow_io/core/kernels/oss)做了很多的性能优化。
* JindoFS Fuse还可以访问JindoFS集群上的缓存数据，利用缓存加速数据访问性能。
* 关于在性能上的优势，我们做了对比测试，可以参考[JindoFS Tensorflow 性能测试](./jindofs_tensorflow_benchmark.md)。

## 如何使用

访问 OSS 以 joss:// 为前缀，访问 JindoFS 以 jfs:// 为前缀。具体使用方法及示例参考：

* [Tensorflow 使用 JindoFS SDK 访问 OSS](/docs/tensorflow/jindofs_tensorflow_for_oss.md)

* [Tensorflow 使用 JindoFS SDK 访问 JindoFS](/docs/tensorflow/jindofs_tensorflow_for_jfs.md)