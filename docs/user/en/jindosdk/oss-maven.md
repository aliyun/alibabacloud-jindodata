# JindoData 6.8.0 Maven Dependencies

In the Maven `pom.xml` file, add dependencies for JindoSDK:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>6.8.0</jindodata.version>
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
        <!-- Add jindo-core -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
              
        <!-- Add jindo-hadoop-sdk -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-sdk</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- Add hadoop dependency -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
        </dependency>
    </dependencies>
</project>
```

Add dependencies for Jindo Flink in Maven's `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>6.8.0</jindodata.version>
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
        <!-- Add jindo-core -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- Add jindo-hadoop-sdk -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-sdk</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- Add jindo flink dependency -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-flink</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
    </dependencies>
</project>
```

Add dependencies for JindoDistCP to Maven's `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.aliyun.jindodata</groupId>
    <artifactId>jindo-sdk-example</artifactId>
    <version>1.0</version>
    
    <properties>
        <jindodata.version>6.8.0</jindodata.version>
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
        
        <!-- Add jindo distcp dependency -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-distcp</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
        
        <!-- Add hadoop dependency -->
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>${hadoop.version}</version>
        </dependency>
    </dependencies>
</project>
```

Other platform dependencies:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!-- ... -->

    <dependencies>
        <!-- Add jindo-core-extended-jar for macOS -->
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

        <!-- Add jindo-core-extended-jar for CentOS6 or EL6 -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-el6-x86_64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- Add jindo-core-extended-jar for Ubuntu22 -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-ubuntu22-x86_64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>

        <!-- Add jindo-core-extended-jar for Aliyun TianYi (ARM architecture) -->
        <dependency>
            <groupId>com.aliyun.jindodata</groupId>
            <artifactId>jindo-core-linux-el7-aarch64</artifactId>
            <version>${jindodata.version}</version>
        </dependency>
    </dependencies>
</project>
```
Please note that the code blocks remain unchanged, and their comments have been translated into English without altering their meaning.