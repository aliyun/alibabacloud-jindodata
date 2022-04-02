# 安装依赖
4.3.0 版本包含 Kerberos 和 Sasl 支持，如果您的环境没有 kerberos 和 sasl2 相关依赖，需要在部署 JindoSDK 和 JindoFSx 的所有节点安装如下依赖。

### Ubuntu or Debian
```
sudo apt-get install libkrb5-dev krb5-admin-server krb5-kdc krb5-user libsasl2-dev libsasl2-modules libsasl2-modules-gssapi-mit
```

### RHEL or CentOS
```
sudo yum install krb5-server krb5-workstation cyrus-sasl-devel cyrus-sasl-gssapi cyrus-sasl-plain
```

### macOS
```
brew install krb5
```