package gov.hhs.fha.nhinc.docretrieve.entity.proxy.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredResponseSecured;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredResponseSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespSecuredWebServiceImpl implements EntityDocRetrieveDeferredRespProxy {

    private Log log = null;
    private boolean debugEnabled = false;
    private static EntityDocRetrieveDeferredResponseSecured service = null;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredRespSecuredWebServiceImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        service = getWebService();
    }

    /**
     *
     * @return EntityDocRetrieveDeferredResponseSecured
     */
    private EntityDocRetrieveDeferredResponseSecured getWebService() {
        return new EntityDocRetrieveDeferredResponseSecured();
    }

    /**
     *
     * @return Log
     */
    private Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     *
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("-- Begin EntityDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse(_... ) --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = getUrl();
        if (NullChecker.isNotNullish(url)) {
            EntityDocRetrieveDeferredResponseSecuredPortType port = getPort(url, assertion);
            RespondingGatewayCrossGatewayRetrieveSecuredResponseType resp = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
            resp.setNhinTargetCommunities(target);
            resp.setRetrieveDocumentSetResponse(response);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            ack = port.crossGatewayRetrieveResponse(resp);
        }
        if (debugEnabled) {
            log.debug("-- End EntityDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse(_... ) --");
        }
        return ack;
    }

    private String getUrl() {
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: '" + NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE + "'");
            log.error(e.getMessage());
        }
        return url;
    }

    /**
     *
     * @param url
     * @param assertion
     * @return EntityDocRetrieveDeferredResponseSecuredPortType
     */
    private EntityDocRetrieveDeferredResponseSecuredPortType getPort(String url, AssertionType assertion) {
        EntityDocRetrieveDeferredResponseSecuredPortType port = service.getEntityDocRetrieveDeferredResponseSecuredPortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);
        List<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForMessageId(assertion));

        return port;
    }
}
