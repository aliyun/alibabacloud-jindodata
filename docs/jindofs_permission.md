# 权限功能

## 启用JindoFS Ranger权限
在 bigboot.cfg 文件的 [bigboot-namespace] section 下对应的 namespace 配置项中添加Key为 jfs.namespaces.<namespace>.permission.method，Value 为 ranger 的配置项。
以 test-cache-ranger namespace 为例:
```
[bigboot-namespace]
jfs.namespaces.test-cache-ranger.permission.method = ranger
```
## 重启 namespace
```
sh sbin/stop-namespace.sh
sh sbin/start-namespace.sh
```

## 进入Ranger UI页面， 添加HDFS Service
<img src="/pic/jindofs_ranger_1.png" width="800"/>

| 参数 | 描述 |
| :--- | ---: |
| Service Name | 固定格式：jfs-{namespace_name}。 <br /> 例如：jfs-test。 |
| Username | 自定义 |
| Password | 自定义 |
| Namenode URL | 输入jfs://{namespace_name}/ |
| Authorization Enabled | 使用默认值No |
| Authentication Type | 使用默认值Simple |
| dfs.datanode.kerberos.principal | 不填写 |
| dfs.namenode.kerberos.principal | 不填写 |
| dfs.secondary.namenode.kerberos.principal | 不填写 |
| Add New Configurations | 不填写 |

以 test-cache-ranger 为例：
<img src="/pic/jindofs_ranger_2.png" width="800"/>

## 配置用户权限
### 单击配置好的 jfs-test-cache-ranger 服务
<img src="/pic/jindofs_ranger_3.png" width="800"/>

### 单击右上角的 Add New Policy
<img src="/pic/jindofs_ranger_4.png" width="800"/>

### 配置相关参数
| 参数 | 描述 |
| :--- | ---: |
| Policy Name | 策略名称，可以自定义 |
| Resource Path | 资源路径 |
| recursive | 子目录或文件是否集成权限 |
| Select Group | 指定添加此策略的用户组 |
| Select User | 指定添加此策略的用户 |
| Permissions | 选择授予的权限 |

以授予 test 用户 /user/test 路径的 Read 和 Execute 权限为例：
<img src="/pic/jindofs_ranger_5.png" width="800"/>

### 检查用户的读写权限
以上面 test 在 /user/test 路径为例子，具有 Read 和 Execute 权限，没有Write：
<img src="/pic/jindofs_ranger_6.png" width="800"/>

