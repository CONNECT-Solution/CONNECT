/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import gov.hhs.fha.nhinc.nhincadapterxdrsecured.AdapterXDRSecuredPortType;
import gov.hhs.fha.nhinc.nhincadapterxdrsecured.AdapterXDRSecuredService;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author dunnek
 */
public class NhinXDRImpl
{
    public static final String XDR_RESPONSE_SUCCESS = "Success";
    public static final String XDR_RESPONSE_FAILURE = "Failure";
    public static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed ";
    public static final String XDR_POLICY_ERROR_CONTEXT = "Policy Check Failed";
    private static Log log = null;
    private static AdapterXDRSecuredService securedAdapterService = null;
    
    public NhinXDRImpl()
    {
        log = createLogger();
        securedAdapterService = createAdapterService();
    }
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body,WebServiceContext context ) {
     RegistryResponseType result;

     log.debug("Entering NhinXDRImpl.documentRepositoryProvideAndRegisterDocumentSetB");

     AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
     XDRAuditLogger auditLogger = new XDRAuditLogger();
     log.debug("Request object is nul = " + (body == null));
     AcknowledgementType ack = auditLogger.auditNhinXDR(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
     log.debug(ack.getMessage());
     String localHCID = "";
        try {
            localHCID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());

        }

     if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID))
     {
         log.debug("Policy Check Succeeded");
         result = forwardToAgency(body, context);
     }
     else
     {
         log.error("Failed Policy Check");
         result = createFailedPolicyCheckResponse();
     }
     

     ack = auditLogger.auditNhinXDRResponse(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

     return result;

    }

    private RegistryResponseType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body,WebServiceContext context)
    {
        log.debug("begin forwardToAgency()");
        
        String url = "";
        RegistryResponseType response = null;
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        
        
        url = getUrl();
                
        if (NullChecker.isNotNullish(url)) {
            AdapterXDRSecuredPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADAPTER_XDR_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.provideAndRegisterDocumentSetb(body);

        } else {
            log.error("The URL for service: " + NhincConstants.ADAPTER_XDR_SECURED_SERVICE_NAME + " is null");
        }

        return response;
    }

    protected String getUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_SECURED_SERVICE_NAME);
            log.error(ex.getMessage());
        }


        return url;
    }

    protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {
        boolean bPolicyOk = false;

        log.debug("checking the policy engine for the new request to a target community");

        //return true if 'permit' returned, false otherwise
        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        return policyChecker.checkXDRRequestPolicy(newRequest, assertion,senderHCID ,receiverHCID, NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

    }
    protected AdapterXDRSecuredService createAdapterService()
    {
        return new AdapterXDRSecuredService();
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private RegistryResponseType createPositiveAck()
    {
        RegistryResponseType result= new RegistryResponseType();

        result.setStatus(XDR_RESPONSE_SUCCESS);

        return result;
    }
    private RegistryResponseType createFailedPolicyCheckResponse()
    {
        RegistryResponseType result= new RegistryResponseType();
        result.setRegistryErrorList(new RegistryErrorList());

        RegistryError policyError = new RegistryError();
        policyError.setErrorCode(XDR_POLICY_ERROR);
        policyError.setCodeContext(XDR_POLICY_ERROR_CONTEXT);
        policyError.setSeverity("Error");
        
        result.setStatus(XDR_RESPONSE_FAILURE);
        result.getRegistryErrorList().getRegistryError().add(policyError);

        return result;
    }
    private AdapterXDRSecuredPortType getPort(String url) {
        AdapterXDRSecuredPortType port = securedAdapterService.getAdapterXDRSecuredPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
