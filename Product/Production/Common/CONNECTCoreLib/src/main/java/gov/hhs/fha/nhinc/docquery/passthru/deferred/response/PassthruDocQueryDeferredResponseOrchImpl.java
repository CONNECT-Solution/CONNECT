/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.passthru.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.response.proxy.NhinDocQueryDeferredResponseProxy;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.response.proxy.NhinDocQueryDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author jhoppesc
 */
public class PassthruDocQueryDeferredResponseOrchImpl {

    /**
     *
     * @param body
     * @param assertion
     * @param target
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse body, AssertionType assertion, NhinTargetSystemType target) {
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_STATUS_MSG);
        respAck.setMessage(regResp);
        // Requireed the responding home community id in the audit log
        String responseCommunityID = null;
        if (target != null &&
                target.getHomeCommunity() != null) {
            responseCommunityID = target.getHomeCommunity().getHomeCommunityId();
        }
        // Audit the outgoing NHIN Message
        AcknowledgementType ack = auditResponse(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        // Call the NHIN Interface
        NhinDocQueryDeferredResponseProxyObjectFactory factory = new NhinDocQueryDeferredResponseProxyObjectFactory();
        NhinDocQueryDeferredResponseProxy proxy = factory.getNhinDocQueryDeferredResponseProxy();
        respAck = proxy.respondingGatewayCrossGatewayQuery(body, assertion, target);

        // Audit the incoming NHIN Message
        ack = auditAck(respAck, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        return respAck;
    }

    private AcknowledgementType auditResponse(AdhocQueryResponse msg, AssertionType assertion, String direction, String _interface, String responseCommunityId) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion, direction, _interface, responseCommunityId);

        return ack;
    }

    private AcknowledgementType auditAck(DocQueryAcknowledgementType msg, AssertionType assertion, String direction, String _interface, String responseCommunityId) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.logDocQueryAck(msg, assertion, direction, _interface, responseCommunityId);

        return ack;
    }
}
