# Export metadata from OSS-HDFS (JindoFS)

(This topic applies to JindoSDK 4.6.0 or later.)

Overview

OSS-HDFS allows you to export metadata of objects in a bucket for which OSS-HDFS is enabled to Object Storage Service (OSS). The metadata is exported as JSON files. This facilitates metadata analysis. This topic describes how to export metadata from OSS-HDFS.

*   Configure the Jindo CLI and an AccessKey pair that you want to use to access the desired bucket. For more information, see Use Jindo commands.
    
*   Run the export command.
    

jindo admin -dumpInventory oss://<hdfs\_bucket\>/

View the output path.

\============Dump Inventory=============

Job Id: 2ce40fba-5704-45c4-8720-d92a891d5cfd

Data Location: oss://<oss\_bucket\>/.dlsdata/.sysinfo/meta\_analyze/inventory/1666584461201.2ce40fba-5704-45c4-8720-d92a891d5cfd

.....................................................................................................................

FINISHED.

The command runs in blocking mode and may take 10 seconds to 10 minutes. The waiting time varies based on the volume of metadata that you want to export. If FINISHED is returned, the metadata is exported. 

*   Download the output files. You can run a jindo command with the AccessKey pair of OSS instead of OSS-HDFS specified, an [ossutil](https://help.aliyun.com/document_detail/50452.html) command, or a Hadoop fs command to download the output files. You can also download the output files in the OSS console. 
    

ossutil cp oss://<oss\_bucket\>/.dlsdata/.sysinfo/meta\_analyze/inventory/1666584461201.2ce40fba-5704-45c4-8720-d92a891d5cfd ./

Run the vi or vim command to open the output files. 

Sample results

{"id":16385,"path":"/","type":"directory","size":0,"user":"admin","group":"supergroup","atime":0,"mtime":1666581702933,"permission":511,"state":1}

{"id":6246684106789500068,"path":"/dls-1000326249","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}

{"id":6246684106789500069,"path":"/dls-1000326249/benchmark","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}

{"id":6246684106789500070,"path":"/dls-1000326249/benchmark/n1","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660889124590,"permission":511,"state":0}

{"id":6246684106789500071,"path":"/dls-1000326249/benchmark/n1/490747449","type":"directory","size":0,"user":"hadoop","group":"supergroup","atime":0,"mtime":1660895613953,"permission":511,"state":0}