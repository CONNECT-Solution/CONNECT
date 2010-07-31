package gov.hhs.fha.nhinc.docretrievedeferred.entity.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredRequestSecured;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredRequestSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqSecuredWebServiceImpl implements EntityDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean enableDebug = false;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredReqSecuredWebServiceImpl() {
        log = createLogger();
        enableDebug = log.isDebugEnabled();
    }

    /**
     * create logger instance
     * @return Log
     */
    protected Log createLogger() {
        return log = (log != null) ? log : LogFactory.getLog(this.getClass());
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
            log.debug("Begin EntityDocRetrieveDeferredReqSecuredWebServiceImpl.crossGatewayRetrieveRequest ");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST);
            EntityDocRetrieveDeferredRequestSecuredPortType port = getPort(url);
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType request = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
            request.setNhinTargetCommunities(target);
            request.setRetrieveDocumentSetRequest(message);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            ack = port.crossGatewayRetrieveRequest(request);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: '" + NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST + "' ");
            log.error(e.getMessage());
        }
        if (enableDebug) {
            log.debug("End EntityDocRetrieveDeferredReqSecuredWebServiceImpl.crossGatewayRetrieveRequest ");
        }
        return ack;
    }

    /**
     * 
     * @param url
     * @return EntityDocRetrieveDeferredRequestSecuredPortType
     */
    protected EntityDocRetrieveDeferredRequestSecuredPortType getPort(String url) {
        EntityDocRetrieveDeferredRequestSecured service = new EntityDocRetrieveDeferredRequestSecured();
        EntityDocRetrieveDeferredRequestSecuredPortType port = service.getEntityDocRetrieveDeferredRequestSecuredPortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
}
