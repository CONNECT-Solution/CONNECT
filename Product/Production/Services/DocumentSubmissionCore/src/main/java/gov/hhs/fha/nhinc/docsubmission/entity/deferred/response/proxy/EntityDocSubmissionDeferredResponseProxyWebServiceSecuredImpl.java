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
package gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.proxy.service.EntityDocSubmissionDeferredResponseSecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredAsyncResponsePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements
        EntityDocSubmissionDeferredResponseProxy {
    private static final Logger LOG = LoggerFactory.getLogger(EntityDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RegistryResponseType request,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        LOG.debug("Begin provideAndRegisterDocumentSetBAsyncResponse");
        XDRAcknowledgementType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME);

            if (request == null) {
                throw new IllegalArgumentException("Request Message must be provided");
            } else if (targets == null) {
                throw new IllegalArgumentException("Target Communities must be provided");
            } else {
                RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType msg = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
                msg.setRegistryResponse(request);
                msg.setNhinTargetCommunities(targets);

                ServicePortDescriptor<EntityXDRSecuredAsyncResponsePortType> portDescriptor = new EntityDocSubmissionDeferredResponseSecuredServicePortDescriptor();

                CONNECTClient<EntityXDRSecuredAsyncResponsePortType> client = CONNECTCXFClientFactory.getInstance()
                        .getCONNECTClientSecured(portDescriptor, url, assertion);

                response = (XDRAcknowledgementType) client.invokePort(EntityXDRSecuredAsyncResponsePortType.class,
                        "provideAndRegisterDocumentSetBAsyncResponse", msg);
            }
        } catch (Exception ex) {
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
            throw new ErrorEventException(ex, response, "Unable to call Doc Submission Entity Deferred Response");
        }

        LOG.debug("End provideAndRegisterDocumentSetBAsyncResponse");
        return response;
    }
}
