CONNECT
=======

CONNECT is an open source software solution that supports health information exchange - both locally and at the national level. CONNECT uses Nationwide Health Information Network standards and governance to make sure that health information exchanges are compatible with other exchanges being set up throughout the country.

This software solution was initially developed by federal agencies to support their health-related missions, but it is now available to all organizations and can be used to help set up health information exchanges and share data using nationally-recognized interoperability standards.

Revised BSD License
-------

CONNECT is released under the [BSD](https://connectopensource.atlassian.net/wiki/x/mQCD) .

Uses
----
CONNECT can be used to:

* Set up a health information exchange within an organization
* Tie a health information exchange into a regional network of health information exchanges using Nationwide Health Information Network standards
* Send and receive Direct messages; see: [Setting up CONNECT as a Direct HISP](/Product/Production/Services/DirectCore/README.md)

By advancing the adoption of interoperable health IT systems and health information exchanges, the country will better be able to achieve the goal of making sure all providers have access to patient health data. Health data will be able to follow a patient across the street or across the country.

Solution
--------
Three primary elements make up the CONNECT solution:

* The Core Services Gateway provides the ability to locate patients at other organizations, request and receive documents associated with the patient, and record these transactions for subsequent auditing by patients and others. Other features include mechanisms for authenticating network participants, formulating and evaluating authorizations for the release of medical information, and honoring consumer preferences for sharing their information. The Nationwide Health Information Network Interface specifications are implemented within this component.

* The Enterprise Service Components provide default implementations of many critical enterprise components required to support electronic health information exchange, including a Master Patient Index (MPI), XDS.b Document Registry and Repository, Authorization Policy Engine, Consumer Preferences Manager, HIPAA-compliant Audit Log and others. Implementers of CONNECT are free to adopt the components or use their own existing software for these purposes.

* The Universal Client Framework contains a set of applications that can be adapted to quickly create an edge system, and be used as a reference system, and/or can be used as a test and demonstration system for the gateway solution. This makes it possible to innovate on top of the existing CONNECT platform.

History
-------
* 4.4 released December 2014
* 4.3 released March 2014
* 4.2 released August 2013
* 4.1 released April 2013
* 4.0 released February 2013
* 3.3 released March 2012

For more information about CONNECT's history, see [HISTORY.md](./HISTORY.md)

Getting Started
---------------
###Building from source
To build CONNECT from source run:

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install

####Building an ear
All services profiles are active by default, if you want to exclude a service, in this case Patient Discovery, you can turn off the profile by adding a "!" to the name of the service profile you'd like to exclude (needs to be escaped with "\" char on *NIX) platforms:

        $ mvn clean install -P \!PD

You can also specify explicitly what services are included in the ear by passing in the individual profiles.  For example, if you only want to include PD:

        $ cd Product/Production/Deploy/
        $ mvn clean install -P PD

For more information on CONNECT supported application servers build and deployment visit: [Installation Instructions](https://connectopensource.atlassian.net/wiki/x/YoAGAQ) page.
       

Testing
-------
###Run the Validation Suite as part of install
At the end of the mvn install process, an embedded GlassFish instance will start and the Validation Suite will run against it. The maven scripts automatically stand up the embedded glassfish using trust chain certificates:

        $ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite 
        $ mvn clean install

###Run the Validation Suite via Maven script
The Validation Suite can be run via a Maven script against a standalone installation of the application server:

        $ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite
        $ mvn verify -Dstandalone -Dproperties.dir=<application server configuration dir>

Several properties can be passed for mvn verify:

        -Dstandalone -- must be passed in for standalone testing
        -Dproperties.dir=<gateway config dir> -- for GlassFish this is <GlassFish home>/domains/domain1/config/nhin; there is an equivalent in WebSphere 
        -Ddb.host=<machine name or IP address of the MySQL server> --  defaults to localhost
        -Ddb.port=<####> -- defaults to 3306
        -Ddb.user=<database user name> -- defaults to nhincuser
        -Ddb.password=<database password> -- defaults to nhincpass
        -Dtest.suite=<g0 or g1>
        -Dtest.case=<test case name> -- one of "Document Submission Deferred Req", "Document Submission Deferred Resp", "Document Submission", "Patient Discovery Deferred Req", "Patient Discovery Deferred Resp", "Patient Discovery", "Document Query", "Document Retrieve", "Admin Distribution"
		
Alternatively, any of these properties can be set in your maven settings.xml file, and they will be propagated to all your builds.  Here is an example showing the mysql.root.password property set to a non-default value:

        <properties>
            <mysql.root.password>f00B4r</mysql.root.password>
            ...
        </properties>

###Run the Validation Suite via SoapUI
The Validation Suite can be run with SoapUI. First, follow the instructions "Setting up SoapUI" below.

Set the property "GatewayPropDir" in ConnectValidation-soapui-project.properties in the Validation Suite directory. This should be set to the gateway configuration directory. For GlassFish this is <GlassFish home>/domains/domain1/config/nhin; there is an equivalent in WebSphere, JBoss and WebLogic.

Run the Validation Suite project file ConnectValidation-soapui-project.xml via SoapUI's command line runner testrunner.sh (or testrunner.bat in Windows).

You can find more details at: [CONNECT Validation Suite](https://connectopensource.atlassian.net/wiki/x/I4Ch)

##Setting up SoapUI
Install SoapUI v4.5.1.

Copy the MySQL jdbc driver mysql-connector-java-5.1.10.jar from the Maven repository directory .m2/repository/mysql/mysql-connector-java/5.1.10 to {$SoapUI_home}/bin/ext.

Copy the file FileUtils-4.0.0-SNAPSHOT.jar (or similarly named) to {$SoapUI_home}/bin/ext.


Documentation
-------------

###Generate & View
You can generate the project's site information by performing the following: 

        $ mvn -P\!embedded-testing site:site site:stage -DstagingSiteURL=/tmp/fullsite

Then open your browser and view [file:///tmp/fullsite/index.html]

Contributing
------------

Please checkout [code contribution](https://connectopensource.atlassian.net/wiki/x/7gCD) for guidelines about how to contribute.