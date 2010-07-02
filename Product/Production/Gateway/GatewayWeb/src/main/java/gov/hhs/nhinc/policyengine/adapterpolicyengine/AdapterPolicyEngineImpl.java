package gov.hhs.nhinc.policyengine.adapterpolicyengine;

import gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecured;
import gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import java.util.Map;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterPolicyEngineImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterPolicyEngineImpl.class);
    private static AdapterPolicyEngineSecured service = new AdapterPolicyEngineSecured();

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest)
    {
        log.debug("Begin AdapterPolicyEngineImpl.checkPolicy");
        CheckPolicyResponseType result = null;
        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.POLICYENGINE_SERVICE_SECURED_NAME);

            AdapterPolicyEngineSecuredPortType port = getPort(url);

            AssertionType assertIn = checkPolicyRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map samlMap = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.POLICY_ENGINE_ACTION);

            Map requestContext = ((BindingProvider) port).getRequestContext();
            requestContext.putAll(samlMap);

            gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType body = new gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType();
            body.setRequest(checkPolicyRequest.getRequest());
            log.debug("Calling secure policy engine service");
            result = port.checkPolicy(body);
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter policy engine secured service: " + ex.getMessage(), ex);
        }
        log.debug("End AdapterPolicyEngineImpl.checkPolicy");
        return result;
    }
    
    private AdapterPolicyEngineSecuredPortType getPort(String url) {
        AdapterPolicyEngineSecuredPortType port = service.getAdapterPolicyEngineSecuredPortSoap11();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
		return port;
    }
}
