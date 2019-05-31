## CONNECT 5.3 Release
### Summary
The 5.3 release includes these exciting new features:

* Import chain of trust wizard
* Configurable SHA version specification
* Server Name Indication (SNI) support and proof of concept
* Optimized SAML assertion builder
* CONNECT wiki enhancement

For more information refer to the [Release Notes](https://connectopensource.atlassian.net/wiki/x/BQAgKg).

## CONNECT 5.2 Release
### Summary
The 5.2 release includes these exciting new features:
* Certificate expiration alerts and chain of trust view
* Exchange Manager internal endpoint editor and improved organizational view
* Document Data Submission and Patient Location Query web services for proposed use cases
* Failure logging to investigate transaction failures without diving into server logs
* Exchange-compliant Cross Query Gateway Client

For more information refer to the [Release Notes](https://connectopensource.atlassian.net/wiki/x/HABrJw).

## CONNECT 5.1.2 Release
### Summary

The 5.1.2 release includes these exciting new features and a bug fix for the adapter spring injection issued discovered in CONNECT 5.1:

* Exchange Manager for downloading organizational data and endpoints from both UDDI and FHIR directories
* Certificate Manager for adding, viewing and deleting certificates within a trust store
* Test Data Loader for simplified data creation and management using the CONNECT reference implementation
* Multiple exchange community targeting - send requests to non-default exchange endpoints
* Organizational endpoint override - replace existing endpoints with user-specified URLs
* Document Tab – CreationDateTime is optional for OnDemand documents.
* Filtering Document Query response by serviceStartTime and serviceStopTime, only if they are know to the responding system.

SAML assertions processing has been updated and CONNECT can now be deployed without defining local data sources. Minor additions include updates to ACP/IACP construction in support of the Carequality patient consent workflow, the addition of HTTP Strict Transport Security (HSTS) headers and the inclusion of the unchained certificate XML signatures hot fix. A convenient tool to convert existing uddiConectionInfo.xml and internalConnectionInfo.xml files into the new exchangeInfo.xml and internalExchangeInfo.xml files for use with the new Exchange Manager has also been provided.
For more information refer to the [Release Notes](https://connectopensource.atlassian.net/wiki/x/GoCgDg).

## CONNECT 5.0 Release
### Summary

The 5.0 release includes a major web stack upgrade (CXF from 2.7.3 to 3.1.9), adding customizable HTTP headers to outgoing NwHIN requests, adding a time stamp to the eHealth Exchange UDDI, an audit.properties file editor, configurable TLS versions for UDDI updates, and code updates to allow CONNECT to be compiled with JDK 1.8. Support for CONNECT has been extended to JBoss EAP 7 with JBoss AS 7.1.1 no longer being officially supported by the FHA CONNECT team.
For more information refer to the [Release Notes](https://connectopensource.atlassian.net/wiki/x/ll4BBw).

## CONNECT 4.7 Release
### Summary

Release 4.7 continues to build on the functionality and architecture introduced in Release 4.0 through additional features, selected improvements, and bug fixes. This release includes upgrading hibernate from 3.2.5 to the 5.1.0, addressing event logging bugs, extending improved software security to the System Administration Module, migrating the automated test environment application server to WildFly, and providing WildFly support for FIPS compliance.

We encourage the CONNECT community to upgrade to Release 4.7 to take advantage of these updates. Complete CONNECT 4.7 details can be found on the [community wiki](https://connectopensource.atlassian.net/wiki/x/BIBcB).

## CONNECT 4.6 Release

### Summary

Release 4.6 continues to build on the functionality and architecture introduced in Release 4.0 through additional features, selected improvements, and bug fixes.  This release includes enhancements to audit logging along with compliance to the NwHIN/ATNA audit message requirements for Patient Discovery (PD), Query for Documents (QD), Retrieve Documents (RD), Document Submission (DS) and SOAP/X12 services. These audit events can be searched and viewed through the System Administration Module to help with monitoring and troubleshooting transactions.

With Release 4.6, CONNECT is now an eHealth Exchange Validated Product. This recognition should give the CONNECT community a very high confidence in CONNECT in that it is specification compliant, interoperable and has been successfully validated for the eHealth Exchange. 

CONNECT 4.6 had performance testing done on all supported application servers which include WebSphere, WebLogic, Jboss and WildFly.  This was the first time performance tests were executed since CONNECT 4.0, and the first time the X12 service was performance tested.  For a little more about our performance testing, please see the next section below.

### Enhancements

**CONNECT is now an eHealth Exchange Validated Product!**: The eHealth Exchange completed the review of CONNECT under its Product Testing program in December 2015. CONNECT passed all product testing requirements and is now officially recognized as a validated system under the eHealth Exchange. As an eHealth Exchange Validated Product, CONNECT is now listed on [Sequoia Project's eHealth Exchange website](http://sequoiaproject.org/ehealth-exchange/testing-overview/ehealth-exchange-validated-products/). 

This recognition should give the CONNECT community including federal agencies as well as potential new adopters a very high confidence in CONNECT in that it is specification compliant, interoperable and has been successfully validated for the eHealth Exchange. We would like to encourage new adopters in the community, or trading partners of federal agencies who are in the market looking for a cost-effective gateway solution for secure health data exchange to adopt CONNECT. 

**Enhanced Audit Logging**: Auditing per Integrating the Healthcare Enterprise's (IHE) Audit Trail and Node Authentication (ATNA) profile is prescribed by the Nationwide Health Information Network (NwHIN) specifications for all synchronous services such as PD, QD,  RD, DS, SOAP/X12 and Deferred services. With 4.6, audit logging has been enhanced to include ATNA specification compliant audit entries while considering CONNECT's performance requirements and deployment flexibility. The Sequoia Project has also published audit checklists for the PD, QD and RD services that are being validated for eHealth Exchange participants. This feature will support adopters to onboard successfully to the eHealth Exchange by conforming to these checklists and specifications.

With this enhancement, CONNECT's audit entries are logged once at the gateway for each service (PD, QD, RD, DS and X12) at the requestor or the responder for both synchronous and deferred services. Auditing will remain a service, which means that the functionality can be enabled or triggered from custom adapters if needed. There were many important design considerations with the development of this feature – most importantly implementing an asynchronous model for audit service calls to ensure the gateway continues to meet performance throughput numbers. The extensible design allows for a component workflow that enables adopters looking to plug-in this capability to other downstream systems or tools. The design is also flexible in that audit entries can be configured for persisting to a database or a log file offering multiple options for logging. This design can be utilized to support other audit event types in the future like Security events or authentication failures related to certificates etc. 

The Product team collaborated with the eHealth Exchange Testing team workgroup while reviewing the Audit checklists for the PD, QD, and RD services. Feedback was provided back to the testing workgroup for checklist items that required updates.  

**System Administration Module / Audit Viewer**: The System Administration Module continues to evolve providing an increased graphical overview of an implementer’s deployment providing administrative tools for managing the gateway. The specification compliant audit events are made viewable and searchable through the System Administration Module to help with monitoring and troubleshooting transactions.

**Performance Testing**: Performance tests were last executed at the time of Release 4.0. While there have not been any significant architecture changes post 4.0, there have been some design updates that have been made in the minor releases that necessitated a re-execution of the tests to ensure that CONNECT continues to meet the performance requirements. CONNECT was performance tested on all supported application servers like WebSphere, WebLogic, Jboss and WildFly and we are happy to announce that message throughput requirements for 1600 messages/min were met and exceeded providing increased traffic support and enabling partner expansion. The X12 service was performance tested for the first time this release.

**Important Bug Fixes**: Prior to 4.5, CONNECT used cxf-bundle in lieu of including multiple, smaller CXF jars. For Release 4.5, in preparation for technology stack upgrades, we moved to an unbundled format.  However, a couple of issues were discovered in some adopter environments, which was attributed to missing libraries.  Those missing CXF libraries were added to 4.6 code base, were packaged within the CONNECT ear in Release 4.6, and updates were made to the pom files as well.

With 4.6, the X12 service code was also refactored to re-architect the X12 services to use the Template Method pattern. This reduced the X12 code to almost half of its original size, increasing message throughput and improving the quality of code by making it easier to maintain.

**Specification Compliance and Conformance update**

NIST Testing:

CONNECT 4.6 was tested and successfully validated utilizing the NIST test cases for SOAP-based transport/XDR and Direct transport testing.

eHealth Exchange Testing:

CONNECT 4.6 was successfully tested against the eHealth Exchange test cases for participant and product certification. Details on the testing can be found at eHealth Exchange/ DIL testing. There are no open CONNECT issues related to participant testing.

**Security Scans findings and security update**: The CONNECT team as part of the release readiness process in 4.6, identified and addressed findings based on security scans performed on the CONNECT gateway code base. Several tools were used including Fortify, OWASP Dependency Checks and FindBugs as part of the scans executed on the 4.6 code.  All Critical, High, Medium, and Cat 1 Low findings were addressed and the team will continue to work with the federal partners to ensure the code quality meets their implementation requirements. Addressing these security findings will ensure adopters deploy a more secure implementation and will assist in meeting their organization's internal security reviews, as they deploy CONNECT in their preferred environments. 

**Release Testing update**: CONNECT 4.6 was install-tested in multiple environments and with multiple operating systems to support the federal partner environments and application servers/configurations used by the community.  As with each release, CONNECT was regression tested as well as integration tested against prior supported versions of CONNECT. 

**Open source Application Server – WildFly**: We encourage the members of the community that are using GlassFish to switch to WildFly as their application server to avail of new features in the future. Please be aware of ‘Oracle/GlassFish sun-setting support’ announcements; more information can be found [here](https://connectopensource.atlassian.net/wiki/display/CONNECTWIKI/Community+Application+Server+Migration). 

Wildfly is now used as the open source application server, replacing Glassfish.  There were several reasons why we moved from GlassFish to WildFly. WildFly is an open source Application server (JBoss Community version) and we are aware of members using CONNECT on JBoss EAP, the enterprise version of WildFly. We had worked on developing instructions for FIPS configurations on JBoss EAP and will be looking at a similar configuration for WildFly in the future. Other reasons for moving to Wildfly include strong community and Red Hat support, clear support path from JBoss WildFly to EAP and ease of installation, configuring, and upgrading.

Going forward beyond 4.6 - Development, installation and new feature testing will be done on WebLogic, WebSphere, JBoss and WildFly only. Product team’s regression testing will be executed on WildFly. *GlassFish will no longer be supported post 4.6*.



## CONNECT 4.5 Release

### Summary

Release 4.5 continues to build on the functionality and architecture introduced in Release 4.0 through further features, selected improvements, and bug fixes, as well as incorporating feedback from the community.  The NwHIN CAQH Core X12 Document Submission service, which was introduced in CONNECT 4.4, has been enhanced to audit X12 transactions utilizing industry standards like ATNA and CAQH CORE X12 specifications. Adopters will have the option of turning auditing off or turning on which enables audit logging to the database. 

The System Administrative Module is created with the goal to help adopters to accelerate their implementation time frames and increase adoption and partner expansion. This module has been enhanced to include the ability to view and configure gateway configuration files (gateway.properties and adapter.properties), allowing managers with limited technical system knowledge to update configuration properties.  Also included is a new test tool that will help administrators to test PD, QD and RD outbound requests from the gateway in pass-through mode. These enhancements provide administrators of the system to do preliminary testing at the gateway to identify connectivity and service issues sooner rather than later.  This testing tool is also part of the enhancement and migration of functionality from the legacy Universal Client into the System Administration Module.

4.5 also introduced a new feature to better manage transactions and exchange traffic with partners.  The entity interface has been updated to include a new parameter to the assertion block (optional element) to control web service timeout for any transaction. This will enable adopters to configure timeouts from the client adapter for individual messages and would allow them to account for larger payloads, slow responses from particular trading partners or for particular messages, and any other connectivity issues that might alter expected connection time for a given gateway to gateway message.  

Release 4.5 also includes bug fixes and other improvements to the product making it more robust and secure. The Product Team worked closely with external testing tools and groups to further assist adopters with a smoother process testing with their exchange partners. Security scans were run on the deployable and packaged source code and all findings of Critical, High and Medium priority issues were addressed. 

Release 4.5 was tested with previous versions of CONNECT and meets backwards compatibility requirements with versions 4.4.1, 4.3.2, 4.2.2.2 and 3.3.1.3.  Release 4.5 has been tested against all test cases for eHealth Exchange automated participant testing. Release 4.5 was also successfully tested against the NIST test environment for the XDR test cases as well as the Direct transport test cases. 

### Enhancements

**X12 Auditing**: With CONNECT 4.5, the X12 service has been enhanced to audit X12 transactions utilizing the specifications like ATNA and CAQH CORE X12 specifications. Payload metadata and ATNA compliant fields are audited for each transaction. Adopters will continue to have the option to turn auditing to the database on or off. Auditing is only supported in pass-through mode for the X12 service.

**Improved gateway administrative support**: The System Administrative Module offers an easy to use monitoring and configuration Graphical User Interface (GUI) for managers and administrators of the CONNECT system. Currently gateway administrators use command line tools to change gateway/configuration parameters and utilize other dashboard viewer applications to monitor gateway health. The goal of this module is to simplify gateway administration and ease adoption/configuration burden.

With 4.5, gateway.properties and adapter.properties for CONNECT can now be viewed and configured through the module, without having to access the property files at the system level. A Cross- Gateway Query Client has been introduced this release to help users with connectivity and services testing.This tool allows administrators to initiate PD, QD and RD outbound requests from the gateway to any targeted remote community to validate and test data exchange with any remote community's gateway. This new tool works only in the pass-through mode currently and will be iterated in future releases to provide greater services testing support. 

Adapter changes:

The entity interface has been updated to include a new parameter to the assertion block (optional element) to control web service timeout for any transaction.

Other enhancements:

* CONNECT now supports the ability to configure FIPS (with store type PKCS11) on JBOSS EAP 6.3.  The CONNECT team worked closely with the implementation team at DOD to test this configuration.
* The legacy Universal Client has been moved to a plugin folder. The legacy Universal Client will still be available only for the GlassFish application server but is packaged separately from the main CONNECT binaries to support any users that wish to continue to use the legacy application. 

**Security Scans findings and security update**: The CONNECT team as part of the release readiness process in 4.5, identified and addressed findings based on security scans performed on the CONNECT gateway code base. Several tools were used including Fortify, OWASP Dependency Checks and FindBugs as part of the scans executed on the 4.5 code.  All Critical, High, Medium, and Cat 1 Low findings were addressed and the team will continue to work with the federal partner to ensure the code quality meets their implementation requirements. Addressing these security findings will ensure adopters deploy a more secure implementation and will assist in meeting their organization's internal security reviews, as they deploy CONNECT in their preferred environments. 

**Specification Compliance and Conformance update**

NIST Testing:

CONNECT 4.5 was tested and successfully validated utilizing the NIST test cases for SOAP-based transport/XDR and Direct transport testing. 

eHealth Exchange Testing:

CONNECT 4.5 was successfully tested against the eHealth Exchange test cases for participant certification.  There are no open CONNECT issues related to participant testing.

**Testing improvements and Release Testing**: Direct service testing was automated this release to include happy path scenarios into the automated regression test suite. With this inclusion, Direct service will be tested as part of the nightly run of regression tests thereby ensuring that any new functionality introduced does not break existing functions and also allows for identifying issues sooner rather than at release testing time. This helps with smoother releases by minimizing any unforeseen risks. 

CONNECT 4.5 was install-tested in multiple environments and with multiple operating systems to support the federal partner environments and application servers/configurations used by the community.  As with each release, CONNECT was regression tested as well as integration tested against prior supported versions of CONNECT.

## CONNECT 4.4 Release

### Summary

As part of CONNECT 4.4, the Direct service has been enhanced to provide/receive delivery notifications and to configure trust bundles to support exchange scalability with an improved workflow. This provides Direct adopters a higher level of assurance that a Direct message has arrived at its destination. Direct email senders are kept informed of failed delivery and may introduce workflow processes along with further processing/steps to handle error cases. With the addition of trust bundle support in CONNECT, adopters that are part of Direct communities will reduce configuration overhead by utilizing and configuring trust bundles instead of configuring anchors in CONNECT. 

CONNECT 4.4 introduces a new Graphical User Interface (GUI) for Administrative functions. The System Administration Module has been developed as a simple and easy to configure application with the goal to help adopters to accelerate their implementation time frames and increase adoption and partner expansion.  

Release 4.4 broadens the supported message transports and use cases that can be used for healthcare data exchange by introducing the NwHIN CAQH Core X12 Document Submission service. This feature allows adopters like CMS and its trading partners and the broader community to now exchange X12 payloads utilizing NwHIN specifications in CONNECT, furthering the return on investment from a single implementation.

Release 4.4 also includes bug fixes and other improvements to the product making it more robust and secure. The Product Team worked closely with external testing tools and groups to further assist adopters with a smoother process testing and with their exchange partners. Security scans were run on the deployable and packaged source code and all findings of Critical, High and Medium priority issues were addressed. 

Release 4.4 was tested with previous versions of CONNECT and meets backwards compatibility requirements with versions 4.3.2, 4.2.2.2 and 3.3.1.3.  Release 4.4 has been tested against all test cases for eHealth Exchange automated participant testing.  Release 4.4 was also tested against the NIST test environment for the XDR test cases as well as the Direct transport test cases. 

### Enhancements

**Enhancements to Direct**: In Release 4.0, CONNECT provided support for the Direct transport, enabling adopters to exchange health data exchange using the Direct transport protocol. In Release 4.4, expanded options are available for adopters communicating with trading partners that support the Direct specifications while also providing them a platform for meeting MU stage 2 requirements related to Direct exchange. CONNECT has leveraged the latest version 3.0.1 of the Direct Reference implementation to incorporate and test various error handling and quality of service scenarios defined in the Implementation Guide for Delivery notifications. Also, Direct project has introduced a concept for Trust Bundles in order to implement scalable trust in the Direct community which can now be configured in CONNECT. These new features are supported by CONNECT on the four supported application servers: JBoss, GlassFish, WebSphere and WebLogic. Adopters utilizing the Direct service can now be kept informed of failures in message delivery and may introduce further processing in their adapters resulting in an improved workflow and high level of assurance for message transmission. The configuration of the trust bundles was integrated into the System Administration Module thereby providing a simple single GUI for managing configuration. The Direct service was also successfully validated by running messages through the NIST validator test cases to ensure the messages were compliant from a Direct transport perspective.

**System Administration Module (Admin GUI)**: This application or module is a new feature introduced in CONNECT 4.4 which offers an easy to use monitoring and configuration Graphical User Interface (GUI) for managers and administrators of the CONNECT system. Currently gateway administrators use command line tools to change gateway/configuration parameters and utilize other dashboard viewer applications to monitor gateway health. The goal of this module is to simplify gateway administration and ease adoption/configuration burden. This module will be delivered in phases with incremental delivery and possible community contributions after initial phase. The first phase delivered in Release 4.4 includes the basic framework and ensures that this framework/stack can be deployed and utilized on all supported application servers. This release includes features for Sign in capability,  Role based access to functions, Gateway dashboard, Page layout framework, and Direct configuration.

With the introduction of the System Administration Module the functionality currently contained in the legacy GUIs such as the Universal Client will be migrated into the System Administration Module.  As part of this migration the legacy GUIs will still be available only for the GlassFish application server but will be packaged separately from the main CONNECT binaries.

**Support for NwHIN CAQH Core X12 Document Submission service**: With the guidance of the Centers for Medicare & Medicaid - Office of Financial Management (CMS/OFM),  the CONNECT team worked to further the capabilities of the CONNECT gateway to broaden the exchange of health information by supporting the NwHIN approved specifications for CAQH Core compliant transactions with ASC X12 submission payload.  With this feature, CONNECT will support the CMS esMD program and broader community to exchange with their trading partners utilizing X12 capabilities and related NwHIN approved specifications for CAQH Core compliant transactions with ASC X12 submission payloads.  This will help to expand CONNECT adopter's possible trading partner base and provide alternative submission mechanisms for the community allowing easier integration with adopter's existing infrastructure that supports X12 transactions. 

**Security Scans findings and security update**: The CONNECT team as part of the release readiness process in 4.4, identified and addressed findings based on security scans performed on the CONNECT gateway codebase. Several tools were used including Fortify and FindBugs as part of the scans executed on the 4.4 code.  All Critical, High, Medium and Cat 1 Low issues/findings were addressed. Addressing these security findings will ensure adopters deploy a more secure implementation and assist in meeting their organization's internal security reviews as they deploy CONNECT in their preferred environments. 

**Specification Compliance and Conformance update**

NIST Testing:

CONNECT 4.4 was tested utilizing the NIST test cases for SOAP-based transport. While testing with NIST using XDR a discrepancy was observed related to the PurposeOfUse and Role declaration, this discussion is still open and the team will continue to work with the specification factory and the NIST team to further understand and clarify the discrepancy. More details of our testing can be found here. CONNECT also executed the NIST test cases for Direct transport testing and successfully validated that the product is compliant with the Direct Project specifications for Direct transport.

eHealth Exchange Testing:

CONNECT 4.4 was also successfully tested against the eHealth Exchange test cases for participant certification. 

**Release Testing**: CONNECT 4.4 was install-tested in multiple environments and with multiple operating systems to support the federal partner environments and application servers/configurations used by the community. As with each release, CONNECT was regression tested as well as integration tested against prior supported versions of CONNECT.


## CONNECT 4.3 Release

### Summary

In CONNECT 4.3, to support the broader community and their preferred deployment environment, the CONNECT technology stack was changed to support application servers like WebSphere, WebLogic, and JBoss, in addition to the GlassFish application server.

The CONNECT Team has focused on ensuring that CONNECT 4.3 and future releases deliver with a high level of production readiness. Therefore, enhancements for CONNECT have focused on ensuring security requirements are met, as well as meeting the requirements for reduced deployment timelines and the ability to meet industry certifications. In support of these goals, testing enhancements and automation were a priority during Release 4.3. The CONNECT team implemented a solid testing infrastructure and process to support product validation. 

The testing enhancements occurred in several key areas of the development phase and Q&A process. The increased automation and testing enhancements made in Release 4.3 ensure adopters of CONNECT can expect and receive a thoroughly tested and secure gateway that is ready for any internal validation and review steps on the path to production, as well as provide confidence that their implementation will be ready for industry, partner, and exchange testing.

### Enhancements

**Enhancements to Transaction Logging**: Transaction logging was introduced in 4.0. With 4.2.1/4.3, this feature was enhanced to include a database-less implementation. This enhancement supports adopters that cannot have databases at the application/gateway tier. CONNECT now supports a Noop (no transaction logging) or an In-memory Cache implementation in addition to the existing Database implementation for transaction logging. Details can be found at Database-less Transaction Logging.

**Secured Entity Interfaces for Query for Documents and Retrieve Document Services**: CONNECT 4.3 fixes a known issue in secured entity interfaces for Query for Documents and Retrieve Document services that were throwing null pointer exceptions. These interfaces/WSDLs have been fixed, and adopters can use these secured interfaces.

**Build and Testing Automation and Infrastructure Improvements**: Improvements have been made to the testing process and test suites to enable an automated approach to testing.  

The Validation suite test cases were extended to cover more areas of the code base touched by CONNECT’s support of additional use cases as well as the latest features. The extended Validation suite is part of Continuous Integration (CI). These validation tests ensure that all services (PD, QD, RD, DS, and AD) work in both request and response mode using happy-path smoke tests. The CI process was further improved by the inclusion of Find Bugs (a security and vulnerability identifier program). The CI process runs against every pull request of updated code that each developer submits. If validation tests fail, the code is not automatically merged into the product, and the team is alerted. Then, when the pull request is merged into the code base, CI will run again against all four supported application servers (WebSphere, WebLogic, Glassfish, and JBoss).

One of the most significant improvements was made to CONNECT Regression testing. The Regression suite's code coverage and rigor has continued to increase over the past couple of releases, and during Release 4.3 there was considerable progress made. Code coverage was increased an additional 24%, completing overall code coverage from a functional and conditional perspective. Also the rigor of the test cases themselves was increased, improving specification compliance and aligning our testing with industry certification test cases, such as the eHealth Exchange and the NIST Meaningful Use testing. New logic was instituted to enable reconfiguration of the CONNECT gateway to test in both standard and pass through modes without restarting the gateway.  

Other areas of product testing have been enhanced and improved as well. The CONNECT Team was able to procure the HP Fortify software, which allows the CONNECT team to identify, triage, and resolve security vulnerabilities identified by static and dynamic analyzers. In addition to the Find Bugs program incorporated in the CI process, Fortify will ensure that when CONNECT is released, it will meet all security requirements to run in federal agency and private environments. During Release 4.3, the CONNECT team has continued to work diligently to ensure passing against the eHealth Exchange and MU2 test scenarios. Updates and improvements were also made in the CONNECT interoperability environment to better reflect the industry production environment and bring increased efficiency and reduced timelines when conducting interoperability testing.

**Fortify Scans and Testing Tools**: The CONNECT team addressed a community-supplied Fortify scan as well as internal Fortify scans as part of the development process in 4.3 to expedite identification and correction of the security violations. Fortify scans were executed on the 4.3 code, and all Critical and High issues/violations have been addressed. Details of the violations and associated fixes can be found in Fortify scan documentation. Addressing these security violations will help adopters to meet their organization's internal reviews as they deploy CONNECT in their preferred environments. With the software purchase and incorporation of security scans utilizing the Fortify application, CONNECT will meet all application security requirements prior to being released to the community.

**Specification Compliance and Conformance**: CONNECT continues to be compliant with NIST test cases for SOAP-based transport. Details on the related tasks and bugs identified can be found in JIRA epic.  During the course of this testing, there were a few issues discovered and fixed after discussions with the eHealth Exchange Spec factory and the NIST team. These fixes include:
* Modifying xsi:type values to be CE instead of hl7:CE in the PurposeOfUse and Role attributes and including the appropriate namespace xsi
* Fixing the WS-Addressing Header <Action> and <ReplyTo> to set the "mustUnderstand" Attribute in the initiating message
* Accepting an empty URI reference ("") for the Resource attribute of the Authorization Decision Statement
* Prepending an '_' to assertion id in SAML Authorization Decision Statement

CONNECT 4.3 was also successfully validated against the eHealth Exchange test cases for participant certification. Details on the testing can be found at eHealth Exchange/ DIL testing.

**Adapter Improvements**: Query for Documents and Retrieve Documents adapters were fixed to retrieve Stable and On Demand documents.

Document Registry and Document Repository adapters were updated to utilize the Apache CXF libraries and were tested with the HIEOS registry and repository software.

Issues related to secured interfaces for Entity Document Query and Document Retrieve have been fixed.

**Auditing Enhancement Design**: A design approach for auditing enhancements was developed, Integrating the Healthcare Enterprise's (IHE) Audit Trail and Node Authentication (ATNA) profile, as prescribed by the Nationwide Health Information Network (NwHIN) specifications for all services such as Patient Discovery, Document Query, Document Retrieve, Document Submission, Administrative Distribution and Health Information Event Messaging. The underlying ATNA IHE profile specifies audit message/record format and audit record transports required for recording any audit event.

**Testing in Multiple Environments**: CONNECT 4.3 was install-tested in multiple environments to support the federal partner environments and the application servers/configurations used by the community. More details on the environments that were tested can be found under Release Testing. Installation testing results can be found here. A new environment for release testing that was included in this release was the WebSphere application server using Liberty Profile. This was included to support the future/planned DoD CONNECT environment. The team was able to successfully test and validate CONNECT 4.3 on WebSphere Liberty Profile 8.5.5.1.


## CONNECT 4.2 Release

### Summary 

Release 4.2 continues to build on the functionality introduced in Release 4.0 through further selected improvements and enhancements along with incorporated feedback from the community.  Release 4.2 provides support for both Java 6 and Java 7 environments allowing adopters greater flexibility as they install CONNECT in their preferred environments. In addition to this, enhancements were made to the multiple specification version support on the initiating and responding side of the gateway. These changes allow adopters the flexibility to target specific specification endpoints by version as well as have greater control on creating the appropriate message responses based on the incoming specification version of the requesting message.

As part of the Java 6 support validation, CONNECT 4.2 was tested on WAS 7 application server.  The WAS 7 environment will no longer be available to the CONNECT team. Future versions of CONNECT will be tested using later versions of the WebSphere application server.

**With Release 4.2, support for Release 4.0 and 4.1 is deprecated.   We encourage all new adopters to use CONNECT 4.2 to avail of the new features, improvements, and resolved issues.**

### Enhancements

**Support for Java 1.6**: Since Release CONNECT 3.3, the CONNECT product has required Java 7; a number of features developed in CONNECT 3.3 and 4.0/4.1 have used Java 7 language constructs, while other features have used Java 7 as a base line for building the project. In order to support the federal partners as they move to CONNECT 4.x in their respective preferred environments (i.e., IBM WebSphere 7 running SDK 6 (CMS-OFM and SSA) or Oracle WebLogic 11g running JDK 1.6 (VA)), the CONNECT product was enhanced to use Java 6 as the base line. Since Java 7 is backwards compatible with Java 6, the CONNECT product will therefore support both Java 6 and 7.

With this feature federal partners can set their CONNECT upgrade timeline and take advantage of 4.X features independently of data center plans for application server upgrades. This will also provide additional implementation options to the community as they use CONNECT in their respective preferred environments.

**Multiple specification version enhancement support**: While CONNECT already supported multiple specification versions(2010 and Summer 2011) for the NwHIN services of Patient Discovery(PD), Query for Documents(QD), Retrieve Documents(RD), Document Submission(DS) and Administrative Distribution(AD), there were enhancements made to support additional use cases required by the partners for QD and RD services. There were two enhancements requested for this release, one related to responding to QD and RD requests and the other related to initiating QD and RD requests.

Adopters using QD and RD services will be able to determine the specification version for the incoming message in their adapter interfaces. This guidance will help them further process or validate the request to determine if it is valid per the specification version as well as enable them to create a version compliant response message. 

With 4.2, Query for Documents service will be implemented using two gateway endpoints and two adapter endpoints. With this enhancement, the inbound message get routed to each adapter based on the specification version (2010 -> a0, Summer 2011 -> a1) to help adopters appropriately process and validate the incoming QD message. With Retrieve for Documents service, in earlier versions there were two separate gateway endpoints and two adapter endpoints possible to support the additional schema changes for On Demand documents. However the design supported the highest API level for adapters that responded to both spec versions. While this minimized the impact to having multiple adapter interfaces, this did not provide information about the spec version to the adapter to determine whether to validate the incoming message against the 2010 or Summer 2010 specifications. With this enhancement the inbound message get routed to each adapter based on the specification version (2010 -> a0, Summer 2011 -> a1) to help adopters appropriately process and validate the incoming RD message.

Adopters will be able to instruct the gateway which specific versions of endpoint they wish to target. This provides adopters greater flexibility by providing them the option to target endpoints based on their use cases, deployment models, and trading partners capabilities.  With 4.2, the adopters can instruct the gateway what specification version for the QD and RD message they wish to target and the gateway will use that guidance to target the appropriate endpoint or return appropriate error messages to the adapter. 

**Enhancements made to pass Healtheway/ CCHIT Participant certification testing**: The CONNECT team continued to participate in testing the CCHIT test scripts for the eHealth Exchange Participant Testing and HIE Certified Network Programs as it evolved from the initial pilot to the release of the actual production tests.  As part of test execution, the team discovered a few issues related to the security related test cases that have been fixed in 4.2.

CONNECT 4.2 has passed all the Healtheway participant certification test cases (published 7/31/13 the participant certification testing production date, by CCHIT), these include both smoke and certification tests.

**Adapter improvements**: Some updates were made to the reference adapters in CONNECT to handle scenarios discovered during pilot testing for CCHIT´s HIE Certified Network pilot. These include

* PD adapter was updated to include country code in PD response if present in the MPI file.
* QD adapter was updated to filter documents correctly based on event codes in the query request i.e. adapter was enhanced to recognize multiple values in the slot as well as filter the documents based on the event codes.

**Code refinements, build improvements and testing coverage with automation**: In order to improve our QA process and uncover issues earlier in the development process as opposed to later at release testing time, the CI process was enhanced to include WebLogic and WebSphere Application Server. With this enhancement the CI process automatically builds and tests on WebSphere and WebLogic in addition to GlassFish. This is done at code check in time and the code is validated against all 3 application servers. Issues can be identified earlier and helps minimize risks. 

Furthering the ability to deliver more stable product with increased code quality the CONNECT team has continued to refine release testing.  Improvements were made in this release in validation, regression and interoperability test suites.  In the validation suite, increased code coverage with more targeted specificity in key functional areas has been accomplished.  A great deal of refinement work was accomplished this release on the regression suite.  Reorganization improvements were made to allow for more straightforward and ease of execution.  Test cases were added and some were refined to better validate CONNECT functionality and mirror some of the certification testing CONNECT adopters will be tested against.  Interoperability testing was reconfigured with improvements of set up to allow for a more streamlined execution that allows the CONNECT team to run interoperability testing more often and with increased confidence.  

**Bug fixes**: The CONNECT team also fixed some important bugs that were discovered during the course of Healtheway certification testing.
* Particularly important is the issue where MTOM service decorator was being set for all service requests and hence non-compliant messages were being created for PD, QD and RD. This was fixed both at the gateway level as well as at the adapter level. 
* Also a bug discovered was related to the Web Service Addressing TO element not being populated for NwHIN messages was fixed.

**Implementation Notes**

* From an interfaces perspective, Query for Documents implementation will now have both a 3.0 Gateway version as well as an a1 Adapter API Level.
* Entity interfaces for QD and RD services have a new field called 'useSpecVersion' to provide guidance to the gateway about the message specification version.
* Implementers using secured adapter interfaces are required to use a0 adapters for receiving 2010 QD and RD requests and a1 interfaces for receiving 2011 QD and RD requests to help validate incoming messages against 2010 and 2011 spec versions.
* Unsecured adapter interfaces for QD and RD services have a new field in the assertion called 'implementsSpecVersion' that provides message specification version information to the adapter interfaces.
* Implementations should expect new error messages at the entity interface ' No matching target endpoint for guidance: $useSpecVersion' for QD and RD requests if guidance provided does not match with the version supported by the targeted endpoints
* Additionally a new error message at the entity interface 'Unsupported guidance for API level' for RD requests will be returned if the guidance provided in the 'useSpecVersion' is not supported by the API level.
* No changes have been made to the entity and adapter interfaces for other services like PD, DS and AD.


## CONNECT 4.1 Release

### Summary

Release 4.1 continues to build on the functionality introduced in Release 4.0 through further selected improvements and enhancements along with incorporated feedback from the community.  Release 4.1 provides the ability to deploy CONNECT to an additional application server, includes code improvements that increase code coverage by the CONNECT team's automated testing, and implements resolutions for issues discovered while working with the community over the last several weeks.  

With Release 4.1, support for Release 4.0 is deprecated. We encourage all new adopters to use CONNECT 4.1 to avail of the new features, improvements, and resolved issues

### Enhancements

**CONNECT deployment with JBoss 7.1.1**: In addition to GlassFish, WebLogic, and WebSphere, CONNECT is now able to be deployed with JBoss 7.1.1, providing an additional application server for adopters seeking to leverage open source solutions. JBoss is one of the industry's leading open source application servers and provides the ability to deploy a FIPS-compliant implementation.  Providing support for another application server further validates the architectural enhancements introduced in Release 4.0 and allows the CONNECT community significant options in deploying CONNECT into their select environments. This provides a roadmap for additional application server support for where the community may wish to deploy CONNECT.

**Further code refinements, build improvements, and automated testing coverage**: The CONNECT team focused resources in the earlier sprints of Release 4.1 on the continuing improvement of the CONNECT code base and test case coverage.  By focusing on the code libraries, the CONNECT team was able to further optimize for increased modularity, contributing to more efficient testing and streamlined build logic. For Release 4.1 the build process improved from 25 minutes to 10 minutes due to these improvements.  


## CONNECT 4.0 Release

**Increased gateway throughput, targeting increased numbers of Patient Discovery, Query for Documents, Retrieve Documents, Administrative Distribution, and Document Submission supported transactions**
To support more widespread health data exchange, national programs roll out plans, and support meeting Meaningful Use 2 CONNECT 4.0 has become more efficient with increased ability to process significantly larger transaction volumes for production services.  The architectural modifications and refactoring of code during work on major features, maintenance, bug fixes, and targeted technical debt helped achieve these new levels of performance.  CONNECT 4.0 also allows for better utilization of the adopter infrastructure contributing to better efficiency and throughput.

**Exchange and process large payload sizes of up to 1 GB while meeting throughput requirements**
CONNECT 4.0 supports larger transaction sizes from current levels to over one gigabyte.  This increased capability will increase the ability to incorporate expanded exchange needs of additional adopters and use cases where larger data exchanges are required.  This functionality has been architected and developed to meet throughput requirements and maintain a positive impact on gateway performance when handling larger payloads and transactions.

**Ability to deploy on additional application servers such as WebSphere and WebLogic to meet individual environment needs**
Run CONNECT 4.0 on additional application servers such as WebSphere and WebLogic to meet unique IT environment needs and redesigning CONNECT to be deployable with other application servers expanding deployment options.  This enhancement also provides a model for the community to add support for additional application servers.  This allows adopters to use their preferred app servers and take advantage of internal sysadmin expertise for things like security, scalability, etc.

CONNECT achieved the ability to work with alternative application servers by removing its dependency on Metro web service stack. CONNECT previously was tightly integrated with Metro, as part of this feature CONNECT was decoupled from Metro.  With the design approach taken CONNECT developed clean components not limited to which web service stack implementation can be used with CONNECT. However, for Release 4.0 CONNECT will be implemented using Apache CXF for its web service stack but the messaging component is open for extension.  CXF was selected due to several key benefits it provides which can be seen by following the links below.

**Capture and utilize more comprehensive event logging and metric data with enhanced logging capabilities**
CONNECT adopters shall have the ability to get more comprehensive event logging and metric data (counts and duration) using improved logging in CONNECT on deployed services.  Allowing adopters to better understand usage and deployment performance as well as provide insight into exchange partner performance. This feature will support better planning, utilization, and management of CONNECT deployment.  It will also provide an opportunity to use external automated monitoring tools. Usage information can be utilized for any dynamic scaling based on load.

**Transaction Logging across messages**
This feature will determine the state of a transaction across messages to better troubleshoot and analyze the operations of deployment, gateway, and exchange partner gateways.  This feature provides a more holistic view of a complete transaction with any given exchange partner.  Each implementer will have increased insight for troubleshooting and issue resolution.  This feature will also provide more input for transaction management and planning.

**Ability to Support the Direct Project Messages**
This feature will support Direct messages to allow for flexible adoption paths, to increase the number of potential exchange partners, and to deploy part of the ONC 2014 Standards & Certification Criteria (S&CC). Adopters are provided functionality to support both eHealth Exchange and the Direct Project transactions through one deployment. The incorporation of this feature in CONNECT offers a flexible built-in growth or migration path for additional use cases while supporting expanding federal, commercial, and state and regional HIE needs. 

**Support for some Meaningful Use 2 Objectives**
Core Meaningful Use 2 objectives related to the secure electronic exchange of health information are supported through CONNECT 4.0 functionality of the Direct Project specifications and NwHIN exchange specifications covering two transports for MU2 (i.e. electronically sending and receiving patient data, transition of care, registering immunization information, public health reporting, and patient access to data).

**Build Refinements and Introduction of Maven**
One key component of CONNECT 4.0 was the continued refactoring and modularization of the code base and how these refinements would contribute to improvements in the build process to better support the CONNECT community.  These refinements have allowed for more isolation of what is built in the JAR. This makes the build process faster and more efficient and allows for quicker identifications for build and code issues. The new approach will be more IDE agnostic and easier to use with everyone's development environments.

By introducing this new build system based on dependency management (Maven), this supports convention over configuration with less things to configure and provides consistency for the community with uniformity in its layout.  With Maven there is a whole ecosystem of plugins, static analysis for code reviews, code coverage and sub-modules, and tools for dependency checks.  Maven also provides robust tools for reporting on builds.

**Integration and Validation Testing Enhancements**
CONNECT 4.0 also contains significant testing enhancements.  There is increased automation and in code coverage of unit tests; these improvements are due to the greater modularity and breaking out of tests for these modular components. In order to maintain consistency with how CONNECT implements tests, the set-up is the same, which removes machine to machine inconsistency.  Embedded GlassFish in the testing process ensures everyone uses the same environment for testing.  An ability was also added to execute tests by service for more targeted testing capabilities as well as a new, more logical organization in using Message Proxy versus Entity. 



## CONNECT 3.3 Release

* New features
* Performance improvements
* Maintenance fixes
* Software updates
* To learn more about the CONNECT 3.3 release and download the software, click here.

Key enhancements included in CONNECT 3.3 are:

**Complying with July 2011 Approved Nationwide Health Information Network Specifications**: CONNECT 3.3 supports the July 2011 specifications for patient discovery, query for documents, retrieve documents, document submission and administrative distribution as well as the foundational specifications for messaging, web services registry and authorization framework. CONNECT 3.3 is the first gateway to comply with these revised specifications and it allows organizations participating in the Nationwide Health Information Network Exchange to ensure their gateways are in compliance.

**Creating Backwards Compatibility between the January 2010 and July 2011 Nationwide Health Information Network Specifications and among CONNECT Versions**: Previously, if an organization were to implement the July 2011 specifications, it would not have been able to interoperate with other organizations using the January 2010 specifications. However, CONNECT 3.3 supports both the January 2010 and July 2011 versions of the specifications using identification capabilities of the Nationwide Health Information Network Universal Description, Discovery and Integration (UDDI) service. In addition, CONNECT 3.3 was built to ensure gateway-to-gateway and gateway-to-adapter backwards capability, which enables organizations to more readily upgrade CONNECT while minimizing changes to already existing adapters and interfaces.

**Incorporating Technology Stack Upgrades**: To implement the July 2011 specifications and to support SAML 2.0, CONNECT 3.3 migrated to newer versions of key technology stack components: Metro to version 2.1.1 and JDK to version 1.7.

**Implementing a Configurable Set of Services for Targeted Selection during Installation**: During deployment, CONNECT 3.3 allows implementers to select message type, supported specification version, and total number of services to be deployed. By making it easier to customize the installation process with selectable services, organizations will not have to install services they will not use. This allows the end user to tailor the software to their environment versus having to tailor their environment to the software.

**Providing Parallel Message Initiation and Processing (Fan Out)**: CONNECT 3.3 provides the ability to fan out patient discovery and query for documents requests to multiple communities, thereby reducing the overall processing duration for multi-community requests.

**Preparing CONNECT for a Clustered Environment**: To handle higher traffic volumes, CONNECT 3.3 supports multiple instances of the gateway in a single implementation.

**Providing Performance-Related Configurable Parameters**: CONNECT 3.3 includes configuration guidance to help you ensure your implementation achieves optimal throughput and resource utilization.

**Enhancing Implementation Instructions**: CONNECT 3.3 provides refined instructions for installing and deploying CONNECT on all supported platforms.

# Release 3.3 New Features

Listed below are the highlights of the many changes and improvements to the CONNECT gateway in this most recent release, 3.3. These sections are brief descriptions from which links are provided to detailed explanatory documents.


### Compliance with the Approved Summer 2011 Nationwide Health Information Network Specifications

The Coordinating Committee of the NwHIN Exchange approved the revisions to certain production specifications and finalized previous emergent and pilot production specifications on June 29, 2011. CONNECT 3.3 supports the requirements specified in the newly approved versions. A complete analysis of the changes between the previously approved specifications (January 2010) and the newly approved versions (Summer 2011) is available.



### Single Instance Support for Multiple Versions of the Nationwide Health Information Network Specifications&nbsp;



To ensure interoperability with other gateways, CONNECT 3.3 supports both the current production version of the NwHIN specifications (January 2010) and the newly approved specification (Summer 2011) in a single instance of the CONNECT gateway. The NwHIN Exchange's migration path to the new Summer 2011 version of the specifications is still undetermined, but the understanding is that not all organizations will be on the same upgrade and implementation path. The need will exist to exchange data with groups using different versions of the NwHIN specifications. CONNECT provisions a set of web services endpoints for each specification set - January 2010 and Summer 2011. This UDDI-based NwHIN Specifications Backwards Compatibility allows CONNECT to read the UDDI to obtain the correct endpoint for each version of the specification.



### CONNECT Version Backward Compatibility; Gateway to Gateway and Gateway to Adapter

In addition to supporting multiple versions of the NwHIN specifications, CONNECT 3.3 is backward-compatible with older versions of CONNECT, and with the adapter interfaces designed for the older versions of CONNECT. This is to reduce the burden and investment for current CONNECT users to upgrade to CONNECT 3.3. In this way, CONNECT adopters can access the benefits of the product's improvements and support for new specifications, while requiring no changes to their existing adapters. In addition to gateway-to-adapter backwards compatibility, extensive testing was performed between CONNECT 3.3 and older versions of CONNECT, including 3.2, 3.1, 2.4.8, and 2.4.7, to ensure backwards compatibility at the gateway layer.




### Configurable Set of Services at Installation

This release of CONNECT implements the capability to select the desired functional NwHIN services in multiple configurations when installing CONNECT. The user can select one or more or all of the services as desired. In prior releases all services were build and deployed together, which forced dependencies on services and utilizing system resources that the implementer did not need. This feature will allow for very flexible deployments - for example, a gateway could be configured and deployed with only a January 2010 and Summer 2011 version of Document Submission, or focused where a particular use case might be supported by only the Summer 2011 Patient Discovery, Query for Documents and Retrieve Documents, or be configured as two gateways with a full suite of January 2010 specifications and a gateway with a full suite of Summer 2011 specifications.

### Parallel Message Initiation and Processing (Fan out)

The CONNECT 3.3 release implemented a requirement to reduce response time and improve gateway performance by switching the way requests are sent, from sequential requests to an approach based on parallel, concurrent execution, or a "fan-out" approach. Instead of the Patient Discovery (PD) and Query of Documents (QD) requests being made serially or sequentially to each community, CONNECT 3.3 sends requests to multiple communities at the same time. This new implementation should be transparent to existing users of the CONNECT Entity Request Services, other than the improved response times for PD and QD requests. For information:



### Enable CONNECT for Deployment in a Clustered Environment

The CONNECT team enabled and verified that the CONNECT gateway could be deployed in a clustered environment where multiple instances of the gateway working together could support an increased volume of messages. The outcomes from of this effort were:
* Tested all production services in a clustered deployment model to ensure software was cluster ‘capable’ (shared state)
* Identified any existing gaps
* Resolved the identified gaps
* Validated a clustered (load balanced) deployment and presented the results


The clustering of CONNECT gateways will support higher volumes, but additional platform specific configuration will be required for fault tolerance and high availability. 


### CONNECT Technology Stack Upgrade

The technical stack for CONNECT 3.3 was upgraded to support the Nationwide Health Information Network Exchange Summer 2011 specifications. The foundational specification Authorization Framework v3.0 requires support for SAML 2.0, which drove the tech stack upgrade to:

* **GlassFish 2.1.1** The team considered upgrading to GlassFish 3.1.1, but found difficulties in enabling FIPS compliance with this version of GlassFish. We'll be investigating this further - check the community wiki for updates.
* **Metro 2.1**. This was the required upgrade to support SAML 2.0.&nbsp; Metro 2.1 was successfully tested with both GlassFish 2.1.1 and GlassFish 3.1.1.
* **JDK 1.7 Update 2**. The JDK 1.7 Update 2 includes JAX-WS 2.2 APIs, which correspond to the JAX-WS that Metro 2.1 uses.




### Benchmark Performance Testing of CONNECT

We performed performance testing on CONNECT to provide insight and guidance into the last two releases of CONNECT, versions 3.1 and 3.2.1.&nbsp; There were three goals for the performance testing: to establish the product baseline performance of the CONNECT gateway using primarily out-of-the-box configuration, to establish the optimal baseline using an optimized gateway configuration which will define an upper bound for all product functionality, and to identify and verify any system bottlenecks. The test execution included capacity stress test, consistent load stress test and single function stress test.

### Provide Configurable or Tunable Parameters to Improve Performance 

We investigated options and experimented with adjusting parameters across three areas to help improve CONNECT performance as users experience an increase in volume. We've created recommendations for tuning or configuring parameters that might have an impact in the following areas: operating system, application server, and CONNECT's configuration. We did evaluate hardware tuning options, but felt that there is too much variety in each implementer’s environment to capably make definitive recommendations.




### Technical Debt: Paying it Off

The significant work contained in the features of this release, especially around decoupling the gateway to support selectable services, highlighted the need to address some of the legacy items that are no longer or were never supported by the NwHIN specifications, lost original vendor support or were never adopted by the user community. **We invested now to address this technical debt rather than to keep expending resources on development and testing to conform and update these items to work with new features such as selectable services.** This effort saves us future expense and reduces our future risk related to these legacy items. **The items approved and addressed in this release were**

* MurialMPI, which is no longer supported by Sun/Oracle and doesn’t comply with Multiple Assigning Authority capabilities.
* LiFT, which was never adopted and violates security rules per NwHIN Authorization Framework.
* Subject Discovery, which was deprecated in favor of the now support Patient Discovery.
* Audit Log Query, which was not released as a NwHIN specification since other NwHIN functional specifications address audit and logging requirements.
* Deferred query for documents and deferred retrieve documents, which are not supported by the NwHIN specifications and are not recognized by IHE as complying with asynchronous requirements.
* Removal of code found that supported a demo trying to provide and register a retrieved document into a local repository.


## Resolved JIRA Tickets&nbsp;

The following issues were resolved during this release. The details of these issues can be found in the [CONNECT Issue Tracker](https://issues.connectopensource.org:8443/secure/IssueNavigator.jsp).

# Additional Release Updates

The following are notifications and updates related to the release.

### Reduced Support of JBoss

Red Hat no longer produces/updates JBoss-Metro. (JBoss-Metro is the customization of Metro that can be installed on JBoss). In order to support NwHIN Specification Compliance including SAML 2.0, we upgraded Metro to 2.1. Without Metro 2.1 support, JBoss is not able to support the Summer 2011 version of the specifications, and this impacts our ability to run CONNECT 3.3 on this platform, since CONNECT 3.3 implements the Summer 2011 specs that rely on functionality in Metro 2.1 to support SAML 2.0. CONNECT adopters who use JBoss in their stacks should be aware that CONNECT 3.3 will no longer run on JBoss. We encourage those who are committed to JBoss to contribute workaround ideas and solutions.&nbsp;

### Support of mustUnderstand Variable in SOAP 1.2

CONNECT 3.3 supports SOAP 1.2. The mustUnderstand attribute values for Action and Reply to elements are now set to 'true' in the SOAP header in outbound messages. (SOAP 1.2 says that 'true' or '1' can be used, and 'true' is preferred.)

On the receiving side, Metro takes care of the mustUnderstand interpretation and CONNECT handles both values of '1' or 'true'.


### Additional SAML 2.0 Subject Confirmation Methods

From the Summer 2011 NwHIN Authorization Framework specification, in addition to a single required holder-of-key subject confirmation method, other SAML 2.0 subject confirmation methods MAY be used.

The NwHIN Initiating Gateway MAY supply more than one subject confirmation method, and those methods MAY include additional holder-of-key subject confirmation methods, and they MAY include one or more "sender-vouches" subject confirmation methods, and they MAY include one or more "bearer" subject confirmation methods. The use of more than a single holder-of-key subject confirmation method is not defined in this specification, but should be defined by higher-level profiles, specifications, or private business agreements between the NwHIN Initiating Gateway and the NwHIN Responding Gateway.

CONNECT will ignore non-essential subject confirmation methods in the SAML 2.0 assertion is acceptable until a use case/profile requires multiple subject confirmations.

### Support Using Different Certificates for 2-way SSL and Message Level Security

From the Summer 2011 NwHIN Authorization Framework specification SAML 2.0 allows for support to use different certificates for 2-way SSL and message level security. &nbsp;The use of different certificates is not defined in this specification and should be defined by higher-level profiles. CONNECT will not support the use of different certificates for 2-way SSL and message level security until a use case/profile requires multiple subject confirmations.


### Responding to Asynchronous Messages

Currently CONNECT doesn’t support asynchronous messaging. The NwHIN Specifications Query for Documents and Retrieve Documents refer to the need to support asynchronous transactions; however, the current versions of these specifications do not provide detailed direction required to implement these capabilities. The guidance we received from the NwHIN Specification Factory is that at a minimum, CONNECT needs to gracefully respond to these transactions. As a result, CONNECT 3.3 will reply with an unknown sender error message in response to an asynchronous message.



