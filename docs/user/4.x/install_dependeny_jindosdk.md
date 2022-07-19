# 安装说明

* 4.3.x 版本包含 Kerberos 和 Sasl 支持，如果您的环境没有 kerberos 和 sasl2 相关依赖，需要在部署 JindoSDK 的所有节点安装如下依赖。
* 4.4.0 及以上版本，如果需要使用 JindoFSx + Ranger 的鉴权方案, 需要在部署 JindoSDK 的所有节点安装如下依赖。

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