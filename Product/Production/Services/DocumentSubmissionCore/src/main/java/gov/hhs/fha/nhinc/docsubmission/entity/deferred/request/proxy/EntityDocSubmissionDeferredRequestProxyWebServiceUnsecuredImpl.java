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
package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.proxy;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.proxy.service.EntityDocSubmissionDeferredRequestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincentityxdr.async.request.EntityXDRAsyncRequestPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredRequestProxyWebServiceUnsecuredImpl implements
        EntityDocSubmissionDeferredRequestProxy {
    private static final Logger LOG = Logger.getLogger(EntityDocSubmissionDeferredRequestProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityDocSubmissionDeferredRequestProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }
    
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<EntityXDRAsyncRequestPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<EntityXDRAsyncRequestPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
                assertion);
    }

    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(
            ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
            NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        LOG.debug("Begin provideAndRegisterDocumentSetBAsyncRequest");
        XDRAcknowledgementType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_XDR_REQUEST_SERVICE_NAME);

            if (request == null) {
                LOG.error("Message was null");
            } else if (assertion == null) {
                LOG.error("assertion was null");
            } else if (targets == null) {
                LOG.error("targets was null");
            } else {
                RespondingGatewayProvideAndRegisterDocumentSetRequestType msg =
                        new RespondingGatewayProvideAndRegisterDocumentSetRequestType();
                msg.setProvideAndRegisterDocumentSetRequest(request);
                msg.setAssertion(assertion);
                msg.setNhinTargetCommunities(targets);

                if (urlInfo != null) {
                    msg.setUrl(urlInfo);
                }

                ServicePortDescriptor<EntityXDRAsyncRequestPortType> portDescriptor =
                        new EntityDocSubmissionDeferredRequestServicePortDescriptor();
                CONNECTClient<EntityXDRAsyncRequestPortType> client = getCONNECTClientUnsecured(portDescriptor, url,
                        assertion);

                response = (XDRAcknowledgementType) client.invokePort(EntityXDRAsyncRequestPortType.class,
                        "provideAndRegisterDocumentSetBAsyncRequest", msg);
            }
        } catch (Exception ex) {
            LOG.error("Error calling provideAndRegisterDocumentSetBAsyncRequest: " + ex.getMessage(), ex);
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        LOG.debug("End provideAndRegisterDocumentSetBAsyncRequest");
        return response;
    }

}
