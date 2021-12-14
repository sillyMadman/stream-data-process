# stream-data-process

stream-data-process is a tool that tries to use SQL to facilitate the development and testing of Flink real-time tasks.


## Instructions


You can run real-time tasks by specifying Kafka input / output source, SQL, and input / output schema information.


## Local test case

### Create Kafka topic using docker

In the environment with docker configured, enter the stream-data-test/docker-kafka directory.
Execute the following command:
```
docker pull wurstmeister/zookeeper
docker pull wurstmeister/kafka
docker-compose up -d
```
After success, Kafka will be run and the two Topic streamdata-source-test and streamdata-sink-test already exist.

### Create topic using existing Kafka

Configure the Kafka port in the config.Properties file of the stream-data-test module.
```
kafka.source.servers = localhost:9092
kafka.sink.servers = localhost:9092
```
You need to manually enter Kafka to create Topic and modify the startup parameters of Kafka port of stream client.
```
bin/kafka-topics.sh --create --zookeeper localhost:2181  --partitions 1 --replication-factor 2 --topic streamdata-source-test
bin/kafka-topics.sh --create --zookeeper localhost:2181  --partitions 1 --replication-factor 2 --topic streamdata-sink-test
```
### Set stream-client run arguments 
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




### Start local test

Start the SimulateKafkaData under the stream-data-test module to generate test data and consumption flink-sink data.

Start the StreamDataProcessCli under the stream client module to start the local flick job.

After success, you can see the processed data under the output result of SimulateKafkaData

Flink-Web-UI: localhost:8081 

