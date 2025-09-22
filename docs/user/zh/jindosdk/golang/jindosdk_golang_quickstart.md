# 使用 Jindo Golang SDK 访问阿里云 OSS/OSS-HDFS

## 背景

本文指导如何使用 Jindo Golang SDK 操作 OSS/OSS-HDFS。

## 部署环境

### 下载 Golang SDK Demo

1. 下载 jindo-gosdk-demo [下载链接](http://jindodata-binary.oss-cn-shanghai.aliyuncs.com/release/6.10.1/jindo-gosdk-demo.tar.gz)。

2. 本 Demo 以 Golang 1.20 版本 在 Linux 平台使用为例，多平台使用可参考[多平台部署文档](../jindosdk_deployment_multi_platform.md) 自行替换 `jindo/libjindosdk_go.so`
    
```
.
├── Gopkg.lock
├── Gopkg.toml
├── README.md
├── go.mod
├── jindo
│   ├── jindo_const.go
│   ├── jindo_error.go
│   ├── jindo_filesystem.go
│   ├── jindo_reader.go
│   ├── jindo_util.go
│   ├── jindo_wrapper.go
│   ├── jindo_writer.go
│   └── libjindosdk_go.so
└── main.go

2 directories, 13 files
```

### Golang Demo 程序

**注意**：需替换 endpoint、accessKeyId、accessKeySecret、user、bucket等参数。

```golang
package main

import (
	"bytes"
	"fmt"
	"jindo-gosdk-demo/jindo"
	"strings"
	"time"
)

func main() {
	option := map[string]string{
		jindo.JdoOptLoggerDir:            "/tmp/",
		jindo.JdoOptLoggerAppender:       "file",
		jindo.JdoOptLoggerLevel:          "2",
		jindo.JdoOptLoggerVerbose:        "0",
		jindo.JdoOptLoggerSync:           "true",
		jindo.JdoOptLoggerProgram:        "jindo-gosdk-demo",
		jindo.JdoOptOssEndpoint:          "<your oss/oss-hdfs endpoint>",
		jindo.JdoOptOssSecondLevelDomain: "true",
		jindo.JdoOptOssDataLakeStorage:   "true",
		jindo.JdoOptOssAccessKeyId:       "<your access key id>",
		jindo.JdoOptOssAccessKeySecret:   "<your access key secret>",
	}
	user := "<your user name>"
	bucket := "<your oss/oss-hdfs bucket name>"
	rootPath := "oss://" + bucket + "/"
	testDir := rootPath + "test-go-example/"

	// init sdk
	fs := jindo.NewJdoFileSystem()
	if err := fs.Init(rootPath, user, option); err != nil {
		panic(err.Message)
	}
	defer fs.Free()

	// clean testDir
	_ = fs.Remove(testDir, true)

	// mkdir testDir
	if err := fs.Mkdir(testDir, false); err != nil {
		fmt.Printf("failed to mkdir, %d: %s\n", err.Code, err.Message)
	} else {
		fmt.Printf("success to mkdir\n")
	}

	// write 10 files (each size 10MB) under testDir
	data := strings.Repeat("abcd", 1024*1024*10)
	current := time.Now().Unix()
	for idx := 0; idx < 10; idx++ {
		path := fmt.Sprintf(testDir+"test_%d_%d", current, idx)
		writer, err := fs.OpenWriter(path, 1024*1024)
		if err != nil {
			panic(err.Message)
		}
		start := time.Now().Unix()
		if cnt, err := writer.Write(bytes.NewReader([]byte(data))); err != nil {
			panic(err.Message)
		} else if err := writer.Flush(); err != nil {
			panic(err.Message)
		} else {
			writer.Free()
			fmt.Printf("success to write %d: %d, takes: %d sec\n", idx, cnt, time.Now().Unix()-start)
		}
	}

	// chmod 777 to testDir
	err := fs.SetPermission(testDir, 0777)
	if err != nil {
		panic(err.Message)
	}

	// chown testDir to testuser & testgroup
	err = fs.SetOwner(testDir, "testuser", "testgroup")
	if err != nil {
		panic(err.Message)
	}

	// getFileInfo of testDir
	fileInfo, err := fs.GetFileInfo(testDir)
	if err != nil {
		panic(err.Message)
	}
	if !fileInfo.IsDir() {
		panic(testDir + " should be dir")
	}
	fmt.Printf("success to getInfo %s, user %s, group %s, Length %d, Mtime %d, Atime %d\n",
		fileInfo.Name, fileInfo.User, fileInfo.Group, fileInfo.Length, fileInfo.MTimeMilliSec, fileInfo.ATimeMilliSec)

	// read 10 files (each size 10MB) under testDir
	for idx := 0; idx < 10; idx++ {
		path := fmt.Sprintf(testDir+"test_%d_%d", current, idx)
		fileInfo, err := fs.GetFileInfo(path)
		if err != nil {
			panic(err.Message)
		}
		if !fileInfo.IsFile() {
			panic(path + " should be file")
		}
		fmt.Printf("success to getInfo %s, user %s, group %s, Length %d, Mtime %d, Atime %d\n",
			fileInfo.Name, fileInfo.User, fileInfo.Group, fileInfo.Length, fileInfo.MTimeMilliSec, fileInfo.ATimeMilliSec)
	}

	// listDir non-recursive
	listResult, err := fs.ListDir(testDir, false)
	if err != nil {
		panic(err.Message)
	}
	if listResult == nil || len(listResult.Infos) != 10 {
		panic("listResult of " + testDir + " should be 10")
	}
	for idx := 0; idx < 10; idx++ {
		fileInfo := listResult.Infos[idx]
		fmt.Printf("success to listResult of %d, name %s, user %s, group %s, Length %d, Mtime %d, Atime %d\n",
			idx, fileInfo.Name, fileInfo.User, fileInfo.Group, fileInfo.Length, fileInfo.MTimeMilliSec, fileInfo.ATimeMilliSec)
	}

	// listDir iterative
	marker := ""
	for idx := 0; idx < 10; idx++ {
		listResult, err := fs.ListDirIterative(testDir, false, 1, marker)
		if err != nil {
			panic(err.Message)
		}
		if listResult == nil || len(listResult.Infos) != 1 {
			panic("listResult of " + testDir + " should be 11")
		}
		fileInfo := listResult.Infos[0]
		fmt.Printf("success to listResult of %d, name %s, user %s, group %s, Length %d, Mtime %d, Atime %d\n",
			idx, fileInfo.Name, fileInfo.User, fileInfo.Group, fileInfo.Length, fileInfo.MTimeMilliSec, fileInfo.ATimeMilliSec)
		marker = listResult.NextMarker
	}

	// getContentSummary
	contentSummary, err := fs.GetContentSummary(testDir, false)
	if err != nil {
		panic(err.Message)
	}
	fmt.Printf("success to getContentSummary oss://jindosdk-gosdk-demo-bucket/test-gosdk-nextarch/, FileSize %d, FileCount %d, DirCount %d\n",
		contentSummary.FileSize, contentSummary.FileCount, contentSummary.DirCount)

	// read 10 files (each size 10MB) written under testDir
	for idx := 0; idx < 10; idx++ {
		path := fmt.Sprintf(testDir+"test_%d_%d", current, idx)
		reader, err := fs.OpenReader(path, 1024*1024)
		if err != nil {
			panic(err.Message)
		}
		start := time.Now().Unix()
		buffer := make([]byte, 1024*1024*4*10)
		if cnt, err := reader.Read(bytes.NewBuffer(buffer)); err != nil {
			panic(err.Message)
		} else {
			reader.Free()
			fmt.Printf("success to read %d: %d, takes: %d sec\n", idx, cnt, time.Now().Unix()-start)
		}
	}

	// remove testDir
	if err := fs.Remove(testDir, true); err != nil {
		fmt.Printf("failed to remove, %d: %s\n", err.Code, err.Message)
	} else {
		fmt.Printf("success to remove\n")
	}
}
```

### 编译测试

```bash
cd jindo-gosdk-demo
go build
LD_LIBRARY_PATH=./jindo ./jindo-gosdk-demo
```

### 执行结果

```
success to mkdir
success to write 0: 41943040, takes: 1 sec
success to write 1: 41943040, takes: 2 sec
success to write 2: 41943040, takes: 2 sec
success to write 3: 41943040, takes: 1 sec
success to write 4: 41943040, takes: 2 sec
success to write 5: 41943040, takes: 2 sec
success to write 6: 41943040, takes: 1 sec
success to write 7: 41943040, takes: 2 sec
success to write 8: 41943040, takes: 2 sec
success to write 9: 41943040, takes: 1 sec
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example, user testuser, group testuser, Length 0, Mtime 1744686630369, Atime 0
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_0, user warlock, group warlock, Length 41943040, Mtime 1744686616545, Atime 1744686615728
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_1, user warlock, group warlock, Length 41943040, Mtime 1744686618285, Atime 1744686616580
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_2, user warlock, group warlock, Length 41943040, Mtime 1744686620061, Atime 1744686618288
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_3, user warlock, group warlock, Length 41943040, Mtime 1744686621733, Atime 1744686620064
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_4, user warlock, group warlock, Length 41943040, Mtime 1744686623446, Atime 1744686621737
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_5, user warlock, group warlock, Length 41943040, Mtime 1744686625144, Atime 1744686623450
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_6, user warlock, group warlock, Length 41943040, Mtime 1744686626853, Atime 1744686625148
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_7, user warlock, group warlock, Length 41943040, Mtime 1744686628758, Atime 1744686626857
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_8, user warlock, group warlock, Length 41943040, Mtime 1744686630366, Atime 1744686628761
success to getInfo oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_9, user warlock, group warlock, Length 41943040, Mtime 1744686631976, Atime 1744686630369
success to listResult of 0, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_0, user warlock, group warlock, Length 41943040, Mtime 1744686616545, Atime 1744686615728
success to listResult of 1, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_1, user warlock, group warlock, Length 41943040, Mtime 1744686618285, Atime 1744686616580
success to listResult of 2, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_2, user warlock, group warlock, Length 41943040, Mtime 1744686620061, Atime 1744686618288
success to listResult of 3, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_3, user warlock, group warlock, Length 41943040, Mtime 1744686621733, Atime 1744686620064
success to listResult of 4, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_4, user warlock, group warlock, Length 41943040, Mtime 1744686623446, Atime 1744686621737
success to listResult of 5, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_5, user warlock, group warlock, Length 41943040, Mtime 1744686625144, Atime 1744686623450
success to listResult of 6, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_6, user warlock, group warlock, Length 41943040, Mtime 1744686626853, Atime 1744686625148
success to listResult of 7, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_7, user warlock, group warlock, Length 41943040, Mtime 1744686628758, Atime 1744686626857
success to listResult of 8, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_8, user warlock, group warlock, Length 41943040, Mtime 1744686630366, Atime 1744686628761
success to listResult of 9, name oss://jindosdk-gosdk-demo-bucket/test-go-example/test_1744686615_9, user warlock, group warlock, Length 41943040, Mtime 1744686631976, Atime 1744686630369
success to getContentSummary oss://jindosdk-gosdk-demo-bucket/test-gosdk-nextarch/, FileSize 419430400, FileCount 10, DirCount 1
success to read 0: 41943040, takes: 0 sec
success to read 1: 41943040, takes: 2 sec
success to read 2: 41943040, takes: 2 sec
success to read 3: 41943040, takes: 2 sec
success to read 4: 41943040, takes: 1 sec
success to read 5: 41943040, takes: 2 sec
success to read 6: 41943040, takes: 2 sec
success to read 7: 41943040, takes: 1 sec
success to read 8: 41943040, takes: 2 sec
success to read 9: 41943040, takes: 2 sec
success to remove
```

### 内存占用优化

如果发现常驻服务，内存缓慢上升。大概率是因为 Jindo Golang SDK 的核心实现使用了 cgo，而默认的 glibc 内存分配器在并发情况下存在大量脏页未及时回收，因此建议使用 jemalloc 或者 tcmalloc 来优化内存占用。

#### 使用方式1

使用方式如下，编译/部署环境中要提前安装tcmalloc、jemalloc。以tcmalloc为例。
```shell
yum install gperftools gperftools-devel
```

修改 `jindo/jindo_wrapper.go` 中的 LDFLAGS这里带上 -ltcmalloc，如：

```golang
#cgo LDFLAGS: -L${SRCDIR}/ -ljindosdk_go -ltcmalloc
```

重新编译运行

#### 使用方式2

用LD_PRELOAD的方式，直接运行即可。以 jemalloc 为例，在启动前带上 LD_PRELOAD 及 MALLOC_CONF 两个环境变量。如：

```shell
export LD_PRELOAD=/opt/apps/JINDOSDK/jindosdk-current/lib/native/libjemalloc.so
export MALLOC_CONF=percpu_arena:percpu,lg_tcache_max:12,dirty_decay_ms:5000,muzzy_decay_ms:5000 
LD_LIBRARY_PATH=./jindo ./jindo-gosdk-demo
```