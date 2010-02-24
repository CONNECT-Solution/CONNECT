/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request;
import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredService;
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
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.xdr.XDRPolicyChecker;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author patlollav
 */
public class NhinXDRRequestImpl
{
    public static final String XDR_RESPONSE_SUCCESS = "Success";
    public static final String XDR_RESPONSE_FAILURE = "Failure";
    public static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed ";
    public static final String XDR_POLICY_ERROR_CONTEXT = "Policy Check Failed";
    private static AdapterXDRRequestSecuredService securedAdapterService = new AdapterXDRRequestSecuredService();

    private static final Log logger = LogFactory.getLog(NhinXDRRequestImpl.class);

    /**
     *
     * @param body
     * @param context
     * @return
     */
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body,WebServiceContext context ) {

        ihe.iti.xdr._2007.AcknowledgementType result;

        logger.debug("Entering NhinXDRRequestImpl.provideAndRegisterDocumentSetBRequest");

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        //XDRAuditLogger auditLogger = new XDRAuditLogger();

        //AcknowledgementType ack = auditLogger.auditNhinXDR(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        //log.debug("Audit Log Ack Message:" + ack.getMessage());

        String localHCID = "";

        try {
            localHCID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            logger.error(ex.getMessage());
        }

         if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID))
         {
             logger.debug("Policy Check Succeeded");
            result = forwardToAgency(body, assertion);
        }
        else
        {
             logger.error("Failed Policy Check");
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
    private ihe.iti.xdr._2007.AcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion)
    {
        logger.debug("begin forwardToAgency()");
        
        String url = "";
        ihe.iti.xdr._2007.AcknowledgementType response = null;
        
        url = getAdapterXDRRequestSecuredUrl();
                
        if (NullChecker.isNotNullish(url)) {
            AdapterXDRRequestSecuredPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADAPTER_XDRREQUEST_SECURED_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.provideAndRegisterDocumentSetBRequest(body);

        } else {
            logger.error("The URL for service: " + NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME + " is null");
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
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME);

        } catch (ConnectionManagerException ex) {
            logger.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME, ex);
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
    protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {

        boolean bPolicyOk = false;

        logger.debug("checking the policy engine for the new request to a target community");

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
    private AdapterXDRRequestSecuredPortType getPort(String url) {
        AdapterXDRRequestSecuredPortType port = securedAdapterService.getAdapterXDRRequestSecuredPortSoap12();

        logger.info("Setting endpoint address to Adapter XDR Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
