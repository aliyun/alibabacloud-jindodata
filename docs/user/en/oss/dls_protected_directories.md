# Background
The JindoFS Directory Protection feature can currently be managed through command line commands. It allows users to view the list of protected directories or modify protection settings. In the future, a more user-friendly interface will be provided through the product itself.

# Directory Protection Feature Description
The JindoFS Directory Protection function safeguards against accidental deletion of directories. If a directory is protected and contains either directories or files, attempts to delete or rename it will fail. A protected directory can only be deleted or renamed once it no longer contains any directories or files.

# Introduction to JindoFS Command Line Tools
Refer to the [JindoFS Command-Line Tools User Guide](../jindofs/jindofs_client_tools.md) for general information.

Directory protection specifically uses the `-putConfig` and `-getConfig` commands, which will be explained below.

# Configuring Access Key (AK)
Directory protection requires the AK information to connect to OSS-HDFS. There are two ways to configure this:

### Configuration File (Recommended)
Set the environment variable `JINDOSDK_CONF_DIR` to point to a directory and create a file named `jindofs.cfg` within it containing:
```yaml
[client]
fs.oss.accessKeyId = <key>                               # Default AK key for all Buckets
fs.oss.accessKeySecret = <secret>                   # Default AK secret for all Buckets
fs.oss.bucket.<bucket>.accessKeyId = <key>  # AK key specific to a Bucket, overrides default
fs.oss.bucket.<bucket>.accessKeySecret = <secret>  # AK secret specific to a Bucket, overrides default
```

### Using `--extraConf`
All command-line tool commands support the `--extraConf` option to provide configuration parameters inline, similar to the config file approach. For instance:
```bash
jindofs admin -getConfig -dlsUri <path> -name <keys>          \
              --extraConf fs.oss.accessKeyId=<AK key>         \
              --extraConf fs.oss.accessKeySecret=<AK secret>
```
This supplies additional AK key and secret configurations to the command.

# Enabling Directory Protection
Command:
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf fs.protected.directories=/path/to/dir1,/path/to/dir2
```
This command protects two directories (`/path/to/dir1` and `/path/to/dir2`) in the `<bucket>` (with endpoint `<oss-hdfs-endpoint>`). Paths are listed together separated by commas and do not start with `oss://`; they are absolute paths within the bucket beginning with `/`. If the command fails, error messages will appear; silence indicates success. After successful execution, protection takes effect within 30 seconds.

# Viewing Protected Directories
Command:
```bash
jindofs admin -getConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -name fs.protected.directories
```
This returns the list of protected directories in the `<bucket>` (with endpoint `<oss-hdfs-endpoint>`), e.g.:
```bash
fs.protected.directories: /path/to/dir1,/path/to/dir2
```
Indicating that these directories (`/path/to/dir1` and `/path/to/dir2`) are currently under protection.

# Removing All Protected Directories
Command:
```bash
jindofs admin -putConfig -dlsUri oss://<bucket>.<oss-hdfs-endpoint>/ -conf fs.protected.directories=
```
This removes all protections from the `<bucket>` (with endpoint `<oss-hdfs-endpoint>`). Within 30 seconds after the command completes, no directories in the bucket will be protected anymore.