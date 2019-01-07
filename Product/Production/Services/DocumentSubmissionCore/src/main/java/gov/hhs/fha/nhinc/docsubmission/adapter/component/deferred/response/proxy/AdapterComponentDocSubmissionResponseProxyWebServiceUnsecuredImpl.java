/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.adaptercomponentxdrresponse.AdapterComponentXDRResponsePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy.service.AdapterComponentDocSubmissionResponseServicePortDescriptor;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
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
 * @author dunnek, Les Westberg
 */
public class AdapterComponentDocSubmissionResponseProxyWebServiceUnsecuredImpl implements
    AdapterComponentDocSubmissionResponseProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocSubmissionResponseProxyWebServiceUnsecuredImpl.class);
    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    protected CONNECTClient<AdapterComponentXDRResponsePortType> getCONNECTClientUnsecured(
        ServicePortDescriptor<AdapterComponentXDRResponsePortType> portDescriptor, String url,
        AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * Receive document deferred document submission response.
     *
     * @param body The doc submission response message.
     * @param assertion The assertion information.
     * @return The ACK
     */
    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body,
        AssertionType assertion) {
        String endpointUrl;
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        String sServiceName = NhincConstants.ADAPTER_COMPONENT_XDR_RESPONSE_SERVICE_NAME;

        try {
            if (body != null) {
                LOG.debug("Before target system URL look up.");
                endpointUrl = oProxyHelper.getAdapterEndPointFromConnectionManager(sServiceName);
                LOG.debug("After target system URL look up. URL for service: {} is: {}",sServiceName, endpointUrl);

                if (NullChecker.isNotNullish(endpointUrl)) {
                    AdapterRegistryResponseType adaptResponse = new AdapterRegistryResponseType();
                    adaptResponse.setAssertion(assertion);
                    adaptResponse.setRegistryResponse(body);

                    ServicePortDescriptor<AdapterComponentXDRResponsePortType> portDescriptor =
                        new AdapterComponentDocSubmissionResponseServicePortDescriptor();

                    CONNECTClient<AdapterComponentXDRResponsePortType> client = getCONNECTClientUnsecured(
                        portDescriptor, endpointUrl, assertion);
                    client.enableMtom();
                    response = (XDRAcknowledgementType) client.invokePort(AdapterComponentXDRResponsePortType.class,
                        "provideAndRegisterDocumentSetBResponse", adaptResponse);
                } else {
                    throw new WebServiceException("Could not determine URL for Doc Submission Deferred Response Adapter Component endpoint");
                }
            } else {
                throw new IllegalArgumentException("Request Message must be provided");
            }
        } catch (Exception e) {
            throw new ErrorEventException(e, "Unable to call Doc Submission Deferred Response Adapter Component");
        }
        return response;
    }
}
