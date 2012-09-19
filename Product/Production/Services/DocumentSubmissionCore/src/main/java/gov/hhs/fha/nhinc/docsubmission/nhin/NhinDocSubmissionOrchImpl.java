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
package gov.hhs.fha.nhinc.docsubmission.nhin;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

public class NhinDocSubmissionOrchImpl {

    private static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed ";
    private static final String XDR_POLICY_ERROR_CONTEXT = "Policy Check Failed";
    private static Log log = LogFactory.getLog(NhinDocSubmissionOrchImpl.class);
    private XDRAuditLogger auditLogger = null;

    /**
     * Default Constructor
     */
    public NhinDocSubmissionOrchImpl() {
        log = getLogger();
        auditLogger = getXDRAuditLogger();
    }

    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
            ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        RegistryResponseType response = null;

        auditRequestFromNhin(body, assertion);

        String localHCID = getLocalHCID();
        if (isPolicyValid(body, assertion, localHCID)) {
            log.debug("Policy Check Succeeded");
            response = sendToAdapter(body, assertion);
        } else {
            log.error("Failed policy check.  Sending error response.");
            response = createFailedPolicyCheckResponse();
        }

        auditResponseToNhin(response, assertion);

        return response;
    }

    /**
     * @return the logging object
     */
    protected Log getLogger() {
        return log;
    }

    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    protected XDRPolicyChecker getXDRPolicyChecker() {
        return new XDRPolicyChecker();
    }

    protected AdapterDocSubmissionProxy getAdapterDocSubmissionProxy() {
        return new AdapterDocSubmissionProxyObjectFactory().getAdapterDocSubmissionProxy();
    }

    protected boolean hasHomeCommunityId(AssertionType assertion) {
        if (assertion != null && assertion.getHomeCommunity() != null
                && NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId())) {
            return true;
        }
        return false;
    }

    private boolean isPolicyValid(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion,
            String receiverHCID) {

        if (!hasHomeCommunityId(assertion)) {
            log.warn("Failed policy check.  Received assertion does not have a home community id.");
            return false;
        }

        String senderHCID = assertion.getHomeCommunity().getHomeCommunityId();

        XDRPolicyChecker policyChecker = getXDRPolicyChecker();
        return policyChecker.checkXDRRequestPolicy(newRequest, assertion, senderHCID, receiverHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
    }

    private RegistryResponseType sendToAdapter(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {

        auditRequestToAdapter(request, assertion);

        AdapterDocSubmissionProxy proxy = getAdapterDocSubmissionProxy();
        RegistryResponseType response = proxy.provideAndRegisterDocumentSetB(request, assertion);

        auditResponseFromAdapter(response, assertion);

        return response;
    }

    private String getLocalHCID() {
        String localHCID = "";
        try {
            localHCID = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Failed to retrieve local HCID from properties file", ex);
        }

        return localHCID;
    }

    private void auditRequestFromNhin(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        auditLogger.auditNhinXDR(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private void auditResponseToNhin(RegistryResponseType response, AssertionType assertion) {
        auditLogger.auditNhinXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    private void auditRequestToAdapter(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        auditLogger.auditAdapterXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    private void auditResponseFromAdapter(RegistryResponseType response, AssertionType assertion) {
        auditLogger.auditAdapterXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private RegistryResponseType createFailedPolicyCheckResponse() {
        RegistryResponseType result = new RegistryResponseType();
        result.setRegistryErrorList(new RegistryErrorList());

        RegistryError policyError = new RegistryError();
        policyError.setErrorCode(XDR_POLICY_ERROR);
        policyError.setCodeContext(XDR_POLICY_ERROR_CONTEXT);
        policyError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

        result.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_FAILURE);
        result.getRegistryErrorList().getRegistryError().add(policyError);

        return result;
    }

}
