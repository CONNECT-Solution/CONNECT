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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11;

import javax.xml.ws.BindingProvider;

import ihe.iti.xdr._2007.XDRDeferredRequestPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11.service.NhinDocSubmissionDeferredRequestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl implements
        NhinDocSubmissionDeferredRequestProxy {
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     * @return the logging object
     */
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<XDRDeferredRequestPortType> getCONNECTClientSecured(
            ServicePortDescriptor<XDRDeferredRequestPortType> portDescriptor, String url, AssertionType assertion) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url,
                assertion);
    }

    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest11(
            ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin provideAndRegisterDocumentSetBAsyncRequest");
        XDRAcknowledgementType response = null;

        try {
            String url = oProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(targetSystem,
                    NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME, GATEWAY_API_LEVEL.LEVEL_g0);

            if (request == null) {
                log.error("Message was null");
            } else {
                ServicePortDescriptor<XDRDeferredRequestPortType> portDescriptor = new NhinDocSubmissionDeferredRequestServicePortDescriptor();
                CONNECTClient<XDRDeferredRequestPortType> client = getCONNECTClientSecured(portDescriptor, url,
                        assertion);
                
                WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
                wsHelper.addTargetCommunity((BindingProvider) client.getPort(), targetSystem);
                wsHelper.addServiceName((BindingProvider) client.getPort(), 
                        NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);

                response = (XDRAcknowledgementType) client.invokePort(XDRDeferredRequestPortType.class,
                        "provideAndRegisterDocumentSetBDeferredRequest", request);
            }
        } catch (Exception ex) {
            log.error("Error calling provideAndRegisterDocumentSetBDeferredRequest: " + ex.getMessage(), ex);
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            RegistryError re = new RegistryError();
            re.setCodeContext(ex.getMessage());
            re.setErrorCode("XDSRegistryError");
            re.setLocation("NhinDocSubmissionDeferredRequestWebServiceProxySecuredImpl");
            re.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
            regResp.getRegistryErrorList().getRegistryError().add(re);
            response.setMessage(regResp);
        }

        log.debug("End provideAndRegisterDocumentSetBAsyncRequest");
        return response;
    }

}
