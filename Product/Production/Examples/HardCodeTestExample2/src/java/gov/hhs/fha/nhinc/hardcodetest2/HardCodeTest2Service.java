/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hardcodetest2;

import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.hardcodepathtest.HardCodeTest;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "HardCodeTest2", portName = "HardCodeTest2PortSoap11", endpointInterface = "gov.hhs.fha.nhinc.hardcodepathtest2.HardCodeTest2PortType", targetNamespace = "urn:gov:hhs:fha:nhinc:hardcodepathtest2", wsdlLocation = "META-INF/wsdl/HardCodeTest2Service/HardCodePathTest2.wsdl")
@Stateless
public class HardCodeTest2Service {
    @WebServiceRef(wsdlLocation = "META-INF/wsdl/HardCodePathTest/HardCodePathTest.wsdl")
    private HardCodeTest service;
    private static Log log = LogFactory.getLog(HardCodeTest2Service.class);

    public gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType test2(gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType test2Request)
    {
        PersonNameType oResponse = null;

        log.debug("Entering HardCodeTest2Service.test2(...)");

        try
        {
            gov.hhs.fha.nhinc.hardcodepathtest.HardCodeTestPortType port = service.getHardCodeTestPortSoap11();
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/HardCodeTest/HardCodeTestService");
            oResponse = port.test1(test2Request);
        }
        catch (Exception ex)
        {
            log.debug("Exception occurred: " + ex.getMessage());
        }

        log.debug("Exiting HardCodeTest2Service.test2(...)");
        return oResponse;
    }

}
