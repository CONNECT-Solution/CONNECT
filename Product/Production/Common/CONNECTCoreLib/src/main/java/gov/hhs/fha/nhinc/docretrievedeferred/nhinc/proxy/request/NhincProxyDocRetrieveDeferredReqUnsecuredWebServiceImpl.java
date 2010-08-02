package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocretrievedeferred.NhincProxyDocRetrieveDeferredRequest;
import gov.hhs.fha.nhinc.nhincproxydocretrievedeferred.NhincProxyDocRetrieveDeferredRequestPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl implements NhincProxyDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     * 
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnabled) {
            log.debug("-- Begin NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
            NhincProxyDocRetrieveDeferredRequestPortType port = getPort(url);
            RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
            req.setAssertion(assertion);
            req.setNhinTargetSystem(target);
            req.setRetrieveDocumentSetRequest(request);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ack = port.crossGatewayRetrieveRequest(req);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
            log.error(e.getMessage());
        }
        if (debugEnabled) {
            log.debug("-- End NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }

        return ack;
    }

    /**
     *
     * @param url
     * @return NhincProxyDocRetrieveDeferredRequestPortType
     */
    protected NhincProxyDocRetrieveDeferredRequestPortType getPort(String url) {
        NhincProxyDocRetrieveDeferredRequest service = new NhincProxyDocRetrieveDeferredRequest();
        NhincProxyDocRetrieveDeferredRequestPortType port = service.getNhincProxyDocRetrieveDeferredRequestPortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
    
}
