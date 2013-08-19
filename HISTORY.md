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


