/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.docquery.inbound;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public abstract class AbstractInboundDocQuery implements InboundDocQuery {

    abstract AdhocQueryResponse processDocQuery(AdhocQueryRequest msg, AssertionType assertion, String hcid);

    protected DocQueryAuditLog auditLogger;

    AbstractInboundDocQuery() {
        this.auditLogger = new DocQueryAuditLog();
    }
    
    AbstractInboundDocQuery(DocQueryAuditLog auditLogger) {
        this.auditLogger = auditLogger;
    }

    /**
     * 
     * @param body
     * @param assertion
     * @return <code>AdhocQueryResponse</code>
     */   
    @InboundProcessingEvent(beforeBuilder = AdhocQueryRequestDescriptionBuilder.class,
            afterReturningBuilder = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query",
            version = "")
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        String senderHcid = null;
        if (msg != null) {
            senderHcid = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        }

        auditRequestFromNhin(msg, assertion, senderHcid);

        AdhocQueryResponse resp = processDocQuery(msg, assertion, HomeCommunityMap.getLocalHomeCommunityId());

        auditResponseToNhin(resp, assertion, senderHcid);

        return resp;
    }

    private AcknowledgementType auditRequestFromNhin(AdhocQueryRequest msg, AssertionType assertion,
            String requestCommunityID) {
        AcknowledgementType ack = auditLogger
                .auditDQRequest(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);

        return ack;
    }

    private AcknowledgementType auditResponseToNhin(AdhocQueryResponse msg, AssertionType assertion,
            String requestCommunityID) {
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                requestCommunityID);

        return ack;
    }

}
