This project is an example of how to run inside of an instance of OpenSSO where this
instance is acting as the PEP and it is making calls to the Jericho PDP that was used
at the HIMSS demonstration.  Since the message is not complete, the Jericho PDP is returning
a deny.  But the intent of this example was to make sure the plumbing was in place to be
able to make the call and get an answer back.

In order to run this example, you must have OpenSSO installed and running.

Once OpenSSO is running - refer to the directions in the document OpenSSOOverview.doc
that is part of this project.    You will primarily run the steps located in the
section called: Setting up Circle of Trust for PEP to PDP Interactions.
You can skip the sections before this one.

The files that are needed to run this section are located in the OpenSSOConfigFiles 
subdirectory of the TestPEP project directory.

Once the circle of trust changes are made (the previous section),

Then you are ready to deploy and run this example.  This example provides a web service
interface to test against.  You will need some sort of utility to send a soap UI message,
like SoapUI.

Deploy the TestPEP example.  Note for some reason the GlassFishESB version of NetBeans GUI is
having trouble seeing the OpenSSO Client API jar file.   It compiles fine, but the editor shows
all of these imports as not being valid.  Furthermore, if you try to deploy with NetBeans, it
also has trouble because it thinkgs it does not have all the libraries.   But if you look
at the underlying war file, it has them and is fine.  So, you have to deploy this war file
outside of NetBeans.  You can use the command line:  asadmin deploy TestPEP.war.  This is
provided you have your path set up to find the asadmin utility and as long as you are in the 
TestPEP\dist directory when you run it.   Or... you can deploy it through the GlassFish admin console.

If using SoapUI.  Create a project off the WSDL located in: TestPEP\src\java\TestPEPService.wsdl.  It
has one parameter, but it does not matter what value is passed in.   When run, this will cause the
call to be made to OpenSSO and return a permit or deny.  Right now it will return a Deny, because
we are passing canned information to Jericho.   The best way to make sure that no errors are occurring 
is to look at the glassfish server log.  Make sure that no exceptions are logged in the communication.
The URL for this service should be: http://localhost:8080/TestPEP/TestPEPServiceService

