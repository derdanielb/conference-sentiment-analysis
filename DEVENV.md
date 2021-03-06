### Register a Twitter Application

To fully follow through the exercises based on this source repository, i.e. to be able to integrate this application with the Twitter Search API, you need to register a Twitter Application under your Twitter Account. First, if you do not have a Twitter account, sign up at https://twitter.com/

Second, create your own Twitter application by following the steps in the following article and privately store your Consumer Key and Consumer Secret.

Note: You do not need to create an Access Token, since this application is not going to access your (any) Twitter user profile, so you can skip step 6.

https://iag.me/socialmedia/how-to-create-a-twitter-app-in-8-easy-steps/ 

Your Consumer Key and Consumer Secret later need to be referenced in the Twitter search service.

### Install Docker Engine

Note: You do not have to follow any of the optional additional installation steps of Docker, but you may.

##### On Ubuntu

https://docs.docker.com/engine/installation/linux/ubuntulinux/

##### On Mac OS X

Install the new "Docker for Mac" directly (do not go the old way via Docker Toolbox and Virtual Box).

https://docs.docker.com/engine/installation/mac/#/docker-for-mac

##### On other operating systems

https://docs.docker.com/engine/installation/

### Install Docker Compose

##### On Linux

This is a simple installation procedure that gives you the desired version. In this example it is version 1.8.1. Replace the version folder if desired.

* check the required release version
* Install with: sudo curl -o /usr/local/bin/docker-compose -L "https://github.com/docker/compose/releases/download/1.8.1/docker-compose-$(uname -s)-$(uname -m)"
* Set permission to execute with: sudo chmod +x /usr/local/bin/docker-compose
* Test to see if you got it: sudo docker-compose -v

#### Under Linux configure Docker group for convenience

Normally, docker and docker-compose are executed with sudo under Linux, which is fine. If you prefer the convenience to omit the sudo, check the optional additional installation steps, where you find a variant to create a docker group on your system.

Note: This is okay for development environments, but it should not be done in production systems.

##### On Ubuntu

https://docs.docker.com/engine/installation/linux/ubuntulinux/#/create-a-docker-group

### Install Java JDK

Java version is 8 or higher. This guide refers to OpenJDK. You may alternatively use Oracle JDK.

##### On Ubuntu

You may install a default JDK on ubuntu with:

* sudo apt-get install default-jdk (currently this yields OpenJDK 8)
* sudo apt-get install openjdk-8-source (this adds the source archive to the just installed OpenJDK 8)

##### On other operation systems

http://openjdk.java.net/install/

### Install a self-contained Maven

Instead of using e.g. apt-get to install Maven, it is better to manually install it.

Just follow these steps:

* Download Maven http://ftp.fau.de/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.zip or newer.
* Move the downloaded file to your home directory ~
* unzip the downloaded file right in your home directory.
* Delete the zip file to clean up.
* Edit the file ~/.profile (or depending on you Linux distribution ~/.bash_profile or ~/.bashrc etc.) in your home directory to append the following lines to it, adjust the version number if required:

```
# Maven
MAVEN_HOME="$HOME/apache-maven-3.3.9"
MAVEN_OPTS="-Xms256m -Xmx512m"
PATH="$PATH:$MAVEN_HOME/bin"
```

Now you can use Maven. Try the CLI with the command "mvn -version". For using mvn see also https://maven.apache.org/run.html

Note: The default directory where Maven stores the local Maven repository with all the Java libraries is located at ~/.m2/repository/

### Install your favourite Java IDE

For example https://www.jetbrains.com/idea/

### Notes on Java project structure

Every Java project using Maven should follow the Maven project structure conventions.

https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

Every such project should have a root pom.xml file located in the root project folder. Sometimes projects have several pom.xml files, but there should be a root. Most project only have 1 anyways.

To open and develop the project in IDEA you can simply use the File > Open dialog to open the project root folder that contains the root pom.xml

The layout supports many use cases as Java is very broad and powerful. Normal simple projects have the following folder structures for sources:

* src/main/java - Java source files
* src/main/resources - configuration files
* src/test/java - Java source files of tests
* src/test/resources - configuration files for tests

On build with e.g. "mvn clean install", Maven creates a directory called target with the build output including e.g. the distribution as a JAR file.

Every Java project should also have a proper .gitignore file to make sure that only relevant files are committed to the version control system.
