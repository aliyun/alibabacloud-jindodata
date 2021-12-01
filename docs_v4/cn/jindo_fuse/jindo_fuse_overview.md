## JindoFUSE

### 环境要求
* 目前 JindoFUSE 只支持 Linux 操作系统。大多数 Linux 发行版已经内置了 FUSE 模块，没有的话您需要安装或者编译 FUSE 模块。

### 什么是 JindoFUSE

FUSE 是 Linux 系统内核提供的一种挂载文件系统的方式。

通过 JindoFUSE 客户端，将对象存储 OSS 服务或 DLS 集群上的文件挂载到本地文件系统中，您能够像操作本地文件一样操作 OSS、DLS 集群上的文件。


### 为什么使用 JindoFUSE

* JindoFUSE 基于 JindoSDK，在访问 OSS 的能力上，相对于开源的 [ossfs客户端](https://github.com/aliyun/ossfs) 做了很多的性能优化。

* JindoFUSE 基于 C++ 实现，不依赖 JVM，相对于 HDFS 或 Alluxio 的 FUSE 实现节省了 JNI 调用的开销。

* 关于 JindoFUSE 在性能上的优势，我们做了对比测试，可以参考 [JindoFUSE 性能测试](jindo_fuse_benchmark.md)。


### JindoFUSE 使用

* [使用 JindoFUSE 访问 OSS](jindo_fuse_on_oss.md)
* [使用 JindoFUSE 访问 JindoFS 服务](jindo_fuse_on_dls.md)
