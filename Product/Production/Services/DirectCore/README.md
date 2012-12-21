Setting up CONNECT as a Direct HISP
===================================

###Security Policy Files

This is now a 4.0 prerequisite -- see main README

Download the jars from:

	http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html

Install under:

	$JAVA_HOME/jre/lib/security 

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

###Edge Client Configuration

--> DirectEdgeClientProxyConfig.xml : _Used to determine which edge client will be used to handle processed inbound direct messages._

This file uses spring to configure which edge client we want to use for handling processed Inbound Direct messages. We can specify:

	<alias alias="directedgeclient" name="directedgeclientsmtp" />

or:

	<alias alias="directedgeclient" name="directedgeclientsoap" />

__Note:__ Processed Inbound Direct MDN Messages are passed to the SMTP edge client. If the edge client is SOAP, the MDN is logged (event logging + log4j). 

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
