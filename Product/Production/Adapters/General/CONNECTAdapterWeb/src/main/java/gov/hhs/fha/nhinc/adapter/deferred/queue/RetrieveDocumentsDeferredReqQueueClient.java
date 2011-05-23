/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitydocretrievereqqueueprocess.EntityDocRetrieveDeferredReqQueueProcess;
import gov.hhs.fha.nhinc.entitydocretrievereqqueueprocess.EntityDocRetrieveDeferredReqQueueProcessPortType;
import gov.hhs.fha.nhinc.gateway.entitydocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitydocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.entitydocretrievereqqueueprocess.SuccessOrFailType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
public class RetrieveDocumentsDeferredReqQueueClient {

    private static final Log log = LogFactory.getLog(RetrieveDocumentsDeferredReqQueueClient.class);

    /**
     * Default constructor
     */
    public RetrieveDocumentsDeferredReqQueueClient() {
    }

    /**
     * Send queue process request for a deferred patient discovery queue record
     *
     * @param messageId
     * @return queue process response
     */
    public DocRetrieveDeferredReqQueueProcessResponseType processDocRetrieveDeferredReqQueue(String messageId) {

        EntityDocRetrieveDeferredReqQueueProcess service = new EntityDocRetrieveDeferredReqQueueProcess();
        String msgText = "";

        DocRetrieveDeferredReqQueueProcessResponseType response = new DocRetrieveDeferredReqQueueProcessResponseType();
        SuccessOrFailType sof = new SuccessOrFailType();
        sof.setSuccess(Boolean.FALSE);
        response.setSuccessOrFail(sof);

        try {
            String sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);

            String endpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunity, NhincConstants.ENTITY_DOC_RETRIEVE_DEFERRED_REQ_QUEUE_PROCESS_SERVICE_NAME);

            if (endpointURL != null && !endpointURL.isEmpty()) {
                EntityDocRetrieveDeferredReqQueueProcessPortType port = service.getEntityDocRetrieveDeferredReqQueueProcessPort();

                BindingProvider bp = (BindingProvider) port;
                // (Optional) Configure RequestContext with endpoint's URL
                Map<String, Object> rc = bp.getRequestContext();
                rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

                DocRetrieveDeferredReqQueueProcessRequestType request = new DocRetrieveDeferredReqQueueProcessRequestType();
                request.setMessageId(messageId);
                response = port.processDocRetrieveDeferredReqQueue(request);
            } else {
                msgText = "Endpoint URL not found for home community [" + sHomeCommunity + "] and service name [" + NhincConstants.ENTITY_DOC_RETRIEVE_DEFERRED_REQ_QUEUE_PROCESS_SERVICE_NAME + "]";
                log.error(msgText);
                response.setResponse(msgText);
            }
        } catch (PropertyAccessException ex) {
            msgText = "Exception accessing gateway property for home community";
            log.error(msgText, ex);
            response.setResponse(msgText);
        } catch (Exception ex) {
            msgText = "Exception occurred during deferred retrieve documents queue processing";
            log.error(msgText, ex);
            response.setResponse(msgText);
        }

        return response;
    }

}
