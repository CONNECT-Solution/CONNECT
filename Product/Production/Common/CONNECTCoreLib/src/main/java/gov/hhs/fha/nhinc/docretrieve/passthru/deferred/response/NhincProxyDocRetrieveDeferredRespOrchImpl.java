/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.response.proxy.NhinDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.response.proxy.NhinDocRetrieveDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredRespOrchImpl() {
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
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.processCrossGatewayRetrieveResponse(...)");
        }
        DocRetrieveAcknowledgementType ack = null;
        // Audit request message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredResponse(retrieveDocumentSetResponse,NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
        try {
            if (debugEnabled) {
                log.debug("Creating NHIN Proxy Component");
            }
            NhinDocRetrieveDeferredRespProxyObjectFactory objFactory = new NhinDocRetrieveDeferredRespProxyObjectFactory();
            NhinDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getNhinDocRetrieveDeferredResponseProxy();
            if (debugEnabled) {
                log.debug("Calling NHIN Proxy Component");
            }
            ack = docRetrieveProxy.sendToRespondingGateway(retrieveDocumentSetResponse, assertion, target);
        } catch (Exception ex) {
            log.error(ex);
            ack = createErrorAck();
        }
        if (ack != null) {
            // Audit response message
            auditLog.auditDocRetrieveDeferredAckResponse(ack.getMessage(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.processCrossGatewayRetrieveResponse(...)");
        }
        return ack;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType createErrorAck() {
        if (debugEnabled) {
            log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.createErrorAck(...)");
        }
        log.error("Error occured sending doc retrieve deferred to NHIN target:");
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        ack.setMessage(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.createErrorAck(...)");
        }
        return ack;
    }
}
