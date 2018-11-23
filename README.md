This repository represents code for Saratov's Thumbtack Expertise Day presentation on Scala abstraction concepts.

# Prerequisites
1. Install and run Kafka localy

Starting a single broker:
```
$ wget http://apache-mirror.rbc.ru/pub/apache/kafka/1.1.1/kafka_2.11-1.1.1.tgz
$ cd kafka_2.11-1.1.1/bin/
$ ./zookeeper-server-start.sh -daemon ../config/zookeeper.properties  
$ ./kafka-server-start.sh -daemon ../config/server.properties
```

2. Create "IN_1" topic
```
./kafka-topics.sh --zookeeper localhost:2181 --create --topic IN_1 --partitions 1 --replication-factor 1
```

3. Connect to the topic by console producer
```
./kafka-console-producer.sh --broker-list localhost:9092 --topic IN_1
```
From this moment you can publish messages one-by-one to the topic and two APIs (Spark Streaming and Kafka Stream) will be consuming data from it. Or you can check data using console consumer:
```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic IN_1
```

# Examples
Run:
- TestStreamExample.scala to process data using in-memory data structure (immutable.Stream)
- SparkStreamExample.scala to process data from IN_1 topic using *Spark Streaming*
- KafkaStreamExample.scala to process data from IN_1 topic using *Kafka Streams*



