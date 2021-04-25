### JindoRuntime log获取


JindoRuntime 的 log 存储路径为您设定的 `runtime.spec.path`下，具体路径为 `<runtime.spec.path>/<namespace>/<DatasetName>/bigboot/log` 


结构如下：
```shell
-rw-r--r--  1 root root   3182592 Apr 25 17:06 b2-namespaceservice-20210425-153854-494826.LOG
lrwxrwxrwx  1 root root        46 Apr 25 15:38 b2-namespaceservice.LOG -> b2-namespaceservice-20210425-153854-494826.LOG
-rw-r--r--  1 root root 400314368 Apr 25 17:05 b2-storageservice-20210425-153914-496492.LOG
lrwxrwxrwx  1 root root        44 Apr 25 15:39 b2-storageservice.LOG -> b2-storageservice-20210425-153914-496492.LOG
-rw-r--r--  1 root root      3289 Apr 25 17:06 jboot-20210425-170645-733103.LOG
lrwxrwxrwx  1 root root        32 Apr 25 17:06 jboot.LOG -> jboot-20210425-170645-733103.LOG
-rw-r--r--  1 root root   8448596 Apr 25 15:41 local_db-rocksdb.log
-rw-r--r--  1 root root  68442510 Apr 25 16:27 raw.log
-rw-r--r--  1 root root  52104338 Apr 25 15:52 slice_db-rocksdb.log
-rw-r--r--  1 root root  67328935 Apr 25 15:52 slice_db-rocksdb.log.1
-rw-r--r--  1 root root    281320 Apr 25 15:39 stsinfo-rocksdb.log
-rw-r--r--  1 root root    281524 Apr 25 15:39 stspart-rocksdb.log
-rw-r--r--  1 root root   5269358 Apr 25 16:27 time.log
```


大多数情况下只要 jboot 相关的 log 就足够。


### ISSUE


如您的环境出现问题，也可开 [ISSUE](https://github.com/aliyun/alibabacloud-jindofs/issues) 给我们，会第一时间处理
