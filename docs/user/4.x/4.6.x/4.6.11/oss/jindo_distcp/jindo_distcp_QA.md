### list 性能低
#### 现象
如您在使用 JindoDistCp 的过程中，发现 list 性能较慢，如遇到如下信息
```
Successfully list objects with prefix xxx/yyy/ in bucket xxx recursive 0 result 315 dur 100036.615031MS
```
其中 dur 100036.615031MS 代表此次 list 用时，单位毫秒，如上述一次 list 耗时 100 秒，OSS 上文件正常的 list 速度是 1000 文件在 1s 以下，您可根据当前目录下文件数量来判断该 list 耗时是否异常，如上述信息显示 list 315 个文件的目录需要 100s 显然是不太正常的。
#### 解决办法
增加客户端内存，执行以下命令增大客户端内存
```
export HADOOP_CLIENT_OPTS="$HADOOP_CLIENT_OPTS -Xmx4096m"
```

### S3 bucket 名称带点号
#### 现象
ERROR jnative.NativeLogger: http_rpc_protocol.cpp:1604] Invalid host=aaa.bbb port=80

#### 解决办法
如果您的 S3 bucket 是 `aaa.bbb` 格式，我们目前的工具尚未正式支持此类域名，[S3也不推荐此类命名规则](https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html)，如下方法可以临时绕过此类异常。

* `aaa`前面作为 bucket 名字
* `bbb`作为 fs.s3.endpoint 的一部分
* 同时设置 `fs.s3.region`，一般来说，region 就在 endpoint 中，如 s3.${region}.amazonaws.com

使用 JindoDistCP 完整命令如下：

```shell
hadoop jar jindo-distcp-tool-${version}.jar --src s3://aaa/ --dest oss://destOssBucket/ \
    --hadoopConf fs.oss.accessKeyId=yourOsskey --hadoopConf fs.oss.accessKeySecret=yourOssSecret --hadoopConf fs.oss.endpoint=oss-cn-xxx.aliyuncs.com \
    --hadoopConf fs.s3.accessKeyId=yourkey --hadoopConf fs.s3.accessKeySecret=yoursecret \
    --hadoopConf fs.s3.endpoint=bbb.s3.xxx.amazonaws.com --hadoopConf fs.s3.region=xxx \
    --parallelism 10
```
