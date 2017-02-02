#!/bin/bash
cd ..
mvn package
cd dockerimage
cp ../target/csa-twitter-search-1.0.0-SNAPSHOT.jar ./
cp /tmp/my_twitter_consumer_key.txt ./
cp /tmp/my_twitter_consumer_secret.txt ./
sudo docker build -t csa-twitter-search .
sudo docker run -d -p 8080:8080 csa-twitter-search:latest
