helloworld-html5: HTML5 + REST Hello World Example to demonstrate mutual client SSL Authentication or securing REST war app
============================================================================================================================
Author: Giriraj Sharma
Level: Beginner
Technologies: SSL, CDI, JAX-RS
Summary: Demonstrates mutual client ssl authentication via the use of wildfly SSL, CDI 1.1 and JAX-RS 2.0
Target Product: WFK
Source: <https://github.com/girirajsharma/wildfly-ssl-examples/>

What is it?
-----------

This example provides a web application demonstrating basic usage of *Mutual client SSL authentication*, *CDI 1.1* and *JAX-RS 2.0* in *WildFly*.

Please enable wildfly ssl and mutual client ssl authentication before running this quickstart from given README [Getting Started Developing Applications Guide](https://github.com/girirajsharma/wildfly-ssl-examples/docs).
The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven
3.0 or better.

The application this project produces is designed to be run on JBoss WildFly.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are
required.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

### Deploying locally

First you need to start the JBoss container. To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact by executing the following command:

    mvn wildfly:deploy

This will deploy both the client and service applications.

The application will be running at the following URL <http://localhost:8080/wildfly-helloworld-html5/>.

To undeploy run this command:

    mvn wildfly:undeploy

You can also start the JBoss container and deploy the project using JBoss Tools. See the
<a href="https://github.com/wildfly/quickstart/guide/Introduction/" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>
for more information.

Importing the project into an IDE
=================================

Detailed instructions for using Eclipse / JBoss Tools with are provided in the
<a href="https://github.com/wildfly/quickstart/guide/Introduction/" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

Development notes
=================

You can test the REST endpoint using the following URL
[http://localhost:8080/\<artifactId>/hello/json/David/]("http://localhost:8080/<artifactId>/hello/json/David/").

HelloWorld.java - establishes the RESTful endpoints using JAX-RS

Web.xml - maps RESTful endpoints to "/hello"

index.html - is a jQuery augmented plain old HTML5 web page

Copyright headers
-----------------

To update the copyright headers, just run `mvn license:format -Dyear=<current year>`


