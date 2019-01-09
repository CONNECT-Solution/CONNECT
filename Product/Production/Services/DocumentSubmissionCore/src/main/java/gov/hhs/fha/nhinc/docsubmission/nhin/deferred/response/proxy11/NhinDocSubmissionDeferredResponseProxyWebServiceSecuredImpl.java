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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.service.NhinDocSubmissionDeferredResponseServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xdr._2007.XDRDeferredResponsePortType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements
    NhinDocSubmissionDeferredResponseProxy {

    private static final Logger LOG = LoggerFactory.getLogger(
        NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.class);

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

    protected CONNECTClient<XDRDeferredResponsePortType> getCONNECTClientSecured(
        ServicePortDescriptor<XDRDeferredResponsePortType> portDescriptor, String url, AssertionType assertion,
        NhinTargetSystemType target, String serviceName) {

        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url, target,
            serviceName);
    }

    @NwhinInvocationEvent(beforeBuilder = DeferredResponseDescriptionBuilder.class,
        afterReturningBuilder = DeferredResponseDescriptionBuilder.class,
        serviceType = "Document Submission Deferred Response", version = "1.1")
    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredResponse11(RegistryResponseType request,
        AssertionType assertion, NhinTargetSystemType target) {
        LOG.debug("Begin provideAndRegisterDocumentSetBDeferredResponse");
        XDRAcknowledgementType response = null;
        try {
            String url = getUrl(target);

            if (request == null) {
                throw new IllegalArgumentException("Request Message must be provided");
            } else {
                ServicePortDescriptor<XDRDeferredResponsePortType> portDescriptor
                    = new NhinDocSubmissionDeferredResponseServicePortDescriptor();
                CONNECTClient<XDRDeferredResponsePortType> client = getCONNECTClientSecured(portDescriptor, url,
                    assertion, target, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
                client.enableMtom();

                response = (XDRAcknowledgementType) client.invokePort(XDRDeferredResponsePortType.class,
                    "provideAndRegisterDocumentSetBDeferredResponse", request);
            }
        } catch (Exception ex) {
            response = getMessageGeneratorUtils().createRegistryErrorXDRAcknowledgementType(ex.getMessage());
            throw new ErrorEventException(ex, response, "Unable to call Doc Submission Deferred Response");
        }

        LOG.debug("End provideAndRegisterDocumentSetBDeferredResponse");
        return response;
    }

    /**
     * @param target
     * @return
     * @throws Exception
     * @throws IllegalArgumentException
     */
    protected String getUrl(NhinTargetSystemType target) throws IllegalArgumentException, Exception {
        return oProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target,
            NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME, GATEWAY_API_LEVEL.LEVEL_g0);
    }

}
