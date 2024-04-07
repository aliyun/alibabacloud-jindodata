# Alibaba Cloud OSS Authentication with Ranger in High-Security EMR Clusters

This article explains how to configure Alibaba Cloud OSS and OSS-HDFS services to use Ranger for authentication in new versions of E-MapReduce Console (EMR clusters version EMR-5.15.0/EMR-3.49.0 and above).

## Background

Apache Ranger provides a centralized framework for managing fine-grained access control across various components in the Hadoop ecosystem. When users store data in Alibaba Cloud OSS, they typically manage access control via RAM (Resource Access Management) users in AliCloud. With JindoAuth integrated into Ranger's client, users can conveniently manage permissions for big data components uniformly.

## Prerequisites

1. Create an E-MapReduce EMR-5.15.0/EMR-3.49.0 or higher cluster and enable both Ranger and Ranger-plugin services.
2. Enable Kerberos authentication for authentication requirements.
3. If all cluster users are trusted, Kerberos authentication can be skipped.

**Note:** Once Kerberos is enabled on an EMR cluster, it cannot be disabled. After enabling Kerberos, all big data components' services require Kerberos authentication, and job submissions to the cluster must pass identity verification. For more information about high-security clusters, refer to the [official documentation](https://help.aliyun.com/document_detail/459040.html).

## Steps

### 1. Access the Cluster Services Page

a. Log in to the EMR on ECS console.
b. Select the appropriate region and resource group from the top menu bar.
c. On the cluster management page, click on 'Cluster Services' under the target cluster's operation column.

### 2. Enable OSS in Ranger

a. On the cluster services page, click on the status in the Ranger-plugin service area.
b. In the service overview section, turn on the 'enableOSS' switch.
c. Confirm the action in the pop-up dialog.

### 3. Deploy Client Configuration

a. On the cluster services page, navigate to the HADOOP-COMMON service page.
b. Click on 'Configuration'.
c. Click on 'Deploy Client Configuration'.

### 4. Restart Persistent Services like HiveServer2

a. On the cluster services page, select 'more' > 'Hive'.
b. In the component list area, click 'Restart' next to HiveServer.
c. Enter a reason for the restart in the dialog box, then click 'OK'.
d. Confirm the action in the confirmation dialog.

### 5. Permissions Configuration Example

After enabling Ranger for OSS, the OSS Service is automatically added.

#### Configuring User 'test' with Full Access to `oss://jindoauth-runjob-cn-shanghai/user/test`

1. In the policy configuration page, remove the `oss://` prefix from the path.
2. Note that the 'recursive' button is not optional.
3. Do not include a trailing slash (/) at the end of the path.

### 6. Accessing OSS

If a user attempts to access a path not authorized by Ranger, an error similar to the following will occur:

```
org.apache.hadoop.security.AccessControlException: Permission denied: user=test, access=READ_EXECUTE, resourcePath="bucket-test-hangzhou/"
```

This error indicates that the user lacks the necessary permissions to access the specified resource. Ensure proper policies have been set up in Ranger before attempting access.