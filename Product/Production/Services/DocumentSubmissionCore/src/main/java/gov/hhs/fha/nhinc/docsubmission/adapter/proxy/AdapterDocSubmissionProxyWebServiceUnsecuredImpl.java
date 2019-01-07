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
package gov.hhs.fha.nhinc.docsubmission.adapter.proxy;

import gov.hhs.fha.nhinc.adapterxdr.AdapterXDRPortType;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.service.AdapterDocSubmissionUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocSubmissionProxyWebServiceUnsecuredImpl implements AdapterDocSubmissionProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterDocSubmissionProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocSubmissionProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return
     */
    @AdapterDelegationEvent(beforeBuilder = DocSubmissionBaseEventDescriptionBuilder.class,
            afterReturningBuilder = DocSubmissionBaseEventDescriptionBuilder.class, serviceType = "Document Submission",
            version = "LEVEL_a0")
    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg,
            AssertionType assertion) {
        LOG.debug("Begin provideAndRegisterDocumentSetB");
        RegistryResponseType response = null;

        try {
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(NhincConstants.ADAPTER_XDR_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {

                AdapterProvideAndRegisterDocumentSetRequestType request = new AdapterProvideAndRegisterDocumentSetRequestType();
                request.setProvideAndRegisterDocumentSetRequest(msg);
                request.setAssertion(assertion);

                ServicePortDescriptor<AdapterXDRPortType> portDescriptor = new AdapterDocSubmissionUnsecuredServicePortDescriptor();

                CONNECTClient<AdapterXDRPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);
                client.enableMtom();
                response = (RegistryResponseType) client.invokePort(AdapterXDRPortType.class,
                        "provideAndRegisterDocumentSetb", request);

            } else {
                throw new WebServiceException("Could not determine URL for Doc Submission Adapter endpoint");
            }
        } catch (Exception ex) {
            response = new RegistryResponseType();
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            throw new ErrorEventException(ex, response, "Unable to call Doc Submission Adapter");

        }

        LOG.debug("End provideAndRegisterDocumentSetB");
        return response;
    }

}
