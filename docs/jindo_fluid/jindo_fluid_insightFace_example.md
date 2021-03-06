### 机器规格
单机
gn6v-c10g1.20xlarge
82 vCPU
336 GiB 内存
8 V100 GPU


### 1、部署 JindoRuntime
#### 云SSD
```yaml
apiVersion: data.fluid.io/v1alpha1
kind: JindoRuntime
metadata:
  name: hadoop
spec:
  replicas: 1
  tieredstore:
    levels:
      - mediumtype: SSD
        path: /var/lib/docker/jindo
        quota: 300Gi
        high: "0.9"
        low: "0.8"
  master:
    properties:
      namespace.cache.sync.load-type: once
  fuse:
    properties:
      jfs.cache.meta-cache.enable: "1"
      client.read.oss.readahead.buffer.size: "8388608"
      client.read.oss.readahead.buffer.count: "8"
      client.oss.download.thread.concurrency: "500"
      client.oss.download.queue.size: "1000"
      client.oss.connection.timeout.millisecond: "12000"
      client.oss.timeout.millisecond: "120000"
```
登陆到 maser 节点上
```bash
$ kubectl exec -ti hadoop-jindofs-master-0 -- /bin/bash
root@iZufdfd8e5rwew0:/# hadoop fs -ls jfs://hadoop/
Found 2 items
-rw-rw-rw-   1  138056739 2019-12-13 18:11 jfs://hadoop/faces_ms1m_112x112_2.pickle
drwxrwxrwx   -          0 2021-03-04 09:18 jfs://hadoop/images_2
```




### 2、元数据预取时间
```bash
$ time jindo jfs  -metaSync -R jfs://hadoop/
real	1m25.114s
user	0m2.130s
sys	0m0.280s
```
### 3、数据预取时间
```bash
$ time jindo jfs -cache -s -l jfs://hadoop/
```
### 4、List文件列表时间


#### 1、清理page cache
```bash
$ sync && echo 3 > /proc/sys/vm/drop_caches
```
#### 2、数据加载
```bash
$ time hadoop fs -ls -R jfs://hadoop/ | wc -l
3804848
```
### 5、模型训练速度


#### 云SSD/单机八卡
提交训练任务


```bash
docker run -v /runtime-mnt/jindo/default/hadoop/jindofs-fuse/hadoop/:/data \
  -e NVIDIA_VISIBLE_DEVICES=all \
  -e gpus=8 \
  -e num_iteration=7500 \
  -e workers=1 \
  -e use_fp16=1 \
  -v /dev/shm:/dev/shm \
  -itd --entrypoint /InsightFace-v3-perseus/launch-example.sh \
  --name=train-faces-ssd-112x112-1x8 \
   registry.cn-huhehaote.aliyuncs.com/tensorflow-samples/insightface:cuda10.0-1.2.2-1.3-py36
```


查看训练时间(无page cache)
```bash
$ docker inspect train-faces-ssd-112x112-1x8| grep -i StartedAt -A3
            
 "StartedAt": "2021-03-04T03:01:45.728864834Z",
 "FinishedAt": "2021-03-04T03:41:24.610078889Z"
```
查看训练时间(有page cache)
```bash
$ docker inspect train-faces-ssd-112x112-1x8| grep -i StartedAt -A3
            
"StartedAt": "2021-03-04T12:37:31.555750812Z"
"FinishedAt": "2021-03-04T13:16:52.892041248Z"
```
#### 测试结果
|  | 无page cache |  | 有page cache |  |
| --- | --- | --- | --- | --- |
| 云SSD | 39m39s | 64x3.16x8=1617.92images/s | 39m21s | 64x3.18x8=1628.16images/s |



