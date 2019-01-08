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
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscoveryasyncresp.EntityPatientDiscoveryAsyncRespPortType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy.service.EntityPatientDiscoveryAsyncRespServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.WebServiceException;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl implements
        EntityPatientDiscoveryDeferredResponseProxy {

    private static final Logger LOG = LoggerFactory.getLogger(EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    @Override
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion,
            NhinTargetCommunitiesType target) {
        LOG.debug("Begin EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl.processPatientDiscoveryAsyncResp(...)");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        String serviceName = NhincConstants.PATIENT_DISCOVERY_ENTITY_ASYNC_RESP_SERVICE_NAME;

        try {
            LOG.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("After target system URL look up. URL for service: {} is: {}",serviceName, url);
            }

            if (NullChecker.isNotNullish(url)) {
                RespondingGatewayPRPAIN201306UV02RequestType wsRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
                if (request != null) {
                    wsRequest.setPRPAIN201306UV02(request);
                    wsRequest.setAssertion(assertion);
                    wsRequest.setNhinTargetCommunities(target);
                }
                ServicePortDescriptor<EntityPatientDiscoveryAsyncRespPortType> portDescriptor = new EntityPatientDiscoveryAsyncRespServicePortDescriptor();
                CONNECTClient<EntityPatientDiscoveryAsyncRespPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);
                response = (MCCIIN000002UV01) client.invokePort(EntityPatientDiscoveryAsyncRespPortType.class,
                        "processPatientDiscoveryAsyncResp", wsRequest);
            } else {
                throw new WebServiceException("Could not determine URL for Patient Discovery Deferred Response endpoint");
            }
        } catch (Exception ex) {
            throw new ErrorEventException(ex, "Unable to call Patient Discovery Deferred Response");
        }

        LOG.debug("End EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl.processPatientDiscoveryAsyncResp(...)");
        return response;
    }
}
