# What is CONNECT? 
CONNECT is an open source software solution that supports health information exchange – both locally and at the national level. CONNECT uses Nationwide Health Information Network standards and governance to make sure that health information exchanges are compatible with other exchanges being set up throughout the country.

This software solution was initially developed by federal agencies to support their health-related missions, but it is now available to all organizations and can be used to help set up health information exchanges and share data using nationally-recognized interoperability standards.

CONNECT can be used to:

* Set up a health information exchange within an organization
* Tie a health information exchange into a regional network of health information exchanges using Nationwide Health Information Network standards

By advancing the adoption of interoperable health IT systems and health information exchanges, the country will better be able to achieve the goal of making sure all citizens have electronic health records by 2014. Health data will be able to follow a patient across the street or across the country.

## The Solution
Three primary elements make up the CONNECT solution::

* The Core Services Gateway provides the ability to locate patients at other organizations, request and receive documents associated with the patient, and record these transactions for subsequent auditing by patients and others. Other features include mechanisms for authenticating network participants, formulating and evaluating authorizations for the release of medical information, and honoring consumer preferences for sharing their information. The Nationwide Health Information Network Interface specifications are implemented within this component.

* The Enterprise Service Components provide default implementations of many critical enterprise components required to support electronic health information exchange, including a Master Patient Index (MPI), XDS.b Document Registry and Repository, Authorization Policy Engine, Consumer Preferences Manager, HIPAA-compliant Audit Log and others. Implementers of CONNECT are free to adopt the components or use their own existing software for these purposes.

* The Universal Client Framework contains a set of applications that can be adapted to quickly create an edge system, and be used as a reference system, and/or can be used as a test and demonstration system for the gateway solution. This makes it possible to innovate on top of the existing CONNECT platform.


## CONNECT 3.3 Release
CONNECT 3.3 is now generally available, and this version of the open source health information exchange software is all about making the software easier for you to use today and well into the future!


CONNECT 3.3 includes multiple enhancements, including:

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

For additional information on the Summer 2011 compliance and specifications delta analysis, please see:
* [CONNECTR33:Nationwide Health Information Specification References]
* [CONNECTWIKI:Nationwide Health Information Network Specifications Compliance]


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

The following issues were resolved during this release. The details of these issues can be found in the [CONNECT Issue Tracker|https://issues.connectopensource.org:8443/secure/IssueNavigator.jspa].



|| Key || Summary ||
| GATEWAY-406 | Exception handling |
| GATEWAY-404 | CONNECT timeout issue |
| GATEWAY-390 | Memory Leak in CONNECT Gateway 2.4.7 running on JBoss |
| GATEWAY-296 | "Merge 3.0 Hardcoded WSDL fix into 2.4.8" doesn't work without coping WSDL files into [file:$%3Cdiv%20class=] directory |
| GATEWAY-284 | OCSP Exception |
| GATEWAY-283 | QD Saml Header Issue |
| GATEWAY-278 | UDDI Not Refreshing |
| GATEWAY-271 | Doc Repository Service failed |
| GATEWAY-269 | Responding Gateway unable to process document query using responding gateway organization identifiers |
| GATEWAY-1157 | Remove Passthru calls from DocumentSubmission. |
| GATEWAY-980 | deployment of 3.1 binaries puts lib jars in wrong location |
| GATEWAY-979 | 3.1 hotfix1 branch is not building correctly |
| GATEWAY-929 | Invoking Deferred QD Response web service (DocQueryDeferredResponseService) with Metro 2.1.1 results in ActionNotSupported error |
| GATEWAY-739 | asenv.conf.template has hardcoded paths to directories |
| GATEWAY-533 | adapterxdsbdocregistry service fails with javax.xml.ws.soap.SOAPFaultException: MustUnderstand headers |
| GATEWAY-532 | AdapterPEPImpl throws MissingResourceException if HCID list or ORGID list has elements with empty values |
| GATEWAY-521 | ASENV.BAT update instructions or GlassFish modification. |
| GATEWAY-420 | Purpose For Use is not captured by the assertion object in the SAML extractor (responding gateway) |
| GATEWAY-405 | Universal Client Problem With SSN as Patient ID |
| GATEWAY-402 | Patient Discovery TrustMode incorrectly attempts to get the receiver's home community id from the optional <asAgent> element |
| GATEWAY-400 | The outbound doc query message is populated with homeCommunityId of the requesting gateway instead of the homeCommunityId of the responding gateway |
| GATEWAY-394 | Check Policy Request incorrectly attempts to get the receiver's home community id from the optional <asAgent> element |
| GATEWAY-393 | Patient Discovery Query Params transformation helper class does not set all required elements |
| GATEWAY-362 | MCCI_IN000002UV01 acknowledgement class used for deferred services is not populating the mandatory Acknowledgement.typeCode attribute |
| GATEWAY-360 | Connect Build 3.1 build fails with ivy repository error |
| GATEWAY-359 | WSS0706: Error: No Matching Certificate for : SunPKCS11-NSS RSA public key |
| GATEWAY-358 | Connect 3.1 build halts or hangs |
| GATEWAY-343 | SAML Assertion/Issuer does not identify a person. |
| GATEWAY-342 | SAML Attribute/AttributeValue for Subject is not a person's name - value: "MEGAHIT". |
| GATEWAY-335 | Order of the SAML elements is incorrect. |
| GATEWAY-396 | PatientIdFormatUtil.hl7EncodePatientId is wrapping the result in single quotes |
| GATEWAY-328 | Doc Retrieve sends remote document id to the policy engine in the resource area, should be local patient id |
| GATEWAY-327 | .MissingResourceException - XACMLRequestProcessor.getInstance.processRequest(pdpRequest, pdpEntity, pepEntity) |
| GATEWAY-325 | CONNECT 2.4.7 error with CRL |
| GATEWAY-317 | EntityDocRetrieve interface always returns RegistryResponse status Success |
| GATEWAY-308 | There 5 Hard coded endpoints inside the code found one is a commented line otherwise 4 needs to be fixed |
| GATEWAY-302 | SAML - empty AccessConsentPolicy attribute present when it is not required/used. |
| GATEWAY-300 | Retry when web service call is made is not working correctly |
| GATEWAY-299 | XDR Response Service does not pass ReleatesTo field through to other interfaces |
| GATEWAY-277 | SAML: Authorization Decision Statement with no ACP references |
| GATEWAY-274 | CONNECT 2.4.7 error 403 connecting to Production UDDI |
| GATEWAY-273 | Gateway gets to an unstable state and then needs a restart |
| GATEWAY-266 | Authorization timestamps from entity service ignored |
| GATEWAY-265 | Subject Locality expressed in assertion from entity is not passed to external organizations |
| GATEWAY-244 | SAML PurposeForUse Attribute should be PurposeOfUse |
| GATEWAY-236 | The Deferred PD Request outgoing policy check is missing the HCID in absence of livingSubjectID from the request |
| GATEWAY-235 | Deferred PD - HCID not getting tranfered to Policy engine service on the Responding gateway side |
| GATEWAY-233 | Mural adapter: suffix is not supported |
| GATEWAY-200 | OpenSSO authenticate method fails |
| GATEWAY-164 | Document Query from across multiple communities fails - DocQueryAggregator times out |
| GATEWAY-114 | Wrong path for AdapterComponentMpi.wsdl in Mural adapter |
| GATEWAY-85 | Setting Subject Discovery to pass through mode fails due to ClassCastException |
| GATEWAY-936 | Gateway version 2.4.7 getting two different UDDI files from the validation UDDI from two different gateway servers |
| GATEWAY-320 | 3.3.1 SAML: Assertion/Subject does not contain a valid identifier for the individual issuing the request. |
| GATEWAY-313 | HibernateUtil.java file in Subscription and Aggregator are using the file name directly instead of using HibernateAccessor.getHibernateFile() method to do so. |
| GATEWAY-312 | soapUI ValidationTest fails if CONNECT Code is deployed in different port other than 8080 |
| GATEWAY-305 | Notify looks for incorrect "Producer" value during subscription search, causing Notfy requests to fail |
| GATEWAY-303 | new element saml2:Issuer is not showing up when we retrieve the wsdls |
| GATEWAY-285 | TLS_ECDH Ciphers suites does not work in Windows environments |
| GATEWAY-261 | Thread locking retry is done based on the exception text in gateway.properties file this is missing in few of the java files |
| GATEWAY-252 | Interim IDE-Neutral Build Scripts do not work in 2.4.7 |
| GATEWAY-245 | MPI Adapter looks for mpi.xml file based on user.dir which is not a constant value in the JBoss environment. |
| GATEWAY-230 | Mural adapter: when patient has middle name, first name field in 201305 is set to middle name |
| GATEWAY-216 | ant \-f deploy.xml recreate.glassfish.domain is not working on Solaris and Linux |
| GATEWAY-208 | LiFT XDR Document Submission |
| GATEWAY-192 | PRPA_IN201306UV02: remove <queryId> element from <queryAck> |
| GATEWAY-159 | Default deployment location does not use environment variable |
| GATEWAY-149 | Environment variables on Solaris require two slashes |
| GATEWAY-138 | Incorrect path references |
| GATEWAY-127 | WS Addressing Error -- while invoking internal secured services. |
| GATEWAY-115 | Product Directory Hard-Coded Again |
| GATEWAY-113 | Add "file:" prefix for FileSystemXmlApplicationContext class |
| GATEWAY-89 | RED4: WebServiceException while sending outbound Audit Log Query |
| GATEWAY-67 | NHINC_PROPERTIES_DIR has first character stripped off |
| GATEWAY-58 | FIPS compliant domain has memory issues |
| GATEWAY-56 | Schema Compliance Assertion failing for AdapterPIP.wsdl - RequestPtConsentByPtId |
| GATEWAY-46 | Outbound policy check not done for Audit Log Query |
| GATEWAY-40 | Query Parameter Filtering Issue |
| GATEWAY-37 | OpenSSO SoapUI Test |
| GATEWAY-161 | Environment variable NHINC_PROPERTIES_DIR used inconsistently |



# Open Issues

The following is a list of open critical and major issues that were not resolved as part of this release.



|| Key || Summary ||
| GATEWAY-1395  | HIEM unsubscribe does not perform a policy check and does not audit |
| GATEWAY-1794 | Document Query Schema compliance error with empty RegistryErrorList |
| GATEWAY-331 | Connect inbound services (Q4D or RD) do not start properly - it is required to restart the Connect. |
| GATEWAY-186 | Doc Query: add support for "XDSUnknownPatientId" error when patient is not known |

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


