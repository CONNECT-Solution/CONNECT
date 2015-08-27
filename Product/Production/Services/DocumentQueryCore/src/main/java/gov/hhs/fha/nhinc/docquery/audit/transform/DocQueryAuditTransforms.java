/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docquery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditTransformConstants;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author cmay
 */
public class DocQueryAuditTransforms extends AuditTransforms<AdhocQueryRequest, AdhocQueryResponse> {

    private static final Logger LOG = Logger.getLogger(DocQueryAuditTransforms.class);

    /**
     *
     * @param assertion
     * @param auditMsg
     * @return
     */
    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(AdhocQueryRequest request,
        AssertionType assertion, AuditMessageType auditMsg) {

        // TODO
        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(AdhocQueryResponse response,
        AssertionType assertion, AuditMessageType auditMsg) {

        // TODO

        return auditMsg;
    }

    @Override
    protected String getServiceEventIdCode() {
        return DocQueryAuditTransformConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return DocQueryAuditTransformConstants.EVENT_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return DocQueryAuditTransformConstants.EVENT_CODE_DISPLAY_REQUESTOR;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return DocQueryAuditTransformConstants.EVENT_CODE_DISPLAY_RESPONDER;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return DocQueryAuditTransformConstants.EVENT_TYPE_CODE;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return DocQueryAuditTransformConstants.EVENT_TYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return DocQueryAuditTransformConstants.EVENT_TYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return DocQueryAuditTransformConstants.EVENT_ACTION_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return DocQueryAuditTransformConstants.EVENT_ACTION_CODE_RESPONDER;
    }
}
