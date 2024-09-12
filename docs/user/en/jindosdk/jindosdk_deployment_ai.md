# Deploying JindoSDK for AI Scenarios

When using JindoFuse, Jindo CLI, Pyjindo or other non-Hadoop ecosystem components, they access configuration files in the directory pointed by the environment variable `JINDOSDK_CONF_DIR`.

## Deploying JindoSDK

1. Download and extract the JindoSDK TAR package.

Execute the following command to download the v6.6.2 JindoSDK TAR package for most Linux x86 environments. For deployment on other platforms, refer to [Installing and Deploying JindoSDK on Multi-platform Environments](../jindosdk/jindosdk_deployment_multi_platform.md):
```bash
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.6.2/jindosdk-6.6.2-linux.tar.gz
```

2. Unzip the JindoSDK TAR package.
```bash
tar zxvf jindosdk-6.6.2-linux.tar.gz
```

3. Set environment variables.

Assuming the extracted folder is `/usr/lib/jindosdk-6.6.2-linux`:

```bash
export JINDOSDK_HOME=/usr/lib/jindosdk-6.6.2-linux
export JINDOSDK_CONF_DIR=${JINDOSDK_HOME}/conf
export PATH=${PATH}:${JINDOSDK_HOME}/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${JINDOSDK_HOME}/lib/native
```
**Important**: Make sure to set these installation directories and environment variables on all nodes where they're required.

## Modifying Configuration

Configuration files are located in the directory pointed by `JINDOSDK_CONF_DIR`, with the filename `jindosdk.cfg`, using an INI-style format.

### `cfg` Configuration File

Example configuration:

```ini
[common]
logger.dir = /tmp/jindosdk-log

[jindosdk]
# Replace with the actual endpoint for your created OSS Bucket. For example, for East China 1 (Hangzhou), use oss-cn-hangzhou.aliyuncs.com.
# Replace with the actual endpoint for your created OSS-HDFS Bucket. For example, for East China 1 (Hangzhou), use cn-hangzhou.oss-dls.aliyuncs.com.
fs.oss.endpoint = <your_endpoint>
# Replace with your AccessKey ID and AccessKey Secret for accessing OSS. AccessKey has full API access rights and is highly risky. Strongly recommended to create and use a RAM user for API access or daily operations. Log in to the RAM Console to create a RAM user.
fs.oss.accessKeyId = <your_key_id>
fs.oss.accessKeySecret = <your_key_secret>
```

#### Anonymous Access

Prerequisite: You're using an Alibaba Cloud ECS instance with a [RAM role attached](https://help.aliyun.com/document_detail/61175.html). Example configuration:

```ini
[common]
logger.dir = /tmp/jindosdk-log

[jindosdk]
# Replace with the actual endpoint for your created OSS Bucket. For example, for East China 1 (Hangzhou), use oss-cn-hangzhou.aliyuncs.com.
# Replace with the actual endpoint for your created OSS-HDFS Bucket. For example, for East China 1 (Hangzhou), use cn-hangzhou.oss-dls.aliyuncs.com.
fs.oss.endpoint = <your_endpoint>
fs.oss.provider.endpoint = ECS_ROLE
fs.oss.provider.format = JSON
```
In this case, JindoSDK will leverage the IAM role associated with the ECS instance to authenticate and access resources without requiring explicit AccessKey credentials.