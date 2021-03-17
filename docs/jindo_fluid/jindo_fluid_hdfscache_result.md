## JindoFS HDFS Cache 性能测试文档

### 1、InsightFace测试

#### 测试脚本
```shell
docker run -v /runtime-mnt/jindo/default/hadoop/jindofs-fuse/hadoop/InsightFace:/data   -e NVIDIA_VISIBLE_DEVICES=all   -e gpus=8   -e num_iteration=7500   -e workers=1   -e use_fp16=1   -v /dev/shm:/dev/shm   -itd --entrypoint /InsightFace-v3-perseus/launch-example.sh   --name=train-faces-ssd-112x112-1x8    registry.cn-huhehaote.aliyuncs.com/tensorflow-samples/insightface:cuda10.0-1.2.2-1.3-py36
```


#### 测试结果
| 单位/s   | 单机单节点 | 单机三节点 | 
| ---  | --- | --- | 
| NoCache  | 3048 | 3048 | 
| Cache  | 2350 | 2397 |
| 训练时间下降  | 23% | 21% |
