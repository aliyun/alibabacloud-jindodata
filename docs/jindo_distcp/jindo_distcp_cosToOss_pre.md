### 使用前须知
* 请参考 [Jindo DistCp 介绍](jindo_distcp_overview.md) 文章内容进行环境适配和工具包下载
* 如您在使用过程中遇到问题可参考 [Jindo DistCp 问题排查指南](jindo_distcp_QA_pre.md) 进行解决，也可 [新建 ISSUE](https://github.com/aliyun/alibabacloud-jindo-sdk/issues/new) 向我们反馈

### 下载 COS 依赖包
下载[依赖包](https://github.com/tencentyun/hadoop-cos/releases), hadoop-cos-{hadoop.version}-x.x.x.jar 和 cos_api-bundle-5.x.x.jar。
如：cos_api-bundle-5.6.35.jar, hadoop-cos-2.8.5-5.9.2.jar

### 配置 COS 相关实现类和用户信息
修改Hadoop的core-site.xml，增加 COS 相关用户和实现类信息。
```
<configuration>
    <property>
        <name>fs.cosn.impl</name>
        <value>org.apache.hadoop.fs.CosFileSystem</value>
    </property>
    <property>
        <name>fs.AbstractFileSystem.cosn.impl</name>
        <value>org.apache.hadoop.fs.CosN</value>
    </property>

    <property>
        <name>fs.cosn.userinfo.secretId</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.cosn.userinfo.secretKey</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.cosn.bucket.endpoint_suffix</name>
        <value>cos.ap-shanghai.myqcloud.com</value>
    </property>
</configuration>
```

### 配置 OSS 相关实现类和用户信息
将OSS的Access Key、Access Key Secret、Endpoint等预先配置在Hadoop的core-site.xml中。
```
<configuration>
    <property>
        <name>fs.AbstractFileSystem.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.OSS</value>
    </property>
    <property>
        <name>fs.oss.impl</name>
        <value>com.aliyun.emr.fs.oss.JindoOssFileSystem</value>
    </property>

    <property>
        <name>fs.jfs.cache.oss-accessKeyId</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.jfs.cache.oss-accessKeySecret</name>
        <value>xxx</value>
    </property>
    <property>
        <name>fs.jfs.cache.oss-endpoint</name>
        <value>oss-cn-xxx.aliyuncs.com</value>
    </property>
</configuration>
```

### 拷贝数据到 OSS 上
您可以使用如下命令将 COS 上的目录拷贝到 OSS 上。
```
hadoop jar jindo-distcp-3.4.0.jar -libjars cos_api-bundle-5.6.35.jar,hadoop-cos-2.8.5-5.9.2.jar --src cosn://srcbucket/ --dest oss://destBucket/
```

### 其他功能
如您需要其他使用其他功能，请参考
* [Jindo DistCp 进行数据迁移的使用说明](jindo_distcp_how_to.md)
* [Jindo DistCp 场景化使用指南](jindo_distcp_scenario_guidance.md)

