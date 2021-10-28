# Performance Comparison of JindoFS SDK and Hadoop-OSS-SDK
[中文版](./jindofs_sdk_vs_hadoop_sdk.md)

<a name="6ZORZ"></a>
## Introduction to JindoFS SDK

JindoFS SDK is a simple and easy to use JindoFS client, which is mainly used in E-MapReduce cluster in the initial stage, it supports access to JindoFS cluster and the ability to read and write OSS files. The JindoFS SDK re-implements the OSS client and makes a lot of performance optimizations compared to Hadoop-OSS-SDK. Now that the JindoFS SDK is open for use, we can use it to read and write OSS and achieve better performance.<br />
<br />

<a name="4sw1q"></a>
## How to Use JindoFS SDK

The JindoFS SDK is currently available on [github repo](https://github.com/aliyun/aliyun-emapreduce-sdk), where we can download the JAR package, install and deploy it according to the usage documentation.<br />
<br />

<a name="MUSUo"></a>
## Setup Test Environment

Instance Type：ecs.c6.8xlarge<br />CPU：32 vCPU<br />Memory:  64 GiB<br />Network Bandwidth：10.00Gbps<br />

<a name="SwQNF"></a>
## Software Versions

jindofs-sdk-2.7.1<br />hadoop-aliyun-3.2.1 (bundle aliyun-sdk-oss-3.4.1）<br />
<br />

<a name="zyr0w"></a>
## Test Steps

We need to test from different angles. We need to cover some operations such as read, write, move and delete, and we need to test the performance of large files and small files respectively. So we prepared three data sets.<br />

- Dataset 1: 1 random file with 5GB size
- Dataset 2: 1000 files,  the size of each file is 2bytes (used to simulate small file scenarios)
- Dataset 3: 1000 files, the size of each file is 5MB


<br />

<a name="ntIJV"></a>
## Test Results




| 		Operations | Hadoop-OSS-SDK（second） | JindoFS SDK（second） | JindoFS SDK提升（percentage） |
| --- | --- | --- | --- |
| 		put 5gb file | 27.48 | 25.94 | 6% |
| 		get 5gb file | 60.1 | 32.65 | 46% |
| 		mv 5gb file | 314.57 | 4.86 | 98% |
| 		delete 5gb file | 1.26 | 0.8 | 37% |
| 		<br /> | <br /> | <br /> | <br /> |
| 		put 1000 small files (2bytes) | 40.70 | 31.82 | 22% |
| 		get 1000 small files | 12.26 | 12.13 |  roughly the same |
| 		mv 1000 small files | 5.5 | 2.12 | 61% |
| 		delete 1000 small files | 1.76 | 1.31 | 26% |
| 		<br /> | <br /> | <br /> | <br /> |
| 		put 1000 files (5mb) | 150.64 | 140.62 | 7% |
| 		get 1000 files | 107.74 | 62.47 | 42% |
| 		mv 1000 files | 39.53 | 5.06 | 87% |
| 		delete 1000 files | 1.78 | 1.2 | 33% |


<br />After analyzing the test results, JindoFS SDK performs significantly better than Hadoop-OSS-SDK in put, get, mv and delete operations.
