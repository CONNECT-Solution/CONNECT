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
package gov.hhs.fha.nhinc.patientlocationquery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayPatientLocationQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayPatientLocationQueryResponseType;
import gov.hhs.fha.nhinc.entitypatientlocationquerysecured.EntityPatientLocationQuerySecuredPortType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientlocationquery.entity.proxy.descriptor.EntityPatientLocationQuerySecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityPatientLocationQueryProxyWebServiceSecuredImpl implements EntityPatientLocationQueryProxy {

    private static final Logger LOG = LoggerFactory.getLogger(EntityPatientLocationQueryProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    protected ServicePortDescriptor<EntityPatientLocationQuerySecuredPortType> getServicePortDescriptor() {
        return new EntityPatientLocationQuerySecuredServicePortDescriptor();
    }

    @Override
    public RespondingGatewayPatientLocationQueryResponseType processPatientLocationQuery(
        PatientLocationQueryRequestType request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        LOG.debug("Begin EntityPatientLocationQueryProxyWebServiceSecuredImpl.processPatientLocationQuery");
        RespondingGatewayPatientLocationQueryResponseType response = new RespondingGatewayPatientLocationQueryResponseType();

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PLQ_ENTITYSECURED_SERVICE_NAME);

            ServicePortDescriptor<EntityPatientLocationQuerySecuredPortType> portDescriptor = getServicePortDescriptor();

            CONNECTClient<EntityPatientLocationQuerySecuredPortType> client = getCONNECTClient(portDescriptor, url, assertion);

            RespondingGatewayPatientLocationQueryRequestType securedRequest = new RespondingGatewayPatientLocationQueryRequestType();
            securedRequest.setNhinTargetCommunities(targets);
            securedRequest.setPatientLocationQueryRequest(request);

            response = (RespondingGatewayPatientLocationQueryResponseType) client
                .invokePort(EntityPatientLocationQuerySecuredPortType.class, "respondingGatewayPatientLocationQuery", securedRequest);

        } catch (Exception e) {
            throw new ErrorEventException(e,"Error calling respondingGatewayPatientLocationQuery");
        }

        LOG.debug("End EntityPatientLocationQueryProxyWebServiceSecuredImpl.processPatientLocationQuery");
        return response;
    }


    protected CONNECTClient<EntityPatientLocationQuerySecuredPortType> getCONNECTClient(
        ServicePortDescriptor<EntityPatientLocationQuerySecuredPortType> portDescriptor, String url,
        AssertionType assertion) {
        return CONNECTCXFClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

}
