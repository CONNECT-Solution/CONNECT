/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public abstract class AbstractInboundDocQuery implements InboundDocQuery {

    public abstract AdhocQueryResponse processDocQuery(AdhocQueryRequest msg, AssertionType assertion, String hcid,
        Properties webContextProperties);
    protected DocQueryAuditLogger auditLogger;

    AbstractInboundDocQuery() {
        auditLogger = new DocQueryAuditLogger();
    }

    AbstractInboundDocQuery(DocQueryAuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    /**
     *
     * @param msg
     * @param assertion
     * @param webContextProperties
     * @return <code>AdhocQueryResponse</code>
     */
    @Override
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion,
        Properties webContextProperties) {
        String senderHcid = null;

        if (msg != null) {
            senderHcid = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        }

        AdhocQueryResponse resp = processDocQuery(msg, assertion, HomeCommunityMap.getLocalHomeCommunityId(),
            webContextProperties);

        auditResponseToNhin(msg, resp, assertion, senderHcid, webContextProperties);

        return resp;
    }

    protected void auditResponseToNhin(AdhocQueryRequest request, AdhocQueryResponse msg, AssertionType assertion,
        String requestCommunityID, Properties webContextProperties) {

        auditLogger.auditResponseMessage(request, msg, assertion, null,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.FALSE, webContextProperties, NhincConstants.DOC_QUERY_SERVICE_NAME);
    }
}
