# Sqoop 使用 Kite SDK 访问 OSS

## 环境要求

在集群上有开源版本 Sqoop 软件，版本不低于 1.4.7。

## 为什么 Sqoop 需要使用 Kite SDK 访问 OSS

Sqoop 本身并不支持对 OSS 的读写访问，需要使用第三方 Kite SDK 进行 URI 的转换从而进行数据交互。

## SDK 配置

需要在所有 Sqoop 节点进行配置。在每个节点 Sqoop 根目录下的 lib 文件夹，放置 kite-data-oss-3.4.0.jar 文件：

[下载页面](/docs/jindofs_sdk_download.md)

## 如何使用
### 1.确认客户端可访问 OSS，并具有读写数据的权限
```
hadoop fs -ls oss://yourbucket/dir/
hadoop fs -put <localfile> oss://yourbucket/dir/
```

### 2.确认下载的文件大小正确 (1.7 MB)：
```
$ du -h 
1.7M     /path/sqoop/lib/kite-data-oss-3.4.0.jar
```
### 3.授予对 JAR 的权限：
```
sudo chmod 755 kite-data-oss-3.4.0.jar
```
### 4.使用 oss 连接器导入 mysql 上的数据
```
sqoop import --connect jdbc:mysql://<host>:<port>/database --username username --password password --table yourtable --target-dir "oss://yourbucket/dir/" --as-parquetfile -m 5
```