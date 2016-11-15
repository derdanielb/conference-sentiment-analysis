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
