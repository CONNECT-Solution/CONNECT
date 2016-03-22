## CONNECT 4.6 Release

**CONNECT is now an eHealth Exchange Validated Product!**: The eHealth Exchange completed the review of CONNECT under its Product Testing program in December 2015. CONNECT passed all product testing requirements and is now officially recognized as a validated system under the eHealth Exchange. As an eHealth Exchange Validated Product, CONNECT is now listed on [Sequoia Project's eHealth Exchange website](http://sequoiaproject.org/ehealth-exchange/testing-overview/ehealth-exchange-validated-products/). 

This recognition should give the CONNECT community including federal agencies as well as potential new adopters a very high confidence in CONNECT in that it is specification compliant, interoperable and has been successfully validated for the eHealth Exchange. We would like to encourage new adopters in the community, or trading partners of federal agencies who are in the market looking for a cost-effective gateway solution for secure health data exchange to adopt CONNECT. 

**Enhanced Audit Logging**: Auditing per Integrating the Healthcare Enterprise's (IHE) Audit Trail and Node Authentication (ATNA) profile is prescribed by the Nationwide Health Information Network (NwHIN) specifications for all synchronous services such as PD, QD,  RD, DS, SOAP/X12 and Deferred services. With 4.6, audit logging has been enhanced to include ATNA specification compliant audit entries while considering CONNECT's performance requirements and deployment flexibility. The Sequoia Project has also published audit checklists for the PD, QD and RD services that are being validated for eHealth Exchange participants. This feature will support adopters to onboard successfully to the eHealth Exchange by conforming to these checklists and specifications.

With this enhancement, CONNECT's audit entries are logged once at the gateway for each service (PD, QD, RD, DS and X12) at the requestor or the responder for both synchronous and deferred services. Auditing will remain a service, which means that the functionality can be enabled or triggered from custom adapters if needed. There were many important design considerations with the development of this feature – most importantly implementing an asynchronous model for audit service calls to ensure the gateway continues to meet performance throughput numbers. The extensible design allows for a component workflow that enables adopters looking to plug-in this capability to other downstream systems or tools. The design is also flexible in that audit entries can be configured for persisting to a database or a log file offering multiple options for logging. This design can be utilized to support other audit event types in the future like Security events or authentication failures related to certificates etc. See design link above for details and sample audit messages. 

As part of the feature analysis effort, the Product team collaborated with the eHealth Exchange Testing team workgroup while reviewing the Audit checklists for the PD, QD, and RD services. Feedback was provided back to the testing workgroup for checklist items that required updates.  

Note - This feature did not include the transport of these entries via Syslog and other compliant methods to a central or external repository. While this release addressed the happy path overall audit design for logging across all services at the gateway, ATNA also defines an Event Outcome Indicator that could be used to track failure outcome e.g. when there is an SSL failure or certificate error or other internal gateway errors that prevent a particular transaction execution. This would require some technical investigation because in some scenarios, underlying CONNECT components such as the application server's HTTP stack and CONNECT's CXF web service stack could be handling these scenarios without notifying CONNECT. These were not scoped into the release but would need to be addressed in future releases.

**System Administration Module / Audit Viewer**: The System Administration Module continues to evolve providing an increased graphical overview of an implementer’s deployment providing administrative tools for managing the gateway. The specification compliant audit events are made viewable and searchable through the System Administration Module to help with monitoring and troubleshooting transactions. For more information on audit search and view, see the System Administration Module User Manual.

**Performance Testing**: Performance tests were last executed at the time of Release 4.0. While there have not been any significant architecture changes post 4.0, there have been some design updates that have been made in the minor releases that necessitated a re-execution of the tests to ensure that CONNECT continues to meet the performance requirements. CONNECT was performance tested on all supported application servers like WebSphere, WebLogic, Jboss and WildFly and we are happy to announce that message throughput requirements for 1600 messages/min were met and exceeded providing increased traffic support and enabling partner expansion. The X12 service was performance tested for the first time this release.

**Important Bug Fixes – CONN-1632/CONN-1625 and Refactoring**: Prior to 4.5, CONNECT used cxf-bundle in lieu of including multiple, smaller CXF jars. In preparation for technology stack upgrades, including to CXF 3.0 and beyond, we moved to an unbundled format with Release 4.5.

However, a couple of issues were discovered in some adopter environments as discussed in CONN-1632 and CONN-1625 with release 4.5. These were attributed to jar files that was missing. The following CXF jars were added to 4.6 code base and packaged within the CONNECT ear in Release 4.6 and updates were made to the pom files as well.

* cxf-tools-wsdlto-frontend-jaxws
* cxf-tools-common
* cxf-tools-wsdlto-frontend-jaxws

With 4.6, the X12 service code was also refactored to re-architect the X12 services to use the Template Method pattern. This reduced the X12 code to almost half of its original size, increasing message throughput and improving the quality of code by making it easier to maintain.

**Specification Compliance and Conformance update**

NIST Testing:

CONNECT 4.6 was tested and successfully validated utilizing the NIST test cases for SOAP-based transport/XDR and Direct transport testing.

eHealth Exchange Testing:

CONNECT 4.6 was successfully tested against the eHealth Exchange test cases for participant and product certification. Details on the testing can be found at eHealth Exchange/ DIL testing. There are no open CONNECT issues related to participant testing.

**Security Scans findings and security update**: The CONNECT team as part of the release readiness process in 4.6, identified and addressed findings based on security scans performed on the CONNECT gateway code base. Several tools were used including Fortify, OWASP Dependency Checks and FindBugs as part of the scans executed on the 4.6 code.  All Critical, High, Medium, and Cat 1 Low findings were addressed and the team will continue to work with the federal partners to ensure the code quality meets their implementation requirements. Addressing these security findings will ensure adopters deploy a more secure implementation and will assist in meeting their organization's internal security reviews, as they deploy CONNECT in their preferred environments. 

**Release Testing update**: CONNECT 4.6 was install-tested in multiple environments and with multiple operating systems to support the federal partner environments and application servers/configurations used by the community.  As with each release, CONNECT was regression tested as well as integration tested against prior supported versions of CONNECT. 

**Open source Application Server – WildFly**: We encourage the members of the community that are using GlassFish to switch to WildFly as their application server to avail of new features in the future. Please be aware of ‘Oracle/GlassFish sun-setting support’ announcements; more information can be found [here](https://connectopensource.atlassian.net/wiki/display/CONNECTWIKI/Community+Application+Server+Migration). 

From a product development perspective, the team now uses WildFly as the open source application server. There were several reasons why we moved from GlassFish to WildFly. Oracle announced the sun setting of commercial support for future major releases of GlassFish Server. WildFly is an open source Application server (JBoss Community version) and we are aware of members using CONNECT on JBoss EAP, the enterprise version of WildFly. We had worked on developing instructions for FIPS configurations on JBoss EAP and will be looking at a similar configuration for WildFly in the future. In addition, strong community and Red Hat support, clear support path from JBoss WildFly to EAP and ease of installation, configuring, and upgrading are some of the other reasons.

Going forward beyond 4.6 - Development, installation and new feature testing will be done on WebLogic, WebSphere, JBoss and WildFly only. Product team’s regression testing will be executed on WildFly. Regression/automated environment is currently utilizing GlassFish with plans to move to WildFly in the near future. *GlassFish will no longer be supported post 4.6*.



## CONNECT 4.5 Release

**X12 Auditing**: With CONNECT 4.5, the X12 service has been enhanced to audit X12 transactions utilizing the specifications like ATNA and CAQH CORE X12 specifications. Payload metadata and ATNA compliant fields are audited for each transaction. Adopters will continue to have the option to turn auditing to the database on or off. Auditing is only supported in pass-through mode for the X12 service.

**Improved gateway administrative support**: The System Administrative Module offers an easy to use monitoring and configuration Graphical User Interface (GUI) for managers and administrators of the CONNECT system. Currently gateway administrators use command line tools to change gateway/configuration parameters and utilize other dashboard viewer applications to monitor gateway health. The goal of this module is to simplify gateway administration and ease adoption/configuration burden.

With 4.5, gateway.properties and adapter.properties for CONNECT can now be viewed and configured through the module, without having to access the property files at the system level. A Cross- Gateway Query Client has been introduced this release to help users with connectivity and services testing.This tool allows administrators to initiate PD, QD and RD outbound requests from the gateway to any targeted remote community to validate and test data exchange with any remote community's gateway. This new tool works only in the pass-through mode currently and will be iterated in future releases to provide greater services testing support. 

Adapter changes:

The entity interface has been updated to include a new parameter to the assertion block (optional element) to control web service timeout for any transaction.

<urn1:transactionTimeout>300000</urn1:transactionTimeout>

Other enhancements:

* CONNECT now supports the ability to configure FIPS (with store type PKCS11) on JBOSS EAP 6.3. (See CONN-1616/CONN-1618 for details.) The CONNECT team worked closely with the implementation team at DOD to test this configuration.
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

**Enhancements to Direct**: In Release 4.0, CONNECT provided support for the Direct transport, enabling adopters to exchange health data exchange using the Direct transport protocol. In Release 4.4, expanded options are available for adopters communicating with trading partners that support the Direct specifications while also providing them a platform for meeting MU stage 2 requirements related to Direct exchange. CONNECT has leveraged the latest version 3.0.1 of the Direct Reference implementation to incorporate and test various error handling and quality of service scenarios defined in the Implementation Guide for Delivery notifications. Also, Direct project has introduced a concept for Trust Bundles in order to implement scalable trust in the Direct community which can now be configured in CONNECT. These new features, ie.,  the ability to provide and receive delivery service  notifications and configure trust bundles are supported by CONNECT on the four supported application servers: JBoss, GlassFish, WebSphere and WebLogic, thereby providing more options to the Direct community. Adopters utilizing the Direct service can now be kept informed of failures in message delivery and may introduce further processing in their adapters resulting in an improved workflow and high level of assurance for message transmission. The configuration of the trust bundles was integrated into the System Administration Module thereby providing a simple single GUI for managing configuration. The Direct configuration screens provide the ability to configure domains, anchors, certificates as well as trust bundles that are utilized during Direct processing. The Direct service was also successfully validated by running messages through the NIST validator test cases to ensure the messages were compliant from a Direct transport perspective.

We encourage the open source community to utilize the Direct service and provide feedback as well as improvement/suggestions based on how they integrate this service in their environments.

There is a known issue related to supporting multiple recipients, which will be looked at in later releases.

**System Administration Module (Admin GUI)**: This application or module is a new feature introduced in CONNECT 4.4 which offers an easy to use monitoring and configuration Graphical User Interface (GUI) for managers and administrators of the CONNECT system. Currently gateway administrators use command line tools to change gateway/configuration parameters and utilize other dashboard viewer applications to monitor gateway health. The goal of this module is to simplify gateway administration and ease adoption/configuration burden. This module will be delivered in phases with incremental delivery and possible community contributions after initial phase. The first phase delivered in Release 4.4 includes the basic framework and ensures that this framework/stack can be deployed and utilized on all supported application servers. This release includes features for Sign in capability,  Role based access to functions, Gateway dashboard, Page layout framework, and Direct configuration. These functions will be enhanced in future releases of the product to provide more administrative utilities to manage the gateway configuration for various services. 

We encourage the open source community to utilize the application as well as contribute and provide feedback to other features related to reporting and monitoring that they would like to see in CONNECT.

With the introduction of the System Administration Module the functionality currently contained in the legacy GUIs such as the Universal Client will be migrated into the System Administration Module.  As part of this migration the legacy GUIs will still be available only for the GlassFish application server but will be packaged separately from the main CONNECT binaries.

**Support for NwHIN CAQH Core X12 Document Submission service**: With the guidance of the Centers for Medicare & Medicaid - Office of Financial Management (CMS/OFM),  the CONNECT team worked to further the capabilities of the CONNECT gateway to broaden the exchange of health information by supporting the NwHIN approved specifications for CAQH Core compliant transactions with ASC X12 submission payload.  With this feature, CONNECT will support the CMS esMD program and broader community to exchange with their trading partners utilizing X12 capabilities and related NwHIN approved specifications for CAQH Core compliant transactions with ASC X12 submission payloads.  This will help to expand CONNECT adopter's possible trading partner base and provide alternative submission mechanisms for the community allowing easier integration with adopter's existing infrastructure that supports X12 transactions.  CONNECT already implements the NwHIN specifications for Document Submission service which utilizes IHE XDR profile as the payload, currently in production as part of the esMD program. 

Future phases of this feature will include audit logging, and event logging support as well as other modes of configuration to support use case needs of our partners.

**Security Scans findings and security update**: The CONNECT team as part of the release readiness process in 4.4, identified and addressed findings based on security scans performed on the CONNECT gateway codebase. Several tools were used including Fortify and FindBugs as part of the scans executed on the 4.4 code.  All Critical, High, Medium and Cat 1 Low issues/findings were addressed. Addressing these security findings will ensure adopters deploy a more secure implementation and assist in meeting their organization's internal security reviews as they deploy CONNECT in their preferred environments. 

**Specification Compliance and Conformance update**

NIST Testing:

CONNECT 4.4 was tested utilizing the NIST test cases for SOAP-based transport. While testing with NIST using XDR a discrepancy was observed related to the PurposeOfUse and Role declaration, this discussion is still open and the team will continue to work with the specification factory and the NIST team to further understand and clarify the discrepancy. More details of our testing can be found here. CONNECT also executed the NIST test cases for Direct transport testing and successfully validated that the product is compliant with the Direct Project specifications for Direct transport.

eHealth Exchange Testing:

CONNECT 4.4 was also successfully tested against the eHealth Exchange test cases for participant certification. Details on the testing can be found at eHealth Exchange/ DIL testing.

There were a couple of specification compliance bugs from the Healtheway manual checklist that have been addressed this release. Note - While these don't impact interoperability between systems, as of November 2014 these are being validated by Healtheway for compliance with their participant certification.

* CONN-1310 - CONNECT is removing SemanticsText's value from the edge/entity in PD request to NwHIN (This was addressed for the ParameterList elements, and will be addressed for the MatchCriterionList elements next release (CONN-1544))
* CONN-1273 - PurposeofUse and Role xsi:type attributes are NOT scoped correctly

There is another outstanding specification compliance issue that the team is aware of is related to CONN-1506 where the mustUnderstand attribute is not being set in the SOAP response header's WSA Action element. This functionality existed in all prior versions of CONNECT. The team is researching this issue.

Based on thorough review by the Healtheway test team working with CONNECT 4.X implementers these are the remaining outstanding findings regarding specification compliance based on the Healtheway manual checklist.  The CONNECT team and FHA will continue to work on a more formal testing and validation program between the two programs. 

**Release Testing**: CONNECT 4.4 was install-tested in multiple environments and with multiple operating systems to support the federal partner environments and application servers/configurations used by the community. As with each release, CONNECT was regression tested as well as integration tested against prior supported versions of CONNECT.


## CONNECT 4.3 Release

**Enhancements to Transaction Logging**: Transaction logging was introduced in 4.0. With 4.2.1/4.3, this feature was enhanced (CONN-335) to include a database-less implementation. This enhancement supports adopters that cannot have databases at the application/gateway tier. CONNECT now supports a Noop (no transaction logging) or an In-memory Cache implementation in addition to the existing Database implementation for transaction logging. Details can be found at Database-less Transaction Logging.

**Secured Entity Interfaces for Query for Documents and Retrieve Document Services**: CONNECT 4.3 fixes a known issue in secured entity interfaces for Query for Documents and Retrieve Document services that were throwing null pointer exceptions. These interfaces/WSDLs have been fixed, and adopters can use these secured interfaces.

**Build and Testing Automation and Infrastructure Improvements**: Improvements have been made to the testing process and test suites to enable an automated approach to testing.  

The Validation suite test cases were extended to cover more areas of the code base touched by CONNECT’s support of additional use cases as well as the latest features. The extended Validation suite is part of Continuous Integration (CI). These validation tests ensure that all services (PD, QD, RD, DS, and AD) work in both request and response mode using happy-path smoke tests. The CI process was further improved by the inclusion of Find Bugs (a security and vulnerability identifier program). This provides the team with insight into the code quality and helps identify any high priority security issues as new fixes are introduced during the course of a sprint or release. The CI process runs against every pull request of updated code that each developer submits. If validation tests fail, the code is not automatically merged into the product, and the team is alerted. Then, when the pull request is merged into the code base, CI will run again against all four supported application servers (WebSphere, WebLogic, Glassfish, and JBoss).  Including this automation support for all four application servers was an additional enhancement for 4.3. The enhanced CI process provides each developer immediate feedback from a functional, technology, and security standpoint, and any issues can be addressed much sooner before integration with other areas of the code base. 

One of the most significant improvements was made to CONNECT Regression testing. The Regression suite’s code coverage and rigor has continued to increase over the past couple of releases, and during Release 4.3 there was considerable progress made. Code coverage was increased an additional 24%, completing overall code coverage from a functional and conditional perspective. Also the rigor of the test cases themselves was increased, improving specification compliance and aligning our testing with industry certification test cases, such as the eHealth Exchange and the NIST Meaningful Use testing. New logic was instituted to enable reconfiguration of the CONNECT gateway to test in both standard and pass through modes without restarting the gateway. This new logic will also be used as a starting point for a future Administration and Configuration GUI. Historically, the Regression suite was only executed during release testing; with the new automation improvements made to the Regression suite, it now runs nightly during the automated build process. More information can be found in Automated Testing and Build Improvements. This provides the CONNECT Team with release-level feedback on functional and non-functional areas of the code base, allowing the CONNECT Team to address any issues or potential impacts much earlier in the release cycle, greatly reducing potential risk.

Other areas of product testing have been enhanced and improved as well. The CONNECT Team was able to procure the HP Fortify software, which allows the CONNECT team to identify, triage, and resolve security vulnerabilities identified by static and dynamic analyzers. In addition to the Find Bugs program incorporated in the CI process, Fortify will ensure that when CONNECT is released, it will meet all security requirements to run in federal agency and private environments. During Release 4.3, the CONNECT team has continued to work diligently to ensure passing against the eHealth Exchange and MU2 test scenarios. Updates and improvements were also made in the CONNECT interoperability environment to better reflect the industry production environment and bring increased efficiency and reduced timelines when conducting interoperability testing.

These improvements have enabled the product team to be more proactive on potential issues while decreasing the release testing cycles -- thus reducing the investment required to have a production-ready product. These improvements position the product to be more stable through a development cycle with a broader open source community making code contributions.  

**Fortify Scans and Testing Tools**: The CONNECT team addressed a community-supplied Fortify scan as well as internal Fortify scans as part of the development process in 4.3 to expedite identification and correction of the security violations (CONN-488). Fortify scans were executed on the 4.3 code, and all Critical and High issues/violations have been addressed. Details of the violations and associated fixes can be found in Fortify scan documentation. Addressing these security violations will help adopters to meet their organization's internal reviews as they deploy CONNECT in their preferred environments. With the software purchase and incorporation of security scans utilizing the Fortify application, CONNECT will meet all application security requirements prior to being released to the community.

**Specification Compliance and Conformance**: CONNECT continues to be compliant with NIST test cases for SOAP-based transport. Details on the related tasks and bugs identified can be found in JIRA epic (CONN-558).  During the course of this testing, there were a few issues discovered and fixed after discussions with the eHealth Exchange Spec factory and the NIST team. These fixes include:
* Modifying xsi:type values to be CE instead of hl7:CE in the PurposeOfUse and Role attributes(CONN-875) and including the appropriate namespace xsi (CONN-740)
* Fixing the WS-Addressing Header <Action> and <ReplyTo> to set the "mustUnderstand" Attribute in the initiating message (CONN-588)
* Accepting an empty URI reference ("") for the Resource attribute of the Authorization Decision Statement (CONN-560)
* Prepending an '_' to assertion id in SAML Authorization Decision Statement (CONN-732)

CONNECT 4.3 was also successfully validated against the eHealth Exchange test cases for participant certification. Details on the testing can be found at eHealth Exchange/ DIL testing.

**Adapter Improvements**: Query for Documents and Retrieve Documents adapters were fixed to retrieve Stable and On Demand documents (CONN-896).

Document Registry and Document Repository adapters were updated to utilize the Apache CXF libraries and were tested with the HIEOS registry and repository software (CONN-668). For details and installation instructions, see HIEOS Document Registry and Repository.

Issues related to secured interfaces for Entity Document Query and Document Retrieve have been fixed. See CONN-662 for details.

**Auditing Enhancement Design**: A design approach for auditing enhancements was developed, Integrating the Healthcare Enterprise's (IHE) Audit Trail and Node Authentication (ATNA) profile, as prescribed by the Nationwide Health Information Network (NwHIN) specifications for all services such as Patient Discovery, Document Query, Document Retrieve, Document Submission, Administrative Distribution and Health Information Event Messaging. The underlying ATNA IHE profile specifies audit message/record format and audit record transports required for recording any audit event. (CONN-334).

**Testing in Multiple Environments**: CONNECT 4.3 was install-tested in multiple environments to support the federal partner environments and the application servers/configurations used by the community. More details on the environments that were tested can be found under Release Testing. Installation testing results can be found here. A new environment for release testing that was included in this release was the WebSphere application server using Liberty Profile. This was included to support the future/planned DoD CONNECT environment. The team was able to successfully test and validate CONNECT 4.3 on WebSphere Liberty Profile 8.5.5.1.


## CONNECT 4.2 Release

**With Release 4.2, support for Release 4.0 and 4.1 is deprecated.   We encourage all new adopters to use CONNECT 4.2 to avail of the new features, improvements, and resolved issues.**

**Support for Java 1.6**: Since Release CONNECT 3.3, the CONNECT product has required Java 7; a number of features developed in CONNECT 3.3 and 4.0/4.1 have used Java 7 language constructs, while other features have used Java 7 as a base line for building the project. In order to support the federal partners as they move to CONNECT 4.x in their respective preferred environments (i.e., IBM WebSphere 7 running SDK 6 (CMS-OFM and SSA) or Oracle WebLogic 11g running JDK 1.6 (VA)), the CONNECT product was enhanced to use Java 6 as the base line. Since Java 7 is backwards compatible with Java 6, the CONNECT product will therefore support both Java 6 and 7.

With this feature federal partners can set their CONNECT upgrade timeline and take advantage of 4.X features independently of data center plans for application server upgrades. This will also provide additional implementation options to the community as they use CONNECT in their respective preferred environments.

**Multiple specification version enhancement support**: While CONNECT already supported multiple specification versions(2010 and Summer 2011) for the NwHIN services of Patient Discovery(PD), Query for Documents(QD), Retrieve Documents(RD), Document Submission(DS) and Administrative Distribution(AD), there were enhancements made to support additional use cases required by the partners for QD and RD services. There were two enhancements requested for this release, one related to responding to QD and RD requests and the other related to initiating QD and RD requests.

With the feature described in CONN-46 adopters using QD and RD services will be able to determine the specification version for the incoming message in their adapter interfaces. This guidance will help them further process or validate the request to determine if it is valid per the specification version as well as enable them to create a version compliant response message. 

In earlier releases CONNECT implemented the Query for Documents specification with one gateway endpoint and one adapter endpoint, however this did not provide information about the specification version to the adapter to determine whether to validate the incoming message against the 2010 or Summer 2010 specifications. With 4.2, Query for Documents service will be implemented using two gateway endpoints and two adapter endpoints. With this enhancement, the inbound message get routed to each adapter based on the specification version (2010 -> a0, Summer 2011 -> a1) to help adopters appropriately process and validate the incoming QD message. With Retrieve for Documents service, in earlier versions there were two separate gateway endpoints and two adapter endpoints possible to support the additional schema changes for On Demand documents. However the design supported the highest API level for adapters that responded to both spec versions. While this minimized the impact to having multiple adapter interfaces, this did not provide information about the spec version to the adapter to determine whether to validate the incoming message against the 2010 or Summer 2010 specifications. With this enhancement the inbound message get routed to each adapter based on the specification version (2010 -> a0, Summer 2011 -> a1) to help adopters appropriately process and validate the incoming RD message.

With the feature described in CONN-15, adopters will be able to instruct the gateway which specific versions of endpoint they wish to target. This provides adopters greater flexibility by providing them the option to target endpoints based on their use cases, deployment models, and trading partners capabilities.
In earlier releases the gateway would automatically target the highest message specification version when both specification version endpoints were available leaving room for some error scenarios not being addressed and not providing the decision making ability to the adopters.  With 4.2, the adopters can instruct the gateway what specification version for the QD and RD message they wish to target and the gateway will use that guidance to target the appropriate endpoint or return appropriate error messages to the adapter. 

**Enhancements made to pass Healtheway/ CCHIT Participant certification testing**: The CONNECT team continued to participate in testing the CCHIT test scripts for the eHealth Exchange Participant Testing and HIE Certified Network Programs as it evolved from the initial pilot to the release of the actual production tests.  A list of the test cases, results of our test execution and documentation can be found here.  As part of test execution, the team discovered a few issues related to the security related test cases that have been fixed in 4.2.

CONNECT 4.2 has passed all the Healtheway participant certification test cases (published 7/31/13 the participant certification testing production date, by CCHIT), these include both smoke and certification tests.

**Adapter improvements**: Some updates were made to the reference adapters in CONNECT to handle scenarios discovered during pilot testing for CCHIT´s HIE Certified Network pilot. These include

* PD adapter was updated to include country code in PD response if present in the MPI file.(CONN-400)
* QD adapter was updated to filter documents correctly based on event codes in the query request i.e. adapter was enhanced to recognize multiple values in the slot as well as filter the documents based on the event codes. (CONN-478)

**Code refinements, build improvements and testing coverage with automation**: In order to improve our QA process and uncover issues earlier in the development process as opposed to later at release testing time, the CI process was enhanced to include WebLogic and WebSphere Application Server. With this enhancement the CI process automatically builds and tests on WebSphere and WebLogic in addition to GlassFish. This is done at code check in time and the code is validated against all 3 application servers. Issues can be identified earlier and helps minimize risks. 

Furthering the ability to deliver more stable product with increased code quality the CONNECT team has continued to refine release testing.  Improvements were made in this release in validation, regression and interoperability test suites.  In the validation suite, increased code coverage with more targeted specificity in key functional areas has been accomplished.  A great deal of refinement work was accomplished this release on the regression suite.  Reorganization improvements were made to allow for more straightforward and ease of execution.  Test cases were added and some were refined to better validate CONNECT functionality and mirror some of the certification testing CONNECT adopters will be tested against.  Interoperability testing was reconfigured with improvements of set up to allow for a more streamlined execution that allows the CONNECT team to run interoperability testing more often and with increased confidence.  This work was viewed as an initial phase in continuing QA improvements planned, these initial improvements allow for the delivery of a more stable product with increased level of code quality.     

**Bug fixes**: The CONNECT team also fixed some important bugs that were discovered during the course of Healtheway certification testing.
* Particularly important is the issue where MTOM service decorator was being set for all service requests and hence non-compliant messages were being created for PD, QD and RD. This was fixed both at the gateway level as well as at the adapter level. See CONN-408 and the other linked tickets for specific details.
* Also a bug discovered was related to the Web Service Addressing TO element not being populated for NwHIN messages was fixed.

**Implementation Notes**

* From an interfaces perspective, Query for Documents implementation will now have both a 3.0 Gateway version as well as an a1 Adapter API Level.
* Entity interfaces for QD and RD services have a new field called 'useSpecVersion' to provide guidance to the gateway about the message specification version.
* Implementers using secured adapter interfaces are required to use a0 adapters for receiving 2010 QD and RD requests and a1 interfaces for receiving 2011 QD and RD requests to help validate incoming messages against 2010 and 2011 spec versions.
* Unsecured adapter interfaces for QD and RD services have a new field in the assertion called 'implementsSpecVersion' that provides message specification version information to the adapter interfaces.
* Implementations should expect new error messages at the entity interface ' No matching target endpoint for guidance: $useSpecVersion' for QD and RD requests if guidance provided does not match with the version supported by the targeted endpoints
* Additionally a new error message at the entity interface 'Unsupported guidance for API level' for RD requests will be returned if the guidance provided in the 'useSpecVersion' is not supported by the API level.
* No changes have been made to the entity and adapter interfaces for other services like PD, DS and AD.

**NOTE**

* RD adapters may need changes if the HCIDs in the request do not match the HCID in the uddi connection info files. This was reported by a community member as an issue since their UDDI files did not contain a urn:oid prefix for their HCIDs and the RD request message includes a urn:oid prefix in the HCID as reqd by the NwHIN specs.The discrepancy can be easily solved if both the request and the UDDI connection info have the same string for the HCID i.e. either both have the urn:oid prefix or both do not. The issue will be addressed in the next release 4.3. However, if that is not feasible for the adopter due to any reason, a new fix was added in Sprint 114 (post 4.2) as part of CONN-535 (see Sprint 114 code tag) to enhance the string compare/matching when we look up the UDDI to make sure that the HCID OID is validated sans the prefix. Also if the RD request does not contain the urn:oid prefix, the gateway includes the prefix since it is required by the NwHIN specifications. Adopters can use this tag for the fix provided and validate the same in their environments. 


## CONNECT 4.1 Release

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
One key component of CONNECT 4.0 was the continued refactoring and modularization of the code base and how these refinements would contribute to improvements in the build process to better support the CONNECT community.  These refinements have allowed for more isolation of what is built in the JAR. This makes the build process faster and more efficient and allows for quicker identifications for build and code issues. The new approach will be more IDE agnostic and easier to use with everyone’s development environments.

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


