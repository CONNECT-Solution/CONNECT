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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy20;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docsubmission.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy20.service.NhinDocSubmissionDeferredResponseServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xdr._2007.XDRDeferredResponse20PortType;

import javax.xml.ws.Holder;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;

/**
 * 
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements
        NhinDocSubmissionDeferredResponseProxy {
    private static final Logger LOG = Logger
            .getLogger(NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected MessageGeneratorUtils getMessageGeneratorUtils() {
        return MessageGeneratorUtils.getInstance();
    }

    protected CONNECTClient<XDRDeferredResponse20PortType> getCONNECTClientSecured(
            ServicePortDescriptor<XDRDeferredResponse20PortType> portDescriptor, String url, AssertionType assertion,
            String target, String serviceName) {

        CONNECTClient<XDRDeferredResponse20PortType> client = CONNECTCXFClientFactory.getInstance()
                .getCONNECTClientSecured(portDescriptor, assertion, url, target, serviceName);
        return client;
    }

    @NwhinInvocationEvent(beforeBuilder = DeferredResponseDescriptionBuilder.class, afterReturningBuilder = DocSubmissionBaseEventDescriptionBuilder.class, serviceType = "Document Submission Deferred Response", version = "")
    public RegistryResponseType provideAndRegisterDocumentSetBDeferredResponse20(RegistryResponseType request,
            AssertionType assertion, NhinTargetSystemType target) {
        LOG.debug("Begin provideAndRegisterDocumentSetBDeferredResponse");
        RegistryResponseType response = null;

        try {
            String url = getUrl(target);

            if (request == null) {
                LOG.error("Message was null");
            } else {
                Holder<RegistryResponseType> respHolder = new Holder<RegistryResponseType>();
                respHolder.value = request;

                ServicePortDescriptor<XDRDeferredResponse20PortType> portDescriptor = new NhinDocSubmissionDeferredResponseServicePortDescriptor();

                CONNECTClient<XDRDeferredResponse20PortType> client = getCONNECTClientSecured(portDescriptor, url,
                        assertion, target.getHomeCommunity().getHomeCommunityId(),
                        NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
                client.enableMtom();
                
                client.invokePort(XDRDeferredResponse20PortType.class,
                        "provideAndRegisterDocumentSetBDeferredResponse", respHolder);
                response = respHolder.value;
            }
        } catch (Exception ex) {
            LOG.error("Error calling provideAndRegisterDocumentSetBDeferredResponse: " + ex.getMessage(), ex);

            response = getMessageGeneratorUtils().createRegistryErrorResponse(ex.getMessage(), "XDSRegistryError",
                    NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
        }

        LOG.debug("End provideAndRegisterDocumentSetBDeferredResponse");
        return response;
    }

    /**
     * @param target
     * @return
     * @throws IllegalArgumentException
     * @throws ConnectionManagerException
     * @throws Exception
     */
    protected String getUrl(NhinTargetSystemType target) throws IllegalArgumentException, ConnectionManagerException,
            Exception {
        return oProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target,
                NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME, GATEWAY_API_LEVEL.LEVEL_g1);
    }
}
