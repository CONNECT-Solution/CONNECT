/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docdatasubmission.inbound;

import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_PROPERTY_FILE;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.HOME_COMMUNITY_ID_PROPERTY;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.POLICYENGINE_INBOUND_DIRECTION;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docdatasubmission.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docdatasubmission.XDSPolicyChecker;
import gov.hhs.fha.nhinc.docdatasubmission.adapter.proxy.AdapterDocDataSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docdatasubmission.aspect.DocDataSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docdatasubmission.audit.DocDataSubmissionAuditLogger;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RegisterDocumentSetRequestType;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardInboundDocDataSubmission extends AbstractInboundDocDataSubmission {

    private static final Logger LOG = LoggerFactory.getLogger(StandardInboundDocDataSubmission.class);
    private MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    private PropertyAccessor propertyAccessor;
    private XDSPolicyChecker policyChecker;

    /**
     * Constructor.
     */
    public StandardInboundDocDataSubmission() {
        this(new AdapterDocDataSubmissionProxyObjectFactory(), new XDSPolicyChecker(), PropertyAccessor.getInstance(),
            new DocDataSubmissionAuditLogger());
    }
    /**
     * Constructor with dependency injection of strategy components.
     * @param adapterFactory
     * @param policyChecker
     * @param propertyAccessor
     * @param auditLogger
     */
    public StandardInboundDocDataSubmission(AdapterDocDataSubmissionProxyObjectFactory adapterFactory,
        XDSPolicyChecker policyChecker, PropertyAccessor propertyAccessor, DocDataSubmissionAuditLogger auditLogger) {
        super(adapterFactory, auditLogger);
        this.policyChecker = policyChecker;
        this.propertyAccessor = propertyAccessor;
    }


    @Override
    @InboundProcessingEvent(beforeBuilder = DocDataSubmissionBaseEventDescriptionBuilder.class,
    afterReturningBuilder = DocDataSubmissionBaseEventDescriptionBuilder.class, serviceType = "Document Data Submission",
    version = "")
    public RegistryResponseType documentRepositoryRegisterDocumentSetB(RegisterDocumentSetRequestType body, AssertionType assertion, Properties webContextProperties) {

        RegistryResponseType response = processDocDataSubmission(body, assertion, webContextProperties);

        auditResponse(body, response, assertion, webContextProperties);

        return response;
    }

    @Override
    public RegistryResponseType processDocDataSubmission(RegisterDocumentSetRequestType body, AssertionType assertion,
        Properties webContextProperties) {
        RegistryResponseType response;

        String localHCID = getLocalHCID();
        if (isPolicyValid(body, assertion, localHCID)) {
            response = sendToAdapter(body, assertion);
        } else {
            LOG.error("Failed policy check.  Sending error response.");
            response = msgUtils.createFailedPolicyCheckResponse();
        }

        return response;
    }

    private boolean isPolicyValid(RegisterDocumentSetRequestType request, AssertionType assertion,
        String receiverHCID) {

        if (!hasHomeCommunityId(assertion)) {
            LOG.warn("Failed policy check.  Received assertion does not have a home community id.");
            return false;
        }

        String senderHCID = assertion.getHomeCommunity().getHomeCommunityId();

        return policyChecker.checkXDRRequestPolicy(request, assertion, senderHCID, receiverHCID,
            POLICYENGINE_INBOUND_DIRECTION);
    }

    private boolean hasHomeCommunityId(AssertionType assertion) {
        if (assertion != null && assertion.getHomeCommunity() != null
            && StringUtils.isNotBlank(assertion.getHomeCommunity().getHomeCommunityId())) {
            return true;
        }
        return false;
    }

    private String getLocalHCID() {
        String localHCID = "";
        try {
            localHCID = propertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Failed to retrieve local HCID from properties file", ex);
        }

        return localHCID;
    }

}
