# JindoSDk 通过 jemalloc 控制内存上涨最佳实践

### 背景

jindosdk hadoop sdk 使用 jni 调用 native 实现，因此 jvm 相关 gc 参数无法有效控制 native 内存上涨，可以通过 jemalloc 配置来进行控制。

### 部属方法

jindosdk 完整产出物在 tar 包中默认携带了 jemalloc：

```
.
├── bin
│   ├── ...
├── conf
│   ├── ...
├── lib
│   ├── jindo-core-6.7.4.jar
│   ├── jindo-sdk-6.7.4.jar
│   └── native
│       ├── libjemalloc.so
│       ├── ...
├── plugins
│   ├── ...
├── tools
│   ├── ...
└── versions
    └── JINDOSDK_VERSION
```

假设安装目录为/opt/apps/JINDOSDK/jindosdk-current/，要保证所有调用jindosdk的环境，都配置以下的环境变量：

```
LD_PRELOAD=/opt/apps/JINDOSDK/jindosdk-current/lib/native/libjemalloc.so
MALLOC_CONF=narenas:1,tcache:false,dirty_decay_ms:0,muzzy_decay_ms:0,background_thread:true,abort_conf:true
```

### 效果

```
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/memcheck/jindo-sdk-memleak-check-1.0.jar
```

执行以下memleak-check测试

`HADOOP_HEAPSIZE=2560 HADOOP_ROOT_LOGGER=ERROR,console  hadoop jar jindo-sdk-memleak-check-1.0.jar MemLeakTestProgram  -baseDir oss:/<your-bucket>/ -duration 8899`

使用默认glibc，最终占用3627M

使用jemalloc不设置MALLOC_CONF，最终占用2929M

使用jemalloc并设置MALLOC_CONF，最终占用2289M

### 其他

上面的配置适合内存紧张的场景，可能对性能有一定影响。当需要综合考虑内存占用与性能时，可以尝试以下配置：
```
LD_PRELOAD=/opt/apps/JINDOSDK/jindosdk-current/lib/native/libjemalloc.so
MALLOC_CONF=percpu_arena:percpu,lg_tcache_max:12,dirty_decay_ms:5000,muzzy_decay_ms:5000
```