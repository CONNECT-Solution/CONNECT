PnR.b WS Patterns

This test generates two WS patterns which are the WS basis 
for the Provide and Register.b and Retrieve.b transactions. This test validates that the Repository.b under test can accept and react to these WS
pattern. The content of this message is a Provide and Register request
which will be sent to the configured PR.b endpoint in actors.xml. The
response must be a well formed RegistryResponse message.

The first pattern is MTOM/XOP. The Provide and Register.b message will
be in this format. The second pattern is MTOM/XOP with un-optimized
content. The response message back from the Repository.b is in MTOM/XOP
format.  The response can be optimized or un-optimized. Examples
of optimized and unoptimized MTOM/XOP message can also be found in 
the ITI Implementation Guide at
http://wiki.ihe.net/index.php?title=XDS.b_Implementation#Example_SOAP.2C_MTOM.2C_and_MTOM.2FXOP_Messages

To see the content and form of the request message, look at log.xml in
the TestStep/MswTransaction/Request section.  The message content is 
HTTP escaped.  To view in original form, copy out the content of this 
node and globally replace &lt; with < and &#xd with \n. To see the 
content of the response from your Repository.b, repeat this on the 
node TestStep/MswTransaction/Response. 

The request message can also be generated from the command line with
the command

soapsample -display -mtom machine port service

See the documentation file docs/soapsample.txt in the xdstestkit package
for more information on this command. 

The un-optimized version can be generated with 

soapsample -display -unop machine port service
