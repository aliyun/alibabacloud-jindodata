# OSS/OSS-HDFS 授权

## 授权访问 OSS 

授权操作账号对OSS的操作权限时，支持通过自定义策略灵活定义RAM用户对OSS数据的访问权限，详细步骤如下。

1.  登录[RAM控制台](https://ram.console.aliyun.com/)。
    
2.  在左侧导航栏，选择**权限管理 > 权限策略**。
    
3.  在**权限策略**页面，单击**创建权限策略**。
    
4.  在**创建权限策略**页面，单击**脚本编辑**页签。
    
5.  输入权限策略内容，然后单击**下一步：编辑基本信息**。
    

OSS 提供了完整的数据权限管控体系，完整的OSS授权策略请参见[RAM Policy概述](https://help.aliyun.com/document_detail/100680.htm#concept-y5r-5rm-2gb)。

**注意** 请根据RAM用户需要使用的权限，谨慎定义权限策略。对于使用hive、spark的用户，无论是否开启多版本都需要配置`oss:ListObjectVersions`、`oss:DeleteObjectVersion`，我们对于一些常见的临时目录，比如"_temporary"、".staging"、".hive-staging"、"__magic" 会触发多版本操作。
```json
    {
      "Version": "1",
      "Statement": [
        {
          "Action": [
            "oss:ListObjects",
            "oss:GetBucketInfo",
            "oss:PutObject",
            "oss:GetObject",
            "oss:DeleteObject",
            "oss:AbortMultipartUpload",
            "oss:ListParts",
            "oss:RestoreObject",
            "oss:ListObjectVersions",
            "oss:DeleteObjectVersion"
          ],
          "Resource": [
            "acs:oss:*:*:<example-bucket>",
            "acs:oss:*:*:<example-bucket>/*"
          ],
          "Effect": "Allow"
        },
        {
          "Action": [
            "oss:ListBuckets"
          ],
          "Resource": "*",
          "Effect": "Allow"
        }
      ]
    }
```

上述自定义策略中的`<example-bucket>`需要替换为被授权的Bucket名称。

6.  输入权限策略**名称**和**备注**，并单击**确定**。
    

### 其他自定义场景示例

只读访问 OSS：

**注意** 请根据RAM用户需要使用的权限，谨慎定义权限策略。对于使用hive、spark的用户，无论是否开启多版本都需要配置`oss:ListObjectVersions`, 我们对于一些常见的临时目录，比如"_temporary"、".staging"、".hive-staging"、"__magic" 会触发多版本操作。
```json
    {
      "Version": "1",
      "Statement": [
        {
          "Action": [
            "oss:ListObjects",
            "oss:GetBucketInfo",
            "oss:GetObject",
            "oss:RestoreObject",
            "oss:ListObjectVersions"
          ],
          "Resource": [
            "acs:oss:*:*:<yourBucketName>",
            "acs:oss:*:*:<yourBucketName>/*"
          ],
          "Effect": "Allow"
        },
        {
          "Action": [
            "oss:ListBuckets"
          ],
          "Resource": "*",
          "Effect": "Allow"
        }
      ]
    }
```
访问开启了多版本功能的 OSS（不建议开启多版本，容易产生性能问题）：
```json
    {
      "Version": "1",
      "Statement": [
        {
          "Action": [
            "oss:ListObjects",
            "oss:GetBucketInfo",
            "oss:PutObject",
            "oss:GetObject",
            "oss:DeleteObject",
            "oss:AbortMultipartUpload",
            "oss:ListParts",
            "oss:RestoreObject",
            "oss:ListObjectVersions",
            "oss:GetObjectVersion",
            "oss:DeleteObjectVersion",
            "oss:RestoreObjectVersion"
          ],
          "Resource": [
            "acs:oss:*:*:<yourBucketName>",
            "acs:oss:*:*:<yourBucketName>/*"
          ],
          "Effect": "Allow"
        },
        {
          "Action": [
            "oss:ListBuckets"
          ],
          "Resource": "*",
          "Effect": "Allow"
        }
      ]
    }
```
### Bucket Policy 授权

建议使用“完全控制”，避免出现以下问题：

[《使用子账号访问 OSS 出现 AccessDenied 问题排查》](./issues/oss_access_denied.md)

## 授权访问 OSS-HDFS

```json
{
    "Statement": [
        {
          "Effect": "Allow",
          "Action": "oss:ListObjects",
          "Resource": [
            "acs:oss:*:*:*"
          ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "oss:GetBucketInfo",                
                "oss:PostDataLakeStorageFileOperation",
                "oss:PostDataLakeStorageAdminOperation"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": "oss:*",
            "Resource": [
                "acs:oss:*:*:*/.dlsdata",
                "acs:oss:*:*:*/.dlsdata*"
            ]
        }
    ],
    "Version": "1"
}
```
