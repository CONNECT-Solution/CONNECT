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
package gov.hhs.fha.nhinc.patientdiscovery.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the AdapterPatientDiscovery (Secured and unsecured) web service.
 *
 * @author jhoppesc, Les Westberg
 */
public class AdapterPatientDiscoveryImpl extends BaseService {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryImpl.class);

    /**
     * This method is called by the secure and unsecure Adapter Secure and unsecure web service. It calls the
     * orchestration code.
     *
     * @param bIsSecure TRUE if being called via secure web service.
     * @param request The message payload.
     * @param context The web service context.
     * @return Returns the response from the Adapter patient discovery request.
     */
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(Boolean bIsSecure,
            RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context) {
        LOG.debug("Entering AdapterPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        AssertionType assertion;
        if ((bIsSecure) && (context != null)) {
            assertion = extractAssertion(context);
        } else {
            assertion = new AssertionType();
        }

        AdapterPatientDiscoveryOrchImpl oOrchestrator = new AdapterPatientDiscoveryOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.respondingGatewayPRPAIN201305UV02(request, assertion);

        // Send response back to the initiating Gateway
        LOG.debug("Exiting AdapterPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;
    }
}
