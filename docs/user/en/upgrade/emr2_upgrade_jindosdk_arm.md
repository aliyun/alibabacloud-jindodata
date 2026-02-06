# EMR New Version Cluster JindoSDK Upgrade Documentation (ARM/Tianhe Environment)

## Scenario One: Upgrading an Existing New Version Cluster

### If you have an E-MapReduce cluster created with the new management console version EMR-5.6.0 or EMR-3.40.0 or later, and encounter issues or need to use JindoSDK's new features, follow these steps to upgrade.

### Step 1: Prepare Software Package and Upgrade Script Configuration

Login to the Master node of your EMR cluster as `emr-user`, download the patch package into the user's HOME directory, extract it, then proceed with `emr-user` user execution.

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
```

Download the JindoSDK software package `jindosdk-{VERSION}-{PLATFORM}.tar.gz` into the extracted folder.

For upgrading the JindoSDK in a new cluster version to `6.10.4` on a Linux ARM environment:

```bash
cd jindosdk-patches
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.10.4/jindosdk-6.10.4-linux-el7-aarch64.tar.gz
ls -l
```

Your `jindosdk-patches` folder should show:

```bash
-rwxrwxr-x 1 emr-user emr-user       575 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 emr-user emr-user      4047 May 01 00:00 apply.sh
-rw-rw-r-- 1 emr-user emr-user        40 May 01 00:00 hosts
-rw-r----- 1 emr-user emr-user xxxxxxxxx May 01 00:00 jindosdk-6.10.4-linux-el7-aarch64.tar.gz
```

> **Note**: Before upgrading from versions below 4.6.8 to 4.6.9 or higher or to version 6.x, set `fs.jdo.committer.allow.concurrent=false` in `core-site.xml` or in Spark configurations (`spark.hadoop.fs.jdo.committer.allow.concurrent=false`) to prevent data loss during the upgrade process. After all JindoSDK instances across GATEWAY nodes have been upgraded, you can remove this configuration at an appropriate time.

### Step 2: Configure Upgrade Node Information

Edit the `hosts` file in the patch package with all cluster node hostnames, like `master-1-1` or `core-1-1`, with each hostname on a separate line.

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

Try fetching all node information with a script; if the `hosts` retrieval fails, manually complete it:

```bash
cat /usr/local/taihao-executor-all/data/cache/.cluster_context | jq --raw-output '.nodes[].hostname.alias[]' > hosts
```

### Step 3: Perform Upgrade

Execute the `apply_all.sh` script to initiate the upgrade.

```bash
./apply_all.sh {NEW_JINDOSDK_VERSION}
```

For upgrading to version `6.10.4`, run:

```bash
./apply_all.sh 6.10.4
```

Upon completion, you'll see a message like this:

```
>>> updating ... master-1-1
>>> updating ... core-1-1
>>> updating ... core-1-2
# # # DONE
```

**Note**: For YARN applications (e.g., Spark Streaming or Flink jobs) currently running, stop them before rolling-restarting YARN NodeManagers.

### Step 4: Restart Services After Upgrade

Components such as Hive, Presto, Impala, Flink, Ranger, Spark, and Zeppelin require a restart after the upgrade.

For Hive, for example, go to the Hive service page in your EMR cluster and select 'More Operations' > 'Restart' from the top-right corner.

## Scenario Two: Expanding an Existing Cluster

### To use a new JindoSDK version when expanding an existing cluster, add a bootstrap action in the EMR console for automatic upgrade and repair during cluster creation or expansion. Follow these steps for JindoSDK upgrade.

### Step 1: Create a Bootstrap Upgrade Package

Download `jindosdk-patches.tar.gz`, `jindosdk-{VERSION}-{PLATFORM}.tar.gz`, and `[bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh)`.

For upgrading the JindoSDK in a new cluster version to `6.10.4` on a Linux ARM environment:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.10.4/jindosdk-6.10.4-linux-el7-aarch64.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh

ls -l
```

Content example:

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.10.4-linux-el7-aarch64.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

Execute the command to create the upgrade package:

```bash
bash bootstrap_jindosdk.sh -gen {NEW_JINDOSDK_VERSION}
```

For upgrading to version `6.10.4`, run:

```bash
bash bootstrap_jindosdk.sh -gen 6.10.4
```
**Explanation of parameters: `-gen` generates a lite upgrade package, `-gen-full` indicates generating a full upgrade package.**

Upon success, you'll see this output:

```bash
Generated patch at /home/emr-user/jindo-patch/jindosdk-bootstrap-patches.tar.gz
```

This creates the patch package `jindosdk-bootstrap-patches.tar.gz`.

### Step 2: Upload Bootstrap Upgrade Package

Upload the patch package and bootstrap script to OSS using Hadoop commands within your EMR cluster or tools like the OSS console, ossutil, or OSS Browser.

```bash
hadoop dfs -mkdir -p oss://{BUCKET_NAME}/path/to/patch/

cd /home/hadoop/patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://{BUCKET_NAME}/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://{BUCKET_NAME}/path/to/patch/

hadoop dfs -ls oss://{BUCKET_NAME}/path/to/patch/
```

You should see a response like this:

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket-name>/.../bootstrap_jindosdk.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket-name>/.../jindosdk-bootstrap-patches.tar.gz
```

Assuming you've uploaded to `oss://{BUCKET_NAME}/path/to/jindosdk-bootstrap-patches.tar.gz` and `oss://{BUCKET_NAME}/path/to/bootstrap_jindosdk.sh`.

### Step 3: Add Bootstrap Action in the EMR Console

Refer to [Managing Bootstrap Actions](https://help.aliyun.com/document_detail/398732.html) for detailed instructions on adding a bootstrap action in the EMR console.

Fill in configuration fields as follows:

| Parameter           | Description                                                                                    | Example                                                   |
|---------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| **Name**            | Name of the bootstrap action, e.g., Update JINDOSDK                                            | update_jindosdk                                              |
| **Script Location** | Specify the location of the script in OSS. Format must be oss://**/*.sh                        | oss://{BUCKET_NAME}/path/to/patch/bootstrap_jindosdk.sh      |
| **Arguments**       | Parameters for the bootstrap action script, specifying values for variables used in the script | -bootstrap oss://{BUCKET_NAME}/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **Execution Scope** | Select **Cluster**                                                                             |                                                            |
| **Execution Time**   | Choose **Before Component Startup**                                                            |                                                            |
| **Failure Strategy** | Select **Continue Execution**                                                                  |                                                            |

### Step 4: Ensure Latest Fixes Are Loaded

* For newly created clusters, restart components like Hive, Presto, Impala, Flink, Ranger, Spark, and Zeppelin.
* For newly added nodes during expansion, restart these components on those nodes.

## Scenario Three: Creating a New Cluster

### If you're creating a new EMR cluster with a new JindoSDK version, add a bootstrap action in the EMR console to automatically upgrade and repair during cluster creation or expansion. Follow these steps for JindoSDK upgrade.

### Step 1: Create a Bootstrap Upgrade Package

Download `jindosdk-patches.tar.gz`, `jindosdk-{VERSION}-{PLATFORM}.tar.gz`, and `[bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh)`.

For upgrading the JindoSDK in a new cluster version to `6.10.4` on a Linux ARM environment:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.10.4/jindosdk-6.10.4-linux-el7-aarch64.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/bootstrap_jindosdk.sh

ls -l
```

Content example:

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.10.4-linux-el7-aarch64.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

Execute the command to create the upgrade package:

```bash
bash bootstrap_jindosdk.sh -gen-full {NEW_JINDOSDK_VERSION}
```

For upgrading to version `6.10.4`, run:

```bash
bash bootstrap_jindosdk.sh -gen-full 6.10.4
```
**Explanation of parameters: `-gen` generates a lite upgrade package, `-gen-full` indicates generating a full upgrade package.**

Upon success, you'll see this output:

```bash
Generated patch at /home/emr-user/jindo-patch/jindosdk-bootstrap-patches.tar.gz
```

This creates the patch package `jindosdk-bootstrap-patches.tar.gz`.

### Step 2: Upload Bootstrap Upgrade Package

Upload the patch package and bootstrap script to OSS using Hadoop commands within your EMR cluster or tools like the OSS console, ossutil, or OSS Browser.

```bash
hadoop dfs -mkdir -p oss://{BUCKET_NAME}/path/to/patch/

cd /home/hadoop/patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://{BUCKET_NAME}/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://{BUCKET_NAME}/path/to/patch/

hadoop dfs -ls oss://{BUCKET_NAME}/path/to/patch/
```

Assuming you've uploaded to `oss://{BUCKET_NAME}/path/to/jindosdk-bootstrap-patches.tar.gz` and `oss://{BUCKET_NAME}/path/to/bootstrap_jindosdk.sh`.

### Step 3: Add Bootstrap Action in the EMR Console

Refer to [Managing Bootstrap Actions](https://help.aliyun.com/document_detail/398732.html) for detailed instructions on adding a bootstrap action in the EMR console.

Fill in configuration fields as follows:

| Parameter           | Description                                                                                    | Example                                                   |
|---------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| **Name**            | Name of the bootstrap action, e.g., Update JINDOSDK                                            | update_jindosdk                                              |
| **Script Location** | Specify the location of the script in OSS. Format must be oss://**/*.sh                        | oss://{BUCKET_NAME}/path/to/patch/bootstrap_jindosdk.sh      |
| **Arguments**       | Parameters for the bootstrap action script, specifying values for variables used in the script | -bootstrap oss://{BUCKET_NAME}/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **Execution Scope** | Select **Cluster**                                                                             |                                                            |
| **Execution Time**   | Choose **Before Component Startup**                                                            |                                                            |
| **Failure Strategy** | Select **Continue Execution**                                                                  |                                                            |

### Step 4: Ensure Latest Fixes Are Loaded

* For newly created clusters, restart components like Hive, Presto, Impala, Flink, Ranger, Spark, and Zeppelin.
* For newly added nodes during expansion, restart these components on those nodes.

## Scenario Four: Rolling Back to Initial Cluster JindoSDK Version

### If you have an E-MapReduce cluster created with the new management console version EMR-5.6.0 or EMR-3.40.0 or later, and need to roll back to the initial JindoSDK version due to issues encountered during an upgrade, follow these steps.

### Step 1: Prepare Revert Script

Login to the Master node of your EMR cluster as `emr-user`, download the patch package into the user's HOME directory, extract it, then proceed with `emr-user` user execution.

```bash
su - emr-user
cd /home/emr-user/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/emr-taihao/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
cd jindosdk-patches
ls -l
```

Your `jindosdk-patches` folder should show:

```bash
-rwxrwxr-x 1 emr-user emr-user       575 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 emr-user emr-user      4047 May 01 00:00 apply.sh
-rw-rw-r-- 1 emr-user emr-user        40 May 01 00:00 hosts
-rwxrwxr-x 1 emr-user emr-user      1112 May 01 00:00 revert_all.sh
-rwxrwxr-x 1 emr-user emr-user      2042 May 01 00:00 revert.sh
```

### Step 2: Configure Revert Node Information

Edit the `hosts` file in the patch package with all cluster node hostnames, like `master-1-1` or `core-1-1`, with each hostname on a separate line.

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

Try fetching all node information with a script; if the `hosts` retrieval fails, manually complete it:

```bash
cat /usr/local/taihao-executor-all/data/cache/.cluster_context | jq --raw-output '.nodes[].hostname.alias[]' > hosts
```

### Step 3: Perform Rollback

Execute the `revert_all.sh` script to initiate the rollback process.

```bash
./revert_all.sh
```

Upon completion, you'll see a message like this:

```
>>> updating ... master-1-1
>>> updating ... core-1-1
>>> updating ... core-1-2
# # # DONE
```

**Note**: For YARN applications (e.g., Spark Streaming or Flink jobs) currently running, stop them before rolling-restarting YARN NodeManagers.

### Step 4: Restart Services After Rollback

Components such as Hive, Presto, Impala, Flink, Ranger, Spark, and Zeppelin require a restart after rollback.

For Hive, for example, go to the Hive service page in your EMR cluster and select `More Operations` > `Restart` from the top-right corner.