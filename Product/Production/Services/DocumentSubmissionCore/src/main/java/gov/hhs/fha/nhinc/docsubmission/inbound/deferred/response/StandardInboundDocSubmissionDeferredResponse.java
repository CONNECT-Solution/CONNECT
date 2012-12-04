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
package gov.hhs.fha.nhinc.docsubmission.inbound.deferred.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author akong
 * 
 */
public class StandardInboundDocSubmissionDeferredResponse extends AbstractInboundDocSubmissionDeferredResponse {

    private Log log = LogFactory.getLog(StandardInboundDocSubmissionDeferredResponse.class);
    private PassthroughInboundDocSubmissionDeferredResponse passthroughDocSubmission = new PassthroughInboundDocSubmissionDeferredResponse();
    private XDRPolicyChecker policyChecker = new XDRPolicyChecker();
    private PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();
    private MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();

    public StandardInboundDocSubmissionDeferredResponse() {
        super();
    }

    public StandardInboundDocSubmissionDeferredResponse(
            PassthroughInboundDocSubmissionDeferredResponse passthroughDocSubmission, XDRPolicyChecker policyChecker,
            PropertyAccessor propertyAccessor, XDRAuditLogger auditLogger, Log log) {
        this.passthroughDocSubmission = passthroughDocSubmission;
        this.policyChecker = policyChecker;
        this.propertyAccessor = propertyAccessor;
        this.auditLogger = auditLogger;
        this.log = log;
    }

    @Override
    XDRAcknowledgementType processDocSubmissionResponse(RegistryResponseType body, AssertionType assertion) {
        XDRAcknowledgementType response;

        String localHCID = getLocalHCID();
        if (isPolicyValid(body, assertion, localHCID)) {
            log.debug("Policy Check Succeeded");
            response = passthroughDocSubmission.processDocSubmissionResponse(body, assertion);
        } else {
            log.error("Policy Check Failed");
            response = msgUtils.createFailedPolicyCheckXDRAcknowledgementType();
        }

        return response;
    }

    private boolean isPolicyValid(RegistryResponseType request, AssertionType assertion, String receiverHCID) {
        if (!hasHomeCommunityId(assertion)) {
            log.warn("Failed policy check.  Received assertion does not have a home community id.");
            return false;
        }

        String senderHCID = assertion.getHomeCommunity().getHomeCommunityId();

        return policyChecker.checkXDRResponsePolicy(request, assertion, senderHCID, receiverHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
    }

    private String getLocalHCID() {
        String localHCID = null;
        try {
            localHCID = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Exception while retrieving home community ID", ex);
        }

        return localHCID;
    }

    private boolean hasHomeCommunityId(AssertionType assertion) {
        if (assertion != null && assertion.getHomeCommunity() != null
                && NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId())) {
            return true;
        }
        return false;
    }

}
