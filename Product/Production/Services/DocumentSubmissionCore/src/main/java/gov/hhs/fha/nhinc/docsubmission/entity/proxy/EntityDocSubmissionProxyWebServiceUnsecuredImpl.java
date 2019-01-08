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
package gov.hhs.fha.nhinc.docsubmission.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.docsubmission.entity.proxy.description.EntityDocSubmissiona0ServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincentityxdr.EntityXDRPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntityDocSubmissionProxyWebServiceUnsecuredImpl implements EntityDocSubmissionProxy {
    private static final Logger LOG = LoggerFactory.getLogger(EntityDocSubmissionProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityDocSubmissionProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public ServicePortDescriptor<EntityXDRPortType> getServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new EntityDocSubmissiona0ServicePortDescriptor();
    }

    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType message,
            AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        LOG.debug("Begin EntityDocSubmissionProxyWebServiceUnsecuredImpl.provideAndRegisterDocumentSetB");
        RegistryResponseType response = new RegistryResponseType();

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_XDR_SERVICE_NAME);

            ServicePortDescriptor<EntityXDRPortType> portDescriptor = getServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

            CONNECTClient<EntityXDRPortType> client = getCONNECTClient(portDescriptor, url, assertion);

            RespondingGatewayProvideAndRegisterDocumentSetRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();
            request.setNhinTargetCommunities(targets);
            request.setProvideAndRegisterDocumentSetRequest(message);
            request.setUrl(urlInfo);

            response = (RegistryResponseType) client.invokePort(EntityXDRPortType.class,
                    "provideAndRegisterDocumentSetB", request);

        } catch (Exception ex) {
            throw new ErrorEventException(ex, response, "Unable to call Doc Submission Entity");
        }

        LOG.debug("End EntityDocSubmissionProxyWebServiceUnsecuredImpl.provideAndRegisterDocumentSetB");
        return response;
    }

    /**
     * @param portDescriptor
     * @param url
     * @param assertion
     * @return
     */
    protected CONNECTClient<EntityXDRPortType> getCONNECTClient(ServicePortDescriptor<EntityXDRPortType> portDescriptor,
            String url, AssertionType assertion) {
        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }
}
