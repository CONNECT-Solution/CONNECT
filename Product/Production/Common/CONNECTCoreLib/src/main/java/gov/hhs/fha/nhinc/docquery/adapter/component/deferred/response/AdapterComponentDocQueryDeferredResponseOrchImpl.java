/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docrepository.DocumentProcessHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.document.DocQueryAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.sql.Timestamp;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredResponseOrchImpl {

    private static Log log = LogFactory.getLog(AdapterComponentDocQueryDeferredResponseOrchImpl.class);

    protected DocumentProcessHelper getDocumentProcessHelper() {
        return new DocumentProcessHelper();
    }

    /**
     * Return acknowledgement based on CONNECT Demo Operation Mode
     * 
     * @param msg
     * @param assertion
     * @return DocQueryAcknowledgementType
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion) {
        log.debug("Begin - AdapterComponentDocQueryDeferredResponseOrchImpl.respondingGatewayCrossGatewayQuery()");

        // Log the start of the adapter performance record
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        Timestamp starttime = new Timestamp(System.currentTimeMillis());
        Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, "Deferred"+NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, homeCommunityId);

        String uniquePatientId = "";
        DocQueryAcknowledgementType ack = new DocQueryAcknowledgementType();

        if (assertion != null &&
                assertion.getUniquePatientId() != null &&
                assertion.getUniquePatientId().size() > 0) {
            uniquePatientId = assertion.getUniquePatientId().get(0);
        }

        if (DocumentProcessHelper.isDemoOperationModeEnabled()) {
            DocumentProcessHelper documentProcessHelper = getDocumentProcessHelper();

            // Demo mode enabled, process AdhocQueryResponse to save document metadata to the CONNECT default document repository
            RegistryResponseType responseType = documentProcessHelper.documentRepositoryProvideAndRegisterDocumentSet(msg, uniquePatientId);
            if (responseType.getStatus().equals(DocumentProcessHelper.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS)) {
                // Set the ack success status of the deferred queue entry
                ack = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG, null, null);
            } else {
                String ackMsg = responseType.getStatus();

                // Set the error acknowledgement status
                // fatal error with deferred queue repository
                ack = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
            }
        } else {
            // Demo mode not enabled, send success acknowledgement by default with no other processing
            ack = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG, null, null);
        }

        // Log the end of the adapter performance record
        Timestamp stoptime = new Timestamp(System.currentTimeMillis());
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);

        log.debug("End - AdapterComponentDocQueryDeferredResponseOrchImpl.respondingGatewayCrossGatewayQuery()");

        return ack;
    }

}
