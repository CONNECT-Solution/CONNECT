/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hardcodetest;

import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;

/**
 *
 * @author westberg
 */
public class CallService
{
    public PersonNameType makeCall(PersonNameType oName)
    {
        //oName.setFamilyName("Smith");
        PersonNameType oResult = null;


        try
        { // Call Web Service Operation
            gov.hhs.fha.nhinc.hardcodepathtest.HardCodeTest service = new gov.hhs.fha.nhinc.hardcodepathtest.HardCodeTest();
            gov.hhs.fha.nhinc.hardcodepathtest.HardCodeTestPortType port = service.getHardCodeTestPortSoap11();
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/HardCodeTest/HardCodeTestService");

            oResult = port.test1(oName);
        }
        catch (Exception ex)
        {
            System.out.println("Failed to call service: Error: " + ex.getMessage());
        }

        return oResult;

    }

}
