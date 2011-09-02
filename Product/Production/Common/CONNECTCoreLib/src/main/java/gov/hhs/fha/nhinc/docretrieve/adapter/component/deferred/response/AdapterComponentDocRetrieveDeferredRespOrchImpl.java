/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docrepository.DocumentProcessHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.document.DocRetrieveAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.sql.Timestamp;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Ralph Saunders
 */
public class AdapterComponentDocRetrieveDeferredRespOrchImpl {

    private Log log = null;

    public AdapterComponentDocRetrieveDeferredRespOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected DocumentProcessHelper getDocumentProcessHelper() {
        return new DocumentProcessHelper();
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetResponseType body, AssertionType assertion) {
        log.debug("Begin - AdapterComponentDocRetrieveDeferredRespImpl.respondingGatewayCrossGatewayRetrieve()");

        // Log the start of the adapter performance record
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        Timestamp starttime = new Timestamp(System.currentTimeMillis());
        Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, "Deferred"+NhincConstants.DOC_RETRIEVE_SERVICE_NAME, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, homeCommunityId);

        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();

        if (DocumentProcessHelper.isDemoOperationModeEnabled()) {
            DocumentProcessHelper documentProcessHelper = getDocumentProcessHelper();

            // Demo mode enabled, process AdhocQueryResponse to save document metadata to the CONNECT default document repository
            RegistryResponseType responseType = documentProcessHelper.documentRepositoryProvideAndRegisterDocumentSet(body);
            if (responseType.getStatus().equals(DocumentProcessHelper.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS)) {
                // Set the ack success status of the deferred queue entry
                ack = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG, null, null);
            } else {
                String ackMsg = responseType.getStatus();

                // Set the error acknowledgement status
                // fatal error with deferred queue repository
                ack = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMsg);
            }
        } else {
            // Demo mode not enabled, send success acknowledgement by default with no other processing
            ack = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG, null, null);
        }

        // Log the end of the adapter performance record
        Timestamp stoptime = new Timestamp(System.currentTimeMillis());
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);

        log.debug("End - AdapterComponentDocRetrieveDeferredRespImpl.respondingGatewayCrossGatewayRetrieve()");

        return ack;
    }
}
