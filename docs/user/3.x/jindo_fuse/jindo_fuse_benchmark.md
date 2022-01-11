## Jindo Fuse 性能测试

### 测试环境

CPU: 80核 | 内存： 128G  
网络规格：25Gbps  
ECS规格： ecs.hfc6.20xlarge

### 测试数据集

数据集A: 大文件集合，每个文件140MB，总共140GB  
数据集B: 小文件集合，每个文件23KB，总共1w个

### 测试结果

单线程测试

| 数据集 | jindo-fuse | ossfs | OSS2/Python |
| --- | --- | --- | --- |
| 数据集A | 191 MB/s | 72 MB/s | 17 MB/s |
| 数据集B  | 1.16 MB/s | 1.25 MB/s | 2.1 MB/s |

多线程测试（10个线程）

| 数据集 | jindo-fuse | ossfs | OSS2/Python |
| --- | --- | --- | --- |
| 数据集A | 1693MB/s | 686 MB/s | 162 MB/s |
| 数据集B | 18 MB/s | 10 MB/s | 9 MB/s |

### 结果分析

1. jindo-fuse 对于大文件 有预读，因此比较快。
2. OSS2 对小文件比较快，小文件 fuse 无法做预读，OSS2 直接 http 请求没有额外开销，所以比较快。
3. 小文件经过 fuse 有额外开销， 因此 jindo-fuse、ossfs 速度比 OSS2 差。
4. OSS2 对单进程多线程效果不好。 我们可以推测 OSS2 多进程成绩 2.1MB/s *10 = 21MB/s。
5. ossfs 对小文件因为有额外一次get_attr操作，即使调大 max_stat_cache_size=500000 效果也不理想，相当于 get_attr + get_object 两次 rpc，因此速度是另外二者的一半，20MB/s /2 = 10MB/s 符合预期。