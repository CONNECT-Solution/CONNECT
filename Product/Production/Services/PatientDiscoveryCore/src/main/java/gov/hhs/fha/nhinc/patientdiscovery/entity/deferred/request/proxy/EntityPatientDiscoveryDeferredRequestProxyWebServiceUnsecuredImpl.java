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
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscoveryasyncreq.EntityPatientDiscoveryAsyncReqPortType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy.service.EntityPatientDiscoveryAsyncReqServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityPatientDiscoveryDeferredRequestProxyWebServiceUnsecuredImpl implements
    EntityPatientDiscoveryDeferredRequestProxy {

    private static final Logger LOG = LoggerFactory.getLogger(EntityPatientDiscoveryDeferredRequestProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityPatientDiscoveryDeferredRequestProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @return
     */
    @Override
    //Unused???
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion,
        NhinTargetCommunitiesType target) {
        LOG.debug("Begin processPatientDiscoveryAsyncReq");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        try {
            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setAssertion(assertion);
            request.setNhinTargetCommunities(target);
            request.setPRPAIN201305UV02(message);
            String url = oProxyHelper
                .getUrlLocalHomeCommunity(NhincConstants.PATIENT_DISCOVERY_ENTITY_ASYNC_REQ_SERVICE_NAME);
            ServicePortDescriptor<EntityPatientDiscoveryAsyncReqPortType> portDescriptor
                = new EntityPatientDiscoveryAsyncReqServicePortDescriptor();
            CONNECTClient<EntityPatientDiscoveryAsyncReqPortType> client = CONNECTClientFactory.getInstance()
                .getCONNECTClientUnsecured(portDescriptor, url, assertion);
            response = (MCCIIN000002UV01) client.invokePort(EntityPatientDiscoveryAsyncReqPortType.class,
                "processPatientDiscoveryAsyncReq", request);
        } catch (Exception ex) {
            throw new ErrorEventException(ex, "Error calling Patient Discovery Service");
        }

        LOG.debug("End processPatientDiscoveryAsyncReq");
        return response;
    }

}
