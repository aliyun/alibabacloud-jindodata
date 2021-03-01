# Sqoop 使用 Kite SDK 读写 OSS

## 环境要求

* 在集群上有开源版本 Sqoop 软件，版本不低于 1.4.7。
* Sqoop 依赖的 Hadoop 环境可访问 OSS，可参考 [Hadoop 使用 JindoFS SDK 访问 OSS](./jindofs_sdk_how_to_hadoop.md)

## 使用 Kite SDK 读写 OSS

Sqoop 本身并不支持对 OSS 的读写，需要使用第三方 Kite SDK 进行 URI 的转换从而进行数据交互。

## 步骤
### 1. 安装 jar 包
下载最新的 jar 包 kite-data-oss-3.4.0.jar ([下载页面](/docs/jindofs_sdk_download.md))，将 sdk 包安装到 Sqoop 的 classpath 目录下。
```
cp ./kite-data-oss-3.4.0.jar <Sqoop_HOME>/lib/kite-data-oss-3.4.0.jar
```

### 2.授予对 JAR 的权限。
```
sudo chmod 755 kite-data-oss-3.4.0.jar
```
### 3.使用 oss 连接器导入 mysql 上的数据。
```
sqoop import --connect jdbc:mysql://<host>:<port>/database --username username --password password --table yourtable --target-dir "oss://yourbucket/dir/" --as-parquetfile -m 5
```