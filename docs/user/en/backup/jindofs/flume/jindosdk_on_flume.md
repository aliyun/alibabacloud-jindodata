# Use JindoSDK with Flume to write data to Alibaba Cloud OSS-HDFS (JindoFS)

Environment requirements

Make sure that Flume and JindoSDK of a version later than 4.0 are deployed in your cluster. 

Why do I need to use JindoSDK with Flume to write data to OSS-HDFS?

Flume calls the flush() method to implement transactional data writes and uses JindoSDK to write data to OSS-HDFS. This way, data can be queried immediately after it is flushed. This also helps prevent data loss. 

JindoSDK configuration

Configure JindoSDK on each node on which Flume is deployed.  

[Download](https://github.com/aliyun/alibabacloud-jindodata/blob/latest/docs/user/en/jindosdk/jindosdk_download.md) the latest version of the jindosdk-x.x.x.tar.gz package, decompress the package, and then install the files after the decompression to the lib folder of the Flume root directory on each node.

cp jindosdk-x.x.x/lib/\*.jar  $FLUME\_HOME/jars/

Sample sink configuration

# Configure an Object Storage Service (OSS) sink.

xxx.sinks.oss\_sink.hdfs.path = oss://${your\_bucket}/flume\_dir/%Y-%m-%d/%H

# Configure sink-related parameters. You must set the batchSize parameter to a large value. We recommend that you flush more than 32 MB of data each time. This helps prevent impacts on the overall performance.

xxx.sinks.oss\_sink.hdfs.batchSize = 100000

...

xxx.sinks.oss\_sink.hdfs.round = true

xxx.sinks.oss\_sink.hdfs.roundValue = 15

xxx.sinks.oss\_sink.hdfs.Unit = minute

xxx.sinks.oss\_sink.hdfs.filePrefix = your\_topic

xxx.sinks.oss\_sink.rollSize = 3600

xxx.sinks.oss\_sink.threadsPoolSize = 30

...