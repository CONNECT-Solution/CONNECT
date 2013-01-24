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
package gov.hhs.fha.nhinc.docsubmission.inbound;

import org.apache.log4j.Logger;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author akong
 * 
 */
public class StandardInboundDocSubmission extends AbstractInboundDocSubmission {

    private static final Logger LOG = Logger.getLogger(StandardInboundDocSubmission.class);
    private MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    private PropertyAccessor propertyAccessor;
    private XDRPolicyChecker policyChecker;

    /**
     * Constructor.
     */
    public StandardInboundDocSubmission() {
        this(new AdapterDocSubmissionProxyObjectFactory(), new XDRPolicyChecker(), PropertyAccessor.getInstance(),
                new XDRAuditLogger());
    }

    /**
     * Constructor with dependency injection of strategy components.
     * 
     * @param adapterFactory
     * @param policyChecker
     * @param propertyAccessor
     * @param auditLogger
     */
    public StandardInboundDocSubmission(AdapterDocSubmissionProxyObjectFactory adapterFactory,
            XDRPolicyChecker policyChecker,
            PropertyAccessor propertyAccessor, XDRAuditLogger auditLogger) {
        super(adapterFactory, auditLogger);
        this.policyChecker = policyChecker;
        this.propertyAccessor = propertyAccessor;
    }

    @Override
    RegistryResponseType processDocSubmission(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        RegistryResponseType response = null;
        
        String localHCID = getLocalHCID();
        if (isPolicyValid(body, assertion, localHCID)) {
            auditRequestToAdapter(body, assertion);
            
            response = sendToAdapter(body, assertion);
        } else {
            LOG.error("Failed policy check.  Sending error response.");
            response = msgUtils.createFailedPolicyCheckResponse();
        }
        
        auditResponseFromAdapter(response, assertion);

        return response;
    }

    private boolean isPolicyValid(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
            String receiverHCID) {

        if (!hasHomeCommunityId(assertion)) {
            LOG.warn("Failed policy check.  Received assertion does not have a home community id.");
            return false;
        }

        String senderHCID = assertion.getHomeCommunity().getHomeCommunityId();

        return policyChecker.checkXDRRequestPolicy(request, assertion, senderHCID, receiverHCID,
                NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
    }

    private boolean hasHomeCommunityId(AssertionType assertion) {
        if (assertion != null && assertion.getHomeCommunity() != null
                && NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId())) {
            return true;
        }
        return false;
    }

    private String getLocalHCID() {
        String localHCID = "";
        try {
            localHCID = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Failed to retrieve local HCID from properties file", ex);
        }

        return localHCID;
    }
    
}
