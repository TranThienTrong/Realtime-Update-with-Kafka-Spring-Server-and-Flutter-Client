# Realtime Update with Kafka on Spring Server and Flutter Client

![91c167b8b221717f2830](https://user-images.githubusercontent.com/36349293/178051599-970d60f3-d4c8-47ad-a8e7-a83b179a7c5d.jpg)

### STEPS
1. Start Kafka
2. Start Spring Boot Server
3. Start Flutter Client

### START KAFKA
1.	https://kafka.apache.org/downloads nhớ download file BINARY

2.	Start Zookeeper

    ./bin/zookeeper-server-start.sh ../config/zookeeper.properties

3.	Vào config/server.properties , thêm 2 dòng này vào server.properties bằng Vim hoặc VSCode
    listeners=PLAINTEXT://localhost:9092
    auto.create.topics.enable=false


4.	Start up the Kafka Broker
    ./kafka-server-start.sh ../config/server.properties


### SAMPLE REQUEST

#### [POST] http://localhost:8070/v1/libraryEvent
{
    "libraryEventId": null,
    "book": {
        "bookId": 1,
        "bookName": "Bi kip tan gai",
        "bookAuthor": "Trong"
    }
}
