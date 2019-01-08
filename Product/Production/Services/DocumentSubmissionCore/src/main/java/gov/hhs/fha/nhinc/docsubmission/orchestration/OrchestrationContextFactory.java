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
package gov.hhs.fha.nhinc.docsubmission.orchestration;

import org.apache.commons.lang.StringUtils;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.connectmgr.UddiSpecVersionRegistry;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionFactory;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestFactory;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.orchestration.AbstractOrchestrationContextFactory;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

/**
 * @author zmelnick
 *
 */
public class OrchestrationContextFactory extends AbstractOrchestrationContextFactory {

    private static OrchestrationContextFactory INSTANCE = new OrchestrationContextFactory();

    private OrchestrationContextFactory() {
    }

    public static OrchestrationContextFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType,
            NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        NhinEndpointManager nem = new NhinEndpointManager();
        NhincConstants.GATEWAY_API_LEVEL apiLevel = nem.getApiVersion(homeCommunityType.getHomeCommunityId(),
                serviceName);
        return getBuilder(apiLevel, serviceName);
    }
    /**
     * Retrieve correct OrchestrationContextBuilder base on user spec and version
     * @param targets NhinTargetSystemType
     * @param serviceName NHIN_SERVICE_NAMES
     * @return OrchestrationContextBuilder
     */
    public OrchestrationContextBuilder getBuilder(NhinTargetSystemType targets,
            NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        final String userSpec = targets.getUseSpecVersion();
        if (StringUtils.isEmpty(userSpec)) {
            return getBuilder(targets.getHomeCommunity(), serviceName);
        } else {
            NhincConstants.GATEWAY_API_LEVEL apiLevel = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(
                    UDDI_SPEC_VERSION.fromString(userSpec), serviceName);
            return getBuilder(apiLevel, serviceName);
        }

    }

    private OrchestrationContextBuilder getBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel,
            NhincConstants.NHIN_SERVICE_NAMES serviceName) {

        switch (serviceName) {
        case DOCUMENT_SUBMISSION:
            return OutboundDocSubmissionFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        case DOCUMENT_SUBMISSION_DEFERRED_REQUEST:
            return OutboundDocSubmissionDeferredRequestFactory.getInstance()
                    .createOrchestrationContextBuilder(apiLevel);
        case DOCUMENT_SUBMISSION_DEFERRED_RESPONSE:
            return OutboundDocSubmissionDeferredResponseFactory.getInstance().createOrchestrationContextBuilder(
                    apiLevel);
        }

        return null;
    }

}
