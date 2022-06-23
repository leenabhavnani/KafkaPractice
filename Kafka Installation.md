# How to Install Apache Kafka on AWS EC2(Ubuntu 22.04 t2.micro)

This article will walk you through the steps to install Kafka on Ubuntu 22.04 using simple steps. 

### Procure an AWS EC2 instance under free tier

### Step 1: Install Java and Zookeeper
Kafka is written in Java and Scala and requires jre 1.7 and above to run it. In this step, you need to ensure Java is installed.

```
sudo apt-get update
sudo apt-get install default-jre
```
Install Zookeeper using following command -

```
sudo apt-get install zookeeperd
```
Check if Zookeeper is alive and if it’s OK
```
telnet localhost 2181
ruok
```
(are you okay) if it’s all okay it will end the telnet session and reply with

todo - screenshot

### Step 2: Create a Service User for Kafka

Run the following command and set the password 
Add the User to the Sudo Group and then change the user to kafka
```
sudo adduser kafka 
sudo adduser kafka sudo
sudo su -l kafka
```

### Step 3: Download Apache Kafka

Create a directory to copy the installation files
Check the latest version in the url - https://downloads.apache.org/kafka/ and update it in the curl command, if required
Change the directory and unzip the contents in new directory

```
mkdir ~/Downloads
curl "https://downloads.apache.org/kafka/3.2.0/kafka_2.12-3.2.0.tgz" -o ~/Downloads/kafka.tgz
mkdir ~/kafka && cd ~/kafka
tar -xvzf ~/Downloads/kafka.tgz --strip 1
```

### Step 4: Configuring Kafka Server

Change the log directory path  to **log.dirs=/home/kafka/logs**

```
sudo vi ~/kafka/config/server.properties

```

### Step 5: Setting Up Kafka Systemd Unit Files

Create systemd unit file for Zookeeper with below command:
```
sudo vi /etc/systemd/system/zookeeper.service
```
Next, you need to add the below content:
```
[Unit]
Requires=network.target remote-fs.target
After=network.target remote-fs.target

[Service]
Type=simple
User=kafka
ExecStart=/home/kafka/kafka/bin/zookeeper-server-start.sh /home/kafka/kafka/config/zookeeper.properties
ExecStop=/home/kafka/kafka/bin/zookeeper-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target
```
Save this file and then close it. Then you need to create a Kafka systemd unit file using the following command snippet:

```
sudo vi /etc/systemd/system/kafka.service

[Unit]
Requires=zookeeper.service
After=zookeeper.service

[Service]
Type=simple
User=kafka
ExecStart=/bin/sh -c '/home/kafka/kafka/bin/kafka-server-start.sh /home/kafka/kafka/config/server.properties > /home/kafka/kafka/kafka.log 2>&1'
ExecStop=/home/kafka/kafka/bin/kafka-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target
```
Use the following command to start Kafka and then check the status:
```
sudo systemctl start kafka
sudo systemctl status kafka
```
You are more likely to see an error and the service will be inactive.
Check the logs 

```
cat /home/kafka/kafka/kafka.log
``` 
It might say something like this
OpenJDK 64-Bit Server VM warning: INFO: os::commit_memory(0x00000000c0000000, 1073741824, 0) failed; error='Not enough space' (errno=12)

#### To fix this issue
Update the kafka-server-start.sh file and set the values as below
 export KAFKA_HEAP_OPTS="-Xmx512M -Xms512M"
 
 ```
sudo vi /home/kafka/kafka/bin/kafka-server-start.sh
systemctl daemon-reload
```
Try to start the service again
```
sudo systemctl start kafka
sudo systemctl status kafka
```

### Step 6: Testing installation

Create a new topic and then list all topics

```
~/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic MyTestTopic

~/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```
The kafka-console-producer.sh script can be used to build a producer from the command line. 
As arguments, it expects the hostname, port, and topic of the Kafka server.

```
echo "Hello, World" | ~/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic MyTestTopic > /dev/null

~/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic MyTestTopic --from-beginning

```
Hello, World will appear in your terminal 
To exit the consumer process, press CTRL+C

Open a new terminal window and log into your server to try this.
Start a producer in this new terminal to send out another message:

```
$ echo "This is a new message! " | ~/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic MyTestTopic > /dev/null

```
This message will appear in the consumer’s output:

```
Hello, World
This is a new message!
```
```
