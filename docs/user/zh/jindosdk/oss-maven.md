# JindoData 6.7.0 Maven 相关依赖

在 maven pom.xml 中添加 JindoSDK 的依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>6.7.0</jindodata.version>
        <hadoop.version>2.8.5</hadoop.version>
    </properties>
    
    <repositories>
        <!-- Add JindoData Maven Repository -->
        <repository>
            <id>jindodata</id>
            <url>https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/</url>
        </repository>
    </repositories>

    
    <dependencies>
        <!-- add jindo-core -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
              
        <!-- add jindo-hadoop-sdk -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-sdk</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- add hadoop dependency. -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
        </dependency>
    </dependencies>
</project>
```

在 maven pom.xml 中添加 Jindo Flink 的依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>6.7.0</jindodata.version>
        <hadoop.version>2.8.5</hadoop.version>
    </properties>
    
    <repositories>
        <!-- Add JindoData Maven Repository -->
        <repository>
            <id>jindodata</id>
            <url>https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/</url>
        </repository>
    </repositories>

    
    <dependencies>
        <!-- add jindo-core -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- add jindo-hadoop-sdk -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-sdk</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- add jindo flink dependency -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-flink</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
    </dependencies>
</project>
```

在 maven pom.xml 中添加 JindoDistCP 的依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>6.7.0</jindodata.version>
        <hadoop.version>2.8.5</hadoop.version>
    </properties>
    
    <repositories>
        <!-- Add JindoData Maven Repository -->
        <repository>
            <id>jindodata</id>
            <url>https://jindodata-binary.oss-cn-shanghai.aliyuncs.com/mvn-repo/</url>
        </repository>
    </repositories>
    
    <dependencies>
        
        <!-- add jindo distcp dependency -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-distcp</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- add hadoop dependency. -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
        </dependency>
    </dependencies>
</project>
```

其他平台依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!-- ... -->
    
    <dependencies>
        <!-- add jindo-core-extended-jar for macos -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-macos-11_0-x86_64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-macos-11_0-aarch64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- add jindo-core-extended-jar for centos6 or el6 -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-el6-x86_64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- add jindo-core-extended-jar for ubuntu22 -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-ubuntu22-x86_64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- add jindo-core-extended-jar for aliyun yitian/arm -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-el7-aarch64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
    </dependencies>
</project>
```
