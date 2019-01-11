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
package gov.hhs.fha.nhinc.docdatasubmission.nhin.proxy;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docdatasubmission.aspect.DocDataSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docdatasubmission.nhin.proxy.service.NhinDocDataSubmissionServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti._2007.DocumentRegistryXDSPortType;
import ihe.iti.xds_b._2007.RegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NhinDocDataSubmissionProxyWebServiceSecuredImpl implements NhinDocDataSubmissionProxy {

    private static final Logger LOG = LoggerFactory.getLogger(NhinDocDataSubmissionProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper proxyHelper = null;

    protected MessageGeneratorUtils getMessageGeneratorUtils() {
        return MessageGeneratorUtils.getInstance();
    }

    protected WebServiceProxyHelper getWebServiceProxHelper() {
        if (proxyHelper == null) {
            proxyHelper = new WebServiceProxyHelper();
        }
        return proxyHelper;
    }

    protected CONNECTClient<DocumentRegistryXDSPortType> getCONNECTClientSecured(
        ServicePortDescriptor<DocumentRegistryXDSPortType> portDescriptor, AssertionType assertion, String url,
        NhinTargetSystemType target, String serviceName) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            target, serviceName);
    }

    @NwhinInvocationEvent(beforeBuilder = DocDataSubmissionBaseEventDescriptionBuilder.class,
        afterReturningBuilder = DocDataSubmissionBaseEventDescriptionBuilder.class,
        serviceType = "Document Data Submission", version = "")
    @Override
    public RegistryResponseType registerDocumentSetB(RegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetSystemType targetSystem, NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        LOG.debug("Begin registerDocumentSetb");
        RegistryResponseType response = new RegistryResponseType();

        try {
            String url = getWebServiceProxHelper().getUrlFromTargetSystemByGatewayAPILevel(targetSystem,
                NhincConstants.NHINC_XDS_SERVICE_NAME, apiLevel);

            ServicePortDescriptor<DocumentRegistryXDSPortType> portDescriptor = new NhinDocDataSubmissionServicePortDescriptor();

            CONNECTClient<DocumentRegistryXDSPortType> client = getCONNECTClientSecured(portDescriptor, assertion, url,
                targetSystem, NhincConstants.NHINC_XDS_SERVICE_NAME);

            response = (RegistryResponseType) client.invokePort(DocumentRegistryXDSPortType.class,
                "documentRegistryXDSRegisterDocumentSetB", request);

        } catch (Exception ex) {
            response = getMessageGeneratorUtils().createRegistryErrorResponseWithAckFailure(ex.getMessage());
            throw new ErrorEventException(ex, response, "Unable to call Nhin Doc Data Submission");
        }

        LOG.debug("End registerDocumentSetb");
        return response;
    }

}
