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
package gov.hhs.fha.nhinc.corex12.ds.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.request.entity.OutboundX12BatchRequestFactory;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.response.entity.OutboundX12BatchResponseFactory;
import gov.hhs.fha.nhinc.corex12.ds.realtime.entity.OutboundX12RealTimeFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AbstractOrchestrationContextFactory;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

/**
 *
 * @author cmay, svalluripalli
 */
public class OrchestrationContextFactory extends AbstractOrchestrationContextFactory {

    /**
     * Private constructor to prevent instantiation
     */
    private OrchestrationContextFactory() {
    }

    public static OrchestrationContextFactory getInstance() {
        return OrchestrationContextFactoryHolder.INSTANCE;
    }

    private static class OrchestrationContextFactoryHolder {

        public static final OrchestrationContextFactory INSTANCE = new OrchestrationContextFactory();
    }

    @Override
    public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType,
        NhincConstants.NHIN_SERVICE_NAMES serviceName) {

        NhinEndpointManager nem = new NhinEndpointManager();
        NhincConstants.GATEWAY_API_LEVEL apiLevel = nem.getApiVersion(homeCommunityType.getHomeCommunityId(),
            serviceName);

        return getBuilder(apiLevel, serviceName);
    }

    private OrchestrationContextBuilder getBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel,
        NhincConstants.NHIN_SERVICE_NAMES serviceName) {

        switch (serviceName) {
            case CORE_X12DS_GENERICBATCH_REQUEST:
                return OutboundX12BatchRequestFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
            case CORE_X12DS_GENERICBATCH_RESPONSE:
                return OutboundX12BatchResponseFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
            case CORE_X12DS_REALTIME:
                return OutboundX12RealTimeFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        }

        return null;
    }
}
