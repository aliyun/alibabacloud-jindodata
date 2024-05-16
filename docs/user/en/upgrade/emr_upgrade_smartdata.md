# EMR Cluster JindoSDK Upgrade Procedure (Legacy Console)

## Context

For E-MapReduce clusters created with the old management console, versions prior to EMR-5.5.0 or EMR-3.39.0, if you encounter issues or need new JindoSDK features, follow these steps for upgrading.

## Scenario One: Upgrading an Existing Cluster

### Step 1: Prepare Software Package and Upgrade Script

Login to the Master node of your EMR cluster as `hadoop`, place the downloaded patch package in the user's HOME directory, extract it, then proceed with `hadoop` user execution.

```bash
su - hadoop
cd /home/hadoop/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
```

Download the JindoSDK software package `jindosdk-{VERSION}-{PLATFORM}.tar.gz` (assuming a Linux x86 platform) into the extracted folder:

```bash
cd jindosdk-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.4.0/jindosdk-6.4.0-linux.tar.gz

ls -l
```

Your `jindosdk-patches` folder should show:

```bash
-rwxrwxr-x 1 hadoop hadoop      1263 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 hadoop hadoop      6840 May 01 00:00 apply.sh
-rw-rw-r-- 1 hadoop hadoop        40 May 01 00:00 hosts
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.4.0-linux.tar.gz
-rwxrwxr-x 1 hadoop hadoop      1308 May 01 00:00 revert_all.sh
-rwxrwxr-x 1 hadoop hadoop      6326 May 01 00:00 revert.sh
```

### Step 2: Configure Upgrade Node Information

Edit the `hosts` file in the patch package with all cluster node hostnames like `emr-header-1` or `emr-worker-1`, each on a separate line.

```bash
cd jindosdk-patches
vim hosts
```

Example `hosts` file content:

```bash
emr-header-1
emr-worker-1
emr-worker-2
```

### Step 3: Perform Upgrade

Execute the `apply_all.sh` script to initiate the upgrade process.

```bash
./apply_all.sh $JINDOSDK_VERSION
```

For example:

```bash
./apply_all.sh 6.4.0
```

Upon successful completion, you'll see this output:

```
>>> updating ... emr-worker-1
>>> updating ... emr-worker-2
# # # DONE
```

**Note**: For YARN applications (such as Spark Streaming or Flink jobs) currently running, stop them before rolling-restarting YARN Node Managers.

### Step 4: Modify Cluster Configurations

#### 4.1 Compatibility with Old JindoSDK Versions (Upgrade to JindoSDK v6.1.1+)

a. In SmartData's **Configuration** section, click the **smartdata-site** tab.
b. On the **smartdata-site** page, search for the parameter **fs.oss.legacy.version**.
c. Set its value to the major.minor version number of the older smartdata release you need compatibility with. For example, set it to `3.8`.

**Note**: This has been tested only with v3.8.x; use caution with other versions.

#### 4.2 Modify Hive/Spark Configurations

a. Go to the corresponding component's settings page (e.g., Hive's **hive-site.xml**).
b. Search for the configuration parameter **hive.exec.post.hooks**.
c. Remove any occurrences of `com.aliyun.emr.table.hive.HivePostHook`(or `com.aliyun.jindotable.hive.HivePostHook`) from the value and retain other hooks, such as `com.aliyun.emr.meta.hive.hook.LineageLoggerHook`.
d. Save changes and redeploy configurations. If not present, skip this step.

Here's an example table showing how to modify configurations for different components:

| Component | Configuration File | Parameter                         | Description                                                                                           |
|-----------|-------------------|----------------------------------|------------------------------------------------------------------------------------------------------|
| HIVE      | hive-site.xml     | hive.exec.post.hooks               | Remove `com.aliyun.emr.table.hive.HivePostHook`(or `com.aliyun.jindotable.hive.HivePostHook`).     |
| SPARK     | spark-defaults.conf | spark.sql.queryExecutionListeners | Remove `com.aliyun.emr.table.spark.SparkSQLQueryListener`(or `com.aliyun.jindotable.spark.SparkSQLQueryListener`). |

### Step 5: Restart Services After Upgrade

Components like Hive, Presto, Impala, Druid, Flink, Solr, Ranger, Storm, Oozie, Spark, and Zeppelin require a restart after the upgrade.

For Hive, for instance, go to your EMR cluster's Hive service page, click 'Operations' in the top-right corner, then choose 'Restart All Components'.

### Step 6: Adjust Log Levels

You can adjust log levels for Hadoop commands, Spark job logs, etc., using log4j properties. Here's how to change Hadoop command logs:

```bash
vim /etc/ecm/hadoop-conf/log4j.properties
```

Add these lines to control logging levels:

```properties
log4j.logger.com.aliyun.jindodata=WARN
log4j.logger.com.aliyun.jindodata.common.FsStats=INFO
```

If you need more verbose logs, remove the WARN level from the first line.

## Scenario Two: Expanding an Existing Cluster

To perform an auto-upgrade and repair during cluster expansion, follow these steps:

### Step 1: Create a Bootstrap Upgrade Package

Download `jindosdk-patches.tar.gz`, `jindosdk-6.4.0-linux.tar.gz`, and `[bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh)`.

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.4.0/jindosdk-6.4.0-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh

ls -l
```

Content example:

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.4.0-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

Create the upgrade package with:

```bash
bash bootstrap_jindosdk.sh -gen $JINDOSDK_VERSION
```

For example:

```bash
bash bootstrap_jindosdk.sh -gen 6.4.0
```
**Explanation of parameters: `-gen` generates a lite upgrade package, `-gen-full` indicates generating a full upgrade package.**

Upon success, you'll see:

```bash
Generated patch at /home/hadoop/jindo-patch/jindosdk-bootstrap-patches.tar.gz
```

This creates the `jindosdk-bootstrap-patches.tar.gz` patch.

### Step 2: Upload Bootstrap Upgrade Package

Upload the patch package and bootstrap script to OSS using Hadoop commands within your EMR cluster or tools like oss console, ossutil, or OSS Browser.

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

cd /home/hadoop/jindo-patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://<bucket-name>/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://<bucket-name>/path/to/patch/

hadoop dfs -ls oss://<bucket-name>/path/to/patch/
```

Assuming you've uploaded to these paths in OSS: `oss://<bucket-name>/path/to/jindosdk-bootstrap-patches.tar.gz` and `oss://<bucket-name>/path/to/bootstrap_jindosdk.sh`.

### Step 3: Add Bootstrap Action in EMR Console

Refer to [Managing Bootstrap Actions](https://help.aliyun.com/document_detail/28108.htm#concept-q52-vln-y2b) for detailed instructions on adding a bootstrap action in the EMR console.

Fill in configuration fields as follows:

| Parameter             | Description                                                  | Example                                                         |
| :-------------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **Name**              | Name of the bootstrap action. For example, Upgrade JINDOSDK    | update_jindosdk                                              |
| **Script Location**    | Specify the location of the script in OSS. Format must be oss://**/*.sh | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **Arguments**         | Parameters for the bootstrap action script, specifying values for variables used in the script | -bootstrap oss://<bucket-name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **Execution Scope**    | Select **Cluster**                                            |                                                              |
| **Execution Time**     | Choose **After Component Startup**                             |                                                              |
| **Execution Failure Strategy** | Select **Continue Execution**                                   |                                                              |

### Step 4: Ensure Latest Fixes Are Loaded

* For newly created clusters, restart components like Hive, Presto, Impala, Druid, Flink, Solr, Ranger, Storm, Oozie, Spark, and Zeppelin.
* For newly added nodes during expansion, restart these components on those nodes.

# Scenario Three: Creating a New Cluster

When creating a new E-MapReduce (EMR) cluster, you can add a bootstrap action in the EMR console for automatic upgrades and repairs. The steps are as follows:

### Step 1: Create a Bootstrap Upgrade Package

Download the `jindosdk-patches.tar.gz`, `jindosdk-6.4.0-linux.tar.gz`, and `[bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh)` files.

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.4.0/jindosdk-6.4.0-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh

ls -l
```

The contents will appear as:

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.4.0-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

Run the command to generate the upgrade package:

```bash
bash bootstrap_jindosdk.sh -gen-full $JINDOSDK_VERSION
```

For example,

```bash
bash bootstrap_jindosdk.sh -gen-full 6.4.0
```
**Explanation of parameters:** `-gen` generates a lite upgrade package, while `-gen-full` indicates generating a full upgrade package.

Upon success, you'll see this:

```bash

Generated patch at /home/hadoop/jindo-patch/jindosdk-bootstrap-patches.tar.gz

```

This means you've created the `jindosdk-bootstrap-patches.tar.gz` patch package.

### Step 2: Upload Bootstrap Upgrade Package

Upload the patch package and bootstrap script to OSS (Object Storage Service).

Within your EMR cluster, use Hadoop commands or tools like the OSS console, ossutil, or OSS Browser.

```bash
hadoop dfs -mkdir -p oss://<bucket_name>/path/to/patch/

cd /home/hadoop/jindo-patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://<bucket_name>/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://<bucket_name>/path/to/patch/

hadoop dfs -ls oss://<bucket_name>/path/to/patch/
```

You should see something like this upon successful upload:

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket_name>/.../bootstrap_jindosdk.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket_name>/.../jindosdk-bootstrap-patches.tar.gz
```

For example, upload to these OSS paths: `oss://<bucket_name>/path/to/jindosdk-bootstrap-patches.tar.gz` and `oss://<bucket_name>/path/to/bootstrap_jindosdk.sh`.

### Adding a Bootstrap Action in the EMR Console

Follow the details in [Managing Bootstrap Actions](https://help.aliyun.com/document_detail/28108.htm#concept-q52-vln-y2b) to add a bootstrap action in the EMR console.

In the **Add Bootstrap Action** dialog, fill out the configuration fields:

| Parameter             | Description                                                  | Example                                                         |
| :-------------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **Name**              | Name of the bootstrap action, e.g., Upgrade JINDOSDK          | update_jindosdk                                              |
| **Script Location**    | Specify the location of the script in OSS. Format must be oss://**/*.sh | oss://<bucket_name>/path/to/patch/bootstrap_jindosdk.sh      |
| **Arguments**         | Parameters for the bootstrap action script, specifying values for variables used in the script | -bootstrap oss://<bucket_name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **Execution Scope**    | Select **Cluster**                                            |                                                              |
| **Execution Time**     | Choose **After Component Startup**                             |                                                              |
| **Execution Failure Strategy** | Select **Continue Execution**                                   |                                                              |

### Ensuring Latest Fixes Are Applied

* For a new cluster, restart components like Hive, Presto, Impala, Druid, Flink, Solr, Ranger, Storm, Oozie, Spark, and Zeppelin.
* For newly added nodes during scaling up, restart these components on those nodes.

## Scenario Four: Rolling Back to the Initial Cluster JindoSDK Version

### Step 1: Prepare Revert Script

Login to the Master node of your EMR cluster as `hadoop`, place the downloaded patch package in the user's HOME directory, extract it, then execute operations as `hadoop` user.

```bash
su - hadoop
cd /home/hadoop/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
cd jindosdk-patches
ls -l
```

The `jindosdk-patches` folder should contain:

```bash
-rwxrwxr-x 1 hadoop hadoop      1263 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 hadoop hadoop      6840 May 01 00:00 apply.sh
-rw-rw-r-- 1 hadoop hadoop        40 May 01 00:00 hosts
-rwxrwxr-x 1 hadoop hadoop      1308 May 01 00:00 revert_all.sh
-rwxrwxr-x 1 hadoop hadoop      6326 May 01 00:00 revert.sh
```

### Step 2: Configure Revert Node Information

Edit the `hosts` file in the patch package, adding all cluster node hostnames like `emr-header-1` or `emr-worker-1`, with each hostname on a separate line.

```bash
cd jindo-patches
vim hosts
```

The `hosts` file content could look like this:

```bash
emr-header-1
emr-worker-1
emr-worker-2
```

## Step 3: Execute Rollback

Use the `revert_all.sh` script to perform the rollback operation.

```bash
./revert_all.sh
```

Upon completion, you'll receive this confirmation:

```
>>> updating ... emr-worker-1
>>> updating ... emr-worker-2
# # # DONE
```

**Note:** For YARN applications (like Spark Streaming or Flink jobs) currently running, stop them before rolling-restarting YARN Node Managers.

### Step 4: Restart Services After Reverting

Hive, Presto, Impala, Druid, Flink, Solr, Ranger, Storm, Oozie, Spark, and Zeppelin components need restarting after reverting.

For Hive, for instance, navigate to your EMR cluster's Hive service page and click 'Operations' > 'Restart All Components' in the top-right corner.
