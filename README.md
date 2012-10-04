The smartcard-web-terminal
==========================

A client-server app with a direct connection between a local PC/SC reader and a web-server.
The smartcard-web-terminal is a simple tech demo and shouldn't be used in production.
The software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.

To build the project from source you need Java 1.7 (JDK 7) and Maven 3


Installation
-----------

    mvn install


Usage
-----

    java -jar terminal-server/target/terminal-server-0.1-SNAPSHOT.jar

Open a web browser an go to http://localhost:8080
