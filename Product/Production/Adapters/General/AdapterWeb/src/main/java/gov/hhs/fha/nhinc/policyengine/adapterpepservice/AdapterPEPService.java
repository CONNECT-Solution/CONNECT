package gov.hhs.fha.nhinc.policyengine.adapterpepservice;

import gov.hhs.fha.nhinc.adapterpep.AdapterPEPPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpep.AdapterPEPImpl;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPEP", portName = "AdapterPEPPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpep.AdapterPEPPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpep", wsdlLocation = "WEB-INF/wsdl/AdapterPEPService/AdapterPEP.wsdl")
public class AdapterPEPService {
   private static Log log = LogFactory.getLog(AdapterPEPService.class);

    /**
     * Given a request to check the access policy, this service will interface
     * with the PDP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The xacml response which contains the access decision
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest) {
        CheckPolicyResponseType checkPolicyResp = new CheckPolicyResponseType();

        AdapterPEPImpl adapterPEPImpl = new AdapterPEPImpl();

        try {
            checkPolicyResp = adapterPEPImpl.checkPolicy(checkPolicyRequest);
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterPEPImpl.checkPolicy.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return checkPolicyResp;
    }

}
