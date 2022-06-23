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
