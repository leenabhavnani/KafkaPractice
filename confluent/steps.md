### To start a single node Zookeeper and Broker in Kafka

Run the following command 

```
docker-compose up -d
docker ps
```

To Test if Kafka is running

```
while ! nc -z localhost 29092; do   
  sleep 1
  echo "Waiting on Kafka to launch on 29092..."
done

echo "Kafka is running"

  ```
  
To create a topic and send messages follow the below steps

```
docker-compose exec kafka bash

kafka-topics \
    --bootstrap-server localhost:9092 \
    --create \
    --topic tweets \
    --partitions 4

kafka-topics \
  --bootstrap-server localhost:9092 \
  --list


kafka-console-producer \
    --bootstrap-server localhost:9092 \
    --topic tweets \
    --property 'key.separator=|' \
    --property 'parse.key=true'


1|{"id": 1, "name": "Elyse"}
2|{"id": 2, "name": "Mitch"}
Finally, hit ^C to exit the prompt.

kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic tweets \
    --from-beginning \
    --property print.key=true

Hit ^C

```

We have saved two additional records to a file called inputs.txt. You can view the contents of this file by running the following command:

cat /data/inputs.txt

 kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic tweets \
  --property 'parse.key=true' \
  --property 'key.separator=|' < /data/inputs.txt

kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic tweets \
    --from-beginning \
    --property print.key=true


### Follow the below steps to create a Kafka cluster

Run following commands

```
export DOCKER_HOST_IP=127.0.0.1

docker-compose -f zk-multiple-kafka-multiple.yml up -d

docker ps
```
