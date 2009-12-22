/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hardcodetest;

import javax.ejb.Stateless;
import javax.jws.WebService;

import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "HardCodeTest", portName = "HardCodeTestPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.hardcodepathtest.HardCodeTestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:hardcodepathtest", wsdlLocation = "META-INF/wsdl/HardCodeTestService/HardCodePathTest.wsdl")
@Stateless
public class HardCodeTestService
{

    private static Log log = LogFactory.getLog(HardCodeTestService.class);

    public PersonNameType test1(PersonNameType test1Request)
    {
        log.debug("Entering HardCodeTestService.test1(...)");
        log.debug("Exiting HardCodeTestService.test1(...)");
        return test1Request;
    }
}
