# Alibaba Cloud OSS-HDFS Service (JindoFS Service) File Content Verification \(Supported since v4.6.2\)

Content verification for files migrated from HDFS to OSS-HDFS using JindoDistCp can be performed as follows.

## Step 1.
Calculate the checksum for the target directory outputted by JindoDistCp.

### Command Format
```bash
jindo distjob -checksum --src <src> --dest <dest> --blockSize <blockSize> --recalculate
```
Explanation of Parameters:

`--src <src>`: The path to the OSS-HDFS for which the file content needs to be verified (required). Note: `src` must be an OSS-HDFS or HDFS path.

`--dest <dest>`: The destination path where the checksum file should be written (required).

`--blockSize <blockSize>`: The block size of source files when writing (optional), defaults to 134217728 bytes.

`--recalculate`: Whether to recompute checksums (optional); by default, it doesn't recalculate but reads checksums calculated during write to OSS-HDFS. Note: This flag is supported only when `--src` is an OSS-HDFS path.

## Step 2.
Calculate the checksum for the source directory from which JindoDistCp was executed.

### Example
Suppose there's a directory `oss://dlsbucket/test` in OSS-HDFS that needs its content verified, and you want to output the checksum file to an OSS directory `oss://ossbucket/test-dls`. The command would be:

```bash
jindo distjob -checksum --src oss://dlsbucket/test --dest oss://ossbucket/test-dls --recalculate
```

## Step 3.
Compare the checksum directories generated in Steps 1 and 2 using JindoDistCp's `--diff` option.

### Example
If the source directory for JindoDistCp was `hdfs:///test`, and you want to output the checksum file to an OSS directory `oss://ossbucket/test-hdfs`, the command would be:

```bash
jindo distjob -checksum --src hdfs:///test --dest oss://ossbucket/test-hdfs
```

## Step 4.
Use JindoDistCp's `--diff` feature to compare the checksum directories from Steps 1 and 2.

### Example
```bash
hadoop jar jindo-distcp-tool-${version}.jar --src oss://ossbucket/test-hdfs --dest oss://ossbucket/test-dls --diff
```
This will compare the checksum files from both directories and report any inconsistencies if the content after migration in OSS-HDFS does not match the original HDFS content.