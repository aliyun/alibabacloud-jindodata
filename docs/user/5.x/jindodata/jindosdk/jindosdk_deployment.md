# 在非 EMR 环境中部署 JindoSDK

## 部署 JindoSDK

连接ECS实例。具体操作，请参见[连接ECS实例](https://help.aliyun.com/document_detail/163467.htm#section-fqu-flq-xvv)。

1.  下载并解压JindoSDK JAR包。
    
执行以下命令，下载4.6.2版本JindoSDK JAR包。
        
```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/4.6.2/jindosdk-4.6.2.tar.gz
```

2.  执行以下命令，解压JindoSDK JAR包。
    
```bash
tar zxvf jindosdk-4.6.2.tar.gz
```

3.  配置环境变量。
    
配置JINDOSDK\_HOME、JINDOSDK\_CONF\_DIR、LD\_LIBRARY\_PATH。
        
以安装包内容解压在/usr/lib/jindosdk-4.6.2目录为例：

```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-4.6.2
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:${JINDOSDK_HOME}/lib/*
```

**重要** 请将安装目录和环境变量部署到所有所需节点上。

## Hadoop 配置文件

1.  配置JindoFS服务实现类及AccessKey。
    
    执行以下命令，进入Hadoop的core-site.xml配置文件。
        
```bash
vim /usr/local/hadoop/etc/hadoop/core-site.xml
```

2.  将JindoSDK DLS实现类配置到Hadoop的core-site.xml中。
    
```xml
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOSS</value>
    </property>

    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.jindodata.oss.JindoOssFileSystem</value>
    </property>
</configuration>
```

3.  将已开启HDFS服务的Bucket对应的accessKeyId、accessKeySecret预先配置在Hadoop的core-site.xml中。
    
```xml
<configuration>
    <property>
        <name>fs.oss.accessKeyId</name>
        <value>xxx</value>
    </property>

    <property>
        <name>fs.oss.accessKeySecret</name>
        <value>xxx</value>
    </property>
</configuration>
```

4.  配置JindoFS服务Endpoint。
    

使用JindoFS服务访问OSS Bucket时需要配置Endpoint。推荐访问路径格式为oss://<Bucket>.<Endpoint>/<Object>，例如oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/exampleobject.txt。配置完成后，JindoSDK会根据访问路径中的Endpoint访问对应的JindoFS服务接口。

您还可以通过其他方式配置JindoFS服务Endpoint，且不同方式配置的Endpoint存在生效优先级。更多信息，请参见[附录一：配置Endpoint的其他方式](https://help.aliyun.com/document_detail/332830.html#section-rw3-w05-2ub)。

## 非 Hadoop 配置文件

### 配置文件

使用 INI 风格配置文件，配置文件的文件名为`jindosdk.cfg`，示例如下：

```ini
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# 已创建 OSS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 oss-cn-hangzhou.aliyuncs.com。
# 已创建 OSS-HDFS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 oss-cn-hangzhou.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
# 用于访问OSS的AccessKey ID和AccessKey Secret。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```

#### 免密访问

前提：使用的是阿里云 ECS，并且该机器已绑定过角色授权。 示例如下：

```ini
[common]
logger.dir = /tmp/fuse-log

[jindosdk]
# 已创建 OSS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 oss-cn-hangzhou.aliyuncs.com。
# 已创建 OSS-HDFS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 oss-cn-hangzhou.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
fs.oss.provider.endpoint = ECS_ROLE
fs.oss.provider.format = JSON
```
