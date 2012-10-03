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
  
[Release Notes](HISTORY.MD)

Getting Started
---------------
###Pre-reqs
* java 7
* maven 3.0.4
* mysql
* eclipse juno (with egit and m2eclipse plugins)
* ant 1.7.1

###Thirdparty libs
from the ThirdParty directory install the required dependencies [Thirdparty](Thirdparty/README.md)

###Eclipse
from the product directory

`mvn clean install` 
`mvn eclipse:clean eclispe:eclipse`

(in eclipse)
File | Import | Existing project in workspace
and choose the clone repo directory (ex. CONNECT )

###Deploying
To setup the GlassFish application server
(in the Product/Install directory)
copy install.properties to local.install.properties and update with your local information
`ant -Dskip.build -Dskip.deploy` 

from the Product directory
`mvn install -PPD,DQ,DR,DS,AD,HIEM`

change to the domain1/config directory in the domain1 that was created from the glassfish install (`cd domain1/config`) 
make a nhin directory (`mkdir nhin`)
change to the domain1/config/nhin (`cd nhin`)

expand the properties jar in the domain1/nhin/directory
`jar xvf $M2_REPO/org/connectopensource/Properties.jar`


start the glassfish domain
`asadmin start-domain`

copy the Product/Production/CONNECT/target/CONNECT.ear to the auto-deploy directory



Usage
-----
TK

Testing
-------

in the Product/SoapUI_Test/ValidationSuite/ directory

mvn clean test

this will run the soapui tests against your local connect

Contributing
------------
1. Fork it.
2. Create a branch (`git checkout -b my_feature`)
3. Commit your changes (`git commit -am "Added new feature"`)
4. Push to the branch (`git push origin my_feature`)
5. Open a [Pull Request][1]

[1]: https://github.com/CONNECT-Solution/CONNECT/pulls
