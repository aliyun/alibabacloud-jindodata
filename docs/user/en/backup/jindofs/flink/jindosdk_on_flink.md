- jindo-flink-${version}-full.jar

 

- flink-oss-fs-hadoop-${flink-version}.jar
- 


StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.enableCheckpointing(<userDefinedCheckpointInterval>, CheckpointingMode.EXACTLY_ONCE);

- 


 
String outputPath = "oss://<user-defined-bucket.dls-endpoint>/<user-defined-dir>"
StreamingFileSink<String> sink = StreamingFileSink.forRowFormat(
new Path(outputPath),
new SimpleStringEncoder<String>("UTF-8")
).build();
outputStream.addSink(sink);
 
<flink_home>/bin/flink run -m yarn-cluster -yD key1=value1 -yD key2=value2 ...
oss.entropy.key=<user-defined-key>
oss.entropy.length=<user-defined-length>
  

