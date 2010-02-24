/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response;
import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.xdr.XDRPolicyChecker;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author patlollav
 */
public class NhinXDRResponseImpl
{
    public static final String XDR_RESPONSE_SUCCESS = "Success";
    public static final String XDR_RESPONSE_FAILURE = "Failure";
    public static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed ";
    public static final String XDR_POLICY_ERROR_CONTEXT = "Policy Check Failed";
    private static AdapterXDRResponseSecuredService securedAdapterService = new AdapterXDRResponseSecuredService();

    private static final Log log = LogFactory.getLog(NhinXDRResponseImpl.class);

    /**
     *
     * @param body
     * @param context
     * @return
     */
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body,WebServiceContext context ) {

        ihe.iti.xdr._2007.AcknowledgementType result;

        log.debug("Entering NhinXDRResponseImpl.provideAndRegisterDocumentSetBResponse");

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        //XDRAuditLogger auditLogger = new XDRAuditLogger();

        //AcknowledgementType ack = auditLogger.auditNhinXDR(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        //log.debug("Audit Log Ack Message:" + ack.getMessage());

        String localHCID = "";

        try {
            localHCID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }

         if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID))
         {
             log.debug("Policy Check Succeeded");
            result = forwardToAgency(body, assertion);
        }
        else
        {
             log.error("Failed Policy Check");
            result = createFailedPolicyCheckResponse();
        }
     

        //ack = auditLogger.auditNhinXDRResponse(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

     return result;

    }

    /**
     * 
     * @param body
     * @param context
     * @return
     */
    private ihe.iti.xdr._2007.AcknowledgementType forwardToAgency(RegistryResponseType body, AssertionType assertion)
    {
        log.debug("begin forwardToAgency()");
        
        String url = "";
        ihe.iti.xdr._2007.AcknowledgementType response = null;
        
        url = getAdapterXDRRequestSecuredUrl();
                
        if (NullChecker.isNotNullish(url)) {
            AdapterXDRResponseSecuredPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADAPTER_XDRRESPONSE_SECURED_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.provideAndRegisterDocumentSetBResponse(body);

        } else {
            log.error("The URL for service: " + NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME + " is null");
        }

        return response;
    }

    /**
     * 
     * @return
     */
    protected String getAdapterXDRRequestSecuredUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME);

        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME, ex);
        }

        return url;
    }

    /**
     *
     * @param newRequest
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @return
     */
    protected boolean isPolicyOk(RegistryResponseType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {

        boolean bPolicyOk = false;

        log.debug("checking the policy engine for the new request to a target community");

        //return true if 'permit' returned, false otherwise
        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        //return policyChecker.checkXDRRequestPolicy(newRequest, assertion,senderHCID ,receiverHCID);
        return true;

    }

    private ihe.iti.xdr._2007.AcknowledgementType createFailedPolicyCheckResponse()
    {
        ihe.iti.xdr._2007.AcknowledgementType result= new ihe.iti.xdr._2007.AcknowledgementType();

        return result;
    }

    /**
     * 
     * @param url
     * @return
     */
    private AdapterXDRResponseSecuredPortType getPort(String url) {
        AdapterXDRResponseSecuredPortType port = securedAdapterService.getAdapterXDRResponseSecuredPortSoap12();

        log.info("Setting endpoint address to Adapter XDR Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
