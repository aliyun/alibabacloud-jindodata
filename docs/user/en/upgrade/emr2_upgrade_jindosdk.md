# Upgrade Document for JindoSDK on New Version EMR Clusters

## Scenario One: Upgrading an Existing New Version Cluster

If you have an existing cluster created with the new version of E-MapReduce (EMR) EMR-5.6.0 or EMR-3.40.0 or later, and wish to upgrade JindoSDK or utilize its new features, follow these steps.

#### Step 1: Prepare Software Package and Upgrade Script Configuration

Log in to the Master node of your EMR cluster as `emr-user`. Download the patch package and extract it, placing the JindoSDK software package in the extracted folder.

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
```

Download the JindoSDK software package `jindosdk-{VERSION}-{PLATFORM}.tar.gz`, replacing `{VERSION}` and `{PLATFORM}` appropriately (for example, `6.7.8` for version and `linux` for platform).

```bash
cd jindosdk-patches
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/{VERSION}/jindosdk-{VERSION}-linux.tar.gz
ls -l
```

Your `jindosdk-patches` folder should resemble:

```bash
-rwxrwxr-x 1 emr-user emr-user      2439 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 emr-user emr-user      7315 May 01 00:00 apply.sh
-rw-rw-r-- 1 emr-user emr-user        40 May 01 00:00 hosts
-rwxrwxr-x 1 emr-user emr-user      1112 May 01 00:00 revert_all.sh
-rwxrwxr-x 1 emr-user emr-user      2042 May 01 00:00 revert.sh
-rw-r----- 1 emr-user emr-user xxxxxxxxx May 01 00:00 jindosdk-{VERSION}-linux.tar.gz
```

> **Note**: When upgrading from versions below 4.6.8 to 4.6.9 or higher, or to version 6.x, set `fs.jdo.committer.allow.concurrent=false` in `core-site.xml` before upgrading to prevent data loss during the process. Once all JindoSDK instances across GATEWAY nodes have been upgraded, you may safely remove this configuration at an appropriate time.

#### Step 2: Configure Upgrade Node Information

Edit the `hosts` file in the patch package, listing all cluster node hostnames, such as `master-1-1` or `core-1-1`.

```bash
cd jindosdk-patches
vim hosts
```

The `hosts` file content might look like this:

```bash
master-1-1
core-1-1
core-1-2
```

Alternatively, attempt to fetch all node information with a script; if the `hosts` retrieval fails, manually fill in the details:

```bash
cat /usr/local/taihao-executor-all/data/cache/.cluster_context | jq --raw-output '.nodes[].hostname.alias[]' > hosts
```

#### Step 3: Perform the Upgrade

Execute the `apply_all.sh` script to carry out the upgrade.

```bash
./apply_all.sh {NEW_JINDOSDK_VERSION}
```

For instance, to upgrade to version `6.7.8`, run:

```bash
./apply_all.sh 6.7.8
```

Upon completion, you will see output like:

```
>>> updating ... master-1-1
>>> updating ... core-1-1
>>> updating ... core-1-2
# # # DONE
```

**Note**: For YARN applications (like Spark Streaming or Flink jobs) that are currently running, stop them before rolling-restarting YARN NodeManagers.

#### Step 4: Restart Services After Upgrade

Services such as Hive, Presto, Impala, Flink, Ranger, Spark, and Zeppelin require restarting to fully adopt the updated JindoSDK.

For Hive, for example, navigate to the Hive service page in your EMR cluster and select 'More Operations' > 'Restart'.

## Scenario Two: Expanding an Existing Cluster

To use the new JindoSDK version when expanding an existing cluster, add a bootstrapping operation in the EMR console for automatic upgrade and repair upon cluster creation or expansion.

#### Step 1: Create a Bootstrap Upgrade Package

Download `jindosdk-patches.tar.gz`, `jindosdk-{VERSION}-{PLATFORM}.tar.gz`, and `[bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh)`.

For example, upgrading to version `6.7.8` on Linux x86:

```bash
mkdir jindo-patch
cd jindo-patch
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.7.8/jindosdk-6.7.8-linux.tar.gz
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh
ls -l
```

Generate the upgrade package next:

```bash
bash bootstrap_jindosdk.sh -gen-full {NEW_JINDOSDK_VERSION}
```

For version `6.7.8`, run:

```bash
bash bootstrap_jindosdk.sh -gen-full 6.7.8
```

A generated patch will be located at `/home/emr-user/jindo-patch/jindosdk-bootstrap-patches.tar.gz`.

#### Step 2: Upload the Bootstrap Upgrade Package

Upload the patch package and bootstrap script to OSS using Hadoop commands, the OSS console, ossutil, or other tools:

```bash
hadoop dfs -mkdir -p oss://{BUCKET_NAME}/path/to/patch/
cd /home/hadoop/patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://{BUCKET_NAME}/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://{BUCKET_NAME}/path/to/patch/
hadoop dfs -ls oss://{BUCKET_NAME}/path/to/patch/
```

Assume that you've uploaded to paths `oss://{BUCKET_NAME}/path/to/jindosdk-bootstrap-patches.tar.gz` and `oss://{BUCKET_NAME}/path/to/bootstrap_jindosdk.sh`.

#### Step 3: Add Bootstrap Operation in the EMR Console

Refer to [Managing Bootstrap Actions](https://help.aliyun.com/document_detail/398732.html) for detailed instructions on adding a bootstrap action in the EMR console.

Fill in configuration fields as follows:

| Parameter           | Description                                                                                    | Example                                                   |
|---------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| **Name**            | Name of the bootstrap action, e.g., Update JINDOSDK                                            | update_jindosdk                                          |
| **Script Location** | Specify the location of the script in OSS. Format must be oss://**/*.sh                        | oss://{BUCKET_NAME}/path/to/patch/bootstrap_jindosdk.sh |
| **Arguments**       | Parameters for the bootstrap action script, specifying values for variables used in the script | -bootstrap oss://{BUCKET_NAME}/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **Execution Scope** | Select **Cluster**                                                                             |                                                           |
| **Execution Time**   | Choose **Before Component Startup**                                                            |                                                           |
| **Failure Strategy** | Select **Continue Execution**                                                                  |                                                           |

### Step 4: Modify Cluster Configuration

#### 4.1 Compatibility with Older Versions of EMR Ranger Component

**Notice**: If EMR Ranger is enabled and you are upgrading JindoSDK from EMR-3.51.1/EMR-5.17.1 or earlier versions to versions between 6.5.0 and 6.7.2, there may be compatibility issues. It is recommended to upgrade to version 6.7.3 or above and modify the cluster configuration as follows:

a. Go to the **Configuration** page of the HADOOP-COMMON service and click on the **core-site.xml** tab.

b. On the **core-site.xml** page, search for the configuration item name.

c. Modify the configuration item.

| Parameter           | Description                                                                                         |
|---------------------|-----------------------------------------------------------------------------------------------------|
| `fs.jdo.plugin.dir` | Configure this to the plugins directory to `/opt/apps/JINDOSDK/jindosdk-current/plugins`. |

These steps will help you ensure compatibility and proper configuration when upgrading JindoSDK in your cluster. If you encounter any issues, please refer to the documentation or contact support for further assistance.

#### Step 5: Ensure Latest Fixes Are Loaded

```bash
ls -l /opt/apps/JINDOSDK/jindosdk-current/lib
```

```bash
lrwxrwxrwx 1 root root 64 Apr 12 11:08 jindo-core-6.2.0.jar -> /opt/apps/JINDOSDK/jindosdk-6.7.8-linux/lib/jindo-core-6.7.8.jar
lrwxrwxrwx 1 root root 82 Apr 12 11:08 jindo-core-linux-el7-aarch64-6.2.0.jar -> /opt/apps/JINDOSDK/jindosdk-6.7.8-linux/lib/jindo-core-linux-el7-aarch64-6.7.8.jar
lrwxrwxrwx 1 root root 63 Apr 12 11:08 jindo-sdk-6.2.0.jar -> /opt/apps/JINDOSDK/jindosdk-6.7.8-linux/lib/jindo-sdk-6.7.8.jar
lrwxrwxrwx 1 root root 50 Apr 12 11:08 native -> /opt/apps/JINDOSDK/jindosdk-6.7.8-linux/lib/native
lrwxrwxrwx 1 root root 57 Apr 12 11:08 site-packages -> /opt/apps/JINDOSDK/jindosdk-6.7.8-linux/lib/site-packages
```

#### Step 5: Restart Services After Upgrade

For freshly created clusters, restart Hive, Presto, Impala, Flink, Ranger, Spark, and Zeppelin services. For expanded nodes, only restart these services on the added nodes.

## Scenario Three: Creating a New Cluster

If you're creating a new EMR cluster and want to use the latest JindoSDK, you can add a bootstrap action in the EMR console to automatically upgrade and fix during cluster creation or expansion. Follow these steps to upgrade JindoSDK.

### 1. Create a Bootstrap Upgrade Package

Download `jindosdk-patches.tar.gz`, `jindosdk-{VERSION}-{PLATFORM}.tar.gz`, and `[bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh)`.

For example, to upgrade the new cluster's JindoSDK to version `6.7.8` on Linux x86:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.7.8/jindosdk-6.7.8-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh

ls -l
```

The contents should look like this:

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.7.8-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

Execute the command to create the upgrade package:

```bash
bash bootstrap_jindosdk.sh -gen-full $NEW_JINDOSDK_VERSION
```

For upgrading to version `6.7.8`, run:

```bash
bash bootstrap_jindosdk.sh -gen-full 6.7.8
```
**Explanation of parameters: `-gen` generates a lite upgrade package, while `-gen-full` generates a full upgrade package.**

Upon success, you'll see this output:

```bash
Generated patch at /home/emr-user/jindo-patch/jindosdk-bootstrap-patches.tar.gz
```

This completes the patch generation, resulting in `jindosdk-bootstrap-patches.tar.gz`.

### 2. Upload the Bootstrap Upgrade Package

Upload the patch package and bootstrap script to OSS using Hadoop commands within your EMR cluster or tools like the OSS console, ossutil, or OSS Browser.

```bash
hadoop dfs -mkdir -p oss://{BUCKET_NAME}/path/to/patch/

cd /home/hadoop/patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://{BUCKET_NAME}/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://{BUCKET_NAME}/path/to/patch/

hadoop dfs -ls oss://{BUCKET_NAME}/path/to/patch/
```

You should see the following response:

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket-name>/.../bootstrap_jindosdk.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket-name>/.../jindosdk-bootstrap-patches.tar.gz
```

Assuming you've uploaded to `oss://{BUCKET_NAME}/path/to/jindosdk-bootstrap-patches.tar.gz` and `oss://{BUCKET_NAME}/path/to/bootstrap_jindosdk.sh`.

### 3. Add Bootstrap Action in the EMR Console

Refer to [Managing Bootstrap Actions](https://help.aliyun.com/document_detail/398732.html) for detailed instructions on adding a bootstrap action in the EMR console.

Fill in the configuration fields as follows:

| Parameter           | Description                                                                                    | Example                                                   |
|---------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| **Name**            | Name of the bootstrap action, e.g., Update JINDOSDK                                            | update_jindosdk                                          |
| **Script Location** | Specify the location of the script in OSS. Format must be oss://**/*.sh                        | oss://{BUCKET_NAME}/path/to/patch/bootstrap_jindosdk.sh      |
| **Arguments**       | Parameters for the bootstrap action script, specifying values for variables used in the script | -bootstrap oss://{BUCKET_NAME}/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **Execution Scope** | Select **Cluster**                                                                             |                                                           |
| **Execution Time**   | Choose **Before Component Startup**                                                            |                                                           |
| **Failure Strategy** | Select **Continue Execution**                                                                  |                                                           |

## Scenario Four: Rolling Back to the Default JindoSDK Version in an Existing Cluster

If you have an E-MapReduce cluster created with the new management console version EMR-5.6.0 or EMR-3.40.0 or later, and encounter issues during an upgrade that requires reverting to the cluster's default JindoSDK version, follow these steps.

### 1. Prepare the Revert Script

Log in to the Master node of your EMR cluster as `emr-user` and place the downloaded patch package in the `emr-user` home directory, then extract it and proceed with `emr-user` user execution.

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
cd jindosdk-patches
ls -l
```

The `jindosdk-patches` folder should contain:

```bash
-rwxrwxr-x 1 emr-user emr-user      2439 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 emr-user emr-user      7315 May 01 00:00 apply.sh
-rw-rw-r-- 1 emr-user emr-user        40 May 01 00:00 hosts
-rwxrwxr-x 1 emr-user emr-user      1112 May 01 00:00 revert_all.sh
-rwxrwxr-x 1 emr-user emr-user      2042 May 01 00:00 revert.sh
```

### 2. Configure Revert Node Information

Edit the `hosts` file in the patch package to include all cluster node hostnames, such as `master-1-1` or `core-1-1`, with each hostname on a separate line.

```bash
cd jindosdk-patches
vim hosts
```

The `hosts` file content might look like this:

```bash
master-1-1
core-1-1
core-1-2
```

Try fetching all node information with a script; if `hosts` retrieval fails, manually complete it:

```bash
cat /usr/local/taihao-executor-all/data/cache/.cluster_context | jq --raw-output '.nodes[].hostname.alias[]' > hosts
```

### 3. Perform the Rollback

Execute the `revert_all.sh` script to initiate the rollback process.

```bash
./revert_all.sh
```

Upon completion, you'll see a message like:

```
>>> updating ... master-1-1
>>> updating ... core-1-1
>>> updating ... core-1-2
# # # DONE
```

**Note**: For YARN applications (such as Spark Streaming or Flink jobs) currently running, stop them before rolling-restarting YARN NodeManagers.

### 4. Restart Services After Reversion

Components like Hive, Presto, Impala, Flink, Ranger, Spark, and Zeppelin require a restart to fully revert to their previous state.

For Hive, for example, go to the Hive service page in your EMR cluster and select 'More Operations' > 'Restart' from the top right corner.

By following these steps, you can successfully roll back your JindoSDK to the default version in your existing E-MapReduce cluster.