# Fluid JindoRuntime 镜像升级
1、修改 jindoruntime-controller 的信息
```shell
kubectl edit deployment jindoruntime-controller -n fluid-system
```

2、编辑 controller，找到如下内容
```yaml
- name: JINDO_SMARTDATA_IMAGE_ENV
    value: registry.cn-shanghai.aliyuncs.com/jindofs/smartdata:4.5.1
- name: JINDO_FUSE_IMAGE_ENV
    value: registry.cn-shanghai.aliyuncs.com/jindofs/jindo-fuse:4.5.1
```

3、例如从 4.5.1 升级到  4.5.2，修改如下镜像即可
```yaml
- name: JINDO_SMARTDATA_IMAGE_ENV
    value: registry.cn-shanghai.aliyuncs.com/jindofs/smartdata:4.5.2
- name: JINDO_FUSE_IMAGE_ENV
    value: registry.cn-shanghai.aliyuncs.com/jindofs/jindo-fuse:4.5.2
```
如果是 `fuseOnly` 模式，那么只需修改 `JINDO_FUSE_IMAGE_ENV` 字段即可

4、保存修改，重新创建集群即可