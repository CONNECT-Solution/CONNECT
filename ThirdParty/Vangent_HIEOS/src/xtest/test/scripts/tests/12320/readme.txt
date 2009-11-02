SQ WS SIMPLE Pattern

This test generates a SIMPLE SOAP WS pattern which is the WS basis 
for the Register.b and Stored Query transactions. This test validates
that the Registry.b under test can accept and react to this WS
pattern. The content of this message is a Stored Query request
which will be sent to the configured SQ endpoint in actors.xml. The
response must be a well formed RegistryResponse message.

To see the content and form of the request message, look at log.xml in
the TestStep/MswTransaction/Request section.  The message content is 
HTTP escaped.  To view in original form, copy out the content of this 
node and globally replace &lt; with < and &#xd with \n. To see the 
content of the response from your Registry.b, repeat this on the 
node TestStep/MswTransaction/Response. 

The request message can also be generated from the command line with
the command

soapsample -display -simple machine port service

See the documentation file docs/soapsample.txt in the xdstestkit package
for more information on this command.  
