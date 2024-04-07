# Quick Start for OSS/OSS-HDFS

## Prerequisites

Before accessing OSS/OSS-HDFS, you need to create the corresponding storage spaces (buckets).

*   [Activate the OSS service](https://help.aliyun.com/document_detail/31884.html).

*   [Create an OSS bucket](https://www.alibabacloud.com/help/zh/object-storage-service/latest/oss-console-create-buckets).

*   Confirm OSS authorization.

    *   In a new EMR cluster console, it is already activated by default. For issues, refer to [Role Authorization](https://help.aliyun.com/document_detail/379951.html).

    *   In an old EMR cluster console, it is also activated by default. For issues, see [Role Authorization](https://help.aliyun.com/document_detail/28072.html).

    *   In non-EMR environments, follow the authorization process outlined in [OSS/OSS-HDFS Authorization](./oss_ram_policy.md).

*   Activate and authorize access to the OSS-HDFS service (optional but recommended):

    *   Read [Precautions Before Using OSS-HDFS](https://www.alibabacloud.com/help/zh/object-storage-service/latest/usage-instructions-of-oss-hdfs).

    *   Follow steps to [Activate and Authorize Access to OSS-HDFS](https://help.aliyun.com/document_detail/419505.html).

*   Verify your deployed JindoSDK version:

    *   In an EMR cluster, JindoSDK is pre-deployed.

        *   To access OSS-HDFS, create an EMR cluster with version EMR-3.42.0 or higher or EMR-5.8.0 or higher.

    *   In non-EMR environments, you can download the latest version of JindoSDK and deploy it yourself. See deployment guides for [Hadoop environments](../jindosdk/jindosdk_deployment_hadoop.md) and [AI environments](../jindosdk/jindosdk_deployment_ai.md).

        *   To access OSS-HDFS, deploy JindoSDK version 4.x or later.



## Path Explanation

|Storage System| Root Path Example| Description|
|---|---|---|
|OSS| oss://examplebucket.oss-cn-shanghai-internal.aliyuncs.com/| Assumes an OSS bucket named "examplebucket" has been created in the Shanghai region using the internal endpoint. Note: In EMR clusters without public network mounts, nodes do not support access to OSS public endpoints by default, meaning cross-region access is not supported.|
|OSS-HDFS| oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/| Assumes an OSS-HDFS bucket named "examplebucket" has been created in the Shanghai region.| Access methods for OSS and OSS-HDFS are identical except for the endpoint in the path.

## Access Methods

|Access Method| Example| Description|
|---|---|---|
|Hadoop Shell Command| hadoop fs -ls oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/| JindoSDK's JindoOssFileSystem implements Hadoop FileSystem. You can access OSS/OSS-HDFS through Hadoop Shell commands by identifying the endpoint in the path. For more usage, see [Accessing OSS/OSS-HDFS Through Hadoop Shell Commands](./usages/oss_hadoop_shell.md).|
|Jindo CLI Command| jindo fs -ls oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/| Jindo CLI provides a way similar to Hadoop Shell to access OSS/OSS-HDFS, along with additional features such as archiving, caching, and error analysis. Details are available in [Accessing OSS/OSS-HDFS Through Jindo CLI Commands](./usages/oss_jindo_cli.md).|
|POSIX Command| mkdir -p /mnt/oss<br>jindo-fuse /mnt/oss -ouri=oss://examplebucket.cn-shanghai.oss-dls.aliyuncs.com/<br>ls /mnt/oss| By implementing FUSE APIs, jindo-fuse allows you to mount an OSS/OSS-HDFS path locally and access it like local files. Learn more in [Accessing OSS/OSS-HDFS Through POSIX](./usages/oss_posix.md).|
|OSS Console| ![image](https://alidocs.oss-accelerate.aliyuncs.com/res/AmPdnp5J3dDpqw98/img/92a7fe0e-28fd-44fa-bf78-9af8155636d0.png)| Access OSS/OSS-HDFS through the [OSS Console](https://oss.console.aliyun.com/) by navigating to File List > OSS Files/HDFS.|