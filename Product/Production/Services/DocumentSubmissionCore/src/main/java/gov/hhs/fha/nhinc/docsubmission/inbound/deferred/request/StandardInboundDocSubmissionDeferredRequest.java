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
package gov.hhs.fha.nhinc.docsubmission.inbound.deferred.request;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 *
 */
public class StandardInboundDocSubmissionDeferredRequest extends AbstractInboundDocSubmissionDeferredRequest {

    private static final Logger LOG = LoggerFactory.getLogger(StandardInboundDocSubmissionDeferredRequest.class);

    private XDRPolicyChecker policyChecker;
    private PropertyAccessor propertyAccessor;
    private AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory errorAdapterFactory;

    /**
     * Constructor.
     */
    public StandardInboundDocSubmissionDeferredRequest() {
        this(new AdapterDocSubmissionDeferredRequestProxyObjectFactory(), new XDRPolicyChecker(), PropertyAccessor
            .getInstance(), new DocSubmissionDeferredRequestAuditLogger(),
            new AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory());
    }

    /**
     * Constructor with dependency injection of strategy components.
     *
     * @param adapterFactory
     * @param policyChecker
     * @param propertyAccessor
     * @param auditLogger
     * @param errorAdapterFactory
     */
    public StandardInboundDocSubmissionDeferredRequest(
        AdapterDocSubmissionDeferredRequestProxyObjectFactory adapterFactory, XDRPolicyChecker policyChecker,
        PropertyAccessor propertyAccessor, DocSubmissionDeferredRequestAuditLogger auditLogger,
        AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory errorAdapterFactory) {
        super(adapterFactory, auditLogger);
        this.policyChecker = policyChecker;
        this.propertyAccessor = propertyAccessor;
        this.errorAdapterFactory = errorAdapterFactory;
    }

    @Override
    @InboundProcessingEvent(beforeBuilder = DocSubmissionBaseEventDescriptionBuilder.class,
    afterReturningBuilder = DocSubmissionArgTransformerBuilder.class,
    serviceType = "Document Submission Deferred Request", version = "")
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion, Properties webContextProperties) {

        XDRAcknowledgementType response = processDocSubmissionRequest(body, assertion);

        auditResponse(body, response, assertion, webContextProperties);

        return response;
    }

    @Override
    public XDRAcknowledgementType processDocSubmissionRequest(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion) {
        logInfoServiceProcess(this.getClass());

        XDRAcknowledgementType response;

        String localHCID = getLocalHCID();
        if (isPolicyValid(body, assertion, localHCID)) {
            try {
                LOG.debug("Policy Check Succeeded");
                getDocSubmissionUtils().convertDataToFileLocationIfEnabled(body);
                response = sendToAdapter(body, assertion);
            } catch (LargePayloadException lpe) {
                LOG.error("Failed to retrieve payload document. " + lpe.getLocalizedMessage(), lpe);
                response = MessageGeneratorUtils.getInstance().createXDRAckWithRegistryErrorResponse();
            }
        } else {
            LOG.error("Policy Check Failed");
            response = sendErrorToAdapter(body, assertion, "Policy Check Failed");
        }

        return response;
    }

    private String getLocalHCID() {
        String localHCID = null;
        try {
            localHCID = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Exception while retrieving home community, " + ex.getLocalizedMessage(), ex);
        }

        return localHCID;
    }

    private static boolean hasHomeCommunityId(AssertionType assertion) {
        return assertion != null && assertion.getHomeCommunity() != null
            && NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId());
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

    private XDRAcknowledgementType sendErrorToAdapter(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion, String errMsg) {

        AdapterDocSubmissionDeferredRequestErrorProxy proxy = errorAdapterFactory
            .getAdapterDocSubmissionDeferredRequestErrorProxy();
        return proxy.provideAndRegisterDocumentSetBRequestError(body, errMsg, assertion);
    }

    public DocSubmissionUtils getDocSubmissionUtils() {
        return DocSubmissionUtils.getInstance();
    }
}
