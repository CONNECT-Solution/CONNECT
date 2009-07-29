/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.wsdlissue.sample;

import gov.hhs.fha.nhinc.wsdlissue.wsdl.sample.SampleServicePortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import gov.hhs.fha.nhinc.wsdlissue.schema.sample.SampleOperationOutputType;
import gov.hhs.fha.nhinc.wsdlissue.schema.sample.SampleOperationInputType;

/**
 *
 * @author Les Westberg
 */
@WebService(serviceName = "SampleServiceService", portName = "SampleServicePort", endpointInterface = "gov.hhs.fha.nhinc.wsdlissue.wsdl.sample.SampleServicePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:wsdlissue:wsdl:sample", wsdlLocation = "META-INF/wsdl/SampleService/SampleService.wsdl")
@Stateless
public class SampleService implements SampleServicePortType {

    public SampleOperationOutputType sampleOperation(SampleOperationInputType part1)
    {
        SampleOperationOutputType oOutput = new SampleOperationOutputType();
        oOutput.setOutputParam("test");
        
        try
        {
            //SampleServiceHelper.retrieveFromUDDIServer();
            SampleServiceHelper oHelper = new SampleServiceHelper();
            oHelper.start();
        }
        catch (Exception e)
        {
            System.out.println("Failed to retrieve data from UDDI server.  Exception: " + e.getMessage());
        }
        
        return oOutput;
    }

}
