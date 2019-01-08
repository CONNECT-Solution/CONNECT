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
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper methods for PD Processing to create a new cumulativeResponse object for a particular spec level and to
 * transform an individualResponse object from one spec to another
 *
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryProcessorHelper {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundPatientDiscoveryProcessorHelper.class);

    /**
     * constructs a new OutboundPatientDiscoveryOrchestratable object with associated new cumulativeResponse
     *
     * @param request
     * @return OutboundPatientDiscoveryOrchestratable
     */
    public static OutboundPatientDiscoveryOrchestratable createNewCumulativeResponse(
        OutboundPatientDiscoveryOrchestratable request) {

        OutboundPatientDiscoveryOrchestratable cumulativeResponse = new OutboundPatientDiscoveryOrchestratable(null,
            Optional.<OutboundResponseProcessor>absent(), null, request.getAssertion(),
            request.getServiceName(), request.getTarget(), request.getRequest());

        // create new cumulativeResponse object
        RespondingGatewayPRPAIN201306UV02ResponseType newResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        cumulativeResponse.setCumulativeResponse(newResponse);
        LOG.debug("EntityPatientDiscoveryProcessorHelper constructed initial cumulativeResponse");
        return cumulativeResponse;
    }
}
