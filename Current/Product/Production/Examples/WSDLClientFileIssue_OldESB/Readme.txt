These three composite applications illustrate two separate issues we are 
having.

Please note that these example projects must be placed in the following directory 
for these examples to work:

C:\projects\NHINC\Current\Product\Production\Examples\WSDLClientFileIssue_OldESB



Issue 1: Calling web service must be done in Thread
--------------------------------------------------
First, we have an issue where when we have an EJB Bean which calls a web service 
from within the java code, the call will fail unless it is wrapped in a thread.

Issue 2: Hard coded path to WSDL
---------------------------------
Second, when we expose a web service in the NMR that is an EJB web service, and
where that EJB web service makes a Java based call to another web service, it
keeps a hard coded path to the WSDL that was used to create the client proxy
code, and when you deploy the composite application, it tries to find (at runtime)
the WSDL in the physical location on the machine it was compiled on, rather
than looking within the deployed location for the WSDL.

Issue 3: Hand modification of jaxws-build.xml to get Schemas
-------------------------------------------------------------
There is a secondary question that we have....  Whenever we create an EJB web service
that we wrap with a composite application, in the EJB project, we have to add in
code to copy the XML Schemas to their appropriate location.  if we do not put that
in, the EJB will not deploy....  This one we know how to work around, but we would 
like to know why NetBeans is not taking care of this.

NOTE: When running these projects, they call a UDDI web service.  This web service
is exposed to the internet, so you should be able to run the tests directly....

List of Projects:

Issue 1: SampleService1EJB & SampleService1CA (Illistrates
------------------------------------ 
This illustrates the issue when trying to access a web service through an EJB
and where the call to the web service is NOT done in a thread.  To run this test
you should compile and deploy SampleService1CA.  Run TestCase1.  It will 
say passed, but this may not be true.  The only way to tell whether this passed
is to look at the glassfish output log.  When it fails, you see the following error:

HTTPBC-E00759: An exception occured while processing a reply message. The server sent HTTP status code -1: null
com.sun.xml.ws.client.ClientTransportException: The server sent HTTP status code -1: null
        at com.sun.xml.ws.transport.http.client.HttpTransportPipe.checkStatusCode(HttpTransportPipe.java:203)
        at com.sun.xml.ws.transport.http.client.HttpTransportPipe.process(HttpTransportPipe.java:177)
        at com.sun.xml.xwss.XWSSClientPipe.process(XWSSClientPipe.java:118)
        at com.sun.xml.ws.api.pipe.helper.PipeAdapter.processRequest(PipeAdapter.java:115)
        at com.sun.xml.ws.api.pipe.Fiber.__doRun(Fiber.java:598)
        at com.sun.xml.ws.api.pipe.Fiber._doRun(Fiber.java:557)
        at com.sun.xml.ws.api.pipe.Fiber.doRun(Fiber.java:542)
        at com.sun.xml.ws.api.pipe.Fiber.runSync(Fiber.java:439)
        at com.sun.xml.ws.client.Stub.process(Stub.java:222)
        at com.sun.xml.ws.client.dispatch.DispatchImpl.doInvoke(DispatchImpl.java:180)
        at com.sun.xml.ws.client.dispatch.DispatchImpl.invoke(DispatchImpl.java:206)
        at com.sun.jbi.httpsoapbc.OutboundMessageProcessor.outboundCall(OutboundMessageProcessor.java:986)
        at com.sun.jbi.httpsoapbc.OutboundMessageProcessor.dispatch(OutboundMessageProcessor.java:1017)
        at com.sun.jbi.httpsoapbc.OutboundMessageProcessor.processRequestReplyOutbound(OutboundMessageProcessor.java:661)
        at com.sun.jbi.httpsoapbc.OutboundMessageProcessor.processMessage(OutboundMessageProcessor.java:243)
        at com.sun.jbi.httpsoapbc.OutboundAction.run(OutboundAction.java:63)
        at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:885)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:907)
        at java.lang.Thread.run(Thread.java:619)

You can see the full output in: Output_SampleService1.txt.   Please note that we have also seen a different error message
when running without a thread.  I cannot recall the text of that message, it was in a different project, but pushing the 
call to a thread resolved it....   I do notice the strange binary data in the trace messages - it makes me wonder if 
what is happening by the HTTP binding component is that it is confusing the response of the encapsualted web service with
the outer one.  

Issue 1: SampleService2EJB & SampleService2CA
----------------------------------------------
Before running this test, you must undeploy sample 1 above....
This project is identical to the previous one, except in this case a thread is created so that the
call to the web service from Java is done in an encapsulated thread.  This one works.  You can see
the output of this example in Output_SampleService2.txt.

Issue 2: SampleService2EJB & SampleService2CA
----------------------------------------------
This one uses the same project as the previous test to illustrate issue 2.  To run this test, with
the SampleService2CA deployed, rename your WSDLInterfaces directory to some other name (i.e. WSDLInterfaces_renamed).
Now re-run the test.  Since it can no longer find the WSDL in the "compiled" location, it will fail.
You can find the output of this one in Output_SampleService2_FailedLocation.txt.

You will notice here that the errors are that it cannot find the WSDL in the specified location.  It 
is not looking in the deployed application area, but rather in the location where the file existed at
compile time.

Issue 2: SampleService3EJB & SampleService3CA
---------------------------------------------
Before running this test, you must undeploy sample 2 above....
In searching the web regarding this problem, we found several pages that described this as a known problem
with a work around.  It defined the problem to be that the jaxws-build.xml file contained the following target:

    <target name="wsimport-client-uddi_v3_service" depends="wsimport-init,wsimport-client-check-uddi_v3_service" unless="wsimport-client-uddi_v3_service.notRequired">
        <wsimport xendorsed="true" sourcedestdir="${build.generated.dir}/wsimport/client" extension="true" destdir="${build.generated.dir}/wsimport/binaries" wsdl="${basedir}/${meta.inf}/xml-resources/web-service-references/uddi_v3_service/wsdl/uddi_v3_service.wsdl" wsdlLocation="file:/C:/projects/nhinc/Current/Product/Production/Tutorials/WSDLClientFileIssue/WSDLInterfaces/src/wsdl/uddi_v3_service.wsdl" catalog="catalog.xml"/>
        <copy todir="${classes.dir}">
            <fileset dir="${build.generated.dir}/wsimport/binaries" includes="**/*.xml"/>
        </copy>
    </target>

It stated that the problem is that it generates with an attribute in the wsimport called "wsdlLocation".  This
is treated as a hard path...  The recommendation on the web sites was to remove the wsdlLocation attribute
and that it would then fall back to using the "wsdl" attribute instead and that it will now be relative to the
deployed composite application.   However, we found that this is not the case.  To illustrate this, we copied the
target from the jaxws-build.xml file to the build.xml file (standard way of overriding targets).   We then removed
the wsldLocation attribute.   We saw a change in behavior, but it was not sufficient...  It just changed the physical
path that was hard coded.  Now rather than looking in 
C:\projects\nhinc\Current\Product\Production\Tutorials\WSDLClientFileIssue\WSDLInterfaces\src\wsdl\uddi_v3_service.wsdl,
it now looks in:
C:\projects\nhinc\Current\Product\Production\Tutorials\WSDLClientFileIssue\SampleService3EJB\src\conf\xml-resources\web-service-references\uddi_v3_service\wsdl\uddi_v3_service.wsdl

I have tried many different options to try to get this to use a relative path to no avail...  When I look at the generated code, it
still tries to find the wsdl in this physical path.  

To reproduce this one, you deploy the composite application, then after it is deployed, you rename the directory where
it is looking for the wsdl  (similar to the previous test).    Rename the SampleService3EJB\src\conf\xml-resources\web-service-references to
some other name... and then run the test... Note that You could also deploy it in a glassfish environment that 
does not have the development projets available at all...  (Different machine altogether...).

An example of the output is found in Output_SampleService3.txt


                                     
