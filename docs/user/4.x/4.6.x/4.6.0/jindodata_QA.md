# JindoData 常见问题

## OSS-HDFS 服务

### 访问 OSS-HDFS 服务显示内容与实际不符

#### 现象
访问 `oss://<bucket>.oss-dls.aliyuncs.com/` 显示内容与实际不符。

#### 解决办法
升级 JindoSDK 到 4.x 版本。

## OSS SDK 方式

### JindoSDK 访问 OSS 出现 CopyNotExcecuted 问题排查

#### 现象
出现报错
java.io.IOException: ErrorCode : 25201 , ErrorMsg: OSS Op Error.  [ErrorMessage]: Error in xxxxxxxxxx/.hive-staging_hive_xxxxxxxxxxx/_tmp.-ext-10002/xxxxx to xxxxxxxxxx/.hive-staging_hive_xxxxxxxxxxx/_tmp.-ext-10002.moved/xxxx, err: CopyNotExcecuted;

#### 解决办法

CopyNotExcecuted 是 OSS 服务端返回的异常，一般是 OSS BatchCopy 服务端过载返回异常。早期版本 OSS  未定义 BatchCopy 异常时如何处理，需要关闭 BatchCopy 才能在老版本上解决。在EMR控制台，修改 smartdata-site 的 fs.jfs.cache.batch-copy.size 为 0，就可以关闭 batch copy，并依次保存->部署-> 勾选同步更新 Gateway


## IDE 开发调试

### maven 里下载不到 JindoSDK 依赖

#### 现象

maven `pom.xml` 里添加了 `repository` 标签仍然下载不到 JindoSDK 依赖

#### 解决办法
可以检查本地是否开启了 mirror 镜像的通配。方法如下：

````bash
# 打开 maven 本地设置

less ~/.m2/settings.xml 
````

检查mirror 标签，如果您设置了 `<mirrorOf>*</mirrorOf>` 建议改成 `<mirrorOf>central</mirrorOf>`

````xml
    <mirrors>
        <mirror>
            <id>alimaven</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
````

如果是构建环境存在限制无法修改，可以将 jar 包上传到内部 maven 仓库，同时建议将上传的 `version` 进行区分，如上传为 `4.6.2-rc0`。或者是使用本地路径依赖
如

````xml
      <dependency>
          <groupId>com.aliyun.jindodata</groupId>
          <artifactId>jindosdk</artifactId>
          <version>4.6.x</version>
          <scope>system</scope>
          <systemPath>/Users/xx/xx/jindosdk-4.6.x.jar</systemPath>
          <version>${jindodata.version}</version>
      </dependency>
````

