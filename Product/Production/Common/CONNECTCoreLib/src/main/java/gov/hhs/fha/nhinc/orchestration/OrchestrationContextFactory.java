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
package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionFactory;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryFactory;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryFactory;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveFactory;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionFactory;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestFactory;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

public class OrchestrationContextFactory {

    private static OrchestrationContextFactory INSTANCE = new OrchestrationContextFactory();

    private OrchestrationContextFactory() {
    }

    public static OrchestrationContextFactory getInstance() {
        return INSTANCE;
    }

    public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType, String serviceName) {
        NhincConstants.GATEWAY_API_LEVEL apiLevel = ConnectionManagerCache.getInstance().getApiVersion(
                homeCommunityType.getHomeCommunityId(), serviceName);
        return getBuilder(apiLevel, serviceName);
    }
    
    private OrchestrationContextBuilder getBuilder(
            NhincConstants.GATEWAY_API_LEVEL apiLevel, String serviceName){

        if(NhincConstants.DOC_RETRIEVE_SERVICE_NAME.equals(serviceName)){
            return OutboundDocRetrieveFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.ADAPTER_DOC_RETRIEVE_SERVICE_NAME.equals(serviceName)){
            return InboundDocRetrieveFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.ADMIN_DIST_SERVICE_NAME.equals(serviceName)){
            return OutboundAdminDistributionFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.DOC_QUERY_SERVICE_NAME.equalsIgnoreCase(serviceName)){
            return OutboundDocQueryFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME.equalsIgnoreCase(serviceName)){
            return OutboundPatientDiscoveryFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        } else if (NhincConstants.NHINC_XDR_SERVICE_NAME.equals(serviceName)) {
            return OutboundDocSubmissionFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        } else if (NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME.equals(serviceName)) {
            return OutboundDocSubmissionDeferredRequestFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        } else if (NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME.equals(serviceName)) {
            return OutboundDocSubmissionDeferredResponseFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        }
        return null;
	}
}
