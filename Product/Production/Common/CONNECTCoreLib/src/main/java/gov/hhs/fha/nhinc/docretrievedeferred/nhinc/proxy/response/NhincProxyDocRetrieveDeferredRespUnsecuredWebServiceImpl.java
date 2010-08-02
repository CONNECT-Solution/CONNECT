package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrieveResponse;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrieveResponsePortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl implements NhincProxyDocRetrieveDeferredRespProxy
{
    private Log log = null;
    private boolean debugEnabled = false;
    private static NhincProxyDocRetrieveResponse service = null;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl()
    {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        service = getWebService();
    }

    /**
     * 
     * @return NhincProxyDocRetrieveResponse
     */
    private NhincProxyDocRetrieveResponse getWebService()
    {
        return new NhincProxyDocRetrieveResponse();
    }

    /**
     *
     * @return Log
     */
    private Log createLogger()
    {
        return (log != null)?log:LogFactory.getLog(this.getClass());
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
            log.debug("-- Begin NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl.crossGatewayRetrieveResponse(...) --");
        DocRetrieveAcknowledgementType ack = null;
        String url = getUrl();
        if (NullChecker.isNotNullish(url))
        {
            NhincProxyDocRetrieveResponsePortType port = getPort(url);
            RespondingGatewayCrossGatewayRetrieveResponseType resp = new RespondingGatewayCrossGatewayRetrieveResponseType();
            resp.setAssertion(assertion);
            resp.setNhinTargetSystem(target);
            resp.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ack = port.crossGatewayRetrieveResponse(resp);
        }
        if(debugEnabled)
            log.debug("-- End NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl.crossGatewayRetrieveResponse(...) --");
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
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_RESPONSE);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: '" + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_RESPONSE + "'");
            log.error(e.getMessage());
        }
        return url;
    }

    /**
     *
     * @param url
     * @return NhincProxyDocRetrieveResponsePortType
     */
    private NhincProxyDocRetrieveResponsePortType getPort(String url)
    {

        NhincProxyDocRetrieveResponsePortType port = service.getNhincProxyDocRetrieveResponsePortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
}
