# Authorizing Access to OSS/OSS-HDFS

## Granting Access to OSS

When authorizing operational permissions for an RAM user on OSS, you can define custom policies flexibly. Here's how:

1.  Log in to the [RAM Console](https://ram.console.aliyun.com/).

2.  From the left sidebar, choose **Permissions Management > Permissions Policy**.

3.  On the **Permissions Policy** page, click **Create Permission Policy**.

4.  On the **Script Editor** tab, enter the policy content and click **Next: Edit Basic Information**.


OSS provides a comprehensive permission control system. For a complete overview of OSS authorization policies, refer to the [RAM Policy Overview](https://help.aliyun.com/document_detail/100680.htm#concept-y5r-5rm-2gb).

**Caution:** Carefully define permission policies based on the required privileges for your RAM users. For Hive and Spark users, whether multi-versioning is enabled or not, `oss:ListObjectVersions` and `oss:DeleteObjectVersion` must be configured due to temporary directories like "_temporary", ".staging", ".hive-staging", "__magic" triggering multi-version operations.

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

Replace `<example-bucket>` with the authorized bucket name in this custom policy.

5.  Enter a **Policy Name** and **Description**, then click **OK**.


### Other Custom Scenario Examples

Read-only Access to OSS:

**Caution:** Define permissions according to your RAM user's needs. For Hive and Spark users, configure `oss:ListObjectVersions` even if multi-versioning isn't enabled because common temporary directories might trigger multi-version operations.

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

Access to an OSS Bucket with Multi-Versioning Enabled (not recommended due to potential performance issues):

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
### Bucket Policy Authorization

It's recommended to use "Full Control" to avoid issues like [Investigating AccessDenied Issues When Sub-Accounts Access OSS](./issues/oss_access_denied.md).

## Granting Access to OSS-HDFS

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

This policy grants access to OSS-HDFS.