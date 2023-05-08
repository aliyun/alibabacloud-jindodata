# 阿里云 OSS-HDFS 服务（JindoFS 服务）分层存储使用说明
*(从 4.6.6 开始支持)*


阿里云 OSS-HDFS 服务（JindoFS 服务）是 OSS 推出新的存储空间类型，兼容HDFS接口, 支持目录以及目录层级，通过 Jindo SDK 4.x 可以兼容访问 OSS-HDFS 服务。关于用户如何创建和使用 OSS-HDFS 服务的基本功能，请参考[链接](jindo_dls_quickstart.md)。

本文主要介绍在 OSS-HDFS 服务中使用分层存储功能的一些常见操作。

为了方便下面命令的介绍，我们假设`oss://oss-dfs-test`这个Bucket已经开启 OSS-HDFS 服务。下面所有的例子都会针对这个Bucket进行操作。

## 分层存储简介

数据湖存储的数据量常常会随着时间而不断增长，但在数据湖内的数据并非同样频繁访问。对于较老的数据，比如超过一年的数据，业务上可能几乎不再访问，但为保证合规、存档等目的，这些数据仍然需要存储以备不时之需。因此，分层存储的需求就出现了：对于较少访问的数据，选择较低成本的存储介质（通常会有更高的访问成本）进行存储，可以降低总存储成本。

标准 OSS Bucket 支持多种分层存储类型，包括标准、低频、归档、冷归档四种，而 JindoFS 服务使用标准 OSS 作为数据块的存储后端，因此 JindoFS 分层存储功能即设置后端数据块在标准 OSS 上所使用的存储类型。

## 使用分层存储

在使用相关功能前，应确保 JindoSDK 已经正确配置，可以正常访问 JindoFS 服务。JindoFS 服务的分层存储功能与 HCFS API 兼容。相关 API 如下所示：

```java
public abstract class FileSystem extends Configured implements Closeable {
    // ......
    public void setStoragePolicy(Path src, String policyName) throws IOException;
    public BlockStoragePolicySpi getStoragePolicy(Path src) throws IOException;
    public Collection<? extends BlockStoragePolicySpi> getAllStoragePolicies() throws IOException;
}
```

JindoFS 也支持使用下列命令操作存储策略：

```bash
jindo fs -listPolicies
jindo fs -setStoragePolicy -path <path> -policy <policy>
jindo fs -getStoragePolicy -path <path>
```

`getAllStoragePolicies()`接口和`listPolicies`命令返回当前支持的存储策略，目前支持的存储策略有 `CLOUD_STD`（标准存储）、 `CLOUD_IA`（低频存储）、 `CLOUD_AR`（归档）和 `CLOUD_COLD_AR`（冷归档）。

`setStoragePolicy()`接口和`setStoragePolicy`命令可以为具体的路径设置存储策略，JindoFS 服务会在后台根据存储策略改变存储对象的存储级别。例如，要将 `oss://oss-dfs-test/dir1` 设置为归档类型，可以使用如下命令：

```bash
jindo fs -setStoragePolicy -path oss://oss-dfs-test/dir1 -policy CLOUD_AR
```

注意文件或目录的默认存储策略为空，显示为`UNSPECIFIED`，当对象存储策略为`UNSPECIFIED`时，对象实际的存储策略将由最近的设置了存储策略的父节点决定。
例如假设`oss://oss-dfs-test/dir2`的存储策略为`CLOUD_STD`，则`oss://oss-dfs-test/dir2/subdir2/subsubdir2`的存储策略也为`CLOUD_STD`，
除非用户对`oss://oss-dfs-test/dir2/subdir2/`或`oss://oss-dfs-test/dir2/subdir2/subsubdir2`单独设置过存储策略。

用户可以通过`getStoragePolicy()`接口和`getStoragePolicy`命令查询某一路径最终生效的存储策略，如：

```bash
jindo fs -getStoragePolicy -path oss://oss-dfs-test/dir1/file1
```

如果最终生效的存储策略为`UNSPECIFIED`（即`getStoragePolicy()`接口返回`null`），则对象默认使用标准存储。

## 目录嵌套

目前 JindoFS <span style="color:red">**不支持嵌套策略**</span>, 例如对 `oss://oss-dfs-test/warehouse/dwd.db/dt=20220101` 使用了`CLOUD_AR`策略, 再对 `oss://oss-dfs-test/warehouse/dwd.db` 父目录使用`CLOUD_STD`策略, 是不会执行解归档操作的. 因此建议<span style="color:red">**只对之前设置过policy的目录修改policy, 不要再修改其上下级目录的policy.**</span>

## 各种场景下 Policy 互转的例子
```bash
标准 转 低频IA
jindo fs -setStoragePolicy -path <path> -policy CLOUD_IA

标准 转 归档CLOUD_AR
jindo fs -setStoragePolicy -path <path> -policy CLOUD_AR

标准 转 冷归档CLOUD_COLD_AR
jindo fs -setStoragePolicy -path <path> -policy CLOUD_COLD_AR

低频IA 转 归档CLOUD_AR
jindo fs -setStoragePolicy -path <path> -policy CLOUD_AR

低频IA 转 冷归档CLOUD_COLD_AR
jindo fs -setStoragePolicy -path <path> -policy CLOUD_COLD_AR

低频IA 转 标准
jindo fs -setStoragePolicy -path <path> -policy CLOUD_STD

归档 转 冷归档 
不支持

冷归档 转 归档 
不支持

归档 转 标准
jindo fs -setStoragePolicy -path <path> -policy CLOUD_STD

冷归档 转 标准
jindo fs -setStoragePolicy -path <path> -policy CLOUD_STD
```

## 设置OSS生命周期
在使用分层存储功能将OSS-HDFS的数据进行归档的时候，需要依赖于OSS生命周期功能，通过下面命令
```bash
jindo fs -setStoragePolicy -path oss://oss-dfs-test/dir1 -policy CLOUD_AR
```
进行数据归档操作，该操作只会将对应目录下的数据进行Tag标记, Tag对应的键值对为transition-storage-class:Archive，需要设置生命周期进行数据的转换，设置按前缀匹配策略规则，前缀为.dlsdata, 具体归档的生命周期策略设置如下：

![生命周期设置](../pic/lifecycle.png)

 上述生命周期策略设置完毕后，用户需要等待改OSS生命周期策略被调度执行才能完成数据的归档。如何设置生命周期可以参考[文档](https://help.aliyun.com/document_detail/31904.html)。

### 注意事项

* 如果需要使用归档/冷归档的存储策略，需要在 OSS 控制台上配置相应的生命周期策略，否则存储的数据块无法真正转为归档/冷归档类型。
* 在 JindoFS 上查询到的存储状态仅表示 JindoFS 已按预设的生命周期策略下发到对应的 OSS 数据块，不表示数据块实际的存储类型已转变完成。
* OSS 的生命周期管理运行周期较长，设置归档存储策略的对象可能需要最长24小时左右才会真正转为归档/冷归档类型。
* 归档/冷归档类型的数据无法访问，需要转回标准存储方可访问。转回标准存储可能需要一定时间，可以通过`checkStoragePolicy`命令查询执行状态。
* 取回归档/冷归档数据会产生一些额外费用，应当尽量避免将还需访问的数据转为归档/冷归档类型。
* 对于存储策略不一致的目录间的文件 rename 操作，rename 的同时会自动生成相应的后台任务，使 rename 后的文件最终符合目标路径的存储策略。
* 当前版本不支持在设置为低频/归档/冷归档存储策略的目录下创建文件。若要在低频/归档/冷归档的目录下添加文件，可以在标准目录下创建并关闭文件后，rename 到目标的低频/归档/冷归档目录。
