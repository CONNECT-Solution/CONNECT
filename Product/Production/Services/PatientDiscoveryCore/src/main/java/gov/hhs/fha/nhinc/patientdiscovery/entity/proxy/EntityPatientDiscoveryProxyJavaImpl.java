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
package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.StandardOutboundPatientDiscovery;

import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 * THIS CLASS IS NOT USED ANYMORE WITH NEW DQ FANOUT EXECUTOR SERVICE IMPL DO NOT USE THIS
 * 
 * @author paul.eftis
 */
public class EntityPatientDiscoveryProxyJavaImpl implements EntityPatientDiscoveryProxy {
    private static final Logger LOG = Logger.getLogger(EntityPatientDiscoveryProxyJavaImpl.class);

    protected StandardOutboundPatientDiscovery getEntityPatientDiscoveryProcessor() {
        return new StandardOutboundPatientDiscovery();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 pdRequest,
            AssertionType assertion, NhinTargetCommunitiesType targetCommunities) {
        LOG.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;
        StandardOutboundPatientDiscovery processor = getEntityPatientDiscoveryProcessor();
        if (processor == null) {
            LOG.warn("EntityPatientDiscoveryProcessor was null");
        } else {
            RespondingGatewayPRPAIN201305UV02RequestType processorRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
            processorRequest.setPRPAIN201305UV02(pdRequest);
            processorRequest.setAssertion(assertion);
            processorRequest.setNhinTargetCommunities(targetCommunities);
            response = processor.respondingGatewayPRPAIN201305UV02(processorRequest, assertion);
        }
        LOG.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }

}
