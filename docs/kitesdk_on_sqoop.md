1、下载  [kite-data-oss-1.1.1.jar](https://smartdata-binary.oss-cn-shanghai.aliyuncs.com/kite/kite-data-oss-1.1.1.jar)

2、将 kite-data-oss-1.1.1.jar 包放到 sqoop 安装目录下的 lib 子目录下

3、确认下载的文件大小正确 (1.7 MB)：
```
$ du -h 
1.7M     /path/sqoop/lib/kite-data-oss-1.1.1.jar
```
4、授予对 JAR 的权限：
```
sudo chmod 755 kite-data-oss-1.1.1.jar
```
5、使用 oss 连接器导入 jar
```
sqoop import --connect jdbc:mysql://<host>:<port>/database --username username --password password --table yourtable --target-dir "oss://yourbucket/dir/" --as-parquetfile -m 5
```