package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveResponseSecured;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveResponseSecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl implements NhincProxyDocRetrieveDeferredRespProxy
{
    private Log log = null;
    private boolean debugEnabled = false;
    private static NhincProxyDocRetrieveResponseSecured service = null;
    
    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl()
    {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        service = getWebService();
    }

    /**
     * 
     * @return NhincProxyDocRetrieveResponseSecured
     */
    private NhincProxyDocRetrieveResponseSecured getWebService()
    {
        return new NhincProxyDocRetrieveResponseSecured();
    }
    
    /**
     * Creates logger instance
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }
    
    /**
     * 
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target) {
        if(debugEnabled)
            log.debug("-- Begin NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse() --");
        DocRetrieveAcknowledgementType ack = null;
        String url = getUrl();
        if (NullChecker.isNotNullish(url))
        {
            NhincProxyDocRetrieveResponseSecuredPortType port = getPort(url);
            RespondingGatewayCrossGatewayRetrieveSecuredResponseType resp = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
            resp.setNhinTargetSystem(target);
            resp.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            ack = port.crossGatewayRetrieveResponse(resp);
        }
        if(debugEnabled)
            log.debug("-- End NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse() --");
        return ack;
    }

    /**
     * 
     * @return String
     */
    private String getUrl()
    {
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: '" + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE + "'");
            log.error(e.getMessage());
        }
        return url;
    }

    /**
     * 
     * @param url
     * @return NhincProxyDocRetrieveResponseSecuredPortType
     */
    private NhincProxyDocRetrieveResponseSecuredPortType getPort(String url)
    {
        
        NhincProxyDocRetrieveResponseSecuredPortType port = service.getNhincProxyDocRetrieveResponseSecuredPortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }

}
