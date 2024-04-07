# Using RootPolicy to Access the OSS-HDFS Service (JindoFS Service) \(Supported Since v4.6.0\)

## Context

JindoFS supports the RootPolicy rule, allowing users to set custom prefixes for accessing the OSS-HDFS (JindoFS service). This enables existing jobs that use the `hdfs://` prefix to run seamlessly on the JindoFS service without modifications.

## 1. Set Up RootPolicy

A set of shell commands is provided to register a custom address for a specific bucket. Use the [SetRootPolicy command](../jindofs/jindofs_client_tools.md) to register a custom prefix for a given bucket, as shown below:

```shell
jindo admin -setRootPolicy oss://<bucket_name>/<dls_endpoint>/ hdfs://<your_ns_name>/
```

* `<bucket_name>`: The name of the OSS-HDFS service bucket. Currently, only root directories are supported.
* `<dls_endpoint>`: The endpoint of the OSS-HDFS service, e.g., `cn-hangzhou.oss-dls.aliyuncs.com`. If your `core-site.xml` has the following configuration:
```xml
<configuration>
    <property>
        <name>fs.oss.endpoint</name>
        <value><dls_endpoint></value>
    </property>
    或者
    <property>
        <name>fs.oss.bucket.<bucket_name>.endpoint</name>
        <value><dls_endpoint></value>
    </property>
</configuration>
```
In this case, you can abbreviate `oss://<bucket_name>/<dls_endpoint>/` to `oss://<bucket_name>/` in places where the endpoint is required throughout this document.
* `<your_ns_name>`: A custom namespace name for accessing HDFS, any non-empty string, e.g., `test`. Only root directories are supported in the current version.

## 2. Configure Access Policy Discovery Address and Scheme Implementation Class

Add the following entries to your `core-site.xml`:

```xml
<configuration>
    <property>
        <name>fs.accessPolicies.discovery</name>
        <value>oss://<bucket_name>/<dls_endpoint>/</value>
    </property>
    <property>
        <name>fs.AbstractFileSystem.hdfs.impl</name>
        <value>com.aliyun.jindodata.hdfs.HDFS</value>
    </property>
    <property>
        <name>fs.hdfs.impl</name>
        <value>com.aliyun.jindodata.hdfs.JindoHdfsFileSystem</value>
    </property>
</configuration>
```
For multiple buckets, separate them with commas.

## 3. Verify and Use

After configuring, test the setup using a simple `hadoop` command:

```shell
hadoop fs -ls hdfs://<your_ns_name>/
```

Once validated, restart relevant services like Hive or Spark to utilize the custom prefix for accessing the OSS-HDFS service.

## 4. Additional Features

## 4.1 Remove RootPolicy Rule

Delete the corresponding rule with the [UnsetRootPolicy command](../jindofs/jindofs_client_tools.md):

```shell
./jindofs admin -unsetRootPolicy oss://<bucket_name>/<dls_endpoint>/ hdfs://<your_ns_name>/
```

## 4.2 List All RootPolicy Rules

List all registered prefix addresses for a particular bucket using the [ListAccessPolicies command](../jindofs/jindofs_client_tools.md):

```shell
./jindofs admin -listAccessPolicies oss://<bucket_name>/<dls_endpoint>/
```