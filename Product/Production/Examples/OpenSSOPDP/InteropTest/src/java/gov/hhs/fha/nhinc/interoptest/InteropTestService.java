/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.interoptest;

import gov.hhs.fha.nhinc.interoptestschema.RunTestOutputType;
import gov.hhs.fha.nhinc.interoptestservice.InteropTestServicePortType;
import gov.hhs.fha.nhinc.testpepservice.TestPEPServiceService;
import gov.hhs.fha.nhinc.testpepservice.TestPEPServicePortType;
import gov.hhs.fha.nhinc.testpepschema.EnforceResourceInputType;
import gov.hhs.fha.nhinc.testpepschema.EnforceResourceOutputType;
import javax.xml.ws.BindingProvider;
import javax.jws.WebService;

/**
 *
 * @author Admin
 */
@WebService(serviceName = "InteropTestServiceService", portName = "InteropTestServicePort", endpointInterface = "gov.hhs.fha.nhinc.interoptestservice.InteropTestServicePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:interoptestservice", wsdlLocation = "WEB-INF/wsdl/InteropTestService/InteropTestService.wsdl")
public class InteropTestService implements InteropTestServicePortType
{
    private String pepEndpoint = "http://localhost:8080/TestPEP/TestPEPServiceService?WSDL";

    public gov.hhs.fha.nhinc.interoptestschema.RunTestOutputType runTest(gov.hhs.fha.nhinc.interoptestschema.RunTestInputType runTestInput)
    {
        System.out.println("Entering RunTest method.");

        TestPEPServiceService service = new TestPEPServiceService();
        TestPEPServicePortType pepPort = service.getTestPEPServicePort();
        ((BindingProvider)pepPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pepEndpoint);

        EnforceResourceInputType oInput = new EnforceResourceInputType();
        oInput.setInputParam("Test");
        EnforceResourceOutputType oOutput = pepPort.enforceResource(oInput);

        gov.hhs.fha.nhinc.interoptestschema.RunTestOutputType oResult = new RunTestOutputType();
        oResult.setOutputParam(oOutput.getOutputParam());

        System.out.println("Leaving RunTest method.  oResult = " + oOutput.getOutputParam());

        return oResult;
    }

}
