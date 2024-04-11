# Best Practices for Managing Memory Growth with Jemalloc in JindoSDK

## Context

JindoSDK's Hadoop SDK employs JNI calls to its native implementation, making JVM garbage collector settings ineffective for managing native memory increases. You can control native memory growth using jemalloc configurations.

## Deployment Procedure

The full output of JindoSDK includes jemalloc within the tar archive:

```text
.
├── bin
│   ├── ...
├── conf
│   ├── ...
├── lib
│   ├── jindo-core-6.3.4.jar
│   ├── jindo-sdk-6.3.4.jar
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

Assuming the installation location is `/opt/apps/JINDOSDK/jindosdk-current/`, make sure all environments using JindoSDK have these environment variables set:
```bash
LD_PRELOAD=/opt/apps/JINDOSDK/jindosdk-current/lib/native/libjemalloc.so
MALLOC_CONF=narenas:1,tcache:false,dirty_decay_ms:0,muzzy_decay_ms:0,background_thread:true,abort_conf:true
```

## Performance Impact

To evaluate the effect of these settings, you can run the `jindo-sdk-memleak-check-1.0.jar` tool:

```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/memcheck/jindo-sdk-memleak-check-1.0.jar
```

Then execute the memleak check test:
```bash
HADOOP_HEAPSIZE=2560 HADOOP_ROOT_LOGGER=ERROR,console hadoop jar jindo-sdk-memleak-check-1.0.jar MemLeakTestProgram -baseDir oss:/<your-bucket>/ -duration 8899
```

Observations after running tests:
- With default glibc: Consumes 3627M.
- With jemalloc but no MALLOC_CONF: Consumes 2929M.
- With jemalloc and MALLOC_CONF: Consumes 2289M.

## Alternative Configuration

The provided configuration is suitable for scenarios where memory conservation is crucial, but it might affect performance slightly. If you need to balance memory usage and performance, try this alternative setup:
```bash
LD_PRELOAD=/opt/apps/JINDOSDK/jindosdk-current/lib/native/libjemalloc.so
MALLOC_CONF=percpu_arena:percpu,lg_tcache_max:12,dirty_decay_ms:5000,muzzy_decay_ms:5000
```
This configuration aims to strike a balance between efficient memory utilization and maintaining acceptable performance levels. Adjust these settings as needed for your specific application requirements.