# Flume Writing to OSS with JindoSDK

## System Requirements

Make sure Flume is installed on your cluster and JindoSDK version 6.0 or later is deployed.

## Installing and Deploying JindoSDK

Configure JindoSDK on Flume nodes. Copy JindoSDK jars to the `lib` folder in each node's Flume root directory:

```bash
# Extract the latest tar.gz package jindosdk-x.y.z.tar.gz (replace x.y.z with the actual version)
# Navigate to the extracted directory
cd jindosdk-x.y.z

# Copy all jar files from the lib directory to Flume's jars directory
cp lib/*.jar $FLUME_HOME/jars/
```

## Configuration

### Flume Sink Configuration

In your Flume configuration file, set up the OSS sink as follows:

```properties
# Specify the OSS sink. Replace 'your_bucket' with the bucket that has OSS-HDFS service enabled.
xxx.sinks.oss_sink.hdfs.path = oss://${your_bucket}/flume_dir/%Y-%m-%d/%H

# Set the maximum number of events in a Flume transaction. It's recommended to have a batch size of 32 MB or more to avoid frequent flushing and small staging files.
# batchSize is in terms of event count (i.e., log lines). Assuming an average event size of 200 bytes and expecting a flush size of 32 MB, set batchSize to approximately 160000 (32 MB / 200 bytes).
xxx.sinks.oss_sink.hdfs.batchSize = 100000

...
xxx.sinks.oss_sink.hdfs.round = true
xxx.sinks.oss_sink.hdfs.roundValue = 15
xxx.sinks.oss_sink.hdfs.Unit = minute
xxx.sinks.oss_sink.hdfs.filePrefix = your_topic
xxx.sinks.oss_sink.rollSize = 3600 # This could be adjusted based on your needs
xxx.sinks.oss_sink.threadsPoolSize = 30 # Adjust according to your environment
...

```

### OSS-HDFS Scenario in core-site.xml

It's recommended to have a flush size of at least 32 MB to minimize frequent flushing and the creation of many small files.

```xml
<configuration>

    <property>
        <!-- Set to false to disable hflush and rely solely on close for file writes, not recommended -->
        <name>fs.oss.hflush.enable</name>
        <value>true</value>
    </property>

    <property>
        <!-- Number of hflushes before triggering a real flush; set to 0 to disable this condition -->
        <name>fs.oss.hflush.interval.count</name>
        <value>0</value>
    </property>

    <property>
        <!-- Size in bytes after which to trigger a real flush; set to 0 to disable this condition -->
        <name>fs.oss.hflush.interval.size</name>
        <value>0</value>
    </property>
</configuration>
```

Note: If both `fs.oss.hflush.interval.count` and `fs.oss.hflush.interval.size` are non-zero, a hflush will be triggered when both conditions are met.