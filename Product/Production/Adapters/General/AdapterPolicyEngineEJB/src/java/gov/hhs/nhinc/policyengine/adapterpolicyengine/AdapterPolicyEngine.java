/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.AdapterPolicyEngineImpl;

import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "AdapterPolicyEngine", portName = "AdapterPolicyEnginePortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyengine", wsdlLocation = "META-INF/wsdl/AdapterPolicyEngine/AdapterPolicyEngine.wsdl")
@Stateless
public class AdapterPolicyEngine implements AdapterPolicyEnginePortType
{
    private static Log log = LogFactory.getLog(AdapterPolicyEngine.class);

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest)
    {
        CheckPolicyResponseType checkPolicyResp = new CheckPolicyResponseType();

        AdapterPolicyEngineImpl oPolicyEngine = new AdapterPolicyEngineImpl();
        try
        {
            checkPolicyResp = oPolicyEngine.checkPolicy(checkPolicyRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineImpl.checkPolicy.  Error: " +
                    e.getMessage();
            log.error(sMessage, e);
            throw new RuntimeException(sMessage, e);
        }
        return checkPolicyResp;
    }
}
