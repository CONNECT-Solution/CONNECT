package gov.hhs.fha.nhinc.docretrieve.entity.proxy.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredRequest;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredRequestPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Entity Doc Retrieve Deferred Request unsecured webservice implementation call
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqUnsecuredWebServiceImpl implements EntityDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean enableDebug = false;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredReqUnsecuredWebServiceImpl() {
        log = createLogger();
        enableDebug = log.isDebugEnabled();
    }

    /**
     * Creating logger instance
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (enableDebug) {
            log.debug("Begin unsecure implementation of Entity Doc retrieve Request unsecured");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
        } catch (Exception e) {
            log.error("Unable to retrieve endpoint for service name '" + NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST + "' " + e.getMessage());
        }

        EntityDocRetrieveDeferredRequestPortType port = getPort(url, assertion);
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        request.setAssertion(assertion);
        request.setNhinTargetCommunities(target);
        request.setRetrieveDocumentSetRequest(message);
        ack = port.crossGatewayRetrieveRequest(request);
        if (enableDebug) {
            log.debug("End unsecure implementation of Entity Doc retrieve Request unsecured");
        }
        return ack;
    }

    /**
     * 
     * @param url
     * @param assertion
     * @return EntityDocRetrieveDeferredRequestPortType
     */
    protected EntityDocRetrieveDeferredRequestPortType getPort(String url, AssertionType assertion) {
        EntityDocRetrieveDeferredRequest service = new EntityDocRetrieveDeferredRequest();
        EntityDocRetrieveDeferredRequestPortType port = service.getEntityDocRetrieveDeferredRequestPortSoap();
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
