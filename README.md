# stream-data-process

stream-data-process is a tool that tries to use SQL to facilitate the development and testing of Flink real-time tasks.

stream-data-process 是一个工具，旨在通过使用 SQL 来促进 Flink 实时任务的开发和测试。

## Instructions使用说明：


You can run real-time tasks by specifying Kafka input / output source, SQL, and input / output schema information.

您可以通过指定 Kafka 输入/输出源、SQL 和输入/输出模式信息来运行实时任务。

## Local test case本地测试案例：

### Create Kafka topic using docker使用 Docker 创建 Kafka 主题

In the environment with docker configured, enter the stream-data-test/docker-kafka directory.
Execute the following command:

在已配置 Docker 的环境中，进入 stream-data-test/docker-kafka 目录。执行以下命令：


```
docker pull wurstmeister/zookeeper
docker pull wurstmeister/kafka
docker-compose up -d
```
After success, Kafka will be run and the two Topic streamdata-source-test and streamdata-sink-test already exist.

成功后，Kafka 将运行，并且已经存在两个主题 streamdata-source-test 和 streamdata-sink-test。

### Create topic using existing Kafka使用现有 Kafka 创建主题

Configure the Kafka port in the config.Properties file of the stream-data-test module.

在 stream-data-test 模块的 config.Properties 文件中配置 Kafka 端口。

```
kafka.source.servers = localhost:9092
kafka.sink.servers = localhost:9092
```
You need to manually enter Kafka to create Topic and modify the startup parameters of Kafka port of stream client.

您需要手动进入 Kafka 来创建主题，并修改 Kafka 端口的流客户端启动参数。

```
bin/kafka-topics.sh --create --zookeeper localhost:2181  --partitions 1 --replication-factor 2 --topic streamdata-source-test
bin/kafka-topics.sh --create --zookeeper localhost:2181  --partitions 1 --replication-factor 2 --topic streamdata-sink-test
```
### Set stream-client run arguments 设置流客户端运行参数：
```
-s1
localhost:9092
-s2
localhost:9092
-sql
"select name,age,eventTime from source"
-c1
kafka
-c2
kafka
-t1
streamdata-source-test
-t2
streamdata-sink-test
-g
test
-ss1
{'name':'string','age':'int','eventTime':'timeStamp(3)'}
-ss2
{'name':'string','age':'int','eventTime':'timeStamp(3)'}
```

Parameter description:

-s1: The port of the Kafka data source for the Flink JOB.

-s2: The port of the Kafka data sink for the Flink JOB.

-sql: Flink SQL to be executed.

-c1: Connection type of data source.

-c2: Connection type of output sink.

-t1: Kafka Topic of data source.

-t2: Kafka Topic of data sink.

-g: Group ID of consumption data source

-ss1: Schema information for the data source

-ss2: Schema information for the data sink

参数说明：

-s1：Flink JOB 的 Kafka 数据源端口。

-s2：Flink JOB 的 Kafka 数据汇出端口。

-sql：要执行的 Flink SQL。

-c1：数据源的连接类型。

-c2：输出汇出的连接类型。

-t1：数据源的 Kafka 主题。

-t2：数据汇出的 Kafka 主题。

-g：消费数据源的组 ID。

-ss1：数据源的模式信息。

-ss2：数据汇出的模式信息。


### Start local test开始本地测试

Start the SimulateKafkaData under the stream-data-test module to generate test data and consumption flink-sink data.

Start the StreamDataProcessCli under the stream client module to start the local flick job.

After success, you can see the processed data under the output result of SimulateKafkaData

Flink-Web-UI: localhost:8081 

启动stream-data-test模块下的SimulateKafkaData来生成测试数据和消费 Flink 输出数据。

启动stream客户端模块下的StreamDataProcessCli来启动本地Flink任务。

成功后，您可以在SimulateKafkaData的输出结果下看到处理后的数据。

Flink Web UI：localhost:8081

