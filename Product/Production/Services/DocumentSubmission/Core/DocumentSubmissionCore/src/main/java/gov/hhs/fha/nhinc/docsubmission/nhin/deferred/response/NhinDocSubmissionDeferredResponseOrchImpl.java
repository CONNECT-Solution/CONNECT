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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.NhinDocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

public class NhinDocSubmissionDeferredResponseOrchImpl {

    private static final Log logger = LogFactory.getLog(NhinDocSubmissionDeferredResponseOrchImpl.class);

    protected Log getLogger() {
        return logger;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body,
            AssertionType assertion) {
        XDRAcknowledgementType response = createAckResponse();

        auditRequestFromNhin(body, assertion);
        if (!(isInPassThroughMode())) {
            String localHCID = getLocalHCID();
            if (isPolicyValid(body, assertion, localHCID)) {
                getLogger().debug("Policy Check Succeeded");
                response = sendToAdapter(body, assertion);
            } else {
                getLogger().error("Policy Check Failed");
                response = createFailedPolicyCheckResponse();
            }
        } else {
            response = sendToAdapter(body, assertion);
        }

        auditResponseToNhin(response, assertion);

        return response;
    }

    private boolean isPolicyValid(RegistryResponseType request, AssertionType assertion, String receiverHCID) {
        if (!hasHomeCommunityId(assertion)) {
            getLogger().warn("Failed policy check.  Received assertion does not have a home community id.");
            return false;
        }

        String senderHCID = assertion.getHomeCommunity().getHomeCommunityId();

        XDRPolicyChecker policyChecker = getXDRPolicyChecker();
        return policyChecker.checkXDRResponsePolicy(request, assertion, senderHCID, receiverHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
    }

    protected XDRAcknowledgementType sendToAdapter(RegistryResponseType body, AssertionType assertion) {
        
        auditRequestToAdapter(body, assertion);

        AdapterDocSubmissionDeferredResponseProxy proxy = getAdapterDocSubmissionDeferredResponseProxy();
        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBResponse(body, assertion);

        auditResponseFromAdapter(response, assertion);
        return response;
    }

    private XDRAcknowledgementType createAckResponse() {
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_RESP_ACK_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResp);

        return response;
    }

    private XDRAcknowledgementType createFailedPolicyCheckResponse() {
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResponse);

        return response;
    }

    private void auditRequestFromNhin(RegistryResponseType body, AssertionType assertion) {
        getXDRAuditLogger().auditNhinXDRResponse(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }
    
    private void auditRequestToAdapter(RegistryResponseType body, AssertionType assertion) {
        getXDRAuditLogger().auditAdapterXDRResponse(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private void auditResponseToNhin(XDRAcknowledgementType response, AssertionType assertion) {
        getXDRAuditLogger().auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.XDR_RESPONSE_ACTION);
    }

    private void auditResponseFromAdapter(XDRAcknowledgementType response, AssertionType assertion) {
        getXDRAuditLogger().auditAdapterAcknowledgement(response, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION);
    }
    
    protected String getLocalHCID() {
        String localHCID = null;
        try {
            localHCID = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            logger.error("Exception while retrieving home community ID", ex);
        }

        return localHCID;
    }

    protected boolean hasHomeCommunityId(AssertionType assertion) {
        if (assertion != null && assertion.getHomeCommunity() != null
                && NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId())) {
            return true;
        }
        return false;
    }

    protected boolean isInPassThroughMode() {
        return getNhinDocSubmissionUtils().isInPassThroughMode(
                NhincConstants.DOC_SUBMISSION_DEFERRED_RESP_PASSTHRU_PROP);
    }

    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    protected XDRPolicyChecker getXDRPolicyChecker() {
        return new XDRPolicyChecker();
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    protected NhinDocSubmissionUtils getNhinDocSubmissionUtils() {
        return NhinDocSubmissionUtils.getInstance();
    }

    protected AdapterDocSubmissionDeferredResponseProxy getAdapterDocSubmissionDeferredResponseProxy() {
        return new AdapterDocSubmissionDeferredResponseProxyObjectFactory()
                .getAdapterDocSubmissionDeferredResponseProxy();
    }
}
