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
package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error.proxy.AdapterDocRetrieveDeferredReqErrorProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error.proxy.AdapterDocRetrieveDeferredReqErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy.AdapterDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.NhinDocRetrieveDeferred;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by User: ralph Date: Aug 2, 2010 Time: 1:50:15 PM
 */
public class NhinDocRetrieveDeferredReqOrchImpl extends NhinDocRetrieveDeferred {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferredReqOrchImpl.class);

    /**
     * 
     * @param body
     * @param context
     * @return <code>DocRetrieveAcknowledgementType</code>
     */
    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetRequestType body,
            AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();

        String errMsg = null;
        String requestCommunityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        auditLog.auditDocRetrieveDeferredRequest(body, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion, requestCommunityId);

        try {
            if (isServiceEnabled(NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_KEY)) {
                if (isInPassThroughMode(NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_PASSTHRU_PROPERTY)) {
                    response = sendDocRetrieveDeferredRequestToAgency(body, assertion, homeCommunityId);
                } else {
                    response = serviceDocRetrieveInternal(body, assertion, homeCommunityId);
                }
            } else {
                errMsg = "Doc retrieve deferred request service is not enabled";
                log.error(errMsg);
                response = sendToAgencyErrorInterface(body, assertion, errMsg, homeCommunityId);
            }
        } catch (Throwable t) {
            errMsg = "Error processing NHIN Doc Retrieve: " + t.getMessage();
            log.error("Error processing NHIN Doc Retrieve: " + t.getMessage(), t);

            response = sendToAgencyErrorInterface(body, assertion, errMsg, homeCommunityId);
        }

        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), body, null, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                requestCommunityId);

        return response;
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param homeCommunityId
     * @return <code>DocRetrieveAcknowledgementType</code>
     */
    private DocRetrieveAcknowledgementType serviceDocRetrieveInternal(RetrieveDocumentSetRequestType request,
            AssertionType assertion, String homeCommunityId) {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");

        DocRetrieveAcknowledgementType response = null;
        HomeCommunityType hcId = new HomeCommunityType();
        String errMsg = null;

        hcId.setHomeCommunityId(homeCommunityId);
        if (isPolicyValidForRequest(request, assertion, hcId)) {
            log.debug("Adapter doc retrieve deferred policy check successful");
            response = sendDocRetrieveDeferredRequestToAgency(request, assertion, homeCommunityId);
        } else {
            errMsg = "Policy Check Failed";
            log.error(errMsg);
            response = sendToAgencyErrorInterface(request, assertion, errMsg, homeCommunityId);
        }

        log.debug("End DocRetrieveImpl.serviceDocRetrieveInternal");

        return response;
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType sendDocRetrieveDeferredRequestToAgency(
            RetrieveDocumentSetRequestType request, AssertionType assertion, String homeCommunityId) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredReqProxy proxy;
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType body;

        log.debug("Begin DocRetrieveReqImpl.sendDocRetrieveToAgency");
        auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion, homeCommunityId);

        proxy = new AdapterDocRetrieveDeferredReqProxyObjectFactory().getAdapterDocRetrieveDeferredRequestProxy();
        body = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        body.setRetrieveDocumentSetRequest(request);

        response = proxy.sendToAdapter(request, assertion);

        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), request, null, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, homeCommunityId);

        log.debug("End DocRetrieveReqImpl.sendDocRetrieveToAgency");
        return response;
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType sendToAgencyErrorInterface(RetrieveDocumentSetRequestType request,
            AssertionType assertion, String errMsg, String homeCommunityId) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredReqErrorProxy proxy;

        log.debug("Begin DocRetrieveReqImpl.sendToAgencyErrorInterface");
        auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion, homeCommunityId);

        proxy = new AdapterDocRetrieveDeferredReqErrorProxyObjectFactory()
                .getAdapterDocRetrieveDeferredRequestErrorProxy();

        response = proxy.sendToAdapter(request, assertion, errMsg);

        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), request, null, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, homeCommunityId);

        log.debug("End DocRetrieveReqImpl.sendToAgencyErrorInterface");
        return response;
    }

    /**
     * 
     * @param request
     * @param direction
     * @param connectInterface
     * @param assertion
     */
    protected void auditDeferredRetrieveMessage(RetrieveDocumentSetRequestType request, String direction,
            String connectInterface, AssertionType assertion, String requestCommunityId) {
        DocRetrieveMessageType message = new DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface,
                requestCommunityId);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }
}
