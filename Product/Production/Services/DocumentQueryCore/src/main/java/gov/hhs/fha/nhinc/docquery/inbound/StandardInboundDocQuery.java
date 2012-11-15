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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.MessageGeneratorUtils;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * @author akong
 * 
 */
public class StandardInboundDocQuery extends AbstractInboundDocQuery {

    private DocQueryPolicyChecker policyChecker = new DocQueryPolicyChecker();
    private PassthroughInboundDocQuery passthroughDQ = new PassthroughInboundDocQuery();

    public StandardInboundDocQuery() {
        super();
    }

    public StandardInboundDocQuery(DocQueryPolicyChecker policyChecker, PassthroughInboundDocQuery passthroughDQ,
            DocQueryAuditLog auditLogger) {
        this.policyChecker = policyChecker;
        this.passthroughDQ = passthroughDQ;
        this.auditLogger = auditLogger;
    }

    /**
     * Forwards the AdhocQueryRequest to this agency's adapter doc query service
     * 
     * @param adhocQueryRequestMsg
     * @param requestCommunityID
     * @return
     */
    @Override
    AdhocQueryResponse processDocQuery(AdhocQueryRequest msg, AssertionType assertion, String requestCommunityID) {
        AdhocQueryResponse resp = new AdhocQueryResponse();

        if (isPolicyValid(msg, assertion)) {
            resp = sendToAdapter(msg, assertion, requestCommunityID);
        } else {
            resp = MessageGeneratorUtils.getInstance().createPolicyErrorResponse();
        }

        return resp;
    }

    private boolean isPolicyValid(AdhocQueryRequest msg, AssertionType assertion) {
        return policyChecker.checkIncomingPolicy(msg, assertion);
    }

    private AdhocQueryResponse sendToAdapter(AdhocQueryRequest msg, AssertionType assertion, String requestCommunityID) {
        return passthroughDQ.processDocQuery(msg, assertion, requestCommunityID);
    }

}
