# EMR Cluster JindoSDK Upgrade Process (Legacy Console)

## Background

In E-MapReduce clusters created with the legacy management console, EMR-5.6.0 or EMR-3.40.0 and above, if you encounter issues or need to use new JindoSDK features, refer to the [version documentation](../releases.md) and follow these steps for upgrading JindoSDK.

## Scenario One: Upgrading an Existing Cluster

### Step 1: Prepare Software Package and Upgrade Script

Log in to the Master node of your EMR cluster as `hadoop`, place the downloaded patch package in the user's HOME directory, extract it, then proceed with `hadoop` user execution.

```bash
su - hadoop
cd /home/hadoop/
wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz
tar zxf jindosdk-patches.tar.gz
```

Download the JindoSDK software package `jindosdk-{VERSION}-{PLATFORM}.tar.gz`, placing it in the extracted folder (using a Linux x86 platform example here):

```bash
cd jindosdk-patches

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.8.1/jindosdk-6.8.1-linux.tar.gz

ls -l
```

Your `jindosdk-patches` folder should display:

```bash
-rwxrwxr-x 1 hadoop hadoop      1263 May 01 00:00 apply_all.sh
-rwxrwxr-x 1 hadoop hadoop      6840 May 01 00:00 apply.sh
-rw-rw-r-- 1 hadoop hadoop        40 May 01 00:00 hosts
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.8.1-linux.tar.gz
```

> **Caution**: When upgrading from versions below 4.6.8 to 4.6.9 or higher, or to version 6.x, first set `fs.jdo.committer.allow.concurrent=false` in `core-site.xml`
> or configure Spark with `spark.hadoop.fs.jdo.committer.allow.concurrent=false`, ensuring no data loss during the upgrade process.
> After all JindoSDK instances across GATEWAY nodes have been upgraded, you can remove this configuration at an opportune moment.

### Step 2: Configure Upgrade Node Information

Edit the `hosts` file in the patch package, adding all cluster node hostnames like `emr-header-1` or `emr-worker-1`, with each hostname on a separate line.

```bash
cd jindosdk-patches
vim hosts
```

An example `hosts` file content is:

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
./apply_all.sh 6.8.1
```

Upon successful completion, you'll see the following output:

```
>>> updating ... emr-worker-1
>>> updating ... emr-worker-2
# # # DONE
```

**Note**: For YARN applications (such as Spark Streaming or Flink jobs) currently running, stop them before rolling-restarting YARN Node Managers.

### Step 4: Restart Services After Upgrade

Components like Hive, Presto, Impala, Druid, Flink, Solr, Ranger, Storm, Oozie, Spark, and Zeppelin require a restart after upgrade.

For Hive, in your EMR cluster's Hive service page, click 'Operations' in the upper right corner and choose 'Restart All Components'.

## Scenario Two: New Cluster Creation & Expansion

When creating a new EMR cluster or expanding an existing one, you can add a bootstrap action in the EMR console for automatic upgrade and repair during cluster creation or expansion. Follow these steps:

### Step 1: Create a Bootstrap Upgrade Package

Download `jindosdk-patches.tar.gz`, `jindosdk-{VERSION}-{PLATFORM}.tar.gz`, and `[bootstrap_jindosdk.sh](https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh)`:

```bash
mkdir jindo-patch

cd jindo-patch

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/jindosdk-patches.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.8.1/jindosdk-6.8.1-linux.tar.gz

wget https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/resources/bootstrap_jindosdk.sh

ls -l
```

Your `jindo-patch` folder should contain:

```bash
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 bootstrap_jindosdk.sh
-rw-r----- 1 hadoop hadoop xxxxxxxxx May 01 00:00 jindosdk-6.8.1-linux.tar.gz
-rw-r----- 1 hadoop hadoop      xxxx May 01 00:00 jindosdk-patches.tar.gz
```

Create the upgrade package by executing this command:

```bash
bash bootstrap_jindosdk.sh -gen 6.8.1
```
**Explanation of parameters: `-gen` generates a lite upgrade package, `-gen-full` indicates generating a full upgrade package.**

Upon success, you'll see:

```bash
Generated patch at /home/hadoop/jindo-patch/jindosdk-bootstrap-patches.tar.gz
```

Now you have the `jindosdk-bootstrap-patches.tar.gz` patch.

### Step 2: Upload Bootstrap Upgrade Package

Upload the patch package and bootstrap script to OSS using Hadoop commands within your EMR cluster or tools like oss console, ossutil, or OSS Browser.

```bash
hadoop dfs -mkdir -p oss://<bucket-name>/path/to/patch/

cd /home/hadoop/jindo-patch/
hadoop dfs -put jindosdk-bootstrap-patches.tar.gz oss://<bucket-name>/path/to/patch/
hadoop dfs -put bootstrap_jindosdk.sh oss://<bucket-name>/path/to/patch/

hadoop dfs -ls oss://<bucket-name>/path/to/patch/
```

You should see a response like this:

```
Found 2 items
-rw-rw-rw-   1       2634 2022-05-13 14:07 oss://<bucket-name>/.../bootstrap_jindosdk.sh
-rw-rw-rw-   1  597342992 2022-05-13 13:41 oss://<bucket-name>/.../jindosdk-bootstrap-patches.tar.gz
```

For example, upload to these paths in OSS: `oss://<bucket-name>/path/to/jindosdk-bootstrap-patches.tar.gz` and `oss://<bucket-name>/path/to/bootstrap_jindosdk.sh`.

### Step 3: Add Bootstrap Action in EMR Console

Refer to [Managing Bootstrap Actions](https://help.aliyun.com/document_detail/28108.htm#concept-q52-vln-y2b) for detailed instructions on adding a bootstrap action in the EMR console.

In the **Add Bootstrap Action** dialog box, fill in the configuration fields as follows:

| Parameter             | Description                                                  | Example                                                         |
| :-------------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **Name**              | Name of the bootstrap action. For example, Upgrade JINDOSDK    | update_jindosdk                                              |
| **Script Location**    | Specify the location of the script in OSS. Format must be oss://**/*.sh | oss://<bucket-name>/path/to/patch/bootstrap_jindosdk.sh      |
| **Arguments**         | Parameters for the bootstrap action script, specifying values for variables used in the script | -bootstrap oss://<bucket-name>/path/to/patch/jindosdk-bootstrap-patches.tar.gz |
| **Execution Scope**    | Select **Cluster**                                            |                                                               |
| **Execution Time**     | Choose **After Component Startup**                             |                                                               |
| **Failure Strategy**   | Select **Continue Execution**                                  |                                                               |

### Step 4: Ensure Latest Fixes Are Loaded

* For a new cluster, restart components like Hive, Presto, Impala, Druid, Flink, Solr, Ranger, Storm, Oozie, Spark, and Zeppelin.
* For newly added nodes during expansion, restart these components on those nodes.

By following these steps, you can successfully upgrade the JindoSDK in both new and existing EMR clusters managed through the legacy console.