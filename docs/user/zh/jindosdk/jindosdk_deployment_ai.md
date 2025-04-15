# AI 场景部署 JindoSDK

在使用 JindoFuse，Jindo CLI，PyJindo 等非 Hadoop 生态组件时，会访问环境变量`JINDOSDK_CONF_DIR`所在的目录读取配置文件。

## 部署 JindoSDK

1.  下载并解压 JindoSDK TAR 包。
    
执行以下命令，下载6.8.3版本JindoSDK TAR包。以大多数linux x86环境为例。其他平台部署参见[《在多平台环境安装部署 JindoSDK》](../jindosdk/jindosdk_deployment_multi_platform.md)
        
```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.8.3/jindosdk-6.8.3-linux.tar.gz
```

2.  执行以下命令，解压 JindoSDK TAR 包。
    
```bash
tar zxvf jindosdk-6.8.3-linux.tar.gz
```

3.  配置环境变量。
    
以安装包内容解压在 /usr/lib/jindosdk-6.8.3-linux 目录为例：

```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-6.8.3-linux
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
```

**重要** 请将安装目录和环境变量部署到所有所需节点上。

## 修改配置

配置文件位于 JINDOSDK_CONF_DIR 所在目录，配置文件的文件名为`jindosdk.cfg`，使用 INI 风格配置文件。

### cfg 配置文件

示例如下：

```ini
[common]
logger.dir = /tmp/jindosdk-log

[jindosdk]
# 已创建 OSS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 oss-cn-hangzhou.aliyuncs.com。
# 已创建 OSS-HDFS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 cn-hangzhou.oss-dls.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
# 用于访问OSS的AccessKey ID和AccessKey Secret。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```

#### 免密访问

前提：使用的是阿里云 ECS，并且该机器已绑定过[RAM角色授权](https://help.aliyun.com/document_detail/61175.html)。 示例如下：

```ini
[common]
logger.dir = /tmp/jindosdk-log

[jindosdk]
# 已创建 OSS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 oss-cn-hangzhou.aliyuncs.com。
# 已创建 OSS-HDFS Bucket 对应的 Endpoint。以华东1（杭州）为例，填写为 cn-hangzhou.oss-dls.aliyuncs.com。
fs.oss.endpoint = <your_endpoint>
fs.oss.provider.endpoint = ECS_ROLE
fs.oss.provider.format = JSON
```
