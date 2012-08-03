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
package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.docretrieve.passthru.proxy.description.PassthruDocRetrieveSecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Neil Webb
 */
public class PassthruDocRetrieveProxyWebServiceSecuredImpl implements PassthruDocRetrieveProxy {
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocRetrieveProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public ServicePortDescriptor<NhincProxyDocRetrieveSecuredPortType> getServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new PassthruDocRetrieveSecuredServicePortDescriptor();
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin PassthruDocRetrieveProxyWebServiceSecuredImpl.respondingGatewayCrossGatewayRetrieve");
        RetrieveDocumentSetResponseType response = null;
        String serviceName = NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SECURED_SERVICE_NAME;

        try {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if (log.isDebugEnabled()) {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url)) {
                RespondingGatewayCrossGatewayRetrieveSecuredRequestType securedRequest = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                if (request != null) {
                    securedRequest.setRetrieveDocumentSetRequest(request);
                    securedRequest.setNhinTargetSystem(targetSystem);
                }

                CONNECTClient<NhincProxyDocRetrieveSecuredPortType> client = getClient(url, assertion);

                response = (RetrieveDocumentSetResponseType) client.invokePort(
                        NhincProxyDocRetrieveSecuredPortType.class, "respondingGatewayCrossGatewayRetrieve", request);

            } else {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error: Failed to retrieve url for service: " + serviceName + " for local home community");
            log.error(ex.getMessage(), ex);
        }

        log.debug("End PassthruDocRetrieveProxyWebServiceSecuredImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

    protected CONNECTClient<NhincProxyDocRetrieveSecuredPortType> getClient(String url, AssertionType assertion) {
        ServicePortDescriptor<NhincProxyDocRetrieveSecuredPortType> portDescriptor = getServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);
        CONNECTClient<NhincProxyDocRetrieveSecuredPortType> client = CONNECTClientFactory.getInstance()
                .getCONNECTClientSecured(portDescriptor, url, assertion);
        return client;
    }

}
