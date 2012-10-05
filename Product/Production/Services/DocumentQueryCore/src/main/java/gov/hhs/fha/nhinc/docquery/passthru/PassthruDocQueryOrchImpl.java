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
package gov.hhs.fha.nhinc.docquery.passthru;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryDelegate;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryOrchestratable;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

/**
 *
 * @author JHOPPESC
 * @paul.eftis updated exception handling to return DQ error response with error/exception detail within DQ response.
 */
public class PassthruDocQueryOrchImpl {

    private static Log log = LogFactory.getLog(PassthruDocQueryOrchImpl.class);

    /**
     *
     * @param body
     * @param assertion
     * @param target
     * @return <code>AdhocQueryResponse</code>
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, AssertionType assertion,
            NhinTargetSystemType target) {
        log.debug("Entering NhincProxyDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        AdhocQueryResponse response = null;

        // The responding home community id is required in the audit log
        String responseCommunityID = null;
        if (target != null && target.getHomeCommunity() != null) {
            responseCommunityID = target.getHomeCommunity().getHomeCommunityId();
        }
        log.debug("=====>>>>> responseCommunityID is " + responseCommunityID);
        // Audit the Document Query Request Message sent on the Nhin Interface
        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        auditLog.auditDQRequest(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        try {
            log.debug("Creating NhinDocQueryProxy");

            OutboundDocQueryDelegate delegate = new OutboundDocQueryDelegate();
            OutboundDocQueryOrchestratable orchestratable = new OutboundDocQueryOrchestratable(delegate, null, null,
                    null, assertion, NhincConstants.DOC_QUERY_SERVICE_NAME, target, body);
            response = delegate.process(orchestratable).getResponse();
        } catch (Exception ex) {
            log.error("PassthruDocQueryOrchImpl Exception", ex);
            String err = ExecutorServiceHelper.getFormattedExceptionInfo(ex, target,
                    NhincConstants.DOC_QUERY_SERVICE_NAME);
            response = generateErrorResponse(target, err);
        }

        // Audit the Document Query Response Message received on the Nhin Interface
        AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
        auditMsg.setAdhocQueryResponse(response);
        auditMsg.setAssertion(assertion);
        auditLog.auditDQResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        log.debug("Leaving NhincProxyDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }

    private AdhocQueryResponse generateErrorResponse(NhinTargetSystemType target, String error) {
        AdhocQueryResponse adhocResponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        adhocResponse.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        RegistryError regErr = new RegistryError();
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setCodeContext("Error from target homeId=" + target.getHomeCommunity().getHomeCommunityId());
        regErr.setValue(error);
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        regErrList.getRegistryError().add(regErr);
        adhocResponse.setRegistryErrorList(regErrList);

        RegistryObjectListType regObjList = new RegistryObjectListType();
        adhocResponse.setRegistryObjectList(regObjList);

        return adhocResponse;
    }
}
