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
package gov.hhs.fha.nhinc.patientdiscovery._10.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.gateway.servlet.InitServlet;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;


/**
 *
 * @author shawc
 */
public class EntityPatientDiscoveryImpl extends BaseService {

    private Log log = null;

    public EntityPatientDiscoveryImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
        // create the orch impl and pass in references to the executor services
        return new EntityPatientDiscoveryOrchImpl(InitServlet.getExecutorService(),
                InitServlet.getLargeJobExecutorService());
    }

    protected PerformanceManager getPerformanceManager() {
        return PerformanceManager.getPerformanceManagerInstance();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
            RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context) {

        log.debug("Entering EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");

        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        if (request == null) {
            log.error("The incomming request was null.");
        } else if (context == null) {
            log.error("The incomming WebServiceContext parameter was null.");
            return null;
        } else {
            AssertionType assertion = extractAssertion(context);

            EntityPatientDiscoveryOrchImpl processor = getEntityPatientDiscoveryProcessor();
            if (processor != null) {
                response = processor.respondingGatewayPRPAIN201305UV02(request, assertion);
            } else {
                log.error("The EntityPatientDiscoveryProcessor was null.");
            }
        }

        log.debug("Exiting EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");
        return response;
    }

   

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
            RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {

        log.debug("Begin EntityPatientDiscoveryUnsecuredImpl.respondingGatewayPRPAIN201305UV02...");

        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        if (respondingGatewayPRPAIN201305UV02Request == null) {
            log.warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
        } else {
            EntityPatientDiscoveryOrchImpl processor = getEntityPatientDiscoveryProcessor();
            if (processor != null) {
                response = processor.respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request,
                        respondingGatewayPRPAIN201305UV02Request.getAssertion());
            } else {
                log.warn("EntityPatientDiscoveryProcessor was null.");
            }
        }

        log.debug("End EntityPatientDiscoveryUnsecuredImpl.respondingGatewayPRPAIN201305UV02...");

        return response;
    }

  
}
