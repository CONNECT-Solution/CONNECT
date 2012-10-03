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
* 4.0 planned Febuary 2013
* 3.3 released March 2012
  
  (see HISTORY.md)

Getting Started
---------------
###Prerequisites
Before you get started, you'll need the following installed and set up: 
* [Java (JDK) 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3.0.4+](http://maven.apache.org/download.html)	See [installation instructions](http://maven.apache.org/download.html#Installation).
* [MySQL 5.1.x](http://dev.mysql.com/downloads/mysql/5.1.html#downloads)
* [Eclipse Juno](http://www.eclipse.org/downloads/) 
  * [egit plugin](http://www.eclipse.org/egit/)
  * [m2eclipse plugin](http://www.eclipse.org/m2e/download/)
* [Apache Ant v1.7.1](http://ant.apache.org/)


###Building from source
To build all CONNECT modules from source, run: 
        
        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install 

###Generate Eclipse Projects
After you have built from source you can create all of the Eclipse Project files

		$ cd <CONNECT_CLONE_DIR>
        $ mvn eclipse:clean eclipse:eclipse

When complete, open Eclipse then click on the following:

		File --> Import --> 'Existing Projects into workspace'

and choose the clone repo directory (\<CONNECT_CLONE_DIR\>), e.g. CONNECT. You may also need to repeat these steps and choose additional directories:
* \<CONNECT_CLONE_DIR\>/Product
* \<CONNECT_CLONE_DIR\>/Product/Production

####Building an ear
For an ear with Patient Discovery, Document Query, Retrieve Document, Document Submission you would execute the following command.

		
        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install -P PD,DQ,DR,DS
        
Available profiles to alter the composition of bundled gateways and adapters within the .ear generated for deployment are enumerated below (use value within parentheses):        
* Admin Distribution (AD)
* Patient Discovery (PD)
* Document Query (DQ)
* Document Submission (DS)
* Document Retrieve (DR)
* HIEM (HIEM)
        
You can alter the composition of the CONNECT.ear at any time by specifying any combination of the available profiles as a comma-separated list 
		
		$ cd <CONNECT_CLONE_DIR>
		$ mvn clean package -P <profiles> -f Product/Production/CONNECT/pom.xml
		
OR 
		
		$ cd Product/Production/CONNECT/
		$ mvn clean package -P <profiles>

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
        
Next, copy install.properties to local.install.properties and update with your local information. Generally this just specifying where 
you want GlassFish to install.	

		$ cp install.properties local.install.properties

Lastly, we're going to install the Glassfish Application Server         
        $ ant install
        	

####Setup GlassFish
These steps will install and configure a Glassfish Application Server to deploy and use CONNECT. Lets get started.  

Navigate to the <CONNECT_CLONE_DIR>/Product/Install directory

        $ cd <CONNECT_CLONE_DIR>/Product/Install
        
Next, copy install.properties to local.install.properties and update with your local information. Generally this just specifying where 
you want GlassFish to install.	

		$ cp install.properties local.install.properties

Lastly, we're going to install the Glassfish Application Server         
        
        $ ant install.glassfish


####Deploy to GlassFish
Navigate to the <CONNECT_CLONE_DIR>/Product/Install directory

        $ cd <CONNECT_CLONE_DIR>/Product/Install
        $ ant deploy.connect

Usage
-----
TK

Testing
-------

###Perform Validation Tests

		$ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite
        $ mvn clean test



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

