CONNECT
=======

CONNECT is an open source software solution that supports health information exchange – both locally and at the national level. CONNECT uses Nationwide Health Information Network standards and governance to make sure that health information exchanges are compatible with other exchanges being set up throughout the country.

This software solution was initially developed by federal agencies to support their health-related missions, but it is now available to all organizations and can be used to help set up health information exchanges and share data using nationally-recognized interoperability standards.

Uses
----
CONNECT can be used to:

* Set up a health information exchange within an organization
* Tie a health information exchange into a regional network of health information exchanges using Nationwide Health Information Network standards

By advancing the adoption of interoperable health IT systems and health information exchanges, the country will better be able to achieve the goal of making sure all citizens have electronic health records by 2014. Health data will be able to follow a patient across the street or across the country.

Solution
--------
Three primary elements make up the CONNECT solution:

* The Core Services Gateway provides the ability to locate patients at other organizations, request and receive documents associated with the patient, and record these transactions for subsequent auditing by patients and others. Other features include mechanisms for authenticating network participants, formulating and evaluating authorizations for the release of medical information, and honoring consumer preferences for sharing their information. The Nationwide Health Information Network Interface specifications are implemented within this component.

* The Enterprise Service Components provide default implementations of many critical enterprise components required to support electronic health information exchange, including a Master Patient Index (MPI), XDS.b Document Registry and Repository, Authorization Policy Engine, Consumer Preferences Manager, HIPAA-compliant Audit Log and others. Implementers of CONNECT are free to adopt the components or use their own existing software for these purposes.

* The Universal Client Framework contains a set of applications that can be adapted to quickly create an edge system, and be used as a reference system, and/or can be used as a test and demonstration system for the gateway solution. This makes it possible to innovate on top of the existing CONNECT platform.

History
-------
* 4.0 planned February 2013
* 3.3 released March 2012

For more information, about CONNECT's history see [HISTORY.md](./CONNECT/HISTORY.md)

Getting Started
---------------
###Prerequisites
Before you get started, you'll need the following installed and set up:
* [Java (JDK) 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3.0.4+](http://maven.apache.org/download.html)    See [installation instructions](http://maven.apache.org/download.html#Installation).
* [MySQL 5.1.x](http://dev.mysql.com/downloads/mysql/5.1.html#downloads)
* [Eclipse Juno](http://www.eclipse.org/downloads/)
  * [egit plugin](http://www.eclipse.org/egit/)
  * [m2eclipse plugin](http://www.eclipse.org/m2e/download/)
* [Apache Ant v1.7.1](http://ant.apache.org/)


###Building from source
To build all CONNECT modules from source, run:

Windows Users:

        $ set MAVEN_OPTS='-Xmx5000m -XX:MaxPermSize=1024m'

OSX / Linux Users:

        $ export MAVEN_OPTS='-Xmx5000m -XX:MaxPermSize=1024m'

Everyone:

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install

###Generate Eclipse Projects
After you have built from source you can create all of the Eclipse Project files

        $ cd <CONNECT_CLONE_DIR>
        $ mvn eclipse:clean eclipse:eclipse

Before launching eclipse, execute the following to set up the M2_REPO var used in lib dependencies

        $ mvn eclipse:configure-workspace -Declipse.workspace=/path/to/your/workspace

When complete, open Eclipse then click on the following:

        File --> Import --> 'Existing Projects into workspace'

and choose the clone repo directory (\<CONNECT_CLONE_DIR\>), e.g. CONNECT. You may also need to repeat these steps and choose additional directories:
* \<CONNECT_CLONE_DIR\>/Product
* \<CONNECT_CLONE_DIR\>/Product/Production

####Building an ear
All services profiles are active by default, so to build an ear containing all services, just execute:

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install

If you want to exclude a service, in this case PD, you can turn off the profile by adding a "!" to the name of the service profile you'd like to exclude (needs to be escaped with "\" char on *NIX) platforms:

        $ mvn clean install -P \!PD

Available service profiles which can be excluded from the generated ear (use value within parentheses):
* Admin Distribution (AD)
* Patient Discovery (PD)
* Document Query (DQ)
* Document Submission (DS)
* Document Retrieve (DR)
* HIEM (HIEM)

You can alter the composition of the CONNECT.ear at any time by turning off any combination of the available profiles as a comma-separated list

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean package -P \!PD,\!DQ,!DR -f Product/Production/Deploy/pom.xml

OR

        $ cd Product/Production/CONNECT/
        $ mvn clean package -P \!PD,\!DQ,!DR

######Altering targeted application server
For some application server deployments the generated .ear needs different dependencies. The following profiles are available to control which type of .ear is generated (use value within parentheses):
* GlassFish v3.1.2.2 (glassfish)
* WebSphere Application Server Community Edition v3.0.0.2 (websphere)

This profile options are used just like above. As an example to generate a WebSphere specific .ear with only Patient Discovery.

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install -P \!AD,\!DQ,\!DS,\!RD,\!HIEM,websphere

###Setup Glassfish, MySQL & Deploy CONNECT
These steps will install and configure a Glassfish Application Server, prepare your MySQL databases and deploy CONNECT for use. Lets get started.

Navigate to the <CONNECT_CLONE_DIR>/Product/Install directory

        $ cd <CONNECT_CLONE_DIR>/Product/Install

Next, copy install.properties to local.install.properties and update with your local information. Generally this just specifying where
you want GlassFish to install.

        $ cp install.properties local.install.properties


####Setup MySQL Databases
These steps will install and configure a Glassfish Application Server to deploy and use CONNECT. Lets get started.

Navigate to the <CONNECT_CLONE_DIR>/Product/Install directory

        $ cd <CONNECT_CLONE_DIR>/Product/Install

Next, copy install.properties to local.install.properties and update with your local information. Generally this is just specifying a password for mysql.

        $ cp install.properties local.install.properties

Lastly, we're going to install the MySQL databases needed for CONNECT.

        $ ant install.databases

The "mysql-connector-java-5.1.10.jar" must be copied into the following folder, for container managed database resources

        <GLASSFISH_HOME>/domains/domain1/lib/ext

####Setup GlassFish
These steps will install and configure a Glassfish Application Server to deploy and use CONNECT. Lets get started.

Navigate to the <CONNECT_CLONE_DIR>/Product/Install directory

        $ cd <CONNECT_CLONE_DIR>/Product/Install

Next, copy install.properties to local.install.properties and update with your local information. Generally this just specifying where
you want GlassFish to install.

        $ cp install.properties local.install.properties

Lastly, we're going to install the Glassfish Application Server

        $ ant install


####Deploy to GlassFish
Navigate to the <CONNECT_CLONE_DIR>/Product/Install directory

        $ cd <CONNECT_CLONE_DIR>/Product/Install
        $ ant deploy.connect

Usage
-----
TK

Testing
-------
###Run the Validation Suite as part of install
At the end of the mvn install process, an embedded GlassFish instance will start and the Validation Suite will run against it:

        $ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite
        $ mvn clean install

###Run the Validation Suite via Maven sript
The Validation Suite can be run via a Maven script against a standalone installation of the application server:

        $ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite
        $ mvn verify -Dstandalone -Dproperties.dir=<applicaiton server configuration dir>

Several properties can be passed for mvn verify:

        -Dstandalone -- must be passed in for standalone testing
        -Dproperties.dir=<gateway config dir> -- for GlassFish this is <GlassFish home>/domains/domain1/config/nhin; there is an equivalent in WebSphere 
        -Dhost=<machine name or IP address to act as the requesting gateway> -- defaults to localhost, but can be passed in to test on remote machines
        -Dport=<####> -- defaults to 8080; the unsecured entity/message-proxy port
        -Dsecured.port=<####> -- defaults to 8181; the secured entity/message-proxy port
        -Ddb.host=<machine name or IP address of the MySQL server> --  defaults to localhost
        -Ddb.port=<####> -- defaults to 3306
        -Ddb.user=<database user name> -- defaults to nhincuser
        -Ddb.password=<database password> -- defaults to nhincpass
        -Dtest.suite=<g0 or g1>
        -Dtest.case=<test case name> -- one of "Document Submission Deferred Req", "Document Submission Deferred Resp", "Document Submission", "Patient Discovery Deferred Req", "Patient Discovery Deferred Resp", "Patient Discovery", "Document Query", "Document Retrieve", "Subscribe", "Notify", "Unsubscribe", "Admin Distribution"
        -Dentity.skip -- pass in to skip Entity testing
        -Dmsgproxy.skip -- pass in to skip MsgProxy testing
		
Alternatively, any of these properties can be set in your maven settings.xml file, and they will be propagated to all your builds.  Here is an example showing the mysql.root.password property set to a non-default value:

        <properties>
            <mysql.root.password>f00B4r</mysql.root.password>
            ...
        </properties>

###Run the Validation Suite via SoapUI
The Validation Suite can be run with SoapUI. First, follow the instructions "Setting up SoapUI" below.

Set the property "GatewayPropDir" in MsgProxyValidation-soapui-project.properties and EntityValidation-soapui-project.properties in the Validation Suite directory. This should be set to the gateway configuration directory. For GlassFish this is <GlassFish home>/domains/domain1/config/nhin; there is an equivalent in WebSphere

Run the Validation Suite project files MsgProxyValidation-soapui-project.xml and EntityValidation-soapui-project.xml via SoapUI's command line runner testrunner.sh (or testrunner.bat in Windows).

##Setting up SoapUI
Install SoapUI v4.5.1.

Copy the MySQL jdbc driver mydql-connector-java-5.1.10.jar from the Maven repository directory .m2/repository/mysql/mysql-connector-java/5.1.10 to <SoapUI home>/bin/ext.

Copy the file FileUtils-4.0.0-SNAPSHOT.jar (or similarly named) to <SoapUI home>/bin/ext.


Contributing
------------
1. Fork it.
2. Create a branch (`git checkout -b my_feature`)
3. Commit your changes (`git commit -am "Added new feature"`)
4. Push to the branch (`git push origin my_feature`)
5. Open a [Pull Request][]

[Pull Request]: https://github.com/CONNECT-Solution/CONNECT/pulls
[Download Maven]: http://maventest.apache.org/download.html
[Install Maven]: http://maventest.apache.org/download.html#Installation
[Eclipse]: http://www.eclipse.org/downloads/
[ant 1.7.1]: http://archive.apache.org/dist/ant/binaries/apache-ant-1.7.1-bin.zip
[egit plugin]: http://www.eclipse.org/egit/download/
[m2eclipse plugin]: http://eclipse.org/m2e/
[MySQL 5.1.x]: http://dev.mysql.com/downloads/mysql/5.1.html

