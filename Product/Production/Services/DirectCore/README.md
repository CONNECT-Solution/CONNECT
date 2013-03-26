Direct Overview
===============
The CONNECT 4.1 implementation of Direct has several interconnecting parts. This is an overview of
how those parts interact and their basic configuration. Further details regarding configurations can be
found in subsequent sections of this readme.

Direct is a secure email standard as described by RFC 5322. The Direct Project requires Health
Information Service Providers (HISPs) to support SMTP as the Backbone Protocol to provide a common
transport standard among all HISPs. Sources and Destinations communicate with the HISPs, who
in turn exchange with one another. CONNECT provides the technical capabilities as a HISP, and so
it implements SMTP using the Direct Project specifications that allow HISPs to communicate to one
another, and in addition it supports Edge Protocols that allow clients to communicate with the HISP.

A typical flow of a Direct message would involve a Source client -> Source's HISP -> Destination's HISP -> Destination's Client.
For example, a Source client initiates a message using the SOAP+XDR edge protocol
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

It is useful to look at an example of how CONNECT 4.1 would send a Direct message in this scenario. A
Direct message is initiated as an email sent from a mailbox on the internal mail server. The internal poller
checks the internal mail server and picks up the outgoing message. CONNECT processes (encryption, logging, etc.)
the message and then sends it to the Destination's HISP via the external mail server.

On the other side, when CONNECT is receiving a message, the external poller checks the external mail
server and picks up the incoming message. CONNECT processes (decryption, logging, etc.) the message
and then sends it to the internal mail server for use by the Destination. A key action on the receiving side
is that the Destination HISP sends an MDN Processed notification upon successful receipt, decryption
and trust validation of a Direct message, That MDN arrives in the Source's HISP's external email server.
The MDN is handled in the same manner as any other incoming message and ultimately arrives at the
internal mail server. While the Applicability Statement indicates that additional MDNs may be sent to
indicate further progress processing the message, they are not required and were not included in this
release of CONNECT. 

A second Edge Protocol is available. It is the SOAP Edge Protocol and it acts in
the same way as any other CONNECT adapter. A URL endpoint is provisioned in the CONNECT gateway
that accepts SOAP XDR messages. The gateway processes the message and sends it out as an SMTP
Direct message via the external mailbox, in the same manner as with the SMTP Edge Protocol. In this
case it is possible that there is no internal mail server at all. Messages could be only initiated via SOAP.
However, note that as long as both Edge Protocols are available, a Direct message can be initiated with
either Edge Protocol and this is a valid use case. (A third, Java Edge Protocol exists as described in the
Direct readme. However it is outside the scope of this introduction.)

Just as outgoing messages can utilize the SOAP Edge Protocol, so can incoming Direct messages.
CONNECT is configured to use a particular Edge Protocol for incoming messages via a Spring
configuration. It is not possible to use multiple Edge Protocols simultaneously for incoming messages.
The gateway utilizes only the Edge Protocol specified in the Spring configuration file for receiving Direct
messages. The URL to which the gateway sends messages is specified just as any other adapter URL in
internalConnectionInfo.xml.

As mentioned above, the gateway processes messages regardless of which Edge Protocol initiates the message. In addition to providing security for the messages, the gateway also
performs two types of logging; Audit logging and Event logging. Audit logging is implemented via the Direct
Java Reference Implementations log4j audit messages. Event Logging is an easy way to determine if a Direct 
message has been processed fully and if the MDN has been received. Please see the CONNECT Event Logging
documentation for more details (3rd link below).

References:  
https://developer.connectopensource.org/display/CONNECTWIKI/Approach+for+Direct+Implementation
https://developer.connectopensource.org/display/CONNECT40/Direct+Integration 
https://developer.connectopensource.org/display/CONNECT40/Direct

Setting up CONNECT as a Direct HISP
===================================

###Security Policy Files

This is now a 4.1 prerequisite -- see main README

Download the jars from:

	http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html

Install under:

	$JAVA_HOME/jre/lib/security 

###Maven

Direct is currently disabled in the deployment pom.xml file. In order to enable it...  
--> .../Product/Production/Deploy/ear/pom.xml : _maven pom for the deployment._ 

    <direct.excluded>false</direct.excluded>

Mail Servers
------------

For this step 2 mail servers are needed. 

###Internal Mail Server

The internal mail server can be used by users at the gateway to initiate Direct messages by sending an email. Routing rules are put in place to drop outgoing messages which need to be processed by Direct into the Inbox of the CONNECT Gateway mail account. These rules typically match the domain of the recipient against a list of HISP domains which the Gateway is allowed to exchange messages.

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

In order for your CONNECT Gateway Direct HISP's public certificate to be discoverable, it must be available via DNS (or LDAP which is beyond the scope of this). Your DNS Service must support editing the Zone File. The public certificate will be converted to conform to a TYPE37 Zone File entry according to the spec RFC4398.  

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
     
This command also interacts with other profiles, for example to build CONNECT with Direct, Patient Discovery, Query for Documents, and Document Retrieve, on Websphere, execute:

	mvn clean install -P Direct,PD,DQ,RD,websphere
	
###Deploying CONNECT from a Direct perspective
    
When running `ant install` to create a local glassfish instance, certificates are automatically generated and placed in the following default stores (which are configurable through the `local.install.properties` file:

    achors.jks - to store the trust anchors of trusted HISPs.
    example-store.jks - to store the public keys of trusted HISPs.
    exmple-key.jks - to store the local HISP private key.
	
As part of the `ant deploy.connect` command (which is executed as part of `ant install`) stores are referenced via the `smtp.agent.config.xml`.

__Note:__ When deploying to glassfish using the ant scripts, any changes made to the glassfish config directory will be overwritten with the configs from the CONNECT properties jar.

To deploy CONNECT to other application servers, the same instructions apply with or without the Direct feature enabled with one exception. Updates to configurations outlined in the below sections of this README.md will need to be completed before deploying the application.

Once the application configuration has been completed and the application deploys successfully, regardless of the target application server, the "pollers" will need to be enabled. The pollers are disabled by default because there is no feasable way at this point to deploy and configure all of the necessary Direct HISP components automatically. Update `<nhinc.properties.dir>/direct.appcontext.xml` from:

    <!-- task:scheduled-tasks scheduler="directScheduler">
        <task:scheduled ref="outboundMessagePoller" method="poll" cron="0,30 * * * * *"/>
        <task:scheduled ref="inboundMessagePoller" method="poll" cron="15,45 * * * * *"/>
    </task:scheduled-tasks>
    <task:scheduler id="directScheduler" / -->
    
to:

    <task:scheduled-tasks scheduler="directScheduler">
        <task:scheduled ref="outboundMessagePoller" method="poll" cron="0,30 * * * * *"/>
        <task:scheduled ref="inboundMessagePoller" method="poll" cron="15,45 * * * * *"/>
    </task:scheduled-tasks>
    <task:scheduler id="directScheduler" />

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


###Configuring the Smtp Agent in XML

--> smtp.agent.config.xml : _defines domains, trust anchors and keystores used by the Direct code integrated with the CONNECT Gateway Direct HISP._

A single _SmtpAgentConfig/Domains/AnchorStore_ is used to specify the keystore containing CA signing certs for the domains we wish to exchange messages with:

      <AnchorStore type="Uniform" storeType="KeyStore" file="/path/to/direct.anchorstore.jks" filePass="changeit" privKeyPass="changeit"/>    

Each domain managed by this CONNECT Gateway Direct HISP will have an _SmtpAgentConfig/Domains/Domain_ entry like:

      <Domain name="direct.connectopensource.org" postmaster="postmaster@direct.connectopensource.org">

_SmtpAgentConfig/Domains/Domain/IncomingTrustAnchors/Anchor_ defines an incoming trust anchor where the name attribute specifies the alias in the keystore. _SmtpAgentConfig/Domains/Domain/OutgoingTrustAnchors/Anchor_ defines an outgoing trust anchor where the name attribute specifies the alias in the keystore. There will be an entry for each HISP we want to talk to:

	<IncomingTrustAnchors> 
		<Anchor name="direct.hispdev1.hispdirect.com"/>
		<!-- ... -->
	</IncomingTrustAnchors>  
	<OutgoingTrustAnchors> 
		<Anchor name="direct.hispdev1.hispdirect.com"/>
		<!-- ... -->        
	</OutgoingTrustAnchors>            

The _SmtpAgentConfig/PublicCertStores_ section is used to define how public certificates are discovered. They could be loaded locally, we can use DNS and/or LDAP:  

	<PublicCertStores>
		<PublicCertStore type="Keystore" file="/path/to/direct.publicstore.jks" filePass="changeit" privKeyPass="changeit"/>
		<PublicCertStore type="DNS" />
	</PublicCertStores>

The _SmtpAgentConfig/PrivateCertStore_ section tells us where the private certificates are stored:  

	<PrivateCertStore type="Keystore" file="/path/to/direct.privatestore.jks" filePass="changeit" privKeyPass="changeit"/>

_SmtpAgentConfig/RawMessageSettings,OutgoingMessagesSettings,IncomingMessagesSettings,BadMessagesSettings_ define folder locations are used for staging and storing messages on the filesystem: 

	<RawMessageSettings saveFolder="RawMsgFolder"/>
	<OutgoingMessagesSettings saveFolder="OutgoingMsgFolder"/>
	<IncomingMessagesSettings saveFolder="IncomingMsgFolder"/>
	<BadMessagesSettings saveFolder="BadMsgFolder"/>  

Use _SmtpAgentConfig/MDNSettings_ to define MDN autoresponse behavior:

	<MDNSettings autoResponse="true" productName="NHIN Direct Security Agent">
		<Text><![CDATA[This is a CDATA subject]]></Text>
	</MDNSettings>

__Links:__  
[http://wiki.directproject.org/smtp+gateway+configuration](http://wiki.directproject.org/smtp+gateway+configuration)  
[http://api.nhindirect.org/java/site/gateway/2.0/users-guide/smtp-depl-xmlconfig.html](http://api.nhindirect.org/java/site/gateway/2.0/users-guide/smtp-depl-xmlconfig.html)

###Configuring Mail Pollers
--> direct.appcontext.xml : _used to schedule the mail pollers._

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

__Links:__  
[http://static.springsource.org/spring/docs/3.0.x/reference/scheduling.html](http://static.springsource.org/spring/docs/3.0.x/reference/scheduling.html)  
[http://blog.springsource.org/2010/01/05/task-scheduling-simplifications-in-spring-3-0/](http://blog.springsource.org/2010/01/05/task-scheduling-simplifications-in-spring-3-0/)

###Edge Client Configuration

The following section is used to configure how inbound direct messages are handled (smtp/soap/java) after an inbound direct message is processed. 

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
	
Messages received on this interface are transformed into an XDM package and sent as an attachment. Please see the following table regardling the full and mimimum metadata set requirements:

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


Due to the paradigm shift between a synchronous SOAP+XDR message and an ansynchronous SMTP/MDN messages, a sucessful response status from this service only means that a message was sent. Currently, adopters will have to check the event logging for the status of MDN messages.

Helpful Links
-------------

[http://engineerbyday.wordpress.com/2011/09/13/how-email-encryption-works/](http://engineerbyday.wordpress.com/2011/09/13/how-email-encryption-works/)  
[http://www.freebsdmadeeasy.com/tutorials/freebsd/create-a-ca-with-openssl.php](http://www.freebsdmadeeasy.com/tutorials/freebsd/create-a-ca-with-openssl.php)  
[http://www.freebsdmadeeasy.com/tutorials/web-server/apache-ssl-certs.php](http://www.freebsdmadeeasy.com/tutorials/web-server/apache-ssl-certs.php)  
[http://blog.jgc.org/2011/06/importing-existing-ssl-keycertificate.html](http://blog.jgc.org/2011/06/importing-existing-ssl-keycertificate.html)
