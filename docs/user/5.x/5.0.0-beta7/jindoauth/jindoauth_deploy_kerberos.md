# 部署 JindoAuth 服务（高安全 EMR 集群）

### 1. 下载 JindoAuth 包

下载最新的 Release 包 jindoauth-x.x.x.tar.gz (\[下载页面\](/docs/user/5.x/jindodata\_download.md))。

注意： 如果下载的是 beta 版本，如 jindoauth-5.0.0-beta7-linux.tar.gz，需要按如下方式重命名。

    mv jindoauth-5.0.0-beta7-linux.tar.gz jindoauth-5.0.0-linux.tar.gz

### 2. 服务端配置

请将下列文件内容复制粘贴到 jindoauth.cfg 文件中，这个文件是 JindoAuth 服务使用的配置文件。其中的部分内容需要根据您的集群信息进行配置：

    [jindoauth-common]
    logger.jnilogger=false
    logger.dir = /mnt/disk1/log/jindoauth
    logger.sync = false
    logger.verbose=99
    logger.cleaner.enable = true
    default.credential.provider = ECS_ROLE
    
    [jindoauth-server]
    jindoauth.meta-dir = /mnt/disk1/jindoauth
    jindoauth.ranger.logger.level = DEBUG
    jindoauth.policy.dir = /mnt/disk1/jindoauth/ranger_policy_cache
    jindoauth.rpc.port = 8201
    ranger.plugin.jindo-auth.policy.rest.url=http://master-1-1:6080
    

#### 2.1 配置 JindoAuth 服务地址

需要在集群中根据访问规模和使用场景部署若干 JindoAuth 服务。

|  参数  |  值  |  说明  |
| --- | --- | --- |
|  jindoauth.rpc.port  |  默认为 8201  |  JindoAuth 服务端口  |

#### 2.2  AK 免密和 Token 管理

*   配置 JindoAuth 服务颁发 Security Token 使用的 Credential Provider：
    

|  参数  |  值  |  说明  |
| --- | --- | --- |
|  jindoauth.oss.credentials.provider  |  例：ECS\_ROLE  |  *   ASSUME\_ROLE： 使用 Assume Role 颁发 Security Token;      *   ECS\_ROLE：颁发 EcsRole 免密 Security Token;       |

*   如果配置的Credential Provider 为 ASSUME\_ROLE，还需如下配置：
    

|  参数  |  值  |  说明  |
| --- | --- | --- |
|  jindoauth.assume.role.name  |  例：JindoAuthTestRole  |  在 RAM 中创建的角色  |
|  jindoauth.assume.role.arn  |  例：acs:ram::xxx:role/JindoAuthTestRole   |  角色的ARN  |
|  jindoauth.assume.access.key   |  xxx  |  Token Service 的 accessKey   |
|  jindoauth.AssumeRoleProvider.access.secret  |  xxx  |  Token Service 的 accessKeySecret   |
|  jindoauth.AssumeRoleProvider.access.endpoint  |  例：sts.cn-shanghai.aliyuncs.com  |  Token Service 的 endpoint  |

#### 2.3 配置 Ranger Admin Service URL

|  参数  |  值  |
| --- | --- |
|  ranger.plugin.jindo-auth.policy.rest.url  |  配置获取 Ranger Policy 的 URL, 如：http://master-1-1:6080， 默认为 http://master-1-1:6080  |

#### 2.4  配置 Principal 和 Keytab（适用于 Kerberos 集群）

##### 2.4.1 创建 principal 和 keytab

如果您是使用 root 用户，登录 KDC（Kerberos的服务端程序）所在的 master-1-1 节点，则可以执行以下命令，直接进入 admin 工具。

    kadmin.local

当返回信息中包含如下信息时，表示已进入 admin.local 命令行。

    Authenticating as principal hadoop/admin@EMR.C-85D4B8D74296****.COM with password.
    kadmin.local:

执行以下命令，创建用户名为 jindodata 的 principal, 格式为 jindodata/<hostname>.<region>.emr.aliyuncs.com。

    addprinc -randkey jindodata/master-1-1.c-9e4fc2dcc****.cn-shanghai.emr.aliyuncs.com

导出 keytab 文件。

    ktadd -k jindodata.keytab jindodata/master-1-1.c-9e4fc2dcc****.cn-shanghai.emr.aliyuncs.co

##### 2.4.2 配置 Kerberos

|  参数  |  值  |  说明  |
| --- | --- | --- |
|  jindoauth.authentication.method  |  kerberos  |  开启 Kerberos 认证， 若为空，则不开启认证  |
|  jindoauth.authentication.keytab    |   例：/opt/apps/JINDOAUTH/jindoauth-current/conf/jindoauth.keytab  |  配置 keytab 所在的路径  |
|  jindoauth.authentication.principal  |   例：jindoauth/master-1-1.c-9e4fc2dcc\*.cn-shanghai.emr.aliyuncs.com@EMR.C-85D4B8D74296\*.COM  |  配置 keytab 中的 principal  |

### 3. HA 部署

#### 3.1 准备节点信息

在后文，我们将通过执行一个简单的脚本命令将 JindoAuth 服务在整个集群上启动起来。为了使脚本执行成功，您需要将部署 JindoAuth 服务的集群节点信息记录下来。您可以将启动 JindoAuth 服务的节点记录在 nodes 文件中。每一行记录一个集群内的地址，例如，nodes 可以形似下文：

    master-1-1
    master-1-2
    master-1-3

您的集群节点信息可以参考系统文件 /etc/hosts。

#### 3.2 准备脚本

请将下列文件内容复制粘贴到 deploy.sh 中，并与 jindoauth-5.0.0-linux.tar.gz（以实际使用版本为准）及准备好的 nodes、jindoauth.cfg 等文件放在同一目录下：

    LOG_DIR="/mnt/disk1/jindoauth/log"           # use your own setting
    INSTALL_DIR="/opt/apps/JINDOAUTH/"           # use your own setting
    TAR_FILE_NAME="jindoauth-5.0.0-linux.tar.gz" # use real tar file name
    JINDOAUTH_DIR_NAME="jindoauth-5.0.0-linux"   # use real tar file name
    
    sudo yum install pssh tmux -y
    
    pssh -O StrictHostKeyChecking=no -t 0 -i -h nodes "sudo rm -f /tmp/${TAR_FILE_NAME}"
    pssh -O StrictHostKeyChecking=no -t 0 -i -h nodes "sudo rm -f /tmp/jindoauth.cfg"
    
    pscp.pssh -O StrictHostKeyChecking=no  -h nodes ./"${TAR_FILE_NAME}" /tmp/
    pscp.pssh -O StrictHostKeyChecking=no  -h nodes ./jindoauth.cfg /tmp/jindoauth.cfg
    pssh -O StrictHostKeyChecking=no -t 0 -i -h nodes "
        sudo mkdir -p ${LOG_DIR}
        sudo mkdir -p ${INSTALL_DIR}
        sudo chown -R emr-user:emr-user ${LOG_DIR}
        sudo chown -R emr-user:emr-user ${INSTALL_DIR}
    
        cd /tmp
        tar -xzvf ${TAR_FILE_NAME} -C ${INSTALL_DIR}
        sudo mv ./jindoauth.cfg ${INSTALL_DIR}/${JINDOAUTH_DIR_NAME}/conf/
    "
    cp nodes ${INSTALL_DIR}/${JINDOAUTH_DIR_NAME}/sbin/
    cd ${INSTALL_DIR}/${JINDOAUTH_DIR_NAME}/
    sh sbin/stop-service.sh
    sleep 10
    sh sbin/start-service.sh
    sleep 10

注意脚本中的 LOG\_DIR 与 INSTALL\_DIR 变量用实际日志路径和安装路径代替，TAR\_FILE\_NAME 与 JINDOAUTH\_DIR\_NAME 用下载下来实际使用的压缩包名称、解压以后的目录名称代替。

#### 3.3 执行脚本

随后，以 emr-user 用户运行上述脚本，就可以完成 JindoAuth 服务在集群中的部署。

    su - emr-user
    sh deploy.sh

### 4. 启动 JindoAuth 服务

在 jindoauth-5.0.0-linux/sbin/nodes 中编辑节点信息。例如，nodes 文件如下格式：

    master-1-1
    master-1-2
    master-1-3

注：您的集群节点信息可以参考系统文件 /etc/hosts。

在 master 节点执行以下脚本：

    cd jindoauth-5.0.0-linux
    sh sbin/start-service.sh

执行完成后，headers 节点将会启动 JindoAuth Server。

另外，可以通过以下命令停止所有 JindoAuth 服务：

    cd jindoauth-x.x.x
    sh sbin/stop-service.sh

### 5. 在 Ranger WebUI 配置 OSS 权限

#### a. 创建一个名为 emr-oss 的 service， 并添加下列配置。

|  参数  |  值  |
| --- | --- |
|  policy.download.auth.users  |  jindodata  |

创建完成后如下，后续就可以在该Service下创建Policy。

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/WgZOZxxjDk58nLX8/img/6bbe0968-f5a6-4d81-9a08-cebea9345d31.png)

#### b. Ranger 规则示例

例：配置用户test拥有访问`oss://jindoauth-runjob-cn-shanghai/user/test`目录的访问权限为 ALL：

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/WgZOZxxjDk58nLX8/img/b2c86e9f-cdd4-40bd-a9eb-bbd45a4c3d62.png)

##### 说明：

*   规则配置页面中，配置的 path 没有`oss://`的前缀。
    
*   recursive按钮不可关闭。
    
*   路径末尾无需带正斜线（/）。