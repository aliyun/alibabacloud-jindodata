# 安装说明

* 支持多平台使用，支持linux（多数linux x86系统）、linux-aarch64(linux armv8系统)、ubuntu22-x86_64、centos6-x86_64、macos-arm64、macos-x86系统。使用java sdk时，除多数的linux x86平台可以直接使用jindo-core.jar外，其他平台需同时依赖jindo-core.jar和jindo-core-{PLATFORM}.jar。
* 包含 Kerberos 和 Sasl 支持，如果您的环境没有 kerberos 和 sasl2 相关依赖，需要在部署 JindoSDK 的所有节点安装如下依赖。

### Ubuntu or Debian
```
sudo apt-get install krb5-user libsasl2-modules libsasl2-modules-gssapi-mit
```

### RHEL or CentOS
```
sudo yum install krb5-workstation cyrus-sasl-gssapi cyrus-sasl-plain
```

### macOS
```
brew install krb5 cyrus-sasl
```
