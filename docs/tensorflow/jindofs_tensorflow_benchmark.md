## JindoFS Tensorflow 性能测试

### 测试环境

CPU: 80核 | 内存： 192G  
网络规格：25Gbps  
ECS规格： ecs.hfc6.20xlarge  


### 测试数据集

数据集A: 大文件集合，每个文件140MB，总共140GB  
数据集B: 小文件集合，每个文件23KB，总共1w个

### 测试结果

单线程测试

| 数据集 | jindofs-fuse<br />有缓存<br />有pagecache | jindofs-fuse<br />无缓存 | jindofs-tensorflow<br />有缓存 | jindofs-tensorflow<br />无缓存 |
| --- | --- | --- | --- | --- |
| 数据集A | 1258 MB/s | 191 MB/s | 1489 MB/s | 191 MB/s |
| 数据集B  | 38 MB/s | 1.16 MB/s | 80 MB/s | 1.3 MB/s |

多线程测试（10个线程）

| 数据集 | jindofs-fuse<br />有缓存<br />有pagecache | jindofs-fuse<br />无缓存 | jindofs-tensorflow<br />有缓存 | jindofs-tensorflow<br />无缓存 |
| --- | --- | --- | --- | --- |
| 数据集A | 3075 MB/s | 1693MB/s | 3302 MB/s | 1693 MB/s |
| 数据集B | 265 MB/s | 18 MB/s | 458 MB/s | 20 MB/s |
