helloworld-rs: Helloworld Using JAX-RS (Java API for RESTful Web Services) to demonstrate mutual client SSL Authentication
==========================================================================================================================
Author: Giriraj Sharma
Level: Intermediate
Technologies: SSL, CDI, JAX-RS
Summary: Demonstrates mutual client ssl authentication via the use of wildfly SSL, CDI 1.1 and JAX-RS 2.0
Target Project: WildFly
Source: <https://github.com/girirajsharma/wildfly-ssl-examples/>

What is it?
-----------

This example provides a web application demonstrating basic usage of *Mutual client SSL authentication*, *CDI 1.1* and *JAX-RS 2.0* in *WildFly*.

Please enable wildfly ssl and mutual client ssl authentication before running this quickstart from given README [Getting Started Developing Applications Guide](https://github.com/girirajsharma/wildfly-ssl-examples/tree/master/docs).

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-helloworld-rs.war` to the running instance of the server.


Access the application 
---------------------

The web application is deployed to <http://localhost:8080/wildfly-helloworld-rs>, and includes a root html page that may be used to request the hello world message from the JAX-RS resource, in either XML or JSON format.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
