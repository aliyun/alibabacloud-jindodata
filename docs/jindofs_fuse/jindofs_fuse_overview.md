## JindoFS Fuse

### 环境要求
* 目前jindofs-fuse只支持Linux操作系统。大多数Linux发行版已经内置了Fuse模块，没有的话您需要安装或者编译Fuse模块。

### 什么是 JindoFS Fuse

Fuse是Linux系统内核提供的一种挂载文件系统的方式。

通过JindoFS的Fuse客户端，将对象存储OSS服务或JindoFS集群上的文件挂载到本地文件系统中，您能够像操作本地文件一样操作OSS、JindoFS集群上的文件。


### 为什么使用 JindoFS Fuse

JindoFS Fuse基于JindoFS SDK，在访问OSS的能力上，相对于开源的[ossfs客户端](https://github.com/aliyun/ossfs)做了很多的性能优化。

JindoFS Fuse基于c++实现，不依赖JVM，相对于HDFS或Alluxio的Fuse实现节省了JNI调用的开销。

JindoFS Fuse还可以访问JindoFS集群上的缓存数据，利用缓存加速数据访问性能。

* [JindoFS Fuse 性能测试](./jindofs_fuse_benchmark.md)

### JindoFS Fuse 使用

* [使用 JindoFS Fuse 访问 OSS](./jindofs_fuse_2_oss.md)

* [使用 JindoFS Fuse 访问 JindoFS Cache/Block 模式集群](./jindofs_fuse_2_block_cache_mode.md)

### JindoFS Fuse 系列文章

* [拥抱云原生，Fluid结合JindoFS ：阿里云OSS加速利器](../jindo_fluid/jindo_fluid_introduce.md)

* [JindoFS Fuse 支持（视频）](https://developer.aliyun.com/live/2766)

