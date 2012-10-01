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
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;

import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * Implements the PatientDiscovery strategy for spec g0 endpoint
 * 
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryStrategyImpl extends OutboundPatientDiscoveryStrategy {

    private static Log log = LogFactory.getLog(OutboundPatientDiscoveryStrategyImpl.class);

    public OutboundPatientDiscoveryStrategyImpl() {

    }

    private Log getLogger() {
        return log;
    }

    /**
     * @param message contains request message to execute
     */
    @Override
    public void execute(OutboundPatientDiscoveryOrchestratable message) {
        if (message instanceof OutboundPatientDiscoveryOrchestratable) {
            executeStrategy((OutboundPatientDiscoveryOrchestratable) message);
        } else {
            // shouldn't get here
            getLogger().error("message was not an OutboundPatientDiscoveryOrchestratable");
        }
    }

    public void executeStrategy(OutboundPatientDiscoveryOrchestratable message) {
        getLogger().debug("begin executeStrategy");
        auditRequestMessage(message.getRequest(), message.getAssertion(), message.getTarget().getHomeCommunity()
                .getHomeCommunityId());
        try {
            NhinPatientDiscoveryProxy proxy = new NhinPatientDiscoveryProxyObjectFactory()
                    .getNhinPatientDiscoveryProxy();
            String url = (new WebServiceProxyHelper()).getUrlFromTargetSystemByGatewayAPILevel(
                    message.getTarget(), NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                    GATEWAY_API_LEVEL.LEVEL_g0);
            message.getTarget().setUrl(url);
            getLogger().debug(
                    "executeStrategy sending nhin patient discovery request to "
                            + " target hcid=" + message.getTarget().getHomeCommunity().getHomeCommunityId() 
                            + " at url=" + url);
            message.setResponse(proxy.respondingGatewayPRPAIN201305UV02(message.getRequest(), message.getAssertion(),
                    message.getTarget()));
            getLogger().debug("executeStrategy returning response");
        } catch (Exception ex) {
            PRPAIN201306UV02 response = new HL7PRPA201306Transforms().createPRPA201306ForErrors(message.getRequest(),
                 NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE, ex.getMessage());
            message.setResponse(response);
            getLogger().debug("executeStrategy returning error response");
        }
        auditResponseMessage(message.getResponse(), message.getAssertion(), message.getTarget().getHomeCommunity()
                .getHomeCommunityId());

    }

}
