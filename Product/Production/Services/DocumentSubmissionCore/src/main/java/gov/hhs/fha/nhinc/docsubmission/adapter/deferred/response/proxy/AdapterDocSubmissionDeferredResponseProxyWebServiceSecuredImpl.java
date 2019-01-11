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
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.service.AdapterDocSubmissionDeferredResponseSecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author Visu Patlolla
 */
public class AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements
        AdapterDocSubmissionDeferredResponseProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<AdapterXDRResponseSecuredPortType> getCONNECTClientSecured(
            ServicePortDescriptor<AdapterXDRResponseSecuredPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

    @AdapterDelegationEvent(beforeBuilder = DeferredResponseDescriptionBuilder.class,
            afterReturningBuilder = DocSubmissionArgTransformerBuilder.class,
            serviceType = "Document Submission Deferred Response", version = "LEVEL_a0")
    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType regResponse,
            AssertionType assertion) {
        LOG.debug("Begin AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetBResponse");
        XDRAcknowledgementType response = null;
        String serviceName = NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME;

        try {
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);

            if (NullChecker.isNotNullish(url)) {
                ServicePortDescriptor<AdapterXDRResponseSecuredPortType> portDescriptor = new AdapterDocSubmissionDeferredResponseSecuredServicePortDescriptor();

                CONNECTClient<AdapterXDRResponseSecuredPortType> client = getCONNECTClientSecured(portDescriptor, url,
                        assertion);
                client.enableMtom();
                response = (XDRAcknowledgementType) client.invokePort(AdapterXDRResponseSecuredPortType.class,
                        "provideAndRegisterDocumentSetBResponse", regResponse);
            } else {
                throw new WebServiceException("Could not determine URL for Doc Submission Deferred Response Adapter endpoint");
            }
        } catch (Exception ex) {
            throw new ErrorEventException(ex, "Unable to call Doc Submission Deferred Response Adapter");
        }

        LOG.debug("End AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetBResponse");
        return response;
    }
}
