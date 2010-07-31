package gov.hhs.fha.nhinc.docretrievedeferred.entity.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredRequest;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredRequestPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Entity Doc Retrieve Deferred Request unsecured webservice implementation call
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqUnsecuredWebServiceImpl implements EntityDocRetrieveDeferredReqProxy
{

    private Log log = null;
    private boolean enableDebug = false;
    
    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredReqUnsecuredWebServiceImpl()
    {
        log = createLogger();
        enableDebug = log.isDebugEnabled();
    }

    /**
     * Creating logger instance
     * @return Log
     */
    protected Log createLogger()
    {
        return (log != null)?log:LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        if(enableDebug)
            log.debug("Begin unsecure implementation of Entity Doc retrieve Request unsecured");
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
        }
        catch(Exception e)
        {
            log.error("Unable to retrieve endpoint for service name '"+NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST+"' "+e.getMessage());
        }
        
        EntityDocRetrieveDeferredRequestPortType port = getPort(url);
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        request.setAssertion(assertion);
        request.setNhinTargetCommunities(target);
        request.setRetrieveDocumentSetRequest(message);
        ack = port.crossGatewayRetrieveRequest(request);
        if(enableDebug)
            log.debug("End unsecure implementation of Entity Doc retrieve Request unsecured");
        return ack;
    }

    /**
     * 
     * @param url
     * @return EntityDocRetrieveDeferredRequestPortType
     */
    protected EntityDocRetrieveDeferredRequestPortType getPort(String url)
    {
        EntityDocRetrieveDeferredRequest service = new EntityDocRetrieveDeferredRequest();
        EntityDocRetrieveDeferredRequestPortType port = service.getEntityDocRetrieveDeferredRequestPortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
}
