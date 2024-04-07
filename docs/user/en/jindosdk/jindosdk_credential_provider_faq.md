# Common Issues with Accessing OSS/OSS-HDFS via AK

## 1. How to Access OSS/OSS-HDFS Anonymously in an EMR Cluster?

JindoSDK uses the ECS instance role (default: AliyunECSInstanceForEMRRole) bound at cluster creation to obtain Security Tokens for accessing OSS/OSS-HDFS.

Update the following parameter in `core-site.xml` under 'Configuration' in the Hadoop-Common section:

| Property            | Value           |
| ------------------- | --------------- |
| fs.oss.credentials.provider | com.aliyun.jindodata.oss.auth.EcsStsCredentialsProvider |

## 2. How to Verify the Availability of Anonymous Service within an EMR Cluster?

## Method One:

Execute the following command:
```bash
curl http://100.100.100.200/latest/meta-data/Ram/Security-credentials/AliyunECSInstanceForEMRRole
```
If the response returns content in the following format, it indicates normal operation:

```json
{
    "AccessKeyId": "STS.NUreXXXXXX",
    "AccessKeySecret": "BsmbnDoXXXXXXXX",
    "Expiration": "2022-11-22T11:27:39Z",
    "SecurityToken": "CAISlwJ1q6FXXXXXXX",
    "LastUpdated": "2022-11-22T05:27:39Z",
    "Code": "Success"
}
```

## Method Two:

Set up anonymous access via ECS:
Update the following parameters in `core-site.xml` under 'Configuration':

| Property            | Value           |
| ------------------- | --------------- |
| fs.oss.credentials.provider | com.aliyun.jindodata.oss.auth.EcsStsCredentialsProvider |

Then, try accessing an OSS/OSS-HDFS bucket using HDFS shell. If successful, anonymous access is working correctly.

## 3. Periodic Failure to Access OSS/OSS-HDFS Every 5-6 Hours

This issue might occur due to known issues in certain versions. Please refer to the [Known Issues](../faq.md).

## Solution One: Use Fixed AK
Configure `SimpleCredentialsProvider`. Follow the instructions in [Configuring OSS/OSS-HDFS Credential Provider](jindosdk_credential_provider.md).

## Solution Two: Upgrade JindoSDK to the Latest Version

* For upgrading on the old console, see: [EMR Cluster JindoSDK Upgrade Process](../upgrade/emr_upgrade_jindosdk.md)

* For upgrading on the new console, see: [EMR Cluster JindoSDK Upgrade Process](../upgrade/emr2_upgrade_jindosdk.md)

* For EMR clusters running EMR-5.5.0/EMR-3.39.0 or earlier, see: [EMR Cluster JindoSDK Upgrade Process](../upgrade/emr_upgrade_smartdata.md)

## 4. Error When Accessing OSS/OSS-HDFS Path Containing AK Information

Error message:
```
The Filesystem URI contains login details. This authentication mechanism is no longer supported.
```
Starting from JindoSDK version 4.0.0, for security reasons, support for AK (AccessKeyId, AccessKeySecret, etc.) in the filesystem URI has been disabled.

## Solution One:
Remove AK information from the access path.

## Solution Two:
If such usage is necessary, enable it by adding the following parameter in `core-site.xml` under 'Configuration':

| Property            | Value         |
| ------------------- | ------------- |
| fs.oss.uri-with-secrets.enable | true |

Please note that this practice is discouraged due to potential security risks.