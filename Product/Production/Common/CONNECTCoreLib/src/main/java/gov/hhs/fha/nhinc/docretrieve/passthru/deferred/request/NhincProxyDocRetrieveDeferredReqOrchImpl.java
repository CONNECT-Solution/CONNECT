/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy.NhinDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Proxy Orchestration Request implementation class
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredReqOrchImpl() {
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
            log.debug("Begin NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...)");
        }
        DocRetrieveAcknowledgementType ack = null;
        if (null != request || null != assertion) {
            // Audit request message
            DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
            try {
                auditLog.auditDocRetrieveDeferredRequest(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
                if (debugEnabled) {
                    log.debug("Creating NHIN doc retrieve proxy");
                }
                NhinDocRetrieveDeferredReqProxyObjectFactory objFactory = new NhinDocRetrieveDeferredReqProxyObjectFactory();
                NhinDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getNhinDocRetrieveDeferredRequestProxy();
                RespondingGatewayCrossGatewayRetrieveSecuredRequestType req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                req.setNhinTargetSystem(target);
                req.setRetrieveDocumentSetRequest(request);
                if (debugEnabled) {
                    log.debug("Calling NHIN doc retrieve proxy");
                }
                ack = docRetrieveProxy.sendToRespondingGateway(request, assertion, target);
            } catch (Exception ex) {
                log.error(ex);
                ack = createErrorAckResponse();
            }
            if (ack != null) {
                // Audit response message
                auditLog.auditDocRetrieveDeferredAckResponse(ack.getMessage(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
            }
        } else {
            log.error("Error in NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...): request/assertion are null");
            ack = createErrorAckResponse();
        }
        if (debugEnabled) {
            log.debug("End NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...)");
        }
        return ack;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType createErrorAckResponse() {
        log.error("Error occured sending doc retrieve deferred to NHIN target: ");
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        ack.setMessage(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        return ack;
    }
}
