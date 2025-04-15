# Using PyJindo to Access Alibaba Cloud OSS/OSS-HDFS

## Background

This guide demonstrates how to utilize the Python toolkit PyJindo to interact with Alibaba Cloud's OSS-HDFS. PyJindo is compatible with Python versions 3.6 and above.

## Deployment Environment

### Downloading the PyJindo Package for Your Python Version

1. Download the latest tar.gz package named `jindosdk-x.y.z.tar.gz`, replacing x.y.z with the actual version from the [download page](../jindosdk_download.md).

2. Deploy `jindosdk-6.8.3.tar.gz`. The whl installation packages are located in the subdirectory `jindosdk-x.x.x/lib/site-packages/` of the complete output artifact. For multi-platform deployment instructions, see the [deployment document](../jindosdk_deployment_multi_platform.md).

3. For a Python 3.6 setup, install `pyjindo-x.y.z-cp36-abi3-linux_x86_64.whl`.

```tree
.
├── bin
│   ├── xxx
├── conf
│   ├── xxx
├── include
│   ├── xxx
├── lib
│   ├── xxx
│   ├── native
│   │   ├── xxxx
│   └── site-packages
│       ├── pyjindo-x.y.z-cp310-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp311-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp312-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp36-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp37-abi3-linux_x86_64.whl
│       ├── pyjindo-x.y.z-cp38-abi3-linux_x86_64.whl
│       └── pyjindo-x.y.z-cp39-abi3-linux_x86_64.whl
├── plugins
│   └── xxxx
├── tools
│   ├── xxx
└── versions
    ├── xxx
```

### Setting Up Environment Variables for Configuration Access

```bash
export JINDOSDK_CONF_DIR=/etc/taihao-apps/jindosdk-conf
export HADOOP_CONF_DIR=/etc/taihao-apps/hadoop-conf
```

1. In an Alibaba Cloud EMR environment, these settings are typically pre-configured and don't require additional setup.

2. For non-EMR environments, refer to: [Deploying JindoSDK in an Environment Other Than EMR](https://help.aliyun.com/zh/emr/emr-on-ecs/user-guide/deploy-jindosdk-in-an-environment-other-than-emr). Note that Hadoop configuration files and `HADOOP_CONF_DIR` are optional for compatibility with HADOOP environment configurations.

### Installing PyJindo

Here's how to install the latest version of PyJindo-6.8.3 for a Python 3.8 environment:

```bash
python3.8 -m ensurepip
python3.8 -m pip install pip --upgrade --trusted-host mirrors.aliyun.com -i http://mirrors.aliyun.com/pypi/simple/
python3.8 -m pip install pyjindo-6.8.3-cp38-abi3-linux_x86_64.whl
```

### Writing a Python Test Program

```python
from pyjindo import fs

bucket = "jindosdk-yanbin-sh"
endpoint = bucket + ".cn-shanghai.oss-dls.aliyuncs.com"
root_path = "oss://" + endpoint + "/"
sub_dir = root_path + "pyjindotest/"
file_path = root_path + "hello.txt"
file_path2 = sub_dir + "hello.txt"
config = fs.read_config()
fs = fs.connect(root_path, "root", config)
# Open (path, mode), 'w' means creating a file if it doesn't exist.
out_file = fs.open(file_path,"wb")
out_file.write(str.encode("hello world, pyjindo")) # Write
out_file.close()

in_file = fs.open(file_path, "rb")
data = in_file.read() # Read
print("The written data is: %s."%(data))
in_file.close()

# List files
ls_file = fs.listdir(root_path)
print("Directory content: %s." %(ls_file))

# Create directory
fs.mkdir(sub_dir)
# Rename and move file
fs.rename(file_path, file_path2)
# List files again
mv_file = fs.listdir(sub_dir)
print("Directory content after moving: %s." %(mv_file))

# Remove the test file and list the directory again
fs.remove(file_path2)
de_file = fs.listdir(sub_dir)
print("Directory content after removing file: %s." %(de_file))
```

## Testing the Program

Execute the test by running:

```bash
python3.8 fs_test.py
```
This script creates a file named `hello.txt`, writes data to it, moves it to a new directory, removes it, and lists the directory contents to verify the operations were successful.

### Execution Result
```
Written data: b'hello world, pyjindo'.
Directory contents: [<FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/.sysinfo/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/apps/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/flume/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hbase/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hello.txt': type=File, size=20>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyarrowtest/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/spark-history/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/tmp/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/user/': type=Directory>, <FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/yarn/': type=Directory>].
Files after moving: [<FileInfo for 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyjindotest/hello.txt': type=File, size=20>].
pyjindotest directory after deleting file: [].
```

### Logging Levels

Adjust the `jindosdk.cfg` configuration in the `$JINDOSDK_CONF_DIR` (e.g., `/etc/taihao-apps/jindosdk-conf`) directory:

```toml
[common]
logger.dir = /var/log/emr/jindosdk
logger.level = 2
logger.verbose = 0
logger.sync = false
logger.jnilogger = true
logger.consolelogger = false
logger.cleaner.enable = true
```
| Configuration Key | Description |
| --- | --- |
| `logger.dir` | Log output directory. |
| `logger.level` | Recommended as 2; level <= 1 indicates WARN; level > 1 indicates INFO. |
| `logger.verbose` | Detailed log level; ranges from 0 to 99; larger values mean more detailed logs. |
| `logger.sync` | Recommended as false; true indicates synchronous log output. |
| `logger.jnilogger` | Takes effect when using JNI, unrelated to PyJindo. |
| `logger.consolelogger` | Takes effect when logging to console, unrelated to PyJindo. |
| `logger.cleaner.enable` | Recommended as true; false disables automatic log cleanup. |

### API Documentation

### Config Class

| Member Function | Return Type | Description |
| --- | --- | --- |
| `set(key, val)` | None | Sets a string configuration; key and val are strings. |
| `get(key, default='')` | str | Retrieves a string configuration; returns default if key is not found. |
| `contains(key)` | bool | Checks if configuration key exists. |

Refer to [Common Client Configurations](../jindosdk_configuration.md) for more details.

### FileType Enum

| Enum Type | Enum Value | Description |
| --- | --- | --- |
| Unknown | 0 | - |
| Directory | 1 | Directory |
| File | 2 | File |
| Symlink | 3 | Symbolic Link |

### FileInfo Class

| Member Attribute | Return Type | Description |
| --- | --- | --- |
| `type` | FileType | File type |
| `is_file` | bool | True if file |
| `is_dir` | bool | True if directory |
| `is_symlink` | bool | True if symbolic link |
| `path` | str | Path |
| `user` | str | Owner |
| `group` | str | Group |
| `size` | int | File size |
| `perm` | int | File permission |
| `atime` | datetime | Last access time |
| `mtime` | datetime | Last modification time |

### FileStream Class

| Member Function | Return Type | Description |
| --- | --- | --- |
| `readable()` | bool | True if readable |
| `writable()` | bool | True if writable |
| `seekable()` | bool | True if seekable |
| `closed()` | bool | True if closed |
| `close()` | None, raises IOError on failure | Closes the file |
| `size()` | int, raises IOError on failure | File size (only for reading) |
| `tell()` | int, raises IOError on failure | Current position in the file stream |
| `flush()` | None, raises IOError on failure | Flushes the buffer |
| `write(data)` | None, raises IOError on failure | Writes data (as bytes) to the buffer |
| `read(nbytes)` | bytes, raises IOError on failure | Reads nbytes bytes from the file |
| `pread(nbytes, offset)` | bytes, raises IOError on failure | Randomly reads nbytes bytes starting from offset |
| `readall()` | bytes, raises IOError on failure | Reads the entire file |
| `download(stream_or_path, buffer_size)` | None, raises IOError on failure | Downloads current file to a local path or target stream |
| `upload(stream, buffer_size)` | None, raises IOError on failure | Uploads a stream to the current file |
| `copy_file(src, dest, buffer_size)` | None, raises IOError on failure | Copies file from src to dest |

### FileSystem Class

| Member Function | Return Type | Description |
| --- | --- | --- |
| `mkdir(path, recursive)` | bool, raises IOError on failure | Creates a directory; recursive is a boolean indicating whether to create parent dirs |
| `rename(src, dest)` | bool, raises IOError on failure | Renames file; src and dest are strings |
| `get_file_info(path)` | FileInfo, raises IOError on failure | Gets file info |
| `exists(path)` | bool, raises IOError on failure | Checks if file exists |
| `listdir(path, recursive)` | List[FileInfo], raises IOError on failure | Lists files; recursive is a boolean |
| `chmod(path, perm)` | bool, raises IOError on failure | Changes permissions; perm is an integer like 0o777 |
| `chown(path, owner, group)` | bool, raises IOError on failure | Changes ownership; owner and group are strings |
| `open(path, mode, buffer_size=None)` | FileStream, raises IOError on failure | Opens a file; mode supports 'rb', 'wb'; buffer_size defaults to 64k |
| `download(path, stream_or_path, buffer_size=None)` | None, raises IOError on failure | Downloads remote file; stream_or_path can be local path or stream; buffer_size defaults to 64k |
| `upload(path, stream, buffer_size=None)` | None, raises IOError on failure | Uploads stream to remote file; buffer_size defaults to 64k |
| `copy_file(src, dest, buffer_size=None)` | None, raises IOError on failure | Copies file from src to dest; buffer_size defaults to 64k |

### fs Module

| Global Function | Return Type | Description |
| --- | --- | --- |
| `read_config()` | Config | Reads configuration; checks for JINDOSDK_CONF_DIR first, then HADOOP_CONF_DIR if it exists and hadoopConf.enable isn't false in jindosdk.cfg |
| `connect(uri, user, config)` | FileSystem, raises IOError on failure | Initializes FileSystem |

## API Compatibility

### ossfs Module Compatible with fsspec Interface

PyJindo also supports fsspec's synchronous interface but only for Python versions 3.7 and above. The interface compatibility remains, but configurations and log levels remain unchanged.

#### Install fsspec Dependency Beforehand

For Python 3.8 environment:
```bash
python3.8 -m pip install fsspec --trusted-host mirrors.aliyun.com -i http://mirrors.aliyun.com/pypi/simple/
```

#### Write Python Test Program

```python
from pyjindo.ossfs import JindoOssFileSystem

bucket = "jindosdk-yanbin-sh"
endpoint = bucket + ".cn-shanghai.oss-dls.aliyuncs.com"
root_path = "oss://" + endpoint + "/"
sub_dir = root_path + "pyjindotest/"
file_path = root_path + "hello.txt"
file_path2 = sub_dir + "hello.txt"
fs = JindoOssFileSystem(root_path)
# Open (path, mode); 'w' creates a file if it doesn't exist.
out_file = fs.open(file_path,"wb")
out_file.write(str.encode("hello world, pyjindo")) # Write
out_file.close()

in_file = fs.open(file_path, "rb")
data = in_file.read() # Read
print("Written data: %s."%(data))
in_file.close()

# List files
ls_file = fs.ls(root_path, detail=False)
print("Directory contents: %s." %(ls_file))
assert file_path in fs.glob(root_path + "*")

# Create directory
fs.mkdir(sub_dir)
# Move and rename file
fs.rename(file_path, file_path2)
# List files again
mv_file = fs.listdir(sub_dir, detail=False)
print("Files after moving: %s." %(mv_file))

# Delete test file and list directory again
fs.rm(file_path2)
de_file = fs.listdir(sub_dir)
print("pyjindotest directory after deleting file: %s." %(de_file))
```

### Execute Test

```bash
python3.8 ossfs_test.py
```

### Execution Result

```
Written data: b'hello world, pyjindo'.
Directory contents: ['oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/.sysinfo/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/apps/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/flume/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hbase/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/hello.txt', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyarrowtest/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/spark-history/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/tmp/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/user/', 'oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/yarn/'].
Files after moving: ['oss://jindosdk-yanbin-sh.cn-shanghai.oss-dls.aliyuncs.com/pyjindotest/hello.txt'].
pyjindotest directory after deleting file: [].
```

For more information on the available interfaces, consult the official fsspec [documentation](https://filesystem-spec.readthedocs.io/en/latest/api.html#fsspec.spec.AbstractFileSystem).