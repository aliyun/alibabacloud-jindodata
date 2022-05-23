# 已知问题

1. JINDOSDK 3.7.1 及之前版本，若开启了免密访问且没有正常拿到AK，OSS 会返回如下错误。
```
<Error>
  <Code>InvalidArgument</Code>
  <Message>Authorization header is invalid.</Message>
  <RequestId>628483DA4CABF1xxxxxxxxxx</RequestId>
  <HostId>xxxx.oss-cn-beijing-internal.aliyuncs.com</HostId>
  <Authorization>OSS :BWFwkWf5zyRS+i1AdoOCb8KFi3Q=</Authorization>
</Error>
```