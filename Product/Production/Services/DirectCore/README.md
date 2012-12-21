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

The following steps to achieve this:
TBD       


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

--> smtp.agent.config.xml : defines domains, trust anchors and keystores used by the Direct code integrated with the CONNECT Gateway Direct HISP.

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

###Edge Client Configuration

--> DirectEdgeClientProxyConfig.xml : Used to determine which edge client will be used to handle processed inbound direct messages.

This file uses spring to configure which edge client we want to use. We can specify:

	<alias alias="directedgeclient" name="directedgeclientsmtp" />

or:

	<alias alias="directedgeclient" name="directedgeclientsoap" />
(how does this msg get passed to an adapter?)

###Configuring Mail Pollers
--> direct.appcontext.xml : used to schedule the mail pollers.

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

###Initiating a message using SOAP+XDR
TBD

