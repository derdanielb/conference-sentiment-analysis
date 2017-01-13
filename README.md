# conference-sentiment-analysis
Analyze the success of your conference.

For a guide how to setup your development environment see [DEVENV](DEVENV.md).

The application initially consists of the following components:

##### csa-conference

Microservice that stores and manages conferences. This service owns the master data of conferences known within the application. Each conference has a name, a period of time when it takes place, a Twitter hash-tag, etc. 

##### csa-twitter-search

Microservice that encapsulates the Twitter Search API. This service exposes basic tweet search functionality via a REST API. 

##### csa-docker-composition

Environment setup including storage engines with Docker Compose.

##### csa-tweet-collector

Job collecting tweets from twitter via csa-twitter-search to Kafka topics.

##### csa-tweet-analysis

Analysis of tweets of conferences.

##### csa-conference-collector

Job collecting conferences from a CSV source and importing them to MongoDB.

### Kafka

Kafka is used to persist streams.

##### Start Kafka

Open a shell and go to the docker-composition directory in this project.

Set the variable SERVER_IP_OR_HOST in your shell to:
* localhost if you are using Linux or Windows
* the IP of your machine if you are using Docker for Mac

by using the command `export SERVER_IP_OR_HOST=<value>`.

Make sure that in the source code or configuration the same value is used for connecting to Kafka.

Launch with the command `docker-compose up -d`

##### See Logging Output

Use the command described at https://docs.docker.com/engine/reference/commandline/logs/

##### List Topics

`docker exec -ti codecamp-akka-streams-java-kafka-01-c /usr/bin/kafka-topics --list --zookeeper codecamp-akka-streams-java-zookeeper4kafka-01-s:44776`

##### Describe a Topic

`docker exec -ti codecamp-akka-streams-java-kafka-01-c /usr/bin/kafka-topics --describe --zookeeper codecamp-akka-streams-java-zookeeper4kafka-01-s:44776 -topic kata06-topic`

##### Create a Topic

`docker exec -ti codecamp-akka-streams-java-kafka-01-c /usr/bin/kafka-topics --create --zookeeper codecamp-akka-streams-java-zookeeper4kafka-01-s:44776 --topic kata06-topic --partitions 1 --replication-factor 1`

##### Stop Kafka and remove all Docker volumes and thus wipe all data

`docker-compose down -v`

##### Stop Kafka keeping data

`docker-compose down`
