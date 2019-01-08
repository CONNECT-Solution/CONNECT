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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy;

import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.AdapterPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the java implementation of the AdapterPatientDiscovery component proxy.
 *
 * @author Les westberg
 */
public class AdapterPatientDiscoveryProxyJavaImpl implements AdapterPatientDiscoveryProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryProxyJavaImpl.class);

    /**
     * This calls the java implementation for this method.
     *
     * @param body The message to be sent to the web service.
     * @param assertion The assertion information to go with the message.
     * @return The response from the web service.
     */

    @AdapterDelegationEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
            afterReturningBuilder = PRPAIN201306UV02EventDescriptionBuilder.class, serviceType = "Patient Discovery",
            version = "LEVEL_a0")
    @Override
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, AssertionType assertion) {
        LOG.debug("Entering AdapterPatientDiscoveryProxyJavaImpl.respondingGatewayPRPAIN201305UV02");
        AdapterPatientDiscoveryOrchImpl oOrchestrator = new AdapterPatientDiscoveryOrchImpl();
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        request.setAssertion(assertion);
        request.setPRPAIN201305UV02(body);
        request.setNhinTargetCommunities(null);
        LOG.debug("Leaving AdapterPatientDiscoveryProxyJavaImpl.respondingGatewayPRPAIN201305UV02");
        return oOrchestrator.respondingGatewayPRPAIN201305UV02(request, assertion);

    }
}
