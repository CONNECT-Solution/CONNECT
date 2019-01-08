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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy;

import gov.hhs.fha.nhinc.adaptercomponentxdrrequest.AdapterComponentXDRRequestPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy.service.AdapterComponentDocSubmissionRequestServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek, Les Westberg
 */
public class AdapterComponentDocSubmissionRequestProxyWebServiceUnsecuredImpl implements
    AdapterComponentDocSubmissionRequestProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocSubmissionRequestProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    protected CONNECTClient<AdapterComponentXDRRequestPortType> getCONNECTClientUnsecured(
        ServicePortDescriptor<AdapterComponentXDRRequestPortType> portDescriptor, String url,
        AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * Receive document deferred document submission request.
     *
     * @param body The doc submission message.
     * @param assertion The assertion information.
     * @param url The URL
     * @return The ACK
     */
    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion) {
        String endpointUrl;
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        String sServiceName = NhincConstants.ADAPTER_COMPONENT_XDR_REQUEST_SERVICE_NAME;

        try {
            if (body != null) {
                LOG.debug("Before target system URL look up.");
                endpointUrl = oProxyHelper.getAdapterEndPointFromConnectionManager(sServiceName);
                LOG.debug("After target system URL look up. URL for service: {} is: {}",sServiceName, endpointUrl);

                if (NullChecker.isNotNullish(endpointUrl)) {
                    AdapterProvideAndRegisterDocumentSetRequestType request = new AdapterProvideAndRegisterDocumentSetRequestType();
                    request.setAssertion(assertion);
                    request.setProvideAndRegisterDocumentSetRequest(body);
                    request.setUrl(endpointUrl);

                    ServicePortDescriptor<AdapterComponentXDRRequestPortType> portDescriptor = new AdapterComponentDocSubmissionRequestServicePortDescriptor();

                    CONNECTClient<AdapterComponentXDRRequestPortType> client = getCONNECTClientUnsecured(
                        portDescriptor, endpointUrl, assertion);
                    client.enableMtom();
                    response = (XDRAcknowledgementType) client.invokePort(AdapterComponentXDRRequestPortType.class,
                        "provideAndRegisterDocumentSetBRequest", request);
                } else {
                    throw new WebServiceException("Could not determine URL for Patient Discovery Deferred Response endpoint");
                }
            } else {
                throw new IllegalArgumentException("Request Message must be provided");
            }
        } catch (Exception e) {
            throw new ErrorEventException(e, "Unable to call Doc Submission Deferred Request Adapter Component");
        }

        return response;
    }
}
