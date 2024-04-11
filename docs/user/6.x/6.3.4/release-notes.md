# 6.3.4，2024-04-11

## 版本概要

发布 JindoSDK 6.3.4 正式版的功能

## 介绍

- JindoSDK 更新 [6.3.4 的 Maven 仓库](oss-maven.md)。
- JindoFuse 支持指定 metrics_ip、metrics_port 指定 promethues 监听 ip 和 端口。
- 修复 JindoOssFileSystem 的 Delegation Token Renew 机制。
- 修复 `fs.accessPolicies.discovery` 末尾不带 `/` 时，getTrashRoot 为空报错（仅影响 6.3.3 版本）。
- 修复 listStatusIterator 不支持 ListObjectV2。即配置 `fs.oss.list.type` 为 `2`，可能导致 listStatusIterator 死循环（默认配置不影响）。