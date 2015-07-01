Direct Overview
===============
The CONNECT implementation of Direct has several interconnecting parts. This is an overview of
how those parts interact and their basic configuration. Further details regarding configurations can be
found in subsequent sections of this readme.

Direct is a secure email standard as described by RFC 5322. The Direct Project requires Health
Information Service Providers (HISPs) to support SMTP as the Backbone Protocol to provide a common
transport standard among all HISPs. Sources and Destinations communicate with the HISPs, who
in turn exchange with one another. CONNECT provides the technical capabilities as a HISP, and so
it implements SMTP using the Direct Project specifications that allow HISPs to communicate to one
another, and in addition it supports Edge Protocols that allow clients to communicate with the HISP.

A typical flow of a Direct message would involve a Source client -> Source's HISP -> Destination's HISP -> Destination's Client. For example, a Source client initiates a message using the SOAP+XDR edge protocol
that is sent via the Source's HISP (the CONNECT instance) to the Destination's HISP, A Message
Disposition Notification (MDN) is sent from the Destination's HISP and received by the Source's HISP as
an acknowledgement of receipt by the Destination's HISP. Similarly, when acting as a Destination's HISP
(again, the CONNECT instance), Direct messages that are received can be made available via the Edge
Protocol to the Destination client, and an MDN notification acknowledging the receipt by the Destination's
HISP is sent to the Source's HISP. There are two primary Edge Protocols: STMP and SOAP. Let's
consider the SMTP edge first as we describe the configuration and components.

The implementation of Direct using the SMTP Edge Protocol requires two mail servers: the internal server
and the external server. The internal mail server is user-facing and acts as an initiation point for users
within the Source to send Direct messages. It is referred to as the SMTP Edge Client. The external server
is public-facing and is responsible for exchange of SMTP-based Direct messages between HISPs. The
external server is utilized only by the HISP (the CONNECT instance) and is invisible to the end-user. The
CONNECT instance is the intermediary that connects the two mail servers.

Two pollers run at regular intervals that check for any new emails to process. One poller checks the
internal mail server for any emails from the Source, and another checks the external mail server for any
emails from other HISPs.

It is useful to look at an example of how CONNECT would send a Direct message in this scenario. A
Direct message is initiated as an email sent from a mailbox on the internal mail server. The internal poller
checks the internal mail server and picks up the outgoing message. CONNECT processes (encryption, logging, etc.)
the message and then sends it to the Destination's HISP via the external mail server.

On the other side, when CONNECT is receiving a Direct message, the external poller checks the external mail
server and picks up the incoming Direct message. CONNECT processes (decryption, logging, etc.) the Direct message
and then sends it to the internal mail server for use by the Destination. A key action on the receiving side
is that the Destination HISP sends an MDN Processed notification upon successful receipt, decryption
and trust validation of a Direct message, that Processed MDN arrives in the Source's HISP's external email server.
The Processed MDN is handled in the same manner as any other incoming message and ultimately arrives at the
local HISP Direct gateway. As of the CONNECT 4.4 release, CONNECT Direct supports receiving and sending 
dispatched MDNs and also Failed Delivery Status Notifications (DSNs). The dispatched MDNs are sent when the Direct mail sender requests for a delivery notification. This is in addition to the default processed MDN, which is required per the Applicability Statement for Secure Health Transport. The Dispatched MDN and Failed DSN are handled in the same manner as any other incoming messages and ultimately arrives in the local HISP Direct Gateway. The MDNs and DSNs are exchanged between the two HISP Direct Gateways and are not visible to the edge client. As part of the Quality Of Service (QOS) enhancement, a custom notification is sent to the edge client after a successful delivery or failed delivery or no response from the Destination HISP within a specified time limit by the Message Monitoring module. The Message Monitoring Module utilizes the MDNs/DSNs received and also the message sent time to calculate and notify the edge client about the status of the direct message sent out. The external poller also calls the Message Monitoring module to track and monitor outgoing Direct emails and sends out an notification to the edge client based on outcome of the email delivery.

A second Edge Protocol, SOAP, is available. It behaves the same way as other CONNECT adapters. A URL endpoint is provisioned in the CONNECT gateway
that accepts SOAP XDR messages. The gateway processes the message and sends it out as an SMTP
Direct message via the external mailbox, in the same manner as with the SMTP Edge Protocol. In this
case it is possible that there is no internal mail server at all. Messages could be only initiated via SOAP.
However, note that as long as both Edge Protocols are available, a Direct message can be initiated with
either Edge Protocol and this is a valid use case. 

A third, Java Edge Protocol exists as described in the Direct readme, however it is outside the scope of this introduction.

Just as outgoing messages can utilize the SOAP Edge Protocol, so can incoming Direct messages.
CONNECT is configured to use a particular Edge Protocol for incoming messages via a Spring
configuration. It is not possible to use multiple Edge Protocols simultaneously for incoming messages.
The gateway utilizes only the Edge Protocol specified in the Spring configuration file for receiving Direct
messages. The URL to which the gateway sends messages is specified just as any other adapter URL in
internalConnectionInfo.xml.

As mentioned above, the gateway processes messages regardless of which Edge Protocol initiates the message. In addition to providing security for the messages, the gateway also
performs two types of logging; Audit logging and Event logging. Audit logging is implemented via the Direct
Java Reference Implementations log4j audit messages. Event Logging is an easy way to determine if a Direct 
message has been processed fully and if the MDN has been received.

References:  
[Approach for Direct Quality of Service Enhancements](https://connectopensource.atlassian.net/wiki/x/KwGD)  
[Approach for Direct Implementation](https://connectopensource.atlassian.net/wiki/x/AQCD)   
[Direct Integration](https://connectopensource.atlassian.net/wiki/x/UICh)   
[Direct](https://connectopensource.atlassian.net/wiki/x/NoCh)   

Setting up CONNECT as a Direct HISP
===================================

###Security Policy Files

This is now a CONNECT prerequisite -- see main [README](https://github.com/CONNECT-Solution/CONNECT/blob/CONNECT/README.md)

Download the jars from:

	http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html

Install under:

	$JAVA_HOME/jre/lib/security 

Mail Servers
------------

For this step two mail servers are needed. 

###Internal Mail Server

The internal mail server can be used by users at the gateway to initiate Direct messages by sending an email. Routing rules are put in place to drop outgoing messages which need to be processed by Direct into the Inbox of the CONNECT Gateway mail account. These rules typically match the domain of the recipient against a list of HISP domains with which the Gateway is allowed to exchange messages.

This internal mail server can also be used as an SMTP edge client when the CONNECT Gateway is configured to handle inbound messages this way. In that manner inbound messages will be mailed to the recipient at the HISP. 

__Note__: The internal mail server can be omitted if the CONNECT Gateway Direct HISP is configured to initiate and receive Direct messages using SOAP/XDR edge clients exclusively. (See "Edge Client Configuration")  

###External Mail Server

The external mail server is used by the CONNECT Gateway to send outbound direct messages and receive inbound direct messages. Inbound messages must be dropped into the Inbox of the CONNECT Gateway mail account.

Both mail servers must support SMTP/TLS and IMAPS.

DNS (RFC4398)
-------------

###Prerequisites
* DNS Solution which supports editing the Zone File.  
* PKIX Certificates signed by a CA.

In order for your CONNECT Gateway Direct HISP's public certificate to be discoverable, it must be available via DNS or LDAP. Your DNS Service must support editing the Zone File. The public certificate will be converted to conform to a TYPE37 Zone File entry according to the spec RFC4398. Please note the information related to configuring and setting up the Public Certificate in LDAP is beyond the scope of this document.

The following steps can be used to achieve this:

1. Use a DNS Service that provides direct access to the zone file, like "bind".

2. Procure signed certificates as you normally would.

3. Transform the signed certificate into a binary format.

	`openssl x509 -in org-cert-signed.pem -inform PEM -out org-cert-signed.der -outform DER`

4. Use the PGP make-dns-cert tool to build the zonefile entry as if it were a PGP key.

	See: [http://www.gushi.org/make-dns-cert/HOWTO.html](http://www.gushi.org/make-dns-cert/HOWTO.html) 

5. Before applying the "big.cert" to your zone file, edit your big.cert file to specify a PKIX type of cert. This involves changing the code in position 5 from "0003" -> "0001". We found that switching this value to it's mneumonic value ("PKIX") was not accepted by our dns server (bind).
    
__Links:__  
[http://www.gushi.org/make-dns-cert/HOWTO.html](http://www.gushi.org/make-dns-cert/HOWTO.html)  
[http://gushi.livejournal.com/524199.html](http://gushi.livejournal.com/524199.html)  
[http://www.faqs.org/rfcs/rfc3597.html](http://www.faqs.org/rfcs/rfc3597.html)  
[http://tools.ietf.org/html/rfc4398](http://tools.ietf.org/html/rfc4398)  
[http://crane.initd.net/source/make-dns-cert.c](http://crane.initd.net/source/make-dns-cert.c)

Installing the CONNECT Direct feature
-------------------------------------

The Direct feature is included in CONNECT as a "selectable service", however it is currently not enabled by default.

###Building CONNECT with the Direct Feature

__Note:__ All of the commands referenced here are explained in more detail in the `<CONNECT git root>/README.md` file.

To build an EAR file with only the CONNECT Direct feature, from `<CONNECT git root>` execute:

	mvn clean install -P Direct
     
This command also interacts with other profiles, for example to build CONNECT with Direct, Patient Discovery, Query for Documents, and Document Retrieve, on WebSphere, execute:

	mvn clean install -P Direct,PD,DQ,RD,was
	
###Deploying CONNECT from a Direct perspective
    
When running `ant install` to create a local Glassfish instance, only the default domain "direct.example.org" is configured in the agent settings by default. To deploy CONNECT to other application servers, the same instructions apply with or without the Direct feature enabled with one exception. Updates to configurations outlined in the below sections of this README.md will need to be completed before deploying the application.

####Configuring the SMTP Agent settings
Agent configuration consists of setting the runtime parameters for security and trust agents. As part of CONNECT, configuration of SMTP Agent Settings are no longer supported through the smtp.agent.config.xml configuration file. All the Config agent settings are stored in the ConfigDB database and are configured through the CONNECT AdminGUI. The CONNECT AdminGUI allows system users to configure the following entities which are used by the Direct code integrated with the CONNECT Gateway Direct HISP:

      1. Domains
      2. Certificates (Key Pair)
      3. Public Certificates
      3. Trust Anchors
      4. Trust Bundles
      5. Agent Settings
By default, CONNECT comes with a domain direct.example.org and additional domains can be created or configured using the CONNECT AdminGUI.
Please refer to the [CONNECT AdminGUI user guide](https://connectopensource.atlassian.net/wiki/x/EQD9), for more information.

#####Configure local HISP Private Key
The Local HISP private/public key pair can be configured in the Agent Settings using the CONNECT AdminGUI. CONNECT Direct supports three Storage Types for holding the local HISP private key:

1. WS  -- the key pair stored in the ConfigDB database and this is the default value
2. KEYSTORE -- the key pair stored in a KeyStore file and referenced
3. LDAP -- the key pair stored in a LDAP server and referenced

Below are the steps to generate a Direct KeyStore:

     #Generate the Direct KeyStore key pair (DirectKeyStore.jks)
     keytool -v -genkey -keyalg RSA -keysize 1024 -keystore DirectKeyStore.jks -keypass changeit -storepass changeit -validity 3650 -alias direct.example.org -dname "cn=direct.example.org"
For Storage Type WS, the following are the steps to configure the Direct Gateway Agent:

1. Convert the direct keystore into PKCS12 (.p12) format.

        # create a PKCS12 file (direct.p12) from Direct KeyStore (DirectKeyStore.jks)
        keytool -importkeystore -srckeystore directkeystore.jks -srcstoretype JKS -deststoretype PKCS12 -destkeystore direct.p12
        # Remove the keystopre pass using openssl
        openssl pkcs12 -in directkeystore.p12 -nodes -out temp.pem
        # Enter twice without entering any password
        openssl pkcs12 -export -in temp.pem  -out direct-unprotected.p12
2. Add the direct-unprotected.p12 file through CONNECT AdminGUI from "Certificates" tab. Please refer to the AdminGUI User Manual for more information.

For Storage Type KEYSTORE, the following properties should be added from CONNECT AdminGUI --> Settings, to Configure Direct Gateway Agent:

        PrivateStoreType="KEYSTORE"
        PrivateStoreFile="path/keystorefilename" (eg: C:\\config\\directkeystore.jks)

The PrivateStoreType property can also have more than one type value, the values are delimited by comma (example, PublicStoreType="KEYSTORE, WS"), but make sure the respective properties are also configured.

#####Configure Public Certificates of trusted HISPs
The  Public Certificate can be configured in the Agent Settings using the CONNECT AdminGUI. CONNECT Direct supports four ways to discover the public certificate of the Destination HISP:

1. DNS -- the public certificate of the Destination HISP is located through DNS lookup and this is the default value. 
2. WS  -- the public certificate stored in the ConfigDB database
2. KEYSTORE -- the public certificate stored in a KeyStore file and referenced
3. LDAP -- the public certificate stored in a LDAP server and referenced

Below are the steps to generate a Direct Public keystore:

     #Generate the Public Direct KeyStore(PublicDirectKeyStore.jks) by imporing a public cert
     # The below sample imports direct.testdirect.org Public certificate into the KeyStore PublicDirectKeyStore.jks
     keytool -v -import -keypass changeit -noprompt -trustcacerts -alias direct.testdirect.org -file direct_testdirect_org.cer -keystore PublicDirectKeyStore.jks -storepass changeit

All the trusted HISPs Public Certificate discovery is done through DNS by default, no further configuration is required for this type. 
For the discovery Type WS, all the trusted HISPs public certificates should be added through the CONNECT AdminGUI Certificates tab and the following property should be added from Settings tab through CONNECT AdminGUI.

       PublicStoreType="WS"

For the Type KEYSTORE, the following properties should be added from CONNECT AdminGUI --> Settings, to Configure Direct Gateway Agent. The KeyStore mentioned in the property PublicKeyStoreFile should also have also have all the trusted Destination HISP Public Certificates.

        PublicStoreType="KEYSTORE"
        PublicKeyStoreFile="path/keystorefilename" (eg: C:\\config\\PublicKeyStore.jks)
        PublicKeyStoreFilePass="changeit"

The PublicStoreType property can also have more than one type value, the values are delimited by comma (example, PublicStoreType="KEYSTORE, WS"), but make sure the respective properties are also configured.

#####Configure Trust Anchors of trusted HISPs
The trust anchors are the CA signing certs for the HISP domains that we wish to exchange messages with. The Trust anchors can be configured in the Agent Settings using the CONNECT AdminGUI. CONNECT Direct supports three Storage Types for holding the Destination HISPs anchor certificates:

1. WS  -- the key pair stored in the ConfigDB database and this is the default value
2. KEYSTORE -- the key pair stored in a Keystore file and referenced
3. LDAP -- the key pair stored in a LDAP server and referenced

Below are the steps to generate a Direct Anchor keystore:

     #Generate the Anchor KeyStore (Anchors.jks) by importing an anchor certificate into Anchors.jks
     keytool -v -import -keypass changeit -noprompt -trustcacerts -alias direct.example.org -file direct_testdirect_org.cer -keystore Anchors.jks -storepass changeit

For the Storage Type WS, all the trusted HISPs Anchor certificates should be added through the CONNECT AdminGUI Domains--> Edit Domain-->Anchors tab.

For Storage Type KEYSTORE, the following properties should be added from CONNECT AdminGUI --> Settings, to Configure Direct Gateway Agent. The KeyStore mentioned in the property AnchorKeyStoreFile should also have also have all the trusted Destination HISP Anchors. All the trusted Destination HISPs Anchor certificate "Common Name" should be added as a comma delimited value for the properties <local HISP domain>IncomingAnchorAliases and <local HISP domain>OutgoingAnchorAliases, in order for Direct to communicate with the Destination HISPs.

        AnchorStoreType="KEYSTORE"
        AnchorKeyStoreFile="path/keystorefilename" (eg: C:\\config\\Anchors.jks)
        AnchorKeyStoreFilePass="changeit"
        AnchorResolverType="Multidomain"
        # Should have <local HISP domain>IncomingAnchorAliases as the key and comma delimited Cert Common Name as value
        direct.example.orgIncomingAnchorAliases="direct.sitenv.org_ca" eg (direct.sitenv.org_ca,direct.testdirect.org)
        # Should have <local HISP domain>OutgoingAnchorAliases as the key and comma delimited Cert Common Name as value
        direct.example.orgOutgoingAnchorAliases="direct.sitenv.org_ca" eg (direct.sitenv.org_ca,direct.testdirect.org)

The AnchorStoreType property can also have more than one type value, the values are delimited by comma (example, AnchorStoreType="KEYSTORE, WS"), but make sure the respective properties are also configured.

#####Configure Trust Bundles
Trust Bundles are a collection of trust anchor certificates. Trust bundles are packaged into a single file using the PKCS7 standard and distributed via a known URL (the location is discovered out of band). Trust bundles are configured from the Trust Bundles tab through CONNECT AdminGUI. In order to use a Trust Bundle, it has to be associated to the domain through Domain Trust Bundle association page(From CONNECT AdminGUI --> Domains tab (Edit Domain) --> Trust Bundles tab).

###Configure Direct Pollers
Once the application configuration has been completed and the application deploys successfully, regardless of the target application server, the "pollers" will need to be enabled. The pollers are disabled by default because there is no feasable way at this point to deploy and configure all of the necessary Direct HISP components automatically. Update `<nhinc.properties.dir>/direct.appcontext.xml` from:

    <!-- task:scheduled-tasks scheduler="directScheduler">
        <task:scheduled ref="outboundMessagePoller" method="poll" cron="0,30 * * * * *"/>
        <task:scheduled ref="inboundMessagePoller" method="poll" cron="15,45 * * * * *"/>
    </task:scheduled-tasks>
    <task:scheduler id="directScheduler" />
    <bean id="manageTaskScheduler" class="gov.hhs.fha.nhinc.mail.ManageTaskScheduler" init-method="init" destroy-method="clean">
        <constructor-arg ref="directScheduler"/>
    </bean-->
    
to:

    <task:scheduled-tasks scheduler="directScheduler">
        <task:scheduled ref="outboundMessagePoller" method="poll" cron="0,30 * * * * *"/>
        <task:scheduled ref="inboundMessagePoller" method="poll" cron="15,45 * * * * *"/>
    </task:scheduled-tasks>
    <task:scheduler id="directScheduler" />
    <bean id="manageTaskScheduler" class="gov.hhs.fha.nhinc.mail.ManageTaskScheduler" init-method="init" destroy-method="clean">
        <constructor-arg ref="directScheduler"/>
    </bean>

Gateway Configuration
---------------------

The configuration files can be found under the file path specified by `-Dproperties.dir=` in the startup script for the application server.

###Configure Mail Server Properties

--> direct.mail.internal.properties : _defines properties for the internal mail server._   
--> direct.mail.external.properties : _defines properties for the internal mail server._

Both files contain the following properties which are used to connect to each mail server. The following properties are used by the CONNECT Gateway Direct HISP:

	# credentials
	connect.mail.user=connectuser@direct.example.org
	connect.mail.pass=passwordxyz

	# number of direct messages to handle in a batch, can be used to throttle, even out load, and prevent DOS.
	connect.max.msgs.in.batch=25

	# (true/false) enables java mail session debugging, set to false in production.
	connect.mail.session.debug=true

	# (true/false) should unhandled messages be deleted from server?
	connect.delete.unhandled.msgs=false

The rest of the properties are used by Javamail:

	# smtp
	mail.smtp.host=smtp-internal.direct.example.org
	mail.smtp.auth=true
	mail.smtp.port=587 
	mail.smtp.starttls.enable=true

	# imap
	mail.imaps.host=imap-internal.direct.example.org
	mail.imaps.port=993

__Links:__  
[http://wiki.directproject.org/smtp+gateway+configuration](http://wiki.directproject.org/smtp+gateway+configuration)  
[http://api.nhindirect.org/java/site/gateway/3.0.1/users-guide/](http://api.nhindirect.org/java/site/gateway/3.0.1/users-guide/)

###Configuring the SMTP Agent Cache
CONNECT Direct caches all the SMTP agent settings during the server startup. The cache can be configured to refresh every 'n' milli seconds using the property "AgentSettingsCacheRefreshTime" which is defined in gateway.properties. The default value is 5 minutes. Whenever a Agent Setting Entity is changed/added/removed, the Direct Gateway will take 'AgentSettingsCacheRefreshTime' milli seconds to take effect. The cache refresh can be enabled or disabled using the property AgentSettingsCacheRefreshActive, by default the cache refresh is enabled. Please note setting the AgentSettingsCacheRefreshTime very low may hamper the performance of the Direct Gateway.

    # Agent Settings Cache Refresh time in milli seconds, 60000=1 minute 300000=5 minutes
    AgentSettingsCacheRefreshTime=300000
    AgentSettingsCacheRefreshActive=true

###Configuring QOS settings
CONNECT Direct Quality Of Service (QOS) enhancement supports tracking and monitoring of outgoing Direct messages. The outgoing Message monitoring and tracking can be configured throguh different properties defined in the gateway.properties. The Message monitoring and tracking can be enabled or disabled through the "MessageMonitoringEnabled" property and by default its enabled. Currently Quality of Service (QOS) is not supported for SOAP/XDR based edge client systems and the property "MessageMonitoringEnabled" should be set to false in this case. 

The time limit before the Processed and Dispatched MDNs should be received from the Destination HISP can be configured through "ProcessedMessageReceiveTimeLimit" and "DispatchedMessageReceiveTimeLimit" properties. The default values are 1 hour and 24 hours respectively. The properties "OutboundFailedMessageRetryCount", "InboundFailedMessageRetryCount" and "NotifyOutboundSecurityFailureImmediate" are for future use and currently not used.

    #Direct Message Monitoring properties
    PostmasterEmailIdPrefix=postmaster
    OutboundFailedMessageRetryCount=1
    InboundFailedMessageRetryCount=1
    NotifyOutboundSecurityFailureImmediate=true
    MessageMonitoringEnabled=true
    # Time Limit in milli seconds 1 minute=60 seconds = 60000 milli seconds
    # 1 Hour
    ProcessedMessageReceiveTimeLimit=3600000
    # 24 hours
    DispatchedMessageReceiveTimeLimit=86400000

###Configuring Mail Pollers
direct.appcontext.xml : _used to schedule the mail pollers._

Both internal and external mail servers must be polled for messages as a scheduled task. CONNECT Gateway Direct HISP uses spring task scheduling to achieve this. The polling task will run as often as you like, according to the format specified in the cron attribute. Note that values of `connect.max.msgs.in.batch` along with the cron entry can be tweaked to even out load and improve performance depending on hardware and infrastructure requirements.

__Note__: The cron format used by Spring supports the seconds setting.  
__Note__: Outbound message polling can be omitted if soap edge clients are used exclusively for sending and receiving direct messages.

The following example schedules the outbound message poller against the internal mail server to run at :00 and :30 seconds each minute. The inbound message poller runs against the external mail server at :15 and :45 seconds each minute. 

	<!-- Set up polling for inbound and outbound direct messages -->
	<task:scheduled-tasks scheduler="directScheduler">
	    <task:scheduled ref="outboundMessagePoller" method="poll" cron="0,30 * * * * *"/>
	    <task:scheduled ref="inboundMessagePoller" method="poll" cron="15,45 * * * * *"/>
	</task:scheduled-tasks>
	<task:scheduler id="directScheduler" />
    <bean id="manageTaskScheduler" class="gov.hhs.fha.nhinc.mail.ManageTaskScheduler" init-method="init" destroy-method="clean">
        <constructor-arg ref="directScheduler"/>
    </bean>

__Links:__  
[http://static.springsource.org/spring/docs/3.0.x/reference/scheduling.html](http://static.springsource.org/spring/docs/3.0.x/reference/scheduling.html)  
[http://blog.springsource.org/2010/01/05/task-scheduling-simplifications-in-spring-3-0/](http://blog.springsource.org/2010/01/05/task-scheduling-simplifications-in-spring-3-0/)

###Edge Client Configuration

The following section is used to configure how inbound direct messages are handled (SMTP/soap/java) after an inbound direct message is processed. 

--> DirectEdgeClientProxyConfig.xml : _Used to determine which edge client will be used to handle processed inbound direct messages._

This file uses spring to configure which edge client we want to use for handling processed Inbound Direct messages. We can specify:

	<alias alias="directedgeclient" name="directedgeclientsmtp" />

or:

	<alias alias="directedgeclient" name="directedgeclientjava" />

or:

	<alias alias="directedgeclient" name="directedgeclientsoap" />
	
####directedgeclientsmtp

When SMTP is specified, processed direct messages and MDN messages are sent to the recipient on the internal mail server.	

####directedgeclientjava

When java is specified, processed direct messages are handled by the Java class configured. A stub class is included by default which can be replaced with a real implementation:

	<bean lazy-init="true" class="gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxyJavaImpl" id="directedgeclientjava" name="directedgeclientjava"> 
		<meta key="impltype" value="java"/>
	</bean>

MDN messages are logged.

####directedgeclientsoap

When SOAP is specified, the endpoint adapter is invoked as specified in internalConnectionInfo.xml.

--> internalConnectionInfo.xml : _Specify an adapter endpoint to be invoked which handles processed inbound direct messages._

Register the adapter endpoint in _businessService/bindingTemplates/bindingTemplate/accessPoint_:

	<!-- Direct Soap Edge  -->
	<businessService serviceKey="uddi:nhincnode:directsoapedge">
	<name xml:lang="en">directsoapedge</name>
	<bindingTemplates>
	    <bindingTemplate bindingKey="uddi:nhincnode:directsoapedge" serviceKey="uddi:nhincnode:directsoapedge">
	        <accessPoint useType="endPoint">https://localhost:8080/YourDirectSoapEndpoint</accessPoint>
	        <categoryBag>
	            <keyedReference tModelKey="CONNECT:adapter:apilevel" keyName="" keyValue="LEVEL_a0"/> 
	        </categoryBag>
	    </bindingTemplate>
	</bindingTemplates>
	<categoryBag>
	    <keyedReference tModelKey="uddi:nhin:standard-servicenames" keyName="directsoapedge" keyValue="directsoapedge"/>
	</categoryBag>
	</businessService>

MDN messages are logged.

###Initiating a message using SOAP+XDR

CONNECT adopters can initiate direct messages using a SOAP web services endpoint modeled after IHE's XDR specification. Use of this service assumes items in the previous sections have been configured appropriately, however there is no technical dependency on the edge client configuration to receive messages from CONNECT. In other words it is a valid use case to use this SOAP+XDR edge service regardless of the Edge Client Configuration.

The SOAP+XDR edge service implements the `XDS.b_DocumentRepositoryWSDLSynchMTOM.wsdl` WSDL interface, and is provisioned at `http://<host>:<port>/CONNECTDirect/DocumentRepository_Service`. This endpoint supports web service headers to facilitate sending of SMTP message. 

__Note__: Domains used in the to and from block must be configured in the SMTP Agent.

Web Service Headers:

    <d:addressBlock xmlns:d="urn:direct:addressing">
        <d:from>CONNECTProduct@direct.connectopensource.org</d:from>
        <d:to>gm2552@direct.securehealthemail.com</d:to>
    </d:addressBlock>
	
Messages received on this interface are transformed into an XDM package and sent as an attachment. Please see the following table regarding the full and minimum metadata set requirements:

    * Metadata Attribute           XDS     Minimal Metadata
    * -----------------------------------------------------
    * author                       R2      R2
    * classCode                    R       R2
    * confidentialityCode          R       R2
    * creationTime                 R       R2
    * entriUUID                    R       R
    * formatCode                   R       R
    * healthcareFacilityTypeCode   R       R2
    * languageCode                 R       R2
    * mimeType                     R       R
    * patientId                    R       R2
    * practiceSettingCode          R       R2
    * sourcePatientId              R       R2
    * typeCode                     R       R2
    * uniqueId                     R       R


Due to the paradigm shift between a synchronous SOAP+XDR message and an asynchronous SMTP/MDN messages, a successful response status from this service only means that a message was sent. Currently, adopters will have to check the event logging for the status of MDN messages.

Helpful Links
-------------

[http://engineerbyday.wordpress.com/2011/09/13/how-email-encryption-works/](http://engineerbyday.wordpress.com/2011/09/13/how-email-encryption-works/)  
[http://www.freebsdmadeeasy.com/tutorials/freebsd/create-a-ca-with-openssl.php](http://www.freebsdmadeeasy.com/tutorials/freebsd/create-a-ca-with-openssl.php)  
[http://www.freebsdmadeeasy.com/tutorials/web-server/apache-ssl-certs.php](http://www.freebsdmadeeasy.com/tutorials/web-server/apache-ssl-certs.php)  
[http://blog.jgc.org/2011/06/importing-existing-ssl-keycertificate.html](http://blog.jgc.org/2011/06/importing-existing-ssl-keycertificate.html)
