CONNECT
=======

CONNECT is an open source software solution that supports health information exchange - both locally and at the national level. CONNECT uses Nationwide Health Information Network standards and governance to make sure that health information exchanges are compatible with other exchanges being set up throughout the country.

This software solution was initially developed by federal agencies to support their health-related missions, but it is now available to all organizations and can be used to help set up health information exchanges and share data using nationally-recognized interoperability standards.

License
-------

CONNECT is released under the [Revised BSD License](https://connectopensource.atlassian.net/wiki/x/mQCD).

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

* The System Administration Module provides a graphical user interface (GUI) to simplify the configuration, support, and administration of the CONNECT solution. Key features include: graphical system overview, connection management, property and adapter configuration management, audit log viewer, Direct configuration, and the Cross-Gateway Query Client for testing basic message exchanges against other systems.

History
-------
* 5.3 released July 2019
* 5.2 released November 2018
* 5.1.2 released August 2018
* 5.0 released June 2017
* 4.7 released September 2016
* 4.6 released March 2016
* 4.5 released July 2015
* 4.4 released December 2014
* 4.3 released March 2014
* 4.2 released August 2013
* 4.1 released April 2013
* 4.0 released February 2013
* 3.3 released March 2012

For more information about CONNECT's history, see [HISTORY.md](./HISTORY.md)

Building from Source
---------------
To build CONNECT from source run:

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install

####Building an ear
All services profiles except DB (execute database scripts), X12 and Direct are active by default, if you want to exclude a service, in this case Patient Discovery, you can turn off the profile by adding a "!" to the name of the service profile you'd like to exclude (needs to be escaped with "\" char on *NIX) platforms:

        $ mvn clean install -P \!PD

You can also specify explicitly what services are included in the ear by passing in the individual profiles.  For example, if you only want to include PD:

        $ cd Product/Production/Deploy/
        $ mvn clean install -P PD
		
Please note that if at least one profile is explicitly defined in the build command, ALL desired profiles must be specified. Available profiles include:

* PD - Patient Discovery
* DQ - Document Query
* DR - Document Retrieve
* DS - Document Submission
* AD - Admin Distribution
* X12 - CAQH Core X12
* DDS - Document Data Submission
* admingui - AdminGUI and AdminGUI webservice
* DB - Database scripts (dropall.sql, nhincdb.sql, populateTestData.sql)
* was - WebSphere CONNECT ear
* weblogic - WebLogic CONNECT ear
* Direct - Direct messaging services

If the DB profile is selected, local database parameters must also be defined in the CONNECT/pom.xml file

		<\!-- DB Options -->
        <db.host>xxxxx</db.host>
        <db.password>xxxxx</db.password>
        <db.port>xxxxx</db.port>
        <db.user>xxxxx</db.user>
        <mysql.root.password>xxxxx</mysql.root.password>
        <mysqldriver.version>xxxxx</mysqldriver.version>

For more information on CONNECT supported application servers build and deployment visit: [Installation Instructions](https://connectopensource.atlassian.net/wiki/x/uyYCBw) page.
       

Testing
-------
After the CONNECT Gateway has been installed, the Validation Suite can be run to verify that the installation is working correctly. 

To run Validation Suite via Maven script against a standalone installation of the application server:

        $ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite
        $ mvn verify -Dstandalone -Dproperties.dir=<application server configuration dir>

To execute Validation Suite via SoapUI, run the Validation Suite project file ConnectValidation-soapui-project.xml via SoapUI's command line runner testrunner.sh (or testrunner.bat in Windows).

You can find more details at: [CONNECT Validation Suite](https://connectopensource.atlassian.net/wiki/x/I4Ch)


Documentation
-------------

###Generate & View
You can generate the project's site information by performing the following: 

        $ mvn -P\!embedded-testing site:site site:stage -DstagingSiteURL=/tmp/fullsite

Then open your browser and view [file:///tmp/fullsite/index.html]

Contributing
------------

Please checkout [code contribution](https://connectopensource.atlassian.net/wiki/x/7gCD) for guidelines about how to contribute.
